package com.prateek.reminderapp.receiver;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.prateek.reminderapp.Constants;
import com.prateek.reminderapp.activity.MainActivity;
import com.prateek.reminderapp.R;
import com.prateek.reminderapp.Utility;
import com.prateek.reminderapp.service.ReminderManagerService;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Prateek on 17-09-2015.
 */
public class ReminderAlarmReceiver extends BroadcastReceiver {

    Context mContext = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Prateek", "[ReminderAlarmReceiver][onRecieve]");
        mContext = context;

        int alarmCount= intent.getIntExtra(Intent.EXTRA_ALARM_COUNT, 100);
        Log.d("Prateek", "[ReminderAlarmReceiver][onRecieve] alarmCount :"+alarmCount);
        if (intent.getAction() == Constants.REMINDER_ALARM_INTENT_ACTION)  {
            /*Intent i = new Intent(context, AlarmActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);*/
            Log.d("Prateek", "[ReminderAlarmReceiver][onRecieve] inside alarmCount :"+alarmCount);
            /*String uid = intent.getStringExtra(Constants.EXTRA_REMINDER_UID);
            String title = intent.getStringExtra(Constants.EXTRA_REMINDER_TITLE);
            String message = intent.getStringExtra(Constants.EXTRA_REMINDER_MESSAGE);

            if (TextUtils.isEmpty(title) && TextUtils.isEmpty(message)) {
                return;
            }*/

            //showCustomNotification(uid, title, message, getNotificationId(intent));

            Intent intentService = new Intent(context.getApplicationContext(), ReminderManagerService.class);
            context.startService(intentService);
        }

    }

    public int getNotificationId(Intent intent) {

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
    }

    public void showCustomNotification(String uid, String title, String message, int notificationId) {

        if (message == null || TextUtils.isEmpty(message)) {
            return;
        }

        Intent intent = new Intent(mContext, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(mContext, (int) System.currentTimeMillis(), intent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext);
        if (!TextUtils.isEmpty(title)) {
            notificationBuilder.setContentTitle(title);
        }

        notificationBuilder.setContentText(message);
        notificationBuilder.setSmallIcon(R.drawable.ic_alarmclock);
        notificationBuilder.setContentIntent(pIntent);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);

        NotificationManager notificationManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    public int getUniqueNotificationId() {
        long time = new Date().getTime();
        String tmpStr = String.valueOf(time);
        String last4Str = tmpStr.substring(tmpStr.length() -5);
        return Integer.valueOf(last4Str);
    }

    public void setAlarm(Intent intent) {
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

    /*public static Calendar getCalendarOfReminder(Intent intent) {

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
    }*/

    public Calendar getNextRepeatTimeCalendar(int repeatIntervalType, int repeatIntervalCount, Calendar dueDateCalendar) {

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

    /*public void ringAlarm() {
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        if (alert == null) {
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            if (alert == null) {
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }

        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        MediaPlayer mediaPlayer = MediaPlayer.create(mContext, alert);


        try {
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mediaPlayer.setLooping(true);
                mediaPlayer.prepare();
                mediaPlayer.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/
}
