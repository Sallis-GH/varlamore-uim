# Phase 3: Item & Minigame Teleport Blocking - Context

**Gathered:** 2026-02-17
**Status:** Ready for planning

<domain>
## Phase Boundary

Block all item-based teleports (jewelry, tablets, quest items, diary gear) and minigame grouping tab teleports that would move the player outside Varlamore. Items with Varlamore destinations remain usable for those destinations.

</domain>

<decisions>
## Implementation Decisions

### Blocking feedback
- Show a chat message on blocked item teleports, consistent with Phase 2 spell blocking
- Message includes the specific item name and destination: e.g., "Ring of Dueling teleport to Duel Arena is blocked — you are locked to Varlamore"
- Same color/style as Phase 2 spell block messages — unified blocking system feel
- Menu options (Rub, Break, etc.) remain visible; block happens on action interception, not menu removal

### Multi-destination items
- Block only outside-Varlamore destinations; allow Varlamore destinations on the same item
- Known Varlamore destinations to allow:
  - Ring of Dueling → Fortis Colosseum
  - Pendant of Ates → All destinations (whitelist entirely, skip checks)
  - Hunter cape / Max cape → Hunter Guild
- All other listed items (glory, games necklace, skills necklace, combat bracelet, wealth, burning amulet, passage, digsite pendant, slayer ring, tablets, quest items, diary gear) teleport exclusively outside Varlamore
- Block must happen before charge consumption / item destruction — preserve the item
- House teleport tablets: block unless player's house is located in Varlamore

### Minigame grouping tab teleports
- Block all minigame teleports from the grouping tab — no minigame teleport destinations are inside Varlamore
- Same feedback style as items: chat message naming the specific minigame destination
- This is separate from item-based minigame access (e.g., Ring of Dueling → Colosseum is an item teleport, not a grouping tab teleport)

### Claude's Discretion
- Event interception mechanism (MenuOptionClicked vs other hooks)
- Data structure for item/destination mapping
- How to detect POH location for house teleport tab logic
- Exact categorization of items into implementation groups (jewelry vs tablets vs quest items)

</decisions>

<specifics>
## Specific Ideas

- Ring of Dueling Colosseum teleport is a concrete example of per-destination blocking — must allow Colosseum while blocking Duel Arena, Castle Wars, Ferox Enclave
- Pendant of Ates is purely Varlamore-internal and should be whitelisted entirely (no blocking logic runs)
- Minigame cooldown is not an issue — cooldown only starts after teleport completes, so blocking before teleport naturally preserves it
- Reference: https://oldschool.runescape.wiki/w/Varlamore (Transportation section) for authoritative list of Varlamore-internal teleports

</specifics>

<deferred>
## Deferred Ideas

None — discussion stayed within phase scope

</deferred>

---

*Phase: 03-item-minigame-teleport-blocking*
*Context gathered: 2026-02-17*
