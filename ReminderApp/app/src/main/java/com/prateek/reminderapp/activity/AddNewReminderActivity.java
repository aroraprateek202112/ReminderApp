package com.prateek.reminderapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.prateek.reminderapp.Constants;
import com.prateek.reminderapp.DatePickerDialogFragment;
import com.prateek.reminderapp.R;
import com.prateek.reminderapp.Reminder;
import com.prateek.reminderapp.TimePickerDialogFragment;
import com.prateek.reminderapp.Utility;
import com.prateek.reminderapp.receiver.ReminderAlarmReceiver;

import java.util.Calendar;

/**
 * Created by prateek02.arora on 16-09-2015.
 */
public class AddNewReminderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView et_title;

    Button bt_due_date;
    Button bt_due_time;

    Calendar mReminderDateAndTime = Calendar.getInstance();
    Calendar mNotifyBeforeDateAndTime = null;

    EditText et_remider_info;
    EditText et_notify_time;

    Spinner sp_notify_interval_type;
    Spinner sp_reminder_type;
    Spinner sp_repeat_interval_type;

    LinearLayout ll_repeat;

    int mCategory = Constants.CATEGORY_TODO;
    int mNotifyIntervalType;
    int mRepeatIntervalType;
    int mMode;

    boolean mIsNotifyBeforeNeed = true;
    boolean mIsVibrationNeeded = true;
    boolean mIsRepeatAllowed = false;
    private EditText et_repeat_interval_number;
    private Spinner sp_category;
    private ImageView iv_category_icon;

    Reminder reminder = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_reminder1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);

        et_title = (EditText) findViewById(R.id.et_title_id);

        iv_category_icon = (ImageView)findViewById(R.id.iv_category_icon_id);

        bt_due_date = (Button) findViewById(R.id.bt_due_date_id);
        bt_due_time = (Button) findViewById(R.id.bt_time_id);

        et_remider_info = (EditText) findViewById(R.id.et_remider_info_id);
        et_notify_time = (EditText) findViewById(R.id.et_notify_id);
        et_repeat_interval_number = (EditText) findViewById(R.id.et_repeat_id);

        sp_category = (Spinner)findViewById(R.id.sp_category_id);
        sp_category.setOnItemSelectedListener(this);
//        setCategorySpinner();

        sp_reminder_type = (Spinner) findViewById(R.id.sp_reminder_type_id);
        sp_reminder_type.setOnItemSelectedListener(this);

        ll_repeat = (LinearLayout) findViewById(R.id.ll_repeat_id);

        sp_repeat_interval_type = (Spinner) findViewById(R.id.sp_repeat_interval_type_id);
        sp_repeat_interval_type.setOnItemSelectedListener(this);

        /*RadioGroup categoryRadioGroup = (RadioGroup) findViewById(R.id.rg_category_id);
        categoryRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                if (checkedId == R.id.rb_category_payment_id) {
                    et_remider_info.setHint(R.string.amount);
                    mCategory = Constants.CATEGORY_PAYMENT;
                } else {
                    et_remider_info.setHint(R.string.notes);
                    mCategory = Constants.CATEGORY_GENERAL;
                }
            }
        });*/

//        registerCheckTextViewListener();

        Intent intent = getIntent();
        mMode = intent.getIntExtra(Constants.EXTRA_REMINDER_OPEN_MODE_TYPE, Constants.MODE_NEW_REMINDER);
        String uid = intent.getStringExtra(Constants.EXTRA_REMINDER_UID);
        if (mMode == Constants.MODE_EDIT_REMINDER && uid != null) {

            reminder = Utility.getReminderObjectFromUID(this, uid);
            updateViewFields();

        } else {

            reminder = new Reminder();
            setDefaultReminderDate();
            setDefaultReminderTime();
        }

    }

    public void updateViewFields() {

        updateCategoryField();

        et_title.setText(reminder.getTitle());
        et_remider_info.setText(reminder.getMessage());
        bt_due_date.setText(reminder.getDueDateString());
        bt_due_time.setText(reminder.getDueTimeString());

        mReminderDateAndTime = Utility.getReminderCalendar(reminder.getDueDayOfMonth(), reminder.getDueMonth(), reminder.getDueYear(), reminder.getDueHour(), reminder.getDueMinute());

        if (reminder.getReminderType() == Constants.SINGLE_SHOT) {
            mIsRepeatAllowed = false;
            sp_reminder_type.setSelection(0);

            ll_repeat.setVisibility(View.GONE);

        } else {
            mIsRepeatAllowed = true;
            sp_reminder_type.setSelection(1);

            ll_repeat.setVisibility(View.VISIBLE);
            et_repeat_interval_number.setText(""+reminder.getRepeatIntervalNumber());

            int reminderRepeatIntervlType = reminder.getRepeatIntervalType();
            int position = 0;
            switch (reminderRepeatIntervlType) {
                case Constants.REPEAT_INTERVAL_DAILY:
                    position = 0;
                    break;
                case Constants.REPEAT_INTERVAL_WEEKLY:
                    position = 1;
                    break;
                case Constants.REPEAT_INTERVAL_MONTHLY:
                    position = 2;
                    break;
                case Constants.REPEAT_INTERVAL_YEARLY:
                    position = 3;
                    break;
                case Constants.REPEAT_INTERVAL_HOURLY:
                    position = 4;
                    break;
                case Constants.REPEAT_INTERVAL_MINUTES:
                    position = 5;
                    break;
                default:
                    break;
            }

            sp_repeat_interval_type.setSelection(position);
        }

    }

    public void updateCategoryField (){

        mCategory = reminder.getCategory();
        int position = 0;
        switch (mCategory) {
            case Constants.CATEGORY_TODO:
                iv_category_icon.setImageResource(R.drawable.ic_todo);
                position = 0;
                break;
            case Constants.CATEGORY_PAYMENT:
                iv_category_icon.setImageResource(R.drawable.ic_payment);
                position = 1;
                break;
            case Constants.CATEGORY_ANNIVERSERY:
                iv_category_icon.setImageResource(R.drawable.ic_birthday);
                position = 2;
                break;
            default:
                break;
        }
        sp_category.setSelection(position);
    }

    public void setCategorySpinner() {

        sp_category = (Spinner)findViewById(R.id.sp_category_id);

        String[] testArray = getResources().getStringArray(R.array.category_list);

        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, testArray);
        adapter_state
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_category.setAdapter(adapter_state);
    }

    /*public void registerCheckTextViewListener() {

        CheckedTextView ctv_vibration = (CheckedTextView) findViewById(R.id.ctv_vibration_id);
        ctv_vibration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckedTextView ctv = (CheckedTextView) view;
                if (ctv.isChecked()) {
                    ctv.setChecked(false);
                    mIsVibrationNeeded = false;
                } else {
                    ctv.setChecked(true);
                    mIsVibrationNeeded = true;
                }
            }
        });
    }*/

    public void setDefaultReminderDate() {

        if (mReminderDateAndTime == null) {
            mReminderDateAndTime = Calendar.getInstance();
        }

        /*DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        String currentDateString = dateFormat.format(mReminderDateAndTime.getTime());*/
        bt_due_date.setText(Utility.getDateString(mReminderDateAndTime));
    }

    public void setDefaultReminderTime() {

        if (mReminderDateAndTime == null) {
            mReminderDateAndTime = Calendar.getInstance();
        }
        mReminderDateAndTime.add(Calendar.MINUTE, 10);      // Reminder Should be 10 min before
        /*DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String currentTime = dateFormat.format(mReminderDateAndTime.getTime());*/
        bt_due_time.setText(Utility.getTimeString(mReminderDateAndTime));
    }

    public void showDatePickerDialog(View view) {

        DatePickerDialogFragment datePickerDialog = new DatePickerDialogFragment();
        datePickerDialog.setListener(new DatePickerDialogFragment.DateSetter() {
            @Override
            public void setDate(int year, int month, int dayOfMonth) {

                if (mReminderDateAndTime == null) {
                    return;
                }

                Calendar calendar = Calendar.getInstance();
                if (calendar.get(Calendar.YEAR) <= year && calendar.get(Calendar.MONTH) <= month && calendar.get(Calendar.DAY_OF_MONTH) <= dayOfMonth) {
                    mReminderDateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    mReminderDateAndTime.set(Calendar.MONTH, month);
                    mReminderDateAndTime.set(Calendar.YEAR, year);
                    bt_due_date.setText(Utility.getDateString(mReminderDateAndTime));
                } else {
                    Toast.makeText(AddNewReminderActivity.this,"Please Don't set Back Date", Toast.LENGTH_SHORT).show();
                }
//                mReminderDateAndTime = Calendar.getInstance();

                /*DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                bt_due_date.setText(dateFormat.format(mReminderDateAndTime.getTime()));*/


            }
        });
        datePickerDialog.show(getSupportFragmentManager(), "datePicker");

    }

    public void showTimePickerDialog(View view) {

        TimePickerDialogFragment timePickerDialog = new TimePickerDialogFragment();
        timePickerDialog.setListener(new TimePickerDialogFragment.TimeSetter() {
            @Override
            public void setTime(int hourOfDay, int minute) {

                if (mReminderDateAndTime == null) {
                    return;
                }

               /* Calendar calendar = Calendar.getInstance();
                if (calendar.get(Calendar.HOUR_OF_DAY) <= year && calendar.get(Calendar.MONTH) <= month && calendar.get(Calendar.DAY_OF_MONTH) <= dayOfMonth) {

                }*/
//                Calendar calendar = Calendar.getInstance();
                mReminderDateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mReminderDateAndTime.set(Calendar.MINUTE, minute);
//                DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
//                bt_due_time.setText(dateFormat.format(mReminderDateAndTime.getTime()));
                bt_due_time.setText(Utility.getTimeString(mReminderDateAndTime));
            }
        });
        timePickerDialog.show(getSupportFragmentManager(), "timePicker");

    }

    public boolean isSavingAllowed() {

        if ((TextUtils.isEmpty(et_title.getText()) || (TextUtils.isEmpty(et_remider_info.getText())))) {
            return false;
        }

        return true;
    }

    public void saveReminder(View view) {

        if (isSavingAllowed()) {

//            Reminder reminder = new Reminder();
            reminder.setTitle(et_title.getText().toString());
            reminder.setCategory(mCategory);

            reminder.setMessage(et_remider_info.getText().toString());

            reminder.setDueDateString(Utility.getDateString(mReminderDateAndTime));
            reminder.setDueDayOfMonth(mReminderDateAndTime.get(Calendar.DAY_OF_MONTH));
            reminder.setDueMonth(mReminderDateAndTime.get(Calendar.MONTH));
            reminder.setDueYear(mReminderDateAndTime.get(Calendar.YEAR));

            reminder.setDueTimeString(Utility.getTimeString(mReminderDateAndTime));
            reminder.setDueHour(mReminderDateAndTime.get(Calendar.HOUR_OF_DAY));
            reminder.setDueMinute(mReminderDateAndTime.get(Calendar.MINUTE));

            reminder.setIsRepeated(mIsRepeatAllowed);

            if (mIsRepeatAllowed) {
                reminder.setRepeatIntervalType(mRepeatIntervalType);
                int intervalNumber = 1;
                if (!TextUtils.isEmpty(et_repeat_interval_number.getText().toString())) {
                    intervalNumber = Integer.parseInt(et_repeat_interval_number.getText().toString());
                }
                reminder.setRepeatIntervalNumber(intervalNumber);
//                reminder.setRepeatExactDuration(0); //TODO Need to set properly

            }
            reminder.setState(Constants.REMINDER_ACTIVE_STATE);

            if (mMode == Constants.MODE_EDIT_REMINDER) {
                reminder.updateItemInDb(this);
            } else  {
                reminder.insertItemInDB(this);
            }

            Utility.setAlarm(this, mReminderDateAndTime);

            finish();
        }

    }


    public Calendar getRepeatTimeCalendar(int repeatIntervalType, int repeatIntervalCount, Calendar dueDateCalendar) {

        Calendar firstRepeatCalendar = (Calendar) dueDateCalendar.clone();

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

    public void setRepeatingInterval(Calendar calendar, int intervalCount) {

        calendar.set(Calendar.SECOND, 0);
        Intent intent = new Intent(this, ReminderAlarmReceiver.class);
        intent.setAction(Constants.REMINDER_ALARM_INTENT_ACTION);
        intent.putExtra(Constants.EXTRA_REMINDER_TYPE, Constants.REPEATED);
        intent.putExtra(Constants.EXTRA_REMINDER_REPEAT_INTERVAL_TYPE, mRepeatIntervalType);
        intent.putExtra(Constants.EXTRA_REMINDER_REPEAT_INTERVAL_COUNT, intervalCount);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

        /*if (id == R.id.sp_notify_interval_id) {
            Toast.makeText(this, "Notify Interval", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.sp_reminder_type_id) {
            Toast.makeText(this, "Repeat", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.sp_repeat_interval_id) {
            Toast.makeText(this, "Repeat Interval", Toast.LENGTH_SHORT).show();
        }*/
        switch (adapterView.getId()) {
            case R.id.sp_category_id:
                changeCategory(position);
                break;
            case R.id.sp_reminder_type_id:
                if (position == 1 && ll_repeat.getVisibility() != View.VISIBLE) {
                    ll_repeat.setVisibility(View.VISIBLE);
                    mIsRepeatAllowed = true;
                } else if (position == 0) {
                    ll_repeat.setVisibility(View.GONE);
                    mIsRepeatAllowed = false;
                }
//                Toast.makeText(this, "Repeat", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sp_repeat_interval_type_id:
                mRepeatIntervalType = getRepeatIntervalType(position);
                Toast.makeText(this, "Repeat Interval :" + mRepeatIntervalType, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

    }

    public void changeCategory(int spinnerPosition) {

        switch (spinnerPosition) {
            case 0:
                mCategory = Constants.CATEGORY_TODO;
                iv_category_icon.setImageResource(R.drawable.ic_todo);
                et_remider_info.setHint(R.string.notes);
                et_remider_info.setInputType(InputType.TYPE_CLASS_TEXT);
                et_title.setHint(R.string.title);
                break;
            case 1:
                mCategory = Constants.CATEGORY_PAYMENT;
                et_remider_info.setHint(R.string.amount);
                et_remider_info.setInputType(InputType.TYPE_CLASS_NUMBER);
                iv_category_icon.setImageResource(R.drawable.ic_payment);
                et_title.setHint(R.string.title_payment);
                break;
            case 2:
                mCategory = Constants.CATEGORY_ANNIVERSERY;
                iv_category_icon.setImageResource(R.drawable.ic_birthday);
                et_remider_info.setHint(R.string.notes);
                et_remider_info.setInputType(InputType.TYPE_CLASS_TEXT);
                et_title.setHint(R.string.title_anniversery);
                break;
            default:
                break;
        }
    }

    public int getRepeatIntervalType(int position) {

        int reminderIntervalType = Constants.REPEAT_INTERVAL_NOT_APPLICABLE;
        switch (position) {
            case 0:
                reminderIntervalType = Constants.REPEAT_INTERVAL_DAILY;
                break;
            case 1:
                reminderIntervalType = Constants.REPEAT_INTERVAL_WEEKLY;
                break;
            case 2:
                reminderIntervalType = Constants.REPEAT_INTERVAL_MONTHLY;
                break;
            case 3:
                reminderIntervalType = Constants.REPEAT_INTERVAL_YEARLY;
                break;
            case 4:
                reminderIntervalType = Constants.REPEAT_INTERVAL_HOURLY;
                break;
            case 5:
                reminderIntervalType = Constants.REPEAT_INTERVAL_MINUTES;
                break;
        }
        return reminderIntervalType;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /*public void setNotifyBeforeAlarm(int position) {

        if (mIsNotifyBeforeNeed) {

            mNotifyBeforeDateAndTime = (Calendar)mReminderDateAndTime.clone();
            if (!TextUtils.isEmpty(et_notify_time.getText().toString())) {
                int noToSubtract = Integer.parseInt(et_notify_time.getText().toString());

                switch (position) {
                    case 0 :
                        mNotifyBeforeDateAndTime.add(Calendar.HOUR_OF_DAY, -noToSubtract);
                        break;
                    case 1:
                        mNotifyBeforeDateAndTime.add(Calendar.MINUTE, -noToSubtract);
                        break;
                    case 2:
                        mNotifyBeforeDateAndTime.add(Calendar.DAY_OF_MONTH, -noToSubtract);
                        break;
                }
            }
            setAlarm(mNotifyBeforeDateAndTime);
        }

    }*/
}
