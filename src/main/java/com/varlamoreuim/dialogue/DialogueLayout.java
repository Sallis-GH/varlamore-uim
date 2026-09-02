package com.varlamoreuim.dialogue;

/**
 * Pixel layout for the dialogue panel, copied from the game's own NPC (ChatLeft, 231)
 * and player (ChatRight, 217) dialogue interfaces as logged in-game on 2026-09-02.
 *
 * The game lays out a 97px tall block (name, text, continue) and a 32x32 model widget
 * whose chathead deliberately overflows its bounds; zoom 796 with a 40 unit X tilt is
 * what makes the head render at roughly 90px. The block is centred vertically in the
 * message layer. X positions are given for a 486px wide content area and the right
 * margins are applied relative to the actual container width.
 */
final class DialogueLayout
{
	static final int BLOCK_HEIGHT = 97;

	static final int HEAD_SIZE = 32;
	static final int HEAD_X_NPC = 35;
	/** Player head sits this far from the right edge (486 - 415). */
	static final int HEAD_RIGHT_OFFSET_PLAYER = 71;
	static final int HEAD_Y = 43;
	static final int HEAD_ZOOM = 796;
	static final int HEAD_ROTATION_X = 40;
	static final int HEAD_ROTATION_Y = 0;
	static final int HEAD_ROTATION_Z_NPC = 1882;
	static final int HEAD_ROTATION_Z_PLAYER = 166;

	static final int TEXT_X_NPC = 96;
	static final int TEXT_X_PLAYER = 5;
	/** 486 - 96 - 380. */
	static final int TEXT_RIGHT_MARGIN_NPC = 10;
	/** 486 - 5 - 380. */
	static final int TEXT_RIGHT_MARGIN_PLAYER = 101;

	static final int NAME_Y = 0;
	static final int NAME_HEIGHT = 17;
	static final int TEXT_Y = 16;
	static final int TEXT_HEIGHT = 67;
	static final int CONTINUE_Y = 80;
	static final int CONTINUE_HEIGHT = 17;

	/** Options page: title height, gap to the first option, and per-option step (game: ~23px). */
	static final int OPTION_TITLE_HEIGHT = 17;
	static final int OPTION_TITLE_GAP = 5;
	static final int OPTION_STEP = 23;

	static final int COLOR_NAME = 0x800000;
	static final int COLOR_TEXT = 0x000000;
	static final int COLOR_CONTINUE = 0x0000FF;
	static final int COLOR_HOVER = 0xFFFFFF;

	private DialogueLayout()
	{
	}
}
