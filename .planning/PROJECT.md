# Varlamore UIM Plugin

## What This Is

A RuneLite plugin for Varlamore-locked Ultimate Ironman accounts that enforces the area restriction and provides tools for the locked gameplay experience. The plugin prevents players from leaving the Varlamore region through any travel mechanic, with an immersive NPC replacement system for NPC-based exits and hard blocks for spell/item-based travel. Built with a modular architecture so future features (QoA tools, content tracking, additional restrictions) can be added without rearchitecting.

## Core Value

Prevent the player from accidentally breaking their Varlamore lock by blocking all travel methods that leave the region — this is the one thing that must work reliably.

## Requirements

### Validated

(None yet — ship to validate)

### Active

- [ ] Define Varlamore boundary via map chunk IDs (full Varlamore region)
- [ ] Block NPC-based travel exits (boats, carts, transport NPCs) by hiding the real NPC and rendering a replacement that gives in-world dialogue explaining travel isn't unlocked
- [ ] Block teleport spells that would take the player outside Varlamore
- [ ] Block jewelry/item-based teleports (ring of dueling, glory amulet, etc.) that lead outside the region
- [ ] Block minigame teleports that would move the player outside Varlamore
- [ ] RuneLite side panel with categorized settings (Restrictions, QoA, etc.) built for extensibility
- [ ] Milestone-based unlock system that can progressively allow travel outside Varlamore as content is completed
- [ ] Plugin Hub-ready structure (proper metadata, config, naming conventions)

### Out of Scope

- Item restrictions (using/equipping items not obtainable in Varlamore) — not yet discussed, future feature
- Content tracking (available quests, items, bosses, skilling methods) — not yet discussed, future feature
- Achievement tracking (custom milestones for the lock) — not yet discussed, future feature
- Collection log (Varlamore-specific drop tracking) — not yet discussed, future feature
- Covering every single edge-case exit method in v1 — key exits first, edge cases later

## Context

- **Game:** Old School RuneScape (OSRS) via the RuneLite client
- **Account type:** Ultimate Ironman (UIM) — cannot bank items, cannot trade
- **Lock type:** Varlamore-locked — player restricts themselves to only the Varlamore region
- **Varlamore:** A continent/region in OSRS with two main cities (Cam Torum, Civitas illa Fortis) and surrounding areas. No wilderness zone.
- **Boundary system:** OSRS map is divided into chunks with unique IDs. The Varlamore boundary is defined by a set of these chunk IDs.
- **NPC replacement approach:** For NPC-based travel (boats, carts), hide the original NPC and spawn a replacement that delivers a chat dialogue explaining the restriction — feels like an in-game mechanic rather than a plugin overlay.
- **Built for a friend**, with plans to publish on the RuneLite Plugin Hub for other Varlamore-locked players.
- **First RuneLite plugin** — need to follow RuneLite API conventions and Plugin Hub submission requirements.

## Constraints

- **Platform:** RuneLite client — must use RuneLite Plugin API (Java)
- **Distribution:** Plugin Hub compatible — must follow their submission guidelines and naming conventions
- **Architecture:** Modular/extensible — settings panel must support adding new feature categories without restructuring
- **Game rules:** Cannot modify game state server-side — all restrictions are client-side enforcement and visual changes

## Key Decisions

| Decision | Rationale | Outcome |
|----------|-----------|---------|
| Chunk ID-based boundary | OSRS maps are already divided by chunk IDs — clean, configurable, standard approach | — Pending |
| NPC replacement for travel NPCs | Immersive experience — feels like a game mechanic, not a plugin block. Supports future unlock system. | — Pending |
| Hard block for spells/items | No need for NPC dialogue on non-NPC actions — just prevent the action | — Pending |
| Categorized settings panel | Extensibility — future features (QoA, tracking) get their own categories without UI rework | — Pending |
| Key exits first, edge cases later | Ship something useful fast, iterate on coverage | — Pending |

---
*Last updated: 2026-02-16 after initialization*
