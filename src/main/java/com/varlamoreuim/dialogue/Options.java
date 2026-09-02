package com.varlamoreuim.dialogue;

import lombok.Value;

import java.util.List;

/** A "Select an option" page. */
@Value
public class Options implements DialoguePage
{
	String title;
	List<Option> options;
}
