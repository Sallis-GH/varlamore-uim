package com.varlamoreuim;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class VarlamoreUimPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(VarlamoreUimPlugin.class);
		RuneLite.main(args);
	}
}
