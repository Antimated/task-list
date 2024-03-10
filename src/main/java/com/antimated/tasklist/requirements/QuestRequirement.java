package com.antimated.tasklist.requirements;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Quest;
import net.runelite.api.QuestState;

@Slf4j
@Getter
@RequiredArgsConstructor
@ToString
public class QuestRequirement implements Requirement
{
	private final Quest quest;

	@Override
	public boolean satisfiesRequirement(Client client)
	{
		QuestState questState = quest.getState(client);
		return questState == QuestState.FINISHED;
	}
}
