# Review: docs/varlamore-ideas/index.html (scoring page)

Reviewed: the four desktop screenshots, the 800px-frame screenshot, `index.template.html`
(321 lines, all CSS/JS inline), and the built `index.html` (6 characters, 50 variants,
975 score boxes, 52 plugin ideas). Counts verified by parsing the inlined JSON:
88 boxes in Unlock design, 835 across the six characters, 52 plugin ideas = 975.

Overall the page looks good. The visual language (paper cards, gold rule, blue/cream
bubbles, red/green score tint) is calm and consistent, nothing is clipped or overlapping
at any width I could reproduce, and the information is genuinely well organised. What
holds it back is a stale build, three small data-integrity bugs in the score input, and
the fact that scoring 975 boxes currently costs about 2,000 keystrokes of pure Tab.

---

## Must fix

### 1. The built page is stale — three characters are missing their editor's pick

`scripts/{fisher,guard,vagrant}/editors-pick.json` are dated 01:40; `index.html` was built
at 01:37 and contains zero occurrences of "editor". `build.py`'s `rank()` sorts
editor's pick first, approved second, writers third, reviewer last — but the built page
shows *reviewer* first in every tab strip, which is the pre-`rank()` order. So the owner
would score a page whose tab order and variant set are both out of date, and the box
count will not be 975 after a rebuild.

**Change:** run `python docs/varlamore-ideas/build.py` and re-check the printed counts
before handing over. Then freeze `scripts/` and `content/` — see item 7.

### 2. The number box can display one value and export a different one

`index.template.html:238-245`:

```js
let v = parseInt(inp.value, 10);
if (!isNaN(v)) { v = Math.max(1, Math.min(100, v)); }
...
if (isNaN(v)) delete store[id].score; else store[id].score = v;
```

The clamp is applied to `v` but never written back to `inp.value`. Type `150` and the box
reads 150 while the export says 100. Type `0` and the box reads 0 while the export says 1.
Silent disagreement between what the owner sees and what they send back is the worst
possible failure mode for this page.

**Change:** after the clamp, write it back, and re-clamp on blur:

```js
if (!isNaN(v)) { v = Math.max(1, Math.min(100, v)); if (String(v) !== inp.value) inp.value = v; }
```

### 3. The scroll wheel silently rewrites the score box under the cursor

`<input type="number">` in Chrome increments on wheel while focused. On a page this long,
the owner will type a score, then scroll — and Chrome will happily turn 74 into 61 on the
way down. Add next to the `input` listener at `index.template.html:236`:

```js
inp.addEventListener('wheel', e => { if (document.activeElement === inp) inp.blur(); }, {passive:true});
```

While there, kill the spinners so they stop overlapping the 92px box on hover:

```css
.score input[type=number]{-moz-appearance:textfield}
.score input::-webkit-outer-spin-button,.score input::-webkit-inner-spin-button{-webkit-appearance:none;margin:0}
```

### 4. Tab does not go from score box to score box

`scoreBox()` (`index.template.html:121`) emits `<input>` then `<button class="notebtn">`,
so Tab lands on the note button between every single box: ~1,950 Tab presses for 975
scores. Hidden tab-panels are `display:none` so they are correctly skipped — the problem
is purely the note button.

**Change:** add `tabindex="-1"` to the notebtn markup, and bind Enter to advance:

```js
const boxes = () => Array.from(main.querySelectorAll('input[data-score]')).filter(i => i.offsetParent);
main.addEventListener('keydown', e => {
  const inp = e.target.closest && e.target.closest('input[data-score]');
  if (!inp) return;
  if (e.key === 'Enter' || e.key === 'ArrowDown' || e.key === 'ArrowUp') {
    e.preventDefault();
    const list = boxes(), i = list.indexOf(inp);
    const next = (e.key === 'ArrowUp' || e.shiftKey) ? list[i-1] : list[i+1];
    if (next) { next.focus(); next.select(); }
  }
});
```

(`ArrowUp/Down` currently do nothing useful in a number input other than +1/-1, which is
not a gesture anyone wants here; reassigning them is a net win. Keep the note button
reachable with a shortcut, e.g. `Alt+N` toggles the note for the focused box.)

### 5. A failed save is completely silent

`index.template.html:113`: `try { localStorage.setItem(...) } catch (e) {}`. If the owner
opens the file with site data blocked, or in a hardened profile, or hits quota, they can
score for two hours and lose all of it with no signal whatsoever. The progress counter
keeps ticking because it reads the in-memory `store`.

**Change:** on the first failed write, set a flag and show a persistent (not toast) red
banner pinned above the content: "Scores are NOT being saved in this browser — copy the
text in *Your scores* before you close this tab." Also probe once at startup with a
throwaway `setItem`/`removeItem` so the warning appears before any work is done.

### 6. Export lines come out in the wrong order and carry no context

`exportText()` does `ids.sort()` on raw string ids, so `…:l10` sorts before `…:l2` and a
five-line exchange is exported scrambled. Every row is also just
`fisher:writer-b:1:o2l1 | 42 | Ask them.` — the owner (and anyone acting on it) has to
decode the id to know which character, phase and variant it belongs to.

**Change:** export in DOM order and emit group headings. The cheap enabler is to record
context at render time — turn `scoreIds` (line 114) from an array of strings into an array
of records and pass the context into `scoreBox(id, label, ctx)`:

```js
const ids = Array.from(main.querySelectorAll('input[data-score]')).map(i => i.dataset.score);
// then walk `ids` in order, emitting "## Fremennik fisher — before the quiver — 'Bad Catch' (writer-b)"
// whenever the recorded group changes, and keep any store keys not present in the DOM
// under a trailing "## Unmatched (older build)" heading so nothing is lost.
```

Then put a per-variant average under each group heading (`mean of 17 line scores: 63;
Overall: 70`) and, at the very top, a leaderboard of the Overall boxes sorted high to low.
That top block is the thing that actually gets acted on; today the owner would have to
compute it by hand from 975 rows.

### 7. Score ids are position-based, so any content edit silently remaps saved scores

Ids are `char:author:variantIndex:lNN`. `build.py`'s docstring is honest about this, but
the consequence is sharper than it reads: insert one line into the middle of a variant and
every later score in that variant now belongs to a different sentence, with no error. Once
the page is handed over, `scripts/` and `content/` must not change. Either freeze them, or
append a short hash of the line text to the id so a rebuild can detect and drop stale
scores instead of misattributing them.

---

## Should fix

- **Bubbles are far too wide on a desktop.** `.row` is `110px minmax(0,1fr) 92px` inside a
  1180px `main`, so "Not mine." renders as a 880px-wide bubble with the score box a long
  eye-travel away (visible in screenshot 2). Cap the conversation column:
  `.convo .row{max-width:860px}` — or `.bubble{max-width:62ch}`. Same for prose:
  `main p, main li{max-width:78ch}`; the overview paragraphs are currently ~150 characters
  per line.
- **Sticky tab bar.** A character section is 130–150 boxes tall. Once you are 3 screens
  into "Bad Catch" there is nothing on screen saying which variant you are in, and
  switching variants means scrolling back up. `.tabs{position:sticky;top:0;z-index:5;
  background:var(--bg);padding:8px 0;margin:0 0 4px}` plus bumping
  `section{scroll-margin-top:56px}` fixes both. Put the character name in the same sticky
  strip if it fits.
- **Per-tab progress counters.** Append `<span class="meta">3/17</span>` to each tab button
  and update it on input. Right now a tab gives no hint whether it is untouched, and the
  975 total silently includes every unopened tab — the owner has no way to see what they
  have not reached. Also relabel the sidebar counter: "0 / 975 boxes (includes tabs you
  haven't opened yet)".
- **Score box for plugin ideas is at the top of a 1,900-character card** (screenshot 3), so
  you read to the bottom and scroll back up to score. `.idea .score{position:sticky;
  top:64px;align-self:start}` keeps it beside you as you read.
- **Active-tab contrast.** White on `--gold` `#b8892b` is ~3.1:1, and the `(writer-b)`
  meta at `#fff3d6` is ~2.8:1 — under AA for 14px/12px text. Darken the active state to
  `#8a6318` (≈5.4:1) rather than lightening the label further.
- **`.notebtn` is 11px.** Bump to 12px; it is currently the smallest text on the page and
  it is an interactive control.
- **"Clear all scores" sits next to "Copy scores".** One confirm dialog is thin protection
  for hours of work. Style it as destructive (`color:var(--red);border-color:#d9a3a3`),
  move it to the right of the row with a gap, and have it stash the old store under
  `vuim-ideas-scores-v1-backup` so it is recoverable.
- **Narrow layout loses the nav entirely.** Below 900px `nav.side` becomes
  `position:relative;height:auto`, so the whole dark block sits at the top of the document
  and scrolls away — no progress bar, no section jumps, for the rest of a very long page.
  If the owner may use a half-width window, collapse it to a sticky one-line bar instead
  (`position:sticky;top:0;display:flex;overflow-x:auto` with the links inline).
- **901–1000px is the cramped range.** Fixed 260px sidebar + 80px `main` padding leaves
  ~260px of bubble. Nothing overflows (every grid uses `minmax(0,1fr)`, and I found no
  tables, images or raw URLs in the content), but the layout would breathe better if the
  breakpoint moved to `max-width:1024px` and `--sidebar` dropped to 220px.
- **`code` does not wrap.** `Quest.CHILDREN_OF_THE_SUN.getState(client)` has no break
  opportunity. It fits at 800px, but add `code{overflow-wrap:anywhere}` for safety.
- **Unlock sub-links never highlight.** The scroll spy collects
  `main section, main [id^="char:"]` (line 309) but the four candidate cards are
  `id="unlock:…"`, so those four sidebar links are dead weight while scrolling. Add
  `, main [id^="unlock:"]` to the selector.

## Nice to have

- **"Overall rows only" filter.** `characters_lead` already tells the owner the Overall box
  matters most when short on time — make that a real mode. Tag the overall rows
  (`class="row overall"` in `overallRow()`), add a sidebar checkbox that sets
  `body.only-overall`, and `body.only-overall .convo .row:not(.overall), body.only-overall
  .options{display:none}`. That turns the job into ~60 decisive boxes with the full
  scripts one click away.
- **"Hide what I've scored."** Same mechanism: toggle a `done` class on the row from the
  input handler, and `body.hide-done .row.done{display:none}`. Makes the second pass over
  975 boxes finite.
- **Jump to next unscored** (button + `Ctrl+Enter`), including switching to the next tab
  that still has empty boxes. With items 4 and this, a full pass is Enter-Enter-Enter.
- **Paste-to-restore.** The export already ends with a full JSON blob but there is no way
  back in. A textarea plus an "Import" button (parse, merge, re-render values) makes the
  page resumable on another machine and is ~10 lines.
- **Collapse "How this was made"** into `<details>`; it is process metadata sitting between
  the owner and the work.
- **Cap the whole layout** (`.layout{max-width:1600px;margin:0 auto}`). On the 2560px
  screen the now-centred column leaves the sidebar stranded far to the left.

## Copy

- Tab labels collide badly. The guard's pre-quiver strip reads
  "Standing orders (writer-a)" next to "Standing Orders (writer-b)" — the same words with
  different casing — and the approved variant renders as
  **"Not for you (current (approved by you))"**, nested parentheses and all. Rename that
  author to `approved` in `scripts/guard/approved.json`, prefix tabs with a short code
  (`A · Standing orders`, `B · Standing Orders`, `R · Wrong queue`, `E · …`), and
  normalise writer-b's Title Case to sentence case to match everyone else. After the
  rebuild, the editor's pick titles duplicate the approved/reviewer ones for fisher, guard
  and vagrant ("Not for you", "Fine", "Stayed for the fish") — retitle or the strip becomes
  unreadable.
- `overallRow` lowercases the character name:
  "does a **fremennik** fisher belong at Sunset Coast?" — `c.name.toLowerCase()` at
  `index.template.html:203` mangles a proper noun. Drop the lowercasing or keep a
  lowercase display name in `characters.json`.
- "One thing to fix regardless of scores" → "A bug worth fixing whatever you score:
  fairy rings are an open exit". The heading currently hides the finding.
- Sidebar "Milestone-gated quality of life" is the longest label in the nav; "Quality of
  life" is enough, the section lead already explains the gating.
- `characters_lead` is five sentences in small muted type. Keep sentence one and the
  "Overall box matters most" sentence; the review-process detail belongs in the overview.
- "Refresh text" → "Update text below" (as written it reads like it might reload the page).

---

**Ready to hand over: no.** It is close — half a day of work. Blocking items are the stale
build (#1), the two input bugs that make the visible score disagree with the exported one
(#2, #3), the silent save failure (#5), and the scrambled, context-free export (#6). Fix
those plus the Tab chain (#4) and it is genuinely pleasant to use.
