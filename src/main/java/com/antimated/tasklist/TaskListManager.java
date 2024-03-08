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
import java.util.ArrayList;
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
import static net.runelite.client.RuneLite.SCREENSHOT_DIR;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ProfileChanged;

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

	private static final String TASKS_FILE = "tasks.json";

	private static final String DEFAULT_TASKS_FILE = "default-tasks.json";

	private static final File TASK_LIST_DIR = new File(RuneLite.RUNELITE_DIR, "task-list");

	private static final Type type = new TypeToken<List<Task>>()
	{
	}.getType();

	@Getter
	private TaskList taskList;

	private boolean shouldLoadTasks;

	public void loadTasks() throws IOException
	{
		log.debug("Attempting to load tasks...");
		gson = new GsonBuilder()
			.registerTypeAdapter(Task.class, new TaskDeserializer())
			.registerTypeAdapter(Task.class, new TaskSerializer())
			.create();

		taskList = loadTasksFromProfile();

		for (Task task : taskList.getTasks())
		{
			log.debug("Task loaded: {}", task);
		}
	}


	// Loads the default tasks list if the plugin hasn't created a savefile yet
	public List<Task> loadDefaultTasks()
	{
		try (InputStream stream = TaskListPlugin.class.getResourceAsStream(DEFAULT_TASKS_FILE))
		{
			assert stream != null;
			InputStreamReader definitionReader = new InputStreamReader(stream);

			return gson.fromJson(definitionReader, type);
		}
		catch (IOException e)
		{
			log.warn("Error loading default tasks", e);
			return null;
		}
	}

	public TaskList loadTasksFromProfile() throws IOException
	{
		File tasksFile = new File(getPluginFolder(), TASKS_FILE);
		List<Task> list;

		try (FileInputStream stream = new FileInputStream(tasksFile))
		{
			InputStreamReader definitionReader = new InputStreamReader(stream);
			list = gson.fromJson(definitionReader, type);

			log.debug("Task list loaded from user profile...");
			return new TaskList(list);
		}
		catch (FileNotFoundException e)
		{
			log.debug("Task list for user not found, loading default task list...");
			list = loadDefaultTasks();

			log.debug("Writing {}", tasksFile.getAbsolutePath());
			FileWriter file = new FileWriter(tasksFile);
			file.write(gson.toJson(list));
			file.close();

			return new TaskList(list);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public File getPluginFolder()
	{
		log.debug("Attempting to create plugin folders...");
		File playerFolder;

		if (client.getLocalPlayer() != null && client.getLocalPlayer().getName() != null)
		{
			String playerDir = client.getLocalPlayer().getName();

			playerFolder = new File(TASK_LIST_DIR, playerDir);
		}
		else
		{
			playerFolder = TASK_LIST_DIR;
		}

		if (playerFolder.mkdirs())
		{
			log.debug("Folder created at {}", playerFolder.getAbsolutePath());
		}
		else if (playerFolder.exists())
		{
			log.debug("Folders already created at {}", playerFolder.getAbsolutePath());
		}

		return playerFolder;
	}

//	public void getTasks()
//	{
//		if (taskList != null)
//		{
//			String jsonString = gson.toJson(taskList);
//			log.debug(jsonString);
//		}
//	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		// Check when user logs in with plugin enabled if we should start loading tasks6
		if (!shouldLoadTasks && gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			shouldLoadTasks = true;
		}
	}


	@Subscribe
	public void onGameTick(GameTick gameTick) throws IOException
	{
		if (shouldLoadTasks)
		{
			shouldLoadTasks = false;
			loadTasks();
		}
	}

	public void clearTasks()
	{
		taskList = null;
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
