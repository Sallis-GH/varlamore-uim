package com.varlamoreuim.dialogue;

import lombok.Value;

import javax.annotation.Nullable;
import java.util.function.Predicate;

/**
 * One entry of an {@link Options} page. A conditional option evaluates its
 * predicate on selection and routes to {@code next} or {@code elseNext}.
 */
@Value
public class Option
{
	String label;
	String next;
	@Nullable
	Predicate<DialogueContext> condition;
	@Nullable
	String elseNext;

	public static Option of(String label, String next)
	{
		return new Option(label, next, null, null);
	}

	public static Option conditional(String label, Predicate<DialogueContext> condition, String next, String elseNext)
	{
		return new Option(label, next, condition, elseNext);
	}

	public String resolve(DialogueContext ctx)
	{
		if (condition == null)
		{
			return next;
		}
		return condition.test(ctx) ? next : elseNext;
	}
}
