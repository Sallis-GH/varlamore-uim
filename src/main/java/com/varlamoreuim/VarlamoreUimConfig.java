package com.varlamoreuim;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("varlamoreuim")
public interface VarlamoreUimConfig extends Config
{
	@ConfigItem(
		keyName = "pluginEnabled",
		name = "Enable Plugin",
		description = "Enable or disable Varlamore UIM restrictions",
		position = 0
	)
	default boolean pluginEnabled()
	{
		return true;
	}

	// Restrictions Section
	@ConfigSection(
		name = "Restrictions",
		description = "Boundary and travel restrictions",
		position = 1
	)
	String restrictionsSection = "restrictions";

	@ConfigItem(
		keyName = "boundaryEnabled",
		name = "Enforce Boundary",
		description = "Block travel outside Varlamore region",
		position = 2,
		section = restrictionsSection
	)
	default boolean boundaryEnabled()
	{
		return true;
	}

	@ConfigItem(
		keyName = "blockSpellTeleports",
		name = "Block Spell Teleports",
		description = "Prevent casting teleport spells that leave Varlamore",
		position = 3,
		section = restrictionsSection
	)
	default boolean blockSpellTeleports()
	{
		return true;
	}

	// Quality of Adventure Section (placeholder)
	@ConfigSection(
		name = "Quality of Adventure",
		description = "QoA features and enhancements",
		position = 10,
		closedByDefault = true
	)
	String qoaSection = "qoa";

	// Progress Tracking Section (placeholder)
	@ConfigSection(
		name = "Progress Tracking",
		description = "Track unlocks and milestones",
		position = 20,
		closedByDefault = true
	)
	String trackingSection = "tracking";

	// Unlocks Section (placeholder)
	@ConfigSection(
		name = "Unlocks",
		description = "Milestone-based travel unlocks",
		position = 30,
		closedByDefault = true
	)
	String unlocksSection = "unlocks";
}
