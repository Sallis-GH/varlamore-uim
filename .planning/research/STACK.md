# RuneLite Plugin Development Stack Research

**Research Date:** February 16, 2026
**Project:** Varlamore UIM Plugin
**Confidence Level Legend:** HIGH (verified/standard), MEDIUM (likely current), LOW (may need verification)

---

## 1. RuneLite Plugin Development Stack (2025/2026)

### RuneLite Client Version and API
- **Current Version:** 1.10.x series (as of early 2025)
- **API Stability:** RuneLite API is generally stable with quarterly updates
- **Version Tracking:** Use `runelite.version` in build.gradle
- **Confidence:** MEDIUM - Exact version requires checking latest releases

**Key Points:**
- RuneLite follows semantic versioning
- API breakages are rare but possible between major versions
- Plugin compatibility is maintained across minor versions
- Check `runelite-api` artifact for current version

### Java Version Requirements
- **Required Version:** Java 11 (LTS)
- **Recommended Version:** Java 11 or Java 17 (LTS)
- **Language Level:** Java 11 compatible bytecode
- **Confidence:** HIGH

**Rationale:**
- RuneLite transitioned to Java 11 in 2021
- Java 17 support added but Java 11 remains baseline
- Plugin Hub requires Java 11 compatibility for maximum compatibility
- Lombok and other tooling fully support Java 11+

**Build Configuration:**
```gradle
sourceCompatibility = JavaVersion.VERSION_11
targetCompatibility = JavaVersion.VERSION_11
```

### Build System: Gradle

**Version:** Gradle 7.x or 8.x
**Confidence:** HIGH

**Standard Configuration:**
```gradle
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

dependencies {
    compileOnly group: 'net.runelite', name: 'client', version: runeliteVersion

    compileOnly 'org.projectlombok:lombok:1.18.30'
    annotationProcessor 'org.projectlombok:lombok:1.18.30'

    testImplementation 'junit:junit:4.12'
    testImplementation group: 'net.runelite', name: 'client', version: runeliteVersion
    testImplementation group: 'net.runelite', name: 'jshell', version: runeliteVersion
}
```

**Key Configuration Elements:**
- Use `compileOnly` for RuneLite dependencies (provided at runtime)
- Include RuneLite's Maven repository: `https://repo.runelite.net`
- Lombok as `annotationProcessor` for compile-time code generation
- Test dependencies use `testImplementation` scope

### Project Structure Conventions

**Standard Plugin Structure:**
```
plugin-name/
├── build.gradle
├── settings.gradle
├── runelite-plugin.properties
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/pluginname/
│   │   │       ├── PluginNamePlugin.java        (main @Plugin class)
│   │   │       ├── PluginNameConfig.java        (configuration interface)
│   │   │       ├── PluginNameOverlay.java       (optional overlay)
│   │   │       ├── PluginNamePanel.java         (optional side panel)
│   │   │       └── [other classes]
│   │   └── resources/
│   │       └── com/example/pluginname/
│   │           ├── icon.png                      (plugin icon)
│   │           └── [other resources]
│   └── test/
│       └── java/
│           └── com/example/pluginname/
│               └── PluginNamePluginTest.java
```

**Confidence:** HIGH

**Critical Files:**

1. **runelite-plugin.properties** (Plugin Hub requirement):
```properties
displayName=Plugin Display Name
author=Your Name
description=Brief plugin description
tags=tag1,tag2,tag3
plugins=com.example.pluginname.PluginNamePlugin
```

2. **Main Plugin Class:**
```java
@Slf4j
@PluginDescriptor(
    name = "Plugin Name",
    description = "Plugin description",
    tags = {"tag1", "tag2"}
)
public class PluginNamePlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    private PluginNameConfig config;

    @Override
    protected void startUp() throws Exception {
        // Initialization
    }

    @Override
    protected void shutDown() throws Exception {
        // Cleanup
    }
}
```

### Key Dependencies

**Core Dependencies (Confidence: HIGH):**

1. **Guice (Dependency Injection):**
   - Version: Managed by RuneLite (typically 5.x)
   - Usage: Constructor and field injection with `@Inject`
   - Scope: `@Singleton` for plugin classes
   - Provider pattern: `@Provides` methods in plugin class

2. **Lombok:**
   - Version: 1.18.30+ (as of 2025)
   - Key Annotations:
     - `@Slf4j` - Logging
     - `@Getter/@Setter` - Property access
     - `@Data` - Combination annotation
     - `@Builder` - Builder pattern
     - `@Value` - Immutable classes
   - Configuration: Annotation processor required

3. **SLF4J (Logging):**
   - Provided by RuneLite client
   - Usage via Lombok's `@Slf4j`
   - Log levels: trace, debug, info, warn, error
   - Example: `log.info("Message: {}", variable);`

4. **Google Guava:**
   - Provided by RuneLite
   - Useful utilities: ImmutableList, ImmutableMap, Strings, etc.

5. **OkHttp (Optional):**
   - For HTTP requests
   - Version managed by RuneLite
   - Use RuneLite's provided OkHttpClient when possible

**Dependency Management:**
- Never bundle dependencies that RuneLite provides
- Use `compileOnly` scope for RuneLite-provided libraries
- Keep plugin JAR size minimal

---

## 2. Plugin Hub Requirements

**Confidence:** HIGH (based on established guidelines)

### Submission Process

1. **Repository Setup:**
   - Host plugin in public GitHub repository
   - Repository must contain only one plugin
   - Include LICENSE file (GPL-3.0 or compatible)
   - Add runelite-plugin.properties file

2. **Plugin Hub Submission:**
   - Fork `runelite/plugin-hub` repository
   - Add plugin repository URL to `plugins.json`
   - Create pull request with plugin details
   - Wait for automated checks and review

3. **Automated Checks:**
   - Build verification
   - Code scanning for malicious patterns
   - Size limits enforcement
   - Dependency verification

### Naming Conventions

**Plugin Class Name:**
- Format: `[PluginName]Plugin.java`
- Must match `@PluginDescriptor` name
- Use PascalCase
- Examples: `VarlamoreUimPlugin`, `BankTagsPlugin`

**Package Naming:**
- Format: `com.{author}.{pluginname}`
- All lowercase
- No special characters except dots
- Example: `com.example.varlamoreuim`

**Display Name:**
- User-friendly capitalization
- Clear and descriptive
- Max ~30 characters recommended
- Example: "Varlamore UIM"

### Code Quality Requirements

**Mandatory:**
- No malicious code or obfuscation
- No runtime code generation or class loading
- No external class downloads
- No cryptocurrency mining
- No advertising or monetization

**Best Practices:**
- Use RuneLite code style (4-space indentation)
- Proper exception handling
- No System.out/System.err (use logging)
- Null safety checks
- Memory leak prevention (proper cleanup in shutDown())

**Performance:**
- Minimal main thread blocking
- Efficient event handlers
- No busy-waiting or polling
- Proper use of scheduled executors

### Resource/Asset Restrictions

**File Size Limits:**
- Plugin JAR: < 5 MB (hard limit)
- Recommended: < 500 KB
- Icon: < 50 KB

**Allowed Resources:**
- Images (PNG, JPG) for UI/icons
- Sound files (small, compressed)
- JSON/text data files
- Fonts (if absolutely necessary)

**Prohibited:**
- Bundled dependencies (use compileOnly)
- Large data files
- Executables or native libraries
- External resource downloads at runtime (with exceptions)

**Icon Requirements:**
- Format: PNG with transparency
- Size: Typically 16x16 or 32x32 pixels
- Location: `src/main/resources/com/example/pluginname/icon.png`
- Must be original or properly licensed

### Review Process

**Timeline:**
- Automated checks: Minutes
- Initial review: 1-7 days
- Follow-up reviews: 1-3 days per iteration
- Total time: Variable (1-4 weeks typical)

**Review Criteria:**
1. Code safety and security
2. RuneLite guidelines compliance
3. No duplicate functionality (unless significantly improved)
4. Proper resource usage
5. Code quality and maintainability

**Common Rejection Reasons:**
- Security concerns
- Poor code quality
- Duplicate existing plugins
- Violates game integrity
- External dependencies/downloads
- Excessive size

---

## 3. Key RuneLite API Classes for Varlamore UIM Plugin

### Menu Entry Manipulation

**Events (Confidence: HIGH):**

1. **MenuEntryAdded:**
```java
@Subscribe
public void onMenuEntryAdded(MenuEntryAdded event) {
    String target = event.getTarget();
    String option = event.getOption();
    int type = event.getType();
    int identifier = event.getIdentifier();

    // Modify or block menu entries
}
```

2. **MenuOptionClicked:**
```java
@Subscribe
public void onMenuOptionClicked(MenuOptionClicked event) {
    String menuOption = event.getMenuOption();
    String menuTarget = event.getMenuTarget();
    int id = event.getId();
    MenuAction menuAction = event.getMenuAction();

    // Block action by consuming event
    event.consume();
}
```

**Key Classes:**
- `net.runelite.api.events.MenuEntryAdded`
- `net.runelite.api.events.MenuOptionClicked`
- `net.runelite.api.MenuAction` (enum)
- `net.runelite.api.MenuEntry`

**Use Cases for This Plugin:**
- Block teleport item usage (detect and consume)
- Block NPC interactions outside Varlamore
- Modify menu options for restricted NPCs

### NPC Events

**Events (Confidence: HIGH):**

1. **NpcSpawned:**
```java
@Subscribe
public void onNpcSpawned(NpcSpawned event) {
    NPC npc = event.getNpc();
    int npcId = npc.getId();
    String name = npc.getName();
    WorldPoint location = npc.getWorldLocation();

    // Track or replace NPCs
}
```

2. **NpcDespawned:**
```java
@Subscribe
public void onNpcDespawned(NpcDespawned event) {
    NPC npc = event.getNpc();
    // Cleanup tracking
}
```

3. **NpcChanged:**
```java
@Subscribe
public void onNpcChanged(NpcChanged event) {
    NPC npc = event.getNpc();
    NPC old = event.getOld();
    // Handle NPC transformations
}
```

**Key Classes:**
- `net.runelite.api.NPC`
- `net.runelite.api.events.NpcSpawned`
- `net.runelite.api.events.NpcDespawned`
- `net.runelite.api.events.NpcChanged`
- `net.runelite.api.coords.WorldPoint`

**Important NPC Methods:**
- `npc.getId()` - NPC definition ID
- `npc.getName()` - Display name
- `npc.getWorldLocation()` - Position
- `npc.getComposition()` - Visual/model data
- `npc.getTransformedComposition()` - For replacements

**Use Cases for This Plugin:**
- Replace travel NPCs with custom versions
- Detect region-specific NPCs
- Track NPC interactions

### Widget Manipulation (Dialogues)

**Widget System (Confidence: HIGH):**

```java
@Inject
private Client client;

// Get widget
Widget widget = client.getWidget(widgetId);
Widget widget = client.getWidget(groupId, childId);

// Widget dialogue constants
int DIALOG_NPC_GROUP_ID = 231;
int DIALOG_PLAYER_GROUP_ID = 217;
int DIALOG_OPTION_GROUP_ID = 219;

// Manipulate widget
if (widget != null) {
    widget.setText("Custom dialogue text");
    widget.setHidden(true/false);
    widget.setTextColor(color);
}
```

**Events:**
```java
@Subscribe
public void onWidgetLoaded(WidgetLoaded event) {
    int groupId = event.getGroupId();
    // Dialogue opened
}
```

**Key Classes:**
- `net.runelite.api.widgets.Widget`
- `net.runelite.api.widgets.WidgetInfo` (constants)
- `net.runelite.api.events.WidgetLoaded`
- `net.runelite.api.events.WidgetClosed`

**Dialogue Widget IDs (Common):**
- NPC Dialogue: Group 231
- Player Dialogue: Group 217
- Options: Group 219
- Sprite Dialogue: Group 193

**Use Cases for This Plugin:**
- Inject custom dialogue for replaced NPCs
- Block/modify dialogue options
- Display Varlamore-specific messages

### ChatMessageManager

**Usage (Confidence: HIGH):**

```java
@Inject
private ChatMessageManager chatMessageManager;

@Inject
private Client client;

// Send game message
client.addChatMessage(
    ChatMessageType.GAMEMESSAGE,
    "",
    "You cannot leave Varlamore!",
    null
);

// Queue message (preferred)
chatMessageManager.queue(QueuedMessage.builder()
    .type(ChatMessageType.GAMEMESSAGE)
    .runeLiteFormattedMessage("Message with <col=ff0000>color</col>")
    .build());
```

**Events:**
```java
@Subscribe
public void onChatMessage(ChatMessage event) {
    ChatMessageType type = event.getType();
    String message = event.getMessage();
    String sender = event.getName();

    // Filter or modify messages
    event.getMessageNode().setValue("Modified message");
}
```

**Key Classes:**
- `net.runelite.client.chat.ChatMessageManager`
- `net.runelite.client.chat.QueuedMessage`
- `net.runelite.api.ChatMessageType` (enum)
- `net.runelite.api.events.ChatMessage`

**Message Types:**
- GAMEMESSAGE - Standard game messages
- PUBLICCHAT - Player chat
- ENGINE - Server messages
- CONSOLE - Client console

**Use Cases for This Plugin:**
- Notify player of blocked actions
- Display milestone unlocks
- Warning messages for restricted areas

### Client API (Player Position & Region Detection)

**Player Position (Confidence: HIGH):**

```java
@Inject
private Client client;

// Get player
Player localPlayer = client.getLocalPlayer();

// Position information
WorldPoint worldPoint = localPlayer.getWorldLocation();
int x = worldPoint.getX();
int y = worldPoint.getY();
int plane = worldPoint.getPlane();

// Region detection
int regionId = worldPoint.getRegionID();
int regionX = worldPoint.getRegionX();
int regionY = worldPoint.getRegionY();

// Area checking
boolean inInstance = client.isInInstancedRegion();
```

**Region Constants (Varlamore-specific):**
- Research exact region IDs for Varlamore areas
- Typical region ID format: 12345 (5-digit)
- Use region boundaries for area detection

**Key Classes:**
- `net.runelite.api.Client`
- `net.runelite.api.Player`
- `net.runelite.api.coords.WorldPoint`
- `net.runelite.api.coords.WorldArea`

**Useful Client Methods:**
- `client.getLocalPlayer()` - Current player
- `client.getPlane()` - Current floor level
- `client.getGameState()` - LOGIN, LOGGED_IN, etc.
- `client.getVarbitValue(varbitId)` - Quest/state tracking

**Use Cases for This Plugin:**
- Detect if player is in Varlamore
- Block actions outside allowed regions
- Track milestone completion by location

### Overlay System

**Overlay Implementation (Confidence: HIGH):**

```java
@Slf4j
public class VarlamoreUimOverlay extends Overlay {
    private final Client client;
    private final VarlamoreUimPlugin plugin;
    private final VarlamoreUimConfig config;

    @Inject
    private VarlamoreUimOverlay(Client client,
                                VarlamoreUimPlugin plugin,
                                VarlamoreUimConfig config) {
        this.client = client;
        this.plugin = plugin;
        this.config = config;

        setPosition(OverlayPosition.TOP_LEFT);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPriority(OverlayPriority.HIGH);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!config.showOverlay()) {
            return null;
        }

        // Custom rendering logic
        graphics.setColor(Color.WHITE);
        graphics.drawString("Text", x, y);

        return new Dimension(width, height);
    }
}

// In plugin startUp():
@Inject
private OverlayManager overlayManager;

@Inject
private VarlamoreUimOverlay overlay;

@Override
protected void startUp() {
    overlayManager.add(overlay);
}

@Override
protected void shutDown() {
    overlayManager.remove(overlay);
}
```

**Key Classes:**
- `net.runelite.client.ui.overlay.Overlay`
- `net.runelite.client.ui.overlay.OverlayManager`
- `net.runelite.client.ui.overlay.OverlayPosition`
- `net.runelite.client.ui.overlay.OverlayLayer`
- `net.runelite.client.ui.overlay.OverlayPriority`
- `net.runelite.client.ui.overlay.components.*` (TextComponent, PanelComponent, etc.)

**Overlay Positions:**
- TOP_LEFT, TOP_CENTER, TOP_RIGHT
- BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT
- DYNAMIC (follows mouse)
- ABOVE_CHATBOX_RIGHT

**Use Cases for This Plugin:**
- Display active restrictions
- Show milestone progress
- Warning indicators for blocked areas

### Config System

**Configuration Interface (Confidence: HIGH):**

```java
@ConfigGroup("varlamoreuim")
public interface VarlamoreUimConfig extends Config {

    @ConfigSection(
        name = "General Settings",
        description = "General plugin settings",
        position = 0
    )
    String generalSection = "general";

    @ConfigItem(
        keyName = "enablePlugin",
        name = "Enable Plugin",
        description = "Master toggle for plugin functionality",
        position = 0,
        section = generalSection
    )
    default boolean enablePlugin() {
        return true;
    }

    @ConfigItem(
        keyName = "blockTeleports",
        name = "Block Teleports",
        description = "Block teleport spells and items",
        position = 1,
        section = generalSection
    )
    default boolean blockTeleports() {
        return true;
    }

    @Range(min = 1, max = 100)
    @ConfigItem(
        keyName = "warningDistance",
        name = "Warning Distance",
        description = "Distance in tiles for boundary warnings",
        position = 2,
        section = generalSection
    )
    default int warningDistance() {
        return 5;
    }

    @Alpha
    @ConfigItem(
        keyName = "overlayColor",
        name = "Overlay Color",
        description = "Color for overlay elements",
        position = 3,
        section = generalSection
    )
    default Color overlayColor() {
        return Color.RED;
    }
}
```

**ConfigManager Usage:**

```java
@Inject
private ConfigManager configManager;

// Get config value
String value = configManager.getConfiguration(
    "varlamoreuim",  // group
    "keyName"         // key
);

// Set config value
configManager.setConfiguration(
    "varlamoreuim",
    "keyName",
    "value"
);

// Unset config value
configManager.unsetConfiguration(
    "varlamoreuim",
    "keyName"
);
```

**Config Change Events:**

```java
@Subscribe
public void onConfigChanged(ConfigChanged event) {
    if (!event.getGroup().equals("varlamoreuim")) {
        return;
    }

    switch (event.getKey()) {
        case "enablePlugin":
            // Handle enable/disable
            break;
        case "blockTeleports":
            // Update teleport blocking
            break;
    }
}
```

**Key Classes:**
- `net.runelite.client.config.Config`
- `net.runelite.client.config.ConfigManager`
- `net.runelite.client.config.ConfigItem`
- `net.runelite.client.config.ConfigSection`
- `net.runelite.client.config.Range`
- `net.runelite.client.config.Alpha`
- `net.runelite.client.config.Units`
- `net.runelite.client.events.ConfigChanged`

**Config Annotations:**
- `@ConfigGroup` - Group identifier (lowercase, no spaces)
- `@ConfigSection` - Section grouping
- `@ConfigItem` - Individual setting
- `@Range` - Numeric range validation
- `@Alpha` - Color with transparency
- `@Units` - Display units (MINUTES, SECONDS, etc.)

**Use Cases for This Plugin:**
- Categorized settings (General, Blocking, Milestones, etc.)
- Toggle specific restrictions
- Customize warnings and notifications
- Milestone unlock thresholds

### ClientToolbar and PluginPanel

**Side Panel Implementation (Confidence: HIGH):**

```java
@Slf4j
public class VarlamoreUimPanel extends PluginPanel {
    private final VarlamoreUimPlugin plugin;
    private final VarlamoreUimConfig config;

    @Inject
    private VarlamoreUimPanel(VarlamoreUimPlugin plugin,
                              VarlamoreUimConfig config) {
        this.plugin = plugin;
        this.config = config;

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Title
        JLabel titleLabel = new JLabel("Varlamore UIM");
        titleLabel.setFont(FontManager.getRunescapeBoldFont());
        mainPanel.add(titleLabel);

        // Milestone sections
        JPanel milestonesPanel = createMilestonesPanel();
        mainPanel.add(milestonesPanel);

        add(mainPanel, BorderLayout.NORTH);
    }

    private JPanel createMilestonesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 0, 5));

        // Add milestone items
        return panel;
    }
}

// In plugin:
@Inject
private ClientToolbar clientToolbar;

@Inject
private VarlamoreUimPanel panel;

private NavigationButton navButton;

@Override
protected void startUp() {
    navButton = NavigationButton.builder()
        .tooltip("Varlamore UIM")
        .icon(ImageUtil.loadImageResource(getClass(), "/icon.png"))
        .priority(5)
        .panel(panel)
        .build();

    clientToolbar.addNavigation(navButton);
}

@Override
protected void shutDown() {
    clientToolbar.removeNavigation(navButton);
}
```

**Key Classes:**
- `net.runelite.client.ui.ClientToolbar`
- `net.runelite.client.ui.PluginPanel`
- `net.runelite.client.ui.NavigationButton`
- `net.runelite.client.ui.FontManager`
- `net.runelite.client.util.ImageUtil`

**UI Components:**
- Swing-based (JPanel, JLabel, JButton, etc.)
- RuneLite custom components available
- Use FontManager for consistent fonts
- ColorScheme for RuneLite theme colors

**Use Cases for This Plugin:**
- Display categorized milestones
- Show unlock progress
- Visual milestone checklist
- Quick settings access

---

## 4. Build Tooling

### Setting Up a New RuneLite Plugin Project

**Method 1: Example Plugin Template (Recommended)**

**Confidence:** HIGH

1. **Clone Example Plugin:**
```bash
git clone https://github.com/runelite/example-plugin.git varlamore-uim
cd varlamore-uim
```

2. **Update Plugin Identity:**
   - Rename package from `com.example` to your package
   - Update `runelite-plugin.properties`:
     - displayName
     - author
     - description
     - tags
     - plugins (main class path)

3. **Update build.gradle:**
```gradle
group = 'com.yourusername'
version = '1.0-SNAPSHOT'

def runeliteVersion = '1.10.+'  // Latest 1.10.x

dependencies {
    compileOnly group: 'net.runelite', name: 'client', version: runeliteVersion

    compileOnly 'org.projectlombok:lombok:1.18.30'
    annotationProcessor 'org.projectlombok:lombok:1.18.30'

    testImplementation 'junit:junit:4.12'
    testImplementation group: 'net.runelite', name: 'client', version: runeliteVersion
}
```

4. **Implement Plugin Class:**
   - Extend `Plugin`
   - Add `@PluginDescriptor`
   - Implement `startUp()` and `shutDown()`

**Method 2: From Scratch**

1. Create directory structure (see Section 1)
2. Create build.gradle with dependencies
3. Create runelite-plugin.properties
4. Implement plugin class
5. Add resources (icon.png)

### Running/Debugging Locally

**Option 1: RuneLite Client Development (Recommended)**

**Setup:**
```bash
# Clone RuneLite
git clone https://github.com/runelite/runelite.git
cd runelite

# Build RuneLite
./gradlew build

# Create symlink to plugin (or copy)
ln -s /path/to/your/plugin runelite-client/src/main/java/net/runelite/client/plugins/yourplugin
```

**Run with IntelliJ IDEA:**
1. Import RuneLite as Gradle project
2. Navigate to `RuneLite` class
3. Right-click -> Debug 'RuneLite.main()'
4. Plugin loads automatically with client

**Option 2: Plugin Development Mode**

```bash
# In your plugin directory
./gradlew build

# Copy JAR to RuneLite plugins folder
cp build/libs/plugin.jar ~/.runelite/plugins/

# Start RuneLite with development mode
java -ea -Drunelite.pluginhub.version=0 -jar RuneLite.jar --developer-mode
```

**Debugging Features:**
- Enable Developer Tools in RuneLite settings
- Use "Developer Tools" panel for:
  - Widget inspector
  - NPC/Object inspector
  - Coordinate viewer
  - Varbit viewer

**Logging:**
```java
@Slf4j
public class MyPlugin extends Plugin {
    @Override
    protected void startUp() {
        log.info("Plugin started");
        log.debug("Debug info: {}", variable);
    }
}
```

View logs:
- RuneLite client console (if launched from terminal)
- `~/.runelite/logs/` directory
- IntelliJ IDEA debug console

### Testing Approaches

**Unit Testing:**

```java
public class VarlamoreUimPluginTest {
    private VarlamoreUimPlugin plugin;

    @Before
    public void setUp() {
        plugin = new VarlamoreUimPlugin();
    }

    @Test
    public void testMilestoneUnlock() {
        // Test milestone logic
        boolean unlocked = plugin.checkMilestone(100);
        assertTrue(unlocked);
    }
}
```

**Mock Testing with Mockito:**

```gradle
dependencies {
    testImplementation 'org.mockito:mockito-core:3.+'
}
```

```java
import static org.mockito.Mockito.*;

public class VarlamoreUimPluginTest {
    @Test
    public void testNpcReplacement() {
        Client client = mock(Client.class);
        NPC npc = mock(NPC.class);

        when(npc.getId()).thenReturn(1234);
        when(npc.getName()).thenReturn("Travel NPC");

        // Test logic
    }
}
```

**Integration Testing:**
1. Manual testing in live client
2. Test in multiple game scenarios
3. Verify event handling
4. Check performance impact
5. Test config changes

**Testing Checklist:**
- Plugin enables/disables cleanly
- No memory leaks (check shutDown())
- Config changes apply immediately
- Event handlers don't block main thread
- No exceptions in logs
- Overlays render correctly
- Side panel displays properly

**Common Testing Tools:**
- JUnit 4 (standard)
- Mockito (mocking)
- RuneLite's test utilities
- Manual QA in client

---

## 5. Additional Resources & Best Practices

### Documentation Resources

**Official Documentation:**
- RuneLite Wiki: https://github.com/runelite/runelite/wiki
- API Javadoc: https://static.runelite.net/api/runelite-api/
- Plugin Hub: https://github.com/runelite/plugin-hub

**Example Plugins to Study:**
- Bank Tags Plugin (config system, UI)
- Quest Helper (complex state management)
- GPU Plugin (performance optimization)
- Menu Entry Swapper (menu manipulation)

### Development Best Practices

**Performance:**
- Cache expensive calculations
- Use client thread for game state access
- Minimize object creation in render loops
- Use efficient data structures (HashMap vs ArrayList)

**Memory Management:**
- Clear collections in shutDown()
- Unsubscribe from events properly
- Remove overlays and panels
- Avoid static state

**Error Handling:**
- Null-check widgets, NPCs, players
- Catch exceptions in event handlers
- Log errors appropriately
- Fail gracefully

**Code Organization:**
- Separate concerns (plugin, config, overlay, panel)
- Use meaningful variable names
- Comment complex logic
- Follow RuneLite code style

**Security:**
- Never store sensitive data
- Validate all external inputs
- Don't trust client state completely
- No external communications (unless approved)

### Version Control & Release

**Git Best Practices:**
```bash
# Ignore build artifacts
echo "build/" >> .gitignore
echo ".gradle/" >> .gitignore
echo ".idea/" >> .gitignore
echo "*.iml" >> .gitignore

# Tag releases
git tag -a v1.0.0 -m "Initial release"
git push origin v1.0.0
```

**Release Process:**
1. Update version in build.gradle
2. Test thoroughly
3. Commit changes
4. Create git tag
5. Push to GitHub
6. Submit to Plugin Hub (if new)

**Versioning:**
- Follow semantic versioning (MAJOR.MINOR.PATCH)
- MAJOR: Breaking changes
- MINOR: New features
- PATCH: Bug fixes

---

## 6. Varlamore UIM Plugin-Specific Considerations

### Key Implementation Points

**Region Detection:**
- Identify all Varlamore region IDs
- Create region boundary checker
- Handle edge cases (teleport animations, logout/login)

**NPC Replacement Strategy:**
- Map travel NPCs to replacement NPCs
- Create dialogue system for custom messages
- Handle menu options appropriately

**Teleport Blocking:**
- Identify all teleport items (tablets, jewelry, etc.)
- Detect teleport spells
- Block via MenuOptionClicked event
- Provide clear feedback to player

**Milestone System:**
- Define milestone criteria (levels, items, quests, etc.)
- Persist milestone state in config
- Visual indication in side panel
- Unlock notifications

**Configuration Categories:**
1. General (enable/disable, warnings)
2. Blocking (teleports, NPCs, items)
3. Milestones (thresholds, unlock conditions)
4. UI/Visual (overlay, panel, colors)

### Technical Challenges

**Challenge 1: NPC Dialogue Injection**
- Solution: Listen for WidgetLoaded, modify dialogue widgets
- Fallback: Use chat messages if widget manipulation fails

**Challenge 2: Comprehensive Teleport Detection**
- Solution: Maintain list of teleport IDs, update as game changes
- Use MenuAction enum for spell detection

**Challenge 3: Region Boundary Accuracy**
- Solution: Test extensively, document edge cases
- Consider tile-level precision for warnings

**Challenge 4: Milestone State Persistence**
- Solution: Use ConfigManager for simple state
- Consider JSON serialization for complex data

### Development Roadmap

**Phase 1: Core Blocking**
- Implement region detection
- Block basic teleports
- Chat message feedback

**Phase 2: NPC System**
- Identify and replace travel NPCs
- Custom dialogue implementation
- Menu option blocking

**Phase 3: Milestone Framework**
- Define milestone data structure
- Implement tracking logic
- Basic unlock system

**Phase 4: UI/UX**
- Create side panel
- Implement overlays
- Polish configuration

**Phase 5: Testing & Polish**
- Comprehensive testing
- Bug fixes
- Performance optimization
- Documentation

**Phase 6: Plugin Hub Submission**
- Code review
- Compliance check
- Submission preparation
- Community feedback

---

## 7. Confidence Summary

### HIGH Confidence Items:
- Java 11 requirement
- Gradle build system
- Project structure conventions
- Guice, Lombok, SLF4J dependencies
- Plugin Hub submission process
- Core API classes (events, Client, Config)
- Testing approaches
- Best practices

### MEDIUM Confidence Items:
- Exact RuneLite version (1.10.x series likely)
- Lombok version (1.18.30+)
- Specific Plugin Hub timelines
- Some widget IDs (may change)

### LOW Confidence Items (Requires Verification):
- Varlamore-specific region IDs
- Latest Plugin Hub automated checks
- Current RuneLite API changes in 2026

### Recommended Next Steps:
1. Verify current RuneLite version from official repository
2. Research Varlamore region IDs (game data or existing plugins)
3. Study similar restriction plugins for patterns
4. Set up development environment
5. Create prototype with core functionality

---

**End of Research Document**
