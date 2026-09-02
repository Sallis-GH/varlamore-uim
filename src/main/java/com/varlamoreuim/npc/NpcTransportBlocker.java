package com.varlamoreuim.npc;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.ItemContainer;
import net.runelite.api.MenuAction;
import net.runelite.api.NPC;
import net.runelite.api.Renderable;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.callback.RenderCallback;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;

import java.awt.Color;
import java.util.Set;

/**
 * Service responsible for blocking NPC-based transport that leaves Varlamore.
 *
 * Handles two transport mechanisms:
 * 1. Charter ship Trader Crewmember NPCs at the 3 Varlamore ports (Sunset Coast,
 *    Aldarin, Fortis Cothon) — hidden via RenderCallback so they are visually absent
 *    and non-interactable. A safety-net menu-click block catches any residual interactions.
 *    While hidden, lore-friendly stand-ins are placed in their stead by
 *    {@link com.varlamoreuim.standin}.
 *
 * 2. Primio quetzal (NPC ID 12889, Civitas illa Fortis side) — blocks interaction via
 *    menu-click consumption with a lore-friendly in-world chat message. Not replaced
 *    visually; the quetzal remains visible but will not fly the player to Varrock.
 *
 * Charter ship blocking respects the {@code unlocked} flag — when the player acquires
 * Dizana's Quiver, charter ships are revealed. Primio is permanently blocked regardless
 * of unlock state.
 */
@Slf4j
public class NpcTransportBlocker
{
	/**
	 * Trader Crewmember NPC IDs at all 3 Varlamore charter ship ports.
	 * Verified in-game:
	 *   Fortis Cothon: 9326, 9362
	 *   Sunset Coast:  9314, 9350
	 *   Aldarin Dock:  9314, 9350
	 */
	private static final Set<Integer> CHARTER_NPC_IDS = Set.of(
		9314, 9326, 9350, 9362
	);

	/**
	 * Dizana's Quiver item IDs — all four variants (uncharged, locked, charged, charged+locked).
	 * When any of these is in the player's inventory or equipment, charter ship access is unlocked.
	 * Source: OSRS Wiki — Dizana's quiver
	 *
	 * 28947: Uncharged
	 * 28949: Uncharged + Locked
	 * 28951: Charged
	 * 28953: Charged + Locked
	 */
	public static final Set<Integer> DIZANAS_QUIVER_IDS = Set.of(28947, 28949, 28951, 28953);

	/**
	 * Primio quetzal NPC ID on the Civitas illa Fortis side.
	 * This bird flies the player to Varrock (outside Varlamore).
	 * NPC ID 12888 is the Varrock-side Primio — unreachable by Varlamore-locked UIMs.
	 * Source: OSRS Wiki — Primio
	 */
	private static final int PRIMIO_NPC_ID = 12889;

	/** ChatMessageManager for message delivery. Provided via initClient(). */
	private ChatMessageManager chatMessageManager;

	/** Client reference for menu click NPC lookups. Provided via initClient(). */
	private Client client;

	/** Whether NPC blocking is active (driven by blockNpcTransport config toggle). */
	private boolean enabled = true;

	/**
	 * Whether charter ship access has been unlocked by obtaining Dizana's Quiver.
	 * When true, charter ship NPCs are shown and charter interactions are allowed.
	 * Primio is never unlocked — it is permanently blocked.
	 * Wired by VarlamoreUimPlugin.
	 */
	private boolean unlocked = false;

	/**
	 * RenderCallback that suppresses rendering of charter ship Trader Crewmember NPCs.
	 * Returns false for any NPC whose ID is in CHARTER_NPC_IDS (causing it to be hidden).
	 * No-ops when disabled or when charter ships are unlocked via Dizana's Quiver.
	 */
	private final RenderCallback renderCallback = new RenderCallback()
	{
		@Override
		public boolean addEntity(Renderable renderable, boolean ui)
		{
			if (!enabled || unlocked)
			{
				return true;
			}
			if (renderable instanceof NPC)
			{
				NPC npc = (NPC) renderable;
				if (CHARTER_NPC_IDS.contains(npc.getId()))
				{
					return false; // suppress rendering — NPC becomes invisible and non-interactable
				}
			}
			return true;
		}
	};

	// -------------------------------------------------------------------------
	// State management
	// -------------------------------------------------------------------------

	/** Enable or disable all NPC blocking. Synced from config on each game tick. */
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	/**
	 * Set charter ship unlock state.
	 * Called by VarlamoreUimPlugin when Dizana's Quiver ownership changes.
	 * No-ops when the state does not actually change.
	 */
	public void setUnlocked(boolean unlocked)
	{
		this.unlocked = unlocked;
	}

	/** Returns whether charter ships are currently unlocked. */
	public boolean isUnlocked()
	{
		return unlocked;
	}

	/**
	 * Check whether an ItemContainer holds any variant of Dizana's Quiver.
	 * Used by VarlamoreUimPlugin to check both inventory and equipment containers.
	 *
	 * @param container the ItemContainer to inspect (may be null)
	 * @return true if any Dizana's Quiver variant is present, false otherwise
	 */
	public static boolean containsDizanasQuiver(ItemContainer container)
	{
		if (container == null)
		{
			return false;
		}
		for (int id : DIZANAS_QUIVER_IDS)
		{
			if (container.contains(id))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Store client and chatMessageManager references.
	 * Called by VarlamoreUimPlugin in startUp() immediately after creating this service.
	 * Avoids @Inject since NpcTransportBlocker is manually instantiated.
	 */
	public void initClient(Client client, ChatMessageManager chatMessageManager)
	{
		this.client = client;
		this.chatMessageManager = chatMessageManager;
	}

	/** Returns the RenderCallback for registration with RenderCallbackManager. */
	public RenderCallback getRenderCallback()
	{
		return renderCallback;
	}

	// -------------------------------------------------------------------------
	// Menu click handlers
	// -------------------------------------------------------------------------

	/**
	 * Handle a menu click event and block if the player clicked on Primio quetzal (NPC 12889).
	 *
	 * Primio is permanently blocked — no unlock gate. The lore-friendly message ("The bird
	 * doesn't seem interested in interacting with you.") gives an in-world reason without
	 * exposing plugin mechanics.
	 *
	 * @param event the menu click event
	 * @return true if the event was consumed (Primio blocked), false otherwise
	 */
	public boolean handlePrimioClick(MenuOptionClicked event)
	{
		if (!enabled)
		{
			return false;
		}

		if (!isNpcAction(event.getMenuAction()))
		{
			return false;
		}

		NPC npc = getNpcFromEvent(event);
		if (npc != null && npc.getId() == PRIMIO_NPC_ID)
		{
			event.consume();
			sendBirdMessage();
			log.debug("Blocked Primio quetzal interaction (NPC index {})", event.getId());
			return true;
		}

		return false;
	}

	/**
	 * Handle a menu click event and block if the player clicked a charter ship NPC.
	 *
	 * This is a safety-net — the RenderCallback suppresses charter ship rendering so they
	 * should not be visible or have menu entries. This catches edge cases where the player
	 * had an open menu before the plugin enabled, or interacts at the NPC's former position.
	 *
	 * Respects the {@code unlocked} flag — if charter ships are unlocked, this returns false
	 * and allows the interaction.
	 *
	 * @param event the menu click event
	 * @return true if the event was consumed (charter ship blocked), false otherwise
	 */
	public boolean handleCharterClick(MenuOptionClicked event)
	{
		if (!enabled || unlocked)
		{
			return false;
		}

		if (!isNpcAction(event.getMenuAction()))
		{
			return false;
		}

		NPC npc = getNpcFromEvent(event);
		if (npc != null && CHARTER_NPC_IDS.contains(npc.getId()))
		{
			event.consume();
			sendCharterMessage();
			log.debug("Blocked charter ship interaction on NPC {} (index {})", npc.getId(), event.getId());
			return true;
		}

		return false;
	}

	// -------------------------------------------------------------------------
	// Private helpers
	// -------------------------------------------------------------------------

	/**
	 * Returns true if the menu action is one of the NPC interaction types.
	 * NPC_FIRST_OPTION through NPC_FIFTH_OPTION cover all right-click and left-click
	 * NPC interactions.
	 */
	private boolean isNpcAction(MenuAction action)
	{
		return action == MenuAction.NPC_FIRST_OPTION
			|| action == MenuAction.NPC_SECOND_OPTION
			|| action == MenuAction.NPC_THIRD_OPTION
			|| action == MenuAction.NPC_FOURTH_OPTION
			|| action == MenuAction.NPC_FIFTH_OPTION;
	}

	/**
	 * Look up the NPC associated with a menu click event.
	 *
	 * For NPC menu actions, {@code event.getId()} returns the NPC's slot index in the
	 * client's NPC cache — NOT the NPC's definition ID. We look up the NPC by index via
	 * {@code WorldView.npcs().byIndex()} and then call {@code npc.getId()} for the definition ID.
	 *
	 * @param event the menu click event
	 * @return the NPC at the event's index, or null if not found
	 */
	private NPC getNpcFromEvent(MenuOptionClicked event)
	{
		int npcIndex = event.getId();
		if (npcIndex < 0)
		{
			return null;
		}
		net.runelite.api.WorldView wv = client.getTopLevelWorldView();
		if (wv == null)
		{
			return null;
		}
		return wv.npcs().byIndex(npcIndex);
	}

	/**
	 * Send a lore-friendly in-world message when Primio quetzal interaction is blocked.
	 * No "Varlamore UIM:" prefix — intentionally presented as the bird's response.
	 */
	private void sendBirdMessage()
	{
		String message = new ChatMessageBuilder()
			.append(Color.WHITE, "The bird doesn't seem interested in interacting with you.")
			.build();

		chatMessageManager.queue(QueuedMessage.builder()
			.type(ChatMessageType.GAMEMESSAGE)
			.runeLiteFormattedMessage(message)
			.build());
	}

	/**
	 * Send a chat message when a charter ship NPC interaction is caught by the safety net.
	 * Uses the standard "Varlamore UIM:" red prefix to match other plugin messages.
	 */
	private void sendCharterMessage()
	{
		String message = new ChatMessageBuilder()
			.append(Color.RED, "Varlamore UIM:")
			.append(Color.WHITE, " The charter ship crew doesn't appear to be taking passengers right now.")
			.build();

		chatMessageManager.queue(QueuedMessage.builder()
			.type(ChatMessageType.GAMEMESSAGE)
			.runeLiteFormattedMessage(message)
			.build());
	}
}
