package com.varlamoreuim.dialogue;

/**
 * Runtime facts a script may query. Implemented by the plugin against the
 * live client and by tests with stubs.
 */
public interface DialogueContext
{
	boolean hasDizanasQuiver();

	String playerName();
}
