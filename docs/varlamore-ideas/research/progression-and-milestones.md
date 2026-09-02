# Varlamore-Locked UIM: Progression & Milestone Research

Source of truth: OSRS Wiki (oldschool.runescape.wiki), fetched live. Community sources via web search where the wiki has no page. Facts I could not confirm from a fetched page are marked **UNVERIFIED**.

---

## 1. Quests doable entirely inside Varlamore

**Critical finding up front:** *Children of the Sun* — the quest that unlocks the Quetzal Transport System and fairy-ring access to Varlamore in the first place — starts in **Varrock Square** (talk to Noah or Alina) and its entire questline (tailing a guard, identifying bandits, interrogating them at the palace) takes place **in Varrock**, not Varlamore. [Children of the Sun](https://oldschool.runescape.wiki/w/Children_of_the_Sun)

This means a strictly Varlamore-locked account **cannot self-unlock Varlamore** — the unlock quest is the one thing that must happen outside the lock. Every other quest below requires Children of the Sun as a prerequisite, so it is the mandatory one-time exception a ruleset/plugin has to explicitly carve out (comparable to the "Druidic Ritual" exception area-locked accounts already make for Herblore, per the wiki's [Area-locked](https://oldschool.runescape.wiki/w/Area-locked) account page). Practical options for the plugin: (a) treat Children of the Sun as a pre-lock setup step done before the boundary check activates, or (b) special-case allow travel to Varrock only for this quest's duration.

| Quest | Start point | Requirements | Leaves Varlamore? | Rewards |
|---|---|---|---|---|
| [Children of the Sun](https://oldschool.runescape.wiki/w/Children_of_the_Sun) | Noah/Alina, Varrock Square | None | **Yes — entirely in Varrock** | 1 QP; Quetzal Transport System + fairy ring access to Varlamore |
| [Twilight's Promise](https://oldschool.runescape.wiki/w/Twilight%27s_Promise) | Ennius/Furia Tullus, outside Sunrise Palace | Children of the Sun; combat gear for two styles or poisoned weapon; ~40 combat recommended | **Yes** — quest requires returning to Varrock's east gate/palace NPCs | 1 QP; Civitas illa Fortis Teleport spell; Quetzal Transport System; 3,000 Thieving XP |
| [Perilous Moons](https://oldschool.runescape.wiki/w/Perilous_Moons) | Zuma/Attala, Cam Torum entrance | Slayer 48, Hunter 20, Fishing 20, Runecraft 20, Construction 10 (all non-boostable); Twilight's Promise | **No** | 2 QP; 40,000 Slayer XP; 5,000 each Runecraft/Hunter/Fishing XP; access to Neypotzli, Moons of Peril bosses, lesser nagua Slayer tasks, Lunar Chest |
| [At First Light](https://oldschool.runescape.wiki/w/At_First_Light) | Guildmaster Apatura, Hunter Guild | Hunter 46, Herblore 30, Construction 27 (non-boostable); Children of the Sun + **Eagles' Peak** | **Yes** — delivers a fur sample to Atza in Outer Fortis (still Varlamore, wiki fetch flagged this as "leaving" the Guild building, not the region — treat as **UNVERIFIED** whether it truly exits Varlamore) | 1 QP; 4,500 Hunter, 800 Construction, 500 Herblore XP; Master Tier Hunters' Rumours |
| [Death on the Isle](https://oldschool.runescape.wiki/w/Death_on_the_Isle) | Patzi, Villa Lucens entrance (Aldarin) | Thieving 34, Agility 32 (non-boostable); Children of the Sun; ~40 combat recommended | **No** | 2 QP; 10,000 Thieving, 7,500 Agility, 5,000 Crafting XP; costume needle, butler's tray, 5 animal masks; North Aldarin Pendant of Ates teleport |
| [The Heart of Darkness](https://oldschool.runescape.wiki/w/The_Heart_of_Darkness) | Prince Itzla Arkan, the Teomat | Mining 55, Thieving 48, Slayer 48, Agility 46 (non-boostable); Twilight's Promise + Children of the Sun | **No** | 2 QP; 8,000 XP each Mining/Thieving/Slayer/Agility; access to Tapoyauik dungeon |
| [Ethically Acquired Antiquities](https://oldschool.runescape.wiki/w/Ethically_Acquired_Antiquities) | Grand Museum empty display, Civitas illa Fortis | Thieving 25 (non-boostable); Children of the Sun + **Shield of Arrav** | **Yes** — travels to Port Sarim and Varrock Museum | 1 QP; 6,000 Thieving XP; 5,000 coins |
| [The Final Dawn](https://oldschool.runescape.wiki/w/The_Final_Dawn) | Servius, Sunrise Palace | Thieving 66, Runecraft 52, Fletching 52 (non-boostable); The Heart of Darkness + Perilous Moons + Twilight's Promise + Children of the Sun | **No** | 3 QP; Arkan blade; 55,000 Thieving, 25,000 Runecraft, 25,000 Fletching XP; 55,000 XP combat lamp; access to Crypt of Tonali and **Doom of Mokhaiotl** |
| [Scrambled!](https://oldschool.runescape.wiki/w/Scrambled!) | Alan, Temple of Tal Teok | Construction 38, Cooking 36, Smithing 35 (non-boostable); Children of the Sun | **No** | 1 QP; 5,000 XP each Construction/Cooking/Smithing; egg pet; Alan's bones |
| [Meat and Greet](https://oldschool.runescape.wiki/w/Meat_and_Greet) | Emelio, Outer Fortis | Children of the Sun; 60 combat / 43+ Prayer recommended | **No** | 1 QP; 8,000 Cooking XP; Emelio's Kebab Shop access |
| [The Ribbiting Tale of a Lily Pad Labour Dispute](https://oldschool.runescape.wiki/w/The_Ribbiting_Tale_of_a_Lily_Pad_Labour_Dispute) | Marcellus, Locus Oasis | Woodcutting 15 (non-boostable); Children of the Sun | **No** | 1 QP; 2,000 Woodcutting XP; new hardwood Farming patch |
| [Shadows of Custodia](https://oldschool.runescape.wiki/w/Shadows_of_Custodia) | Notice board, Auburn Pub (Auburnvale) | Slayer 54, Fishing 45, Construction 41, Hunter 36 (non-boostable); Children of the Sun | **Yes** — investigates caves/trails outside Auburnvale (wiki summary implies leaving the immediate town, **UNVERIFIED** whether it exits Varlamore proper — treat as at-risk) | 2 QP; 10,000 Slayer, 4,000 Hunter, 3,000 Fishing, 3,000 Construction XP; Stalker Den access |

Note on "Curse of the Moon": there is **no quest by this name**. It's a boss mechanic inside Perilous Moons/Moons of Peril (a stacking curse that causes glancing/reduced-damage hits), not a separate quest. [Moons of Peril/Strategies](https://oldschool.runescape.wiki/w/Moons_of_Peril/Strategies) — confirmed via web search, no wiki quest page exists.

**Practical quest order for a locked account** (after the mandatory Children of the Sun exception): Scrambled! → Meat and Greet → Ribbiting Tale → Death on the Isle → Twilight's Promise → Perilous Moons → The Heart of Darkness → The Final Dawn, with At First Light and Ethically Acquired Antiquities requiring outside prerequisites (Eagles' Peak, Shield of Arrav) done pre-lock, and Shadows of Custodia flagged for manual boundary review.

---

## 2. Achievement diaries, combat achievements, collection log

**Achievement Diary:** Confirmed **no Varlamore Achievement Diary exists**. The wiki's [Achievement Diary](https://oldschool.runescape.wiki/w/Achievement_Diary) page lists exactly 12 diary regions (Ardougne, Desert, Falador, Fremennik Province, Kandarin, Karamja, Kourend & Kebos, Lumbridge & Draynor, Morytania, Varrock, Western Provinces, Wilderness) — Varlamore is not among them, and a direct fetch of `w/Varlamore_Diary` returned HTTP 404. Community sources (RS forums/guide sites) confirm this is a long-standing player request that Jagex has not shipped as of 2026; Mod Ash has reportedly said any future Varlamore diary would likely borrow the Combat Achievements tier-unlock model rather than the traditional diary structure — **UNVERIFIED**, sourced from secondary guide sites, not the wiki itself.

**Combat Achievements tied to Varlamore content** ([Combat Achievements](https://oldschool.runescape.wiki/w/Combat_Achievements)):

| Encounter | In Varlamore? | Task count | Notes |
|---|---|---|---|
| Fortis Colosseum (Sol Heredit) | Yes | 13 tasks | Civitas illa Fortis |
| Moons of Peril | Yes | 12 tasks | Neypotzli, beneath Cam Torum |
| The Hueycoatl | Yes | 11 tasks | Added Oct 2024 |
| Amoxliatl | Yes | 9 tasks | Added alongside Hueycoatl |
| Araxxor | **No — Morytania**, not Varlamore | 12 tasks | Confirmed via wiki: lair is in the Morytania Spider Cave near Darkmeyer. The user's premise that Araxxor is Varlamore content is **incorrect** — exclude it from any Varlamore-specific tracker. |
| Doom of Mokhaiotl | Yes | 15 tasks | Ruins of Mokhaiotl beneath Tlati Rainforest, accessed via The Final Dawn; added Sept 2025 |

Combat Achievement tiers award cumulative points (Easy/Medium/Hard/Elite/Master/Grandmaster, 1–6 pts/task) with reported cumulative unlock thresholds of 41 / 169 / 436 / 1100 / 1965 / 2697 points for the Achievement Diary Cape perks system — exact per-tier task/point breakdown for each individual boss was not enumerated by the wiki fetch; treat granular numbers as **UNVERIFIED** pending a per-boss task-list fetch.

**Collection log:** Fortis Colosseum, Moons of Peril, The Hueycoatl, Amoxliatl, Doom of Mokhaiotl, and Yama all have dedicated subsections under the **Bosses** tab of the [Collection log](https://oldschool.runescape.wiki/w/Collection_log). (Araxxor also has a Bosses entry but, per above, is not Varlamore content.) No Varlamore-specific Minigames or non-boss collection-log tab was identified in this pass — Mastering Mixology and Vale Totems reward *shop-purchasable* items rather than log drops, so they likely don't have log entries — **UNVERIFIED**, not directly checked.

---

## 3. Skill caps in Varlamore

Varlamore has genuine, wiki-documented training methods for many skills, but the *depth* of the wiki's per-skill "how far can you actually go" data was inconsistent — several location pages describe the activity but omit XP rates or hard level ceilings. Facts below are cited where the wiki gave numbers; gaps are marked.

| Skill | Best Varlamore method | Cap / notes |
|---|---|---|
| Hunter | [Hunter Guild](https://oldschool.runescape.wiki/w/Hunter_Guild), Avium Savannah (req. Hunter 46) | Hunters' Rumours reward system covers creatures up to high-level parts (tufts, wings, claws — kebbit, chinchompa, salamander, butterfly, moth tiers). No page-stated hard cap or XP rate — **UNVERIFIED** ceiling, but guild content scales past 46 toward the 90s via rumour tiers. |
| Herblore | [Mastering Mixology](https://oldschool.runescape.wiki/w/Mastering_Mixology), Alchemical Society, Aldarin (req. Herblore 60 + Children of the Sun) | ~45,000 XP/hr passive, ~70,000 XP/hr active at level 81; full recipe unlock at 81. No effective cap — scales to 99. |
| Mining | Mistrock outpost, Aldarin south coast | 5 gold, 5 silver, 6 coal, 8 iron rocks near a bank. Ralos' Rise is a religious/Prayer site, **not** a mining location (corrects the brief's premise) — wiki fetch found no ore or mining content there. No XP-rate data captured — **UNVERIFIED** rate/cap. |
| Cooking | Aldarin settlements / Moonrise Brewery & Winery | Wiki page had no explicit cooking XP/level data for Aldarin; Moonrise Brewery focuses on grape-harvesting for the winery, not a cooking training site — **UNVERIFIED**, premise of "Aldarin wine cooking" not confirmed by the fetched page. |
| Thieving | Stealing Valuables, Civitas illa Fortis (req. Thieving 50) | "Modest," "low intensity" GP/XP; valuables sell to Oriana for 55gp each; incidental clue scrolls, jewelry, blessed bone statuettes. No numeric XP rate given. |
| Fletching | [Vale Totems](https://oldschool.runescape.wiki/w/Vale_Totems), Auburn Valley (req. Fletching 20, scales to 90 for Redwood) | Oak totem ≈254.8 XP/build, Redwood ≈3,787.2 XP/build (plus Construction XP). Effectively trains to 90+. |
| Woodcutting | Auburnvale Sawmill | Described as "one of the closest sawmills to a bank in the game" — logistics advantage, no unique high-level content confirmed. |
| Construction | Vale Totems (Construction XP side-award); Scrambled! quest reward | No dedicated POH-equivalent construction trainer identified in Varlamore beyond quest/minigame XP — **UNVERIFIED** as a real training cap; likely low-cap without a player-owned house in the region. |
| Farming | Kastori village; Locus Oasis hardwood patch (unlocked via Ribbiting Tale quest) | Wiki confirms agricultural specialization at Kastori and a new hardwood patch from the quest, but no allotment/tree-patch level breakdown captured — **UNVERIFIED** ceiling. |
| Runecraft | Perilous Moons quest reward XP (5,000) only identified method; no standalone altar/training site confirmed in this pass | **UNVERIFIED** — needs a dedicated Runecraft-in-Varlamore check (e.g., is there a Varlamore runecraft altar?). |
| Slayer | Perilous Moons unlocks lesser nagua Slayer assignments; Shadows of Custodia unlocks Stalker Den | Task-based Slayer scales toward endgame once nagua/stalker tasks are assigned, gated behind those two quests. |
| Combat (Att/Str/Def/Rng/Mage/HP/Prayer) | [Fortis Colosseum](https://oldschool.runescape.wiki/w/Fortis_Colosseum), Civitas illa Fortis | Wave-based, 12 waves culminating in Sol Heredit. Wiki's recommended stats for full clear: Attack/Strength 95+, Defence 90+, Hitpoints 95+, Ranged 90+, Magic 94+, Prayer 77+ — implying near-endgame combat is achievable and rewarded (Sunfire fanatic armour, Dizana's quiver, Tonalztics of Ralos) entirely with Varlamore content. |
| Overall | — | No skill is wiki-confirmed as *structurally capped* below 99 by Varlamore-only content; several (Construction, Runecraft, Cooking, Mining ore-tier) lack a documented high-level Varlamore method and likely bottleneck a strict lock — flagged for deeper follow-up research rather than asserted as fact. |

---

## 4. Existing "locked account" community rulesets and tools

- **Area-locked accounts** (wiki term; "region-locked" redirects here): [Area-locked](https://oldschool.runescape.wiki/w/Area-locked) — a snowflake-account category restricting play to a specific area (e.g., a city or Zeah). The wiki explicitly notes rulesets carve out **small exceptions** to keep the account playable or avoid hard-blocking content, citing Druidic Ritual as the precedent exception to unlock Herblore on an otherwise-locked account. This is the direct precedent for the Children of the Sun exception this plugin will need.
- **Chunk-locked / "Chunkman" modes** (community-originated, not an official wiki ruleset page beyond area-locked): players lock to a single 64×64 map chunk and expand via task completion. Named variants include Pookaguy's OneChunkMan, Pelipper's ChunkManOnly, and Caveman's MonkManMode, with difficulty tiers "vanilla / Xtreme / Supreme" (Supreme requires skill capes, bank slots, and skilling pets earned inside the chunk).
- **RuneLite "Region Locker" plugin** (github.com/slaytostay/region-locker, Plugin Hub listing): "Plugin for chunk locked accounts." Mechanics: hold a key and click chunks on the world map to mark them unlocked; maintains a comma-separated list of unlocked region IDs (manually editable or map-driven); presumably renders locked chunks as inaccessible/darkened. This is the closest existing analogue to `BoundaryChecker`'s chunk-ID allowlist approach already used in this codebase.
- **Chunk Picker V2** (source-chunk.github.io/chunk-picker-v2): a companion planning tool, not a RuneLite plugin. Unlock trigger is **manual** (player clicks a chunk to mark it unlocked) but the tool auto-generates the *task list* that justifies each unlock, computed from resources available in currently-unlocked chunks, quest/diary progress, and skill levels. Presets ("Standard," "Xtreme," "Supreme") configure which skills count as "primary," whether collection-log completion or BiS gear is mandatory, etc. Progress is shown via an Active Tasks panel, a Backlog, a Chunk Info sidebar, map screenshots, and a timestamped "chunk-roll history." Exports an unlocked-chunk list to clipboard for pasting into the Region Locker RuneLite plugin.
- **Snowflake Ironman** accounts generally: self-imposed rule sets beyond the base Ironman restrictions (no trading, no GE except bonds, no Accept Aid, no PvP XP, no others'-drop pickup — standard Ironman rules per the [Ironman guide](https://oldschool.runescape.wiki/w/Ironman_guide)), documented informally via the wiki's [Account builds](https://oldschool.runescape.wiki/w/Account_builds) page and the r/UniqueIronmen community rather than a single canonical ruleset page. One cited example restricts a player to only the *first* unique obtained from each collection-log section — illustrating the general pattern of "self-imposed restriction + a visible tracked progress mechanic" that this plugin's milestone system should follow.

**Unlock-mechanic summary for this plugin's design:** the community pattern is (1) a hard boundary enforced by a client-side plugin (Region Locker's chunk allowlist ≈ this project's `BoundaryChecker`), (2) a *manual* unlock trigger (player self-certifies a task is done and clicks to unlock), and (3) an external or in-panel task list/progress tracker that tells the player what to do next and records history. Nothing in the community tooling does automatic, verified unlock — it's honor-system, same as Ironman mode itself.

---

## 5. Progress and milestone ideas (15, roughly ordered)

| # | Milestone | Wiki fact that makes it meaningful | Rough order |
|---|---|---|---|
| 1 | Complete Children of the Sun (the pre-lock exception quest) | Unlocks Quetzal Transport System + fairy ring access to Varlamore — the literal entry point to the challenge. [Children of the Sun](https://oldschool.runescape.wiki/w/Children_of_the_Sun) | 0 (pre-lock) |
| 2 | Obtain first Quetzal whistle / use the Quetzal Transport System | The Quetzal network is Varlamore's internal fast-travel — first non-walking mobility inside the lock. [Children of the Sun](https://oldschool.runescape.wiki/w/Children_of_the_Sun) | 1 |
| 3 | Complete Twilight's Promise | Unlocks the Civitas illa Fortis Teleport spell — first spellbook teleport confined to Varlamore. [Twilight's Promise](https://oldschool.runescape.wiki/w/Twilight%27s_Promise) | 2 |
| 4 | Complete Scrambled! / Meat and Greet / Ribbiting Tale (the "no-leave, low-req" trio) | All three are confirmed fully inside Varlamore with minimal skill gates — natural early questline sweep. | 2 |
| 5 | Reach Hunter 46 and enter the Hunter Guild | Hard level gate to the guild building itself. [Hunter Guild](https://oldschool.runescape.wiki/w/Hunter_Guild) | 3 |
| 6 | Complete At First Light | Unlocks Master Tier Hunters' Rumours — endgame Hunter content gated behind this quest. [At First Light](https://oldschool.runescape.wiki/w/At_First_Light) | 3 |
| 7 | Complete Death on the Isle | Unlocks the North Aldarin Pendant of Ates teleport destination — expands the accessible map. [Death on the Isle](https://oldschool.runescape.wiki/w/Death_on_the_Isle) | 3 |
| 8 | Complete Perilous Moons | Grants access to Neypotzli and the Moons of Peril bosses plus lesser nagua Slayer tasks — first proper boss content. [Perilous Moons](https://oldschool.runescape.wiki/w/Perilous_Moons) | 4 |
| 9 | Reach 12,000 Glory / "Hero" tier at Fortis Colosseum | Unlocks ring of dueling teleport directly to the Colosseum. [Fortis Colosseum](https://oldschool.runescape.wiki/w/Fortis_Colosseum) | 5 |
| 10 | Clear Colosseum Wave 1 (any wave) | The Colosseum is explicitly 12 waves culminating in Sol Heredit — each wave is a natural checkpoint. [Fortis Colosseum](https://oldschool.runescape.wiki/w/Fortis_Colosseum) | 5 |
| 11 | Obtain Dizana's quiver or a Sunfire fanatic armour piece | Colosseum unique drops — best-in-slot Varlamore-native combat gear. [Fortis Colosseum](https://oldschool.runescape.wiki/w/Fortis_Colosseum) | 6 |
| 12 | Complete The Heart of Darkness | Unlocks the Tapoyauik dungeon. [The Heart of Darkness](https://oldschool.runescape.wiki/w/The_Heart_of_Darkness) | 5 |
| 13 | Complete The Final Dawn | Highest quest-point reward in the line (3 QP), grants Arkan blade + access to Crypt of Tonali and Doom of Mokhaiotl. [The Final Dawn](https://oldschool.runescape.wiki/w/The_Final_Dawn) | 7 |
| 14 | First Doom of Mokhaiotl kill | Newest (Sept 2025) Varlamore boss, 15 Combat Achievement tasks tied to it — a clear "current endgame" marker. [Doom of Mokhaiotl](https://oldschool.runescape.wiki/w/Doom_of_Mokhaiotl) | 8 |
| 15 | Reach Herblore 81 via Mastering Mixology | Unlocks all Mixology potion recipes — the skill ceiling milestone for Varlamore's best XP-rate training method. [Mastering Mixology](https://oldschool.runescape.wiki/w/Mastering_Mixology) | varies (parallel skilling track) |

---

## 6. Milestone-gated quality-of-life ideas (12)

RuneLite plugins can reliably do: overlays (on-screen panels/highlights), menu-entry swapping/injection, chat message injection, infoboxes, config-driven toggles, and reading client-side game state (inventory, varbits, NPC/object positions, quest progress varbits). They **cannot** do anything server-authoritative: no item creation, no teleporting the player, no bypassing actual game requirements, no multiplayer trading logic. All ideas below are scoped to what's realistically client-side.

| # | Trigger | What it does | Why it fits the theme | RuneLite feasibility |
|---|---|---|---|---|
| 1 | Children of the Sun completed | Shows a "Varlamore Lock Active" panel banner and starts the boundary check | Marks the true start of the challenge | Easy — quest-completion varbit + panel state |
| 2 | First Quetzal whistle obtained | Overlay highlighting nearby Quetzal stop locations on the minimap/world map | Rewards exploration with a navigation aid, mirroring "earned convenience" | Easy — static coordinate overlay gated by inventory/varbit check |
| 3 | Twilight's Promise completed | Adds a menu-entry shortcut / chat reminder surfacing the Civitas illa Fortis Teleport spell in the spellbook | Reinforces that a new internal teleport is now "legal" and available | Easy — menu entry highlight via widget/spellbook state |
| 4 | 12,000 Glory reached | Un-hides a "Colosseum via Ring of Dueling" reminder overlay / stops flagging that teleport option as blocked | Ties a QoL unlock to an actual in-game unlock rather than an arbitrary plugin gate | Easy — the plugin already blocks/allows ring of dueling destinations by menu text; this flips one destination from blocked to allowed once the real unlock exists |
| 5 | Perilous Moons completed | Infobox / sidebar counter for lesser nagua Slayer task streak | Gives visible progress on a Varlamore-exclusive Slayer line | Easy — chat/varbit parsing for Slayer task assignment text |
| 6 | Any Colosseum wave cleared | Persistent best-wave tracker in the side panel ("Best: Wave 7") | Mirrors the wiki-confirmed 12-wave structure as a visible ladder | Easy — parse Colosseum wave widget/chat text, store in config |
| 7 | Death on the Isle completed | Overlay tile/marker for the North Aldarin Pendant of Ates teleport landing spot | Small navigational reward for finishing a quest that unlocks a teleport | Easy — static tile overlay gated by quest varbit |
| 8 | Mastering Mixology 60 Herblore reached | Adds an infobox reminding which potion packs are now purchasable (Apprentice/Adept/Expert) | Surfaces a real unlock the player earned, reducing wiki alt-tabbing | Easy — read Herblore level, static text lookup |
| 9 | Reaching Hunter 46 (Guild access) | Chat message + one-time notification "Hunter Guild access unlocked" the moment the level-up happens | Immediate positive feedback for hitting a hard content gate | Easy — level-up event listener with a level-46 check |
| 10 | Stealing Valuables unlocked (Thieving 50) | Overlay highlighting which houses in Civitas illa Fortis currently have an active valuable to steal | Reduces tedium of a "modest, low-intensity" grind the wiki itself flags as slow | Medium — requires tracking object/NPC state per house, doable via GameObject/ground-item scanning already common in RuneLite plugins |
| 11 | The Final Dawn completed | A "Varlamore Bank Substitute" panel: since UIM has no bank, show a highlighted checklist of key untradeable/quest items the player should keep on them or in a POH-less storage equivalent (looting bag, seed box, rune pouch contents) | Directly addresses the no-bank UIM pain point using only inventory-read access | Medium — inventory/equipment scanning is standard RuneLite API; the "storage" itself is just a smarter checklist overlay, not real storage (that would be server-side and impossible) |
| 12 | First Doom of Mokhaiotl kill | Unlocks a "Varlamore Completionist" milestone badge in the plugin panel + resets/advances an overall progress-bar percentage | Gives the account a visible "current endgame reached" marker, consistent with the wiki-confirmed newest and hardest Varlamore content | Easy — cosmetic panel state change, no game-state manipulation needed |

All twelve are purely observational/cosmetic (overlays, panel state, chat, menu highlighting) — none require or attempt to bypass real game restrictions, consistent with how the existing `SpellTeleportBlocker`/`ItemTeleportBlocker` classes already just read menu state and block/allow client-side, never touching server logic.
