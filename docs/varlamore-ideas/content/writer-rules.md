# Rules for dialogue writers

You are writing lines for one character who stands at a charter-ship dock in Varlamore, in Old School RuneScape. The lines will be shown in the game's real chatbox dialogue window, so they must read exactly like the game's own NPC dialogue.

## The only lore you get

In this Varlamore, nobody may sail out of the kingdom until they have proven they can fend for themselves. The one who decides is the **harbour registrar** at **Fortis Cothon**, the capital's harbour in Civitas illa Fortis; the registrar keeps the harbour ledger for the crown. Champions of the **Fortis Colosseum** carry **Dizana's quiver** as their proof. Nobody at the docks can unlock anything, and most of them only half-know the rule, or don't care.

Write what YOUR character would actually know and say. A fisher knows fish. A vagrant knows boots and crews. A guard knows the order he was given. Do not make everyone an expert on the law. A whole conversation can be a joke with nothing useful in it. Do not hand-hold the player.

## What to deliver

One JSON file. Three variants:

- Two **pre** variants: the player has no quiver. The character does not help them sail. Each variant should feel different from the other (a different mood, moment, or angle).
- One **post** variant: the player is visibly wearing Dizana's quiver. The character reacts in their own way and, in character, ends up sending the player toward the harbour registrar in the Cothon (or toward "the harbour in the capital", if that is all they'd know).

Every variant: the player speaks first (a plain question about getting a ship or boat, in your words), then 2 to 5 more lines back and forth, then ONE options menu with 3 or 4 options, each option ending the conversation in 1 to 3 lines. No loops back to the menu.

## JSON format (exactly this shape)

```json
{
  "character": "<display name>",
  "author": "<given to you>",
  "variants": [
    {
      "title": "<two or three words>",
      "phase": "pre",
      "premise": "<one sentence: what the character is doing when the player arrives>",
      "lines": [
        {"speaker": "P", "text": "..."},
        {"speaker": "N", "text": "..."},
        {"speaker": "X", "text": "..."}
      ],
      "options": [
        {"label": "...", "lines": [{"speaker": "N", "text": "..."}]},
        {"label": "...", "lines": [{"speaker": "N", "text": "..."}, {"speaker": "P", "text": "..."}, {"speaker": "N", "text": "..."}]}
      ]
    }
  ]
}
```

Speakers: `P` player, `N` your character, `X` a narration box (flat, present tense: "You show the guard the quiver." "The fisher doesn't look up.").

## Register (this is how the game writes)

- 3 to 14 words per line. Hard maximum 90 characters. Options under 36 characters.
- Speech, not writing. Contractions always. Fragments are fine ("Fair enough." "Your loss."). If a line would look good on a poster, cut it.
- One joke is one beat, one line, then move on. No build-up, no callback, never explain it.
- One exclamation mark OR one ellipsis per line, never both. Exclamations for greetings and alarm only; complaints and refusals end in full stops.
- British spelling. No modern idioms, no memes, no sarcasm about the player's worth, no paperwork or form-number gags, no fourth-wall jokes.
- Varlamore locals may sprinkle ONE local word per line, rarely: "Nilsal" (hello), "iknami" (friend, said to the player), "Tetamo" (oh no / damn), "Timoiva" (goodbye). Fremenniks and foreigners never use them.
- The character says "champion", "Colosseum" or "registrar" only if that person would know the word, and only if asked.
- Real examples of the game's register: "Sorry, but I have lots of work to do before we head back out to sea." / "I wish these grapes could pick themselves." / "Sorry, traveller, but you don't have permission to enter the Kingdom of Varlamore." / "Fair enough." / "We aren't a laundry ship." / "Don't talk to me outerlander. I need to fix this longboat."

Write the file, validate that it is well-formed JSON, and reply with only the file path.
