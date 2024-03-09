package com.antimated.tasklist.notifications;

import lombok.Getter;

@Getter
public class NotificationItem
{
	private final String title;
	private final String text;
	private final int color;

	// Constructor with color
	public NotificationItem(String title, String text, int color)
	{
		this.title = title;
		this.text = text;
		this.color = color;
	}
}
