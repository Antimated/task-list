package com.antimated.tasklist;

import com.antimated.tasklist.tasks.Task;
import com.antimated.tasklist.tasks.TaskTier;
import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Prayer;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.StatChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import static net.runelite.client.plugins.banktags.BankTagsPlugin.CONFIG_GROUP;

@Slf4j
@PluginDescriptor(
	name = "Task list"
)
public class TaskListPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private EventBus eventBus;
	@Inject
	private TaskListConfig config;

	@Inject
	private TaskListManager taskListManager;

	@Inject
	private ConfigManager configManager;

	private boolean canFetchStats; // Variable set if stats are fetched

	@Override
	protected void startUp() throws Exception
	{
		log.info("Task list started!");
		taskListManager.startUp();
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Task list stopped!");
		taskListManager.shutDown();
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			canFetchStats = true;
		}
	}

	@Subscribe
	public void onGameTick(GameTick gameTick)
	{
		// Get initial stats, quests, equipment
		if (canFetchStats) {
			canFetchStats = false;

			for (Task task: taskListManager.getTaskList().getTasksByTiers(TaskTier.ELITE).getTasksByCompletion(true).all())
			{
				log.debug("All incompleted hard tasks {}", task);
			}

			for (Task task: taskListManager.getTaskList().getTasksByTiers(TaskTier.EASY, TaskTier.HARD).getTasksByCompletion(false).all())
			{
				log.debug("All incompleted easy tasks {}", task);
			}

			for (Task task: taskListManager.getTaskList().getTasksByCompletion(false).getSatisfyingTasks(client).all())
			{
				task.complete(true);

				log.debug("{}", task.isCompleted());
			}

			for (Task task: taskListManager.getTaskList().getTasksByCompletion(false).getSatisfyingTasks(client).all())
			{
				task.complete(true);

				log.debug("Are they completed? {}", task);
			}


			for (Task task: taskListManager.getTaskList().all())
			{
				log.debug("All tasks again {}", task);
			}
		}
	}
	@Subscribe
	public void onStatChanged(StatChanged statChanged)
	{
		if (statChanged == null) {
			return;
		}

		log.debug("Stat changed");
	}

	@Provides
	TaskListConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TaskListConfig.class);
	}
}
