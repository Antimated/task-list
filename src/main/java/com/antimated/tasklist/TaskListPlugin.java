package com.antimated.tasklist;

import com.antimated.tasklist.notifications.NotificationManager;
import com.antimated.tasklist.tasks.TaskListManager;
import com.google.inject.Provides;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.http.api.item.ItemEquipmentStats;

@Slf4j
@PluginDescriptor(
	name = "Task list"
)
public class TaskListPlugin extends Plugin
{
	@Inject
	private TaskListConfig config;

	@Inject
	private TaskListManager taskListManager;

	@Inject
	private NotificationManager notifications;

	@Inject
	private ConfigManager configManager;

	@Override
	protected void startUp()
	{
		log.info("TaskListPlugin started!");
		notifications.startUp();
		taskListManager.startUp();
	}

	@Override
	protected void shutDown()
	{
		log.info("TaskListPlugin stopped!");
		notifications.shutDown();
		taskListManager.shutDown();
	}

	@Provides
	TaskListConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TaskListConfig.class);
	}
}
