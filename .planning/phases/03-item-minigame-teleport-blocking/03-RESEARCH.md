# Phase 3: Item & Minigame Teleport Blocking - Research

**Researched:** 2026-02-17
**Domain:** RuneLite plugin event handling, OSRS item system, inventory interaction, minigame grouping tab
**Confidence:** HIGH

<user_constraints>
## User Constraints (from CONTEXT.md)

### Locked Decisions

#### Blocking feedback
- Show a chat message on blocked item teleports, consistent with Phase 2 spell blocking
- Message includes the specific item name and destination: e.g., "Ring of Dueling teleport to Duel Arena is blocked — you are locked to Varlamore"
- Same color/style as Phase 2 spell block messages — unified blocking system feel
- Menu options (Rub, Break, etc.) remain visible; block happens on action interception, not menu removal

#### Multi-destination items
- Block only outside-Varlamore destinations; allow Varlamore destinations on the same item
- Known Varlamore destinations to allow:
  - Ring of Dueling → Fortis Colosseum
  - Pendant of Ates → All destinations (whitelist entirely, skip checks)
  - Hunter cape / Max cape → Hunter Guild
- All other listed items (glory, games necklace, skills necklace, combat bracelet, wealth, burning amulet, passage, digsite pendant, slayer ring, tablets, quest items, diary gear) teleport exclusively outside Varlamore
- Block must happen before charge consumption / item destruction — preserve the item
- House teleport tablets: block unless player's house is located in Varlamore (Aldarin)

#### Minigame grouping tab teleports
- Block all minigame teleports from the grouping tab — no minigame teleport destinations are inside Varlamore
- Same feedback style as items: chat message naming the specific minigame destination
- This is separate from item-based minigame access (e.g., Ring of Dueling → Colosseum is an item teleport, not a grouping tab teleport)

### Claude's Discretion
- Event interception mechanism (MenuOptionClicked vs other hooks)
- Data structure for item/destination mapping
- How to detect POH location for house teleport tab logic
- Exact categorization of items into implementation groups (jewelry vs tablets vs quest items)

### Deferred Ideas (OUT OF SCOPE)

None — discussion stayed within phase scope
</user_constraints>

<phase_requirements>
## Phase Requirements

| ID | Description | Research Support |
|----|-------------|-----------------|
| ITEM-01 | User cannot use jewelry teleports outside Varlamore (Amulet of Glory, Ring of Dueling, Games Necklace, Skills Necklace, Combat Bracelet, Ring of Wealth, Burning Amulet, Necklace of Passage, Digsite Pendant, Slayer Ring) | MenuOptionClicked + getItemId() + option name matching; Ring of Dueling needs per-destination logic; Pendant of Ates whitelisted |
| ITEM-02 | User cannot use teleport tablets that lead outside Varlamore (Varrock, Lumbridge, Falador, Camelot, Ardougne, Watchtower, House tab if POH outside) | Same event + item ID set; House tab requires POH location check via getVarbitValue() |
| ITEM-03 | User cannot use quest teleport items leading outside Varlamore (Ectophial, Xeric's Talisman, Kharedst's Memoirs, Pharaoh's Sceptre, Skull Sceptre, Enchanted Lyre, Drakan's Medallion) | Same event + item ID set; all go exclusively outside Varlamore so no per-destination logic needed |
| ITEM-04 | User cannot use achievement diary gear teleports outside Varlamore (Karamja Gloves, Explorer's Ring, Ardougne Cloak, Morytania Legs, Desert Amulet, Wilderness Sword, Rada's Blessing) | Same event + item ID set; Hunter cape/Max cape need per-option logic (Hunter Guild = allow) |
| ITEM-05 | Blocked item teleport attempts are silently prevented (action consumed) | event.consume() is the correct prevention mechanism; block BEFORE any game processing runs |
</phase_requirements>

## Summary

Phase 3 extends the MenuOptionClicked interception pattern established in Phase 2 to cover item-based teleports and minigame grouping tab teleports. The same event that fires for spell casts also fires for inventory item option clicks, making the implementation a natural extension of the existing SpellTeleportBlocker service.

The key difference from Phase 2 is the identification mechanism: instead of matching spell names from `getMenuTarget()`, item blocking uses `getItemId()` to identify the item and `getMenuOption()` to identify which teleport option was selected. For most items, all teleport options go outside Varlamore and the item ID alone is sufficient to block. A small number of items (Ring of Dueling, Hunter cape/Max cape) have mixed destinations and require per-option matching.

The most technically complex aspects are: (1) the Ring of Dueling's per-destination logic where "Fortis Colosseum" must be allowed while other options are blocked; (2) house teleport tab detection where a VarBit lookup determines if the player's POH is in Aldarin (the only Varlamore POH location); and (3) minigame grouping tab blocking which uses a different interface ID than the inventory.

**Primary recommendation:** Create a new `ItemTeleportBlocker` service parallel to `SpellTeleportBlocker`. Use `isItemOp()` + `getItemId()` for item identification, option name matching for per-destination logic, and `client.getVarbitValue()` for POH location detection. Wire into the existing `onMenuOptionClicked` handler with a parallel config toggle.

## Standard Stack

### Core
| Library | Version | Purpose | Why Standard |
|---------|---------|---------|--------------|
| RuneLite API | latest.release | MenuOptionClicked, MenuEntry, Widget | Core plugin framework, all needed APIs present |
| Google Guice | (via RuneLite) | Dependency injection for Client | Already used in plugin for Client injection |
| Lombok | 1.18.30 | @Slf4j logging | Already in project, consistent with Phase 2 |

### Supporting
| Library | Version | Purpose | When to Use |
|---------|---------|---------|-------------|
| ChatMessageManager | (RuneLite client) | Send chat feedback | Required — same as Phase 2 pattern |
| Client | (RuneLite API) | getVarbitValue() for POH location | Required for house tab blocking logic |

### Alternatives Considered
| Instead of | Could Use | Tradeoff |
|------------|-----------|----------|
| isItemOp() + getItemId() | Widget interface ID check | isItemOp() is the intended API for item ops; widget check is redundant |
| VarBit lookup for POH | WorldPoint coordinate check | VarBit is definitive; coordinate check requires knowing world location which changes per region |
| Per-item option name matching | Widget param0/param1 index | Option name is readable and stable; param indices are undocumented and brittle |

**Installation:**
```bash
# No additional dependencies needed - all provided by RuneLite
```

## Architecture Patterns

### Recommended Project Structure
```
src/main/java/com/varlamoreuim/
├── VarlamoreUimPlugin.java          # Main plugin, event subscribers
├── BoundaryChecker.java             # Existing boundary service
├── VarlamoreUimConfig.java          # Config options (add blockItemTeleports toggle)
├── VarlamoreUimPanel.java           # UI panel
└── teleport/
    ├── SpellTeleportBlocker.java    # Phase 2 - existing
    └── ItemTeleportBlocker.java     # Phase 3 - new
```

### Pattern 1: Item Operation Identification
**What:** Use `event.isItemOp()` to confirm the click is an item operation (not walk/examine/use), then `event.getItemId()` to get the item
**When to use:** All inventory item option blocking

**Example:**
```java
// Source: RuneLite API - MenuOptionClicked javadoc
@Subscribe
public void onMenuOptionClicked(MenuOptionClicked event)
{
    if (!event.isItemOp())
    {
        return; // Not an item operation (filters walk, NPC clicks, spell casts, etc.)
    }

    int itemId = event.getItemId(); // The item in the inventory slot clicked
    String option = event.getMenuOption(); // e.g., "Rub", "Teleport", "Break", "Wear"
    String target = event.getMenuTarget().replaceAll("<[^>]*>", "").trim(); // Item name

    // Check if this item+option combination is a blocked teleport
}
```

**Key insight:** `isItemOp()` returns false for "Use" and "Examine" actions — these are not considered item ops. The item teleport options (Rub, Teleport, Break, Tele) are item ops and return true.

### Pattern 2: Item ID-Based Blocking with Set Lookup
**What:** Maintain an immutable Set of blocked item IDs; O(1) lookup to block all-destination-blocked items
**When to use:** Items where ALL teleport options go outside Varlamore (most items)

**Example:**
```java
// All item IDs that teleport exclusively outside Varlamore
private static final Set<Integer> BLOCKED_ITEM_IDS = Set.of(
    // Amulet of Glory variants (all charges)
    ItemID.AMULET_OF_GLORY,
    ItemID.AMULET_OF_GLORY_1,
    ItemID.AMULET_OF_GLORY_2,
    ItemID.AMULET_OF_GLORY_3,
    ItemID.AMULET_OF_GLORY_4,
    // ... all variants
);

// In handleMenuClick:
if (BLOCKED_ITEM_IDS.contains(itemId))
{
    // All teleport options on this item go outside Varlamore
    // Block any teleport option (Rub, Teleport, etc.)
    if (isTeleportOption(option))
    {
        event.consume();
        sendBlockedMessage(target, option);
        return true;
    }
}
```

### Pattern 3: Per-Destination Blocking (Ring of Dueling Pattern)
**What:** Check the specific menu option text to allow some destinations and block others
**When to use:** Items with mixed destinations (Ring of Dueling, Hunter cape/Max cape)

**Example:**
```java
// Ring of Dueling: allow Fortis Colosseum, block everything else
private static final Set<Integer> RING_OF_DUELING_IDS = Set.of(
    ItemID.RING_OF_DUELING_1,
    ItemID.RING_OF_DUELING_2,
    // ... all 8 variants
);
private static final String COLOSSEUM_OPTION = "Fortis Colosseum";

// In handleMenuClick after isItemOp() check:
if (RING_OF_DUELING_IDS.contains(itemId))
{
    String option = event.getMenuOption();
    if (!COLOSSEUM_OPTION.equals(option)) // Block everything except Colosseum
    {
        event.consume();
        sendBlockedMessage("Ring of Dueling", option);
        return true;
    }
}
```

**Note:** Ring of Dueling right-click options appear as the destination names directly:
"Emir's Arena", "Castle Wars", "Ferox Enclave", "Fortis Colosseum" — these are the exact option strings to match.

### Pattern 4: House Teleport Tab POH Location Detection
**What:** Read the POH location VarBit to determine if the player's house is in Aldarin (Varlamore)
**When to use:** House teleport tablet blocking (ITEM-02)

The key finding: **Varbit 9449** stores POH location when the house advertisement noticeboard is open — this is NOT reliable for persistent detection. The actual POH location VarBit used persistently is not exposed in RuneLite's `Varbits.java` constants file under a named constant.

**Recommended approach:** Since house teleport tablets block the ENTIRE teleport in Phase 2 discussion (and Aldarin at Construction 35 is the only Varlamore POH location), the simplest approach is:

```java
// House teleport tablets - block unless POH is in Aldarin
// POH location can be determined by checking VarBit 2187 (house location)
// Values: 0=Rimmington, 1=Taverley, 2=Pollnivneach, 3=Rellekka, 4=Brimhaven,
//         5=Yanille, 6=Hosidius, 7=Prifddinas, 8=Aldarin
// Note: Exact varbit ID needs verification via RuneLite DevTools Var Inspector
private static final int HOUSE_LOCATION_VARBIT = 2187; // VERIFY WITH DEVTOOLS
private static final int ALDARIN_HOUSE_LOCATION = 8;   // VERIFY WITH DEVTOOLS

// In handleMenuClick for house tablet:
int houseLocation = client.getVarbitValue(HOUSE_LOCATION_VARBIT);
if (houseLocation != ALDARIN_HOUSE_LOCATION)
{
    event.consume();
    sendBlockedMessage("Teleport to house", "House tab");
    return true;
}
// else: POH is in Aldarin (Varlamore) - allow
```

**IMPORTANT:** The exact VarBit ID and value for Aldarin is **LOW confidence** — it must be verified in-game using RuneLite DevTools Var Inspector. The planner should make this a required validation step before relying on specific values.

**Alternative approach (simpler, safe):** If VarBit detection proves unreliable or complex, block house teleport tablets entirely and document it as a known limitation. Players with POH in Aldarin can use the spell teleport "Teleport to House" instead (which is outside Phase 2/3 scope). This is a safe default.

### Pattern 5: Minigame Grouping Tab Blocking
**What:** Intercept teleport clicks from the grouping tab interface, which uses a different widget/interface than the inventory
**When to use:** Blocking all 17 minigame teleports

Minigame grouping tab teleports fire `MenuOptionClicked` events, but they come from a widget in the grouping tab interface, NOT from the inventory. The menu option text for grouping tab teleports is typically "Teleport" and the target is the minigame name.

```java
// Minigame grouping teleports:
// - MenuAction: CC_OP (widget click)
// - MenuOption: "Teleport"
// - MenuTarget: minigame name (e.g., "Barbarian Assault", "Castle Wars")
// - Widget: grouping tab widget, NOT inventory

// The interface ID for grouping tab:
// WidgetID.MINIGAME_TAB_GROUP_ID or similar - verify with DevTools
// One approach: check if option is "Teleport" and the widget is NOT inventory
```

**Key distinction from item teleports:**
- Item teleports: `isItemOp()` returns true
- Minigame teleports: `isItemOp()` returns false (they're widget button clicks, not item ops)

Use `isItemOp()` = false AND option = "Teleport" AND known minigame name OR widget interface ID check to filter grouping tab teleports.

**Recommended approach for minigame blocking:**

```java
private static final Set<String> BLOCKED_MINIGAME_NAMES = Set.of(
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

// Check for grouping tab teleport:
if (!event.isItemOp() && "Teleport".equals(event.getMenuOption()))
{
    String target = event.getMenuTarget().replaceAll("<[^>]*>", "").trim();
    if (BLOCKED_MINIGAME_NAMES.contains(target))
    {
        event.consume();
        sendBlockedMinigameMessage(target);
        return true;
    }
}
```

**Caveat:** The minigame name matching approach may be fragile if target text includes additional formatting. Widget interface ID verification via DevTools is recommended as a secondary check.

### Anti-Patterns to Avoid
- **Blocking menu display:** Per user decision, menu options remain visible; block happens on click, not by removing menu entries. Do NOT use `onMenuEntryAdded` to remove entries.
- **Blocking before isItemOp check:** Spell casts also fire MenuOptionClicked. Without `isItemOp()` check, item blocking code may incorrectly inspect spell events.
- **Forgetting charge variants:** Many items come in multiple charge variants (Glory(1) through Glory(4+uncharged)). ALL variants must be in the blocked set or items will not be blocked when partially used.
- **Using param0/param1 for option identification:** These parameters are undocumented for item ops and may change with game updates. Use `getMenuOption()` string matching instead.
- **Assuming getItemId() returns base item ID:** `getItemId()` returns the exact item ID including charge variant. Either list all variants explicitly or use `ItemVariationMapping.map(itemId)` to get base ID.

## Don't Hand-Roll

| Problem | Don't Build | Use Instead | Why |
|---------|-------------|-------------|-----|
| Item variant normalization | Custom charge-stripping logic | `ItemVariationMapping.map(itemId)` (if needed) | RuneLite provides this utility; handles edge cases |
| Chat messages | Custom packet injection | ChatMessageManager (same as Phase 2) | Already established pattern |
| Event system | Custom hooks | RuneLite @Subscribe events | Built-in, tested, maintained |
| VarBit reading | Polling game state | `client.getVarbitValue(id)` | Direct API call, updated each tick automatically |

**Key insight:** The Phase 2 `SpellTeleportBlocker` pattern is nearly identical to what Phase 3 needs. The primary difference is `isItemOp()` + `getItemId()` replaces the spell name check. Reuse the same message format and event consumption approach.

## Common Pitfalls

### Pitfall 1: Missing Item Charge Variants
**What goes wrong:** Ring of Dueling(8) is blocked but Ring of Dueling(3) is not (different item IDs)
**Why it happens:** Each charge count is a distinct item ID in OSRS
**How to avoid:** Include ALL charge variants in blocked sets. For Ring of Dueling: item IDs 2552 (8), 2554 (7), 2556 (6), 2558 (5), 2560 (4), 2562 (3), 2564 (2), 2566 (1). For Glory: multiple IDs for each charge level plus uncharged variants.
**Warning signs:** Item blocked at 8 charges but not at 3 charges

### Pitfall 2: getItemId() Returns -1 for Non-Item Events
**What goes wrong:** Plugin crashes or incorrectly identifies events when getItemId() is called on non-item events
**Why it happens:** `getItemId()` only returns a valid ID for item op events; documentation says "if this menu entry is an item op"
**How to avoid:** Always call `isItemOp()` before calling `getItemId()`. Never skip this guard.
**Warning signs:** NullPointerExceptions or -1 item IDs matching nothing (benign but wasteful without guard)

### Pitfall 3: Ring of Dueling Option Name Mismatch
**What goes wrong:** "Fortis Colosseum" option is blocked instead of allowed, or blocked options use wrong names
**Why it happens:** Option text in MenuOptionClicked must match exactly; OSRS may use slightly different formatting than the wiki
**How to avoid:** Log all option names during testing to verify exact strings before finalizing the whitelist. The option names are the destination names shown in the right-click menu: "Emir's Arena", "Castle Wars", "Ferox Enclave", "Fortis Colosseum"
**Warning signs:** Colosseum option being incorrectly blocked

### Pitfall 4: House Teleport VarBit Uncertainty
**What goes wrong:** Plugin uses wrong VarBit ID or wrong value for Aldarin, either always blocking or never blocking house tabs correctly
**Why it happens:** No named RuneLite Varbits.java constant exists for POH location; ID must be found via DevTools
**How to avoid:** Use RuneLite DevTools Var Inspector: (1) open DevTools, (2) select Var Inspector, (3) move your house to Aldarin and observe which var changes. Alternatively, fall back to always-block approach for house tabs as safe default.
**Warning signs:** House tab blocked even when POH is in Aldarin, or not blocked when POH is elsewhere

### Pitfall 5: Minigame Tab Teleport Detection Ambiguity
**What goes wrong:** "Teleport" option exists in other contexts (not minigame tab), causing false positives
**Why it happens:** The "Teleport" menu option string is used by multiple interfaces
**How to avoid:** Combine option name check with minigame name set AND/OR widget interface ID check. The widget for grouping tab teleports has a specific interface ID that can be checked via `event.getWidget().getId() >>> 16`.
**Warning signs:** Non-minigame teleports being blocked, or minigame teleports not blocked

### Pitfall 6: Pendant of Ates Not Whitelisted
**What goes wrong:** Pendant of Ates teleports are blocked, trapping players
**Why it happens:** Plugin treats all jewelry items with "Rub" option as potential blocks
**How to avoid:** Explicitly check for Pendant of Ates item IDs before any blocking logic and return early (whitelist entirely). This item's IDs must be in a whitelist, not the blocked set.
**Warning signs:** Player cannot use Pendant of Ates to travel within Varlamore

### Pitfall 7: Blocking Non-Teleport Options on Multi-Function Items
**What goes wrong:** Slayer ring's "Master" option (contact slayer master) gets blocked instead of just "Rub"/"Teleport"
**Why it happens:** isItemOp() returns true for ALL right-click options on items, not just teleport ones
**How to avoid:** Always check `getMenuOption()` to verify the click is a teleport action ("Rub", "Teleport", "Break") before blocking. Do not block based on item ID alone without verifying the option is teleport-related.
**Warning signs:** Non-teleport item functions blocked (examine, drop, wear, use, check)

## Code Examples

Verified patterns from official sources and Phase 2 implementation:

### Core Item Teleport Check Pattern
```java
// Source: RuneLite API - MenuOptionClicked.isItemOp(), getItemId(), getMenuOption()
// Extended from Phase 2 SpellTeleportBlocker pattern

public boolean handleMenuClick(MenuOptionClicked event, ChatMessageManager chatMessageManager, Client client)
{
    // Guard: only process item operations (not spells, walks, NPC clicks)
    if (!event.isItemOp())
    {
        // Note: minigame grouping tab teleports also land here — handle separately
        return handleMinigameTeleport(event, chatMessageManager);
    }

    int itemId = event.getItemId();
    String option = event.getMenuOption();
    String itemName = event.getMenuTarget().replaceAll("<[^>]*>", "").trim();

    // Early exit: Pendant of Ates is always allowed (all destinations Varlamore)
    if (PENDANT_OF_ATES_IDS.contains(itemId))
    {
        return false;
    }

    // Ring of Dueling: per-destination blocking
    if (RING_OF_DUELING_IDS.contains(itemId))
    {
        return handleRingOfDueling(event, option, chatMessageManager);
    }

    // Hunter cape / Max cape: allow Hunter Guild, block others
    if (HUNTER_CAPE_IDS.contains(itemId) || MAX_CAPE_IDS.contains(itemId))
    {
        return handleHunterCape(event, option, chatMessageManager);
    }

    // House teleport tablet: check POH location
    if (HOUSE_TABLET_IDS.contains(itemId))
    {
        return handleHouseTablet(event, client, chatMessageManager, itemName);
    }

    // All other blocked items: any teleport option on these items is blocked
    if (BLOCKED_ITEM_IDS.contains(itemId) && isTeleportOption(option))
    {
        event.consume();
        sendBlockedMessage(itemName, option, chatMessageManager);
        return true;
    }

    return false;
}

private boolean isTeleportOption(String option)
{
    // Teleport option names used by different item categories:
    // Jewelry: "Rub" (ring/amulet) or destination name directly
    // Tablets: "Break"
    // Quest items: "Rub", "Activate", etc.
    // Diary gear: "Teleport", destination names
    return "Rub".equals(option)
        || "Break".equals(option)
        || "Teleport".equals(option)
        || option.contains("Teleport"); // catches "Teleport to POH" etc.
}
```

### Chat Message Format (Phase 2 Pattern - Reuse Exactly)
```java
// Source: SpellTeleportBlocker.java (Phase 2 implementation)
// Reuse this EXACT format for unified blocking system feel

private void sendBlockedMessage(String itemName, String destination, ChatMessageManager chatMessageManager)
{
    String message = new ChatMessageBuilder()
        .append(Color.RED, "Varlamore UIM:")
        .append(Color.WHITE, " " + itemName + " teleport to " + destination
            + " is blocked — you are locked to Varlamore")
        .build();

    chatMessageManager.queue(QueuedMessage.builder()
        .type(ChatMessageType.GAMEMESSAGE)
        .runeLiteFormattedMessage(message)
        .build());
}
```

### House Teleport Tab Detection
```java
// Source: Derived from OSRS Wiki VarBit research
// WARNING: HOUSE_LOCATION_VARBIT value MUST be verified via DevTools before use

// DevTools verification steps:
// 1. Open RuneLite DevTools (Ctrl+Shift+Alt+D)
// 2. Select "Var Inspector" tab
// 3. Move house to Rimmington (base state), note varbit values
// 4. Move house to Aldarin, observe which varbit changes and to what value
// 5. Record: varbit ID and Aldarin value

private static final int HOUSE_LOCATION_VARBIT = 2187; // NEEDS DEVTOOLS VERIFICATION
private static final int ALDARIN_VALUE = 8;             // NEEDS DEVTOOLS VERIFICATION

private boolean isHouseInVarlamore(Client client)
{
    int houseLocation = client.getVarbitValue(HOUSE_LOCATION_VARBIT);
    return houseLocation == ALDARIN_VALUE;
}

private boolean handleHouseTablet(MenuOptionClicked event, Client client,
    ChatMessageManager chatMessageManager, String itemName)
{
    if (!isHouseInVarlamore(client))
    {
        event.consume();
        sendBlockedMessage(itemName, "your house (outside Varlamore)", chatMessageManager);
        return true;
    }
    return false; // POH is in Aldarin - allow
}
```

### Minigame Grouping Tab Blocking
```java
// Source: OSRS Wiki Grouping page + MenuOptionClicked API
// All minigame destinations are outside Varlamore

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

private boolean handleMinigameTeleport(MenuOptionClicked event, ChatMessageManager chatMessageManager)
{
    // Minigame tab teleports are NOT item ops, but they use "Teleport" option
    if (!"Teleport".equals(event.getMenuOption()))
    {
        return false;
    }

    String target = event.getMenuTarget().replaceAll("<[^>]*>", "").trim();
    if (BLOCKED_MINIGAME_DESTINATIONS.contains(target))
    {
        event.consume();
        sendBlockedMinigameMessage(target, chatMessageManager);
        return true;
    }

    return false;
}
```

## Complete Item Catalog

### ITEM-01: Jewelry Teleports

All jewelry items and their teleport options (all destinations outside Varlamore unless noted):

**Amulet of Glory** — Edgeville, Karamja, Draynor Village, Al Kharid
- Item IDs: Multiple variants for each charge level (0-6, uncharged, t versions)
- Option: "Rub"
- Block: ALL destinations

**Ring of Dueling** — Emir's Arena, Castle Wars, Ferox Enclave, Fortis Colosseum
- Item IDs: 2552 (8) through 2566 (1)
- Option: Destination name directly (e.g., "Emir's Arena")
- Block: Emir's Arena, Castle Wars, Ferox Enclave
- ALLOW: Fortis Colosseum (Varlamore)

**Games Necklace** — Burthorpe, Barbarian Outpost, Corporeal Beast, Tears of Guthix, Wintertodt Camp
- Item IDs: Multiple charge variants
- Option: "Rub"
- Block: ALL destinations

**Skills Necklace** — Fishing Guild, Mining Guild, Crafting Guild, Cooking Guild, Woodcutting Guild, Farming Guild
- Item IDs: Multiple charge variants
- Option: "Rub"
- Block: ALL destinations

**Combat Bracelet** — Warriors' Guild, Champions' Guild, Edgeville Monastery, Ranging Guild
- Item IDs: Multiple charge variants
- Option: "Rub"
- Block: ALL destinations

**Ring of Wealth** — Grand Exchange, Falador, Miscellania, Dondakan's Rock
- Item IDs: Multiple charge variants
- Option: Destination names or "Rub"
- Block: ALL destinations

**Burning Amulet** — Chaos Temple, Bandit Camp, Lava Maze
- Item IDs: Multiple charge variants
- Option: "Rub"
- Block: ALL destinations

**Necklace of Passage** — Wizards' Tower, Outpost, Eagle's Eyrie
- Item IDs: Multiple charge variants
- Option: "Rub"
- Block: ALL destinations

**Digsite Pendant** — Digsite, Fossil Island Volcano, Lithkren Vault
- Item IDs: Multiple charge variants
- Option: "Rub"
- Block: ALL destinations

**Slayer Ring** — Stronghold Slayer Cave, Slayer Tower, Fremennik Slayer Dungeon, Tarn's Lair, Dark Beasts, Haunted Mine
- Item IDs: Multiple charge variants
- Options: "Rub" or "Teleport" (depends on equipped/inventory state)
- Block: ALL destinations

**Pendant of Ates** — All destinations in Varlamore (Darkfrost, Twilight Temple, Ralos' Rise, North Aldarin, North of Kastori, Nemus Retreat)
- WHITELIST ENTIRELY: Skip all checks, always allow

### ITEM-02: Teleport Tablets

**Varrock Tablet, Lumbridge Tablet, Falador Tablet, Camelot Tablet, Ardougne Tablet, Watchtower Tablet**
- Option: "Break"
- Block: ALL (all go outside Varlamore)

**House Teleport Tablet (Teleport to House)**
- Option: "Break"
- Block: CONDITIONAL — block unless POH is in Aldarin (requires VarBit check)

**Scroll of Redirection** (redirected house tabs) — same conditional logic as house tab

### ITEM-03: Quest Teleport Items

**Ectophial** — Ectofuntus (Morytania)
- Block ALL

**Xeric's Talisman** — Xeric's Lookout, Glade, Inferno, Heart, Honour (all Great Kourend)
- Block ALL (none in Varlamore)

**Kharedst's Memoirs** — Multiple Great Kourend locations
- Block ALL

**Pharaoh's Sceptre** — Pyramid locations (Desert)
- Block ALL

**Skull Sceptre** — Barbarian Village
- Block ALL

**Enchanted Lyre** — Rellekka
- Block ALL

**Drakan's Medallion** — Ver Sinhaza, Darkmeyer, Slepe
- Block ALL

### ITEM-04: Achievement Diary Gear

**Karamja Gloves** (tiers 1-4) — Shilo Village mine
- Block ALL

**Explorer's Ring** (tiers 2-4) — Cabbage patch near Falador
- Block ALL

**Ardougne Cloak** (tiers 1-4) — Ardougne Monastery
- Block ALL

**Morytania Legs** (tiers 3-4) — Burgh de Rott
- Block ALL

**Desert Amulet** (tiers 4) — Nardah
- Block ALL

**Wilderness Sword** (tiers 2-4) — Edgeville
- Block ALL

**Rada's Blessing** (tiers 3-4) — Kourend Woodland, Mount Karuulm
- Block ALL

**Hunter cape / Max cape** — Hunter Guild (Varlamore), Feldip Hunter area, Wilderness chinchompas
- ALLOW: Hunter Guild option
- Block: Feldip Hunter area, Wilderness options

### ITEM-05: Minigame Grouping Tab (17 destinations)
All blocked:
Barbarian Assault, Burthorpe Games Room, Castle Wars, Clan Wars, Fishing Trawler, Giants' Foundry, Guardians of the Rift, Last Man Standing, Mage Training Arena, Nightmare Zone, Pest Control, Rat Pits, Shades of Mort'ton, Soul Wars, Tithe Farm, Trouble Brewing, TzHaar Fight Pit

## State of the Art

| Old Approach | Current Approach | When Changed | Impact |
|--------------|------------------|--------------|--------|
| ITEM_FIRST_OPTION/ITEM_SECOND_OPTION MenuAction | CC_OP + isItemOp() | RuneLite API evolution | Old constants deprecated; use isItemOp() API |
| WidgetInfo enum for specific items | InterfaceID + dynamic widget access | ~2022-2023 | WidgetInfo partially deprecated |
| MenuAction enum type checks | isItemOp() convenience method | Recent API | Cleaner and more semantic; handles edge cases |

**Deprecated/outdated:**
- `MenuAction.ITEM_FIRST_OPTION` through `ITEM_FIFTH_OPTION`: Still exist but superseded by CC_OP + isItemOp() pattern
- `WidgetInfo` constants for specific item interfaces: Use `InterfaceID` and programmatic checks instead

## Open Questions

1. **Exact POH location VarBit ID and Aldarin value**
   - What we know: VarBit 9449 tracks house advertisement noticeboard location (not persistent POH location); no named Varbits constant found; Aldarin requires Construction 35
   - What's unclear: The persistent varbit that stores where the player's house actually is (not the noticeboard context)
   - Recommendation: **MUST verify via RuneLite DevTools Var Inspector before implementing.** Planner should make this an explicit investigation step. As a safe fallback, always block house tabs.

2. **Exact Ring of Dueling option strings for right-click menu**
   - What we know: Destinations are Emir's Arena, Castle Wars, Ferox Enclave, Fortis Colosseum (from wiki)
   - What's unclear: Exact string in MenuOptionClicked.getMenuOption() — may differ from wiki display name
   - Recommendation: Verify option strings in-game with DevTools/logging before finalizing whitelist check

3. **Hunter Cape / Max Cape option strings**
   - What we know: Options include "Feldip Hunter area", "Wilderness chinchompas" (or similar), "Hunter Guild"
   - What's unclear: Exact menu option strings for the three destinations; max cape may use sub-menus
   - Recommendation: Verify in-game; if max cape uses RUNELITE_SUBMENU_WIDGET, different handling may be needed (see RuneLite PR #18149)

4. **Minigame grouping tab widget interface ID**
   - What we know: Grouping tab fires CC_OP events with "Teleport" option and minigame name as target
   - What's unclear: Whether additional widget ID check is needed to avoid false positives with other "Teleport" options in other contexts
   - Recommendation: Start with option name + target name matching; add widget ID check only if false positives are observed in testing

5. **Amulet of Glory and Glory(t) variant IDs**
   - What we know: Basic IDs 1704/1706/1708/1710/1712; trimmed versions have different IDs
   - What's unclear: Complete list of all Glory variants (including amulet of eternal glory, Amulet of Fury, etc. — are they in scope?)
   - Recommendation: Focus on "Amulet of Glory" variants; check if Amulet of Eternal Glory has same options and block it too. Use OSRS Wiki item ID tables for authoritative list.

## Sources

### Primary (HIGH confidence)
- [MenuOptionClicked API](https://github.com/runelite/runelite/blob/master/runelite-api/src/main/java/net/runelite/api/events/MenuOptionClicked.java) - isItemOp(), getItemId(), getMenuOption(), getMenuTarget(), consume()
- [RuneLite API Javadoc - MenuOptionClicked](https://static.runelite.net/runelite-api/apidocs/net/runelite/api/events/MenuOptionClicked.html) - All method signatures and documentation
- [MenuEntrySwapperPlugin.java](https://github.com/runelite/runelite/blob/master/runelite-client/src/main/java/net/runelite/client/plugins/menuentryswapper/MenuEntrySwapperPlugin.java) - Item teleport option detection pattern using CC_OP and getItemId()
- [SpellTeleportBlocker.java (Phase 2)](../02-spell-teleport-blocking/02-01-SUMMARY.md) - Established chat message pattern with Color.RED/WHITE
- [OSRS Wiki: Varlamore transportation](https://oldschool.runescape.wiki/w/Varlamore) - Authoritative list of Varlamore-internal teleport destinations

### Secondary (MEDIUM confidence)
- [OSRS Wiki: Ring of Dueling](https://oldschool.runescape.wiki/w/Ring_of_dueling) - Destination list including Fortis Colosseum
- [OSRS Wiki: Pendant of Ates](https://oldschool.runescape.wiki/w/Pendant_of_Ates) - All destinations confirmed within Varlamore
- [OSRS Wiki: Grouping](https://oldschool.runescape.wiki/w/Grouping) - 17 minigame teleport destinations, none in Varlamore
- [OSRS Wiki: Player-owned house](https://oldschool.runescape.wiki/w/Player-owned_house) - Aldarin is the only Varlamore POH location (Construction 35)
- [OSRS Wiki: Hunter cape](https://oldschool.runescape.wiki/w/Hunter_cape) - Hunter Guild (Varlamore) confirmed as one of three destinations
- [OSRS Wiki: Slayer ring](https://oldschool.runescape.wiki/w/Slayer_ring) - All destinations outside Varlamore confirmed
- [OSRS Wiki: Xeric's Talisman](https://oldschool.runescape.wiki/w/Xeric%27s_talisman) - All destinations in Great Kourend, none in Varlamore
- [OSRS Wiki: Drakan's Medallion](https://oldschool.runescape.wiki/w/Drakan%27s_medallion) - All destinations in Morytania
- [OSRS Wiki: Kharedst's Memoirs](https://oldschool.runescape.wiki/w/Kharedst%27s_memoirs) - All destinations in Great Kourend
- [RuneLite PR #18149: Max cape menu entries](https://github.com/runelite/runelite/pull/18149/files) - Cape teleport option detection patterns
- [OSRS Wiki VarBit 9449](https://oldschool.runescape.wiki/w/RuneScape:Varbit/9449) - House advertisement noticeboard VarBit (context-only, not persistent)

### Tertiary (LOW confidence - requires validation)
- VarBit ID for persistent POH location: Not found in RuneLite Varbits.java constants; must be verified via DevTools
- Exact option strings for Ring of Dueling right-click menu: Assumed to match wiki destination names; must verify in-game
- Exact option strings for Hunter cape / Max cape: Must verify in-game
- Minigame target string exact formatting: Must verify in-game to confirm no color tags or prefixes

## Metadata

**Confidence breakdown:**
- Standard stack: HIGH — Same RuneLite API as Phase 2; event interception pattern proven
- Architecture: HIGH — ItemTeleportBlocker mirrors SpellTeleportBlocker structure exactly
- Item catalog (destinations): HIGH — Verified from OSRS Wiki for all listed items
- API patterns (isItemOp, getItemId): HIGH — Documented in RuneLite API Javadoc
- POH VarBit detection: LOW — No named constant found; exact ID requires DevTools verification
- Option strings (Ring of Dueling, Hunter cape): MEDIUM — Wiki names likely correct but need in-game verification
- Minigame tab blocking: MEDIUM — Event type behavior deduced from API; widget ID needs verification

**Research date:** 2026-02-17
**Valid until:** 30 days (stable RuneLite API; item destination data is fixed game content)

**Critical verification steps before implementation:**
1. Verify POH location VarBit ID and Aldarin value via RuneLite DevTools Var Inspector
2. Verify Ring of Dueling right-click option strings (especially "Fortis Colosseum" exact text)
3. Verify Hunter cape / Max cape menu option strings for Hunter Guild vs other destinations
4. Log all minigame tab teleport events to confirm target name format
5. Compile complete item ID list including ALL charge variants for each jewelry/tablet item
