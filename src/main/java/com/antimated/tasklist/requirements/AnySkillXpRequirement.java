package com.antimated.tasklist.requirements;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Skill;

@Slf4j
@RequiredArgsConstructor
@Getter
public class AnySkillXpRequirement implements Requirement
{
	private final Skill skill;

	private final int xp;

	@Override
	public String toString()
	{
		return "Any skill xp " + xp;
	}

	@Override
	public boolean satisfiesRequirement(Client client)
	{
		for (Skill s : Skill.values())
		{
			if (client.getSkillExperience(s) >= xp)
			{
				return true;
			}
		}

		return false;
	}
}
