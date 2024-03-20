package com.antimated.tasklist.tasks;

import com.antimated.tasklist.requirements.SkillXpRequirement;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Skill;

@Slf4j
public class XpTaskList extends TaskList
{
	public XpTaskList()
	{
		super("XP Tasks");

		add(new Task("Obtain 25 Million Attack XP", TaskTier.ELITE, new SkillXpRequirement(Skill.ATTACK, 25000000)));
		add(new Task("Obtain 35 Million Attack XP", TaskTier.ELITE, new SkillXpRequirement(Skill.ATTACK, 35000000)));
		add(new Task("Obtain 50 Million Attack XP", TaskTier.ELITE, new SkillXpRequirement(Skill.ATTACK, 50000000)));
		add(new Task("Obtain 100 Million Attack XP", TaskTier.MASTER, new SkillXpRequirement(Skill.ATTACK, 100000000)));
		add(new Task("Obtain 200 Million Attack XP", TaskTier.MASTER, new SkillXpRequirement(Skill.ATTACK, 200000000)));

		add(new Task("Obtain 25 Million Defence XP", TaskTier.ELITE, new SkillXpRequirement(Skill.DEFENCE, 25000000)));
		add(new Task("Obtain 35 Million Defence XP", TaskTier.ELITE, new SkillXpRequirement(Skill.DEFENCE, 35000000)));
		add(new Task("Obtain 50 Million Defence XP", TaskTier.ELITE, new SkillXpRequirement(Skill.DEFENCE, 50000000)));
		add(new Task("Obtain 100 Million Defence XP", TaskTier.MASTER, new SkillXpRequirement(Skill.DEFENCE, 100000000)));
		add(new Task("Obtain 200 Million Defence XP", TaskTier.MASTER, new SkillXpRequirement(Skill.DEFENCE, 200000000)));

		add(new Task("Obtain 25 Million Strength XP", TaskTier.ELITE, new SkillXpRequirement(Skill.STRENGTH, 25000000)));
		add(new Task("Obtain 35 Million Strength XP", TaskTier.ELITE, new SkillXpRequirement(Skill.STRENGTH, 35000000)));
		add(new Task("Obtain 50 Million Strength XP", TaskTier.ELITE, new SkillXpRequirement(Skill.STRENGTH, 50000000)));
		add(new Task("Obtain 100 Million Strength XP", TaskTier.MASTER, new SkillXpRequirement(Skill.STRENGTH, 100000000)));
		add(new Task("Obtain 200 Million Strength XP", TaskTier.MASTER, new SkillXpRequirement(Skill.STRENGTH, 200000000)));

		add(new Task("Obtain 25 Million Hitpoints XP", TaskTier.ELITE, new SkillXpRequirement(Skill.HITPOINTS, 25000000)));
		add(new Task("Obtain 35 Million Hitpoints XP", TaskTier.ELITE, new SkillXpRequirement(Skill.HITPOINTS, 35000000)));
		add(new Task("Obtain 50 Million Hitpoints XP", TaskTier.ELITE, new SkillXpRequirement(Skill.HITPOINTS, 50000000)));
		add(new Task("Obtain 100 Million Hitpoints XP", TaskTier.MASTER, new SkillXpRequirement(Skill.HITPOINTS, 100000000)));
		add(new Task("Obtain 200 Million Hitpoints XP", TaskTier.MASTER, new SkillXpRequirement(Skill.HITPOINTS, 200000000)));

		add(new Task("Obtain 25 Million Ranged XP", TaskTier.ELITE, new SkillXpRequirement(Skill.RANGED, 25000000)));
		add(new Task("Obtain 35 Million Ranged XP", TaskTier.ELITE, new SkillXpRequirement(Skill.RANGED, 35000000)));
		add(new Task("Obtain 50 Million Ranged XP", TaskTier.ELITE, new SkillXpRequirement(Skill.RANGED, 50000000)));
		add(new Task("Obtain 100 Million Ranged XP", TaskTier.MASTER, new SkillXpRequirement(Skill.RANGED, 100000000)));
		add(new Task("Obtain 200 Million Ranged XP", TaskTier.MASTER, new SkillXpRequirement(Skill.RANGED, 200000000)));

		add(new Task("Obtain 25 Million Prayer XP", TaskTier.ELITE, new SkillXpRequirement(Skill.PRAYER, 25000000)));
		add(new Task("Obtain 35 Million Prayer XP", TaskTier.ELITE, new SkillXpRequirement(Skill.PRAYER, 35000000)));
		add(new Task("Obtain 50 Million Prayer XP", TaskTier.ELITE, new SkillXpRequirement(Skill.PRAYER, 50000000)));
		add(new Task("Obtain 100 Million Prayer XP", TaskTier.MASTER, new SkillXpRequirement(Skill.PRAYER, 100000000)));
		add(new Task("Obtain 200 Million Prayer XP", TaskTier.MASTER, new SkillXpRequirement(Skill.PRAYER, 200000000)));

		add(new Task("Obtain 25 Million Magic XP", TaskTier.ELITE, new SkillXpRequirement(Skill.MAGIC, 25000000)));
		add(new Task("Obtain 35 Million Magic XP", TaskTier.ELITE, new SkillXpRequirement(Skill.MAGIC, 35000000)));
		add(new Task("Obtain 50 Million Magic XP", TaskTier.ELITE, new SkillXpRequirement(Skill.MAGIC, 50000000)));
		add(new Task("Obtain 100 Million Magic XP", TaskTier.MASTER, new SkillXpRequirement(Skill.MAGIC, 100000000)));
		add(new Task("Obtain 200 Million Magic XP", TaskTier.MASTER, new SkillXpRequirement(Skill.MAGIC, 200000000)));

		add(new Task("Obtain 25 Million Cooking XP", TaskTier.ELITE, new SkillXpRequirement(Skill.COOKING, 25000000)));
		add(new Task("Obtain 35 Million Cooking XP", TaskTier.ELITE, new SkillXpRequirement(Skill.COOKING, 35000000)));
		add(new Task("Obtain 50 Million Cooking XP", TaskTier.ELITE, new SkillXpRequirement(Skill.COOKING, 50000000)));
		add(new Task("Obtain 100 Million Cooking XP", TaskTier.MASTER, new SkillXpRequirement(Skill.COOKING, 100000000)));
		add(new Task("Obtain 200 Million Cooking XP", TaskTier.MASTER, new SkillXpRequirement(Skill.COOKING, 200000000)));

		add(new Task("Obtain 25 Million Woodcutting XP", TaskTier.ELITE, new SkillXpRequirement(Skill.WOODCUTTING, 25000000)));
		add(new Task("Obtain 35 Million Woodcutting XP", TaskTier.ELITE, new SkillXpRequirement(Skill.WOODCUTTING, 35000000)));
		add(new Task("Obtain 50 Million Woodcutting XP", TaskTier.ELITE, new SkillXpRequirement(Skill.WOODCUTTING, 50000000)));
		add(new Task("Obtain 100 Million Woodcutting XP", TaskTier.MASTER, new SkillXpRequirement(Skill.WOODCUTTING, 100000000)));
		add(new Task("Obtain 200 Million Woodcutting XP", TaskTier.MASTER, new SkillXpRequirement(Skill.WOODCUTTING, 200000000)));

		add(new Task("Obtain 25 Million Fletching XP", TaskTier.ELITE, new SkillXpRequirement(Skill.FLETCHING, 25000000)));
		add(new Task("Obtain 35 Million Fletching XP", TaskTier.ELITE, new SkillXpRequirement(Skill.FLETCHING, 35000000)));
		add(new Task("Obtain 50 Million Fletching XP", TaskTier.ELITE, new SkillXpRequirement(Skill.FLETCHING, 50000000)));
		add(new Task("Obtain 100 Million Fletching XP", TaskTier.MASTER, new SkillXpRequirement(Skill.FLETCHING, 100000000)));
		add(new Task("Obtain 200 Million Fletching XP", TaskTier.MASTER, new SkillXpRequirement(Skill.FLETCHING, 200000000)));

		add(new Task("Obtain 25 Million Fishing XP", TaskTier.ELITE, new SkillXpRequirement(Skill.FISHING, 25000000)));
		add(new Task("Obtain 35 Million Fishing XP", TaskTier.ELITE, new SkillXpRequirement(Skill.FISHING, 35000000)));
		add(new Task("Obtain 50 Million Fishing XP", TaskTier.ELITE, new SkillXpRequirement(Skill.FISHING, 50000000)));
		add(new Task("Obtain 100 Million Fishing XP", TaskTier.MASTER, new SkillXpRequirement(Skill.FISHING, 100000000)));
		add(new Task("Obtain 200 Million Fishing XP", TaskTier.MASTER, new SkillXpRequirement(Skill.FISHING, 200000000)));

		add(new Task("Obtain 25 Million Firemaking XP", TaskTier.ELITE, new SkillXpRequirement(Skill.FIREMAKING, 25000000)));
		add(new Task("Obtain 35 Million Firemaking XP", TaskTier.ELITE, new SkillXpRequirement(Skill.FIREMAKING, 35000000)));
		add(new Task("Obtain 50 Million Firemaking XP", TaskTier.ELITE, new SkillXpRequirement(Skill.FIREMAKING, 50000000)));
		add(new Task("Obtain 100 Million Firemaking XP", TaskTier.MASTER, new SkillXpRequirement(Skill.FIREMAKING, 100000000)));
		add(new Task("Obtain 200 Million Firemaking XP", TaskTier.MASTER, new SkillXpRequirement(Skill.FIREMAKING, 200000000)));

		add(new Task("Obtain 25 Million Crafting XP", TaskTier.ELITE, new SkillXpRequirement(Skill.CRAFTING, 25000000)));
		add(new Task("Obtain 35 Million Crafting XP", TaskTier.ELITE, new SkillXpRequirement(Skill.CRAFTING, 35000000)));
		add(new Task("Obtain 50 Million Crafting XP", TaskTier.ELITE, new SkillXpRequirement(Skill.CRAFTING, 50000000)));
		add(new Task("Obtain 100 Million Crafting XP", TaskTier.MASTER, new SkillXpRequirement(Skill.CRAFTING, 100000000)));
		add(new Task("Obtain 200 Million Crafting XP", TaskTier.MASTER, new SkillXpRequirement(Skill.CRAFTING, 200000000)));

		add(new Task("Obtain 25 Million Smithing XP", TaskTier.ELITE, new SkillXpRequirement(Skill.SMITHING, 25000000)));
		add(new Task("Obtain 35 Million Smithing XP", TaskTier.ELITE, new SkillXpRequirement(Skill.SMITHING, 35000000)));
		add(new Task("Obtain 50 Million Smithing XP", TaskTier.ELITE, new SkillXpRequirement(Skill.SMITHING, 50000000)));
		add(new Task("Obtain 100 Million Smithing XP", TaskTier.MASTER, new SkillXpRequirement(Skill.SMITHING, 100000000)));
		add(new Task("Obtain 200 Million Smithing XP", TaskTier.MASTER, new SkillXpRequirement(Skill.SMITHING, 200000000)));

		add(new Task("Obtain 25 Million Mining XP", TaskTier.ELITE, new SkillXpRequirement(Skill.MINING, 25000000)));
		add(new Task("Obtain 35 Million Mining XP", TaskTier.ELITE, new SkillXpRequirement(Skill.MINING, 35000000)));
		add(new Task("Obtain 50 Million Mining XP", TaskTier.ELITE, new SkillXpRequirement(Skill.MINING, 50000000)));
		add(new Task("Obtain 100 Million Mining XP", TaskTier.MASTER, new SkillXpRequirement(Skill.MINING, 100000000)));
		add(new Task("Obtain 200 Million Mining XP", TaskTier.MASTER, new SkillXpRequirement(Skill.MINING, 200000000)));

		add(new Task("Obtain 25 Million Herblore XP", TaskTier.ELITE, new SkillXpRequirement(Skill.HERBLORE, 25000000)));
		add(new Task("Obtain 35 Million Herblore XP", TaskTier.ELITE, new SkillXpRequirement(Skill.HERBLORE, 35000000)));
		add(new Task("Obtain 50 Million Herblore XP", TaskTier.ELITE, new SkillXpRequirement(Skill.HERBLORE, 50000000)));
		add(new Task("Obtain 100 Million Herblore XP", TaskTier.MASTER, new SkillXpRequirement(Skill.HERBLORE, 100000000)));
		add(new Task("Obtain 200 Million Herblore XP", TaskTier.MASTER, new SkillXpRequirement(Skill.HERBLORE, 200000000)));

		add(new Task("Obtain 25 Million Agility XP", TaskTier.ELITE, new SkillXpRequirement(Skill.AGILITY, 25000000)));
		add(new Task("Obtain 35 Million Agility XP", TaskTier.ELITE, new SkillXpRequirement(Skill.AGILITY, 35000000)));
		add(new Task("Obtain 50 Million Agility XP", TaskTier.ELITE, new SkillXpRequirement(Skill.AGILITY, 50000000)));
		add(new Task("Obtain 100 Million Agility XP", TaskTier.MASTER, new SkillXpRequirement(Skill.AGILITY, 100000000)));
		add(new Task("Obtain 200 Million Agility XP", TaskTier.MASTER, new SkillXpRequirement(Skill.AGILITY, 200000000)));

		add(new Task("Obtain 25 Million Thieving XP", TaskTier.ELITE, new SkillXpRequirement(Skill.THIEVING, 25000000)));
		add(new Task("Obtain 35 Million Thieving XP", TaskTier.ELITE, new SkillXpRequirement(Skill.THIEVING, 35000000)));
		add(new Task("Obtain 50 Million Thieving XP", TaskTier.ELITE, new SkillXpRequirement(Skill.THIEVING, 50000000)));
		add(new Task("Obtain 100 Million Thieving XP", TaskTier.MASTER, new SkillXpRequirement(Skill.THIEVING, 100000000)));
		add(new Task("Obtain 200 Million Thieving XP", TaskTier.MASTER, new SkillXpRequirement(Skill.THIEVING, 200000000)));

		add(new Task("Obtain 25 Million Slayer XP", TaskTier.ELITE, new SkillXpRequirement(Skill.SLAYER, 25000000)));
		add(new Task("Obtain 35 Million Slayer XP", TaskTier.ELITE, new SkillXpRequirement(Skill.SLAYER, 35000000)));
		add(new Task("Obtain 50 Million Slayer XP", TaskTier.ELITE, new SkillXpRequirement(Skill.SLAYER, 50000000)));
		add(new Task("Obtain 100 Million Slayer XP", TaskTier.MASTER, new SkillXpRequirement(Skill.SLAYER, 100000000)));
		add(new Task("Obtain 200 Million Slayer XP", TaskTier.MASTER, new SkillXpRequirement(Skill.SLAYER, 200000000)));

		add(new Task("Obtain 25 Million Farming XP", TaskTier.ELITE, new SkillXpRequirement(Skill.FARMING, 25000000)));
		add(new Task("Obtain 35 Million Farming XP", TaskTier.ELITE, new SkillXpRequirement(Skill.FARMING, 35000000)));
		add(new Task("Obtain 50 Million Farming XP", TaskTier.ELITE, new SkillXpRequirement(Skill.FARMING, 50000000)));
		add(new Task("Obtain 100 Million Farming XP", TaskTier.MASTER, new SkillXpRequirement(Skill.FARMING, 100000000)));
		add(new Task("Obtain 200 Million Farming XP", TaskTier.MASTER, new SkillXpRequirement(Skill.FARMING, 200000000)));

		add(new Task("Obtain 25 Million Runecraft XP", TaskTier.ELITE, new SkillXpRequirement(Skill.RUNECRAFT, 25000000)));
		add(new Task("Obtain 35 Million Runecraft XP", TaskTier.ELITE, new SkillXpRequirement(Skill.RUNECRAFT, 35000000)));
		add(new Task("Obtain 50 Million Runecraft XP", TaskTier.ELITE, new SkillXpRequirement(Skill.RUNECRAFT, 50000000)));
		add(new Task("Obtain 100 Million Runecraft XP", TaskTier.MASTER, new SkillXpRequirement(Skill.RUNECRAFT, 100000000)));
		add(new Task("Obtain 200 Million Runecraft XP", TaskTier.MASTER, new SkillXpRequirement(Skill.RUNECRAFT, 200000000)));

		add(new Task("Obtain 25 Million Hunter XP", TaskTier.ELITE, new SkillXpRequirement(Skill.HUNTER, 25000000)));
		add(new Task("Obtain 35 Million Hunter XP", TaskTier.ELITE, new SkillXpRequirement(Skill.HUNTER, 35000000)));
		add(new Task("Obtain 50 Million Hunter XP", TaskTier.ELITE, new SkillXpRequirement(Skill.HUNTER, 50000000)));
		add(new Task("Obtain 100 Million Hunter XP", TaskTier.MASTER, new SkillXpRequirement(Skill.HUNTER, 100000000)));
		add(new Task("Obtain 200 Million Hunter XP", TaskTier.MASTER, new SkillXpRequirement(Skill.HUNTER, 200000000)));

		add(new Task("Obtain 25 Million Construction XP", TaskTier.ELITE, new SkillXpRequirement(Skill.CONSTRUCTION, 25000000)));
		add(new Task("Obtain 35 Million Construction XP", TaskTier.ELITE, new SkillXpRequirement(Skill.CONSTRUCTION, 35000000)));
		add(new Task("Obtain 50 Million Construction XP", TaskTier.ELITE, new SkillXpRequirement(Skill.CONSTRUCTION, 50000000)));
		add(new Task("Obtain 100 Million Construction XP", TaskTier.MASTER, new SkillXpRequirement(Skill.CONSTRUCTION, 100000000)));
		add(new Task("Obtain 200 Million Construction XP", TaskTier.MASTER, new SkillXpRequirement(Skill.CONSTRUCTION, 200000000)));

//
//		Map<Integer, TaskTier> SKILL_XP_TASKS = ImmutableMap.<Integer, TaskTier>builder()
//			.put(25_000_000, TaskTier.ELITE)
//			.put(35_000_000, TaskTier.ELITE)
//			.put(50_000_000, TaskTier.ELITE)
//			.put(100_000_000, TaskTier.MASTER)
//			.put(200_000_000, TaskTier.MASTER)
//			.build();
//
//		StringBuilder test = new StringBuilder();
//
//		for (Skill skill : Skill.values())
//		{
//			for (Map.Entry<Integer, TaskTier> entry : SKILL_XP_TASKS.entrySet())
//			{
//				int xp = entry.getKey();
//				TaskTier tier = entry.getValue();
//
//				test.append("add(new Task(\"Obtain " + (xp / 1_000_000) + " Million " + skill.getName() + " XP\", TaskTier." + tier.getName().toUpperCase() + ", new SkillXpRequirement(Skill." + skill.getName().toUpperCase() + ", " + xp + ")));");
//			}
//		}
//
//		log.debug("{}", test.toString());
	}
}
