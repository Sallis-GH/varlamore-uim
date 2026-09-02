package com.varlamoreuim.dialogue;

import lombok.Value;

/** Who is talking on an NPC page: display name and NPC definition id for the chathead. */
@Value
public class Speaker
{
	String name;
	int npcId;
}
