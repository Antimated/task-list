package com.antimated.tasklist.requirements;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Experience;
import net.runelite.api.Skill;

@Slf4j
@Getter
@RequiredArgsConstructor
@ToString
public class AllSkillXpRequirement implements Requirement
{
	private final int xp;

	@Override
	public boolean satisfiesRequirement(Client client)
	{
		for (Skill skill : Skill.values())
		{
			// Ignore hitpoints skill when experience is below the xp for lvl 10
			if (skill.equals(Skill.HITPOINTS) && xp < Experience.getXpForLevel(10))
			{
				continue;
			}

			if (client.getSkillExperience(skill) < xp)
			{
				return false;
			}
		}

		return true;
	}
}
