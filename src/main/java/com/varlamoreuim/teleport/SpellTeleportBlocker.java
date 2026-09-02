package com.varlamoreuim.teleport;

import com.varlamoreuim.BoundaryChecker;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.MenuAction;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;

import java.awt.Color;
import java.util.Map;
import java.util.Set;

/**
 * Service responsible for blocking teleport spells that leave Varlamore.
 * Handles all 31 spellbook teleports across Standard, Ancient, Lunar, and Arceuus spellbooks,
 * plus Home Teleport (SPELL-02) with spellbook-aware destination logic.
 */
@Slf4j
public class SpellTeleportBlocker
{
	/**
	 * Spellbook widget group IDs. Only CC_OP events from these widgets are spell casts.
	 * Other CC_OP events (e.g. equipment panel clicks) are ignored.
	 */
	private static final Set<Integer> SPELLBOOK_WIDGET_GROUPS = Set.of(
		218,  // Standard spellbook
		219,  // Ancient Magicks
		430   // Lunar spellbook (Arceuus shares 218)
	);

	private static final Set<String> BLOCKED_SPELLS = Set.of(
		// Standard spellbook (6 spells - SPELL-01)
		"Varrock Teleport",
		"Lumbridge Teleport",
		"Falador Teleport",
		"Camelot Teleport",
		"Ardougne Teleport",
		"Watchtower Teleport",

		// Ancient Magicks (8 spells - SPELL-03)
		"Paddewwa Teleport",
		"Senntisten Teleport",
		"Kharyrll Teleport",
		"Lassar Teleport",
		"Dareeyak Teleport",
		"Carrallangar Teleport",
		"Annakarl Teleport",
		"Ghorrock Teleport",

		// Lunar spellbook (8 spells - SPELL-04)
		"Moonclan Teleport",
		"Waterbirth Teleport",
		"Barbarian Teleport",
		"Khazard Teleport",
		"Fishing Guild Teleport",
		"Catherby Teleport",
		"Ice Plateau Teleport",
		"Trollheim Teleport",

		// Arceuus spellbook (9 spells - SPELL-05)
		"Cemetery Teleport",
		"Draynor Manor Teleport",
		"Mind Altar Teleport",
		"Salve Graveyard Teleport",
		"Fenkenstrain's Castle Teleport",
		"West Ardougne Teleport",
		"Harmony Teleport",
		"Ape Atoll Teleport",
		"Battlefront Teleport"
	);

	/**
	 * Home Teleport destinations by spellbook widget group ID (SPELL-02).
	 * Widget group ID is extracted from the widget via: widgetId >>> 16
	 *
	 * Spellbook group IDs:
	 * - 218: Standard Spellbook -> Lumbridge (outside Varlamore)
	 * - 219: Ancient Magicks -> Edgeville (outside Varlamore)
	 * - 218: Lunar Spellbook -> Lunar Isle (outside Varlamore)
	 * - 218: Arceuus Spellbook -> Dark Altar in Kourend (potentially inside Varlamore boundary)
	 *
	 * Note: All spellbooks may use group 218, making widget-based detection unreliable.
	 * However, all Home Teleport destinations (Lumbridge, Edgeville, Lunar Isle, Dark Altar)
	 * are currently outside the Varlamore boundary per placeholder region data.
	 * Therefore, Home Teleport is blocked unconditionally for now.
	 */
	private static final Map<Integer, WorldPoint> HOME_TELEPORT_DESTINATIONS = Map.of(
		218, new WorldPoint(3222, 3218, 0),  // Standard -> Lumbridge
		219, new WorldPoint(3087, 3496, 0),  // Ancient -> Edgeville
		430, new WorldPoint(2099, 3914, 0),  // Lunar -> Lunar Isle
		// Arceuus -> Dark Altar (~1698, 3881, 0) - omitted for now, will be blocked by default
		// If Dark Altar becomes part of Varlamore boundary, add here: 1698, 3881, 0
		0, new WorldPoint(1698, 3881, 0)     // Arceuus placeholder (group ID unknown)
	);

	/**
	 * Handle a menu click event and block if it's a blocked spell cast.
	 *
	 * @param event the menu click event
	 * @param chatMessageManager the chat message manager for feedback
	 * @param boundaryChecker the boundary checker for Home Teleport destination validation
	 * @return true if the spell was blocked, false otherwise
	 */
	public boolean handleMenuClick(MenuOptionClicked event, ChatMessageManager chatMessageManager, BoundaryChecker boundaryChecker)
	{
		// Only handle spellbook widget actions (CC_OP) - skip unrelated menu clicks
		if (event.getMenuAction() != MenuAction.CC_OP && event.getMenuAction() != MenuAction.CC_OP_LOW_PRIORITY)
		{
			return false;
		}

		// Filter to spellbook widgets only — equipment panel also uses CC_OP
		Widget widget = event.getWidget();
		if (widget == null || !SPELLBOOK_WIDGET_GROUPS.contains(WidgetUtil.componentToInterface(widget.getId())))
		{
			return false;
		}

		String menuOption = event.getMenuOption();

		// Skip non-cast actions (e.g. "Examine")
		if ("Cancel".equals(menuOption) || "Examine".equals(menuOption))
		{
			return false;
		}

		// Strip color tags from spell name in the target
		String spellName = event.getMenuTarget().replaceAll("<[^>]*>", "").trim();

		log.debug("Spellbook action: option='{}', spell='{}', action={}",
			menuOption, spellName, event.getMenuAction());

		// Check if spell is in blocked set (31 non-Home spells)
		// This catches both "Cast" and alternate options like "Grand Exchange", "Seers'", "Yanille"
		if (BLOCKED_SPELLS.contains(spellName))
		{
			event.consume();
			sendBlockedMessage(spellName, menuOption, chatMessageManager);
			log.debug("Blocked spell: '{}' (option: '{}')", spellName, menuOption);
			return true;
		}

		// Check for Home Teleport (SPELL-02) - requires destination checking
		if (spellName.contains("Home Teleport"))
		{
			if (isHomeTeleportBlocked(event, boundaryChecker))
			{
				event.consume();
				sendBlockedMessage(spellName, menuOption, chatMessageManager);
				log.debug("Blocked Home Teleport: '{}' (option: '{}')", spellName, menuOption);
				return true;
			}
		}

		return false;
	}

	/**
	 * Check if Home Teleport should be blocked based on spellbook and destination.
	 *
	 * Extracts the widget group ID to identify the active spellbook, looks up the destination,
	 * and checks if that destination is outside Varlamore.
	 *
	 * @param event the menu click event
	 * @param boundaryChecker the boundary checker for destination validation
	 * @return true if Home Teleport should be blocked (destination outside Varlamore or unknown)
	 */
	private boolean isHomeTeleportBlocked(MenuOptionClicked event, BoundaryChecker boundaryChecker)
	{
		// Get widget from event
		Widget widget = event.getWidget();
		if (widget == null)
		{
			// Cannot determine spellbook - block by default (safer)
			log.warn("Home Teleport clicked but widget is null - blocking by default");
			return true;
		}

		// Extract widget group ID (top 16 bits of widget ID)
		int widgetGroupId = widget.getId() >>> 16;

		// Look up destination for this spellbook
		WorldPoint destination = HOME_TELEPORT_DESTINATIONS.get(widgetGroupId);

		if (destination == null)
		{
			// Unknown spellbook - block by default and log for investigation
			log.warn("Home Teleport clicked from unknown widget group {} - blocking by default", widgetGroupId);
			return true;
		}

		// Check if destination is outside Varlamore
		boolean isDestinationInVarlamore = boundaryChecker.isInVarlamore(destination);

		log.debug("Home Teleport destination check: widget group {}, destination {}, in Varlamore: {}",
			widgetGroupId, destination, isDestinationInVarlamore);

		// Block if destination is OUTSIDE Varlamore
		return !isDestinationInVarlamore;
	}

	/**
	 * Send a chat message informing the player that a spell was blocked.
	 *
	 * @param spellName the name of the blocked spell
	 * @param menuOption the menu option used (e.g. "Cast", "Grand Exchange")
	 * @param chatMessageManager the chat message manager
	 */
	private void sendBlockedMessage(String spellName, String menuOption, ChatMessageManager chatMessageManager)
	{
		String displayName = "Cast".equals(menuOption) ? spellName : spellName + " (" + menuOption + ")";
		String message = new ChatMessageBuilder()
			.append(Color.RED, "Varlamore UIM:")
			.append(Color.WHITE, " You cannot cast " + displayName + " - it would take you outside Varlamore!")
			.build();

		chatMessageManager.queue(QueuedMessage.builder()
			.type(ChatMessageType.GAMEMESSAGE)
			.runeLiteFormattedMessage(message)
			.build());
	}
}
