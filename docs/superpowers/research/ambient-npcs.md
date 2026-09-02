# Ambient NPC Dialogue in OSRS — Research Notes

Purpose: model the register of ordinary, non-quest OSRS NPCs (guards, sailors, fishers,
dock officials, merchants, citizens) to write authentic lines for the six Varlamore
charter-dock stand-ins (guard, harbourmaster, fisher, vintner, pilgrim of Ralos,
mysterious old man).

## 1. Sources fetched

Successfully fetched (21 pages/sections):

- Transcript:Guard (Falador party-room variant — generic "Guard" page; low relevance but confirms the bare-bones idle-shout pattern)
- Transcript:Border_Guard_(Varlamore)
- Transcript:Twilight's_Promise (quest cutscene lines spoken by "Guard (Varlamore)" — formal register only, not ambient, used for tone contrast)
- Transcript:Citizen_(Civitas_illa_Fortis)
- Transcript:Citizen_(Aldarin)
- Transcript:Tourist (Varlamore)
- Transcript:Sailor_(Varlamore)
- Transcript:Sailor (Fremennik longboat sailor)
- Transcript:Trader_Crewmember (Trader Stan's crew)
- Transcript:Captain_Tobias
- Transcript:Seaman_Lorris
- Transcript:Captain_Barnaby
- Transcript:Customs_officer (Port Sarim)
- Transcript:Dock_worker
- Transcript:Fisher (Varlamore-style generic fisher)
- Transcript:Vineyard_worker
- Transcript:Vineyard_foreman
- Transcript:Worker_(Varlamore)
- Transcript:Mysterious_Old_Man
- Transcript:Wise_Old_Man
- Transcript:Monk (Saradomin monastery)

404 / not found (moved on per instructions): Transcript:Guard_(Varlamore) (direct page absent; content recovered via Twilight's Promise instead), Transcript:Guard_(Civitas_illa_Fortis), Transcript:Priest, Transcript:Harbourmaster, Transcript:Port_Sarim_guard, Transcript:Bartender, Transcript:Fisherman, Transcript:Shopkeeper, Transcript:Gnome_pilot, Transcript:Barbarian_(Barbarian_Village). Transcript:Hunter_Guild was not attempted once 15+ good sources were secured.

Note: no dedicated Ralos pilgrim or vintner-specific (wine-merchant) transcript exists yet on the wiki (Varlamore ambient NPCs are thin so far), so the pilgrim/vintner sections below lean on the closest analogues: Monk (devotional ambient NPC), Vineyard worker/foreman (Varlamore wine labourers), and Citizen/Tourist (Varlamore small talk, including the "Nilsal" greeting).

## 2. The shape of an ambient conversation

- **Length**: almost all ambient trees are 1–3 exchanges. A single greeting line, one NPC reply (often randomised — the wiki lists "one of five" possible responses picked at random), and the conversation ends. Functional NPCs (captains, customs officers, the Wise Old Man, Trader Crewmember) run longer, branching trees of 5–15 lines because they gate a transaction (fare, search, junk-clearing).
- **Opening**: the player almost always opens, and almost always with the same stock line: *"Hello there. How's it going?"* (citizens, fishers, vineyard workers, sailors, workers) — this is the single most repeated player line in the corpus. Functional NPCs get opened with a task-specific line instead, e.g. *"Can I journey on this ship?"* (customs officer), *"Do you need any help in the vineyards?"* (vineyard foreman).
- **Small talk replies are randomised barks**, not a fixed script: the wiki explicitly notes the Fisher/Citizen/Vineyard-worker/Sailor/Worker pages draw one of ~5 stock replies each time — a mix of a mood answer, a "nice weather" observation, a mild complaint about being busy, and occasionally a foreign-flavour greeting.
- **Closing**: ambient chit-chat has no explicit farewell — it just ends after the single reply (no goodbye line). Functional/gatekeeper trees close with either a transaction resolving ("Well done, matey! Here you go!" — Captain Tobias) or an explicit dead end ("Sorry, but you don't have permission to enter the Kingdom of Varlamore." — Border Guard).
- **Randomisation as a design tool**: reusing the same 4–6 NPC across a whole region (Fisher, Sailor, Citizen, Worker) with a shared random-reply pool keeps dialogue cheap to write while making the world feel populated — worth mirroring for six stand-ins that might reuse a small bark pool each.

## 3. Per role, with verbatim examples

### Guards
- Greeting/challenge: *"Can I help you?"* (Border Guard, Varlamore)
- States purpose plainly: *"This is the only land border into the Kingdom of Varlamore. We're here to make sure no one enters without permission."* (Border Guard, Varlamore)
- Refuses with a clear rule + apology token: *"Sorry, traveller, but you don't have permission to enter the Kingdom of Varlamore."* (Border Guard, Varlamore)
- Accepts pushback gracefully once satisfied: *"Fair enough."* (Border Guard, Varlamore)
- In-quest register (Guard, Varlamore) is clipped and dutiful, addressing nobility: *"Of course, my Prince. We'll go at once."* (Twilight's Promise)
- Idle ambient "Guard" barks (generic, non-Varlamore) are pure alarm shouts, no small talk at all: *"Get to the party room NOW!"* / *"Someone's having a big party!"* (Transcript:Guard)
- Addresses the player as "traveller"; never uses the player's name.

### Sailors and ship captains
- Sailors greet with weary small talk, always some variant of being busy: *"Sorry, but I'm busy right now."* / *"Sorry, but I have lots of work to do before we head back out to sea."* (Sailor, Varlamore)
- Fremennik sailor gatekeeps by relationship, not payment, and is brusque to outsiders: *"Don't talk to me outerlander. I need to fix this longboat. Go talk to the chieftain."* (Sailor)
- Captains open with a menu-style pitch naming destinations and price up front: *"Hello there. Do you want to go on a trip to Karamja? We can take you to Musa Point for only 30 coins."* (Captain Tobias)
- Captains refuse politely on lack of funds, inviting a return visit: *"Come back when you've got 30 coins for me."* (Captain Barnaby)
- Captains reward compliance warmly and informally: *"Well done, matey! Here you go!"* (Captain Tobias)
- Trader Crewmember is the most talkative — chatty about work conditions, refuses odd cargo firmly: *"Sorry, but we aren't a laundry ship. You'll need to leave those dirty bedsheets behind."*
- Trader Crewmember shuts down smuggling attempts flatly: *"I'm afraid we can't take you anywhere if you're trying to smuggle that rum."*
- Address term for the player: "friend", "matey", or no name at all — captains never use the player's actual name.

### Fishers
- Greeting ties directly to their trade: *"Hello! Nice day for fishing!"* (Fisher)
- Mood answers reference the day's catch, not generic feelings: *"Well given how good today's catch was, I'd say it's going pretty well!"* (Fisher)
- Foreign-flavour greeting variant: *"Nilsal! It's going well, thanks."* (Fisher) — "Nilsal" recurs as a Varlamorean-accented hello across citizen/fisher/vineyard-worker barks.

### Dock, customs and harbour officials
- Customs officer opens exactly like a border guard: *"Can I help you?"*
- States the rule, then the process, in two short beats: *"You need to be searched before you can board."* → *"Because Asgarnia has banned the import of intoxicating spirits."*
- Gatekeeping has three resolutions modeled here: pay (*"you need to pay a boarding charge of 30 coins"*), earn a discount by reputation (*"Aren't those Karamja gloves? ... You can go on half price mate."*), or get caught and refused (*"You're not getting on this ship then."*).
- Suspicion is voiced as a direct, almost gleeful accusation: *"Aha, trying to smuggle rum are we?"*
- Self-aware refusal to search further than the rules require: *"I'm not that sort of customs officer."*
- Dock worker is task-barky, no gatekeeping dialogue at all, just work exclamations: *"Blasted thing's broken again!"* / *"Can I get some assistance over here?"*

### Merchants and workers
- Vineyard worker greets warmly, ties mood to product: *"Lovely day for some wine!"*
- Refuses work interruption gently: *"I'm sorry, but the vineyards need tending to."*
- Always plugs the product before parting: *"Have you tried the wine? You won't regret it if you do!"*
- Vineyard foreman (a functional NPC) opens with the "Nilsal" greeting and immediately offers a task: *"Nilsal, friend. How goes it?"* → *"Well we could always use a spare pair of hands!"*
- Foreman explains a rule of the task plainly and visually: *"perfectly ripe grapes are full and vibrant... They'll shine in the sunlight more than others."*
- Generic Varlamore worker's stock complaint bark: *"Not too bad, but the work is definitely tiring."*

### Priests, monks and pilgrims
- Monk greets formally, slightly archaic: *"Greetings traveller."*
- States the community's purpose as devotion, not defense: *"We offer sanctuary to those seeking the guidance of Saradomin."*
- Deflects a deeper request to a named superior rather than refusing outright: *"You'll need to talk to Abbot Langley about that."*
- Restricts access with a simple membership rule, not hostility: *"Only members of our order can go up there."*
- Gets a jab in at a rival faith, showing pious NPCs can have dry humour: *"If Saradomin's not good enough for you, those otherworldly freaks from Arceuus are probably your kind of people."*
- Signs off with a devotional farewell instead of "bye": *"Peace brother"*
- (No direct Ralos-pilgrim transcript exists yet; the monk's pattern — greet formally, state the god's virtue, deflect authority upward, bless on parting — is the best available template for a Ralos pilgrim.)

### Old men and hermits
- Mysterious Old Man never gives a straight answer about himself; he offers a strange invitation instead of small talk: *"Hey, [player name], would you like to come and perform in a mime show?"*
- Uses the player's name constantly and plaintively when ignored: *"Talk to me, [player name]!"* / *"Are you there?"*
- Rewards attention with cryptic secrecy: *"Here, take this. But tell no one I was here."*
- Wise Old Man corrects the player's assumption immediately and cheekily: *"Less of the 'old' man, if you please! ... I prefer to think of myself as a sage."*
- Frames long experience through evocative place-name lists rather than stats: *"I've strode through the depths of the deadliest dungeons, roamed the murky jungles of Karamja... meditated on the glories of Saradomin on Entrana."*
- Deflects begging with a soft refusal + open door: *"I'm not giving out free money, but if you try again later I'd be glad to reward you."*
- Turns a philosophical aside into dry wit: *"That's called 'money'... but when you've got as much as I have, you realise that money doesn't matter."*
- Both "old man" archetypes withhold their real purpose — mystery over exposition is the throughline for this role.

### Varlamore citizens
- Standard bare greeting: *"Hello!"*
- Weather small talk is the default filler: *"Hello there! Nice weather we've been having."*
- Foreign greeting, repeated across roles as regional flavour: *"Nilsal!"*
- Curt, polite refusal of unsolicited sale/chat, same phrasing reused across citizen NPCs verbatim: *"Sorry, but I don't want to buy anything."*
- Same busy-refusal template as workers/sailors: *"Sorry, but I'm busy right now."*
- Curious about the player's job, replies with light flattery regardless of answer: *"Ah, a very noble profession."* (Citizen/Tourist, after being told "I'm a bold adventurer.")
- Tourist NPC layers in visible excitement about the region itself: *"I can't wait to visit the rest of the kingdom!"* / *"Isn't this place wonderful?"*

## 4. Humour devices, ranked by frequency

1. **Deadpan self-deprecation / dry aside** (most frequent in functional NPCs). Example: Wise Old Man, *"but when you've got as much as I have, you realise that money doesn't matter."* Rule: let a worldly character undercut their own gravity with one flat, ironic sentence — don't explain the joke.
2. **Mundane complaint about the job**, delivered as if it were the character's whole personality. Example: Trader Crewmember, *"if I fall overboard because of this getup one more time, I'm going to quit."* Rule: give a working NPC one recurring gripe about their specific labour, not life in general.
3. **Absurd bureaucratic literalism** — a rule applied so precisely it becomes funny. Example: Customs officer, *"This is not the Karamja rum we are looking for."* (a direct Star Wars riff). Rule: let an official quote/misapply the rule in a way that's technically correct and slightly ridiculous.
4. **Backhanded/non-answer flattery**. Example: Citizen, *"Ah, a very noble profession,"* said to any answer including "bold adventurer." Rule: give a stock compliment that works for any player response, exposing that the NPC isn't really listening.
5. **Faction ribbing** (light jab at a rival group, delivered as helpful advice). Example: Monk, *"those otherworldly freaks from Arceuus are probably your kind of people."* Rule: one aside mocking a rival faith/group, framed as friendly redirection, not hostility.
6. **Meta/genre-aware exaggeration**, rare but memorable. Example: Trader Crewmember's magically self-repairing uniform forced on him by his employer. Rule: use sparingly — one surreal detail per NPC at most, delivered totally straight.

## 5. Refusal and deflection patterns for gatekeepers (guards, captains, customs)

This is the closest match to the Varlamore dock NPCs' job (refusing passage), so patterns are collected in full:

1. **State the rule, then apply it to the player** — never refuse before explaining why. Border Guard: *"This is the only land border into the Kingdom of Varlamore... We're here to make sure no one enters without permission."* then *"Sorry, traveller, but you don't have permission to enter."*
2. **Apologetic softener + hard "no"** in the same breath. Pattern: "Sorry, but..." / "I'm afraid..." appears in nearly every refusal (Trader Crewmember, Border Guard, Customs officer, Citizen, Sailor, Worker). Rule: pair the refusal word with an apology token so the NPC reads as regretful, not hostile.
3. **Refusal has a stated path to reversal**, not a dead end. Border Guard: permission is pending on the Queen's arrangements, and later opens once "arrangements have now been made." Captain Barnaby: *"Come back when you've got 30 coins for me."* Rule: gatekeepers should gesture at the condition that would change their answer, even if the player can't meet it yet.
4. **Escalating suspicion is voiced, not silent** — the NPC narrates their own detective work. Customs officer: *"Hang on a sec... Aren't those Karamja gloves?"* / *"Aha, trying to smuggle rum are we?"* Rule: let a gatekeeper comment on evidence/appearance before ruling, so refusal feels earned rather than arbitrary.
5. **Reward compliance instantly and warmly** once conditions are met — the tone flips from formal to friendly the moment the gate opens. Border Guard: *"Fair enough."* Customs officer: *"Okay, jump aboard then."* Rule: don't let a satisfied gatekeeper stay stiff — reward with a shorter, warmer line than the refusal.
6. **A charm/alternate option exists but is rebuffed in-character**, not broken. Customs officer to a bribe/charm attempt: *"[Charm] Or perhaps you could let me travel for free?"* → the officer still requires payment; charm options are for flavour/flirtation, not for skipping the gate outright in ambient content.
7. **Refusal never insults the player** — it's about policy, or about the player's cargo/behaviour, never the player's character. The harshest line found (*"No, then we would just mock you"* — Trader Crewmember, refusing a joke request) is still played as banter, not contempt.
8. **The player is addressed generically** ("traveller", "friend", "matey", "mate") — gatekeepers essentially never use the player's actual name, keeping the tone impersonal-but-not-cold.

## 6. Option-menu conventions

- Menus are short: typically 2–4 options, rarely more (Customs officer's tree tops out around 4 live options per node).
- One option is almost always the polite decline/exit: *"No, thank you."* / *"I'm good thanks."* / *"Actually, I don't want to go anywhere."* — always phrased with a softener ("thanks", "actually"), never a bare "No."
- A `[Charm]`-tagged option recurs as a distinct flavour: it's bracketed in the menu text itself (e.g. *"[Charm] Or I could pay you nothing at all..."*, *"[Charm] This is not the Karamja rum you are looking for."*) — signalling a personality/skill-gated option distinct from plain dialogue choices.
- Options are written in first person, casual register, shorter than the NPC's replies: *"Yes, let's see what you're trading."* / *"Search away, I have nothing to hide."*
- Menus commonly branch into exactly two symmetric destinations plus a decline (Rimmington/Brimhaven/Nowhere; Musa Point/Pandemonium/nowhere) — a simple ternary structure for fare-NPCs.
- Follow-up player reaction lines after an NPC response are common and short: *"Well that was easy!"* / *"Drat..."* — used to close a beat rather than open a new menu.

## 7. Narration box conventions

None of the fetched ambient/gatekeeper transcripts used a narration (asterisk/italic action) box — all content was spoken NPC or player lines. The one asterisk-style aside found was informal onomatopoeia inside a spoken line, not a separate narration box: Monk, *"*hic* What a party! Wow!"* (a drunken monk easter-egg bark). Treat narration boxes as rare in this register — reserve any stage-direction text for quest cutscenes, not ambient barks.

## 8. Varlamore-specific speech

- **"Nilsal"** is the recurring in-world greeting/exclamation, used interchangeably with "Hello" by citizens, tourists, fishers, and the vineyard foreman: *"Nilsal!"* / *"Nilsal, friend. How goes it?"* / *"Nilsal! I'm pretty good, thank you."* Use it as a regional colour word for a local NPC greeting the player, not for the player's own lines.
- **"The Kingdom of Varlamore"** is the formal name used by officials for the polity: Border Guard, *"the only land border into the Kingdom of Varlamore."*
- **The Queen** is referenced as the authority who controls entry permissions (no name given in the border-guard barks): *"the Queen is currently making arrangements to allow outsiders like yourself to enter Varlamore."*
- Civitas illa Fortis and Aldarin citizens share verbatim stock lines (busy/no-buy/weather), suggesting the wiki-documented ambient Varlamore citizen pool is a single shared script reused city-to-city — safe precedent for reusing a small bark pool across the six charter-dock NPCs.
- No direct mentions of Ralos, Ranul, the Colosseum, or the sea-as-symbol turned up in the ambient (non-quest) transcripts fetched — Varlamore's ambient layer here skews to greetings and mundane worker complaints rather than lore-dense flavour text; devotional/god-referencing colour for a Ralos pilgrim will need to be extrapolated from the Monk template (state the god's virtue, defer to a superior figure, bless on parting) rather than copied from an existing Ralos NPC transcript.

## 9. Ten "do" and ten "don't" rules for an ambient gatekeeper NPC

**Do:**
1. Open with a plain, generic greeting or challenge ("Can I help you?") — don't make the NPC initiate lore-dumping.
2. State the rule before stating the refusal, so the "no" reads as policy, not personality.
3. Soften every refusal with an apology token ("Sorry, but...", "I'm afraid...").
4. Gesture at the condition that would reverse the refusal, even if unmet right now.
5. Warm up instantly and briefly once a condition is satisfied — reward should feel like a tone shift, not just an unlocked menu.
6. Address the player generically ("traveller", "friend", "mate") rather than by name.
7. Give the NPC exactly one job-specific complaint or gripe to repeat, not a list of woes.
8. Keep small-talk replies short (one sentence) and rotate 3–5 stock variants so repeat visits don't feel identical.
9. Let suspicion or reasoning be voiced aloud ("Aha, trying to smuggle...") rather than silently gating.
10. Keep a light, dry sense of humour available for one aside — never sarcasm aimed at the player's worth.

**Don't:**
1. Don't have the gatekeeper insult or demean the player — refusals target policy/cargo/timing, never the player's character.
2. Don't write a refusal with no stated reason — arbitrary "no" reads as broken, not authentic.
3. Don't give the NPC a long paragraph of exposition before getting to the point; ambient lines run one to two sentences.
4. Don't use the player's real name in spoken dialogue (reserve name-use for special/mysterious NPCs like the Mysterious Old Man, where it's a deliberate unsettling device).
5. Don't add narration/stage-direction boxes for ordinary ambient chat — that register belongs to quest cutscenes.
6. Don't make every option a serious one; keep one light/charm/needling option per menu where it fits the character.
7. Don't let the player's polite decline read as rude — always phrase it as "No, thank you" / "I'm good, thanks," never a bare "No."
8. Don't give a gatekeeper omniscient knowledge of the player's business — let discovery/suspicion be visible and incremental.
9. Don't repeat the exact same joke device twice for one NPC — pick one humour device (see Section 4) and let it define that character.
10. Don't end an ambient exchange with a farewell line for simple small talk — most ambient chats just stop after the reply; save explicit goodbyes for functional/transactional trees.
