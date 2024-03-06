package com.antimated.tasklist.json;

import com.antimated.tasklist.tasks.Task;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class TaskSerializer implements JsonSerializer<Task>
{
	@Override
	public JsonElement serialize(Task src, Type typeOfSrc, JsonSerializationContext context)
	{
		JsonObject jsonObject = new JsonObject();

		// Serialize common fields
		jsonObject.addProperty("id", src.getId());
		jsonObject.add("type", context.serialize(src.getType()));
		jsonObject.add("tier", context.serialize(src.getTier()));
		jsonObject.addProperty("description", src.getDescription());
		jsonObject.addProperty("completed", src.isCompleted());

		// Serialize the "requirement" field based on the "type" field
		jsonObject.add("requirement", context.serialize(src.getRequirement()));

		return jsonObject;
	}
}
