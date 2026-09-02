# Boatman — script review

Character: coastal boatman, Aldarin dock. Drafts: `scripts/boatman/writer-a.json`, `scripts/boatman/writer-b.json`.

## Wiki checks made

- Aldarin is "a large island off the south-west coast of Varlamore"; "its rocky south and west coasts are inhabited by dwarves and bandits." [Aldarin] — the brief is sound, and **you cannot walk off Aldarin**.
- Mistrock: "a dwarven settlement on the island of Aldarin", southern coast, bank and mine; most residents dislike humans. [Mistrock] — usable, and it is on the boatman's own run.
- Antonia runs the Sunset Coast–Aldarin boat for 20 coins. [Antonia] — a rival for short hops, unmentioned by either draft.
- Bandit (Varlamore) exists as an NPC, combat level 23, spawning inland (Mons Gratia, Quetzacalli, Ralos' Rise). No bandit spawns are documented on Aldarin's coast, but the Aldarin page asserts they live there, so "bandits down the coast" is safe.
- Smuggling is canon in Varlamore: in Twilight's Promise the player searches a weapons crate at the Fortis Cothon and learns "the name of the smuggling ship, the Fortis Spark." [Twilight's Promise] — a smuggler at a Varlamore dock is fully supported. Neither draft uses it.
- Dizana's quiver: "a quiver awarded to those who have proved their worth in the Colosseum", from Sol Heredit. [Dizana's quiver] — recognisable on sight, so "Colosseum quiver" is fine; naming it *Dizana's* quiver is a scholar's word, not a boatman's.
- Geography: the Cothon is on the **north** side of Civitas illa Fortis, and the capital is north-east of Aldarin. From this pier the capital is *up*, never *down*.
- Fortis Cothon, harbour registrar, ledger, the crown: registrar and ledger are the brief's own invention and carry no wiki conflict.

## Line-by-line

### Writer A

| Variant | Field | Line | Verdict | Reason | Replacement |
|---|---|---|---|---|---|
| Coves not kingdoms | lines[0] P | Any chance of a lift out of Varlamore? | PASS | | |
| Coves not kingdoms | lines[1] N | Out? I go along the coast, iknami. Not out. | PASS | | |
| Coves not kingdoms | lines[2] P | What's the difference? | PASS | | |
| Coves not kingdoms | lines[3] N | The difference is who's waiting at the other end. | PASS | | |
| Coves not kingdoms | lines[4] N | Coves, I know. Kingdoms, I don't. | FAIL | poetic | I know the coves. That's all. |
| Coves not kingdoms | opt1 label | What's in the crates? | PASS | | |
| Coves not kingdoms | opt1 N | Wine. | PASS | | |
| Coves not kingdoms | opt1 P | Just wine? | PASS | | |
| Coves not kingdoms | opt1 N | Wine's what's written on them. | PASS | | |
| Coves not kingdoms | opt2 label | I can pay you well. | PASS | | |
| Coves not kingdoms | opt2 N | Money's the easy part. | PASS | | |
| Coves not kingdoms | opt2 N | Getting stopped is the expensive part. | PASS | | |
| Coves not kingdoms | opt3 label | Who would stop us? | PASS | | |
| Coves not kingdoms | opt3 N | The crown keeps a ledger down in the capital. | FAIL | fact, capital is north | Someone in the capital, I hear. Never asked. |
| Coves not kingdoms | opt3 N | My boat's never been in a book. Keeping it that way. | PASS | | |
| Coves not kingdoms | opt4 label | Fair enough. | PASS | | |
| Coves not kingdoms | opt4 N | Mind the ropes on your way back up. | PASS | | |
| Tide's out | lines[0] P | Could you row me past the kingdom's waters? | PASS | | |
| Tide's out | lines[1] N | Nilsal. No. | PASS | | |
| Tide's out | lines[2] P | That was quick. | PASS | | |
| Tide's out | lines[3] N | Tide's out and so's my patience. | FAIL | poetic, written wordplay | Not on this tide. Not on any. |
| Tide's out | lines[4] N | Coast work only. I like my boat unburnt. | PASS | | |
| Tide's out | opt1 label | Who'd burn your boat? | PASS | | |
| Tide's out | opt1 N | The crown's got men for that sort of thing. | FAIL | lore framing, implies ban | Bandits, mostly. They watch the coves too. |
| Tide's out | opt1 N | Very thorough men. | PASS | | |
| Tide's out | opt2 label | Nobody would ever know. | PASS | | |
| Tide's out | opt2 N | Somebody always knows. | PASS | | |
| Tide's out | opt2 N | That's how I eat, most weeks. | PASS | | |
| Tide's out | opt3 label | Try the guards instead? | FAIL | not a reply, ambiguous | Don't the guards mind? |
| Tide's out | opt3 N | Half of them wave me through the cove at dusk. | PASS | | |
| Tide's out | opt3 P | And the other half? | PASS | | |
| Tide's out | opt3 N | The other half I go round. | PASS | | |
| Tide's out | opt4 label | Timoiva, then. | PASS | | |
| Tide's out | opt4 N | Mind the stone dust. Gets in everything down here. | PASS | | |
| Name in a book | lines[0] P | I still need a ship out of Varlamore. | PASS | | |
| Name in a book | lines[1] X | The boatman stops coiling the rope. | PASS | | |
| Name in a book | lines[2] N | Tetamo. That's a Colosseum quiver. | PASS | | |
| Name in a book | lines[3] P | It is. | PASS | | |
| Name in a book | lines[4] N | Then you don't want me. You want the capital. | PASS | | |
| Name in a book | lines[5] N | Harbour registrar, down at the Cothon. He keeps the ledger. | FAIL | fact, Cothon is north | Harbour registrar, up at the Cothon. He keeps the ledger. |
| Name in a book | opt1 label | Why not row me yourself? | PASS | | |
| Name in a book | opt1 N | Because your name goes in a book now. | PASS | | |
| Name in a book | opt1 N | Mine never has. Let's keep us both happy. | PASS | | |
| Name in a book | opt2 label | Where's the Cothon? | PASS | | |
| Name in a book | opt2 N | Big harbour in Civitas illa Fortis. You can't miss it. | PASS | | |
| Name in a book | opt2 N | Ask for the registrar. He'll be the bored one. | PASS | | |
| Name in a book | opt3 label | You won't come with me? | PASS | | |
| Name in a book | opt3 N | I've not set foot in that city in nine years. | PASS | | |
| Name in a book | opt3 P | Why not? | PASS | | |
| Name in a book | opt3 N | Too many people there remember faces. | PASS | | |
| Name in a book | opt4 label | Thanks for that. | PASS | | |
| Name in a book | opt4 N | Timoiva, iknami. | FAIL | two local words | Timoiva. |
| Name in a book | opt4 N | Don't tell him where you heard it. | PASS | | |

**Writer A: 46/53 = 87%.** It sounds like the game — clipped, uninterested, funny by omission — apart from two poster lines and a compass error.

### Writer B

| Variant | Field | Line | Verdict | Reason | Replacement |
|---|---|---|---|---|---|
| Wine Crates | lines[0] P | Can you take me out on the water? | PASS | | |
| Wine Crates | lines[1] N | Not you, not today. Got wine to move. | PASS | | |
| Wine Crates | lines[2] X | A gull screeches over the crates and he swears at it. | PASS | | |
| Wine Crates | lines[3] N | Even if I had the time, you're not allowed past the docks. | FAIL | lore framing | Even if I had. I don't cross open water. |
| Wine Crates | opt1 label | Why not? | PASS | | |
| Wine Crates | opt1 N | Nobody sails out 'til they've proven themselves, so they say. | FAIL | lore framing | No captain takes a stranger. Their boats, their call. |
| Wine Crates | opt1 P | Proven themselves how? | FAIL | follows cut line | Says who? |
| Wine Crates | opt1 N | Don't ask me, iknami. I just row the crates. | PASS | | |
| Wine Crates | opt2 label | Who decides that? | PASS | | |
| Wine Crates | opt2 N | Some registrar up in the capital, far as I know. | FAIL | hand-holding | Captains do. Ask one. |
| Wine Crates | opt2 N | Never been myself. Don't much care to. | PASS | | |
| Wine Crates | opt3 label | Nice wine you've got there. | PASS | | |
| Wine Crates | opt3 N | Aldarin's finest. Don't touch the crates. | PASS | | |
| Wine Crates | opt3 P | Wasn't going to. | PASS | | |
| Wine Crates | opt3 N | Good. Last one who did went home wet. | PASS | | |
| Wine Crates | opt4 label | Fine, I'll walk. | PASS | | |
| Wine Crates | opt4 N | Suit yourself. Long walk from here. | FAIL | fact, Aldarin is an island | Suit yourself. It's an island, mind. |
| Rough Company | lines[0] P | Got room on your boat for a paying passenger? | PASS | | |
| Rough Company | lines[1] N | Depends what you're paying with, and where you think you're going. | PASS | | |
| Rough Company | lines[2] X | He glances up the cliff path, then back to you. | PASS | | |
| Rough Company | lines[3] N | Out past Varlamore? Not a chance. Nobody's allowed. | FAIL | lore framing | Out past Varlamore? Not in this boat. |
| Rough Company | opt1 label | Nobody's allowed? | FAIL | lore framing | Why not? |
| Rough Company | opt1 N | That's the word. Nobody sails 'til they prove themselves. | FAIL | lore framing | Charter captains won't risk a stranger. |
| Rough Company | opt1 N | Tetamo, don't ask me who decided that. | FAIL | follows cut line | Tetamo. Their boats, their rules. |
| Rough Company | opt2 label | I could pay well. | PASS | | |
| Rough Company | opt2 N | Keep your coin. Not worth crossing whoever makes that rule. | FAIL | lore framing | Keep your coin. I don't do open water. |
| Rough Company | opt3 label | Are you smuggling something? | PASS | | |
| Rough Company | opt3 N | Wine, mostly. And things I don't put in the ledger. | FAIL | wrong register, confesses | Wine. It's always wine. |
| Rough Company | opt3 P | That sounds illegal. | PASS | | |
| Rough Company | opt3 N | Sounds like your walk back to town. | FAIL | not funny | Then don't say it out loud. |
| Rough Company | opt4 label | Forget it. | PASS | | |
| Rough Company | opt4 N | Wise choice, iknami. | PASS | | |
| Proof Enough | lines[0] P | I've got a boat request, if you'll take me. | FAIL | wrong register | Any chance of a boat now? |
| Proof Enough | lines[1] N | Wait... is that Dizana's quiver on your back? | FAIL | fact, too informed | That quiver. Colosseum, isn't it? |
| Proof Enough | lines[2] X | He looks you over properly for the first time. | PASS | | |
| Proof Enough | lines[3] N | Tetamo. You actually did it, then. | PASS | | |
| Proof Enough | opt1 label | Can you take me now? | PASS | | |
| Proof Enough | opt1 N | Not me. You want the harbour in the capital for that. | PASS | | |
| Proof Enough | opt1 N | I just do the coast, iknami. | PASS | | |
| Proof Enough | opt2 label | What do I do with this? | FAIL | hand-holding | Does that change anything? |
| Proof Enough | opt2 N | Show it at the harbour, up in the capital. | PASS | | |
| Proof Enough | opt2 P | Is that really it? | PASS | | |
| Proof Enough | opt2 N | That's what I hear. Never seen it myself. | PASS | | |
| Proof Enough | opt3 label | Thanks for the help. | PASS | | |
| Proof Enough | opt3 N | Didn't do much. Go on, the capital's waiting. | FAIL | hand-holding, poster line | Didn't do much. Mind the crates. |
| Proof Enough | opt4 label | You've heard of this before? | PASS | | |
| Proof Enough | opt4 N | Only rumours. Some quiver, some crown ledger, that's all I know. | FAIL | wrong register, listy | Only rumours. Never paid them much mind. |
| Proof Enough | opt4 N | Now go bother someone who knows more. | PASS | | |

**Writer B: 31/48 = 65%.** The crate and wine beats sound like the game, but three of the six refusals recite a kingdom-wide ban the wiki does not support, and the man explains the rule far more than a smuggler would.

## Challenges

**1. A smuggler cannot refuse on legal grounds.** This is the one character on the docks whose whole job is ignoring the rules, so "you're not allowed" (Writer B, both pre variants) is self-defeating comedy — the player's obvious reply is "you smuggle wine, take my money." Writer A dodges it but only halfway ("I like my boat unburnt", "the crown's got men"), which still makes the crown the enforcer of an outbound ban. **Instead:** refuse on capability and appetite. He rows a *small boat* along a *coast*. He has never crossed open water and does not want to. The gate stays personal and conditional — charter captains won't take an unknown, and that's their call, not the crown's — and he doesn't care enough to explain it.

**2. The post beat should cost him something, not congratulate the player.** Writer B's boatman is impressed ("You actually did it, then"), which is what any NPC would say. Writer A gets much closer with "your name goes in a book now." **Instead:** make the quiver *bad news for him*. A player who is about to be written into the harbour ledger is exactly the sort of passenger a man who stays off ledgers wants nowhere near his crates. The push toward the registrar becomes a brush-off rather than directions, which is both funnier and gets the player moving.

**3. Nobody used the two best real facts.** Aldarin is an **island** (so "I'll walk" is absurd, and Antonia already sells the only short hop for 20 coins), and Varlamore has canon smuggling at the Cothon — the *Fortis Spark* weapons crate in Twilight's Promise. Both drafts instead lean on invented "crown ledger" menace. **Instead:** ground the refusal in things on the wiki — Mistrock is as far as his run goes and it's still the island; the dwarves there don't like humans; the crates say wine. One true detail per option, no invented state apparatus.
