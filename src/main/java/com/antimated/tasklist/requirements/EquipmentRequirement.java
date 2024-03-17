package com.antimated.tasklist.requirements;

import com.antimated.tasklist.equipment.Equipment;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;

@Slf4j
@Getter
@ToString
public class EquipmentRequirement implements Requirement
{
	private final List<Equipment> equipment;

	public EquipmentRequirement(Equipment... equipment)
	{
		List<Equipment> uniques = new ArrayList<>();

		// Equipment may only be added if the passed slot is not added yet
		for (Equipment unique : equipment)
		{
			if (!containsEquipmentSlot(uniques, unique.getSlot()))
			{
				uniques.add(unique);
				continue;
			}

			log.warn("Duplicate slot {} added, skipping.", unique.getSlot());
		}

		this.equipment = List.copyOf(uniques);
	}

	private boolean containsEquipmentSlot(List<Equipment> slots, EquipmentInventorySlot slot)
	{
		for (Equipment equipment : slots)
		{
			if (equipment.getSlot() == slot)
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean satisfiesRequirement(Client client)
	{
		ItemContainer equipmentContainer = client.getItemContainer(InventoryID.EQUIPMENT);

		if (equipmentContainer == null)
		{
			return false;
		}

		for (Equipment e : equipment)
		{
			EquipmentInventorySlot slot = e.getSlot();
			List<Integer> items = e.getAllowedItems();
			Item item = equipmentContainer.getItem(slot.getSlotIdx());

			// Item not equipped
			if (item == null)
			{
				return false;
			}

			// Item equipped but not in the list of items to equip.
			if (!items.contains(item.getId()))
			{
				return false;
			}
		}

		return true;
	}
}
