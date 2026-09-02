package com.varlamoreuim.dialogue;

/**
 * Pixel layout for the dialogue panel, mirroring the game's NPC (ChatLeft, 231)
 * and player (ChatRight, 217) dialogue interfaces. Calibrated in-game.
 */
final class DialogueLayout
{
	static final int HEAD_SIZE = 96;
	static final int HEAD_MARGIN_X = 16;
	static final int HEAD_Y = 8;
	static final int HEAD_ZOOM = 1500;
	static final int HEAD_ROTATION_X = 0;
	static final int HEAD_ROTATION_Y = 0;
	static final int HEAD_ROTATION_Z = 0;

	static final int NAME_Y = 16;
	static final int TEXT_Y = 44;
	static final int LINE_HEIGHT = 16;
	static final int CONTINUE_BOTTOM_MARGIN = 20;

	static final int COLOR_NAME = 0x8B0000;
	static final int COLOR_TEXT = 0x000000;
	static final int COLOR_CONTINUE = 0x0000FF;
	static final int COLOR_HOVER = 0xFFFFFF;

	private DialogueLayout()
	{
	}
}
