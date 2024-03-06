package com.antimated.tasklist.json;

import com.antimated.tasklist.requirements.AllSkillLevelRequirement;
import com.antimated.tasklist.requirements.Requirement;
import com.antimated.tasklist.requirements.SkillLevelRequirement;
import com.antimated.tasklist.requirements.TotalLevelRequirement;
import com.antimated.tasklist.tasks.Task;
import com.antimated.tasklist.tasks.TaskTier;
import com.antimated.tasklist.tasks.TaskType;
import com.google.gson.*;

import java.lang.reflect.Type;

public class TaskDeserializer implements JsonDeserializer<Task>
{
	@Override
	public Task deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		JsonObject jsonObject = json.getAsJsonObject();

		// Deserialize common fields
		int id = jsonObject.get("id").getAsInt();
		TaskTier tier = context.deserialize(jsonObject.get("tier"), TaskTier.class);
		TaskType type = context.deserialize(jsonObject.get("type"), TaskType.class);
		String description = jsonObject.get("description").getAsString();
		boolean completed = jsonObject.get("completed").getAsBoolean();

		// Deserialize the "requirement" field based on the "type" field
		JsonObject requirementJson = jsonObject.getAsJsonObject("requirement");
		Requirement requirement = context.deserialize(requirementJson, getRequirementClass(type));

		// Create and return the Task instance
		return new Task(id, type, tier, description, requirement, completed);
	}

	// Helper method to get the Requirement class based on TaskType
	private Class<? extends Requirement> getRequirementClass(TaskType type)
	{
		switch (type)
		{
			case ALL_SKILL_LEVELS:
				return AllSkillLevelRequirement.class;
			case SKILL_LEVEL:
				return SkillLevelRequirement.class;
			case TOTAL_LEVEL:
				return TotalLevelRequirement.class;
			// Add cases for other types as needed
			default:
				throw new JsonParseException("Unsupported TaskType: " + type);
		}
	}
}
