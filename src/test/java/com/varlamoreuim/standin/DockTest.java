package com.varlamoreuim.standin;

import net.runelite.api.coords.WorldPoint;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class DockTest
{
	@Test
	public void nearestPicksDockWithinRange()
	{
		assertEquals(Optional.of(Dock.SUNSET_COAST), Dock.nearest(new WorldPoint(1516, 2973, 0)));
		assertEquals(Optional.of(Dock.ALDARIN), Dock.nearest(new WorldPoint(1450, 2970, 0)));
		assertEquals(Optional.of(Dock.FORTIS_COTHON), Dock.nearest(new WorldPoint(1740, 3130, 0)));
	}

	@Test
	public void nearestIsEmptyFarAway()
	{
		assertFalse(Dock.nearest(new WorldPoint(3222, 3218, 0)).isPresent());
	}
}
