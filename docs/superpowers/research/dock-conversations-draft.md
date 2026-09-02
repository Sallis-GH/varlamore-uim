# Charter Dock Stand-In Conversations — Draft

Six persona drafts for the charter-dock stand-in NPC. Written against
`osrs-dialogue-style-guide.md`.

Shared premise: the Kingdom has closed the sea lanes to civilians. Only champions of the Fortis
Colosseum may sail, and Dizana's quiver is the proof. Every NPC line reads as a reply to the
line the player just spoke. One menu, no loops, every option ends the conversation.

Field key:
- `examine` — examine text, not spoken
- `open` / `why` — the player's fixed lines
- `reply` / `whyNot` — the NPC's answers to those lines
- `menu` — one option list; each `->` is the ending line, and the conversation stops there
- champion row — `challenge` (NPC's push-back) | `shown` (narration box) | `yes` (has quiver) |
  `cant` (player admits it) | `no` (NPC closes the door)

---

## Fortis Guard
examine: A guard of Civitas illa Fortis. He has been standing here a long time.
open: Any ships sailing today?
reply: Not for you.
why: Why not?
whyNot: Next question.
menu:
- Who is allowed on, then? -> Champions.
- Can't you make an exception? -> No.
- Do you say anything other than no? -> No.
- What if I'm a champion? -> challenge: (none - the guard skips straight to the check) | shown: The guard notices the quiver on your back. | yes: Fine. | no: You're not.

*Why this is in register:* §4 Authority — the guard is the clipped end of the scale, citing
orders he does not explain, with a warm switch that never exceeds one word ("Fair enough." is
the model for "Fine."). §3 device 7 — he doubts the claim, never the player's worth.
Deliberately the one exception to §2's soft hedge: his refusal is bare because his job is
silence, and the other five personas carry the full refusal grammar.

---

## Harbourmaster
examine: The keeper of the harbour's comings and goings.
open: Any ships sailing today?
reply: Ships, yes. Passengers, no, I'm afraid.
why: Why not?
whyNot: The Kingdom has closed the lanes to civilians. Only Colosseum champions sail now.
menu:
- How does one become a champion? -> Win through the Fortis Colosseum. Dizana's quiver is what they hand you, and what I check.
- When will the lanes open again? -> Nobody has told me. I'd hear it from the capital before I heard it from the tide.
- Could I pay my way aboard? -> Save your coin, traveller. The order came from the Kingdom, and it isn't mine to sell.
- I am a champion of the Colosseum. -> challenge: Then prove it. Champions carry Dizana's quiver. | shown: You show the harbourmaster Dizana's quiver. | yes: So you are. Board when you're ready, champion. | cant: Well... I can't. | no: Then we're done here. Come back with the quiver and we'll talk.

*Why this is in register:* §2 refusal grammar in full — hedge, the rule stated as policy from
the Kingdom rather than his own preference, one clause of reason, a named path to reversal, and
a warm switch shorter than the refusal ("Okay, jump aboard then." is the model). §4 Officials —
formal name of the polity, "traveller", no over-explanation, and the bribe refused as
"not mine to sell" rather than as an insult.

---

## Fisher
examine: A Sunset Coast fisher, mending her nets.
open: Any ships sailing today?
reply: Nilsal! Fishing boats, aye. Anything bigger stays tied to the post.
why: Why not?
whyNot: The Kingdom shut the lanes to civilians. Only Colosseum champions get past the bay now.
menu:
- How does someone become a champion? -> You fight the Fortis Colosseum and win. They give you Dizana's quiver to prove it.
- How's the catch been? -> Better than the trade. All these fish and no ship to carry them anywhere.
- Could you row me out yourself? -> And lose my licence over it? Sorry, friend, I'd rather keep the boat.
- I'm a champion of the Colosseum. -> challenge: Are you now? Champions carry Dizana's quiver. Let's see it. | shown: You show the fisher Dizana's quiver. | yes: Well, look at that. I'll point you to a captain who'll take you. | cant: Well... I can't. | no: Then you're stuck on the sand with me. Come back when you've got it.

*Why this is in register:* §7 Varlamore — "Nilsal" as a local's greeting, and §4 Workers plus
§3 device 6, where her one gripe is the day's catch and nothing else ("Well given how good
today's catch was..." is the model). §2 — her refusal to row is an excuse with a stated cost
(her licence), softened with "Sorry, friend", and her closing line includes herself in the
stranding rather than mocking the player.

---

## Vintner
examine: An Aldarin vintner, counting crates that aren't going anywhere.
open: Any ships sailing today?
reply: Nilsal, friend. Not one, and my wine is turning while we talk.
why: Why not?
whyNot: The Kingdom closed the lanes to civilians. Only Colosseum champions sail these days.
menu:
- How does one become a champion? -> You win the Fortis Colosseum. They hand you Dizana's quiver, and doors open for it.
- What happens to your wine now? -> It sits. Aldarin red keeps a year, but the buyers up north won't wait one.
- Could I carry a crate for you? -> Kind of you, but they'd stop you at the gangplank the same as me.
- I'm a champion of the Colosseum. -> challenge: Then prove it. Show me the quiver and I'll believe you gladly. | shown: You show the vintner Dizana's quiver. | yes: So it's true. Take a bottle with you, champion, and my thanks. | cant: Well... I can't. | no: Then we're both stuck ashore. Come back with it and I'll pour.

*Why this is in register:* §4 Workers and §3 device 6 — the closure is a commercial problem,
not a political one, and his single gripe is the standing stock ("Have you tried the wine?" and
"Lovely day for some wine!" are the models for plugging the product). §7 — "Nilsal" from a
second local, and §2 — the warm switch is a gift and three words of thanks, shorter than the
refusal that preceded it.

---

## Pilgrim of Ralos
examine: A pilgrim of Ralos, waiting on the tide and on the sun.
open: Any ships sailing today?
reply: Greetings, traveller. None that will carry the likes of us, sadly.
why: Why not?
whyNot: The Kingdom has closed the lanes to civilians. Only champions of the Colosseum may sail.
menu:
- How does one become a champion? -> By winning the Colosseum. Dizana's quiver is their token, and the guards know it well.
- Where were you hoping to sail? -> To the shrines across the water, while the light lasts. Ralos is patient, and I am learning to be.
- Will Ralos not open the way? -> Ralos lights the road. He does not argue with harbourmasters.
- I am a champion of the Colosseum. -> challenge: Then prove it, friend. Show me the quiver and I will believe you gladly. | shown: You show the pilgrim Dizana's quiver. | yes: Then Ralos has sent me a companion. Go safely, champion. | cant: Well... I can't. | no: Then we wait together. Ralos keep you until the lanes open.

*Why this is in register:* §4 Priests, built on the Monk template flagged in §7 as the only
available source — formal greeting, the god's virtue stated rather than his power, patience as a
discipline, a blessing in place of a goodbye ("Peace brother"). §3 device 11 — exactly one dry
pious aside, aimed at his own faith's limits, never at the player.

---

## Mysterious Old Man
examine: An old man in a dark hood. He does not appear to be waiting for a ship.
open: Any ships sailing today?
reply: Not for you, no. Not for me either, though I have my own ways of travelling.
why: Why not?
whyNot: The Kingdom closed the lanes to civilians. Only champions of the Colosseum sail.
menu:
- How does one become a champion? -> Win the Colosseum. They give you Dizana's quiver, and every guard on this coast knows it.
- What ways of travelling? -> Mine tend to find me rather than the other way round. Yours need a ship.
- Who are you? -> An old man on a dock. Ask me again in a year and you'll get a different answer.
- I am a champion of the Colosseum. -> challenge: Then show me. Champions carry a quiver I would know at a glance. | shown: You show the old man Dizana's quiver. | yes: So they do. Sail well, and tell no one I was here. | cant: Well... I can't. | no: I thought as much. Come and find me when you have it.

*Why this is in register:* §4 Hermits and §8 role sheet — mystery over exposition, never a
straight answer about himself, and the random-event nod carried entirely by "my own ways of
travelling" and the verbatim "tell no one I was here" rather than by any statement about the
game (§3 device 12's ceiling). §2 — even his closing line leaves the door hinged, and "I thought
as much" doubts the claim the player just withdrew, not the player.
