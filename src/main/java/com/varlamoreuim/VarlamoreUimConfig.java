package com.varlamoreuim;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("varlamoreuim")
public interface VarlamoreUimConfig extends Config
{
	@ConfigItem(
		keyName = "pluginEnabled",
		name = "Enable Plugin",
		description = "Enable or disable Varlamore UIM restrictions"
	)
	default boolean enabled()
	{
		return true;
	}
}
