package com.antimated.tasklist.tasks;

import com.antimated.tasklist.TaskListConfig;
import com.antimated.tasklist.TaskListPlugin;
import com.antimated.tasklist.notifications.NotificationManager;
import com.antimated.tasklist.util.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

	private TaskList taskList;

	public void startUp()
	{
		eventBus.register(this);
		gson = new GsonBuilder().registerTypeAdapterFactory(Util.requirementAdapterFactory()).create();
		loginFlag = client.getGameState() == GameState.LOGGED_IN;
	}

	public void shutDown()
	{
		eventBus.unregister(this);
		taskList = null;
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
			log.debug("Logged in, attempting display tasks for now.");
			loginFlag = false;
			taskList = loadTaskList();

			completeSatisfiableTasks(false);
		}

		completeSatisfiableTasks(true);
	}

	/**
	 * Complete incomplete satisfiable tasks
	 *
	 * @param shouldNotify If completed tasks should throw a notification
	 */
	private void completeSatisfiableTasks(boolean shouldNotify)
	{
		TaskList satisfiableTasks = taskList
			.getSatisfyingTasks(client)
			.getTasksByCompletion(false);

		if (!satisfiableTasks.isEmpty())
		{
			log.debug("Found tasks to complete:");

			satisfiableTasks.forEach(task -> {
				task.setCompleted(true);

				if (shouldNotify)
				{
					notificationManager.addNotification(task);
				}

				log.debug(("Completing task: {} {}").trim(), task.getDescription(), shouldNotify ? "(and notify)." : "");
			});

			log.debug("taskList after completion: {}", taskList);
			setRSProfileTasks(taskList);
		}
	}

	public TaskList loadTaskList()
	{
		TaskList list;

		if (getRSProfileTasks().isEmpty())
		{
			log.debug("Loading default tasks - no RS-profile tasks found.");
			list = getDefaultTasks();
			// Save tasks to RS profile if they have never been loaded.
			log.debug("Saving default tasks to RS-profile");
			setRSProfileTasks(list);
		}
		else
		{
			log.debug("Loading RS-profile tasks.");
			list = getRSProfileTasks();
		}

		return list;
	}

	/**
	 * Sets tasks to an RS profile
	 *
	 * @param tasks TaskList
	 */
	public void setRSProfileTasks(TaskList tasks)
	{
		if (tasks != null)
		{
			String json = gson.toJson(tasks, TaskListConfig.TASK_LIST_TYPE);

			configManager.setRSProfileConfiguration(TaskListConfig.GROUP_NAME, TaskListConfig.TASKS_KEY, json);
		}
		else
		{
			configManager.unsetRSProfileConfiguration(TaskListConfig.GROUP_NAME, TaskListConfig.TASKS_KEY);
		}
	}

	/**
	 * Gets tasks from an RS profile.
	 *
	 * @return TaskList
	 */
	public TaskList getRSProfileTasks()
	{
		String tasks = configManager.getRSProfileConfiguration(TaskListConfig.GROUP_NAME, TaskListConfig.TASKS_KEY, String.class);

		if (tasks != null)
		{
			return gson.fromJson(tasks, TaskListConfig.TASK_LIST_TYPE);
		}

		return new TaskList();
	}

	/**
	 * Gets the default tasks from plugin resources.
	 *
	 * @return TaskList
	 */
	public TaskList getDefaultTasks()
	{
		try (InputStream stream = TaskListPlugin.class.getResourceAsStream(TaskListConfig.DEFAULT_TASKS_FILE_NAME))
		{
			if (stream != null)
			{
				InputStreamReader definitionReader = new InputStreamReader(stream);
				return gson.fromJson(definitionReader, TaskListConfig.TASK_LIST_TYPE);
			}
			else
			{
				throw new NullPointerException("Default tasks file not found: " + TaskListConfig.DEFAULT_TASKS_FILE_NAME);
			}
		}
		catch (IOException ioe)
		{
			log.error("Error loading default tasks: ", ioe);
		}

		return new TaskList();
	}
}
