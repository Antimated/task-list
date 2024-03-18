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
public class AnySkillLevelRequirement implements Requirement
{
	private final int level;

	@Override
	public boolean satisfiesRequirement(Client client)
	{
		for (Skill skill : Skill.values())
		{
			// Ignore hitpoints as the minimum level for hitpoints is level 10
			// else not all skill requirement could be completed from lvl 1 - 9
			if (skill.equals(Skill.HITPOINTS) && level <= 10)
			{
				continue;
			}

			if (client.getRealSkillLevel(skill) >= level)
			{
				return true;
			}
		}

		return false;
	}
}
