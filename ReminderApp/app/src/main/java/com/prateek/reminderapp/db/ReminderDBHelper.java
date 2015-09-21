package com.prateek.reminderapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.prateek.reminderapp.Constants;

/**
 * Created by prateek02.arora on 18-09-2015.
 */
public class ReminderDBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = ReminderDBHelper.class.getSimpleName();

    public ReminderDBHelper(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, Constants.DB_NAME, factory, version);
        Log.d(LOG_TAG, "DB construction!");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "[onCreate] Creating Tables...");

        db.execSQL("create table " + Constants.DB_REMINDER_TABLE_NAME + "("

                        + Constants.DB_ID + " Integer primary key autoincrement,"
                        + Constants.UID + " text,"
                        + Constants.TITLE + " text,"
                        + Constants.CATEGORY + " int,"
                        + Constants.NOTE + " text,"
                        + Constants.DUE_DATE_STRING + " text,"
                        + Constants.DUE_DAY_OF_MONTH + " int,"
                        + Constants.DUE_MONTH + " int,"
                        + Constants.DUE_YEAR + " int,"
                        + Constants.DUE_TIME_STRING + " text,"
                        + Constants.DUE_HOUR + " int,"
                        + Constants.DUE_MINUTE + " int,"
                        + Constants.REMINDER_TYPE + " int,"
                        + Constants.REPEAT_INTERVAL_NUMBER + " int,"
                        + Constants.REPEAT_INTERVAL_TYPE + " int,"
                        + Constants.REPEAT_EXACT_DURATION + " text,"
                        + Constants.REMINDER_STATE + " int,"
                        + Constants.IS_REMINDER_MISSSED + " int,"
                        + Constants.REMINDER_NOTIFICATION_STATE + " int,"
                        + Constants.REMINDER_NOTIFICATION_ID + " int,"
                        + Constants.DATE_STRING + " text,"
                        + Constants.TIME_STRING + " text);"

        );
        Log.d(LOG_TAG, "query sent for" + Constants.DB_REMINDER_TABLE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(LOG_TAG, "[onUpgrade]");

        db.execSQL("drop table if exists " +  Constants.DB_REMINDER_TABLE_NAME);
        onCreate(db);
    }
}
