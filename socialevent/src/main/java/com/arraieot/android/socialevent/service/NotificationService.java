package com.arraieot.android.socialevent.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;

import com.arraieot.android.socialevent.helper.SocialEventHelper;
import com.arraieot.android.socialevent.model.Event;
import com.arraieot.android.socialevent.model.db.EventsDataSource;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {
    private final static String LOG_TAG = "SOCIAL_TAG";
    private final static long oneHour = 3600000; //default is 1 hour which equal to 3600000
    private final static long repeatTime = 60000; //Change this repeat time to 1800000 to get rid of duplicated task
    private EventsDataSource datasource;
    private SocialEventHelper socialHelper;
    private Timer t;
    private List<Event> events;

    public NotificationService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        datasource = new EventsDataSource(this);
        socialHelper = new SocialEventHelper();
        //Declare the timer
        t = new Timer();

        Calendar cal = Calendar.getInstance();

        doTask(cal.getTimeInMillis());

        repeatTask(repeatTime);
        Log.i(LOG_TAG, "Start Notification Service");
    }


    public void repeatTask(long time){
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.i(LOG_TAG, "Repeat Task");
                datasource.open();
                Calendar cal = Calendar.getInstance();

                doTask(cal.getTimeInMillis());

                repeatTask(repeatTime);
            }
        },
        time);
    }

    public void doTask(long startTime){
        datasource.open();
        Log.i(LOG_TAG, "Length:" + socialHelper.convertLongToSQLiteDateTime(startTime) + " - " + socialHelper.convertLongToSQLiteDateTime(startTime + oneHour));

        events = datasource.findInHour(socialHelper.convertLongToSQLiteDateTime(startTime), socialHelper.convertLongToSQLiteDateTime(startTime + oneHour));

        if(events.size() > 0){
            for(Event event: events){
                setAlarm(event);
            }
        }

        events = null;

        datasource.close();
    }

    public void setAlarm(Event event){
        Long alertTime = event.getStartDateTime() - (oneHour / 2);
        Log.i(LOG_TAG, "Set Alarm at: " + socialHelper.convertLongToSQLiteDateTime(alertTime));

        Intent alertIntent = new Intent(this, AlertReceiver.class);
        alertIntent.putExtra(".model.Event", (Parcelable) event);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime, PendingIntent.getBroadcast(this, 1, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT));
    }
}
