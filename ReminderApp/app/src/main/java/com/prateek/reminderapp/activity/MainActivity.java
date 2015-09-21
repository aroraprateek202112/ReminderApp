package com.prateek.reminderapp.activity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import com.prateek.reminderapp.Constants;
import com.prateek.reminderapp.R;
import com.prateek.reminderapp.Reminder;
import com.prateek.reminderapp.ReminderCursorAdapter;
import com.prateek.reminderapp.Utility;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String firstLaunch = "firstLaunch";
    SharedPreferences sharedpreferences;

    private final int LODER_ID = 1;
    private ReminderCursorAdapter mReminderCursorAdapter;
    private ListView lv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fb_add_new_entry);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNewReminderActivity.class);
                startActivity(intent);
            }
        });


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (!sharedpreferences.contains(firstLaunch)) {

            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putBoolean(firstLaunch, true);
            editor.commit();
            Utility.setAlarmForDailyReminderSync(this);
        }

        lv_content = (ListView)findViewById(R.id.lv_contentId);
        mReminderCursorAdapter = new ReminderCursorAdapter(this, null, false);
        lv_content.setAdapter(mReminderCursorAdapter);

        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.d(LOG_TAG, "onItemClick");

                Cursor cursor = (Cursor)mReminderCursorAdapter.getItem(position);

                String uid = cursor.getString(cursor.getColumnIndex(Constants.UID));

                Intent intent = new Intent(MainActivity.this, ReminderViewActivity.class);
                intent.putExtra(Constants.EXTRA_REMINDER_UID, uid);
                startActivity(intent);
                /*Reminder reminder = new Reminder((Cursor)mReminderCursorAdapter.getItem(position));
                Log.d(LOG_TAG, "onItemClick reminder uid :"+reminder.getUID());
                Reminder reminder1 = (Reminder)view.getTag();
                Log.d(LOG_TAG, "onItemClick reminder1 uid :"+reminder1.getUID());*/
            }
        });

        getLoaderManager().initLoader(LODER_ID, null,this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utility.resetMissedReminderObject(this);
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        if(id == LODER_ID) {
            Log.d(LOG_TAG, "onCreateLoader loader");

            return new CursorLoader(this, Constants.DB_REMINDER_TABLE_NAME_URI,
                    Constants.DB_REMINDER_TABLE_PROJECTION, null, null,
                    null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(loader.getId() == LODER_ID) {
            if (data != null) {
                Log.d(LOG_TAG, "onLoadFinished  loader: COUNT: " + data.getCount());
            }
            mReminderCursorAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(loader.getId() == LODER_ID) {
            Log.d(LOG_TAG, "reseting  loader");
            mReminderCursorAdapter.swapCursor(null);
        }
    }
}
