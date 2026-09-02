# Varlamore UIM ideas package

Everything produced in the overnight ideation run: dialogue drafts for the dock stand-ins, proposals for the NPC who unlocks the charter ships, plugin feature ideas, and an interactive page to score all of it.

## Open the page

Double-click `index.html`. It is a single self-contained file; no server needed. Scores and notes save in the browser as you type (localStorage). When done, open **Your scores** at the bottom, press **Copy scores**, and paste the text back into the chat.

Score scale: 1 useless, 100 masterpiece. Every line, option, idea and each script's **Overall** box has a slot. If you are short on time, score the **Overall** boxes and the **editor's pick** tab for each character.

## What's inside

| Path | What |
|---|---|
| `index.html` | The scoring page (built; do not edit by hand) |
| `index.template.html` + `build.py` | Template and builder. `python build.py` regenerates `index.html` from the content below |
| `content/overview.json`, `content/unlock.json`, `content/characters.json`, `content/plugin.json` | Page copy, unlocker candidates, character briefs, plugin proposals |
| `content/unlocker-scripts.json` | The four unlocker candidates' exchanges (with and without the quiver) |
| `content/writer-rules.md`, `reviewer-rules.md`, `round2-rules.md` | The exact instructions writers and reviewers worked from |
| `scripts/<character>/` | Dialogue drafts: `writer-a.json`, `writer-b.json` (independent writers), `reviewer.json` (fact-checker's own alternative), `editors-pick.json` (best-of assembly), and for the guard `approved.json` (the version already approved in-game) |
| `characters/*-review.md` | First-round fact-check and challenge per character, with every failing line and its replacement |
| `characters/round2-*.md` | Second, independent review round per dock, plus fixes applied |
| `characters/unlocker-review.md`, `characters/plugin-review.md`, `characters/page-review.md` | Reviews of the unlocker exchanges, the plugin proposals (facts, RuneLite feasibility, fun), and the page itself |
| `research/*.md` | Wiki research: docks and geography, Colosseum and authority, items and shops, progression and milestones, real NPC voice samples |

## How it was made

1. Five research agents mined the OSRS wiki.
2. Six characters were chosen from what actually lives at each dock (Sunset Coast is a Fremennik village; Aldarin is vineyards over a rough coast; Fortis Cothon is the capital's harbour).
3. Twelve writers drafted independently from a brief of a few sentences, the place, and one lore rule. No existing lines, no plugin details.
4. Six fact-checkers verified names, places and plausibility on the wiki, graded every line against the game's register, and wrote their own alternative.
5. Writers revised. Three fresh reviewers re-graded everything and fixed the residue in place (final pass rates 90 to 100%).
6. A plugin-ideas author wrote 47 proposals from the research; a challenger spot-checked 34 wiki facts, corrected 6, flagged feasibility, and added 5 ideas.
7. Editors assembled an editor's pick per character.
8. The page was checked in Chrome at desktop and 800px widths for overflow, clipping and console errors, then reviewed by a separate agent.

## Things worth acting on regardless of scores

- **Fairy rings are an open escape route.** AIS, AJP and CKQ are inside Varlamore and nothing blocks travel from them. See *Other ideas: Fairy ring lockdown*.
- **The lore constraint.** The wiki shows Varlamore never closed outbound travel; the gate must be personal (an official who won't vouch for an unproven outsider), not a kingdom ban. All scripts follow that.
- **Children of the Sun starts in Varrock.** Every Varlamore quest requires it, so the lock needs a pre-lock exception.
