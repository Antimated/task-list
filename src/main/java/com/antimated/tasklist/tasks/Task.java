package com.antimated.tasklist.tasks;

import com.antimated.tasklist.TaskListManager;
import com.antimated.tasklist.requirements.Requirement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@ToString
@AllArgsConstructor
public class Task
{
	private final int id;
	private final TaskType type;
	private final TaskTier tier;
	private final String description;
	private final Requirement requirement;
	private boolean completed;

	public void complete()
	{
		complete(true);
	}

	public void complete(boolean shouldNotify)
	{
		this.completed = true;

		// Update task state in taskManager?
		if (shouldNotify)
		{
			// Add message to task queue
			log.debug("Completed a task: {}", this);
		}
	}
}
