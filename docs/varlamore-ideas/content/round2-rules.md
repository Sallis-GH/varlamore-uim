# Round-two review rules

You are a fresh, independent reviewer. Scripts for dock characters in Varlamore (Old School RuneScape) have been drafted, fact-checked once, and revised. You judge the revised versions against the real game and fix what still fails.

Base directory: `C:\dev\varlamore-uim\.claude\worktrees\native-npc-standins\docs\varlamore-ideas\`

## Calibrate first (WebFetch, verbatim lines under 125 chars)
At least five of: https://oldschool.runescape.wiki/w/Transcript:Customs_officer , https://oldschool.runescape.wiki/w/Transcript:Border_Guard_(Varlamore) , https://oldschool.runescape.wiki/w/Transcript:Fisher , https://oldschool.runescape.wiki/w/Transcript:Vineyard_worker , https://oldschool.runescape.wiki/w/Transcript:Sailor_(Varlamore) , https://oldschool.runescape.wiki/w/Transcript:Trader_Crewmember , https://oldschool.runescape.wiki/w/Transcript:Citizen_(Civitas_illa_Fortis) , https://oldschool.runescape.wiki/w/Transcript:Cook's_Assistant , https://oldschool.runescape.wiki/w/Transcript:Pirate's_Treasure

## Then read
- `content/writer-rules.md` (the standard the writers had)
- `content/characters.json` (the briefs) for your characters
- Every `*.json` in your character folders EXCEPT `approved.json`. Do NOT read the first-round `characters/*-review.md` files; you are meant to be independent.

## Judge every line (intro lines, option labels, option ending lines, premises)
FAIL reasons, five words or fewer: poetic (reads as prose, lyrical cadence, metaphor), not a reply (does not answer the previous line), hand-holding (steers the player toward the Colosseum, quiver or registrar when the player did not ask, in a pre variant), lore framing (states a kingdom-wide ban rather than a personal, conditional refusal), fact (contradicts the wiki or the brief), too long (over 14 words or 90 chars; option over 36 chars), wrong register (modern idiom, sarcasm at the player, explained joke, two punctuation devices in one line), not funny (a joke that does not land), other.
The owner's benchmark for tone is the guard in `scripts/guard/approved.json`: short, dismissive, funny because of what is not said. Post variants MUST still end up pointing the player toward the harbour in the capital, in character.

## Deliver
1. `characters/round2-<dock>.md`: per file, a table of FAIL rows only (file, variant, field, line, reason, replacement), then a pass rate per file, then one sentence per file on whether it sounds like the game, then the three best lines you read across all files (quote them) and the three worst.
2. FIX every FAIL directly in the JSON files (replace the line with your replacement), keeping shape, order and counts identical. Validate each JSON after editing.

Do not dispatch subagents. Reply with only the review path and the pass rates per file before your fixes.
