# Varlamore UIM Plugin - Feature Research

**Research Date:** 2026-02-16
**Researcher:** Project Research Agent
**Purpose:** Comprehensive research on OSRS Varlamore travel methods, boundaries, and area-locked plugin features

---

## Executive Summary

This document provides comprehensive research on all travel and exit methods from the Varlamore region in OSRS, map boundaries, existing area-locked plugins, and feature recommendations for a Varlamore-locked UIM plugin. The primary focus is identifying every possible way a player could leave Varlamore to ensure complete restriction enforcement.

**Key Findings:**
- Varlamore has multiple exit vectors: NPC-based transport, spell teleports, item teleports, and minigame systems
- Region boundary defined by specific map chunk IDs (to be extracted from game data)
- Existing plugins (Region Locker, Area Tasks) provide architecture patterns
- Critical to distinguish internal Varlamore transport from external exits

---

## 1. ALL TRAVEL/EXIT METHODS FROM VARLAMORE

### 1.1 Transport NPCs Near Varlamore Borders

#### A. Boat/Ship NPCs

**Quetzal Transport System (Internal - DO NOT BLOCK):**
- **Purpose:** Internal Varlamore travel between cities
- **NPCs:** Quetzal riders/handlers
- **Locations:**
  - Civitas illa Fortis (main city)
  - Cam Torum (second city)
  - Various settlements within Varlamore
- **Destinations:** All within Varlamore region
- **Action:** ALLOW - this is internal transport only
- **Note:** Critical to distinguish these from external exits

**Port/Harbor NPCs:**
- **Risk Level:** HIGH - potential exits to mainland Gielinor
- **Expected NPCs:**
  - Ship captains at Varlamore ports
  - Dock workers offering passage
  - Charter ship crews
- **Destinations:** Likely connections to Port Sarim, Karamja, or other ports
- **NPC IDs:** TO BE RESEARCHED (need to identify specific NPCs in-game)
- **Blocking Strategy:** Replace with custom NPC that explains travel is locked

**Cart/Wagon NPCs:**
- **Expected Locations:** Varlamore city gates, trade routes
- **Destinations:** Potentially to Kourend or other mainland regions
- **Action:** BLOCK if destination is outside Varlamore
- **Implementation:** Check destination coordinates against boundary system

#### B. Other Transport NPCs

**Magic Carpet Merchants:**
- **Expected:** Varlamore may have desert-style transport
- **Risk:** Carpets often connect to Al Kharid, Pollnivneach, etc.
- **NPC Pattern:** "Carpet merchant", "Rug merchant"
- **Action:** BLOCK if present and leads outside region

**Gnome Glider NPCs:**
- **NPC:** Gnome pilot
- **Location:** IF a glider exists in Varlamore
- **Destinations:** Gnome Stronghold, Feldip Hills, Al Kharid, etc.
- **Action:** BLOCK - all glider destinations are outside Varlamore

**Spirit Tree Network:**
- **NPC:** Spirit tree (clickable object, not NPC)
- **Location:** IF present in Varlamore
- **Destinations:** Tree Gnome Stronghold, Gnome Stronghold, Etceteria, etc.
- **Action:** BLOCK all destinations
- **Note:** May need special handling as it's an object, not NPC

**Fairy Ring System:**
- **Object:** Fairy ring
- **Location:** IF present in Varlamore
- **Risk:** CRITICAL - fairy rings connect to entire map
- **Codes:** All codes outside Varlamore must be blocked
- **Implementation:** Intercept fairy ring interface, block non-Varlamore codes

**Minigame Teleport NPCs:**
- **General Pattern:** NPCs that teleport to minigames
- **Examples:** Nightmare Zone host, Pest Control recruiter
- **Action:** BLOCK - minigames are not in Varlamore

### 1.2 Teleport Spells (All Spellbooks)

#### A. Standard Spellbook Teleports

**City Teleports (BLOCK ALL):**
- Varrock Teleport → Varrock (BLOCK)
- Lumbridge Teleport → Lumbridge (BLOCK)
- Falador Teleport → Falador (BLOCK)
- Camelot Teleport → Seers' Village (BLOCK)
- Ardougne Teleport → Ardougne (BLOCK)
- Watchtower Teleport → Watchtower (BLOCK)

**Home Teleport:**
- Destination: Depends on respawn point
- Risk: HIGH - could teleport outside Varlamore
- Action: BLOCK if respawn is not in Varlamore
- Implementation: Check player's set respawn location

**Miscellaneous Standard Teleports:**
- Paddewwa Teleport → Edgeville dungeon (BLOCK)
- Senntisten Teleport → Senntisten (BLOCK)
- Kourend Castle Teleport → Kourend (BLOCK - adjacent region)
- Teleport to House → POH location (BLOCK if house not in Varlamore)

#### B. Ancient Magicks Teleports

**ALL Ancient Teleports (BLOCK):**
- Paddewwa Teleport → Edgeville Dungeon
- Senntisten Teleport → Senntisten
- Kharyrll Teleport → Canifis
- Lassar Teleport → Ice Mountain
- Dareeyak Teleport → Wilderness
- Carrallangar Teleport → Wilderness
- Annakarl Teleport → Wilderness
- Ghorrock Teleport → Wilderness

**Note:** All Ancient Magicks teleports lead outside Varlamore

#### C. Lunar Spellbook Teleports

**ALL Lunar Teleports (BLOCK):**
- Moonclan Teleport → Moonclan Island
- Waterbirth Teleport → Waterbirth Island
- Barbarian Teleport → Barbarian Outpost
- Khazard Teleport → Port Khazard
- Fishing Guild Teleport → Fishing Guild
- Catherby Teleport → Catherby
- Ice Plateau Teleport → Ice Plateau
- Trollheim Teleport → Trollheim

**Teleport Other Spells:**
- Teleport to Target → Random location (BLOCK)

#### D. Arceuus Spellbook Teleports

**Cemetery Teleport → Arceuus (BLOCK)**
**Draynor Manor Teleport → Draynor Manor (BLOCK)**
**Mind Altar Teleport → Mind Altar (BLOCK)**
**Salve Graveyard Teleport → Salve Graveyard (BLOCK)**
**Fenkenstrain's Castle Teleport → Morytania (BLOCK)**
**West Ardougne Teleport → West Ardougne (BLOCK)**
**Harmony Island Teleport → Harmony Island (BLOCK)**
**Ape Atoll Teleport → Ape Atoll (BLOCK)**
**Battlefront Teleport → Great Kourend (BLOCK)**

**Note:** Kourend is adjacent to Varlamore but is a separate region - must be blocked

### 1.3 Item-Based Teleports

#### A. Jewelry Teleports

**Ring of Dueling:**
- Al Kharid Duel Arena (BLOCK)
- Castle Wars (BLOCK)
- Ferox Enclave (BLOCK)
- **Risk Level:** HIGH - commonly used for banking

**Games Necklace:**
- Burthorpe Games Room (BLOCK)
- Barbarian Outpost (BLOCK)
- Corporeal Beast (BLOCK)
- Wintertodt Camp (BLOCK)
- **Risk Level:** HIGH - used for skilling/content access

**Amulet of Glory:**
- Edgeville (BLOCK)
- Karamja (BLOCK)
- Draynor Village (BLOCK)
- Al Kharid (BLOCK)
- **Risk Level:** CRITICAL - primary teleport item

**Skills Necklace:**
- Fishing Guild (BLOCK)
- Mining Guild (BLOCK)
- Crafting Guild (BLOCK)
- Cooking Guild (BLOCK)
- Woodcutting Guild (BLOCK)
- Farming Guild (BLOCK if outside Varlamore)

**Combat Bracelet:**
- Warriors' Guild (BLOCK)
- Champions' Guild (BLOCK)
- Monastery (BLOCK)
- Ranging Guild (BLOCK)

**Ring of Wealth:**
- Grand Exchange (BLOCK)
- Falador Park (BLOCK)
- Miscellania (BLOCK)

**Digsite Pendant:**
- Fossil Island (BLOCK if outside Varlamore)
- Digsite (BLOCK)
- Lithkren Vault (BLOCK)

#### B. Quest/Achievement Teleport Items

**Ectophial:**
- Ectofuntus (BLOCK)
- **Risk:** Medium - popular teleport

**Pharaoh's Sceptre:**
- Jalsavrah (BLOCK)
- Jaleustrophos (BLOCK)
- Jaldraocht (BLOCK)

**Skull Sceptre:**
- Barbarian Village (BLOCK)

**Kharedst's Memoirs:**
- Various Kourend locations (BLOCK - Kourend is not Varlamore)

**Xeric's Talisman:**
- Xeric's Lookout (BLOCK)
- Xeric's Glade (BLOCK)
- Xeric's Inferno (BLOCK)
- Xeric's Heart (BLOCK)

#### C. Teleport Tablets

**ALL Standard Teleport Tablets (BLOCK):**
- Varrock teleport tablet
- Lumbridge teleport tablet
- Falador teleport tablet
- Camelot teleport tablet
- Ardougne teleport tablet
- Watchtower teleport tablet
- House teleport tablet (if POH not in Varlamore)

**Redirection Scrolls:**
- Allow retargeting teleport tablets
- **Action:** BLOCK - redirected destinations likely outside Varlamore

#### D. Miscellaneous Teleport Items

**Enchanted Lyre:**
- Rellekka (BLOCK)
- Waterbirth Island (BLOCK)

**Slayer Rings:**
- Gnome Stronghold Slayer Cave (BLOCK)
- Rellekka Slayer Cave (BLOCK)
- Fremennik Slayer Dungeon (BLOCK)
- Tarn's Lair (BLOCK)
- Stronghold Slayer Cave (BLOCK)
- Dark Beasts (BLOCK)

**Fairy Rings (when activated by item):**
- BLOCK all non-Varlamore codes

**Burning Amulet:**
- Chaos Temple (BLOCK)
- Bandit Camp (BLOCK)
- Lava Maze (BLOCK)

**Necklace of Passage:**
- Wizards' Tower (BLOCK)
- Outpost (BLOCK)
- Eagle's Eyrie (BLOCK)

**Drakan's Medallion:**
- Morytania teleports (BLOCK)

**Construction Cape:**
- Teleport to POH (BLOCK if house not in Varlamore)

**Max Cape/Completionist Cape:**
- Various teleports (BLOCK per destination)

**Achievement Diary Gear:**
- Karamja Gloves → Karamja (BLOCK)
- Explorer's Ring → Cabbage patch (BLOCK)
- Ardougne Cloak → Ardougne (BLOCK)
- Morytania Legs → Morytania (BLOCK)
- Desert Amulet → Desert (BLOCK)
- Wilderness Sword → Wilderness (BLOCK)
- Rada's Blessing → Kourend (BLOCK)

### 1.4 Other Travel Methods

#### A. Minigame Teleports (Group Finder)

**All Minigame Teleports (BLOCK):**
- Nightmare Zone
- Pest Control
- Soul Wars
- Castle Wars
- Barbarian Assault
- Blast Furnace
- Tithe Farm
- Fishing Trawler
- Trouble Brewing
- Shades of Mort'ton
- Fight Pits
- TzHaar Fight Cave
- Chambers of Xeric
- Theatre of Blood
- Tombs of Amascut

**Implementation:**
- Intercept minigame teleport interface
- Block all selections
- Show message: "Minigame teleports are not available in Varlamore-locked mode"

#### B. Canoes

**Location:** River-based transport system
**Risk:** Medium - connects multiple river locations
**Destinations:**
- Lumbridge
- Champions' Guild
- Barbarian Village
- Edgeville
- Wilderness

**Action:** BLOCK if any canoe stations exist near/in Varlamore

#### C. Charter Ships

**NPCs:** Ship crew members at docks
**Destinations:** Multiple ports across Gielinor
**Common Routes:**
- Port Sarim
- Catherby
- Brimhaven
- Karamja
- Mos Le'Harmless
- Port Tyras
- Port Phasmatys

**Action:** BLOCK all charter ship options

#### D. Random Events

**Risk:** LOW to MEDIUM
**Potential Teleports:**
- Surprise Exam (teleports to classroom)
- Evil Twin (teleports to dungeon)
- Genie (no teleport, just rewards)

**Note:** Most random events no longer forcibly teleport players (OSRS update)
**Action:** Monitor but likely low priority

#### E. Death Mechanics

**Respawn Points:**
- Default: Lumbridge (BLOCK - outside Varlamore)
- Falador (BLOCK)
- Camelot (BLOCK)
- Edgeville (BLOCK)
- Prifddinas (BLOCK)
- Varlamore? (ALLOW if respawn in Varlamore)

**Implementation:**
- Check player's set respawn point
- Warn if set to non-Varlamore location
- Consider blocking respawn point changes to non-Varlamore locations

**Gravestone Mechanics:**
- Player dies and can retrieve items from gravestone
- Risk: Respawn could be outside Varlamore
- **Critical:** Must handle death without breaking the lock

**Death's Office:**
- Location: Outside of Death's office
- Risk: May allow retrieval from outside Varlamore
- Action: Research required - may need special handling

#### F. Quest-Forced Teleportation

**Quest Teleports:**
- Some quests forcibly teleport players during cutscenes
- Examples:
  - Dragon Slayer (to Crandor)
  - Recipe for Disaster (to various locations)
  - Desert Treasure (to various pyramids)

**Action:**
- Research Varlamore-specific quests
- Block starting quests that would teleport outside region
- Allow quests contained within Varlamore

#### G. POH (Player-Owned House) Portals

**Portal Room Portals:**
- Can be built to teleport to various locations
- Examples:
  - Varrock portal
  - Lumbridge portal
  - Falador portal
  - Ardougne portal
  - Watchtower portal
  - Senntisten portal
  - Kharyrll portal
  - Lunar Isle portal
  - Waterbirth portal
  - Fishing Guild portal
  - Kourend portal
  - Many others

**Implementation:**
- Block entering POH portals that lead outside Varlamore
- Allow portals within Varlamore (if any exist)
- Check destination on click, not just presence of portal

**Amulet of Glory (mounted):**
- Same destinations as regular glory
- Block all options

**Digsite Pendant (mounted):**
- Block all options

**Xeric's Talisman (mounted):**
- Block all options (Kourend destinations)

**Portal Nexus:**
- Can have many teleport destinations
- Must check each destination individually

**Jewellery Box:**
- Consolidated jewelry teleports
- Block based on individual teleport destinations

### 1.5 Varlamore-Specific Transport (DO NOT BLOCK)

#### A. Quetzal Transport System

**Purpose:** Internal Varlamore travel between settlements
**NPCs:** Quetzal riders, Quetzal handlers
**Locations:**
- Civitas illa Fortis (main hub)
- Cam Torum
- Aldarin
- Quetzal flight master locations

**Routes (Internal Only):**
- Civitas illa Fortis ↔ Cam Torum
- Civitas illa Fortis ↔ Aldarin
- Cam Torum ↔ Hunter's Rumour
- Other internal city connections

**Action:** ALLOW ALL - this is core Varlamore gameplay
**Implementation Notes:**
- Must identify Quetzal NPC IDs
- Whitelist these NPCs in restriction system
- Ensure dialogue/interaction works normally

#### B. Other Internal Travel

**Agility Shortcuts:**
- Within Varlamore region
- Action: ALLOW - no risk of leaving region

**Minecarts (if present):**
- May exist for internal Varlamore travel
- Action: ALLOW if all destinations within region

**Teleportation Devices (Varlamore-specific):**
- Research needed for any Varlamore quest teleports
- Action: ALLOW if destinations are within Varlamore

---

## 2. Map Chunk/Region IDs for Varlamore

### 2.1 Understanding OSRS Map System

**Chunk System:**
- OSRS map divided into square chunks
- Each chunk has unique region ID
- Region ID calculated from world coordinates
- Formula: `regionID = ((x >> 6) << 8) | (y >> 6)`

**Varlamore Region IDs:**
- **Status:** NEEDS EXTRACTION FROM GAME DATA
- **Method:** Use RuneLite API to walk Varlamore boundary and log region IDs
- **Tool:** Create temporary logging plugin to capture all region IDs while exploring

### 2.2 Expected Varlamore Boundaries

**Geographical Bounds:**
- North: Varlamore wilderness border (if exists)
- South: Southern coast/ocean
- East: Ocean/unexplored territory
- West: Boundary with Kourend region

**Critical Boundary Points:**
- Kourend/Varlamore border (MUST BE PRECISE)
- Port/harbor edges
- Any mountain passes or gates
- Quest-locked areas within Varlamore

### 2.3 Extraction Strategy

**Step 1: Manual Exploration**
- Walk entire Varlamore perimeter
- Log region ID at every significant location
- Note transitions between regions

**Step 2: Data Compilation**
- Compile all unique region IDs
- Create `varlamore_chunks.json` resource file
- Format: JSON array of integer region IDs

**Step 3: Validation**
- Test boundary detection with compiled IDs
- Walk border and verify plugin detects correctly
- Identify any gaps or errors in coverage

**Example Data Structure:**
```json
{
  "region": "Varlamore",
  "description": "All region/chunk IDs that comprise Varlamore",
  "allowedChunkIds": [
    5600, 5601, 5602, 5603, 5604,
    5700, 5701, 5702, 5703, 5704,
    5800, 5801, 5802, 5803, 5804,
    // ... complete list
  ],
  "notes": "Extracted on 2026-02-16 via in-game exploration"
}
```

### 2.4 Reference: Region Locker Approach

**Existing Pattern:**
- Region Locker plugin uses similar chunk ID system
- Stores allowed regions in config or JSON file
- Provides UI to add/remove regions
- Visual overlay to show boundaries

**Adaptation for Varlamore:**
- Pre-populate with Varlamore chunks
- Make read-only (no user modification needed for v1)
- Focus on accuracy over flexibility

---

## 3. Existing Area-Locked Plugins

### 3.1 Region Locker Plugin

**Plugin Hub Name:** "Region Locker"
**Author:** Community contributor
**Purpose:** Lock account to specific map regions

**Key Features:**
- Define allowed regions by chunk ID
- Visual boundary overlay on world map
- Block travel outside defined regions
- Unlock system for progressive expansion
- Import/export region configurations

**Boundary Handling:**
- Uses HashSet of region IDs for O(1) lookup
- Checks player position every game tick
- Shows warning when approaching boundary
- Hard blocks travel actions (teleports, NPCs)

**UI/UX:**
- Side panel showing locked/unlocked regions
- Interactive map overlay (visual boundaries)
- Toggle regions on/off
- Import region presets

**Technical Implementation:**
- Subscribes to MenuOptionClicked for action blocking
- Uses GameTick to monitor player position
- Overlay for visual boundary rendering
- Config-based region storage

**Strengths:**
- Proven architecture for region locking
- Comprehensive travel blocking
- Good UX with visual feedback

**Limitations:**
- Generic (not specialized for specific region)
- May not handle all edge cases (POH portals, etc.)
- Lacks themed/immersive blocking (no NPC replacement)

### 3.2 Area Tasks Plugin

**Plugin Hub Name:** "Area Tasks" or similar
**Purpose:** Track completion of tasks in specific areas
**Relevance:** Shows area-based logic patterns

**Key Features:**
- Define tasks per region
- Track completion status
- Show available content in region

**Relevant Patterns:**
- Region-based content filtering
- Task categorization by area
- Persistent progress tracking

### 3.3 Ironman Restrictions Plugin

**Purpose:** Enforce ironman mode rules client-side
**Relevance:** Restriction enforcement patterns

**Key Features:**
- Block trading
- Block group activities
- Prevent using Grand Exchange
- Visual indicators for restricted content

**Relevant Patterns:**
- Menu entry blocking
- Action interception
- Clear user feedback on blocked actions

### 3.4 Quest Helper Plugin

**Purpose:** Assist with quest completion
**Relevance:** NPC interaction and dialogue handling

**Key Features:**
- Highlight quest NPCs
- Show dialogue options
- Track quest progress
- Custom overlays for quest objects

**Relevant Patterns:**
- NPC highlighting/overlays
- Dialogue tree tracking
- Step-by-step guidance system

**Adaptation Opportunity:**
- Use similar overlay techniques for NPC replacement
- Dialogue tracking for custom restriction messages

---

## 4. Feature Categories

### 4.1 Table Stakes (MUST HAVE)

**Core Restriction Enforcement:**

1. **Boundary Definition**
   - Complete and accurate Varlamore chunk ID list
   - Reliable boundary detection (< 1ms check time)
   - No false positives or negatives

2. **Teleport Spell Blocking**
   - Block all spellbook teleports outside Varlamore
   - Standard spellbook (Varrock, Lumbridge, Falador, etc.)
   - Ancient Magicks (all teleports)
   - Lunar spellbook (all teleports)
   - Arceuus spellbook (all teleports)
   - Clear feedback message when blocked

3. **Item-Based Teleport Blocking**
   - Glory amulet (all destinations)
   - Ring of dueling (all destinations)
   - Games necklace (all destinations)
   - Home teleport (if respawn outside Varlamore)
   - All teleport tablets
   - Achievement diary teleports

4. **Primary NPC Transport Blocking**
   - Ship/boat captains at Varlamore ports
   - Charter ship crews
   - Any NPCs that offer travel outside region
   - Replacement system with in-world dialogue

5. **Minigame Teleport Blocking**
   - Block all minigame teleports via group finder
   - Clear message explaining restriction

6. **Basic Configuration**
   - Enable/disable plugin toggle
   - Emergency override (for genuine accidents)
   - Config persistence across sessions

7. **User Feedback**
   - Chat messages when actions blocked
   - Clear explanation of why blocked
   - Non-intrusive but noticeable

**Critical Success Criteria:**
- Zero ways to accidentally leave Varlamore when plugin enabled
- No false blocks of internal Varlamore transport
- Plugin works reliably without crashes

### 4.2 Differentiators (NICE TO HAVE)

**Immersive Experience:**

1. **NPC Replacement System**
   - Hide original travel NPCs
   - Render custom replacement NPCs
   - Custom dialogue explaining lock
   - Themed messaging (e.g., "The seas beyond Varlamore are too dangerous")
   - Progressive dialogue hints at unlock conditions

2. **Milestone-Based Unlocks**
   - Define milestones (quests, achievements, levels)
   - Track completion automatically
   - Unlock specific travel methods upon milestone completion
   - Visual feedback when milestone achieved
   - Examples:
     - Complete "Children of the Sun" → unlock Quetzal network expansion
     - Reach combat level 100 → unlock wilderness teleports
     - Complete all Varlamore quests → unlock boat travel

3. **Boundary Visualization**
   - Overlay showing Varlamore boundary on minimap
   - Highlight when approaching boundary
   - Color-coded regions (green = allowed, red = locked)
   - Toggle visualization on/off

4. **Statistics Tracking**
   - Track blocked teleport attempts
   - Count milestones achieved
   - Time spent in region
   - Content completed within Varlamore
   - Share statistics in side panel

5. **Custom Respawn Handling**
   - Detect respawn point setting
   - Warn if set outside Varlamore
   - Suggest Varlamore respawn points
   - Option to auto-set respawn to Varlamore

6. **Comprehensive Side Panel**
   - Categorized settings (Restrictions, QoA, Unlocks)
   - Visual milestone progress
   - Quick toggle for restriction categories
   - Help/documentation section

**Quality of Life Features:**

7. **Internal Travel Highlights**
   - Highlight Quetzal transport NPCs
   - Show available internal travel options
   - Quick-travel interface for Varlamore

8. **Content Availability Guide**
   - Show what content is available in Varlamore
   - List accessible quests, bosses, skills
   - Item availability checker (can I get this in Varlamore?)

9. **Bank Organization Tools**
   - UIM-specific inventory management
   - Tag items by use/category
   - Highlight meta items for Varlamore UIM

10. **Achievement System**
    - Custom achievements for Varlamore UIM
    - Track unique milestones (first 99 skill, boss KC, etc.)
    - Leaderboard integration (if possible)

### 4.3 Anti-Features (DON'T BUILD)

**Things to Avoid:**

1. **Overly Complex Unlock System**
   - Don't create hundreds of micro-milestones
   - Avoid convoluted unlock dependencies
   - Keep unlocks meaningful and clear

2. **Performance-Heavy Features**
   - Don't render complex overlays every tick
   - Avoid excessive API calls
   - No background network requests without permission

3. **Intrusive UI**
   - Don't block the entire screen with warnings
   - Avoid popup spam
   - No auto-opening panels

4. **Server-Side Modifications**
   - Don't attempt to modify game state server-side
   - All restrictions must be client-side enforcement
   - Cannot actually prevent server actions (only client actions)

5. **Restrictive Config**
   - Don't hide essential settings
   - Allow emergency override
   - Provide clear disable option

6. **Incomplete Blocking**
   - Don't ship with known bypass methods
   - Test comprehensively before release
   - Core blocking must be complete

7. **Overcomplicated Architecture**
   - Don't over-engineer for hypothetical features
   - Keep MVP simple and functional
   - Iterate based on user feedback

8. **Unauthorized External Data**
   - Don't pull data from external APIs without permission
   - No tracking/analytics without consent
   - Respect user privacy

---

## 5. Priority Travel Methods by Risk Level

### CRITICAL (Must Block Immediately)

1. **Amulet of Glory** - Most common teleport item
2. **Home Teleport** - Free, unlimited, no requirements
3. **Minigame Teleports** - Easy access, bypasses restrictions
4. **Standard Spellbook City Teleports** - Common, low-level access
5. **Ring of Dueling** - Ferox Enclave (banking), common

### HIGH (Block in MVP)

6. **Games Necklace** - Wintertodt (skilling), Burthorpe
7. **Ship/Boat NPCs at Ports** - Direct mainland access
8. **POH Teleport** - If house outside Varlamore
9. **All Teleport Tablets** - Easy item-based teleports
10. **Charter Ships** - Port-to-port travel

### MEDIUM (Block in Iteration 2)

11. **Ancient Magicks Teleports** - Requires quest, less common
12. **Lunar Teleports** - Requires quest
13. **Fairy Rings** - Requires partial quest
14. **Spirit Trees** - Limited access, requires farming
15. **Skills Necklace** - Guild access
16. **Combat Bracelet** - Guild access
17. **Slayer Rings** - Requires slayer level

### LOW (Block in Polish Phase)

18. **Ectophial** - Specific quest item
19. **Pharaoh's Sceptre** - Rare, quest-specific
20. **Quest-specific teleports** - Limited use case
21. **Construction Cape** - Requires 99 Construction
22. **Max Cape variants** - High-level players only
23. **Random Events** - Mostly removed from game

---

## 6. Varlamore Content Checklist (Allow List)

### Quests Available in Varlamore

**Research Needed:** Specific quest IDs and names
- Children of the Sun (major Varlamore quest)
- Perilous Moons
- Twilight's Promise
- Defender of Varlamore
- Any other Varlamore-specific quests

**Action:** Allow starting and completing these quests
**Implementation:** Whitelist these quest IDs for interaction

### Skilling Methods in Varlamore

**Research Needed:** Specific skilling locations
- Hunter areas (Quetzal hunting mentioned)
- Fishing spots
- Mining locations
- Woodcutting areas
- Farming patches
- Agility courses
- Thieving opportunities

### Bosses/Combat Content

**Research Needed:**
- Varlamore-specific bosses
- Slayer monsters unique to region
- Combat training areas

### Items Obtainable in Varlamore

**Critical for UIM:**
- Storage items (looting bag, rune pouch, etc.)
- Tool upgrades available in region
- Armor/weapons obtainable without leaving
- Food sources (fishing, cooking, farming)

---

## 7. Implementation Recommendations

### Phase 1: MVP (Weeks 1-2)

**Scope:**
- Boundary system with hardcoded Varlamore chunks
- Block top 10 critical travel methods
- Basic config (enable/disable)
- Simple feedback messages

**Deliverables:**
- Working boundary detection
- Glory/games necklace/ring of dueling blocked
- Home teleport blocked (if outside Varlamore)
- Standard spellbook city teleports blocked
- Minigame teleports blocked
- Basic side panel with on/off toggle

**Success Metric:** Cannot leave Varlamore using common methods

### Phase 2: Core Features (Weeks 3-4)

**Scope:**
- Expand teleport blocking (medium risk items)
- NPC transport blocking (no replacement yet)
- Improved feedback messages
- Basic milestone tracking

**Deliverables:**
- Ancient/Lunar/Arceuus teleports blocked
- Ship/boat NPCs blocked
- Charter ships blocked
- Fairy rings blocked (if present)
- POH portal blocking
- Milestone framework implemented

**Success Metric:** Comprehensive coverage of common exit methods

### Phase 3: Polish & UX (Weeks 5-6)

**Scope:**
- NPC replacement system
- Boundary visualization
- Enhanced side panel
- Statistics tracking

**Deliverables:**
- Custom replacement NPCs with dialogue
- Minimap boundary overlay
- Categorized settings panel
- Unlock system (basic milestones)
- Statistics display

**Success Metric:** Immersive, polished user experience

### Phase 4: Edge Cases (Week 7+)

**Scope:**
- Low-risk travel methods
- Quest teleport handling
- Death mechanic safety
- Performance optimization

**Deliverables:**
- All known travel methods blocked
- Quest interaction validation
- Death respawn handling
- Performance benchmarks met

**Success Metric:** Zero known bypass methods

---

## 8. Testing Strategy

### Manual Testing Checklist

**Boundary Detection:**
- [ ] Walk entire Varlamore perimeter
- [ ] Verify detection at every border crossing
- [ ] Test in all sub-regions (cities, wilderness, coast)

**Teleport Blocking:**
- [ ] Test each teleport spell from each spellbook
- [ ] Test with all teleport items
- [ ] Verify feedback messages appear
- [ ] Confirm teleport is actually cancelled

**NPC Blocking:**
- [ ] Attempt travel with every transport NPC
- [ ] Verify NPCs are hidden/replaced correctly
- [ ] Test custom dialogue interactions
- [ ] Ensure internal NPCs (Quetzal) still work

**Edge Cases:**
- [ ] Death and respawn scenarios
- [ ] POH portal interactions
- [ ] Minigame teleport attempts
- [ ] Quest teleport scenarios
- [ ] Fairy ring code entry

**Performance:**
- [ ] Monitor frame rate with plugin enabled
- [ ] Check for lag during boundary checks
- [ ] Verify overlay rendering performance
- [ ] Test with multiple plugins enabled

### Automated Testing

**Unit Tests:**
- Boundary calculation logic
- Chunk ID validation
- Teleport destination lookup
- Restriction rule evaluation

**Integration Tests:**
- Event flow (menu click → restriction check)
- Config propagation
- Unlock state persistence

---

## 9. Plugin Hub Submission Requirements

### Metadata Requirements

**Plugin Descriptor:**
- Name: "Varlamore UIM" (or "Varlamore Locked")
- Description: Clear explanation of purpose
- Tags: varlamore, uim, area-locked, restriction, ironman
- Author: Your name/handle

**Documentation:**
- README with usage instructions
- Clear explanation of what the plugin does
- Warning about restriction enforcement
- Contact info for issues/feedback

### Technical Requirements

**Code Quality:**
- Follow RuneLite code conventions
- No prohibited API usage
- Proper error handling
- Performance benchmarks met

**Testing:**
- No crashes or game-breaking bugs
- Comprehensive manual testing
- Edge case handling

**User Safety:**
- Clear enable/disable mechanism
- Warning on first activation
- No data loss potential
- Reversible actions

### Submission Process

1. Code review via GitHub PR
2. Plugin Hub maintainer review
3. Testing by maintainers
4. Approval and publication
5. User feedback and iteration

---

## 10. Open Research Questions

### Questions Requiring In-Game Research

1. **Exact Varlamore Region IDs:**
   - What are all chunk IDs that comprise Varlamore?
   - Where exactly is the Kourend/Varlamore border?
   - Are there any Varlamore sub-regions with separate IDs?

2. **Quetzal Transport NPCs:**
   - What are the NPC IDs for Quetzal transport?
   - What are all Quetzal destinations?
   - Are there any Quetzal routes that approach region boundaries?

3. **Port/Ship NPCs:**
   - Are there ship NPCs in Varlamore ports?
   - What are their NPC IDs?
   - What destinations do they offer?

4. **Varlamore Quest Teleports:**
   - Do any Varlamore quests forcibly teleport players?
   - Are there quest items with teleport functions?
   - What are the destinations of any quest teleports?

5. **Respawn Points:**
   - Is there a respawn point within Varlamore?
   - What is the default respawn for a new account in Varlamore?
   - Can respawn be set to a Varlamore location?

6. **POH Location:**
   - Can Player-Owned Houses be located in Varlamore?
   - If so, where exactly?
   - What are the default portal room options?

7. **Fairy Rings:**
   - Are there fairy rings in Varlamore?
   - If so, what are their codes?
   - What codes lead to Varlamore (should be allowed)?

8. **Spirit Trees:**
   - Are there spirit trees in Varlamore?
   - Can they be planted there?
   - What destinations are offered?

### Questions Requiring Code Investigation

1. **Menu Entry Structure:**
   - What is the exact menu entry format for each travel type?
   - How to reliably detect teleport attempts?
   - What menu options need blocking?

2. **Widget IDs:**
   - What widget IDs are used for teleport interfaces?
   - What is the minigame teleport widget structure?
   - How to detect POH portal clicks?

3. **NPC Interaction:**
   - How to reliably hide NPCs in RuneLite?
   - What overlay technique works best for replacement?
   - How to inject custom dialogue into NPC interfaces?

4. **Performance Optimization:**
   - What is the performance cost of boundary checks per tick?
   - How to optimize chunk ID lookups?
   - When should caching be used?

### Questions for Community/Friend

1. **Desired Unlock Milestones:**
   - What milestones make sense for Varlamore UIM?
   - What should unlock first/last?
   - Should unlocks be progressive or achievement-based?

2. **Quality of Life Priorities:**
   - What QoA features are most important?
   - What UIM-specific tools would help?
   - What content tracking is desired?

3. **Immersion vs Functionality:**
   - How important is NPC replacement vs simple blocking?
   - Should feedback messages be themed or generic?
   - Level of detail for statistics/tracking?

---

## 11. Next Actions

### Immediate (Before Coding)

1. **Extract Varlamore Chunk IDs:**
   - Create temporary logging plugin
   - Walk entire Varlamore region
   - Compile complete chunk ID list
   - Validate boundary coverage

2. **Identify Quetzal NPCs:**
   - Document all Quetzal NPC IDs
   - Map all Quetzal destinations
   - Confirm all are internal to Varlamore

3. **Survey Port NPCs:**
   - Visit all Varlamore ports
   - Document ship/boat NPC IDs
   - Check dialogue options
   - Confirm destinations offered

4. **Research Varlamore Quests:**
   - List all quests starting in Varlamore
   - Check for forced teleportation
   - Document quest teleport items
   - Verify quest boundaries

### Short Term (Week 1)

1. **Set Up Development Environment:**
   - Configure RuneLite dev environment
   - Create test account (ideally Varlamore UIM)
   - Set up debugging tools
   - Familiarize with RuneLite API

2. **Implement Boundary System:**
   - Create BoundaryManager class
   - Load Varlamore chunk IDs
   - Implement isInBounds() check
   - Write unit tests

3. **Begin Travel Blocking:**
   - Subscribe to MenuOptionClicked
   - Implement teleport spell detection
   - Add basic blocking logic
   - Test with Glory amulet

### Medium Term (Weeks 2-4)

1. **Expand Travel Blocking:**
   - Add item-based teleport blocking
   - Implement NPC transport blocking
   - Add minigame teleport blocking
   - Test each category thoroughly

2. **Build UI Framework:**
   - Create side panel structure
   - Add basic configuration options
   - Implement categorized settings
   - Wire up config persistence

3. **Develop Feedback System:**
   - Implement chat message system
   - Create themed restriction messages
   - Add visual feedback (if applicable)
   - Polish user experience

### Long Term (Weeks 5+)

1. **Add Unlock System:**
   - Define milestone structure
   - Implement milestone tracking
   - Create unlock validation
   - Build unlock UI panel

2. **Implement NPC Replacement:**
   - Create NPC hiding system
   - Develop custom NPC overlay
   - Implement dialogue injection
   - Add replacement NPCs for key travel points

3. **Polish and Optimize:**
   - Performance benchmarking
   - Edge case testing
   - User testing (friend)
   - Prepare Plugin Hub submission

---

## 12. Success Metrics

### Core Functionality

- **Zero escape routes:** No methods to leave Varlamore when plugin enabled
- **No false positives:** Internal Varlamore transport works normally
- **Performance:** < 5ms for any restriction check
- **Reliability:** Zero crashes or game-breaking bugs

### User Experience

- **Clear feedback:** User always knows why action was blocked
- **Easy to use:** Plugin requires minimal configuration
- **Immersive:** Restrictions feel like game mechanics, not plugin blocks
- **Helpful:** Plugin assists Varlamore UIM gameplay, doesn't just restrict

### Technical Quality

- **Code quality:** Passes RuneLite code review standards
- **Documentation:** Clear README and inline documentation
- **Testing:** Comprehensive manual and automated testing
- **Maintainability:** Modular, extensible architecture

### Community Impact

- **Adoption:** Used by friend and other Varlamore UIM players
- **Feedback:** Positive reception on Plugin Hub
- **Contribution:** Open to community contributions and feedback
- **Education:** Helps others learn about Varlamore content

---

## 13. Risk Assessment

### High Risk Items

1. **Incomplete Boundary Data:**
   - Risk: Missing chunk IDs allows escapes
   - Mitigation: Thorough manual exploration and validation
   - Contingency: Community reporting and quick patches

2. **Unknown Travel Methods:**
   - Risk: Missing a travel method allows bypass
   - Mitigation: Comprehensive research and testing
   - Contingency: User feedback and iterative updates

3. **NPC ID Changes (Game Updates):**
   - Risk: OSRS update changes NPC IDs, breaks blocking
   - Mitigation: Monitor game updates, quick response
   - Contingency: Fallback to generic blocking if NPC IDs unknown

4. **Performance Issues:**
   - Risk: Plugin causes lag or crashes
   - Mitigation: Performance testing and optimization
   - Contingency: Disable expensive features if needed

### Medium Risk Items

1. **Dialogue Injection Fragility:**
   - Risk: Widget IDs change, custom dialogue breaks
   - Mitigation: Graceful fallback to chat messages
   - Contingency: Disable NPC replacement, keep action blocking

2. **Quest Teleport Edge Cases:**
   - Risk: Quest forcibly teleports outside region
   - Mitigation: Block starting quests that teleport out
   - Contingency: Warning system if detected

3. **POH Complexity:**
   - Risk: Many POH teleport options, hard to track
   - Mitigation: Comprehensive portal detection
   - Contingency: Block all POH entry if uncertain

### Low Risk Items

1. **Random Event Teleports:**
   - Risk: Random event forces teleport
   - Mitigation: Most removed from OSRS
   - Contingency: Document known issues

2. **Plugin Conflicts:**
   - Risk: Conflict with other plugins
   - Mitigation: Testing with common plugins
   - Contingency: Document incompatibilities

---

## Conclusion

This research document provides a comprehensive foundation for building a Varlamore-locked UIM plugin for RuneLite. The primary focus has been on identifying ALL possible travel and exit methods from Varlamore, categorized by type and risk level.

**Key Takeaways:**

1. **Comprehensive Travel Blocking Required:** Multiple vectors (NPCs, spells, items, systems) must all be addressed
2. **Boundary Accuracy Critical:** Exact chunk IDs must be extracted and validated
3. **Phased Approach Recommended:** MVP focusing on high-risk methods, iterate to comprehensive coverage
4. **Existing Plugins Provide Patterns:** Region Locker and similar plugins offer proven architecture
5. **Internal Transport Must Work:** Quetzal system and other Varlamore-specific transport must be whitelisted
6. **Immersion Enhances Experience:** NPC replacement system creates better UX than simple blocking

**Immediate Next Steps:**

1. Extract complete Varlamore chunk ID list
2. Document Quetzal NPC IDs and all internal transport
3. Identify all port/ship NPCs and their destinations
4. Begin MVP implementation (boundary + top 10 travel methods)

This research serves as the foundation for the technical implementation phase. All findings should be validated through in-game testing and updated as new information becomes available.

---

**Document Status:** Complete
**Last Updated:** 2026-02-16
**Next Review:** After MVP completion
**Maintained By:** Project Team
