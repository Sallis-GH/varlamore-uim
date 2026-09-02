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

	private static DialogueScript.Builder open(String reply, String whyNot)
	{
		return DialogueScript.builder("open")
			.player("open", OPENER, "reply")
			.npc("reply", reply, "why")
			.player("why", "Why not?", "whyNot")
			.npc("whyNot", whyNot, "menu");
	}

	private static Persona oldMan()
	{
		DialogueScript script = open(
			"No. Nothing's moving out there.",
			"The lanes are shut. I don't use them anyway.")
			.options("menu", MENU_TITLE,
				Option.of("How do you get about, then?", "about"),
				Option.of("Who are you?", "who"),
				Option.of("Are you waiting for someone?", "waiting"),
				Option.of("Will you be here later?", "later"))
			.npc("about", "I turn up. It's served me well.", DialogueScript.END)
			.npc("who", "Just an old man. Nothing more.", DialogueScript.END)
			.npc("waiting", "I was. He didn't come.", DialogueScript.END)
			.npc("later", "Almost certainly not.", DialogueScript.END)
			.build();
		return new Persona("old_man", "Mysterious Old Man", 2830,
			"An old man in a dark hood. He was not here a moment ago.", script);
	}

	private static Persona fisher()
	{
		DialogueScript script = open(
			"Nilsal. Not that I've heard, no.",
			"The Kingdom shut the lanes. Doesn't stop the fish.")
			.options("menu", MENU_TITLE,
				Option.of("Who shut them?", "who"),
				Option.of("How's the fishing?", "fishing"),
				Option.of("Could you row me out?", "row"),
				Option.of("Mind if I wait here?", "wait"))
			.npc("who", "Someone in the capital. Never met them.", DialogueScript.END)
			.npc("fishing", "Better since the big boats stopped.", DialogueScript.END)
			.npc("row", "No. I'd have to stop fishing.", DialogueScript.END)
			.npc("wait", "Wait where you like. Mind the nets.", DialogueScript.END)
			.build();
		return new Persona("fisher", "Fisher", 13252,
			"A fisher from the Sunset Coast. Her nets need mending.", script);
	}

	private static Persona vintner()
	{
		DialogueScript script = open(
			"Nilsal! None at all. My wine is going nowhere.",
			"The lanes are closed. My crates are stuck here.")
			.options("menu", MENU_TITLE,
				Option.of("Who closed them?", "who"),
				Option.of("How much wine is stuck here?", "howmuch"),
				Option.of("Is the wine any good?", "good"),
				Option.of("Can I buy a bottle?", "buy"))
			.npc("who", "I didn't ask. I was busy panicking.", DialogueScript.END)
			.npc("howmuch", "Forty crates. I've started drinking it.", DialogueScript.END)
			.npc("good", "Better than it'll be tomorrow.", DialogueScript.END)
			.npc("buy", "Buy? I'm nearly giving it away.", DialogueScript.END)
			.build();
		return new Persona("vintner", "Vintner", 13908,
			"An Aldarin vintner. He is guarding his crates.", script);
	}

	private static Persona pilgrim()
	{
		DialogueScript script = open(
			"I couldn't say. I don't watch the water.",
			"Something about the lanes being closed. I wasn't really listening.")
			.options("menu", MENU_TITLE,
				Option.of("Aren't you waiting for a ship?", "ship"),
				Option.of("Who told you they were closed?", "who"),
				Option.of("Does Ralos know a way across?", "ralos"),
				Option.of("Doesn't the sun bother you?", "sun"))
			.npc("ship", "No. I've nowhere to be.", DialogueScript.END)
			.npc("who", "A man with a hat. He seemed sure.", DialogueScript.END)
			.npc("ralos", "He's never mentioned one to me.", DialogueScript.END)
			.npc("sun", "That's rather the point.", DialogueScript.END)
			.build();
		return new Persona("pilgrim", "Pilgrim", 13883,
			"A pilgrim of Ralos. He has been standing in the sun a while.", script);
	}

	/**
	 * The guard is the one persona who gives nothing away. Every answer is as
	 * short as he can make it, and the quiver is only acknowledged if it is
	 * actually on the player's back.
	 */
	private static Persona guard()
	{
		DialogueScript script = open("Not for you.", "Next question.")
			.options("menu", MENU_TITLE,
				Option.of("Who is allowed on, then?", "who"),
				Option.of("Can't you make an exception?", "exception"),
				Option.of("Do you say anything other than no?", "other"),
				Option.conditional("What if I'm a champion?", DialogueContext::hasDizanasQuiver, "notices", "haveNo"))
			.npc("who", "Champions.", DialogueScript.END)
			.npc("exception", "No.", DialogueScript.END)
			.npc("other", "No.", DialogueScript.END)
			.narration("notices", "The guard notices the quiver on your back.", "haveYes")
			.npc("haveYes", "Fine.", Expression.DEFAULT, DialogueEffect.UNLOCK_CHARTER, DialogueScript.END)
			.npc("haveNo", "You're not.", Expression.LAUGH, DialogueEffect.NONE, DialogueScript.END)
			.build();
		return new Persona("guard", "Fortis Guard", 13100,
			"Stands guard. Mostly stands.", script);
	}

	private static Persona harbourmaster()
	{
		DialogueScript script = open(
			"Some. None you can get on.",
			"The Kingdom closed the lanes. I just tell people about it.")
			.options("menu", MENU_TITLE,
				Option.of("Who closed the lanes?", "who"),
				Option.of("When will they open again?", "when"),
				Option.of("Who is allowed through?", "allowed"),
				Option.conditional("I'm a champion of the Colosseum.", DialogueContext::hasDizanasQuiver, "prove", "proveNo"))
			.npc("who", "The Queen, I'd expect. Not me.", DialogueScript.END)
			.npc("when", "When someone tells me. Nobody has yet.", DialogueScript.END)
			.npc("allowed", "Champions of the Colosseum. That's the list.", DialogueScript.END)
			.npc("prove", "Prove it, then.", "shown")
			.narration("shown", "You show the harbourmaster your quiver.", "haveYes")
			.npc("haveYes", "Right. Off you go.", Expression.DEFAULT, DialogueEffect.UNLOCK_CHARTER, DialogueScript.END)
			.npc("proveNo", "Prove it, then.", "cant")
			.player("cant", "Well... I can't.", "no")
			.npc("no", "Then it's still no.", DialogueScript.END)
			.build();
		return new Persona("harbourmaster", "Harbourmaster", 13248,
			"The harbourmaster. He looks like he'd rather be sitting down.", script);
	}
}
