package com.antimated.tasklist.requirements;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.VarPlayer;

@Slf4j
@Getter
@RequiredArgsConstructor
@ToString
public class QuestPointRequirement implements Requirement
{
	private final int qp;

	@Override
	public boolean satisfiesRequirement(Client client)
	{
		return client.getVarpValue(VarPlayer.QUEST_POINTS) >= qp;
	}
}
