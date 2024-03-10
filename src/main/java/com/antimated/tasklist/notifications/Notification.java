package com.antimated.tasklist.notifications;

import lombok.Data;
import lombok.NonNull;


@Data
@NonNull
public class Notification
{
	private final String title;
	private final String text;
	private final int color;
}
