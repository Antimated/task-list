package com.antimated.tasklist.tasks.lists;

import com.antimated.tasklist.requirements.AnySkillLevelRequirement;
import com.antimated.tasklist.requirements.BaseSkillLevelRequirement;
import com.antimated.tasklist.requirements.SkillLevelRequirement;
import com.antimated.tasklist.tasks.TaskTier;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Skill;

@Slf4j
@ToString
public class LevelTaskList extends TaskList
{
	public LevelTaskList()
	{
		super("Level tasks");

		// Achieve your First Level x
		add("Achieve your First Level Up", TaskTier.EASY, new AnySkillLevelRequirement(2));
		add("Achieve your First Level 5", TaskTier.EASY, new AnySkillLevelRequirement(5));
		add("Achieve your First Level 10", TaskTier.EASY, new AnySkillLevelRequirement(10));
		add("Achieve your First Level 20", TaskTier.EASY, new AnySkillLevelRequirement(20));
		add("Achieve your First Level 30", TaskTier.MEDIUM, new AnySkillLevelRequirement(30));
		add("Achieve your First Level 40", TaskTier.MEDIUM, new AnySkillLevelRequirement(40));
		add("Achieve your First Level 50", TaskTier.MEDIUM, new AnySkillLevelRequirement(50));
		add("Achieve your First Level 60", TaskTier.HARD, new AnySkillLevelRequirement(60));
		add("Achieve your First Level 70", TaskTier.HARD, new AnySkillLevelRequirement(70));
		add("Achieve your First Level 80", TaskTier.HARD, new AnySkillLevelRequirement(80));
		add("Achieve your First Level 90", TaskTier.ELITE, new AnySkillLevelRequirement(90));
		add("Achieve your First Level 95", TaskTier.ELITE, new AnySkillLevelRequirement(95));
		add("Achieve your First Level 99", TaskTier.MASTER, new AnySkillLevelRequirement(99));

		// Reach Base Level
		add("Reach Base Level 5", TaskTier.EASY, new BaseSkillLevelRequirement(5));
		add("Reach Base Level 10", TaskTier.EASY, new BaseSkillLevelRequirement(10));
		add("Reach Base Level 20", TaskTier.EASY, new BaseSkillLevelRequirement(20));
		add("Reach Base Level 30", TaskTier.MEDIUM, new BaseSkillLevelRequirement(30));
		add("Reach Base Level 40", TaskTier.MEDIUM, new BaseSkillLevelRequirement(40));
		add("Reach Base Level 50", TaskTier.MEDIUM, new BaseSkillLevelRequirement(50));
		add("Reach Base Level 60", TaskTier.HARD, new BaseSkillLevelRequirement(60));
		add("Reach Base Level 70", TaskTier.HARD, new BaseSkillLevelRequirement(70));
		add("Reach Base Level 80", TaskTier.ELITE, new BaseSkillLevelRequirement(80));
		add("Reach Base Level 90", TaskTier.ELITE, new BaseSkillLevelRequirement(90));

		// Reach Level 99 in skill
		add("Reach Level 99 Attack", TaskTier.MASTER, new SkillLevelRequirement(Skill.ATTACK, 99));
		add("Reach Level 99 Defence", TaskTier.MASTER, new SkillLevelRequirement(Skill.DEFENCE, 99));
		add("Reach Level 99 Strength", TaskTier.MASTER, new SkillLevelRequirement(Skill.STRENGTH, 99));
		add("Reach Level 99 Hitpoints", TaskTier.MASTER, new SkillLevelRequirement(Skill.HITPOINTS, 99));
		add("Reach Level 99 Ranged", TaskTier.MASTER, new SkillLevelRequirement(Skill.RANGED, 99));
		add("Reach Level 99 Prayer", TaskTier.MASTER, new SkillLevelRequirement(Skill.PRAYER, 99));
		add("Reach Level 99 Magic", TaskTier.MASTER, new SkillLevelRequirement(Skill.MAGIC, 99));
		add("Reach Level 99 Cooking", TaskTier.MASTER, new SkillLevelRequirement(Skill.COOKING, 99));
		add("Reach Level 99 Woodcutting", TaskTier.MASTER, new SkillLevelRequirement(Skill.WOODCUTTING, 99));
		add("Reach Level 99 Fletching", TaskTier.MASTER, new SkillLevelRequirement(Skill.FLETCHING, 99));
		add("Reach Level 99 Fishing", TaskTier.MASTER, new SkillLevelRequirement(Skill.FISHING, 99));
		add("Reach Level 99 Firemaking", TaskTier.MASTER, new SkillLevelRequirement(Skill.FIREMAKING, 99));
		add("Reach Level 99 Crafting", TaskTier.MASTER, new SkillLevelRequirement(Skill.CRAFTING, 99));
		add("Reach Level 99 Smithing", TaskTier.MASTER, new SkillLevelRequirement(Skill.SMITHING, 99));
		add("Reach Level 99 Mining", TaskTier.MASTER, new SkillLevelRequirement(Skill.MINING, 99));
		add("Reach Level 99 Herblore", TaskTier.MASTER, new SkillLevelRequirement(Skill.HERBLORE, 99));
		add("Reach Level 99 Agility", TaskTier.MASTER, new SkillLevelRequirement(Skill.AGILITY, 99));
		add("Reach Level 99 Thieving", TaskTier.MASTER, new SkillLevelRequirement(Skill.THIEVING, 99));
		add("Reach Level 99 Slayer", TaskTier.MASTER, new SkillLevelRequirement(Skill.SLAYER, 99));
		add("Reach Level 99 Farming", TaskTier.MASTER, new SkillLevelRequirement(Skill.FARMING, 99));
		add("Reach Level 99 Runecraft", TaskTier.MASTER, new SkillLevelRequirement(Skill.RUNECRAFT, 99));
		add("Reach Level 99 Hunter", TaskTier.MASTER, new SkillLevelRequirement(Skill.HUNTER, 99));
		add("Reach Level 99 Construction", TaskTier.MASTER, new SkillLevelRequirement(Skill.CONSTRUCTION, 99));

		log.debug("Loaded {} tasks...", getTasks().size());
	}
}
