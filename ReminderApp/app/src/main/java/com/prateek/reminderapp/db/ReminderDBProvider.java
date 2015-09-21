package com.prateek.reminderapp.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.prateek.reminderapp.Constants;

/**
 * Created by prateek02.arora on 18-09-2015.
 */
public class ReminderDBProvider extends ContentProvider {

    private static final String LOG_TAG = ReminderDBProvider.class.getSimpleName();

    private static ReminderDBHelper mReminderDBHelperInstance = null;
    private SQLiteDatabase mReminderDatabase = null;

    @Override
    public boolean onCreate() {
        Log.d(LOG_TAG, "[onCreate]");

        mReminderDBHelperInstance = new ReminderDBHelper(getContext(), null, Constants.DB_VERSION);

        mReminderDatabase = mReminderDBHelperInstance.getWritableDatabase();

        return true;
    }

    static UriMatcher uriMatcher;
    static final int REMINDER_TABLE_INDEX = 1;
//    static final int REMINDER_TABLE_ROW_INDEX = 2;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(Constants.DB_AUTHORITY, Constants.DB_REMINDER_TABLE_NAME, REMINDER_TABLE_INDEX);
//        uriMatcher.addURI(Constants.DB_AUTHORITY, Constants.DB_REMINDER_TABLE_NAME, REMINDER_TABLE_INDEX);
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(LOG_TAG, "[query..]");

        Cursor c =  mReminderDatabase.query(Constants.DB_REMINDER_TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        Log.d(LOG_TAG,"coursor returned from "+Constants.DB_REMINDER_TABLE_NAME);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(LOG_TAG,"[inserting..]");

        long rowId = mReminderDatabase.insert(Constants.DB_REMINDER_TABLE_NAME, null, values);

        Toast.makeText(getContext(), "Inserted", Toast.LENGTH_SHORT).show();

        if (rowId > 0) {
            Log.d(LOG_TAG, "inserted in " + Constants.DB_REMINDER_TABLE_NAME);
            Uri objUri = ContentUris.withAppendedId(uri, rowId);
            getContext().getContentResolver().notifyChange(objUri, null);
            return objUri;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        Log.d(LOG_TAG, "[Deleting..] data from " + Constants.DB_REMINDER_TABLE_NAME);
        int count = mReminderDatabase.delete(Constants.DB_REMINDER_TABLE_NAME, selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);
        Log.d(LOG_TAG, "item deleted from db");
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int count = mReminderDatabase.update(Constants.DB_REMINDER_TABLE_NAME, values, selection, selectionArgs);
        Toast.makeText(getContext(), "Inserted", Toast.LENGTH_SHORT).show();

        getContext().getContentResolver().notifyChange(uri, null);
        Log.d(LOG_TAG, "updated : " + Constants.DB_REMINDER_TABLE_NAME);
        return count;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

}
