# Review — dock-conversations-v2 (Reviewer A)

Judged against nine wiki transcripts (Customs officer, Border Guard, Guard, Fisher,
Sailor (Varlamore), Citizen (Aldarin), Monk, Wise Old Man, Children of the Sun).

Structural finding that drives most failures: criterion 2 allows **one** non-guard persona
to mention the Colosseum or the quiver. The Harbourmaster is that persona. The Fisher,
Vintner, Pilgrim and Mysterious Old Man each carry a full quiver-check row as well. Those
four rows fail as a block, not line by line — the replacement is to delete the row and give
the persona a fourth option that is just a joke.

---

## 1. Line-by-line

### Fortis Guard (benchmark — unchanged, all PASS)

| Field | Line | Verdict | Reason | Replacement |
|---|---|---|---|---|
| examine | A guard of Civitas illa Fortis. He has been standing here a long time. | PASS | | |
| reply | Not for you. | PASS | | |
| whyNot | Next question. | PASS | | |
| opt1 text | Who is allowed on, then? | PASS | | |
| opt1 reply | Champions. | PASS | | |
| opt2 text | Can't you make an exception? | PASS | | |
| opt2 reply | No. | PASS | | |
| opt3 text | Do you say anything other than no? | PASS | | |
| opt3 reply | No. | PASS | | |
| opt4 text | What if I'm a champion? | PASS | | |
| shown | The guard notices the quiver on your back. | PASS | | |
| yes | Fine. | PASS | | |
| no | You're not. | PASS | | |

Note: four of these replies sit under the stated 3-word floor. The transcripts back the guard,
not the floor ("Ok." — Monk; "Mostly." — Children of the Sun). The floor is the rule that
should bend, not the benchmark.

### Harbourmaster

| Field | Line | Verdict | Reason | Replacement |
|---|---|---|---|---|
| examine | The harbourmaster. He looks like he'd rather be sitting down. | PASS | | |
| reply | Sailing, yes. Carrying you, no. | PASS | | |
| whyNot | The Kingdom closed the lanes. I just tell people about it. | PASS | | |
| opt1 text | Who closed the lanes? | PASS | | |
| opt1 reply | The Queen, I'd expect. Nobody writes to me. | FAIL | paperwork gag, duplicate joke | The Queen, most likely. Well above me. |
| opt2 text | When will they open again? | PASS | | |
| opt2 reply | When someone tells me. Nobody has yet. | PASS | | |
| opt3 text | Who is allowed through? | PASS | | |
| opt3 reply | Champions of the Colosseum. That's the list. | PASS | | |
| opt4 text | I'm a champion of the Colosseum. | PASS | | |
| challenge | Prove it, then. | PASS | | |
| shown | You show the harbourmaster your quiver. | PASS | | |
| yes | Right. Off you go. | PASS | | |
| cant | Well... I can't. | PASS | | |
| no | Then it's still no. | PASS | | |

### Fisher

| Field | Line | Verdict | Reason | Replacement |
|---|---|---|---|---|
| examine | A fisher from the Sunset Coast. Her nets need mending. | PASS | | |
| reply | Nilsal. Not that I've heard, no. | PASS | | |
| whyNot | The Kingdom shut the lanes. Doesn't stop the fish. | PASS | | |
| opt1 text | Who shut them? | PASS | | |
| opt1 reply | Someone in the capital. Never met them. | PASS | | |
| opt2 text | How's the fishing? | PASS | | |
| opt2 reply | Better since the big boats stopped. | PASS | | |
| opt3 text | Could you row me out? | PASS | | |
| opt3 reply | No. I'd have to stop fishing. | PASS | | |
| opt4 text | I've earned my passage. | FAIL | hand-holding | Mind if I wait here? |
| shown | The fisher looks at the quiver on your back. | FAIL | hand-holding | Wait where you like. Mind the nets. |
| yes | Right you are. Mind my nets. | FAIL | hand-holding | (remove field) |
| cant | Well... I can't. | FAIL | hand-holding | (remove field) |
| no | Doesn't look like it. | FAIL | hand-holding | (remove field) |

### Vintner

| Field | Line | Verdict | Reason | Replacement |
|---|---|---|---|---|
| examine | An Aldarin vintner. He is guarding his crates. | PASS | | |
| reply | Nilsal! None at all. My wine is going nowhere. | PASS | | |
| whyNot | The lanes are closed. Wine doesn't improve on a dock. | FAIL | poetic | The lanes are closed. My crates are stuck here. |
| opt1 text | Who closed them? | PASS | | |
| opt1 reply | I didn't ask. I was busy panicking. | PASS | | |
| opt2 text | How much wine is stuck here? | PASS | | |
| opt2 reply | Forty crates. I've started drinking it. | PASS | | |
| opt3 text | Is the wine any good? | PASS | | |
| opt3 reply | Better than it'll be tomorrow. | PASS | | |
| opt4 text | I've earned passage out of here. | FAIL | hand-holding | Can I buy a bottle? |
| shown | You show the vintner the quiver. | FAIL | hand-holding | Buy? I'm nearly giving it away. |
| yes | Take a bottle. Take two. | FAIL | hand-holding | (remove field) |
| cant | Well... I can't. | FAIL | hand-holding | (remove field) |
| no | Then neither of us is leaving. | FAIL | hand-holding | (remove field) |

### Pilgrim of Ralos

| Field | Line | Verdict | Reason | Replacement |
|---|---|---|---|---|
| examine | A pilgrim of Ralos. He has been standing in the sun a while. | PASS | | |
| reply | I couldn't say. I don't watch the water. | PASS | | |
| whyNot | Something about the lanes being closed. I wasn't really listening. | PASS | | |
| opt1 text | Aren't you waiting for a ship? | PASS | | |
| opt1 reply | I'm waiting. The ship is optional. | FAIL | poetic | Not for a ship, no. I just stand here. |
| opt2 text | Who told you they were closed? | PASS | | |
| opt2 reply | A man with a hat. He seemed sure. | PASS | | |
| opt3 text | Does Ralos know a way across? | PASS | | |
| opt3 reply | Ralos gives light. Not boats. | PASS | | |
| opt4 text | I've earned my passage. | FAIL | hand-holding | Doesn't the sun bother you? |
| shown | You show the pilgrim the quiver. | FAIL | hand-holding | That's rather the point. |
| yes | Oh. Well done, then. | FAIL | hand-holding | (remove field) |
| cant | Well... I can't. | FAIL | hand-holding | (remove field) |
| no | Then stand here. The sun's lovely. | FAIL | hand-holding | (remove field) |

### Mysterious Old Man

| Field | Line | Verdict | Reason | Replacement |
|---|---|---|---|---|
| examine | An old man in a dark hood. He was not here a moment ago. | PASS | | |
| reply | Not for you. Not for me either. | FAIL | copies guard's signature line | None today. None tomorrow either. |
| whyNot | The lanes are shut. I don't use them anyway. | PASS | | |
| opt1 text | How do you get about, then? | PASS | | |
| opt1 reply | I turn up. It's served me well. | PASS | | |
| opt2 text | Who are you? | PASS | | |
| opt2 reply | Nobody worth writing down. | FAIL | wrong register | Nobody important. Just an old man. |
| opt3 text | Are you waiting for someone? | PASS | | |
| opt3 reply | I was. He didn't come. | PASS | | |
| opt4 text | I've earned my passage. | FAIL | hand-holding | Will you be here later? |
| shown | The old man glances at your quiver. | FAIL | hand-holding | Almost certainly not. |
| yes | So you have. Off you go. | FAIL | hand-holding | (remove field) |
| cant | Well... I can't. | FAIL | hand-holding | (remove field) |
| no | Few have. Tell no one I asked. | FAIL | hand-holding | (remove field) |

---

## 2. Persona verdicts

- **Fortis Guard** — Yes; nothing to fix, and it is the only conversation in the draft where
  every reply is shorter than the question that prompted it.
- **Harbourmaster** — Yes; biggest fix is that options 1 and 2 tell the same "nobody tells me
  anything" joke twice, so one of them has to stop being a joke.
- **Fisher** — Yes for the first three exchanges, no for the fourth; biggest fix is deleting
  the quiver row so she stays a fisher who has opinions about big boats.
- **Vintner** — Yes apart from two lines; biggest fix is the whyNot, which is a written
  wine-ageing pun rather than something a panicking merchant says out loud.
- **Pilgrim of Ralos** — Mostly yes; biggest fix is "The ship is optional", which is a koan,
  not an answer — the rest of him is genuinely funny and plain.
- **Mysterious Old Man** — No; biggest fix is that he opens by reciting the guard's line and
  then answers "Who are you?" with a phrase no one has ever said aloud.

---

## 3. Register the draft should be hitting

1. "You're not getting on this ship then." — Customs officer, Transcript:Customs_officer
2. "Sorry, but I have lots of work to do before we head back out to sea." — Sailor (Varlamore), Transcript:Sailor_(Varlamore)
3. "Adventure? Sounds dangerous. I'll be staying right here, thank you very much!" — Transcript:Children_of_the_Sun

All three are flat, plain-worded, and refuse or deflect the player without a flourish. None of
them contains a general truth about the world.

---

## 4. Pass rates

| Persona | Pass | Lines | Rate |
|---|---|---|---|
| Fortis Guard | 13 | 13 | 100% |
| Harbourmaster | 14 | 15 | 93% |
| Fisher | 9 | 14 | 64% |
| Vintner | 8 | 14 | 57% |
| Pilgrim of Ralos | 8 | 14 | 57% |
| Mysterious Old Man | 7 | 14 | 50% |
| **Overall** | **59** | **84** | **70%** |
