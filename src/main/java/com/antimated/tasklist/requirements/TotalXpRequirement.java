package com.antimated.tasklist.requirements;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Skill;

@Slf4j
@Data
public class TotalXpRequirement implements Requirement
{
	private final int xp;

	/**
	 * Gets the Total XP from the client
	 * @param client Client
	 * @return long
	 */
	private static long getTotalXp(Client client)
	{
		long total = 0;

		for (Skill skill : Skill.values())
		{
			total += client.getSkillExperience(skill);
		}

		return total;
	}

	@Override
	public boolean satisfiesRequirement(Client client)
	{
		return getTotalXp(client) >= xp;
	}
}
