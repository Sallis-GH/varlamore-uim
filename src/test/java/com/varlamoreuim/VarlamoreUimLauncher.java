package com.varlamoreuim;

import java.lang.reflect.Method;

/**
 * Entry point for the fat jar. RuneLite's {@code ExternalPluginManager.loadBuiltin}
 * refuses to run unless assertions are enabled, so this turns them on for the
 * system class loader before any RuneLite class is loaded, then hands over to
 * {@link VarlamoreUimPluginTest}. Lets testers run {@code java -jar} without {@code -ea}.
 */
public final class VarlamoreUimLauncher
{
	private VarlamoreUimLauncher()
	{
	}

	public static void main(String[] args) throws Exception
	{
		ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
		Class<?> test = Class.forName("com.varlamoreuim.VarlamoreUimPluginTest");
		Method main = test.getMethod("main", String[].class);
		main.invoke(null, (Object) args);
	}
}
