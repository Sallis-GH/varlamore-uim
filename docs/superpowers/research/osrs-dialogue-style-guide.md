# OSRS NPC Dialogue Style Guide

A single working reference for writing Old School RuneScape NPC dialogue that sounds like it
shipped with the game. Synthesised from five research passes over OSRS Wiki transcripts:
classic free-to-play quests (2001-2004), comedy quests (2004-2007), modern quests (2018-2025),
the Varlamore quest line (2024+), and ambient/non-quest NPCs (guards, sailors, fishers, dock
officials, merchants, monks, old men).

Every quoted line below is verbatim from those transcripts and attributed. Nothing in quotation
marks was invented for this guide.

---

## 1. Line shape

**Word count per box.** Most NPC boxes run **5-20 words** — roughly **20-70 characters**. This
is consistent across all four eras. A line long enough that the reader's eye has to track back
is already too long. Treat **110 characters as a hard ceiling** for an ordinary NPC.

Exceptions: one designated lore-dump character per quest (a queen, an archmage, a curator) may
stretch to 25-35 words, and even then the text breaks into short clauses rather than one long
subordinate chain.

**Sentences per box.** One or two. A third sentence is rare enough to read as a mistake. The
dominant rhythm across the modern corpus is **declarative-then-clarify**: a flat short fact,
then a second short sentence that colours or complicates it. Comic beats often land as a single
fragment — "Ha! Good one." (*Perilous Moons*), "Nice try." (*The Final Dawn*), "Charming"
(comedy-era corpus).

Never stack two long expository sentences back to back. If exposition needs three sentences,
it needs three boxes.

**Punctuation devices and their limits.**

| Device | What it does | Frequency limit |
|---|---|---|
| Ellipsis `...` | Hesitation, trailing off, a beat a voice actor would take | At most **one per line**. Not for suspense padding. |
| Exclamation mark | Genuine surprise, panic, comic punchline, an in-world greeting ("Nilsal!") | At most **one per line**. Never for routine emphasis. |
| Both in one line | — | **Never.** Pick one device per box. |
| Question mark | Deadpan literalism, pedantic pushback | Free, but a box rarely carries two questions. |
| Repetition | Panic or menace rendered as repeated text rather than described | One repeated phrase per character, e.g. "Oh dear, oh dear, oh dear..." (Cook, *Cook's Assistant*) |
| ALL CAPS | Emphasis on a single outsized fact | Once per quest at most: "It's taken me FIVE YEARS, but it's almost ready." (Witch, *Black Knights' Fortress*) |

**British spelling and idiom, always.** honour, colour, favour, armour, realise, civilised,
defence, centre, artefact, licence (noun), apologise, travelling, marvellous. Never
honor/color/realize/artifact/traveling. Idiom skews plain and slightly old-fashioned:
"obliged", "big cheeses", "not up to snuff", "be off wi' you", "Fair enough."

**Interjections that are in register.** These are attested in the corpus and carry
characterisation more cheaply than an adjective:

> "Er...", "Erm...", "Um", "Well...", "Hmm", "Bah!", "Ha!", "Arr,", "Aha,", "Alas,", "Oh dear",
> "Egad...", "Golly", "Oooh", "Nope", "Okay" (as in "Okay, jump aboard then." — Customs officer),
> "Right...", "Now then", "Nilsal!" (Varlamore only)

A single interjection does the work a full descriptive sentence would otherwise need.

**Interjections that are NOT in register.** These are modern, American, or internet-shaped and
appear nowhere in the corpus: "Yikes", "Whoa", "Dude", "My bad", "No worries", "Sure thing",
"Gotcha", "Uh-huh", "Seriously?", "Anyway...", "Like, ...", "Ugh", "OMG", "Welp", "Yep".

**Register split by class.** Formal NPCs (queens, curators, archmages, highlords) use complete,
unclipped sentences and subordinate clauses. Working and comic NPCs clip, contract, and drop
words freely. This gap is a characterisation tool, not an inconsistency — use it deliberately.

**Dialect is phonetic, never grammatical.** Pirates, ogres, ghosts and old sailors keep normal
word order and vocabulary; only spelling and a handful of stock words change ("ye", "yer",
"arr", "fer", "sez", dropped g's). Markers are sprinkled, not applied to every word.
Comprehension always beats authenticity. Reserve dialect for a narratively marked group —
pirates, goblins, giants — and never give a generic dock worker or guard a pirate accent just
because they stand near water.

---

## 2. The refusal grammar (the most important section)

Every one of the five reports independently converges on the same finding: **a bare "no" is
essentially absent from OSRS**. An unexplained refusal reads as a game-logic wall rather than a
character, and the game does not write walls. The gatekeeper — guard, customs officer, captain,
official, foreman — is the single most common NPC shape you will write, and it has a fixed
grammar.

### The template

```
[soft hedge]  +  [the rule, stated impersonally]  +  [why the rule exists / who it applies to]
              (+ implied or explicit path to reversal)
              (+ instant warm switch, in fewer words, once satisfied)
```

Each part in detail:

1. **Soft hedge.** "Sorry, but...", "I'm afraid...", "Well...", "Not for...". The apology token
   makes the NPC read as regretful rather than hostile. It costs two words and it is present in
   nearly every refusal in the corpus.
2. **The rule, not the preference.** The refusal is about policy, clearance, a charter, an
   order, a uniform, a rank — never about the NPC's own feelings toward the player. Working-class
   NPCs may substitute an excuse (busy, tired, not my job) but it is still framed as a fact
   external to their opinion of you.
3. **The reason.** One clause. Not a paragraph. "Because Asgarnia has banned the import of
   intoxicating spirits." is the whole justification, and it is enough.
4. **The path to reversal.** OSRS refusals are puzzle clues, not dead ends. The NPC states —
   or clearly implies — the exact condition that flips the no to a yes: an item, a uniform, a
   fee, a proof, a rank. Even when the player cannot meet it yet, the door is visibly hinged.
5. **The warm switch.** The moment the condition is met, tone flips instantly and the reply gets
   *shorter and warmer* than the refusal was. "Fair enough." "Okay, jump aboard then." "Bah!
   Fine." A satisfied gatekeeper never stays stiff.

Two supporting habits:

- **Voice the suspicion.** Let the gatekeeper narrate their own detective work out loud before
  ruling — "Hang on a sec... Aren't those Karamja gloves?" — so the verdict feels earned rather
  than arbitrary.
- **Address the player generically.** "traveller", "friend", "matey", "mate", "citizen",
  "adventurer". Gatekeepers essentially never use the player's actual name.

### Six verbatim examples

1. "Sorry, traveller, but you don't have permission to enter the Kingdom of Varlamore." —
   Border Guard, *Varlamore* (ambient)
2. "Well rules are rules, I'm afraid. Without proof, I can't take any orders from you." —
   Knight, *Twilight's Promise*
3. "I'm afraid not, only the big cheeses get to dine with the Duke." — Cook, *Cook's Assistant*
4. "I'm sorry comrade, you do not have the clearance to go through these doors." — KGP Agent,
   *Cold War*
5. "Sorry citizen. Only Knights of Ardougne are allowed in the prison." — Knight of Ardougne,
   *Song of the Elves*
6. "Sorry, but you need to be a better hunter before we can help you out." — *At First Light*

### The reversal, verbatim

- Path stated: "Come back when you've got 30 coins for me." — Captain Barnaby
- Path stated: "Well come back when you have it." — Archmage Sedridor, *Rune Mysteries*
- Warm switch: "Fair enough." — Border Guard, *Varlamore*
- Warm switch: "Okay, jump aboard then." — Customs officer, Port Sarim
- Warm switch: "Bah! Fine." — Wizard Mizgog, *Imp Catcher*
- Prop flips status in one line: "This crest will mark you as a representative of the Queen
  herself." — Furia Tullus, *Twilight's Promise*

### Refusal registers by class

- **Authority figures** refuse with rules and clearance, and do not over-explain. The more
  institutional power an NPC has, the less they justify themselves: "No. I can't take any risks.
  Now I'm afraid I have business to attend to." (King Lathas, *Song of the Elves*)
- **Guards and low officials** refuse procedurally and notice concrete, checkable details —
  a uniform, a badge, an entrance — rather than issuing vague menace: "You're not even wearing
  proper guards uniform!" (*Black Knights' Fortress*)
- **Working-class and comic NPCs** refuse with excuses and mild threats: "Stop bothering me,
  can't you see I'm busy!" ('Gummy', *Tower of Life*)
- **Villains** are the only characters allowed to refuse with direct threat and no deflection:
  "Silence!! Or I will remove your head." (Necrovarus, *Ghosts Ahoy*)

### The one hard prohibition

**A refusal never targets the player's character or worth.** It targets policy, cargo, timing,
rank, or an unmet condition. The harshest ambient refusal in the entire corpus — "No, then we
would just mock you" (Trader Crewmember) — still reads as banter. NPCs may doubt the player's
*credentials* ("You're not a pirate yet by a long shot!"); they may not sneer at the player's
*value as a person*.

---

## 3. Humour catalogue, ranked by frequency across all eras

### 1. Deadpan understatement / dry deflation — the single most common device
**Rule:** state a disproportionate or disturbing fact in the same neutral tone you would use
for the weather; never explain the joke.
- "Well, that doesn't surprise me much." — Bill Teach, *Cabin Fever*
- "I am confident that you will probably survive this mission." — Itzla Arkan, *The Heart of Darkness*
- "Well, good job I found it again." — Wizard Persten, on losing an ancient artefact, *Temple of the Eye*

*Used by:* almost everyone, but heaviest in competent-but-flawed professionals — captains,
wizards, princes, worldly old men. It is the default humour of authority, because it is dry
rather than silly.

### 2. Deadpan literalism / pedantry
**Rule:** have the NPC seize on the literal wording of the player's request and answer *that*,
or question its logic, instead of answering the intent. The mismatch is the joke.
- "Give me a quest what?" — Wizard Mizgog, *Imp Catcher*
- "What am I, an encyclopedia?" — Cook, *Freeing Pirate Pete*
- "You're asking me how you should interrogate me?" — Guard, *Children of the Sun*

*Used by:* craftsmen, cooks, shopkeepers, low-ranking guards, and any NPC being interrupted at
work. Not used by villains.

### 3. Procedural / rule-citing absurdity
**Rule:** give the official a rule, a clearance level, or a chain of command, and have them cite
it mechanically instead of reacting to the plot. The refusal is about the rule, never about the
NPC's preference.
- "No ID, no entry!" — KGP Agent, *Cold War*
- "I am afraid our charter prevents us using espionage in any form." — Sir Amik Varze, *Black Knights' Fortress*
- "Report? I don't think so! It's our day off!" — Knight, *Twilight's Promise*

*Used by:* guards, customs officers, butlers, knights, foremen, council members. See the
prohibition on form numbers in the "not in the voice" list below.

### 4. Bathos / anticlimax
**Rule:** escalate verbally, then puncture it with one flat, mundane sentence. Follow a dramatic
declaration with something small, practical, or petty.
- "Ah, excellent! Such should be the fate of all traitors." → "My deepest sympathies." — Itzla Arkan, *Twilight's Promise*
- "Well... it's just two Penguins; Penguins disguised as a sheep." — Fred, *Sheep Shearer*
- "I have my castle back, if not my soul." — Rologarth, *Creature of Fenkenstrain*

*Used by:* comic-relief NPCs and, most effectively, by serious characters who don't realise
they've deflated the moment.

### 5. Vanity / self-importance
**Rule:** let a minor NPC announce their own job title, slogan, or superiority with unearned
pride, or correct the player's credentials before answering the actual question.
- "I run something of a shipping company. If you need it moving, Xocotla's Got Ya!" — Xocotla, *Death on the Isle*
- "Really now? I am the Archmage you know." — Archmage Sedridor, *Rune Mysteries*
- "Oh well, at least I'm cleverer, prettier and will have a better bed." — Ava, *Animal Magnetism*

*Used by:* one throwaway NPC per area is the right dosage. Merchants and mid-ranking specialists
carry it best.

### 6. Laziness and the job gripe
**Rule:** the NPC's real objection is effort or their own workload, not danger or morality —
and they say so plainly. Give a working NPC exactly **one** recurring gripe about their specific
labour, never a list of woes about life.
- "Quit bugging me then! I got me some fish to catch!" — Joshua, *Fishing Contest*
- "Sorry, but I have lots of work to do before we head back out to sea." — Sailor, Varlamore (ambient)
- "I'm sorry, but the vineyards need tending to." — Vineyard worker (ambient)

*Used by:* every working NPC in the game. This is the ambient layer's backbone.

### 7. Needling the player's credentials
**Rule:** minor NPCs openly doubt the player's competence or standing rather than deferring to
them. Aim at the *claim* or the *plan*, never at the player's worth.
- "You're not a pirate yet by a long shot!" — Bill Teach, *Cabin Fever*
- "Naaah...you no real deal." — Nung, *Freeing Pirate Pete*
- "You don't strike me as being... devout, you know?" — Davey, *Rum Deal*

*Used by:* gatekeepers testing a claim, and low-status comic NPCs. Authority figures test more
formally: "No. You have not yet proven yourself enough to be trusted with that information."
(Osman, *Prince Ali Rescue*)

### 8. Running gag / verbal tic
**Rule:** give a recurring named NPC one invented noun or signature phrase for an ordinary thing
and reuse it verbatim every time. Or repeat a near-identical line from a *second* character to
turn a quirk into a structural joke.
- "Da sickies" — Grish, *Zogre Flesh Eaters* (repeated across the quest)
- "Me bigger general!" — said by both goblin generals in turn, *Goblin Diplomacy*
- Rotating farmer-couple names in *Scrambled!*: "Rumpty Bumpty," "Gumty Fumty," "Humphrey Dumphrey."

*Used by:* a handful of recurring, named characters only. Do not give every NPC a catchphrase.

### 9. Gallows / dark comedy
**Rule:** name a gruesome consequence casually, in a list, alongside something mundane.
- "I'll have ye flogged, hung, slapped with a haddock and sent back out there!" — Captain Braindeath, *Rum Deal*
- "The corpses thing was just a hobby." — Dr Fenkenstrain, *Creature of Fenkenstrain*
- "We lost three diving parties that way!" — Murphy, *Freeing Pirate Pete* (said cheerfully)

*Used by:* pirates, undertakers, mad scientists, veterans of dangerous trades.

### 10. Comic cowardice
**Rule:** let a supposedly tough archetype confess fear in plain language, undercutting the
stereotype — one line, right before or during the climax.
- "Look, I think I'll wait here for someone a little more... you know... heroic." — Pirate Pete, *Rum Deal*
- "Oh boy, this doesn't look good!" — Black Knight Captain, *Black Knights' Fortress*
- "That thing looks brutal! We don't want to kill him!" — Captain Vibia, *The Final Dawn*

### 11. Faction ribbing and non-answer flattery
**Rule (ribbing):** one aside mocking a rival group, framed as friendly redirection rather than
hostility. **Rule (flattery):** give a stock compliment that fits any player answer, exposing
that the NPC isn't really listening.
- "If Saradomin's not good enough for you, those otherworldly freaks from Arceuus are probably your kind of people." — Monk (ambient)
- "Ah, a very noble profession." — Citizen, Varlamore, in reply to any stated occupation (ambient)
- "Only humans would ever manage to reach this level of silliness." — Attala, *The Final Dawn*

*Used by:* priests and monks (ribbing), idle citizens (flattery).

### 12. Self-aware / lampshading, and the fourth-wall wink — rarest
**Rule (lampshading, safe):** let a character name the trope out loud instead of playing it
earnestly. This is in-world and always available.
**Rule (fourth wall, dangerous):** at most one wink per quest, delivered as a throwaway question
by a comic secondary character, never by a quest-critical authority figure, and never as a
direct "this is a game" statement.
- "It's always the butler, isn't it." — Stradius, *Death on the Isle* (lampshading)
- "A loose cannon? A wild card maybe. Or a lone ranger. But a loose cannon?!" — Haig Halen, *Ethically Acquired Antiquities* (lampshading)
- "Old School RuneScape isn't that sort of fantasy game, in case ye hadn't been told that a-fore today." — Ithoi the Navigator, *The Corsair Curse* (a genuine wink — this is the ceiling, not the norm)

---

### Devices that are NOT in the game's voice

Mark these off. None of them appear in the corpus, or they appear in a strictly narrower form
than modern writers assume.

- **Modern meme humour, internet slang, and pop-culture references as such.** The one Star Wars
  riff in the corpus ("This is not the Karamja rum you are looking for.") is a *charm option
  phrased as an in-world sentence* — it works because it reads as plain speech to anyone who
  misses the reference. Anything that only functions as a reference is out.
- **Sarcasm aimed at the player's worth.** Doubting a *claim* is in voice; contempt for the
  player as a person is not. Refusals target policy, cargo, timing, or rank — never character.
- **Fourth-wall jokes as a habit.** One wink per quest, from a comic secondary. Random events
  (the Mysterious Old Man and company) are the game's one licensed home for meta-strangeness,
  and even there it is delivered totally straight, in-world.
- **Bureaucratic form-number and stamp gags.** OSRS officials cite *rules, orders, charters,
  clearance, and uniforms* — never form numbers, stamps, filing, permits-in-triplicate, or
  "you'll need to fill out a...". The paperwork joke is a different franchise's.
- **Puns and wordplay as a default tool.** Heavy in 2001-2004, largely abandoned by modern
  Jagex writing in favour of situational irony and character consistency. Avoid unless writing
  deliberate classic pastiche.
- **Explained jokes and extended riffing.** Land the line and move on. A joke never runs two
  beats.
- **Cruelty from the player.** Player sarcasm is self-deprecating or mild, never cruel.
- **Villain monologues.** One dry or fearful aside instead of a speech.
- **Editorialising narration.** See section 6.
- **Uniform wit.** Not every NPC is funny. Comedy clusters on specific personalities; authority
  figures get *colder and drier* humour, not sillier.

---

## 4. Authority and warmth

Tone scales inversely with institutional power in a very consistent way:

> **The higher the NPC's status, the more clipped and formal their diction, the lower their
> joke density, and the less they explain themselves. The lower the status, the chattier,
> warmer, more digressive, and more willing to complain or joke.**

Status is also signalled by *how the NPC addresses the player*: titles and generic nouns
("traveller", "citizen", "adventurer") from officials; first names and "friend"/"matey"/"mate"
from workers and allies.

**Guards** — procedural, rank-conscious, cite orders over judgement, notice concrete checkable
details. Curt to strangers, instantly deferential to a seal, crest or proof. They do not
philosophise.
> "This is the only land border into the Kingdom of Varlamore. We're here to make sure no one
> enters without permission." — Border Guard, Varlamore

**Officials and authority figures** (kings, curators, highlords, archmages, council members) —
complete sentences, formal grants and denials, no over-explanation, occasional cold dryness.
They test the player before trusting them.
> "I grant you permission to search for these cultists." — Highlord Katlo, *The Final Dawn*

**Workers** (fishers, dock hands, vineyard labourers, sailors, cooks) — short, warm, task-focused,
one gripe about their specific job, easily interrupted and slightly resentful of it. They treat
the player as an interruption or a spare pair of hands.
> "Not too bad, but the work is definitely tiring." — Worker, Varlamore

**Priests and monks** — formal, faintly archaic greeting; state the god's virtue rather than
defending territory; deflect deeper requests upward to a named superior rather than refusing;
bless on parting; permitted exactly one dry jab at a rival faith.
> "We offer sanctuary to those seeking the guidance of Saradomin." — Monk
> "Peace brother" — Monk, as a farewell

**Hermits and old men** — withhold their real purpose; mystery over exposition; never a straight
answer about themselves; philosophical asides that land as dry wit; the only archetype licensed
to use the player's actual name, where it functions as a deliberately unsettling device.
> "Here, take this. But tell no one I was here." — Mysterious Old Man
> "Less of the 'old' man, if you please! ... I prefer to think of myself as a sage." — Wise Old Man

**Villains** — short, control-asserting sentences, refuse with direct threat rather than
deflection, explain nothing until a late reveal.
> "Then may your death come quick, human." — Catalytic Guardian, *Desert Treasure II*

**Do not make every guard or official identical.** Vary between procedural-calm (rule-quoting)
and gruff-dismissive (three words and a stare), and let at least one show dry warmth.

---

## 5. Player-line conventions

- **Register parity.** The player speaks in the same short, punchy register as the NPCs around
  them — 6-13 words, first person, complete natural sentences. The player never out-eloquences
  the world.
- **Confident, never grovelling.** Player lines stay assertive even toward high-status NPCs:
  "I don't care. I'm going in anyway." (*Black Knights' Fortress*). No extended apologising.
- **Allowed naivety.** The player may ask plainly expository or even silly questions without
  being written as stupid for it — "What can you tell me about Varlamore?", "What's a horse?"
  A naive question is the standard trigger for an NPC's deadpan literalism.
- **Allowed cheek.** Mild sarcasm, dry self-deprecation, and bravado are all in range, but
  bravado is nearly always undercut immediately by an NPC or by circumstance — it never lands
  as pure cool. "I laugh in the face of danger!" (*Black Knights' Fortress*); "Oh, this is
  nothing. All in a day's work for me." (*The Great Brain Robbery*, delivered right before
  something goes wrong).
- **Not allowed: cruelty.** Player sarcasm points at the situation or at the player themselves,
  never contemptuously at an NPC.
- **Plain sincerity.** When the player is sincere, it is short and unembellished: "I'd be happy
  to help!", "Yes, please." Never purple.
- **Admitting weakness is in character.** "Gah! This time travel stuff makes my head hurt!"

**How options are worded.**

- **Full sentences the player would say aloud** — never keywords or fragments. "Where can I find
  Silverlight?" not "Silverlight". The only exception is a bare "Yes." / "No." confirmation.
- **Typical count: 2 to 4.** Ambient and gatekeeper trees top out around 4 live options per node.
  Quest hubs occasionally run higher; a dock conversation should not.
- **Mix the kinds.** A well-formed menu contains one information-seeking question, one
  commit/progress choice, and one stalling or exit choice. Menus front-load curiosity (why, how,
  what else) before commitment.
- **Questions vs statements.** Roughly even. Info options are questions; decisions and exits are
  declarative: "I'd better be off.", "Alright, I'll get to it.", "That'll be all for now."
- **Soften the decline.** A polite refusal option is always softened — "No, thank you.", "I'm
  good thanks.", "Not right now, thanks." — never a bare "No."
- **Give options personality.** One earnest, one dry or cheeky, is the house pattern. Neutral
  placeholder options are a wasted line.
- **Bracketed tone tags** are a real, rare precedent: "[Charm] This is not the Karamja rum you
  are looking for." Charm options are flavour; they do not skip a gate.

---

## 6. Narration boxes

- **Second person, present tense, addressed to the player.** "You show the crest to the
  knights.", "You search the chest and find some cultist robes.", "You climb down the steps."
- **Dominant formula:** `You [verb] the [object]` or `You [verb] the [object] and [result]`.
  Compound outcomes use "and", not a second sentence.
- **Third person for NPC and world actions, equally flat:** "Ennius kills Velam.", "Ithoi rushes
  out of the hut.", "The gate is securely locked."
- **One short sentence.** Longer narration is reserved for cutscene stage direction and stays
  script-like and functional.
- **No editorialising.** No emotional adverbs, no interpretation, no adjectives doing mood work.
  Narration stays neutral even during the most dramatic beat in the quest: "Alan falls to the
  ground, dying at the bottom of the stairs." The feeling belongs to the dialogue that follows.
- **Humour in narration is rare and always understatement**, appended as one extra clause to an
  otherwise factual report — "The rope falls off the mast! Clearly your knots are sub-sailor
  standard." Never a joke construction of its own.
- **Ambient chat almost never uses narration boxes at all.** In the entire ambient corpus, not
  one gatekeeper or small-talk tree used a narration box. Reserve them for the moment something
  is actually shown, given, or done — a proof produced, an item handed over, a door opened.

---

## 7. Varlamore flavour

**"Nilsal"** is the region's greeting — an in-world "hello", used interchangeably with "Hello"
by citizens, tourists, fishers, and the vineyard foreman. Verbatim attestations: "Nilsal!" /
"Nilsal, friend. How goes it?" / "Nilsal! It's going well, thanks." / "Nilsal! I'm pretty good,
thank you." Use it as colour on a **local NPC greeting the player** — never in the player's own
lines, and not from every NPC in a scene or it stops reading as flavour. Its sibling exclamations
"Kuani!" and "Tetamo!" appear in the same register.

**"Iknami"** is a respectful term of address applied *to the player* by Fortis-culture NPCs
(Jatziri, Curator Herminius). Sparingly.

**Ralos and Ranul.** Ralos is the sun-god of the region, invoked in formal or earnest speech —
"...and with the guidance of Ralos..." (*The Final Dawn*) — with a "Church of Ralos" and a rival
"Sect of Ranul" (*Twilight's Promise*), signalling competing religious factions inside the
Kingdom. Ralos is light, day, guidance; Ranul is the twilight counterpart.

**The Kingdom.** Officials use the full formal name — "the Kingdom of Varlamore" — and reference
**the Queen** as the authority who controls entry and permissions: "the Queen is currently making
arrangements to allow outsiders like yourself to enter Varlamore." (Border Guard)

**Civitas illa Fortis** is the capital, referred to by officials simply as "the capital".
Aldarin is the southern city with the vineyards; the Sunset Coast is the southern shoreline.

**The Colosseum** is the Fortis arena; its champions are the region's proven heroes, and
Dizana's quiver is the reward and the visible proof of the title.

**Roman-flavoured naming.** Fortis-culture personal names skew Latinate: Tullus, Cento, Vibia,
Herminius, Regulus, Ennius, Stradius, Furia. Mesoamerican-flavoured names run alongside them for
the older cultures: Itzla Arkan, Zyanyi Arkan, Xocotla, Kauayotl, Jatziri, Katlo, Metzli.
Institutions take Latin forms ("Civitas illa Fortis"). When inventing a Fortis official, a
two-part Latinate name is always safe.

**How locals treat outsiders.** Fortis city-dwellers are used to travellers and default to
polished, transactional politeness. Tourists are openly delighted with the region ("Isn't this
place wonderful?"). By contrast, the older and more insular groups — Cam Torum dwarves,
jaguar-worshipping farmers — default to suspicion of the player as "human"/outsider and soften
only through earned trust. Guards are procedural to a stranger and instantly deferential to a
crest or seal.

**Ambient Varlamore is thin and heavily reused.** Civitas illa Fortis and Aldarin citizens share
verbatim stock lines (busy / no-buy / weather), so reusing a small shared bark pool across
several NPCs in one area is authentic, not lazy.

### What does NOT exist on the wiki — where you are inventing

Be honest with yourself about the boundary between reproducing and extrapolating:

- **No Ralos pilgrim transcript exists.** There is no documented pilgrim, priest or devotee of
  Ralos to copy. The nearest template is the Saradomin **Monk**: greet formally, state the god's
  virtue, deflect authority upward to a named superior, bless on parting, one dry jab at a rival
  faith. Everything Ralos-specific in a pilgrim's mouth is invention on that scaffold.
- **No vintner or wine-merchant transcript exists.** The nearest sources are the **Vineyard
  worker** and **Vineyard foreman** (Varlamore labourers, who do use "Nilsal" and do plug the
  product) plus generic merchant patter. A vintner as a *trader* character is extrapolation.
- **No Harbourmaster transcript exists** (the page 404s). The role must be assembled from the
  Port Sarim **Customs officer**, **Captain Tobias**/**Captain Barnaby**, and the Varlamore
  **Border Guard**.
- **No Guard (Varlamore) or Guard (Civitas illa Fortis) ambient transcript exists.** Varlamore
  guard voice comes from the **Border Guard (Varlamore)** page plus in-quest guard lines from
  *Twilight's Promise*.
- **The ambient layer contains no mentions of Ralos, Ranul, the Colosseum, or the sea as a
  symbol.** All ambient Varlamore lore colour is extrapolated from quest transcripts.
- **Transcript:Curse_of_the_Moon** does not exist under that title (404).

---

## 8. Role sheets

### The Fortis guard at the harbour

**Register:** Clipped to the bone. Procedural, rank-conscious, cites orders instead of reasons.
Notices concrete details. Zero small talk, zero curiosity about the player. The shortest lines
of any character on the dock — often one or two words. Addresses the player as "traveller" or
"citizen", or not at all. Warms only as far as "Fair enough."

**Would say:** the rule; who the rule applies to; "No."; a flat acknowledgement when satisfied.
**Would not say:** an explanation he wasn't asked for; a joke; a compliment; anything about his
own feelings; the player's name; a hint he isn't obliged to give.

- "Sorry, traveller, but you don't have permission to enter the Kingdom of Varlamore." (verbatim, Border Guard)
- "Fair enough." (verbatim, Border Guard — the entire warm switch)
- "Move along, citizen." (verbatim, Varlamore guard register)

### The harbourmaster

**Register:** The gatekeeper proper. Polite, official, unhurried; states the rule then applies it
to the player. Uses the formal name of the polity. Announces his own procedure aloud. Warms
instantly and briefly once satisfied, dropping into a shorter, friendlier line. Addresses the
player as "traveller" or "friend".

**Would say:** the closure order and who it exempts; what he is required to check; where the
order came from; a clean, warm one-line yes.
**Would not say:** the exact proof he wants before the player has claimed anything; a bribe price;
an insult; a lengthy apology.

- Adapted from Customs officer: state the rule, then the process, in two short beats — "You need
  to be searched before you can board." → "Because Asgarnia has banned the import of intoxicating
  spirits."
- "Come back when you've got 30 coins for me." (verbatim, Captain Barnaby — the model for a
  stated path to reversal)
- "Okay, jump aboard then." (verbatim, Customs officer — the model warm switch)

### The fisher on the Sunset Coast

**Register:** Warm, local, chatty in one-sentence bursts. Mood is reported through the day's
catch rather than through feelings. Uses "Nilsal". Has exactly one gripe, and it is about fish or
boats — never about life. Sympathetic to the player's problem but not empowered to solve it.

**Would say:** how the catch is going; what the closure has done to her trade; a friendly
redirect; an easy, delighted yes.
**Would not say:** anything official; anything about the Queen or the capital; a rule she has read
rather than heard.

- "Hello! Nice day for fishing!" (verbatim, Fisher)
- "Nilsal! It's going well, thanks." (verbatim, Fisher)
- "Well given how good today's catch was, I'd say it's going pretty well!" (verbatim, Fisher)

### The vintner from the Aldarin vineyards

**Register:** Merchant warmth over merchant anxiety. Greets with "Nilsal", plugs the product
unprompted, and treats the closed lanes as a commercial catastrophe rather than a political one.
Vanity is permitted — about the wine, not himself. One gripe: the stock is sitting.

**Would say:** what the closure costs him; an offer of a drink; admiration for anyone who can
actually sail; his product's virtues.
**Would not say:** the Kingdom's reasoning as if he agreed with it; a paperwork complaint; a jab
at the player.

- "Nilsal, friend. How goes it?" (verbatim, Vineyard foreman)
- "Have you tried the wine? You won't regret it if you do!" (verbatim, Vineyard worker)
- "Lovely day for some wine!" (verbatim, Vineyard worker)

### The pilgrim of Ralos

**Register:** Formal, faintly archaic, patient. Built on the Monk template because no Ralos
pilgrim transcript exists. Greets with "Greetings, traveller"-style formality rather than
"Nilsal". States the god's virtue instead of arguing; defers to authority rather than defying it;
blesses on parting. Permitted exactly one dry, pious aside.

**Would say:** where he was hoping to sail and why; that patience is a discipline; a blessing;
a gentle deferral upward.
**Would not say:** an angry word about the Kingdom; a bribe; a boast; a joke at anyone's expense
other than, mildly, his own faith's rivals.

- "Greetings traveller." (verbatim, Monk)
- "We offer sanctuary to those seeking the guidance of Saradomin." (verbatim, Monk — the pattern
  to adapt: state the god's virtue, not the order's power)
- "Peace brother" (verbatim, Monk — the devotional farewell in place of "bye")

### The mysterious old man

**Register:** Cryptic, unhurried, never a straight answer about himself. Mystery over exposition
is the whole character. The one archetype licensed to use the player's name, and to be slightly
unsettling with it. Philosophical asides land as dry wit, delivered totally straight. His nod to
the random-event NPC is a hint that he travels by means the player doesn't have and won't be
explained — never a statement about the game.

**Would say:** an oblique answer; a request for secrecy; a remark that implies he has been
everywhere and will be gone shortly.
**Would not say:** anything that names a game system; a rule he is bound by; a clear account of
who he is; an unearned kindness.

- "Here, take this. But tell no one I was here." (verbatim, Mysterious Old Man)
- "I've strode through the depths of the deadliest dungeons, roamed the murky jungles of
  Karamja..." (verbatim, Wise Old Man — experience told as a list of places, never as stats)
- "That's called 'money'... but when you've got as much as I have, you realise that money
  doesn't matter." (verbatim, Wise Old Man — the dry philosophical undercut)

---

## 9. Twenty rules

### Ten do

1. Keep every ordinary NPC box to one or two short sentences, 5-20 words, under ~110 characters.
2. Open every refusal with a soft hedge ("Sorry, but...", "I'm afraid...", "Well...") before the rule.
3. State the rule as policy external to the NPC, then give exactly one clause of reason.
4. Name — or clearly imply — the condition that would flip the "no" to a "yes"; refusals are puzzle clues, not dead ends.
5. Flip tone instantly and warmly the moment the condition is met, in fewer words than the refusal took.
6. Let the gatekeeper voice their suspicion or reasoning aloud before ruling, so the verdict feels earned.
7. Give each working NPC one job-specific gripe and reuse it, instead of a list of woes.
8. Write narration boxes as flat second-person "You [verb] the [noun]" with no editorialising.
9. Use British spelling and plainly old-fashioned idiom throughout.
10. Give player options real personality — one earnest, one dry or curious — phrased as full spoken sentences, 2 to 4 per menu.

### Ten don't

1. Don't write a bare "no" with no stated reason — it reads as a game-logic wall, not a character.
2. Don't aim a refusal or a joke at the player's worth; target policy, cargo, timing, or an unmet claim.
3. Don't put an ellipsis and an exclamation mark in the same box, and never more than one of either.
4. Don't use American spellings anywhere in-world.
5. Don't let an NPC explain their own joke or run it past one beat.
6. Don't use form numbers, stamps, permits or filing gags — officials cite rules, orders, charters and clearance, never paperwork.
7. Don't break the fourth wall more than once per quest, and never as a direct statement; leave meta-strangeness to random-event NPCs, played straight.
8. Don't let authority figures over-explain themselves, and don't make them funny in the same silly way as comic NPCs — their humour is colder and drier.
9. Don't give generic dock workers or guards pirate dialect; reserve accent markers for a narratively marked group.
10. Don't make every NPC witty, or every guard identical — cluster comedy on specific personalities and vary curtness with occasional dry warmth.
