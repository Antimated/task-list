package com.antimated.tasklist;

import com.antimated.tasklist.notifications.NotificationsManager;
import com.antimated.tasklist.tasks.TaskListManager;
import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Task list"
)
public class TaskListPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private EventBus eventBus;
	@Inject
	private TaskListConfig config;

	@Inject
	private TaskListManager taskListManager;

	@Inject
	private NotificationsManager notifications;

	@Inject
	private ConfigManager configManager;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Task list started!");
		notifications.startUp();
		taskListManager.startUp();
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Task list stopped!");
		notifications.shutDown();
		taskListManager.shutDown();
	}

	@Provides
	TaskListConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TaskListConfig.class);
	}
}
