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
