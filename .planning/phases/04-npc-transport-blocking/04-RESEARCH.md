# Phase 4: NPC Transport Blocking - Research

**Researched:** 2026-03-15
**Domain:** RuneLite NPC rendering hooks, NPC event handling, inventory item detection
**Confidence:** HIGH

---

<user_constraints>
## User Constraints (from CONTEXT.md)

### Locked Decisions
- No menu-click blocking for NPC transport — transport NPCs are **hidden entirely** and replaced with stand-in NPCs.
- Blocking and replacement are one feature (Phases 4 & 5 merged).
- Primio quetzal (NPC ID 12889, Civitas illa Fortis side) is a special case — block interaction with a chat message instead of NPC replacement.
- Charter ship NPCs at 3 locations (Sunset Coast, Aldarin, Fortis Cothon) — hide and replace with Mysterious Old Man stand-ins.
- Primio quetzal uses chat message only: "The bird doesn't seem interested in interacting with you."
- Fairy rings, spirit trees, gnome gliders — skipped entirely (quest-gated, inaccessible to Varlamore-locked UIMs).
- Replacement NPCs use NPC dialogue boxes (not chat messages) for immersive feedback.
- Dialogue tone is lore-friendly excuses (in-world reasons, not meta restriction messages).
- All replacement NPCs use the same dialogue — one generic lore-friendly excuse.
- Stand-in NPC model: Mysterious Old Man.
- Transport NPCs unlocked when player acquires Dizana's Quiver (uncharged) — item ID 28947.
- Once unlocked: hide the Mysterious Old Man, show the real charter ship NPCs.

### Claude's Discretion
- Specific dialogue text for the Mysterious Old Man (within lore-friendly constraint).
- How to detect Dizana's Quiver ownership (inventory check, equipment check, or VarBit).
- Technical approach for NPC hiding/spawning.
- Whether Primio quetzal should also be unlock-gated or permanently blocked.

### Deferred Ideas (OUT OF SCOPE)
- Item-based unlock gating for other restrictions (spells, items).
- Per-location unique dialogue for replacement NPCs.
- Fairy ring / spirit tree blocking.
</user_constraints>

---

<phase_requirements>
## Phase Requirements

| ID | Description | Research Support |
|----|-------------|-----------------|
| NPC-01 | User cannot use ship/boat NPCs at Varlamore ports to travel outside the region | Charter ship Trader Crewmembers at 3 Varlamore docks: NPC IDs 15510–15517 (Sunset Coast), 15518–15525 (Aldarin), 15526–15533 (Fortis Cothon). Hide via RenderCallback.addEntity(), block interaction via onMenuOptionClicked. |
| NPC-02 | User cannot use charter ship crews to leave Varlamore | Same as NPC-01. Trader Crewmembers share the charter ship mechanism. Hide + replace with Mysterious Old Man stand-in. |
| NPC-03 | User cannot use gnome gliders to travel outside Varlamore (if present in region) | No gnome gliders in Varlamore. Out of scope per locked decisions. Requirement is trivially satisfied. |
| NPC-04 | User cannot use spirit trees to travel outside Varlamore (if present in region) | No spirit trees in Varlamore accessible to non-quest-completed players. Out of scope per locked decisions. Requirement is trivially satisfied. |
| NPC-05 | User cannot use fairy rings to travel to codes outside Varlamore (if present in region) | Fairy ring codes AJP, CKQ, AIS are in Varlamore. Access requires A Fairy Tale Part II quest — inaccessible to Varlamore-locked UIMs. Out of scope per locked decisions. Requirement is trivially satisfied. |
</phase_requirements>

---

## Summary

This phase implements NPC-based transport blocking by hiding the real charter ship Trader Crewmember NPCs and replacing them with Mysterious Old Man stand-ins who deliver a lore-friendly refusal when talked to. A second, simpler mechanism blocks the Primio quetzal via menu-click consumption with a chat message. An unlock gate based on owning Dizana's Quiver (item ID 28947) lets the player restore real charter ship access after completing the Fortis Colosseum.

The core RuneLite API for NPC hiding is the `RenderCallbackManager` (introduced as the replacement for the now-deprecated `Hooks.RenderableDrawListener`). The `addEntity(Renderable, boolean)` method on the `RenderCallback` interface returns `false` to suppress rendering of a specific NPC. NPC tracking is maintained via `NpcSpawned` and `NpcDespawned` events. Replacement NPC "dialogue" is delivered through the existing `ChatMessageManager` GAMEMESSAGE pattern already established by the plugin — there is no RuneLite API for programmatically opening a real NPC chathead dialogue box from plugin code.

The Dizana's Quiver unlock check is best done via `ItemContainer.contains(int itemId)` across both INVENTORY and EQUIPMENT containers, evaluated each game tick (or on `ItemContainerChanged`). Raw item ID 28947 (uncharged) is the unlock trigger; the charged variant (28951) and locked variants (28949, 28953) should all count.

**Primary recommendation:** Use `RenderCallbackManager` + `NpcSpawned`/`NpcDespawned` tracking for NPC hiding. Deliver refusal feedback as a GAMEMESSAGE chat message (not a real NPC dialogue box — the RuneLite API does not support opening fake dialogue). Add `onMenuOptionClicked` blocking for Primio (NPC_FIRST_OPTION on NPC ID 12889).

---

## Standard Stack

### Core
| Library / API | Version | Purpose | Why Standard |
|---------------|---------|---------|--------------|
| `net.runelite.client.callback.RenderCallbackManager` | latest.release | Suppress specific NPC rendering | Official replacement for deprecated `Hooks.RenderableDrawListener`. Entity Hider plugin pattern. |
| `net.runelite.api.events.NpcSpawned` / `NpcDespawned` | latest.release | Track charter ship NPC instances | Standard event pair for NPC lifecycle in all RuneLite NPC-tracking plugins. |
| `net.runelite.api.events.MenuOptionClicked` | latest.release | Block Primio quetzal interaction | Already used by plugin for spell/item blocking. Same pattern applies. |
| `net.runelite.api.ItemContainer` | latest.release | Detect Dizana's Quiver ownership | Standard approach for inventory/equipment item checking. |
| `net.runelite.api.InventoryID` | latest.release | Identify inventory vs equipment containers | Standard enum. Note: class itself is `@Deprecated` but values still work. |
| `net.runelite.client.chat.ChatMessageManager` | latest.release | Deliver refusal feedback messages | Already injected and used in plugin. Same GAMEMESSAGE pattern. |

### Supporting
| Library / API | Version | Purpose | When to Use |
|---------------|---------|---------|-------------|
| `net.runelite.api.events.ItemContainerChanged` | latest.release | Detect when player picks up Dizana's Quiver | Preferred over polling every tick — fires when inventory/equipment changes. |
| `net.runelite.api.events.GameTick` | latest.release | Periodic checks | Already used for boundary detection. Avoid adding Quiver polling here if ItemContainerChanged covers it. |
| `net.runelite.api.MenuAction` | latest.release | Filter NPC click types in onMenuOptionClicked | Use `NPC_FIRST_OPTION` through `NPC_FIFTH_OPTION` to detect NPC interactions. |

### Alternatives Considered
| Instead of | Could Use | Tradeoff |
|------------|-----------|----------|
| `RenderCallbackManager` | `Hooks.RenderableDrawListener` | Deprecated since ~1.12. Use RenderCallbackManager. |
| `ItemContainerChanged` for unlock detection | Poll on `GameTick` | ItemContainerChanged is event-driven and more efficient. GameTick polling works but creates unnecessary per-tick overhead. |
| Chat message for "dialogue" | Real NPC chathead dialogue | RuneLite has no public API to open a fake NPC dialogue widget. Chathead-style dialogue would require widget manipulation deep in the client — not supported via plugin API. |

**Installation:** No new dependencies required. All APIs are part of `net.runelite:client:latest.release` already declared.

---

## Architecture Patterns

### Recommended Project Structure
```
src/main/java/com/varlamoreuim/
├── teleport/
│   ├── SpellTeleportBlocker.java    # existing
│   └── ItemTeleportBlocker.java     # existing
├── npc/
│   ├── NpcTransportBlocker.java     # new: charter ship hide + Primio block
│   └── NpcTransportOverlay.java     # optional: visual indicator if needed
├── BoundaryChecker.java             # existing
├── VarlamoreUimConfig.java          # add blockNpcTransport toggle
├── VarlamoreUimPlugin.java          # wire NpcTransportBlocker
└── VarlamoreUimPanel.java           # existing
```

### Pattern 1: NPC Rendering Hook (Hide Charter Ships)

**What:** Register a `RenderCallback` that suppresses rendering of specific NPC IDs.
**When to use:** Any time you want to make an NPC visually absent from the world.

```java
// Source: EntityHiderPlugin pattern, updated to RenderCallbackManager API
// net.runelite.client.callback.RenderCallback

@Inject
private RenderCallbackManager renderCallbackManager;

private final RenderCallback renderCallback = new RenderCallback() {
    @Override
    public boolean addEntity(Renderable renderable, boolean ui) {
        if (renderable instanceof NPC) {
            NPC npc = (NPC) renderable;
            if (hiddenNpcIds.contains(npc.getId())) {
                return false; // suppress rendering
            }
        }
        return true; // show everything else
    }
};

@Override
protected void startUp() {
    renderCallbackManager.register(renderCallback);
}

@Override
protected void shutDown() {
    renderCallbackManager.unregister(renderCallback);
}
```

### Pattern 2: NPC Lifecycle Tracking (NpcSpawned / NpcDespawned)

**What:** Maintain a `Map<Integer, NPC>` of known NPC instances (index → NPC) for active manipulation.
**When to use:** Required alongside the render hook to know which NPCs are currently in world.

```java
// Source: NpcIndicatorsPlugin pattern
private final Map<Integer, NPC> trackedCharters = new HashMap<>();

@Subscribe
public void onNpcSpawned(NpcSpawned event) {
    NPC npc = event.getNpc();
    if (CHARTER_NPC_IDS.contains(npc.getId())) {
        trackedCharters.put(npc.getIndex(), npc);
    }
}

@Subscribe
public void onNpcDespawned(NpcDespawned event) {
    NPC npc = event.getNpc();
    trackedCharters.remove(npc.getIndex());
}
```

### Pattern 3: NPC Menu Click Blocking (Primio Quetzal)

**What:** Consume `MenuOptionClicked` for `NPC_FIRST_OPTION` through `NPC_FIFTH_OPTION` on a specific NPC ID.
**When to use:** Simpler case where hiding the NPC is not desired but interaction must be blocked.

```java
// Source: existing SpellTeleportBlocker pattern, adapted for NPC MenuAction
@Subscribe
public void onMenuOptionClicked(MenuOptionClicked event) {
    MenuAction action = event.getMenuAction();
    if (action == MenuAction.NPC_FIRST_OPTION
        || action == MenuAction.NPC_SECOND_OPTION
        || action == MenuAction.NPC_THIRD_OPTION
        || action == MenuAction.NPC_FOURTH_OPTION
        || action == MenuAction.NPC_FIFTH_OPTION) {
        // getNpc() available on MenuEntry, but on MenuOptionClicked use getId()
        if (event.getId() == PRIMIO_NPC_ID) {
            event.consume();
            sendBlockedBirdMessage(chatMessageManager);
        }
    }
}
```

**Note on event.getId() for NPC clicks:** When the menu action is an NPC action, `event.getId()` returns the NPC's **index** in the client NPC array, not the NPC's definition ID. The safe approach is to look up the NPC by index from the client's NPC array and check `npc.getId()`.

```java
// Safer Primio check using NPC array lookup
NPC[] npcs = client.getCachedNPCs();
int npcIndex = event.getId();
if (npcIndex >= 0 && npcIndex < npcs.length) {
    NPC npc = npcs[npcIndex];
    if (npc != null && npc.getId() == PRIMIO_NPC_ID) {
        event.consume();
        sendBlockedBirdMessage(chatMessageManager);
    }
}
```

### Pattern 4: Unlock Detection via ItemContainerChanged

**What:** Check if Dizana's Quiver is in inventory or equipment when the item containers change.
**When to use:** Persistent state that determines whether charter ships are blocked or allowed.

```java
// Source: ItemContainer.contains() API
private boolean dizanaUnlocked = false;

@Subscribe
public void onItemContainerChanged(ItemContainerChanged event) {
    int id = event.getContainerId();
    // InventoryID.INVENTORY.getId() and InventoryID.EQUIPMENT.getId()
    if (id == InventoryID.INVENTORY.getId() || id == InventoryID.EQUIPMENT.getId()) {
        refreshDizanaUnlocked();
    }
}

private void refreshDizanaUnlocked() {
    for (int quiverId : DIZANAS_QUIVER_IDS) {
        ItemContainer inv = client.getItemContainer(InventoryID.INVENTORY);
        ItemContainer equip = client.getItemContainer(InventoryID.EQUIPMENT);
        if ((inv != null && inv.contains(quiverId))
            || (equip != null && equip.contains(quiverId))) {
            dizanaUnlocked = true;
            return;
        }
    }
    dizanaUnlocked = false;
}
```

### Anti-Patterns to Avoid

- **Blocking menu entries via `MenuEntryAdded`:** Removing menu entries from an NPC that still visually exists creates confusing UX (right-click shows no options, left-click walks). The hide-and-replace approach is the correct design.
- **Using `npc.setDead(true)` to "hide" NPCs:** Setting an actor dead removes health bar and stops animations but does NOT make the NPC invisible. The NPC remains clickable and visible.
- **Polling Dizana's Quiver every `GameTick`:** Wastes CPU on every tick. Use `ItemContainerChanged` which fires only when inventory/equipment actually changes.
- **Checking only `InventoryID.INVENTORY` for unlock:** Players may have Dizana's Quiver equipped in the ammo slot. Always check both INVENTORY and EQUIPMENT containers.

---

## Don't Hand-Roll

| Problem | Don't Build | Use Instead | Why |
|---------|-------------|-------------|-----|
| NPC visibility control | Custom model swap or setDead trick | `RenderCallbackManager.register(RenderCallback)` | Official API, already used by Entity Hider. Model swap requires internal access. setDead doesn't hide the NPC. |
| Item ownership check | VarBit scanning or cache parsing | `ItemContainer.contains(int itemId)` | Direct O(1) check in the standard container API. |
| NPC chathead dialogue | Widget manipulation / script injection | `ChatMessageManager` GAMEMESSAGE | No public RuneLite API exists for opening fake NPC dialogue boxes. Widget injection is fragile and against Plugin Hub guidelines. |

**Key insight:** NPC hiding in RuneLite is a pure rendering concern — the game still processes the NPC normally, only the visual presentation is suppressed. The right place to intercept is the render callback, not the NPC data itself.

---

## Common Pitfalls

### Pitfall 1: event.getId() Returns NPC Array Index, Not NPC Definition ID

**What goes wrong:** Using `event.getId()` directly as the NPC's ID in `onMenuOptionClicked` returns the NPC's slot in the client NPC array (index), not the NPC's definition ID (like 12889).
**Why it happens:** RuneLite's `MenuOptionClicked.getId()` semantics differ by menu action type — for NPC actions it's the index, for item actions it's the item ID.
**How to avoid:** Look up the NPC via `client.getCachedNPCs()[event.getId()]` and then call `npc.getId()` to get the definition ID.
**Warning signs:** Comparing `event.getId() == 12889` never fires, or fires for wrong NPCs.

### Pitfall 2: RenderableDrawListener Is Deprecated

**What goes wrong:** Following old EntityHiderPlugin examples that use `hooks.registerRenderableDrawListener()`.
**Why it happens:** EntityHiderPlugin is widely referenced but still uses the old API. The `@Deprecated` annotation on `registerRenderableDrawListener` was added around RuneLite 1.12.
**How to avoid:** Use `RenderCallbackManager.register(RenderCallback)` instead. The `RenderCallback.addEntity(Renderable, boolean)` method replaces the old `shouldDraw(Renderable, boolean)` pattern.
**Warning signs:** Compiler deprecation warnings on `hooks.registerRenderableDrawListener`.

### Pitfall 3: NPC Index Reuse After Despawn

**What goes wrong:** Keeping a `Set<NPC>` reference to a despawned NPC — the client may reuse the slot for a different NPC in a later tick.
**Why it happens:** The NPC index is a slot in the client's NPC cache array, not a stable identity.
**How to avoid:** Remove from tracking in `onNpcDespawned`. Use `Map<Integer, NPC>` keyed on index and validate in `onNpcSpawned` that the ID still matches.
**Warning signs:** Wrong NPCs being hidden after the player moves between areas.

### Pitfall 4: Dizana's Quiver Has Four Relevant Item IDs

**What goes wrong:** Only checking for the uncharged variant (28947), missing players who have the charged (28951) or locked variants (28949, 28953).
**Why it happens:** OSRS items frequently have multiple IDs across charge states and "locked" variants.
**How to avoid:** Build a `Set<Integer> DIZANAS_QUIVER_IDS` with all four variants: 28947, 28949, 28951, 28953.
**Warning signs:** Unlock gate doesn't trigger for players who charged their quiver.

### Pitfall 5: Trader Crewmember NPC IDs Are Location-Specific

**What goes wrong:** Using a single generic NPC ID for all charter ship crew members, missing some locations.
**Why it happens:** OSRS uses different NPC IDs per port to enable per-location dialogue and behavior.
**How to avoid:** Track the full range. The wiki reports: Sunset Coast (15510–15517), Aldarin (15518–15525), Civitas illa Fortis / Fortis Cothon (15526–15533). Use a Set covering all three ranges.
**Warning signs:** Charter ship NPCs at one dock are hidden but not others.

### Pitfall 6: RenderCallback Must Be Unregistered on Plugin Shutdown

**What goes wrong:** Forgetting to call `renderCallbackManager.unregister(renderCallback)` in `shutDown()`, causing NPCs to remain hidden when the plugin is disabled.
**Why it happens:** The callback persists in the manager until explicitly removed.
**How to avoid:** Mirror every `register()` call in `startUp()` with an `unregister()` call in `shutDown()`.
**Warning signs:** NPCs remain invisible after the plugin is toggled off in the RuneLite panel.

---

## Code Examples

### Full NPC Hide Service Skeleton

```java
// Source: EntityHiderPlugin pattern + RenderCallbackManager API (current)
package com.varlamoreuim.npc;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.NPC;
import net.runelite.api.Renderable;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.callback.RenderCallback;
import net.runelite.client.callback.RenderCallbackManager;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.api.ChatMessageType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.QueuedMessage;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class NpcTransportBlocker {

    // Trader Crewmember IDs at Varlamore ports
    // Source: OSRS Wiki — Trader Crewmember
    private static final Set<Integer> CHARTER_NPC_IDS = Set.of(
        // Sunset Coast: 15510–15517 (range — add all variants in use)
        15510, 15511, 15512, 15513, 15514, 15515, 15516, 15517,
        // Aldarin: 15518–15525
        15518, 15519, 15520, 15521, 15522, 15523, 15524, 15525,
        // Civitas illa Fortis / Fortis Cothon: 15526–15533
        15526, 15527, 15528, 15529, 15530, 15531, 15532, 15533
    );

    // Primio quetzal: ID 12889 = Civitas illa Fortis side (flies to Varrock)
    // ID 12888 = Varrock east gate side (player would need to leave Varlamore to reach it)
    // Source: OSRS Wiki — Primio
    private static final int PRIMIO_NPC_ID = 12889;

    private final Map<Integer, NPC> trackedCharters = new HashMap<>();

    private boolean unlocked = false;

    private final RenderCallback renderCallback = new RenderCallback() {
        @Override
        public boolean addEntity(Renderable renderable, boolean ui) {
            if (unlocked) return true;
            if (renderable instanceof NPC) {
                NPC npc = (NPC) renderable;
                if (CHARTER_NPC_IDS.contains(npc.getId())) {
                    return false; // hide charter ship NPCs
                }
            }
            return true;
        }
    };

    // ... inject RenderCallbackManager, Client, ChatMessageManager
    // ... register/unregister on startUp/shutDown
    // ... NpcSpawned/NpcDespawned handlers
    // ... onMenuOptionClicked for Primio
    // ... setUnlocked(boolean) called from plugin when Dizana's Quiver detected
}
```

### ItemContainerChanged Handler for Unlock

```java
// Source: ItemContainer.contains() API — net.runelite.api.ItemContainer
private static final Set<Integer> DIZANAS_QUIVER_IDS = Set.of(
    28947,  // Uncharged — Source: OSRS Wiki — Dizana's quiver
    28949,  // Uncharged + Locked
    28951,  // Charged
    28953   // Charged + Locked
);

@Subscribe
public void onItemContainerChanged(ItemContainerChanged event) {
    int id = event.getContainerId();
    if (id == InventoryID.INVENTORY.getId() || id == InventoryID.EQUIPMENT.getId()) {
        boolean nowUnlocked = checkDizanaOwned();
        if (nowUnlocked != npcTransportBlocker.isUnlocked()) {
            npcTransportBlocker.setUnlocked(nowUnlocked);
            log.debug("Charter ship unlock state changed: {}", nowUnlocked);
        }
    }
}

private boolean checkDizanaOwned() {
    for (int quiverId : DIZANAS_QUIVER_IDS) {
        ItemContainer inv = client.getItemContainer(InventoryID.INVENTORY);
        ItemContainer equip = client.getItemContainer(InventoryID.EQUIPMENT);
        if ((inv != null && inv.contains(quiverId))
                || (equip != null && equip.contains(quiverId))) {
            return true;
        }
    }
    return false;
}
```

### Chat Message for Primio Quetzal Block

```java
// Source: ChatMessageManager pattern from SpellTeleportBlocker/ItemTeleportBlocker
private void sendBlockedBirdMessage(ChatMessageManager chatMessageManager) {
    String message = new ChatMessageBuilder()
        .append(Color.WHITE, "The bird doesn't seem interested in interacting with you.")
        .build();
    chatMessageManager.queue(QueuedMessage.builder()
        .type(ChatMessageType.GAMEMESSAGE)
        .runeLiteFormattedMessage(message)
        .build());
}
```

### Config Toggle Addition Pattern

```java
// Source: VarlamoreUimConfig.java existing pattern
@ConfigItem(
    keyName = "blockNpcTransport",
    name = "Block NPC Transport",
    description = "Hide charter ship NPCs and block transport NPCs that leave Varlamore",
    position = 7,
    section = restrictionsSection
)
default boolean blockNpcTransport() {
    return true;
}
```

---

## State of the Art

| Old Approach | Current Approach | When Changed | Impact |
|--------------|------------------|--------------|--------|
| `Hooks.RenderableDrawListener` | `RenderCallbackManager.register(RenderCallback)` | ~RuneLite 1.12 | Must use new API; old API still compiles but is deprecated |
| `InventoryID` enum directly | Same enum still works, class is deprecated | Ongoing | Use `InventoryID.INVENTORY.getId()` pattern; direct `client.getItemContainer(InventoryID.INVENTORY)` also still works |

**Deprecated/outdated:**
- `hooks.registerRenderableDrawListener(listener)`: Use `renderCallbackManager.register(renderCallback)`.
- `net.runelite.api.InventoryID` class: Deprecated but enum values still functional. Use `InventoryID.INVENTORY` and `InventoryID.EQUIPMENT` as before.

---

## NPC ID Reference

| NPC | ID(s) | Location | Action |
|-----|-------|----------|--------|
| Trader Crewmember (Sunset Coast) | 15510–15517 | Sunset Coast dock | Hide + replace |
| Trader Crewmember (Aldarin) | 15518–15525 | Aldarin dock | Hide + replace |
| Trader Crewmember (Fortis Cothon) | 15526–15533 | Fortis Cothon | Hide + replace |
| Primio (Civitas side) | 12889 | Civitas illa Fortis | Block interaction only |
| Mysterious Old Man | 2830, 6742, 6750, 6752, 6753 | Spawned by random event system | Stand-in model (NOT spawnable by plugin) |

**CRITICAL NOTE on Mysterious Old Man spawning:** RuneLite plugins cannot spawn new NPC instances in the game world. The "Mysterious Old Man as stand-in" requirement from the design decision is **not achievable** through the RuneLite API. The plugin can only hide NPCs from rendering — it cannot create new world NPCs. The replacement NPC appearance must be delivered differently (see Open Questions).

---

## Open Questions

1. **Mysterious Old Man spawning is not achievable via RuneLite plugin API**
   - What we know: RuneLite plugins can hide NPCs via `RenderCallback.addEntity()` returning false. They cannot spawn new NPC instances — there is no `client.spawnNpc()` or equivalent API.
   - What's unclear: Whether the CONTEXT.md intent was purely visual (show a different model) or truly spawning a new interactive NPC. If purely visual, a NPC model swap via `NpcComposition` override *might* be possible but is not documented in the public API and would be fragile. If an interactive stand-in is required, it cannot be done.
   - Recommendation: Implement as "hide the charter ship NPC, block interaction via `onMenuOptionClicked` if the player clicks where the NPC was, and show a chat message". This achieves the functional blocking goal. The visual "Mysterious Old Man" replacement is a cosmetic stretch goal that would require undocumented API access.
   - Alternative recommendation: Simply hide charter ship NPCs and rely on the menu click block + chat message for feedback. This is consistent with how other blocking works in the plugin.

2. **Trader Crewmember NPC IDs need in-game verification**
   - What we know: OSRS Wiki's Trader Crewmember page reports IDs approximately in the 15510–15533 range for Varlamore locations. The exact IDs within each range that are actually spawned (not all 8 may be used) require in-game verification.
   - What's unclear: Which specific IDs within each range are the interactable NPCs vs. decorative variants.
   - Recommendation: Block the full ranges (15510–15533) during development, then verify in-game with `./gradlew run` and the NPC Indicators plugin to confirm exact IDs.

3. **Dizana's Quiver unlock: permanent or per-session**
   - What we know: The wiki confirms item IDs 28947/28949/28951/28953. `ItemContainer.contains()` can check per-session.
   - What's unclear: UIMs cannot bank items, so Dizana's Quiver would be carried. An inventory/equipment check is sufficient. No VarBit alternative was found. Permanent state persistence across sessions would require `ConfigManager`.
   - Recommendation: Check inventory + equipment on `ItemContainerChanged`. If the quiver is ever detected in either container, set a session-local `boolean dizanaUnlocked = true`. This is sufficient since UIMs always carry the quiver.

4. **Primio quetzal: permanently blocked or unlock-gated (Claude's Discretion)**
   - What we know: Primio (12889, Civitas side) flies to Varrock, which is outside Varlamore.
   - Recommendation: Permanently blocked (no unlock gate). The Primio flight to Varrock is a core route out of Varlamore. The quiver unlock is appropriate for charter ships (internal-to-OSRS transport that serves legitimate trade purposes) but Primio is a direct Varrock link with no Varlamore-internal value.

---

## Sources

### Primary (HIGH confidence)
- RuneLite API Javadoc — `RenderCallbackManager`, `RenderCallback.addEntity()` — https://static.runelite.net/runelite-client/apidocs/net/runelite/client/callback/RenderCallbackManager.html
- RuneLite API Javadoc — `Hooks` (deprecated listener reference) — https://static.runelite.net/runelite-client/apidocs/net/runelite/client/callback/Hooks.html
- RuneLite API Javadoc — `NPC.getComposition()`, `NPC.getId()`, `NPC.getIndex()` — https://static.runelite.net/runelite-api/apidocs/net/runelite/api/NPC.html
- RuneLite API Javadoc — `NpcSpawned`, `NpcDespawned` events
- RuneLite API Javadoc — `ItemContainer.contains(int)`, `ItemContainer.getItems()` — https://static.runelite.net/runelite-api/apidocs/net/runelite/api/ItemContainer.html
- RuneLite API Javadoc — `InventoryID` enum — https://static.runelite.net/runelite-api/apidocs/net/runelite/api/InventoryID.html
- RuneLite API Javadoc — `MenuAction.NPC_FIRST_OPTION` through `NPC_FIFTH_OPTION` — https://static.runelite.net/runelite-api/apidocs/net/runelite/api/MenuAction.html
- RuneLite source — `EntityHiderPlugin.java` (NPC hide pattern) — https://github.com/runelite/runelite/blob/master/runelite-client/src/main/java/net/runelite/client/plugins/entityhider/EntityHiderPlugin.java
- OSRS Wiki — Dizana's quiver item IDs — https://oldschool.runescape.wiki/w/Dizana%27s_quiver
- OSRS Wiki — Primio NPC IDs (12888, 12889) — https://oldschool.runescape.wiki/w/Primio
- OSRS Wiki — Mysterious Old Man NPC IDs (2830, 6742, 6750, 6752, 6753) — https://oldschool.runescape.wiki/w/Mysterious_Old_Man
- OSRS Wiki — Trader Crewmember NPC IDs at Varlamore ports — https://oldschool.runescape.wiki/w/Trader_Crewmember
- Existing plugin source — `SpellTeleportBlocker.java`, `ItemTeleportBlocker.java`, `VarlamoreUimPlugin.java` (pattern reference)

### Secondary (MEDIUM confidence)
- RenderCallback interface details (addEntity method signature) — verified via RuneLite Javadoc
- Trader Crewmember ID ranges (15510–15533) — from wiki fetch; exact active IDs need in-game verification

### Tertiary (LOW confidence)
- Specific NPC IDs within each Trader Crewmember range: OSRS Wiki reported approximate ranges. The exact subset that are spawned and interactive at each dock requires in-game validation.

---

## Metadata

**Confidence breakdown:**
- Standard stack: HIGH — All APIs verified via official RuneLite Javadoc
- Architecture: HIGH — Based on EntityHiderPlugin source (official plugin, same codebase)
- Pitfalls: HIGH — Verified from API semantics and documented deprecation notices
- NPC IDs (charter ship): MEDIUM — Wiki-reported ranges, need in-game validation
- NPC spawning limitation: HIGH — Confirmed: no RuneLite API exists for spawning NPCs

**Research date:** 2026-03-15
**Valid until:** 2026-04-15 (30 days; stable APIs)

**CRITICAL PLANNING NOTE:** The "Mysterious Old Man as visible stand-in NPC" is not achievable through the RuneLite plugin API. The plan should either (a) implement NPC hiding only with chat message feedback, or (b) scope the visual replacement out and deliver functional blocking as the primary goal. This constraint must be reflected in the plan.
