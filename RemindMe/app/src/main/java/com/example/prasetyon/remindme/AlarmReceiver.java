package com.example.prasetyon.remindme;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import model.RemindMe;

/**
 * Created by prasetyon on 9/18/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        long id = intent.getLongExtra("id", 0);
        String msg = intent.getStringExtra("msg");

        Notification n = new Notification(R.drawable.ic_launcher, msg,
                System.currentTimeMillis());
        PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(), 0);

        n.setLatestEventInfo(context, "Remind Me", msg, pi);
        // TODO check user preferences
        n.defaults |= Notification.DEFAULT_VIBRATE;
        n.sound = Uri.parse(RemindMe.getRingtone());
        //		n.defaults |= Notification.DEFAULT_SOUND;
        n.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager nm = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify((int)id, n);
    }

}