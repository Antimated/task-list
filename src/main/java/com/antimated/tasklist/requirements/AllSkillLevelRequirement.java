package com.antimated.tasklist.requirements;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Skill;

@Slf4j
@RequiredArgsConstructor
@Getter
public class AllSkillLevelRequirement implements Requirement
{
	private final int level;

	@Override
	public String toString()
	{
		return "Level " + level + " minimum in all skills";
	}


	@Override
	public boolean satisfiesRequirement(Client client)
	{
		for (Skill skill : Skill.values())
		{
			if (client.getRealSkillLevel(skill) < level)
			{
				return false;
			}
		}
		
		return true;
	}
}
