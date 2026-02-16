# Common Pitfalls for RuneLite Area-Locked Account Plugin

**Research Date:** 2026-02-16
**Research Type:** Project Research - Pitfalls & Risk Analysis
**Project:** Varlamore-locked UIM RuneLite Plugin

---

## Executive Summary

This document catalogs common pitfalls, edge cases, and rejection reasons for RuneLite plugins, with specific focus on area-locked account enforcement. Each pitfall includes warning signs, prevention strategies, and the development phase that should address it.

**Critical Risk Areas:**
1. Plugin Hub submission requirements (licensing, naming, code quality)
2. RuneLite API misuse (threading, lifecycle, event handling)
3. Performance issues (tick-rate processing, memory leaks)
4. Area-lock edge cases (missed teleports, instanced areas, death mechanics)
5. UIM-specific mechanics that bypass restrictions

---

## 1. RuneLite Plugin Development Pitfalls

### 1.1 Plugin Hub Rejection Reasons

#### Pitfall: Incorrect or Missing License

**Description:** Plugin Hub requires specific open-source licenses (BSD-2-Clause preferred). Missing LICENSE file or incompatible license = automatic rejection.

**Warning Signs:**
- No LICENSE file in repository root
- Using GPL, MIT, or other non-BSD license
- License file doesn't match Plugin Hub requirements

**Prevention Strategy:**
- Use BSD-2-Clause license (RuneLite standard)
- Include LICENSE file in repository root
- Ensure runelite-plugin.properties specifies correct license

**Development Phase:** Initial Setup (before first commit)

**Example Rejection:**
```
Rejection reason: "Plugin does not include a BSD-2-Clause license file"
```

---

#### Pitfall: Plugin Name Conflicts or Violations

**Description:** Plugin Hub rejects plugins with names that:
- Conflict with existing plugins
- Use "RuneLite" in the name
- Are generic/misleading (e.g., "Helper", "Tool")
- Reference trademarked terms inappropriately

**Warning Signs:**
- Plugin name is too generic
- Name includes "RuneLite" or "RL"
- Name conflicts with existing Plugin Hub entries

**Prevention Strategy:**
- Check Plugin Hub for existing plugins before naming
- Use specific, descriptive names (e.g., "Varlamore UIM" not "Area Lock")
- Avoid brand names or trademarks
- Update `runelite-plugin.properties` displayName to match

**Development Phase:** Initial Setup

**Example Rejection:**
```
Rejection reason: "Plugin name 'Area Lock Helper' is too generic and conflicts with existing plugin"
```

---

#### Pitfall: External Resource Dependencies

**Description:** Plugin Hub rejects plugins that:
- Make external HTTP requests without approval
- Load resources from external URLs
- Include bundled native libraries
- Use non-whitelisted dependencies

**Warning Signs:**
- Code contains `HttpClient`, `URL.openConnection()`, etc.
- Dependencies outside RuneLite's approved list
- Native library (.dll, .so) files in resources

**Prevention Strategy:**
- Keep all data files as local resources
- Use only RuneLite-approved dependencies
- Request approval for external APIs before implementing
- Bundle all assets in the plugin JAR

**Development Phase:** Architecture & Implementation

**Example Rejection:**
```
Rejection reason: "Plugin makes unapproved external HTTP requests to api.example.com"
```

---

#### Pitfall: Insufficient Code Quality

**Description:** Plugin Hub requires:
- No compiler warnings
- Proper null checking
- No dead code or unused imports
- Consistent code style
- Meaningful variable/method names

**Warning Signs:**
- Build shows warnings
- Code contains `@SuppressWarnings` annotations
- Variables named `a`, `b`, `temp`, etc.
- Unused imports or methods

**Prevention Strategy:**
- Enable `-Werror` (treat warnings as errors)
- Use static analysis tools (SpotBugs, Checkstyle)
- Follow Java naming conventions
- Remove all unused code before submission

**Development Phase:** Implementation & Polish

**Example Rejection:**
```
Rejection reason: "Plugin contains compiler warnings and unused imports"
```

---

#### Pitfall: Missing or Inadequate Plugin Metadata

**Description:** `runelite-plugin.properties` must include:
- Accurate description
- Relevant tags
- Valid support URL
- Correct plugin name
- Author information

**Warning Signs:**
- Generic/placeholder descriptions
- Empty or irrelevant tags
- No support URL
- Author listed as "Nobody" or empty

**Prevention Strategy:**
- Write clear, concise description (1-2 sentences)
- Use relevant, searchable tags
- Provide GitHub issues URL for support
- Include real author name

**Development Phase:** Initial Setup & Pre-Submission

**Example Configuration:**
```properties
displayName=Varlamore UIM
author=YourName
description=Enforces Varlamore area restrictions for Ultimate Ironman accounts with milestone-based unlocks
tags=varlamore,uim,area-locked,restriction,ironman
support=https://github.com/username/varlamore-uim/issues
```

---

### 1.2 API Misuse Patterns

#### Pitfall: Wrong Thread for API Calls

**Description:** RuneLite runs on a single game thread. Making Client API calls from other threads causes crashes or undefined behavior.

**Warning Signs:**
- Creating threads in plugin code
- Using `CompletableFuture`, `ExecutorService` without proper thread safety
- API calls in Swing event handlers (EDT != game thread)

**Prevention Strategy:**
- Never call Client API from non-game threads
- Use `clientThread.invoke()` or `clientThread.invokeLater()` for async operations
- Keep UI updates on EDT, game state queries on game thread

**Development Phase:** Implementation

**Example Error:**
```java
// WRONG - called from Swing EDT
button.addActionListener(e -> {
    WorldPoint location = client.getLocalPlayer().getWorldLocation(); // CRASH
});

// CORRECT - invoke on game thread
button.addActionListener(e -> {
    clientThread.invokeLater(() -> {
        WorldPoint location = client.getLocalPlayer().getWorldLocation();
        // Process location
    });
});
```

---

#### Pitfall: Wrong Lifecycle Hook Usage

**Description:** Plugins have specific lifecycle methods that must be used correctly:
- `startUp()` - plugin enabled (may be before login)
- `shutDown()` - plugin disabled
- Subscription events - only fired when appropriate

**Warning Signs:**
- Accessing `client.getLocalPlayer()` in `startUp()` (may be null)
- Not cleaning up in `shutDown()` (memory leaks)
- Assuming player is logged in during startup

**Prevention Strategy:**
- Initialize data structures in `startUp()`
- Initialize game-dependent state in `GameStateChanged` (LOGIN/LOGGED_IN)
- Clean up all resources in `shutDown()`
- Don't assume game state in lifecycle methods

**Development Phase:** Implementation

**Example:**
```java
@Override
protected void startUp() {
    // CORRECT - initialize structures
    hiddenNpcs = new HashSet<>();
    unlockManager.loadState();

    // WRONG - player might not be logged in
    // WorldPoint location = client.getLocalPlayer().getWorldLocation();
}

@Subscribe
public void onGameStateChanged(GameStateChanged event) {
    if (event.getGameState() == GameState.LOGGED_IN) {
        // CORRECT - now safe to access player
        WorldPoint location = client.getLocalPlayer().getWorldLocation();
    }
}

@Override
protected void shutDown() {
    // CRITICAL - cleanup all resources
    hiddenNpcs.clear();
    overlayManager.remove(overlay);
    clientToolbar.removeNavigation(navButton);
}
```

---

#### Pitfall: Event Subscription Memory Leaks

**Description:** Event subscriptions are managed automatically by `@Subscribe`, but manual subscriptions or retained references can leak memory.

**Warning Signs:**
- Creating listeners without removing them
- Storing references to game objects (NPCs, Players)
- Not clearing collections in `shutDown()`

**Prevention Strategy:**
- Use `@Subscribe` for event handling (automatic cleanup)
- Clear all collections in `shutDown()`
- Don't hold references to game objects beyond event scope
- Use weak references if needed

**Development Phase:** Implementation

**Example:**
```java
// WRONG - memory leak
private final Set<NPC> trackedNpcs = new HashSet<>();

@Subscribe
public void onNpcSpawned(NpcSpawned event) {
    trackedNpcs.add(event.getNpc()); // Never removed!
}

// CORRECT - cleanup
@Subscribe
public void onNpcDespawned(NpcDespawned event) {
    trackedNpcs.remove(event.getNpc());
}

@Override
protected void shutDown() {
    trackedNpcs.clear(); // Critical cleanup
}
```

---

### 1.3 Performance Issues

#### Pitfall: Processing on Every Game Tick

**Description:** Game ticks fire ~600ms (1.8 ticks/second). Heavy processing on every tick causes lag and poor performance.

**Warning Signs:**
- Complex logic in `@Subscribe onGameTick()`
- Iterating large collections every tick
- No caching of expensive calculations

**Prevention Strategy:**
- Only process on tick if state changed
- Cache calculations, invalidate on change
- Use event-driven updates instead of polling
- Debounce/throttle expensive operations

**Development Phase:** Implementation & Optimization

**Example:**
```java
// WRONG - expensive operation every tick
@Subscribe
public void onGameTick(GameTick event) {
    for (NPC npc : client.getNpcs()) {
        if (shouldHideNpc(npc)) { // Expensive check
            hideNpc(npc);
        }
    }
}

// CORRECT - event-driven
@Subscribe
public void onNpcSpawned(NpcSpawned event) {
    NPC npc = event.getNpc();
    if (shouldHideNpc(npc)) { // Only check new NPCs
        hideNpc(npc);
    }
}
```

---

#### Pitfall: Inefficient Menu Entry Processing

**Description:** `MenuEntryAdded` fires for every right-click menu option. Heavy processing here causes noticeable lag.

**Warning Signs:**
- Complex string parsing in `onMenuEntryAdded()`
- Database/map lookups for every menu entry
- Modifying every menu entry

**Prevention Strategy:**
- Use fast string comparisons (avoid regex in hot path)
- Pre-compute lookup tables
- Only process relevant menu entries (early return)
- Cache teleport destination mappings

**Development Phase:** Implementation & Optimization

**Example:**
```java
// WRONG - slow processing
@Subscribe
public void onMenuEntryAdded(MenuEntryAdded event) {
    String target = event.getTarget();
    // Slow regex on every menu entry
    if (target.matches(".*Teleport.*")) {
        WorldPoint dest = lookupDestination(target); // Expensive
        if (!boundaryManager.isInBounds(dest)) {
            event.setModified();
        }
    }
}

// CORRECT - optimized
private static final Set<String> TELEPORT_OPTIONS = Set.of("Cast", "Rub", "Break");

@Subscribe
public void onMenuEntryAdded(MenuEntryAdded event) {
    if (!TELEPORT_OPTIONS.contains(event.getOption())) {
        return; // Early exit for non-teleports
    }

    String target = event.getTarget();
    WorldPoint dest = teleportDestCache.get(target); // Pre-computed
    if (dest != null && !boundaryManager.isInBounds(dest)) {
        event.setModified();
    }
}
```

---

#### Pitfall: Unnecessary Widget Polling

**Description:** Constantly checking widgets (UI elements) is expensive and unnecessary.

**Warning Signs:**
- Checking `client.getWidget()` every tick
- Iterating widgets to find specific elements
- Not using `WidgetLoaded`/`WidgetClosed` events

**Prevention Strategy:**
- Use `WidgetLoaded` event to detect widget opening
- Cache widget references (invalidate on close)
- Only process widgets when events fire

**Development Phase:** Implementation

**Example:**
```java
// WRONG - polling every tick
@Subscribe
public void onGameTick(GameTick event) {
    Widget dialogueWidget = client.getWidget(WidgetInfo.DIALOG_NPC_TEXT);
    if (dialogueWidget != null) {
        // Process dialogue
    }
}

// CORRECT - event-driven
@Subscribe
public void onWidgetLoaded(WidgetLoaded event) {
    if (event.getGroupId() == WidgetInfo.DIALOG_NPC_TEXT.getGroupId()) {
        Widget dialogueWidget = client.getWidget(WidgetInfo.DIALOG_NPC_TEXT);
        // Process dialogue
    }
}
```

---

### 1.4 Config System Mistakes

#### Pitfall: Not Handling Config Changes Dynamically

**Description:** Config changes should take effect immediately without restart.

**Warning Signs:**
- Config only read in `startUp()`
- No `@Subscribe onConfigChanged()` handler
- User must restart plugin for changes

**Prevention Strategy:**
- Subscribe to `ConfigChanged` event
- Update active restrictions when config changes
- Propagate changes to all components
- Test config changes while plugin running

**Development Phase:** Implementation

**Example:**
```java
@Subscribe
public void onConfigChanged(ConfigChanged event) {
    if (!event.getGroup().equals("varlamoreuim")) {
        return;
    }

    switch (event.getKey()) {
        case "blockTeleports":
            travelEngine.updateTeleportBlocking(config.blockTeleports());
            break;
        case "allowedChunks":
            boundaryManager.reloadChunks(config.allowedChunks());
            break;
    }
}
```

---

#### Pitfall: Config Validation Issues

**Description:** Config values should be validated to prevent invalid states.

**Warning Signs:**
- No range validation on numeric configs
- No length limits on string configs
- Accepting invalid enum/list values

**Prevention Strategy:**
- Use `@Range` annotation for numeric configs
- Validate string length/format
- Provide sensible defaults
- Handle invalid config gracefully

**Development Phase:** Implementation

**Example:**
```java
@ConfigItem(
    keyName = "maxTeleportDistance",
    name = "Max Teleport Distance",
    description = "Maximum allowed teleport distance in tiles"
)
@Range(min = 0, max = 1000)
default int maxTeleportDistance() {
    return 100;
}
```

---

### 1.5 NPC Manipulation Edge Cases

#### Pitfall: NPC Despawn/Respawn Not Handled

**Description:** NPCs despawn when player moves away, respawn when returning. Replacement state must be maintained.

**Warning Signs:**
- NPC replacements disappear when player returns
- Duplicate NPCs appear (original + replacement)
- Replacement state tied to specific NPC instance

**Prevention Strategy:**
- Track NPCs by ID and location, not instance
- Handle `NpcDespawned` event to cleanup
- Re-check replacement on `NpcSpawned`
- Use region-based tracking

**Development Phase:** Implementation

**Example:**
```java
// Track by NPC ID + location, not instance
private final Map<NpcLocation, NPC> hiddenNpcs = new HashMap<>();

@Subscribe
public void onNpcSpawned(NpcSpawned event) {
    NPC npc = event.getNpc();
    NpcLocation key = new NpcLocation(npc.getId(), npc.getWorldLocation());

    if (shouldReplaceNpc(npc.getId())) {
        hiddenNpcs.put(key, npc);
    }
}

@Subscribe
public void onNpcDespawned(NpcDespawned event) {
    NPC npc = event.getNpc();
    NpcLocation key = new NpcLocation(npc.getId(), npc.getWorldLocation());
    hiddenNpcs.remove(key);
}
```

---

#### Pitfall: Dialogue State Corruption

**Description:** Modifying dialogue widgets incorrectly can break dialogue flow or cause visual glitches.

**Warning Signs:**
- Game dialogue becomes unresponsive
- Text overlaps or displays incorrectly
- Player can't exit dialogue

**Prevention Strategy:**
- Only modify text content, not structure
- Don't modify dialogue during transitions
- Test with all dialogue states (NPC, player choice, tutorial, etc.)
- Use ChatMessageManager as fallback

**Development Phase:** Implementation & Testing

**Example:**
```java
@Subscribe
public void onWidgetLoaded(WidgetLoaded event) {
    if (event.getGroupId() == WidgetInfo.DIALOG_NPC_TEXT.getGroupId()) {
        clientThread.invokeLater(() -> {
            Widget widget = client.getWidget(WidgetInfo.DIALOG_NPC_TEXT);
            if (widget != null && !widget.isHidden()) {
                // Only modify visible, stable widgets
                widget.setText(customDialogue);
            }
        });
    }
}
```

---

### 1.6 Overlay Rendering Pitfalls

#### Pitfall: Drawing Off-Screen or Invalid Coordinates

**Description:** Drawing outside screen bounds or with invalid coordinates causes visual glitches or crashes.

**Warning Signs:**
- Overlays flicker or don't appear
- Rendering exceptions in logs
- NPCs/objects missing from overlays

**Prevention Strategy:**
- Check if coordinates are on screen before rendering
- Use `Perspective.localToCanvas()` for world-to-screen conversion
- Handle null returns from coordinate conversions
- Set appropriate overlay layer priority

**Development Phase:** Implementation

**Example:**
```java
@Override
public Dimension render(Graphics2D graphics) {
    for (NPC npc : hiddenNpcs.values()) {
        Point screenLoc = npc.getCanvasTextLocation(graphics, "Locked", 0);

        if (screenLoc == null) {
            continue; // NPC off-screen or invalid
        }

        // Safe to render
        graphics.drawString("Locked", screenLoc.getX(), screenLoc.getY());
    }
    return null;
}
```

---

#### Pitfall: Overlay Performance Issues

**Description:** Overlays render every frame (50-60 FPS). Heavy rendering causes FPS drops.

**Warning Signs:**
- FPS drops when overlay visible
- Complex calculations in `render()`
- Allocating objects in render loop

**Prevention Strategy:**
- Pre-compute positions and cache
- Use simple primitives (avoid complex shapes)
- Minimize object allocation in render
- Use `@OverlayPriority` appropriately

**Development Phase:** Implementation & Optimization

**Example:**
```java
// WRONG - allocates objects every frame
@Override
public Dimension render(Graphics2D graphics) {
    List<NPC> npcs = new ArrayList<>(hiddenNpcs.values()); // Allocation!
    for (NPC npc : npcs) {
        String text = "Locked: " + npc.getName(); // Allocation!
        // Render
    }
    return null;
}

// CORRECT - minimize allocations
private static final String LOCKED_TEXT = "Locked";

@Override
public Dimension render(Graphics2D graphics) {
    for (NPC npc : hiddenNpcs.values()) { // Iterate directly
        Point loc = npc.getCanvasTextLocation(graphics, LOCKED_TEXT, 0);
        if (loc != null) {
            graphics.drawString(LOCKED_TEXT, loc.getX(), loc.getY());
        }
    }
    return null;
}
```

---

## 2. Area-Lock Plugin Specific Pitfalls

### 2.1 Missed Travel Methods

#### Pitfall: Fairy Rings Not Blocked

**Description:** Fairy rings are a common teleport method easily overlooked.

**Warning Signs:**
- Only blocking spells/items, not objects
- No checks for object interactions
- Fairy ring codes not mapped to destinations

**Prevention Strategy:**
- Map all fairy ring codes to coordinates
- Block via `MenuOptionClicked` on fairy ring objects
- Check destination before allowing travel
- Test with all 3-letter codes

**Development Phase:** Implementation

**Fairy Ring Code Mapping Example:**
```java
private static final Map<String, WorldPoint> FAIRY_RING_DESTINATIONS = Map.of(
    "AIQ", new WorldPoint(2996, 3114, 0), // Asgarnia
    "AKQ", new WorldPoint(2735, 3496, 0), // Piscatoris
    "CKR", new WorldPoint(2801, 3003, 0)  // Karamja
    // ... all codes
);
```

---

#### Pitfall: Spirit Trees Missing

**Description:** Spirit trees provide fast travel across the game world.

**Prevention Strategy:**
- Block spirit tree interactions
- Map all spirit tree locations
- Check destination region
- Handle both quest and farming spirit trees

**Development Phase:** Implementation

---

#### Pitfall: Minigame Teleports Overlooked

**Description:** Minigame teleports (grouping teleports, minigame tab) bypass standard restrictions.

**Warning Signs:**
- Only blocking standard spellbook
- No checks for minigame interfaces
- Widget-based teleports not handled

**Prevention Strategy:**
- Block minigame teleport widgets
- Check `WidgetLoaded` for minigame interfaces
- Map minigame destinations
- Test with Clan Wars, Last Man Standing, etc.

**Development Phase:** Implementation

**Example Minigame Widgets:**
- Quest Helper teleports
- Achievement Diary teleports
- Minigame Group Finder teleports

---

#### Pitfall: POH Portal and Teleports

**Description:** Player-owned house (POH) has portals, teleport nexus, and jewelry box that can bypass restrictions.

**Warning Signs:**
- Not checking POH objects
- Missing portal destinations
- Jewelry box combinations not blocked

**Prevention Strategy:**
- Block POH portal interactions
- Block jewelry box usage
- Check if player is in POH (instance check)
- Map all nexus destinations

**Development Phase:** Implementation

**POH Teleport Objects:**
- Portal Nexus (all destinations)
- Jewelry Box (glory, duel, games necklace, etc.)
- Portals (direct teleports)
- Mounted symbols (PoH teleport to house)

---

#### Pitfall: Quest-Specific Teleports

**Description:** Some quests provide unique teleports (Ghommal's Hilt, Iorwerth Camp teleport, etc.)

**Prevention Strategy:**
- Research all quest reward teleports
- Block quest item usage
- Check for quest-specific widgets
- Update as new quests added

**Development Phase:** Implementation & Ongoing Maintenance

**Common Quest Teleports:**
- Ghommal's Hilt (Warriors' Guild)
- Ectophial (Ectofuntus)
- Kharedst's memoirs (Kourend)
- Slayer rings (Slayer masters)

---

#### Pitfall: Charter Ships

**Description:** Charter ships provide travel between ports.

**Prevention Strategy:**
- Block charter ship NPCs
- Map all charter destinations
- Replace charter NPCs with locked dialogue
- Test all charter routes

**Development Phase:** Implementation

---

#### Pitfall: Magic Carpets

**Description:** Desert magic carpet network.

**Prevention Strategy:**
- Block magic carpet NPC interactions
- Map all carpet destinations
- Replace NPCs appropriately
- Test full carpet network

**Development Phase:** Implementation

---

#### Pitfall: Canoes

**Description:** Canoe travel along rivers.

**Prevention Strategy:**
- Block canoe station interactions
- Map canoe destinations
- Check destination before allowing
- Test all canoe types (log, dugout, waka, etc.)

**Development Phase:** Implementation

---

#### Pitfall: Gnome Gliders

**Description:** Gnome glider network for fast travel.

**Prevention Strategy:**
- Block glider NPC interactions
- Map all glider destinations
- Replace glider NPCs
- Test all glider routes

**Development Phase:** Implementation

---

#### Pitfall: Wilderness Levers and Obelisks

**Description:** Wilderness has unique teleport mechanics.

**Prevention Strategy:**
- Block lever interactions (Edgeville, Ardougne)
- Block obelisk usage
- Map obelisk destinations
- Check if entering/leaving Wilderness

**Development Phase:** Implementation

---

#### Pitfall: Teleport Tablets

**Description:** Teleport tablets (house tablets, POH teleports, redirected tablets)

**Prevention Strategy:**
- Block tablet usage via item interaction
- Map all tablet types
- Check redirection destinations
- Test all tablet variants

**Development Phase:** Implementation

---

### 2.2 Boundary Edge Cases

#### Pitfall: Chunk Loading Boundary Issues

**Description:** OSRS loads chunks in a grid. Standing on chunk boundary may load chunks outside allowed region.

**Warning Signs:**
- Player can see into restricted areas
- NPCs from adjacent regions appear
- Objects from restricted areas visible

**Prevention Strategy:**
- Define boundary with buffer zone (1-2 chunks inward)
- Don't rely on visual boundary alone
- Test boundary edges thoroughly
- Handle partial chunk visibility

**Development Phase:** Implementation & Testing

**Example:**
```java
// Bad: exact boundary
private static final int BOUNDARY_CHUNK = 5700;

// Good: buffer zone
private static final int BOUNDARY_CHUNK_MIN = 5600;
private static final int BOUNDARY_CHUNK_MAX = 5698; // 2-chunk buffer
```

---

#### Pitfall: Instanced Areas Not Handled

**Description:** Raids, minigames, and boss fights are instanced. Instance IDs can cause boundary checks to fail.

**Warning Signs:**
- Boundary checks fail in instances
- Player can enter restricted instances
- Instance exit teleports not blocked

**Prevention Strategy:**
- Check if area is instanced (`WorldPoint.isInInstance()`)
- Maintain whitelist of allowed instances
- Block instance entry if destination restricted
- Block instance exit teleports to restricted areas

**Development Phase:** Implementation

**Example Instances:**
- Chambers of Xeric (raids)
- Theatre of Blood (raids)
- Nightmare Zone
- Fight Caves
- Inferno
- Boss instances (Vorkath, Zulrah, etc.)

---

#### Pitfall: Cutscenes Force Player Movement

**Description:** Quest cutscenes and game events can teleport player without interaction.

**Warning Signs:**
- Player moved during cutscenes
- Quest progression teleports player
- Random event teleports

**Prevention Strategy:**
- Detect cutscene state (widget checks)
- Warn player if cutscene would violate lock
- Block quest progression if cutscene moves player
- Handle gracefully (don't break quest)

**Development Phase:** Implementation & Testing

**Example Cutscenes:**
- Tutorial Island completion
- Quest completions (many teleport player)
- Romeo & Juliet quest (teleports to Varrock)

---

#### Pitfall: Death Mechanics Teleport Outside Region

**Description:** Death respawns player at specific locations (Lumbridge, Falador, etc.) which may be outside locked region.

**Warning Signs:**
- Player dies and respawns outside region
- Hardcore Ironman death
- Unsafe death (Wilderness, PvP)

**Prevention Strategy:**
- Detect death event
- Check respawn location
- Warn player before risky activities
- Consider allowing death respawn as exception (with warning)
- Block dangerous activities if respawn would break lock

**Development Phase:** Implementation

**Death Respawn Locations:**
- Normal death: Lumbridge (outside Varlamore)
- Gravestone death: Retrieval location
- Unsafe death: Item loss location
- Instance death: Outside instance

**Recommended Approach:**
```java
@Subscribe
public void onPlayerDeath(PlayerDeath event) {
    WorldPoint respawnPoint = getRespawnLocation();

    if (!boundaryManager.isInBounds(respawnPoint)) {
        // Warn player
        sendChatMessage("WARNING: Death will respawn you outside Varlamore!");
        sendChatMessage("Your area-lock will be broken.");
    }
}
```

---

#### Pitfall: Login/Logout at Boundary Chunks

**Description:** Logging out near boundary and logging back in may place player outside region (server-side position adjustment).

**Warning Signs:**
- Player logs in at slightly different position
- Boundary checks fail on login
- Player "drifts" across boundary

**Prevention Strategy:**
- Check player position on login
- Warn if player near boundary
- Consider grace period after login
- Don't auto-ban for server positioning issues

**Development Phase:** Implementation & Testing

**Example:**
```java
@Subscribe
public void onGameStateChanged(GameStateChanged event) {
    if (event.getGameState() == GameState.LOGGED_IN) {
        clientThread.invokeLater(() -> {
            WorldPoint location = client.getLocalPlayer().getWorldLocation();

            if (!boundaryManager.isInBounds(location)) {
                sendChatMessage("WARNING: You logged in outside Varlamore!");
                // Handle appropriately (warn, log, etc.)
            }
        });
    }
}
```

---

#### Pitfall: World Hopping Position Changes

**Description:** World hopping may adjust player position slightly (anti-griefing measure).

**Warning Signs:**
- Player hops worlds near boundary
- Position changes after world hop
- Boundary violation after hop

**Prevention Strategy:**
- Detect world hop (`GameStateChanged` to `HOPPING`)
- Compare position before/after hop
- Allow small position adjustments
- Warn if hop moves player significantly

**Development Phase:** Implementation

**Example:**
```java
private WorldPoint positionBeforeHop;

@Subscribe
public void onGameStateChanged(GameStateChanged event) {
    if (event.getGameState() == GameState.HOPPING) {
        positionBeforeHop = client.getLocalPlayer().getWorldLocation();
    } else if (event.getGameState() == GameState.LOGGED_IN && positionBeforeHop != null) {
        WorldPoint currentPos = client.getLocalPlayer().getWorldLocation();
        int distance = positionBeforeHop.distanceTo(currentPos);

        if (distance > 5) { // Threshold for acceptable movement
            sendChatMessage("WARNING: World hop moved you " + distance + " tiles!");
        }

        positionBeforeHop = null;
    }
}
```

---

### 2.3 Random Events and Special Mechanics

#### Pitfall: Random Events Moving Player

**Description:** Random events like Genie, Mysterious Old Man can teleport player.

**Warning Signs:**
- Random event NPCs appear
- Player teleported to random event area
- Player dismissed from random event to different location

**Prevention Strategy:**
- Detect random event entry/exit
- Check return location
- Most random events return to original location (safe)
- Warn if random event destination is unknown

**Development Phase:** Implementation

**Random Event Behavior:**
- Most return player to original location
- Some (discontinued) could move player
- Modern random events are generally safe

---

#### Pitfall: Quest Requirements Force Travel

**Description:** Some quests require visiting areas outside the lock, making them impossible.

**Warning Signs:**
- Player trying to complete impossible quests
- Quest progression blocked
- Quest teleports forced

**Prevention Strategy:**
- Maintain list of completable quests
- Warn player if quest requires leaving region
- Block quest start if quest requires travel
- Provide quest guide for locked region

**Development Phase:** Implementation & Documentation

---

### 2.4 Transportation Items and Spells

#### Pitfall: Xerician Fabric (Xeric's Talisman)

**Description:** Kourend-specific teleport item that may or may not be in Varlamore region.

**Prevention Strategy:**
- Map all Xerician fabric destinations
- Block usage if destination outside region
- Check all teleport options

**Development Phase:** Implementation

---

#### Pitfall: Construction Cape Teleport

**Description:** Construction cape teleports to POH, which may be outside region.

**Warning Signs:**
- Skillcape teleports not checked
- POH location not validated

**Prevention Strategy:**
- Block all skillcape teleports
- Check POH location against allowed regions
- Map all skillcape destinations

**Development Phase:** Implementation

---

#### Pitfall: Jewelry Teleports (Glory, Duel, Games, etc.)

**Description:** Multiple jewelry items with various teleport options.

**Prevention Strategy:**
- Map all jewelry destinations:
  - Amulet of Glory (Edgeville, Karamja, Draynor, Al Kharid)
  - Ring of Dueling (Castle Wars, Ferox, Duel Arena)
  - Games Necklace (Burthorpe, Barbarian Outpost, Corporeal Beast, etc.)
  - Skills Necklace (Fishing Guild, Mining Guild, etc.)
  - Combat Bracelet (Warriors' Guild, Champions' Guild, etc.)
- Block via item interaction
- Test all charge levels (uncharged jewelry may have different options)

**Development Phase:** Implementation

---

#### Pitfall: Special Attack Teleports

**Description:** Some weapons have teleport special attacks (Pharaoh's Sceptre).

**Prevention Strategy:**
- Block special attack usage if weapon teleports
- Map special attack destinations
- Test all special attack weapons

**Development Phase:** Implementation

---

## 3. UIM-Specific Considerations

### 3.1 Death Storage Mechanics

#### Pitfall: Death Storage for Items

**Description:** UIMs use death storage (Zulrah, Vorkath, Hespori) to temporarily store items. Death occurs outside Varlamore region.

**Warning Signs:**
- Player dies at boss for storage
- Boss is outside locked region
- Retrieval requires leaving region

**Prevention Strategy:**
- Allow death storage exceptions (intentional mechanic)
- Track death storage usage
- Warn player they're leaving region for death
- Provide config option to allow/disallow

**Development Phase:** Implementation & Design Decision

**Death Storage Locations:**
- Zulrah (Zul-Andra, outside Varlamore)
- Vorkath (Ungael, outside Varlamore)
- Hespori (Farming Guild, may be outside)
- Galvek (quest boss)

**Recommended Approach:**
```java
@ConfigItem(
    keyName = "allowDeathStorage",
    name = "Allow Death Storage",
    description = "Allow travel to bosses for UIM death storage mechanic"
)
default boolean allowDeathStorage() {
    return true; // UIM essential mechanic
}
```

---

### 3.2 Looting Bag Interactions

#### Pitfall: Looting Bag Usage Restrictions

**Description:** Looting bag is a UIM inventory management tool. No travel restrictions, but ensure interactions work.

**Prevention Strategy:**
- Don't block looting bag usage
- Ensure plugin doesn't interfere with bag mechanics
- Test opening/closing bag

**Development Phase:** Testing

---

### 3.3 Hespori Patch and Farming

#### Pitfall: Hespori Death Storage Location

**Description:** Hespori patch is in Farming Guild. If Farming Guild is outside region, death storage is inaccessible.

**Prevention Strategy:**
- Check if Farming Guild in locked region
- If outside, warn player about Hespori limitation
- Document in plugin description

**Development Phase:** Design & Documentation

---

### 3.4 Item Drop Tricks

#### Pitfall: Item Drop Trading to Bypass Restrictions

**Description:** UIMs sometimes drop items to another account or world hop to manipulate items.

**Prevention Strategy:**
- No client-side prevention (server-side behavior)
- Not a plugin concern (can't enforce)
- Focus on travel restrictions only

**Development Phase:** N/A (Out of Scope)

---

## 4. Plugin Hub Submission Pitfalls

### 4.1 Naming Conventions

#### Pitfall: Generic or Misleading Names

**Description:** Names like "Helper", "Utility", "Tool" are rejected.

**Prevention Strategy:**
- Use specific, descriptive names
- Include game mode if relevant (UIM, HCIM)
- Include region/area if area-locked

**Development Phase:** Initial Setup

**Good Names:**
- "Varlamore UIM"
- "Kourend Area Lock"
- "Wilderness-Locked Ironman"

**Bad Names:**
- "Area Lock Helper"
- "UIM Tool"
- "Restriction Plugin"

---

### 4.2 Code Quality Requirements

#### Pitfall: Code Style Violations

**Description:** Plugin Hub expects consistent code style.

**Prevention Strategy:**
- Use RuneLite code style (Google Java Style)
- Run Checkstyle before submission
- Format code consistently
- Remove all debugging code

**Development Phase:** Pre-Submission

---

#### Pitfall: Unused Dependencies

**Description:** Including dependencies not used in code.

**Prevention Strategy:**
- Remove unused imports
- Only include necessary dependencies
- Check build.gradle for unused deps

**Development Phase:** Pre-Submission

---

### 4.3 Resource and Asset Restrictions

#### Pitfall: Oversized Assets

**Description:** Plugin Hub limits asset file sizes.

**Prevention Strategy:**
- Compress images (PNG optimization)
- Keep total plugin size under 1MB
- Don't bundle unnecessary resources

**Development Phase:** Asset Creation

---

#### Pitfall: Copyright/Licensed Assets

**Description:** Using copyrighted images or sprites without permission.

**Prevention Strategy:**
- Create original assets
- Use open-source/public domain assets
- Credit asset sources
- Don't extract game assets directly

**Development Phase:** Asset Creation

---

### 4.4 Third-Party Dependency Restrictions

#### Pitfall: Non-Approved Dependencies

**Description:** Plugin Hub has approved dependency list. Others are rejected.

**Prevention Strategy:**
- Use only RuneLite-approved dependencies
- Check Plugin Hub guidelines for allowed deps
- Request approval for new dependencies before using

**Development Phase:** Architecture & Build Setup

**Commonly Approved:**
- Gson (JSON parsing)
- Guava (utilities)
- Lombok (code generation)
- Apache Commons (specific modules)

**Commonly Rejected:**
- HTTP clients (external requests)
- Database drivers
- Heavy frameworks

---

## 5. Common First-Time Plugin Developer Mistakes

### 5.1 Not Understanding RuneLite Event System

#### Pitfall: Polling Instead of Event-Driven

**Description:** Checking game state every tick instead of using events.

**Prevention Strategy:**
- Use `@Subscribe` for all game state changes
- Rely on events, not polling
- Understand event lifecycle and timing

**Development Phase:** Architecture & Implementation

**Example:**
```java
// WRONG - polling
@Subscribe
public void onGameTick(GameTick event) {
    if (client.getWidget(TELEPORT_WIDGET) != null) {
        // Process teleport
    }
}

// CORRECT - event-driven
@Subscribe
public void onWidgetLoaded(WidgetLoaded event) {
    if (event.getGroupId() == TELEPORT_WIDGET_GROUP) {
        // Process teleport
    }
}
```

---

### 5.2 Not Handling Plugin Enable/Disable Properly

#### Pitfall: State Persists After Disable

**Description:** Plugin state not cleaned up in `shutDown()`, causing issues on re-enable.

**Prevention Strategy:**
- Clear all collections in `shutDown()`
- Remove all overlays and UI elements
- Unsubscribe from manual listeners
- Reset state to initial conditions

**Development Phase:** Implementation

**Example:**
```java
@Override
protected void shutDown() {
    // Remove UI
    if (navButton != null) {
        clientToolbar.removeNavigation(navButton);
    }

    // Remove overlays
    overlayManager.remove(npcOverlay);

    // Clear state
    hiddenNpcs.clear();
    teleportDestCache.clear();

    // Reset components
    travelEngine = null;
    boundaryManager = null;
}
```

---

### 5.3 Memory Leaks from Event Subscriptions

#### Pitfall: Not Removing Manual Listeners

**Description:** Adding listeners without cleanup causes memory leaks.

**Prevention Strategy:**
- Use `@Subscribe` (automatic cleanup)
- If manual listeners needed, remove in `shutDown()`
- Use weak references for long-lived objects

**Development Phase:** Implementation

---

### 5.4 Not Testing Edge Cases

#### Pitfall: Only Testing Happy Path

**Description:** Testing only successful scenarios, missing edge cases.

**Prevention Strategy:**
- Test null values
- Test boundary conditions
- Test with minimal/maximal values
- Test rapid enable/disable
- Test with different account states (logged out, different regions)

**Development Phase:** Testing

**Test Cases:**
- Player logged out when plugin enabled
- Player in different region
- Config changes while plugin running
- Rapid world hopping
- Death and respawn
- Cutscene interruptions

---

### 5.5 Not Handling Null Safely

#### Pitfall: NullPointerException Crashes

**Description:** Accessing Client API that can return null without checking.

**Prevention Strategy:**
- Always null-check Client API returns
- Use Optional for uncertain values
- Defensive programming for game state

**Development Phase:** Implementation

**Common Null Returns:**
- `client.getLocalPlayer()` - null when logged out
- `client.getWidget()` - null if widget not loaded
- `npc.getCanvasTextLocation()` - null if off-screen

**Example:**
```java
@Subscribe
public void onGameTick(GameTick event) {
    Player player = client.getLocalPlayer();
    if (player == null) {
        return; // Not logged in
    }

    WorldPoint location = player.getWorldLocation();
    if (location == null) {
        return; // Invalid state
    }

    // Safe to proceed
    processLocation(location);
}
```

---

## 6. Development Phase Checklist

### Phase 1: Initial Setup
- [ ] Choose BSD-2-Clause license
- [ ] Create LICENSE file
- [ ] Choose specific, descriptive plugin name
- [ ] Update runelite-plugin.properties with accurate metadata
- [ ] Set up build configuration (no external dependencies)
- [ ] Configure code style (Checkstyle)

### Phase 2: Architecture
- [ ] Design event-driven architecture (no polling)
- [ ] Plan for config changes at runtime
- [ ] Design for proper lifecycle (startup, shutdown)
- [ ] Map all teleport methods and destinations
- [ ] Define boundary with buffer zone

### Phase 3: Implementation
- [ ] Use @Subscribe for all events
- [ ] Null-check all Client API calls
- [ ] Implement proper cleanup in shutDown()
- [ ] Subscribe to ConfigChanged
- [ ] Cache expensive calculations
- [ ] Optimize menu entry processing
- [ ] Handle NPC despawn/respawn
- [ ] Validate all config values

### Phase 4: Testing
- [ ] Test logged out state
- [ ] Test all teleport methods (spells, items, objects, NPCs)
- [ ] Test boundary edges
- [ ] Test instanced areas
- [ ] Test death mechanics
- [ ] Test world hopping
- [ ] Test plugin enable/disable
- [ ] Test rapid config changes
- [ ] Test with different account states

### Phase 5: Polish
- [ ] Remove all compiler warnings
- [ ] Remove unused imports and code
- [ ] Add meaningful comments and Javadoc
- [ ] Optimize overlay rendering
- [ ] Profile performance (tick processing, menu entries)
- [ ] Compress assets
- [ ] Write comprehensive README

### Phase 6: Pre-Submission
- [ ] Verify BSD-2-Clause license present
- [ ] Verify plugin metadata accurate
- [ ] Run Checkstyle (no violations)
- [ ] No external HTTP requests
- [ ] No unapproved dependencies
- [ ] Plugin size under 1MB
- [ ] Test in production RuneLite client
- [ ] Create GitHub repository with issues enabled

### Phase 7: Submission
- [ ] Create Plugin Hub pull request
- [ ] Respond to reviewer feedback
- [ ] Address all rejection reasons
- [ ] Verify plugin works after merge

---

## 7. Critical Risk Matrix

| Risk | Severity | Likelihood | Mitigation Priority |
|------|----------|-----------|---------------------|
| Plugin Hub rejection (license) | High | Medium | Critical - Phase 1 |
| Memory leaks from event subscriptions | High | High | Critical - Phase 3 |
| Missed teleport methods | High | High | Critical - Phase 3 |
| Threading issues (Client API on wrong thread) | High | Medium | Critical - Phase 3 |
| Performance issues (tick processing) | Medium | High | High - Phase 4 |
| NPC replacement glitches | Medium | Medium | High - Phase 3 |
| Death mechanics breaking lock | High | Low | Medium - Phase 4 |
| Boundary edge cases | Medium | Medium | Medium - Phase 3 |
| Config validation missing | Low | Medium | Low - Phase 3 |
| Asset copyright issues | High | Low | Medium - Phase 5 |

---

## 8. References and Resources

### RuneLite Documentation
- Plugin Hub submission guidelines: https://github.com/runelite/runelite/wiki/Plugin-Hub
- Plugin development guide: https://github.com/runelite/runelite/wiki/Building-with-Gradle
- API documentation: https://static.runelite.net/api/runelite-api/

### Common Patterns
- Study existing plugins: Region Locker, Menu Entry Swapper, NPC Indicators
- Review Plugin Hub approved plugins for examples
- Join RuneLite Discord for developer support

### OSRS Game Mechanics
- Teleport methods wiki: https://oldschool.runescape.wiki/w/Teleportation
- Map regions: https://oldschool.runescape.wiki/w/Coordinates
- UIM death storage: https://oldschool.runescape.wiki/w/Ultimate_Ironman_Guide

---

## Conclusion

Building a RuneLite area-locked account plugin requires careful attention to:

1. **Plugin Hub Standards** - License, naming, code quality, dependencies
2. **RuneLite API Proper Usage** - Event-driven, threading, lifecycle
3. **Performance** - Efficient processing, caching, minimal overhead
4. **Comprehensive Coverage** - All teleport methods, edge cases, special mechanics
5. **Testing** - Edge cases, boundary conditions, state transitions

**Most Critical Pitfalls to Avoid:**
1. Wrong license (instant rejection)
2. Threading issues (crashes)
3. Memory leaks (performance degradation)
4. Missed teleport methods (incomplete restriction)
5. Poor performance (lag, FPS drops)

**Development Philosophy:**
- Start simple (MVP with core features)
- Test incrementally (one feature at a time)
- Profile early (catch performance issues)
- Document thoroughly (maintainability)
- Follow conventions (Plugin Hub approval)

By systematically addressing each pitfall category and following the phase checklist, the plugin can achieve reliable area-lock enforcement while meeting Plugin Hub standards.

---

**Document Version:** 1.0
**Last Updated:** 2026-02-16
**Author:** Research Agent
**Status:** Complete
