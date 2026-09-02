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
	 * Adds the shared "prove it" ending, the gatekeeper grammar the game uses:
	 * the NPC doubts the claim, never the player. The conditional option routes
	 * to {@code prove} when the player has the quiver and to {@code proveNo} when
	 * they do not; both open with the same challenge line.
	 */
	private static DialogueScript.Builder proof(DialogueScript.Builder b, String challenge,
		String shown, String yes, String no)
	{
		return b
			.npc("prove", challenge, "shown")
			.narration("shown", shown, "haveYes")
			.npc("haveYes", yes, Expression.HAPPY, DialogueEffect.UNLOCK_CHARTER, DialogueScript.END)
			.npc("proveNo", challenge, "cant")
			.player("cant", "Well... I can't.", "no")
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
			.npc("reply", "Not for you, no. Not for me either, though I have my own ways of travelling.", "why")
			.player("why", "Why not?", "whyNot")
			.npc("whyNot", "The Kingdom closed the lanes to civilians. Only champions of the Colosseum sail.", "menu")
			.options("menu", MENU_TITLE,
				Option.of("How does one become a champion?", "how"),
				Option.of("What ways of travelling?", "ways"),
				Option.of("Who are you?", "who"),
				champion("I am a champion of the Colosseum."))
			.npc("how", "Win the Colosseum. They give you Dizana's quiver, and every guard on this coast knows it.", DialogueScript.END)
			.npc("ways", "Mine tend to find me rather than the other way round. Yours need a ship.", DialogueScript.END)
			.npc("who", "An old man on a dock. Ask me again in a year and you'll get a different answer.", DialogueScript.END);
		return new Persona("old_man", "Mysterious Old Man", 2830,
			"An old man in a dark hood. He does not appear to be waiting for a ship.",
			proof(b, "Then show me. Champions carry a quiver I would know at a glance.",
				"You show the old man Dizana's quiver.",
				"So they do. Sail well, and tell no one I was here.",
				"I thought as much. Come and find me when you have it.").build());
	}

	private static Persona fisher()
	{
		DialogueScript.Builder b = DialogueScript.builder("open")
			.player("open", OPENER, "reply")
			.npc("reply", "Nilsal! Fishing boats, aye. Anything bigger stays tied to the post.", "why")
			.player("why", "Why not?", "whyNot")
			.npc("whyNot", "The Kingdom shut the lanes to civilians. Only Colosseum champions get past the bay now.", "menu")
			.options("menu", MENU_TITLE,
				Option.of("How does someone become a champion?", "how"),
				Option.of("How's the catch been?", "catch"),
				Option.of("Could you row me out yourself?", "row"),
				champion("I'm a champion of the Colosseum."))
			.npc("how", "You fight the Fortis Colosseum and win. They give you Dizana's quiver to prove it.", DialogueScript.END)
			.npc("catch", "Better than the trade. All these fish and no ship to carry them anywhere.", DialogueScript.END)
			.npc("row", "And lose my licence over it? Sorry, friend, I'd rather keep the boat.", DialogueScript.END);
		return new Persona("fisher", "Fisher", 13252,
			"A Sunset Coast fisher, mending her nets.",
			proof(b, "Are you now? Champions carry Dizana's quiver. Let's see it.",
				"You show the fisher Dizana's quiver.",
				"Well, look at that. I'll point you to a captain who'll take you.",
				"Then you're stuck on the sand with me. Come back when you've got it.").build());
	}

	private static Persona vintner()
	{
		DialogueScript.Builder b = DialogueScript.builder("open")
			.player("open", OPENER, "reply")
			.npc("reply", "Nilsal, friend. Not one, and my wine is turning while we talk.", "why")
			.player("why", "Why not?", "whyNot")
			.npc("whyNot", "The Kingdom closed the lanes to civilians. Only Colosseum champions sail these days.", "menu")
			.options("menu", MENU_TITLE,
				Option.of("How does one become a champion?", "how"),
				Option.of("What happens to your wine now?", "wine"),
				Option.of("Could I carry a crate for you?", "crate"),
				champion("I'm a champion of the Colosseum."))
			.npc("how", "You win the Fortis Colosseum. They hand you Dizana's quiver, and doors open for it.", DialogueScript.END)
			.npc("wine", "It sits. Aldarin red keeps a year, but the buyers up north won't wait one.", DialogueScript.END)
			.npc("crate", "Kind of you, but they'd stop you at the gangplank the same as me.", DialogueScript.END);
		return new Persona("vintner", "Vintner", 13908,
			"An Aldarin vintner, counting crates that aren't going anywhere.",
			proof(b, "Then prove it. Show me the quiver and I'll believe you gladly.",
				"You show the vintner Dizana's quiver.",
				"So it's true. Take a bottle with you, champion, and my thanks.",
				"Then we're both stuck ashore. Come back with it and I'll pour.").build());
	}

	private static Persona pilgrim()
	{
		DialogueScript.Builder b = DialogueScript.builder("open")
			.player("open", OPENER, "reply")
			.npc("reply", "Greetings, traveller. None that will carry the likes of us, sadly.", "why")
			.player("why", "Why not?", "whyNot")
			.npc("whyNot", "The Kingdom has closed the lanes to civilians. Only champions of the Colosseum may sail.", "menu")
			.options("menu", MENU_TITLE,
				Option.of("How does one become a champion?", "how"),
				Option.of("Where were you hoping to sail?", "where"),
				Option.of("Will Ralos not open the way?", "ralos"),
				champion("I am a champion of the Colosseum."))
			.npc("how", "By winning the Colosseum. Dizana's quiver is their token, and the guards know it well.", DialogueScript.END)
			.npc("where", "To the shrines across the water, while the light lasts. Ralos is patient, and I am learning to be.", DialogueScript.END)
			.npc("ralos", "Ralos lights the road. He does not argue with harbourmasters.", DialogueScript.END);
		return new Persona("pilgrim", "Pilgrim", 13883,
			"A pilgrim of Ralos, waiting on the tide and on the sun.",
			proof(b, "Then prove it, friend. Show me the quiver and I will believe you gladly.",
				"You show the pilgrim Dizana's quiver.",
				"Then Ralos has sent me a companion. Go safely, champion.",
				"Then we wait together. Ralos keep you until the lanes open.").build());
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
			.npc("reply", "Ships, yes. Passengers, no, I'm afraid.", "why")
			.player("why", "Why not?", "whyNot")
			.npc("whyNot", "The Kingdom has closed the lanes to civilians. Only Colosseum champions sail now.", "menu")
			.options("menu", MENU_TITLE,
				Option.of("How does one become a champion?", "how"),
				Option.of("When will the lanes open again?", "when"),
				Option.of("Could I pay my way aboard?", "pay"),
				champion("I am a champion of the Colosseum."))
			.npc("how", "Win through the Fortis Colosseum. Dizana's quiver is what they hand you, and what I check.", DialogueScript.END)
			.npc("when", "Nobody has told me. I'd hear it from the capital before I heard it from the tide.", DialogueScript.END)
			.npc("pay", "Save your coin, traveller. The order came from the Kingdom, and it isn't mine to sell.", DialogueScript.END);
		return new Persona("harbourmaster", "Harbourmaster", 13248,
			"The keeper of the harbour's comings and goings.",
			proof(b, "Then prove it. Champions carry Dizana's quiver.",
				"You show the harbourmaster Dizana's quiver.",
				"So you are. Board when you're ready, champion.",
				"Then we're done here. Come back with the quiver and we'll talk.").build());
	}
}
