package com.antimated.tasklist;

import com.antimated.tasklist.json.TaskDeserializer;
import com.antimated.tasklist.json.TaskSerializer;
import com.antimated.tasklist.notifications.NotificationManager;
import com.antimated.tasklist.tasks.Task;
import com.antimated.tasklist.tasks.TaskListManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Provides;
import java.lang.reflect.Type;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.events.CommandExecuted;
import net.runelite.client.config.ConfigManager;
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
	log.debug("COMMAND EXECUTED");
	if (developerMode)
	{
		String[] args = commandExecuted.getArguments();
		switch (commandExecuted.getCommand())
		{
//			case "level":
//				if (args.length == 2)
//				{
//					Skill skill = Skill.valueOf(args[0].toUpperCase());
//					int currentLevel = Integer.parseInt(args[1]);
//
//					onLevelUp(skill, currentLevel);
//				}
//				else
//				{
//					log.debug("Invalid number of arguments for ::level command. Expected 2 got {}.", args.length);
//				}
//				break;

			case "test":
//				for (int i = 1; i <= 500; i++)
//				{
//					notifications.addNotification("Notification", "Test notification number: <col=ffffff>" + i + "</col>");
//				}
				String userJson = "[{\"id\":1,\"description\":\"Get level 60 in any skill\",\"tier\":\"EASY\",\"type\":\"ANY_SKILL_LEVEL\",\"requirement\":{\"level\":60},\"completed\":false},{\"id\":2,\"description\":\"Get 20M xp in any skill\",\"tier\":\"EASY\",\"type\":\"ANY_SKILL_XP\",\"requirement\":{\"xp\":20000000},\"completed\":false},{\"id\":3,\"description\":\"Get level 50 in Attack\",\"tier\":\"EASY\",\"type\":\"SKILL_LEVEL\",\"requirement\":{\"skill\":\"ATTACK\",\"level\":60},\"completed\":false},{\"id\":4,\"description\":\"Get level 1200 total\",\"tier\":\"EASY\",\"type\":\"TOTAL_LEVEL\",\"requirement\":{\"level\":1200},\"completed\":false},{\"id\":5,\"description\":\"Get 300M total xp\",\"tier\":\"ELITE\",\"type\":\"TOTAL_XP\",\"requirement\":{\"xp\":300000000},\"completed\":false},{\"id\":6,\"description\":\"Complete the Fremenik Trials\",\"tier\":\"ELITE\",\"type\":\"QUEST\",\"requirement\":{\"quest\":\"THE_FREMENNIK_TRIALS\"},\"completed\":false},{\"id\":7,\"description\":\"Complete the Children of the Sun\",\"tier\":\"ELITE\",\"type\":\"QUEST\",\"requirement\":{\"quest\":\"CHILDREN_OF_THE_SUN\"},\"completed\":false},{\"id\":7,\"description\":\"Equip a full set of steel\",\"tier\":\"EASY\",\"type\":\"EQUIPMENT\",\"requirement\":{\"equipment\":{\"HEAD\":[\"STEEL_FULL_HELM\"],\"BODY\":[\"STEEL_PLATEBODY\"],\"LEGS\":[\"STEEL_PLATELEGS\",\"STEEL_PLATESKIRT\"],\"SHIELD\":[\"STEEL_KITESHIELD\"]}},\"completed\":false},{\"id\":8,\"description\":\"Enable the Piety prayer\",\"tier\":\"HARD\",\"type\":\"PRAYER\",\"requirement\":{\"prayer\":\"PIETY\"},\"completed\":false}]";
				String defaultJson = "[{\"id\":1,\"description\":\"Get level 60 in any skill\",\"tier\":\"EASY\",\"type\":\"ANY_SKILL_LEVEL\",\"requirement\":{\"level\":60},\"completed\":false},{\"id\":2,\"description\":\"Get 20M xp in any skill\",\"tier\":\"EASY\",\"type\":\"ANY_SKILL_XP\",\"requirement\":{\"xp\":20000000},\"completed\":false},{\"id\":3,\"description\":\"Get level 50 in Attack\",\"tier\":\"EASY\",\"type\":\"SKILL_LEVEL\",\"requirement\":{\"skill\":\"ATTACK\",\"level\":60},\"completed\":false},{\"id\":4,\"description\":\"Get level 1200 total\",\"tier\":\"EASY\",\"type\":\"TOTAL_LEVEL\",\"requirement\":{\"level\":1200},\"completed\":false},{\"id\":5,\"description\":\"Get 300M total xp\",\"tier\":\"ELITE\",\"type\":\"TOTAL_XP\",\"requirement\":{\"xp\":300000000},\"completed\":false},{\"id\":6,\"description\":\"Complete the Fremenik Trials\",\"tier\":\"ELITE\",\"type\":\"QUEST\",\"requirement\":{\"quest\":\"THE_FREMENNIK_TRIALS\"},\"completed\":false},{\"id\":7,\"description\":\"Complete the Children of the Sun\",\"tier\":\"ELITE\",\"type\":\"QUEST\",\"requirement\":{\"quest\":\"CHILDREN_OF_THE_SUN\"},\"completed\":false},{\"id\":7,\"description\":\"Equip a full set of steel\",\"tier\":\"EASY\",\"type\":\"EQUIPMENT\",\"requirement\":{\"equipment\":{\"HEAD\":[\"STEEL_FULL_HELM\"],\"BODY\":[\"STEEL_PLATEBODY\"],\"LEGS\":[\"STEEL_PLATELEGS\",\"STEEL_PLATESKIRT\"],\"SHIELD\":[\"STEEL_KITESHIELD\"]}},\"completed\":true},{\"id\":8,\"description\":\"Enable the Piety prayer\",\"tier\":\"HARD\",\"type\":\"PRAYER\",\"requirement\":{\"prayer\":\"PIETY\"},\"completed\":true},{\"id\":8,\"description\":\"Enable the Chivalry prayer\",\"tier\":\"HARD\",\"type\":\"PRAYER\",\"requirement\":{\"prayer\":\"CHIVALRY\"},\"completed\":false}]";

				Gson gson = new GsonBuilder()
					.registerTypeAdapter(Task.class, new TaskDeserializer())
					.registerTypeAdapter(Task.class, new TaskSerializer())
					.setPrettyPrinting()
					.create();
				Type mapType = new TypeToken<List<Task>>() {}.getType();
				List<Task> defaultList = gson.fromJson(defaultJson, mapType);
				List<Task> userList = gson.fromJson(userJson, mapType);
//			  System.out.println(Maps.difference(firstMap, secondMap));
				log.debug("defaultList {}", defaultList);
				log.debug("userList {}", userList);
				break;
		}
	}
}
}
