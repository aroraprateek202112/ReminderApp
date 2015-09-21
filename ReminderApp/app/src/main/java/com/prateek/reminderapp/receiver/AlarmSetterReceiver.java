package com.prateek.reminderapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.prateek.reminderapp.service.AlarmSetterService;

/**
 * Created by Prateek on 20-09-2015.
 */
public class AlarmSetterReceiver extends BroadcastReceiver{

    private static final String LOG_TAG = AlarmSetterReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "" + LOG_TAG, Toast.LENGTH_LONG).show();

        if (intent.getAction() == Intent.ACTION_BOOT_COMPLETED) {

            Log.d(LOG_TAG, "onReceive");

            Intent alarmSetterService = new Intent(context, AlarmSetterService.class);
            context.startService(alarmSetterService);
        }

    }
}
