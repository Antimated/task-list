package com.antimated.tasklist.tasks;

import com.antimated.tasklist.TaskListPlugin;
import com.antimated.tasklist.json.TaskDeserializer;
import com.antimated.tasklist.json.TaskSerializer;
import com.antimated.tasklist.notifications.NotificationManager;
import com.antimated.tasklist.util.Utils;
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

	private static final Type TASK_LIST_TYPE = new TypeToken<List<Task>>()
	{
	}.getType();

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
		TaskList loadedTasks;
		File tasksFile = new File(Utils.getPluginFolder(client), TASKS_FILE_NAME);

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

		completeSatisfiable(loadedTasks, false);
		taskList = loadedTasks;

		log.debug("Task list loaded successfully.");
	}


	private void completeSatisfiable(TaskList taskList, boolean shouldNotify)
	{
		List<Task> satisfiableTasks = taskList
			.getSatisfyingTasks(client)
			.getTasksByCompletion(false).all();

		if (satisfiableTasks.isEmpty())
		{
			return;
		}

		log.debug("Satisfiable tasks found, completing them...");

		satisfiableTasks.forEach(task -> {
			task.setCompleted(true);

			if (shouldNotify)
			{
				StringBuilder text = new StringBuilder();

				text.append("Task completed: ");
				text.append("<col=ffffff>");
				text.append(task.getDescription());
				text.append("</col>");
				text.append("<br>");
				text.append("Points earned: ");
				text.append("<col=ffffff>");
				text.append(task.getTier().getPoints());
				text.append("</col>");

				notifications.addNotification("Task complete!", text.toString());
			}
		});

		// After all satisfiable tasks are done, save our tasks to our profile json
		saveTaskListToJson(taskList);
	}


	private void saveTaskListToJson(TaskList taskList)
	{
		try
		{
			File tasksFile = new File(Utils.getPluginFolder(client), TASKS_FILE_NAME);
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
		if (RuneScapeProfileType.getCurrent(client) != RuneScapeProfileType.STANDARD || Utils.isPlayerWithinMapRegion(client, LAST_MAN_STANDING_REGIONS))
		{
			return;
		}

		if (shouldLoadTasks)
		{
			shouldLoadTasks = false;
			// Only load tasks on FIRST gametick that is registered, so we are sure stats have been fetched from the server
			loadTasksFromProfile();
		}

		completeSatisfiable(taskList, true);
	}

	public void startUp()
	{
		eventBus.register(this);
		gson = new GsonBuilder()
			.registerTypeAdapter(Task.class, new TaskDeserializer())
			.registerTypeAdapter(Task.class, new TaskSerializer())
			.create();
		shouldLoadTasks = client.getGameState() == GameState.LOGGED_IN; // Check on plugin startup if we should load tasks at first gameTick.
	}

	public void shutDown()
	{
		eventBus.unregister(this);
		taskList = null;
		shouldLoadTasks = false; // Disable loading of tasks at first gameTick
	}
}
