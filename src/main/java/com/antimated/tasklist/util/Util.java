package com.antimated.tasklist.util;

import com.antimated.tasklist.requirements.AllSkillLevelsRequirement;
import com.antimated.tasklist.requirements.AllSkillXpRequirement;
import com.antimated.tasklist.requirements.AnySkillLevelRequirement;
import com.antimated.tasklist.requirements.AnySkillXpRequirement;
import com.antimated.tasklist.requirements.CombatLevelRequirement;
import com.antimated.tasklist.requirements.EquipmentRequirement;
import com.antimated.tasklist.requirements.OrRequirement;
import com.antimated.tasklist.requirements.PrayerRequirement;
import com.antimated.tasklist.requirements.QuestPointRequirement;
import com.antimated.tasklist.requirements.QuestRequirement;
import com.antimated.tasklist.requirements.Requirement;
import com.antimated.tasklist.requirements.SkillLevelRequirement;
import com.antimated.tasklist.requirements.SkillXpRequirement;
import com.antimated.tasklist.requirements.TotalLevelRequirement;
import com.antimated.tasklist.requirements.TotalXpRequirement;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.util.RuntimeTypeAdapterFactory;

@Slf4j
public class Util
{
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

	public static RuntimeTypeAdapterFactory<Requirement> requirementAdapterFactory()
	{
		return RuntimeTypeAdapterFactory
			.of(Requirement.class, "type")
			.registerSubtype(AllSkillLevelsRequirement.class, "AllSkillLevelsRequirement")
			.registerSubtype(AllSkillXpRequirement.class, "AllSkillXpRequirement")
			.registerSubtype(AnySkillLevelRequirement.class, "AnySkillLevelRequirement")
			.registerSubtype(AnySkillXpRequirement.class, "AnySkillXpRequirement")
			.registerSubtype(CombatLevelRequirement.class, "CombatLevelRequirement")
			.registerSubtype(EquipmentRequirement.class, "EquipmentRequirement")
			.registerSubtype(OrRequirement.class, "OrRequirement")
			.registerSubtype(PrayerRequirement.class, "PrayerRequirement")
			.registerSubtype(QuestPointRequirement.class, "QuestPointRequirement")
			.registerSubtype(QuestRequirement.class, "QuestRequirement")
			.registerSubtype(SkillLevelRequirement.class, "SkillLevelRequirement")
			.registerSubtype(SkillXpRequirement.class, "SkillXpRequirement")
			.registerSubtype(TotalLevelRequirement.class, "TotalLevelRequirement")
			.registerSubtype(TotalXpRequirement.class, "TotalXpRequirement");

	}
}
