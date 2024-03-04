package com.antimated.tasklist;

import com.antimated.tasklist.requirements.SkillXpRequirement;
import com.antimated.tasklist.tasks.AllSkillsTask;
import com.antimated.tasklist.tasks.Task;
import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.StatChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
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
	private TaskListConfig config;

	private boolean fetchStats; // Variable set if stats are fetched

	SkillXpRequirement herbloreXpRequirement = new SkillXpRequirement(Skill.HERBLORE, 12_171_496);

	@Override
	protected void startUp() throws Exception
	{
		log.info("Task list started!");

	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Task list stopped!");
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

			Task allSkillsTo90 = new AllSkillsTask(90);

			log.debug("Are all skills to lvl 90? {}", allSkillsTo90.satisfiesRequirements(client));

//			Player player = client.getLocalPlayer();
//
//			SkillRequirement agilityRequirement = new SkillRequirement(Skill.AGILITY, 99);
//
//			TotalRequirement totalLevelRequirement = new TotalRequirement(1200);
//			TotalXpRequirement totalXpRequirement = new TotalXpRequirement(300_000_000);
//
//			log.debug("Agility requirement: Current level: {}, Required level: {}, satisfiesRequirement: {}", client.getRealSkillLevel(Skill.AGILITY), agilityRequirement.getLevel(), agilityRequirement.satisfiesRequirement(client));
//			log.debug("Herblore requirement: Current xp: {}, Required xp: {}, satisfiesRequirement: {}", client.getSkillExperience(Skill.HERBLORE), herbloreXpRequirement.getXp(), herbloreXpRequirement.satisfiesRequirement(client));
//			log.debug("Total level requirement: Current level: {}, Required level: {}, satisfiesRequirement: {}", client.getTotalLevel(), totalLevelRequirement.getTotalLevel(), totalLevelRequirement.satisfiesRequirement(client));
//			log.debug("Total xp requirement: Current total xp: TODO, Required total xp: {}, satisfiesRequirement: {}", totalXpRequirement.getTotalXp(), totalXpRequirement.satisfiesRequirement(client));
			fetchStats = false;
		}
	}
	@Subscribe
	public void onStatChanged(StatChanged statChanged)
	{
		if (statChanged == null) {
			return;
		}

//		if (statChanged.getSkill() == Skill.HERBLORE) {
//			if(herbloreXpRequirement.satisfiesRequirement(client))
//			{
//				log.debug("Herblore requirement passed, now dont trigger anymore ktnxbye");
//			} else {
//				log.debug("Herblore xp requirement not met, still need {} xp", herbloreXpRequirement.getXp() - client.getSkillExperience(Skill.HERBLORE));
//			}
//		}

//		log.debug("Changed stat: {}", statChanged.toString());
//		SkillRequirement agilityReq = new SkillRequirement(Skill.AGILITY, 80);
//		TotalRequirement totalLevelRequirement = new TotalRequirement(1200);
//		log.debug("Agility requirement: Has level? {}", agilityReq.toString());
//		log.debug("Total level requirement: Has level? {}", totalLevelRequirement.toString());
	}

	@Provides
	TaskListConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TaskListConfig.class);
	}
}
