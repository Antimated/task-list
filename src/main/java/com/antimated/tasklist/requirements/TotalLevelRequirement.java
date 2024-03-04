package com.antimated.tasklist.requirements;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

@Slf4j
@RequiredArgsConstructor
@Getter
public class TotalLevelRequirement implements Requirement
{
	private final int totalLevel;

	@Override
	public String toString()
	{
		return totalLevel + " Total";
	}
	@Override
	public boolean satisfiesRequirement(Client client)
	{
		return client.getTotalLevel() >= totalLevel;
	}
}
