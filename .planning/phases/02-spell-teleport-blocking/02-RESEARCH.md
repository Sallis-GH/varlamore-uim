# Phase 2: Spell Teleport Blocking - Research

**Researched:** 2026-02-16
**Domain:** RuneLite plugin event handling, OSRS spell system, menu interception
**Confidence:** HIGH

## Summary

Phase 2 requires blocking all spellbook teleports that would take the player outside Varlamore. RuneLite provides the `MenuOptionClicked` event for intercepting player actions before they execute. The standard approach is to:

1. Subscribe to `MenuOptionClicked` events
2. Check if the menu option is "Cast" and the widget group ID matches the spellbook
3. Parse the spell name from `getMenuTarget()` to identify specific teleports
4. Use the existing `BoundaryChecker` to determine if the teleport destination is outside Varlamore
5. Call `event.consume()` to block the cast and display a chat message explaining why

This pattern is well-established in RuneLite plugins and directly supported by the API. The main challenge is mapping spell names to destination coordinates, but hardcoding destination checks is acceptable since teleport locations are fixed game content.

**Primary recommendation:** Use `MenuOptionClicked` event interception with spell name pattern matching. Block all spellbook teleports except Home Teleport (which requires respawn point checking). Display clear feedback via `ChatMessageManager`.

## Standard Stack

### Core
| Library | Version | Purpose | Why Standard |
|---------|---------|---------|--------------|
| RuneLite API | latest.release | Event system, client access | Core plugin framework |
| Google Guice | (via RuneLite) | Dependency injection | Standard RuneLite injection pattern |
| Lombok | 1.18.30 | Boilerplate reduction | Already in project |
| Gson | (via RuneLite) | JSON parsing (if needed for config) | RuneLite standard |

### Supporting
| Library | Version | Purpose | When to Use |
|---------|---------|---------|-------------|
| ChatMessageManager | (RuneLite client) | Send chat feedback | Required for user feedback messages |
| Client | (RuneLite API) | Access game state, VarPlayers | Required for respawn point checking |

### Alternatives Considered
| Instead of | Could Use | Tradeoff |
|------------|-----------|----------|
| MenuOptionClicked | ChatMessage parsing | Post-hoc detection; can't prevent cast |
| Spell name matching | Widget ID enumeration | More brittle; IDs change with game updates |
| Hardcoded destinations | Live coordinate lookup | Unnecessary complexity; teleports are fixed |

**Installation:**
```bash
# No additional dependencies needed - all provided by RuneLite
```

## Architecture Patterns

### Recommended Project Structure
```
src/main/java/com/varlamoreuim/
├── VarlamoreUimPlugin.java      # Main plugin, event subscribers
├── BoundaryChecker.java         # Existing boundary service
├── VarlamoreUimConfig.java      # Config options
├── VarlamoreUimPanel.java       # UI panel
└── teleport/
    ├── TeleportBlocker.java     # Spell interception logic (new)
    └── TeleportDestinations.java # Hardcoded destination mappings (new)
```

### Pattern 1: Event Interception with MenuOptionClicked
**What:** Subscribe to `MenuOptionClicked`, check for spell casts, consume event if blocked
**When to use:** All user-initiated actions that need prevention (not just detection)

**Example:**
```java
// Source: https://github.com/runelite/runelite/blob/master/runelite-api/src/main/java/net/runelite/api/events/MenuOptionClicked.java
@Subscribe
public void onMenuOptionClicked(MenuOptionClicked event)
{
    if (!"Cast".equals(event.getMenuOption()))
    {
        return;
    }

    // Get spell name from target (e.g., "Varrock Teleport")
    String spellName = event.getMenuTarget();

    // Check if spell is a blocked teleport
    if (isTeleportOutsideVarlamore(spellName))
    {
        event.consume(); // Prevent the spell cast
        sendChatMessage("You cannot teleport outside Varlamore!");
    }
}
```

### Pattern 2: Spell Name Pattern Matching
**What:** Parse spell names from `getMenuTarget()` to identify teleport types
**When to use:** When widget IDs are unreliable or spell-specific behavior is needed

**Example:**
```java
// Source: Community pattern from research
private boolean isTeleportSpell(String target)
{
    // Standard spellbook city teleports
    if (target.contains("Varrock Teleport") ||
        target.contains("Lumbridge Teleport") ||
        target.contains("Falador Teleport") ||
        target.contains("Camelot Teleport") ||
        target.contains("Ardougne Teleport") ||
        target.contains("Watchtower Teleport"))
    {
        return true;
    }

    // Ancient Magicks (all end with "Teleport")
    if (target.contains("Paddewwa") || target.contains("Senntisten") ||
        target.contains("Kharyrll") || target.contains("Lassar") ||
        target.contains("Dareeyak") || target.contains("Carrallangar") ||
        target.contains("Annakarl") || target.contains("Ghorrock"))
    {
        return true;
    }

    // Similar for Lunar and Arceuus...
    return false;
}
```

### Pattern 3: Chat Message Feedback
**What:** Use `ChatMessageManager` to queue messages visible to the player
**When to use:** Whenever blocking user actions; clear feedback is critical UX

**Example:**
```java
// Source: https://github.com/runelite/runelite/blob/master/runelite-client/src/main/java/net/runelite/client/chat/ChatMessageManager.java
@Inject
private ChatMessageManager chatMessageManager;

private void sendBlockedMessage(String spellName)
{
    String message = new ChatMessageBuilder()
        .append(ChatColorType.NORMAL)
        .append("You cannot cast ")
        .append(ChatColorType.HIGHLIGHT)
        .append(spellName)
        .append(ChatColorType.NORMAL)
        .append(" - it would take you outside Varlamore!")
        .build();

    chatMessageManager.queue(QueuedMessage.builder()
        .type(ChatMessageType.GAMEMESSAGE)
        .runeLiteFormattedMessage(message)
        .build());
}
```

### Pattern 4: Home Teleport Special Case
**What:** Check respawn point location before blocking Home Teleport
**When to use:** Home Teleport destination varies by spellbook and respawn setting

**Example:**
```java
// Source: Derived from VarPlayer research and spawn mechanics
private boolean isHomeTeleportBlocked()
{
    // Home teleport destinations by spellbook:
    // Standard: Lumbridge
    // Ancient: Edgeville
    // Lunar: Lunar Isle
    // Arceuus: Dark Altar (in Kourend/Varlamore region)

    // TODO: Detect current spellbook via widget or VarPlayer
    // For Phase 2, may need to block all Home Teleports except Arceuus
    // or implement spellbook detection

    // Alternative: Always allow Home Teleport and accept edge case
    return false; // Placeholder
}
```

### Anti-Patterns to Avoid

- **Post-hoc detection:** Don't try to detect teleports after they happen via coordinate changes; prevent them with `MenuOptionClicked`
- **Widget ID hardcoding:** Don't rely on child widget IDs for specific spells; they change with game updates. Use spell names from `getMenuTarget()`.
- **Silent blocking:** Never consume an event without chat feedback; players need to know why their action was blocked
- **Over-engineering destinations:** Don't build complex coordinate lookup systems; teleport destinations are fixed game content

## Don't Hand-Roll

| Problem | Don't Build | Use Instead | Why |
|---------|-------------|-------------|-----|
| Chat messages | Custom packet injection | ChatMessageManager | Handles formatting, queuing, client integration |
| Event system | Custom hooks | RuneLite @Subscribe events | Built-in, tested, maintained |
| Widget detection | Manual widget traversal | MenuOptionClicked fields | Event provides parsed data already |
| JSON parsing | String manipulation | Gson (already in project) | If needed for destination config |

**Key insight:** RuneLite's event system and client services are mature and well-tested. Custom solutions introduce bugs and maintenance burden. Use the framework's provided tools.

## Common Pitfalls

### Pitfall 1: MenuOptionClicked fires for all actions
**What goes wrong:** Event fires for walking, item clicks, NPC interactions—not just spells. Logic triggers on unrelated actions.
**Why it happens:** The event is triggered for "any left click action performed (clicking an item, walking to a tile, etc) as well as any right-click menu option."
**How to avoid:** Always check `getMenuOption()` equals "Cast" first, then verify widget group ID or spell name pattern
**Warning signs:** Plugin logging spam, performance issues, non-spell actions being blocked

### Pitfall 2: getMenuTarget() format varies
**What goes wrong:** Spell names may have color tags (`<col=00ff00>Varrock Teleport</col>`), inconsistent casing, or trailing text
**Why it happens:** OSRS client formats menu text with HTML-like tags for colors, may include level requirements
**How to avoid:** Use `.contains()` instead of `.equals()`, strip color tags with `.replaceAll("<[^>]*>", "")`, normalize case
**Warning signs:** Spells not detected despite correct name, intermittent blocking failures

### Pitfall 3: Consuming events without feedback
**What goes wrong:** Player casts spell, nothing happens, no explanation—feels like a bug
**Why it happens:** `event.consume()` silently prevents the action; no built-in feedback mechanism
**How to avoid:** ALWAYS call `chatMessageManager.queue()` before or after `event.consume()`
**Warning signs:** User confusion, bug reports about "spells not working"

### Pitfall 4: Home Teleport complexity
**What goes wrong:** Blocking all Home Teleports includes Arceuus Home (which goes to Dark Altar in Kourend, likely inside Varlamore boundary)
**Why it happens:** Home Teleport destinations vary by active spellbook, detecting spellbook is non-trivial
**How to avoid:** Either (a) allow all Home Teleports and accept edge case, (b) detect spellbook via widgets, or (c) block all and document limitation
**Warning signs:** User complaints about Arceuus Home being blocked when it shouldn't be

### Pitfall 5: Forgetting to test with plugin disabled
**What goes wrong:** Logic assumes plugin is always enabled; NPE or incorrect behavior when toggled off
**Why it happens:** Plugin enable/disable is per user config; event subscribers still fire unless checked
**How to avoid:** Check `config.pluginEnabled()` at start of event handlers, guard all blocking logic
**Warning signs:** Errors when plugin disabled, config toggle doesn't work

### Pitfall 6: Race condition with config loading
**What goes wrong:** Plugin tries to access config or BoundaryChecker before initialization completes
**Why it happens:** `startUp()` may not have completed when first events fire
**How to avoid:** Null-check injected dependencies, verify `boundaryChecker.isLoaded()` before use
**Warning signs:** NPE on plugin startup, intermittent failures on login

## Code Examples

Verified patterns from official sources and research:

### Spell Cast Detection and Blocking
```java
// Source: https://github.com/runelite/runelite/blob/master/runelite-api/src/main/java/net/runelite/api/events/MenuOptionClicked.java
// Composite pattern from research

@Subscribe
public void onMenuOptionClicked(MenuOptionClicked event)
{
    if (!config.pluginEnabled())
    {
        return;
    }

    // Only process spell casts
    if (!"Cast".equals(event.getMenuOption()))
    {
        return;
    }

    String target = event.getMenuTarget();

    // Strip color tags: <col=ffffff>Spell Name</col>
    String spellName = target.replaceAll("<[^>]*>", "").trim();

    // Check if this is a teleport spell that goes outside Varlamore
    if (isTeleportOutsideVarlamore(spellName))
    {
        event.consume();

        chatMessageManager.queue(QueuedMessage.builder()
            .type(ChatMessageType.GAMEMESSAGE)
            .runeLiteFormattedMessage(new ChatMessageBuilder()
                .append(ChatColorType.HIGHLIGHT)
                .append("Varlamore UIM: ")
                .append(ChatColorType.NORMAL)
                .append("You cannot cast ")
                .append(spellName)
                .append(" - it would take you outside Varlamore!")
                .build())
            .build());
    }
}
```

### Spell Name to Destination Mapping
```java
// Source: https://oldschool.runescape.wiki/w/Standard_spellbook
// https://oldschool.runescape.wiki/w/Ancient_Magicks
// https://oldschool.runescape.wiki/w/Lunar_spellbook
// https://oldschool.runescape.wiki/w/Arceuus_spellbook

private static final Map<String, WorldPoint> TELEPORT_DESTINATIONS = new HashMap<>();

static
{
    // Standard spellbook city teleports
    TELEPORT_DESTINATIONS.put("Varrock Teleport", new WorldPoint(3212, 3424, 0));
    TELEPORT_DESTINATIONS.put("Lumbridge Teleport", new WorldPoint(3222, 3218, 0));
    TELEPORT_DESTINATIONS.put("Falador Teleport", new WorldPoint(2965, 3379, 0));
    TELEPORT_DESTINATIONS.put("Camelot Teleport", new WorldPoint(2757, 3477, 0));
    TELEPORT_DESTINATIONS.put("Ardougne Teleport", new WorldPoint(2661, 3305, 0));
    TELEPORT_DESTINATIONS.put("Watchtower Teleport", new WorldPoint(2547, 3113, 0));

    // Ancient Magicks teleports (all 8)
    TELEPORT_DESTINATIONS.put("Paddewwa Teleport", new WorldPoint(3098, 9884, 0));
    TELEPORT_DESTINATIONS.put("Senntisten Teleport", new WorldPoint(3345, 3375, 0));
    TELEPORT_DESTINATIONS.put("Kharyrll Teleport", new WorldPoint(3492, 3471, 0));
    TELEPORT_DESTINATIONS.put("Lassar Teleport", new WorldPoint(3006, 3471, 0));
    TELEPORT_DESTINATIONS.put("Dareeyak Teleport", new WorldPoint(2966, 3695, 0));
    TELEPORT_DESTINATIONS.put("Carrallangar Teleport", new WorldPoint(3156, 3666, 0));
    TELEPORT_DESTINATIONS.put("Annakarl Teleport", new WorldPoint(3288, 3886, 0));
    TELEPORT_DESTINATIONS.put("Ghorrock Teleport", new WorldPoint(2977, 3873, 0));

    // Lunar spellbook teleports (8 main teleports)
    TELEPORT_DESTINATIONS.put("Moonclan Teleport", new WorldPoint(2111, 3915, 0));
    TELEPORT_DESTINATIONS.put("Waterbirth Teleport", new WorldPoint(2527, 3739, 0));
    TELEPORT_DESTINATIONS.put("Barbarian Teleport", new WorldPoint(2544, 3572, 0));
    TELEPORT_DESTINATIONS.put("Khazard Teleport", new WorldPoint(2656, 3157, 0));
    TELEPORT_DESTINATIONS.put("Fishing Guild Teleport", new WorldPoint(2610, 3391, 0));
    TELEPORT_DESTINATIONS.put("Catherby Teleport", new WorldPoint(2804, 3451, 0));
    TELEPORT_DESTINATIONS.put("Ice Plateau Teleport", new WorldPoint(2972, 3873, 0));
    TELEPORT_DESTINATIONS.put("Trollheim Teleport", new WorldPoint(2888, 3674, 0));

    // Arceuus spellbook teleports (9 main teleports)
    TELEPORT_DESTINATIONS.put("Cemetery Teleport", new WorldPoint(1774, 3516, 0)); // Forgotten Cemetery
    TELEPORT_DESTINATIONS.put("Draynor Manor Teleport", new WorldPoint(3108, 3352, 0));
    TELEPORT_DESTINATIONS.put("Mind Altar Teleport", new WorldPoint(2982, 3514, 0));
    TELEPORT_DESTINATIONS.put("Salve Graveyard Teleport", new WorldPoint(3432, 3460, 0));
    TELEPORT_DESTINATIONS.put("Fenkenstrain's Castle Teleport", new WorldPoint(3548, 3528, 0));
    TELEPORT_DESTINATIONS.put("West Ardougne Teleport", new WorldPoint(2500, 3290, 0));
    TELEPORT_DESTINATIONS.put("Harmony Teleport", new WorldPoint(3796, 2867, 0)); // Harmony Island
    TELEPORT_DESTINATIONS.put("Ape Atoll Teleport", new WorldPoint(2767, 2775, 0)); // Ape Atoll Dungeon
    TELEPORT_DESTINATIONS.put("Battlefront Teleport", new WorldPoint(1349, 3739, 0));
}

private boolean isTeleportOutsideVarlamore(String spellName)
{
    WorldPoint destination = TELEPORT_DESTINATIONS.get(spellName);
    if (destination == null)
    {
        // Not a tracked teleport spell
        return false;
    }

    // Use existing BoundaryChecker
    return !boundaryChecker.isInVarlamore(destination);
}
```

### Checking Widget Group ID for Spellbook
```java
// Source: https://github.com/runelite/runelite/blob/master/runelite-api/src/main/java/net/runelite/api/widgets/WidgetID.java
// Pattern: Verify the click is on the spellbook interface

import static net.runelite.api.widgets.WidgetID.SPELLBOOK_GROUP_ID;

@Subscribe
public void onMenuOptionClicked(MenuOptionClicked event)
{
    Widget widget = event.getWidget();
    if (widget != null && widget.getId() >>> 16 == SPELLBOOK_GROUP_ID)
    {
        // Click is on spellbook interface
        // Safe to check for "Cast" option
    }
}
```

## State of the Art

| Old Approach | Current Approach | When Changed | Impact |
|--------------|------------------|--------------|--------|
| WidgetInfo enum for spell widgets | InterfaceID + dynamic widget access | ~2022-2023 | WidgetInfo deprecated; use group ID checks |
| String-based widget ID constants | Programmatic `widget.getId() >>> 16` | RuneLite API evolution | More robust against client updates |
| Hardcoded widget child IDs | MenuEntry parsing from events | Ongoing best practice | Less brittle, survives game updates better |

**Deprecated/outdated:**
- `WidgetInfo.SPELL_*` constants: Still exist but documented as potentially outdated; prefer `InterfaceID` references
- Direct widget traversal for spell detection: Use `MenuOptionClicked.getMenuTarget()` instead

## Open Questions

1. **How to detect current spellbook for Home Teleport?**
   - What we know: Home Teleport destinations vary (Lumbridge, Edgeville, Lunar Isle, Dark Altar)
   - What's unclear: Reliable VarPlayer or widget to determine active spellbook
   - Recommendation: Phase 2 may need to block all Home Teleports or implement spellbook detection as sub-task

2. **Do teleport spell names change with game updates?**
   - What we know: Spell names in menu targets appear stable based on wiki
   - What's unclear: Whether Jagex has ever renamed teleport spells in updates
   - Recommendation: Use `.contains()` matching for flexibility; add logging to detect mismatches

3. **Are Varlamore region coordinates finalized?**
   - What we know: Phase 1 uses placeholder region IDs
   - What's unclear: Whether teleport destination coordinates need similar placeholder treatment
   - Recommendation: Hardcode known coordinates; update if Varlamore boundary changes

4. **Should Teleport to House be blocked?**
   - What we know: Player-owned houses can be in 9 locations (Rimmington, Taverley, Pollnivneach, Hosidius, etc.)
   - What's unclear: Whether POH locations are part of Varlamore restriction scope
   - Recommendation: Defer to Phase 3 (Item Teleport Blocking) or discuss with user; may require VarPlayer detection

## Complete Teleport Spell Lists

### Standard Spellbook (6 city teleports to block)
1. Varrock Teleport → Varrock market square
2. Lumbridge Teleport → Lumbridge Castle courtyard
3. Falador Teleport → Falador centre
4. Camelot Teleport → Camelot gates
5. Ardougne Teleport → East Ardougne market square
6. Watchtower Teleport → Watchtower (outside Yanille)

**Note:** Home Teleport (Lumbridge) requires special handling per SPELL-02

### Ancient Magicks (8 teleports)
1. Paddewwa Teleport → Edgeville Dungeon entrance
2. Senntisten Teleport → Digsite (between Southgate and Exam Centre)
3. Kharyrll Teleport → Canifis (inside Tavern)
4. Lassar Teleport → Top of Ice Mountain
5. Dareeyak Teleport → Western ruins (Wilderness)
6. Carrallangar Teleport → Graveyard of Shadows (Wilderness)
7. Annakarl Teleport → Demonic Ruins (Wilderness)
8. Ghorrock Teleport → Frozen Wastes Plateau (Wilderness)

**Note:** Edgeville Home Teleport also needs blocking

### Lunar Spellbook (8 teleports)
1. Moonclan Teleport → Lunar Isle gates
2. Waterbirth Teleport → Waterbirth Island (Jarvald's ship)
3. Barbarian Teleport → Barbarian Outpost
4. Khazard Teleport → Port Khazard
5. Fishing Guild Teleport → Outside Fishing Guild
6. Catherby Teleport → Catherby bank
7. Ice Plateau Teleport → Ice Plateau (Level 53 Wilderness)
8. Trollheim Teleport → Trollheim summit

**Note:** Lunar Home Teleport (Lunar Isle) also needs blocking

### Arceuus Spellbook (9 teleports)
1. Cemetery Teleport → Forgotten Cemetery
2. Draynor Manor Teleport → Draynor Manor
3. Mind Altar Teleport → Mind Altar
4. Salve Graveyard Teleport → Ghoul area (Mort Myre entrance)
5. Fenkenstrain's Castle Teleport → Fenkenstrain's Castle
6. West Ardougne Teleport → West Ardougne graveyard
7. Harmony Teleport → Harmony Island
8. Ape Atoll Teleport → Ape Atoll Dungeon
9. Battlefront Teleport → Ancient Grave (beyond Battlefront)

**Note:** Arceuus Home Teleport → Dark Altar (likely INSIDE Varlamore/Kourend boundary; may need to allow)

## Sources

### Primary (HIGH confidence)
- [MenuOptionClicked API](https://github.com/runelite/runelite/blob/master/runelite-api/src/main/java/net/runelite/api/events/MenuOptionClicked.java) - Event structure and consume() method
- [ChatMessageManager](https://github.com/runelite/runelite/blob/master/runelite-client/src/main/java/net/runelite/client/chat/ChatMessageManager.java) - Sending chat messages
- [VarPlayers, VarBits, and VarClients Wiki](https://github.com/runelite/runelite/wiki/VarPlayers,-VarBits,-and-VarClients) - Game variable access
- [VarPlayer.java](https://github.com/runelite/runelite/blob/master/runelite-api/src/main/java/net/runelite/api/VarPlayer.java) - VarPlayer constants
- [MenuAction.java](https://github.com/runelite/runelite/blob/master/runelite-api/src/main/java/net/runelite/api/MenuAction.java) - MenuAction enum values
- [WidgetID.java](https://github.com/runelite/runelite/blob/master/runelite-api/src/main/java/net/runelite/api/widgets/WidgetID.java) - Widget group IDs

### Secondary (MEDIUM confidence)
- [OSRS Wiki: Standard Spellbook](https://oldschool.runescape.wiki/w/Standard_spellbook) - Teleport spell list and destinations
- [OSRS Wiki: Ancient Magicks](https://oldschool.runescape.wiki/w/Ancient_Magicks) - Ancient teleport spells
- [OSRS Wiki: Lunar Spellbook](https://oldschool.runescape.wiki/w/Lunar_spellbook) - Lunar teleport spells
- [OSRS Wiki: Arceuus Spellbook](https://oldschool.runescape.wiki/w/Arceuus_spellbook) - Arceuus teleport spells
- [OSRS Wiki: Home Teleport](https://oldschool.runescape.wiki/w/Home_Teleport) - Home Teleport destinations by spellbook
- [OSRS Wiki: Spawning](https://oldschool.runescape.wiki/w/Spawning) - Respawn point mechanics
- [RuneLite Developer Guide](https://github.com/runelite/runelite/wiki/Developer-Guide) - Plugin development patterns
- [RuneLite onMenuOptionClicked Example](https://gist.github.com/phyce/238d5091abf04dc424007e517cd055ab) - Event handler example

### Tertiary (LOW confidence - requires validation)
- WebSearch results for spell detection patterns - Various plugin development discussions
- WebSearch results for VarPlayer usage - Community documentation

## Metadata

**Confidence breakdown:**
- Standard stack: HIGH - RuneLite API is stable and well-documented
- Architecture: HIGH - MenuOptionClicked pattern is proven in existing plugins
- Pitfalls: MEDIUM - Based on event documentation and general plugin development wisdom
- Spell lists: HIGH - Verified from OSRS Wiki official documentation
- Destination coordinates: MEDIUM - Wiki data; may need in-game verification with DevTools

**Research date:** 2026-02-16
**Valid until:** 30 days (stable RuneLite API; spell lists are fixed game content)

**Notes:**
- Teleport destination coordinates marked MEDIUM confidence; recommend verifying with RuneLite DevTools World Location plugin
- Home Teleport spellbook detection remains open question; may require additional research in Phase 2 planning
- Player-owned house teleport blocking scope unclear; recommend clarifying with user
