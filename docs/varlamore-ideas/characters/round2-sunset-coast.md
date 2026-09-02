# Round-two review: Sunset Coast

Independent second pass over the Sunset Coast dock characters: **Fisher** (Fremennik fisher) and **Vagrant** (pier drifter). Calibrated against six wiki transcripts (Customs officer, Border Guard (Varlamore), Fisher, Sailor (Varlamore), Trader Crewmember, Citizen (Civitas illa Fortis)) plus the Sunset Coast and Aldarin location pages.

Denominator for pass rates = every judged item: premises, intro lines, option labels, option ending lines.

Premises are held to the 90-character hard cap but not the 3–14 word speech rule, since they are stage directions rather than spoken lines.

---

## scripts/fisher/writer-a.json

| File | Variant | Field | Line | Reason | Replacement |
|---|---|---|---|---|---|
| writer-a.json | Mending nets (pre) | option "What happened to the net?", line 1 | "Shark went through it like a hot knife." | poetic | "Shark went straight through it." |
| writer-a.json | Gutting sharks (pre) | option "Have you been to the capital?", line 2 | "Everything I need swims past this rock." | poetic | "Nothing up there but people." |
| writer-a.json | The quiver (post) | premise | "The one Fremennik here who stayed for the fish instead of the arena, hauling a crate of shark up the dock, catches sight of what you're wearing." | too long | "The fisher is hauling a crate of shark up the dock when the player stops him." |
| writer-a.json | The quiver (post) | option "Where is this harbour?", line 3 | "Too many people for one shore." | poetic | "Too many people, they tell me." |

**Pass rate: 52/56 = 92.9%**

Sounds like the game most of the time, but it is the one file that keeps reaching for a nice phrase when a flat one would land harder.

---

## scripts/fisher/writer-b.json

| File | Variant | Field | Line | Reason | Replacement |
|---|---|---|---|---|---|
| writer-b.json | Bad Catch (pre) | line 3 (N) | "Sharp of you." | wrong register | "I wouldn't." |

**Pass rate: 47/48 = 97.9%**

Very close to the real thing — "Do I look like a ship to you?" and "Rocks are that way. Fish are that way. Ships, no idea." could both be lifted straight out of a Varlamore transcript.

---

## scripts/fisher/reviewer.json

| File | Variant | Field | Line | Reason | Replacement |
|---|---|---|---|---|---|
| reviewer.json | Stayed for the fish (post) | premise | "The fisher is salting shark on a plank and stops when he sees the cape." | fact | "The fisher is salting shark on a plank and stops when he sees the quiver." |

**Pass rate: 35/36 = 97.2%**

The best Fisher file: it uses the real wiki fact that the Sunset Coast Fremenniks came from Rellekka to fight in the Colosseum, and gets a character out of one man who didn't.

Note (not scored as a FAIL): this file carries one pre and one post variant, not the two pre variants the writer rules ask for. I left the shape alone as instructed.

---

## scripts/vagrant/writer-a.json

| File | Variant | Field | Line | Reason | Replacement |
|---|---|---|---|---|---|
| writer-a.json | Under the pier (pre) | premise | "He is picking through a bucket of fish scraps in the shade under the pier, having just watched Antonia's boat pull out for Aldarin, when the player crouches down to him." | too long | "He is picking through a bucket of fish scraps in the shade under the pier." |
| writer-a.json | Boots and bells (pre) | premise | "He is sat with his back to a piling, watching a charter crew load crates and naming them under his breath." | too long | "He is sat with his back to a piling, watching a charter crew load crates." |
| writer-a.json | The quiver (post) | premise | "He is coiling a length of rope the crews dropped when he catches sight of the quiver and stops." | too long | "He is coiling a length of rope the crews dropped under the pier." |

**Pass rate: 50/53 = 94.3%**

The dialogue itself is the strongest writing in either folder — every fail is a stage direction that ran away with itself, not a spoken line.

---

## scripts/vagrant/writer-b.json

| File | Variant | Field | Line | Reason | Replacement |
|---|---|---|---|---|---|
| writer-b.json | Boots, Not Boats (pre) | option "Can't you just stow away?", line 3 | "Boots. Big boots. That's what happened." | wrong register | "Boots. Someone else's, in my ribs." |
| writer-b.json | Watching the Gangplank (pre) | option "I'm not hiding anything.", line 1 | "Sure you're not. None of them are." | wrong register | "Never met one who was." |

**Pass rate: 45/47 = 95.7%**

Solid and consistent, but it leans on the boots motif until it stops being a throwaway and starts being a bit.

---

## scripts/vagrant/reviewer.json

No FAIL rows.

**Pass rate: 32/32 = 100%**

The only file where every line sounds like something an actual OSRS NPC would say, and the Antonia/twenty-coins detail checks out against the wiki exactly.

---

## Three best lines

1. "It's out of here." — vagrant/reviewer.json, "The wrong boat"
2. "I came for the fish." — fisher/reviewer.json, "Stayed for the fish"
3. "I've got the pier." — vagrant/reviewer.json, "The wrong boat"

## Three worst lines

1. "Shark went through it like a hot knife." — fisher/writer-a.json, "Mending nets"
2. "Boots. Big boots. That's what happened." — vagrant/writer-b.json, "Boots, Not Boats"
3. "Too many people for one shore." — fisher/writer-a.json, "The quiver"

---

## Fact checks passed

- Sunset Coast is a Fremennik settlement of people who came from Rellekka to fight in the Colosseum — supports the reviewer Fisher's "Half this village came south to fight for one of those."
- Antonia runs the boat from Sunset Coast to Aldarin for 20 coins — the vagrant reviewer file's "I can get you Aldarin. Twenty coins." and "Antonia. She's got the boat." are both accurate.
- Sharks off the rocks, shark on the ovens, Picaria's Fishing Shop, Thurid the helmet smith: all consistent with the location.
- No Varlamorian words (Nilsal, iknami, Tetamo, Timoiva) appear in either folder — correct, both characters are outsiders.
- All three post variants in each folder end up pointing the player at the harbour in the capital, in character.
