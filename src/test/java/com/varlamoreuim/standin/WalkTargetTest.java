package com.varlamoreuim.standin;

import net.runelite.api.coords.WorldPoint;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class WalkTargetTest
{
	private static final WorldPoint NPC = new WorldPoint(1500, 3000, 0);

	@Test
	public void picksNeighbourFacingThePlayer()
	{
		assertEquals(new WorldPoint(1499, 3000, 0), WalkTarget.adjacentTile(NPC, new WorldPoint(1490, 3000, 0)));
		assertEquals(new WorldPoint(1500, 3001, 0), WalkTarget.adjacentTile(NPC, new WorldPoint(1500, 3010, 0)));
		assertEquals(new WorldPoint(1501, 3001, 0), WalkTarget.adjacentTile(NPC, new WorldPoint(1505, 3005, 0)));
	}

	@Test
	public void neverReturnsNpcTile()
	{
		WorldPoint t = WalkTarget.adjacentTile(NPC, NPC);
		assertNotEquals(NPC, t);
		assertEquals(1, t.distanceTo(NPC));
	}
}
