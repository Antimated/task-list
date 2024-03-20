package com.antimated.tasklist.tasks;

import java.util.ArrayList;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

@Slf4j
public class TaskList extends ArrayList<Task>
{
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
	
	public Task getTaskByDescription(String description) {
		return this.stream()
			.filter(task -> task.getDescription().equals(description))
			.findFirst()
			.orElse(null);
	}
}