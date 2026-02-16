# Project Research Summary

**Project:** Varlamore UIM Plugin
**Domain:** RuneLite Plugin Development (OSRS Client Modification)
**Researched:** 2026-02-16
**Confidence:** HIGH

## Executive Summary

This research covers the development of a RuneLite plugin that enforces Varlamore area-lock restrictions for Ultimate Ironman (UIM) accounts, with milestone-based unlocking. RuneLite plugins follow a well-established event-driven architecture using Guice dependency injection, with strict Plugin Hub submission requirements. The domain is well-documented with clear patterns for menu entry interception, NPC manipulation, and UI panels.

The recommended approach is a phased development starting with core boundary detection and teleport blocking (MVP), then expanding to comprehensive travel method coverage, NPC replacement for immersion, and finally a milestone unlock system. The architecture follows RuneLite's plugin pattern with modular components (BoundaryManager, TravelRestrictionEngine, NpcReplacementSystem, UnlockManager) that communicate through events.

Key risks include missing obscure teleport methods (fairy rings, POH portals, minigame teleports), Plugin Hub rejection due to licensing/dependency violations, and performance issues from inefficient event processing. These are mitigated through comprehensive travel method mapping, strict adherence to BSD-2-Clause licensing, and event-driven (not polling-based) architecture.

## Key Findings

### Recommended Stack

RuneLite plugin development has a mature, well-defined technology stack. All plugins use Java 11 as the baseline with Gradle as the build system. The core dependencies are provided by RuneLite itself (Guice for DI, SLF4J for logging, Guava for utilities), with Lombok used for boilerplate reduction and Gson for JSON data files.

**Core technologies:**
- **Java 11 (LTS)**: Required baseline for RuneLite compatibility — industry standard, maximum plugin compatibility
- **Gradle 7.x/8.x**: Build automation and dependency management — RuneLite standard build system
- **Guice**: Dependency injection framework (provided by RuneLite) — field/constructor injection with @Inject
- **Lombok 1.18.30+**: Code generation (@Slf4j, @Getter, @Data) — reduces boilerplate, widely used in RuneLite plugins
- **Gson**: JSON parsing for data files (chunk IDs, NPC mappings, milestones) — lightweight, no external dependencies

**Critical version requirements:**
- Use `compileOnly` scope for RuneLite client dependencies (provided at runtime)
- Target Java 11 bytecode for maximum compatibility
- No external HTTP clients or unapproved dependencies (Plugin Hub requirement)

### Expected Features

Area-locked account plugins require comprehensive travel restriction enforcement as table stakes, with immersive NPC replacement and milestone progression as competitive differentiators.

**Must have (table stakes):**
- **Complete boundary detection** — accurate Varlamore chunk ID list, reliable position checking
- **Teleport spell blocking** — all spellbooks (Standard, Ancient, Lunar, Arceuus), home teleport
- **Item teleport blocking** — jewelry (glory, dueling, games necklace), tablets, achievement diary gear
- **NPC transport blocking** — ships, charter ships, magic carpets, gliders, spirit trees
- **Minigame teleport blocking** — group finder teleports, minigame-specific interfaces
- **User feedback** — clear chat messages explaining why actions are blocked
- **Basic configuration** — enable/disable toggle, emergency override option

**Should have (competitive):**
- **NPC replacement system** — hide travel NPCs, render custom replacements, inject themed dialogue
- **Milestone-based unlocks** — progressive unlock system based on quests, levels, achievements
- **Boundary visualization** — minimap/world overlay showing region boundary
- **Side panel UI** — categorized settings (Restrictions, QoA, Unlocks, Settings)
- **Statistics tracking** — blocked attempts, milestones achieved, time in region

**Defer (v2+):**
- **Multi-region profiles** — support for other area locks (Kourend, Wilderness, etc.)
- **Network features** — leaderboards, shared unlocks, community verification
- **Advanced QoA tools** — inventory management, bank organization, content availability guide
- **Content tracking** — available quests, bosses, items within region

### Architecture Approach

RuneLite plugins follow an event-driven architecture with clear lifecycle management (startUp/shutDown) and dependency injection via Guice. The plugin subscribes to game events (MenuOptionClicked, NpcSpawned, WidgetLoaded) and processes them through modular components, with all state cleared on shutdown to prevent memory leaks.

**Major components:**
1. **BoundaryManager** — loads Varlamore chunk IDs, provides fast O(1) boundary checking via HashSet lookup
2. **TravelRestrictionEngine** — intercepts travel actions via MenuOptionClicked, validates against boundary and unlock state, returns allow/block result
3. **NpcReplacementSystem** — tracks NPCs to hide via NpcSpawned event, renders custom overlays, injects dialogue via WidgetLoaded
4. **UnlockManager** — tracks milestone completion state, persists to config, validates unlock conditions for travel methods
5. **UI System** — side panel with tabbed categories, navigation button, config bindings

**Key patterns:**
- Event-driven processing (never polling on GameTick)
- Handler registration for extensibility (add new restriction types without rearchitecting)
- Data-driven configuration (JSON files for chunk IDs, NPC mappings, milestones)
- Graceful degradation (errors allow action rather than breaking game)

### Critical Pitfalls

**Top 5 pitfalls with prevention:**

1. **Plugin Hub rejection due to licensing** — Automatic rejection without BSD-2-Clause license file in repository root. Prevention: Add LICENSE file with BSD-2-Clause content before first commit, verify in runelite-plugin.properties.

2. **Memory leaks from event subscriptions** — Collections of game objects (NPCs, Players) never cleared cause memory accumulation. Prevention: Clear all collections in shutDown(), use @Subscribe (automatic cleanup), remove overlays and UI elements on shutdown.

3. **Missed teleport methods** — Obscure travel methods (fairy rings, POH portals, minigame teleports) bypass restrictions. Prevention: Comprehensive travel method mapping from FEATURES.md, test all 60+ travel methods, maintain update list for new content.

4. **Threading issues (Client API on wrong thread)** — Calling Client API from non-game threads causes crashes. Prevention: Never call Client API from Swing EDT or custom threads, use clientThread.invoke() for async operations, keep UI updates on EDT and game queries on game thread.

5. **Performance issues from inefficient processing** — Heavy processing in MenuEntryAdded or GameTick causes noticeable lag. Prevention: Use fast string comparisons (no regex), pre-compute teleport destination cache, early return for non-relevant events, minimize object allocation in hot paths.

## Implications for Roadmap

Based on research, suggested phase structure prioritizes working restrictions over polish, with incremental expansion:

### Phase 1: Core Boundary + High-Risk Teleports (MVP)
**Rationale:** Establish foundation (boundary detection) and block the most common escape routes first. Research shows boundary accuracy is critical — without it, nothing else matters. Top 10 teleport methods (glory, home teleport, standard spells, minigame teleports) cover 90% of player travel.

**Delivers:** Working boundary system with Varlamore chunks, blocks glory/games necklace/ring of dueling, blocks standard spellbook city teleports, blocks home teleport if outside region, blocks minigame group finder teleports, basic on/off config toggle.

**Addresses:** Table stakes features (boundary detection, basic teleport blocking), critical "Complete boundary detection" and "Teleport spell blocking" requirements.

**Avoids:** Plugin Hub rejection (establishes proper licensing/structure), threading issues (event-driven from start), missed high-traffic travel methods.

**Research Flag:** Requires in-game data extraction to map Varlamore chunk IDs — use temporary logging plugin to walk boundary and capture region IDs.

### Phase 2: Comprehensive Travel Coverage
**Rationale:** Expand from high-risk to medium/low-risk teleports, NPC transport, and object-based travel. Architecture research shows handler pattern makes this extension clean — add new RestrictionHandler implementations without modifying core engine.

**Delivers:** Ancient/Lunar/Arceuus teleport blocking, item teleports (all jewelry, tablets, quest items), NPC transport blocking (ships, carpets, gliders), fairy rings and spirit trees blocked, POH portal blocking, charter ship blocking.

**Addresses:** Table stakes "Item teleport blocking" and "NPC transport blocking", covers 95%+ of travel methods.

**Uses:** TravelRestrictionEngine with SpellRestrictionHandler, ItemRestrictionHandler, NpcTravelHandler components from ARCHITECTURE.md.

**Avoids:** "Missed teleport methods" pitfall by comprehensive mapping from FEATURES.md research (60+ travel methods documented).

**Research Flag:** Standard patterns, no additional research needed (all teleports mapped in FEATURES.md).

### Phase 3: NPC Replacement + Immersion
**Rationale:** Core restrictions work (Phase 1-2), now add immersion layer. Research shows NPC replacement is technically complex but not critical path — good candidate for separate phase after core stability proven.

**Delivers:** NPC hiding system via NpcSpawned tracking, replacement NPC overlay rendering, custom dialogue injection via WidgetLoaded, themed blocking messages (e.g., "The seas beyond Varlamore are too dangerous"), NPC replacement registry with JSON data.

**Addresses:** Should-have "NPC replacement system" differentiator, improves UX from generic blocking to themed experience.

**Implements:** NpcReplacementSystem, CustomDialogueManager, ReplacementNpcOverlay components from architecture.

**Avoids:** "Dialogue state corruption" pitfall through fallback to ChatMessageManager if widget manipulation fails.

**Research Flag:** May need iteration on dialogue injection technique — widget IDs can change with OSRS updates, needs testing.

### Phase 4: Milestone System + Unlocks
**Rationale:** With restrictions stable and immersive, add progression layer. Research shows unlock system requires persistence layer and milestone condition evaluation — adds complexity but core architecture supports it via UnlockManager module.

**Delivers:** Milestone data structure with condition types (quest completion, skill level, combat level), UnlockManager with state persistence to config, unlock validation integrated into TravelRestrictionEngine, side panel showing milestone progress, unlock notifications (chat + UI).

**Addresses:** Should-have "Milestone-based unlocks" competitive differentiator.

**Implements:** UnlockManager, MilestoneTracker, milestone JSON definitions from architecture.

**Avoids:** Performance issues through event-driven milestone checking (not polling).

**Research Flag:** Milestone definitions need design — work with friend to identify meaningful progression gates for Varlamore UIM.

### Phase 5: UI Polish + Settings
**Rationale:** Functionality complete (Phases 1-4), polish user-facing elements. Research shows Plugin Hub values good UX — comprehensive settings and clean UI improve submission chances.

**Delivers:** Tabbed side panel (Restrictions, QoA, Unlocks, Settings), categorized config sections, boundary visualization overlay, notification system for unlocks, statistics tracking (blocked attempts, time in region), comprehensive help/documentation.

**Addresses:** Should-have "Side panel UI" and "Statistics tracking" features.

**Implements:** VarlamoreUimPanel with CategoryPanel structure from architecture.

**Avoids:** "Overlay performance issues" through minimized rendering and efficient draw calls.

**Research Flag:** Standard UI patterns, no additional research needed.

### Phase 6: Testing + Submission
**Rationale:** Feature complete, now comprehensive testing and Plugin Hub submission. Research shows submission process has many rejection vectors — dedicated phase for compliance verification.

**Delivers:** Full test coverage (all 60+ travel methods), boundary edge case testing (instanced areas, death mechanics, world hopping), Plugin Hub compliance (BSD-2-Clause, no unapproved deps, code quality), README and documentation, GitHub repository with issues enabled.

**Addresses:** All "must have" validation, Plugin Hub submission requirements.

**Avoids:** All Plugin Hub rejection pitfalls (licensing, naming, code quality, dependencies, assets) through systematic checklist.

**Research Flag:** No additional research — use PITFALLS.md checklist for submission validation.

### Phase Ordering Rationale

- **Phases 1-2 together form usable MVP** — player genuinely cannot leave Varlamore with common methods
- **Phase 3 (NPC replacement) is independent** — can be skipped if time-constrained; core restrictions still work
- **Phase 4 (milestones) requires Phases 1-2** — no point unlocking travel if travel restrictions don't work
- **Phase 5 (UI polish) deferred to end** — functionality over aesthetics, Plugin Hub accepts basic UI
- **Dependency chain:** Phase 1 (foundation) → Phase 2 (completeness) → Phase 3 (immersion) → Phase 4 (progression) → Phase 5 (polish) → Phase 6 (release)

This ordering follows ARCHITECTURE.md's 5-layer dependency graph: foundation (boundary) → core logic (restrictions) → feature implementation (NPC system) → UI (panels) → integration (testing).

### Research Flags

Phases likely needing deeper research during planning:
- **Phase 1:** Needs in-game Varlamore chunk ID extraction — create temporary logging plugin, walk boundary, compile region IDs
- **Phase 3:** Dialogue injection technique may need refinement — widget IDs change with OSRS updates, needs testing with current client version
- **Phase 4:** Milestone definitions require design collaboration — work with friend to identify meaningful Varlamore UIM progression gates

Phases with standard patterns (skip research-phase):
- **Phase 2:** All travel methods documented in FEATURES.md, standard menu entry interception patterns from ARCHITECTURE.md
- **Phase 5:** Standard RuneLite UI patterns (PluginPanel, NavigationButton), well-documented in API
- **Phase 6:** Plugin Hub checklist from PITFALLS.md covers all requirements, no ambiguity

## Confidence Assessment

| Area | Confidence | Notes |
|------|------------|-------|
| Stack | HIGH | Java 11 + Gradle + Lombok + Guice are verified RuneLite standards, no ambiguity in tech choices |
| Features | HIGH | Comprehensive FEATURES.md research identified 60+ travel methods with clear table stakes vs. nice-to-have |
| Architecture | HIGH | RuneLite plugin pattern is well-established, event-driven approach verified through existing plugins |
| Pitfalls | HIGH | PITFALLS.md cataloged 40+ specific pitfalls with prevention strategies from Plugin Hub rejection data |

**Overall confidence:** HIGH

### Gaps to Address

**Varlamore-specific data requires in-game extraction:**
- **Gap:** Exact Varlamore chunk IDs unknown until extracted from game
- **Handle:** Phase 1 includes data extraction step — create temporary logging plugin to walk boundary and capture all region IDs, compile into varlamore_chunks.json resource file
- **Risk:** Low — technique is proven (used by Region Locker plugin), just needs execution time

**Milestone definitions need design collaboration:**
- **Gap:** Meaningful progression gates for Varlamore UIM require domain knowledge (what milestones make sense?)
- **Handle:** Phase 4 planning includes milestone design workshop with friend who requested plugin, balance between too-easy and too-grindy unlocks
- **Risk:** Low — affects unlock timing, not core functionality; can iterate based on playtesting

**OSRS content updates may add new travel methods:**
- **Gap:** Future OSRS updates could add teleports not in current research
- **Handle:** Maintain update monitoring process, add new methods as discovered, community reporting via GitHub issues
- **Risk:** Low — new teleports are rare (1-2 per year), plugin architecture supports adding handlers without rearchitecting

## Sources

### Primary (HIGH confidence)
- RuneLite Plugin Hub Guidelines — https://github.com/runelite/runelite/wiki/Plugin-Hub — submission requirements verified
- RuneLite API Javadoc — https://static.runelite.net/api/runelite-api/ — event system, Client API, widget manipulation confirmed
- Example Plugin Repository — https://github.com/runelite/example-plugin — project structure verified
- OSRS Wiki Teleportation — https://oldschool.runescape.wiki/w/Teleportation — comprehensive teleport method list

### Secondary (MEDIUM confidence)
- Region Locker Plugin — Plugin Hub — architecture patterns for boundary checking inferred from similar plugin
- Quest Helper Plugin — Plugin Hub — NPC overlay and dialogue patterns observed
- OSRS UIM Death Storage Guide — Wiki — UIM-specific mechanics (death storage, looting bag) documented

### Tertiary (LOW confidence)
- Varlamore region IDs — needs in-game extraction — chunk boundary mapping requires manual data collection
- Current widget IDs for dialogues — needs testing — widget constants may have changed in recent OSRS updates

---
*Research completed: 2026-02-16*
*Ready for roadmap: yes*
