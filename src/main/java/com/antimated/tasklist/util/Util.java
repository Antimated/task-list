package com.antimated.tasklist.util;

import java.io.File;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.client.RuneLite;

@Slf4j
public class Util
{

	private static final File TASK_LIST_DIR = new File(RuneLite.RUNELITE_DIR, "task-list");

	/**
	 * Is player currently within the provided map regions
	 */
	public static boolean isPlayerWithinMapRegion(Client client, Set<Integer> definedMapRegions)
	{
		final int[] mapRegions = client.getMapRegions();

		for (int region : mapRegions)
		{
			if (definedMapRegions.contains(region))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns the integer of an ItemID field
	 * e.g. STEEL_FULL_HELM returns 1157
	 * @param fieldName String
	 * @return int
	 */
	public static int getItemID(String fieldName)
	{
		try
		{
			return (int) ItemID.class.getField(fieldName).get(null);
		}
		catch (NoSuchFieldException | IllegalAccessException e)
		{
			throw new IllegalArgumentException("Invalid item name: " + fieldName, e);
		}
	}

	public static File getPluginFolder(Client client)
	{
		File playerFolder;

		if (client.getLocalPlayer() != null && client.getLocalPlayer().getName() != null)
		{
			playerFolder = new File(TASK_LIST_DIR, client.getLocalPlayer().getName());
		}
		else
		{
			playerFolder = TASK_LIST_DIR;
		}

		if (playerFolder.mkdirs())
		{
			log.debug("Folder created at {}", playerFolder.getAbsolutePath());
		}

		return playerFolder;
	}
}
