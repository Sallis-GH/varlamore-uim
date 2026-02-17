package com.varlamoreuim.teleport;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Service responsible for blocking item-based teleports that leave Varlamore.
 *
 * Handles all-destination-blocked items: jewelry (Amulet of Glory, Games Necklace,
 * Skills Necklace, Combat Bracelet, Ring of Wealth, Burning Amulet, Necklace of Passage,
 * Digsite Pendant, Slayer Ring), teleport tablets (Varrock, Lumbridge, Falador, Camelot,
 * Ardougne, Watchtower), quest items (Ectophial, Xeric's Talisman, Kharedst's Memoirs,
 * Pharaoh's Sceptre, Skull Sceptre, Enchanted Lyre, Drakan's Medallion), and achievement
 * diary gear (Karamja Gloves, Explorer's Ring, Ardougne Cloak, Morytania Legs, Desert Amulet,
 * Wilderness Sword, Rada's Blessing).
 *
 * Whitelists Pendant of Ates (all destinations inside Varlamore).
 * Per-destination items: Ring of Dueling (allow Fortis Colosseum), Hunter/Max cape
 * (allow Hunter Guild), House teleport tablet (conditional on POH location).
 * Minigame grouping tab blocking handled via handleMinigameTeleport().
 */
@Slf4j
public class ItemTeleportBlocker
{
	/**
	 * Pendant of Ates — all destinations are within Varlamore. Always allow.
	 * Item IDs: base + 6 charged variants.
	 * Source: OSRS Wiki — Pendant of Ates
	 */
	private static final Set<Integer> PENDANT_OF_ATES_IDS;

	/**
	 * Ring of Dueling — Fortis Colosseum (Varlamore) is allowed; other destinations blocked.
	 * IDs: RING_OF_DUELING8 (2552) through RING_OF_DUELING1 (2566)
	 * Source: OSRS Wiki — Ring of dueling
	 */
	private static final Set<Integer> RING_OF_DUELING_IDS = Set.of(
		ItemID.RING_OF_DUELING8,  // 2552
		ItemID.RING_OF_DUELING7,  // 2554
		ItemID.RING_OF_DUELING6,  // 2556
		ItemID.RING_OF_DUELING5,  // 2558
		ItemID.RING_OF_DUELING4,  // 2560
		ItemID.RING_OF_DUELING3,  // 2562
		ItemID.RING_OF_DUELING2,  // 2564
		ItemID.RING_OF_DUELING1   // 2566
	);

	/**
	 * The only Ring of Dueling destination that is within Varlamore.
	 * Other destinations (Emir's Arena, Castle Wars, Ferox Enclave) are outside.
	 */
	private static final String ALLOWED_DUELING_DESTINATION = "Fortis Colosseum";

	/**
	 * Hunter Cape — Hunter Guild (Varlamore) is allowed; other destinations blocked.
	 * IDs: HUNTER_CAPE (9948), HUNTER_CAPET (9949, trimmed)
	 * Source: OSRS Wiki — Hunter cape
	 */
	private static final Set<Integer> HUNTER_CAPE_IDS = Set.of(
		ItemID.HUNTER_CAPE,   // 9948
		ItemID.HUNTER_CAPET   // 9949 (trimmed)
	);

	/**
	 * Known teleport option strings for the Hunter cape.
	 * Used to distinguish teleport menu entries from wear/equip/drop options.
	 * Destinations: Hunter Guild (Varlamore, allowed), Feldip Hunter area, Carnivorous chinchompas (blocked).
	 * Source: OSRS Wiki — Hunter cape
	 */
	private static final Set<String> HUNTER_TELEPORT_OPTIONS = Set.of(
		"Hunter Guild",
		"Feldip Hunter area",
		"Carnivorous chinchompas"
	);

	/**
	 * The only Hunter cape destination that is within Varlamore.
	 */
	private static final Set<String> ALLOWED_HUNTER_DESTINATIONS = Set.of("Hunter Guild");

	/**
	 * Max Cape — includes Hunter cape teleport destinations. Hunter Guild is allowed;
	 * all other teleport options (from other skill capes) are blocked.
	 * Includes all Max cape variants that have teleport functionality.
	 * Source: OSRS Wiki — Max cape
	 */
	private static final Set<Integer> MAX_CAPE_IDS = Set.of(
		ItemID.MAX_CAPE,                     // 13280 (base)
		ItemID.FIRE_MAX_CAPE,                // 13329
		ItemID.SARADOMIN_MAX_CAPE,           // 13331
		ItemID.ZAMORAK_MAX_CAPE,             // 13333
		ItemID.GUTHIX_MAX_CAPE,              // 13335
		ItemID.ACCUMULATOR_MAX_CAPE,         // 13337
		ItemID.MAX_CAPE_13342,               // 13342
		ItemID.ARDOUGNE_MAX_CAPE,            // 20760
		ItemID.FIRE_MAX_CAPE_21186,          // 21186
		ItemID.INFERNAL_MAX_CAPE,            // 21284
		ItemID.INFERNAL_MAX_CAPE_21285,      // 21285
		ItemID.IMBUED_SARADOMIN_MAX_CAPE,    // 21776
		ItemID.IMBUED_ZAMORAK_MAX_CAPE,      // 21780
		ItemID.IMBUED_GUTHIX_MAX_CAPE,       // 21784
		ItemID.ASSEMBLER_MAX_CAPE,           // 21898
		ItemID.INFERNAL_MAX_CAPE_L,          // 24133
		ItemID.FIRE_MAX_CAPE_L,              // 24134
		ItemID.ASSEMBLER_MAX_CAPE_L,         // 24135
		ItemID.IMBUED_SARADOMIN_MAX_CAPE_L,  // 24232
		ItemID.IMBUED_ZAMORAK_MAX_CAPE_L,    // 24233
		ItemID.IMBUED_GUTHIX_MAX_CAPE_L,     // 24234
		ItemID.MYTHICAL_MAX_CAPE,            // 24855
		ItemID.MASORI_ASSEMBLER_MAX_CAPE,    // 27363
		ItemID.MASORI_ASSEMBLER_MAX_CAPE_L,  // 27365
		ItemID.DIZANAS_MAX_CAPE,             // 28902
		ItemID.DIZANAS_MAX_CAPE_L            // 28906
	);

	/**
	 * House teleport tablet — allowed only when player's POH is in Aldarin (Varlamore).
	 * VarBit 2187 controls POH location; value 8 = Aldarin.
	 * Source: OSRS Wiki — Teleport to house, Player-owned house
	 */
	private static final Set<Integer> HOUSE_TABLET_IDS = Set.of(
		ItemID.TELEPORT_TO_HOUSE  // 8013
	);

	/**
	 * VarBit ID for Player-Owned House location.
	 * Value 8 corresponds to Aldarin (the only Varlamore POH location).
	 * If VarBit value differs, POH is outside Varlamore — block the teleport.
	 */
	private static final int HOUSE_LOCATION_VARBIT = 2187;
	private static final int ALDARIN_HOUSE_VALUE = 8;

	/**
	 * All item IDs where every teleport destination is outside Varlamore.
	 * Any teleport option on these items should be blocked.
	 */
	private static final Set<Integer> BLOCKED_ITEM_IDS;

	/**
	 * Maps blocked item IDs to their destination display strings.
	 * Used in chat messages: "[item] teleport to [destinations] is blocked"
	 * Every item in BLOCKED_ITEM_IDS must have a corresponding entry here.
	 */
	private static final Map<Integer, String> ITEM_DESTINATION_DISPLAY;

	/**
	 * All 17 minigame grouping tab teleport destinations.
	 * None are in Varlamore — all are blocked when blockMinigameTeleports is enabled.
	 * Source: OSRS Wiki — Minigame Group Finder
	 */
	private static final Set<String> BLOCKED_MINIGAME_DESTINATIONS = Set.of(
		"Barbarian Assault",
		"Burthorpe Games Room",
		"Castle Wars",
		"Clan Wars",
		"Fishing Trawler",
		"Giants' Foundry",
		"Guardians of the Rift",
		"Last Man Standing",
		"Mage Training Arena",
		"Nightmare Zone",
		"Pest Control",
		"Rat Pits",
		"Shades of Mort'ton",
		"Soul Wars",
		"Tithe Farm",
		"Trouble Brewing",
		"TzHaar Fight Pit"
	);

	/**
	 * Menu option strings that indicate a teleport action.
	 * Teleport option names used by different item categories:
	 * - Jewelry: "Rub" (amulets, rings)
	 * - Tablets: "Break"
	 * - Quest items: "Rub", "Activate", "Empty", "Play", "Read", "Invoke"
	 * - Diary gear: "Teleport"
	 */
	private static final Set<String> TELEPORT_OPTIONS = Set.of(
		"Rub",      // Jewelry: amulets, rings
		"Break",    // Tablets
		"Teleport", // Generic teleport option / diary gear
		"Activate", // Some quest items
		"Empty",    // Ectophial uses "Empty" option
		"Play",     // Enchanted Lyre
		"Read",     // Kharedst's Memoirs
		"Invoke"    // Skull Sceptre
	);

	static
	{
		// --- PENDANT OF ATES WHITELIST ---
		// All destinations are within Varlamore — always allow.
		// ItemID.PENDANT_OF_ATES = 28733 (base/uncharged)
		// Charged variants use sequential IDs (28734 through 28739 for charges 1-6)
		// Source: OSRS Wiki — Pendant of Ates
		Set<Integer> pendantIds = new HashSet<>();
		pendantIds.add(ItemID.PENDANT_OF_ATES);
		for (int i = ItemID.PENDANT_OF_ATES + 1; i <= ItemID.PENDANT_OF_ATES + 6; i++)
		{
			pendantIds.add(i);
		}
		PENDANT_OF_ATES_IDS = Set.copyOf(pendantIds);

		// --- BLOCKED ITEM IDS + DESTINATION DISPLAY MAP ---
		Set<Integer> blocked = new HashSet<>();
		Map<Integer, String> destinations = new HashMap<>();

		// =====================================================================
		// JEWELRY — ITEM-01
		// All destinations outside Varlamore
		// =====================================================================

		// Amulet of Glory — Edgeville, Karamja, Draynor Village, Al Kharid
		// Option: "Rub"
		// Named constants: AMULET_OF_GLORY (uncharged), AMULET_OF_GLORY1-6 (charges)
		// Trimmed: AMULET_OF_GLORY_T (uncharged), AMULET_OF_GLORY_T1-6 (charges)
		// Eternal glory: AMULET_OF_ETERNAL_GLORY (infinite charges)
		// Source: OSRS Wiki — Amulet of glory
		addVariants(blocked, destinations,
			"Edgeville, Karamja, Draynor Village, Al Kharid",
			ItemID.AMULET_OF_GLORY,        // uncharged (1712)
			ItemID.AMULET_OF_GLORY1,       // 1 charge  (1710)
			ItemID.AMULET_OF_GLORY2,       // 2 charges (1708)
			ItemID.AMULET_OF_GLORY3,       // 3 charges (1706)
			ItemID.AMULET_OF_GLORY4,       // 4 charges (1704)
			ItemID.AMULET_OF_GLORY5,       // 5 charges (11976)
			ItemID.AMULET_OF_GLORY6,       // 6 charges (11978)
			ItemID.AMULET_OF_GLORY_T,      // trimmed uncharged (10362)
			ItemID.AMULET_OF_GLORY_T1,     // trimmed 1 charge  (10360)
			ItemID.AMULET_OF_GLORY_T2,     // trimmed 2 charges (10358)
			ItemID.AMULET_OF_GLORY_T3,     // trimmed 3 charges (10356)
			ItemID.AMULET_OF_GLORY_T4,     // trimmed 4 charges (10354)
			ItemID.AMULET_OF_GLORY_T5,     // trimmed 5 charges (11980)
			ItemID.AMULET_OF_GLORY_T6,     // trimmed 6 charges (11982)
			ItemID.AMULET_OF_ETERNAL_GLORY // eternal glory (19707)
		);

		// Games Necklace — Burthorpe, Barbarian Outpost, Corporeal Beast, Tears of Guthix, Wintertodt
		// Option: "Rub"
		// IDs: GAMES_NECKLACE8 (3853) through GAMES_NECKLACE1 (3867)
		// Source: OSRS Wiki — Games necklace
		addVariants(blocked, destinations,
			"Burthorpe, Barbarian Outpost, Corporeal Beast, Tears of Guthix, Wintertodt",
			ItemID.GAMES_NECKLACE8,  // 3853
			ItemID.GAMES_NECKLACE7,  // 3855
			ItemID.GAMES_NECKLACE6,  // 3857
			ItemID.GAMES_NECKLACE5,  // 3859
			ItemID.GAMES_NECKLACE4,  // 3861
			ItemID.GAMES_NECKLACE3,  // 3863
			ItemID.GAMES_NECKLACE2,  // 3865
			ItemID.GAMES_NECKLACE1   // 3867
		);

		// Skills Necklace — Fishing Guild, Mining Guild, Crafting Guild, Cooking Guild, Woodcutting Guild, Farming Guild
		// Option: "Rub"
		// IDs: SKILLS_NECKLACE6 (11105) through SKILLS_NECKLACE1 (11115)
		// Source: OSRS Wiki — Skills necklace
		addVariants(blocked, destinations,
			"Fishing Guild, Mining Guild, Crafting Guild, Cooking Guild, Woodcutting Guild, Farming Guild",
			ItemID.SKILLS_NECKLACE6,  // 11105
			ItemID.SKILLS_NECKLACE5,  // 11107
			ItemID.SKILLS_NECKLACE4,  // 11109
			ItemID.SKILLS_NECKLACE3,  // 11111
			ItemID.SKILLS_NECKLACE2,  // 11113
			ItemID.SKILLS_NECKLACE1   // 11115
		);

		// Combat Bracelet — Warriors' Guild, Champions' Guild, Edgeville Monastery, Ranging Guild
		// Option: "Rub"
		// IDs: COMBAT_BRACELET6 (11118) through COMBAT_BRACELET1 (11128)
		// Source: OSRS Wiki — Combat bracelet
		addVariants(blocked, destinations,
			"Warriors' Guild, Champions' Guild, Edgeville Monastery, Ranging Guild",
			ItemID.COMBAT_BRACELET6,  // 11118
			ItemID.COMBAT_BRACELET5,  // 11120
			ItemID.COMBAT_BRACELET4,  // 11122
			ItemID.COMBAT_BRACELET3,  // 11124
			ItemID.COMBAT_BRACELET2,  // 11126
			ItemID.COMBAT_BRACELET1   // 11128
		);

		// Ring of Wealth — Grand Exchange, Falador, Miscellania, Dondakan's Rock
		// Option: "Rub"
		// Regular: RING_OF_WEALTH_5 (2572) through RING_OF_WEALTH_1 (2580)
		// Imbued: RING_OF_WEALTH_I5 (12785) through RING_OF_WEALTH_I1 (12793)
		// Note: naming format is I5/I4 not I_5/I_4
		// Source: OSRS Wiki — Ring of wealth
		addVariants(blocked, destinations,
			"Grand Exchange, Falador, Miscellania, Dondakan's Rock",
			ItemID.RING_OF_WEALTH_5,   // 2572
			ItemID.RING_OF_WEALTH_4,   // 2574
			ItemID.RING_OF_WEALTH_3,   // 2576
			ItemID.RING_OF_WEALTH_2,   // 2578
			ItemID.RING_OF_WEALTH_1,   // 2580
			ItemID.RING_OF_WEALTH_I5,  // 12785
			ItemID.RING_OF_WEALTH_I4,  // 12787
			ItemID.RING_OF_WEALTH_I3,  // 12789
			ItemID.RING_OF_WEALTH_I2,  // 12791
			ItemID.RING_OF_WEALTH_I1   // 12793
		);

		// Burning Amulet — Chaos Temple, Bandit Camp, Lava Maze
		// Option: "Rub"
		// IDs: BURNING_AMULET5 (21166) through BURNING_AMULET1 (21174)
		// Source: OSRS Wiki — Burning amulet
		addVariants(blocked, destinations,
			"Chaos Temple, Bandit Camp, Lava Maze",
			ItemID.BURNING_AMULET5,  // 21166
			ItemID.BURNING_AMULET4,  // 21168
			ItemID.BURNING_AMULET3,  // 21170
			ItemID.BURNING_AMULET2,  // 21172
			ItemID.BURNING_AMULET1   // 21174
		);

		// Necklace of Passage — Wizards' Tower, Outpost, Eagle's Eyrie
		// Option: "Rub"
		// IDs: NECKLACE_OF_PASSAGE5 (21146) through NECKLACE_OF_PASSAGE1 (21154)
		// Source: OSRS Wiki — Necklace of passage
		addVariants(blocked, destinations,
			"Wizards' Tower, Outpost, Eagle's Eyrie",
			ItemID.NECKLACE_OF_PASSAGE5,  // 21146
			ItemID.NECKLACE_OF_PASSAGE4,  // 21148
			ItemID.NECKLACE_OF_PASSAGE3,  // 21150
			ItemID.NECKLACE_OF_PASSAGE2,  // 21152
			ItemID.NECKLACE_OF_PASSAGE1   // 21154
		);

		// Digsite Pendant — Digsite, Fossil Island Volcano, Lithkren Vault
		// Option: "Rub"
		// IDs: DIGSITE_PENDANT_5 (11194) through DIGSITE_PENDANT_1 (11186)
		// Source: OSRS Wiki — Digsite pendant
		addVariants(blocked, destinations,
			"Digsite, Fossil Island Volcano, Lithkren Vault",
			ItemID.DIGSITE_PENDANT_5,  // 11194
			ItemID.DIGSITE_PENDANT_4,  // 11192
			ItemID.DIGSITE_PENDANT_3,  // 11190
			ItemID.DIGSITE_PENDANT_2,  // 11188
			ItemID.DIGSITE_PENDANT_1   // 11186
		);

		// Slayer Ring — Stronghold Slayer Cave, Slayer Tower, Fremennik Slayer Dungeon, Tarn's Lair, Dark Beasts, Haunted Mine
		// Option: "Rub" or "Teleport"
		// IDs: SLAYER_RING_8 (11866) through SLAYER_RING_1 (11873), SLAYER_RING_ETERNAL (11864)
		// Source: OSRS Wiki — Slayer ring
		addVariants(blocked, destinations,
			"Stronghold Slayer Cave, Slayer Tower, Fremennik Slayer Dungeon, Tarn's Lair, Dark Beasts, Haunted Mine",
			ItemID.SLAYER_RING_8,       // 11866
			ItemID.SLAYER_RING_7,       // 11867
			ItemID.SLAYER_RING_6,       // 11868
			ItemID.SLAYER_RING_5,       // 11869
			ItemID.SLAYER_RING_4,       // 11870
			ItemID.SLAYER_RING_3,       // 11871
			ItemID.SLAYER_RING_2,       // 11872
			ItemID.SLAYER_RING_1,       // 11873
			ItemID.SLAYER_RING_ETERNAL  // 11864
		);

		// =====================================================================
		// TELEPORT TABLETS — ITEM-02
		// All go outside Varlamore
		// Option: "Break"
		// Source: OSRS Wiki — Teleport tablets
		// =====================================================================

		addVariants(blocked, destinations,
			"Varrock",
			ItemID.VARROCK_TELEPORT   // 8007
		);

		addVariants(blocked, destinations,
			"Lumbridge",
			ItemID.LUMBRIDGE_TELEPORT // 8008
		);

		addVariants(blocked, destinations,
			"Falador",
			ItemID.FALADOR_TELEPORT   // 8009
		);

		addVariants(blocked, destinations,
			"Camelot",
			ItemID.CAMELOT_TELEPORT   // 8010
		);

		addVariants(blocked, destinations,
			"Ardougne",
			ItemID.ARDOUGNE_TELEPORT  // 8011
		);

		addVariants(blocked, destinations,
			"Watchtower",
			ItemID.WATCHTOWER_TELEPORT // 8012
		);

		// =====================================================================
		// QUEST TELEPORT ITEMS — ITEM-03
		// All destinations outside Varlamore
		// =====================================================================

		// Ectophial — Ectofuntus (Morytania)
		// Option: "Empty"
		// ID: 4251
		// Source: OSRS Wiki — Ectophial
		addVariants(blocked, destinations,
			"Ectofuntus",
			ItemID.ECTOPHIAL  // 4251
		);

		// Xeric's Talisman — Xeric's Lookout, Glade, Inferno, Heart, Honour (all Great Kourend)
		// Option: "Rub"
		// IDs: XERICS_TALISMAN_INERT (13392 uncharged), XERICS_TALISMAN (13393 charged base)
		// Source: OSRS Wiki — Xeric's talisman
		addVariants(blocked, destinations,
			"Xeric's Lookout, Xeric's Glade, Xeric's Inferno, Xeric's Heart, Xeric's Honour",
			ItemID.XERICS_TALISMAN_INERT,  // 13392 (inert/uncharged)
			ItemID.XERICS_TALISMAN         // 13393 (charged)
		);

		// Kharedst's Memoirs — Multiple Great Kourend locations
		// Option: "Read"
		// ID: 23946
		// Source: OSRS Wiki — Kharedst's memoirs
		addVariants(blocked, destinations,
			"Hosidius, Piscarilius, Lovakengj, Shayzien, Arceuus",
			ItemID.KHAREDSTS_MEMOIRS  // 23946
		);

		// Pharaoh's Sceptre — Jalsavrah, Agility Pyramid, Pyramid Plunder (all Desert)
		// Option: "Rub"
		// Named constant PHARAOHS_SCEPTRE exists (uncharged 9043).
		// Charge variants (1-3 charges: 9044-9046) have no named constants in RuneLite API 1.12.17.
		// Extended variants (4-8 charges: 26938, 26940, 26942, 26944, 26946) added with higher charges.
		// Using numeric IDs per plan fallback: "If a constant is not available, use the numeric ID."
		// Source: OSRS Wiki — Pharaoh's sceptre
		addVariants(blocked, destinations,
			"Jalsavrah Pyramid, Agility Pyramid, Pyramid Plunder",
			ItemID.PHARAOHS_SCEPTRE,  // 9043 (uncharged)
			9044,                      // 1 charge
			9045,                      // 2 charges
			9046,                      // 3 charges
			26938,                     // 4 charges (updated sceptre)
			26940,                     // 5 charges
			26942,                     // 6 charges
			26944,                     // 7 charges
			26946                      // 8 charges
		);

		// Skull Sceptre — Barbarian Village
		// Option: "Invoke"
		// IDs: SKULL_SCEPTRE (4155), SKULL_SCEPTRE_I (21270, imbued version)
		// Source: OSRS Wiki — Skull sceptre
		addVariants(blocked, destinations,
			"Barbarian Village",
			ItemID.SKULL_SCEPTRE,    // 4155
			ItemID.SKULL_SCEPTRE_I   // 21270 (imbued)
		);

		// Enchanted Lyre — Rellekka
		// Option: "Play"
		// IDs: ENCHANTED_LYRE (3689 - base?), ENCHANTED_LYRE1-4, ENCHANTED_LYREI (7006 - unlimited)
		// Source: OSRS Wiki — Enchanted lyre
		addVariants(blocked, destinations,
			"Rellekka",
			ItemID.ENCHANTED_LYRE,   // base/uncharged
			ItemID.ENCHANTED_LYRE1,  // 1 charge
			ItemID.ENCHANTED_LYRE2,  // 2 charges
			ItemID.ENCHANTED_LYRE3,  // 3 charges
			ItemID.ENCHANTED_LYRE4,  // 4 charges
			ItemID.ENCHANTED_LYREI   // 7006 (unlimited - Fremennik hard diary reward)
		);

		// Drakan's Medallion — Ver Sinhaza, Darkmeyer, Slepe (all Morytania)
		// Option: "Teleport"
		// ID: 22400
		// Source: OSRS Wiki — Drakan's medallion
		addVariants(blocked, destinations,
			"Ver Sinhaza, Darkmeyer, Slepe",
			ItemID.DRAKANS_MEDALLION  // 22400
		);

		// =====================================================================
		// ACHIEVEMENT DIARY GEAR — ITEM-04
		// All destinations outside Varlamore
		// =====================================================================

		// Karamja Gloves (tiers 1-4) — Shilo Village mine
		// Option: "Teleport"
		// IDs: KARAMJA_GLOVES_1 (11133) through KARAMJA_GLOVES_4 (11136)
		// Source: OSRS Wiki — Karamja gloves
		addVariants(blocked, destinations,
			"Shilo Village mine",
			ItemID.KARAMJA_GLOVES_1,  // 11133
			ItemID.KARAMJA_GLOVES_2,  // 11134
			ItemID.KARAMJA_GLOVES_3,  // 11135
			ItemID.KARAMJA_GLOVES_4   // 11136
		);

		// Explorer's Ring (tiers 2-4) — Cabbage patch near Falador
		// Option: "Teleport" (tier 2+ only; tier 1 has no teleport option)
		// IDs: EXPLORERS_RING_2 (10849) through EXPLORERS_RING_4 (10851)
		// Source: OSRS Wiki — Explorer's ring
		addVariants(blocked, destinations,
			"Cabbage patch near Falador",
			ItemID.EXPLORERS_RING_2,  // 10849
			ItemID.EXPLORERS_RING_3,  // 10850
			ItemID.EXPLORERS_RING_4   // 10851
		);

		// Ardougne Cloak (tiers 1-4) — Ardougne Monastery
		// Option: "Teleport"
		// IDs: ARDOUGNE_CLOAK_1 (10946) through ARDOUGNE_CLOAK_4 (10949)
		// Source: OSRS Wiki — Ardougne cloak
		addVariants(blocked, destinations,
			"Ardougne Monastery",
			ItemID.ARDOUGNE_CLOAK_1,  // 10946
			ItemID.ARDOUGNE_CLOAK_2,  // 10947
			ItemID.ARDOUGNE_CLOAK_3,  // 10948
			ItemID.ARDOUGNE_CLOAK_4   // 10949
		);

		// Morytania Legs (tiers 3-4) — Burgh de Rott
		// Option: "Teleport" (tier 3+ only; tiers 1-2 have no teleport)
		// IDs: MORYTANIA_LEGS_3 (11000), MORYTANIA_LEGS_4 (11001)
		// Source: OSRS Wiki — Morytania legs
		addVariants(blocked, destinations,
			"Burgh de Rott",
			ItemID.MORYTANIA_LEGS_3,  // 11000
			ItemID.MORYTANIA_LEGS_4   // 11001
		);

		// Desert Amulet (tier 4) — Nardah
		// Option: "Teleport" (tier 4 only; tiers 1-3 have no teleport to Nardah)
		// ID: DESERT_AMULET_4 (13229)
		// Source: OSRS Wiki — Desert amulet
		addVariants(blocked, destinations,
			"Nardah",
			ItemID.DESERT_AMULET_4  // 13229
		);

		// Wilderness Sword (tiers 2-4) — Edgeville (via wilderness lever)
		// Option: "Teleport" (tier 2+; tier 1 has no teleport)
		// IDs: WILDERNESS_SWORD_2 (21631) through WILDERNESS_SWORD_4 (21633)
		// Source: OSRS Wiki — Wilderness sword
		addVariants(blocked, destinations,
			"Edgeville",
			ItemID.WILDERNESS_SWORD_2,  // 21631
			ItemID.WILDERNESS_SWORD_3,  // 21632
			ItemID.WILDERNESS_SWORD_4   // 21633
		);

		// Rada's Blessing (tiers 3-4) — Kourend Woodland, Mount Karuulm
		// Option: "Teleport" (tier 3+; tiers 1-2 have no teleport)
		// IDs: RADAS_BLESSING_3 (20027), RADAS_BLESSING_4 (20028)
		// Source: OSRS Wiki — Rada's blessing
		addVariants(blocked, destinations,
			"Kourend Woodland, Mount Karuulm",
			ItemID.RADAS_BLESSING_3,  // 20027
			ItemID.RADAS_BLESSING_4   // 20028
		);

		BLOCKED_ITEM_IDS = Set.copyOf(blocked);
		ITEM_DESTINATION_DISPLAY = Map.copyOf(destinations);
	}

	/**
	 * Helper method to add all given item IDs to both the blocked set and
	 * the destination display map with the same destination string.
	 * Accepts a varargs int array to allow both named constants and numeric literals.
	 */
	private static void addVariants(Set<Integer> blocked, Map<Integer, String> destinations,
		String destinationDisplay, int... itemIds)
	{
		for (int id : itemIds)
		{
			blocked.add(id);
			destinations.put(id, destinationDisplay);
		}
	}

	/**
	 * Handle a menu click event and block if it's an item teleport that leaves Varlamore.
	 * Dispatch order:
	 * 1. Whitelist: Pendant of Ates (always allowed)
	 * 2. Per-destination: Ring of Dueling (allow Fortis Colosseum, block others)
	 * 3. Per-destination: Hunter cape (allow Hunter Guild, block Feldip/Chinchompas)
	 * 4. Per-destination: Max cape (Hunter Guild allowed; all other teleports blocked)
	 * 5. Conditional: House teleport tablet (check POH VarBit)
	 * 6. All-destination-blocked items from BLOCKED_ITEM_IDS
	 *
	 * @param event the menu click event
	 * @param chatMessageManager the chat message manager for feedback
	 * @param client the RuneLite client (used for POH VarBit checks)
	 * @return true if the teleport was blocked, false otherwise
	 */
	public boolean handleMenuClick(MenuOptionClicked event, ChatMessageManager chatMessageManager, Client client)
	{
		if (!event.isItemOp())
		{
			return false; // Not an item operation — skip (spells, walks, NPC clicks, minigame tab)
		}

		int itemId = event.getItemId();
		String option = event.getMenuOption();
		String itemName = event.getMenuTarget().replaceAll("<[^>]*>", "").trim();

		// Whitelist: Pendant of Ates — all destinations in Varlamore
		if (PENDANT_OF_ATES_IDS.contains(itemId))
		{
			return false;
		}

		// Per-destination: Ring of Dueling (allow Fortis Colosseum, block others)
		if (RING_OF_DUELING_IDS.contains(itemId))
		{
			return handleRingOfDueling(event, option, itemName, chatMessageManager);
		}

		// Per-destination: Hunter cape (allow Hunter Guild, block Feldip/Chinchompas)
		if (HUNTER_CAPE_IDS.contains(itemId))
		{
			return handleHunterCape(event, option, itemName, chatMessageManager);
		}

		// Per-destination: Max cape (Hunter Guild allowed via Hunter logic; all other teleports blocked)
		if (MAX_CAPE_IDS.contains(itemId))
		{
			if (HUNTER_TELEPORT_OPTIONS.contains(option))
			{
				return handleHunterCape(event, option, itemName, chatMessageManager);
			}
			if (isTeleportOption(option))
			{
				event.consume();
				sendBlockedDestinationMessage(itemName, option, chatMessageManager);
				log.debug("Blocked Max cape teleport to '{}'", option);
				return true;
			}
			return false;
		}

		// Conditional: House teleport tablet (check POH location VarBit)
		if (HOUSE_TABLET_IDS.contains(itemId))
		{
			return handleHouseTablet(event, client, itemName, chatMessageManager);
		}

		// All-destination-blocked items (from 03-01)
		if (BLOCKED_ITEM_IDS.contains(itemId) && isTeleportOption(option))
		{
			event.consume();
			sendBlockedMessage(itemName, itemId, chatMessageManager);
			log.debug("Blocked item teleport: '{}' (id: {}, option: '{}')", itemName, itemId, option);
			return true;
		}

		return false;
	}

	/**
	 * Handle Ring of Dueling teleport — allow Fortis Colosseum, block all other destinations.
	 * Ring of Dueling right-click options directly show destination names.
	 *
	 * @return true if the teleport was blocked
	 */
	private boolean handleRingOfDueling(MenuOptionClicked event, String option, String itemName,
		ChatMessageManager chatMessageManager)
	{
		if (ALLOWED_DUELING_DESTINATION.equals(option))
		{
			return false; // Fortis Colosseum is in Varlamore — allow
		}
		// Block Emir's Arena, Castle Wars, Ferox Enclave (and any future destination)
		event.consume();
		sendBlockedDestinationMessage(itemName, option, chatMessageManager);
		log.debug("Blocked Ring of Dueling teleport to '{}' (allowed: {})", option, ALLOWED_DUELING_DESTINATION);
		return true;
	}

	/**
	 * Handle Hunter cape (and Max cape Hunter destinations) — allow Hunter Guild, block others.
	 * Checks HUNTER_TELEPORT_OPTIONS to distinguish teleport entries from other item options.
	 *
	 * @return true if the teleport was blocked
	 */
	private boolean handleHunterCape(MenuOptionClicked event, String option, String itemName,
		ChatMessageManager chatMessageManager)
	{
		// Only process known teleport options (not Wear, Remove, Drop, Examine, etc.)
		if (!HUNTER_TELEPORT_OPTIONS.contains(option))
		{
			return false;
		}

		// Allow Hunter Guild (Varlamore destination)
		if (ALLOWED_HUNTER_DESTINATIONS.contains(option))
		{
			return false;
		}

		// Block non-Varlamore destinations (Feldip Hunter area, Carnivorous chinchompas)
		event.consume();
		sendBlockedDestinationMessage(itemName, option, chatMessageManager);
		log.debug("Blocked {} teleport to '{}' (allowed: Hunter Guild)", itemName, option);
		return true;
	}

	/**
	 * Handle House teleport tablet — allowed only when POH is in Aldarin (Varlamore).
	 * Uses VarBit 2187 to check POH location. Value 8 = Aldarin.
	 * Defaults to blocking if VarBit check is inconclusive (safe fallback).
	 *
	 * @return true if the teleport was blocked
	 */
	private boolean handleHouseTablet(MenuOptionClicked event, Client client, String itemName,
		ChatMessageManager chatMessageManager)
	{
		int houseLocation = client.getVarbitValue(HOUSE_LOCATION_VARBIT);
		if (houseLocation == ALDARIN_HOUSE_VALUE)
		{
			return false; // POH is in Aldarin (Varlamore) — allow
		}
		event.consume();
		sendBlockedDestinationMessage(itemName, "your house (outside Varlamore)", chatMessageManager);
		log.debug("Blocked house teleport tablet — POH location {} is not Aldarin ({})",
			houseLocation, ALDARIN_HOUSE_VALUE);
		return true;
	}

	/**
	 * Handle minigame grouping tab teleport attempts.
	 * Called for non-item-op events that may be minigame tab clicks.
	 * Minigame tab teleports use "Teleport" as the menu option with the minigame name as target.
	 *
	 * @return true if a minigame teleport was blocked
	 */
	public boolean handleMinigameTeleport(MenuOptionClicked event, ChatMessageManager chatMessageManager)
	{
		// Minigame tab teleports have "Teleport" as the option
		if (!"Teleport".equals(event.getMenuOption()))
		{
			return false;
		}

		String target = event.getMenuTarget().replaceAll("<[^>]*>", "").trim();
		if (BLOCKED_MINIGAME_DESTINATIONS.contains(target))
		{
			event.consume();
			sendBlockedMinigameMessage(target, chatMessageManager);
			log.debug("Blocked minigame teleport to '{}'", target);
			return true;
		}

		return false;
	}

	/**
	 * Check if the given menu option string indicates a teleport action.
	 * Matches explicit option names and any string containing "Teleport".
	 *
	 * @param option the menu option string
	 * @return true if the option is a teleport action
	 */
	private boolean isTeleportOption(String option)
	{
		return TELEPORT_OPTIONS.contains(option) || option.contains("Teleport");
	}

	/**
	 * Send a chat message informing the player that an item teleport was blocked.
	 * Looks up the destination string from ITEM_DESTINATION_DISPLAY and formats
	 * the message using Phase 2 Color.RED/WHITE format.
	 *
	 * @param itemName the name of the item
	 * @param itemId the item ID (used for destination lookup)
	 * @param chatMessageManager the chat message manager
	 */
	private void sendBlockedMessage(String itemName, int itemId, ChatMessageManager chatMessageManager)
	{
		String destDisplay = ITEM_DESTINATION_DISPLAY.getOrDefault(itemId, "outside Varlamore");
		String message = new ChatMessageBuilder()
			.append(Color.RED, "Varlamore UIM:")
			.append(Color.WHITE, " " + itemName + " teleport to " + destDisplay
				+ " is blocked \u2014 you are locked to Varlamore!")
			.build();

		chatMessageManager.queue(QueuedMessage.builder()
			.type(ChatMessageType.GAMEMESSAGE)
			.runeLiteFormattedMessage(message)
			.build());
	}

	/**
	 * Send a chat message for per-destination blocked teleports.
	 * Used when the option text IS the destination name (Ring of Dueling, Hunter/Max cape).
	 * Also used for house tablet with a descriptive destination string.
	 *
	 * @param itemName the name of the item
	 * @param destination the destination name (from menu option or custom string)
	 * @param chatMessageManager the chat message manager
	 */
	private void sendBlockedDestinationMessage(String itemName, String destination,
		ChatMessageManager chatMessageManager)
	{
		String message = new ChatMessageBuilder()
			.append(Color.RED, "Varlamore UIM:")
			.append(Color.WHITE, " " + itemName + " teleport to " + destination
				+ " is blocked \u2014 you are locked to Varlamore!")
			.build();

		chatMessageManager.queue(QueuedMessage.builder()
			.type(ChatMessageType.GAMEMESSAGE)
			.runeLiteFormattedMessage(message)
			.build());
	}

	/**
	 * Send a chat message informing the player that a minigame grouping tab teleport was blocked.
	 *
	 * @param destination the minigame destination name
	 * @param chatMessageManager the chat message manager
	 */
	private void sendBlockedMinigameMessage(String destination, ChatMessageManager chatMessageManager)
	{
		String message = new ChatMessageBuilder()
			.append(Color.RED, "Varlamore UIM:")
			.append(Color.WHITE, " Minigame teleport to " + destination
				+ " is blocked \u2014 you are locked to Varlamore!")
			.build();

		chatMessageManager.queue(QueuedMessage.builder()
			.type(ChatMessageType.GAMEMESSAGE)
			.runeLiteFormattedMessage(message)
			.build());
	}
}
