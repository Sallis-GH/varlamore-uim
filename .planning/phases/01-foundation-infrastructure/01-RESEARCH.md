# Phase 1: Foundation & Infrastructure - Research

**Researched:** 2026-02-16
**Domain:** RuneLite Plugin Development (Java)
**Confidence:** HIGH

## Summary

Phase 1 establishes a RuneLite plugin with chunk ID-based boundary detection for Varlamore region restrictions. RuneLite uses a mature plugin architecture with event-driven patterns, dependency injection, and well-defined conventions enforced by Plugin Hub submission requirements.

**Standard stack:** RuneLite API (latest.release), Gradle build system, Java 11+, Gson (bundled), Lombok (common), JUnit for testing. Plugins follow strict BSD-2-Clause licensing and Plugin Hub structure requirements.

**Key technical decisions validated:**
- Chunk/region ID-based boundary detection is the standard approach (used by region-locker plugin and built into RuneLite's coordinate system)
- O(1) lookup via HashSet is proven pattern for region checks
- ConfigSection annotation supports categorized, extensible settings panels
- Menu entry swapping is the standard pattern for NPC interaction modification

**Primary recommendation:** Follow example-plugin template structure, use WorldPoint.getRegionID() for boundary checks with HashSet<Integer> storage, load region data via getResourceAsStream() from src/main/resources, and organize config with @ConfigSection for future extensibility.

## Standard Stack

### Core
| Library | Version | Purpose | Why Standard |
|---------|---------|---------|--------------|
| RuneLite API | latest.release | Client hooks, events, coordinate system | Required by Plugin Hub, provides game state access |
| Gradle | 7.x+ | Build system, dependency verification | Required by Plugin Hub, enforces reproducible builds |
| Java JDK | 11+ | Language runtime | RuneLite requirement, Eclipse Temurin recommended |
| Gson | Bundled | JSON parsing | Already available in RuneLite client, no extra dependency |

### Supporting
| Library | Version | Purpose | When to Use |
|---------|---------|---------|-------------|
| Lombok | 1.18.x | Annotation processing, boilerplate reduction | Common in RuneLite plugins, @Inject, @Slf4j |
| JUnit | 5.x | Unit testing | Standard for plugin testing, example-plugin includes |
| Guice | Bundled | Dependency injection | Already in RuneLite, use @Inject for services |

### Alternatives Considered
| Instead of | Could Use | Tradeoff |
|------------|-----------|----------|
| Gson | Jackson, org.json | Gson bundled with client, additional libs need verification |
| HashSet | ArrayList | ArrayList O(n) vs HashSet O(1) for contains() checks |
| Region IDs | WorldPoint storage | Region IDs more compact, proven by region-locker plugin |

**Installation:**
```bash
# Clone example-plugin template
git clone https://github.com/runelite/example-plugin.git varlamore-uim-plugin

# Update build.gradle
runeLiteVersion = 'latest.release'

# No additional dependencies needed for core functionality
```

## Architecture Patterns

### Recommended Project Structure
```
varlamore-uim-plugin/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── varlamoreuim/
│   │   │           ├── VarlamoreUimPlugin.java      # @PluginDescriptor, main class
│   │   │           ├── VarlamoreUimConfig.java      # Config interface
│   │   │           ├── VarlamoreUimPanel.java       # Side panel UI
│   │   │           └── BoundaryChecker.java         # Region validation service
│   │   └── resources/
│   │       ├── varlamore_regions.json               # Chunk ID data
│   │       └── icon.png                             # 48x72px plugin icon
│   └── test/
│       └── java/
│           └── com/varlamoreuim/
│               └── VarlamoreUimPluginTest.java
├── build.gradle                                      # Gradle configuration
├── runelite-plugin.properties                        # Plugin metadata
├── LICENSE                                           # BSD-2-Clause required
└── .gitignore
```

### Pattern 1: Event-Driven Plugin Lifecycle
**What:** Plugins extend Plugin base class with startUp()/shutDown() lifecycle and @Subscribe event handlers
**When to use:** Always - core RuneLite pattern
**Example:**
```java
// Source: https://github.com/runelite/runelite/wiki/Developer-Guide
@PluginDescriptor(
    name = "Varlamore UIM",
    description = "Ultimate Iron restrictions for Varlamore",
    tags = {"varlamore", "uim", "ironman", "region"}
)
@Slf4j
public class VarlamoreUimPlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    private VarlamoreUimConfig config;

    @Inject
    private ClientToolbar clientToolbar;

    private VarlamoreUimPanel panel;
    private NavigationButton navButton;
    private BoundaryChecker boundaryChecker;

    @Override
    protected void startUp() {
        // Initialize services
        boundaryChecker = new BoundaryChecker();
        boundaryChecker.loadRegions(); // Load from resources

        // Register UI components
        panel = injector.getInstance(VarlamoreUimPanel.class);
        navButton = NavigationButton.builder()
            .tooltip("Varlamore UIM")
            .icon(getIcon())
            .panel(panel)
            .build();
        clientToolbar.addNavigation(navButton);
    }

    @Override
    protected void shutDown() {
        // Clean up resources
        clientToolbar.removeNavigation(navButton);
        boundaryChecker = null;
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        if (event.getGameState() == GameState.LOGGED_IN) {
            // Player logged in, check location
            checkPlayerBoundary();
        }
    }
}
```

### Pattern 2: O(1) Region Boundary Detection
**What:** Use HashSet<Integer> for region IDs, check via WorldPoint.getRegionID()
**When to use:** Real-time boundary checks (every game tick if needed)
**Example:**
```java
// Based on region-locker plugin pattern and RuneLite coordinate system
public class BoundaryChecker {
    private final Set<Integer> varlamoreRegions = new HashSet<>();

    public void loadRegions() {
        try (InputStream is = getClass().getResourceAsStream("/varlamore_regions.json")) {
            // Use Gson (bundled with RuneLite)
            Gson gson = new Gson();
            int[] regionIds = gson.fromJson(
                new InputStreamReader(is),
                int[].class
            );
            for (int id : regionIds) {
                varlamoreRegions.add(id);
            }
        } catch (IOException e) {
            log.error("Failed to load Varlamore regions", e);
        }
    }

    public boolean isInVarlamore(WorldPoint location) {
        // O(1) HashSet lookup
        return varlamoreRegions.contains(location.getRegionID());
    }
}
```

**Region ID calculation:**
```java
// From WorldPoint.java source
public int getRegionID() {
    return ((x >> 6) << 8) | (y >> 6);
}
// Region is 64x64 tiles (8x8 chunks)
// Chunk is 8x8 tiles
```

### Pattern 3: Categorized Extensible Config Panel
**What:** Use @ConfigSection to organize settings into collapsible categories
**When to use:** When plugin has multiple feature areas (Restrictions, QoA, Tracking, etc.)
**Example:**
```java
// Source: https://github.com/runelite/runelite/wiki/Creating-plugin-config-panels
@ConfigGroup("varlamoreuim")
public interface VarlamoreUimConfig extends Config {

    // Main toggle
    @ConfigItem(
        position = 0,
        keyName = "enabled",
        name = "Enable Plugin",
        description = "Enable/disable Varlamore UIM restrictions"
    )
    default boolean enabled() {
        return true;
    }

    // Restrictions section (Phase 1)
    @ConfigSection(
        name = "Restrictions",
        description = "Boundary and travel restrictions",
        position = 1
    )
    String restrictionsSection = "restrictions";

    @ConfigItem(
        position = 2,
        keyName = "boundaryEnabled",
        name = "Enforce boundary",
        description = "Block travel outside Varlamore",
        section = restrictionsSection
    )
    default boolean boundaryEnabled() {
        return true;
    }

    // Future: QoA section (Phase 2)
    @ConfigSection(
        name = "Quality of Adventure",
        description = "QoA features and enhancements",
        position = 10,
        closedByDefault = true
    )
    String qoaSection = "qoa";

    // Future: Tracking section (Phase 3)
    @ConfigSection(
        name = "Progress Tracking",
        description = "Track unlocks and milestones",
        position = 20,
        closedByDefault = true
    )
    String trackingSection = "tracking";
}
```

### Pattern 4: Side Panel with Navigation Button
**What:** Swing-based PluginPanel registered via ClientToolbar
**When to use:** When plugin needs persistent UI beyond overlays
**Example:**
```java
// Source: https://github.com/runelite/runelite/blob/master/runelite-client/src/main/java/net/runelite/client/plugins/devtools/DevToolsPlugin.java
public class VarlamoreUimPanel extends PluginPanel {

    public VarlamoreUimPanel() {
        super();
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel();
        header.add(new JLabel("Varlamore UIM"));
        add(header, BorderLayout.NORTH);

        // Content with sections
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        // Restrictions section (Phase 1)
        content.add(createSection("Restrictions"));

        // Placeholder sections for future phases
        content.add(createSection("Quality of Adventure (Coming Soon)"));
        content.add(createSection("Progress Tracking (Coming Soon)"));

        add(content, BorderLayout.CENTER);
    }

    private JPanel createSection(String title) {
        JPanel section = new JPanel();
        section.setBorder(BorderFactory.createTitledBorder(title));
        // Add section content
        return section;
    }
}
```

### Pattern 5: Resource Loading from JAR
**What:** Load data files via getResourceAsStream(), not getResource()
**When to use:** Loading JSON, CSV, or other bundled data files
**Example:**
```java
// Source: https://github.com/runelite/plugin-hub README
// CORRECT - works in both IDE and deployed JAR
try (InputStream is = getClass().getResourceAsStream("/varlamore_regions.json")) {
    // Process stream
}

// INCORRECT - returns jar-URL in deployment, file-URL in IDE
// URL url = getClass().getResource("/varlamore_regions.json");
```

### Pattern 6: Menu Entry Modification (for NPC replacement)
**What:** Subscribe to MenuEntryAdded event, modify or hide menu options
**When to use:** Changing NPC interactions, swapping default actions
**Example:**
```java
// Source: https://github.com/runelite/runelite/wiki/Menu-Entry-Swapper
@Subscribe
public void onMenuEntryAdded(MenuEntryAdded event) {
    if (!config.enabled()) {
        return;
    }

    // Check if player is trying to use exit transport
    if (isExitTransportNpc(event.getTarget())) {
        if (!boundaryChecker.isInVarlamore(client.getLocalPlayer().getWorldLocation())) {
            // Player already outside, allow normal travel
            return;
        }

        // Replace or modify menu entry
        // Implementation depends on specific Quetzal NPCs
    }
}

private boolean isExitTransportNpc(String target) {
    // Check against known Quetzal transport NPCs that exit Varlamore
    // Will need actual NPC IDs from game data
    return target.contains("Quetzal") && isExitLocation();
}
```

### Anti-Patterns to Avoid
- **Don't use ArrayList for region checks:** O(n) lookup kills performance on every location check
- **Don't skip dependency verification:** Plugin Hub rejects plugins with unverified dependencies
- **Don't use INFO logging excessively:** Clutters user logs, use DEBUG for developer info
- **Don't forget overlay cleanup:** Register in startUp(), unregister in shutDown() to prevent memory leaks
- **Don't use getResource() for JAR files:** Use getResourceAsStream() for deployed plugins

## Don't Hand-Roll

| Problem | Don't Build | Use Instead | Why |
|---------|-------------|-------------|-----|
| Coordinate system conversion | Custom x/y to region math | WorldPoint.getRegionID(), WorldPoint.fromRegion() | Handles instancing, mirroring (Prifddinas), proven in production |
| Menu option swapping | Direct menu manipulation | @Subscribe MenuEntryAdded event pattern | RuneLite event bus, tested extensively |
| Configuration UI | Custom Swing panels for settings | @ConfigItem, @ConfigSection annotations | Auto-generates UI, persists settings, standard UX |
| Dependency injection | Manual service initialization | Guice @Inject | Already in RuneLite, prevents lifecycle bugs |
| JSON parsing | String manipulation, regex | Gson (bundled) | No extra dependency, handles edge cases |
| Resource loading | File paths, manual JAR extraction | getResourceAsStream() | Works in IDE and deployed JAR consistently |

**Key insight:** RuneLite's API is mature (6+ years) and handles edge cases you won't anticipate (instanced regions, mirrored areas, coordinate system changes). Use provided APIs rather than reimplementing.

## Common Pitfalls

### Pitfall 1: Memory Leaks from Unreleased Resources
**What goes wrong:** Registering overlays, navigation buttons, or event handlers in startUp() but forgetting to unregister in shutDown()
**Why it happens:** RuneLite keeps plugin instances in memory when disabled, unreleased resources accumulate
**How to avoid:**
- Every `overlayManager.add()` needs matching `overlayManager.remove()` in shutDown()
- Every `clientToolbar.addNavigation()` needs matching `removeNavigation()` in shutDown()
- Store references to registered components for cleanup
**Warning signs:** Client memory usage grows when toggling plugin repeatedly, other plugins slow down

### Pitfall 2: Using ArrayList Instead of HashSet for Region Checks
**What goes wrong:** `regionList.contains(regionId)` called every game tick becomes performance bottleneck
**Why it happens:** ArrayList seems simpler, O(n) not obvious for small datasets
**How to avoid:** Always use HashSet<Integer> for region ID storage, O(1) lookup critical for real-time checks
**Warning signs:** FPS drops when plugin enabled, client lag in specific areas, Plugin Hub performance complaints

### Pitfall 3: Outdated runeLiteVersion in build.gradle
**What goes wrong:** Plugin breaks after RuneLite updates, users get errors, Plugin Hub CI fails
**Why it happens:** Forgetting to update version string, using specific version instead of latest.release
**How to avoid:** Set `runeLiteVersion = 'latest.release'` in build.gradle, never pin to specific version
**Warning signs:** "Failed to load plugin" errors after RuneLite updates, Plugin Hub build failures

### Pitfall 4: Using getResource() Instead of getResourceAsStream()
**What goes wrong:** Plugin works in IDE but fails when deployed to Plugin Hub as JAR
**Why it happens:** getResource() returns file-URL in IDE, jar-URL in deployment, different behavior
**How to avoid:** Always use `getResourceAsStream()` for loading bundled resources, test with `./gradlew runeLite`
**Warning signs:** "FileNotFoundException" in deployed plugin, works locally but not for users

### Pitfall 5: Missing Dependency Verification
**What goes wrong:** Plugin Hub rejects submission, long review delays
**Why it happens:** Adding non-transitive dependency without Gradle dependency verification
**How to avoid:** Only use dependencies already in runelite-client, or add cryptographic hash verification
**Warning signs:** Plugin Hub CI failure with dependency verification error, reviewer requests changes

### Pitfall 6: INFO Level Logging Spam
**What goes wrong:** User log files fill with debug messages, complaints about log spam
**Why it happens:** Using log.info() for debugging instead of log.debug()
**How to avoid:**
- INFO: Important user-facing events (plugin loaded, feature activated)
- DEBUG: Developer debugging (region checks, menu events)
- ERROR: Actual failures requiring user attention
**Warning signs:** Log file size complaints, users reporting performance issues

### Pitfall 7: Forgetting BSD-2-Clause License
**What goes wrong:** Plugin Hub rejects submission immediately
**Why it happens:** Copying wrong license template or missing LICENSE file entirely
**How to avoid:** Use example-plugin template, verify LICENSE file exists before submission
**Warning signs:** Plugin Hub automated check fails, "missing license" error

### Pitfall 8: Event Handler Not Checking Plugin State
**What goes wrong:** @Subscribe methods fire even when plugin disabled via config toggle
**Why it happens:** Event subscriptions active as long as plugin loaded, not tied to config.enabled()
**How to avoid:** First line of every @Subscribe method: `if (!config.enabled()) return;`
**Warning signs:** Plugin behavior occurs when user thinks it's disabled, config toggle seems broken

## Code Examples

Verified patterns from official sources:

### Loading Configuration
```java
// Source: https://github.com/runelite/runelite/wiki/Creating-plugin-config-panels
@Inject
private VarlamoreUimConfig config;

@Provides
VarlamoreUimConfig provideConfig(ConfigManager configManager) {
    return configManager.getConfig(VarlamoreUimConfig.class);
}

// Accessing config values
if (config.boundaryEnabled()) {
    // Check boundary
}
```

### Region ID Lookup Data Structure
```java
// Efficient O(1) lookup for boundary detection
private final Set<Integer> varlamoreRegions = new HashSet<>();

// Load from JSON
try (InputStream is = getClass().getResourceAsStream("/varlamore_regions.json")) {
    Gson gson = new Gson();
    int[] regionIds = gson.fromJson(new InputStreamReader(is), int[].class);
    varlamoreRegions.addAll(Arrays.stream(regionIds).boxed().collect(Collectors.toSet()));
}

// Check boundary
WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
boolean inVarlamore = varlamoreRegions.contains(playerLocation.getRegionID());
```

### JSON Data File Format
```json
{
  "description": "Varlamore region IDs (chunk-based boundary)",
  "version": "1.0",
  "regions": [
    5678,
    5679,
    5934,
    5935
  ]
}
```
*Note: Actual region IDs need to be collected using RuneLite's world-location plugin or extracted from game data*

### Game Tick Event for Real-Time Checking
```java
// Subscribe to game tick for continuous boundary monitoring
@Subscribe
public void onGameTick(GameTick event) {
    if (!config.enabled() || !config.boundaryEnabled()) {
        return;
    }

    Player player = client.getLocalPlayer();
    if (player == null) {
        return;
    }

    WorldPoint location = player.getWorldLocation();
    boolean inVarlamore = boundaryChecker.isInVarlamore(location);

    // Update UI or trigger warnings
    panel.updateBoundaryStatus(inVarlamore);
}
```

### Plugin Metadata (runelite-plugin.properties)
```properties
# Source: https://github.com/runelite/plugin-hub
displayName=Varlamore UIM
author=YourName
description=Ultimate Iron Man restrictions and enhancements for Varlamore region
tags=varlamore,uim,ironman,region,restriction
plugins=com.varlamoreuim.VarlamoreUimPlugin
```

### Build Configuration (build.gradle)
```gradle
// Source: https://github.com/runelite/example-plugin
plugins {
    id 'java'
}

repositories {
    mavenLocal()
    maven {
        url = 'https://repo.runelite.net'
    }
    mavenCentral()
}

def runeLiteVersion = 'latest.release'

dependencies {
    compileOnly group: 'net.runelite', name:'client', version: runeLiteVersion

    compileOnly 'org.projectlombok:lombok:1.18.30'
    annotationProcessor 'org.projectlombok:lombok:1.18.30'

    testImplementation 'junit:junit:4.12'
    testImplementation group: 'net.runelite', name:'client', version: runeLiteVersion
}

group = 'com.varlamoreuim'
version = '1.0-SNAPSHOT'
sourceCompatibility = '11'

tasks.withType(JavaCompile) {
    options.encoding = 'UTF-8'
}
```

## State of the Art

| Old Approach | Current Approach | When Changed | Impact |
|--------------|------------------|--------------|--------|
| Manual coordinate boundary boxes | Region/chunk ID sets with WorldPoint API | RuneLite API maturity (~2019) | More robust, handles instanced regions |
| String-based menu manipulation | MenuEntryAdded event system | Menu API refactor (~2020) | Type-safe, less brittle |
| Manual config UI with Swing | @ConfigItem/@ConfigSection annotations | Config system v2 (~2018) | Auto-generated, consistent UX |
| File-based plugin loading | Plugin Hub with dependency verification | Plugin Hub launch (2019) | Secure, reproducible builds |
| Custom JSON libraries | Use bundled Gson | Always bundled | No extra dependencies |

**Deprecated/outdated:**
- **MenuOpened event for menu swapping:** Replaced by MenuEntryAdded for most use cases
- **Custom coordinate conversion math:** WorldPoint.getRegionID() handles edge cases
- **Pinned runeLiteVersion:** Use 'latest.release' to stay compatible
- **Eclipse IDE:** While supported, IntelliJ IDEA is now recommended standard

## Open Questions

### 1. Varlamore Region ID Collection
- **What we know:** Regions are 64x64 tiles, identified by unique IDs, WorldPoint.getRegionID() provides lookup
- **What's unclear:** Complete list of region IDs covering all Varlamore areas (Auburnvale, Hailstorm Mountains, Tlati Rainforest, Aldarin, Avium Savannah, Civitas Illa Fortis)
- **Recommendation:** Use RuneLite's world-location plugin to manually map boundaries, or extract from OSRS Wiki map data. Plan task to collect region IDs before implementation.

### 2. Quetzal Transport NPC Identification
- **What we know:** Quetzal Transport System has 8 initial + 6 buildable landing sites, some are exits from Varlamore
- **What's unclear:** Specific NPC IDs for Quetzal transport NPCs, which landing sites are "exits" vs internal
- **Recommendation:** Use RuneLite DevTools plugin to inspect NPC IDs during gameplay, document which NPCs need menu modification. May require in-game testing.

### 3. Internal vs Exit Transport Whitelisting
- **What we know:** Requirement BNDRY-03 states "Internal Varlamore transport (Quetzal system) works normally"
- **What's unclear:** Complete mapping of internal (Varlamore-to-Varlamore) vs exit (Varlamore-to-outside) routes
- **Recommendation:** Create data file with NPC IDs and route classifications, test each landing site in-game. Consider edge cases like player building new sites.

### 4. Testing Without Varlamore Access
- **What we know:** JUnit testing supported, plugin testing possible with `./gradlew runeLite`
- **What's unclear:** How to test boundary detection without actually being in Varlamore in-game
- **Recommendation:** Focus Unit tests on region ID lookup logic (mock WorldPoint), integration testing requires actual gameplay in Varlamore region

## Sources

### Primary (HIGH confidence)
- [RuneLite Developer Guide](https://github.com/runelite/runelite/wiki/Developer-Guide) - Event system, plugin structure, architecture patterns
- [RuneLite Plugin Hub Repository](https://github.com/runelite/plugin-hub) - Submission requirements, build.gradle structure, dependency verification
- [RuneLite Example Plugin](https://github.com/runelite/example-plugin) - Template structure, best practices
- [RuneLite WorldPoint.java Source](https://github.com/runelite/runelite/blob/master/runelite-api/src/main/java/net/runelite/api/coords/WorldPoint.java) - Region ID calculation, coordinate system
- [Plugin Hub Information Wiki](https://github.com/runelite/runelite/wiki/Information-about-the-Plugin-Hub) - Plugin Hub requirements and rules

### Secondary (MEDIUM confidence)
- [Region Locker Plugin](https://github.com/slaytostay/region-locker) - Chunk restriction pattern, config system for region lists
- [Creating Plugin Config Panels Wiki](https://github.com/runelite/runelite/wiki/Creating-plugin-config-panels) - @ConfigSection usage, settings organization
- [Menu Entry Swapper Wiki](https://github.com/runelite/runelite/wiki/Menu-Entry-Swapper) - NPC interaction modification patterns
- [OSRSBox Plugin Tutorials](https://www.osrsbox.com/blog/2018/08/12/writing-runelite-plugins-part-2-structure/) - Plugin structure, configuration, overlays
- [Sly Automation RuneLite Plugin Tutorial](https://www.slyautomation.com/blog/creating-your-first-runelite-plugin/) - Getting started, basic patterns

### Tertiary (LOW confidence - needs verification)
- [Varlamore OSRS Wiki](https://oldschool.runescape.wiki/w/Varlamore) - Region areas, general geography (needs region ID mapping)
- [Quetzal Transport System OSRS Wiki](https://oldschool.runescape.wiki/w/Quetzal_Transport_System) - Landing sites, transport mechanics (needs NPC ID data)
- [Rune-Server Region System](https://rune-server.org/threads/how-the-runescape-region-system-works.318873/) - Chunk/region math explanation (verified against WorldPoint source)

## Metadata

**Confidence breakdown:**
- **Standard stack:** HIGH - Official example-plugin, Plugin Hub requirements documented, RuneLite API stable
- **Architecture:** HIGH - Multiple official sources, verified patterns in production plugins, clear best practices
- **Pitfalls:** MEDIUM-HIGH - Derived from issue trackers, Plugin Hub rejections, community experience
- **Varlamore-specific data:** LOW - Region IDs and NPC IDs need in-game collection, not documented

**Research date:** 2026-02-16
**Valid until:** ~2026-03-16 (30 days - RuneLite API stable, Varlamore region static since 2025-07-23)

**Key gaps requiring Phase 1 planning:**
1. Collect complete Varlamore region ID list via world-location plugin
2. Identify Quetzal transport NPC IDs via DevTools plugin
3. Map internal vs exit Quetzal routes through gameplay testing
4. Determine chunk-level precision needed for boundary (region-level may be sufficient)
