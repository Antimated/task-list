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
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.util.RuntimeTypeAdapterFactory;

@Slf4j
public class Util
{
	public static final Set<Integer> LAST_MAN_STANDING_REGIONS = ImmutableSet.of(13658, 13659, 13660, 13914, 13915, 13916, 13918, 13919, 13920, 14174, 14175, 14176, 14430, 14431, 14432);

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
			.registerSubtype(AllSkillLevelsRequirement.class)
			.registerSubtype(AllSkillXpRequirement.class)
			.registerSubtype(AnySkillLevelRequirement.class)
			.registerSubtype(AnySkillXpRequirement.class)
			.registerSubtype(CombatLevelRequirement.class)
			.registerSubtype(EquipmentRequirement.class)
			.registerSubtype(OrRequirement.class)
			.registerSubtype(PrayerRequirement.class)
			.registerSubtype(QuestPointRequirement.class)
			.registerSubtype(QuestRequirement.class)
			.registerSubtype(SkillLevelRequirement.class)
			.registerSubtype(SkillXpRequirement.class)
			.registerSubtype(TotalLevelRequirement.class)
			.registerSubtype(TotalXpRequirement.class);

	}
}
