package com.antimated.tasklist.requirements;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Skill;

@Slf4j
@RequiredArgsConstructor
@Getter
public class SkillXpRequirement implements Requirement
{
	private final Skill skill;

	private final int xp;

	private final boolean anyXp;

	@Override
	public String toString()
	{
		return anyXp ? "Any skill xp " + xp : xp + " " + skill.getName();
	}

	@Override
	public boolean satisfiesRequirement(Client client)
	{
		// Check if there is any skill with the required xp
		if (anyXp)
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

		return client.getSkillExperience(skill) >= xp;
	}
}
