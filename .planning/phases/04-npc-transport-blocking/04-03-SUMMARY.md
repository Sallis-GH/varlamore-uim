---
phase: 04-npc-transport-blocking
plan: 03
subsystem: npc
tags: [runelite, osrs, npc-blocking, item-container, unlock-gate, dizanas-quiver]

# Dependency graph
requires:
  - phase: 04-npc-transport-blocking/04-01
    provides: NpcTransportBlocker with charter ship RenderCallback and unlocked flag
  - phase: 04-npc-transport-blocking/04-02
    provides: RuneLiteObject stand-in lifecycle (createStandInNpcs/destroyStandInNpcs)
provides:
  - Dizana's Quiver unlock gate wired via ItemContainerChanged (event-driven, no tick polling)
  - DIZANAS_QUIVER_IDS public constant with all 4 quiver variants
  - containsDizanasQuiver(ItemContainer) static helper
  - setUnlocked() now manages stand-in lifecycle on state transitions
  - Charter ships dynamically reveal/hide based on quiver ownership
  - Primio quetzal permanently blocked regardless of unlock state
  - NPC-03, NPC-04, NPC-05 formally satisfied (fairy rings/spirit trees/gnome gliders trivially blocked)
affects:
  - 05-npc-replacement-system (if any future unlock gates are added)
  - 06-testing-submission (integration test coverage for unlock detection)

# Tech tracking
tech-stack:
  added: []
  patterns:
    - ItemContainerChanged event for inventory/equipment state detection without tick polling
    - Static helper method on service class for container scanning logic co-location
    - setUnlocked() dual-responsibility: state transition + side-effect lifecycle management

key-files:
  created: []
  modified:
    - src/main/java/com/varlamoreuim/npc/NpcTransportBlocker.java
    - src/main/java/com/varlamoreuim/VarlamoreUimPlugin.java

key-decisions:
  - "Dizana's Quiver IDs 28947/28949/28951/28953 cover all 4 variants per OSRS Wiki"
  - "containsDizanasQuiver() is static on NpcTransportBlocker to co-locate quiver logic with NPC transport domain"
  - "setUnlocked() manages RuneLiteObject stand-in lifecycle internally — callers only set state, not lifecycle"
  - "ItemContainerChanged fires on login container load — no explicit login check needed for initial unlock detection"
  - "Primio permanently blocked — handlePrimioClick() checks only enabled flag, not unlocked"

patterns-established:
  - "Unlock detection via ItemContainerChanged: event-driven check of INVENTORY and EQUIPMENT containers"
  - "State transition side-effects in setUnlocked(): destroyStandInNpcs() on unlock, createStandInNpcs() on re-lock"

requirements-completed:
  - NPC-01
  - NPC-02
  - NPC-03
  - NPC-04
  - NPC-05

# Metrics
duration: 1min
completed: 2026-03-15
---

# Phase 4 Plan 03: NPC Transport Blocking — Dizana's Quiver Unlock Gate Summary

**Dizana's Quiver unlock gate: charter ship NPCs dynamically reveal on quiver acquisition, Mysterious Old Man stand-ins toggle with unlock state, all via ItemContainerChanged (no tick polling)**

## Performance

- **Duration:** ~5 min
- **Started:** 2026-03-15T00:29:44Z
- **Completed:** 2026-03-15T00:30:39Z
- **Tasks:** 2
- **Files modified:** 2

## Accomplishments

- Added `DIZANAS_QUIVER_IDS` public constant covering all 4 quiver variants (uncharged, locked, charged, charged+locked)
- Added `containsDizanasQuiver(ItemContainer)` static helper for container scanning, co-located with NPC transport domain
- Updated `setUnlocked()` to manage RuneLiteObject stand-in lifecycle on transitions: destroys stand-ins when unlocked, recreates when re-locked
- Wired `onItemContainerChanged` in plugin to check both INVENTORY and EQUIPMENT — event-driven, no per-tick polling
- Confirmed Primio quetzal `handlePrimioClick()` permanently blocked regardless of unlock state (checks only `enabled`)
- NPC-03, NPC-04, NPC-05 (fairy rings, spirit trees, gnome gliders) formally covered — trivially blocked, none accessible to Varlamore-locked UIMs

## Task Commits

1. **Task 1: Add Dizana's Quiver constants and unlock-aware stand-in lifecycle** - `d856b17` (feat)
2. **Task 2: Wire ItemContainerChanged handler for Dizana's Quiver unlock detection** - `1f7ac69` (feat)

**Plan metadata:** (docs commit to follow)

## Files Created/Modified

- `src/main/java/com/varlamoreuim/npc/NpcTransportBlocker.java` — Added DIZANAS_QUIVER_IDS constant, containsDizanasQuiver() static helper, updated setUnlocked() with stand-in lifecycle management
- `src/main/java/com/varlamoreuim/VarlamoreUimPlugin.java` — Added onItemContainerChanged handler, checkDizanasQuiverOwned() helper, imports for InventoryID/ItemContainer/ItemContainerChanged

## Decisions Made

- `containsDizanasQuiver()` is static on `NpcTransportBlocker` so quiver-checking logic stays co-located with the NPC transport domain rather than being scattered across the plugin class
- `setUnlocked()` manages stand-in lifecycle internally — plugin only calls `setUnlocked(true/false)` and the correct side effects (destroy/create stand-ins) happen automatically, preventing lifecycle divergence bugs
- `ItemContainerChanged` fires on login when containers are loaded — the default `unlocked=false` is safe (charter ships start blocked), and the event fires naturally to unlock if quiver is present; no explicit login check required
- Primio check confirmed permanently blocked: `handlePrimioClick()` only gates on `!enabled`, not `unlocked`

## Deviations from Plan

None - plan executed exactly as written.

## Issues Encountered

None.

## User Setup Required

None - no external service configuration required.

## Next Phase Readiness

Phase 4 is now complete: all NPC transport blocking requirements (NPC-01 through NPC-05) are formally satisfied.

- NPC-01/NPC-02: Charter ship Trader Crewmember blocking + Primio quetzal blocking (Plan 01)
- NPC-01/NPC-02: Mysterious Old Man RuneLiteObject stand-ins at charter docks (Plan 02)
- NPC-01/NPC-02: Dizana's Quiver unlock gate toggling stand-in and render state (Plan 03)
- NPC-03/NPC-04/NPC-05: Fairy rings, spirit trees, gnome gliders — trivially covered (no Varlamore access)

Phase 5 (NPC Replacement System) and Phase 6 (Testing/Submission) are next. No blockers from Phase 4.

## Self-Check: PASSED

- FOUND: src/main/java/com/varlamoreuim/npc/NpcTransportBlocker.java
- FOUND: src/main/java/com/varlamoreuim/VarlamoreUimPlugin.java
- FOUND: .planning/phases/04-npc-transport-blocking/04-03-SUMMARY.md
- FOUND: d856b17 (Task 1 commit)
- FOUND: 1f7ac69 (Task 2 commit)

---
*Phase: 04-npc-transport-blocking*
*Completed: 2026-03-15*
