package com.varlamoreuim.standin;

import com.varlamoreuim.dialogue.DialogueContext;
import com.varlamoreuim.dialogue.DialogueEffect;
import com.varlamoreuim.dialogue.DialoguePage;
import com.varlamoreuim.dialogue.DialogueScript;
import com.varlamoreuim.dialogue.Narration;
import com.varlamoreuim.dialogue.DialogueText;
import com.varlamoreuim.dialogue.NpcLine;
import com.varlamoreuim.dialogue.Option;
import com.varlamoreuim.dialogue.Options;
import com.varlamoreuim.dialogue.PlayerLine;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PersonaRosterTest
{
	private static final DialogueContext HAS_QUIVER = new DialogueContext()
	{
		@Override
		public boolean hasDizanasQuiver()
		{
			return true;
		}

		@Override
		public String playerName()
		{
			return "T";
		}
	};

	@Test
	public void everyDockHasBothCrewmembers()
	{
		assertTrue(PersonaRoster.get(Dock.SUNSET_COAST, 9314).isPresent());
		assertTrue(PersonaRoster.get(Dock.SUNSET_COAST, 9350).isPresent());
		assertTrue(PersonaRoster.get(Dock.ALDARIN, 9314).isPresent());
		assertTrue(PersonaRoster.get(Dock.ALDARIN, 9350).isPresent());
		assertTrue(PersonaRoster.get(Dock.FORTIS_COTHON, 9326).isPresent());
		assertTrue(PersonaRoster.get(Dock.FORTIS_COTHON, 9362).isPresent());
		assertEquals(6, PersonaRoster.all().size());
	}

	@Test
	public void personasAreDistinct()
	{
		Set<String> names = new HashSet<>();
		Set<Integer> npcIds = new HashSet<>();
		for (Persona p : PersonaRoster.all())
		{
			names.add(p.getDisplayName());
			npcIds.add(p.getNpcId());
		}
		assertEquals(6, names.size());
		assertEquals(6, npcIds.size());
	}

	@Test
	public void quiverBranchesUnlockAndAtLeastOnePersonaHasOne()
	{
		int personasWithCheck = 0;
		for (Persona p : PersonaRoster.all())
		{
			DialogueScript s = p.getScript();
			boolean foundUnlock = false;
			boolean foundConditional = false;
			for (DialoguePage page : s.getPages().values())
			{
				if (page instanceof NpcLine && ((NpcLine) page).getEffect() == DialogueEffect.UNLOCK_CHARTER)
				{
					foundUnlock = true;
				}
				if (page instanceof Options)
				{
					for (Option o : ((Options) page).getOptions())
					{
						if (o.getCondition() != null)
						{
							foundConditional = true;
							// Walk the yes-branch (challenge, narration, ...) to the line that unlocks.
							String id = o.resolve(HAS_QUIVER);
							boolean unlocks = false;
							for (int hops = 0; hops < 10 && !DialogueScript.END.equals(id); hops++)
							{
								DialoguePage target = s.page(id);
								if (target instanceof NpcLine)
								{
									if (((NpcLine) target).getEffect() == DialogueEffect.UNLOCK_CHARTER)
									{
										unlocks = true;
										break;
									}
									id = ((NpcLine) target).getNext();
								}
								else if (target instanceof Narration)
								{
									id = ((Narration) target).getNext();
								}
								else if (target instanceof PlayerLine)
								{
									id = ((PlayerLine) target).getNext();
								}
								else
								{
									break;
								}
							}
							assertTrue(p.getId() + " yes-branch never unlocks", unlocks);
						}
					}
				}
			}
			// A persona may skip the quiver check entirely, but one that has it must unlock.
			assertEquals(p.getId() + " unlock page without quiver option, or vice versa", foundConditional, foundUnlock);
			if (foundConditional)
			{
				personasWithCheck++;
			}
		}
		assertTrue("no persona checks for the quiver", personasWithCheck >= 1);
	}

	@Test
	public void everyPageFitsTheChatbox()
	{
		for (Persona p : PersonaRoster.all())
		{
			for (DialoguePage page : p.getScript().getPages().values())
			{
				String text = page instanceof NpcLine ? ((NpcLine) page).getText()
					: page instanceof PlayerLine ? ((PlayerLine) page).getText()
					: page instanceof Narration ? ((Narration) page).getText() : null;
				if (text != null)
				{
					int lines = DialogueText.wrap(text, DialogueText.MAX_CHARS_PER_LINE).size();
					assertTrue(p.getId() + " page too long: " + text, lines <= DialogueText.MAX_LINES_PER_PAGE);
				}
			}
		}
	}
}
