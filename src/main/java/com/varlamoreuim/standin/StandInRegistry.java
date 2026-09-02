package com.varlamoreuim.standin;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Model;
import net.runelite.api.ModelData;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.RuneLiteObject;
import net.runelite.api.WorldView;
import net.runelite.client.callback.ClientThread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Owns every active {@link StandIn}. Binds a puppet when a charter crewmember
 * spawns, drops it on despawn, and mirrors positions on each client tick.
 * {@link #setActive(boolean)} switches the whole system on (rescan) or off (clear).
 */
@Slf4j
public class StandInRegistry
{
	private final Client client;
	private final ClientThread clientThread;
	private final Map<Integer, StandIn> byIndex = new LinkedHashMap<>();
	private final Set<String> warnedPersonas = new HashSet<>();
	private boolean active = false;

	public StandInRegistry(Client client, ClientThread clientThread)
	{
		this.client = client;
		this.clientThread = clientThread;
	}

	public void setActive(boolean active)
	{
		if (this.active == active)
		{
			return;
		}
		this.active = active;
		if (active)
		{
			rescan();
		}
		else
		{
			clear();
		}
	}

	public Collection<StandIn> active()
	{
		return Collections.unmodifiableCollection(byIndex.values());
	}

	public Optional<StandIn> byNpcIndex(int index)
	{
		return Optional.ofNullable(byIndex.get(index));
	}

	public void bind(NPC npc)
	{
		if (!active || npc == null || !PersonaRoster.CHARTER_NPC_IDS.contains(npc.getId()))
		{
			return;
		}
		if (byIndex.containsKey(npc.getIndex()))
		{
			return;
		}
		Optional<Dock> dock = Dock.nearest(npc.getWorldLocation());
		if (!dock.isPresent())
		{
			log.debug("Charter NPC {} at {} is not near a known dock", npc.getId(), npc.getWorldLocation());
			return;
		}
		Optional<Persona> persona = PersonaRoster.get(dock.get(), npc.getId());
		if (!persona.isPresent())
		{
			log.debug("No persona for dock {} npc {}", dock.get(), npc.getId());
			return;
		}
		clientThread.invoke(() ->
		{
			if (!active)
			{
				return;
			}
			if (byIndex.containsKey(npc.getIndex()))
			{
				return;
			}
			WorldView wv = client.getTopLevelWorldView();
			if (wv == null)
			{
				return;
			}
			if (wv.npcs().byIndex(npc.getIndex()) != npc)
			{
				return;
			}
			Persona resolved = withUsableChathead(persona.get());
			Model model = buildModel(resolved.getNpcId());
			if (model == null)
			{
				model = buildModel(PersonaRoster.FALLBACK_NPC_ID);
			}
			if (model == null)
			{
				log.warn("Could not build model for persona {}", resolved.getId());
				return;
			}
			RuneLiteObject object = client.createRuneLiteObject();
			object.setModel(model);
			object.setRadius(60);
			StandIn standIn = new StandIn(client, npc, resolved, object);
			standIn.sync();
			byIndex.put(npc.getIndex(), standIn);
			log.debug("Bound {} to crewmember index {} at {}", resolved.getId(), npc.getIndex(), dock.get());
		});
	}

	public void unbind(NPC npc)
	{
		StandIn s = byIndex.remove(npc.getIndex());
		if (s != null)
		{
			s.destroy();
		}
	}

	public void sync()
	{
		for (StandIn s : byIndex.values())
		{
			try
			{
				s.sync();
			}
			catch (Exception e)
			{
				log.debug("stand-in sync failed", e);
			}
		}
	}

	public void rescan()
	{
		if (!active)
		{
			return;
		}
		WorldView wv = client.getTopLevelWorldView();
		if (wv == null)
		{
			return;
		}
		for (NPC npc : wv.npcs())
		{
			bind(npc);
		}
	}

	public void clear()
	{
		for (StandIn s : byIndex.values())
		{
			s.destroy();
		}
		byIndex.clear();
	}

	/**
	 * Returns the persona unchanged when its NPC definition supplies chathead models,
	 * otherwise a copy pointing at {@link PersonaRoster#FALLBACK_NPC_ID} so the dialogue
	 * chathead has something to draw. Warns once per persona. Client thread only.
	 */
	private Persona withUsableChathead(Persona p)
	{
		NPCComposition comp = client.getNpcDefinition(p.getNpcId());
		if (comp != null && comp.getChatheadModels() != null && comp.getChatheadModels().length > 0)
		{
			return p;
		}
		if (warnedPersonas.add(p.getId()))
		{
			log.warn("Persona {} npc {} has no chathead models, falling back to {}",
				p.getId(), p.getNpcId(), PersonaRoster.FALLBACK_NPC_ID);
		}
		return new Persona(p.getId(), p.getDisplayName(), PersonaRoster.FALLBACK_NPC_ID, p.getExamine(), p.getScript());
	}

	private Model buildModel(int npcId)
	{
		NPCComposition comp = client.getNpcDefinition(npcId);
		if (comp == null || comp.getModels() == null || comp.getModels().length == 0)
		{
			return null;
		}
		short[] recolFrom = comp.getColorToReplace();
		short[] recolTo = comp.getColorToReplaceWith();
		List<ModelData> parts = new ArrayList<>();
		for (int modelId : comp.getModels())
		{
			ModelData md = client.loadModelData(modelId);
			if (md == null)
			{
				continue;
			}
			md.cloneColors().cloneVertices();
			if (recolFrom != null && recolTo != null)
			{
				for (int i = 0; i < recolFrom.length; i++)
				{
					md.recolor(recolFrom[i], recolTo[i]);
				}
			}
			parts.add(md);
		}
		if (parts.isEmpty())
		{
			return null;
		}
		return client.mergeModels(parts.toArray(new ModelData[0])).light();
	}
}
