package com.antimated.tasklist.tasks;

import com.antimated.tasklist.requirements.Requirement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
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
	@Setter
	private boolean completed;
}
