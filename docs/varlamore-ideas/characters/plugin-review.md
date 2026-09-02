# Plugin proposal review — 47 ideas, challenged

Reviewer pass over `docs/varlamore-ideas/content/plugin.json` (sections: progress 12, key-items 15,
qol-unlocks 12, other 8 = 47). Facts spot-checked live against oldschool.runescape.wiki in September 2026.

**34 wiki claims spot-checked. 6 came back wrong or unsupported:**

| # | Claim as written | What the wiki actually says |
|---|---|---|
| 1 | "only The Runic Emporium in Cam Torum stocks law runes; the Tal Teklan Rune Shop does not" | [The Runic Emporium](https://oldschool.runescape.wiki/w/The_Runic_Emporium) stock list has **no law rune**, and [Law rune](https://oldschool.runescape.wiki/w/Law_rune) lists **no Varlamore vendor at all** (Mage Arena, Wizards' Guild, Ali Morrisane, Tutab's, Amlodd's). The [Tal Teklan Rune Shop](https://oldschool.runescape.wiki/w/Tal_Teklan_Rune_Shop) lede *mentions* law runes but its stock table does not list them. Contested across three pages — must be confirmed in-client. Cosmic-runes-at-Tal-Teklan-only **is** correct. |
| 2 | "Snape grass has no confirmed Varlamore source" | [Snape grass](https://oldschool.runescape.wiki/w/Snape_grass) lists a spawn **in Civitas illa Fortis** (1 spawn, requires telekinetic grab). It is obtainable in region. |
| 3 | "Ring of dueling … UNOBTAINABLE" / "the region cannot produce" | Every input is in region: gold ore at [Stonecutter Outpost](https://oldschool.runescape.wiki/w/Stonecutter_Outpost) (Mining 65, 40 rocks), furnaces at Tal Teklan/Cam Torum/Auburnvale, ring mould at [Artima's](https://oldschool.runescape.wiki/w/Artima%27s_Crafting_Supplies), cut emerald at [Toci's Gem Store](https://oldschool.runescape.wiki/w/Toci%27s_Gem_Store), then Lvl-2 Enchant (Magic 27, 1 cosmic + 3 air) per [Ring of dueling](https://oldschool.runescape.wiki/w/Ring_of_dueling). The "wry joke" the proposal builds on is not true. |
| 4 | Pendant destinations: "Kastori, the Cam Torum area and the Darkfrost, Salvager Overlook near the Twilight Temple, and North Aldarin" | [Pendant of Ates](https://oldschool.runescape.wiki/w/Pendant_of_Ates) has **six**: The Darkfrost, Twilight Temple, Ralos' Rise, North Aldarin, North of Kastori, Nemus Retreat. Salvager Overlook is not one. Ralos' Rise and Nemus Retreat are missing from the list. Also: it holds 1,000 charges fed by frozen tears, which the proposals never mention. |
| 5 | At First Light "unverified whether it truly exits Varlamore" (quest-boundary-planner marks it amber) | [At First Light](https://oldschool.runescape.wiki/w/At_First_Light) confirms the quest stays inside Varlamore (Civitas illa Fortis, Avium Savannah, Locus Oasis). The **prerequisite** Eagles' Peak is outside; the quest itself is green. |
| 6 | Stealing Valuables "the wiki itself calls the GP and XP 'modest'"; valuables sell for 55 gp | [Stealing valuables](https://oldschool.runescape.wiki/w/Stealing_valuables) says "medium profit and experience rates", and Oriana pays **65 gp after Gladiator rank** (8,000 glory) — which is also the missing reward the colosseum-glory item leaves blank for the Gladiator tier. |

Verified correct (a sample): the 14 quetzal sites / 8 default / 6 buildable at 30,330 gp each and 181,980 gp total;
all six glory tiers and their thresholds; Dizana's quiver guaranteed from Sol Heredit, Sunfire fanatic from wave 4,
Tonalztics from wave 7; Lunar Chest 1/224, 13 uniques, ~320 opens, all three Moons required for the first open;
rumour tiers 46/57/72/91 and blueprints at 10/100/250 with the (Hunter+5)×50/55/60 formula; whistle = blueprint +
willow logs + knife, 500 gp replacement, recharged by Soar Leader Pitri at The Roost; Hunter Guild 46 with a bank
three tiles from a fire; Children of the Sun entirely in Varrock; Twilight's Promise's Regulus Cento step outside
Varrock's east gate; Perilous Moons 48/20/20/20/10 and 40k Slayer XP; Death on the Isle 34/32; The Final Dawn
66/52/52 and Arkan blade; Scrambled! 38/36/35; Mastering Mixology Herblore 60, all recipes at 81, ~45k passive /
~70k active — and the apprentice potion pack **does** contain prayer potions and (super)antipoison; blessed bone
shards Prayer 30, 5 XP / 6 XP with sunfire wine, calcified rocks Mining 41 at 1–3 shards and 74/75; bow string
spool 250 points (plus 1,250 more to max its capacity); saw 13 gp and Auburnvale the closest sawmill to a bank;
Sebamo's really does refuse to stock battlestaves; Artima's shears + glassblowing pipe; Kastori's five farming
tools; Lunami's tops out at adamant with no Woodcutting requirement; UIM death drops everything for one hour with
no grave, no Coffer, no Protect Item; Doom of Mokhaiotl 15 CA tasks.

---

## Verdict table

Legend — **Fact**: OK / WRONG / UNVERIFIED. **Feas**: EASY / MED / HARD / BLOCKED (needs server or a
non-existent varbit). **Fun**: KEEP / WEAK / DUP (duplicates an existing plugin) / CREEP (generic QoL).

| Section | id | Title | Fact | Feas | Fun | Note |
|---|---|---|---|---|---|---|
| progress | children-of-the-sun | Children of the Sun (pre-lock exception) | OK | EASY | KEEP | The honest carve-out; `Quest.getState` + panel banner. Ship it first. |
| progress | starter-toolkit | First toolkit assembled | OK | EASY | KEEP | Trivial `ItemContainerChanged` scan; persist per-item first-seen in ConfigManager. |
| progress | native-quest-sweep | Three no-leave starter quests | OK | EASY | KEEP | Quest states only. At First Light's amber flag elsewhere should now be green. |
| progress | twilights-promise | Twilight's Promise / first teleport | OK | EASY | KEEP | Correctly notes the Varrock east-gate leg. |
| progress | hunter-46-guild | Hunter 46 — Guild access | OK | EASY | KEEP | `StatChanged` + login check. The single most load-bearing level in the region. |
| progress | basic-quetzal-whistle | Ten rumours — basic whistle | OK | EASY | DUP | Facts all check out, but the Hunter Rumours Plugin Hub plugin already counts rumours; keep only the whistle-item tick. |
| progress | quetzal-network-complete | All fourteen landing sites | OK | MED | KEEP | Costs exact. Per-site varbits unconfirmed — the manual-tickbox fallback is the right call. |
| progress | death-on-the-isle-pendant | Death on the Isle + pendant | WRONG | MED | KEEP | Destination list wrong (see #4); statue varbits unconfirmed; ignores the 1,000-charge frozen-tear economy. |
| progress | perilous-moons | Perilous Moons — first boss | OK | EASY | KEEP | Reqs and 40k Slayer XP correct; omits the 5k each RC/Hunter/Fishing. |
| progress | colosseum-glory | Colosseum waves and glory tiers | OK | EASY | KEEP | Tiers exact. Fill in Gladiator 8,000 = better NPC rates (Oriana 55→65 gp). |
| progress | dizanas-quiver | Dizana's quiver + Sunfire armour | OK | MED | KEEP | Quiver guaranteed, wave 4 / wave 7 correct. "Mirror the collection log" is the catch — the log is only readable while its interface is open. |
| progress | final-dawn-doom | The Final Dawn + Doom | OK | EASY | KEEP | Reqs and Arkan blade correct; omits 25k RC / 25k Fletching / the 55k combat lamp. |
| key-items | knife | Knife | OK | EASY | KEEP | Seven general stores confirmed. Soft reminder is proportionate. |
| key-items | tiered-axe | Iron to adamant axe | OK | EASY | KEEP | Lunami's confirmed sole tiered seller, adamant ceiling, no WC requirement. |
| key-items | full-range-pickaxe | Iron to rune pickaxe | UNVERIFIED | EASY | KEEP | Tizoro's full bronze–rune range not re-confirmed this pass; the mining/Prayer link is right. |
| key-items | saw | Saw | OK | EASY | KEEP | 13 gp, Auburnvale, closest sawmill to a bank — all correct. |
| key-items | shears | Shears | OK | EASY | KEEP | Artima's-only confirmed. |
| key-items | glassblowing-pipe | Glassblowing pipe | OK | EASY | KEEP | Artima's-only confirmed; vials genuinely sold in four other shops. |
| key-items | farming-toolkit | Farming toolkit | OK | EASY | KEEP | All five Kastori-only confirmed. Missing the one thing that makes them survivable: tool leprechauns. |
| key-items | bow-string | Bow string | OK | MED | KEEP | 250 points confirmed (+1,250 to max the spool). The "research points" progress bar needs a varbit nobody has identified. |
| key-items | law-runes | Law runes | WRONG | EASY | KEEP | See #1 — the Cam Torum attribution is unsupported and law runes may have no in-region source at all. |
| key-items | bows-and-arrows | Bows and arrows | OK | EASY | KEEP | Arcuani's sole archery vendor; maple ceiling matters against Ranged 90+ for Colosseum. |
| key-items | elemental-staves | Elemental staves | OK | EASY | KEEP | Sebamo's stock and the no-battlestaff lore both verbatim correct. |
| key-items | prayer-potions-antipoison | Prayer potions and antipoison | WRONG | EASY | KEEP | Apprentice pack contains both; Amoxliatl also drops prayer potions; snape grass **is** in region (#2), so self-brewing is not off the table. |
| key-items | pendant-of-ates | Pendant of Ates | WRONG | EASY | KEEP | Rates 1/100 and 1/25 correct; destination list wrong (#4); no mention of the 1,000-charge frozen-tear system. |
| key-items | quetzal-whistle-item | Basic quetzal whistle | OK | EASY | KEEP | All numbers correct. Add the 5-charge cap and the meat-per-charge table. |
| key-items | unobtainable-watchlist | The unobtainable watchlist | WRONG | EASY | KEEP | Two of seven are wrong: ring of dueling (#3) and snape grass (#2). Holy symbol, glory, essence, battlestaves hold up. |
| qol-unlocks | landing-site-atlas | Landing-site atlas | OK | MED | KEEP | Best gate in the section — atlas literally cannot exist before the whistle. |
| qol-unlocks | fortis-teleport-legal | Fortis Teleport allow-listing | OK | EASY | KEEP | One boolean in `SpellTeleportBlocker` + a widget highlight. Pure plugin identity. |
| qol-unlocks | pendant-destination-pins | Pendant destination pins | WRONG | MED | KEEP | Same destination error; statue varbits unconfirmed. |
| qol-unlocks | moons-loot-infobox | Moons loot infobox | OK | MED | DUP | Numbers correct, but core Loot Tracker + the collection log already do kills, opens and missing uniques. |
| qol-unlocks | mixology-shopping-list | Mixology shopping list | OK | EASY | DUP | Herblore 60 / 81 / XP rates correct, but the shop prices are Mox-Aga-Lye, not gp, and a Mixology plugin already exists on the Hub. |
| qol-unlocks | valuables-overlay | Stealing Valuables overlay | WRONG | MED | KEEP | "Modest" misquote and the Gladiator 65 gp bump missing; the overlay itself is legitimate and unserved. |
| qol-unlocks | master-rumour-tracker | Master rumour tracker | OK | MED | DUP | At First Light 46/30/27 + Eagles' Peak correct, Master 91 correct — and Hunter Rumours (Plugin Hub) already routes assignments. Drop. |
| qol-unlocks | libation-bowl-calculator | Libation bowl calculator | OK | EASY | WEAK | Every number correct, but it is arithmetic an XP tracker already gives. Merge into the Prayer row of the skill atlas. |
| qol-unlocks | colosseum-respawn-planner | Death-pile planner | OK | MED | KEEP | The most UIM-native idea in the file. Keep the straight-line version; skip pathfinding. |
| qol-unlocks | ring-of-dueling-swap | Ring of dueling Colosseum swap | WRONG | EASY | KEEP | Hero 12,000 and the destination are right; the "region cannot produce the ring" premise is false (#3). Rewrite the gag as a crafting chain. |
| qol-unlocks | charter-dock-unlock | Three in-region charter routes | UNVERIFIED | EASY | KEEP | Docks and the quiver gate are right; Antonia's and Achilka's boats unverified. Already half-built in `NpcTransportBlocker`. |
| qol-unlocks | completionist-panel | Completionist panel | OK | EASY | KEEP | Cosmetic over data already tracked. Cheap, and it gives the ladder a top. |
| other | death-pile-tracker | Death pile tracker | OK | EASY | DUP | Mechanics quoted verbatim-correct, but core RuneLite Death Indicator already pins the tile. Differentiate with the carried-items snapshot and the 60-minute clock. |
| other | boundary-warning | Boundary-crossing warning | OK | EASY | KEEP | Reuses the existing `HashSet<Integer>`. The lock's missing "guide" half. |
| other | region-map-overlay | Region map overlay | OK | EASY | DUP | This is Region Locker with a fixed list. Only worth shipping because the allowlist ships with it. |
| other | skill-training-atlas | "What can I train here?" atlas | UNVERIFIED | EASY | CREEP | Fletching/Prayer/Mining/Thieving rows check out; Runecraft and Construction "no answer" flags are inferences, and the table omits Agility (Colossal Wyrm), Fishing, Crafting and Slayer entirely. |
| other | rumour-tracker | Hunters' Rumours tracker | OK | MED | DUP | Tiers, blueprints and the XP formula all verified correct — and all already provided by Hunter Rumours on the Hub. |
| other | shop-router | Single-source shop router | UNVERIFIED | EASY | KEEP | Stock tables carry the law-rune error and several unlisted stalls; a reviewer will ask who maintains it. |
| other | quest-boundary-planner | Quest boundary planner | WRONG | EASY | KEEP | At First Light is now confirmed in-region (#5); the amber list needs updating. Otherwise the single most useful feature here. |
| other | standin-dialogue-codex | Stand-in dialogue codex | OK | EASY | KEEP | Already scaffolded in `standin/PersonaRoster` and `dialogue/`; pure differentiation, zero API risk. |

---

## Top 10 to build first

1. **children-of-the-sun** — the lock has to have a defined start, and saying the exception out loud is what
   separates an honest challenge plugin from one that quietly cheats. One quest-state read.
2. **boundary-warning** — the existing `BoundaryChecker` already runs per tick and holds the chunk set; turning
   "am I inside" into "how close is the edge" is a few lines and it is the difference between a bouncer and a guide.
3. **x-fairy-ring-lockdown** (new, below) — this is not a feature, it is a hole. Three fairy rings sit inside
   Varlamore and nothing in `SpellTeleportBlocker`, `ItemTeleportBlocker` or `NpcTransportBlocker` touches them.
4. **fortis-teleport-legal** — the plugin's thesis stated as a reward. A boolean and a chat line.
5. **quest-boundary-planner** — prevents the worst failure mode of the whole challenge (discovering at step four
   that you are in Port Sarim). Pure static table plus quest states; the research is already done.
6. **death-pile-tracker** — one hour, no grave, no Coffer. Core Death Indicator pins the tile but does not tell
   you what you were carrying or how long is left; that delta is the whole value.
7. **colosseum-respawn-planner** — the only proposal that treats respawn distance as a resource. Genuinely
   UIM-specific, genuinely gated on a real reward, and nothing else on the Hub does it.
8. **standin-dialogue-codex** — the stand-in system is already built; branching the lines on milestone state costs
   almost nothing and is the only thing in the file no other plugin could plausibly copy.
9. **landing-site-atlas** — best gate-to-payload fit in the file, and it makes the 181,980 gp infrastructure
   project legible. Ship with manual tickboxes, swap to varbits later.
10. **starter-toolkit + key-item drop guards** — the checklist is trivial, and consuming a Drop/Alch menu click on
    a single-source tool is the highest-value line of code in the plugin for a bankless account.

## Drop or merge

- **rumour-tracker** and **master-rumour-tracker** — merge into one, then cut it. The Hunter Rumours plugin on the
  Hub already tracks tier, assignment, location and lifetime count. Keep only the 10/100/250 blueprint markers
  inside `basic-quetzal-whistle`, since those are the Varlamore-lock-relevant part.
- **moons-loot-infobox** — merge into `perilous-moons`. Loot Tracker plus the collection log already cover kills,
  opens and missing uniques, and reading the log requires the player to open it anyway.
- **mixology-shopping-list** — merge the ranking (reagent pouch and potion storage first, because they are the only
  bank-shaped objects a UIM will ever own) into the key-items checklist, and drop the shop mirror. Fix the currency:
  costs are Mox/Aga/Lye, not coins.
- **libation-bowl-calculator** — merge into the Prayer row of the skill atlas. Shards × 5 or 6 is not a feature.
- **region-map-overlay** — keep, but describe it as "ships the Varlamore allowlist so you don't have to click 400
  chunks in Region Locker", not as a new capability. A Hub reviewer will otherwise ask why this is not a
  Region Locker preset.
- **skill-training-atlas** — narrow it. As written it is a wiki page pasted into the client and is the one item in
  the file that reads as generic QoL. Cut it to the four skills where the region genuinely constrains you
  (Runecraft, Construction, Prayer, Agility) and drop the rest.
- **shop-router** — keep the single-source badges, drop the full searchable stock index. The index carries the
  law-rune error, several unpriced stalls, and an unbounded maintenance commitment a reviewer will notice.
- **unobtainable-watchlist** — remove ring of dueling and snape grass from the greyed-out list; both are makeable
  or gettable in region. Leaving them in would actively mislead a player into not trying.

---

## Missing ideas

Five the author did not think of, each closer to the plugin's actual thesis than the QoL items above.

### 1. Fairy ring lockdown (`x-fairy-ring-lockdown`, section: other)
Varlamore contains three fairy rings — **AIS** (Auburn Valley), **AJP** (Avium Savannah) and **CKQ** (Aldarin) —
and Children of the Sun is precisely the quest that unlocks them. The plugin blocks spells, jewellery, tablets, the
minigame tab and the charter crews, and then leaves the player standing next to a device that reaches all of
Gielinor for four rune-free clicks. This is the largest enforcement gap in the current design.
*Source:* https://oldschool.runescape.wiki/w/Fairy_ring
*Detection:* intercept `MenuOptionClicked` on the fairy ring object — consume "Ring-last-destination" whenever the
stored destination varbit is not one of the three in-region codes, and consume the confirm click on the fairy ring
code interface (the dial widget exposes the three letter values before travel resolves). Both are ordinary
client-side menu consumption; the server never sees the click, exactly as with the existing teleport blockers.

### 2. Termite exchange and the Colossal Wyrm teleport scroll (`x-termite-exchange`, section: qol-unlocks)
The Colossal Wyrm Agility Course in the Avium Savannah is the region's only Agility content (50 basic / 62
advanced), pays in termites, and sells a **Colossal wyrm teleport scroll for 40 termites** — an in-region teleport
the item blocker must *allow*, not block. It also drops 22–38 blessed bone shards on an 80% scoop, tying Agility
directly into the Prayer loop the libation-bowl item already describes. The atlas currently has no Agility row at all.
*Source:* https://oldschool.runescape.wiki/w/Colossal_Wyrm_Agility_Course
*Detection:* Agility level via `StatChanged`; termite count from the inventory item ID; whitelist the teleport
scroll's item ID in `ItemTeleportBlocker` gated on Agility 50, with the destination-name check the ring of dueling
already uses.

### 3. Tool leprechaun ledger (`x-tool-leprechaun`, section: key-items)
The farming-toolkit item correctly flags five Kastori-only tools as single-source and then never mentions the one
mechanic that makes them survivable: tool leprechauns store rake, dibber, secateurs, watering can, trowel and spade
for free, account-wide, and a UIM's stored tools do **not** drop on death. For an account with no bank, the
leprechauns at the Varlamore patches are the only free storage in the region.
*Source:* https://oldschool.runescape.wiki/w/Tool_leprechaun
*Detection:* the farming tool-storage state is already varp-backed and exposed to the client (RuneLite's own
Farming/tool plugins read it); pair that with static leprechaun coordinates at the Ortus Farm, Kastori and Locus
Oasis patches, and raise an overlay warning whenever a single-source tool is being carried into combat while a
leprechaun slot for it is empty.

### 4. The in-region prayer-potion chain (`x-prayer-potion-chain`, section: key-items)
This directly corrects fact error #2. Snape grass has a Civitas illa Fortis spawn reachable with Telekinetic Grab,
vials are sold in four shops, the region has herb patches (Ortus Farm, plus the disease-free patch the Colosseum
Champion tier awards at 16,000 glory), and Amoxliatl drops prayer potions outright. Prayer restoration is therefore
*not* Mixology-or-nothing — it is a five-step chain the plugin can route, which is a far more interesting thing to
show a locked account than "minigame-gated".
*Source:* https://oldschool.runescape.wiki/w/Snape_grass
*Detection:* tile marker on the snape grass spawn; a readiness row that checks law + air rune counts for the
telegrab, Magic 33, Herblore 38, and carried vials of water; item-ID ticks for ranarr weed and each potion stage.

### 5. Varlamore Combat Achievements ladder (`x-varlamore-combat-achievements`, section: progress)
There is no Varlamore Achievement Diary — a direct fetch of the diary list confirms twelve regions and no
Varlamore. What the region does have is 60 Combat Achievement tasks spread across its five native bosses:
Fortis Colosseum 13, Moons of Peril 12, The Hueycoatl 11, Amoxliatl 9, Doom of Mokhaiotl 15. That is the region's
only official completion ladder, and the proposed progress track ignores four of those five bosses entirely
(Hueycoatl, Amoxliatl and the Gemstone Crab appear nowhere in the file).
*Source:* https://oldschool.runescape.wiki/w/Combat_Achievements
*Detection:* scrape the Combat Achievements interface when it is opened and cache per-task completion in
ConfigManager, topped up live by the per-task completion chat message; filter to the five Varlamore boss task
lists. No varbit hunting required, and the cache survives logout.
