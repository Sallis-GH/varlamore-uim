# Round-two review — Fortis Cothon (Guard, Dockworker, unlockers)

Independent second pass. Calibrated against the wiki transcripts for the Border Guard (Varlamore),
Customs officer, Fisher, Sailor (Varlamore), Citizen (Civitas illa Fortis) and Trader Crewmember,
plus the Flaming Arrow, Minimus, Fortis Colosseum, Dizana's quiver and Curator Herminius articles.
Every judged item is counted: premises, spoken lines, narration boxes, option labels and option
ending lines. Only FAIL rows are listed; everything else passed.

---

## scripts/guard/reviewer.json

No FAILs.

**Pass rate: 34/34 = 100%.**

Sounds like the game: yes — this is the closest thing here to the approved benchmark, all refusal
and no explanation, and "None of them." is exactly how a posted guard shuts a conversation down.

---

## scripts/guard/writer-a.json

| File | Variant | Field | Line | Reason | Replacement |
|---|---|---|---|---|---|
| writer-a.json | Standing orders (pre) | premise | "The guard is at his post beside the charter berth, watching the gangplank and nothing else." | too long | "The guard is at his post by the berth, watching the gangplank." |
| writer-a.json | Standing orders (pre) | line 5 (P) | "Proven how?" | not a reply | "Vouched by who?" |
| writer-a.json | Cargo only (pre) | premise | "The guard has stood at the berth since dawn while the charter ship loads spice and takes on no passengers." | too long | "The guard has stood since dawn while the charter ship loads spice." |

**Pass rate: 55/58 = 94.8%.**

Sounds like the game: yes — the dialogue is solid and "Cargo only tomorrow." is a real guard joke;
only the stage directions overran, and the player's "Proven how?" answered a word nobody had said.

---

## scripts/guard/writer-b.json

| File | Variant | Field | Line | Reason | Replacement |
|---|---|---|---|---|---|
| writer-b.json | Nobody's Business (pre) | option 1 ending line | "Don't ask me. Ask the registrar." | hand-holding | "Don't ask me. Nobody tells me either." |

**Pass rate: 36/37 = 97.3%.**

Sounds like the game: yes — the shortest file of the three and the better for it, though in a pre
variant it handed over the registrar when the player had only asked what counted as proof.

---

## scripts/dockworker/reviewer.json

No FAILs.

**Pass rate: 35/35 = 100%.**

Sounds like the game: yes — the counting gag runs underneath the whole scene without ever being
explained, and the guards being "for looking at" is the brief turned into one line.

---

## scripts/dockworker/writer-a.json

| File | Variant | Field | Line | Reason | Replacement |
|---|---|---|---|---|---|
| writer-a.json | He Sees It (post) | line 4 (P) | "It is. I earned it." | not a reply | "I earned it at the Colosseum." |

**Pass rate: 51/52 = 98.1%.**

Sounds like the game: yes — the bad back, the spice count and the sunbeam ale at the Flaming Arrow
are all true to the place, and only the post variant had a player line answering a question that
had been edited away.

---

## scripts/dockworker/writer-b.json

| File | Variant | Field | Line | Reason | Replacement |
|---|---|---|---|---|---|
| writer-b.json | Aching Back (pre) | line 5 (P) | "Says so how?" | wrong register | "Says who?" |
| writer-b.json | Counting Coins (pre) | line 4 (N) | "The Sablewind crew tip well. The rest never do." | fact | "The Aldarin crews tip well. The rest never do." |
| writer-b.json | Counting Coins (pre) | option 1 ending line | "Sablewind, if you don't mind heavy lifting." | fact | "The Aldarin lot, if you don't mind heavy lifting." |

**Pass rate: 44/47 = 93.6%.**

Sounds like the game: mostly — the money-and-crates voice is right, but it invented a named crew
the wiki has never heard of, and one player line came out as garbled English.

---

## content/unlocker-scripts.json

| File | Variant | Field | Line | Reason | Replacement |
|---|---|---|---|---|---|
| unlocker-scripts.json | registrar / with | line 4 (N) | "Huh. Colosseum-made, and blooded. Not a market copy." | poetic | "Huh. Real Colosseum work. Not a market copy." |
| unlocker-scripts.json | registrar / with | option 3 ending line | "Every day's long. The sea never stops arriving." | poetic | "Every day's long. The ships keep coming in." |
| unlocker-scripts.json | minimus / without | line 2 (N) | "Oi! I run an arena, not a harbour." | wrong register | "Me? I run an arena, not a harbour." |
| unlocker-scripts.json | steward / with | option 3 label | "Timoiva." | wrong register | "Goodbye, then." |

**Pass rate: 80/84 = 95.2%.**

Sounds like the game: yes — Herminius is the standout, because "Not a problem, iknami" is his real
catchphrase and the replica answer is straight off the wiki; Minimus was the one who slipped, since
the Colosseum Master speaks with ceremony and would never open with "Oi!".

Checked and kept as correct: Minimus is the Colosseum Master and the kingdom did give him the role;
Rookie is a real rank; twelve waves and Sol Heredit are right; the quiver really is a replica of
Dizana's; Herminius really is the Grand Museum's curator and really does authenticate objects.

---

## Three best lines

1. "So's the blacksmith." — dockworker/reviewer.json, Losing Count. The whole opinion of the city
   guard delivered without stating it.
2. "Timoiva. Sixty-one, sixty-two..." — dockworker/reviewer.json, Losing Count. He goes back to
   counting mid-goodbye; the joke never gets acknowledged.
3. "The crates have permission. You don't." — guard/writer-a.json, Cargo only. Personal and
   conditional, exactly the gate the lore needs, and funnier for being flat.

## Three worst lines

1. "It is. I earned it." — dockworker/writer-a.json, He Sees It. A player line replying to nothing;
   "It is" answers a question that is not on the screen.
2. "Says so how?" — dockworker/writer-b.json, Aching Back. Not a sentence anyone says out loud.
3. "Every day's long. The sea never stops arriving." — unlocker-scripts.json, registrar. A clerk
   should be complaining about the hours, not writing about the tide.
