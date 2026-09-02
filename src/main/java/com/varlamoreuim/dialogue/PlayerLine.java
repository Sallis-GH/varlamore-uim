package com.varlamoreuim.dialogue;

import lombok.Value;

/** A line spoken by the local player. */
@Value
public class PlayerLine implements DialoguePage
{
	String text;
	String next;
}
