package com.antimated.tasklist.tasks;

import com.antimated.tasklist.notifications.NotificationManager;
import com.antimated.tasklist.util.Util;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Set;
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
	private static final Set<Integer> LAST_MAN_STANDING_REGIONS = ImmutableSet.of(13658, 13659, 13660, 13914, 13915, 13916, 13918, 13919, 13920, 14174, 14175, 14176, 14430, 14431, 14432);
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
		if (Util.isPlayerWithinMapRegion(client, LAST_MAN_STANDING_REGIONS))
		{
			return;
		}

		if (loginFlag)
		{
			log.debug("Logged in, attempting display tasks for now.");
			loginFlag = false;
			taskList = new TaskList();


			gson = new GsonBuilder()
				.registerTypeAdapterFactory(Util.requirementAdapterFactory())
				.create();


			String json = gson.toJson(taskList.all());

			log.debug("JSON response string {}", json);

//			Set<Task> tasksFromJson = gson.fromJson(json, TaskListConfig.TASK_LIST_TYPE);
//
//			log.debug("Tasks from JSON: {}", tasksFromJson);
//
//			for (Task task :  tasksFromJson)
//			{
//				log.debug("Task: {}", task);
//			}
//			completeSatisfiableTasks(false);
		}

//		completeSatisfiableTasks(true);
	}

	/**
	 * Complete incomplete satisfiable tasks
	 *
	 * @param shouldNotify If completed tasks should throw a notification
	 */
	private void completeSatisfiableTasks(boolean shouldNotify)
	{
		clientThread.invoke(() -> {
			Set<Task> satisfiableTasks = taskList.getSatisfyingTasks(client).getTasksByCompletion(false).all();

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

//			setRSProfileTasks(taskList.getTasks());
			}
		});
	}

//	@Subscribe
//	public void onGameStateChanged(GameStateChanged event)
//	{
//		switch (event.getGameState())
//		{
//			case HOPPING:
//			case LOGGING_IN:
//			case CONNECTION_LOST:
//				loginFlag = true; // Makes sure we re-initialise our current tasks.
//				break;
//		}
//	}

//	public void startUp()
//	{
//		eventBus.register(this);
//		gson = new GsonBuilder().registerTypeAdapter(Task.class, new TaskDeserializer()).registerTypeAdapter(Task.class, new TaskSerializer()).create();
//
//		if (client.getGameState() == GameState.LOGGED_IN)
//		{
//			loginFlag = true;
//		}
//	}

//	public void shutDown()
//	{
//		loginFlag = false;
//		eventBus.unregister(this);
//	}

//	public void loadTaskList()
//	{
//		// TODO: If new plugin version is available we should check if a set of new tasks is available
//		if (getRSProfileTasks().isEmpty())
//		{
//			log.debug("Loading default tasks - no RS-profile tasks found.");
//			taskList = new TaskList(getDefaultTasks());
//			// Save tasks to RS profile if they have never been loaded.
//			setRSProfileTasks(taskList.getTasks());
//		}
//		else
//		{
//			log.debug("Loading RS-profile tasks.");
//			taskList = new TaskList(getRSProfileTasks());
//		}
//
//		completeSatisfiableTasks(false);
//	}


	/**
	 * Unsets tasks from an RS profile
	 */
//	public void unSetRSProfileTasks()
//	{
//		configManager.unsetRSProfileConfiguration(TaskListConfig.GROUP_NAME, TaskListConfig.TASKS_KEY);
//	}

	/**
	 * Sets tasks to an RS profile
	 *
	 * @param tasks List of Task objects
	 */
//	public void setRSProfileTasks(List<Task> tasks)
//	{
//		String json = gson.toJson(tasks, TaskListConfig.TASK_LIST_TYPE);
//
//		configManager.setRSProfileConfiguration(TaskListConfig.GROUP_NAME, TaskListConfig.TASKS_KEY, json);
//	}

	/**
	 * Gets tasks from an RS profile.
	 *
	 * @return List<Task>
	 */
//	public List<Task> getRSProfileTasks()
//	{
//		String tasks = configManager.getRSProfileConfiguration(TaskListConfig.GROUP_NAME, TaskListConfig.TASKS_KEY, String.class);
//
//		if (tasks == null)
//		{
//			return new ArrayList<>();
//		}
//
//		return gson.fromJson(tasks, TaskListConfig.TASK_LIST_TYPE);
//	}

	/**
	 * Gets the default tasks from plugin resources.
	 *
	 * @return List<Task>
	 */
//	public List<Task> getDefaultTasks()
//	{
//		try (InputStream stream = TaskListPlugin.class.getResourceAsStream(TaskListConfig.DEFAULT_TASKS_FILE_NAME))
//		{
//			assert stream != null;
//			InputStreamReader definitionReader = new InputStreamReader(stream);
//			return gson.fromJson(definitionReader, TaskListConfig.TASK_LIST_TYPE);
//		}
//		catch (IOException ioe)
//		{
//			log.error("Error loading default tasks: ", ioe);
//		}
//
//		return new ArrayList<>();
//	}

	/**
	 * Complete incomplete satisfiable tasks
	 *
	 * @param shouldNotify If completed tasks should throw a notification
	 */
//	private void completeSatisfiableTasks(boolean shouldNotify)
//	{
//		List<Task> satisfiableTasks = taskList.getSatisfyingTasks(client).getTasksByCompletion(false).all();
//
//		if (!satisfiableTasks.isEmpty())
//		{
//			log.debug("Found tasks to complete:");
//
//			satisfiableTasks.forEach(task -> {
//				task.setCompleted(true);
//
//				if (shouldNotify)
//				{
//					notificationManager.addNotification(task);
//				}
//
//				log.debug(("Completing task: {} {}").trim(), task.getDescription(), shouldNotify ? "(and notify)." : "");
//			});
//
//			setRSProfileTasks(taskList.getTasks());
//		}
//	}
}
