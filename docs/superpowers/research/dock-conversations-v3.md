# Charter Dock Stand-In Conversations — v3

Six personas for the charter-dock stand-in NPC. The player always opens with
"Any ships sailing today?" and follows with "Why not?". One menu each, no loops, every option
ends the conversation.

Only the Fortis Guard and the Harbourmaster mention the Colosseum or check the quiver. The
Fisher, Vintner, Pilgrim and Old Man have no champion row at all — their fourth option is just
a joke that ends. None of them controls access to a ship, so none of them behaves as if it did.

Field key: `examine` is not spoken. `reply` / `whyNot` answer the two fixed player lines.
Where a champion row exists: `challenge` (NPC push-back) | `shown` (narration) | `yes` (has
quiver) | `cant` (player admits it) | `no` (NPC closes the door).

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
- What if I'm a champion? -> challenge: none | shown: The guard notices the quiver on your back. | yes: Fine. | cant: none | no: You're not.

Modelled on: "You're not getting on this ship then." — Customs officer, Transcript:Customs_officer

---

## Harbourmaster
examine: The harbourmaster. He looks like he'd rather be sitting down.
open: Any ships sailing today?
reply: Sailing, yes. Carrying you, no.
why: Why not?
whyNot: The Kingdom closed the lanes. I just tell people about it.
menu:
- Who closed the lanes? -> The Queen, I'd expect. Not me.
- When will they open again? -> When someone tells me. Nobody has yet.
- Who is allowed through? -> Champions of the Colosseum. That's the list.
- I'm a champion of the Colosseum. -> challenge: Prove it, then. | shown: You show the harbourmaster your quiver. | yes: Right. Off you go. | cant: Well... I can't. | no: Then it's still no.

Modelled on: "I'm not that sort of customs officer." — Customs officer, Transcript:Customs_officer

---

## Fisher
examine: A fisher from the Sunset Coast. Her nets need mending.
open: Any ships sailing today?
reply: Nilsal. Not that I've heard, no.
why: Why not?
whyNot: The Kingdom shut the lanes. Doesn't stop the fish.
menu:
- Who shut them? -> Someone in the capital. Never met them.
- How's the fishing? -> Better since the big boats stopped.
- Could you row me out? -> No. I'd have to stop fishing.
- Mind if I wait here? -> Wait where you like. Mind the nets.

Modelled on: "Well given how good today's catch was, I'd say it's going pretty well!" — Fisher, Transcript:Fisher

---

## Vintner
examine: An Aldarin vintner. He is guarding his crates.
open: Any ships sailing today?
reply: Nilsal! None at all. My wine is going nowhere.
why: Why not?
whyNot: The lanes are closed. My crates are stuck here.
menu:
- Who closed them? -> I didn't ask. I was busy panicking.
- How much wine is stuck here? -> Forty crates. I've started drinking it.
- Is the wine any good? -> Better than it'll be tomorrow.
- Can I buy a bottle? -> Buy? I'm nearly giving it away.

Modelled on: "Tricky? Tricky!" — Trader Crewmember, Transcript:Trader_Crewmember

---

## Pilgrim of Ralos
examine: A pilgrim of Ralos. He has been standing in the sun a while.
open: Any ships sailing today?
reply: I couldn't say. I don't watch the water.
why: Why not?
whyNot: Something about the lanes being closed. I wasn't really listening.
menu:
- Aren't you waiting for a ship? -> No. I've nowhere to be.
- Who told you they were closed? -> A man with a hat. He seemed sure.
- Does Ralos know a way across? -> Ralos gives light. Not boats.
- Doesn't the sun bother you? -> That's rather the point.

Modelled on: "Ah, a very noble profession." — Tourist, Transcript:Tourist

---

## Mysterious Old Man
examine: An old man in a dark hood. He was not here a moment ago.
open: Any ships sailing today?
reply: No. Nothing's moving out there.
why: Why not?
whyNot: The lanes are shut. I don't use them anyway.
menu:
- How do you get about, then? -> I turn up. It's served me well.
- Who are you? -> Just an old man. Nothing more.
- Are you waiting for someone? -> I was. He didn't come.
- Will you be here later? -> Almost certainly not.

Modelled on: "Ok suit yourself." — Border Guard, Transcript:Border_Guard

---

## Changelog (v2 → v3)

26 lines changed. Guard unchanged, verbatim.

1. Harbourmaster/opt1 reply: "The Queen, I'd expect. Nobody writes to me." → "The Queen, I'd expect. Not me." — paperwork gag, and duplicated the next option's "nobody tells me" joke (A, B). Took B's shorter wording.
2. Fisher/opt4 label: "I've earned my passage." → "Mind if I wait here?" — hand-holding, and used verbatim in three personas (A, B). Took A's shorter label.
3. Fisher/shown: "The fisher looks at the quiver on your back." → removed — quiver named outside the one permitted persona (A, B).
4. Fisher/yes: "Right you are. Mind my nets." → "Wait where you like. Mind the nets." — champion row cut, so this becomes a plain ending line; the old line also reversed the refusal she gave one option earlier (B).
5. Fisher/cant: "Well... I can't." → removed — champion row cut (A).
6. Fisher/no: "Doesn't look like it." → removed — champion row cut (A).
7. Vintner/whyNot: "The lanes are closed. Wine doesn't improve on a dock." → "The lanes are closed. My crates are stuck here." — epigram, not speech (A, B). Took A's wording; B's "so my wine sits here" echoed the passing reply line above it.
8. Vintner/opt4 label: "I've earned passage out of here." → "Can I buy a bottle?" — hand-holding (A).
9. Vintner/shown: "You show the vintner the quiver." → removed — quiver named outside the one permitted persona (A, B).
10. Vintner/yes: "Take a bottle. Take two." → "Buy? I'm nearly giving it away." — champion row cut, so this becomes a plain ending line (A).
11. Vintner/cant: "Well... I can't." → removed — champion row cut (A).
12. Vintner/no: "Then neither of us is leaving." → removed — champion row cut (A).
13. Pilgrim/opt1 reply: "I'm waiting. The ship is optional." → "No. I've nowhere to be." — a koan, not an answer (A, B). Took B's shorter version.
14. Pilgrim/opt4 label: "I've earned my passage." → "Doesn't the sun bother you?" — hand-holding, repeated label (A, B). Took A's shorter pair.
15. Pilgrim/shown: "You show the pilgrim the quiver." → removed — quiver named outside the one permitted persona (A, B).
16. Pilgrim/yes: "Oh. Well done, then." → "That's rather the point." — champion row cut, so this becomes a plain ending line (A).
17. Pilgrim/cant: "Well... I can't." → removed — champion row cut (A).
18. Pilgrim/no: "Then stand here. The sun's lovely." → removed — champion row cut (A).
19. Old Man/reply: "Not for you. Not for me either." → "No. Nothing's moving out there." — copied the guard's signature line (A, B). Took B's shorter version.
20. Old Man/opt2 reply: "Nobody worth writing down." → "Just an old man. Nothing more." — wrong register, third writing gag in the draft (A, B). Took B's shorter version.
21. Old Man/opt4 label: "I've earned my passage." → "Will you be here later?" — hand-holding, repeated label (A, B). Took A's shorter pair.
22. Old Man/shown: "The old man glances at your quiver." → removed — quiver named outside the one permitted persona (A, B).
23. Old Man/yes: "So you have. Off you go." → "Almost certainly not." — champion row cut, so this becomes a plain ending line (A).
24. Old Man/cant: "Well... I can't." → removed — champion row cut (A).
25. Old Man/no: "Few have. Tell no one I asked." → removed — champion row cut; its second half also replied to nothing (A, B).
26. Pilgrim/attribution: Transcript:Citizen_(Civitas_illa_Fortis) → Transcript:Tourist — misattributed source line (B).

Rules that bent: both reviewers found the 3-word floor is contradicted by the approved guard
("Next question.", "Champions.", "No."), so the floor is treated as ~1 word for the guard and
observed everywhere else. The per-persona quiver check from the original brief is dropped for
the four personas that could not carry it without breaking the one-persona limit on quiver
mentions.
