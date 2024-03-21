package com.antimated.tasklist.tasks.lists;

import com.antimated.tasklist.requirements.Requirement;
import com.antimated.tasklist.tasks.Task;
import com.antimated.tasklist.tasks.TaskTier;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public abstract class TaskList
{
	private final String name;

	private final List<Task> tasks = new ArrayList<>();

	public TaskList(String name)
	{
		this.name = name;
	}

	public void add(String description, TaskTier tier, Requirement requirement)
	{
		Task task = new Task(description, tier, requirement);
		this.tasks.add(task);
	}
}