# Classic-Era OSRS Quest Dialogue (2001-2004 Free-to-Play Quests)

Research notes on how Old School RuneScape's earliest quests write NPC dialogue, for
use as a voice reference when authoring new NPC lines that should feel like they
belong in this era of the game.

## 1. Sources fetched

All fetched via WebFetch against the OSRS Wiki transcript namespace
(`oldschool.runescape.wiki/w/Transcript:*`). Category page
`Category:Quest_transcript` was checked first to confirm exact titles.

1. Transcript:Cook's Assistant
2. Transcript:Romeo & Juliet
3. Transcript:Ernest the Chicken
4. Transcript:Demon Slayer
5. Transcript:Dragon Slayer I
6. Transcript:Pirate's Treasure
7. Transcript:Black Knights' Fortress
8. Transcript:Prince Ali Rescue
9. Transcript:Goblin Diplomacy
10. Transcript:Imp Catcher
11. Transcript:Vampyre Slayer
12. Transcript:Shield of Arrav
13. Transcript:The Restless Ghost
14. Transcript:The Knight's Sword
15. Transcript:Sheep Shearer
16. Transcript:Witch's Potion
17. Transcript:Rune Mysteries

(Doric's Quest was skipped — 16 of the 18 target pages were already fetched
successfully, well past the 10-source minimum, and Doric's Quest is a short,
low-dialogue quest unlikely to add new patterns.)

Note on fidelity: quotes below were extracted by an intermediate summarising
fetch and are reproduced as returned; treat exact punctuation/casing as
approximate on a handful of lines even though content and register are accurate
to source. Every quote is kept under 125 characters and attributed to its NPC
and quest.

## 2. Sentence and line shape

- **Line length is short.** Most NPC lines run 5-20 words; exposition
  (lore dumps from officials, wizards, librarians) stretches to 25-35 words but
  rarely more, and even then breaks into short clauses.
- **One idea per dialogue box.** Boxes are built to be read in a couple of
  seconds; multi-clause sentences are the exception, used mainly for formal
  officials (Sir Amik Varze, Chancellor Hassan, Archmage Sedridor).
- **Ellipses are a stock tool** for hesitation, trailing dread, or comic
  timing: "Oh dear, oh dear, oh dear..." (Cook, *Cook's Assistant*); "Ermmm....."
  (Romeo, *Romeo & Juliet*); "Well... it's just two Penguins" (Fred, *Sheep
  Shearer*).
- **Interjections carry tone cheaply**: "Er...", "Um", "Bah!", "Arr,", "Ha!",
  "Oh boy", "Golly", "Oooh". A single interjection does the characterisation
  work a full sentence would otherwise need.
- **Exclamation marks are frequent** but localised to panic, threats, or comic
  outbursts, not routine dialogue — e.g. "He'll sack me! What will I do?"
  (Cook, *Cook's Assistant*); "Noo! Not Silverlight!" (Denath, *Demon Slayer*).
- **Repetition for emphasis** is a recognisable tic: "Oh dear, oh dear, oh
  dear" (Cook, *Cook's Assistant*); "terrible terrible mess" (Cook, *Cook's
  Assistant*); "Me bigger general!" said by both goblins in turn (*Goblin
  Diplomacy*).
- **British spelling and idiom** throughout: "favour", "colour", "armour",
  "honour"; idioms like "big cheeses", "callous beasts", "not up to snuff",
  "be off wi' you".
- **Broken/accented English marks certain speakers**, not as mockery so much
  as a fast dialect shorthand: goblins drop pronouns and articles ("You not
  helping.", "Me hate humans!"); a drunk apothecary lisps ("No help for
  yoush then."); sailors/pirates use "ye", "arr", "canna".

## 3. Humour catalogue

**Bureaucratic absurdity / red tape** — officials refuse for procedural, not
personal, reasons.
- "I'm afraid not, only the big cheeses get to dine with the Duke." — Cook,
  *Cook's Assistant*
- "I am afraid our charter prevents us using espionage in any form." — Sir
  Amik Varze, *Black Knights' Fortress*
- "Only authorised personnel of the VTAM Corporation are allowed." — Straven,
  *Shield of Arrav*
- Rule: make the refusal about a rule or chain of command, never the NPC's
  own preference.

**Deadpan literalism** — the NPC answers the literal words, ignoring the
obvious intent.
- "Give me a quest what?" — Wizard Mizgog, *Imp Catcher*
- "He's that chicken over there." — Professor Oddenstein, *Ernest the
  Chicken*
- "No, D-Y-E, not D-I-E." — Player correcting a goblin, *Goblin Diplomacy*
- Rule: have the NPC take a pun, idiom, or vague request at face value and
  answer that instead of the real question.

**Bathos / anticlimax** — a big build-up resolves into something trivial.
- "Well... it's just two Penguins; Penguins disguised as a sheep." — Fred,
  *Sheep Shearer*
- "Have you been out in the sun too long?" — Fred, *Sheep Shearer* (reacting
  to his own monster hunt)
- Rule: escalate the threat verbally, then puncture it with one flat,
  mundane sentence.

**Dry understatement** — a huge event described in a small, calm sentence.
- "It's taken me FIVE YEARS, but it's almost ready." — Witch (Sarah),
  *Black Knights' Fortress*
- "That's an odd coincidence... Were you planning on making a cake too?" —
  Cook, *Cook's Assistant*
- Rule: pair a small, dry verb ("almost ready", "an odd coincidence") with an
  outsized fact.

**Self-important vanity** — NPCs who prize their own status/possessions above
the plot.
- "That sword belonged to my great-grandfather. Make sure you treat it with
  respect!" — Sir Prysin, *Demon Slayer*
- "Well seeing as you asked nicely... I could do with some help." — Wizard
  Mizgog, *Imp Catcher*
- "Really now? I am the Archmage you know." — Archmage Sedridor, *Rune
  Mysteries*
- Rule: let the NPC correct the player's tone or credentials before ever
  answering the actual request.

**Insults to the player / condescension** — NPCs mock the player's
competence or species.
- "Even human should be able to work that out!" — General Bentnoze, *Goblin
  Diplomacy*
- "Right... You're rather careless aren't you." — Archmage Sedridor, *Rune
  Mysteries*
- "You're a fool, [player name]. Do you really think you'll find four imps
  out of thousands?" — Wizard Grayzag, *Imp Catcher*
- Rule: insult the player's plan or species generally, not their appearance
  or backstory — keeps it snide rather than cruel.

**Laziness / self-preservation over duty** — quest-givers admit they'd
rather not.
- "I canna help ye. And right sorry I be to see a fine young 'un go off to
  yer doom." — Ahab, *Dragon Slayer I*
- "It's rude to shave another person without permission." — Fred, *Sheep
  Shearer*
- Rule: have the NPC state the safe/lazy option as if it were simple common
  sense, not cowardice.

**Running gags / callbacks** — a joke set up early pays off later in the same
quest.
- Cook: "My wife gets sea sick, and I have an irrational fear of
  eyepatches." (seeded early, pays off with the pirate-flavoured quest chain)
- Two goblin generals each insisting "Me bigger general!" (repeated,
  symmetrical) — *Goblin Diplomacy*
- Rule: repeat a near-identical line from a second character to turn a quirk
  into a structural joke.

**Fourth-wall / self-aware asides** — brief nods to the game itself.
- "Sounds a good name for a ship. Are you sure it's not the name of a sword
  rather than a ship?" — Wizard Traiborn, *Demon Slayer* (riffing on naming)
- "So do I get to go to the Duke's Party?" — Player, *Cook's Assistant*
  (flags the quest's own reward mechanic)
- Rule: use sparingly — one wink per quest at most, delivered as a throwaway
  question, never a direct "this is a game" statement.

**Cowardice under pressure** — comic panic that undercuts a serious moment.
- "Oh boy, this doesn't look good!" — Black Knight Captain, *Black Knights'
  Fortress*
- "No need to... get upset... you're scaring the cat." — Black Knight
  Captain, *Black Knights' Fortress*
- Rule: give a villain or authority figure one line of visible, understated
  fear right before or during the climax.

## 4. Refusal and deflection patterns

- **Missing-item stall**: "Well come back when you have it." (Archmage
  Sedridor, *Rune Mysteries*); "The crate is already full." (Luthas,
  *Pirate's Treasure*). The NPC states the blocking condition flatly, no
  apology.
- **Trust-not-yet-earned**: "No. You have not yet proven yourself enough to
  be trusted with that information." (Osman, *Prince Ali Rescue*); "You've
  brought me everything I need! I am saved!" only after full delivery (Cook,
  *Cook's Assistant*) — refusal converts to gratitude only on completion.
  Nothing is granted halfway.
- **Property/territory refusal**: "Get lost. This is private property."
  (Fortress Guard, *Black Knights' Fortress*); "Hey, stay off my ship!
  That's private property!" (Klarense, *Dragon Slayer I*).
- **Redirection to someone else**: "I don't report to Sir Prysin, I report
  directly to the king!" (Captain Rovin, *Demon Slayer*) — refusal names the
  correct authority instead of just saying no.
- **Refusal followed by a testable exception**: NPCs almost always give the
  exact condition that flips a "no" to a "yes" ("only Draynor Manor cabbage
  will do", "you need to put your white apron on first") — refusal is a
  puzzle clue, not a dead end.
- **Grumpy but literal compliance**: "Bah! Fine." (Wizard Mizgog, *Imp
  Catcher*) — a refusal reversed in as few words as possible once pressed.

## 5. How NPCs address and treat the player

- Player is usually addressed as "Adventurer" or not addressed by title at
  all; NPCs rarely use the player's chosen name except for pointed insults
  ("You're a fool, [player name]" — Wizard Grayzag).
- Low-status NPCs (guards, farmers, cooks) treat the player as a useful pair
  of hands and are quick to ask for favours plainly: "I'd be much obliged if
  you could shear them." (Fred, *Sheep Shearer*).
- Authority figures (Sir Amik Varze, Chancellor Hassan, Archmage Sedridor,
  Duke Horacio) are more formal and use conditional, procedural language,
  and they test the player before trusting them ("You have not yet proven
  yourself" — Osman).
- Once the player is helpful, tone visibly warms within the same quest — the
  Cook goes from despair to "You are saved! Thank you!"; Thurgo goes from
  "I don't talk about that anymore" to friendly banter after a pie.
- Villains and gang members address the player with open contempt or threat
  scaled to the player's power: "You've got some guts coming here, Phoenix
  scum!" (Katrine, *Shield of Arrav*).
- Player dialogue, in turn, is confident and slightly cheeky even to
  authority: "I laugh in the face of danger!" (*Black Knights' Fortress*) —
  the player character is never deferential for long.

## 6. Player-line conventions

- Player options are almost always short, direct questions or statements,
  6-13 words: "Where can I find Silverlight?" (*Demon Slayer*); "Have you any
  quests for me?" (*Rune Mysteries*).
- A near-universal quest-start line is a variant of "I'm looking for a
  quest" / "I seek a quest!" / "I am in search of a quest." — this stock
  phrase recurs almost verbatim across many quests.
- Player options sometimes carry dry humour of their own, mirroring NPC
  style rather than staying neutral: "It doesn't really sound like anyone is
  living in fear apart from you." (*Vampyre Slayer*); "So she has hair,
  lips and shoulders... that should cut it down a bit." (*Romeo & Juliet*).
- Bracketed action/tone cues appear in menus, e.g. "(Charm) This is not the
  Karamja rum you are looking for." (*Pirate's Treasure*) — a rare precedent
  for a tagged tone option.
- Player lines never apologise at length; refusals or objections from the
  player are also short and blunt: "I don't care. I'm going in anyway."
  (*Black Knights' Fortress*).

## 7. Option-menu conventions

- Menus mix a practical/informational option, a jokey/cheeky option, and a
  straightforward accept/decline — rarely more than 4 choices.
- Options are phrased as complete, natural sentences the player would say
  aloud, not fragments or keywords.
- A closing/exit option is common and short: "I'd better get going." /
  "Thanks." / "Goodbye."
- Menus are used to let the player be curious (asking "why", "how", "what
  else") before committing to the quest's next step — front-loading
  lore-gathering options before action options.
- Occasional menu option deliberately underlines the game's own logic for
  comic effect: "So do I get to go to the Duke's Party?" (*Cook's
  Assistant*).

## 8. Narration box conventions

- Narration boxes are short, present-tense-adjacent, plain past tense:
  "You put the three pieces together and assemble a map..." (*Dragon Slayer
  I*); "You pack a banana into the crate." (*Pirate's Treasure*).
- They describe only observable action/result, never internal feelings
  ("Wormbrain drops a map piece on the floor." — *Dragon Slayer I*).
- Comic or dramatic narration still stays terse: "Fire rains down on the
  ship." (*Dragon Slayer I*); "The cabbage falls through the hole in the
  ceiling and lands in the potion, destroying it." (*Black Knights'
  Fortress*).
- Conditional narration is explicit and bracketed by context ("If the player
  already has the necessary items:") in the wiki transcripts, reflecting
  in-game branching, not a style choice to imitate directly — but the plain,
  declarative sentence that follows is exactly the target tone.
- Standard closing narration: "Congratulations! Quest complete!" appears
  near-verbatim across nearly every quest.

## 9. Sea, ship, dock and guard flavour

- **Port Sarim / dockside**: casual, matter-of-fact hazard talk — "Oh, I'll
  be fine. I've got work as Port Sarim's first lifeguard!" (Klarense,
  *Dragon Slayer I*); customs/bureaucracy at the dock: "You need to be
  searched before you can board." (Customs officer, *Pirate's Treasure*);
  "Because Asgarnia has banned the import of intoxicating spirits."
  (Customs officer, *Pirate's Treasure*).
- **Karamja boat / crew**: brisk nautical imperatives and old-salt dialect —
  "Splice the mainsail!" / "Swab the deck!" (Cabin Boy Jenkins, *Dragon
  Slayer I*); "Ah, it's good to feel the salt spray on my face once again!"
  (Captain Ned, *Dragon Slayer I*); pirate dialect markers "Arr", "yer",
  "ye" throughout Redbeard Frank's lines (*Pirate's Treasure*).
- **Dragon Slayer's ship quest chain**: ominous but dry warnings rather than
  melodrama — "I canna help ye... to see a fine young 'un go off to yer
  doom." (Ahab, *Dragon Slayer I*); "keep yeself away from that accursed
  place if ye values yer skin!" (Jack Seagull, *Dragon Slayer I*); comic
  relief undercuts the doom-talk immediately after (Melzar: "By the power of
  custard!").
- **Black Knights' guards**: short, suspicious, procedural — "Get lost. This
  is private property."; "I might be new here but I can tell you're not a
  guard."; "You're not even wearing proper guards uniform!" — guards notice
  specific, checkable details (uniform, entrance) rather than vague menace.
- **Pirate's Treasure dock/plantation banter**: pragmatic trade talk mixed
  with rum-runner charm — "There's no rum like Karamja Rum!" (Redbeard
  Frank); "You wouldn't believe the demand for bananas from Wydin's shop."
  (Luthas) — economic/logistics chatter, not swashbuckling monologue.
- Overall: sailors and guards get dialect markers and procedural obstinacy;
  they are rarely poetic — the "doom" and "danger" lines are short and
  immediately grounded by a mundane detail (lifeguard job, uniform check,
  customs rule).

## 10. Do / don't rules

**Do**
1. Keep each dialogue box to one short sentence or two at most; save
   compound sentences for formal officials only.
2. Give refusals a specific, checkable condition that flips them to "yes"
   (an item, a uniform, a proof of trust).
3. Let minor NPCs (guards, sailors, farmers) speak in clipped, procedural
   sentences noticing concrete details.
4. Use one strong interjection ("Bah!", "Er...", "Arr,") to carry tone
   instead of an adjective-heavy sentence.
5. Let a threat or doom-laden line get undercut immediately by something
   mundane (bathos) at least once per quest.
6. Warm an NPC's tone visibly once the player has actually helped them —
   contrast before/after.
7. Have authority figures test or question the player's credibility before
   handing over information.
8. Give player dialogue options genuine personality — cheeky, curious, or
   blunt — not neutral placeholders.
9. Write narration boxes as plain, past-tense, observable action only.
10. Reuse a near-identical phrase from two different characters when it
    serves as a joke (symmetry, echoing).

**Don't**
1. Don't write long paragraphs of exposition in a single box — split it
   across multiple short lines/boxes.
2. Don't make NPCs apologise extensively or hedge for more than a
   sentence — get to the refusal/reason fast.
3. Don't have the player character grovel; keep player lines assertive even
   toward high-status NPCs.
4. Don't use modern slang or anachronistic idiom — stick to plain, slightly
   old-fashioned British phrasing ("obliged", "callous beasts", "big
   cheeses").
5. Don't let jokes run more than one beat — deliver the line and move on,
   no extended riffing.
6. Don't give narration boxes emotional interpretation ("sadly", "joyfully")
   — describe only what happens.
7. Don't make villains monologue at length; give them one dry or fearful
   aside instead of a speech.
8. Don't overuse ellipses/exclamation marks outside genuine hesitation or
   panic — they lose effect if constant.
9. Don't make every guard/official identical — vary between procedural
   (calm, rule-quoting) and gruff (short, dismissive) rather than one
   template.
10. Don't break the fourth wall more than once per quest, and never state it
    outright — keep it a wink, not a statement.
