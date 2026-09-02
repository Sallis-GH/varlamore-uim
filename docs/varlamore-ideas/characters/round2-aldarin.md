# Round two: Aldarin dock (Vineyard worker, Boatman)

Independent second-pass review. Calibrated against the wiki transcripts for Customs officer, Border Guard (Varlamore), Fisher, Vineyard worker, Sailor (Varlamore), Trader Crewmember, Citizen (Civitas illa Fortis), Cook's Assistant and Pirate's Treasure, plus the Aldarin, Mistrock and Fortis Cothon articles for geography.

Judged units = premises + intro lines + option labels + option ending lines. No line in any of the six files breaks the length caps (14 words / 90 chars, options 36 chars), so every FAIL below is a judgement call, not arithmetic.

Geography checks that several lines depend on, all confirmed: Aldarin is "a large island off the south-west coast of Varlamore"; Mistrock is a dwarven settlement on that same island; the Fortis Cothon is "a large harbour on the north side of Civitas illa Fortis"; the island's winery is Moonrise. So "It's an island, mind.", "As far as Mistrock. That's still the island." and "North side of the capital." all stand.

---

## scripts/vineyard/writer-a.json

| File | Variant | Field | Line | Reason | Replacement |
|---|---|---|---|---|---|
| writer-a.json | Foreman's temper (pre) | lines[2] N | "He's about somewhere. What do you want?" | not a reply | "The foreman, I mean. He's about somewhere." |

The player has just said exactly what they want, so asking again is dead air; "he" also had no antecedent, which left the option label "Why's he so sour?" hanging. Naming the foreman fixes both in one line.

**Pass rate: 52/53 (98%).**

Sounds like the game: yes — the lost count, "Then ask a sailor. I pick grapes." and "You won't regret it if you try some." sit right next to the real Vineyard worker transcript.

## scripts/vineyard/writer-b.json

| File | Variant | Field | Line | Reason | Replacement |
|---|---|---|---|---|---|
| writer-b.json | The Quiver (post) | options[2].lines[0] N | "Don't thank me, thank the grapes. Timoiva." | not funny | "Thank me when you're on a ship. Timoiva." |

Whimsy rather than dryness — the grapes gag is the writer enjoying himself, not the character deflecting. The replacement keeps the local sign-off and the shrug.

**Pass rate: 48/49 (98%).**

Sounds like the game: yes — "Turned green before we left the dock." and "Bellows like a bull if the grapes bruise." are the best-observed lines in either character's folder.

## scripts/vineyard/reviewer.json

| File | Variant | Field | Line | Reason | Replacement |
|---|---|---|---|---|---|
| reviewer.json | Not my crates (pre) | options[1].lines[0] N | "Wine. Eclipse red, the dear stuff." | fact | "Wine. Moonrise red, the dear stuff." |

Aldarin's winery is Moonrise (and the brief names the Moonrise foreman); "Eclipse" is invented and reads as a stray Perilous Moons echo.

**Pass rate: 31/32 (97%).**

Sounds like the game: yes, and it is the tightest writing of the three — but note it ships only two variants (one pre, one post) where the standard asks for two pre; I have not added one, since fixes must keep counts identical.

## scripts/boatman/writer-a.json

| File | Variant | Field | Line | Reason | Replacement |
|---|---|---|---|---|---|
| writer-a.json | Tide's out (pre) | options[3] label | "Timoiva, then." | player isn't a local | "I'll be off, then." |
| writer-a.json | Name in a book (post) | lines[1] X | "The boatman stops coiling the rope." | contradicts own premise | "The boatman drops the tarpaulin and looks up." |

The local words are for Varlamore locals; the player is the outerlander trying to leave, and putting Timoiva in their mouth breaks the one rule the vocabulary has. The narration box had him coiling rope while the premise had him under a tarpaulin.

**Pass rate: 54/56 (96%).**

Sounds like the game: yes — this is the most confident voice in the set, a smuggler who answers everything sideways without ever naming a crime.

## scripts/boatman/writer-b.json

| File | Variant | Field | Line | Reason | Replacement |
|---|---|---|---|---|---|
| writer-b.json | Wine Crates (pre) | lines[3] N | "Even if I had. I don't cross open water." | other (broken grammar) | "Even if I hadn't. I don't cross open water." |
| writer-b.json | Wine Crates (pre) | options[1].lines[1] N | "Never been myself. Don't much care to." | not a reply | "They'll say no slower than I did." |
| writer-b.json | Rough Company (pre) | options[0].lines[1] N | "Tetamo. Their boats, their rules." | local word misused | "Take it up with them, not me." |
| writer-b.json | Proof Enough (post) | options[2].lines[0] N | "Didn't do much. Mind the crates." | post must point to harbour | "Didn't do much. Ask at the capital harbour." |
| writer-b.json | Proof Enough (post) | options[3].lines[1] N | "Now go bother someone who knows more." | post must point to harbour | "Now go bother the harbour in the capital." |

"Even if I had" is missing its negative — he has wine to move, so the concession has to be "even if I hadn't". "Never been" had no place to refer to. Tetamo is "oh no / damn"; there is no dismay in "their boats, their rules", so it reads as a word dropped in for local flavour. And the post variant's intro never names the harbour, so two of its four endings sent a quiver-wearing champion away with nowhere to go — both endings now point at the capital in his own voice.

**Pass rate: 46/51 (90%).**

Sounds like the game: mostly — "Then don't say it out loud." and "Suit yourself. It's an island, mind." are excellent, but the two pre variants answer "Why not?" with nearly the same line, and it is the only file where the post variant failed its one job.

## scripts/boatman/reviewer.json

No FAIL rows.

**Pass rate: 34/34 (100%).**

Sounds like the game: yes — "Ask the label." / "The label says wine." / "There you go." is the closest anything here gets to the approved guard, and the post variant justifies the boatman knowing about the registrar before he uses the word.

---

## Three best lines

1. "Nilsal. No." — boatman/writer-a, Tide's out. A greeting and a refusal in three syllables; this is the guard's register exactly.
2. "Wine's what's written on them." — boatman/writer-a, Coves not kingdoms. Funny because of what is not said.
3. "Last one said double. He swam back." — boatman/reviewer, Coast work. Answers the bribe, threatens nothing, closes the option.

## Three worst lines

1. "He's about somewhere. What do you want?" — vineyard/writer-a. Asks the player a question they have just answered, about a person who has not been mentioned.
2. "Never been myself. Don't much care to." — boatman/writer-b. Never been where? The line refers to nothing in the conversation.
3. "Don't thank me, thank the grapes. Timoiva." — vineyard/writer-b. A cute line in a folder that works best when nobody is trying to be cute.
