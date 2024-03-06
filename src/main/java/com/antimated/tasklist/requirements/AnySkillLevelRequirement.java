package com.antimated.tasklist.requirements;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Skill;

@Slf4j
@RequiredArgsConstructor
@Getter
public class AnySkillLevelRequirement implements Requirement
{
	private final int level;

	@Override
	public String toString()
	{
		return "Any skill level " + level;
	}

	@Override
	public boolean satisfiesRequirement(Client client)
	{
		for (Skill s : Skill.values())
		{
			if (client.getRealSkillLevel(s) >= level)
			{
				return true;
			}
		}

		return false;
	}
}
