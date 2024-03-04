package com.antimated.tasklist.tasks;

import com.antimated.tasklist.requirements.Requirement;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class Task
{
	private final TaskType type;
	private final TaskTier tier;
	private final String description;
	private final Requirement requirement;
	private boolean completed;

	public Task(TaskType type, TaskTier tier, String description, Requirement requirement, boolean completed)
	{
		this.type = type;
		this.tier = tier;
		this.description = description;
		this.requirement = requirement;
		this.completed = completed;
	}
}
