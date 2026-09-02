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
