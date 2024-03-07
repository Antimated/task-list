package com.antimated.tasklist.json;

import com.antimated.tasklist.tasks.Task;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaskSerializer implements JsonSerializer<Task>
{
	@Override
	public JsonElement serialize(Task task, Type typeOfSrc, JsonSerializationContext context)
	{
		JsonObject jsonObject = new JsonObject();

		// Serialize common fields
		jsonObject.addProperty("id", task.getId());
		jsonObject.addProperty("description", task.getDescription());
		jsonObject.add("tier", context.serialize(task.getTier()));
		jsonObject.add("type", context.serialize(task.getType()));
		// Serialize the "requirement" field based on the "type" field
		jsonObject.add("requirement", context.serialize(task.getRequirement()));
		jsonObject.addProperty("completed", task.isCompleted());

		return jsonObject;
	}
}
