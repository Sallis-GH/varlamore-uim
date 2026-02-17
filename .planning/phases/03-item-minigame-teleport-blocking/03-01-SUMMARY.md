---
phase: 03-item-minigame-teleport-blocking
plan: 01
subsystem: teleport-blocking
tags: [runelite, osrs, item-teleport, menu-option-clicked, inventory-blocking]

requires:
  - phase: 02-spell-teleport-blocking
    provides: SpellTeleportBlocker service pattern with Color.RED/WHITE chat message format

provides:
  - ItemTeleportBlocker service blocking all-destination-blocked jewelry, tablets, quest items, diary gear
  - PENDANT_OF_ATES_IDS whitelist (Varlamore-internal teleport item)
  - BLOCKED_ITEM_IDS set covering ~70 item IDs across 4 categories
  - ITEM_DESTINATION_DISPLAY map with human-readable destination strings per item ID
  - blockItemTeleports config toggle at position 4 in restrictionsSection
  - onMenuOptionClicked dispatch for both spell and item blockers with short-circuit logic

affects:
  - 03-02 (mixed-destination items: Ring of Dueling, Hunter cape, House tablet)
  - any future phase adding item-based blocking

tech-stack:
  added: []
  patterns:
    - isItemOp() guard before getItemId() for item event filtering
    - addVariants() helper maps item IDs to destination display strings atomically
    - numeric item IDs as fallback when RuneLite named constants unavailable
    - static initializer block builds immutable Sets and Maps from mutable staging collections

key-files:
  created:
    - src/main/java/com/varlamoreuim/teleport/ItemTeleportBlocker.java
  modified:
    - src/main/java/com/varlamoreuim/VarlamoreUimPlugin.java
    - src/main/java/com/varlamoreuim/VarlamoreUimConfig.java

key-decisions:
  - "Use isItemOp() + getItemId() for item identification (not MenuAction type check)"
  - "Use addVariants() helper with varargs int[] to support both named constants and numeric IDs in one call"
  - "Pharaoh's Sceptre charge variants use numeric IDs (9044-9046, 26938-26946) — no named RuneLite constants in API 1.12.17"
  - "Amulet of Glory Trimmed uses AMULET_OF_GLORY_T/T1-T6 naming (underscore before T) not AMULET_OF_GLORYT"
  - "Ring of Wealth Imbued uses RING_OF_WEALTH_I5/I4 format (no underscore before number)"
  - "ITEM_DESTINATION_DISPLAY getOrDefault fallback ensures no crash if ID missing from map"
  - "Spell blocker checked first in onMenuOptionClicked then short-circuits to avoid double-processing"

patterns-established:
  - "Item teleport blocking: isItemOp() guard -> whitelist check -> BLOCKED_ITEM_IDS set lookup -> isTeleportOption() -> event.consume() -> sendBlockedMessage()"
  - "Destination display map: every blocked item ID maps to human-readable destination string(s)"
  - "Chat message format: Color.RED 'Varlamore UIM:' prefix + Color.WHITE '[item] teleport to [dest] is blocked'"

requirements-completed:
  - ITEM-01
  - ITEM-02
  - ITEM-03
  - ITEM-04
  - ITEM-05

duration: 35min
completed: 2026-02-17
---

# Phase 3 Plan 01: Item Teleport Blocking Summary

**ItemTeleportBlocker service blocking ~70 item ID variants across jewelry, tablets, quest items, and diary gear using isItemOp() + destination display map for Phase-2-consistent chat feedback**

## Performance

- **Duration:** 35 min
- **Started:** 2026-02-17T00:00:00Z
- **Completed:** 2026-02-17T00:35:00Z
- **Tasks:** 2
- **Files modified:** 3

## Accomplishments

- Created ItemTeleportBlocker.java with BLOCKED_ITEM_IDS covering all 4 item categories (jewelry, tablets, quest items, diary gear), PENDANT_OF_ATES_IDS whitelist, ITEM_DESTINATION_DISPLAY map for destination-aware chat feedback, and isTeleportOption() filter protecting non-teleport item options
- Added blockItemTeleports config toggle at position 4 in restrictionsSection (default true, independent of blockSpellTeleports)
- Wired ItemTeleportBlocker into plugin with proper lifecycle (startUp/shutDown) and short-circuit dispatch logic in onMenuOptionClicked

## Task Commits

Each task was committed atomically:

1. **Task 1: Create ItemTeleportBlocker service** - `10babbd` (feat)
2. **Task 2: Wire ItemTeleportBlocker into plugin with config toggle** - `30a024d` (feat)

## Files Created/Modified

- `src/main/java/com/varlamoreuim/teleport/ItemTeleportBlocker.java` - New service: item teleport blocking with ~70 blocked item IDs across 4 categories, Pendant of Ates whitelist, ITEM_DESTINATION_DISPLAY map, and Phase-2-matching chat messages
- `src/main/java/com/varlamoreuim/VarlamoreUimPlugin.java` - Added ItemTeleportBlocker field, lifecycle, and onMenuOptionClicked dispatch with spell-blocker short-circuit
- `src/main/java/com/varlamoreuim/VarlamoreUimConfig.java` - Added blockItemTeleports toggle at position 4 in restrictionsSection

## Decisions Made

- Used `addVariants(Set, Map, String, int...)` varargs helper — allows mixing named `ItemID.*` constants and numeric literals in a single call, which was critical since some items (Pharaoh's Sceptre charge variants 1-3 and 4-8) have no named constants in RuneLite API 1.12.17
- Confirmed naming patterns through iterative compile testing: `AMULET_OF_GLORY_T4` (underscore before T) vs `RING_OF_WEALTH_I5` (no underscore before number)
- Numeric IDs used as authoritative fallback per the plan's explicit instruction when named constants unavailable
- `ITEM_DESTINATION_DISPLAY.getOrDefault(itemId, "outside Varlamore")` provides safe fallback while still delivering a meaningful user message

## Deviations from Plan

None - plan executed exactly as written. ItemID constant naming required iterative compile-test discovery (names not predictable without access to sources), but this is expected implementation detail work within the scope of Task 1.

## Issues Encountered

- RuneLite API ItemID constant naming is not consistent: trimmed Glory uses `AMULET_OF_GLORY_T4` (underscore T), imbued Ring of Wealth uses `RING_OF_WEALTH_I5` (no underscore before number). Pharaoh's Sceptre has no charge variant constants at all in API 1.12.17. Resolved by iterative compile-test probing — each compile attempt revealed exactly which constants don't exist, allowing targeted numeric ID substitution.

## User Setup Required

None - no external service configuration required.

## Next Phase Readiness

- ItemTeleportBlocker service established with extensible structure for plan 03-02
- Client reference passed through handleMenuClick signature — plan 03-02 can use `client.getVarbitValue()` for POH location detection without any signature changes
- Mixed-destination items (Ring of Dueling, Hunter cape, Max cape) and conditional items (House teleport tablet) ready for plan 03-02 implementation

---
*Phase: 03-item-minigame-teleport-blocking*
*Completed: 2026-02-17*
