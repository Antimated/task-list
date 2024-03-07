package com.antimated.tasklist;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.StatChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

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

	private boolean fetchStats; // Variable set if stats are fetched

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
			fetchStats = true;
		}
	}

	@Subscribe
	public void onGameTick(GameTick gameTick)
	{
		// Get initial stats, quests, equipment
		if (fetchStats) {
			fetchStats = false;
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
