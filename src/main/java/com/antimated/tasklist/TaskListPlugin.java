package com.antimated.tasklist;

import com.antimated.tasklist.notifications.NotificationManager;
import com.antimated.tasklist.tasks.TaskListManager;
import com.google.inject.Provides;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.http.api.item.ItemEquipmentStats;

@Slf4j
@PluginDescriptor(
	name = "Task list"
)
public class TaskListPlugin extends Plugin
{
	@Inject
	private TaskListConfig config;

	@Inject
	private TaskListManager taskListManager;

	@Inject
	private NotificationManager notifications;

	@Inject
	private ConfigManager configManager;

	@Inject
	private Client client;

	@Subscribe
	public void onGameTick(GameTick gameTick)
	{
		if (client.getLocalPlayer() != null)
		{

//			Set<Integer> armourSet = new HashSet<>();

//			log.debug("Equiped steel full helm? {}", isItemInSlot(EquipmentInventorySlot.HEAD, ItemID.STEEL_FULL_HELM));
//			log.debug("Equiped steel platebody? {}", isItemInSlot(EquipmentInventorySlot.BODY, ItemID.STEEL_PLATEBODY, ItemID.STEEL_PLATESKIRT));
			//log.debug("Equiped steel platelegs/plateskirt? {}", isItemInSlot(EquipmentInventorySlot.LEGS, ItemID.STEEL_PLATELEGS, ItemID.STEEL_PLATESKIRT));
//			for (EquipmentInventorySlot slot : equipmentSlots) {
//				log.debug("Slot: {} with index: {}", slot, slot.getSlotIdx());
//			}
//			armourSet.add(ItemID.STEEL_FULL_HELM);
//			armourSet.add(ItemID.STEEL_PLATEBODY);
//			armourSet.add(ItemID.STEEL_PLATELEGS);
//			armourSet.add(ItemID.STEEL_KITESHIELD);
			//log.debug("Player name: {}", client.getLocalPlayer().getName());

		}
	}

	private boolean isItemInSlot(EquipmentInventorySlot slot, int... itemIds)
	{
		ItemContainer container = client.getItemContainer(InventoryID.EQUIPMENT);

		if (container != null)
		{
			Item item = container.getItem(slot.getSlotIdx());

			if (item != null)
			{
				for (int itemId : itemIds)
				{
					if (item.getId() == itemId)
					{
						return true;
					}
				}
			}
		}

		return false;
	}

	//	private boolean isItemInSlot(EquipmentInventorySlot slot, int... itemIds)
//	{
//		ItemContainer container = client.getItemContainer(InventoryID.EQUIPMENT);
//
//		if (container != null) {
//			Item item = container.getItem(slot.getSlotIdx());
//
//			if (item != null) {
//				for (int itemId : itemIds) {
//					if (item.getId() == itemId) {
//						return true;
//					}
//				}
//			}
//		}
//
//		return false;
//	}
	@Override
	protected void startUp() throws Exception
	{
		log.info("Task list started!");
		notifications.startUp();
		taskListManager.startUp();
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Task list stopped!");
		notifications.shutDown();
		taskListManager.shutDown();
	}

	@Provides
	TaskListConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TaskListConfig.class);
	}
}
