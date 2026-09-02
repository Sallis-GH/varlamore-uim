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
