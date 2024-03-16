package com.antimated.tasklist.tasks;

import com.antimated.tasklist.requirements.Requirement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@ToString
public class Task
{
	private final String description;
	private final TaskTier tier;
	private final Requirement requirement;
	@Setter
	private boolean completed;

	public Task(String description, TaskTier tier, Requirement requirement, boolean completed)
	{
		this.description = description;
		this.tier = tier;
		this.requirement = requirement;
		this.completed = completed;
	}


}
