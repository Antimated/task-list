package com.antimated.tasklist.requirements;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.VarPlayer;

@Slf4j
@Data
public class QuestPointRequirement implements Requirement
{
	private final int qp;

	@Override
	public boolean satisfiesRequirement(Client client)
	{
		return client.getVarpValue(VarPlayer.QUEST_POINTS) >= qp;
	}
}
