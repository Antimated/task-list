package com.antimated.tasklist;

import com.antimated.tasklist.notifications.NotificationManager;
import com.antimated.tasklist.tasks.TaskListManager;
import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Task list",
	conflicts = "Milestone Levels"
)
public class TaskListPlugin extends Plugin
{
	@Inject
	private TaskListConfig config;

	@Inject
	private TaskListManager taskListManager;

	@Inject
	private NotificationManager notificationManager;

	@Inject
	private ConfigManager configManager;

	@Override
	protected void startUp()
	{
		log.info("TaskListPlugin started!");
		notificationManager.startUp();
		taskListManager.startUp();
	}

	@Override
	protected void shutDown()
	{
		log.info("TaskListPlugin stopped!");
		notificationManager.shutDown();
		taskListManager.shutDown();
	}

	@Provides
	TaskListConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TaskListConfig.class);
	}
}
