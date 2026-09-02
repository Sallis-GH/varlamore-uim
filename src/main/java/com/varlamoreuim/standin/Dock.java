package com.varlamoreuim.standin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.coords.WorldPoint;

import java.util.Optional;

/**
 * The three Varlamore charter ship docks. Anchors identify which dock a charter
 * crewmember belongs to; they are not spawn positions.
 */
@Getter
@RequiredArgsConstructor
public enum Dock
{
	SUNSET_COAST(new WorldPoint(1514, 2971, 0)),
	ALDARIN(new WorldPoint(1455, 2968, 0)),
	FORTIS_COTHON(new WorldPoint(1743, 3136, 0));

	public static final int MAX_DISTANCE = 20;

	private final WorldPoint anchor;

	public static Optional<Dock> nearest(WorldPoint point)
	{
		Dock best = null;
		int bestDistance = Integer.MAX_VALUE;
		for (Dock d : values())
		{
			int distance = d.anchor.distanceTo2D(point);
			if (distance < bestDistance)
			{
				bestDistance = distance;
				best = d;
			}
		}
		return bestDistance <= MAX_DISTANCE ? Optional.of(best) : Optional.empty();
	}
}
