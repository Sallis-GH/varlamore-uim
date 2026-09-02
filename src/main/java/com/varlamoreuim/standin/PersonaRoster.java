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
	 * Common shape: the player asks, the NPC refuses, the player asks why, the NPC
	 * names Dizana's quiver as proof of strength, then one menu whose choices
	 * respond to that moment. Every choice ends the conversation.
	 *
	 * @param reply      the NPC's refusal
	 * @param whyNot     the reason, naming the quiver as proof you're strong enough
	 * @param goWin      reply to "I'll go and win one, then."
	 * @param exception  reply to "Can't you make an exception?"
	 * @param askLabel   the persona-specific third question
	 * @param askReply   reply to that question
	 * @param haveYes    reply when the player really has the quiver (unlocks)
	 * @param haveNo     reply when the player claims a quiver they don't have
	 */
	private static DialogueScript script(String reply, String whyNot, String goWin, String exception,
		String askLabel, String askReply, String haveYes, String haveNo)
	{
		return DialogueScript.builder("open")
			.player("open", OPENER, "reply")
			.npc("reply", reply, "why")
			.player("why", "Why not?", "whyNot")
			.npc("whyNot", whyNot, "menu")
			.options("menu", MENU_TITLE,
				Option.of("I'll go and win one, then.", "goWin"),
				Option.of("Can't you make an exception?", "exception"),
				Option.of(askLabel, "ask"),
				Option.conditional("I already have one.", DialogueContext::hasDizanasQuiver, "haveYes", "haveNo"))
			.npc("goWin", goWin, DialogueScript.END)
			.npc("exception", exception, DialogueScript.END)
			.npc("ask", askReply, DialogueScript.END)
			.npc("haveYes", haveYes, Expression.HAPPY, DialogueEffect.UNLOCK_CHARTER, DialogueScript.END)
			.npc("haveNo", haveNo, Expression.LAUGH, DialogueEffect.NONE, DialogueScript.END)
			.build();
	}

	private static Persona oldMan()
	{
		return new Persona("old_man", "Mysterious Old Man", 2830,
			"Definitely not the random event one. Definitely.",
			script(
				"Ships? Not for you. Not for anyone, really. Well. Not for you.",
				"The Kingdom closed the sea lanes. Only champions sail now. Bring me proof you're strong enough, Dizana's quiver from the Colosseum, and I might put in a word. I know people. Allegedly.",
				"That's the spirit. The Colosseum's in Civitas illa Fortis. Mind the lions. And the archers. And the lava.",
				"I made an exception once. It followed me around for a week. Never again.",
				"Aren't you the random event man?",
				"I have no idea what you're talking about. Do you want a free spin? No? Then move along.",
				"Well I'll be. A real champion! Ignore me then, the crew's just over there. Always was.",
				"Do you? Then what's that quiver-shaped patch of nothing on your back?"));
	}

	private static Persona fisher()
	{
		return new Persona("fisher", "Fisher", 13252,
			"Smells like fish and unfulfilled ambition.",
			script(
				"Sailing? Nothing's sailed from here since the Kingdom closed the lanes. Even the fish left.",
				"Champions only, they said. Prove you're strong enough, win Dizana's quiver at the Colosseum, and the crews will take you anywhere. Me? I can barely lift a net.",
				"Good luck. Bring back a fish if you see one. Any fish. I'm not fussy any more.",
				"I'm a fisher, not a harbourmaster. I can't even get an exception for myself.",
				"Caught anything?",
				"A boot. Two, actually. Different sizes. That's the sea for you.",
				"Blimey, an actual champion. Go on then, before the fish come back and ask for autographs.",
				"You've got a fishing rod and optimism. Neither one is a quiver."));
	}

	private static Persona vintner()
	{
		return new Persona("vintner", "Vintner", 13908,
			"Has opinions about vintages. Strong ones.",
			script(
				"Ships? I've been waiting on a barrel shipment for three weeks. Wine gets through. People don't.",
				"The Kingdom closed the lanes to everyone but champions. Prove you're strong enough, win Dizana's quiver at the Colosseum, and any captain will carry you. Even without the barrels.",
				"Splendid. Come back with the quiver and I'll open something older than you.",
				"I make wine, not law. If I made law, barrels would arrive on time.",
				"Need a hand with the grapes?",
				"Always. Pay's in grapes. Some of them are even ripe.",
				"A champion! The crew's right there. Take a bottle for the road.",
				"That's a back, not a quiver. The vat's that way if you're bored."));
	}

	private static Persona pilgrim()
	{
		return new Persona("pilgrim", "Pilgrim", 13883,
			"Radiantly unbothered.",
			script(
				"Ralos guides ships by daylight, friend. Today Ralos has chosen not to guide yours.",
				"The Kingdom closed the sea lanes. Champions only. Show that you are strong enough, Dizana's quiver from the Colosseum, and Ralos will light your way. Captains tend to follow the light.",
				"Go with the sun. It rises over the Colosseum, which I have always found telling.",
				"Ralos makes exceptions. He simply hasn't made one for you. Be patient. Or be a champion.",
				"Do you ever stop smiling?",
				"Only at night. Ralos isn't watching then.",
				"Ah! Ralos smiles upon you. And the crew, apparently. Go with the sun.",
				"Ralos sees all things. Ralos does not see a quiver."));
	}

	private static Persona guard()
	{
		return new Persona("guard", "Fortis Guard", 13100,
			"Stands guard. Mostly stands.",
			script(
				"Not for you. The harbour's closed to civilians by royal decree.",
				"Kingdom's orders. The sea lanes are for champions only. Prove you're strong enough, bring me Dizana's quiver from the Colosseum, and I'll walk you aboard myself.",
				"Good. The Colosseum's in the city, past the bazaar. Come back in one piece. I'd rather salute you than sweep you up.",
				"I'm a guard. I don't make exceptions, I stand in front of them.",
				"Do you ever let anyone through?",
				"Champions. And the fish, technically. They don't need a permit.",
				"That's Dizana's quiver, that is. Apologies, champion. The crew's waiting for you.",
				"I know what a quiver looks like. That's a back. Nice try, citizen."));
	}

	private static Persona harbourmaster()
	{
		return new Persona("harbourmaster", "Harbourmaster", 13248,
			"Runs a tight harbour. Very tight.",
			script(
				"Plenty. None of them with you on board, I'm afraid.",
				"Sea lanes are closed to civilians by order of the Kingdom. Champions only. Bring me proof you're strong enough, Dizana's quiver from the Colosseum, and I might let you on.",
				"Marvellous. The Colosseum's up in the city. Do try not to die, it's dreadful for the schedule.",
				"I make exceptions for champions. That's what the quiver is for.",
				"What if I just swim?",
				"Then I'll wave. It's the least I can do.",
				"That's the quiver, right enough. Welcome aboard, champion. The crew's over there.",
				"I see no quiver. I see a person with hope, which floats about as well as you would."));
	}
}
