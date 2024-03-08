package com.antimated.tasklist;

import com.antimated.tasklist.json.TaskDeserializer;
import com.antimated.tasklist.json.TaskSerializer;
import com.antimated.tasklist.tasks.Task;
import com.antimated.tasklist.tasks.TaskList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
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
import net.runelite.api.events.GameTick;
import net.runelite.client.RuneLite;
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

	private static final String DEFAULT_TASKS_FILE = "default-tasks.json";

	private static final File TASK_LIST_DIR = new File(RuneLite.RUNELITE_DIR, "task-list");

	private static final Type type = new TypeToken<List<Task>>(){}.getType();

	@Getter
	private TaskList taskList;

	public void loadTasks()
	{
		gson = new GsonBuilder()
			.registerTypeAdapter(Task.class, new TaskDeserializer())
			.registerTypeAdapter(Task.class, new TaskSerializer())
			.create();

		try (InputStream stream = TaskListPlugin.class.getResourceAsStream(DEFAULT_TASKS_FILE))
		{
			assert stream != null;
			InputStreamReader definitionReader = new InputStreamReader(stream);

			List<Task> tasks = gson.fromJson(definitionReader, type);

			taskList = new TaskList(tasks);
		}
		catch (IOException e)
		{
			log.warn("Error loading default tasks", e);
		}
	}

	@Subscribe
	public void onProfileChanged(ProfileChanged e)
	{
		log.debug("Profile changed!!!!!!!!");
	}

	public void clearTasks() {
		taskList = new TaskList(List.of());
	}

	public void startUp()
	{
		eventBus.register(this);
		loadTasks();
	}

	public void shutDown()
	{
		eventBus.unregister(this);
		clearTasks();
	}
}
