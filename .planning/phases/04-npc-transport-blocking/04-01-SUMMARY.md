---
phase: 04-npc-transport-blocking
plan: 01
subsystem: npc
tags: [runelite, npc-hiding, rendercallback, charter-ships, primio, menu-blocking]

# Dependency graph
requires:
  - phase: 03-item-blocking
    provides: chat message pattern (ChatMessageBuilder + ChatMessageManager) and onMenuOptionClicked delegation pattern
provides:
  - NpcTransportBlocker service with RenderCallback for charter ship hiding (15510-15533)
  - handlePrimioClick() for Primio quetzal (NPC 12889) menu blocking
  - handleCharterClick() safety-net for charter NPC menu interactions
  - blockNpcTransport config toggle in restrictions section
  - setEnabled/setUnlocked/isUnlocked state API for unlock gate wiring (Plan 03)
affects:
  - 04-02 (RuneLiteObject stand-ins — builds on this blocker foundation)
  - 04-03 (Dizana's Quiver unlock gate — calls setUnlocked() on this service)

# Tech tracking
tech-stack:
  added: [net.runelite.client.callback.RenderCallbackManager, net.runelite.client.callback.RenderCallback]
  patterns: [RenderCallback.addEntity() returning false to suppress NPC rendering, WorldView.npcs().byIndex() for NPC array index lookup]

key-files:
  created:
    - src/main/java/com/varlamoreuim/npc/NpcTransportBlocker.java
  modified:
    - src/main/java/com/varlamoreuim/VarlamoreUimConfig.java
    - src/main/java/com/varlamoreuim/VarlamoreUimPlugin.java

key-decisions:
  - "Use WorldView.npcs().byIndex(event.getId()) — getCachedNPCs() does not exist in this RuneLite API version"
  - "Primio quetzal permanently blocked (no unlock gate) — direct Varrock route, no Varlamore-internal value"
  - "Charter ship NPC IDs 15510-15533 cover full range pending in-game verification of which IDs are actively spawned"
  - "RenderCallback registered/unregistered in startUp/shutDown — unregister happens before null assignment"
  - "enabled state synced from config on each game tick so RenderCallback respects toggle changes without restart"

patterns-established:
  - "NPC hiding via RenderCallback.addEntity() returning false for target NPC IDs"
  - "NPC index lookup: WorldView.npcs().byIndex() not client.getCachedNPCs() (method does not exist)"
  - "Service class pattern: plain Java class with no @Inject, instantiated by plugin in startUp()"

requirements-completed:
  - NPC-01
  - NPC-02

# Metrics
duration: 3min
completed: 2026-03-15
---

# Phase 4 Plan 01: NPC Transport Blocking Summary

**RenderCallback-based charter ship NPC hiding (IDs 15510-15533) plus Primio quetzal menu-click blocking, wired into plugin via blockNpcTransport config toggle**

## Performance

- **Duration:** 3 min
- **Started:** 2026-03-15T20:15:54Z
- **Completed:** 2026-03-15T20:19:18Z
- **Tasks:** 2
- **Files modified:** 3 (1 created, 2 modified)

## Accomplishments
- Created `NpcTransportBlocker` service in new `npc/` package with RenderCallback that hides all 24 charter ship Trader Crewmember NPC IDs across Sunset Coast, Aldarin, and Fortis Cothon
- Implemented `handlePrimioClick()` to block NPC 12889 interactions with lore-friendly "The bird doesn't seem interested in interacting with you." message
- Implemented `handleCharterClick()` as a safety-net fallback for charter interactions when RenderCallback is active
- Added `blockNpcTransport` config toggle at position 7 in the restrictions section
- Wired RenderCallback registration in `startUp()` and unregistration in `shutDown()`

## Task Commits

Each task was committed atomically:

1. **Task 1: Create NpcTransportBlocker service** - `5089f72` (feat)
2. **Task 2: Add config toggle and wire into plugin** - `40648ae` (feat)

**Plan metadata:** (created next)

## Files Created/Modified
- `src/main/java/com/varlamoreuim/npc/NpcTransportBlocker.java` - NPC hiding service with RenderCallback, Primio blocking, charter safety-net, enabled/unlocked state management
- `src/main/java/com/varlamoreuim/VarlamoreUimConfig.java` - Added `blockNpcTransport()` at position 7 in restrictions section
- `src/main/java/com/varlamoreuim/VarlamoreUimPlugin.java` - Added `RenderCallbackManager` injection, `NpcTransportBlocker` lifecycle, config sync in `onGameTick`, NPC blocking chain in `onMenuOptionClicked`

## Decisions Made
- `WorldView.npcs().byIndex(event.getId())` is the correct NPC lookup — `client.getCachedNPCs()` does not exist in this RuneLite API version
- Primio quetzal is permanently blocked (no unlock gate) — it is a direct Varrock route with no Varlamore-internal value; the Dizana's Quiver unlock is appropriate for charter ships only
- Full NPC ID ranges 15510-15533 are blocked pending in-game verification of which IDs are actively spawned at each dock

## Deviations from Plan

### Auto-fixed Issues

**1. [Rule 3 - Blocking] Replaced non-existent getCachedNPCs() with WorldView.npcs().byIndex()**
- **Found during:** Task 1 (NpcTransportBlocker service creation)
- **Issue:** Plan specified `client.getCachedNPCs()[event.getId()]` but `getCachedNPCs()` does not exist on the `Client` interface in RuneLite API 1.12.17/1.12.20. Build failed with "cannot find symbol".
- **Fix:** Used `client.getTopLevelWorldView().npcs().byIndex(npcIndex)` — the `IndexedObjectSet` returned by `WorldView.npcs()` provides `byIndex(int)` method for index-based NPC lookup. Null-checks added for both WorldView and the result.
- **Files modified:** `src/main/java/com/varlamoreuim/npc/NpcTransportBlocker.java`
- **Verification:** `./gradlew build` succeeded after the fix
- **Committed in:** `5089f72` (Task 1 commit)

---

**Total deviations:** 1 auto-fixed (1 blocking API issue)
**Impact on plan:** Fix necessary for compilation — functionally equivalent to the plan's intent. The lookup semantics are identical: NPC index from `event.getId()` used to retrieve the NPC object and check its definition ID.

## Issues Encountered
None beyond the API deviation documented above.

## User Setup Required
None - no external service configuration required.

## Next Phase Readiness
- Plan 02 (RuneLiteObject stand-ins) can build on this foundation — `npcTransportBlocker` is exposed to the plugin and the `unlocked` state is already designed to hide stand-ins when the quiver is acquired
- Plan 03 (Dizana's Quiver unlock gate) can call `npcTransportBlocker.setUnlocked(true)` when Dizana's Quiver is detected in inventory or equipment
- Charter ship NPC IDs need in-game verification before Phase 4 is considered production-ready

## Self-Check: PASSED

- `src/main/java/com/varlamoreuim/npc/NpcTransportBlocker.java` — FOUND
- `.planning/phases/04-npc-transport-blocking/04-01-SUMMARY.md` — FOUND
- Commit `5089f72` (Task 1) — FOUND
- Commit `40648ae` (Task 2) — FOUND
- Commit `717bc77` (docs) — FOUND
- `./gradlew build` — BUILD SUCCESSFUL

---
*Phase: 04-npc-transport-blocking*
*Completed: 2026-03-15*
