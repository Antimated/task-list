package com.antimated.tasklist;

import com.antimated.tasklist.tasks.lists.TaskList;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(TaskListConfig.GROUP_NAME)
public interface TaskListConfig extends Config
{
	String GROUP_NAME = "task-list";

	String TASK_LISTS_KEY = "lists";

	// @formatter:off
	Type TASK_LIST_TYPE = new TypeToken<List<TaskList>>() {}.getType();
	// @formatter:on

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
