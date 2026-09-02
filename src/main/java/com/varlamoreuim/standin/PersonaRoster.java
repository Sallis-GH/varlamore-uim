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
	 * Adds the shared "prove it" ending. The conditional option routes to
	 * {@code prove} when the player has the quiver and to {@code proveNo} when
	 * they do not; both open with the same challenge line.
	 */
	private static DialogueScript.Builder proof(DialogueScript.Builder b, String challenge,
		String shown, String yes, String cant, String no)
	{
		return b
			.npc("prove", challenge, "shown")
			.narration("shown", shown, "haveYes")
			.npc("haveYes", yes, Expression.HAPPY, DialogueEffect.UNLOCK_CHARTER, DialogueScript.END)
			.npc("proveNo", challenge, "cant")
			.player("cant", cant, "no")
			.npc("no", no, DialogueScript.END);
	}

	private static Option champion(String label)
	{
		return Option.conditional(label, DialogueContext::hasDizanasQuiver, "prove", "proveNo");
	}

	private static Persona oldMan()
	{
		DialogueScript.Builder b = DialogueScript.builder("open")
			.player("open", OPENER, "reply")
			.npc("reply", "Not today. Nor tomorrow, I shouldn't think.", "why")
			.player("why", "Why not?", "whyNot")
			.npc("whyNot", "The Kingdom closed the sea lanes. Champions only, they say.", "menu")
			.options("menu", MENU_TITLE,
				Option.of("How do I become a champion?", "how"),
				Option.of("Can't you do anything?", "anything"),
				Option.of("Do I know you from somewhere?", "know"),
				champion("I'm a champion."))
			.npc("how", "The Colosseum. Win, and they hand you a quiver. Simple.", DialogueScript.END)
			.npc("anything", "I could make you disappear. You wouldn't like where you turned up.", DialogueScript.END)
			.npc("know", "I get that a lot.", DialogueScript.END);
		return new Persona("old_man", "Mysterious Old Man", 2830,
			"Definitely not the random event one. Definitely.",
			proof(b, "Oh? Let's see it.",
				"You show the old man Dizana's quiver.",
				"So you are. Off you go, the crew's just there.",
				"I... don't have it on me.",
				"Then you're not.").build());
	}

	private static Persona fisher()
	{
		DialogueScript.Builder b = DialogueScript.builder("open")
			.player("open", OPENER, "reply")
			.npc("reply", "No. Nothing's sailed from here in weeks.", "why")
			.player("why", "Why not?", "whyNot")
			.npc("whyNot", "Kingdom's orders. Sea lanes are for champions only now.", "menu")
			.options("menu", MENU_TITLE,
				Option.of("How do I become a champion?", "how"),
				Option.of("Caught anything?", "caught"),
				Option.of("Can't you take me out in your boat?", "boat"),
				champion("I'm a champion."))
			.npc("how", "Fight in the Colosseum. Win and they give you a quiver. Not my idea of a good time.", DialogueScript.END)
			.npc("caught", "A boot. Two, actually.", DialogueScript.END)
			.npc("boat", "It's a rowing boat. You'd be bailing before we cleared the harbour.", DialogueScript.END);
		return new Persona("fisher", "Fisher", 13252,
			"Smells of fish. Mostly fish.",
			proof(b, "Are you? Show us, then.",
				"You show the fisher Dizana's quiver.",
				"Well I never. Crew's over there, champion.",
				"I don't have one.",
				"Thought not.").build());
	}

	private static Persona vintner()
	{
		DialogueScript.Builder b = DialogueScript.builder("open")
			.player("open", OPENER, "reply")
			.npc("reply", "Not for passengers. I've been waiting on a shipment for three weeks.", "why")
			.player("why", "Why not?", "whyNot")
			.npc("whyNot", "The Kingdom closed the sea lanes. Champions only. Barrels don't count.", "menu")
			.options("menu", MENU_TITLE,
				Option.of("How do I become a champion?", "how"),
				Option.of("Can't you make an exception?", "exception"),
				Option.of("Need a hand with the grapes?", "grapes"),
				champion("I'm a champion."))
			.npc("how", "Win at the Colosseum. They give you a quiver. Show it to a captain.", DialogueScript.END)
			.npc("exception", "I make wine, not the rules.", DialogueScript.END)
			.npc("grapes", "Always. Pay's in grapes.", DialogueScript.END);
		return new Persona("vintner", "Vintner", 13908,
			"Has opinions about vintages. Strong ones.",
			proof(b, "Oh? Prove it.",
				"You show the vintner Dizana's quiver.",
				"Well then. The crew's right there. Take a bottle for the road.",
				"I can't.",
				"Then stomp some grapes. It's very calming.").build());
	}

	private static Persona pilgrim()
	{
		DialogueScript.Builder b = DialogueScript.builder("open")
			.player("open", OPENER, "reply")
			.npc("reply", "Not for you, friend. Ralos has not willed it.", "why")
			.player("why", "Why not?", "whyNot")
			.npc("whyNot", "The Kingdom closed the sea lanes. Champions only. Ralos agrees, I'm told.", "menu")
			.options("menu", MENU_TITLE,
				Option.of("How do I become a champion?", "how"),
				Option.of("Can't Ralos make an exception?", "exception"),
				Option.of("Do you ever stop smiling?", "smile"),
				champion("I'm a champion."))
			.npc("how", "The Colosseum. Win, and they give you a quiver. Ralos rather likes a champion.", DialogueScript.END)
			.npc("exception", "He could. He hasn't.", DialogueScript.END)
			.npc("smile", "Only at night.", DialogueScript.END);
		return new Persona("pilgrim", "Pilgrim", 13883,
			"Radiantly unbothered.",
			proof(b, "Then show me.",
				"You show the pilgrim Dizana's quiver.",
				"Ralos smiles on you. So does the crew, over there.",
				"I can't.",
				"Ralos sees all things. He does not see a quiver.").build());
	}

	/**
	 * The guard is the one persona who gives nothing away. Every answer is as
	 * short as he can make it, and the quiver is only acknowledged if it is
	 * actually on the player's back.
	 */
	private static Persona guard()
	{
		DialogueScript script = DialogueScript.builder("open")
			.player("open", OPENER, "reply")
			.npc("reply", "Not for you.", "why")
			.player("why", "Why not?", "whyNot")
			.npc("whyNot", "Next question.", "menu")
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
		DialogueScript.Builder b = DialogueScript.builder("open")
			.player("open", OPENER, "reply")
			.npc("reply", "Plenty. None of them with you on board.", "why")
			.player("why", "Why not?", "whyNot")
			.npc("whyNot", "Sea lanes are closed to civilians by order of the Kingdom. Champions only.", "menu")
			.options("menu", MENU_TITLE,
				Option.of("How do I become a champion?", "how"),
				Option.of("Can't you make an exception?", "exception"),
				Option.of("What if I just swim?", "swim"),
				champion("I am a champion."))
			.npc("how", "Win at the Colosseum. They'll give you Dizana's quiver. Bring it here.", DialogueScript.END)
			.npc("exception", "Rules are rules, I'm afraid.", DialogueScript.END)
			.npc("swim", "Then I'll wave.", DialogueScript.END);
		return new Persona("harbourmaster", "Harbourmaster", 13248,
			"Runs a tight harbour.",
			proof(b, "Then prove it.",
				"You show the harbourmaster Dizana's quiver.",
				"Well why didn't you say so? The crew's over there.",
				"Well... I can't.",
				"Then come back when you can.").build());
	}
}
