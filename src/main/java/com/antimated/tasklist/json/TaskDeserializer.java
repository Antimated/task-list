package com.antimated.tasklist.json;

import com.antimated.tasklist.requirements.Requirement;
import com.antimated.tasklist.tasks.Task;
import com.antimated.tasklist.tasks.TaskTier;
import com.antimated.tasklist.tasks.TaskType;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaskDeserializer implements JsonDeserializer<Task>
{
	@Override
	public Task deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		try
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
			Requirement requirement = context.deserialize(requirementJson, type.getRequirementClass());

			// Create and return the Task instance
			return new Task(id, type, tier, description, requirement, completed);
		}
		catch (JsonParseException e)
		{
			log.error("Error deserializing Task from JSON", e);
			throw new JsonParseException("Error deserializing Task from JSON", e);
		}
	}
}
