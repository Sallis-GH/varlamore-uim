package com.varlamoreuim.dialogue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Chathead animation per mood. Each has a talking animation shown while the
 * page is fresh and a still animation once it has been shown. Ids are the
 * standard OSRS chathead animations; verify in-game (Task 3 calibration).
 */
@Getter
@RequiredArgsConstructor
public enum Expression
{
	DEFAULT(567, 588),
	HAPPY(567, 588),
	SAD(610, 588),
	ANGRY(614, 588),
	LAUGH(605, 588);

	private final int talkAnimationId;
	private final int stillAnimationId;
}
