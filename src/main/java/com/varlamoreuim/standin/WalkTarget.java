package com.varlamoreuim.standin;

import net.runelite.api.coords.WorldPoint;

/**
 * Chooses the tile next to an NPC that the player should walk to before talking:
 * the neighbour (8-way) with the smallest distance to the player.
 */
public final class WalkTarget
{
	private static final int[][] OFFSETS = {
		{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
	};

	private WalkTarget()
	{
	}

	public static WorldPoint adjacentTile(WorldPoint npc, WorldPoint player)
	{
		WorldPoint best = null;
		int bestDistance = Integer.MAX_VALUE;
		for (int[] o : OFFSETS)
		{
			WorldPoint candidate = new WorldPoint(npc.getX() + o[0], npc.getY() + o[1], npc.getPlane());
			int dx = candidate.getX() - player.getX();
			int dy = candidate.getY() - player.getY();
			int distance = dx * dx + dy * dy;
			if (distance < bestDistance)
			{
				bestDistance = distance;
				best = candidate;
			}
		}
		return best;
	}
}
