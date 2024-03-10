package com.antimated.tasklist.notifications;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.WidgetNode;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetModalMode;
import net.runelite.api.widgets.WidgetUtil;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;

@Slf4j
@Singleton
public class NotificationManager
{
	private static final int SCRIPT_ID = 3343; // NOTIFICATION_DISPLAY_INIT

	private static final int COMPONENT_ID = WidgetUtil.packComponentId(303, 2); // (interfaceId << 16) | childId

	private static final int INTERFACE_ID = 660;

	private final Queue<Notification> notifications = new ConcurrentLinkedQueue<>();

	private boolean isProcessingNotification = false;

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private EventBus eventBus;

	@Subscribe
	public void onGameTick(GameTick event)
	{
		processNotification();
	}

	public void startUp()
	{
		eventBus.register(this);
	}

	public void shutDown()
	{
		notifications.clear();
		eventBus.unregister(this);
	}

	public void addNotification(String title, String text)
	{
		Notification notification = new Notification(title, text, -1);
		notifications.offer(notification);
	}

	/**
	 * Processes a notification
	 */
	private void processNotification()
	{
		// Only process notifications if the queue is not empty AND the queue is not processing any notifications.
		if (!notifications.isEmpty() && !isProcessingNotification)
		{
			// Get and remove the first element in the notifications queue.
			Notification notification = notifications.poll();

			// Display notification
			displayNotification(notification);
		}
	}

	/**
	 * Display a notification and close it afterward.
	 *
	 * @param notification Notification
	 */
	private void displayNotification(Notification notification) throws IllegalStateException, IllegalArgumentException
	{
		isProcessingNotification = true;

		WidgetNode notificationNode = client.openInterface(COMPONENT_ID, INTERFACE_ID, WidgetModalMode.MODAL_CLICKTHROUGH);
		Widget notificationWidget = client.getWidget(INTERFACE_ID, 1);

		// Runs a client script to set the initial title, text and color values of the notifications
		client.runScript(SCRIPT_ID, notification.getTitle(), notification.getText(), notification.getColor());

		// Only remove notification when widget is fully closed.
		clientThread.invokeLater(() -> {
			if (notificationWidget != null && notificationWidget.getWidth() > 0)
			{
				return false;
			}

			// Close the interface
			client.closeInterface(notificationNode, true);

			// We can now start processing notifications again.
			isProcessingNotification = false;

			// Invoke done
			return true;
		});
	}
}
