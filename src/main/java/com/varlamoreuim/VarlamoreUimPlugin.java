package com.varlamoreuim;

import javax.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.varlamoreuim.dialogue.DialogueContext;
import com.varlamoreuim.dialogue.DialogueManager;
import com.varlamoreuim.dialogue.DialogueScript;
import com.varlamoreuim.dialogue.Expression;
import com.varlamoreuim.dialogue.DialogueEffect;
import com.varlamoreuim.dialogue.Option;
import com.varlamoreuim.dialogue.Speaker;
import com.varlamoreuim.npc.NpcTransportBlocker;
import com.varlamoreuim.teleport.ItemTeleportBlocker;
import com.varlamoreuim.teleport.SpellTeleportBlocker;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;
import net.runelite.api.Player;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.PostMenuSort;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.callback.RenderCallbackManager;
import net.runelite.client.chat.ChatCommandManager;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.chatbox.ChatboxPanelManager;
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

	@Inject
	private ClientThread clientThread;

	@Inject
	private ChatCommandManager chatCommandManager;

	@Inject
	private ChatboxPanelManager chatboxPanelManager;

	private VarlamoreUimPanel panel;
	private NavigationButton navButton;
	private BoundaryChecker boundaryChecker;
	private SpellTeleportBlocker spellTeleportBlocker;
	private ItemTeleportBlocker itemTeleportBlocker;
	private NpcTransportBlocker npcTransportBlocker;
	private DialogueManager dialogueManager;
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
		npcTransportBlocker.initClient(client, clientThread, chatMessageManager);
		renderCallbackManager.register(npcTransportBlocker.getRenderCallback());

		// If the plugin was enabled while already logged in, create stand-ins immediately
		if (client.getGameState() == GameState.LOGGED_IN)
		{
			npcTransportBlocker.createStandInNpcs();
		}

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

		// TEMP: removed in Task 9
		dialogueManager = new DialogueManager(client, clientThread, chatboxPanelManager);
		chatCommandManager.registerCommand("::vuimtalk", (chatMessage, message) ->
		{
			DialogueScript s = DialogueScript.builder("p")
				.player("p", "Any ships sailing today?", "n")
				.npc("n", "Ships? No, no. Not today. It's the tides, you see. Terrible tides. Absolutely dreadful tides, the worst tides anyone has seen in years.", "m")
				.options("m", "Select an option",
					Option.of("Why not?", "why"),
					Option.of("Never mind.", DialogueScript.END))
				.npc("why", "Tides.", Expression.ANGRY, DialogueEffect.NONE, DialogueScript.END)
				.build();
			dialogueManager.open(s, new Speaker("Mysterious Old Man", 2830), new DialogueContext()
			{
				@Override
				public boolean hasDizanasQuiver()
				{
					return false;
				}

				@Override
				public String playerName()
				{
					return client.getLocalPlayer() != null ? client.getLocalPlayer().getName() : "You";
				}
			}, effect -> log.debug("effect {}", effect));
		});

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
		if (npcTransportBlocker != null)
		{
			npcTransportBlocker.destroyStandInNpcs();
			renderCallbackManager.unregister(npcTransportBlocker.getRenderCallback());
			npcTransportBlocker = null;
		}

		// TEMP: removed in Task 9
		chatCommandManager.unregisterCommand("::vuimtalk");
		dialogueManager = null;

		log.debug("Varlamore UIM plugin stopped");
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (dialogueManager != null)
		{
			dialogueManager.onGameTick();
		}

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

			// Ensure stand-ins exist — some transports (e.g., Antonia's boat) don't
			// trigger LOADING/LOGGED_IN, so stand-ins may be missing after travel.
			// The flag prevents retrying every tick when no docks are in the scene.
			if (config.blockNpcTransport() && !npcTransportBlocker.isUnlocked()
				&& npcTransportBlocker.getStandInCount() == 0
				&& !npcTransportBlocker.isStandInCreationAttempted())
			{
				npcTransportBlocker.createStandInNpcs();
			}
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() == GameState.LOGIN_SCREEN || event.getGameState() == GameState.HOPPING)
		{
			if (npcTransportBlocker != null)
			{
				npcTransportBlocker.destroyStandInNpcs();
			}
			panel.resetStatus();
		}

		// Scene reload (e.g., crossing chunk boundaries) — destroy stale RuneLiteObjects
		// so LOGGED_IN (which always follows LOADING) recreates them for the new scene
		if (event.getGameState() == GameState.LOADING)
		{
			if (npcTransportBlocker != null)
			{
				npcTransportBlocker.destroyStandInNpcs();
			}
		}

		if (event.getGameState() == GameState.LOGGED_IN && npcTransportBlocker != null && config.blockNpcTransport())
		{
			npcTransportBlocker.createStandInNpcs();
		}
	}

	/**
	 * TEMP: removed in Task 9. Logs the real dialogue widget geometry so {@link com.varlamoreuim.dialogue.DialogueLayout}
	 * can be calibrated against the game's own NPC (ChatLeft, group 231) and player (ChatRight, group 217) interfaces.
	 */
	@Subscribe
	public void onWidgetLoaded(WidgetLoaded event)
	{
		if (event.getGroupId() == 231)
		{
			clientThread.invokeLater(() -> logCalibrationWidgets("ChatLeft",
				InterfaceID.ChatLeft.HEAD, InterfaceID.ChatLeft.NAME, InterfaceID.ChatLeft.TEXT, InterfaceID.ChatLeft.CONTINUE));
		}
		else if (event.getGroupId() == 217)
		{
			clientThread.invokeLater(() -> logCalibrationWidgets("ChatRight",
				InterfaceID.ChatRight.HEAD, InterfaceID.ChatRight.NAME, InterfaceID.ChatRight.TEXT, InterfaceID.ChatRight.CONTINUE));
		}
	}

	// TEMP: removed in Task 9
	private void logCalibrationWidgets(String label, int headId, int nameId, int textId, int continueId)
	{
		logCalibrationWidget(label, "HEAD", client.getWidget(headId));
		logCalibrationWidget(label, "NAME", client.getWidget(nameId));
		logCalibrationWidget(label, "TEXT", client.getWidget(textId));
		logCalibrationWidget(label, "CONTINUE", client.getWidget(continueId));
	}

	// TEMP: removed in Task 9
	private void logCalibrationWidget(String label, String field, Widget widget)
	{
		if (widget == null)
		{
			log.info("[vuim-calib] {} {} = null", label, field);
			return;
		}
		log.info("[vuim-calib] {} {}: x={} y={} w={} h={} zoom={} rotX={} rotY={} rotZ={} anim={} font={} color={}",
			label, field, widget.getOriginalX(), widget.getOriginalY(), widget.getWidth(), widget.getHeight(),
			widget.getModelZoom(), widget.getRotationX(), widget.getRotationY(), widget.getRotationZ(),
			widget.getAnimationId(), widget.getFontId(), widget.getTextColor());
	}

	@Subscribe
	public void onPostMenuSort(PostMenuSort event)
	{
		if (!config.pluginEnabled() || !config.blockNpcTransport() || npcTransportBlocker == null)
		{
			return;
		}
		npcTransportBlocker.handlePostMenuSort(event);
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		if (npcTransportBlocker == null || !config.blockNpcTransport())
		{
			return;
		}

		int containerId = event.getContainerId();
		if (containerId == InventoryID.INVENTORY.getId() || containerId == InventoryID.EQUIPMENT.getId())
		{
			boolean nowUnlocked = checkDizanasQuiverOwned();
			if (nowUnlocked != npcTransportBlocker.isUnlocked())
			{
				npcTransportBlocker.setUnlocked(nowUnlocked);
				log.debug("Charter ship unlock state changed: {}", nowUnlocked);
			}
		}
	}

	/**
	 * Check whether the player currently possesses Dizana's Quiver in inventory or equipment.
	 * Delegates to {@link NpcTransportBlocker#containsDizanasQuiver(ItemContainer)} for each container.
	 *
	 * @return true if any Dizana's Quiver variant is found in inventory or equipment
	 */
	private boolean checkDizanasQuiverOwned()
	{
		ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);
		ItemContainer equipment = client.getItemContainer(InventoryID.EQUIPMENT);
		return NpcTransportBlocker.containsDizanasQuiver(inventory)
			|| NpcTransportBlocker.containsDizanasQuiver(equipment);
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
			if (npcTransportBlocker.handlePrimioClick(event))
			{
				return;
			}
			if (npcTransportBlocker.handleCharterClick(event))
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
