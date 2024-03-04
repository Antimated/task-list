package com.antimated.tasklist.tasks;

public enum TaskTier
{
	EASY("Easy", 10),
	MEDIUM("Medium", 40),
	HARD("Hard", 80),
	ELITE("Elite", 200),
	MASTER("Master", 400);

	private final String name;
	public final int points;

	TaskTier(String name, int points)
	{
		this.name = name;
		this.points = points;
	}

	public String getName()
	{
		return name;
	}

	public int getPoints()
	{
		return points;
	}
}
