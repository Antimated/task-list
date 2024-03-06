package com.antimated.tasklist.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskList extends ArrayList<Task>
{
	public Task getTaskById(int id)
	{
		return this.stream()
			.filter(task -> task.getId() == id)
			.findFirst()
			.orElse(null);
	}

	public List<Task> getTasksByType(TaskType type)
	{
		return this.stream()
			.filter(task -> task.getType() == type)
			.collect(Collectors.toList());
	}

	public List<Task> getTasksByTier(TaskTier tier)
	{
		return this.stream()
			.filter(task -> task.getTier() == tier)
			.collect(Collectors.toList());
	}
}
