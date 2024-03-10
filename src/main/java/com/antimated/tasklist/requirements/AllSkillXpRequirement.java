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
