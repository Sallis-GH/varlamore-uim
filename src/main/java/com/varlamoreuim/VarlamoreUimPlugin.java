package com.varlamoreuim;

import com.google.inject.Injector;
import com.google.inject.Provides;
import com.varlamoreuim.teleport.SpellTeleportBlocker;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
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

	private VarlamoreUimPanel panel;
	private NavigationButton navButton;
	private BoundaryChecker boundaryChecker;
	private SpellTeleportBlocker spellTeleportBlocker;
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
		if (!config.pluginEnabled() || !config.blockSpellTeleports())
		{
			return;
		}

		spellTeleportBlocker.handleMenuClick(event, chatMessageManager, boundaryChecker);
	}

	@Provides
	VarlamoreUimConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(VarlamoreUimConfig.class);
	}
}
