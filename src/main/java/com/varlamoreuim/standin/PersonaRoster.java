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
	 * Common skeleton: opener, reply, menu with Why not / What's the quiver /
	 * I have one / Never mind. Callers pass the persona-specific lines.
	 */
	private static DialogueScript script(String reply, String why1, String why2, String quiver,
		String haveNo, String haveYes, String bye)
	{
		DialogueScript.Builder b = DialogueScript.builder("open")
			.player("open", OPENER, "reply")
			.npc("reply", reply, "menu")
			.options("menu", MENU_TITLE,
				Option.of("Why not?", "why1"),
				Option.of("What's Dizana's quiver?", "quiver"),
				Option.conditional("I have one right here!", DialogueContext::hasDizanasQuiver, "haveYes", "haveNo"),
				Option.of("Never mind.", "bye"))
			.npc("why1", why1, "why2")
			.npc("why2", why2, "menu");
		return b
			.npc("quiver", quiver, "menu")
			.npc("haveNo", haveNo, Expression.LAUGH, DialogueEffect.NONE, "menu")
			.npc("haveYes", haveYes, Expression.HAPPY, DialogueEffect.UNLOCK_CHARTER, DialogueScript.END)
			.npc("bye", bye, DialogueScript.END)
			.build();
	}

	private static Persona oldMan()
	{
		return new Persona("old_man", "Mysterious Old Man", 2830,
			"Definitely not the random event one. Definitely.",
			script(
				"Ships? No, no. Not today. It's the tides, you see. Terrible tides.",
				"Well, if it isn't the tides it's the moon. And if it isn't the moon, the harbourmaster may have confiscated my boat. Allegedly.",
				"Anyway, the sea lanes are closed to anyone who isn't a champion. Kingdom's orders. Nothing to do with me.",
				"Dizana's quiver, from the Fortis Colosseum. Win it and every captain from here to Fortis will fall over themselves to take you aboard. I'd get one myself, but I'm between adventures.",
				"Do you now? Then what's that quiver-shaped patch of nothing on your back?",
				"Well I'll be. A proper champion! Ignore me then, the crew's just over there. Always was.",
				"Suit yourself. I'll be here. Not for any particular reason. Definitely not waiting for someone."));
	}

	private static Persona fisher()
	{
		return new Persona("fisher", "Fisher", 13252,
			"Smells like fish and unfulfilled ambition.",
			script(
				"Sailing? Nothing's sailed from here since the Kingdom closed the lanes. Even the fish left.",
				"Kingdom's decree. No champion, no passage. The fish don't care about decrees, they just left out of spite.",
				"If you fancy disappointment with a net, the Hunter Guild's up the road.",
				"Dizana's quiver, from the Colosseum. Champions get one. Champions get boats. Fishers get told about it a lot.",
				"You've got a fishing rod and optimism. Neither one is a quiver.",
				"Blimey, an actual champion. Go on then, before the fish come back and ask for autographs.",
				"Aye. Mind the rocks."));
	}

	private static Persona vintner()
	{
		return new Persona("vintner", "Vintner", 13908,
			"Has opinions about vintages. Strong ones.",
			script(
				"Ships? I've been waiting on a barrel shipment for three weeks. Wine gets through. People don't.",
				"The Kingdom closed the lanes to everyone but champions. Apparently barrels are less likely to wander off and die.",
				"If you're stuck here, I could use a grape stomper. Pay's in grapes.",
				"Dizana's quiver. You win it in the Fortis Colosseum. Show it to any captain and they'll carry you anywhere. Even without the barrels.",
				"Then you're stomping grapes, not sailing. The vat's that way.",
				"A champion! Splendid. The crew's right there. Take a bottle for the road.",
				"Mind the vines on your way out."));
	}

	private static Persona pilgrim()
	{
		return new Persona("pilgrim", "Pilgrim", 13883,
			"Radiantly unbothered.",
			script(
				"Ralos guides ships by daylight, friend. Today Ralos has chosen not to guide yours.",
				"The Kingdom closed the sea lanes. The Kingdom says it is a decree. I say it is Ralos. We are both right.",
				"Only champions may sail. Ralos loves a champion.",
				"Dizana's quiver, earned in the Colosseum. Ralos sees it and smiles. Captains see it and lower the gangplank.",
				"Ralos sees all things. Ralos does not see a quiver.",
				"Ah! Ralos smiles upon you. And the crew, apparently. Go with the sun.",
				"Walk in the light, friend."));
	}

	private static Persona guard()
	{
		return new Persona("guard", "Fortis Guard", 13100,
			"Following orders. Enthusiastically.",
			script(
				"Halt. Harbour's closed by royal decree. No passage without a champion's token.",
				"The decree says, and I quote, 'Ultimate Ironmen keep wandering off and losing everything.'",
				"I don't know what that means, but it was underlined twice, so it's serious.",
				"Dizana's quiver. Win it in the Colosseum, then you're a champion and I salute you instead of standing in your way. It's a whole thing.",
				"I've been trained to spot a quiver, citizen. That's a back. Move along.",
				"A champion! Apologies. The crew awaits. Try not to wander off.",
				"Carry on, citizen. Stay on dry land."));
	}

	private static Persona harbourmaster()
	{
		return new Persona("harbourmaster", "Harbourmaster", 13248,
			"Loves a stamp.",
			script(
				"Passage? Certainly. I'll just need to see your form 7B.",
				"Without form 7B you are not a champion, and without being a champion you cannot sail. It's all very tidy.",
				"Form 7B is Dizana's quiver. No, you cannot fill it in. You have to win it.",
				"Dizana's quiver. Awarded at the Fortis Colosseum. Doubles as a form, a permit and a hat, if you're desperate.",
				"I see no form 7B. I see a person with hope, which is not a recognised document.",
				"Form 7B, present and correct! Welcome aboard, champion. The crew's over there. Stamp, stamp.",
				"The office is open dawn to dusk. Do come back with paperwork."));
	}
}
