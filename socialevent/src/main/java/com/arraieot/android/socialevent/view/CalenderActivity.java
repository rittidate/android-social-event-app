package com.arraieot.android.socialevent.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.arraieot.android.socialevent.R;
import com.arraieot.android.socialevent.controller.CalendarActivityController;
import com.arraieot.android.socialevent.helper.SocialEventHelper;
import com.arraieot.android.socialevent.model.Event;
import com.arraieot.android.socialevent.model.db.EventsDataSource;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalenderActivity extends AppCompatActivity {

    private static final String LOG_TAG = "SOCIAL_TAG";

    private CaldroidFragment caldroidFragment;

    private Context context;
    private CalendarActivityController controller;
    private EventsDataSource datasource;
    private SocialEventHelper socialHelper;

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    private List<Event> events;
    boolean isNewEvent;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity);

        init();

        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        } else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

            caldroidFragment.setArguments(args);
        }

        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();
        refreshDisplay();
    }

    @Override
    protected void onResume() {
        super.onResume();
        datasource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        datasource.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        isNewEvent = true;
        Intent intent = controller.addEvent(context, item, isNewEvent);
        startActivityForResult(intent, socialHelper.EDIT_ACTIVITY);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == socialHelper.EDIT_ACTIVITY){
            datasource.open();
            if(resultCode == socialHelper.DONE_RESULTCODE) {
                events = datasource.findAll();
                isNewEvent = false;
                refreshDisplay();
            }
        }
    }

    private void setCustomResourceForDates() {
        Calendar cal;
        Date blueDate;
        ColorDrawable green = new ColorDrawable(Color.GREEN);
        ColorDrawable blue = new ColorDrawable(getResources().getColor(R.color.blue));
        if (caldroidFragment != null) {
            cal = Calendar.getInstance();
            Date today = cal.getTime();
            caldroidFragment.setBackgroundDrawableForDate(green, today);
            for(Event event : events){
                cal = Calendar.getInstance();
                cal.setTimeInMillis(event.getStartDateTime());
                blueDate = cal.getTime();
                caldroidFragment.setBackgroundDrawableForDate(blue, blueDate);
                caldroidFragment.setTextColorForDate(R.color.white, blueDate);
            }
        }
    }

    public void actionListView(View v){
        setResult(socialHelper.BACK_RESULTCODE);
        finish();
    }

    public void actionWeekView(View v){
    }

    public void init(){
        datasource = new EventsDataSource(this);
        datasource.open();
        events = datasource.findAll();

        controller = new CalendarActivityController();
        context = getApplicationContext();
        socialHelper = new SocialEventHelper();

        caldroidFragment = new CaldroidFragment();

        isNewEvent = false;
    }

    public void refreshDisplay(){
        setCustomResourceForDates();
        caldroidFragment.refreshView();

        caldroidFragment.setCaldroidListener(new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                datasource.open();
                Event event = datasource.findFromDate(formatter.format(date));
                isNewEvent = false;

                if(event != null) {
                    if(datasource.isOneEventPerDate(formatter.format(date))) {
                        Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                        intent.putExtra(".model.Event", (Parcelable) event);
                        intent.putExtra("isNewEvent", isNewEvent);

                        startActivityForResult(intent, socialHelper.EDIT_ACTIVITY);
                    }else{
                        Intent output = new Intent();
                        output.putExtra("selectedDate", formatter.format(date));
                        setResult(socialHelper.DONE_SELECETED_DATE_RESULTCODE, output);
                        finish();
                    }
                }else{
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    isNewEvent = true;
                    Intent intent = controller.addEventWithDate(context, isNewEvent,dateFormat.format(date));
                    startActivityForResult(intent, socialHelper.EDIT_ACTIVITY);
                }
            }
        });

    }

}
