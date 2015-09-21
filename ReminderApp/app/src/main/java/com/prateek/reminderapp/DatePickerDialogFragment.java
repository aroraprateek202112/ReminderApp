package com.prateek.reminderapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by prateek02.arora on 16-09-2015.
 */
public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    DateSetter mDateSetter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current date as the default date in the picker
        Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        // Create a new instance of Date Picker Dialog and return it.
        return new DatePickerDialog(getActivity(), this, year, month, date);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        if (mDateSetter != null) {
            mDateSetter.setDate(year, monthOfYear, dayOfMonth);
        }
    }

    public void setListener(DateSetter dateSetter) {
        mDateSetter = dateSetter;
    }

    public interface DateSetter {
        public void setDate(int year, int month, int dayOfMonth);
    }
}
