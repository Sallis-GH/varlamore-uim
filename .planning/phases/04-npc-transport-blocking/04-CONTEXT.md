# Phase 4: NPC Transport Blocking - Context

**Gathered:** 2026-03-15
**Status:** Ready for planning

<domain>
## Phase Boundary

Block all NPC-based transport methods that would take the player out of Varlamore. This phase merges the original Phase 4 (NPC Transport Blocking) and Phase 5 (NPC Replacement System) into a single phase, since the blocking mechanism IS the replacement — hiding transport NPCs and spawning stand-ins.

**In scope:** Charter ship NPCs (3 locations), Primio quetzal (Varrock route), NPC hiding, replacement NPC spawning, dialogue system, unlock gating.

**Out of scope:** Fairy rings, spirit trees, gnome gliders (all quest-gated and inaccessible to Varlamore-locked UIMs).

</domain>

<decisions>
## Implementation Decisions

### Blocking mechanism
- No menu-click blocking for NPC transport — the transport NPCs are **hidden entirely** and replaced with stand-in NPCs
- Blocking and replacement are one feature (Phases 4 & 5 merged)
- Primio quetzal (NPC ID 12889, Varrock route) is a special case — block interaction with a chat message instead of NPC replacement

### Transport inventory
- **Charter ship NPCs** at 3 locations: Sunset Coast, Aldarin, Fortis Cothon — hide and replace with stand-in NPCs
- **Primio quetzal** (NPC ID 12889) — block with chat message: "The bird doesn't seem interested in interacting with you."
- **Fairy rings, spirit trees, gnome gliders** — skipped entirely, all quest-gated and inaccessible to Varlamore-locked UIMs
- OSRS Wiki (https://oldschool.runescape.wiki/w/Varlamore) is the authoritative source for transport NPC details

### Player feedback
- Replacement NPCs use **GAMEMESSAGE chat messages** for feedback (RuneLite API cannot open NPC dialogue boxes programmatically — revised from original NPC dialogue decision after API research)
- Dialogue tone is **lore-friendly excuses** (in-world reasons, not meta restriction messages)
- All replacement NPCs use the **same dialogue** — one generic lore-friendly excuse for all charter ship locations
- Primio quetzal uses **chat message only** (it's a bird, not a person) — "The bird doesn't seem interested in interacting with you."

### Replacement NPC identity
- Stand-in NPC model: **Mysterious Old Man** (https://oldschool.runescape.wiki/w/Mysterious_Old_Man)
- Appears at all 3 charter ship locations in place of hidden transport NPCs
- Spawned via **RuneLiteObject** with model loaded from `NPCComposition.getModels()` (Creator's Kit pattern — Plugin Hub compliant)
- Right-click "Talk-to Mysterious Old Man" menu entry injected via `PostMenuSort` event
- Idle animation set from `NPCComposition.getStandingAnimationID()`

### Unlock condition
- Transport NPCs are unlocked when the player acquires **Dizana's Quiver (uncharged)** (https://oldschool.runescape.wiki/w/Dizana%27s_quiver#Uncharged)
- Dizana's Quiver is the reward for beating the Fortis Colosseum
- Once unlocked: hide the Mysterious Old Man, show the real charter ship NPCs

### Claude's Discretion
- Specific dialogue text for the Mysterious Old Man (within the lore-friendly constraint)
- How to detect Dizana's Quiver ownership (inventory check, equipment check, or VarBit)
- Whether Primio quetzal should also be unlock-gated or permanently blocked
- Exact WorldPoint coordinates for Mysterious Old Man placement at each dock

</decisions>

<specifics>
## Specific Ideas

- The Mysterious Old Man is a well-known OSRS random event NPC — his presence at a dock would feel naturally in-world
- Dizana's Quiver represents beating the Colosseum, which is a major Varlamore achievement — good thematic unlock gate
- Quetzal transport is mostly internal to Varlamore and whitelisted (Phase 1); only the Varrock route via Primio needs blocking

</specifics>

<deferred>
## Deferred Ideas

- Item-based unlock gating for other restrictions (spells, items) — could be a future phase
- Per-location unique dialogue for replacement NPCs — deferred in favor of single shared dialogue
- Fairy ring / spirit tree blocking — unnecessary due to quest gating, but could be added if game updates change accessibility

</deferred>

---

*Phase: 04-npc-transport-blocking*
*Context gathered: 2026-03-15*
