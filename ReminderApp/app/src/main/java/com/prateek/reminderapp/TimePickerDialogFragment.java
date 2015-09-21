package com.prateek.reminderapp;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by prateek02.arora on 16-09-2015.
 */
public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    TimeSetter mTimeSetter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, min, false);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        if (mTimeSetter != null) {
            mTimeSetter.setTime(hourOfDay, minute);
        }
    }

    public void setListener(TimeSetter timeSetter) {
        mTimeSetter = timeSetter;
    }

    public interface TimeSetter {
        public void setTime(int hourOfDay, int minute);
    }
}
