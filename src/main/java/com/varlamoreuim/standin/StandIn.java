package com.varlamoreuim.standin;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Animation;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.RuneLiteObject;
import net.runelite.api.coords.LocalPoint;

/**
 * One puppet: a RuneLiteObject wearing a persona's model, bound to a hidden
 * charter crewmember. {@link #sync()} copies the crewmember's position,
 * orientation and pose animation every client tick.
 */
@Slf4j
public class StandIn
{
	@Getter
	private final NPC npc;
	@Getter
	private final Persona persona;
	@Getter
	private final RuneLiteObject object;
	private final Client client;

	private int lastPose = -1;

	public StandIn(Client client, NPC npc, Persona persona, RuneLiteObject object)
	{
		this.client = client;
		this.npc = npc;
		this.persona = persona;
		this.object = object;
	}

	public void sync()
	{
		LocalPoint lp = npc.getLocalLocation();
		if (lp == null)
		{
			return;
		}
		object.setLocation(lp, client.getTopLevelWorldView().getPlane());
		object.setOrientation(npc.getOrientation());
		int pose = npc.getPoseAnimation();
		if (pose != lastPose && pose >= 0)
		{
			Animation anim = client.loadAnimation(pose);
			if (anim != null)
			{
				object.setAnimation(anim);
				object.setShouldLoop(true);
				lastPose = pose;
			}
		}
		if (!object.isActive())
		{
			object.setActive(true);
		}
	}

	public void destroy()
	{
		object.setActive(false);
	}
}
