# The Fortis Colosseum, Dizana's Quiver, and Who Can Let You Leave

Research for choosing an NPC (overlay or spawn) who, once shown Dizana's quiver, unlocks
charter-ship travel out of Varlamore for the plugin's premise. All facts below are sourced
from the OSRS Wiki (fetched live); quotes are wiki phrasing, kept under 125 chars for reuse
in dialogue strings.

---

## 1. The Colosseum and the quiver

**What it is.** The Fortis Colosseum is a 12-wave solo combat minigame on the cliffs east of
Civitas illa Fortis. Players fight increasingly hard waves, culminating in a fight against
Sol Heredit. [Fortis Colosseum]

**History/why it exists.** The Colosseum was built on the site of a temple where convicts
were once sacrificed to Ralos. King Maximus Tullus tore the temple down, declaring the
kingdom "would no longer tolerate human sacrifices," and built the Colosseum instead, a place
where "warriors gather and battle for glory, with the blood spilled in battle symbolising an
offering to Ralos." [Fortis Colosseum] Notably, the wiki also records that "Varlamore sends
thieves to the Colosseum" [Ethically Acquired Antiquities] — i.e. it already functions
in-lore as a place where the state channels people who need to prove or redeem themselves.

**Who runs it.**
- **Minimus** — "a mighty gladiator," a dwarven ex-gladiator who is the **Colosseum Master**.
  He manages day-to-day operations: "Eventually, the kingdom gave me the role of Colosseum
  Master. I still make sure to fit some fighting in..." [Minimus]. He hands out handicaps,
  starts waves, and is the reward-exchange NPC: players trade spare unblessed Dizana's
  quivers with him for 4,000 sunfire splinters or an enhanced Smol Heredit roll. [Minimus]
- **Ueman, Teoki of Ralos** — one of seven religious overseers who manage the Colosseum's
  sacred aspect; Minimus answers to Ueman on matters of the site's holiness. Ueman: "Each
  drop of blood spilt here is a glorious offering to Ralos. It is these offerings that
  sustain him." [Fortis Colosseum]
- **Lanistas** — elite warriors who select and train gladiators. [Fortis Colosseum]
- **Sol Heredit** — final boss, currently the strongest monster in OSRS (combat level 1,563).
  He "would oversee the Fortis Colosseum, watching challengers battle in the stadium for
  glory and fame," stepping in personally only once a challenger proves formidable enough.
  [Sol Heredit] He is fight-only content — seated on a throne, then an active combatant in
  wave 12 — never a standing, talkable NPC elsewhere. Devs deliberately gave him no
  backstory, "opting for players to create their own lore." [Sol Heredit] His formal
  authority over the city itself is undocumented; his oversight is scoped to the arena.

**What winning means in-lore.** Victory is explicitly about earning **Glory**, a reputation
track with titles: Brawler (2,000) → Challenger (5,000) → Gladiator (8,000) → Hero (12,000)
→ Champion (16,000) → Grand Champion (20,000). [Fortis Colosseum] These are usable as
in-fiction rank names for "who counts as proven" (e.g., gate the plugin's unlock at
"Champion" or simply at "has the quiver," since the quiver only drops after clearing wave 12
outright).

**Dizana's quiver, exactly.**
- Reward for defeating Sol Heredit on wave 12; requires 75 Ranged to equip; untradeable.
  [Dizana's quiver]
- Flavor text: "a quiver awarded to those who have proved their worth in the Colosseum."
  [Dizana's quiver] — this line is close to ready-made dialogue for an unlocker NPC.
- It's explicitly a **replica**: "The quiver is one of many used by fighters in the Fortis
  Colosseum, made as replicas of the original that was used by Dizana, the Heroine of the
  Hunt." [Dizana's quiver]
- **Dizana** herself is only referenced as "the Heroine of the Hunt" — no dedicated wiki page
  exists for her as a person; she's a legendary/historical archer figure, not a currently
  standing NPC. The original quiver's fate is an open mystery: "while assumed by many to be
  lost, some believe that the original quiver is mixed in with the replicas used in the
  Fortis Colosseum." [Dizana's quiver] This makes her useful as *offscreen myth* (an
  unlocker NPC can invoke her name) but not as a candidate NPC herself.

**Access to the Colosseum** requires completing **Children of the Sun** (the entry quest to
Varlamore itself) — no additional skill/quest gate on top of that for entry, though a Twilight's
Promise completion unlocks the fast Civitas illa Fortis teleport spell used to get there
quickly. [Fortis Colosseum]

---

## 2. Power structure

**Crown.** Varlamore is a monarchy currently ruled by **Queen Zyanyi Arkan**, the "Sun
Queen," of the **Arkan Dynasty** (founded ~100 years ago by her great-grandfather Mezpah
Arkan after overthrowing Emperor Imafore). [Varlamore] She resides at the **Sunrise Palace**
in Civitas illa Fortis but is frequently absent on kingdom business, returning to her throne
permanently only after **The Final Dawn**. [Sunrise Palace] [The Final Dawn]

**Heir.** **Prince Itzla Arkan**, her son, is "the heir to the throne of Varlamore." He is
the one who, at the end of **Children of the Sun**, personally grants the player leave to
visit Varlamore for the first time: "Prince Itzla grants the player special permission to
visit Varlamore." [Children of the Sun] [Prince Itzla Arkan] He is seen at Varrock Palace,
the Teomat, Quetzacalli Gorge, and Twilight Temple depending on quest state — i.e. he moves
with the plot rather than standing fixed, though he does return to reside at the Sunrise
Palace after The Final Dawn.

**Religious authority.** **Servius**, **Teokan of Ralos** (high priest), is father to Prince
Itzla and the Queen's chief religious/political advisor — "the current ruler of Varlamore
should unite with a Teokan of Ralos" to receive divine sanction. [Servius] He is itinerant:
Crypt beneath Fortis Temple, the Teomat, and the Sunrise Palace, settling at the Teomat after
The Final Dawn to oversee the selection of a new Teokan. [Servius] [The Final Dawn] Ueman,
Teoki of Ralos, is a subordinate cleric specifically overseeing the Colosseum's sacred
aspects. [Fortis Colosseum]

**Guard/military chain.** The **Kualti** are the Queen's elite personal bodyguard — "A Kualti
must never be distracted from their one, crucial purpose: to protect the Queen" — commanded
directly by the Queen, headquartered next to the Sunrise Palace in Civitas illa Fortis.
[Kualti] Below them, the city's general watch is led by the **Guard Captain (Civitas illa
Fortis)**, who "tries to keep the other guards in order," stationed in the **underground
barracks of Civitas illa Fortis** — i.e. not a public street presence. [Guard Captain
(Civitas illa Fortis)] Rank-and-file **Knight of Varlamore** and **Guard** NPCs are posted
around the Kualti Headquarters and the Sunrise Palace (ground and upper floors). [Knight of
Varlamore] [Sunrise Palace] None of these guard-tier NPCs have any documented authority over
travel or permits — they're security, not administration.

**Former dynasty (context/lore-guardrail).** The **Tullus family** ruled Varlamore for most
of its history until Emperor Imafore Tullus's execution ~100 years ago. Their last two
descendants, twins **Ennius Tullus** and **Furia Tullus**, are royal-household servants
(administrative, not military) who are manipulated into believing they're living avatars of
Ralos/Ranul and become cult leaders in the Twilight Emissaries storyline (Twilight's
Promise → The Heart of Darkness → The Final Dawn). Ennius dies; Furia is spared and taken
into custody for treason. [Ennius Tullus] [Furia Tullus] [The Final Dawn] They hold no formal
power today and are not viable "grants passage" figures — they're disgraced/imprisoned by
the current quest state.

**Museum/cultural authority.** **Curator Herminius** is "the head of the Grand Museum in the
north-eastern portion of Civitas illa Fortis," permanently stationed there as its custodian,
and the quest-giver for Ethically Acquired Antiquities (recovering the stolen Xerna's
Diadem). [Curator Herminius] The museum plausibly already displays/discusses Colosseum
history and champions, given the city's culture of glorifying arena victors.

**Harbour and trade (the Fortis Cothon).** The **Fortis Cothon** is "a large harbour on the
north side of Civitas illa Fortis," where charter-ship arrivals dock and where players with
38 Sailing can dock personal boats. [Fortis Cothon] Charter ships worldwide are run by
**Trader Stan** (based at Port Sarim) and his crew; at every other destination, including
Civitas illa Fortis, two **Trader crewmembers** stand at the dock and sell passage — "at each
port, there are two crewmembers, with the exception of Port Sarim and Corsair Cove, where
Trader Stan himself is also present." [Charter ship] [Trader crewmember] **Shipwright
Sennia**, gated behind Children of the Sun + 38 Sailing, is also stationed at Civitas illa
Fortis and handles personal-boat services (buy/destroy/customise/retrieve a boat) — a fixed,
service-desk-style NPC. [Shipwright] No dedicated "harbourmaster" or "customs official" NPC
is documented at the Cothon; the wiki explicitly says it "does not specify who manages the
harbor or identify specific officials like a harbourmaster or customs agents." [Fortis
Cothon] That's a gap the plugin can fill with a spawned NPC without contradicting anything.

**Quest-relevant harbour NPC.** **Nel**, a Knight of Varlamore, is posted at the Fortis
Cothon docks (southwestern area) during Twilight's Promise, investigating a weapons-crate
smuggling lead tied to the ship *Fortis Spark*. [Twilight's Promise] This shows the Cothon
already has an in-lore security/inspection presence during at least one questline, which is
useful precedent for a customs-style figure there.

**Quetzal transport (internal, not overseas).** **Regulus Cento**, "a quetzal keeper for the
Varlamore royal family," works on behalf of Prince Itzla, stationed fixed just outside
Varrock's east gate. He operates the Varrock<->Civitas illa Fortis quetzal shuttle (Primio)
and also surfaces in Ethically Acquired Antiquities. He has no guard/military authority and
is outside Varlamore proper, so he's a weak fit for an in-Varlamore unlocker, but he
illustrates the "royal servant personally authorizes your travel" pattern Itzla already set
up in Children of the Sun. [Regulus Cento]

---

## 3. Unlocker candidates

Ranked roughly by plausibility.

### 1. Minimus, Colosseum Master — 9/10 (overlay)
- **Where he stands:** Fortis Colosseum entrance, Civitas illa Fortis region. Players already
  must walk up and talk to him before every run (Talk-to / Start-wave / Leave), and he already
  runs a reward-exchange dialogue for spare quivers. [Minimus]
- **Why it makes lore sense:** He is the literal authority who certifies Colosseum victories —
  "the kingdom gave me the role of Colosseum Master." He already trades with players who hold
  quivers. Extending his dialogue to "you've proven yourself, I'll vouch for you at the docks"
  requires no new lore, just a new dialogue branch.
- **Early reachable:** Yes — only Children of the Sun required, same gate as the Colosseum
  itself and as Varlamore access in general.
- **Overlay-friendly:** Yes, he stands still at a single entrance point when not mid-wave-setup.
- **Score: 9/10.** Best off-the-shelf fit; the one drawback is he has no stated authority
  outside the arena (he "answers to Ueman" on holy matters, and nothing to the harbour), so
  the writing needs a light hand-wave ("word from the Colosseum Master carries weight
  everywhere in Fortis" or similar).

### 2. A spawned Colosseum Steward/Herald at the Colosseum exit — 8/10 (spawn new)
- **Exact spot:** Just outside the Colosseum's main gate, on the path back toward Civitas
  illa Fortis (the same approach players use after collecting their reward chest).
- **Role:** A steward whose entire job is to formally record Colosseum results for the crown
  ("Fortis records every champion for the Queen's ledger") and, on sight of Dizana's quiver,
  stamps a travel writ.
- **Why it makes sense:** Fits the established pattern that Colosseum glory already feeds a
  public reputation system (the Glory titles), so a steward who logs champions for the state
  is a small, consistent addition.
- **Early reachable:** Yes, same gate as Minimus.
- **Overlay-friendly:** Purpose-built to stand still.
- **Score: 8/10.** Slightly weaker than Minimus only because it's an invented character
  rather than reusing an established one, but it avoids overloading Minimus's existing voice
  and lets you write travel-specific dialogue cleanly.

### 3. A spawned Harbour Registrar in a Cothon customs house — 8/10 (spawn new)
- **Exact spot:** Fortis Cothon, north side of Civitas illa Fortis, in/near the dock building
  where the two Trader crewmembers and Shipwright Sennia already stand — the wiki confirms
  no harbourmaster/customs NPC currently exists there, so this is a clean gap to fill.
  [Fortis Cothon]
- **Role:** A Harbour Registrar/customs clerk who checks travel writs before charter-ship
  crews will sell passage — directly mirroring the real Customs Officer at Musa Point, who
  inspects "peoples' packages" before departure is permitted. [Customs officer]
- **Why it makes sense:** Every other charter port has an unremarkable Trader crewmember;
  Varlamore being newly opened and politically cautious (it was isolationist until Children
  of the Sun) is good justification for it alone having an extra checkpoint. Nel's smuggling
  investigation at this exact dock during Twilight's Promise already establishes the Cothon
  as a place with security/inspection stakes. [Twilight's Promise]
- **Early reachable:** Yes, Cothon is reachable immediately after Children of the Sun.
- **Overlay/spawn-friendly:** Purpose-built stationary desk NPC.
- **Score: 8/10.** Most mechanically elegant (it sits exactly at the choke point — the
  charter ship desk itself) but requires the most invented lore.

### 4. Curator Herminius, Grand Museum — 6/10 (overlay)
- **Where he stands:** Fixed at the Grand Museum, north-eastern Civitas illa Fortis. [Curator
  Herminius]
- **Why plausible:** He is the city's authority on history and (implicitly) on Colosseum
  lore/artifacts; a museum that surely already contains information about the Colosseum and
  its champions and about Dizana. A curator "authenticating" a quiver as genuine proof and
  then vouching for the player to harbour officials is a reasonable civic-authority chain.
- **Caveat:** Wiki confirms no explicit documented tie between Herminius and the Colosseum —
  this connection is inferred, not sourced. [Curator Herminius]
- **Early reachable:** Yes (Children of the Sun; Ethically Acquired Antiquities not required
  to simply talk to him).
- **Overlay-friendly:** Yes, permanently stationed.
- **Score: 6/10.** Thematically pleasant (a curator/scholar recognizing the quiver as
  significant) but requires inventing his authority over ships, which is a bigger leap than
  Minimus.

### 5. Guard Captain (Civitas illa Fortis) — 4/10 (overlay, weak)
- **Where he stands:** Underground barracks of Civitas illa Fortis — not a public,
  passer-by-accessible spot. [Guard Captain (Civitas illa Fortis)]
- **Why plausible:** He is the actual chain-of-command head for the city watch, "tries to
  keep the other guards in order," a natural analogue to Border Guards/Customs officers who
  gate travel elsewhere.
- **Caveat:** His location is a barracks basement, not somewhere a departing player would
  naturally detour to, and nothing ties him to harbour or travel administration. Would need
  either relocating him (contradicts his documented spot) or writing a justification for why
  the harbour defers to the city watch.
- **Early reachable:** Yes technically, but out-of-the-way.
- **Overlay-friendly:** Unclear if stationary (undocumented); likely yes given "barracks."
- **Score: 4/10.** Right archetype (military gatekeeper), wrong location for a
  travel-departure beat.

### 6. Regulus Cento, royal quetzal keeper — 4/10 (overlay, weak)
- **Where he stands:** Fixed, just outside/south of Varrock's east gate — i.e. **outside
  Varlamore entirely**. [Regulus Cento]
- **Why plausible:** Already the person who personally facilitates travel *to* Varlamore on
  the Prince's authority, so he's an established "authorizes your journey" figure.
- **Caveat:** Wrong side of the ocean — he's stationed at Varrock, not in Civitas illa Fortis,
  and he only manages quetzal transport (internal to Varlamore/Kourend), not charter ships.
  Doesn't fit "unlocks the ships out of Varlamore" without relocating him, which would
  contradict his documented spot.
- **Score: 4/10.** Good precedent/flavor reference, poor mechanical fit.

### 7. Prince Itzla Arkan in person — 5/10 (overlay, high drama but impractical)
- **Where he stands:** Moves with the questline; settles at the Sunrise Palace post-Final
  Dawn but isn't a simple fixed "always there" NPC the way Minimus is. [Prince Itzla Arkan]
  [Sunrise Palace]
- **Why plausible:** He already personally granted the player's *entry* to Varlamore at the
  end of Children of the Sun — having him personally grant *exit* once you've proven yourself
  in the Colosseum is a strong narrative bookend, and he's the actual heir to the throne, an
  unimpeachable authority.
- **Caveat:** Overlay needs a stationary NPC; the Prince isn't reliably planted in one spot
  early-game the way the design wants, and he'd need to be free of ongoing quest state
  (he's imprisoned mid-story in The Heart of Darkness). Using him means either picking a
  specific safe "idle" placement (e.g. Sunrise Palace throne room, post-Final-Dawn state)
  or spawning a stand-in specifically labeled as him — which risks feeling wrong once players
  know his questline. Best treated as a "quest-complete" bonus interaction rather than the
  primary mechanism.
- **Score: 5/10.** Best lore payoff, worst implementation fit for a same-NPC-every-time
  overlay.

### 8. A spawned Colosseum Champions' Herald/Sunfire Attendant at the Grand Museum's Colosseum exhibit — 5/10 (spawn new)
- **Exact spot:** Inside the Grand Museum, in a hypothetical Colosseum-history wing near
  Curator Herminius.
- **Role:** An attendant who catalogs champions' gear (ties into the "replica quiver" lore —
  someone has to know which replicas are which) and issues a museum-certified travel
  endorsement.
- **Why plausible:** Reinforces the "replicas vs. the one true quiver" mystery already in the
  item's lore. [Dizana's quiver]
- **Caveat:** Adds a second invented authority chain (museum -> harbour) on top of an already
  invented NPC; more moving parts than needed.
- **Score: 5/10.** Flavorful but redundant next to candidates #2 and #3.

**Top pick for the plugin:** Overlay **Minimus** (candidate #1) as the primary unlocker —
reuses an existing, already-interactive, always-stationary NPC with a directly relevant
existing role and existing "trade in your quiver" dialogue hook. If a harbour-side beat is
wanted too (e.g., "the ships still won't take you until the harbour signs off"), pair it with
the **spawned Harbour Registrar** (candidate #3) at the Cothon as a two-step unlock: Minimus
certifies you're a champion, the Registrar at the dock actually clears you to sail.

---

## 4. How the game gates travel elsewhere (design precedent)

- **Musa Point <-> Port Sarim, Customs officer.** A stationed NPC "inspects peoples'
  packages" and charges a fare (30gp, or 15gp with Karamja gloves) before allowing departure;
  a Ring of Charos(a) can charm free passage. During Pirate's Treasure she actively searches
  for and confiscates contraband (Karamjan rum), forcing players to smuggle past her.
  [Customs officer] — Precedent for a **fixed dockside NPC who personally inspects/approves
  before travel is permitted**, exactly the shape of candidate #3.
- **Border Guard, Lumbridge/Al Kharid gate.** Charges a 10gp toll to cross unless the player
  has completed Prince Ali Rescue, after which passage is free. [Border Guard] — Precedent
  for **quest completion permanently changing what a stationary gate-NPC will allow**, i.e.
  exactly the "show the quiver once, unlocked forever" shape the plugin wants.
- **Charter ships generally.** Run by Trader Stan's crew; individual destinations carry
  their own quest gates (e.g., Mos Le'Harmless needs Cabin Fever, Port Tyras needs Regicide,
  Prifddinas needs Song of the Elves). [Charter ship] — Precedent for **per-destination
  quest-gating of the charter network itself**, meaning a Varlamore-specific gate on
  outbound charter travel (rather than inbound) isn't a novel mechanic type for the game.
- **Kingdom of Miscellania/Etceteria.** Boat access ties back to completing The Fremennik
  Trials (same sailor from that quest operates the longboat); wiki text on explicit gating
  dialogue wasn't found on the Kingdom of Miscellania page itself, but the underlying pattern
  — a quest unlocks a boat operator's willingness to carry you — matches. [Kingdom of
  Miscellania]
- **Children of the Sun itself.** This is the closest existing precedent to the plugin's
  premise: Prince Itzla, an authority figure, "grants the player special permission to visit
  Varlamore" after the player proves trustworthy (foiling an assassination plot), and only
  then does Regulus Cento actually operationalize the travel. [Children of the Sun] — The
  plugin's design (prove yourself -> named authority approves -> travel unlocks) is a mirror
  image of this, which is strong internal-consistency support for the concept.

---

## 5. Lore constraints

- **Varlamore was never closed to outsiders — the opposite.** Before Children of the Sun,
  the kingdom was in self-imposed isolation, and the player's entry required Prince Itzla's
  explicit "special permission" after the player proved trustworthy during the assassination
  plot. [Children of the Sun] There is no wiki evidence that Varlamore restricts *outbound*
  travel for anyone, let alone for endgame players — charter ships, Quetzal transport, and
  fairy rings all operate freely once you're in. So a "the sea lanes out are closed" premise
  would contradict established lore if stated as a general policy.
- **Phrasing fix:** Frame the restriction as personal/conditional, not civic policy — e.g.
  "the charter crews won't yet vouch for you" or "no captain will risk taking an unproven
  outsider back across open water" rather than "Varlamore has sealed its harbour." This
  matches real precedent: the Musa Point Customs officer and the Al Kharid Border Guard don't
  represent kingdom-wide travel bans, they're individual gatekeepers with individual
  criteria — exactly the granularity the plugin needs (only inconveniences the player
  character, doesn't require any other lore to change).
- **Avoid overloading Minimus/any NPC with authority they're not documented to have.**
  Minimus explicitly answers to Ueman on Colosseum matters and has no stated authority over
  ships. [Minimus] Keep the unlock dialogue framed as personal vouching/reputation ("word
  will reach the harbour that you're no longer a green outsider") rather than a formal
  legal decree, so it doesn't imply Minimus outranks the Queen, Servius, or the Guard
  Captain.
- **Don't resurrect or reposition quest-locked NPCs.** Ennius Tullus is dead and Furia
  Tullus is in custody for treason by the end of The Final Dawn [The Final Dawn]; Prince
  Itzla and Servius are both quest-mobile and shouldn't be treated as always-idle fixtures
  without picking a specific, safe (post-questline) placement. [Prince Itzla Arkan] [Servius]
- **The Dizana myth is safely open-ended.** Because the wiki itself leaves Dizana's fate and
  the original quiver's location ambiguous ("some believe that the original quiver is mixed
  in with the replicas") [Dizana's quiver], any dialogue that treats the replica quiver as
  sufficient proof of skill — without claiming it IS the original — stays consistent with
  canon.

---

## Sources (OSRS Wiki pages fetched)
Fortis Colosseum; Dizana's quiver; Sol Heredit; Minimus; Varlamore; Civitas illa Fortis;
Prince Itzla Arkan; Servius; Regulus Cento; Ennius Tullus; Furia Tullus; Knight of Varlamore;
Curator Herminius; Guard Captain (redirect, Yanille — ruled out); Guard Captain (Civitas illa
Fortis); Fortis Cothon; Charter ship; Trader crewmember; Customs officer; Border Guard;
Shipwright; Kualti; Sunrise Palace; Children of the Sun; Twilight's Promise; Perilous Moons;
At First Light; Death on the Isle; The Heart of Darkness; Ethically Acquired Antiquities;
The Final Dawn; Kingdom of Miscellania.
