package com.antimated.tasklist.requirements;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Skill;

@Slf4j
@RequiredArgsConstructor
@Getter
public class TotalXpRequirement implements Requirement
{
	private final int totalXp;

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
		return getTotalXp(client) >= totalXp;
	}
}
