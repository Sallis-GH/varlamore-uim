package com.varlamoreuim.teleport;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;

import java.awt.Color;
import java.util.Set;

/**
 * Service responsible for blocking teleport spells that leave Varlamore.
 * Handles all 31 spellbook teleports across Standard, Ancient, Lunar, and Arceuus spellbooks.
 */
@Slf4j
public class SpellTeleportBlocker
{
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
	 * Handle a menu click event and block if it's a blocked spell cast.
	 *
	 * @param event the menu click event
	 * @param chatMessageManager the chat message manager for feedback
	 * @return true if the spell was blocked, false otherwise
	 */
	public boolean handleMenuClick(MenuOptionClicked event, ChatMessageManager chatMessageManager)
	{
		// Only handle Cast menu options
		if (!"Cast".equals(event.getMenuOption()))
		{
			return false;
		}

		// Strip color tags from spell name
		String spellName = event.getMenuTarget().replaceAll("<[^>]*>", "").trim();

		// Check if spell is in blocked set
		if (BLOCKED_SPELLS.contains(spellName))
		{
			// Consume the event to block the cast
			event.consume();

			// Send chat feedback
			sendBlockedMessage(spellName, chatMessageManager);

			log.debug("Blocked spell cast: {}", spellName);
			return true;
		}

		return false;
	}

	/**
	 * Send a chat message informing the player that a spell was blocked.
	 *
	 * @param spellName the name of the blocked spell
	 * @param chatMessageManager the chat message manager
	 */
	private void sendBlockedMessage(String spellName, ChatMessageManager chatMessageManager)
	{
		String message = new ChatMessageBuilder()
			.append(Color.RED, "Varlamore UIM:")
			.append(Color.WHITE, " You cannot cast " + spellName + " - it would take you outside Varlamore!")
			.build();

		chatMessageManager.queue(QueuedMessage.builder()
			.type(ChatMessageType.GAMEMESSAGE)
			.runeLiteFormattedMessage(message)
			.build());
	}
}
