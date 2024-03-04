package com.antimated.tasklist.tasks;

import com.antimated.tasklist.requirements.Requirement;
import com.antimated.tasklist.requirements.SkillRequirement;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Skill;
@Slf4j
public class AllSkillsTask extends Task
{
	public AllSkillsTask(int level)
	{
		super("this is my title", getTasks(level));
	}

	private static List<Requirement> getTasks(int level)
	{
		List<Requirement> requirements = new ArrayList<>();

		for (Skill skill : Skill.values())
		{
			log.debug(skill.getName());
			requirements.add(new SkillRequirement(skill, level));
			log.debug("Count {}", requirements.stream().count());
		}

		return requirements;
	}
}
