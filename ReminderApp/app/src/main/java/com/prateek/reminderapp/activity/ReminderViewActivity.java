package com.prateek.reminderapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prateek.reminderapp.Constants;
import com.prateek.reminderapp.R;
import com.prateek.reminderapp.Reminder;
import com.prateek.reminderapp.Utility;

/**
 * Created by Prateek on 21-09-2015.
 */
public class ReminderViewActivity extends AppCompatActivity {

    private Reminder reminder;
    private TextView tv_title;
    private TextView tv_category;
    private ImageView iv_category_icon;
    private TextView tv_reminderMessage;
    private Button bt_dueDate;
    private Button bt_dueTime;
    private TextView tv_reminder_type;
    private LinearLayout ll_reminder_repeat_info;
    private TextView tv_repeat_count;
    private TextView tv_repeat_type;
    private TextView tv_reminder_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reminder_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);

        tv_category = (TextView)findViewById(R.id.tv_category_id);
        iv_category_icon = (ImageView)findViewById(R.id.iv_category_icon_id);

        tv_title = (TextView)findViewById(R.id.tv_title_id);
        tv_reminderMessage = (TextView)findViewById(R.id.tv_remider_info_id);

        bt_dueDate = (Button)findViewById(R.id.bt_due_date_id);
        bt_dueTime = (Button)findViewById(R.id.bt_time_id);

        tv_reminder_type = (TextView)findViewById(R.id.tv_reminder_type_id);

        ll_reminder_repeat_info = (LinearLayout)findViewById(R.id.ll_repeat_id);
        tv_repeat_count = (TextView)findViewById(R.id.tv_repeat_count_id);
        tv_repeat_type = (TextView) findViewById(R.id.tv_repeat_interval_type_id);

//        tv_reminder_state = (TextView)findViewById(R.id.tv_reminder_state_id);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fb_edit_reminder);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReminderViewActivity.this, AddNewReminderActivity.class);
                intent.putExtra(Constants.EXTRA_REMINDER_OPEN_MODE_TYPE, Constants.MODE_EDIT_REMINDER);
                intent.putExtra(Constants.EXTRA_REMINDER_UID, reminder.getUID());
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            initContent(intent);
        }
    }

    public void initContent(Intent intent) {

        String uid = intent.getStringExtra(Constants.EXTRA_REMINDER_UID);

        reminder = Utility.getReminderObjectFromUID(this, uid);

        updateReminderView();
    }

    public void updateReminderView() {

        switch (reminder.getCategory()) {
            case Constants.CATEGORY_TODO:
                iv_category_icon.setImageResource(R.drawable.ic_todo);
                tv_category.setText(R.string.todo);
                break;
            case Constants.CATEGORY_PAYMENT:
                iv_category_icon.setImageResource(R.drawable.ic_payment);
                tv_category.setText(R.string.payments);
                break;
            case Constants.CATEGORY_ANNIVERSERY:
                iv_category_icon.setImageResource(R.drawable.ic_birthday);
                tv_category.setText(R.string.anniversery);
                break;
            default:
                break;
        }

        /*if (reminder.getState() == Constants.REMINDER_COMPLETE_STATE) {
            tv_reminder_state.setText(R.string.inactive);
//            tv_reminder_type.setTextColor(getResources().getColor(R.color.dull_white));
            tv_reminder_type.setTextColor(Color.parseColor("#FAFAFA"));

        } else {
            tv_reminder_state.setText(R.string.active);
//            tv_reminder_type.setTextColor(getResources().getColor(R.color.blue));
            tv_reminder_type.setTextColor(Color.parseColor("#29B6F6"));
        }*/

        tv_title.setText(reminder.getTitle());
        tv_reminderMessage.setText(reminder.getMessage());
        bt_dueDate.setText(reminder.getDueDateString());
        bt_dueTime.setText(reminder.getDueTimeString());

        if (reminder.getReminderType() == Constants.SINGLE_SHOT) {
            ll_reminder_repeat_info.setVisibility(View.GONE);
            tv_reminder_type.setText(R.string.one_time_event);
        } else {
            ll_reminder_repeat_info.setVisibility(View.VISIBLE);
            tv_reminder_type.setText(R.string.repeat);
            tv_repeat_count.setText(""+reminder.getRepeatIntervalNumber());

            int reminderRepeatIntervlType = reminder.getRepeatIntervalType();

            String[] testArray = getResources().getStringArray(R.array.repeat_interval);
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
            tv_repeat_type.setText(testArray[position]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_delete) {
            reminder.deleteItemInDb(getApplicationContext());
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
