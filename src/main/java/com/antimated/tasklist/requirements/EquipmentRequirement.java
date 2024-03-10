package com.antimated.tasklist.requirements;

import com.antimated.tasklist.util.Util;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;

@Slf4j
@Getter
@RequiredArgsConstructor
@ToString
public class EquipmentRequirement implements Requirement
{
	/**
	 * JSON format
	 * "equipment": {
	 * 		"HEAD": [
	 * 		  "STEEL_FULL_HELM"
	 * 		],
	 * 		"BODY": [
	 * 		  "STEEL_PLATEBODY"
	 * 		],
	 * 		"LEGS": [
	 * 		  "STEEL_PLATELEGS",
	 * 		  "STEEL_PLATESKIRT"
	 * 		],
	 * 		"SHIELD": [
	 * 		  "STEEL_KITESHIELD"
	 * 		]
	 * }
	 */
	private final Map<EquipmentInventorySlot, Set<String>> equipment;

	@Override
	public boolean satisfiesRequirement(Client client)
	{
		ItemContainer equipmentContainer = client.getItemContainer(InventoryID.EQUIPMENT);

		if (equipmentContainer == null)
		{
			return false;
		}

		for (Map.Entry<EquipmentInventorySlot, Set<String>> entry : equipment.entrySet())
		{
			// Get equipment slots
			EquipmentInventorySlot equipmentSlot = entry.getKey();

			// Convert item names to item IDs
			Set<Integer> allowedItemIds = entry.getValue()
				.stream()
				.mapToInt(Util::getItemID)
				.boxed()
				.collect(Collectors.toSet());

			// Get the item in the specified equipment slot
			Item item = equipmentContainer.getItem(equipmentSlot.getSlotIdx());

			// Slot is not equipped
			if (item == null) {
				return false;
			}

			// Equipped item is not in list of allowed items
			if (!allowedItemIds.contains(item.getId())) {
				return false;
			}
		}

		return true;
	}
}
