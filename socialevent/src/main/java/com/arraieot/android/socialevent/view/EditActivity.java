package com.arraieot.android.socialevent.view;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arraieot.android.socialevent.R;
import com.arraieot.android.socialevent.controller.EditActivityController;
import com.arraieot.android.socialevent.helper.SocialEventHelper;
import com.arraieot.android.socialevent.http.DurationAsyncTask;
import com.arraieot.android.socialevent.http.RequestPackage;
import com.arraieot.android.socialevent.model.Event;
import com.arraieot.android.socialevent.model.SimpleEvent;
import com.arraieot.android.socialevent.model.db.EventsDataSource;
import com.arraieot.android.socialevent.service.AlertReceiver;
import com.arraieot.android.socialevent.service.GPSTracker;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final static String LOG_TAG = "SOCIAL_TAG";
    private final static long oneHour = 3600000;

    private final static int PLACE_PICKER_REQUEST = 3001;
    private final static String GOOGLE_MAP_MATRIX_URI = "https://maps.googleapis.com/maps/api/distancematrix/json";
    private Context context;

    private EditText title;

    private EditText location;
    private TextView location_lat;
    private TextView location_long;
    private TextView location_address;
    public TextView location_duration_time;

    private EditText description;
    private EditText attendee;
    private EditText note;
    private EditText startDateText;
    private EditText startTimeText;
    private EditText endDateText;
    private EditText endTimeText;
    private Button deleteButton;
    public ProgressBar progressBar;


    private GoogleMap mMap;
    private MapView mapView;

    GPSTracker gps;
    double currentLatitude;
    double currentLongitude;
    double destinationLatitude;
    double destinationLongitude;

    private EditActivityController activity;
    private EventsDataSource datasource;

    private SocialEventHelper socialHelper;

    private SimpleEvent event;
    private boolean isNewEvent;
    private String selectedDate;

    public List<DurationAsyncTask> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);

        context = getApplicationContext();
        tasks = new ArrayList<>();

        Bundle b = getIntent().getExtras();
        event = b.getParcelable(".model.Event");
        isNewEvent = b.getBoolean("isNewEvent");
        selectedDate = b.getString("selectedDate");

        activity = new EditActivityController(EditActivity.this);
        datasource = new EventsDataSource(this);
        socialHelper = new SocialEventHelper();

        getAllFields();
        setDateTimeListener();
        setLocationPicker();
        generateCurrentLocation();


        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        if(!isNewEvent)
            refreshDisplay();
        else {
            fillDate();
        }

        if (isOnline())
            requestData(GOOGLE_MAP_MATRIX_URI);
        else
            Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        datasource.open();
        generateCurrentLocation();
        if (mMap != null) {
            refreshMap();
            requestData(GOOGLE_MAP_MATRIX_URI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        datasource.close();
        gps.stopUsingGPS();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PLACE_PICKER_REQUEST){
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                location.setText(place.getName());
                location_address.setText(place.getAddress());
                location_lat.setText(String.valueOf(place.getLatLng().latitude));
                location_long.setText(String.valueOf(place.getLatLng().longitude));

                destinationLatitude = place.getLatLng().latitude;
                destinationLongitude = place.getLatLng().longitude;
                refreshMap();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng position;
        if(isNewEvent || socialHelper.empty(location.getText().toString())) {
            position = new LatLng(currentLatitude, currentLongitude);
            mMap.addMarker(new MarkerOptions().position(position).title("Current Position"));
            mMap.setMinZoomPreference(15.0f);
            mMap.setMaxZoomPreference(20.0f);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        }else{
            position = new LatLng(destinationLatitude, destinationLongitude);
            mMap.addMarker(new MarkerOptions().position(position).title(location.getText().toString()));
            mMap.setMinZoomPreference(15.0f);
            mMap.setMaxZoomPreference(20.0f);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        }
    }

    private void setDateTimeListener(){
        activity.setDatePickerListener(startDateText);
        activity.setDatePickerListener(endDateText);

        activity.setTimePickerListener(startTimeText);
        activity.setTimePickerListener(endTimeText);
    }

    private void setLocationPicker(){
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityToGooglePlacePicker();
            }
        });

        location.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    ActivityToGooglePlacePicker();
                }
            }
        });
    }

    private void ActivityToGooglePlacePicker(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        Intent intent;

        try {
            intent = builder.build(EditActivity.this);
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void getAllFields(){
        title = (EditText) findViewById(R.id.title_text);
        description = (EditText) findViewById(R.id.description_text);
        attendee = (EditText) findViewById(R.id.attendee_text);
        note = (EditText) findViewById(R.id.note_text);

        location = (EditText) findViewById(R.id.location_text);
        location_address = (TextView) findViewById(R.id.location_address_hidden);
        location_lat = (TextView) findViewById(R.id.location_lat_hidden);
        location_long = (TextView) findViewById(R.id.location_long_hidden);

        startDateText = (EditText) findViewById(R.id.start_date_text);
        startTimeText = (EditText) findViewById(R.id.start_time_text);

        endDateText = (EditText) findViewById(R.id.end_date_text);
        endTimeText = (EditText) findViewById(R.id.end_time_text);
        deleteButton = (Button) findViewById(R.id.delete_button);

        location_duration_time = (TextView) findViewById(R.id.location_time_estimated);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void refreshDisplay() {
        title.setText(event.getTitle());
        location.setText(event.getLocation());
        location_address.setText(event.getAddress());
        location_lat.setText(String.valueOf(event.getLatitude()));
        location_long.setText(String.valueOf(event.getLongitude()));

        destinationLatitude = event.getLatitude();
        destinationLongitude = event.getLongitude();

        description.setText(event.getDescription());
        attendee.setText(event.getAttendee());
        note.setText(event.getNote());

        startDateText.setText(socialHelper.convertLongToDateWithFullYear(event.getStartDateTime()));
        startTimeText.setText(socialHelper.convertLongToTime(event.getStartDateTime()));

        endDateText.setText(socialHelper.convertLongToDateWithFullYear(event.getEndDateTime()));
        endTimeText.setText(socialHelper.convertLongToTime(event.getEndDateTime()));
    }


    public void actionBackView(View view){
        Log.i(LOG_TAG, "Clicked back button");
        setResult(socialHelper.BACK_RESULTCODE);
        finish();
    }

    public void actionSaveView(View view) throws ParseException {
        if(fieldsValidator()) {
            event = setFieldsEvent();
            if(isNewEvent) {
                if(!datasource.isOverlapEvent(event)) {
                    if (datasource.create(event)) {
                        Log.i(LOG_TAG, "created data");
                        setResult(socialHelper.DONE_RESULTCODE);
                        finish();
                    }
                }else{
                    Toast.makeText(this, "Time is Overlapping!!!", Toast.LENGTH_LONG).show();
                }
            }else{
                if(!datasource.isOverlapEvent(event)) {
                    if (datasource.update(event)) {
                        Log.i(LOG_TAG, "updated data");
                        setResult(socialHelper.DONE_RESULTCODE);
                        finish();
                    }
                }else{
                    Toast.makeText(this, "Time is Overlapping!!!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void actionDeleteEvent(View view){
        new AlertDialog.Builder(this)
                .setTitle("Delete This event")
                .setMessage("Do you really want to delete?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (datasource.delete(event)) {
                            Log.i(LOG_TAG, "deleted data");
                            setResult(socialHelper.DONE_RESULTCODE);
                            finish();
                        }
                        Toast.makeText(EditActivity.this, "The event has been deleted", Toast.LENGTH_SHORT).show();
                    }})
                .setNegativeButton(android.R.string.no, null).show();

    }

    private boolean fieldsValidator(){
        if(socialHelper.empty(title.getText().toString())){
            Toast.makeText(this, R.string.warning_fill_title, Toast.LENGTH_LONG).show();
            return false;
        }else if(socialHelper.empty(startDateText.getText().toString()) || socialHelper.empty(startTimeText.getText().toString())){
            Toast.makeText(this, R.string.warning_fill_start_date_time, Toast.LENGTH_LONG).show();
            return false;
        }else if(socialHelper.empty(endDateText.getText().toString()) || socialHelper.empty(endTimeText.getText().toString())){
            Toast.makeText(this, R.string.warning_fill_end_date_time, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private SimpleEvent setFieldsEvent(){
        SimpleEvent e = new SimpleEvent();

        if(!isNewEvent) e.setId(event.getId());

        e.setTitle(title.getText().toString());
        e.setLocation(location.getText().toString());
        e.setAddress(location_address.getText().toString());
        e.setLatitude(Double.parseDouble(location_lat.getText().toString()));
        e.setLongitude(Double.parseDouble(location_long.getText().toString()));

        e.setDescription(description.getText().toString());
        e.setAttendee(attendee.getText().toString());
        e.setNote(note.getText().toString());

        e.setStartDate(socialHelper.convertDateToLong(startDateText.getText().toString()));
        e.setStartTime(startTimeText.getText().toString());
        e.setStartDateTime(socialHelper.convertDateTimeToLong(getStartDateTimeFromField()));

        e.setEndDate(socialHelper.convertDateToLong(endDateText.getText().toString()));
        e.setEndTime(endTimeText.getText().toString());
        e.setEndDateTime(socialHelper.convertDateTimeToLong(getEndDateTimeFromField()));
        return e;
    }

    private String getStartDateTimeFromField(){
        return String.format("%s %s", startDateText.getText().toString(), startTimeText.getText().toString());
    }

    private String getEndDateTimeFromField(){
        return String.format("%s %s", endDateText.getText().toString(), endTimeText.getText().toString());
    }

    private void fillDate(){
        startDateText.setText(selectedDate);
        endDateText.setText(selectedDate);
        deleteButton.setVisibility(View.INVISIBLE);
    }

    private void generateCurrentLocation() {
        gps = new GPSTracker(EditActivity.this);

        // check if GPS enabled
        if(gps.canGetLocation()){
            currentLatitude = gps.getLatitude();
            currentLongitude = gps.getLongitude();
        }else{
            gps.showSettingsAlert();
        }
    }

    protected void refreshMap(){
        mMap.clear();

        LatLng position;
        String title;
        // Add a marker in Sydney and move the camera
        if(socialHelper.empty(location.getText().toString())){
            title = "Current Position";
            position = new LatLng(currentLatitude, currentLongitude);
        }else {
            title = location.getText().toString();
            position = new LatLng(destinationLatitude, destinationLongitude);
        }

        mMap.addMarker(new MarkerOptions().position(position).title(title));
        mMap.setMinZoomPreference(15.0f);
        mMap.setMaxZoomPreference(20.0f);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
    }

    protected boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        }else
            return false;
    }

    private void requestData(String uri) {
        if(!socialHelper.empty(location.getText().toString())) {
            RequestPackage p = new RequestPackage();
            p.setMethod("GET");
            p.setUri(uri);
            p.setParam("units", "imperial");
            p.setParam("origins", currentLatitude + "," + currentLongitude);
            p.setParam("destinations", destinationLatitude + "," + destinationLongitude);

            DurationAsyncTask task = new DurationAsyncTask(this);
            task.execute(p);
        }
    }
}
