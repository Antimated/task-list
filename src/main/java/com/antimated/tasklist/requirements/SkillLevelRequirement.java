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
public class SkillLevelRequirement implements Requirement
{
	private final Skill skill;

	private final int level;

	@Override
	public boolean satisfiesRequirement(Client client)
	{
		return client.getRealSkillLevel(skill) >= level;
	}
}
