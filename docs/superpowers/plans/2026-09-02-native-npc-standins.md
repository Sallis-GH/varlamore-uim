# Native NPC Stand-ins and Dialogue Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Replace the fixed-tile Mysterious Old Man stand-ins with six lore NPC puppets bound to the hidden charter crewmembers, clickable on their bodies, that open a native-looking chatbox dialogue with chatheads and an options menu.

**Architecture:** A `dialogue` package renders scripted conversations on RuneLite's chatbox message layer via `ChatboxPanelManager`, knowing nothing about docks. A `standin` package binds one RuneLiteObject puppet per hidden Trader Crewmember, mirrors its position each client tick, injects menu entries when the mouse is inside the puppet's projected clickbox, and optionally retypes the click into a walk before opening the dialogue. `NpcTransportBlocker` shrinks to hiding, Primio and the quiver gate.

**Tech Stack:** Java 11, RuneLite client 1.12.38 API, Lombok `@Value`, JUnit 4.12, Gradle 8.

**Spec:** `docs/superpowers/specs/2026-09-02-native-npc-standins-design.md`

## Global Constraints

- Source level is Java 11 (`options.release.set(11)` in build.gradle). No records, no sealed types, no `Stream.toList()`. Use Lombok `@Value` for immutable data.
- Code style matches the repo: tabs, Allman braces (opening brace on its own line), Javadoc on public types.
- Only public RuneLite API. No reflection into client internals, no mutation of arrays returned by `NPCComposition`.
- Every new class gets a one-paragraph Javadoc explaining its role.
- Unit tests must not touch `Client`, widgets or the event bus. Anything needing them is verified in-game per the checklist in Task 9.
- Run tests with `./gradlew test --tests '<fully.qualified.TestClass>'`. Run the whole suite with `./gradlew test`. Build with `./gradlew build`.
- Commit after every task with a conventional message (`feat:`, `refactor:`, `test:`, `docs:`).
- Scripts and text: keep OSRS humour, no plugin jargon ("Varlamore UIM", "toggle", "config") inside any dialogue line.

## File Structure

| File | Responsibility |
|---|---|
| `src/main/java/com/varlamoreuim/dialogue/DialoguePage.java` | Marker interface for page types. |
| `src/main/java/com/varlamoreuim/dialogue/PlayerLine.java` | Player speech page. |
| `src/main/java/com/varlamoreuim/dialogue/NpcLine.java` | NPC speech page with expression and effect. |
| `src/main/java/com/varlamoreuim/dialogue/Options.java` | Options menu page. |
| `src/main/java/com/varlamoreuim/dialogue/Option.java` | One option, optionally conditional. |
| `src/main/java/com/varlamoreuim/dialogue/Expression.java` | Chathead animation ids per mood. |
| `src/main/java/com/varlamoreuim/dialogue/DialogueEffect.java` | Side effects a page can trigger. |
| `src/main/java/com/varlamoreuim/dialogue/DialogueContext.java` | Runtime facts the script can query. |
| `src/main/java/com/varlamoreuim/dialogue/DialogueText.java` | Word wrap. |
| `src/main/java/com/varlamoreuim/dialogue/DialogueScript.java` | Page graph, validation, builder, expand. |
| `src/main/java/com/varlamoreuim/dialogue/Speaker.java` | Name and NPC id for the chathead. |
| `src/main/java/com/varlamoreuim/dialogue/DialogueLayout.java` | Widget position constants. |
| `src/main/java/com/varlamoreuim/dialogue/DialogueInput.java` | ChatboxInput that renders pages and handles keys. |
| `src/main/java/com/varlamoreuim/dialogue/DialogueManager.java` | Open, close, close-on-move, effect callbacks. |
| `src/main/java/com/varlamoreuim/standin/Dock.java` | Dock enum with anchor points and nearest lookup. |
| `src/main/java/com/varlamoreuim/standin/Persona.java` | Name, NPC id, examine, script. |
| `src/main/java/com/varlamoreuim/standin/PersonaRoster.java` | The six personas and their scripts. |
| `src/main/java/com/varlamoreuim/standin/WalkTarget.java` | Adjacent tile selection. |
| `src/main/java/com/varlamoreuim/standin/PendingTalk.java` | Pending walk-then-talk state. |
| `src/main/java/com/varlamoreuim/standin/StandIn.java` | One puppet bound to one NPC. |
| `src/main/java/com/varlamoreuim/standin/StandInRegistry.java` | Bind, unbind, sync, rescan, clear. |
| `src/main/java/com/varlamoreuim/standin/StandInMenuInjector.java` | Clickbox menu entries, examine, talk-to, walk-to. |
| `src/main/java/com/varlamoreuim/npc/NpcTransportBlocker.java` | Modified: stand-in code removed. |
| `src/main/java/com/varlamoreuim/VarlamoreUimConfig.java` | Modified: two toggles. |
| `src/main/java/com/varlamoreuim/VarlamoreUimPlugin.java` | Modified: wiring. |
| `src/test/java/com/varlamoreuim/dialogue/DialogueTextTest.java` | Wrap tests. |
| `src/test/java/com/varlamoreuim/dialogue/DialogueScriptTest.java` | Validation, expand, option resolution. |
| `src/test/java/com/varlamoreuim/standin/DockTest.java` | Nearest dock. |
| `src/test/java/com/varlamoreuim/standin/WalkTargetTest.java` | Adjacent tile. |
| `src/test/java/com/varlamoreuim/standin/PendingTalkTest.java` | Expiry. |
| `src/test/java/com/varlamoreuim/standin/PersonaRosterTest.java` | Roster completeness and script validity. |

---

### Task 1: Dialogue script model and text wrap

**Files:**
- Create: `src/main/java/com/varlamoreuim/dialogue/DialoguePage.java`
- Create: `src/main/java/com/varlamoreuim/dialogue/PlayerLine.java`
- Create: `src/main/java/com/varlamoreuim/dialogue/NpcLine.java`
- Create: `src/main/java/com/varlamoreuim/dialogue/Options.java`
- Create: `src/main/java/com/varlamoreuim/dialogue/Option.java`
- Create: `src/main/java/com/varlamoreuim/dialogue/Expression.java`
- Create: `src/main/java/com/varlamoreuim/dialogue/DialogueEffect.java`
- Create: `src/main/java/com/varlamoreuim/dialogue/DialogueContext.java`
- Create: `src/main/java/com/varlamoreuim/dialogue/DialogueText.java`
- Create: `src/main/java/com/varlamoreuim/dialogue/DialogueScript.java`
- Test: `src/test/java/com/varlamoreuim/dialogue/DialogueTextTest.java`
- Test: `src/test/java/com/varlamoreuim/dialogue/DialogueScriptTest.java`

**Interfaces:**
- Produces: `DialogueScript.builder(String startId)` with `.player(id, text, next)`, `.npc(id, text, next)`, `.npc(id, text, Expression, DialogueEffect, next)`, `.options(id, title, Option...)`, `.build()`. `DialogueScript.END` constant. `DialogueScript.page(String id)`. `Option.of(label, next)`, `Option.conditional(label, Predicate<DialogueContext>, next, elseNext)`, `Option.resolve(DialogueContext)`. `DialogueText.wrap(String, int) -> List<String>`. `DialogueText.MAX_CHARS_PER_LINE`, `DialogueText.MAX_LINES_PER_PAGE`.

- [ ] **Step 1: Write the failing wrap tests**

`src/test/java/com/varlamoreuim/dialogue/DialogueTextTest.java`:

```java
package com.varlamoreuim.dialogue;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class DialogueTextTest
{
	@Test
	public void shortTextIsOneLine()
	{
		List<String> lines = DialogueText.wrap("Hello there.", 20);
		assertEquals(1, lines.size());
		assertEquals("Hello there.", lines.get(0));
	}

	@Test
	public void wrapsOnWordBoundary()
	{
		List<String> lines = DialogueText.wrap("The quick brown fox jumps over the lazy dog", 15);
		assertEquals("The quick brown", lines.get(0));
		assertEquals("fox jumps over", lines.get(1));
		assertEquals("the lazy dog", lines.get(2));
		assertEquals(3, lines.size());
	}

	@Test
	public void singleWordLongerThanLimitStaysWhole()
	{
		List<String> lines = DialogueText.wrap("Supercalifragilistic yes", 5);
		assertEquals("Supercalifragilistic", lines.get(0));
		assertEquals("yes", lines.get(1));
	}

	@Test
	public void collapsesRepeatedSpaces()
	{
		List<String> lines = DialogueText.wrap("a   b", 10);
		assertEquals("a b", lines.get(0));
	}
}
```

- [ ] **Step 2: Run to verify it fails**

Run: `./gradlew test --tests 'com.varlamoreuim.dialogue.DialogueTextTest'`
Expected: compilation FAIL, `DialogueText` does not exist.

- [ ] **Step 3: Implement DialogueText**

`src/main/java/com/varlamoreuim/dialogue/DialogueText.java`:

```java
package com.varlamoreuim.dialogue;

import java.util.ArrayList;
import java.util.List;

/**
 * Word-wrap helper for dialogue pages. The chatbox dialogue area fits roughly
 * MAX_CHARS_PER_LINE quill-font characters per line and MAX_LINES_PER_PAGE lines,
 * matching the game's own NPC dialogue. Both constants are verified in-game.
 */
public final class DialogueText
{
	public static final int MAX_CHARS_PER_LINE = 52;
	public static final int MAX_LINES_PER_PAGE = 4;

	private DialogueText()
	{
	}

	public static List<String> wrap(String text, int maxCharsPerLine)
	{
		List<String> lines = new ArrayList<>();
		StringBuilder current = new StringBuilder();
		for (String word : text.trim().split("\\s+"))
		{
			if (current.length() == 0)
			{
				current.append(word);
			}
			else if (current.length() + 1 + word.length() <= maxCharsPerLine)
			{
				current.append(' ').append(word);
			}
			else
			{
				lines.add(current.toString());
				current = new StringBuilder(word);
			}
		}
		if (current.length() > 0)
		{
			lines.add(current.toString());
		}
		return lines;
	}
}
```

- [ ] **Step 4: Run wrap tests**

Run: `./gradlew test --tests 'com.varlamoreuim.dialogue.DialogueTextTest'`
Expected: PASS, 4 tests.

- [ ] **Step 5: Write the failing script tests**

`src/test/java/com/varlamoreuim/dialogue/DialogueScriptTest.java`:

```java
package com.varlamoreuim.dialogue;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DialogueScriptTest
{
	private static final DialogueContext HAS_QUIVER = new DialogueContext()
	{
		@Override
		public boolean hasDizanasQuiver()
		{
			return true;
		}

		@Override
		public String playerName()
		{
			return "Tester";
		}
	};

	private static final DialogueContext NO_QUIVER = new DialogueContext()
	{
		@Override
		public boolean hasDizanasQuiver()
		{
			return false;
		}

		@Override
		public String playerName()
		{
			return "Tester";
		}
	};

	@Test
	public void buildsLinearScript()
	{
		DialogueScript s = DialogueScript.builder("a")
			.player("a", "Hi", "b")
			.npc("b", "Hello", DialogueScript.END)
			.build();
		assertTrue(s.page("a") instanceof PlayerLine);
		assertEquals("b", ((PlayerLine) s.page("a")).getNext());
		assertEquals(DialogueScript.END, ((NpcLine) s.page("b")).getNext());
	}

	@Test
	public void rejectsDanglingNext()
	{
		try
		{
			DialogueScript.builder("a").player("a", "Hi", "missing").build();
			fail("expected IllegalStateException");
		}
		catch (IllegalStateException expected)
		{
			assertTrue(expected.getMessage().contains("missing"));
		}
	}

	@Test
	public void rejectsMissingStart()
	{
		try
		{
			DialogueScript.builder("nope").npc("a", "x", DialogueScript.END).build();
			fail("expected IllegalStateException");
		}
		catch (IllegalStateException expected)
		{
			assertTrue(expected.getMessage().contains("nope"));
		}
	}

	@Test
	public void rejectsDanglingOptionTarget()
	{
		try
		{
			DialogueScript.builder("m")
				.options("m", "Pick", Option.of("Go", "gone"))
				.build();
			fail("expected IllegalStateException");
		}
		catch (IllegalStateException expected)
		{
			assertTrue(expected.getMessage().contains("gone"));
		}
	}

	@Test
	public void conditionalOptionResolvesBothWays()
	{
		Option o = Option.conditional("Have it", DialogueContext::hasDizanasQuiver, "yes", "no");
		assertEquals("yes", o.resolve(HAS_QUIVER));
		assertEquals("no", o.resolve(NO_QUIVER));
		assertEquals("plain", Option.of("Plain", "plain").resolve(NO_QUIVER));
	}

	@Test
	public void expandSplitsLongNpcLineIntoChain()
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 40; i++)
		{
			sb.append("word ");
		}
		DialogueScript s = DialogueScript.builder("a")
			.npc("a", sb.toString().trim(), "b")
			.npc("b", "End", DialogueScript.END)
			.build();
		NpcLine first = (NpcLine) s.page("a");
		assertEquals("a#1", first.getNext());
		NpcLine second = (NpcLine) s.page("a#1");
		assertEquals("b", second.getNext());
		assertTrue(DialogueText.wrap(first.getText(), DialogueText.MAX_CHARS_PER_LINE).size() <= DialogueText.MAX_LINES_PER_PAGE);
	}

	@Test
	public void expandKeepsEffectOnLastChunkOnly()
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 40; i++)
		{
			sb.append("word ");
		}
		DialogueScript s = DialogueScript.builder("a")
			.npc("a", sb.toString().trim(), Expression.HAPPY, DialogueEffect.UNLOCK_CHARTER, DialogueScript.END)
			.build();
		assertEquals(DialogueEffect.NONE, ((NpcLine) s.page("a")).getEffect());
		assertEquals(DialogueEffect.UNLOCK_CHARTER, ((NpcLine) s.page("a#1")).getEffect());
	}
}
```

- [ ] **Step 6: Run to verify it fails**

Run: `./gradlew test --tests 'com.varlamoreuim.dialogue.DialogueScriptTest'`
Expected: compilation FAIL, classes do not exist.

- [ ] **Step 7: Implement the model classes**

`DialoguePage.java`:

```java
package com.varlamoreuim.dialogue;

/**
 * Marker for one page in a dialogue script. Implementations are
 * {@link PlayerLine}, {@link NpcLine} and {@link Options}.
 */
public interface DialoguePage
{
}
```

`Expression.java`:

```java
package com.varlamoreuim.dialogue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Chathead animation per mood. Each has a talking animation shown while the
 * page is fresh and a still animation once it has been shown. Ids are the
 * standard OSRS chathead animations; verify in-game (Task 3 calibration).
 */
@Getter
@RequiredArgsConstructor
public enum Expression
{
	DEFAULT(567, 588),
	HAPPY(567, 588),
	SAD(610, 588),
	ANGRY(614, 588),
	LAUGH(605, 588);

	private final int talkAnimationId;
	private final int stillAnimationId;
}
```

`DialogueEffect.java`:

```java
package com.varlamoreuim.dialogue;

/**
 * Side effect fired when an {@link NpcLine} carrying it is shown.
 */
public enum DialogueEffect
{
	NONE,
	UNLOCK_CHARTER
}
```

`DialogueContext.java`:

```java
package com.varlamoreuim.dialogue;

/**
 * Runtime facts a script may query. Implemented by the plugin against the
 * live client and by tests with stubs.
 */
public interface DialogueContext
{
	boolean hasDizanasQuiver();

	String playerName();
}
```

`PlayerLine.java`:

```java
package com.varlamoreuim.dialogue;

import lombok.Value;

/** A line spoken by the local player. */
@Value
public class PlayerLine implements DialoguePage
{
	String text;
	String next;
}
```

`NpcLine.java`:

```java
package com.varlamoreuim.dialogue;

import lombok.Value;

/** A line spoken by the NPC, with a chathead expression and optional effect. */
@Value
public class NpcLine implements DialoguePage
{
	String text;
	Expression expression;
	DialogueEffect effect;
	String next;
}
```

`Option.java`:

```java
package com.varlamoreuim.dialogue;

import lombok.Value;

import javax.annotation.Nullable;
import java.util.function.Predicate;

/**
 * One entry of an {@link Options} page. A conditional option evaluates its
 * predicate on selection and routes to {@code next} or {@code elseNext}.
 */
@Value
public class Option
{
	String label;
	String next;
	@Nullable
	Predicate<DialogueContext> condition;
	@Nullable
	String elseNext;

	public static Option of(String label, String next)
	{
		return new Option(label, next, null, null);
	}

	public static Option conditional(String label, Predicate<DialogueContext> condition, String next, String elseNext)
	{
		return new Option(label, next, condition, elseNext);
	}

	public String resolve(DialogueContext ctx)
	{
		if (condition == null)
		{
			return next;
		}
		return condition.test(ctx) ? next : elseNext;
	}
}
```

`Options.java`:

```java
package com.varlamoreuim.dialogue;

import lombok.Value;

import java.util.List;

/** A "Select an option" page. */
@Value
public class Options implements DialoguePage
{
	String title;
	List<Option> options;
}
```

- [ ] **Step 8: Implement DialogueScript with builder, validation and expand**

`DialogueScript.java`:

```java
package com.varlamoreuim.dialogue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * An immutable graph of dialogue pages keyed by id. Built via {@link #builder(String)},
 * which validates that every page reference resolves and splits speech pages that
 * exceed the chatbox height into a chain of pages ("id", "id#1", "id#2", ...).
 */
public final class DialogueScript
{
	public static final String END = "END";

	private final String startId;
	private final Map<String, DialoguePage> pages;

	private DialogueScript(String startId, Map<String, DialoguePage> pages)
	{
		this.startId = startId;
		this.pages = Collections.unmodifiableMap(pages);
	}

	public static Builder builder(String startId)
	{
		return new Builder(startId);
	}

	public String getStartId()
	{
		return startId;
	}

	public DialoguePage page(String id)
	{
		return pages.get(id);
	}

	public Map<String, DialoguePage> getPages()
	{
		return pages;
	}

	public static final class Builder
	{
		private final String startId;
		private final Map<String, DialoguePage> pages = new LinkedHashMap<>();

		private Builder(String startId)
		{
			this.startId = startId;
		}

		public Builder player(String id, String text, String next)
		{
			pages.put(id, new PlayerLine(text, next));
			return this;
		}

		public Builder npc(String id, String text, String next)
		{
			return npc(id, text, Expression.DEFAULT, DialogueEffect.NONE, next);
		}

		public Builder npc(String id, String text, Expression expression, DialogueEffect effect, String next)
		{
			pages.put(id, new NpcLine(text, expression, effect, next));
			return this;
		}

		public Builder options(String id, String title, Option... options)
		{
			pages.put(id, new Options(title, List.of(options)));
			return this;
		}

		public DialogueScript build()
		{
			Map<String, DialoguePage> expanded = expand(pages);
			validate(startId, expanded);
			return new DialogueScript(startId, expanded);
		}

		private static Map<String, DialoguePage> expand(Map<String, DialoguePage> source)
		{
			Map<String, DialoguePage> out = new LinkedHashMap<>();
			for (Map.Entry<String, DialoguePage> e : source.entrySet())
			{
				String id = e.getKey();
				DialoguePage page = e.getValue();
				if (page instanceof Options)
				{
					out.put(id, page);
					continue;
				}
				String text = page instanceof PlayerLine ? ((PlayerLine) page).getText() : ((NpcLine) page).getText();
				String next = page instanceof PlayerLine ? ((PlayerLine) page).getNext() : ((NpcLine) page).getNext();
				List<String> chunks = chunk(text);
				for (int i = 0; i < chunks.size(); i++)
				{
					boolean last = i == chunks.size() - 1;
					String chunkId = i == 0 ? id : id + "#" + i;
					String chunkNext = last ? next : id + "#" + (i + 1);
					if (page instanceof PlayerLine)
					{
						out.put(chunkId, new PlayerLine(chunks.get(i), chunkNext));
					}
					else
					{
						NpcLine n = (NpcLine) page;
						DialogueEffect effect = last ? n.getEffect() : DialogueEffect.NONE;
						out.put(chunkId, new NpcLine(chunks.get(i), n.getExpression(), effect, chunkNext));
					}
				}
			}
			return out;
		}

		private static List<String> chunk(String text)
		{
			List<String> lines = DialogueText.wrap(text, DialogueText.MAX_CHARS_PER_LINE);
			List<String> chunks = new ArrayList<>();
			for (int i = 0; i < lines.size(); i += DialogueText.MAX_LINES_PER_PAGE)
			{
				int end = Math.min(lines.size(), i + DialogueText.MAX_LINES_PER_PAGE);
				chunks.add(String.join(" ", lines.subList(i, end)));
			}
			if (chunks.isEmpty())
			{
				chunks.add("");
			}
			return chunks;
		}

		private static void validate(String startId, Map<String, DialoguePage> pages)
		{
			if (!pages.containsKey(startId))
			{
				throw new IllegalStateException("Start page '" + startId + "' does not exist");
			}
			for (Map.Entry<String, DialoguePage> e : pages.entrySet())
			{
				DialoguePage p = e.getValue();
				if (p instanceof PlayerLine)
				{
					require(pages, e.getKey(), ((PlayerLine) p).getNext());
				}
				else if (p instanceof NpcLine)
				{
					require(pages, e.getKey(), ((NpcLine) p).getNext());
				}
				else if (p instanceof Options)
				{
					for (Option o : ((Options) p).getOptions())
					{
						require(pages, e.getKey(), o.getNext());
						if (o.getCondition() != null)
						{
							require(pages, e.getKey(), o.getElseNext());
						}
					}
				}
			}
		}

		private static void require(Map<String, DialoguePage> pages, String from, String target)
		{
			if (target == null || (!END.equals(target) && !pages.containsKey(target)))
			{
				throw new IllegalStateException("Page '" + from + "' references missing page '" + target + "'");
			}
		}
	}
}
```

- [ ] **Step 9: Run both dialogue tests**

Run: `./gradlew test --tests 'com.varlamoreuim.dialogue.*'`
Expected: PASS, 11 tests.

- [ ] **Step 10: Commit**

```bash
git add src/main/java/com/varlamoreuim/dialogue src/test/java/com/varlamoreuim/dialogue
git commit -m "feat(dialogue): script model, builder, validation and word wrap"
```

---

### Task 2: Config toggles

**Files:**
- Modify: `src/main/java/com/varlamoreuim/VarlamoreUimConfig.java` (after `blockNpcTransport`, before the QoA section)

**Interfaces:**
- Produces: `config.walkToStandIns()` and `config.nativeDialogue()`, both `boolean`, default `true`.

- [ ] **Step 1: Add the two items**

Insert after the `blockNpcTransport()` method:

```java
	@ConfigItem(
		keyName = "walkToStandIns",
		name = "Walk to dock NPCs",
		description = "Talk-to on a dock NPC walks you over before the conversation starts",
		position = 8,
		section = restrictionsSection
	)
	default boolean walkToStandIns()
	{
		return true;
	}

	@ConfigItem(
		keyName = "nativeDialogue",
		name = "Native dialogue",
		description = "Show dock NPC conversations in the chatbox dialogue window. Off falls back to a chat message.",
		position = 9,
		section = restrictionsSection
	)
	default boolean nativeDialogue()
	{
		return true;
	}
```

- [ ] **Step 2: Build**

Run: `./gradlew build -q`
Expected: success.

- [ ] **Step 3: Commit**

```bash
git add src/main/java/com/varlamoreuim/VarlamoreUimConfig.java
git commit -m "feat(config): walk-to and native dialogue toggles"
```

---

### Task 3: Dialogue renderer and manager

**Files:**
- Create: `src/main/java/com/varlamoreuim/dialogue/Speaker.java`
- Create: `src/main/java/com/varlamoreuim/dialogue/DialogueLayout.java`
- Create: `src/main/java/com/varlamoreuim/dialogue/DialogueInput.java`
- Create: `src/main/java/com/varlamoreuim/dialogue/DialogueManager.java`
- Modify: `src/main/java/com/varlamoreuim/VarlamoreUimPlugin.java` (temporary debug command, removed in Task 9)

**Interfaces:**
- Consumes: `DialogueScript`, `DialoguePage` types, `Expression`, `DialogueEffect`, `DialogueContext` from Task 1.
- Produces: `Speaker(String name, int npcId)`. `DialogueManager` constructor `(Client, ClientThread, ChatboxPanelManager)`, methods `open(DialogueScript, Speaker, DialogueContext, Consumer<DialogueEffect>)`, `close()`, `isOpen()`, `onGameTick()`.

Reference: RuneLite's `ChatboxTextMenuInput` builds children on `chatboxPanelManager.getContainerWidget()` with `createChild(-1, WidgetType.TEXT)`, `FontID.QUILL_8`, `setAction(0, "Continue")`, `setOnOpListener`, `setHasListener(true)`, `revalidate()`. The manager calls `deleteAllChildren()` on the container before `open()` and runs `MESSAGE_LAYER_OPEN`. `close()` on the manager runs `MESSAGE_LAYER_CLOSE` and then calls our `close()`.

- [ ] **Step 1: Speaker and layout constants**

`Speaker.java`:

```java
package com.varlamoreuim.dialogue;

import lombok.Value;

/** Who is talking on an NPC page: display name and NPC definition id for the chathead. */
@Value
public class Speaker
{
	String name;
	int npcId;
}
```

`DialogueLayout.java`. Defaults are a starting point; Step 6 calibrates them against the real dialogue widget.

```java
package com.varlamoreuim.dialogue;

/**
 * Pixel layout for the dialogue panel, mirroring the game's NPC (ChatLeft, 231)
 * and player (ChatRight, 217) dialogue interfaces. Calibrated in-game.
 */
final class DialogueLayout
{
	static final int HEAD_SIZE = 96;
	static final int HEAD_MARGIN_X = 16;
	static final int HEAD_Y = 8;
	static final int HEAD_ZOOM = 1500;
	static final int HEAD_ROTATION_X = 0;
	static final int HEAD_ROTATION_Y = 0;
	static final int HEAD_ROTATION_Z = 0;

	static final int NAME_Y = 16;
	static final int TEXT_Y = 44;
	static final int LINE_HEIGHT = 16;
	static final int CONTINUE_BOTTOM_MARGIN = 20;

	static final int COLOR_NAME = 0x8B0000;
	static final int COLOR_TEXT = 0x000000;
	static final int COLOR_CONTINUE = 0x0000FF;
	static final int COLOR_HOVER = 0xFFFFFF;

	private DialogueLayout()
	{
	}
}
```

- [ ] **Step 2: DialogueInput**

`DialogueInput.java`:

```java
package com.varlamoreuim.dialogue;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.FontID;
import net.runelite.api.widgets.JavaScriptCallback;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetModelType;
import net.runelite.api.widgets.WidgetPositionMode;
import net.runelite.api.widgets.WidgetSizeMode;
import net.runelite.api.widgets.WidgetTextAlignment;
import net.runelite.api.widgets.WidgetType;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.chatbox.ChatboxInput;
import net.runelite.client.game.chatbox.ChatboxPanelManager;
import net.runelite.client.input.KeyListener;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.function.Consumer;

/**
 * Renders a {@link DialogueScript} on the chatbox message layer, one page at a time,
 * and advances on space, number keys, or clicks on the continue/option widgets.
 * All widget work runs on the client thread.
 */
@Slf4j
public class DialogueInput extends ChatboxInput implements KeyListener
{
	private final Client client;
	private final ClientThread clientThread;
	private final ChatboxPanelManager chatboxPanelManager;
	private final DialogueScript script;
	private final Speaker speaker;
	private final DialogueContext context;
	private final Consumer<DialogueEffect> effects;
	private final Runnable onClosed;

	private String currentId;
	private DialoguePage currentPage;
	private Widget headWidget;

	public DialogueInput(Client client, ClientThread clientThread, ChatboxPanelManager chatboxPanelManager,
		DialogueScript script, Speaker speaker, DialogueContext context,
		Consumer<DialogueEffect> effects, Runnable onClosed)
	{
		this.client = client;
		this.clientThread = clientThread;
		this.chatboxPanelManager = chatboxPanelManager;
		this.script = script;
		this.speaker = speaker;
		this.context = context;
		this.effects = effects;
		this.onClosed = onClosed;
	}

	@Override
	protected void open()
	{
		show(script.getStartId());
	}

	@Override
	protected void close()
	{
		onClosed.run();
	}

	private void show(String id)
	{
		if (DialogueScript.END.equals(id))
		{
			chatboxPanelManager.close();
			return;
		}
		DialoguePage page = script.page(id);
		if (page == null)
		{
			log.warn("Dialogue page '{}' missing, closing", id);
			chatboxPanelManager.close();
			return;
		}
		currentId = id;
		currentPage = page;
		render(page);
		if (page instanceof NpcLine)
		{
			DialogueEffect effect = ((NpcLine) page).getEffect();
			if (effect != DialogueEffect.NONE)
			{
				effects.accept(effect);
			}
		}
	}

	private void render(DialoguePage page)
	{
		Widget container = chatboxPanelManager.getContainerWidget();
		if (container == null)
		{
			log.warn("Chatbox container missing, closing dialogue");
			chatboxPanelManager.close();
			return;
		}
		container.deleteAllChildren();
		headWidget = null;

		if (page instanceof Options)
		{
			renderOptions(container, (Options) page);
		}
		else if (page instanceof PlayerLine)
		{
			PlayerLine p = (PlayerLine) page;
			renderSpeech(container, context.playerName(), p.getText(), true, Expression.DEFAULT, -1);
		}
		else
		{
			NpcLine n = (NpcLine) page;
			renderSpeech(container, speaker.getName(), n.getText(), false, n.getExpression(), speaker.getNpcId());
		}
	}

	private void renderSpeech(Widget container, String name, String text, boolean player, Expression expression, int npcId)
	{
		int width = container.getWidth();
		int headX = player ? width - DialogueLayout.HEAD_MARGIN_X - DialogueLayout.HEAD_SIZE : DialogueLayout.HEAD_MARGIN_X;

		Widget head = container.createChild(-1, WidgetType.MODEL);
		head.setModelType(player ? WidgetModelType.LOCAL_PLAYER_CHATHEAD : WidgetModelType.NPC_CHATHEAD);
		if (!player)
		{
			head.setModelId(npcId);
		}
		head.setAnimationId(expression.getTalkAnimationId());
		head.setModelZoom(DialogueLayout.HEAD_ZOOM);
		head.setRotationX(DialogueLayout.HEAD_ROTATION_X);
		head.setRotationY(DialogueLayout.HEAD_ROTATION_Y);
		head.setRotationZ(DialogueLayout.HEAD_ROTATION_Z);
		head.setOriginalX(headX);
		head.setOriginalY(DialogueLayout.HEAD_Y);
		head.setOriginalWidth(DialogueLayout.HEAD_SIZE);
		head.setOriginalHeight(DialogueLayout.HEAD_SIZE);
		head.revalidate();
		headWidget = head;

		// Text column is the space not taken by the head.
		int textX = player ? 0 : DialogueLayout.HEAD_MARGIN_X + DialogueLayout.HEAD_SIZE;
		int textWidth = width - DialogueLayout.HEAD_MARGIN_X - DialogueLayout.HEAD_SIZE;

		Widget nameWidget = container.createChild(-1, WidgetType.TEXT);
		nameWidget.setText(name);
		nameWidget.setTextColor(DialogueLayout.COLOR_NAME);
		nameWidget.setFontId(FontID.QUILL_8);
		nameWidget.setOriginalX(textX);
		nameWidget.setOriginalY(DialogueLayout.NAME_Y);
		nameWidget.setOriginalWidth(textWidth);
		nameWidget.setOriginalHeight(DialogueLayout.LINE_HEIGHT);
		nameWidget.setXTextAlignment(WidgetTextAlignment.CENTER);
		nameWidget.setYTextAlignment(WidgetTextAlignment.CENTER);
		nameWidget.revalidate();

		List<String> lines = DialogueText.wrap(text, DialogueText.MAX_CHARS_PER_LINE);
		Widget body = container.createChild(-1, WidgetType.TEXT);
		body.setText(String.join("<br>", lines));
		body.setTextColor(DialogueLayout.COLOR_TEXT);
		body.setFontId(FontID.QUILL_8);
		body.setOriginalX(textX);
		body.setOriginalY(DialogueLayout.TEXT_Y);
		body.setOriginalWidth(textWidth);
		body.setOriginalHeight(DialogueLayout.LINE_HEIGHT * DialogueText.MAX_LINES_PER_PAGE);
		body.setXTextAlignment(WidgetTextAlignment.CENTER);
		body.setYTextAlignment(WidgetTextAlignment.CENTER);
		body.setAction(0, "Continue");
		body.setOnOpListener((JavaScriptCallback) ev -> advance());
		body.setHasListener(true);
		body.revalidate();

		Widget cont = container.createChild(-1, WidgetType.TEXT);
		cont.setText("Click here to continue");
		cont.setTextColor(DialogueLayout.COLOR_CONTINUE);
		cont.setFontId(FontID.QUILL_8);
		cont.setOriginalX(textX);
		cont.setOriginalY(container.getHeight() - DialogueLayout.CONTINUE_BOTTOM_MARGIN - DialogueLayout.LINE_HEIGHT);
		cont.setOriginalWidth(textWidth);
		cont.setOriginalHeight(DialogueLayout.LINE_HEIGHT);
		cont.setXTextAlignment(WidgetTextAlignment.CENTER);
		cont.setYTextAlignment(WidgetTextAlignment.CENTER);
		cont.setAction(0, "Continue");
		cont.setOnOpListener((JavaScriptCallback) ev -> advance());
		cont.setOnMouseOverListener((JavaScriptCallback) ev -> cont.setTextColor(DialogueLayout.COLOR_HOVER));
		cont.setOnMouseLeaveListener((JavaScriptCallback) ev -> cont.setTextColor(DialogueLayout.COLOR_CONTINUE));
		cont.setHasListener(true);
		cont.revalidate();
	}

	private void renderOptions(Widget container, Options page)
	{
		Widget title = container.createChild(-1, WidgetType.TEXT);
		title.setText(page.getTitle());
		title.setTextColor(DialogueLayout.COLOR_NAME);
		title.setFontId(FontID.QUILL_8);
		title.setXPositionMode(WidgetPositionMode.ABSOLUTE_CENTER);
		title.setOriginalX(0);
		title.setOriginalY(8);
		title.setOriginalHeight(24);
		title.setXTextAlignment(WidgetTextAlignment.CENTER);
		title.setYTextAlignment(WidgetTextAlignment.CENTER);
		title.setWidthMode(WidgetSizeMode.MINUS);
		title.revalidate();

		List<Option> options = page.getOptions();
		int y = title.getOriginalY() + title.getHeight() + 6;
		int height = container.getHeight() - y - 8;
		int step = height / options.size();
		int maxStep = options.size() >= 3 ? 25 : 30;
		if (step > maxStep)
		{
			int ds = step - maxStep;
			step = maxStep;
			y += (ds * options.size()) / 2;
		}
		for (Option option : options)
		{
			Widget w = container.createChild(-1, WidgetType.TEXT);
			w.setText(option.getLabel());
			w.setTextColor(DialogueLayout.COLOR_TEXT);
			w.setFontId(FontID.QUILL_8);
			w.setXPositionMode(WidgetPositionMode.ABSOLUTE_CENTER);
			w.setOriginalX(0);
			w.setOriginalY(y);
			w.setOriginalHeight(24);
			w.setXTextAlignment(WidgetTextAlignment.CENTER);
			w.setYTextAlignment(WidgetTextAlignment.CENTER);
			w.setWidthMode(WidgetSizeMode.MINUS);
			w.setAction(0, "Continue");
			w.setOnOpListener((JavaScriptCallback) ev -> select(option));
			w.setOnMouseOverListener((JavaScriptCallback) ev -> w.setTextColor(DialogueLayout.COLOR_HOVER));
			w.setOnMouseLeaveListener((JavaScriptCallback) ev -> w.setTextColor(DialogueLayout.COLOR_TEXT));
			w.setHasListener(true);
			w.revalidate();
			y += step;
		}
	}

	private void advance()
	{
		if (currentPage instanceof PlayerLine)
		{
			show(((PlayerLine) currentPage).getNext());
		}
		else if (currentPage instanceof NpcLine)
		{
			show(((NpcLine) currentPage).getNext());
		}
	}

	private void select(Option option)
	{
		show(option.resolve(context));
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		if (!chatboxPanelManager.shouldTakeInput())
		{
			return;
		}
		char c = e.getKeyChar();
		if (c == '\033')
		{
			e.consume();
			clientThread.invokeLater(chatboxPanelManager::close);
			return;
		}
		if (currentPage instanceof Options)
		{
			int n = c - '1';
			List<Option> options = ((Options) currentPage).getOptions();
			if (n >= 0 && n < options.size())
			{
				e.consume();
				Option option = options.get(n);
				clientThread.invokeLater(() -> select(option));
			}
			return;
		}
		if (c == ' ')
		{
			e.consume();
			clientThread.invokeLater(this::advance);
		}
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (!chatboxPanelManager.shouldTakeInput())
		{
			return;
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			e.consume();
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
	}
}
```

- [ ] **Step 3: DialogueManager**

`DialogueManager.java`:

```java
package com.varlamoreuim.dialogue;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.chatbox.ChatboxPanelManager;

import java.util.function.Consumer;

/**
 * Opens and closes dialogues on the chatbox. Holds at most one open dialogue,
 * closes it when the player moves, and forwards page effects to the caller.
 */
@Slf4j
public class DialogueManager
{
	private final Client client;
	private final ClientThread clientThread;
	private final ChatboxPanelManager chatboxPanelManager;

	private DialogueInput current;
	private WorldPoint openedAt;

	public DialogueManager(Client client, ClientThread clientThread, ChatboxPanelManager chatboxPanelManager)
	{
		this.client = client;
		this.clientThread = clientThread;
		this.chatboxPanelManager = chatboxPanelManager;
	}

	public boolean isOpen()
	{
		return current != null && chatboxPanelManager.getCurrentInput() == current;
	}

	public void open(DialogueScript script, Speaker speaker, DialogueContext context, Consumer<DialogueEffect> effects)
	{
		if (isOpen())
		{
			return;
		}
		Player player = client.getLocalPlayer();
		openedAt = player != null ? player.getWorldLocation() : null;
		DialogueInput input = new DialogueInput(client, clientThread, chatboxPanelManager,
			script, speaker, context, effects, this::onClosed);
		current = input;
		chatboxPanelManager.openInput(input);
		log.debug("Opened dialogue with {}", speaker.getName());
	}

	public void close()
	{
		if (isOpen())
		{
			chatboxPanelManager.close();
		}
	}

	/** Call from the plugin's GameTick. Closes the dialogue if the player has moved. */
	public void onGameTick()
	{
		if (!isOpen() || openedAt == null)
		{
			return;
		}
		Player player = client.getLocalPlayer();
		if (player != null && !openedAt.equals(player.getWorldLocation()))
		{
			close();
		}
	}

	private void onClosed()
	{
		current = null;
		openedAt = null;
	}
}
```

- [ ] **Step 4: Build**

Run: `./gradlew build -q`
Expected: success. If `Widget.setOnMouseOverListener` or `setOnMouseLeaveListener` are missing in 1.12.38, drop those two calls; hover colour is cosmetic.

- [ ] **Step 5: Temporary debug command for in-game testing**

In `VarlamoreUimPlugin`, add an injected `ChatCommandManager chatCommandManager` and `ChatboxPanelManager chatboxPanelManager`, a `DialogueManager dialogueManager` field, and in `startUp()`:

```java
		dialogueManager = new DialogueManager(client, clientThread, chatboxPanelManager);
		chatCommandManager.registerCommand("::vuimtalk", (chatMessage, message) ->
		{
			DialogueScript s = DialogueScript.builder("p")
				.player("p", "Any ships sailing today?", "n")
				.npc("n", "Ships? No, no. Not today. It's the tides, you see. Terrible tides. Absolutely dreadful tides, the worst tides anyone has seen in years.", "m")
				.options("m", "Select an option",
					Option.of("Why not?", "why"),
					Option.of("Never mind.", DialogueScript.END))
				.npc("why", "Tides.", Expression.ANGRY, DialogueEffect.NONE, DialogueScript.END)
				.build();
			dialogueManager.open(s, new Speaker("Mysterious Old Man", 2830), new DialogueContext()
			{
				@Override
				public boolean hasDizanasQuiver()
				{
					return false;
				}

				@Override
				public String playerName()
				{
					return client.getLocalPlayer() != null ? client.getLocalPlayer().getName() : "You";
				}
			}, effect -> log.debug("effect {}", effect));
		});
```

In `shutDown()` add `chatCommandManager.unregisterCommand("::vuimtalk");`. In `onGameTick` add `if (dialogueManager != null) dialogueManager.onGameTick();`.

Imports: `com.varlamoreuim.dialogue.*`, `net.runelite.client.chat.ChatCommandManager`, `net.runelite.client.game.chatbox.ChatboxPanelManager`.

- [ ] **Step 6: Calibrate layout in-game**

Add a temporary `@Subscribe onWidgetLoaded(WidgetLoaded e)` in the plugin: when `e.getGroupId() == 231`, on the next client tick log every field of `client.getWidget(InterfaceID.ChatLeft.HEAD)`, `NAME`, `TEXT`, `CONTINUE`: `getOriginalX/Y`, `getWidth/Height`, `getModelZoom`, `getRotationX/Y/Z`, `getAnimationId`, `getFontId`, `getTextColor`. Do the same for group 217 with `InterfaceID.ChatRight`.

Run `./gradlew run`, log in, talk to any NPC, then run the command `::vuimtalk`. Compare and:

1. Copy the real head widget's x, y, width, height, zoom, rotation into `DialogueLayout`.
2. Copy the real name, text and continue y positions and colours.
3. Note the head `getAnimationId()` while the NPC is talking and after the text finishes; set `Expression` talk/still ids to match.
4. Adjust `DialogueText.MAX_CHARS_PER_LINE` until a full line in `::vuimtalk` fills the text column without clipping.

Remove the `onWidgetLoaded` logger when done. Keep `::vuimtalk` until Task 9.

- [ ] **Step 7: Verify behaviour in-game**

With `::vuimtalk` open: space advances, the long NPC line spans two pages, options respond to keys 1 and 2 and to clicks, Escape closes, walking a tile closes it, opening the bank closes it cleanly.

- [ ] **Step 8: Commit**

```bash
git add src/main/java/com/varlamoreuim/dialogue src/main/java/com/varlamoreuim/VarlamoreUimPlugin.java
git commit -m "feat(dialogue): chatbox renderer, manager and calibrated layout"
```

---

### Task 4: Dock, WalkTarget, PendingTalk

**Files:**
- Create: `src/main/java/com/varlamoreuim/standin/Dock.java`
- Create: `src/main/java/com/varlamoreuim/standin/WalkTarget.java`
- Create: `src/main/java/com/varlamoreuim/standin/PendingTalk.java`
- Test: `src/test/java/com/varlamoreuim/standin/DockTest.java`
- Test: `src/test/java/com/varlamoreuim/standin/WalkTargetTest.java`
- Test: `src/test/java/com/varlamoreuim/standin/PendingTalkTest.java`

**Interfaces:**
- Produces: `Dock.nearest(WorldPoint) -> Optional<Dock>`, `Dock.getAnchor()`. `WalkTarget.adjacentTile(WorldPoint npc, WorldPoint player) -> WorldPoint`. `PendingTalk(int npcIndex, int deadlineTick)`, `isExpired(int tick)`. `PendingTalk.TIMEOUT_TICKS = 15`.

- [ ] **Step 1: Write failing tests**

`DockTest.java`:

```java
package com.varlamoreuim.standin;

import net.runelite.api.coords.WorldPoint;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class DockTest
{
	@Test
	public void nearestPicksDockWithinRange()
	{
		assertEquals(Optional.of(Dock.SUNSET_COAST), Dock.nearest(new WorldPoint(1516, 2973, 0)));
		assertEquals(Optional.of(Dock.ALDARIN), Dock.nearest(new WorldPoint(1450, 2970, 0)));
		assertEquals(Optional.of(Dock.FORTIS_COTHON), Dock.nearest(new WorldPoint(1740, 3130, 0)));
	}

	@Test
	public void nearestIsEmptyFarAway()
	{
		assertFalse(Dock.nearest(new WorldPoint(3222, 3218, 0)).isPresent());
	}
}
```

`WalkTargetTest.java`:

```java
package com.varlamoreuim.standin;

import net.runelite.api.coords.WorldPoint;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class WalkTargetTest
{
	private static final WorldPoint NPC = new WorldPoint(1500, 3000, 0);

	@Test
	public void picksNeighbourFacingThePlayer()
	{
		assertEquals(new WorldPoint(1499, 3000, 0), WalkTarget.adjacentTile(NPC, new WorldPoint(1490, 3000, 0)));
		assertEquals(new WorldPoint(1500, 3001, 0), WalkTarget.adjacentTile(NPC, new WorldPoint(1500, 3010, 0)));
		assertEquals(new WorldPoint(1501, 3001, 0), WalkTarget.adjacentTile(NPC, new WorldPoint(1505, 3005, 0)));
	}

	@Test
	public void neverReturnsNpcTile()
	{
		WorldPoint t = WalkTarget.adjacentTile(NPC, NPC);
		assertNotEquals(NPC, t);
		assertEquals(1, t.distanceTo(NPC));
	}
}
```

`PendingTalkTest.java`:

```java
package com.varlamoreuim.standin;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PendingTalkTest
{
	@Test
	public void expiresAfterDeadline()
	{
		PendingTalk p = new PendingTalk(7, 100);
		assertFalse(p.isExpired(99));
		assertFalse(p.isExpired(100));
		assertTrue(p.isExpired(101));
	}
}
```

- [ ] **Step 2: Run to verify they fail**

Run: `./gradlew test --tests 'com.varlamoreuim.standin.*'`
Expected: compilation FAIL.

- [ ] **Step 3: Implement**

`Dock.java`:

```java
package com.varlamoreuim.standin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.coords.WorldPoint;

import java.util.Optional;

/**
 * The three Varlamore charter ship docks. Anchors identify which dock a charter
 * crewmember belongs to; they are not spawn positions.
 */
@Getter
@RequiredArgsConstructor
public enum Dock
{
	SUNSET_COAST(new WorldPoint(1514, 2971, 0)),
	ALDARIN(new WorldPoint(1455, 2968, 0)),
	FORTIS_COTHON(new WorldPoint(1743, 3136, 0));

	public static final int MAX_DISTANCE = 20;

	private final WorldPoint anchor;

	public static Optional<Dock> nearest(WorldPoint point)
	{
		Dock best = null;
		int bestDistance = Integer.MAX_VALUE;
		for (Dock d : values())
		{
			int distance = d.anchor.distanceTo2D(point);
			if (distance < bestDistance)
			{
				bestDistance = distance;
				best = d;
			}
		}
		return bestDistance <= MAX_DISTANCE ? Optional.of(best) : Optional.empty();
	}
}
```

`WalkTarget.java`:

```java
package com.varlamoreuim.standin;

import net.runelite.api.coords.WorldPoint;

/**
 * Chooses the tile next to an NPC that the player should walk to before talking:
 * the neighbour (8-way) with the smallest distance to the player.
 */
public final class WalkTarget
{
	private static final int[][] OFFSETS = {
		{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
	};

	private WalkTarget()
	{
	}

	public static WorldPoint adjacentTile(WorldPoint npc, WorldPoint player)
	{
		WorldPoint best = null;
		int bestDistance = Integer.MAX_VALUE;
		for (int[] o : OFFSETS)
		{
			WorldPoint candidate = new WorldPoint(npc.getX() + o[0], npc.getY() + o[1], npc.getPlane());
			int dx = candidate.getX() - player.getX();
			int dy = candidate.getY() - player.getY();
			int distance = dx * dx + dy * dy;
			if (distance < bestDistance)
			{
				bestDistance = distance;
				best = candidate;
			}
		}
		return best;
	}
}
```

`PendingTalk.java`:

```java
package com.varlamoreuim.standin;

import lombok.Value;

/** A Talk-to click waiting for the player to arrive next to the stand-in. */
@Value
public class PendingTalk
{
	public static final int TIMEOUT_TICKS = 15;

	int npcIndex;
	int deadlineTick;

	public boolean isExpired(int tick)
	{
		return tick > deadlineTick;
	}
}
```

- [ ] **Step 4: Run tests**

Run: `./gradlew test --tests 'com.varlamoreuim.standin.*'`
Expected: PASS, 5 tests.

- [ ] **Step 5: Commit**

```bash
git add src/main/java/com/varlamoreuim/standin src/test/java/com/varlamoreuim/standin
git commit -m "feat(standin): dock lookup, walk target and pending talk"
```

---

### Task 5: Persona roster with all six scripts

**Files:**
- Create: `src/main/java/com/varlamoreuim/standin/Persona.java`
- Create: `src/main/java/com/varlamoreuim/standin/PersonaRoster.java`
- Test: `src/test/java/com/varlamoreuim/standin/PersonaRosterTest.java`

**Interfaces:**
- Consumes: `DialogueScript`, `Option`, `Expression`, `DialogueEffect`, `DialogueContext` (Task 1), `Dock` (Task 4).
- Produces: `Persona(String id, String displayName, int npcId, String examine, DialogueScript script)`. `PersonaRoster.get(Dock, int crewmemberNpcId) -> Optional<Persona>`. `PersonaRoster.all() -> Collection<Persona>`. `PersonaRoster.FALLBACK_NPC_ID = 2830`. `PersonaRoster.CHARTER_NPC_IDS`.

- [ ] **Step 1: Write failing test**

`PersonaRosterTest.java`:

```java
package com.varlamoreuim.standin;

import com.varlamoreuim.dialogue.DialogueContext;
import com.varlamoreuim.dialogue.DialogueEffect;
import com.varlamoreuim.dialogue.DialoguePage;
import com.varlamoreuim.dialogue.DialogueScript;
import com.varlamoreuim.dialogue.NpcLine;
import com.varlamoreuim.dialogue.Option;
import com.varlamoreuim.dialogue.Options;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PersonaRosterTest
{
	private static final DialogueContext HAS_QUIVER = new DialogueContext()
	{
		@Override
		public boolean hasDizanasQuiver()
		{
			return true;
		}

		@Override
		public String playerName()
		{
			return "T";
		}
	};

	@Test
	public void everyDockHasBothCrewmembers()
	{
		assertTrue(PersonaRoster.get(Dock.SUNSET_COAST, 9314).isPresent());
		assertTrue(PersonaRoster.get(Dock.SUNSET_COAST, 9350).isPresent());
		assertTrue(PersonaRoster.get(Dock.ALDARIN, 9314).isPresent());
		assertTrue(PersonaRoster.get(Dock.ALDARIN, 9350).isPresent());
		assertTrue(PersonaRoster.get(Dock.FORTIS_COTHON, 9326).isPresent());
		assertTrue(PersonaRoster.get(Dock.FORTIS_COTHON, 9362).isPresent());
		assertEquals(6, PersonaRoster.all().size());
	}

	@Test
	public void personasAreDistinct()
	{
		Set<String> names = new HashSet<>();
		Set<Integer> npcIds = new HashSet<>();
		for (Persona p : PersonaRoster.all())
		{
			names.add(p.getDisplayName());
			npcIds.add(p.getNpcId());
		}
		assertEquals(6, names.size());
		assertEquals(6, npcIds.size());
	}

	@Test
	public void everyScriptHasQuiverBranchThatUnlocks()
	{
		for (Persona p : PersonaRoster.all())
		{
			DialogueScript s = p.getScript();
			boolean foundUnlock = false;
			boolean foundConditional = false;
			for (DialoguePage page : s.getPages().values())
			{
				if (page instanceof NpcLine && ((NpcLine) page).getEffect() == DialogueEffect.UNLOCK_CHARTER)
				{
					foundUnlock = true;
				}
				if (page instanceof Options)
				{
					for (Option o : ((Options) page).getOptions())
					{
						if (o.getCondition() != null)
						{
							foundConditional = true;
							DialoguePage target = s.page(o.resolve(HAS_QUIVER));
							assertTrue(p.getId(), target instanceof NpcLine);
							assertEquals(p.getId(), DialogueEffect.UNLOCK_CHARTER, ((NpcLine) target).getEffect());
						}
					}
				}
			}
			assertTrue(p.getId() + " lacks unlock page", foundUnlock);
			assertTrue(p.getId() + " lacks quiver option", foundConditional);
		}
	}
}
```

- [ ] **Step 2: Run to verify it fails**

Run: `./gradlew test --tests 'com.varlamoreuim.standin.PersonaRosterTest'`
Expected: compilation FAIL.

- [ ] **Step 3: Implement Persona and PersonaRoster**

`Persona.java`:

```java
package com.varlamoreuim.standin;

import com.varlamoreuim.dialogue.DialogueScript;
import lombok.Value;

/** A lore character that stands in for a hidden charter crewmember. */
@Value
public class Persona
{
	String id;
	String displayName;
	int npcId;
	String examine;
	DialogueScript script;
}
```

`PersonaRoster.java`. NPC ids from the OSRS wiki, all with chatheads: Mysterious Old Man 2830, Fisher 13252, Vineyard foreman 13908, Citizen (Aldarin) 13883, Guard (Varlamore) 13100, Sailor (Varlamore) 13248.

```java
package com.varlamoreuim.standin;

import com.varlamoreuim.dialogue.DialogueContext;
import com.varlamoreuim.dialogue.DialogueEffect;
import com.varlamoreuim.dialogue.DialogueScript;
import com.varlamoreuim.dialogue.Expression;
import com.varlamoreuim.dialogue.Option;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * The six dock personas keyed by dock and crewmember NPC id, with their scripts.
 * Shared premise: the Kingdom closed the sea lanes to all but Colosseum champions,
 * and Dizana's quiver is the champion's token.
 */
public final class PersonaRoster
{
	/** Trader Crewmember ids at the three docks (Sunset Coast and Aldarin share ids). */
	public static final Set<Integer> CHARTER_NPC_IDS = Set.of(9314, 9326, 9350, 9362);

	/** Mysterious Old Man, used when a persona's NPC data cannot be loaded. */
	public static final int FALLBACK_NPC_ID = 2830;

	private static final String OPENER = "Any ships sailing today?";
	private static final String MENU_TITLE = "Select an option";

	private static final Map<String, Persona> BY_KEY = new LinkedHashMap<>();

	static
	{
		put(Dock.SUNSET_COAST, 9314, oldMan());
		put(Dock.SUNSET_COAST, 9350, fisher());
		put(Dock.ALDARIN, 9314, vintner());
		put(Dock.ALDARIN, 9350, pilgrim());
		put(Dock.FORTIS_COTHON, 9326, guard());
		put(Dock.FORTIS_COTHON, 9362, harbourmaster());
	}

	private PersonaRoster()
	{
	}

	public static Optional<Persona> get(Dock dock, int crewmemberNpcId)
	{
		return Optional.ofNullable(BY_KEY.get(key(dock, crewmemberNpcId)));
	}

	public static Collection<Persona> all()
	{
		return Collections.unmodifiableCollection(BY_KEY.values());
	}

	private static void put(Dock dock, int npcId, Persona persona)
	{
		BY_KEY.put(key(dock, npcId), persona);
	}

	private static String key(Dock dock, int npcId)
	{
		return dock.name() + ":" + npcId;
	}

	/**
	 * Common skeleton: opener, reply, menu with Why not / What's the quiver /
	 * I have one / Never mind. Callers pass the persona-specific lines.
	 */
	private static DialogueScript script(String reply, String why1, String why2, String quiver,
		String haveNo, String haveYes, String bye)
	{
		DialogueScript.Builder b = DialogueScript.builder("open")
			.player("open", OPENER, "reply")
			.npc("reply", reply, "menu")
			.options("menu", MENU_TITLE,
				Option.of("Why not?", "why1"),
				Option.of("What's Dizana's quiver?", "quiver"),
				Option.conditional("I have one right here!", DialogueContext::hasDizanasQuiver, "haveYes", "haveNo"),
				Option.of("Never mind.", "bye"))
			.npc("why1", why1, why2 == null ? "menu" : "why2");
		if (why2 != null)
		{
			b.npc("why2", why2, "menu");
		}
		return b
			.npc("quiver", quiver, "menu")
			.npc("haveNo", haveNo, Expression.LAUGH, DialogueEffect.NONE, "menu")
			.npc("haveYes", haveYes, Expression.HAPPY, DialogueEffect.UNLOCK_CHARTER, DialogueScript.END)
			.npc("bye", bye, DialogueScript.END)
			.build();
	}

	private static Persona oldMan()
	{
		return new Persona("old_man", "Mysterious Old Man", 2830,
			"Definitely not the random event one. Definitely.",
			script(
				"Ships? No, no. Not today. It's the tides, you see. Terrible tides.",
				"Well, if it isn't the tides it's the moon. And if it isn't the moon, the harbourmaster may have confiscated my boat. Allegedly.",
				"Anyway, the sea lanes are closed to anyone who isn't a champion. Kingdom's orders. Nothing to do with me.",
				"Dizana's quiver, from the Fortis Colosseum. Win it and every captain from here to Fortis will fall over themselves to take you aboard. I'd get one myself, but I'm between adventures.",
				"Do you now? Then what's that quiver-shaped patch of nothing on your back?",
				"Well I'll be. A proper champion! Ignore me then, the crew's just over there. Always was.",
				"Suit yourself. I'll be here. Not for any particular reason. Definitely not waiting for someone."));
	}

	private static Persona fisher()
	{
		return new Persona("fisher", "Fisher", 13252,
			"Smells like fish and unfulfilled ambition.",
			script(
				"Sailing? Nothing's sailed from here since the Kingdom closed the lanes. Even the fish left.",
				"Kingdom's decree. No champion, no passage. The fish don't care about decrees, they just left out of spite.",
				"If you fancy disappointment with a net, the Hunter Guild's up the road.",
				"Dizana's quiver, from the Colosseum. Champions get one. Champions get boats. Fishers get told about it a lot.",
				"You've got a fishing rod and optimism. Neither one is a quiver.",
				"Blimey, an actual champion. Go on then, before the fish come back and ask for autographs.",
				"Aye. Mind the rocks."));
	}

	private static Persona vintner()
	{
		return new Persona("vintner", "Vintner", 13908,
			"Has opinions about vintages. Strong ones.",
			script(
				"Ships? I've been waiting on a barrel shipment for three weeks. Wine gets through. People don't.",
				"The Kingdom closed the lanes to everyone but champions. Apparently barrels are less likely to wander off and die.",
				"If you're stuck here, I could use a grape stomper. Pay's in grapes.",
				"Dizana's quiver. You win it in the Fortis Colosseum. Show it to any captain and they'll carry you anywhere. Even without the barrels.",
				"Then you're stomping grapes, not sailing. The vat's that way.",
				"A champion! Splendid. The crew's right there. Take a bottle for the road.",
				"Mind the vines on your way out."));
	}

	private static Persona pilgrim()
	{
		return new Persona("pilgrim", "Pilgrim", 13883,
			"Radiantly unbothered.",
			script(
				"Ralos guides ships by daylight, friend. Today Ralos has chosen not to guide yours.",
				"The Kingdom closed the sea lanes. The Kingdom says it is a decree. I say it is Ralos. We are both right.",
				"Only champions may sail. Ralos loves a champion.",
				"Dizana's quiver, earned in the Colosseum. Ralos sees it and smiles. Captains see it and lower the gangplank.",
				"Ralos sees all things. Ralos does not see a quiver.",
				"Ah! Ralos smiles upon you. And the crew, apparently. Go with the sun.",
				"Walk in the light, friend."));
	}

	private static Persona guard()
	{
		return new Persona("guard", "Fortis Guard", 13100,
			"Following orders. Enthusiastically.",
			script(
				"Halt. Harbour's closed by royal decree. No passage without a champion's token.",
				"The decree says, and I quote, 'Ultimate Ironmen keep wandering off and losing everything.'",
				"I don't know what that means, but it was underlined twice, so it's serious.",
				"Dizana's quiver. Win it in the Colosseum, then you're a champion and I salute you instead of standing in your way. It's a whole thing.",
				"I've been trained to spot a quiver, citizen. That's a back. Move along.",
				"A champion! Apologies. The crew awaits. Try not to wander off.",
				"Carry on, citizen. Stay on dry land."));
	}

	private static Persona harbourmaster()
	{
		return new Persona("harbourmaster", "Harbourmaster", 13248,
			"Loves a stamp.",
			script(
				"Passage? Certainly. I'll just need to see your form 7B.",
				"Without form 7B you are not a champion, and without being a champion you cannot sail. It's all very tidy.",
				"Form 7B is Dizana's quiver. No, you cannot fill it in. You have to win it.",
				"Dizana's quiver. Awarded at the Fortis Colosseum. Doubles as a form, a permit and a hat, if you're desperate.",
				"I see no form 7B. I see a person with hope, which is not a recognised document.",
				"Form 7B, present and correct! Welcome aboard, champion. The crew's over there. Stamp, stamp.",
				"The office is open dawn to dusk. Do come back with paperwork."));
	}
}
```

- [ ] **Step 4: Run tests**

Run: `./gradlew test --tests 'com.varlamoreuim.standin.PersonaRosterTest'`
Expected: PASS, 3 tests. Script validation runs in the static initializer, so a typo in any page id fails here.

- [ ] **Step 5: Commit**

```bash
git add src/main/java/com/varlamoreuim/standin/Persona.java src/main/java/com/varlamoreuim/standin/PersonaRoster.java src/test/java/com/varlamoreuim/standin/PersonaRosterTest.java
git commit -m "feat(standin): six dock personas with scripts"
```

---

### Task 6: StandIn puppet and registry

**Files:**
- Create: `src/main/java/com/varlamoreuim/standin/StandIn.java`
- Create: `src/main/java/com/varlamoreuim/standin/StandInRegistry.java`

**Interfaces:**
- Consumes: `Persona`, `PersonaRoster`, `Dock` (Tasks 4, 5).
- Produces: `StandIn` with `getNpc()`, `getPersona()`, `getObject()`, `sync()`, `destroy()`. `StandInRegistry(Client, ClientThread)` with `setActive(boolean)`, `bind(NPC)`, `unbind(NPC)`, `sync()`, `rescan()`, `clear()`, `active() -> Collection<StandIn>`, `byNpcIndex(int) -> Optional<StandIn>`.

- [ ] **Step 1: StandIn**

`StandIn.java`:

```java
package com.varlamoreuim.standin;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Animation;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.RuneLiteObject;
import net.runelite.api.coords.LocalPoint;

/**
 * One puppet: a RuneLiteObject wearing a persona's model, bound to a hidden
 * charter crewmember. {@link #sync()} copies the crewmember's position,
 * orientation and pose animation every client tick.
 */
@Slf4j
public class StandIn
{
	@Getter
	private final NPC npc;
	@Getter
	private final Persona persona;
	@Getter
	private final RuneLiteObject object;
	private final Client client;

	private int lastPose = -1;

	public StandIn(Client client, NPC npc, Persona persona, RuneLiteObject object)
	{
		this.client = client;
		this.npc = npc;
		this.persona = persona;
		this.object = object;
	}

	public void sync()
	{
		LocalPoint lp = npc.getLocalLocation();
		if (lp == null)
		{
			return;
		}
		object.setLocation(lp, client.getTopLevelWorldView().getPlane());
		object.setOrientation(npc.getOrientation());
		int pose = npc.getPoseAnimation();
		if (pose != lastPose && pose >= 0)
		{
			Animation anim = client.loadAnimation(pose);
			if (anim != null)
			{
				object.setAnimation(anim);
				object.setShouldLoop(true);
				lastPose = pose;
			}
		}
		if (!object.isActive())
		{
			object.setActive(true);
		}
	}

	public void destroy()
	{
		object.setActive(false);
	}
}
```

- [ ] **Step 2: StandInRegistry**

`StandInRegistry.java`. The model-building code is the existing logic from `NpcTransportBlocker.createStandInNpcs`, moved here and parameterised by NPC id.

```java
package com.varlamoreuim.standin;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Model;
import net.runelite.api.ModelData;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.RuneLiteObject;
import net.runelite.api.WorldView;
import net.runelite.client.callback.ClientThread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Owns every active {@link StandIn}. Binds a puppet when a charter crewmember
 * spawns, drops it on despawn, and mirrors positions on each client tick.
 * {@link #setActive(boolean)} switches the whole system on (rescan) or off (clear).
 */
@Slf4j
public class StandInRegistry
{
	private final Client client;
	private final ClientThread clientThread;
	private final Map<Integer, StandIn> byIndex = new LinkedHashMap<>();
	private boolean active = false;

	public StandInRegistry(Client client, ClientThread clientThread)
	{
		this.client = client;
		this.clientThread = clientThread;
	}

	public void setActive(boolean active)
	{
		if (this.active == active)
		{
			return;
		}
		this.active = active;
		if (active)
		{
			rescan();
		}
		else
		{
			clear();
		}
	}

	public boolean isActive()
	{
		return active;
	}

	public Collection<StandIn> active()
	{
		return Collections.unmodifiableCollection(byIndex.values());
	}

	public Optional<StandIn> byNpcIndex(int index)
	{
		return Optional.ofNullable(byIndex.get(index));
	}

	public void bind(NPC npc)
	{
		if (!active || npc == null || !PersonaRoster.CHARTER_NPC_IDS.contains(npc.getId()))
		{
			return;
		}
		if (byIndex.containsKey(npc.getIndex()))
		{
			return;
		}
		Optional<Dock> dock = Dock.nearest(npc.getWorldLocation());
		if (!dock.isPresent())
		{
			log.debug("Charter NPC {} at {} is not near a known dock", npc.getId(), npc.getWorldLocation());
			return;
		}
		Optional<Persona> persona = PersonaRoster.get(dock.get(), npc.getId());
		if (!persona.isPresent())
		{
			log.debug("No persona for dock {} npc {}", dock.get(), npc.getId());
			return;
		}
		clientThread.invoke(() ->
		{
			if (byIndex.containsKey(npc.getIndex()))
			{
				return;
			}
			RuneLiteObject object = client.createRuneLiteObject();
			Model model = buildModel(persona.get().getNpcId());
			if (model == null)
			{
				model = buildModel(PersonaRoster.FALLBACK_NPC_ID);
			}
			if (model == null)
			{
				log.warn("Could not build model for persona {}", persona.get().getId());
				return;
			}
			object.setModel(model);
			object.setRadius(60);
			StandIn standIn = new StandIn(client, npc, persona.get(), object);
			standIn.sync();
			byIndex.put(npc.getIndex(), standIn);
			log.debug("Bound {} to crewmember index {} at {}", persona.get().getId(), npc.getIndex(), dock.get());
		});
	}

	public void unbind(NPC npc)
	{
		StandIn s = byIndex.remove(npc.getIndex());
		if (s != null)
		{
			s.destroy();
		}
	}

	public void sync()
	{
		for (StandIn s : byIndex.values())
		{
			s.sync();
		}
	}

	public void rescan()
	{
		if (!active)
		{
			return;
		}
		WorldView wv = client.getTopLevelWorldView();
		if (wv == null)
		{
			return;
		}
		for (NPC npc : wv.npcs())
		{
			bind(npc);
		}
	}

	public void clear()
	{
		for (StandIn s : byIndex.values())
		{
			s.destroy();
		}
		byIndex.clear();
	}

	private Model buildModel(int npcId)
	{
		NPCComposition comp = client.getNpcDefinition(npcId);
		if (comp == null || comp.getModels() == null || comp.getModels().length == 0)
		{
			return null;
		}
		short[] recolFrom = comp.getColorToReplace();
		short[] recolTo = comp.getColorToReplaceWith();
		List<ModelData> parts = new ArrayList<>();
		for (int modelId : comp.getModels())
		{
			ModelData md = client.loadModelData(modelId);
			if (md == null)
			{
				continue;
			}
			md.cloneColors().cloneVertices();
			if (recolFrom != null && recolTo != null)
			{
				for (int i = 0; i < recolFrom.length; i++)
				{
					md.recolor(recolFrom[i], recolTo[i]);
				}
			}
			parts.add(md);
		}
		if (parts.isEmpty())
		{
			return null;
		}
		return client.mergeModels(parts.toArray(new ModelData[0])).light();
	}
}
```

- [ ] **Step 3: Build**

Run: `./gradlew build -q`
Expected: success. If `WorldView.npcs()` is not iterable in 1.12.38, use `for (NPC npc : wv.npcs())` replaced by iterating `wv.npcs().iterator()`; the existing `NpcTransportBlocker` uses `wv.npcs().byIndex(...)`, so the accessor exists.

- [ ] **Step 4: Commit**

```bash
git add src/main/java/com/varlamoreuim/standin/StandIn.java src/main/java/com/varlamoreuim/standin/StandInRegistry.java
git commit -m "feat(standin): puppet bound to hidden crewmember with position sync"
```

---

### Task 7: Menu injection, examine, talk-to and walk-to

**Files:**
- Create: `src/main/java/com/varlamoreuim/standin/StandInMenuInjector.java`

**Interfaces:**
- Consumes: `StandInRegistry`, `StandIn`, `WalkTarget`, `PendingTalk` (Tasks 4, 6), `DialogueManager`, `Speaker`, `DialogueContext`, `DialogueEffect` (Task 3).
- Produces: `StandInMenuInjector(Client, ChatMessageManager, DialogueManager, StandInRegistry, DialogueContext, Runnable onUnlockRequested)` with `onPostMenuSort()`, `onMenuOptionClicked(MenuOptionClicked)`, `onGameTick(int tick)`, `setWalkToEnabled(boolean)`, `setNativeDialogueEnabled(boolean)`.

- [ ] **Step 1: Implement**

```java
package com.varlamoreuim.standin;

import com.varlamoreuim.dialogue.DialogueContext;
import com.varlamoreuim.dialogue.DialogueEffect;
import com.varlamoreuim.dialogue.DialogueManager;
import com.varlamoreuim.dialogue.Speaker;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.Model;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.RuneLiteObject;
import net.runelite.api.WorldView;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;

import java.awt.Color;
import java.awt.Shape;

/**
 * Adds "Talk-to" and "Examine" menu entries when the mouse is over a stand-in's
 * projected model, handles those clicks, and implements walk-then-talk.
 */
@Slf4j
public class StandInMenuInjector
{
	private static final String TALK = "Talk-to";
	private static final String EXAMINE = "Examine";

	private final Client client;
	private final ChatMessageManager chatMessageManager;
	private final DialogueManager dialogueManager;
	private final StandInRegistry registry;
	private final DialogueContext context;
	private final Runnable onUnlockRequested;

	@Setter
	private boolean walkToEnabled = true;
	@Setter
	private boolean nativeDialogueEnabled = true;

	private PendingTalk pending;

	public StandInMenuInjector(Client client, ChatMessageManager chatMessageManager, DialogueManager dialogueManager,
		StandInRegistry registry, DialogueContext context, Runnable onUnlockRequested)
	{
		this.client = client;
		this.chatMessageManager = chatMessageManager;
		this.dialogueManager = dialogueManager;
		this.registry = registry;
		this.context = context;
		this.onUnlockRequested = onUnlockRequested;
	}

	public void onPostMenuSort()
	{
		if (registry.active().isEmpty() || client.isMenuOpen())
		{
			return;
		}
		Point mouse = client.getMouseCanvasPosition();
		if (mouse == null)
		{
			return;
		}
		WorldView wv = client.getTopLevelWorldView();
		for (StandIn s : registry.active())
		{
			if (hit(wv, s.getObject(), mouse))
			{
				String target = "<col=ffff00>" + s.getPersona().getDisplayName() + "</col>";
				int index = s.getNpc().getIndex();
				client.getMenu().createMenuEntry(-1)
					.setOption(EXAMINE)
					.setTarget(target)
					.setType(MenuAction.RUNELITE)
					.setIdentifier(index);
				client.getMenu().createMenuEntry(-1)
					.setOption(TALK)
					.setTarget(target)
					.setType(MenuAction.RUNELITE)
					.setIdentifier(index);
				return;
			}
		}
	}

	private boolean hit(WorldView wv, RuneLiteObject object, Point mouse)
	{
		try
		{
			Model model = object.getModel();
			LocalPoint lp = object.getLocation();
			if (model == null || lp == null || !object.isActive())
			{
				return false;
			}
			int z = Perspective.getTileHeight(client, lp, wv.getPlane());
			Shape clickbox = Perspective.getClickbox(client, wv, model, object.getOrientation(), lp.getX(), lp.getY(), z);
			return clickbox != null && clickbox.contains(mouse.getX(), mouse.getY());
		}
		catch (Exception e)
		{
			log.debug("clickbox projection failed", e);
			return false;
		}
	}

	/** Returns true if the click was ours. */
	public boolean onMenuOptionClicked(MenuOptionClicked event)
	{
		MenuEntry entry = event.getMenuEntry();
		if (entry.getType() != MenuAction.RUNELITE)
		{
			pending = null;
			return false;
		}
		String option = entry.getOption();
		if (!TALK.equals(option) && !EXAMINE.equals(option))
		{
			pending = null;
			return false;
		}
		StandIn s = registry.byNpcIndex(entry.getIdentifier()).orElse(null);
		if (s == null)
		{
			return false;
		}
		if (EXAMINE.equals(option))
		{
			event.consume();
			examine(s);
			return true;
		}
		Player player = client.getLocalPlayer();
		if (walkToEnabled && player != null && player.getWorldLocation().distanceTo(s.getNpc().getWorldLocation()) > 1)
		{
			WorldPoint target = WalkTarget.adjacentTile(s.getNpc().getWorldLocation(), player.getWorldLocation());
			LocalPoint lp = LocalPoint.fromWorld(client.getTopLevelWorldView(), target);
			if (lp != null)
			{
				entry.setType(MenuAction.WALK);
				entry.setIdentifier(0);
				entry.setParam0(lp.getSceneX());
				entry.setParam1(lp.getSceneY());
				pending = new PendingTalk(s.getNpc().getIndex(), client.getTickCount() + PendingTalk.TIMEOUT_TICKS);
				log.debug("Walking to {} before talking to {}", target, s.getPersona().getId());
				return true; // not consumed: the client performs the walk
			}
		}
		event.consume();
		talk(s);
		return true;
	}

	public void onGameTick(int tick)
	{
		if (pending == null)
		{
			return;
		}
		if (pending.isExpired(tick))
		{
			pending = null;
			return;
		}
		StandIn s = registry.byNpcIndex(pending.getNpcIndex()).orElse(null);
		Player player = client.getLocalPlayer();
		if (s == null || player == null)
		{
			pending = null;
			return;
		}
		boolean adjacent = player.getWorldLocation().distanceTo(s.getNpc().getWorldLocation()) <= 1;
		boolean idle = player.getPoseAnimation() == player.getIdlePoseAnimation();
		if (adjacent && idle)
		{
			pending = null;
			talk(s);
		}
	}

	private void talk(StandIn s)
	{
		Persona p = s.getPersona();
		if (!nativeDialogueEnabled)
		{
			String message = new ChatMessageBuilder()
				.append(Color.WHITE, p.getDisplayName() + ": \"The ships aren't running. Come back with Dizana's quiver.\"")
				.build();
			chatMessageManager.queue(QueuedMessage.builder()
				.type(ChatMessageType.GAMEMESSAGE)
				.runeLiteFormattedMessage(message)
				.build());
			return;
		}
		dialogueManager.open(p.getScript(), new Speaker(p.getDisplayName(), p.getNpcId()), context, effect ->
		{
			if (effect == DialogueEffect.UNLOCK_CHARTER)
			{
				onUnlockRequested.run();
			}
		});
	}

	private void examine(StandIn s)
	{
		chatMessageManager.queue(QueuedMessage.builder()
			.type(ChatMessageType.NPC_EXAMINE)
			.runeLiteFormattedMessage(s.getPersona().getExamine())
			.build());
	}
}
```

- [ ] **Step 2: Build**

Run: `./gradlew build -q`
Expected: success. If `Perspective.getTileHeight(Client, LocalPoint, int)` is absent, use `Perspective.getTileHeight(client, lp, wv.getPlane())` from the `WorldView` overload, or pass `0` and confirm in-game that the clickbox still lands on the body.

- [ ] **Step 3: Commit**

```bash
git add src/main/java/com/varlamoreuim/standin/StandInMenuInjector.java
git commit -m "feat(standin): clickbox menu entries, examine, talk-to and walk-to"
```

---

### Task 8: Strip NpcTransportBlocker and rewire the plugin

**Files:**
- Modify: `src/main/java/com/varlamoreuim/npc/NpcTransportBlocker.java`
- Modify: `src/main/java/com/varlamoreuim/VarlamoreUimPlugin.java`

**Interfaces:**
- Consumes: everything from Tasks 3, 6, 7.
- `NpcTransportBlocker` keeps: `CHARTER_NPC_IDS`, `DIZANAS_QUIVER_IDS`, `PRIMIO_NPC_ID`, `renderCallback`, `setEnabled`, `setUnlocked(boolean)` (flag only), `isUnlocked`, `containsDizanasQuiver`, `initClient(Client, ChatMessageManager)`, `handlePrimioClick`, `handleCharterClick`, `getRenderCallback`.

- [ ] **Step 1: Remove stand-in code from NpcTransportBlocker**

Delete these members and their imports: `MYSTERIOUS_OLD_MAN_NPC_ID`, `STAND_IN_IDLE_ANIMATION_ID`, `DOCK_LOCATIONS`, `DOCK_ORIENTATIONS`, `standInNpcs`, `standInCreationAttempted`, `clientThread`, `createStandInNpcs()`, `destroyStandInNpcs()`, `handlePostMenuSort(...)`, `getStandInCount()`, `isStandInCreationAttempted()`, `sendStandInMessage()`. Change `initClient` to `initClient(Client client, ChatMessageManager chatMessageManager)`. Replace `setUnlocked` with:

```java
	public void setUnlocked(boolean unlocked)
	{
		this.unlocked = unlocked;
	}
```

Update the class Javadoc: charter crewmembers are hidden here; their stand-ins live in `com.varlamoreuim.standin`.

- [ ] **Step 2: Rewire VarlamoreUimPlugin**

Fields and injections:

```java
	@Inject
	private ChatboxPanelManager chatboxPanelManager;

	private DialogueManager dialogueManager;
	private StandInRegistry standInRegistry;
	private StandInMenuInjector standInMenuInjector;
```

`startUp()`, replacing the old NpcTransportBlocker block:

```java
		npcTransportBlocker = new NpcTransportBlocker();
		npcTransportBlocker.initClient(client, chatMessageManager);
		renderCallbackManager.register(npcTransportBlocker.getRenderCallback());

		dialogueManager = new DialogueManager(client, clientThread, chatboxPanelManager);
		standInRegistry = new StandInRegistry(client, clientThread);
		DialogueContext dialogueContext = new DialogueContext()
		{
			@Override
			public boolean hasDizanasQuiver()
			{
				return checkDizanasQuiverOwned();
			}

			@Override
			public String playerName()
			{
				Player p = client.getLocalPlayer();
				return p != null && p.getName() != null ? p.getName() : "You";
			}
		};
		standInMenuInjector = new StandInMenuInjector(client, chatMessageManager, dialogueManager,
			standInRegistry, dialogueContext, () -> setUnlocked(true));
		syncStandInState();
```

Helper methods:

```java
	private void syncStandInState()
	{
		boolean blocking = config.pluginEnabled() && config.blockNpcTransport();
		npcTransportBlocker.setEnabled(blocking);
		standInRegistry.setActive(blocking && !npcTransportBlocker.isUnlocked()
			&& client.getGameState() == GameState.LOGGED_IN);
		standInMenuInjector.setWalkToEnabled(config.walkToStandIns());
		standInMenuInjector.setNativeDialogueEnabled(config.nativeDialogue());
	}

	private void setUnlocked(boolean unlocked)
	{
		if (unlocked == npcTransportBlocker.isUnlocked())
		{
			return;
		}
		npcTransportBlocker.setUnlocked(unlocked);
		syncStandInState();
		log.debug("Charter ship unlock state changed: {}", unlocked);
	}
```

`shutDown()`: replace the `destroyStandInNpcs()` call with `standInRegistry.clear(); dialogueManager.close();` and null the three new fields.

`onGameTick`: replace the whole `if (npcTransportBlocker != null) { ... }` block with:

```java
		if (npcTransportBlocker != null)
		{
			syncStandInState();
			dialogueManager.onGameTick();
			standInMenuInjector.onGameTick(client.getTickCount());
		}
```

`onGameStateChanged`: replace the three stand-in branches with:

```java
		if (event.getGameState() == GameState.LOGIN_SCREEN || event.getGameState() == GameState.HOPPING
			|| event.getGameState() == GameState.LOADING)
		{
			if (standInRegistry != null)
			{
				standInRegistry.clear();
			}
			if (event.getGameState() != GameState.LOADING)
			{
				panel.resetStatus();
			}
		}
		if (event.getGameState() == GameState.LOGGED_IN && standInRegistry != null)
		{
			syncStandInState();
			standInRegistry.rescan();
		}
```

New subscriptions:

```java
	@Subscribe
	public void onNpcSpawned(NpcSpawned event)
	{
		if (standInRegistry != null)
		{
			standInRegistry.bind(event.getNpc());
		}
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned event)
	{
		if (standInRegistry != null)
		{
			standInRegistry.unbind(event.getNpc());
		}
	}

	@Subscribe
	public void onClientTick(ClientTick event)
	{
		if (standInRegistry != null)
		{
			standInRegistry.sync();
		}
	}
```

`onPostMenuSort`: body becomes `standInMenuInjector.onPostMenuSort();` guarded by the same config checks.

`onItemContainerChanged`: replace the inner unlock block with `setUnlocked(checkDizanasQuiverOwned());`.

`onMenuOptionClicked`: before the Primio/charter checks inside `if (config.blockNpcTransport())`, add:

```java
			if (standInMenuInjector.onMenuOptionClicked(event))
			{
				return;
			}
```

Imports to add: `com.varlamoreuim.dialogue.DialogueContext`, `com.varlamoreuim.dialogue.DialogueManager`, `com.varlamoreuim.standin.StandInMenuInjector`, `com.varlamoreuim.standin.StandInRegistry`, `net.runelite.api.events.ClientTick`, `net.runelite.api.events.NpcDespawned`, `net.runelite.api.events.NpcSpawned`, `net.runelite.client.game.chatbox.ChatboxPanelManager`.

- [ ] **Step 3: Build and run the full test suite**

Run: `./gradlew build`
Expected: success, all tests pass.

- [ ] **Step 4: Commit**

```bash
git add src/main/java/com/varlamoreuim
git commit -m "refactor(npc): move stand-ins to registry, wire dialogue and menu injector"
```

---

### Task 9: In-game verification, cleanup, docs

**Files:**
- Modify: `src/main/java/com/varlamoreuim/VarlamoreUimPlugin.java` (remove `::vuimtalk`)
- Modify: `README.md` (feature list)
- Modify: `docs/superpowers/specs/2026-09-02-native-npc-standins-design.md` (status line)

- [ ] **Step 1: Run the checklist**

Run `./gradlew run`, log in on a Varlamore-locked account without a quiver, and confirm each item. Record any deviation as a follow-up in the commit message.

1. Sunset Coast: Mysterious Old Man and Fisher stand where the two crewmembers were. Aldarin: Vintner and Pilgrim. Fortis Cothon: Fortis Guard and Harbourmaster.
2. Walk away until the dock unloads, come back: puppets reappear without a relog. Crossing a chunk boundary (LOADING) leaves no stale puppets.
3. Hover the body: top-left shows "Talk-to Name". Hover the tile beside: nothing. Rotate the camera and repeat.
4. Right-click shows Talk-to and Examine. Examine prints in the NPC-examine style.
5. From five tiles away, Talk-to walks you adjacent and the dialogue opens on arrival. Clicking elsewhere mid-walk cancels. With "Walk to dock NPCs" off it opens at once.
6. Chathead is sized and angled like a real dialogue and animates while the text shows.
7. Space, click on text, click on "Click here to continue", keys 1 to 4 and clicking options all work. Escape closes.
8. Walking closes the dialogue. Opening the bank while it is open does not leave the chatbox stuck.
9. "I have one right here!" without a quiver goes to the mocking line and back to the menu. Put a quiver in the inventory (test account or borrowed drop), pick the option again: unlock line shows, dialogue ends, real crew visible.
10. "Native dialogue" off: Talk-to prints the fallback chat line.
11. Disable "Block NPC Transport": puppets vanish, crew visible. Re-enable: puppets return.

- [ ] **Step 2: Remove the debug command**

Delete the `::vuimtalk` registration, its unregister call, and the `ChatCommandManager` injection from `VarlamoreUimPlugin`.

- [ ] **Step 3: Fix anything the checklist surfaced, then build**

Run: `./gradlew build`
Expected: success.

- [ ] **Step 4: Update README feature list and spec status**

In `README.md` under the NPC transport feature, replace the Mysterious Old Man sentence with: "Charter crews at the three docks are replaced by six lore NPCs you can talk to. They explain, in their own way, that Dizana's quiver is the sailing permit, and they check whether you have it."

In the spec, change the status line to `**Status:** Implemented`.

- [ ] **Step 5: Commit**

```bash
git add src/main/java/com/varlamoreuim/VarlamoreUimPlugin.java README.md docs/superpowers/specs/2026-09-02-native-npc-standins-design.md
git commit -m "docs: native stand-ins verified in-game, remove debug command"
```

---

## Self-review notes

- Spec coverage: script model, wrap and expand (Task 1); config (Task 2); renderer, input, lifecycle, calibration (Task 3); dock, walk target, pending talk (Task 4); personas and lore (Task 5); puppet binding and sync (Task 6); clickbox, examine, walk-to, fallback message (Task 7); NpcTransportBlocker reduction, unlock wiring, event plumbing (Task 8); checklist and docs (Task 9).
- Spec deviation: the spec listed `MouseListener` on `DialogueInput`. Clicks are handled by widget op listeners instead, the same mechanism RuneLite's own chatbox menu uses, so no mouse listener is needed.
- Type consistency: `DialogueContext` has `hasDizanasQuiver()` and `playerName()` everywhere. `PendingTalk.TIMEOUT_TICKS` is used by Task 7. `StandInRegistry.byNpcIndex` and `active()` are used by Task 7. `Speaker(name, npcId)` matches Task 3 and Task 7.
