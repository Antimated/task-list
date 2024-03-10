package com.antimated.tasklist.requirements;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

@Slf4j
@Getter
@RequiredArgsConstructor
@ToString
public class TotalLevelRequirement implements Requirement
{
	private final int level;

	@Override
	public boolean satisfiesRequirement(Client client)
	{
		return client.getTotalLevel() >= level;
	}
}
