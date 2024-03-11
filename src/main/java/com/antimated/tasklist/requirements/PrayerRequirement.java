package com.antimated.tasklist.requirements;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Prayer;

@Slf4j
@Getter
@RequiredArgsConstructor
@ToString
public class PrayerRequirement implements Requirement
{
	private final Prayer prayer;

	@Override
	public boolean satisfiesRequirement(Client client)
	{
		return client.isPrayerActive(prayer);
	}
}
