---
phase: 03-item-minigame-teleport-blocking
plan: 02
subsystem: teleport-blocking
tags: [runelite, osrs, item-teleport, menu-option-clicked, per-destination, minigame-blocking, varbit]

requires:
  - phase: 03-item-minigame-teleport-blocking
    plan: 01
    provides: ItemTeleportBlocker service with BLOCKED_ITEM_IDS, handleMenuClick signature with Client parameter

provides:
  - Per-destination blocking for Ring of Dueling (allow Fortis Colosseum, block 3 others)
  - Per-destination blocking for Hunter cape and Hunter cape (t) (allow Hunter Guild, block Feldip Hunter area and Carnivorous chinchompas)
  - Per-destination blocking for 26 Max cape variants (delegate Hunter destinations to Hunter handler, block all other teleport options)
  - House teleport tablet conditional blocking via VarBit 2187 (Aldarin = 8 = allowed)
  - handleMinigameTeleport method blocking all 17 minigame grouping tab destinations
  - blockMinigameTeleports config toggle at position 5 in restrictionsSection
  - sendBlockedDestinationMessage for per-destination messages including specific destination name
  - Short-circuit dispatch in onMenuOptionClicked: spell -> item -> minigame

affects:
  - 03-03 (if applicable — phase 3 is now complete for item + minigame blocking)
  - any future phase adding item-based blocking

tech-stack:
  added: []
  patterns:
    - Per-destination blocking pattern: item ID set check -> option string matching -> allow/block per destination
    - VarBit-based conditional blocking: client.getVarbitValue(VARBIT_ID) for POH location check
    - Minigame tab blocking: non-item-op event with "Teleport" option + target name set lookup
    - Shared message helper pattern: sendBlockedDestinationMessage for both item and minigame per-destination feedback

key-files:
  created: []
  modified:
    - src/main/java/com/varlamoreuim/teleport/ItemTeleportBlocker.java
    - src/main/java/com/varlamoreuim/VarlamoreUimPlugin.java
    - src/main/java/com/varlamoreuim/VarlamoreUimConfig.java

key-decisions:
  - "RING_OF_DUELING_IDS checked BEFORE BLOCKED_ITEM_IDS to prevent Ring of Dueling from being blocked by generic item check"
  - "MAX_CAPE_IDS includes all 26 functional Max cape variants (base + fire, saradomin, zamorak, guthix, accumulator, ardougne, infernal, imbued variants, assembler, mythical, masori, dizanas)"
  - "handleMinigameTeleport placed on ItemTeleportBlocker (not SpellTeleportBlocker) to share chat message utilities; called independently from handleMenuClick"
  - "House tablet defaults to blocking (safe fallback) when VarBit returns value other than Aldarin (8)"
  - "Max cape teleport option check uses isTeleportOption() which catches both exact TELEPORT_OPTIONS strings and strings containing 'Teleport'"
  - "Item teleport blocking now short-circuits with return statement (was missing in 03-01 wiring)"

patterns-established:
  - "Per-destination blocking: ITEM_IDS.contains(itemId) -> method(event, option, itemName, chatMessageManager) -> option string matching -> allow or block"
  - "VarBit conditional: client.getVarbitValue(VARBIT) == ALLOWED_VALUE ? allow : block"
  - "Minigame tab blocking: !'Teleport'.equals(event.getMenuOption()) check as fast-exit guard before destination lookup"

requirements-completed:
  - ITEM-01
  - ITEM-02
  - ITEM-04
  - ITEM-05

duration: 20min
completed: 2026-02-17
---

# Phase 3 Plan 02: Per-Destination Blocking and Minigame Grouping Tab Summary

**Per-destination Ring of Dueling/Hunter/Max cape blocking via option-string matching, VarBit-based house tablet POH detection, and 17-destination minigame grouping tab blocking with independent config toggle**

## Performance

- **Duration:** 20 min
- **Started:** 2026-02-17T00:35:00Z
- **Completed:** 2026-02-17T00:55:00Z
- **Tasks:** 2
- **Files modified:** 3

## Accomplishments

- Extended ItemTeleportBlocker with per-destination handling for Ring of Dueling (allow Fortis Colosseum), Hunter cape (allow Hunter Guild), and 26 Max cape variants (delegate to Hunter handler + block all other teleport options). All use option-string matching since right-click menus show destination names directly.
- Added VarBit-based house tablet conditional blocking: VarBit 2187 checked against value 8 (Aldarin) — only allows the teleport when POH is confirmed in Varlamore, defaults to blocking otherwise.
- Added handleMinigameTeleport covering all 17 minigame grouping tab destinations with independent blockMinigameTeleports config toggle at position 5, dispatched after item blocker in onMenuOptionClicked.

## Task Commits

Each task was committed atomically:

1. **Task 1: Add per-destination blocking, house tablet, and minigame handling to ItemTeleportBlocker** - `c6a0b2a` (feat)
2. **Task 2: Add blockMinigameTeleports config toggle and wire minigame blocking in plugin** - `e5d26f8` (feat)

## Files Created/Modified

- `src/main/java/com/varlamoreuim/teleport/ItemTeleportBlocker.java` - Added RING_OF_DUELING_IDS, HUNTER_CAPE_IDS, MAX_CAPE_IDS (26 variants), HOUSE_TABLET_IDS, BLOCKED_MINIGAME_DESTINATIONS (17 entries), per-destination handler methods, handleMinigameTeleport, sendBlockedDestinationMessage, sendBlockedMinigameMessage; updated handleMenuClick dispatch order
- `src/main/java/com/varlamoreuim/VarlamoreUimConfig.java` - Added blockMinigameTeleports toggle at position 5 in restrictionsSection (default true)
- `src/main/java/com/varlamoreuim/VarlamoreUimPlugin.java` - Added minigame blocking dispatch with its own config guard; fixed item blocker short-circuit (missing return statement from 03-01)

## Decisions Made

- The item teleport blocker short-circuit was missing from 03-01's wiring (it called `handleMenuClick` but didn't return early if handled). Fixed as part of Task 2 wiring — this is correctness improvement, not scope creep.
- Max cape variants checked against all named constants available in RuneLite API 1.12.17. Broken/degraded variants (FIRE_MAX_CAPE_BROKEN, etc.) excluded since they have no teleport functionality.
- Dizanas Max Cape and Masori Assembler Max Cape included as they are obtainable Max cape variants with teleport functionality.

## Deviations from Plan

### Auto-fixed Issues

**1. [Rule 1 - Bug] Fixed missing return statement in item teleport blocker dispatch**
- **Found during:** Task 2 (VarlamoreUimPlugin.java wiring)
- **Issue:** 03-01 wired `itemTeleportBlocker.handleMenuClick(...)` without checking return value or returning early. This meant minigame events could be double-processed by both item blocker (which correctly returns false for non-item-ops) and minigame blocker.
- **Fix:** Added `if (itemTeleportBlocker.handleMenuClick(...)) { return; }` pattern consistent with spell blocker wiring
- **Files modified:** src/main/java/com/varlamoreuim/VarlamoreUimPlugin.java
- **Verification:** Build compiles clean; dispatch chain matches plan spec
- **Committed in:** e5d26f8 (Task 2 commit)

---

**Total deviations:** 1 auto-fixed (Rule 1 - Bug)
**Impact on plan:** Fix restores intended short-circuit behavior. No scope creep.

## Issues Encountered

- Max cape has many item ID variants in RuneLite API (26 total). Checked ItemID sources jar directly to enumerate all functional variants. Excluded broken/degraded variants (FIRE_MAX_CAPE_BROKEN, INFERNAL_MAX_CAPE_BROKEN, etc.) since they don't teleport.

## User Setup Required

None - no external service configuration required.

## Next Phase Readiness

- Phase 3 item and minigame teleport blocking is now complete (ITEM-01 through ITEM-05 all satisfied across 03-01 and 03-02)
- Three independent config toggles operational: blockSpellTeleports, blockItemTeleports, blockMinigameTeleports
- VarBit 2187 / Aldarin value 8 requires in-game verification during playtesting
- In-game testing should confirm: Ring of Dueling Fortis Colosseum allowed, Hunter Guild allowed, house tab blocked outside Aldarin, all 17 minigame tab destinations blocked

---
*Phase: 03-item-minigame-teleport-blocking*
*Completed: 2026-02-17*

## Self-Check: PASSED

- FOUND: src/main/java/com/varlamoreuim/teleport/ItemTeleportBlocker.java
- FOUND: src/main/java/com/varlamoreuim/VarlamoreUimConfig.java
- FOUND: src/main/java/com/varlamoreuim/VarlamoreUimPlugin.java
- FOUND: .planning/phases/03-item-minigame-teleport-blocking/03-02-SUMMARY.md
- FOUND: commit c6a0b2a (Task 1)
- FOUND: commit e5d26f8 (Task 2)
- Build: SUCCESSFUL (0 errors, deprecated API note is pre-existing)
