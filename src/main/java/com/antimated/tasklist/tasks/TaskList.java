package com.antimated.tasklist.tasks;

import java.util.ArrayList;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

@Slf4j
public class TaskList extends ArrayList<Task>
{
	// Leaving this so I can test with multiple and requirements
//	public TaskList()
//	{
//		add(new Task("Get level 10 in any skill", TaskTier.EASY, new AnySkillLevelRequirement(10), false));
//		add(new Task("Get level 20 in any skill", TaskTier.EASY, new AnySkillLevelRequirement(20), false));
//		add(new Task("Get level 30 in any skill", TaskTier.EASY, new AnySkillLevelRequirement(30), false));
//		add(new Task("Get level 40 in any skill", TaskTier.EASY, new AnySkillLevelRequirement(40), false));
//		add(new Task("Get level 50 in any skill", TaskTier.MEDIUM, new AnySkillLevelRequirement(50), false));
//		add(new Task("Get level 60 in any skill", TaskTier.MEDIUM, new AnySkillLevelRequirement(60), false));
//		add(new Task("Get level 70 in any skill", TaskTier.MEDIUM, new AnySkillLevelRequirement(70), false));
//		add(new Task("Get level 80 in any skill", TaskTier.HARD, new AnySkillLevelRequirement(80), false));
//		add(new Task("Get level 90 in any skill", TaskTier.ELITE, new AnySkillLevelRequirement(90), false));
//		add(new Task("Get level 99 in any skill", TaskTier.ELITE, new AnySkillLevelRequirement(99), false));
//		add(new Task("Use the Piety prayer", TaskTier.ELITE, new PrayerRequirement(Prayer.PIETY), false));
//		add(new Task("Use Chivalry or Piety prayer", TaskTier.ELITE, new OrRequirement(
//			new PrayerRequirement(Prayer.PIETY),
//			new PrayerRequirement(Prayer.CHIVALRY)
//		), false));
//		add(new Task("Equip a full steel armour set", TaskTier.EASY, new EquipmentRequirement(
//			new Equipment(EquipmentInventorySlot.HEAD, ItemID.STEEL_FULL_HELM),
//			new Equipment(EquipmentInventorySlot.BODY, ItemID.STEEL_PLATEBODY),
//			new Equipment(EquipmentInventorySlot.LEGS, ItemID.STEEL_PLATELEGS, ItemID.STEEL_PLATESKIRT),
//			new Equipment(EquipmentInventorySlot.SHIELD, ItemID.STEEL_KITESHIELD)
//		), false));
//	}

	public TaskList getTasksByTier(TaskTier tier)
	{
		return stream()
			.filter(task -> task.getTier() == tier)
			.collect(Collectors.toCollection(TaskList::new));
	}

	public TaskList getSatisfyingTasks(Client client)
	{
		return stream()
			.filter(task -> task.getRequirement().satisfiesRequirement(client))
			.collect(Collectors.toCollection(TaskList::new));
	}

	public TaskList getTasksByCompletion(boolean isCompleted)
	{
		return stream()
			.filter(task -> task.isCompleted() == isCompleted)
			.collect(Collectors.toCollection(TaskList::new));
	}
}