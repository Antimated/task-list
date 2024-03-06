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
public class AllSkillXpRequirement implements Requirement
{
	private final int xp;

	@Override
	public String toString()
	{
		return xp + " xp minimum in all skills";
	}

	@Override
	public boolean satisfiesRequirement(Client client)
	{
		for (Skill s : Skill.values())
		{
			if (client.getSkillExperience(s) < xp)
			{
				return false;
			}
		}

		return true;
	}
}
