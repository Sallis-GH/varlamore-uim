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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

	public boolean isActive()
	{
		return active;
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
			if (byIndex.containsKey(npc.getIndex()))
			{
				return;
			}
			RuneLiteObject object = client.createRuneLiteObject();
			Model model = buildModel(persona.get().getNpcId());
			if (model == null)
			{
				model = buildModel(PersonaRoster.FALLBACK_NPC_ID);
			}
			if (model == null)
			{
				log.warn("Could not build model for persona {}", persona.get().getId());
				return;
			}
			object.setModel(model);
			object.setRadius(60);
			StandIn standIn = new StandIn(client, npc, persona.get(), object);
			standIn.sync();
			byIndex.put(npc.getIndex(), standIn);
			log.debug("Bound {} to crewmember index {} at {}", persona.get().getId(), npc.getIndex(), dock.get());
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
			s.sync();
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
