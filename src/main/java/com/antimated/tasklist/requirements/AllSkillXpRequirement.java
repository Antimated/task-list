package com.antimated.tasklist.requirements;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Skill;

@Slf4j
@Data
public class AllSkillXpRequirement implements Requirement
{
	private final int xp;

	@Override
	public boolean satisfiesRequirement(Client client)
	{
		for (Skill skill : Skill.values())
		{
			if (client.getSkillExperience(skill) < xp)
			{
				return false;
			}
		}

		return true;
	}
}
