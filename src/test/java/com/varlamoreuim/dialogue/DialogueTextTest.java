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
