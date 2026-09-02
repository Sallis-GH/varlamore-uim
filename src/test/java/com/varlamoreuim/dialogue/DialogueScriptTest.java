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
		for (int i = 0; i < 60; i++)
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
		for (int i = 0; i < 60; i++)
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
