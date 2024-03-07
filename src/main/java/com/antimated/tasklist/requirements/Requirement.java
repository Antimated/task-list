package com.antimated.tasklist.requirements;

import net.runelite.api.Client;

public interface Requirement
{
	boolean satisfiesRequirement(Client client);
}
