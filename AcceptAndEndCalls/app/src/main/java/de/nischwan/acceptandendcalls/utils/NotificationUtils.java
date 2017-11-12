package de.nischwan.acceptandendcalls.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;

import java.util.Locale;

import de.nischwan.acceptandendcalls.R;

/**
 * @author Nico Schwanebeck
 */

public final class NotificationUtils {
    private NotificationUtils() {
        // Utility class
    }

    public static void displayHangupTimeNotification(Context context, String hangupTime) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        if (notificationManager.areNotificationsEnabled()) {
            String title = context.getString(R.string.notification_hangup_title);
            String message = String.format(Locale.getDefault(), context.getString(R.string.notification_hangup_message), hangupTime);

            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentTitle(title);
            builder.setContentText(message);
            builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
            builder.setOngoing(true);
            Notification notification = builder.getNotification();
            notificationManager.notify(R.string.notification_hangup_message, notification);
        }
    }

    public static void removeNotification(Context context) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(R.string.notification_hangup_message);
    }
}
