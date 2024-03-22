package com.antimated.tasklist.tasks.lists;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenericTaskList extends TaskList
{
	// Used to create new TaskList instances on the fly
	public GenericTaskList(String name)
	{
		super(name);
	}
}
