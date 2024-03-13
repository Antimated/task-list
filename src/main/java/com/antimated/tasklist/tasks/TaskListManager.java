package com.antimated.tasklist.tasks;

import com.antimated.tasklist.TaskListConfig;
import com.antimated.tasklist.TaskListPlugin;
import com.antimated.tasklist.json.TaskDeserializer;
import com.antimated.tasklist.json.TaskSerializer;
import com.antimated.tasklist.notifications.NotificationManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
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

	@Subscribe
	public void onGameTick(GameTick gameTick)
	{
		if (loginFlag)
		{
			log.debug("Login flag triggered");
			clientThread.invoke(this::loadTaskList);
			loginFlag = false;
		}

		completeSatisfiableTasks(true);
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

	public void startUp()
	{
		eventBus.register(this);
		gson = new GsonBuilder().registerTypeAdapter(Task.class, new TaskDeserializer()).registerTypeAdapter(Task.class, new TaskSerializer()).create();

		if (client.getGameState() == GameState.LOGGED_IN)
		{
			loginFlag = true;
		}
	}

	public void shutDown()
	{
		loginFlag = false;
		eventBus.unregister(this);
	}

	public void loadTaskList()
	{
		if (getRSProfileTasks().isEmpty())
		{
			log.debug("Loading default task list as no list for this profile is found.");
			taskList = new TaskList(getDefaultTasks());
			// Save tasks to RS profile if they have never been loaded.
			setRSProfileTasks(taskList.getTasks());
		}
		else
		{
			log.debug("Loading tasks.");
			taskList = new TaskList(getRSProfileTasks());
		}

		completeSatisfiableTasks(false);
	}


	/**
	 * Unsets tasks from an RS profile
	 */
	public void unSetRSProfileTasks()
	{
		configManager.unsetRSProfileConfiguration(TaskListConfig.GROUP_NAME, TaskListConfig.TASKS_KEY);
	}

	/**
	 * Sets tasks to an RS profile
	 *
	 * @param tasks List of Task objects
	 */
	public void setRSProfileTasks(List<Task> tasks)
	{
		String json = gson.toJson(tasks, TaskListConfig.TASK_LIST_TYPE);

		configManager.setRSProfileConfiguration(TaskListConfig.GROUP_NAME, TaskListConfig.TASKS_KEY, json);
	}

	/**
	 * Gets tasks from an RS profile.
	 *
	 * @return List<Task>
	 */
	public List<Task> getRSProfileTasks()
	{
		String tasks = configManager.getRSProfileConfiguration(TaskListConfig.GROUP_NAME, TaskListConfig.TASKS_KEY, String.class);

		if (tasks == null)
		{
			return new ArrayList<>();
		}

		return gson.fromJson(tasks, TaskListConfig.TASK_LIST_TYPE);
	}

	/**
	 * Gets the default tasks from plugin resources.
	 *
	 * @return List<Task>
	 */
	public List<Task> getDefaultTasks()
	{
		try (InputStream stream = TaskListPlugin.class.getResourceAsStream(TaskListConfig.DEFAULT_TASKS_FILE_NAME))
		{
			assert stream != null;
			InputStreamReader definitionReader = new InputStreamReader(stream);
			return gson.fromJson(definitionReader, TaskListConfig.TASK_LIST_TYPE);
		}
		catch (IOException ioe)
		{
			log.warn("Error loading default tasks: ", ioe);
		}

		return new ArrayList<>();
	}

	/**
	 * Complete incomplete satisfiable tasks
	 *
	 * @param shouldNotify If completed tasks should throw a notification
	 */
	private void completeSatisfiableTasks(boolean shouldNotify)
	{
		List<Task> satisfiableTasks = taskList.getSatisfyingTasks(client).getTasksByCompletion(false).all();

		if (!satisfiableTasks.isEmpty())
		{
			log.debug("Satisfiable tasks found.");

			satisfiableTasks.forEach(task -> {
				log.debug("Completing task: {}", task);

				task.setCompleted(true);

				if (shouldNotify)
				{
					notificationManager.addNotification(task);
				}
			});

			setRSProfileTasks(taskList.getTasks());
		}
	}
}
