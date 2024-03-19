package com.antimated.tasklist.tasks;

import com.antimated.tasklist.TaskListConfig;
import com.antimated.tasklist.TaskListPlugin;
import com.antimated.tasklist.notifications.NotificationManager;
import com.antimated.tasklist.requirements.AnySkillLevelRequirement;
import com.antimated.tasklist.requirements.BaseSkillLevelRequirement;
import com.antimated.tasklist.requirements.CombatLevelRequirement;
import com.antimated.tasklist.requirements.SkillLevelRequirement;
import com.antimated.tasklist.requirements.SkillXpRequirement;
import com.antimated.tasklist.requirements.TotalLevelRequirement;
import com.antimated.tasklist.util.Util;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Experience;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
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

	private TaskList taskList;

	public void startUp()
	{
		eventBus.register(this);
		gson = new GsonBuilder().registerTypeAdapterFactory(Util.requirementAdapterFactory()).create();
		loginFlag = client.getGameState() == GameState.LOGGED_IN;
	}

	public void shutDown()
	{
		eventBus.unregister(this);
		taskList = null;
		loginFlag = false;
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
			log.debug("Logged in, attempting display tasks for now.");
			loginFlag = false;
			taskList = loadTaskList();

			completeSatisfiableTasks(false);

			//createDefaultTasks();
		}

		// Every game tick, check for completable tasks
		completeSatisfiableTasks(true);
	}

	private void createDefaultTasks()
	{
		TaskList tasks = new TaskList();

		// Any level tasks
		Map<Integer, TaskTier> ANY_LEVEL_TASKS = ImmutableMap.<Integer, TaskTier>builder()
			.put(2, TaskTier.EASY) // When level = 2 -> Level Up instead of Level 2
			.put(5, TaskTier.EASY)
			.put(10, TaskTier.EASY)
			.put(20, TaskTier.EASY)
			.put(30, TaskTier.MEDIUM)
			.put(40, TaskTier.MEDIUM)
			.put(50, TaskTier.MEDIUM)
			.put(60, TaskTier.HARD)
			.put(70, TaskTier.HARD)
			.put(80, TaskTier.HARD)
			.put(90, TaskTier.ELITE)
			.put(95, TaskTier.ELITE)
			.put(99, TaskTier.MASTER)
			.build();

		for (Map.Entry<Integer, TaskTier> entry : ANY_LEVEL_TASKS.entrySet())
		{
			int level = entry.getKey();
			TaskTier tier = entry.getValue();
			AnySkillLevelRequirement requirement = new AnySkillLevelRequirement(level);
			String description = "Achieve your First Level " + (level == 2 ? "Up" : level);
			boolean completed = requirement.satisfiesRequirement(client);

			tasks.add(new Task(description, tier, requirement, completed));
		}

		// Base level tasks
		Map<Integer, TaskTier> BASE_LEVEL_TASKS = ImmutableMap.<Integer, TaskTier>builder()
			.put(5, TaskTier.EASY)
			.put(10, TaskTier.EASY)
			.put(20, TaskTier.EASY)
			.put(30, TaskTier.MEDIUM)
			.put(40, TaskTier.MEDIUM)
			.put(50, TaskTier.MEDIUM)
			.put(60, TaskTier.HARD)
			.put(70, TaskTier.HARD)
			.put(80, TaskTier.ELITE)
			.put(90, TaskTier.ELITE)
			.build();

		for (Map.Entry<Integer, TaskTier> entry : BASE_LEVEL_TASKS.entrySet())
		{
			int level = entry.getKey();
			TaskTier tier = entry.getValue();
			BaseSkillLevelRequirement requirement = new BaseSkillLevelRequirement(level);
			String description = "Reach Base Level " + level;
			boolean completed = requirement.satisfiesRequirement(client);

			tasks.add(new Task(description, tier, requirement, completed));
		}

		// Level 99 tasks
		for (Skill skill : Skill.values())
		{
			// Level 99
			int level = Experience.MAX_REAL_LEVEL;
			SkillLevelRequirement requirement = new SkillLevelRequirement(skill, level);
			String description = "Reach Level " + level + " " + skill.getName();
			boolean completed = requirement.satisfiesRequirement(client);

			tasks.add(new Task(description, TaskTier.MASTER, requirement, completed));
		}


		Map<Integer, TaskTier> SKILL_XP_TASKS = ImmutableMap.<Integer, TaskTier>builder()
			.put(25_000_000, TaskTier.ELITE)
			.put(35_000_000, TaskTier.ELITE)
			.put(50_000_000, TaskTier.ELITE)
			.put(100_000_000, TaskTier.MASTER)
			.put(200_000_000, TaskTier.MASTER)
			.build();


		for (Skill skill : Skill.values())
		{
			for (Map.Entry<Integer, TaskTier> entry : SKILL_XP_TASKS.entrySet())
			{
				int xp = entry.getKey();
				TaskTier tier = entry.getValue();
				SkillXpRequirement requirement = new SkillXpRequirement(skill, xp);
				String description = "Obtain " + (xp / 1_000_000) + " Million " + skill.getName() + " XP";
				boolean completed = requirement.satisfiesRequirement(client);

				tasks.add(new Task(description, tier, requirement, completed));
			}
		}

		Map<Integer, TaskTier> TOTAL_LEVEL_TASKS = ImmutableMap.<Integer, TaskTier>builder()
			.put(100, TaskTier.EASY)
			.put(250, TaskTier.EASY)
			.put(500, TaskTier.EASY)
			.put(750, TaskTier.MEDIUM)
			.put(1000, TaskTier.MEDIUM)
			.put(1250, TaskTier.MEDIUM)
			.put(1500, TaskTier.HARD)
			.put(1750, TaskTier.HARD)
			.put(2000, TaskTier.ELITE)
			.put(2100, TaskTier.ELITE)
			.put(2200, TaskTier.ELITE)
			.put(2277, TaskTier.MASTER)
			.build();

		for (Map.Entry<Integer, TaskTier> entry : TOTAL_LEVEL_TASKS.entrySet())
		{
			int level = entry.getKey();
			TaskTier tier = entry.getValue();
			TotalLevelRequirement requirement = new TotalLevelRequirement(level);
			String description = "Reach Total Level " + level;
			boolean completed = requirement.satisfiesRequirement(client);

			tasks.add(new Task(description, tier, requirement, completed));
		}

		Map<Integer, TaskTier> COMBAT_LEVEL_TASKS = ImmutableMap.<Integer, TaskTier>builder()
			.put(10, TaskTier.EASY)
			.put(25, TaskTier.EASY)
			.put(50, TaskTier.MEDIUM)
			.put(75, TaskTier.MEDIUM)
			.put(100, TaskTier.HARD)
			.put(110, TaskTier.HARD)
			.put(120, TaskTier.ELITE)
			.put(126, TaskTier.ELITE)
			.build();

		for (Map.Entry<Integer, TaskTier> entry : COMBAT_LEVEL_TASKS.entrySet())
		{
			int level = entry.getKey();
			TaskTier tier = entry.getValue();
			CombatLevelRequirement requirement = new CombatLevelRequirement(level);
			String description = "Reach Combat Level " + level;
			boolean completed = requirement.satisfiesRequirement(client);

			tasks.add(new Task(description, tier, requirement, completed));
		}


		for (Task task : tasks)
		{
			log.debug("Task: {}", task);
		}

		log.debug("TaskJSON: {}", gson.toJson(tasks, TaskListConfig.TASK_LIST_TYPE));
	}

	/**
	 * Complete incomplete satisfiable tasks
	 *
	 * @param shouldNotify If completed tasks should throw a notification
	 */
	private void completeSatisfiableTasks(boolean shouldNotify)
	{
		TaskList satisfiableTasks = taskList
			.getSatisfyingTasks(client)
			.getTasksByCompletion(false);

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

			setRSProfileTasks(taskList);
		}
	}

	public TaskList loadTaskList()
	{
		TaskList list;

		if (getRSProfileTasks().isEmpty())
		{
			log.debug("Loading default tasks - no RS-profile tasks found.");
			list = getDefaultTasks();
			// Save tasks to RS profile if they have never been loaded.
			log.debug("Saving default tasks to RS-profile");
			setRSProfileTasks(list);
		}
		else
		{
			log.debug("Loading RS-profile tasks.");
			list = getRSProfileTasks();
		}

		return list;
	}

	/**
	 * Sets tasks to an RS profile
	 *
	 * @param tasks TaskList
	 */
	public void setRSProfileTasks(TaskList tasks)
	{
		if (tasks != null)
		{
			String json = gson.toJson(tasks, TaskListConfig.TASK_LIST_TYPE);

			configManager.setRSProfileConfiguration(TaskListConfig.GROUP_NAME, TaskListConfig.TASKS_KEY, json);
		}
		else
		{
			configManager.unsetRSProfileConfiguration(TaskListConfig.GROUP_NAME, TaskListConfig.TASKS_KEY);
		}
	}

	/**
	 * Gets tasks from an RS profile.
	 *
	 * @return TaskList
	 */
	public TaskList getRSProfileTasks()
	{
		String tasks = configManager.getRSProfileConfiguration(TaskListConfig.GROUP_NAME, TaskListConfig.TASKS_KEY, String.class);

		if (tasks != null)
		{
			return gson.fromJson(tasks, TaskListConfig.TASK_LIST_TYPE);
		}

		return new TaskList();
	}

	/**
	 * Gets the default tasks from plugin resources.
	 *
	 * @return TaskList
	 */
	public TaskList getDefaultTasks()
	{
		try (InputStream stream = TaskListPlugin.class.getResourceAsStream(TaskListConfig.DEFAULT_TASKS_FILE_NAME))
		{
			if (stream != null)
			{
				InputStreamReader definitionReader = new InputStreamReader(stream);
				return gson.fromJson(definitionReader, TaskListConfig.TASK_LIST_TYPE);
			}
			else
			{
				throw new NullPointerException("Default tasks file not found: " + TaskListConfig.DEFAULT_TASKS_FILE_NAME);
			}
		}
		catch (IOException ioe)
		{
			log.error("Error loading default tasks: ", ioe);
		}

		return new TaskList();
	}
}
