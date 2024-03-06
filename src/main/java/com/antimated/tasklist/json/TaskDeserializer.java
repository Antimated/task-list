package com.antimated.tasklist.json;

import com.antimated.tasklist.requirements.AllSkillLevelRequirement;
import com.antimated.tasklist.requirements.AllSkillXpRequirement;
import com.antimated.tasklist.requirements.AnySkillLevelRequirement;
import com.antimated.tasklist.requirements.AnySkillXpRequirement;
import com.antimated.tasklist.requirements.Requirement;
import com.antimated.tasklist.requirements.SkillLevelRequirement;
import com.antimated.tasklist.requirements.SkillXpRequirement;
import com.antimated.tasklist.requirements.TotalLevelRequirement;
import com.antimated.tasklist.requirements.TotalXpRequirement;
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
			case ANY_SKILL_LEVEL:
				return AnySkillLevelRequirement.class;
			case ANY_SKILL_XP:
				return AnySkillXpRequirement.class;
			case SKILL_LEVEL:
				return SkillLevelRequirement.class;
			case SKILL_XP:
				return SkillXpRequirement.class;
			case TOTAL_LEVEL:
				return TotalLevelRequirement.class;
			case TOTAL_XP:
				return TotalXpRequirement.class;
			case ALL_SKILL_LEVEL:
				return AllSkillLevelRequirement.class;
			case ALL_SKILL_XP:
				return AllSkillXpRequirement.class;

			// Add cases for other types as needed
			default:
				throw new JsonParseException("Unsupported TaskType: " + type);
		}
	}
}
