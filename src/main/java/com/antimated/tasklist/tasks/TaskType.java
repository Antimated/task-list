package com.antimated.tasklist.tasks;

import com.antimated.tasklist.requirements.AllSkillLevelsRequirement;
import com.antimated.tasklist.requirements.AllSkillXpRequirement;
import com.antimated.tasklist.requirements.AnySkillLevelRequirement;
import com.antimated.tasklist.requirements.AnySkillXpRequirement;
import com.antimated.tasklist.requirements.Requirement;
import com.antimated.tasklist.requirements.SkillLevelRequirement;
import com.antimated.tasklist.requirements.SkillXpRequirement;
import com.antimated.tasklist.requirements.TotalLevelRequirement;
import com.antimated.tasklist.requirements.TotalXpRequirement;
import lombok.Getter;

@Getter
public enum TaskType
{
	ANY_SKILL_LEVEL("Any Skill Level", AnySkillLevelRequirement.class),
	ANY_SKILL_XP("Any Skill XP", AnySkillXpRequirement.class),
	SKILL_LEVEL("Skill Level", SkillLevelRequirement.class),
	SKILL_XP("Skill XP", SkillXpRequirement.class),
	TOTAL_LEVEL("Total Level", TotalLevelRequirement.class),
	TOTAL_XP("Total XP", TotalXpRequirement.class),
	ALL_SKILL_LEVELS("All Skill Levels", AllSkillLevelsRequirement.class),
	ALL_SKILL_XP("All Skill XP", AllSkillXpRequirement.class);

	private final String name;
	private final Class<? extends Requirement> requirementClass;

	TaskType(String name, Class<? extends Requirement> requirementClass)
	{
		this.name = name;
		this.requirementClass = requirementClass;
	}
}
