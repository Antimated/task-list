package com.antimated.tasklist;

import com.antimated.tasklist.tasks.TaskList;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(TaskListConfig.GROUP_NAME)
public interface TaskListConfig extends Config
{
	String GROUP_NAME = "task-list";

	String TASKS_KEY = "tasks";

	// @formatter:off
	Type TASK_LIST_TYPE = new TypeToken<TaskList>() {}.getType();
	// @formatter:on
	String DEFAULT_TASKS_FILE_NAME = "default-tasks.json";

	@ConfigItem(
		keyName = "greeting",
		name = "Welcome Greeting",
		description = "The message to show to the user when they login"
	)
	default String greeting()
	{
		return "Hello";
	}
}
