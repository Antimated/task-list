package com.antimated.tasklist.tasks;

import com.antimated.tasklist.TaskListPlugin;
import com.antimated.tasklist.json.TaskDeserializer;
import com.antimated.tasklist.json.TaskSerializer;
import com.antimated.tasklist.notifications.NotificationManager;
import com.antimated.tasklist.util.Util;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.RuneScapeProfileType;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;

@Slf4j
@Singleton
public class TaskListManager
{
	private static final String TASKS_FILE_NAME = "tasks.json";

	private static final String DEFAULT_TASKS_FILE_NAME = "default-tasks.json";

	// @formatter:off
	private static final Type TASK_LIST_TYPE = new TypeToken<List<Task>>() {}.getType();
	// @formatter:on

	private static final Set<Integer> LAST_MAN_STANDING_REGIONS = ImmutableSet.of(13658, 13659, 13660, 13914, 13915, 13916, 13918, 13919, 13920, 14174, 14175, 14176, 14430, 14431, 14432);

	private boolean shouldLoadTasks;

	@Getter
	private TaskList taskList;

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private NotificationManager notifications;

	@Inject
	private EventBus eventBus;

	@Inject
	private Gson gson;

	private void loadTasksFromProfile()
	{
		// TODO: Add some way we can patch new tasks in...
		gson = new GsonBuilder()
			.registerTypeAdapter(Task.class, new TaskDeserializer())
			.registerTypeAdapter(Task.class, new TaskSerializer())
			.create();
		TaskList loadedTasks;
		File tasksFile = new File(Util.getPluginFolder(client), TASKS_FILE_NAME);

		try (FileInputStream stream = new FileInputStream(tasksFile))
		{
			InputStreamReader definitionReader = new InputStreamReader(stream);
			loadedTasks = new TaskList(gson.fromJson(definitionReader, TASK_LIST_TYPE));
			log.debug("Loading user task list");
		}
		catch (FileNotFoundException e)
		{
			log.debug("User task list not found, trying default task list");

			try (InputStream stream = TaskListPlugin.class.getResourceAsStream(DEFAULT_TASKS_FILE_NAME))
			{
				assert stream != null;
				InputStreamReader definitionReader = new InputStreamReader(stream);
				loadedTasks = new TaskList(gson.fromJson(definitionReader, TASK_LIST_TYPE));
				log.debug("Default task list loaded");
			}
			catch (IOException ioe)
			{
				log.warn("Error loading default tasks", ioe);
				throw new RuntimeException(ioe);
			}
		}
		catch (IOException ioe)
		{
			throw new RuntimeException(ioe);
		}

		// We check our loadedTasks for already completed tasks,
		// so we can prevent spamming the user with task completed notifications
		completeSatisfiable(loadedTasks, false);
		taskList = loadedTasks;

		log.debug("Task list loaded successfully.");
	}


	private void completeSatisfiable(TaskList taskList, boolean shouldNotify)
	{
		// Get a list of satisfiable tasks that are not completed yet.
		List<Task> satisfiableTasks = taskList
			.getSatisfyingTasks(client)
			.getTasksByCompletion(false)
			.all();

		// Don't continue when no tasks
		if (satisfiableTasks.isEmpty())
		{
			return;
		}

		log.debug("Satisfiable tasks found, completing them...");

		satisfiableTasks.forEach(task -> {
			task.setCompleted(true);

			if (shouldNotify)
			{
				notifications.addNotification("Task complete!", getNotificationText(task));
			}
		});

		// After all satisfiable tasks are done, save our tasks to our profile json
		saveTaskListToJson(taskList);
	}

	private String getNotificationText(Task task)
	{
		StringBuilder text = new StringBuilder();

		text.append("Task completed: ")
			.append("<col=ffffff>")
			.append(task.getDescription())
			.append("</col>")
			.append("<br>")
			.append("Points earned: ")
			.append("<col=ffffff>")
			.append(task.getTier().getPoints())
			.append("</col>");

		return text.toString();
	}


	private void saveTaskListToJson(TaskList taskList)
	{
		try
		{
			File tasksFile = new File(Util.getPluginFolder(client), TASKS_FILE_NAME);
			String loadedTasksJson = gson.toJson(taskList.getTasks(), TASK_LIST_TYPE);
			FileWriter file = new FileWriter(tasksFile);

			file.write(loadedTasksJson);
			file.close();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		// Check when user logs in with plugin enabled if we should start loading tasks
		shouldLoadTasks = gameStateChanged.getGameState() == GameState.LOGGED_IN;
	}

	@Subscribe
	public void onGameTick(GameTick gameTick)
	{
		// Don't do anything on non-standard worlds OR when a player is within LMS
		if (RuneScapeProfileType.getCurrent(client) != RuneScapeProfileType.STANDARD || Util.isPlayerWithinMapRegion(client, LAST_MAN_STANDING_REGIONS))
		{
			return;
		}

		// On first registered game tick we want to load in our tasks
		if (shouldLoadTasks)
		{
			shouldLoadTasks = false;
			loadTasksFromProfile();
		}

		// After our tasks are loaded in we can start and check for tasks that are completed.
		completeSatisfiable(taskList, true);
	}

	public void startUp()
	{
		eventBus.register(this);
		shouldLoadTasks = client.getGameState() == GameState.LOGGED_IN; // Check on plugin startup if we should load tasks at first gameTick.
	}

	public void shutDown()
	{
		eventBus.unregister(this);
		taskList = null;
		shouldLoadTasks = false; // Disable loading of tasks at first gameTick
	}
}
