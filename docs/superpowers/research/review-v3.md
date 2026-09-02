# Independent Review — dock-conversations-v3

Reviewer calibrated against verbatim transcripts: Customs officer, Border Guard, Fisher,
Wise Old Man, Citizen (Civitas illa Fortis), Cook's Assistant. (Transcript:Bartender returned
404; Cook's Assistant used as the sixth.)

Baseline register from those pages: NPCs answer plainly and a little loosely — "You're not
getting on this ship then.", "Ok suit yourself.", "The money goes to the city of Al-Kharid.",
"I'm afraid not, only the big cheeses get to dine with the Duke." Jokes are blunt, never
epigrammatic. Nothing in the corpus is built on a balanced X-yes/Y-no antithesis. That is the
one construction this draft still reaches for, and it is where both failures sit.

## Line-by-line

### Fortis Guard (benchmark — must be unchanged; verified unchanged)

| Field | Line | Verdict | Reason / replacement |
|---|---|---|---|
| examine | A guard of Civitas illa Fortis. He has been standing here a long time. | PASS | |
| reply | Not for you. | PASS | |
| whyNot | Next question. | PASS | |
| opt1 label | Who is allowed on, then? | PASS | |
| opt1 reply | Champions. | PASS | |
| opt2 label | Can't you make an exception? | PASS | |
| opt2 reply | No. | PASS | |
| opt3 label | Do you say anything other than no? | PASS | |
| opt3 reply | No. | PASS | |
| opt4 label | What if I'm a champion? | PASS | |
| opt4 shown | The guard notices the quiver on your back. | PASS | |
| opt4 yes | Fine. | PASS | |
| opt4 no | You're not. | PASS | |

### Harbourmaster

| Field | Line | Verdict | Reason / replacement |
|---|---|---|---|
| examine | The harbourmaster. He looks like he'd rather be sitting down. | PASS | |
| reply | Sailing, yes. Carrying you, no. | **FAIL** | Written antithesis, not speech. Replace: "Ships are sailing, but you can't board one." |
| whyNot | The Kingdom closed the lanes. I just tell people about it. | PASS | |
| opt1 label | Who closed the lanes? | PASS | |
| opt1 reply | The Queen, I'd expect. Not me. | PASS | |
| opt2 label | When will they open again? | PASS | |
| opt2 reply | When someone tells me. Nobody has yet. | PASS | |
| opt3 label | Who is allowed through? | PASS | |
| opt3 reply | Champions of the Colosseum. That's the list. | PASS | Player asked outright; permitted persona. |
| opt4 label | I'm a champion of the Colosseum. | PASS | 32 chars. |
| opt4 challenge | Prove it, then. | PASS | |
| opt4 shown | You show the harbourmaster your quiver. | PASS | |
| opt4 yes | Right. Off you go. | PASS | |
| opt4 cant | Well... I can't. | PASS | Player line; one ellipsis, no exclamation. |
| opt4 no | Then it's still no. | PASS | |

### Fisher

| Field | Line | Verdict | Reason / replacement |
|---|---|---|---|
| examine | A fisher from the Sunset Coast. Her nets need mending. | PASS | |
| reply | Nilsal. Not that I've heard, no. | PASS | Greeting matches Fisher/Citizen transcripts. |
| whyNot | The Kingdom shut the lanes. Doesn't stop the fish. | PASS | Contrast, but conversational. |
| opt1 label | Who shut them? | PASS | |
| opt1 reply | Someone in the capital. Never met them. | PASS | |
| opt2 label | How's the fishing? | PASS | |
| opt2 reply | Better since the big boats stopped. | PASS | |
| opt3 label | Could you row me out? | PASS | |
| opt3 reply | No. I'd have to stop fishing. | PASS | |
| opt4 label | Mind if I wait here? | PASS | |
| opt4 reply | Wait where you like. Mind the nets. | PASS | |

### Vintner

| Field | Line | Verdict | Reason / replacement |
|---|---|---|---|
| examine | An Aldarin vintner. He is guarding his crates. | PASS | |
| reply | Nilsal! None at all. My wine is going nowhere. | PASS | One exclamation, no ellipsis. |
| whyNot | The lanes are closed. My crates are stuck here. | PASS | Restates the reply's second half; tolerable, not wrong. |
| opt1 label | Who closed them? | PASS | |
| opt1 reply | I didn't ask. I was busy panicking. | PASS | |
| opt2 label | How much wine is stuck here? | PASS | |
| opt2 reply | Forty crates. I've started drinking it. | PASS | |
| opt3 label | Is the wine any good? | PASS | |
| opt3 reply | Better than it'll be tomorrow. | PASS | Weakest pass; aphorism-shaped but a merchant's joke. |
| opt4 label | Can I buy a bottle? | PASS | |
| opt4 reply | Buy? I'm nearly giving it away. | PASS | Question-echo is straight out of the corpus. |

### Pilgrim of Ralos

| Field | Line | Verdict | Reason / replacement |
|---|---|---|---|
| examine | A pilgrim of Ralos. He has been standing in the sun a while. | PASS | |
| reply | I couldn't say. I don't watch the water. | PASS | |
| whyNot | Something about the lanes being closed. I wasn't really listening. | PASS | Best line in the draft. |
| opt1 label | Aren't you waiting for a ship? | PASS | |
| opt1 reply | No. I've nowhere to be. | PASS | |
| opt2 label | Who told you they were closed? | PASS | |
| opt2 reply | A man with a hat. He seemed sure. | PASS | |
| opt3 label | Does Ralos know a way across? | PASS | |
| opt3 reply | Ralos gives light. Not boats. | **FAIL** | Aphoristic, scripture cadence. Replace: "He's never mentioned one to me." |
| opt4 label | Doesn't the sun bother you? | PASS | |
| opt4 reply | That's rather the point. | PASS | |

### Mysterious Old Man

| Field | Line | Verdict | Reason / replacement |
|---|---|---|---|
| examine | An old man in a dark hood. He was not here a moment ago. | PASS | The permitted random-event nod, kept gentle. |
| reply | No. Nothing's moving out there. | PASS | |
| whyNot | The lanes are shut. I don't use them anyway. | PASS | |
| opt1 label | How do you get about, then? | PASS | |
| opt1 reply | I turn up. It's served me well. | PASS | |
| opt2 label | Who are you? | PASS | |
| opt2 reply | Just an old man. Nothing more. | PASS | Slightly stagey; still in-register for a hooded stranger. |
| opt3 label | Are you waiting for someone? | PASS | |
| opt3 reply | I was. He didn't come. | PASS | |
| opt4 label | Will you be here later? | PASS | |
| opt4 reply | Almost certainly not. | PASS | |

## Would I believe it came from the game?

- **Fortis Guard** — Yes; it reads like the Customs officer's refusal branch and nothing in it is invented.
- **Harbourmaster** — Yes, once the opening line stops being a balanced epigram; everything after it is plain functionary speech.
- **Fisher** — Yes; the Nilsal greeting, the shrug about the capital and the nets all match the Sunset Coast fisher transcripts.
- **Vintner** — Yes; the panicking-merchant register and the "Buy?" echo are exactly how OSRS writes a flustered trader.
- **Pilgrim of Ralos** — Yes apart from the Ralos line, which is the one place the writing shows.
- **Mysterious Old Man** — Yes; short, unhelpful and faintly ominous without ever winking at the player.

## Compliance checks (whole draft)

- Length: longest NPC line is Pilgrim/whyNot at 66 chars, 10 words. No line exceeds 90 chars or 15 words.
- Options: longest is Guard/opt3 at 34 chars and Harbourmaster/opt4 at 32. All under 36.
- Punctuation: no line carries both an ellipsis and an exclamation; none carries two of either.
- Spelling: British throughout ("harbourmaster").
- Colosseum/quiver: named by the Guard and the Harbourmaster only — one non-guard persona, within the limit. Fisher, Vintner, Pilgrim and Old Man are clean.
- Hand-holding: none. The only Colosseum answer follows an explicit player question.
- No paperwork gags, no jokes at the player's expense outside the approved Guard, no fourth-wall break beyond the old man's examine and "I turn up."
- Guard verified unchanged against the benchmark.

## Pass rate

| Persona | Rows | Pass | Rate |
|---|---|---|---|
| Fortis Guard | 13 | 13 | 100% |
| Harbourmaster | 15 | 14 | 93% |
| Fisher | 11 | 11 | 100% |
| Vintner | 11 | 11 | 100% |
| Pilgrim of Ralos | 11 | 10 | 91% |
| Mysterious Old Man | 11 | 11 | 100% |
| **Overall** | **72** | **70** | **97%** |
