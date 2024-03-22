package com.antimated.tasklist.tasks;

import com.antimated.tasklist.TaskListConfig;
import com.antimated.tasklist.notifications.NotificationManager;
import com.antimated.tasklist.tasks.lists.GenericTaskList;
import com.antimated.tasklist.tasks.lists.LevelTaskList;
import com.antimated.tasklist.tasks.lists.TaskList;
import com.antimated.tasklist.tasks.lists.XpTaskList;
import com.antimated.tasklist.util.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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

	private List<TaskList> taskLists;

	public void startUp()
	{
		eventBus.register(this);
		gson = new GsonBuilder()
			.registerTypeAdapterFactory(Util.requirementAdapterFactory())
//			.registerTypeAdapterFactory(Util.taskListAdapterFactory())
			.create();
		loginFlag = client.getGameState() == GameState.LOGGED_IN;
	}

	public void shutDown()
	{
		eventBus.unregister(this);
		loginFlag = false;
		clearTaskLists();
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
		if (Util.isPlayerWithinMapRegion(client, Util.LAST_MAN_STANDING_REGIONS))
		{
			return;
		}

		if (loginFlag)
		{
			loginFlag = false;
			loadTaskLists();
		}

		// Every game tick, check for completable tasks
		completeTasks(true);
	}


	public void loadTaskLists()
	{
		List<TaskList> savedTaskLists = getSavedTaskLists();
		List<TaskList> defaultTaskLists = getDefaultTaskLists();

		if (!savedTaskLists.isEmpty())
		{
			log.info("Saved task lists found");
			// Saved tasks found, start up patch task lists.
			List<TaskList> patchedTaskLists = new ArrayList<>();

			// Loop through default task lists
			for (TaskList defaultTaskList : defaultTaskLists)
			{
				TaskList savedTaskList = findTaskList(savedTaskLists, defaultTaskList);
				GenericTaskList patchedTaskList = new GenericTaskList(defaultTaskList.getName());

				// We found a TaskList with the same name
				if (savedTaskList != null)
				{
					log.debug("Patching task list '{}'", defaultTaskList.getName());

					for (Task task : defaultTaskList.getTasks())
					{
						Task foundTask = findTask(savedTaskList, task);

						if (foundTask != null)
						{
							patchedTaskList.add(foundTask);
							//log.debug("Task '{}' found in saved list. Patched into '{}'", task.getDescription(), defaultTaskList.getName());
						}
						else
						{
							patchedTaskList.add(task);
							log.debug("Task '{}' not found in saved list. Using default from '{}'", task.getDescription(), defaultTaskList.getName());
						}
					}

					patchedTaskLists.add(patchedTaskList);
				}
				else
				{
					log.debug("No saved task list found for '{}'. Using default task list.", defaultTaskList.getName());

					for (Task task : defaultTaskList.getTasks())
					{
						patchedTaskList.add(task);
						//log.debug("Task '{}' from default added to '{}'", task.getDescription(), defaultTaskList.getName());
					}

					patchedTaskLists.add(patchedTaskList);
				}
			}

			taskLists = patchedTaskLists;
			log.info("Task lists patched successfully.");
		}
		else
		{
			taskLists = defaultTaskLists;
			log.info("No saved task lists found. Using default task lists.");

		}

		completeTasks(false);
		saveTaskLists(taskLists);
	}

	private TaskList findTaskList(List<TaskList> lists, TaskList list)
	{
		for (TaskList l : lists)
		{
			if (l.getName().equals(list.getName()))
			{
				return l;
			}
		}
		return null;
	}

	private Task findTask(TaskList list, Task task)
	{
		for (Task t : list.getTasks())
		{
			if (t.getDescription().equals(task.getDescription()))
			{
				return t;
			}
		}

		return null;
	}

	private List<TaskList> getSavedTaskLists()
	{
		String lists = configManager.getRSProfileConfiguration(TaskListConfig.GROUP_NAME, TaskListConfig.TASK_LISTS_KEY, String.class);

		if (lists != null)
		{
			return gson.fromJson(lists, TaskListConfig.TASK_LIST_TYPE);
		}

		return new ArrayList<>();
	}

	private List<TaskList> getDefaultTaskLists()
	{
		List<TaskList> lists = new ArrayList<>();

		lists.add(new LevelTaskList());
		lists.add(new XpTaskList());

		return lists;
	}


	private void completeTasks(boolean shouldNotify)
	{
		// Only save when tasks have been completed
		boolean shouldSave = false;

		for (TaskList taskList : taskLists)
		{
			List<Task> completableTasks = taskList.getTasks()
				.stream()
				.filter(task -> !task.isCompleted() && task.getRequirement().satisfiesRequirement(client))
				.collect(Collectors.toList());

			if (!completableTasks.isEmpty())
			{
				log.debug("Found completable tasks in '{}'...", taskList.getName());

				for (Task task : completableTasks)
				{
					task.setCompleted(true);

					if (shouldNotify)
					{
						notificationManager.addNotification(task);
					}

					log.debug(("Completing task: {} {}").trim(), task.getDescription(), shouldNotify ? "(and notify)." : "");
				}

				shouldSave = true;
			}
		}

		if (shouldSave)
		{
			// Save task lists on RS-profile
			log.debug("Tasks have been completed so we should save.");
			saveTaskLists(taskLists);
		}
	}

	public void clearTaskLists()
	{
		if (taskLists != null)
		{
			taskLists.clear();
		}
	}

	public void saveTaskLists(List<TaskList> lists)
	{
		if (lists != null)
		{
			String json = gson.toJson(lists, TaskListConfig.TASK_LIST_TYPE);
			log.debug("Saving task lists to RS-profile");
			configManager.setRSProfileConfiguration(TaskListConfig.GROUP_NAME, TaskListConfig.TASK_LISTS_KEY, json);
		}
		else
		{
			log.debug("Clearing task lists from RS-profile");
			configManager.unsetRSProfileConfiguration(TaskListConfig.GROUP_NAME, TaskListConfig.TASK_LISTS_KEY);
		}
	}
}
