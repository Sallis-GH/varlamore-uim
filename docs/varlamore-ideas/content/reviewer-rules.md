# Rules for script reviewers

You are reviewing dialogue drafts for ONE character who stands at a charter dock in Varlamore, written for a RuneLite plugin that shows the lines in Old School RuneScape's real chatbox. Two writers drafted independently from the same short brief. Your job: fact-check against the wiki, challenge the writing, and offer your own alternative.

## Read first
- The writer rules the drafts were written to: `content/writer-rules.md` (same folder as this file).
- Research on the docks and society: `research/docks-and-geography.md`.
- Real voice samples per role: `research/voice-samples.md`.
- Both drafts in `scripts/<character>/` (and `approved.json` if present, which is the owner's own approved version for comparison; do not review it, but you may refer to it as the tone the owner liked: short, dismissive, funny because of what is not said).

## Fact-check (use WebFetch on https://oldschool.runescape.wiki/ for anything the drafts assert)
Every named place, person, product, creature, price, distance, custom, or piece of history in the lines must be true to the wiki or be safely invented (a character's own life, unnamed people). Flag anything false or anachronistic (e.g. the Fremennik do not use Varlamorian words; Aldarin wine is real; "Picaria" and "Thurid" are real Sunset Coast NPCs; the Kualti are the royal guard; the capital is Civitas illa Fortis and its harbour is the Cothon, on the coast, not inland). Also check the character could plausibly know what they say.

## Lore framing (important)
The wiki shows Varlamore never banned outbound travel. The gate must read as personal and conditional: no captain will risk an unproven outsider, the registrar won't vouch yet. Flag any line that states a kingdom-wide ban ("the crown says none of us sail") and give a fix that keeps the same beat.

## Challenge the writing
The owner rejected earlier drafts for being "written, poetic, more like prose than two people talking", for "forcing a hint toward what to do", and for "not fitting the OSRS theme". Judge every line by the game's register: 3-14 words, contractions, fragments, one joke beat, no lyrical cadence, no explained jokes, narration flat and present tense ("The fisher doesn't look up."). A conversation that is purely a joke with no guidance is good. Options must be plain sentences under 36 characters.

## Deliver two files
1. `characters/<character>-review.md`: a table with one row per line in both drafts (writer, variant, field, line, verdict PASS/FAIL, reason in five words or fewer: fact, poetic, not a reply, hand-holding, too long, wrong register, not funny, lore framing, other) and a replacement line for every FAIL. Then, per draft, a pass rate and one sentence on whether the whole thing sounds like the game. Then a "Challenges" section: three things you would push back on in how the character was conceived or written, with what you'd do instead.
2. `scripts/<character>/reviewer.json`: your OWN alternative, one `pre` variant and one `post` variant in the exact JSON shape from the writer rules, author `"reviewer"`. Write it after the review so it avoids the failures you found. Validate the JSON.

Keep every quote under 125 characters. Do not dispatch subagents. Reply with only the two file paths and the two pass rates.
