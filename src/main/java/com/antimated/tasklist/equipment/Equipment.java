package com.antimated.tasklist.equipment;

import com.google.common.collect.ImmutableList;
import java.util.List;
import lombok.Getter;
import lombok.ToString;
import net.runelite.api.EquipmentInventorySlot;

@Getter
@ToString
public class Equipment
{
	private final EquipmentInventorySlot slot;
	private final List<Integer> allowedItems;

	public Equipment(EquipmentInventorySlot slot, Integer... items)
	{
		this.slot = slot;
		this.allowedItems = ImmutableList.copyOf(items);
	}
}
