package model;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import com.example.prasetyon.remindme.R;

/**
 * Created by prasetyon on 9/18/2016.
 */
public class RemindMe extends Application {

    public static DbHelper dbHelper;
    public static SQLiteDatabase db;
    public static SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        sp = PreferenceManager.getDefaultSharedPreferences(this);

        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();
    }

    public static String getRingtone() {
        return sp.getString(RemindMe.RINGTONE_PREF, DEFAULT_NOTIFICATION_URI.toString());
    }

}