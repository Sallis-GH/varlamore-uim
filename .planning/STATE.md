# Project State

## Project Reference

See: .planning/PROJECT.md (updated 2026-02-16)

**Core value:** Prevent the player from accidentally breaking their Varlamore lock by blocking all travel methods that leave the region
**Current focus:** Phase 5: NPC Replacement System

## Current Position

Phase: 5 of 6 (NPC Replacement System)
Plan: 1 of ? in current phase
Status: Phase 4 complete — ready for Phase 5
Last activity: 2026-03-15 — Completed 04-03 (Dizana's Quiver unlock gate for charter ship NPCs)

Progress: [███████░░░] 67%

## Performance Metrics

**Velocity:**
- Total plans completed: 6
- Average duration: 12 min
- Total execution time: 1.35 hours

**By Phase:**

| Phase | Plans | Total  | Avg/Plan |
|-------|-------|--------|----------|
| 01    | 2     | 36 min | 18 min   |
| 02    | 2     | 5 min  | 2.5 min  |
| 03    | 2     | 55 min | 27.5 min |
| 04    | 3     | 13 min | 4.3 min  |

**Recent Trend:**
- Last 5 plans: 02-02 (2 min), 03-01 (35 min), 03-02 (20 min), 04-02 (4 min), 04-03 (5 min)
- Trend: Phase 4 plans average ~4.3 min; NPC blocking implementation was well-scoped

*Updated after each plan completion*

## Accumulated Context

### Decisions

Decisions are logged in PROJECT.md Key Decisions table.
Recent decisions affecting current work:

- Chunk ID-based boundary system chosen for configurability and standard approach
- NPC replacement for travel NPCs provides immersive experience vs hard blocks
- Categorized settings panel built for future extensibility (QoA, tracking, unlocks)
- Key exits first, edge cases later strategy enables fast initial delivery
- Use BSD-2-Clause license for Plugin Hub compatibility (01-01)
- Use HashSet for region storage for O(1) lookup performance (01-01)
- Load region data from JSON via getResourceAsStream for JAR compatibility (01-01)
- Use placeholder region IDs with explicit note pending in-game collection (01-01)
- Use collapsible drawer sections for panel UI instead of TitledBorder (01-02)
- Use Injector.getInstance() for PluginPanel creation following RuneLite patterns (01-02)
- Track boundary state changes to avoid per-tick logging spam (01-02)
- Add 4 config sections (1 active + 3 placeholders) for future extensibility (01-02)
- Use Set.of() immutable set for 31 blocked spell names (02-01)
- Use Color constants instead of ChatColorType for chat messages (02-01)
- Block ALL spellbook teleports via name-based set without destination lookup (02-01)
- Use widget group ID to detect active spellbook for Home Teleport (02-02)
- Map Home Teleport destinations by widget group ID for O(1) lookup (02-02)
- Block by default for null widgets or unknown spellbooks (02-02)
- All current Home Teleport destinations are outside Varlamore (02-02)
- Use isItemOp() + getItemId() for item identification (not MenuAction type check) (03-01)
- addVariants() helper with varargs int[] supports both named constants and numeric IDs in one call (03-01)
- Pharaoh's Sceptre charge variants use numeric IDs — no named RuneLite constants in API 1.12.17 (03-01)
- ITEM_DESTINATION_DISPLAY getOrDefault fallback ensures no crash if ID missing from map (03-01)
- Spell blocker short-circuits item blocker when spell event is handled (03-01)
- RING_OF_DUELING_IDS checked before BLOCKED_ITEM_IDS to enable per-destination allow/block logic (03-02)
- MAX_CAPE_IDS includes all 26 functional variants; broken/degraded variants excluded (03-02)
- handleMinigameTeleport on ItemTeleportBlocker (not SpellTeleportBlocker) to share chat message utilities (03-02)
- House tablet defaults to blocking when VarBit returns non-Aldarin value (safe fallback) (03-02)
- VarBit 2187 value 8 = Aldarin (only Varlamore POH location) — requires in-game verification (03-02)
- Equipment panel teleports require widget-based item ID resolution via getChild(1).getItemId() — getItemId() returns -1 for CC_OP events (03-bugfix)
- NON_TELEPORT_OPTIONS exclusion pattern replaces positive TELEPORT_OPTIONS matching — catches equipped destination names automatically (03-bugfix)
- Use net.runelite.api.gameval.InterfaceID.WORNITEMS (not deprecated widgets.InterfaceID.EQUIPMENT) for equipment panel detection (03-bugfix)
- SpellTeleportBlocker filters on SPELLBOOK_WIDGET_GROUPS (218, 219, 430) to avoid processing equipment CC_OP events (03-bugfix)
- Use WorldView.npcs().byIndex() for NPC index lookup — client.getCachedNPCs() does not exist in this RuneLite API version (04-01)
- Primio quetzal permanently blocked (no unlock gate) — direct Varrock route, no Varlamore-internal value (04-01)
- Charter ship full ID ranges 15510-15533 blocked pending in-game verification of which IDs are actively spawned (04-01)
- RenderCallback enabled state synced from config on each game tick to respect toggle changes without restart (04-01)
- NPCComposition has no getStandingAnimationID() in RuneLite API — used hardcoded animation ID 808 (standard human idle) for Mysterious Old Man stand-in (04-02)
- initClient(Client, ClientThread, ChatMessageManager) pattern used for non-@Inject dependency provision in manually instantiated service classes (04-02)
- PostMenuSort used for RuneLiteObject menu injection — fires after default menu built, before display (04-02)
- LocalPoint.equals() comparison safe for stand-in tile matching — Lombok @Value class with structural equals (04-02)
- Dizana's Quiver IDs 28947/28949/28951/28953 cover all 4 variants (uncharged, locked, charged, charged+locked) per OSRS Wiki (04-03)
- containsDizanasQuiver() static on NpcTransportBlocker to co-locate quiver logic with NPC transport domain (04-03)
- setUnlocked() manages RuneLiteObject stand-in lifecycle internally — callers only set state, lifecycle follows automatically (04-03)
- ItemContainerChanged fires on login container load — no explicit login check needed for initial unlock detection (04-03)
- Primio permanently blocked — handlePrimioClick() checks only enabled flag, not unlocked (04-03)

### Pending Todos

None yet.

### Blockers/Concerns

- Phase 1: Varlamore chunk ID extraction requires in-game data collection (temporary logging plugin to walk boundary)
- Phase 5: Dialogue injection technique may need refinement based on current OSRS widget IDs

## Session Continuity

Last session: 2026-03-15
Stopped at: Completed 04-03-PLAN.md (Dizana's Quiver unlock gate — Phase 4 complete)
Next: Phase 5 (NPC Replacement System)

---
*State initialized: 2026-02-16*
*Last updated: 2026-03-15 (04-03 complete: Dizana's Quiver unlock gate — Phase 4 all plans done)*
