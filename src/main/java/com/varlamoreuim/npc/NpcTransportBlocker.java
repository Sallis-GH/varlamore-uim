package com.varlamoreuim.npc;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Animation;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.Model;
import net.runelite.api.ModelData;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.Renderable;
import net.runelite.api.RuneLiteObject;
import net.runelite.api.Tile;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.PostMenuSort;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.callback.RenderCallback;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Service responsible for blocking NPC-based transport that leaves Varlamore.
 *
 * Handles two transport mechanisms:
 * 1. Charter ship Trader Crewmember NPCs at the 3 Varlamore ports (Sunset Coast,
 *    Aldarin, Fortis Cothon) — hidden via RenderCallback so they are visually absent
 *    and non-interactable. A safety-net menu-click block catches any residual interactions.
 *
 * 2. Primio quetzal (NPC ID 12889, Civitas illa Fortis side) — blocks interaction via
 *    menu-click consumption with a lore-friendly in-world chat message. Not replaced
 *    visually; the quetzal remains visible but will not fly the player to Varrock.
 *
 * Charter ship blocking respects the {@code unlocked} flag — when the player acquires
 * Dizana's Quiver, charter ships are revealed (the unlock gate is wired in Plan 03).
 * Primio is permanently blocked regardless of unlock state.
 */
@Slf4j
public class NpcTransportBlocker
{
	/**
	 * Trader Crewmember NPC IDs at all 3 Varlamore charter ship ports.
	 * Source: OSRS Wiki — Trader Crewmember
	 *
	 * Sunset Coast:      15510–15517 (8 IDs)
	 * Aldarin:           15518–15525 (8 IDs)
	 * Fortis Cothon:     15526–15533 (8 IDs)
	 *
	 * Total: 24 IDs. Full ranges are blocked pending in-game verification
	 * of which specific IDs are actively spawned at each dock.
	 */
	private static final Set<Integer> CHARTER_NPC_IDS = Set.of(
		// Sunset Coast
		15510, 15511, 15512, 15513, 15514, 15515, 15516, 15517,
		// Aldarin
		15518, 15519, 15520, 15521, 15522, 15523, 15524, 15525,
		// Fortis Cothon
		15526, 15527, 15528, 15529, 15530, 15531, 15532, 15533
	);

	/**
	 * Primio quetzal NPC ID on the Civitas illa Fortis side.
	 * This bird flies the player to Varrock (outside Varlamore).
	 * NPC ID 12888 is the Varrock-side Primio — unreachable by Varlamore-locked UIMs.
	 * Source: OSRS Wiki — Primio
	 */
	private static final int PRIMIO_NPC_ID = 12889;

	/**
	 * Mysterious Old Man NPC definition ID — used to load model and colour data
	 * for the RuneLiteObject stand-ins placed at charter ship docks.
	 * Source: OSRS Wiki — Mysterious Old Man (random event NPC, primary form)
	 */
	private static final int MYSTERIOUS_OLD_MAN_NPC_ID = 2830;

	/**
	 * Generic human NPC standing idle animation ID.
	 * NPCComposition does not expose getStandingAnimationID() in the RuneLite API,
	 * so we use animation 808 (standard human idle) as a safe default for the
	 * Mysterious Old Man stand-in. In-game verification may reveal a more accurate ID.
	 */
	private static final int STAND_IN_IDLE_ANIMATION_ID = 808;

	/**
	 * WorldPoint coordinates for each of the 3 charter ship dock locations.
	 * Stand-in Mysterious Old Man NPCs are placed at these approximate positions.
	 * Exact coordinates should be verified in-game with {@code ./gradlew run}.
	 *
	 * Sunset Coast: south-east dock area near the beach
	 * Aldarin: port area on the east side of Aldarin
	 * Fortis Cothon: the harbour area adjacent to the Colosseum
	 */
	private static final WorldPoint[] DOCK_LOCATIONS = {
		new WorldPoint(1504, 2956, 0), // Sunset Coast dock
		new WorldPoint(1588, 3075, 0), // Aldarin dock
		new WorldPoint(1732, 3107, 0), // Fortis Cothon dock
	};

	/** Tracks active RuneLiteObject stand-in instances for cleanup. */
	private final List<RuneLiteObject> standInNpcs = new ArrayList<>();

	/** Client reference for RuneLiteObject creation and model loading. Provided via initClient(). */
	private Client client;

	/** ClientThread for invoking model loading on the client thread. Provided via initClient(). */
	private ClientThread clientThread;

	/** ChatMessageManager for stand-in dialogue delivery. Provided via initClient(). */
	private ChatMessageManager chatMessageManager;

	/** Whether NPC blocking is active (driven by blockNpcTransport config toggle). */
	private boolean enabled = true;

	/**
	 * Whether charter ship access has been unlocked by obtaining Dizana's Quiver.
	 * When true, charter ship NPCs are shown and charter interactions are allowed.
	 * Primio is never unlocked — it is permanently blocked.
	 * Wired by VarlamoreUimPlugin in Plan 03.
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

	/** Set charter ship unlock state. Called by plugin when Dizana's Quiver is detected. */
	public void setUnlocked(boolean unlocked)
	{
		this.unlocked = unlocked;
		log.debug("Charter ship unlock state: {}", unlocked);
	}

	/** Returns whether charter ships are currently unlocked. */
	public boolean isUnlocked()
	{
		return unlocked;
	}

	/**
	 * Store client, clientThread, and chatMessageManager references.
	 * Called by VarlamoreUimPlugin in startUp() immediately after creating this service.
	 * Avoids @Inject since NpcTransportBlocker is manually instantiated.
	 */
	public void initClient(Client client, ClientThread clientThread, ChatMessageManager chatMessageManager)
	{
		this.client = client;
		this.clientThread = clientThread;
		this.chatMessageManager = chatMessageManager;
	}

	/**
	 * Spawn a Mysterious Old Man RuneLiteObject stand-in at each charter ship dock.
	 * Called on plugin startUp() (if already logged in) and when LOGGED_IN game state fires.
	 *
	 * Does nothing if charter ships are unlocked ({@code unlocked == true}) since the real
	 * Trader Crewmembers become visible again via the RenderCallback no-op path.
	 */
	public void createStandInNpcs()
	{
		if (unlocked || client == null || clientThread == null)
		{
			return;
		}

		for (WorldPoint dockLocation : DOCK_LOCATIONS)
		{
			final WorldPoint finalLocation = dockLocation;
			clientThread.invoke(() -> {
				RuneLiteObject npcObject = client.createRuneLiteObject();

				NPCComposition comp = client.getNpcDefinition(MYSTERIOUS_OLD_MAN_NPC_ID);
				if (comp == null)
				{
					log.warn("Failed to get NPCComposition for Mysterious Old Man (ID {})", MYSTERIOUS_OLD_MAN_NPC_ID);
					return;
				}

				int[] modelIds = comp.getModels();
				if (modelIds == null || modelIds.length == 0)
				{
					log.warn("Mysterious Old Man NPCComposition has no model IDs");
					return;
				}

				short[] recolFrom = comp.getColorToReplace();
				short[] recolTo = comp.getColorToReplaceWith();

				ModelData[] mds = new ModelData[modelIds.length];
				for (int i = 0; i < modelIds.length; i++)
				{
					ModelData md = client.loadModelData(modelIds[i]);
					if (md == null)
					{
						continue;
					}
					md.cloneColors().cloneVertices();
					if (recolFrom != null && recolTo != null)
					{
						for (int j = 0; j < recolFrom.length; j++)
						{
							md.recolor(recolFrom[j], recolTo[j]);
						}
					}
					mds[i] = md;
				}

				ModelData[] filtered = Arrays.stream(mds)
					.filter(Objects::nonNull)
					.toArray(ModelData[]::new);

				if (filtered.length == 0)
				{
					log.warn("Failed to load Mysterious Old Man model data for dock at {}", finalLocation);
					return;
				}

				Model model = client.mergeModels(filtered).light();
				npcObject.setModel(model);

				Animation idle = client.loadAnimation(STAND_IN_IDLE_ANIMATION_ID);
				if (idle != null)
				{
					npcObject.setAnimation(idle);
					npcObject.setShouldLoop(true);
				}

				npcObject.setRadius(60);

				LocalPoint lp = LocalPoint.fromWorld(client.getTopLevelWorldView(), finalLocation);
				if (lp != null)
				{
					npcObject.setLocation(lp, 0);
					npcObject.setActive(true);
					standInNpcs.add(npcObject);
					log.debug("Spawned Mysterious Old Man stand-in at {}", finalLocation);
				}
				else
				{
					log.debug("Dock location {} not in current scene, skipping stand-in", finalLocation);
				}
			});
		}
	}

	/**
	 * Deactivate and discard all active RuneLiteObject stand-in NPCs.
	 * Called on logout, plugin shutdown, and when unlocked state transitions to true.
	 */
	public void destroyStandInNpcs()
	{
		int count = standInNpcs.size();
		for (RuneLiteObject npcObject : standInNpcs)
		{
			npcObject.setActive(false);
		}
		standInNpcs.clear();
		log.debug("Destroyed {} Mysterious Old Man stand-ins", count);
	}

	/**
	 * Inject a "Talk-to Mysterious Old Man" menu entry when the player hovers over a
	 * tile that contains an active stand-in NPC. Called from the PostMenuSort event
	 * subscription in VarlamoreUimPlugin.
	 *
	 * PostMenuSort fires after the default menu is built but before it is displayed,
	 * giving us the correct hook point to add RuneLiteObject interactions.
	 *
	 * @param event  the PostMenuSort event (unused — we use tile position instead)
	 * @param client the RuneLite client, for tile and menu access
	 */
	public void handlePostMenuSort(PostMenuSort event, Client client)
	{
		if (!enabled || unlocked || standInNpcs.isEmpty())
		{
			return;
		}

		Tile tile = client.getTopLevelWorldView().getSelectedSceneTile();
		if (tile == null)
		{
			return;
		}

		LocalPoint tileLocation = tile.getLocalLocation();
		for (RuneLiteObject npcObject : standInNpcs)
		{
			if (npcObject.isActive() && npcObject.getLocation() != null
				&& npcObject.getLocation().equals(tileLocation))
			{
				client.getMenu().createMenuEntry(1)
					.setOption("Talk-to")
					.setTarget("<col=ffff00>Mysterious Old Man</col>")
					.setType(MenuAction.RUNELITE)
					.onClick(ev -> sendStandInMessage());
				break; // only one stand-in per tile
			}
		}
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
	 * @param client the RuneLite client (for NPC array lookup)
	 * @param chatMessageManager the chat message manager for feedback
	 * @return true if the event was consumed (Primio blocked), false otherwise
	 */
	public boolean handlePrimioClick(MenuOptionClicked event, Client client, ChatMessageManager chatMessageManager)
	{
		if (!enabled)
		{
			return false;
		}

		if (!isNpcAction(event.getMenuAction()))
		{
			return false;
		}

		NPC npc = getNpcFromEvent(event, client);
		if (npc != null && npc.getId() == PRIMIO_NPC_ID)
		{
			event.consume();
			sendBirdMessage(chatMessageManager);
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
	 * @param client the RuneLite client (for NPC array lookup)
	 * @param chatMessageManager the chat message manager for feedback
	 * @return true if the event was consumed (charter ship blocked), false otherwise
	 */
	public boolean handleCharterClick(MenuOptionClicked event, Client client, ChatMessageManager chatMessageManager)
	{
		if (!enabled || unlocked)
		{
			return false;
		}

		if (!isNpcAction(event.getMenuAction()))
		{
			return false;
		}

		NPC npc = getNpcFromEvent(event, client);
		if (npc != null && CHARTER_NPC_IDS.contains(npc.getId()))
		{
			event.consume();
			sendCharterMessage(chatMessageManager);
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
	 * @param client the RuneLite client
	 * @return the NPC at the event's index, or null if not found
	 */
	private NPC getNpcFromEvent(MenuOptionClicked event, Client client)
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
	private void sendBirdMessage(ChatMessageManager chatMessageManager)
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
	private void sendCharterMessage(ChatMessageManager chatMessageManager)
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

	/**
	 * Send a lore-friendly GAMEMESSAGE when the player clicks "Talk-to" on a
	 * Mysterious Old Man stand-in NPC at a charter ship dock.
	 * Uses the stored chatMessageManager reference (set via initClient()).
	 */
	private void sendStandInMessage()
	{
		if (chatMessageManager == null)
		{
			return;
		}
		String message = new ChatMessageBuilder()
			.append(Color.WHITE, "The old man scratches his chin. \"Hmm, the ships aren't running today. Something about the tides, I think. Best stay on dry land for now.\"")
			.build();

		chatMessageManager.queue(QueuedMessage.builder()
			.type(ChatMessageType.GAMEMESSAGE)
			.runeLiteFormattedMessage(message)
			.build());
	}
}
