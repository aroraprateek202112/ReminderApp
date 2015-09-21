package com.prateek.reminderapp;

import android.net.Uri;

/**
 * Created by Prateek on 17-09-2015.
 */
public class Constants {

//    public static final ONE_TIME_REMINDER;
    public static final String REMINDER_ALARM_INTENT_ACTION = "com.prateek.reminderapp.alarm.ALARM_ACTION";
    public static final String REMINDER_ALARM_SETTER_INTENT_ACTION = "com.prateek.reminderapp.alarm.ALARM_SETTER_ACTION";

    public static final String EXTRA_REMINDER_UID = "com.prateek.reminderapp.alarm.UID";
    public static final String EXTRA_REMINDER_TITLE = "com.prateek.reminderapp.extra.TITLE";
    public static final String EXTRA_REMINDER_MESSAGE = "com.prateek.reminderapp.extra.MESSAGE";
    public static final String EXTRA_REMINDER_TYPE = "com.prateek.reminderapp.extra.TYPE";
    public static final String EXTRA_REMINDER_REPEAT_INTERVAL_TYPE = "com.prateek.reminderapp.extra.REPEAT_INTERVAL_TYPE";
    public static final String EXTRA_REMINDER_REPEAT_INTERVAL_COUNT = "com.prateek.reminderapp.extra.REPEAT_INTERVAL_COUNT";
    public static final String EXTRA_REMINDER_REPEAT_DAY_OF_MONTH = "com.prateek.reminderapp.extra.REPEAT_DAY_OF_MONTH";
    public static final String EXTRA_REMINDER_REPEAT_MONTH = "com.prateek.reminderapp.extra.REPEAT_MONTH";
    public static final String EXTRA_REMINDER_REPEAT_YEAR = "com.prateek.reminderapp.extra.REPEAT_YEAR";
    public static final String EXTRA_REMINDER_REPEAT_HOUR_OF_DAY = "com.prateek.reminderapp.extra.REPEAT_HOUR_OF_DAY";
    public static final String EXTRA_REMINDER_REPEAT_MINUTE = "com.prateek.reminderapp.extra.REPEAT_MINUTE";
    public static final String EXTRA_REMINDER_NOTIFICATION_ID = "com.prateek.reminderapp.extra.NOTIFICAITON_ID";

    // Reminder Type
    public static final int SINGLE_SHOT = 10;
    public static final int REPEATED = 11;

    public static final String EXTRA_REMINDER_OPEN_MODE_TYPE = "mode_type";
    public static final int MODE_NEW_REMINDER = 501;
    public static final int MODE_EDIT_REMINDER = 502;




    // Reminder Repeat interval
    public static final int REPEAT_INTERVAL_NOT_APPLICABLE = 0;
    public static final int REPEAT_INTERVAL_DAILY = 1;
    public static final int REPEAT_INTERVAL_WEEKLY = 2;
    public static final int REPEAT_INTERVAL_MONTHLY = 3;
    public static final int REPEAT_INTERVAL_YEARLY = 4;
    public static final int REPEAT_INTERVAL_HOURLY = 5;
    public static final int REPEAT_INTERVAL_MINUTES = 6;


    // Reminder State
    public static final int REMINDER_CREATION_STATE = 101;
    public static final int REMINDER_ACTIVE_STATE = 102;
    public static final int REMINDER_COMPLETE_STATE = 103;


    public static final int REMINDER_NOTIFICATION_PENDING = 201;
    public static final int REMINDER_NOTIFICATION_MISSED = 202;
    public static final int REMINDER_NOTIFICATION_COMPLETE = 203;

    public static final int CATEGORY_TODO = 20;
    public static final int CATEGORY_PAYMENT = 21;
    public static final int CATEGORY_ANNIVERSERY = 22;




    // DB Constants
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "ReminderDatabase";
    public static final String DB_AUTHORITY = "company.prateek.reminderapp.db";

    public static final String DB_REMINDER_TABLE_NAME = "reminderTable";
    public static final Uri DB_REMINDER_TABLE_NAME_URI = Uri.parse("content://"+DB_AUTHORITY+"/" + DB_REMINDER_TABLE_NAME);

    // DB Fields
    public static final String DB_ID = "_id";
    public static final String UID = "uid";

    public static final String TITLE = "title";
    public static final String CATEGORY = "category";

    public static final String NOTE = "note";

    public static final String DUE_DATE_STRING ="dueDate";
    public static final String DUE_DAY_OF_MONTH = "day";
    public static final String DUE_MONTH = "month";
    public static final String DUE_YEAR = "year";

    public static final String DUE_TIME_STRING ="dueTime";
    public static final String DUE_HOUR = "hour";
    public static final String DUE_MINUTE = "min";

    public static final String REMINDER_TYPE = "reminderType";
    public static final String REPEAT_INTERVAL_NUMBER = "repeatIntervalNumber";
    public static final String REPEAT_INTERVAL_TYPE = "repeatIntervalType";
    public static final String REPEAT_EXACT_DURATION = "repeatExactDuration";

    public static final String DATE_STRING = "dateString";
    public static final String TIME_STRING = "timeString";

    public static final String REMINDER_STATE = "reminderState";
    public static final String IS_REMINDER_MISSSED = "isReminderMissed";
    public static final String REMINDER_NOTIFICATION_STATE = "reminderNotificationState";
    public static final String REMINDER_NOTIFICATION_ID = "reminderNotificationId";

    public static final String[] DB_REMINDER_TABLE_PROJECTION = new String[]{
            DB_ID,UID,
            TITLE,CATEGORY,NOTE,DUE_DATE_STRING,
            DUE_DAY_OF_MONTH,DUE_MONTH,DUE_YEAR,
            DUE_TIME_STRING,DUE_HOUR, DUE_MINUTE,
            REMINDER_TYPE, REPEAT_INTERVAL_NUMBER,
            REPEAT_INTERVAL_TYPE, REPEAT_EXACT_DURATION,
            REMINDER_STATE,
            IS_REMINDER_MISSSED,
            REMINDER_NOTIFICATION_STATE,
            REMINDER_NOTIFICATION_ID,
            DATE_STRING, TIME_STRING
    };

}
