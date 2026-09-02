package com.varlamoreuim.dialogue;

import lombok.Value;

/**
 * A page of action or scene text with no speaker: no chathead, no name, just the
 * text and "Click here to continue", like the game's plain message box.
 */
@Value
public class Narration implements DialoguePage
{
	String text;
	String next;
}
