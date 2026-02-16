package com.varlamoreuim;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldPoint;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class BoundaryChecker
{
	private final Set<Integer> varlamoreRegions = new HashSet<>();
	private final Set<Integer> quetzalWhitelistedRegions = new HashSet<>();
	private boolean loaded = false;

	/**
	 * Load Varlamore region data from the JSON resource file.
	 * Must be called during plugin initialization.
	 */
	public void loadRegions()
	{
		try (InputStream inputStream = getClass().getResourceAsStream("/varlamore_regions.json"))
		{
			if (inputStream == null)
			{
				log.error("Failed to load varlamore_regions.json: resource not found");
				return;
			}

			Gson gson = new Gson();
			JsonObject data = gson.fromJson(
				new InputStreamReader(inputStream, StandardCharsets.UTF_8),
				JsonObject.class
			);

			// Load main Varlamore regions
			data.getAsJsonArray("regions").forEach(element ->
				varlamoreRegions.add(element.getAsInt())
			);

			// Load Quetzal whitelist (internal transport regions)
			data.getAsJsonArray("quetzalWhitelist").forEach(element ->
				quetzalWhitelistedRegions.add(element.getAsInt())
			);

			loaded = true;
			log.debug("Loaded {} Varlamore regions", varlamoreRegions.size());
		}
		catch (Exception e)
		{
			log.error("Failed to load Varlamore region data", e);
		}
	}

	/**
	 * Check if a WorldPoint is inside Varlamore.
	 * O(1) HashSet lookup.
	 *
	 * @param location The WorldPoint to check
	 * @return true if the location is inside Varlamore, false otherwise
	 */
	public boolean isInVarlamore(WorldPoint location)
	{
		return varlamoreRegions.contains(location.getRegionID());
	}

	/**
	 * Check if a region ID is inside Varlamore.
	 * O(1) HashSet lookup.
	 *
	 * @param regionId The region ID to check
	 * @return true if the region is inside Varlamore, false otherwise
	 */
	public boolean isInVarlamore(int regionId)
	{
		return varlamoreRegions.contains(regionId);
	}

	/**
	 * Check if a region ID is whitelisted for internal Quetzal transport.
	 * Supports BNDRY-03 requirement for internal transportation within Varlamore.
	 *
	 * @param regionId The region ID to check
	 * @return true if the region is whitelisted for Quetzal transport, false otherwise
	 */
	public boolean isQuetzalWhitelisted(int regionId)
	{
		return quetzalWhitelistedRegions.contains(regionId);
	}

	/**
	 * Check if region data has been successfully loaded.
	 *
	 * @return true if loadRegions() completed successfully, false otherwise
	 */
	public boolean isLoaded()
	{
		return loaded;
	}

	/**
	 * Get the number of loaded Varlamore regions.
	 * Useful for verification and debugging.
	 *
	 * @return The count of regions in the Varlamore boundary
	 */
	public int getRegionCount()
	{
		return varlamoreRegions.size();
	}
}
