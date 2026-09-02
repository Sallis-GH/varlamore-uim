# Native NPC Stand-ins and Dialogue

**Date:** 2026-09-02
**Status:** Approved design, pending implementation plan
**Scope:** Charter ship docks only. Primio the quetzal keeps its chat-message behaviour.

## Goal

Make the blocked charter ship docks feel like part of the game instead of a plugin. Today each dock has a Mysterious Old Man RuneLiteObject at a fixed tile, clickable only via the tile, whose "Talk-to" prints a line in the chatbox. After this work:

- Every hidden Trader Crewmember is replaced by a distinct lore NPC that stands where the crewmember stands and moves when it moves.
- The stand-in is clickable on its body, not its tile, with "Talk-to" and "Examine" menu entries that look like a real NPC's.
- "Talk-to" walks the player over, then opens a real-looking chatbox dialogue with chatheads, multi-page text, and an options menu.
- The dialogue explains, in OSRS humour, that Dizana's quiver is the sailing permit, and checks for it live.

## Constraints from the RuneLite API (1.12.38)

- `NpcOverrides` is read-only, so the real NPC cannot be transmogrified through the public API. Hiding via `RenderCallback` plus a RuneLiteObject puppet stays the approach.
- `Perspective.getClickbox(Client, WorldView, Model, orientation, x, y, z)` projects a model to a screen `Shape`. This gives the puppet a hitbox.
- `Widget.setModelType(WidgetModelType.NPC_CHATHEAD)` with `setModelId(npcId)` and `setAnimationId` renders an NPC chathead client-side. `LOCAL_PLAYER_CHATHEAD` does the same for the player.
- `ChatboxPanelManager.openInput(ChatboxInput)` opens the chatbox message layer and provides a container widget. If the input implements RuneLite's `KeyListener` and `MouseListener`, the manager registers them while open.
- `MenuEntry.setType`, `setParam0`, `setParam1` allow retyping a clicked entry into a `WALK` action.
- Fonts: `FontID.QUILL_8` (497) for dialogue text.
- Dialogue widget layout reference: `InterfaceID.ChatLeft` (NPC, group 231) and `InterfaceID.ChatRight` (player, group 217), children HEAD, NAME, CONTINUE, TEXT.

## Architecture

Three packages under `com.varlamoreuim`:

| Package | Responsibility |
|---|---|
| `dialogue` | Script model, chatbox renderer, input handling, lifecycle. Knows nothing about docks or NPCs. |
| `standin` | Puppet lifecycle bound to real NPCs, persona roster, clickbox menu injection, walk-to. Uses `dialogue` to open conversations. |
| `npc` | Existing `NpcTransportBlocker`, reduced to: hiding charter NPCs via RenderCallback, Primio blocking, Dizana's quiver detection and the unlock flag. All stand-in code moves out. |

`VarlamoreUimPlugin` subscribes to events and delegates. No new event plumbing patterns; follow the existing `initClient(...)` style for manually constructed services, or inject where the class is created by Guice.

### Data flow

```
NpcSpawned(charter id) ──> StandInRegistry.bind(npc)
                              ├─ dock = nearest DockAnchor to npc.getWorldLocation()
                              ├─ persona = PersonaRoster.get(dock, npc.getId())
                              └─ StandIn { RuneLiteObject, NPC, Persona }

ClientTick ──> StandInRegistry.sync()   (copy location/orientation/pose to each puppet)

PostMenuSort ──> StandInMenuInjector.inject()
                    for each active StandIn: clickbox hit-test on mouse position
                    on hit: add "Talk-to" + "Examine" entries

MenuOptionClicked(Talk-to) ──> StandInMenuInjector.onTalkTo(standIn)
                                  walkTo enabled: retype entry to WALK, set pendingTalk
                                  walkTo disabled: DialogueManager.open(persona.script, persona)

GameTick ──> StandInMenuInjector.tickPendingTalk()
                player adjacent && not moving ──> DialogueManager.open(...)
                timeout / player clicked elsewhere ──> clear

NpcDespawned / unlock / logout ──> StandInRegistry.unbind(...) / clear()
```

## Dialogue engine (`com.varlamoreuim.dialogue`)

### Script model

The build targets Java 11, so these are plain immutable classes (Lombok `@Value`), not records.

```java
interface DialoguePage {}
@Value class PlayerLine implements DialoguePage { String text; String next; }
@Value class NpcLine implements DialoguePage { String text; Expression expression; String next; }
@Value class Options implements DialoguePage { String title; List<Option> options; }
@Value class Option { String label; String next; @Nullable Predicate<DialogueContext> condition; @Nullable String elseNext; }
@Value class DialogueScript { String startId; Map<String, DialoguePage> pages; }
enum Expression { DEFAULT, HAPPY, SAD, ANGRY, LAUGH }   // maps to chathead animation ids
```

- `next` is a page id or the constant `DialogueScript.END`.
- An `Option` with a `condition` evaluates it on selection: true goes to `next`, false goes to `elseNext`. Used for the live quiver check.
- `DialogueContext` exposes `hasDizanasQuiver()` and `playerName()`. It is an interface so tests can stub it.
- `DialogueScript` validates on construction: every `next` and option target must exist or be END, and `startId` must exist. Invalid scripts throw, so a bad persona is caught at plugin start rather than in-game.

### Text layout

`DialogueText.wrap(String text, int maxCharsPerLine)` splits on word boundaries. A speech page shows at most four lines. `DialogueScript.expand()` splits any page whose wrapped text exceeds four lines into a chain of pages at build time, so the renderer never handles overflow. Character width is approximated at build time and verified in-game; the constant lives in one place.

### Renderer: `DialogueInput`

Extends `ChatboxInput`, implements `KeyListener` and `MouseListener`.

On `open()`, on the client thread, creates children on `chatboxPanelManager.getContainerWidget()`:

| Child | Type | Notes |
|---|---|---|
| head | MODEL | `NPC_CHATHEAD` + persona NPC id for NpcLine, `LOCAL_PLAYER_CHATHEAD` for PlayerLine. Zoom and rotation constants copied from the real 231/217 head widget in-game. Left side for NPC, right side for player, matching the game. Absent on Options pages. |
| name | TEXT | Quill font, colour `0x8B0000`, centred. Persona display name or local player name. |
| body | TEXT | Quill font, black, centred, up to four lines. |
| continue | TEXT | Quill font, colour `0x0000FF`, "Click here to continue". Speech pages only. |
| option N | TEXT | Quill font, black, one per option, prefixed by number. Options pages only. Title above in quill font. |

`Expression` maps to the standard chathead talk animation ids. The still pose is applied when the page has been fully shown, matching the game. The exact ids are confirmed in-game during implementation and kept in one enum.

`render(page)` clears and rebuilds the children. The container's own layout, background and message-layer state are managed by the panel manager.

### Input

- Speech page: space, or a mouse click within the chatbox bounds, advances to `next`.
- Options page: keys 1 to 5 select by index. A click on an option line's bounds selects it.
- Escape closes.
- All page transitions run on the client thread. Key and mouse events are consumed while the dialogue is open so they do not reach the game.

### Lifecycle: `DialogueManager`

- `open(DialogueScript, Persona, DialogueContext)`: no-op if a dialogue is already open. Otherwise creates a `DialogueInput` and calls `chatboxPanelManager.openInput`.
- `close()`: `chatboxPanelManager.close()` if our input is current.
- Closes automatically when a page transitions to END.
- Subscribes to `GameTick`: if the local player's world location changed since open, close. This mirrors the game.
- The panel manager already closes on `GameStateChanged` and on interface conflicts; nothing extra needed.
- A completion callback lets the caller react to a specific END, used by the quiver-success branch to trigger unlock.

## Stand-in system (`com.varlamoreuim.standin`)

### `Persona`

```java
@Value class Persona { String id; String displayName; int npcId; String examine; DialogueScript script; }
```

`PersonaRoster` is a static map keyed by `(Dock, crewmemberNpcId)`. On plugin start it validates each persona's NPC definition has chathead models via `client.getNpcDefinition(npcId).getChatheadModels()`; a persona that fails logs a warning and falls back to the Mysterious Old Man model and chathead while keeping its own name and script.

### `Dock`

Enum `SUNSET_COAST`, `ALDARIN`, `FORTIS_COTHON`, each with the anchor `WorldPoint` already in the codebase. `Dock.nearest(WorldPoint)` returns the dock within 20 tiles or empty. Anchors are identifiers now, not spawn positions.

### `StandIn`

Holds the `RuneLiteObject`, the bound `NPC` and the `Persona`. `sync()` copies `npc.getLocalLocation()`, `npc.getOrientation()` and the current pose animation onto the object. Pose mirroring: if `npc.getPoseAnimation()` differs from the last applied id, load and set that animation on the puppet, looping. The puppet model is built from the persona's NPC definition the same way the current code builds the Mysterious Old Man.

### `StandInRegistry`

- `onNpcSpawned(NPC)`: if id is a charter crewmember and blocking is enabled and not unlocked, resolve dock and persona, create and activate a `StandIn`. Unknown dock or persona logs at debug and skips.
- `onNpcDespawned(NPC)`: deactivate and remove.
- `onClientTick()`: sync all.
- `rescan()`: iterate `client.getTopLevelWorldView().npcs()` and bind any charter NPC not already bound. Called on login, on config enable, and on lock after unlock.
- `clear()`: deactivate all. Called on logout, shutdown, unlock, config disable.
- `activeStandIns()`: read-only view for the injector.

### `StandInMenuInjector`

- `onPostMenuSort()`: for each active stand-in whose object has a model and location, compute `Perspective.getClickbox(client, worldView, model, orientation, localX, localY, height)` and test `client.getMouseCanvasPosition()`. First hit adds two entries via `client.getMenu().createMenuEntry(-1)`: "Examine" then "Talk-to", both typed `RUNELITE`, target `<col=ffff00>Name</col>`, with `onClick` handlers. Left-click therefore resolves to Talk-to.
- Examine: queue a `ChatMessageType.NPC_EXAMINE` message with the persona's examine text.
- Talk-to with walk-to disabled: open dialogue immediately.
- Talk-to with walk-to enabled: pick the adjacent tile to the puppet closest to the player, retype the clicked `MenuEntry` to `MenuAction.WALK` with the tile's scene x and y in param0 and param1, and store a `PendingTalk(standIn, deadlineTick)`. Do not consume the event, so the client performs the walk.
- `onGameTick()`: if a `PendingTalk` exists and the player is within one tile of the puppet's NPC and its pose is idle, open dialogue and clear. If the deadline (15 ticks) passes, clear.
- `onMenuOptionClicked(any other entry)`: clear `PendingTalk`.
- Only one pending talk at a time.

### Unlock interaction

`NpcTransportBlocker.setUnlocked(true)` now calls `StandInRegistry.clear()` instead of managing objects itself, and `setUnlocked(false)` calls `rescan()`. The dialogue's quiver-success END callback calls the existing unlock path so the crew appears immediately after the conversation closes.

## Personas and scripts

Shared premise: the Kingdom of Varlamore has closed its sea lanes to anyone who isn't a proven champion. Dizana's quiver, the Fortis Colosseum champion's token, is the sailing permit.

Every script has the same skeleton:

1. PlayerLine: "Any ships sailing today?"
2. NpcLine: persona-specific refusal, one or two pages.
3. Options "Select an option":
   - "Why not?" → one or two NpcLines → back to options.
   - "What's Dizana's quiver?" → one or two NpcLines → back to options.
   - "I have one right here!" → condition `hasDizanasQuiver`:
     - true → NpcLine caving in and pointing at the crew → END with unlock callback.
     - false → NpcLine mocking the empty quiver-shaped space → back to options.
   - "Never mind." → NpcLine short sign-off → END.

Persona voices. Exact lines are written in the implementation plan; the tone is set here.

| Dock | Crewmember id | Persona | Voice |
|---|---|---|---|
| Sunset Coast | 9314 | Mysterious Old Man | Denies being the random event man while being obviously the random event man. Blames the tides, then the moon, then admits the harbourmaster took his boat. |
| Sunset Coast | 9350 | Fisherman | Hasn't caught anything since the lanes closed; the fish are "sailing out of spite". Recommends the Hunter Guild "if you like disappointment with a net". |
| Aldarin | 9314 | Vintner | Waiting on barrels that never come. Wine ships fine, people don't. Offers a grape-stomping job. |
| Aldarin | 9350 | Ralos Pilgrim | Ralos guides ships by day and has personally decided not to guide yours. Serene. |
| Fortis Cothon | 9326 | Fortis Guard | Harbour closed by decree. Quotes the decree: "Ultimate Ironmen keep wandering off and losing everything." Doesn't know what it means but it sounds serious. |
| Fortis Cothon | 9362 | Harbourmaster | Bureaucrat. You need form 7B. Form 7B is Dizana's quiver. You can't fill it in, you have to win it. |

NPC definition ids for the five new personas are resolved during implementation by name lookup against the client cache and confirmed to have chathead models. Fallback is the Mysterious Old Man id 2830.

## Configuration

Two new items in the existing Restrictions section of `VarlamoreUimConfig`:

| Key | Name | Default | Effect |
|---|---|---|---|
| `walkToStandIns` | Walk to dock NPCs | on | Talk-to walks the player adjacent before the dialogue opens. |
| `nativeDialogue` | Native dialogue | on | Off falls back to the current single chat message for Talk-to. |

`blockNpcTransport` continues to gate the whole system.

## Error handling

- Missing NPC definition or model data for a persona: warn once, fall back to Mysterious Old Man visuals.
- Chatbox container widget null on open: log, fall back to chat message.
- Clickbox projection returning null (off-screen): treat as no hit.
- Walk retype failing to move the player: the 15-tick deadline clears the pending talk. No dialogue opens; the player can click again. Turning the toggle off avoids the path entirely.
- Any exception inside per-frame handlers is caught and logged at debug so a bad frame cannot spam or crash the client thread.

## Testing

Unit tests (JUnit 4, no client):

- `DialogueScriptTest`: validation rejects dangling ids; `expand()` splits long pages; navigation reaches END; conditional options route both ways with a stubbed `DialogueContext`.
- `DialogueTextTest`: wrap respects word boundaries and the line limit.
- `PersonaRosterTest`: every roster entry has a valid script; lookup by dock and id; each dock maps its two crewmember ids.
- `DockTest`: `nearest` picks the right dock and returns empty far away.
- `WalkTargetTest`: adjacent tile selection prefers the tile nearest the player and skips the puppet's own tile.
- `PendingTalkTest`: deadline expiry and clear-on-other-click.

In-game verification checklist, recorded in the plan:

1. Six distinct stand-ins appear, one per hidden crewmember, at all three docks.
2. Puppets track crewmembers after a scene reload and when a crewmember turns.
3. Hover on the body shows "Talk-to Name" top-left; hover on the adjacent tile does not.
4. Examine prints in the NPC-examine style.
5. Walk-to moves the player adjacent and the dialogue opens on arrival; toggle off opens instantly.
6. Chatheads render with correct size and angle and animate while talking.
7. Space, click, number keys and option clicks all work; Escape closes.
8. Moving closes the dialogue; opening the bank does not leave the panel stuck.
9. "I have one right here!" with a quiver equipped or in inventory ends the dialogue and reveals the real crew.
10. Turning off Native dialogue restores the chat message behaviour.

## Out of scope

- Primio the quetzal dialogue.
- Sound effects.
- Persisting dialogue state across sessions.
- Any change to spell, item or minigame blocking.
