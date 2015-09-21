package com.prateek.reminderapp;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * Created by Prateek on 19-09-2015.
 */
public class ReminderCursorAdapter extends CursorAdapter {

    private static final String LOG_TAG = ReminderCursorAdapter.class.getSimpleName();

    public ReminderCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.d(LOG_TAG, "newView");

        View view = new ReminderItemView(context, null);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.d(LOG_TAG, "bindView");

        Reminder reminder = new Reminder(cursor);

        if (reminder == null || reminder.getUID() == null) {
            Log.d(LOG_TAG,"Adapter model  = null");
        } else {
            ((ReminderItemView)view).initView(reminder);
            view.setTag(reminder);
        }

    }
}
