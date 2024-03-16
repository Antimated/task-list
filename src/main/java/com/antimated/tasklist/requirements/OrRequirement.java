package com.antimated.tasklist.requirements;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import java.util.List;
import lombok.Getter;
import net.runelite.api.Client;

@Getter
public class OrRequirement implements Requirement
{
	private final List<Requirement> requirements;

	public OrRequirement(Requirement... reqs)
	{
		this.requirements = ImmutableList.copyOf(reqs);
	}

	@Override
	public String toString()
	{
		return Joiner.on(" or ").join(requirements);
	}

	@Override
	public boolean satisfiesRequirement(Client client)
	{
		for (Requirement r : getRequirements())
		{
			if (r.satisfiesRequirement(client))
			{
				return true;
			}
		}
		return false;
	}
}
