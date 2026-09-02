package com.varlamoreuim.standin;

import lombok.Value;

/** A Talk-to click waiting for the player to arrive next to the stand-in. */
@Value
public class PendingTalk
{
	public static final int TIMEOUT_TICKS = 15;

	int npcIndex;
	int deadlineTick;

	public boolean isExpired(int tick)
	{
		return tick > deadlineTick;
	}
}
