package com.antimated.tasklist.tasks;

import com.antimated.tasklist.requirements.AllSkillLevelsRequirement;
import com.antimated.tasklist.requirements.AllSkillXpRequirement;
import com.antimated.tasklist.requirements.AnySkillLevelRequirement;
import com.antimated.tasklist.requirements.AnySkillXpRequirement;
import com.antimated.tasklist.requirements.CombatLevelRequirement;
import com.antimated.tasklist.requirements.QuestPointRequirement;
import com.antimated.tasklist.requirements.Requirement;
import com.antimated.tasklist.requirements.SkillLevelRequirement;
import com.antimated.tasklist.requirements.SkillXpRequirement;
import com.antimated.tasklist.requirements.TotalLevelRequirement;
import com.antimated.tasklist.requirements.TotalXpRequirement;
import lombok.Getter;

@Getter
public enum TaskType
{
	ANY_SKILL_LEVEL("Any skill level", AnySkillLevelRequirement.class),
	ANY_SKILL_XP("Any skill XP", AnySkillXpRequirement.class),
	SKILL_LEVEL("Skill level", SkillLevelRequirement.class),
	SKILL_XP("Skill XP", SkillXpRequirement.class),
	TOTAL_LEVEL("Total level", TotalLevelRequirement.class),
	TOTAL_XP("Total XP", TotalXpRequirement.class),
	ALL_SKILL_LEVELS("All skill levels", AllSkillLevelsRequirement.class),
	ALL_SKILL_XP("All skill XP", AllSkillXpRequirement.class),
	COMBAT_LEVEL("Combat level", CombatLevelRequirement.class),
	QUEST_POINTS("Quest points", QuestPointRequirement.class);

	private final String name;
	private final Class<? extends Requirement> requirementClass;

	TaskType(String name, Class<? extends Requirement> requirementClass)
	{
		this.name = name;
		this.requirementClass = requirementClass;
	}
}
