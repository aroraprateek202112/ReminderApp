package com.prateek.reminderapp;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Prateek on 19-09-2015.
 */
public class ReminderItemView extends LinearLayout {

    private final ImageView iv_reminderIcon;
    private final TextView tv_title;
    private final TextView tv_message;
    private final ImageView iv_alarm_handle;
    private final TextView tv_date_and_time;
    private final TextView tv_reminder_type;
    private final TextView tv_missed_info;

    Context mContext;

    public ReminderItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.reminder_item_layout, this, true);

        iv_reminderIcon = (ImageView)viewGroup.findViewById(R.id.iv_item_icon_id);
        tv_title = (TextView)viewGroup.findViewById(R.id.tv_item_title_id);
        tv_message = (TextView)viewGroup.findViewById(R.id.tv_item_message_id);
        iv_alarm_handle = (ImageView)viewGroup.findViewById(R.id.iv_alaram_handle_id);
        tv_date_and_time = (TextView)viewGroup.findViewById(R.id.tv_date_and_time_id);
        tv_reminder_type = (TextView)viewGroup.findViewById(R.id.tv_reminder_type_id);
        tv_missed_info = (TextView)viewGroup.findViewById(R.id.tv_missed_reminder_id);
    }

    public void initView(Reminder reminder) {

        tv_title.setText(reminder.getTitle());

        if (reminder.getCategory() == Constants.CATEGORY_PAYMENT) {
            iv_reminderIcon.setImageResource(R.drawable.ic_payment);
        } else if (reminder.getCategory() == Constants.CATEGORY_ANNIVERSERY) {
            iv_reminderIcon.setImageResource(R.drawable.ic_birthday);
        } else {
            iv_reminderIcon.setImageResource(R.drawable.ic_todo);
        }

        if (reminder.getMessage().equals("NA")) {
            tv_message.setVisibility(GONE);
        } else  {
            tv_message.setText(reminder.getMessage());
        }

        tv_date_and_time.setText(reminder.getDueDateString() + " | " +reminder.getDueTimeString());
        if (reminder.getReminderType() == Constants.SINGLE_SHOT) {
            tv_reminder_type.setText("Once");
        } else {
            tv_reminder_type.setText("Repeated");
        }
        if (reminder.getState() == Constants.REMINDER_COMPLETE_STATE) {
            iv_alarm_handle.setImageResource(R.drawable.ic_reminder_disable);
        } else {
            iv_alarm_handle.setImageResource(R.drawable.ic_reminder_enable);
        }

        iv_alarm_handle.setTag(reminder.getUID());
        iv_alarm_handle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ReminderItemView", "onClick");
                String uid = (String) view.getTag();
                Utility.changeReminderState(mContext, uid);
            }
        });
        if (reminder.getNotificationState() == Constants.REMINDER_NOTIFICATION_MISSED){
            tv_missed_info.setVisibility(VISIBLE);
        } else {
            tv_missed_info.setVisibility(GONE);
        }

    }



}
