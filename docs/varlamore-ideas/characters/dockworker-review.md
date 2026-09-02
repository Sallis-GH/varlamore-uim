# Dockworker — script review

Character: Dockworker, Fortis Cothon. Drafts: `scripts/dockworker/writer-a.json`,
`scripts/dockworker/writer-b.json`. No `approved.json` present for this character.

Wiki checks run: Fortis Cothon, Shipwright Sennia, Charter ship, The Flaming Arrow,
Fortis Spice Stall, Fortis Fur Stall, Trader crewmember, Sailor (Varlamore),
Transcript:Sailor (Varlamore), Worker (Varlamore), Fortis Colosseum, Dizana's quiver.

Findings that drive the FAILs below:
- Charter ports do not include **Hosidius**. Kourend's charter port is Port Piscarilius.
  From Civitas illa Fortis the cheap runs are Sunset Coast (500) and Aldarin (600).
- **The Flaming Arrow** is a single pub room with a bartender; no stairs, no cellar
  documented. The one real "insider" detail there is the sunbeam ale spawn on a table.
- Colosseum quivers are **replicas**; Dizana's original is lost and "a heavily debated
  topic among Varlamorians". So "that's the real thing" is wrong.
- The **Fortis Colosseum** is "a large stadium found on the cliffs east of Civitas illa
  Fortis". The Cothon is "on the north side" of the city. You cannot hear the crowd.
- The **spice** detail is correct and worth keeping: spice sells at 230 coins, and both
  the Spice and Fur Merchants call guards on thieves ("Guards! Guards!").
- Charter ships into Varlamore are new; twelve years of *charter* cargo is not possible.
  Twelve years of *hauling crates* is.
- No harbour office/"dock building" exists at the Cothon — see Challenges, not a per-line
  FAIL, since the registrar is an invention of the brief anyway.

---

## Line-by-line

| Writer | Variant | Field | Line | Verdict | Reason | Replacement |
|---|---|---|---|---|---|---|
| A | Bad Back Day | L1 P | Can you get me onto a ship out of here? | PASS | | |
| A | Bad Back Day | L2 N | Nilsal! Help me with this crate first. | PASS | | |
| A | Bad Back Day | L3 X | You take one end. It is full of nails. | PASS | | |
| A | Bad Back Day | L4 N | There. My back's gone again. | PASS | | |
| A | Bad Back Day | L5 N | Ships? Nobody's sailing you anywhere. | PASS | | |
| A | Bad Back Day | opt1 label | Who decides who sails? | PASS | | |
| A | Bad Back Day | opt1 L1 N | Someone in the dock building with a ledger. | PASS | | |
| A | Bad Back Day | opt1 L2 N | I've never been past that door. | PASS | | |
| A | Bad Back Day | opt2 label | Your back sounds bad. | PASS | | |
| A | Bad Back Day | opt2 L1 N | Twelve years of charter cargo. Worth it for the pay. | FAIL | fact | Twelve years of hauling crates. Worth it for the pay. |
| A | Bad Back Day | opt2 L2 P | Is the pay good? | PASS | | |
| A | Bad Back Day | opt2 L3 N | Not really, no. | PASS | | |
| A | Bad Back Day | opt3 label | What's in the crates? | PASS | | |
| A | Bad Back Day | opt3 L1 N | Spice mostly. Furs when the northern lot come in. | PASS | | |
| A | Bad Back Day | opt3 L2 N | Don't touch the spice. They count it. | PASS | | |
| A | Bad Back Day | opt4 label | I'll leave you to it. | PASS | | |
| A | Bad Back Day | opt4 L1 N | Timoiva. Send the cart lad over if you see him. | PASS | | |
| A | Tide Going Out | L1 P | I need a boat out of Varlamore. | PASS | | |
| A | Tide Going Out | L2 N | Tide's going out. So's my lunch break. | PASS | | |
| A | Tide Going Out | L3 N | Even if a crew wanted you, they can't take you. | PASS | | |
| A | Tide Going Out | L4 P | Says who, exactly? | PASS | | |
| A | Tide Going Out | L5 N | The desk inside. The guards just stand about looking smart. | PASS | | |
| A | Tide Going Out | opt1 label | Which crews might take me? | PASS | | |
| A | Tide Going Out | opt1 L1 N | The Hosidius run tips well. The spice lot never tip. | FAIL | fact | The Aldarin run tips well. The spice lot never tip. |
| A | Tide Going Out | opt1 L2 N | Neither will sail you out, mind. | PASS | | |
| A | Tide Going Out | opt2 label | What's this desk inside? | PASS | | |
| A | Tide Going Out | opt2 L1 N | Big ledger, bigger frown. Everything that floats goes in it. | FAIL | poetic | A ledger. Every hull in the harbour's written in it. |
| A | Tide Going Out | opt2 L2 N | Don't go bothering it without a reason. | PASS | | |
| A | Tide Going Out | opt3 label | Where can I get a drink? | PASS | | |
| A | Tide Going Out | opt3 L1 N | Flaming Arrow. Ask for the barrel behind the stairs. | FAIL | fact | Flaming Arrow. Ask for the sunbeam ale. |
| A | Tide Going Out | opt3 L2 P | Behind which stairs? | FAIL | fact | Is it any good? |
| A | Tide Going Out | opt3 L3 N | You'll thank me. | PASS | | |
| A | Tide Going Out | opt4 label | Enjoy your bread. | PASS | | |
| A | Tide Going Out | opt4 L1 N | I will. Tide's back in an hour anyway. | PASS | | |
| A | He Sees It | L1 P | Any chance of a ship out of the kingdom? | PASS | | |
| A | He Sees It | L2 X | He looks at the quiver on your back and drops the rope. | PASS | | |
| A | He Sees It | L3 N | Tetamo. That's the real thing, that is. | FAIL | fact | Tetamo. You don't see those down here. |
| A | He Sees It | L4 P | It is. I earned it. | PASS | | |
| A | He Sees It | L5 N | Then stop asking dockhands. Go and see the registrar. | PASS | | |
| A | He Sees It | opt1 label | Where's the registrar? | PASS | | |
| A | He Sees It | opt1 L1 N | Dock building, desk at the back. You'll see the ledger. | PASS | | |
| A | He Sees It | opt2 label | You know what this is? | PASS | | |
| A | He Sees It | opt2 L1 N | Everyone at the Cothon knows. We hear the crowd from here. | FAIL | fact | Everyone here knows. The betting lot talk of nothing else. |
| A | He Sees It | opt2 L2 N | Loudest thing in the capital. | PASS | | |
| A | He Sees It | opt3 label | Will the crews take me? | PASS | | |
| A | He Sees It | opt3 L1 N | They take whoever the ledger says. Get your name in it. | PASS | | |
| A | He Sees It | opt3 L2 N | Then find a crew. The Hosidius lot are decent. | FAIL | fact | Then find a crew. The Aldarin lot are decent. |
| A | He Sees It | opt4 label | Thanks. I'll go now. | PASS | | |
| A | He Sees It | opt4 L1 N | Timoiva. Mind the wet steps on your way in. | PASS | | |
| B | Aching Back | L1 P | Any ships heading out of the harbour? | PASS | | |
| B | Aching Back | L2 N | Ask the tide, not me, iknami. | PASS | | |
| B | Aching Back | L3 X | He straightens up slowly, hand pressed to his back. | PASS | | |
| B | Aching Back | L4 N | None of them are going anywhere till the crown says so. | FAIL | lore framing | They'll sail all right. Just not with you aboard. |
| B | Aching Back | L5 P | Says so how? | PASS | | |
| B | Aching Back | L6 N | No idea. Above my pay, and my pay's already rubbish. | PASS | | |
| B | Aching Back | opt1 label | What's the crown's rule, then? | FAIL | lore framing | Why won't they take me? |
| B | Aching Back | opt1 L1 N | Something about proving yourself first. Ask a guard. | FAIL | hand-holding | You'd have to prove yourself first. No idea how. |
| B | Aching Back | opt2 label | Rough day? | PASS | | |
| B | Aching Back | opt2 L1 N | Every day's rough when you lift crates for a living. | PASS | | |
| B | Aching Back | opt2 L2 P | Suppose so. | PASS | | |
| B | Aching Back | opt2 L3 N | Mind how you go. | PASS | | |
| B | Aching Back | opt3 label | Never mind. | PASS | | |
| B | Aching Back | opt3 L1 N | Wise choice. | PASS | | |
| B | Counting Coins | L1 P | Can I catch a ride out of Varlamore? | PASS | | |
| B | Counting Coins | L2 N | Ha. Wish I could tell you yes. | PASS | | |
| B | Counting Coins | L3 X | He pockets the coins and nods towards the moored ships. | PASS | | |
| B | Counting Coins | L4 N | The Sablewind crew tip well. The rest are tight as a barnacle. | FAIL | poetic | The Sablewind crew tip well. The rest never do. |
| B | Counting Coins | L5 P | But can any of them sail me out? | PASS | | |
| B | Counting Coins | L6 N | Not one. Harbour's shut tight till the crown lifts it. | FAIL | lore framing | Not one. None of them will vouch for a stranger. |
| B | Counting Coins | opt1 label | Who lifts it, then? | FAIL | lore framing | Who would vouch for me? |
| B | Counting Coins | opt1 L1 N | Some registrar up in the dock building. Never met her. | PASS | | |
| B | Counting Coins | opt2 label | Any good crews to work for? | PASS | | |
| B | Counting Coins | opt2 L1 N | Sablewind, if you don't mind heavy lifting. | PASS | | |
| B | Counting Coins | opt2 L2 P | Noted. | PASS | | |
| B | Counting Coins | opt2 L3 N | Timoiva. | PASS | | |
| B | Counting Coins | opt3 label | Forget it. | PASS | | |
| B | Counting Coins | opt3 L1 N | Suit yourself. | PASS | | |
| B | Quiver Spotted | L1 P | I need a ship out of Varlamore. | PASS | | |
| B | Quiver Spotted | L2 X | He spots Dizana's quiver and stops mid-lift. | PASS | | |
| B | Quiver Spotted | L3 N | Tetamo... that's a Colosseum quiver, that is. | PASS | | |
| B | Quiver Spotted | L4 P | Does that mean something? | PASS | | |
| B | Quiver Spotted | L5 N | Means you've earned your ticket. Go find the registrar. | PASS | | |
| B | Quiver Spotted | L6 N | She keeps the ledger, in the dock building. Ask her, not me. | FAIL | hand-holding | She keeps the ledger. Ask her, not me. |
| B | Quiver Spotted | opt1 label | Where's the dock building? | PASS | | |
| B | Quiver Spotted | opt1 L1 N | Right there, past the crates. Can't miss it. | PASS | | |
| B | Quiver Spotted | opt2 label | Thanks for the tip. | PASS | | |
| B | Quiver Spotted | opt2 L1 N | Don't thank me, thank your own arms. | PASS | | |
| B | Quiver Spotted | opt2 L2 P | Ha. | PASS | | |
| B | Quiver Spotted | opt2 L3 N | Go on, then. | PASS | | |
| B | Quiver Spotted | opt3 label | One more question. | PASS | | |
| B | Quiver Spotted | opt3 L1 N | Make it quick, I've crates waiting. | PASS | | |
| B | Quiver Spotted | opt3 L2 P | Never mind. | PASS | | |
| B | Quiver Spotted | opt3 L3 N | Timoiva, then. | PASS | | |

---

## Verdicts

**Writer A — 41/49 PASS (84%).** Sounds like the game: clipped, grumpy, jokes land in one
beat, and the crate-and-spice business is exactly what this NPC would talk about — the
failures are almost all invented geography rather than bad writing.

**Writer B — 37/44 PASS (84%).** Also sounds like the game line by line, but it keeps
telling the player the harbour is shut by royal order, which is both wrong and the one
framing the brief warns against.

---

## Challenges

**1. He knows too much about the ledger in the pre variants.** Both writers have him name
a registrar and a ledger before the player has proved anything (A opt1, B opt1). The brief
says nobody at the docks can unlock anything and most of them only half-know the rule. A
man who lifts crates would know one thing: crews won't take a stranger nobody's vouched
for. Everything else is above him. I'd strip the ledger out of pre entirely and let the
word "registrar" appear for the first time when he sees the quiver — that's what makes the
post variant feel like a door opening.

**2. The "dock building" is invented and both drafts point at it.** The wiki's Cothon is
stalls and open wharf: gem, fur, silk, spice and baker's stalls, Cobado's Groceries,
Artima's Crafting Supplies, a general store, a blacksmith and the Flaming Arrow. There is
no harbour office, and B's "Right there, past the crates. Can't miss it." promises the
player a landmark that isn't on the map. Keep the registrar placeless and human — "the one
with the ledger", "up the wharf" — never a room with a door.

**3. Both pre variants are the same joke twice.** A does bad back then lunch break; B does
bad back then bad pay. Each writer used two of the four things the brief gave him and both
picked the same two. He also knows the tide table, what the spice merchants pay, and where
the Flaming Arrow keeps the good barrel. I'd make the second pre variant change subject,
not posture: him counting spice sacks and losing count is a different man in the same body,
and it gets the funniest true detail on the docks — spice is worth more per sack than he is,
and the merchants count it twice.
