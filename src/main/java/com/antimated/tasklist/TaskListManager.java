package com.antimated.tasklist;

import com.antimated.tasklist.json.TaskDeserializer;
import com.antimated.tasklist.json.TaskSerializer;
import com.antimated.tasklist.tasks.Task;
import com.antimated.tasklist.tasks.TaskList;
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
	private EventBus eventBus;

	@Inject
	private Gson gson;

	private static final String TASKS_FILE_NAME = "tasks.json";

	private static final String DEFAULT_TASKS_FILE_NAME = "default-tasks.json";

	private static final File TASK_LIST_DIR = new File(RuneLite.RUNELITE_DIR, "task-list");

	private static final Type TASK_LIST_TYPE = new TypeToken<List<Task>>() {}.getType();

	@Getter
	private TaskList taskList;

	private boolean shouldLoadTasks;

	public void loadTasks()
	{
		log.debug("Attempting to load tasks...");
		gson = new GsonBuilder()
			.registerTypeAdapter(Task.class, new TaskDeserializer())
			.registerTypeAdapter(Task.class, new TaskSerializer())
			.create();

		loadTasksFromProfile();
	}


	// Loads the default tasks list if the plugin hasn't created a savefile yet
	public List<Task> loadDefaultTasks()
	{
		try (InputStream stream = TaskListPlugin.class.getResourceAsStream(DEFAULT_TASKS_FILE_NAME))
		{
			assert stream != null;
			InputStreamReader definitionReader = new InputStreamReader(stream);

			return gson.fromJson(definitionReader, TASK_LIST_TYPE);
		}
		catch (IOException e)
		{
			log.warn("Error loading default tasks", e);
			return null;
		}
	}

	public void loadTasksFromProfile()
	{
		File tasksFile = new File(getPluginFolder(), TASKS_FILE_NAME);

		try (FileInputStream stream = new FileInputStream(tasksFile))
		{
			InputStreamReader definitionReader = new InputStreamReader(stream);
			TaskList loadedTasks = new TaskList(gson.fromJson(definitionReader, TASK_LIST_TYPE));
			completeSatisfiable(loadedTasks, false);
			taskList = loadedTasks;

			log.debug("Task list loaded from user profile...");
		}
		catch (FileNotFoundException e)
		{
			log.debug("Task list for user not found, loading default task list...");

			TaskList loadedTasks = new TaskList(loadDefaultTasks());
			completeSatisfiable(loadedTasks, false);
			taskList = loadedTasks;

			log.debug("Task list loaded from user profile...");
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}


	public void completeSatisfiable(TaskList taskList, boolean shouldNotify)
	{
		List<Task> allSatisfiable = taskList.getSatisfyingTasks(client).getTasksByCompletion(false).all();

		if (!allSatisfiable.isEmpty()) {
			log.debug("Satisfiable tasks found...");

			for (Task task : allSatisfiable) {
				log.debug("Completing task: {}", task.getDescription());
				task.complete(shouldNotify);
			}

			// After all satisfiable tasks are done, save our tasks to our profile json
			saveTaskListToJson(taskList);
		} else {
			log.debug("No tasks need to be pre-completed, as none are satisfiable.");
		}
	}
	public void saveTaskListToJson(TaskList taskList) {
		try  {
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
	public void onGameTick(GameTick gameTick) throws IOException
	{
		if (shouldLoadTasks)
		{
			shouldLoadTasks = false;
			// Only load tasks on FIRST gametick that is registered so we are sure stats have been fetched from the server
			loadTasks();
		}

		completeSatisfiable(taskList, true);

//		log.debug("Total points {}", taskList.get);
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
