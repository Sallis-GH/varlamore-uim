package com.varlamoreuim;

import javax.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.varlamoreuim.npc.NpcTransportBlocker;
import com.varlamoreuim.teleport.ItemTeleportBlocker;
import com.varlamoreuim.teleport.SpellTeleportBlocker;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.client.callback.RenderCallbackManager;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

@Slf4j
@PluginDescriptor(
	name = "Varlamore UIM",
	description = "Varlamore area-lock restrictions for Ultimate Ironman accounts",
	tags = {"varlamore", "uim", "ironman", "region", "restriction"}
)
public class VarlamoreUimPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private VarlamoreUimConfig config;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private Injector injector;

	@Inject
	private ChatMessageManager chatMessageManager;

	@Inject
	private RenderCallbackManager renderCallbackManager;

	private VarlamoreUimPanel panel;
	private NavigationButton navButton;
	private BoundaryChecker boundaryChecker;
	private SpellTeleportBlocker spellTeleportBlocker;
	private ItemTeleportBlocker itemTeleportBlocker;
	private NpcTransportBlocker npcTransportBlocker;
	private boolean wasInVarlamore = true;

	@Override
	protected void startUp() throws Exception
	{
		// Initialize BoundaryChecker
		boundaryChecker = new BoundaryChecker();
		boundaryChecker.loadRegions();
		log.debug("Loaded {} Varlamore regions", boundaryChecker.getRegionCount());

		// Initialize SpellTeleportBlocker
		spellTeleportBlocker = new SpellTeleportBlocker();

		// Initialize ItemTeleportBlocker
		itemTeleportBlocker = new ItemTeleportBlocker();

		// Initialize NpcTransportBlocker and register render callback
		npcTransportBlocker = new NpcTransportBlocker();
		renderCallbackManager.register(npcTransportBlocker.getRenderCallback());

		// Create panel
		panel = injector.getInstance(VarlamoreUimPanel.class);

		// Create navigation button with programmatic icon
		final BufferedImage icon = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = icon.createGraphics();
		g.setColor(new Color(0, 180, 120));
		g.fillRect(0, 0, 16, 16);
		g.dispose();

		navButton = NavigationButton.builder()
			.tooltip("Varlamore UIM")
			.icon(icon)
			.priority(5)
			.panel(panel)
			.build();

		// Register navigation button
		clientToolbar.addNavigation(navButton);

		log.debug("Varlamore UIM plugin started");
	}

	@Override
	protected void shutDown() throws Exception
	{
		// Clean up resources
		clientToolbar.removeNavigation(navButton);
		panel.resetStatus();
		panel = null;
		navButton = null;
		boundaryChecker = null;
		spellTeleportBlocker = null;
		itemTeleportBlocker = null;
		renderCallbackManager.unregister(npcTransportBlocker.getRenderCallback());
		npcTransportBlocker = null;

		log.debug("Varlamore UIM plugin stopped");
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (!config.pluginEnabled())
		{
			return;
		}

		Player player = client.getLocalPlayer();
		if (player == null)
		{
			return;
		}

		boolean inVarlamore = boundaryChecker.isInVarlamore(player.getWorldLocation());
		panel.updateBoundaryStatus(inVarlamore);

		// Only log on boundary state changes
		if (inVarlamore != wasInVarlamore)
		{
			log.debug("Player boundary: {}", inVarlamore ? "inside" : "outside");
			wasInVarlamore = inVarlamore;
		}

		// Sync NPC transport blocker config state so RenderCallback respects toggle changes
		if (npcTransportBlocker != null)
		{
			npcTransportBlocker.setEnabled(config.blockNpcTransport());
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() == GameState.LOGIN_SCREEN || event.getGameState() == GameState.HOPPING)
		{
			panel.resetStatus();
		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (!config.pluginEnabled())
		{
			return;
		}

		// Spell teleport blocking — check first (returns false fast for item ops)
		if (config.blockSpellTeleports())
		{
			if (spellTeleportBlocker.handleMenuClick(event, chatMessageManager, boundaryChecker))
			{
				return; // Already handled — don't double-process
			}
		}

		// Item teleport blocking — only reached if spell blocker did not handle the event
		if (config.blockItemTeleports())
		{
			int resolvedItemId = resolveItemId(event);
			if (itemTeleportBlocker.handleMenuClick(event, chatMessageManager, config.blockHouseTablet(), resolvedItemId))
			{
				return; // Already handled — don't double-process
			}
		}

		// Minigame grouping tab teleport blocking — separate config toggle from item blocking
		if (config.blockMinigameTeleports())
		{
			if (itemTeleportBlocker.handleMinigameTeleport(event, chatMessageManager))
			{
				return;
			}
		}

		// NPC transport blocking — charter ship hiding + Primio quetzal blocking
		if (config.blockNpcTransport())
		{
			if (npcTransportBlocker.handlePrimioClick(event, client, chatMessageManager))
			{
				return;
			}
			if (npcTransportBlocker.handleCharterClick(event, client, chatMessageManager))
			{
				return;
			}
		}
	}

	/**
	 * Resolve the item ID from a menu click event.
	 * For inventory items, getItemId() works directly.
	 * For equipped items (CC_OP on equipment panel), getItemId() returns -1,
	 * so we get the item from the equipment widget's child.
	 */
	private int resolveItemId(MenuOptionClicked event)
	{
		int itemId = event.getItemId();
		if (itemId != -1)
		{
			return itemId;
		}

		Widget widget = event.getWidget();
		if (widget != null && WidgetUtil.componentToInterface(widget.getId()) == InterfaceID.WORNITEMS)
		{
			Widget child = widget.getChild(1);
			if (child != null && child.getItemId() > -1)
			{
				return child.getItemId();
			}
		}

		return -1;
	}

	@Provides
	VarlamoreUimConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(VarlamoreUimConfig.class);
	}
}
