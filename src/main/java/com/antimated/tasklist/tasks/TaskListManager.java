package com.antimated.tasklist.tasks;

import com.antimated.tasklist.TaskListConfig;
import com.antimated.tasklist.notifications.NotificationManager;
import com.antimated.tasklist.tasks.lists.LevelTaskList;
import com.antimated.tasklist.tasks.lists.TaskList;
import com.antimated.tasklist.tasks.lists.XpTaskList;
import com.antimated.tasklist.util.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;

@Slf4j
@Singleton
public class TaskListManager
{
	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private ConfigManager configManager;

	@Inject
	private NotificationManager notificationManager;

	@Inject
	private EventBus eventBus;

	@Inject
	private Gson gson;

	public boolean loginFlag;

	private List<TaskList> taskLists;

	public void startUp()
	{
		eventBus.register(this);
		gson = new GsonBuilder()
			.registerTypeAdapterFactory(Util.requirementAdapterFactory())
			.registerTypeAdapterFactory(Util.taskListAdapterFactory())
			.create();
		loginFlag = client.getGameState() == GameState.LOGGED_IN;
	}

	public void shutDown()
	{
		eventBus.unregister(this);
		clearTaskLists();
		loginFlag = false;
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		switch (event.getGameState())
		{
			case HOPPING:
			case LOGGING_IN:
			case CONNECTION_LOST:
				loginFlag = true; // Makes sure we re-initialise our current tasks.
				break;
		}
	}

	@Subscribe
	public void onGameTick(GameTick gameTick)
	{
		// Stop completing satisfiable tasks when in LMS regions
		if (Util.isPlayerWithinMapRegion(client, Util.LAST_MAN_STANDING_REGIONS))
		{
			return;
		}

		if (loginFlag)
		{
			loadTaskLists();
		}

		// Every game tick, check for completable tasks
		completeTasks(true);
	}

	private void completeTasks(boolean shouldNotify)
	{
		for (TaskList taskList : taskLists)
		{
			List<Task> completableTasks = taskList.getTasks()
				.stream()
				.filter(task -> !task.isCompleted() && task.getRequirement().satisfiesRequirement(client))
				.collect(Collectors.toList());

			if (!completableTasks.isEmpty())
			{
				log.debug("Found completable tasks in '{}'...", taskList.getName());

				for (Task task : completableTasks)
				{
					task.setCompleted(true);

					if (shouldNotify)
					{
						notificationManager.addNotification(task);
					}

					log.debug(("Completing task: {} {}").trim(), task.getDescription(), shouldNotify ? "(and notify)." : "");
				}

				// Save task lists on RS-profile
				setSavedTaskLists(taskLists);
			}
		}
	}

	public void clearTaskLists()
	{
		if (taskLists != null)
		{
			taskLists.clear();
		}
	}

	public void setSavedTaskLists(List<TaskList> lists)
	{
		if (lists != null)
		{
			String json = gson.toJson(lists, TaskListConfig.TASK_LIST_TYPE);
			log.debug("Saving task lists to RS-profile");
			configManager.setRSProfileConfiguration(TaskListConfig.GROUP_NAME, TaskListConfig.TASK_LISTS_KEY, json);
		}
		else
		{
			log.debug("Clearing task lists from RS-profile");
			configManager.unsetRSProfileConfiguration(TaskListConfig.GROUP_NAME, TaskListConfig.TASK_LISTS_KEY);
		}
	}


	public void loadTaskLists()
	{
		// User logged in, reset flag to false so loading only happens first gametick.
		loginFlag = false;

		// Fetch tasks for RS-profile.
		String tasks = configManager.getRSProfileConfiguration(TaskListConfig.GROUP_NAME, TaskListConfig.TASK_LISTS_KEY, String.class);

		if (tasks != null)
		{
			log.debug("RS-profile tasks found, loading them...");
			taskLists = gson.fromJson(tasks, TaskListConfig.TASK_LIST_TYPE);
		}
		else
		{
			log.debug("No RS-profile tasks found, loading default task lists");
			taskLists = new ArrayList<>();
			taskLists.add(new LevelTaskList());
			taskLists.add(new XpTaskList());
		}

		// Auto complete loaded tasks if possible (whether RS-profile or default)
		// but don't notify player
		completeTasks(false);
	}
}
