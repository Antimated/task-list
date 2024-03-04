package com.antimated.tasklist.requirements;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Skill;

@Slf4j
@RequiredArgsConstructor
@Getter
public class SkillRequirement implements Requirement
{
	private final Skill skill;

	private final int level;

	@Override
	public String toString()
	{
		return level + " " + skill.getName();
	}

	@Override
	public boolean satisfiesRequirement(Client client)
	{
		log.debug("Real skill level: {} - passed level: {}", client.getRealSkillLevel(skill), level);
		return client.getRealSkillLevel(skill) >= level;
	}
}
