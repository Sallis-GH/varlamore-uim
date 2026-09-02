package com.varlamoreuim.standin;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PendingTalkTest
{
	@Test
	public void expiresAfterDeadline()
	{
		PendingTalk p = new PendingTalk(7, 100);
		assertFalse(p.isExpired(99));
		assertFalse(p.isExpired(100));
		assertTrue(p.isExpired(101));
	}
}
