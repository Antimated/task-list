package com.antimated.tasklist;

import com.antimated.tasklist.requirements.AllSkillLevelRequirement;
import com.antimated.tasklist.requirements.Requirement;
import com.antimated.tasklist.requirements.SkillLevelRequirement;
import com.antimated.tasklist.requirements.TotalLevelRequirement;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequirementAdapter implements JsonSerializer<Requirement>, JsonDeserializer<Requirement>
{
	@Override
	public JsonElement serialize(Requirement requirement, Type typeOfSrc, JsonSerializationContext context)
	{
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("type", requirement.getClass().getSimpleName());
		jsonObject.add("data", context.serialize(requirement));
		return jsonObject;
	}

	@Override
	public Requirement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		JsonObject jsonObject = json.getAsJsonObject();
		String type = jsonObject.get("type").getAsString();
		JsonElement data = jsonObject.get("data");

		log.debug("json {}", json);
		try
		{
			switch (type)
			{
				case "ALL_SKILL_LEVELS":
					return context.deserialize(data, AllSkillLevelRequirement.class);
				case "SKILL_LEVEL":
					return context.deserialize(data, SkillLevelRequirement.class);
				case "TOTAL_LEVEL":
					return context.deserialize(data, TotalLevelRequirement.class);
				default:
					throw new JsonParseException("Unknown requirement type: " + type);
			}
		}
		catch (JsonParseException e)
		{
			throw new JsonParseException("Error deserializing Requirement", e);
		}
	}
}