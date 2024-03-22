package com.antimated.tasklist.tasks.lists;

import com.antimated.tasklist.requirements.Requirement;
import com.antimated.tasklist.tasks.Task;
import com.antimated.tasklist.tasks.TaskTier;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@ToString
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
		add(task);
	}

	public void add(Task task)
	{
		this.tasks.add(task);
	}
}