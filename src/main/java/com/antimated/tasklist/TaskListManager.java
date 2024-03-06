package com.antimated.tasklist;

import com.antimated.tasklist.json.TaskDeserializer;
import com.antimated.tasklist.json.TaskSerializer;
import com.antimated.tasklist.requirements.Requirement;
import com.antimated.tasklist.tasks.Task;
import com.antimated.tasklist.tasks.TaskTier;
import com.antimated.tasklist.tasks.TaskType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonStreamParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
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

	private static final String DEFAULT_TASKS_FILE = "default-tasks.json";

	private static final Type type = new TypeToken<List<Task>>(){}.getType();

	@Getter
	private List<Task> tasks;

	public List<Task> loadTasks()
	{
		gson = new GsonBuilder()
			.registerTypeAdapter(Task.class, new TaskDeserializer())
			.registerTypeAdapter(Task.class, new TaskSerializer())
			.create();

		try (InputStream stream = TaskListPlugin.class.getResourceAsStream(DEFAULT_TASKS_FILE))
		{
			assert stream != null;
			InputStreamReader definitionReader = new InputStreamReader(stream);

			return gson.fromJson(definitionReader, type);
		}
		catch (IOException e)
		{
			log.warn("Error loading default tasks", e);
		}

		return List.of();
	}

	public Task getTaskById(int id) {
		return tasks.stream()
			.filter(task -> task.getId() == id)
			.findFirst()
			.orElse(null);
	}

	public List<Task> getTasksByType(TaskType type)
	{
		return tasks.stream()
			.filter(task -> task.getType() == type)
			.collect(Collectors.toList());
	}

	public List<Task> getTasksByTier(TaskTier tier)
	{
		return tasks.stream()
			.filter(task -> task.getTier() == tier)
			.collect(Collectors.toList());
	}

	public List<Task> getIncompleteTasks()
	{
		return tasks.stream()
			.filter(task -> !task.isCompleted())
			.collect(Collectors.toList());
	}

	public List<Task> getCompleteTasks()
	{
		return tasks.stream()
			.filter(Task::isCompleted)
			.collect(Collectors.toList());
	}

	@Subscribe
	public void onGameTick(GameTick gameTick)
	{
//		log.debug("Gametick update inside TaskListManager.");
//		log.debug("List of tasks {}", tasks);
	}

	public void startUp()
	{
		eventBus.register(this);
		tasks = loadTasks();
		log.debug("Deserialized tasks {}", gson.toJson(tasks));
	}

	public void shutDown()
	{
		eventBus.unregister(this);
		tasks = List.of();
	}
}
