package com.varlamoreuim.standin;

import com.varlamoreuim.dialogue.DialogueContext;
import com.varlamoreuim.dialogue.DialogueEffect;
import com.varlamoreuim.dialogue.DialoguePage;
import com.varlamoreuim.dialogue.DialogueScript;
import com.varlamoreuim.dialogue.NpcLine;
import com.varlamoreuim.dialogue.Option;
import com.varlamoreuim.dialogue.Options;
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
	public void everyScriptHasQuiverBranchThatUnlocks()
	{
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
							DialoguePage target = s.page(o.resolve(HAS_QUIVER));
							assertTrue(p.getId(), target instanceof NpcLine);
							assertEquals(p.getId(), DialogueEffect.UNLOCK_CHARTER, ((NpcLine) target).getEffect());
						}
					}
				}
			}
			assertTrue(p.getId() + " lacks unlock page", foundUnlock);
			assertTrue(p.getId() + " lacks quiver option", foundConditional);
		}
	}
}
