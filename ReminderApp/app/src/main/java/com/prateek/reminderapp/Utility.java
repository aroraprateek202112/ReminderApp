package com.prateek.reminderapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.prateek.reminderapp.activity.MainActivity;
import com.prateek.reminderapp.receiver.ReminderAlarmReceiver;
import com.prateek.reminderapp.receiver.AlarmSetterReceiver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Prateek on 19-09-2015.
 */
public class Utility {

    private static final String LOG_TAG = Utility.class.getSimpleName();

    public static boolean isPassed(String date, int hour, int min) {
        boolean isIt;
        try {
            Calendar dateToBeChecked = Calendar.getInstance();
            DateFormat format = getDateFormat();
            Date dateObj = format.parse(date);
            dateToBeChecked.setTime(dateObj);
            dateToBeChecked.set(Calendar.HOUR_OF_DAY, hour);
            dateToBeChecked.set(Calendar.MINUTE, min);

            Calendar currDate = Calendar.getInstance();
            if (dateToBeChecked.after(currDate) || dateToBeChecked.equals(currDate)) {
                isIt = false;
            } else {
                isIt = true;
            }
        } catch (Exception e) {
            isIt =  true;//true is a fail case here hence default
        }
        Log.d("Prateek", "is Passed  [" + date + "] : " + isIt);
        return isIt;
    }

    public static boolean isPassed(String date, int hour, int min, Calendar calendarToCheckedFrom) {
        boolean isIt;
        try {
            Calendar dateToBeChecked = Calendar.getInstance();
            DateFormat format = getDateFormat();
            Date dateObj = format.parse(date);
            dateToBeChecked.setTime(dateObj);
            dateToBeChecked.set(Calendar.HOUR_OF_DAY, hour);
            dateToBeChecked.set(Calendar.MINUTE, min);

            Calendar currDate = Calendar.getInstance();
            if (dateToBeChecked.after(currDate) || dateToBeChecked.equals(currDate)) {
                isIt = false;
            } else {
                isIt = true;
            }
        } catch (Exception e) {
            isIt =  true;//true is a fail case here hence default
        }
        Log.d("Prateek", "is Passed  [" + date + "] : " + isIt);
        return isIt;
    }

    public static boolean isDatePassed(String dateStringToBeChecked, Calendar calendarToCheckedFrom) {
        boolean isDatePassed;
        try {
            Calendar dateToBeChecked = Calendar.getInstance();
            DateFormat format = getDateFormat();
            Date dateObj = format.parse(dateStringToBeChecked);
            dateToBeChecked.setTime(dateObj);

            if (dateToBeChecked.after(calendarToCheckedFrom) || dateToBeChecked.equals(calendarToCheckedFrom)) {
                isDatePassed = false;
            } else {
                isDatePassed = true;
            }
        } catch (Exception e) {
            isDatePassed =  true;//true is a fail case here hence default
        }
        Log.d("Prateek", "is Passed  [" + dateStringToBeChecked + "] : " + isDatePassed);
        return isDatePassed;
    }

//    public static boolean isReminderMissed(String dateStringToBeChecked, int hour, int min, Calendar calendarToBeCheckedWith) {
    public static boolean isReminderMissed(int dayOfMonth, int month, int year , int hour, int min, Calendar calendarToBeCheckedWith) {
        boolean isReminderMissed = true;

//        if (!isDatePassed(dateStringToBeChecked, calendarToBeCheckedWith)) {
//        if (!isPassed(dateStringToBeChecked, hour, min, calendarToBeCheckedWith)) {
        if (year == calendarToBeCheckedWith.get(Calendar.YEAR) && month == calendarToBeCheckedWith.get(Calendar.MONTH)
                && dayOfMonth == calendarToBeCheckedWith.get(Calendar.DAY_OF_MONTH)) {

            int hourToBeCheckedWith = calendarToBeCheckedWith.get(Calendar.HOUR_OF_DAY);
            int minToBeCheckedWith = calendarToBeCheckedWith.get(Calendar.MINUTE);

            if (hour == hourToBeCheckedWith && min <= minToBeCheckedWith) {
                return false;
            } else if (hour == hourToBeCheckedWith - 1 && min > minToBeCheckedWith) {
                return false;
            }
        }

        return isReminderMissed;
    }

    public static String getDateString(Calendar calendar) {

        DateFormat dateFormat = getDateFormat();
        return dateFormat.format(calendar.getTime());
    }

    public static DateFormat getDateFormat() {
        return new SimpleDateFormat("dd MMM yyyy");
    }

    public static String getTimeString(Calendar calendar) {
        DateFormat dateFormat = getTimeFormat();
        return dateFormat.format(calendar.getTime());
    }

    public static DateFormat getTimeFormat() {
        return new SimpleDateFormat("hh:mm a");
    }

    public static Calendar getCalendarOfReminder(Intent intent) {

        int dayOfMonth = intent.getIntExtra(Constants.EXTRA_REMINDER_REPEAT_DAY_OF_MONTH, 1);
        int month = intent.getIntExtra(Constants.EXTRA_REMINDER_REPEAT_MONTH, 0);
        int year = intent.getIntExtra(Constants.EXTRA_REMINDER_REPEAT_YEAR, 2024);
        int hour = intent.getIntExtra(Constants.EXTRA_REMINDER_REPEAT_HOUR_OF_DAY, 7);
        int min = intent.getIntExtra(Constants.EXTRA_REMINDER_REPEAT_MINUTE, 0);

        Log.d("Prateek", "[ReminderAlarmReceiver][onRecieve] dayOfMonth :"+dayOfMonth);
        Log.d("Prateek", "[ReminderAlarmReceiver][onRecieve] month :" + month);
        Log.d("Prateek", "[ReminderAlarmReceiver][onRecieve] year :" + year);
        Log.d("Prateek", "[ReminderAlarmReceiver][onRecieve] hour :" + hour);
        Log.d("Prateek", "[ReminderAlarmReceiver][onRecieve] min :" + min);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);

        return calendar;
    }

    public static void updateReminderObjectForUID(Context context, Intent intent) {

        String uid = intent.getStringExtra(Constants.EXTRA_REMINDER_UID);

        if(context == null || TextUtils.isEmpty(uid)) {
            return;
        }

        String selection=Constants.UID + "=" + "?";
        String [] selArgs = new String[]{""+uid};

        Reminder reminder = null;

        Cursor cursor = context.getContentResolver().query(Constants.DB_REMINDER_TABLE_NAME_URI, null, selection, selArgs, null);
        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            reminder =  new Reminder(cursor);
            cursor.close();
        }

        boolean isPassed = isPassed(reminder.getDueDateString(), reminder.getDueHour(), reminder.getDueMinute());

        int reminderType = reminder.getReminderType();
        if (reminderType == Constants.REPEATED) {

            Calendar dueInfoCalendar = getCalendarOfReminder(intent);
            reminder.setDueDateString(Utility.getDateString(dueInfoCalendar));
            reminder.setDueDayOfMonth(dueInfoCalendar.get(Calendar.DAY_OF_MONTH));
            reminder.setDueMonth(dueInfoCalendar.get(Calendar.MONTH));
            reminder.setDueYear(dueInfoCalendar.get(Calendar.YEAR));

            reminder.setDueTimeString(Utility.getTimeString(dueInfoCalendar));
            reminder.setDueHour(dueInfoCalendar.get(Calendar.HOUR_OF_DAY));
            reminder.setDueMinute(dueInfoCalendar.get(Calendar.MINUTE));

        } else {
            reminder.setState(Constants.REMINDER_COMPLETE_STATE);
        }

        if (isPassed) {
            reminder.setIsReminderMissed(true);
        }

        reminder.updateItemInDb(context);

    }

    public static void resetMissedReminderObject(Context context) {

//        String selection=Constants.IS_REMINDER_MISSSED + " = 1";
        String selection=Constants.REMINDER_NOTIFICATION_STATE + " = " + Constants.REMINDER_NOTIFICATION_MISSED;

        Cursor cursor = context.getContentResolver().query(Constants.DB_REMINDER_TABLE_NAME_URI, null, selection, null, null);
        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            Reminder reminder = null;
            while(!cursor.isAfterLast()) {
                reminder =  new Reminder(cursor);
                reminder.setIsReminderMissed(false);
                if (reminder.getReminderType() == Constants.SINGLE_SHOT) {
                    reminder.setNotificationState(Constants.REMINDER_NOTIFICATION_COMPLETE);
                } else {
                    reminder.setNotificationState(Constants.REMINDER_NOTIFICATION_PENDING);
                }

                reminder.updateItemInDb(context);
                cursor.moveToNext();
            }

            cursor.close();
        }
    }

    public static void showCustomNotification(Context context, String uid, String title, String message, int notificationId) {

        if (message == null || TextUtils.isEmpty(message)) {
            return;
        }

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            notificationBuilder.setContentTitle(title);
        }

        notificationBuilder.setContentText(message);
        notificationBuilder.setSmallIcon(R.drawable.ic_alarmclock);
        notificationBuilder.setContentIntent(pIntent);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    public static void showCustomNotification(Context context, Reminder reminder) {

        if (reminder == null || TextUtils.isEmpty(reminder.getMessage())) {
            return;
        }

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        if (!TextUtils.isEmpty(reminder.getTitle())) {
            notificationBuilder.setContentTitle(reminder.getTitle());
        }

        if (!reminder.getMessage().equals("NA")) {
            notificationBuilder.setContentText(reminder.getMessage());
        }

        notificationBuilder.setSmallIcon(R.drawable.ic_alarmclock);
        notificationBuilder.setContentIntent(pIntent);
        notificationBuilder.setAutoCancel(true);
//        notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);

        Notification notification = notificationBuilder.build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_LIGHTS;

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = getUniqueNotificationId();

        if (reminder.getReminderType() == Constants.REPEATED) {
            if (reminder.getNotificationId() == 0) {
                setNotificationIdInReminderObject(context, reminder, notificationId);
            } else {
                notificationId = reminder.getNotificationId();
            }
        }

        notificationManager.notify(notificationId, notification);
    }

    public static void setNotificationIdInReminderObject(Context context, Reminder reminder, int notificationId) {

        if (context == null  || reminder == null || notificationId == 0) {
            return;
        }

        reminder.setNotificationId(notificationId);
        reminder.updateItemInDb(context);
    }

    /*public int getNotificationId(Intent intent) {

        int notificationId = 0;

        int reminderType = intent.getIntExtra(Constants.EXTRA_REMINDER_TYPE, Constants.SINGLE_SHOT);

        if (reminderType == Constants.REPEATED) {
            notificationId = intent.getIntExtra(Constants.EXTRA_REMINDER_NOTIFICATION_ID, 0);
            if (notificationId == 0) {
                notificationId = getUniqueNotificationId();
                intent.putExtra(Constants.EXTRA_REMINDER_NOTIFICATION_ID, notificationId);
            }
            setAlarm(intent);
        } else {
            notificationId = getUniqueNotificationId();
            Utility.updateReminderObjectForUID(mContext, intent);
        }
        return notificationId;
    }*/

    public static int getUniqueNotificationId() {
        long time = new Date().getTime();
        String tmpStr = String.valueOf(time);
        String last4Str = tmpStr.substring(tmpStr.length() -5);
        return Integer.valueOf(last4Str);
    }

    public static void setAlarm(Context mContext, Intent intent) {
        int repeatIntervalType = intent.getIntExtra(Constants.EXTRA_REMINDER_REPEAT_INTERVAL_TYPE, Constants.REPEAT_INTERVAL_DAILY);
        int repeatIntervalCount = intent.getIntExtra(Constants.EXTRA_REMINDER_REPEAT_INTERVAL_COUNT, 1);

        Log.d("Prateek", "[ReminderAlarmReceiver][onRecieve] repeatIntervalType :"+repeatIntervalType);
        Log.d("Prateek", "[ReminderAlarmReceiver][onRecieve] repeatIntervalCount :" + repeatIntervalCount);

        Calendar repeatCalendar = getNextRepeatTimeCalendar(repeatIntervalType, repeatIntervalCount, Utility.getCalendarOfReminder(intent));

        intent.putExtra(Constants.EXTRA_REMINDER_REPEAT_DAY_OF_MONTH, repeatCalendar.get(Calendar.DAY_OF_MONTH));
        intent.putExtra(Constants.EXTRA_REMINDER_REPEAT_MONTH, repeatCalendar.get(Calendar.MONTH));
        intent.putExtra(Constants.EXTRA_REMINDER_REPEAT_YEAR, repeatCalendar.get(Calendar.YEAR));
        intent.putExtra(Constants.EXTRA_REMINDER_REPEAT_HOUR_OF_DAY, repeatCalendar.get(Calendar.HOUR_OF_DAY));
        intent.putExtra(Constants.EXTRA_REMINDER_REPEAT_MINUTE, repeatCalendar.get(Calendar.MINUTE));

        Utility.updateReminderObjectForUID(mContext, intent);

        Log.d("Prateek", "[ReminderAlarmReceiver][setAlarm] dayOfMonth :" + repeatCalendar.get(Calendar.DAY_OF_MONTH));
        Log.d("Prateek", "[ReminderAlarmReceiver][setAlarm] month :" + repeatCalendar.get(Calendar.MONTH));
        Log.d("Prateek", "[ReminderAlarmReceiver][setAlarm] year :" + repeatCalendar.get(Calendar.YEAR));
        Log.d("Prateek", "[ReminderAlarmReceiver][setAlarm] hour :" + repeatCalendar.get(Calendar.HOUR_OF_DAY));
        Log.d("Prateek", "[ReminderAlarmReceiver][setAlarm] min :" + repeatCalendar.get(Calendar.MINUTE));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, repeatCalendar.getTimeInMillis(), pendingIntent);

    }

    public static void setAlarm(Context context, Calendar calendar) {

//        Log.d("Prateek", "[ReminderAlarmReceiver][onRecieve] repeatIntervalType :"+repeatIntervalType);
//        Log.d("Prateek", "[ReminderAlarmReceiver][onRecieve] repeatIntervalCount :" + repeatIntervalCount);

       /* Calendar repeatCalendar = getNextRepeatTimeCalendar(repeatIntervalType, repeatIntervalCount, Utility.getCalendarOfReminder(intent));

        intent.putExtra(Constants.EXTRA_REMINDER_REPEAT_DAY_OF_MONTH, repeatCalendar.get(Calendar.DAY_OF_MONTH));
        intent.putExtra(Constants.EXTRA_REMINDER_REPEAT_MONTH, repeatCalendar.get(Calendar.MONTH));
        intent.putExtra(Constants.EXTRA_REMINDER_REPEAT_YEAR, repeatCalendar.get(Calendar.YEAR));
        intent.putExtra(Constants.EXTRA_REMINDER_REPEAT_HOUR_OF_DAY, repeatCalendar.get(Calendar.HOUR_OF_DAY));
        intent.putExtra(Constants.EXTRA_REMINDER_REPEAT_MINUTE, repeatCalendar.get(Calendar.MINUTE));*/

//        Utility.updateReminderObjectForUID(mContext, intent);

        /*Log.d("Prateek", "[ReminderAlarmReceiver][setAlarm] dayOfMonth :" + repeatCalendar.get(Calendar.DAY_OF_MONTH));
        Log.d("Prateek", "[ReminderAlarmReceiver][setAlarm] month :" + repeatCalendar.get(Calendar.MONTH));
        Log.d("Prateek", "[ReminderAlarmReceiver][setAlarm] year :" + repeatCalendar.get(Calendar.YEAR));
        Log.d("Prateek", "[ReminderAlarmReceiver][setAlarm] hour :" + repeatCalendar.get(Calendar.HOUR_OF_DAY));
        Log.d("Prateek", "[ReminderAlarmReceiver][setAlarm] min :" + repeatCalendar.get(Calendar.MINUTE));*/

        calendar.set(Calendar.SECOND, 0);
        Intent intent = new Intent(context, ReminderAlarmReceiver.class);
        intent.setAction(Constants.REMINDER_ALARM_INTENT_ACTION);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

    public static Calendar getNextRepeatTimeCalendar(int repeatIntervalType, int repeatIntervalCount, Calendar dueDateCalendar) {

        Calendar firstRepeatCalendar = (Calendar)dueDateCalendar.clone();

        switch (repeatIntervalType) {
            case Constants.REPEAT_INTERVAL_DAILY:
                firstRepeatCalendar.add(Calendar.DAY_OF_MONTH, repeatIntervalCount);
                break;
            case Constants.REPEAT_INTERVAL_WEEKLY:
                firstRepeatCalendar.add(Calendar.DAY_OF_MONTH, repeatIntervalCount * 7);
                break;
            case Constants.REPEAT_INTERVAL_MONTHLY:
                firstRepeatCalendar.add(Calendar.MONTH, repeatIntervalCount);
                break;
            case Constants.REPEAT_INTERVAL_YEARLY:
                firstRepeatCalendar.add(Calendar.YEAR, repeatIntervalCount);
                break;
            case Constants.REPEAT_INTERVAL_HOURLY:
                firstRepeatCalendar.add(Calendar.HOUR_OF_DAY, repeatIntervalCount);
                break;
            case Constants.REPEAT_INTERVAL_MINUTES:
                firstRepeatCalendar.add(Calendar.MINUTE, repeatIntervalCount);
                break;
        }

        return firstRepeatCalendar;
    }

    public static Calendar getReminderCalendar(int dayOfMonth, int month, int year, int hour, int min) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);

        return calendar;
    }

    public static void setAlarmForDailyReminderSync(Context context) {

        Log.d(LOG_TAG, "setAlarmForDailyReminderSync");

        Calendar calendar = Calendar.getInstance();

        // Daily Sync at 12:05 in the morning
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 5);
        calendar.set(Calendar.SECOND, 0);

        Log.d("Prateek", "[ReminderAlarmReceiver][setAlarm] hour :" + calendar.get(Calendar.HOUR_OF_DAY));
        Log.d("Prateek", "[ReminderAlarmReceiver][setAlarm] min :" + calendar.get(Calendar.MINUTE));

        Intent intent = new Intent(context, AlarmSetterReceiver.class);
        intent.setAction(Constants.REMINDER_ALARM_SETTER_INTENT_ACTION);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public static void changeReminderState(Context context, String uid) {

        String selection=Constants.UID + " = ?";
        String [] selArgs = new String[]{""+uid};

        Cursor cursor = context.getContentResolver().query(Constants.DB_REMINDER_TABLE_NAME_URI, null, selection, selArgs, null);
        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            Reminder reminder = new Reminder(cursor);
            if (reminder.getState() == Constants.REMINDER_ACTIVE_STATE) {
                reminder.setState(Constants.REMINDER_COMPLETE_STATE);
                reminder.setNotificationState(Constants.REMINDER_NOTIFICATION_COMPLETE);
            } else {
                reminder.setState(Constants.REMINDER_ACTIVE_STATE);
                reminder.setNotificationState(Constants.REMINDER_NOTIFICATION_PENDING);

                reminder.getDueDayOfMonth();
                reminder.getDueYear();
                reminder.getDueMonth();

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.SECOND, 0);

                if (reminder.getReminderType() == Constants.SINGLE_SHOT) {

                    if ((reminder.getDueHour() < calendar.get(Calendar.HOUR_OF_DAY))
                            || ((reminder.getDueHour() == calendar.get(Calendar.HOUR_OF_DAY)) && (reminder.getDueMinute() <= calendar.get(Calendar.MINUTE)))) {

                        calendar.add(Calendar.DAY_OF_MONTH, 1);

                    }

                    calendar.set(Calendar.HOUR_OF_DAY, reminder.getDueHour());
                    calendar.set(Calendar.MINUTE, reminder.getDueMinute());

                } else  {

                    switch (reminder.getRepeatIntervalType()) {

                        case Constants.REPEAT_INTERVAL_MINUTES :
                        case Constants.REPEAT_INTERVAL_HOURLY:
                            calendar = getNextRepeatTimeCalendar(reminder.getRepeatIntervalType(), reminder.getRepeatIntervalNumber(), calendar);
                            /*reminder.setDueTimeString(Utility.getTimeString(calendar));
                            reminder.setDueHour(calendar.get(Calendar.HOUR_OF_DAY));
                            reminder.setDueMinute(calendar.get(Calendar.MINUTE));*/
                            break;
                        case Constants.REPEAT_INTERVAL_DAILY:
                            if ((reminder.getDueHour() < calendar.get(Calendar.HOUR_OF_DAY))
                                    || ((reminder.getDueHour() == calendar.get(Calendar.HOUR_OF_DAY)) && (reminder.getDueMinute() <= calendar.get(Calendar.MINUTE)))) {

                                calendar.add(Calendar.DAY_OF_MONTH, 1);

                            }

                            calendar.set(Calendar.HOUR_OF_DAY, reminder.getDueHour());
                            calendar.set(Calendar.MINUTE, reminder.getDueMinute());
                            break;
                        case Constants.REPEAT_INTERVAL_WEEKLY:
                        case Constants.REPEAT_INTERVAL_MONTHLY:
                        case Constants.REPEAT_INTERVAL_YEARLY:
                            int currentdayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                            int currentMonth = calendar.get(Calendar.MONTH);
                            int currentYear = calendar.get(Calendar.YEAR);
                            if (currentYear <=  reminder.getDueYear() && ((currentMonth < reminder.getDueMonth()) ||
                                     ((currentdayOfMonth == reminder.getDueDayOfMonth())&& (currentdayOfMonth < reminder.getDueDayOfMonth())))) {
                                calendar = getReminderCalendar(reminder.getDueDayOfMonth(), reminder.getDueMonth(), reminder.getDueYear(),
                                        reminder.getDueHour(), reminder.getDueMinute());
                            } else if (currentYear >=  reminder.getDueYear() && currentMonth >= reminder.getDueMonth()
                                    && currentdayOfMonth > reminder.getDueDayOfMonth()){
                                calendar = getReminderCalendar(reminder.getDueDayOfMonth(), reminder.getDueMonth(), reminder.getDueYear(),
                                        reminder.getDueHour(), reminder.getDueMinute());
                            calendar = getNextRepeatTimeCalendar(reminder.getRepeatIntervalType(), reminder.getRepeatIntervalNumber(), calendar);
                            } else if (currentYear ==  reminder.getDueYear() && currentMonth == reminder.getDueMonth()
                                    && currentdayOfMonth == reminder.getDueDayOfMonth()) {
                                calendar = getReminderCalendar(reminder.getDueDayOfMonth(), reminder.getDueMonth(), reminder.getDueYear(),
                                        reminder.getDueHour(), reminder.getDueMinute());
                                if ((reminder.getDueHour() < calendar.get(Calendar.HOUR_OF_DAY))
                                        || ((reminder.getDueHour() == calendar.get(Calendar.HOUR_OF_DAY)) && (reminder.getDueMinute() <= calendar.get(Calendar.MINUTE)))) {

                                    calendar = getNextRepeatTimeCalendar(reminder.getRepeatIntervalType(), reminder.getRepeatIntervalNumber(), calendar);

                                }
                            }
                            break;
                        default:
                            break;
                    }

                }

                reminder.setDueDateString(Utility.getDateString(calendar));
                reminder.setDueDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
                reminder.setDueMonth(calendar.get(Calendar.MONTH));
                reminder.setDueYear(calendar.get(Calendar.YEAR));

                reminder.setDueTimeString(Utility.getTimeString(calendar));
                reminder.setDueHour(calendar.get(Calendar.HOUR_OF_DAY));
                reminder.setDueMinute(calendar.get(Calendar.MINUTE));

                setAlarm(context, calendar);
            }

            reminder.updateItemInDb(context);

            cursor.close();
        }

        /*calendar.set(Calendar.DAY_OF_MONTH, reminder.getDueDayOfMonth());
                        calendar.set(Calendar.MONTH, reminder.getDueMonth());
                        calendar.set(Calendar.YEAR, reminder.getDueYear());*/



                        /*reminder.setDueTimeString(Utility.getTimeString(calendar));
                        reminder.setDueHour(calendar.get(Calendar.HOUR_OF_DAY));
                        reminder.setDueMinute(calendar.get(Calendar.MINUTE));*/
    }

    public static Reminder getReminderObjectFromUID(Context context, String uid) {

        String selection=Constants.UID + " = ?";
        String [] selArgs = new String[]{""+uid};

        Reminder reminder = null;
        Cursor cursor = context.getContentResolver().query(Constants.DB_REMINDER_TABLE_NAME_URI, null, selection, selArgs, null);
        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            reminder = new Reminder(cursor);
        }

        return reminder;
    }
}
