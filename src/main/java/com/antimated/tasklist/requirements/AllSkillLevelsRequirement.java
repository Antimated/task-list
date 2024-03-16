package com.antimated.tasklist.requirements;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Skill;

@Slf4j
@Getter
@RequiredArgsConstructor
@ToString
public class AllSkillLevelsRequirement implements Requirement
{
	private final int level;

	@Override
	public boolean satisfiesRequirement(Client client)
	{
		for (Skill skill : Skill.values())
		{
			log.debug("AllSkillLevelsRequirement with client.getRealSkillLevel(skill) {} < level {}", client.getRealSkillLevel(skill), level);
			if (client.getRealSkillLevel(skill) < level)
			{
				return false;
			}
		}

		return true;
	}
}
