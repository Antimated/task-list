package com.antimated.tasklist;

import com.antimated.tasklist.json.TaskDeserializer;
import com.antimated.tasklist.json.TaskSerializer;
import com.antimated.tasklist.requirements.Requirement;
import com.antimated.tasklist.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonStreamParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;

@Slf4j
@Singleton
public class TaskListManager
{
	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private Gson gson;

	private static final String DEFAULT_TASKS_FILE = "default-tasks.json";

//	public TaskListManager()
//	{
//
//	}
	public List<Task> loadTasks()
	{
		gson = new GsonBuilder()
			.registerTypeAdapter(Task.class, new TaskDeserializer())
			.registerTypeAdapter(Task.class, new TaskSerializer())
			.create();
		Type typeOfT = new TypeToken<List<Task>>(){}.getType();

		try (InputStream stream = TaskListPlugin.class.getResourceAsStream(DEFAULT_TASKS_FILE))
		{
			assert stream != null;
			InputStreamReader definitionReader = new InputStreamReader(stream);

			return gson.fromJson(definitionReader, typeOfT);
		}
		catch (IOException e)
		{
			log.warn("Error loading default tasks", e);
		}

		return List.of();
	}
}
