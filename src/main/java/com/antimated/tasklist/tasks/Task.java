package com.antimated.tasklist.tasks;

import com.antimated.tasklist.requirements.Requirement;
import java.util.List;
import net.runelite.api.Client;

public abstract class Task
{
	private final String title;

	private final List<Requirement> requirements;

	public Task(String title, List<Requirement> requirements)
	{
		this.title = title;
		this.requirements = requirements;
	}

	public String getTitle()
	{
		return title;
	}

	public List<Requirement> getRequirements()
	{
		return requirements;
	}

	public boolean satisfiesRequirements(Client client) {
		for (Requirement requirement : requirements) {
			if (!requirement.satisfiesRequirement(client)) {
				return false; // If any requirement is not met, return false
			}
		}
		return true; // All requirements are met
	}
}
