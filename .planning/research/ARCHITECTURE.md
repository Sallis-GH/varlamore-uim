# Architecture Research: Varlamore-locked UIM RuneLite Plugin

**Research Date:** 2026-02-16
**Research Type:** Project Research - Architecture Dimension
**Milestone:** Greenfield - Understanding RuneLite plugin structure for area-locked account plugin

---

## Executive Summary

This document outlines the architectural patterns for building a RuneLite plugin that enforces Varlamore-locked UIM restrictions. The architecture follows RuneLite's event-driven plugin model with clear separation between boundary management, restriction enforcement, UI presentation, and state persistence.

**Key Architectural Decisions:**
- Event-driven architecture subscribing to RuneLite game events
- Menu entry interception for action blocking
- NPC manipulation through rendering hooks and actor management
- Modular restriction system with category-based organization
- Config-driven settings with side panel UI
- Milestone-based unlock state management

---

## 1. RuneLite Plugin Architecture Patterns

### 1.1 Plugin Lifecycle

RuneLite plugins follow a well-defined lifecycle managed by the plugin manager:

**Lifecycle Phases:**
```
Load → Start → Configure → Shutdown
```

**Key Methods:**
- `@Provides` - Dependency injection for config
- `startUp()` - Initialize state, register overlays, subscribe to events
- `shutDown()` - Cleanup, unregister overlays, clear state
- `@Subscribe` methods - Event handlers that respond to game events

**Implementation Pattern:**
```java
@PluginDescriptor(name = "Varlamore UIM", description = "...")
public class VarlamoreUimPlugin extends Plugin {
    @Inject private Client client;
    @Inject private VarlamoreUimConfig config;
    @Inject private OverlayManager overlayManager;
    @Inject private ChatMessageManager chatMessageManager;

    @Provides
    VarlamoreUimConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(VarlamoreUimConfig.class);
    }

    @Override
    protected void startUp() {
        // Initialize components
        // Register overlays
        // Subscribe to events (automatic via @Subscribe)
    }

    @Override
    protected void shutDown() {
        // Cleanup state
        // Unregister overlays
    }
}
```

### 1.2 Event-Driven Architecture

**Critical Events for Area-Locked Plugin:**

| Event | Purpose | Usage in Plugin |
|-------|---------|-----------------|
| `GameStateChanged` | Track login/logout, world changes | Initialize player state, reset restrictions |
| `MenuEntryAdded` | Intercept menu options | Block travel spells/items, modify NPC menus |
| `MenuOptionClicked` | Validate action execution | Final validation before action executes |
| `ChatMessage` | Game feedback | Provide restriction messages, unlock notifications |
| `NpcSpawned` / `NpcDespawned` | NPC lifecycle | Track NPCs for replacement |
| `ConfigChanged` | Settings updates | Propagate config changes to active restrictions |
| `GameTick` | Regular game loop | Periodic checks, delayed actions |
| `WidgetLoaded` | Interface detection | Detect teleport interfaces, travel dialogues |

**Event Flow Pattern:**
```
Game Action → Event Fired → Plugin @Subscribe Method →
    Restriction Check → Allow/Block/Modify → Game Continues
```

### 1.3 Action Interception

**Menu Entry Manipulation:**

RuneLite provides menu entry hooks to intercept and modify player actions:

```java
@Subscribe
public void onMenuEntryAdded(MenuEntryAdded event) {
    String option = event.getOption();
    String target = event.getTarget();

    // Example: Block teleport spells
    if (option.equals("Cast") && isTeleportSpell(target)) {
        if (!travelRestrictions.isAllowed(target)) {
            // Modify menu to show blocked message
            event.setModified();
        }
    }
}

@Subscribe
public void onMenuOptionClicked(MenuOptionClicked event) {
    // Final validation and blocking
    if (shouldBlock(event)) {
        event.consume(); // Prevents action
        showBlockedMessage();
    }
}
```

**Widget Manipulation:**
- Use `WidgetLoaded` event to detect travel interfaces
- Modify widget text/visibility for blocked actions
- Hide/show interface elements based on unlock state

### 1.4 NPC Manipulation

**Hiding NPCs:**
```java
@Subscribe
public void onNpcSpawned(NpcSpawned event) {
    NPC npc = event.getNpc();
    if (shouldHideNpc(npc)) {
        // Mark NPC as hidden in rendering
        hiddenNpcs.add(npc);
    }
}

// In overlay or rendering hook
if (hiddenNpcs.contains(npc)) {
    return; // Skip rendering
}
```

**Custom NPC Rendering:**
- Use `Overlay` to render custom NPC sprites at NPC positions
- Track original NPC positions via `NPC.getWorldLocation()`
- Render custom sprite/model with same positioning

**Custom Dialogue:**
- Intercept `WidgetLoaded` for dialogue widgets
- Replace dialogue text with custom messages
- Create custom dialogue trees via widget manipulation
- Option: Use `ChatMessageManager` to inject fake dialogue

**Pattern:**
```java
@Subscribe
public void onWidgetLoaded(WidgetLoaded event) {
    if (event.getGroupId() == DIALOG_WIDGET_GROUP) {
        Widget dialogueWidget = client.getWidget(...);
        if (isReplacementNpcDialogue()) {
            modifyDialogueText(dialogueWidget, customDialogue);
        }
    }
}
```

### 1.5 Side Panel Architecture

**Panel Creation:**
```java
@Inject
private ClientToolbar clientToolbar;

private NavigationButton navButton;
private VarlamoreUimPanel panel;

@Override
protected void startUp() {
    panel = injector.getInstance(VarlamoreUimPanel.class);

    navButton = NavigationButton.builder()
        .tooltip("Varlamore UIM")
        .icon(icon)
        .priority(5)
        .panel(panel)
        .build();

    clientToolbar.addNavigation(navButton);
}
```

**Panel Structure (Categorized Settings):**
```java
public class VarlamoreUimPanel extends PluginPanel {
    private JTabbedPane tabbedPane;

    public VarlamoreUimPanel() {
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Restrictions", new RestrictionsPanel());
        tabbedPane.addTab("QoA", new QoAPanel());
        tabbedPane.addTab("Unlocks", new UnlocksPanel());
        tabbedPane.addTab("Settings", new SettingsPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }
}
```

**Category Panels:**
- Each category extends `JPanel`
- Use `PluginPanel` styling utilities
- Bind to config values via listeners
- Update in real-time on config changes

### 1.6 Config System Patterns

**Config Definition:**
```java
@ConfigGroup("varlamoreuim")
public interface VarlamoreUimConfig extends Config {
    @ConfigSection(
        name = "Travel Restrictions",
        description = "Configure travel blocking",
        position = 0
    )
    String travelSection = "travel";

    @ConfigItem(
        keyName = "blockTeleports",
        name = "Block Teleports",
        description = "Prevent teleporting outside Varlamore",
        section = travelSection
    )
    default boolean blockTeleports() {
        return true;
    }

    @ConfigItem(
        keyName = "allowedTeleports",
        name = "Allowed Teleports",
        description = "Comma-separated list of allowed teleport spells",
        section = travelSection
    )
    default String allowedTeleports() {
        return "";
    }
}
```

**Config Change Handling:**
```java
@Subscribe
public void onConfigChanged(ConfigChanged event) {
    if (!event.getGroup().equals("varlamoreuim")) {
        return;
    }

    switch (event.getKey()) {
        case "blockTeleports":
            travelRestrictions.updateTeleportBlocking(config.blockTeleports());
            break;
        case "allowedTeleports":
            travelRestrictions.updateAllowedList(config.allowedTeleports());
            break;
    }
}
```

---

## 2. Component Design for Varlamore UIM Plugin

### 2.1 Component Overview

```
┌─────────────────────────────────────────────────────────────┐
│                    VarlamoreUimPlugin                        │
│                  (Main Plugin Coordinator)                   │
└──────────────────────┬──────────────────────────────────────┘
                       │
         ┌─────────────┼─────────────┬──────────────┬─────────┐
         │             │             │              │         │
         ▼             ▼             ▼              ▼         ▼
  ┌───────────┐ ┌───────────┐ ┌──────────┐  ┌─────────┐ ┌─────────┐
  │ Boundary  │ │  Travel   │ │   NPC    │  │   UI    │ │ Unlock  │
  │  System   │ │Restriction│ │Replacement│  │ System  │ │ System  │
  └───────────┘ └───────────┘ └──────────┘  └─────────┘ └─────────┘
       │             │              │             │          │
       │             │              │             │          │
       ▼             ▼              ▼             ▼          ▼
  ┌───────────────────────────────────────────────────────────┐
  │              RuneLite API Layer                           │
  │  (Client, Events, Overlays, Config, ChatMessageManager)   │
  └───────────────────────────────────────────────────────────┘
```

### 2.2 Boundary System

**Responsibility:** Determine if player/location is within allowed region

**Key Classes:**
```
BoundaryManager
├── ChunkIdStore (static data: allowed chunk IDs)
├── RegionDetector (runtime: current player region)
└── BoundaryValidator (validation logic)
```

**Interface:**
```java
public class BoundaryManager {
    private final Set<Integer> allowedChunkIds;
    private final Client client;

    public boolean isInBounds(WorldPoint location) {
        int chunkId = getChunkId(location);
        return allowedChunkIds.contains(chunkId);
    }

    public boolean isDestinationAllowed(WorldPoint destination) {
        return isInBounds(destination);
    }

    private int getChunkId(WorldPoint point) {
        // Chunk ID calculation from world coordinates
        return point.getRegionID(); // or custom calculation
    }
}
```

**Data Storage:**
- Static chunk ID list (loaded from resource file or hardcoded)
- Format: JSON or CSV of chunk IDs defining Varlamore region
- Example: `allowed_chunks.json` in resources

**Integration Points:**
- Used by TravelRestrictionEngine for validation
- Provides boundary overlay visualization
- Consulted on every travel attempt

### 2.3 Travel Restriction Engine

**Responsibility:** Intercept and block travel actions based on type and unlock state

**Key Classes:**
```
TravelRestrictionEngine
├── SpellRestrictionHandler (teleport spells)
├── ItemRestrictionHandler (teleport items, Xerician fabric)
├── NpcTravelHandler (boats, magic carpets, NPCs)
├── TransportationRegistry (mapping of travel methods)
└── UnlockValidator (check if travel method unlocked)
```

**Architecture Pattern:**
```java
public class TravelRestrictionEngine {
    private final BoundaryManager boundaryManager;
    private final UnlockManager unlockManager;
    private final List<RestrictionHandler> handlers;

    public RestrictionResult checkTravel(TravelAttempt attempt) {
        // 1. Check unlock state
        if (!unlockManager.isUnlocked(attempt.getTravelMethod())) {
            return RestrictionResult.blocked("Not unlocked yet");
        }

        // 2. Check destination
        if (!boundaryManager.isDestinationAllowed(attempt.getDestination())) {
            return RestrictionResult.blocked("Outside Varlamore");
        }

        // 3. Handler-specific validation
        RestrictionHandler handler = getHandler(attempt.getType());
        return handler.validate(attempt);
    }
}

public interface RestrictionHandler {
    boolean canHandle(TravelType type);
    RestrictionResult validate(TravelAttempt attempt);
}
```

**Event Integration:**
```java
// In main plugin
@Subscribe
public void onMenuOptionClicked(MenuOptionClicked event) {
    TravelAttempt attempt = TravelAttempt.fromMenuClick(event);

    if (attempt != null) {
        RestrictionResult result = travelEngine.checkTravel(attempt);

        if (result.isBlocked()) {
            event.consume();
            chatMessageManager.queue(QueuedMessage.builder()
                .type(ChatMessageType.GAMEMESSAGE)
                .runeLiteFormattedMessage(result.getMessage())
                .build());
        }
    }
}
```

**Travel Types:**
- SPELL_TELEPORT (Cast teleport spell)
- ITEM_TELEPORT (Use teleport item)
- NPC_DIALOGUE (Talk to travel NPC)
- PORTAL (Use portal)
- LADDER (Climb ladder - for boundary edges)
- BOAT (Use boat)
- MAGIC_CARPET (Use carpet)

### 2.4 NPC Replacement System

**Responsibility:** Hide real travel NPCs and render custom replacements with blocking dialogue

**Key Classes:**
```
NpcReplacementSystem
├── NpcHidingManager (tracks hidden NPCs)
├── ReplacementNpcRenderer (custom overlay rendering)
├── CustomDialogueManager (dialogue injection)
└── NpcReplacementRegistry (mapping: NPC ID → Replacement)
```

**Architecture:**
```java
public class NpcReplacementSystem {
    private final Set<NPC> hiddenNpcs = new HashSet<>();
    private final Map<Integer, ReplacementNpcData> replacements;
    private final OverlayManager overlayManager;

    @Subscribe
    public void onNpcSpawned(NpcSpawned event) {
        NPC npc = event.getNpc();

        if (replacements.containsKey(npc.getId())) {
            hiddenNpcs.add(npc);
            // NPC is hidden via rendering overlay
        }
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned event) {
        hiddenNpcs.remove(event.getNpc());
    }

    public boolean isHidden(NPC npc) {
        return hiddenNpcs.contains(npc);
    }
}

public class ReplacementNpcOverlay extends Overlay {
    @Override
    public Dimension render(Graphics2D graphics) {
        for (NPC npc : npcReplacementSystem.getHiddenNpcs()) {
            ReplacementNpcData replacement = registry.getReplacement(npc.getId());

            // Render custom sprite at NPC location
            Point screenLoc = npc.getCanvasTextLocation(...);
            renderCustomNpc(graphics, replacement, screenLoc);
        }
        return null;
    }
}
```

**Dialogue System:**
```java
public class CustomDialogueManager {
    private final Map<Integer, DialogueTree> customDialogues;

    @Subscribe
    public void onWidgetLoaded(WidgetLoaded event) {
        if (event.getGroupId() == DIALOG_NPC_GROUP) {
            Widget npcDialog = client.getWidget(DIALOG_NPC_TEXT);

            // Check if this is a replacement NPC
            int npcId = getCurrentDialogueNpc();
            if (customDialogues.containsKey(npcId)) {
                injectCustomDialogue(npcDialog, npcId);
            }
        }
    }

    private void injectCustomDialogue(Widget widget, int npcId) {
        DialogueTree tree = customDialogues.get(npcId);
        widget.setText(tree.getCurrentText());
        // Remove travel options, add flavor text
    }
}
```

**Data Structure:**
```java
public class ReplacementNpcData {
    private final int originalNpcId;
    private final String customName;
    private final String spriteResourcePath; // Optional custom sprite
    private final DialogueTree dialogue;

    public static class DialogueTree {
        private List<DialogueNode> nodes;

        public static class DialogueNode {
            private String text;
            private List<String> options;
            private Map<String, Integer> nextNodes; // option → node index
        }
    }
}
```

### 2.5 Settings/Config Panel Architecture

**Extensible Category System:**

```
VarlamoreUimPanel (JTabbedPane)
├── RestrictionsPanel
│   ├── TravelRestrictionsSection
│   ├── SkillRestrictionsSection (future)
│   └── ItemRestrictionsSection (future)
├── QoAPanelPanel
│   ├── InventoryTagsSection
│   ├── BankOrganizationSection
│   └── NotificationsSection
├── UnlocksPanel
│   ├── MilestoneListSection
│   ├── CurrentProgressSection
│   └── UnlockHistorySection
└── SettingsPanel
    ├── BoundaryVisualizationSection
    └── DebugSection
```

**Panel Builder Pattern:**
```java
public abstract class CategoryPanel extends JPanel {
    protected final VarlamoreUimConfig config;
    protected final VarlamoreUimPlugin plugin;

    public CategoryPanel(VarlamoreUimConfig config, VarlamoreUimPlugin plugin) {
        this.config = config;
        this.plugin = plugin;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        buildPanel();
    }

    protected abstract void buildPanel();

    protected void addSection(String title, JPanel content) {
        add(createTitleLabel(title));
        add(content);
        add(Box.createVerticalStrut(10));
    }
}

public class RestrictionsPanel extends CategoryPanel {
    @Override
    protected void buildPanel() {
        addSection("Travel Restrictions", buildTravelSection());
        addSection("Future Restrictions", buildPlaceholderSection());
    }

    private JPanel buildTravelSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));

        // Bind to config
        JCheckBox blockTeleports = new JCheckBox("Block All Teleports",
            config.blockTeleports());
        blockTeleports.addActionListener(e ->
            configManager.setConfiguration("varlamoreuim", "blockTeleports",
                blockTeleports.isSelected()));

        panel.add(blockTeleports);
        return panel;
    }
}
```

**Extensibility:**
- Each category panel is independent
- New categories added via `tabbedPane.addTab()`
- Sections within panels use builder pattern
- Config changes propagate via listeners

### 2.6 Unlock System

**Responsibility:** Track milestone-based unlock state and manage progressive travel unlocking

**Key Classes:**
```
UnlockManager
├── MilestoneTracker (monitors achievement conditions)
├── UnlockStateStore (persists unlock state)
├── UnlockValidator (checks if content unlocked)
└── UnlockNotifier (UI notifications)
```

**Architecture:**
```java
public class UnlockManager {
    private final Map<String, Milestone> milestones;
    private final Set<String> unlockedMilestones;
    private final ConfigManager configManager;

    public boolean isUnlocked(String travelMethod) {
        Milestone requiredMilestone = getRequiredMilestone(travelMethod);
        return requiredMilestone == null ||
               unlockedMilestones.contains(requiredMilestone.getId());
    }

    public void checkMilestoneCompletion(String milestoneId) {
        Milestone milestone = milestones.get(milestoneId);

        if (milestone.isCompleted(client)) {
            unlockMilestone(milestoneId);
        }
    }

    private void unlockMilestone(String milestoneId) {
        unlockedMilestones.add(milestoneId);
        saveUnlockState();
        notifyUnlock(milestoneId);
    }

    private void saveUnlockState() {
        String state = String.join(",", unlockedMilestones);
        configManager.setConfiguration("varlamoreuim", "unlockState", state);
    }
}

public class Milestone {
    private final String id;
    private final String name;
    private final String description;
    private final MilestoneCondition condition;
    private final List<String> unlocksContent; // Travel methods unlocked

    public boolean isCompleted(Client client) {
        return condition.isMet(client);
    }
}

public interface MilestoneCondition {
    boolean isMet(Client client);
}

// Example conditions
public class QuestCompletionCondition implements MilestoneCondition {
    private final int questId;

    @Override
    public boolean isMet(Client client) {
        return client.getVarbitValue(questId) == QUEST_COMPLETE;
    }
}
```

**State Persistence:**
- Unlock state stored in config as comma-separated milestone IDs
- Loaded on plugin startup
- Saved on each new unlock
- Per-account via RuneLite profile system

---

## 3. Data Flow

### 3.1 Game Event Flow → Restriction Check

```
Player Action (e.g., click teleport spell)
    │
    ▼
RuneLite fires MenuOptionClicked event
    │
    ▼
Plugin @Subscribe method receives event
    │
    ▼
Parse event into TravelAttempt object
    │
    ▼
TravelRestrictionEngine.checkTravel(attempt)
    │
    ├─▶ UnlockManager.isUnlocked(travel method) → Check unlock state
    │       │
    │       └─▶ Return: unlocked or blocked
    │
    ├─▶ BoundaryManager.isDestinationAllowed() → Check destination
    │       │
    │       └─▶ Return: in bounds or out of bounds
    │
    └─▶ RestrictionHandler.validate(attempt) → Type-specific validation
            │
            └─▶ Return: RestrictionResult (allowed/blocked + message)
    │
    ▼
RestrictionResult returned
    │
    ├─▶ If BLOCKED: event.consume() + show chat message
    │
    └─▶ If ALLOWED: event continues → game executes action
```

### 3.2 Config Change Propagation

```
User changes config in side panel
    │
    ▼
ConfigManager updates config value
    │
    ▼
ConfigChanged event fired
    │
    ▼
Plugin @Subscribe(ConfigChanged) method
    │
    ▼
Identify changed key (e.g., "blockTeleports")
    │
    ▼
Route to appropriate component
    │
    ├─▶ TravelRestrictionEngine.updateSettings()
    │       │
    │       └─▶ Reload restriction rules
    │
    ├─▶ BoundaryManager.updateBoundary()
    │       │
    │       └─▶ Reload allowed chunk IDs
    │
    └─▶ NpcReplacementSystem.updateReplacements()
            │
            └─▶ Reload NPC replacement registry
    │
    ▼
Components reflect new config state
    │
    ▼
Active restrictions updated (takes effect immediately)
```

### 3.3 Unlock State Persistence

```
Milestone condition met (e.g., quest completed)
    │
    ▼
MilestoneTracker detects completion
    │
    ▼
UnlockManager.unlockMilestone(id)
    │
    ├─▶ Add to unlockedMilestones set
    │
    ├─▶ Save to config (persistent storage)
    │       │
    │       └─▶ ConfigManager.setConfiguration("unlockState", "milestone1,milestone2,...")
    │
    └─▶ Fire UnlockNotification event
            │
            ├─▶ Show chat message
            │
            ├─▶ Update UI panel (unlock list)
            │
            └─▶ Enable newly unlocked travel methods
    │
    ▼
TravelRestrictionEngine queries UnlockManager on next travel attempt
    │
    ▼
New travel methods now allowed
```

### 3.4 NPC Replacement Flow

```
NPC spawns in game world
    │
    ▼
NpcSpawned event fired
    │
    ▼
NpcReplacementSystem.onNpcSpawned(event)
    │
    ▼
Check if NPC ID in replacement registry
    │
    ├─▶ YES: Add to hiddenNpcs set
    │       │
    │       └─▶ NPC marked for hiding
    │
    └─▶ NO: Ignore (normal NPC)
    │
    ▼
RuneLite render cycle
    │
    ▼
ReplacementNpcOverlay.render() called
    │
    ▼
For each hidden NPC:
    │
    ├─▶ Get world position
    │
    ├─▶ Convert to screen position
    │
    ├─▶ Render custom sprite/text
    │
    └─▶ Original NPC model not rendered (via hiding logic)
    │
    ▼
Player right-clicks NPC
    │
    ▼
MenuEntryAdded event
    │
    ▼
CustomDialogueManager intercepts menu
    │
    └─▶ Replace travel options with "Talk-to" only
    │
    ▼
Player selects "Talk-to"
    │
    ▼
WidgetLoaded event (dialogue widget)
    │
    ▼
CustomDialogueManager.onWidgetLoaded()
    │
    └─▶ Replace dialogue text with custom message
    │       (e.g., "I'm not allowed to take you anywhere yet...")
    │
    ▼
Dialogue shows custom content (no travel)
```

---

## 4. Extensibility Patterns

### 4.1 Adding New Restriction Categories

**Without Rearchitecting:**

The plugin is designed for extensibility via **handler registration pattern**:

```java
// In TravelRestrictionEngine
private final List<RestrictionHandler> handlers = new ArrayList<>();

public void registerHandler(RestrictionHandler handler) {
    handlers.add(handler);
}

// Adding new restriction type (e.g., skill-based restrictions)
public class SkillRestrictionHandler implements RestrictionHandler {
    @Override
    public boolean canHandle(TravelType type) {
        return type == TravelType.REQUIRES_SKILL;
    }

    @Override
    public RestrictionResult validate(TravelAttempt attempt) {
        // Check skill requirements for travel method
        if (!hasRequiredSkillLevel(attempt)) {
            return RestrictionResult.blocked("Insufficient skill level");
        }
        return RestrictionResult.allowed();
    }
}

// In plugin startup
travelEngine.registerHandler(new SkillRestrictionHandler());
```

**UI Extensibility:**
```java
// In VarlamoreUimPanel
public void addCategory(String title, CategoryPanel panel) {
    tabbedPane.addTab(title, panel);
}

// Adding new category
SkillRestrictionsPanel skillPanel = new SkillRestrictionsPanel(config, plugin);
panel.addCategory("Skill Restrictions", skillPanel);
```

**Config Extensibility:**
```java
// New config sections added to interface
@ConfigSection(
    name = "Skill Restrictions",
    description = "Skill-based travel requirements",
    position = 3
)
String skillSection = "skills";

@ConfigItem(
    keyName = "requireAgility",
    name = "Require Agility",
    description = "Require agility level for shortcuts",
    section = skillSection
)
default boolean requireAgility() {
    return true;
}
```

### 4.2 Plugin-within-a-Plugin Pattern

**Modular Feature Organization:**

```
VarlamoreUimPlugin (Core Coordinator)
    │
    ├─▶ Feature Module: TravelRestrictions
    │   ├── TravelRestrictionEngine
    │   ├── SpellRestrictionHandler
    │   └── ItemRestrictionHandler
    │
    ├─▶ Feature Module: NpcReplacement
    │   ├── NpcReplacementSystem
    │   ├── CustomDialogueManager
    │   └── ReplacementNpcOverlay
    │
    ├─▶ Feature Module: QualityOfLife
    │   ├── InventoryTagging
    │   ├── BankOrganization
    │   └── Notifications
    │
    └─▶ Feature Module: Unlocks
        ├── UnlockManager
        ├── MilestoneTracker
        └── UnlockNotifier
```

**Module Interface:**
```java
public interface FeatureModule {
    void initialize(Client client, VarlamoreUimConfig config);
    void shutdown();
    void onConfigChanged(String key, String value);
    String getModuleName();
}

public class TravelRestrictionsModule implements FeatureModule {
    private TravelRestrictionEngine engine;

    @Override
    public void initialize(Client client, VarlamoreUimConfig config) {
        engine = new TravelRestrictionEngine(client, config);
        // Register event subscribers
    }

    @Override
    public void shutdown() {
        engine.cleanup();
    }

    @Override
    public void onConfigChanged(String key, String value) {
        if (key.startsWith("travel")) {
            engine.updateSettings();
        }
    }
}

// In main plugin
private final List<FeatureModule> modules = new ArrayList<>();

@Override
protected void startUp() {
    modules.add(new TravelRestrictionsModule());
    modules.add(new NpcReplacementModule());
    modules.add(new QualityOfLifeModule());
    modules.add(new UnlocksModule());

    modules.forEach(m -> m.initialize(client, config));
}

@Subscribe
public void onConfigChanged(ConfigChanged event) {
    modules.forEach(m -> m.onConfigChanged(event.getKey(), event.getNewValue()));
}
```

**Benefits:**
- Each module is independent and testable
- New features added without modifying core plugin
- Modules can be enabled/disabled via config
- Clear separation of concerns

### 4.3 Data-Driven Configuration

**External Data Files:**
```
resources/
├── varlamore_chunks.json         # Boundary chunk IDs
├── npc_replacements.json          # NPC replacement mappings
├── milestones.json                # Milestone definitions
├── travel_methods.json            # Travel method registry
└── dialogues/
    ├── boat_captain_blocked.json
    ├── carpet_merchant_blocked.json
    └── ...
```

**Loading Pattern:**
```java
public class DataLoader {
    public static Set<Integer> loadAllowedChunks() {
        InputStream stream = VarlamoreUimPlugin.class
            .getResourceAsStream("/varlamore_chunks.json");

        try (Reader reader = new InputStreamReader(stream)) {
            Gson gson = new Gson();
            ChunkData data = gson.fromJson(reader, ChunkData.class);
            return new HashSet<>(data.allowedChunkIds);
        }
    }

    public static Map<Integer, ReplacementNpcData> loadNpcReplacements() {
        // Similar pattern
    }
}
```

**Advantages:**
- Easy to modify restrictions without code changes
- Community can contribute data files
- Supports multiple "rule sets" (e.g., Varlamore-locked vs other areas)

---

## 5. Integration Points

### 5.1 RuneLite API Dependencies

| Component | RuneLite API | Purpose |
|-----------|--------------|---------|
| BoundaryManager | `Client.getLocalPlayer().getWorldLocation()` | Get player position |
| | `WorldPoint.getRegionID()` | Calculate chunk/region ID |
| TravelRestrictionEngine | `MenuOptionClicked`, `MenuEntryAdded` | Intercept actions |
| | `ChatMessageManager` | Send restriction messages |
| NpcReplacementSystem | `NpcSpawned`, `NpcDespawned` | Track NPCs |
| | `Overlay`, `Graphics2D` | Render custom NPCs |
| | `WidgetLoaded`, `Widget` | Modify dialogues |
| UnlockManager | `Client.getVarbitValue()` | Check quest/achievement state |
| | `ConfigManager` | Persist unlock state |
| UI Panel | `ClientToolbar` | Add navigation button |
| | `PluginPanel` | Side panel framework |
| Config | `ConfigManager` | Settings persistence |
| | `@ConfigItem`, `@ConfigSection` | Config definition |

### 5.2 Event Subscription Summary

**Priority Events (Critical Path):**
- `MenuOptionClicked` - Final action validation
- `MenuEntryAdded` - Pre-action interception
- `GameStateChanged` - Initialize/reset state

**Secondary Events:**
- `NpcSpawned` / `NpcDespawned` - NPC tracking
- `WidgetLoaded` - Dialogue manipulation
- `ConfigChanged` - Settings updates
- `GameTick` - Periodic checks
- `ChatMessage` - Game feedback monitoring

### 5.3 Third-Party Dependencies

**Build Dependencies:**
```xml
<dependencies>
    <!-- RuneLite Client -->
    <dependency>
        <groupId>net.runelite</groupId>
        <artifactId>client</artifactId>
        <version>${runelite.version}</version>
    </dependency>

    <!-- JSON parsing (Gson) -->
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.8.9</version>
    </dependency>

    <!-- Lombok (optional, for cleaner code) -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.30</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

---

## 6. Build Order and Dependencies

### 6.1 Component Dependency Graph

```
Layer 1 (Foundation - No Dependencies):
    - BoundaryManager (loads chunk data)
    - DataLoader (utility)
    - Config (interface definition)

Layer 2 (Core Logic - Depends on Layer 1):
    - UnlockManager (depends on Config)
    - RestrictionHandler interface

Layer 3 (Feature Implementation - Depends on Layers 1-2):
    - TravelRestrictionEngine (depends on BoundaryManager, UnlockManager)
    - NpcReplacementSystem (depends on BoundaryManager, Config)
    - CustomDialogueManager (depends on Config)

Layer 4 (UI - Depends on Layers 1-3):
    - VarlamoreUimPanel (depends on Config, all feature components)
    - CategoryPanels (depend on Config, feature components)
    - Overlays (depend on feature components)

Layer 5 (Integration - Depends on All Layers):
    - VarlamoreUimPlugin (main plugin, coordinates all components)
```

### 6.2 Suggested Build Order

**Phase 1: Foundation**
1. Define `VarlamoreUimConfig` interface (basic structure)
2. Implement `BoundaryManager` with hardcoded chunk IDs
3. Create `DataLoader` utility for JSON resources
4. Write unit tests for boundary detection

**Phase 2: Core Restrictions**
1. Implement `TravelRestrictionEngine` framework
2. Create `RestrictionHandler` interface
3. Implement `SpellRestrictionHandler` (teleport blocking)
4. Add event subscriptions in main plugin
5. Test basic teleport blocking

**Phase 3: NPC System**
1. Implement `NpcReplacementSystem` (NPC hiding)
2. Create `ReplacementNpcOverlay` (rendering)
3. Implement `CustomDialogueManager` (dialogue injection)
4. Test with one example NPC (e.g., boat captain)

**Phase 4: UI Foundation**
1. Create `VarlamoreUimPanel` shell (tabbed pane)
2. Implement `RestrictionsPanel` (basic version)
3. Add navigation button to toolbar
4. Wire up config bindings

**Phase 5: Unlock System**
1. Implement `UnlockManager` (state tracking)
2. Define `Milestone` data structure
3. Create milestone JSON definitions
4. Integrate with `TravelRestrictionEngine`
5. Add `UnlocksPanel` to UI

**Phase 6: Expansion**
1. Add more restriction handlers (items, NPCs, etc.)
2. Expand UI panels (QoA, Settings)
3. Add more NPC replacements
4. Create comprehensive milestone set

**Phase 7: Polish**
1. Add overlays for boundary visualization
2. Implement notifications
3. Performance optimization
4. Comprehensive testing

### 6.3 Minimal Viable Product (MVP)

**MVP Scope:**
- BoundaryManager with Varlamore chunks
- TravelRestrictionEngine with spell blocking only
- Basic config (enable/disable plugin)
- Simple UI panel (on/off toggle)
- No unlocks (all restrictions active)
- No NPC replacement (just action blocking)

**MVP Component List:**
1. `VarlamoreUimPlugin` (main plugin)
2. `VarlamoreUimConfig` (minimal config)
3. `BoundaryManager` (chunk validation)
4. `TravelRestrictionEngine` (spell blocking only)
5. Event handlers: `MenuOptionClicked`, `MenuEntryAdded`

**MVP Build Time Estimate:** 1-2 weeks for first-time RuneLite developer

---

## 7. Technical Considerations

### 7.1 Performance

**Optimization Strategies:**
- Cache chunk ID lookups (boundary checks are frequent)
- Use `HashSet` for O(1) chunk ID lookups
- Lazy-load NPC replacement data (only when NPC spawns)
- Debounce config change events (batch updates)
- Minimize overlay rendering (only draw visible NPCs)

**Benchmarking Targets:**
- Boundary check: < 1ms
- Travel restriction check: < 5ms
- NPC overlay render: < 10ms per frame
- Config propagation: < 50ms

### 7.2 State Management

**State Storage:**
- Unlock state: Stored in RuneLite config (per-account)
- Current player position: Retrieved from Client on-demand
- Hidden NPCs: In-memory `Set<NPC>` (cleared on shutdown)
- Loaded milestones: In-memory, loaded once at startup

**State Synchronization:**
- No multi-threading needed (RuneLite is single-threaded)
- Config changes are synchronous
- Event handlers are called on game thread

### 7.3 Error Handling

**Graceful Degradation:**
```java
public RestrictionResult checkTravel(TravelAttempt attempt) {
    try {
        // Validation logic
    } catch (Exception e) {
        log.error("Error checking travel restriction", e);
        // Fail-safe: allow travel (don't break game)
        return RestrictionResult.allowed();
    }
}
```

**Logging Strategy:**
- Use SLF4J logger (`@Slf4j` Lombok annotation)
- Log restriction blocks at DEBUG level
- Log errors at ERROR level
- Log milestone unlocks at INFO level

### 7.4 Testing Strategy

**Unit Tests:**
- Test boundary calculations with known chunk IDs
- Test restriction handlers in isolation
- Test milestone condition evaluation
- Mock RuneLite Client for testing

**Integration Tests:**
- Test event flow (mock events → restriction checks)
- Test config propagation
- Test unlock state persistence

**Manual Testing:**
- Test in-game with real travel methods
- Verify NPC replacements render correctly
- Verify dialogue injections work
- Test milestone unlocking

---

## 8. Future Architecture Enhancements

### 8.1 Multi-Region Support

**Extensibility for Other Area Locks:**
```java
public class RegionProfile {
    private final String name; // "Varlamore", "Kourend", "Wilderness"
    private final Set<Integer> allowedChunkIds;
    private final Map<Integer, ReplacementNpcData> npcReplacements;
    private final List<Milestone> milestones;
}

public class BoundaryManager {
    private RegionProfile activeProfile;

    public void loadProfile(String profileName) {
        activeProfile = DataLoader.loadRegionProfile(profileName);
    }
}
```

**Config:**
```java
@ConfigItem(
    keyName = "regionProfile",
    name = "Region Profile",
    description = "Select which area-locked profile to use"
)
default RegionProfile regionProfile() {
    return RegionProfile.VARLAMORE;
}
```

### 8.2 Network Features (Future)

**Potential Network Integration:**
- Shared unlock achievements (leaderboard)
- Community milestone verification
- Profile sharing/export

**Architecture:**
```java
public interface NetworkService {
    void syncUnlocks(String accountId, Set<String> unlockIds);
    List<LeaderboardEntry> getLeaderboard(String region);
    void publishMilestone(String accountId, String milestoneId);
}
```

**Note:** RuneLite plugin API restricts external network calls. This would require approval and careful implementation.

### 8.3 Advanced Dialogue System

**Interactive Dialogue Trees:**
```java
public class InteractiveDialogueTree {
    private DialogueNode currentNode;

    public void selectOption(int optionIndex) {
        currentNode = currentNode.getNextNode(optionIndex);
        renderDialogue(currentNode);
    }

    public void renderDialogue(DialogueNode node) {
        // Modify widget with node text and options
        // Handle NPC face animations
        // Display player responses
    }
}
```

**Use Cases:**
- Flavor dialogue with replacement NPCs
- Tutorial/help system
- In-game milestone hints

---

## 9. Architecture Validation

### 9.1 Quality Gate Checklist

- [x] **Components clearly defined with boundaries**
  - BoundaryManager, TravelRestrictionEngine, NpcReplacementSystem, UnlockManager, UI System
  - Each component has clear responsibilities
  - Interfaces defined between components

- [x] **Data flow direction explicit**
  - Event flow: Game → Plugin → Components → Validation → Response
  - Config flow: UI → Config → Components → Active Restrictions
  - Unlock flow: Milestone → UnlockManager → Config → Components
  - NPC flow: Spawn → Tracking → Hiding → Rendering → Dialogue

- [x] **Build order implications noted**
  - 5-layer dependency graph defined
  - MVP scope identified (minimal dependencies)
  - Phased build approach outlined (7 phases)
  - Dependencies between components explicit

- [x] **RuneLite API integration points identified**
  - Event system: MenuOptionClicked, MenuEntryAdded, NpcSpawned, etc.
  - Client API: WorldPoint, NPC, Widget access
  - Config system: ConfigManager, @ConfigItem
  - UI system: ClientToolbar, PluginPanel, Overlay
  - Chat system: ChatMessageManager

### 9.2 Architecture Strengths

1. **Modularity**: Clear component separation enables independent development and testing
2. **Extensibility**: Handler pattern and module system support new features without rearchitecting
3. **Maintainability**: Data-driven configuration separates rules from code
4. **Testability**: Components can be unit tested in isolation
5. **Performance**: Efficient data structures (HashSet, Maps) for frequent lookups
6. **User Experience**: Immediate feedback via chat messages and UI updates

### 9.3 Architecture Risks

1. **Complexity**: Multiple layers and components may be overwhelming for first plugin
   - **Mitigation**: MVP approach focuses on minimal components first

2. **RuneLite API Changes**: API updates may break plugin
   - **Mitigation**: Follow RuneLite versioning, test with new releases

3. **NPC Rendering**: Custom NPC rendering is complex and error-prone
   - **Mitigation**: Start with simple text overlays, iterate to sprites

4. **Dialogue Injection**: Widget manipulation is fragile
   - **Mitigation**: Use ChatMessageManager as fallback for blocked messages

---

## 10. Recommended Next Steps

### 10.1 Immediate Actions (Pre-Development)

1. **Study RuneLite Examples:**
   - Review existing area-restriction plugins (e.g., Region Locker)
   - Study NPC manipulation plugins (e.g., NPC Indicators)
   - Examine menu entry plugins (e.g., Menu Entry Swapper)

2. **Set Up Development Environment:**
   - Configure RuneLite development environment
   - Set up debugging tools
   - Create test accounts for in-game testing

3. **Define Varlamore Boundary:**
   - Extract Varlamore chunk IDs from game data
   - Create `varlamore_chunks.json` resource file
   - Validate chunk coverage (ensure full region)

### 10.2 Phase 1 Development Tasks

1. Implement `BoundaryManager` with hardcoded chunk IDs
2. Create basic `VarlamoreUimPlugin` shell
3. Subscribe to `MenuOptionClicked` event
4. Implement basic teleport spell blocking
5. Test in-game with common teleport spells

### 10.3 Documentation Tasks

1. Create `DEVELOPMENT.md` with setup instructions
2. Document RuneLite API usage patterns
3. Create contribution guidelines (for future contributors)
4. Write inline code documentation (Javadoc)

---

## Appendix A: Example Code Snippets

### A.1 Main Plugin Structure

```java
package com.varlamoreuim;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;

@Slf4j
@PluginDescriptor(
    name = "Varlamore UIM",
    description = "Enforces Varlamore-locked UIM restrictions",
    tags = {"varlamore", "uim", "area-locked", "restriction"}
)
public class VarlamoreUimPlugin extends Plugin {
    @Inject private Client client;
    @Inject private ClientToolbar clientToolbar;
    @Inject private VarlamoreUimConfig config;

    private BoundaryManager boundaryManager;
    private TravelRestrictionEngine travelEngine;
    private NpcReplacementSystem npcSystem;
    private UnlockManager unlockManager;
    private VarlamoreUimPanel panel;
    private NavigationButton navButton;

    @Provides
    VarlamoreUimConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(VarlamoreUimConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        log.info("Varlamore UIM plugin started");

        // Initialize components
        boundaryManager = new BoundaryManager(client);
        unlockManager = new UnlockManager(client, configManager);
        travelEngine = new TravelRestrictionEngine(client, config,
            boundaryManager, unlockManager);
        npcSystem = new NpcReplacementSystem(client, config);

        // Initialize UI
        panel = injector.getInstance(VarlamoreUimPanel.class);
        navButton = NavigationButton.builder()
            .tooltip("Varlamore UIM")
            .icon(loadIcon())
            .priority(5)
            .panel(panel)
            .build();
        clientToolbar.addNavigation(navButton);
    }

    @Override
    protected void shutDown() throws Exception {
        log.info("Varlamore UIM plugin stopped");

        clientToolbar.removeNavigation(navButton);
        travelEngine.shutdown();
        npcSystem.shutdown();
    }

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked event) {
        TravelAttempt attempt = TravelAttempt.fromMenuClick(event);

        if (attempt != null && config.enableRestrictions()) {
            RestrictionResult result = travelEngine.checkTravel(attempt);

            if (result.isBlocked()) {
                event.consume();
                showBlockedMessage(result.getMessage());
            }
        }
    }

    private void showBlockedMessage(String message) {
        chatMessageManager.queue(QueuedMessage.builder()
            .type(ChatMessageType.GAMEMESSAGE)
            .runeLiteFormattedMessage("<col=ef1020>" + message + "</col>")
            .build());
    }
}
```

### A.2 Boundary Manager Implementation

```java
package com.varlamoreuim.boundary;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class BoundaryManager {
    private final Client client;
    private final Set<Integer> allowedChunkIds;

    public BoundaryManager(Client client) {
        this.client = client;
        this.allowedChunkIds = loadAllowedChunks();
        log.info("Loaded {} allowed chunks for Varlamore", allowedChunkIds.size());
    }

    public boolean isInBounds(WorldPoint location) {
        if (location == null) {
            return false;
        }

        int regionId = location.getRegionID();
        return allowedChunkIds.contains(regionId);
    }

    public boolean isPlayerInBounds() {
        if (client.getLocalPlayer() == null) {
            return false;
        }

        WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
        return isInBounds(playerLocation);
    }

    private Set<Integer> loadAllowedChunks() {
        // For MVP: hardcoded chunk IDs
        // Later: load from JSON resource
        Set<Integer> chunks = new HashSet<>();

        // Example: Varlamore region IDs (placeholder)
        // These would be extracted from game data
        for (int i = 5600; i <= 5700; i++) {
            chunks.add(i);
        }

        return chunks;
    }
}
```

### A.3 Travel Restriction Engine

```java
package com.varlamoreuim.restrictions;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.events.MenuOptionClicked;

@Slf4j
public class TravelRestrictionEngine {
    private final Client client;
    private final VarlamoreUimConfig config;
    private final BoundaryManager boundaryManager;
    private final UnlockManager unlockManager;

    public TravelRestrictionEngine(Client client, VarlamoreUimConfig config,
                                   BoundaryManager boundaryManager,
                                   UnlockManager unlockManager) {
        this.client = client;
        this.config = config;
        this.boundaryManager = boundaryManager;
        this.unlockManager = unlockManager;
    }

    public RestrictionResult checkTravel(TravelAttempt attempt) {
        // 1. Check if enabled
        if (!config.enableRestrictions()) {
            return RestrictionResult.allowed();
        }

        // 2. Check unlock state
        if (!unlockManager.isUnlocked(attempt.getTravelMethod())) {
            return RestrictionResult.blocked(
                "You haven't unlocked this travel method yet."
            );
        }

        // 3. Check destination
        if (attempt.getDestination() != null) {
            if (!boundaryManager.isInBounds(attempt.getDestination())) {
                return RestrictionResult.blocked(
                    "That would take you outside of Varlamore!"
                );
            }
        }

        return RestrictionResult.allowed();
    }

    public void shutdown() {
        // Cleanup if needed
    }
}

// Supporting classes
class TravelAttempt {
    private final String travelMethod;
    private final WorldPoint destination;

    public static TravelAttempt fromMenuClick(MenuOptionClicked event) {
        String option = event.getMenuOption();
        String target = event.getMenuTarget();

        // Parse teleport spells
        if (option.equals("Cast") && isTeleportSpell(target)) {
            return new TravelAttempt(
                target,
                getTeleportDestination(target)
            );
        }

        return null;
    }

    private static boolean isTeleportSpell(String spell) {
        return spell.contains("Teleport") || spell.contains("Teleportation");
    }

    private static WorldPoint getTeleportDestination(String spell) {
        // Map spell names to destinations
        // This would be a comprehensive lookup table
        return null; // Placeholder
    }
}

class RestrictionResult {
    private final boolean blocked;
    private final String message;

    public static RestrictionResult allowed() {
        return new RestrictionResult(false, null);
    }

    public static RestrictionResult blocked(String message) {
        return new RestrictionResult(true, message);
    }

    public boolean isBlocked() {
        return blocked;
    }

    public String getMessage() {
        return message;
    }
}
```

---

## Appendix B: Resource File Examples

### B.1 varlamore_chunks.json

```json
{
  "region": "Varlamore",
  "description": "Chunk IDs defining the Varlamore region boundary",
  "allowedChunkIds": [
    5600, 5601, 5602, 5603,
    5700, 5701, 5702, 5703,
    5800, 5801, 5802, 5803
  ],
  "notes": "These are placeholder values. Extract actual chunk IDs from game data."
}
```

### B.2 milestones.json

```json
{
  "milestones": [
    {
      "id": "quest_children_of_the_sun",
      "name": "Children of the Sun",
      "description": "Complete the Children of the Sun quest",
      "condition": {
        "type": "quest_completion",
        "questId": 1234
      },
      "unlocks": [
        "gnome_glider_varlamore",
        "spirit_tree_varlamore"
      ]
    },
    {
      "id": "combat_level_50",
      "name": "Combat Level 50",
      "description": "Reach combat level 50",
      "condition": {
        "type": "combat_level",
        "requiredLevel": 50
      },
      "unlocks": [
        "wilderness_teleport_lever"
      ]
    }
  ]
}
```

### B.3 npc_replacements.json

```json
{
  "replacements": [
    {
      "originalNpcId": 1234,
      "customName": "Boat Captain (Locked)",
      "dialogueFile": "boat_captain_blocked.json",
      "hideOriginal": true
    },
    {
      "originalNpcId": 5678,
      "customName": "Magic Carpet Merchant (Locked)",
      "dialogueFile": "carpet_merchant_blocked.json",
      "hideOriginal": true
    }
  ]
}
```

---

## Conclusion

This architecture provides a solid foundation for building a RuneLite plugin that enforces Varlamore-locked UIM restrictions. The design emphasizes:

1. **Modularity** - Components are independent and can be developed/tested separately
2. **Extensibility** - Easy to add new restriction types and features
3. **Maintainability** - Data-driven configuration and clear separation of concerns
4. **Performance** - Efficient data structures for frequent operations
5. **User Experience** - Immediate feedback and intuitive UI

The recommended build order (MVP → Core Features → Expansions) allows for incremental development while maintaining a working plugin at each stage.

**Key Success Factors:**
- Start with MVP (boundary + basic spell blocking)
- Iterate on one component at a time
- Test thoroughly in-game at each step
- Follow RuneLite best practices and conventions
- Use data-driven configuration for flexibility

This architecture supports both the immediate Varlamore UIM use case and future expansion to other area-locked challenges or additional quality-of-life features.

---

**Document Version:** 1.0
**Last Updated:** 2026-02-16
**Author:** Research Agent (GSD Framework)
**Status:** Ready for Development
