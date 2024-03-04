package com.antimated.tasklist.requirements;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Skill;

@Slf4j
@RequiredArgsConstructor
@Getter
public class SkillLevelRequirement implements Requirement
{
	private final Skill skill;

	private final int level;

	private final boolean anySkill;

	@Override
	public String toString()
	{
		return anySkill ? "Any skill level " + level : level + " " + skill.getName();
	}

	@Override
	public boolean satisfiesRequirement(Client client)
	{
		if (anySkill)
		{
			// Check if there is any skill with the required level
			for (Skill s : Skill.values())
			{
				if (client.getRealSkillLevel(s) >= level)
				{
					return true;
				}
			}
			return false;
		}

		return client.getRealSkillLevel(skill) >= level;
	}
}
