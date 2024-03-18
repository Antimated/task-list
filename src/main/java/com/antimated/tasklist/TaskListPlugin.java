package com.antimated.tasklist;

import com.antimated.tasklist.notifications.NotificationManager;
import com.antimated.tasklist.tasks.TaskListManager;
import com.google.common.primitives.Ints;
import com.google.inject.Provides;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Experience;
import net.runelite.api.Skill;
import net.runelite.api.events.CommandExecuted;
import net.runelite.api.events.StatChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Task list",
	conflicts = "Milestone Levels"
)
public class TaskListPlugin extends Plugin
{
	@Inject
	private TaskListConfig config;

	@Inject
	private TaskListManager taskListManager;

	@Inject
	private NotificationManager notificationManager;

	@Inject
	private ConfigManager configManager;

	@Inject
	private ClientThread clientThread;

	@Inject
	private EventBus eventBus;

	@Inject
	private Client client;

	@Inject
	@Named("developerMode")
	boolean developerMode;

	@Provides
	TaskListConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TaskListConfig.class);
	}

	@Override
	protected void startUp()
	{
		log.info("TaskListPlugin started!");
		notificationManager.startUp();
		taskListManager.startUp();
	}

	@Override
	protected void shutDown()
	{
		log.info("TaskListPlugin stopped!");
		notificationManager.shutDown();
		taskListManager.shutDown();
	}


	@Subscribe
	public void onCommandExecuted(CommandExecuted commandExecuted)
	{
		if (developerMode)
		{
			String[] args = commandExecuted.getArguments();
			switch (commandExecuted.getCommand())
			{
				// Adds an absurd amount of notifications
				case "notify":
					for (int i = 1; i <= 5000; i++)
					{
						notificationManager.addNotification("Test notification", "Notification added: " + i);
					}

					break;

				// Reset tasks and set loginFlag to true so tasks get loaded in again
				case "reset":
					taskListManager.setRSProfileTasks(null);
					taskListManager.loginFlag = true;

					break;

				case "setstats":

					for (Skill skill : Skill.values())
					{
						int level = Integer.parseInt(args[0]);

						if (skill == Skill.HITPOINTS && level < 10)
						{
							level = 10;
						}


						level = Ints.constrainToRange(level, 1, Experience.MAX_REAL_LEVEL);
						int xp = Experience.getXpForLevel(level);

						client.getBoostedSkillLevels()[skill.ordinal()] = level;
						client.getRealSkillLevels()[skill.ordinal()] = level;
						client.getSkillExperiences()[skill.ordinal()] = xp;

						client.queueChangedSkill(skill);

						StatChanged statChanged = new StatChanged(
							skill,
							xp,
							level,
							level
						);
						eventBus.post(statChanged);
					}
					break;
			}
		}
	}
}
