# Review B — dock-conversations-v2

Independent review against real OSRS transcripts. Calibration set (fetched before reading the
draft): Cabin Fever, Rum Deal, Pirate's Treasure, Dragon Slayer I, Twilight's Promise, Sea Slug,
Tourist, Guard. `Transcript:Shopkeeper` and `Transcript:Bartender` both return 404 — the draft
should not cite either.

Mechanical check ran clean: every NPC line is under 90 characters and 15 words, every option
under 36 characters, no line carries both an ellipsis and an exclamation, spelling is British
throughout. **The only line in the draft that breaks the 3-word floor is the Fortis Guard's
"Next question." (2 words).** Since the Guard is the approved benchmark, the floor is the thing
that is wrong — lower it to 1 word. Nothing else in the draft is failed on length.

Headline finding: the draft's own preamble claims "Only the Harbourmaster mentions the
Colosseum." True of the word, false of the thing. The **quiver is named in all six personas**,
including four that criterion 2 forbids. That is the review's largest block of failures.

---

## 1. Line-by-line verdicts

### Fortis Guard — benchmark, unchanged

| Field | Line | Verdict | Reason | Replacement |
|---|---|---|---|---|
| examine | A guard of Civitas illa Fortis. He has been standing here a long time. | PASS | — | — |
| reply | Not for you. | PASS | — | — |
| whyNot | Next question. | PASS | — | — |
| opt1 label | Who is allowed on, then? | PASS | — | — |
| opt1 reply | Champions. | PASS | — | — |
| opt2 label | Can't you make an exception? | PASS | — | — |
| opt2 reply | No. | PASS | — | — |
| opt3 label | Do you say anything other than no? | PASS | — | — |
| opt3 reply | No. | PASS | — | — |
| opt4 label | What if I'm a champion? | PASS | — | — |
| shown | The guard notices the quiver on your back. | PASS | — | — |
| yes | Fine. | PASS | — | — |
| no | You're not. | PASS | — | — |

### Harbourmaster

| Field | Line | Verdict | Reason | Replacement |
|---|---|---|---|---|
| examine | The harbourmaster. He looks like he'd rather be sitting down. | PASS | — | — |
| reply | Sailing, yes. Carrying you, no. | PASS | — | — |
| whyNot | The Kingdom closed the lanes. I just tell people about it. | PASS | — | — |
| opt1 label | Who closed the lanes? | PASS | — | — |
| opt1 reply | The Queen, I'd expect. Nobody writes to me. | **FAIL** | paperwork gag, duplicates next line | The Queen, I'd expect. Not me. |
| opt2 label | When will they open again? | PASS | — | — |
| opt2 reply | When someone tells me. Nobody has yet. | PASS | — | — |
| opt3 label | Who is allowed through? | PASS | — | — |
| opt3 reply | Champions of the Colosseum. That's the list. | PASS | — | — |
| opt4 label | I'm a champion of the Colosseum. | PASS | — | — |
| challenge | Prove it, then. | PASS | — | — |
| shown | You show the harbourmaster your quiver. | PASS | — | — |
| yes | Right. Off you go. | PASS | — | — |
| cant | Well... I can't. | PASS | — | — |
| no | Then it's still no. | PASS | — | — |

### Fisher

| Field | Line | Verdict | Reason | Replacement |
|---|---|---|---|---|
| examine | A fisher from the Sunset Coast. Her nets need mending. | PASS | — | — |
| reply | Nilsal. Not that I've heard, no. | PASS | — | — |
| whyNot | The Kingdom shut the lanes. Doesn't stop the fish. | PASS | — | — |
| opt1 label | Who shut them? | PASS | — | — |
| opt1 reply | Someone in the capital. Never met them. | PASS | — | — |
| opt2 label | How's the fishing? | PASS | — | — |
| opt2 reply | Better since the big boats stopped. | PASS | — | — |
| opt3 label | Could you row me out? | PASS | — | — |
| opt3 reply | No. I'd have to stop fishing. | PASS | — | — |
| opt4 label | I've earned my passage. | **FAIL** | verbatim in three personas | Need a hand with the nets? |
| shown | The fisher looks at the quiver on your back. | **FAIL** | hand-holding | *(cut — see note)* From you? No. Thanks though. |
| yes | Right you are. Mind my nets. | **FAIL** | contradicts her own refusal | *(cut with branch)* Right you are. Hop in. |
| cant | Well... I can't. | PASS | — | — |
| no | Doesn't look like it. | PASS | — | — |

### Vintner

| Field | Line | Verdict | Reason | Replacement |
|---|---|---|---|---|
| examine | An Aldarin vintner. He is guarding his crates. | PASS | — | — |
| reply | Nilsal! None at all. My wine is going nowhere. | PASS | — | — |
| whyNot | The lanes are closed. Wine doesn't improve on a dock. | **FAIL** | poetic | The lanes are closed. So my wine sits here. |
| opt1 label | Who closed them? | PASS | — | — |
| opt1 reply | I didn't ask. I was busy panicking. | PASS | — | — |
| opt2 label | How much wine is stuck here? | PASS | — | — |
| opt2 reply | Forty crates. I've started drinking it. | PASS | — | — |
| opt3 label | Is the wine any good? | PASS | — | — |
| opt3 reply | Better than it'll be tomorrow. | PASS | — | — |
| opt4 label | I've earned passage out of here. | PASS | — | — |
| shown | You show the vintner the quiver. | **FAIL** | hand-holding | *(cut — see note)* Sell me a bottle, then. → You're the first sensible person today. |
| yes | Take a bottle. Take two. | PASS | — | — |
| cant | Well... I can't. | PASS | — | — |
| no | Then neither of us is leaving. | PASS | — | — |

### Pilgrim of Ralos

| Field | Line | Verdict | Reason | Replacement |
|---|---|---|---|---|
| examine | A pilgrim of Ralos. He has been standing in the sun a while. | PASS | — | — |
| reply | I couldn't say. I don't watch the water. | PASS | — | — |
| whyNot | Something about the lanes being closed. I wasn't really listening. | PASS | — | — |
| opt1 label | Aren't you waiting for a ship? | PASS | — | — |
| opt1 reply | I'm waiting. The ship is optional. | **FAIL** | poetic | No. I've nowhere to be. |
| opt2 label | Who told you they were closed? | PASS | — | — |
| opt2 reply | A man with a hat. He seemed sure. | PASS | — | — |
| opt3 label | Does Ralos know a way across? | PASS | — | — |
| opt3 reply | Ralos gives light. Not boats. | PASS | — | — |
| opt4 label | I've earned my passage. | **FAIL** | verbatim in three personas | How long have you been here? |
| shown | You show the pilgrim the quiver. | **FAIL** | hand-holding | *(cut — see note)* Since morning. Or the morning before. |
| yes | Oh. Well done, then. | PASS | — | — |
| cant | Well... I can't. | PASS | — | — |
| no | Then stand here. The sun's lovely. | PASS | — | — |

### Mysterious Old Man

| Field | Line | Verdict | Reason | Replacement |
|---|---|---|---|---|
| examine | An old man in a dark hood. He was not here a moment ago. | PASS | — | — |
| reply | Not for you. Not for me either. | **FAIL** | copies the guard's signature line | No. Nothing's moving out there. |
| whyNot | The lanes are shut. I don't use them anyway. | PASS | — | — |
| opt1 label | How do you get about, then? | PASS | — | — |
| opt1 reply | I turn up. It's served me well. | PASS | — | — |
| opt2 label | Who are you? | PASS | — | — |
| opt2 reply | Nobody worth writing down. | **FAIL** | third writing gag, over-crafted | Just an old man. Nothing more. |
| opt3 label | Are you waiting for someone? | PASS | — | — |
| opt3 reply | I was. He didn't come. | PASS | — | — |
| opt4 label | I've earned my passage. | **FAIL** | verbatim in three personas | Will you be here tomorrow? |
| shown | The old man glances at your quiver. | **FAIL** | hand-holding | *(cut — see note)* Doubt it. I don't stay put. |
| yes | So you have. Off you go. | PASS | — | — |
| cant | Well... I can't. | PASS | — | — |
| no | Few have. Tell no one I asked. | **FAIL** | second half replies to nothing | Few have. Best stay put. |

**Note on the four cut branches.** Criterion 2 allows exactly one non-guard persona to mention the
Colosseum or the quiver. The Harbourmaster is that persona. The Fisher, Vintner, Pilgrim and Old
Man must therefore lose the entire champion row — `challenge`, `shown`, `yes`, `cant`, `no` — and
their fourth option becomes an ordinary option. The replacement pairs above are those options.
Each is a joke that ends, with no guidance in it, which criterion 2 calls out as the good case.
This also removes the awkwardness of a fisher, a wine merchant and a pilgrim behaving as though
they were the ones controlling access to a ship.

---

## 2. Per-persona verdict

- **Fortis Guard** — Yes. Nothing to fix; it is the only conversation here that reads like the
  Customs officer, and the 3-word floor should be relaxed to accommodate it rather than the
  reverse.
- **Harbourmaster** — Yes. Biggest fix: kill the "nobody writes to me" clause so the same
  no-word-from-above joke does not land twice in consecutive options.
- **Fisher** — Yes, apart from the ending. Biggest fix: drop the quiver branch, whose `yes` line
  reverses the refusal she gave two options earlier.
- **Vintner** — Yes, and the panicking and the forty crates are the best jokes in the document.
  Biggest fix: "Wine doesn't improve on a dock" is an epigram, not speech — say it plainly.
- **Pilgrim of Ralos** — Yes. Biggest fix: "The ship is optional" is the most writerly line in the
  draft; the hat man and the Ralos quip already carry the character without it.
- **Mysterious Old Man** — No. Biggest fix: he is assembled from other people's lines (the Guard's
  opener, a third variation on the writing gag) and trails off instead of closing; give him his
  own opener and let his last line be a full stop.

Two cross-cutting problems the persona notes do not cover. First, four of five non-guard personas
open the same way — lanes-closed plus a quip — and three of them offer a near-identical "Who
closed them?" option. The player will hear one conversation five times. Give at least two of them
a different reason to be unhelpful. Second, `cant` is the byte-identical "Well... I can't." in all
five, which reads as a template rather than a character; vary it or move it to a shared fallback.

Also: the Pilgrim cites "Ah, a very noble profession." to `Transcript:Citizen_(Civitas_illa_Fortis)`.
That line is the **Tourist's** in `Transcript:Tourist`. Recheck every "Modelled on" attribution.

---

## 3. Three transcript lines that set the target register

1. **Customs officer** — "You're not getting on this ship then." (Transcript:Pirate's_Treasure)
   The refusal is the whole line. No reason, no softening, no follow-up.
2. **Ned** — "That old pile of junk? Last I heard, she wasn't seaworthy." (Transcript:Dragon_Slayer_I)
   Repeats the player's subject back, then answers. This is what "a direct reply" sounds like.
3. **Ennius Tullus** — "Doesn't look like much." (Transcript:Twilight's_Promise)
   Four words, complete, mildly rude, and it moves on. No line in this draft should be worked
   harder than this one.

---

## 4. Pass rate

| Persona | Pass | Total | Rate |
|---|---|---|---|
| Fortis Guard | 13 | 13 | 100% |
| Harbourmaster | 14 | 15 | 93% |
| Fisher | 11 | 14 | 79% |
| Vintner | 12 | 14 | 86% |
| Pilgrim of Ralos | 11 | 14 | 79% |
| Mysterious Old Man | 9 | 14 | 64% |
| **Overall** | **70** | **84** | **83%** |
