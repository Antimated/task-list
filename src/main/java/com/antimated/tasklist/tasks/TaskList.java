package com.antimated.tasklist.tasks;

import com.antimated.tasklist.equipment.Equipment;
import com.antimated.tasklist.requirements.AnySkillLevelRequirement;
import com.antimated.tasklist.requirements.EquipmentRequirement;
import com.antimated.tasklist.requirements.OrRequirement;
import com.antimated.tasklist.requirements.PrayerRequirement;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.ItemID;
import net.runelite.api.Prayer;

@Slf4j
@ToString
public class TaskList
{
	private final Set<Task> tasks = new HashSet<>();

	private TaskList(Set<Task> tasks) {
		this.tasks.addAll(tasks);
	}

	public TaskList()
	{
		tasks.add(new Task("Get level 10 in any skill", TaskTier.EASY, new AnySkillLevelRequirement(10), false));
		tasks.add(new Task("Get level 20 in any skill", TaskTier.EASY, new AnySkillLevelRequirement(20), false));
		tasks.add(new Task("Get level 30 in any skill", TaskTier.EASY, new AnySkillLevelRequirement(30), false));
		tasks.add(new Task("Get level 40 in any skill", TaskTier.EASY, new AnySkillLevelRequirement(40), false));
		tasks.add(new Task("Get level 50 in any skill", TaskTier.MEDIUM, new AnySkillLevelRequirement(50), false));
		tasks.add(new Task("Get level 60 in any skill", TaskTier.MEDIUM, new AnySkillLevelRequirement(60), false));
		tasks.add(new Task("Get level 70 in any skill", TaskTier.MEDIUM, new AnySkillLevelRequirement(70), false));
		tasks.add(new Task("Get level 80 in any skill", TaskTier.HARD, new AnySkillLevelRequirement(80), false));
		tasks.add(new Task("Get level 90 in any skill", TaskTier.ELITE, new AnySkillLevelRequirement(90), false));
		tasks.add(new Task("Get level 99 in any skill", TaskTier.ELITE, new AnySkillLevelRequirement(99), false));
		tasks.add(new Task("Use the Piety prayer", TaskTier.ELITE, new PrayerRequirement(Prayer.PIETY), false));
		tasks.add(new Task("Use Chivalry or Piety prayer", TaskTier.ELITE, new OrRequirement(
			new PrayerRequirement(Prayer.PIETY),
			new PrayerRequirement(Prayer.CHIVALRY)
		), false));
		tasks.add(new Task("Equip a full steel armour set", TaskTier.EASY, new EquipmentRequirement(
			new Equipment(EquipmentInventorySlot.HEAD, ItemID.STEEL_FULL_HELM),
			new Equipment(EquipmentInventorySlot.BODY, ItemID.STEEL_PLATEBODY),
			new Equipment(EquipmentInventorySlot.LEGS, ItemID.STEEL_PLATELEGS, ItemID.STEEL_PLATESKIRT),
			new Equipment(EquipmentInventorySlot.SHIELD, ItemID.STEEL_KITESHIELD)
		), false));
	}

	// Constructor for creating a TaskList from an existing set of tasks

	public TaskList getTasksByTiers(TaskTier tier)
	{
		return new TaskList(tasks.stream()
			.filter(task -> task.getTier() == tier)
			.collect(Collectors.toSet()));
	}

	public TaskList getSatisfyingTasks(Client client)
	{
		return new TaskList(tasks.stream()
			.filter(task -> task.getRequirement().satisfiesRequirement(client))
			.collect(Collectors.toSet()));
	}

	public TaskList getTasksByCompletion(boolean isCompleted)
	{
		return new TaskList(tasks.stream()
			.filter(task -> task.isCompleted() == isCompleted)
			.collect(Collectors.toSet()));
	}

	public Set<Task> all()
	{
		return tasks;
	}
}
