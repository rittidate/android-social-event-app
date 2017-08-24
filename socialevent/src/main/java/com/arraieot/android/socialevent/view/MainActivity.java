package com.arraieot.android.socialevent.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.arraieot.android.socialevent.R;
import com.arraieot.android.socialevent.controller.MainActivityController;
import com.arraieot.android.socialevent.helper.SocialEventHelper;
import com.arraieot.android.socialevent.model.Event;
import com.arraieot.android.socialevent.model.db.EventsDataSource;
import com.arraieot.android.socialevent.service.NotificationService;
import com.arraieot.android.socialevent.view.model.EventListAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String LOG_TAG = "SOCIAL_TAG";

    private MainActivityController controller;
    private Context context;
    private EventsDataSource datasource;
    private List<Event> events;

    private SocialEventHelper socialHelper;

    boolean isNewEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, NotificationService.class));
        init();
        refreshDisplay();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == socialHelper.EDIT_ACTIVITY){
            if(resultCode == socialHelper.DONE_RESULTCODE) {
                queryAllEvent();
                refreshDisplay();
            }
        }else if(requestCode == socialHelper.CALENDAR_ACTIVITY){
            if(resultCode == socialHelper.BACK_RESULTCODE) {
                queryAllEvent();
                refreshDisplay();
            }else if(resultCode == socialHelper.DONE_SELECETED_DATE_RESULTCODE){
                datasource.open();
                events = datasource.findSeletedDate(data.getStringExtra("selectedDate"));
                isNewEvent = false;
                refreshDisplay();
            }
        }
    }

    public void actionListView(View v){
        queryAllEvent();
        refreshDisplay();
    }

    public void actionWeekView(View v){
        Intent intent = controller.weekView(context);
        startActivityForResult(intent, socialHelper.CALENDAR_ACTIVITY);
    }

    private void init(){
        datasource = new EventsDataSource(this);
        datasource.open();
        events = datasource.findAll();

        controller = new MainActivityController();
        context = getApplicationContext();

        isNewEvent = false;
    }

    private void queryAllEvent(){
        datasource.open();
        events = datasource.findAll();
        isNewEvent = false;
    }

    private void refreshDisplay() {
        ArrayAdapter<Event> adapter;

        adapter = new EventListAdapter(this, R.layout.event_list_item, events);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Event event = events.get(position);

                isNewEvent = false;

                Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                intent.putExtra(".model.Event", (Parcelable) event);
                intent.putExtra("isNewEvent", isNewEvent);

                startActivityForResult(intent, socialHelper.EDIT_ACTIVITY);
            }
        });
    }
}
