# Charter Dock Stand-In Conversations — v2

Six personas for the charter-dock stand-in NPC. The player always opens with
"Any ships sailing today?" and follows with "Why not?". One menu each, no loops, every option
ends the conversation.

Only the Harbourmaster mentions the Colosseum, and only when asked directly. The other four
give a reason and stop. Some of them are just jokes.

Field key: `examine` is not spoken. `reply` / `whyNot` answer the two fixed player lines.
Champion row: `challenge` (NPC push-back) | `shown` (narration) | `yes` (has quiver) |
`cant` (player admits it) | `no` (NPC closes the door).

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
- Who closed the lanes? -> The Queen, I'd expect. Nobody writes to me.
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
- I've earned my passage. -> challenge: none | shown: The fisher looks at the quiver on your back. | yes: Right you are. Mind my nets. | cant: Well... I can't. | no: Doesn't look like it.

Modelled on: "Well given how good today's catch was, I'd say it's going pretty well!" — Fisher, Transcript:Fisher

---

## Vintner
examine: An Aldarin vintner. He is guarding his crates.
open: Any ships sailing today?
reply: Nilsal! None at all. My wine is going nowhere.
why: Why not?
whyNot: The lanes are closed. Wine doesn't improve on a dock.
menu:
- Who closed them? -> I didn't ask. I was busy panicking.
- How much wine is stuck here? -> Forty crates. I've started drinking it.
- Is the wine any good? -> Better than it'll be tomorrow.
- I've earned passage out of here. -> challenge: none | shown: You show the vintner the quiver. | yes: Take a bottle. Take two. | cant: Well... I can't. | no: Then neither of us is leaving.

Modelled on: "Tricky? Tricky!" — Trader Crewmember, Transcript:Trader_Crewmember

---

## Pilgrim of Ralos
examine: A pilgrim of Ralos. He has been standing in the sun a while.
open: Any ships sailing today?
reply: I couldn't say. I don't watch the water.
why: Why not?
whyNot: Something about the lanes being closed. I wasn't really listening.
menu:
- Aren't you waiting for a ship? -> I'm waiting. The ship is optional.
- Who told you they were closed? -> A man with a hat. He seemed sure.
- Does Ralos know a way across? -> Ralos gives light. Not boats.
- I've earned my passage. -> challenge: none | shown: You show the pilgrim the quiver. | yes: Oh. Well done, then. | cant: Well... I can't. | no: Then stand here. The sun's lovely.

Modelled on: "Ah, a very noble profession." — Citizen, Transcript:Citizen_(Civitas_illa_Fortis)

---

## Mysterious Old Man
examine: An old man in a dark hood. He was not here a moment ago.
open: Any ships sailing today?
reply: Not for you. Not for me either.
why: Why not?
whyNot: The lanes are shut. I don't use them anyway.
menu:
- How do you get about, then? -> I turn up. It's served me well.
- Who are you? -> Nobody worth writing down.
- Are you waiting for someone? -> I was. He didn't come.
- I've earned my passage. -> challenge: none | shown: The old man glances at your quiver. | yes: So you have. Off you go. | cant: Well... I can't. | no: Few have. Tell no one I asked.

Modelled on: "Ok suit yourself." — Border Guard, Transcript:Border_Guard
