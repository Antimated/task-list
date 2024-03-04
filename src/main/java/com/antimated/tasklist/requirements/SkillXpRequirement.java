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

	@Override
	public String toString()
	{
		return xp + " " + skill.getName();
	}

	@Override
	public boolean satisfiesRequirement(Client client)
	{
		log.debug("Real skill experience: {} - passed experience: {}", client.getSkillExperience(skill), xp);
		return client.getSkillExperience(skill) >= xp;
	}
}
