package com.varlamoreuim;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Varlamore UIM",
	description = "Varlamore area-lock restrictions for Ultimate Ironman accounts",
	tags = {"varlamore", "uim", "ironman", "region", "restriction"}
)
public class VarlamoreUimPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private VarlamoreUimConfig config;

	@Override
	protected void startUp() throws Exception
	{
		log.debug("Varlamore UIM plugin started");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.debug("Varlamore UIM plugin stopped");
	}

	@Provides
	VarlamoreUimConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(VarlamoreUimConfig.class);
	}
}
