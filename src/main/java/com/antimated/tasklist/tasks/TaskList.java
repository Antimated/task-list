package com.antimated.tasklist.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

@Slf4j
@Getter
@ToString
public class TaskList
{
	private final String name;

	private final List<Task> tasks;

	public TaskList(String name)
	{
		this.name = name;
		this.tasks = new ArrayList<>();
	}

	private TaskList(String name, List<Task> tasks)
	{
		this.name = name;
		this.tasks = tasks;
	}

	public void add(Task task)
	{
		this.tasks.add(task);
	}
	public TaskList getTasksByTier(TaskTier tier)
	{
		return new TaskList(this.name, this.tasks.stream()
			.filter(task -> task.getTier() == tier)
			.collect(Collectors.toList()));
	}

	public TaskList getSatisfyingTasks(Client client)
	{
		return new TaskList(this.name, this.tasks.stream()
			.filter(task -> task.getRequirement().satisfiesRequirement(client))
			.collect(Collectors.toList()));
	}

	public TaskList getTasksByCompletion(boolean isCompleted)
	{
		return new TaskList(this.name, tasks.stream()
			.filter(task -> task.isCompleted() == isCompleted)
			.collect(Collectors.toList()));
	}

	public Task getTaskByDescription(String description)
	{
		return tasks.stream()
			.filter(task -> task.getDescription().equals(description))
			.findFirst()
			.orElse(null);
	}
}