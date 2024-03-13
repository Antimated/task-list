package com.antimated.tasklist.tasks;

import lombok.Getter;

@Getter
public enum TaskTier
{
	EASY("Easy", 10),
	MEDIUM("Medium", 40),
	HARD("Hard", 80),
	ELITE("Elite", 200),
	MASTER("Master", 400);

	private final String name;
	private final int points;

	TaskTier(String name, int points)
	{
		this.name = name;
		this.points = points;
	}
}
