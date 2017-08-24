package com.arraieot.android.socialevent.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class EventsDBOpenHelper extends SQLiteOpenHelper {
    private static final String LOGTAG = "SOCIAL_TAG";

    private static final String DATABASE_NAME = "events.db";
    private static final int DATABASE_VERSION = 3;

    public static final String TABLE_EVENTS = "events";
    public static final String COLUMN_ID = "eventId";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_START_DATETIME = "start_datetime";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_START_TIME = "start_time";
    public static final String COLUMN_END_DATETIME = "end_datetime";
    public static final String COLUMN_END_DATE = "end_date";
    public static final String COLUMN_END_TIME = "end_time";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_ATTENDEE = "attendee";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_UPDATED_AT = "updated_at";


    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_EVENTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_LOCATION + " TEXT, " +
                    COLUMN_ADDRESS + " TEXT, " +
                    COLUMN_LATITUDE + " DOUBLE, " +
                    COLUMN_LONGITUDE + " DOUBLE, " +
                    COLUMN_START_DATETIME + " DATETIME, " +
                    COLUMN_START_DATE + " DATE, " +
                    COLUMN_START_TIME + " TEXT, " +
                    COLUMN_END_DATETIME + " DATETIME, " +
                    COLUMN_END_DATE + " DATE, " +
                    COLUMN_END_TIME + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_ATTENDEE + " TEXT, " +
                    COLUMN_NOTE + " TEXT, " +
                    COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    COLUMN_UPDATED_AT + " DATETIME " +
                    ")";


    public EventsDBOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);

        Log.i(LOGTAG, "Table has been created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);

        Log.i(LOGTAG, "Database has been upgraded from " + oldVersion + " to " + newVersion);
    }
}
