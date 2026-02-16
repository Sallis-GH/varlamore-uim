# Roadmap: Varlamore UIM Plugin

## Overview

This roadmap delivers a RuneLite plugin that enforces Varlamore area-lock restrictions for Ultimate Ironman accounts. The journey starts with core infrastructure (boundary detection, plugin structure), then systematically blocks all travel methods (spells, items, NPCs), adds immersive NPC replacement for blocked travel NPCs, and concludes with comprehensive testing and Plugin Hub submission. Each phase delivers a complete, testable capability that builds toward reliable area-lock enforcement.

## Phases

**Phase Numbering:**
- Integer phases (1, 2, 3): Planned milestone work
- Decimal phases (2.1, 2.2): Urgent insertions (marked with INSERTED)

Decimal phases appear between their surrounding integers in numeric order.

- [x] **Phase 1: Foundation & Infrastructure** - Plugin structure, boundary system, and extensible settings panel
- [x] **Phase 2: Spell Teleport Blocking** - Block all spellbook teleports that leave Varlamore
- [ ] **Phase 3: Item & Minigame Teleport Blocking** - Block jewelry, tablets, and quest item teleports
- [ ] **Phase 4: NPC Transport Blocking** - Block ships, gliders, spirit trees, and fairy rings
- [ ] **Phase 5: NPC Replacement System** - Immersive NPC hiding and dialogue-based blocking
- [ ] **Phase 6: Testing & Plugin Hub Submission** - Comprehensive testing and submission

## Phase Details

### Phase 1: Foundation & Infrastructure
**Goal**: Plugin has working boundary detection, basic infrastructure, and extensible settings panel
**Depends on**: Nothing (first phase)
**Requirements**: BNDRY-01, BNDRY-02, BNDRY-03, INFRA-01, INFRA-02, INFRA-03, INFRA-04
**Success Criteria** (what must be TRUE):
  1. Plugin loads in RuneLite without errors and appears in plugin list
  2. Plugin can accurately determine if any location in OSRS is inside or outside Varlamore in real-time
  3. Internal Varlamore transport (Quetzal system) works normally without interference
  4. Plugin has a side panel with categorized sections that can be expanded in future phases
  5. Plugin can be enabled/disabled via config toggle
**Plans**: 2 plans

Plans:
- [x] 01-01-PLAN.md -- Project scaffold and BoundaryChecker service
- [x] 01-02-PLAN.md -- Config sections, side panel, and plugin wiring

### Phase 2: Spell Teleport Blocking
**Goal**: Player cannot use any spellbook teleport that would move them outside Varlamore
**Depends on**: Phase 1
**Requirements**: SPELL-01, SPELL-02, SPELL-03, SPELL-04, SPELL-05, SPELL-06
**Success Criteria** (what must be TRUE):
  1. Player cannot cast Standard spellbook city teleports (Varrock, Lumbridge, Falador, Camelot, Ardougne, Watchtower)
  2. Player cannot cast Home Teleport when respawn point is outside Varlamore
  3. Player cannot cast Ancient Magicks teleports (all 8 locations)
  4. Player cannot cast Lunar spellbook teleports (all 8 locations)
  5. Player cannot cast Arceuus spellbook teleports (all 9 locations)
  6. Blocked spell attempts show clear chat message explaining the restriction
**Plans**: 2 plans

Plans:
- [x] 02-01-PLAN.md -- SpellTeleportBlocker service with 31 blocked spells, plugin wiring, and config toggle
- [x] 02-02-PLAN.md -- Home Teleport blocking with spellbook-aware destination logic

### Phase 3: Item & Minigame Teleport Blocking
**Goal**: Player cannot use item-based teleports or minigame teleports that leave Varlamore
**Depends on**: Phase 2
**Requirements**: ITEM-01, ITEM-02, ITEM-03, ITEM-04, ITEM-05
**Success Criteria** (what must be TRUE):
  1. Player cannot use jewelry teleports (glory, dueling, games necklace, skills necklace, combat bracelet, wealth, burning amulet, passage, digsite pendant, slayer ring) to locations outside Varlamore
  2. Player cannot use teleport tablets (Varrock, Lumbridge, Falador, Camelot, Ardougne, Watchtower, house tab) that lead outside the region
  3. Player cannot use quest item teleports (Ectophial, Xeric's Talisman, Kharedst's Memoirs, Pharaoh's Sceptre, Skull Sceptre, Enchanted Lyre, Drakan's Medallion) that lead outside Varlamore
  4. Player cannot use achievement diary gear teleports (Karamja Gloves, Explorer's Ring, Ardougne Cloak, Morytania Legs, Desert Amulet, Wilderness Sword, Rada's Blessing) to locations outside Varlamore
  5. Blocked item teleport attempts are silently prevented (action does not execute)
**Plans**: TBD

Plans:
- [ ] 03-01: TBD
- [ ] 03-02: TBD
- [ ] 03-03: TBD

### Phase 4: NPC Transport Blocking
**Goal**: Player cannot use NPC-based transport methods to leave Varlamore
**Depends on**: Phase 3
**Requirements**: NPC-01, NPC-02, NPC-03, NPC-04, NPC-05
**Success Criteria** (what must be TRUE):
  1. Player cannot use ships/boats at Varlamore ports to travel outside the region
  2. Player cannot use charter ship crews to leave Varlamore
  3. Player cannot use gnome gliders to travel to locations outside Varlamore (if present in region)
  4. Player cannot use spirit trees to travel to locations outside Varlamore (if present in region)
  5. Player cannot use fairy rings to travel to ring codes outside Varlamore (if present in region)
**Plans**: TBD

Plans:
- [ ] 04-01: TBD
- [ ] 04-02: TBD

### Phase 5: NPC Replacement System
**Goal**: Travel NPCs at Varlamore exits are replaced with immersive dialogue-based blocking
**Depends on**: Phase 4
**Requirements**: REPL-01, REPL-02, REPL-03, REPL-04
**Success Criteria** (what must be TRUE):
  1. Travel NPCs at Varlamore exits are visually hidden from the player's view
  2. Replacement NPCs appear at the same locations as the hidden travel NPCs
  3. Clicking a replacement NPC triggers in-world dialogue explaining travel is restricted or not yet unlocked
  4. New NPC replacements can be added via JSON data file without code changes
  5. NPC replacement system gracefully handles game updates (fallback to chat messages if widget manipulation fails)
**Plans**: TBD

Plans:
- [ ] 05-01: TBD
- [ ] 05-02: TBD
- [ ] 05-03: TBD

### Phase 6: Testing & Plugin Hub Submission
**Goal**: Plugin is comprehensively tested and submitted to RuneLite Plugin Hub
**Depends on**: Phase 5
**Requirements**: (All requirements validated)
**Success Criteria** (what must be TRUE):
  1. All 60+ travel methods (spells, items, NPCs) are tested and confirmed blocked
  2. Boundary edge cases (instanced areas, death mechanics, world hopping) are tested and handle correctly
  3. Plugin passes all Plugin Hub compliance checks (BSD-2-Clause license, naming conventions, no unapproved dependencies, code quality)
  4. Plugin repository has comprehensive README with installation instructions, feature list, and known limitations
  5. Plugin is submitted to Plugin Hub with all required metadata
**Plans**: TBD

Plans:
- [ ] 06-01: TBD
- [ ] 06-02: TBD

## Progress

**Execution Order:**
Phases execute in numeric order: 1 -> 2 -> 3 -> 4 -> 5 -> 6

| Phase | Plans Complete | Status | Completed |
|-------|----------------|--------|-----------|
| 1. Foundation & Infrastructure | 2/2 | Complete | 2026-02-16 |
| 2. Spell Teleport Blocking | 2/2 | Complete | 2026-02-16 |
| 3. Item & Minigame Teleport Blocking | 0/3 | Not started | - |
| 4. NPC Transport Blocking | 0/2 | Not started | - |
| 5. NPC Replacement System | 0/3 | Not started | - |
| 6. Testing & Plugin Hub Submission | 0/2 | Not started | - |

---
*Roadmap created: 2026-02-16*
*Last updated: 2026-02-16 -- Phase 2 complete*
