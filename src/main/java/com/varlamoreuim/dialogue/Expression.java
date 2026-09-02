package com.varlamoreuim.dialogue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Chathead animation per mood: the talking animation played while the page is
 * shown. Ids are the standard OSRS chathead animations; verify in-game
 * (Task 3 calibration).
 */
@Getter
@RequiredArgsConstructor
public enum Expression
{
	DEFAULT(567),
	HAPPY(567),
	SAD(610),
	ANGRY(614),
	LAUGH(605);

	private final int talkAnimationId;
}
