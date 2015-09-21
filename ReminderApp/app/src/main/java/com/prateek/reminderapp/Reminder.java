package com.prateek.reminderapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.UUID;

/**
 * Created by prateek02.arora on 18-09-2015.
 */
public class Reminder {

    private int dbId;
    private String UID;
    private String title;
    private int category;
    private String message = "NA";
    private String dueDateString;
    private int dueDayOfMonth;
    private int dueMonth;
    private int dueYear;

    private String dueTimeString;
    private int dueHour;
    private int dueMinute;

    private boolean isRepeatNeeded;

    private int reminderType;
    private int repeatIntervalNumber = 0;
    private int repeatIntervalType;
    private int repeatExactDuration;

    private String dateString;
    private String timeString;

    private int state;
    private int notificationState = Constants.REMINDER_NOTIFICATION_PENDING;
    private int notificationId = 0;

    private boolean isReminderMissed = false;

    public Reminder() {
        setUID(generateUniqueId());
        state = Constants.REMINDER_CREATION_STATE;
    }

    public Reminder(Cursor cursor) {

        if (cursor != null) {
            dbId = cursor.getInt(cursor.getColumnIndex(Constants.DB_ID));
            UID = cursor.getString(cursor.getColumnIndex(Constants.UID));
            title = cursor.getString(cursor.getColumnIndex(Constants.TITLE));
            category = cursor.getInt(cursor.getColumnIndex(Constants.CATEGORY));
            message = cursor.getString(cursor.getColumnIndex(Constants.NOTE));
            dueDateString = cursor.getString(cursor.getColumnIndex(Constants.DUE_DATE_STRING));
            dueDayOfMonth = cursor.getInt(cursor.getColumnIndex(Constants.DUE_DAY_OF_MONTH));
            dueMonth = cursor.getInt(cursor.getColumnIndex(Constants.DUE_MONTH));
            dueYear = cursor.getInt(cursor.getColumnIndex(Constants.DUE_YEAR));
            dueTimeString = cursor.getString(cursor.getColumnIndex(Constants.DUE_TIME_STRING));
            dueHour = cursor.getInt(cursor.getColumnIndex(Constants.DUE_HOUR));
            dueMinute = cursor.getInt(cursor.getColumnIndex(Constants.DUE_MINUTE));
            state = cursor.getInt(cursor.getColumnIndex(Constants.REMINDER_STATE));
            reminderType = cursor.getInt(cursor.getColumnIndex(Constants.REMINDER_TYPE));
            if (reminderType == Constants.REPEATED && state != Constants.REMINDER_COMPLETE_STATE) {
                isRepeatNeeded = true;
            } else {
                isRepeatNeeded = false;
            }
            repeatIntervalNumber = cursor.getInt(cursor.getColumnIndex(Constants.REPEAT_INTERVAL_NUMBER));
            repeatIntervalType = cursor.getInt(cursor.getColumnIndex(Constants.REPEAT_INTERVAL_TYPE));
            repeatExactDuration = cursor.getInt(cursor.getColumnIndex(Constants.REPEAT_EXACT_DURATION));
            isReminderMissed = (cursor.getInt(cursor.getColumnIndex(Constants.IS_REMINDER_MISSSED))== 1) ? true : false;
            notificationState = cursor.getInt(cursor.getColumnIndex(Constants.REMINDER_NOTIFICATION_STATE));
            notificationId = cursor.getInt(cursor.getColumnIndex(Constants.REMINDER_NOTIFICATION_ID));
            dateString = cursor.getString(cursor.getColumnIndex(Constants.DATE_STRING));
            timeString = cursor.getString(cursor.getColumnIndex(Constants.TIME_STRING));
        }
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public int getDbId() {
        return dbId;
    }

    public void setDbId(int dbId) {
        this.dbId = dbId;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDueDateString() {
        return dueDateString;
    }

    public void setDueDateString(String dueDateString) {
        this.dueDateString = dueDateString;
    }

    public String getDueTimeString() {
        return dueTimeString;
    }

    public void setDueTimeString(String dueTimeString) {
        this.dueTimeString = dueTimeString;
    }

    public boolean isRepeatNeeded() {
        return isRepeatNeeded;
    }

    public void setIsRepeated(boolean isRepeated) {
        this.isRepeatNeeded = isRepeated;
        if (isRepeated) {
            reminderType = Constants.REPEATED;
        } else {
            reminderType = Constants.SINGLE_SHOT;
        }
    }

    public int getRepeatIntervalNumber() {
        return repeatIntervalNumber;
    }

    public void setRepeatIntervalNumber(int repeatIntervalNumber) {
        this.repeatIntervalNumber = repeatIntervalNumber;
    }

    public int getRepeatIntervalType() {
        return repeatIntervalType;
    }

    public void setRepeatIntervalType(int repeatIntervalType) {
        this.repeatIntervalType = repeatIntervalType;
    }

    public int getRepeatExactDuration() {
        return repeatExactDuration;
    }

    public void setRepeatExactDuration(int repeatExactDuration) {
        this.repeatExactDuration = repeatExactDuration;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public int getDueDayOfMonth() {
        return dueDayOfMonth;
    }

    public void setDueDayOfMonth(int dueDayOfMonth) {
        this.dueDayOfMonth = dueDayOfMonth;
    }

    public int getDueMonth() {
        return dueMonth;
    }

    public void setDueMonth(int dueMonth) {
        this.dueMonth = dueMonth;
    }

    public int getDueYear() {
        return dueYear;
    }

    public void setDueYear(int dueYear) {
        this.dueYear = dueYear;
    }

    public int getDueHour() {
        return dueHour;
    }

    public void setDueHour(int dueHour) {
        this.dueHour = dueHour;
    }

    public int getDueMinute() {
        return dueMinute;
    }

    public void setDueMinute(int dueMinute) {
        this.dueMinute = dueMinute;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;

        if (state != Constants.REMINDER_COMPLETE_STATE && reminderType == Constants.REPEATED) {
            isRepeatNeeded = true;
        } else {
            isRepeatNeeded = false;
        }

    }

    public int getReminderType() {
        return reminderType;
    }

    public boolean isReminderMissed() {
        return isReminderMissed;
    }

    public void setIsReminderMissed(boolean isReminderMissed) {
        this.isReminderMissed = isReminderMissed;
    }

    public int getNotificationState() {
        return notificationState;
    }

    public void setNotificationState(int notificationState) {
        this.notificationState = notificationState;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public ContentValues getContentValues() {

        ContentValues values = new ContentValues();
        values.put(Constants.UID, UID);
        values.put(Constants.TITLE, title);
        values.put(Constants.CATEGORY, category);
        values.put(Constants.NOTE, message);
        values.put(Constants.DUE_DATE_STRING, dueDateString);
        values.put(Constants.DUE_DAY_OF_MONTH, dueDayOfMonth);
        values.put(Constants.DUE_MONTH, dueMonth);
        values.put(Constants.DUE_YEAR, dueYear);
        values.put(Constants.DUE_TIME_STRING, dueTimeString);
        values.put(Constants.DUE_HOUR, dueHour);
        values.put(Constants.DUE_MINUTE, dueMinute);
        values.put(Constants.REMINDER_TYPE, reminderType);
        values.put(Constants.REPEAT_INTERVAL_NUMBER, repeatIntervalNumber);
        values.put(Constants.REPEAT_INTERVAL_TYPE, repeatIntervalType);
        values.put(Constants.REPEAT_EXACT_DURATION, repeatExactDuration);
        values.put(Constants.REMINDER_STATE, state);
        values.put(Constants.IS_REMINDER_MISSSED, isReminderMissed ? 1 : 0);
        values.put(Constants.REMINDER_NOTIFICATION_STATE, notificationState);
        values.put(Constants.REMINDER_NOTIFICATION_ID, notificationId);
        values.put(Constants.DATE_STRING, dateString);
        values.put(Constants.TIME_STRING, timeString);

        return values;

    }

    public Uri insertItemInDB(Context context) {
        Log.d("Split", "MODEL: INSERTING :----->");
        ContentValues values = getContentValues();
        return context.getContentResolver().insert(Constants.DB_REMINDER_TABLE_NAME_URI, values);
    }

    public int updateItemInDb(Context context) {
        String where = Constants.UID + "=" + "?";
        String[] selectionArgs = new String[]{""+UID};
        Log.d("Split", "MODEL: UPDATING :----->");
        ContentValues values = getContentValues();
        return context.getContentResolver().update(Constants.DB_REMINDER_TABLE_NAME_URI, values, where, selectionArgs);
    }

    public int deleteItemInDb(Context context) {
        String where = Constants.UID + "=" + "?";
        String[] selectionArgs = new String[]{""+UID};
        Log.d("Split", "MODEL: DELETING :----->");
        return context.getContentResolver().delete(Constants.DB_REMINDER_TABLE_NAME_URI, where, selectionArgs);
    }

    public String generateUniqueId() {
        String uid = UUID.randomUUID().toString();
        uid = uid.replaceAll("-", "");
        return uid;
    }

}
