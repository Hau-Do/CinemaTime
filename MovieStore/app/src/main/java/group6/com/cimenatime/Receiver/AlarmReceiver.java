package group6.com.cimenatime.Receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import group6.com.cimenatime.Activity.MainActivity;
import group6.com.cimenatime.R;

/**
 * Created by HauDT on 05/03/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Congratulation", Toast.LENGTH_SHORT).show();
        showReminderNotification(context);
    }

    private void showReminderNotification(Context context) {

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);

        Notification noti = new Notification.Builder(context)
                .setContentTitle("Go Cinema")
                .setContentText("Time to go get the ticket for ").setSmallIcon(R.drawable.ic_notifications)
                .setContentIntent(pIntent)
                .build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, noti);
    }
}
