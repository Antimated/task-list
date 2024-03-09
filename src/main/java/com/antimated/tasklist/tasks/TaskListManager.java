package com.antimated.tasklist.tasks;

import com.antimated.tasklist.TaskListPlugin;
import com.antimated.tasklist.json.TaskDeserializer;
import com.antimated.tasklist.json.TaskSerializer;
import com.antimated.tasklist.notifications.NotificationsManager;
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
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.RuneLite;
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

	private static final File TASK_LIST_DIR = new File(RuneLite.RUNELITE_DIR, "task-list");

	private static final Type TASK_LIST_TYPE = new TypeToken<List<Task>>()
	{
	}.getType();

	private boolean shouldLoadTasks;

	@Getter
	private TaskList taskList;

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private NotificationsManager notifications;

	@Inject
	private EventBus eventBus;

	@Inject
	private Gson gson;


	public void loadTasks()
	{
		log.debug("Attempting to load tasks...");
		gson = new GsonBuilder()
			.registerTypeAdapter(Task.class, new TaskDeserializer())
			.registerTypeAdapter(Task.class, new TaskSerializer())
			.create();

		loadTasksFromProfile();
	}

	public void loadTasksFromProfile()
	{
		TaskList loadedTasks;
		File tasksFile = new File(getPluginFolder(), TASKS_FILE_NAME);

		try (FileInputStream stream = new FileInputStream(tasksFile))
		{
			log.debug("Attempting to load task list for user...");
			InputStreamReader definitionReader = new InputStreamReader(stream);
			loadedTasks = new TaskList(gson.fromJson(definitionReader, TASK_LIST_TYPE));
			log.debug("Task list for user loaded");
		}
		catch (FileNotFoundException e)
		{
			log.debug("Task list for user not found, loading default task list...");

			try (InputStream stream = TaskListPlugin.class.getResourceAsStream(DEFAULT_TASKS_FILE_NAME))
			{
				log.debug("Attempting to load default task list...");
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
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

		completeSatisfiable(loadedTasks, true);
		taskList = loadedTasks;

		log.debug("Task list loaded successfully.");
	}


	public void completeSatisfiable(TaskList taskList, boolean shouldNotify)
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
				notifications.addNotification("Task completed", task.getDescription());
			}
		});

		// After all satisfiable tasks are done, save our tasks to our profile json
		saveTaskListToJson(taskList);
	}

	public void saveTaskListToJson(TaskList taskList)
	{
		try
		{
			File tasksFile = new File(getPluginFolder(), TASKS_FILE_NAME);
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

	public File getPluginFolder()
	{
		File playerFolder;

		if (client.getLocalPlayer() != null && client.getLocalPlayer().getName() != null)
		{
			playerFolder = new File(TASK_LIST_DIR, client.getLocalPlayer().getName());
		}
		else
		{
			playerFolder = TASK_LIST_DIR;
		}

		if (playerFolder.mkdirs())
		{
			log.debug("Folder created at {}", playerFolder.getAbsolutePath());
		}

		return playerFolder;
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
		log.debug("Profile type: {}", RuneScapeProfileType.getCurrent(client));
		if (RuneScapeProfileType.getCurrent(client) != RuneScapeProfileType.STANDARD)
		{
			return;
		}

		if (shouldLoadTasks)
		{
			shouldLoadTasks = false;
			// Only load tasks on FIRST gametick that is registered so we are sure stats have been fetched from the server
			loadTasks();
		}

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
