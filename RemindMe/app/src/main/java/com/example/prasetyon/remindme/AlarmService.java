package com.example.prasetyon.remindme;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;

import model.RemindMe;

/**
 * Created by prasetyon on 9/18/2016.
 */
public class AlarmService extends IntentService {

    public static final String CREATE = "CREATE";
    public static final String CANCEL = "CANCEL";

    private IntentFilter matcher;

    public AlarmService() {
        super(TAG);
        matcher = new IntentFilter();
        matcher.addAction(CREATE);
        matcher.addAction(CANCEL);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        String notificationId = intent.getStringExtra("notificationId");

        if (matcher.matchAction(action)) {
            execute(action, notificationId);
        }
    }

    private void execute(String action, String notificationId) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Cursor c = RemindMe.db.query(Notification.TABLE_NAME, null, "_id = ?",
                new String[]{notificationId}, null, null, null);

        if (c.moveToFirst()) {
            Intent i = new Intent(this, AlarmReceiver.class);
            i.putExtra("id", c.getLong(c.getColumnIndex(Notification.COL_ID)));
            i.putExtra("msg", c.getString(c.getColumnIndex(Notification.COL_MSG)));

            PendingIntent pi = PendingIntent.getBroadcast(this, 0, i,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            long time = c.getLong(c.getColumnIndex(Notification.COL_DATETIME));
            if (CREATE.equals(action)) {
                am.set(AlarmManager.RTC_WAKEUP, time, pi);

            } else if (CANCEL.equals(action)) {
                am.cancel(pi);
            }
        }
        c.close();
    }
}