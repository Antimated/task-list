package com.antimated.tasklist.tasks;

import com.antimated.tasklist.requirements.Requirement;
import lombok.Data;

@Data
public class Task
{
	private final int id;
	private final TaskType type;
	private final TaskTier tier;
	private final String description;
	private final Requirement requirement;
	private boolean completed;

	public Task(int id, TaskType type, TaskTier tier, String description, Requirement requirement, boolean completed)
	{
		this.id = id;
		this.type = type;
		this.tier = tier;
		this.description = description;
		this.requirement = requirement;
		this.completed = completed;
	}
}
