package com.prateek.reminderapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.prateek.reminderapp.Constants;
import com.prateek.reminderapp.Reminder;
import com.prateek.reminderapp.Utility;

import java.util.Calendar;

/**
 * Created by Prateek on 20-09-2015.
 */
public class AlarmSetterService extends IntentService {


    private static final String LOG_TAG = AlarmSetterService.class.getSimpleName();

    public AlarmSetterService() {
        super("AlarmSetterService");
        Log.d(LOG_TAG, "Constructor");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(LOG_TAG, "onHandleIntent");

        Toast.makeText(this, ""+LOG_TAG, Toast.LENGTH_LONG).show();

        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        Utility.setAlarmForDailyReminderSync(this);

        Log.d(LOG_TAG, "dayOfMonth :" + calendar.get(Calendar.DAY_OF_MONTH));
        Log.d(LOG_TAG, "month :" + calendar.get(Calendar.MONTH));
        Log.d(LOG_TAG, "year :" + calendar.get(Calendar.YEAR));
        Log.d(LOG_TAG, "hour :" + calendar.get(Calendar.HOUR_OF_DAY));
        Log.d(LOG_TAG, "min :" + calendar.get(Calendar.MINUTE));

        String selection = Constants.DUE_YEAR + "= ? and " + Constants.DUE_MONTH + "= ? and "
                + Constants.DUE_DAY_OF_MONTH + "= ? and " + Constants.REMINDER_STATE + " = " + Constants.REMINDER_ACTIVE_STATE + " and " +
                Constants.DUE_HOUR + " >= ? ";//and " + Constants.DUE_MINUTE + ">= ?";
        String [] selArgs = new String[]{""+year, ""+month, ""+dayOfMonth, ""+hour/*, ""+min*/};

        Cursor cursor = getContentResolver().query(Constants.DB_REMINDER_TABLE_NAME_URI, null, selection, selArgs, null);

        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            Reminder reminder = null;
            Calendar reminderAlarmtime = null;
            while (!cursor.isAfterLast()) {

                reminder = new Reminder(cursor);
                if (reminder == null) {
                    continue;
                }

                reminderAlarmtime = Utility.getReminderCalendar(reminder.getDueDayOfMonth(), reminder.getDueMonth(), reminder.getDueYear(),
                        reminder.getDueHour(), reminder.getDueMinute());

                Log.d(LOG_TAG, "setAlarm dayOfMonth :" + reminderAlarmtime.get(Calendar.DAY_OF_MONTH));
                Log.d(LOG_TAG, "setAlarm month :" + reminderAlarmtime.get(Calendar.MONTH));
                Log.d(LOG_TAG, "setAlarm year :" + reminderAlarmtime.get(Calendar.YEAR));
                Log.d(LOG_TAG, "setAlarm hour :" + reminderAlarmtime.get(Calendar.HOUR_OF_DAY));
                Log.d(LOG_TAG, "setAlarm min :" + reminderAlarmtime.get(Calendar.MINUTE));
                Log.d(LOG_TAG, "onHandleIntent setAlarm");

                Utility.setAlarm(this, reminderAlarmtime);

                cursor.moveToNext();
            }
        }

    }
}
