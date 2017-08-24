package com.arraieot.android.socialevent.model.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.arraieot.android.socialevent.helper.SocialEventHelper;
import com.arraieot.android.socialevent.model.Event;
import com.arraieot.android.socialevent.model.SimpleEvent;

import java.util.ArrayList;
import java.util.List;

public class EventsDataSource {

    public static final String LOGTAG="SOCIAL_TAG";

    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;

    SocialEventHelper socialHelper;

    public EventsDataSource(Context context){
        dbhelper = new EventsDBOpenHelper(context);
        socialHelper = new SocialEventHelper();
    }

    private static final String[] allColumns = {
            EventsDBOpenHelper.COLUMN_ID,
            EventsDBOpenHelper.COLUMN_TITLE,
            EventsDBOpenHelper.COLUMN_LOCATION,
            EventsDBOpenHelper.COLUMN_ADDRESS,
            EventsDBOpenHelper.COLUMN_LATITUDE,
            EventsDBOpenHelper.COLUMN_LONGITUDE,
            EventsDBOpenHelper.COLUMN_START_DATE,
            EventsDBOpenHelper.COLUMN_START_TIME,
            EventsDBOpenHelper.COLUMN_START_DATETIME,
            EventsDBOpenHelper.COLUMN_END_DATE,
            EventsDBOpenHelper.COLUMN_END_TIME,
            EventsDBOpenHelper.COLUMN_END_DATETIME,
            EventsDBOpenHelper.COLUMN_DESCRIPTION,
            EventsDBOpenHelper.COLUMN_ATTENDEE,
            EventsDBOpenHelper.COLUMN_NOTE
    };

    public void open(){
        Log.i(LOGTAG, "Database opened");
        database = dbhelper.getWritableDatabase();
    }

    public void close(){
        Log.i(LOGTAG, "Database closed");
        dbhelper.close();
    }

    public List<Event> findAll(){
        Cursor cursor = database.query(EventsDBOpenHelper.TABLE_EVENTS, allColumns,
                null, null, null, null, EventsDBOpenHelper.COLUMN_START_DATETIME);
        Log.i(LOGTAG, "Returned " + cursor.getCount() + " rows");
        List<Event> events = cursorToList(cursor);
        return events;
    }

    public List<Event> findSeletedDate(String date){
        String query = "SELECT * FROM " + EventsDBOpenHelper.TABLE_EVENTS +
                " WHERE  " + EventsDBOpenHelper.COLUMN_START_DATE + " = '" + date + "'";
        Cursor cursor = database.rawQuery(query, null);
        List<Event> events = cursorToList(cursor);
        return events;
    }

    public List<Event> findInHour(String startDate, String endDate){
        String query = "SELECT * FROM " + EventsDBOpenHelper.TABLE_EVENTS +
                " WHERE  " + EventsDBOpenHelper.COLUMN_START_DATETIME + " >= '" + startDate + "' and "
                + EventsDBOpenHelper.COLUMN_START_DATETIME + " < '" + endDate + "'";
        Cursor cursor = database.rawQuery(query, null);
        List<Event> events = cursorToList(cursor);
        return events;
    }

    public SimpleEvent findFromDate(String date){
        String query = "SELECT * FROM " + EventsDBOpenHelper.TABLE_EVENTS +
                " WHERE  " + EventsDBOpenHelper.COLUMN_START_DATE + " = '" + date + "'";
        Cursor cursor = database.rawQuery(query, null);
        if(cursor.getCount() > 0) {
            SimpleEvent event = new SimpleEvent();
            while (cursor.moveToNext()) {
                event.setId(cursor.getLong(cursor.getColumnIndex(EventsDBOpenHelper.COLUMN_ID)));
                event.setTitle(cursor.getString(cursor.getColumnIndex(EventsDBOpenHelper.COLUMN_TITLE)));

                event.setLocation(cursor.getString(cursor.getColumnIndex(EventsDBOpenHelper.COLUMN_LOCATION)));
                event.setAddress(cursor.getString(cursor.getColumnIndex(EventsDBOpenHelper.COLUMN_ADDRESS)));
                event.setLatitude(cursor.getDouble(cursor.getColumnIndex(EventsDBOpenHelper.COLUMN_LATITUDE)));
                event.setLongitude(cursor.getDouble(cursor.getColumnIndex(EventsDBOpenHelper.COLUMN_LONGITUDE)));

                event.setDescription(cursor.getString(cursor.getColumnIndex(EventsDBOpenHelper.COLUMN_DESCRIPTION)));
                event.setNote(cursor.getString(cursor.getColumnIndex(EventsDBOpenHelper.COLUMN_NOTE)));
                event.setAttendee(cursor.getString(cursor.getColumnIndex(EventsDBOpenHelper.COLUMN_ATTENDEE)));

                event.setStartTime(cursor.getString(cursor.getColumnIndex(EventsDBOpenHelper.COLUMN_START_TIME)));
                event.setStartDate(socialHelper.convertSQLiteDateToLong(cursor.getString(cursor.getColumnIndex(EventsDBOpenHelper.COLUMN_START_DATE))));
                event.setStartDateTime(socialHelper.convertSQLiteDateTimeToLong(cursor.getString(cursor.getColumnIndex(EventsDBOpenHelper.COLUMN_START_DATETIME))));

                event.setEndTime(cursor.getString(cursor.getColumnIndex(EventsDBOpenHelper.COLUMN_END_TIME)));
                event.setEndDate(socialHelper.convertSQLiteDateToLong(cursor.getString(cursor.getColumnIndex(EventsDBOpenHelper.COLUMN_END_DATE))));
                event.setEndDateTime(socialHelper.convertSQLiteDateTimeToLong(cursor.getString(cursor.getColumnIndex(EventsDBOpenHelper.COLUMN_END_DATETIME))));
            }
            return event;
        }
        return null;
    }

    public boolean isOneEventPerDate(String date){
        String query = "SELECT * FROM " + EventsDBOpenHelper.TABLE_EVENTS +
                " WHERE  " + EventsDBOpenHelper.COLUMN_START_DATE + " = '" + date + "'";
        Cursor cursor = database.rawQuery(query, null);
        if(cursor.getCount() == 1)
            return true;
        else
            return false;
    }


    private List<Event> cursorToList(Cursor cursor) {
        List<Event> events = new ArrayList<Event>();
        if(cursor.getCount() > 0){
            while (cursor.moveToNext()){
                SimpleEvent event = new SimpleEvent();
                event.setId(cursor.getLong(cursor.getColumnIndex(EventsDBOpenHelper.COLUMN_ID)));
                event.setTitle(cursor.getString(cursor.getColumnIndex(EventsDBOpenHelper.COLUMN_TITLE)));

                event.setLocation(cursor.getString(cursor.getColumnIndex(EventsDBOpenHelper.COLUMN_LOCATION)));
                event.setAddress(cursor.getString(cursor.getColumnIndex(EventsDBOpenHelper.COLUMN_ADDRESS)));
                event.setLatitude(cursor.getDouble(cursor.getColumnIndex(EventsDBOpenHelper.COLUMN_LATITUDE)));
                event.setLongitude(cursor.getDouble(cursor.getColumnIndex(EventsDBOpenHelper.COLUMN_LONGITUDE)));

                event.setDescription(cursor.getString(cursor.getColumnIndex(EventsDBOpenHelper.COLUMN_DESCRIPTION)));
                event.setNote(cursor.getString(cursor.getColumnIndex(EventsDBOpenHelper.COLUMN_NOTE)));
                event.setAttendee(cursor.getString(cursor.getColumnIndex(EventsDBOpenHelper.COLUMN_ATTENDEE)));

                event.setStartTime(cursor.getString(cursor.getColumnIndex(EventsDBOpenHelper.COLUMN_START_TIME)));
                event.setStartDate(socialHelper.convertSQLiteDateToLong(cursor.getString(cursor.getColumnIndex(EventsDBOpenHelper.COLUMN_START_DATE))));
                event.setStartDateTime(socialHelper.convertSQLiteDateTimeToLong(cursor.getString(cursor.getColumnIndex(EventsDBOpenHelper.COLUMN_START_DATETIME))));

                event.setEndTime(cursor.getString(cursor.getColumnIndex(EventsDBOpenHelper.COLUMN_END_TIME)));
                event.setEndDate(socialHelper.convertSQLiteDateToLong(cursor.getString(cursor.getColumnIndex(EventsDBOpenHelper.COLUMN_END_DATE))));
                event.setEndDateTime(socialHelper.convertSQLiteDateTimeToLong(cursor.getString(cursor.getColumnIndex(EventsDBOpenHelper.COLUMN_END_DATETIME))));
                events.add(event);
            }
        }
        return events;
    }

    public boolean create(SimpleEvent event) {
        ContentValues values = new ContentValues();
        values.put(EventsDBOpenHelper.COLUMN_TITLE, event.getTitle());

        values.put(EventsDBOpenHelper.COLUMN_LOCATION, event.getLocation());
        values.put(EventsDBOpenHelper.COLUMN_ADDRESS, event.getAddress());
        values.put(EventsDBOpenHelper.COLUMN_LATITUDE, event.getLatitude());
        values.put(EventsDBOpenHelper.COLUMN_LONGITUDE, event.getLongitude());

        values.put(EventsDBOpenHelper.COLUMN_START_DATE, socialHelper.convertLongToSQLiteDate(event.getStartDate()));
        values.put(EventsDBOpenHelper.COLUMN_START_TIME, event.getStartTime());
        values.put(EventsDBOpenHelper.COLUMN_START_DATETIME, socialHelper.convertLongToSQLiteDateTime(event.getStartDateTime()));
        values.put(EventsDBOpenHelper.COLUMN_END_DATE, socialHelper.convertLongToSQLiteDate(event.getEndDate()));
        values.put(EventsDBOpenHelper.COLUMN_END_TIME, event.getEndTime());
        values.put(EventsDBOpenHelper.COLUMN_END_DATETIME, socialHelper.convertLongToSQLiteDateTime(event.getEndDateTime()));
        values.put(EventsDBOpenHelper.COLUMN_DESCRIPTION, event.getDescription());
        values.put(EventsDBOpenHelper.COLUMN_ATTENDEE, event.getAttendee());
        values.put(EventsDBOpenHelper.COLUMN_NOTE, event.getNote());
        long result = database.insert(EventsDBOpenHelper.TABLE_EVENTS, null, values);
        return (result != -1);
    }

    public boolean isOverlapEvent(SimpleEvent event){
        Log.i(LOGTAG, "ID : " + event.getId());
        String startDateTime = socialHelper.convertLongToSQLiteDateTime(event.getStartDateTime());
        String endDateTime = socialHelper.convertLongToSQLiteDateTime(event.getEndDateTime());
        String query = "SELECT * FROM " + EventsDBOpenHelper.TABLE_EVENTS +
                        " WHERE  " + EventsDBOpenHelper.COLUMN_START_DATETIME + " < '" + endDateTime + "' AND " + EventsDBOpenHelper.COLUMN_END_DATETIME + " > '" +  startDateTime + "'";

        if(event.getId() > 0)
            query += " AND " + EventsDBOpenHelper.COLUMN_ID + " <> " + event.getId();

        Cursor cursor = database.rawQuery(query, null);

        if(cursor.getCount() > 0){
            return true;
        }
        return false;
    }

    public boolean update(SimpleEvent event){
        ContentValues values = new ContentValues();
        values.put(EventsDBOpenHelper.COLUMN_TITLE, event.getTitle());

        values.put(EventsDBOpenHelper.COLUMN_LOCATION, event.getLocation());
        values.put(EventsDBOpenHelper.COLUMN_ADDRESS, event.getAddress());
        values.put(EventsDBOpenHelper.COLUMN_LATITUDE, event.getLatitude());
        values.put(EventsDBOpenHelper.COLUMN_LONGITUDE, event.getLongitude());

        values.put(EventsDBOpenHelper.COLUMN_START_DATE, socialHelper.convertLongToSQLiteDate(event.getStartDate()));
        values.put(EventsDBOpenHelper.COLUMN_START_TIME, event.getStartTime());
        values.put(EventsDBOpenHelper.COLUMN_START_DATETIME, socialHelper.convertLongToSQLiteDateTime(event.getStartDateTime()));
        values.put(EventsDBOpenHelper.COLUMN_END_DATE, socialHelper.convertLongToSQLiteDate(event.getEndDate()));
        values.put(EventsDBOpenHelper.COLUMN_END_TIME, event.getEndTime());
        values.put(EventsDBOpenHelper.COLUMN_END_DATETIME, socialHelper.convertLongToSQLiteDateTime(event.getEndDateTime()));
        values.put(EventsDBOpenHelper.COLUMN_DESCRIPTION, event.getDescription());
        values.put(EventsDBOpenHelper.COLUMN_ATTENDEE, event.getAttendee());
        values.put(EventsDBOpenHelper.COLUMN_NOTE, event.getNote());
        values.put(EventsDBOpenHelper.COLUMN_NOTE, event.getNote());
        values.put(EventsDBOpenHelper.COLUMN_UPDATED_AT, socialHelper.currentDateTime());
        String where = EventsDBOpenHelper.COLUMN_ID + " = " + event.getId();

        long result = database.update(EventsDBOpenHelper.TABLE_EVENTS, values, where, null);
        return (result != -1);
    }

    public boolean delete(SimpleEvent event){
        String where = EventsDBOpenHelper.COLUMN_ID + " = " + event.getId();
        long result = database.delete(EventsDBOpenHelper.TABLE_EVENTS, where, null);
        return (result != -1);
    }
}
