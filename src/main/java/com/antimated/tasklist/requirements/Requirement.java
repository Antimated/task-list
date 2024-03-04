package com.antimated.tasklist.requirements;

import javax.inject.Inject;
import net.runelite.api.Client;

public interface Requirement
{
	boolean satisfiesRequirement(Client client);
}
