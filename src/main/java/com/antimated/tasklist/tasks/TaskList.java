package com.antimated.tasklist.tasks;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

@Slf4j
@Getter
public class TaskList
{
	private final List<Task> tasks;

	public TaskList(List<Task> tasks)
	{
		this.tasks = tasks;
	}

	public TaskList getTasksByType(TaskType... types)
	{
		List<TaskType> typeList = Arrays.asList(types);

		List<Task> filteredTasks = tasks.stream()
			.filter(task ->  typeList.contains(task.getType()))
			.collect(Collectors.toList());

		return new TaskList(filteredTasks);
	}

	public TaskList getTasksByTiers(TaskTier... tiers)
	{
		List<TaskTier> tierList = Arrays.asList(tiers);

		List<Task> filteredTasks = tasks.stream()
			.filter(task ->  tierList.contains(task.getTier()))
			.collect(Collectors.toList());

		return new TaskList(filteredTasks);
	}

	public TaskList getSatisfyingTasks(Client client)
	{
		List<Task> filteredTasks = tasks.stream()
			.filter(task -> task.getRequirement().satisfiesRequirement(client))
			.collect(Collectors.toList());

		return new TaskList(filteredTasks);
	}


	public TaskList getTasksByCompletion(boolean isCompleted)
	{
		List<Task> filteredTasks = tasks.stream()
			.filter(task -> task.isCompleted() == isCompleted)
			.collect(Collectors.toList());

		return new TaskList(filteredTasks);
	}

	public List<Task> all()
	{
		log.debug("Tasks in all() query: {}", this.tasks);
		return this.tasks;
	}
}
