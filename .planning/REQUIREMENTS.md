# Requirements: Varlamore UIM Plugin

**Defined:** 2026-02-16
**Core Value:** Prevent the player from accidentally breaking their Varlamore lock by blocking all travel methods that leave the region

## v1 Requirements

Requirements for initial release. Each maps to roadmap phases.

### Boundary

- [ ] **BNDRY-01**: Plugin defines Varlamore region via a set of map chunk IDs loaded from a data file
- [ ] **BNDRY-02**: Plugin can determine if a given world location is inside or outside Varlamore in O(1) time
- [ ] **BNDRY-03**: Internal Varlamore transport (Quetzal system) is whitelisted and works normally

### Teleport Spell Blocking

- [ ] **SPELL-01**: User cannot cast Standard spellbook city teleports (Varrock, Lumbridge, Falador, Camelot, Ardougne, Watchtower)
- [ ] **SPELL-02**: User cannot cast Home Teleport if respawn point is outside Varlamore
- [ ] **SPELL-03**: User cannot cast Ancient Magicks teleports (Paddewwa, Senntisten, Kharyrll, Lassar, Dareeyak, Carrallangar, Annakarl, Ghorrock)
- [ ] **SPELL-04**: User cannot cast Lunar spellbook teleports (Moonclan, Waterbirth, Barbarian, Khazard, Fishing Guild, Catherby, Ice Plateau, Trollheim)
- [ ] **SPELL-05**: User cannot cast Arceuus spellbook teleports (Cemetery, Draynor Manor, Mind Altar, Salve Graveyard, Fenkenstrain's Castle, West Ardougne, Harmony Island, Ape Atoll, Battlefront)
- [ ] **SPELL-06**: Blocked spell attempts show a chat message explaining the restriction

### Item Teleport Blocking

- [ ] **ITEM-01**: User cannot use jewelry teleports outside Varlamore (Amulet of Glory, Ring of Dueling, Games Necklace, Skills Necklace, Combat Bracelet, Ring of Wealth, Burning Amulet, Necklace of Passage, Digsite Pendant, Slayer Ring)
- [ ] **ITEM-02**: User cannot use teleport tablets that lead outside Varlamore (Varrock, Lumbridge, Falador, Camelot, Ardougne, Watchtower, House tab if POH outside)
- [ ] **ITEM-03**: User cannot use quest teleport items leading outside Varlamore (Ectophial, Xeric's Talisman, Kharedst's Memoirs, Pharaoh's Sceptre, Skull Sceptre, Enchanted Lyre, Drakan's Medallion)
- [ ] **ITEM-04**: User cannot use achievement diary gear teleports outside Varlamore (Karamja Gloves, Explorer's Ring, Ardougne Cloak, Morytania Legs, Desert Amulet, Wilderness Sword, Rada's Blessing)
- [ ] **ITEM-05**: Blocked item teleport attempts are silently prevented (action consumed)

### NPC Transport Blocking

- [ ] **NPC-01**: User cannot use ship/boat NPCs at Varlamore ports to travel outside the region
- [ ] **NPC-02**: User cannot use charter ship crews to leave Varlamore
- [ ] **NPC-03**: User cannot use gnome gliders to travel outside Varlamore (if present in region)
- [ ] **NPC-04**: User cannot use spirit trees to travel outside Varlamore (if present in region)
- [ ] **NPC-05**: User cannot use fairy rings to travel to codes outside Varlamore (if present in region)

### NPC Replacement

- [ ] **REPL-01**: Travel NPCs at Varlamore exits are visually hidden from the player
- [ ] **REPL-02**: Replacement NPCs are rendered at the same location as hidden travel NPCs
- [ ] **REPL-03**: Interacting with a replacement NPC triggers an in-world chat dialogue explaining travel is not yet unlocked
- [ ] **REPL-04**: NPC replacement data is data-driven (JSON config) so new replacements can be added without code changes

### Plugin Infrastructure

- [ ] **INFRA-01**: Plugin has a RuneLite side panel with categorized sections (Restrictions, with future expansion slots)
- [ ] **INFRA-02**: Plugin follows RuneLite Plugin Hub structure, naming conventions, and BSD-2-Clause licensing
- [ ] **INFRA-03**: Plugin has enable/disable toggle accessible from config
- [ ] **INFRA-04**: Plugin architecture supports adding new feature categories (QoA, Tracking, Unlocks) without restructuring

## v2 Requirements

Deferred to future release. Tracked but not in current roadmap.

### System Teleport Blocking

- **SYS-01**: User cannot use minigame group finder teleports to leave Varlamore
- **SYS-02**: User cannot use POH portal room portals to teleport outside Varlamore
- **SYS-03**: User cannot use Portal Nexus to teleport outside Varlamore
- **SYS-04**: User cannot use mounted jewellery box to teleport outside Varlamore

### Milestone/Unlock System

- **UNLK-01**: Plugin tracks milestone completion (quests, levels, achievements)
- **UNLK-02**: Completing milestones progressively unlocks specific travel methods
- **UNLK-03**: Unlock state persists across sessions (per-account)
- **UNLK-04**: Side panel displays milestone progress and available unlocks

### Visualization & Tracking

- **VIS-01**: Boundary overlay shows Varlamore region on minimap/world map
- **VIS-02**: Plugin tracks and displays statistics (blocked attempts, time in region)

### Edge Cases

- **EDGE-01**: Death respawn is handled to prevent respawn outside Varlamore
- **EDGE-02**: Quests that force-teleport outside the region are blocked from starting

## Out of Scope

Explicitly excluded. Documented to prevent scope creep.

| Feature | Reason |
|---------|--------|
| Item restrictions (equipping non-Varlamore items) | Not yet discussed, different restriction type than travel |
| Content tracking (quests, bosses, items available in region) | Future feature, not core to travel restriction |
| Achievement system (custom Varlamore UIM milestones) | Future feature, depends on unlock system |
| Collection log (Varlamore-specific drops) | Future feature, not core to restriction enforcement |
| Multi-region profiles (Kourend-locked, etc.) | v2+ extensibility, not needed for Varlamore-specific plugin |
| Network features (leaderboards, shared unlocks) | Plugin Hub restricts external network calls |
| Random event handling | Most random events no longer force-teleport in OSRS |

## Traceability

Which phases cover which requirements. Updated during roadmap creation.

| Requirement | Phase | Status |
|-------------|-------|--------|
| BNDRY-01 | — | Pending |
| BNDRY-02 | — | Pending |
| BNDRY-03 | — | Pending |
| SPELL-01 | — | Pending |
| SPELL-02 | — | Pending |
| SPELL-03 | — | Pending |
| SPELL-04 | — | Pending |
| SPELL-05 | — | Pending |
| SPELL-06 | — | Pending |
| ITEM-01 | — | Pending |
| ITEM-02 | — | Pending |
| ITEM-03 | — | Pending |
| ITEM-04 | — | Pending |
| ITEM-05 | — | Pending |
| NPC-01 | — | Pending |
| NPC-02 | — | Pending |
| NPC-03 | — | Pending |
| NPC-04 | — | Pending |
| NPC-05 | — | Pending |
| REPL-01 | — | Pending |
| REPL-02 | — | Pending |
| REPL-03 | — | Pending |
| REPL-04 | — | Pending |
| INFRA-01 | — | Pending |
| INFRA-02 | — | Pending |
| INFRA-03 | — | Pending |
| INFRA-04 | — | Pending |

**Coverage:**
- v1 requirements: 27 total
- Mapped to phases: 0
- Unmapped: 27

---
*Requirements defined: 2026-02-16*
*Last updated: 2026-02-16 after initial definition*
