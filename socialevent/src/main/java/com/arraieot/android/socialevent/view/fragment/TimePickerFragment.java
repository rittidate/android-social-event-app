package com.arraieot.android.socialevent.view.fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.arraieot.android.socialevent.R;
import com.arraieot.android.socialevent.helper.SocialEventHelper;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private int itemID;
    private EditText startDateText;
    private EditText endDateText;
    private EditText startTimeText;
    private EditText endTimeText;

    private String[] time;
    private int mhour;
    private int mminute;

    private SocialEventHelper socialHelper;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        mhour = c.get(Calendar.HOUR_OF_DAY);
        mminute = c.get(Calendar.MINUTE);

        init();

        setTimeFragment();

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, mhour, mminute,
                DateFormat.is24HourFormat(getActivity()));
    }

    private void init(){
        itemID = getArguments().getInt("itemID");

        startDateText = (EditText) getActivity().findViewById(R.id.start_date_text);
        endDateText = (EditText) getActivity().findViewById(R.id.end_date_text);

        startTimeText = (EditText) getActivity().findViewById(R.id.start_time_text);
        endTimeText = (EditText) getActivity().findViewById(R.id.end_time_text);

        socialHelper = new SocialEventHelper();
    }

    private void setTimeFragment(){
        switch(itemID){
            case R.id.start_time_text:
                if (!socialHelper.empty(startTimeText.getText().toString())) {
                    time = startTimeText.getText().toString().split ( ":" );
                    mhour = Integer.parseInt ( time[0].trim() );
                    mminute = Integer.parseInt ( time[1].trim() );
                }else if (!socialHelper.empty(endTimeText.getText().toString()) && socialHelper.empty(startTimeText.getText().toString())) {
                    time = endTimeText.getText().toString().split ( ":" );
                    mhour = Integer.parseInt ( time[0].trim() );
                    mminute = Integer.parseInt ( time[1].trim() );
                }
                break;
            case R.id.end_time_text:
                if(!socialHelper.empty(endTimeText.getText().toString()))  {
                    time = endTimeText.getText().toString().split ( ":" );
                    mhour = Integer.parseInt ( time[0].trim() );
                    mminute = Integer.parseInt ( time[1].trim() );
                }else if (!socialHelper.empty(startTimeText.getText().toString()) && socialHelper.empty(endTimeText.getText().toString())) {
                    time = startTimeText.getText().toString().split ( ":" );
                    mhour = Integer.parseInt ( time[0].trim() );
                    mminute = Integer.parseInt ( time[1].trim() );
                }
                break;
        }
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        switch (itemID){
            case R.id.start_time_text:
                addStartTimeText(hourOfDay, minute);
                break;
            case R.id.end_time_text:
                addEndTimeText(hourOfDay, minute);
                break;
        }
    }


    private void addStartTimeText(int hourOfDay, int minute) {
        String date = socialHelper.convertTimeToSting(hourOfDay, minute);
        if(isEventTimeCorrect("start", date)) {
            startTimeText.setText(date);
        }else{
            Toast.makeText(getActivity(), "Please Change Your Picked Time!!!", Toast.LENGTH_LONG).show();
        }
    }

    private void addEndTimeText(int hourOfDay, int minute) {
        String time = socialHelper.convertTimeToSting(hourOfDay, minute);
        if(isEventTimeCorrect("end", time)) {
            endTimeText.setText(time);
        }else{
            Toast.makeText(getActivity(), "Please Change Your Picked Time!!!", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isEventTimeCorrect(String field, String time) {
        long start, end;
        switch (field){
            case "start":
                if(!socialHelper.empty(endDateText.getText().toString()) && !socialHelper.empty(endTimeText.getText().toString()) && !socialHelper.empty(startDateText.getText().toString())) {
                    start = socialHelper.convertDateTimeToLong(startDateText.getText().toString() + " " + time);
                    end = socialHelper.convertDateTimeToLong(endDateText.getText().toString() + " " + endTimeText.getText().toString());
                    if (start > end)
                        return false;
                }else if((socialHelper.empty(startDateText.getText().toString()) ||  socialHelper.empty(endDateText.getText().toString())) && !socialHelper.empty(endTimeText.getText().toString()) ){
                    start = socialHelper.convertTimeToLong(time);
                    end = socialHelper.convertTimeToLong(endTimeText.getText().toString());
                    if (start > end)
                        return false;
                }else{
                    endTimeText.setText(time);
                }
                break;
            case "end":
                if(!socialHelper.empty(startDateText.getText().toString()) && !socialHelper.empty(startTimeText.getText().toString()) && !socialHelper.empty(endDateText.getText().toString())){
                    start = socialHelper.convertDateTimeToLong(startDateText.getText().toString() + " " + startTimeText.getText().toString());
                    end = socialHelper.convertDateTimeToLong(endDateText.getText().toString() + " " + time);
                    if (start > end)
                        return false;
                }else if((socialHelper.empty(startDateText.getText().toString()) ||  socialHelper.empty(endDateText.getText().toString())) && !socialHelper.empty(startTimeText.getText().toString()) ){
                    start = socialHelper.convertTimeToLong(startTimeText.getText().toString());
                    end = socialHelper.convertTimeToLong(time);
                    if (start > end)
                        return false;
                }else{
                    startTimeText.setText(time);
                }
                break;
        }

        return true;
    }
}