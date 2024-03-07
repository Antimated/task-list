package com.antimated.tasklist.requirements;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

@Slf4j
@Data
public class TotalLevelRequirement implements Requirement
{
	private final int level;

	@Override
	public boolean satisfiesRequirement(Client client)
	{
		return client.getTotalLevel() >= level;
	}
}
