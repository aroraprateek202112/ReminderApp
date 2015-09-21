package com.prateek.reminderapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.prateek.reminderapp.Constants;
import com.prateek.reminderapp.Reminder;
import com.prateek.reminderapp.Utility;

import java.util.Calendar;

/**
 * Created by Prateek on 20-09-2015.
 */
public class ReminderManagerService extends IntentService {

    private static final String LOG_TAG = ReminderManagerService.class.getSimpleName();

    public ReminderManagerService() {
        super("ReminderManagerService");
        Log.d(LOG_TAG, "Constructor");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(LOG_TAG, "onHandleIntent");
        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        String selection = Constants.DUE_YEAR + "<= ? and " + Constants.DUE_MONTH + "<= ? and "
                + Constants.DUE_DAY_OF_MONTH + "<= ? and " + Constants.REMINDER_STATE + " = "
                + Constants.REMINDER_ACTIVE_STATE + " and " + Constants.REMINDER_NOTIFICATION_STATE + " = "
                + Constants.REMINDER_NOTIFICATION_PENDING + " and ((" + Constants.DUE_HOUR + " < ?) OR ("
                + Constants.DUE_HOUR +" = "+ hour + " and " + Constants.DUE_MINUTE + " <=?))";
        String [] selArgs = new String[]{""+year, ""+month, ""+dayOfMonth, ""+hour, ""+min};

        Cursor cursor = getContentResolver().query(Constants.DB_REMINDER_TABLE_NAME_URI, null, selection, selArgs, null);

        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            Reminder reminder = null;
            Calendar currentReminderTimeCalendar = null;
            Calendar nextRepeatTimeCalendar = null;
            while (!cursor.isAfterLast()) {
                reminder = new Reminder(cursor);
                /*if (dayOfMonth == reminder.getDueDayOfMonth() && month == reminder.getDueMonth()
                        && year == reminder.getDueYear() && (reminder.getDueHour() <= hour && reminder.getDueHour() > hour - 2)) {*/
//                if(!Utility.isPassed(reminder.getDueDateString(), reminder.getDueHour(), reminder.getDueMinute(), calendar)) {
                if(!Utility.isReminderMissed(reminder.getDueDayOfMonth(), reminder.getDueMonth(), reminder.getDueYear(),
                        reminder.getDueHour(), reminder.getDueMinute(), calendar)) {

                    Utility.showCustomNotification(this, reminder);

                    if (reminder.getReminderType() == Constants.SINGLE_SHOT) {
                        reminder.setNotificationState(Constants.REMINDER_NOTIFICATION_COMPLETE);
                    }

                } else {
                    reminder.setNotificationState(Constants.REMINDER_NOTIFICATION_MISSED);
                }

                if (reminder.getReminderType() == Constants.SINGLE_SHOT) {
                    reminder.setState(Constants.REMINDER_COMPLETE_STATE);
                } else {
                    currentReminderTimeCalendar = Utility.getReminderCalendar(reminder.getDueDayOfMonth(), reminder.getDueMonth(), reminder.getDueYear(),
                            reminder.getDueHour(), reminder.getDueMinute());
                    nextRepeatTimeCalendar = Utility.getNextRepeatTimeCalendar(reminder.getRepeatIntervalType(), reminder.getRepeatIntervalNumber(), currentReminderTimeCalendar);

                    reminder.setDueDateString(Utility.getDateString(nextRepeatTimeCalendar));
                    reminder.setDueDayOfMonth(nextRepeatTimeCalendar.get(Calendar.DAY_OF_MONTH));
                    reminder.setDueMonth(nextRepeatTimeCalendar.get(Calendar.MONTH));
                    reminder.setDueYear(nextRepeatTimeCalendar.get(Calendar.YEAR));

                    reminder.setDueTimeString(Utility.getTimeString(nextRepeatTimeCalendar));
                    reminder.setDueHour(nextRepeatTimeCalendar.get(Calendar.HOUR_OF_DAY));
                    reminder.setDueMinute(nextRepeatTimeCalendar.get(Calendar.MINUTE));
                    Utility.setAlarm(this, nextRepeatTimeCalendar);
                }

                reminder.updateItemInDb(this);
                cursor.moveToNext();
            }
        }

    }
}
