# Unlocker scripts: line-by-line review

Reviewed against `content/writer-rules.md` (Register section), the lore constraint in
`content/unlock.json` (`intro_html`), and `research/colosseum-and-authority.md` §1, §3, §5.
Facts checked live against the OSRS Wiki: `Transcript:Minimus`, `Transcript:Curator_Herminius`,
`Dizana's quiver`, `Fortis Cothon`.

Counting: one row per dialogue line, one row per option (label and its reply judged together).
15 rows per candidate, 60 rows total.

## Wiki findings that drove the verdicts

- **Minimus** addresses the player by their current Glory title — "Rookie" first, then Brawler /
  Challenger / Gladiator / Hero / Champion / Grand Champion. He opens with "Oi! Where d'you think
  you're going?" and states "the kingdom gave me the role of Colosseum Master."
- **Dizana's quiver** drops from defeating Sol Heredit on wave 12. It is explicitly "one of many
  used by fighters in the Fortis Colosseum, made as replicas of the original that was used by
  Dizana, the Heroine of the Hunt." **Sunfire** enters only as splinters you charge it with
  afterwards; a freshly won quiver has no sunfire on it. The wiki records no "stitching" and no
  "blooding" as physical marks.
- **Curator Herminius** greets with "Nilsal to you, iknami. Welcome to the Grand Museum," and signs
  off "Not a problem, iknami!" His register is warm, teacherly, explanatory ("I often like to
  challenge people to try and find them all!"). He is never snide and never brags about rank.
- **Fortis Cothon** is a real harbour on the north side of Civitas illa Fortis with charter ships,
  Trader crewmembers and Shipwright Sennia, and **no** documented harbourmaster, registrar or
  customs official — so an invented registrar contradicts nothing.

---

## Harbour Registrar

| Exchange | Field | Line | Verdict | Reason | Replacement |
|---|---|---|---|---|---|
| with | line 1 (P) | Can you clear me for a ship out of Varlamore? | PASS | Plain ask, correct register | — |
| with | line 2 (N) | Only if you're proven. They all say they are. Show me. | PASS | Clipped, personal criterion | — |
| with | line 3 (X) | You show the registrar Dizana's quiver. | PASS | Flat present-tense narration | — |
| with | line 4 (N) | Huh. Colosseum-made, and blooded. Not a market copy. | PASS | Blood-offering lore supports it | — |
| with | line 5 (N) | You're in the ledger now. The crews won't argue with the ledger. | PASS | Crews choose, not law | — |
| with | option 1 | "Thanks for your help." / It's the first entry I've enjoyed all week. | PASS | One dry joke beat | — |
| with | option 2 | "Does this ever expire?" / Ink doesn't lift, iknami. You're written for good. | PASS | Local word used sparingly | — |
| with | option 3 | "Long day?" / Every day's long. The sea never stops arriving. | PASS | Clerk knows his own day | — |
| without | line 1 (P) | Can you clear me for a ship out of Varlamore? | PASS | Same plain ask | — |
| without | line 2 (N) | No. I clear the proven, and you're not on my list. | PASS | Refusal is his own | — |
| without | line 3 (P) | What counts as proven? | PASS | Short natural follow-up | — |
| without | line 4 (N) | Come back wearing something the Colosseum gave you. | PASS | One hint, not lecture | — |
| without | option 1 | "Who decides that?" / The crown decides. I just keep the ledger tidy. | **FAIL** | Makes it crown policy | reply → `No captain wants that risk. I just keep the ledger.` |
| without | option 2 | "Can't you make an exception?" / I made one once. I'd rather not discuss it. | PASS | Perfect single joke beat | — |
| without | option 3 | "I'll be back." / They all say that. Some of them are. | **FAIL** | Reads as Terminator quote | label → `I'll come back proven.` |

**Pass rate: 13/15 (87%).**

Sounds like the game: yes — the clipped, faintly worn-out clerk voice is the closest thing here to
a real OSRS service NPC, and only the "crown decides" line breaks the spell.

---

## Minimus

| Exchange | Field | Line | Verdict | Reason | Replacement |
|---|---|---|---|---|---|
| with | line 1 (P) | Minimus, can you get me a ship out of Varlamore? | PASS | Plain ask, names him | — |
| with | line 2 (N) | Ships? I'm no sailor, Champion. Show me what you're carrying. | **FAIL** | "Champion" is unearned Glory rank | → `Ships? I'm no sailor. What's that on your back, though?` |
| with | line 3 (X) | You show Minimus Dizana's quiver. | PASS | Flat narration, correct form | — |
| with | line 4 (N) | Twelve waves, and Sol Heredit face down. That's not luck. | PASS | Wave count and boss correct | — |
| with | line 5 (N) | My word carries at the docks. Tell them the Master sent you. | PASS | Vouching, not decree | — |
| with | option 1 | "Will they believe you?" / They will. I've never had to say a thing twice. | PASS | Matches his documented pride | — |
| with | option 2 | "Why does your word count?" / The kingdom gave me this role. Word came with it. | PASS | Near-verbatim wiki line | — |
| with | option 3 | "Thanks, Minimus." / Now, did you need anything else? Off you go. | **FAIL** | Asks then dismisses; muddled | reply → `Don't thank me. Thank the twelve waves.` |
| without | line 1 (P) | Minimus, can you get me a ship out of Varlamore? | PASS | Consistent plain ask | — |
| without | line 2 (N) | Oi! I run an arena, not a harbour. | PASS | "Oi!" is his canon opener | — |
| without | line 3 (P) | So there's nothing you can do? | PASS | Natural six-word follow-up | — |
| without | line 4 (N) | Not for a Rookie. Prove yourself in my sand first. | PASS | "Rookie" is canon address | — |
| without | option 1 | "How do I prove myself?" / Twelve waves. Come out the other side. Simple enough. | PASS | Wave count accurate, clipped | — |
| without | option 2 | "That's a lot to ask." / Then the sea's not for you. Neither is my arena. | PASS | Gruff, not personally cruel | — |
| without | option 3 | "I'll think about it." / Everyone thinks about it. Fewer of them come back. | PASS | Clean single beat | — |

**Pass rate: 13/15 (87%).**

Sounds like the game: yes, and it is the most convincing of the four — the `without` exchange could
be dropped into his existing transcript unaltered.

---

## Colosseum Steward

| Exchange | Field | Line | Verdict | Reason | Replacement |
|---|---|---|---|---|---|
| with | line 1 (P) | I want passage overseas. Who signs for that? | PASS | Plain, sets the premise | — |
| with | line 2 (N) | The Queen's ledger does. Have you anything worth entering? | PASS | Matches Glory-titles precedent | — |
| with | line 3 (X) | You show the steward Dizana's quiver. | PASS | Flat present-tense narration | — |
| with | line 4 (N) | A quiver off the sand. I've not written one this season. | PASS | Rarity claim is safe | — |
| with | line 5 (N) | Sail when you please. The harbour reads this ledger each morning. | **FAIL** | Implies institutional harbour permission | → `The crews read champions' names. Yours will get you aboard.` |
| with | option 1 | "What goes in the ledger?" / Your name, the wave, and the day. Nothing more. | PASS | Clerkish, concrete, short | — |
| with | option 2 | "Does the Queen read it?" / She reads the ones that matter. Yours might be one. | PASS | Hedged, invents nothing | — |
| with | option 3 | "Timoiva." / Timoiva. Come back with a better story than most. | PASS | Local farewell used once | — |
| without | line 1 (P) | I want passage overseas. Who signs for that? | PASS | Same plain ask | — |
| without | line 2 (N) | Nobody signs for an empty name. Mine is a champions' ledger. | PASS | Refusal scoped to his ledger | — |
| without | line 3 (P) | So how do I get a name? | PASS | Short natural follow-up | — |
| without | line 4 (N) | Through that gate. Twelve waves. Then I'll dip my pen. | PASS | Wave count correct, clipped | — |
| without | option 1 | "Who else could help me?" / Nobody down at the harbour will outrank this page. | **FAIL** | Asserts kingdom-wide permit hierarchy | reply → `Nobody. The captains want a name they've heard of.` |
| without | option 2 | "That seems steep." / It is. That's rather the point of the thing. | PASS | Dry, single beat | — |
| without | option 3 | "I'll come back." / They mostly don't. I'll leave the line empty. | PASS | Bleak joke, one line | — |

**Pass rate: 13/15 (87%).**

Sounds like the game: mostly — the pen-and-ledger business is well observed, but the steward is the
candidate most tempted into speaking for the state rather than for himself.

---

## Curator Herminius

| Exchange | Field | Line | Verdict | Reason | Replacement |
|---|---|---|---|---|---|
| with | line 1 (P) | Curator, I need a ship out of Varlamore. Can you vouch? | PASS | Plain ask, names role | — |
| with | line 2 (N) | Vouching is not my trade, iknami. Authenticating is. Show me. | PASS | "iknami" is his canon habit | — |
| with | line 3 (X) | You show Curator Herminius Dizana's quiver. | PASS | Flat present-tense narration | — |
| with | line 4 (N) | Indeed. Colosseum stitching, and the sunfire scoring is genuine. | **FAIL** | Sunfire only on charged quivers | → `Indeed. Arena-made, and worn hard. This came off the sand.` |
| with | line 5 (N) | I'll write to the harbour. My hand is known there. | PASS | Personal vouching, correctly framed | — |
| with | option 1 | "Is it the original quiver?" / No. A replica, though a very honest one. | PASS | Replica lore exactly right | — |
| with | option 2 | "Why would they listen?" / I have named half the objects in this city, iknami. | **FAIL** | Boastful; off his teacherly voice | reply → `Half the labels in this museum are in my hand.` |
| with | option 3 | "Thank you, Curator." / Not a problem, iknami. Do come back and read something. | PASS | "Not a problem, iknami" verbatim | — |
| without | line 1 (P) | Curator, I need a ship out of Varlamore. Can you vouch? | PASS | Consistent plain ask | — |
| without | line 2 (N) | For what, exactly? I authenticate objects, not eager strangers. | **FAIL** | Snide; he is never snide | → `For what, exactly? I authenticate objects. People are not objects.` |
| without | line 3 (P) | Anything that proves I can look after myself. | PASS | Plain, states the premise | — |
| without | line 4 (N) | Then bring me something the Colosseum gave you. Nothing else counts. | PASS | One directive, not lecture | — |
| without | option 1 | "What would you accept?" / A quiver from the sand. I would know it at once. | PASS | Formality matches his canon | — |
| without | option 2 | "Could you make an exception?" / I could. Then my word would be worth rather less. | PASS | Best line in the file | — |
| without | option 3 | "I'll find something." / Not a problem, iknami. The museum keeps long hours. | PASS | Canon closer reused well | — |

**Pass rate: 12/15 (80%).**

Sounds like the game: partly — the replica exchange and the "Not a problem, iknami" closers are
pitch-perfect, but the scene keeps reaching for a sharper, more superior curator than the friendly
guide the wiki actually documents.

Note (not scored as a failure): "iknami" appears four times across his two exchanges, which breaks
the writer-rule's "rarely". It survives review only because Herminius is the one NPC on the wiki who
genuinely says it in almost every line. Two of the four are removed by the fixes above.

---

## Challenges

**1. Pick the Harbour Registrar, and cut the other three.**
Four unlockers is three too many for one gate, and shipping out of Varlamore is a shipping decision
— it belongs where the ships are. The Registrar has two structural advantages nothing else here can
match: he stands at the Cothon choke point, so every dock stand-in at every port can say "the
harbour in the capital" and mean one desk; and because the wiki documents no harbourmaster there, an
invented clerk cannot be out of character. Minimus is the better *voice* — his `without` exchange
is the strongest writing in the file — but overlaying him puts new branches inside the NPC that runs
Start-wave and the reward exchange, which is exactly where an overlay bug costs a player a
1,563-combat kill. The Steward is the Registrar with a worse address, standing a few tiles from a
real NPC who does the same job. Herminius is a museum, and the scripts never fully answer why a
curator's letter moves a ship.

**2. The ledger is one idea wearing four hats, and the chatbox cannot show a ledger.**
The Registrar keeps a ledger, the Steward keeps the Queen's ledger, Herminius writes a letter,
Minimus sends word. All four resolve the unlock into an off-screen record the player never sees,
which is the one thing OSRS dialogue habitually refuses to do. Jagex gates travel with objects the
player holds and can look at: the Karamja gloves, Ring of Charos(a), a completed quest in the log.
If the gate has to exist, it should hand over something — a stamped chit, a captain's token — and
then the `with` exchange has a reason to explain what happens next, instead of asking the player to
trust a piece of paperwork in another room.

**3. A real Jagex writer would fire the refusal at the gangplank, not at a desk the player sought out.**
Every one of these eight exchanges opens with the player walking up and asking permission, which is
backwards: in the game, the Customs officer stops you when you try to board, and the Border Guard
stops you when you reach the gate. The gate should be discovered by clicking the charter ship and
being turned away by the crewmember who is already standing there, with the named unlocker as the
short reward beat afterwards — probably two or three lines, not a five-line scene with a menu.
Relatedly, all four `without` variants end by telling the player exactly what to go and fetch. Jagex
would let one NPC know the answer and let the other three shrug, which is also what
`writer-rules.md` asks for and what makes a world feel populated rather than signposted.
