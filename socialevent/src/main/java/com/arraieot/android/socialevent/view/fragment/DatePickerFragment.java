package com.arraieot.android.socialevent.view.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.arraieot.android.socialevent.R;
import com.arraieot.android.socialevent.helper.SocialEventHelper;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    private static final String LOG_TAG = "SOCIAL_TAG";
    private int itemID;
    private EditText startText;
    private EditText endText;

    private SocialEventHelper socialHelper;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        itemID = getArguments().getInt("itemID");

        startText = (EditText) getActivity().findViewById(R.id.start_date_text);
        endText = (EditText) getActivity().findViewById(R.id.end_date_text);

        socialHelper = new SocialEventHelper();

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        switch (itemID){
            case R.id.start_date_text:
                addStartDateText(year, month, day);
                break;
            case R.id.end_date_text:
                addEndDateText(year, month, day);
                break;
        }
    }

    private void addStartDateText(int year, int month, int day) {
        String date = socialHelper.convertDateToSting(year, month, day);
        if(isEventDateCorrect("start", date)) {
            startText.setText(date);
        }else{
            Toast.makeText(getActivity(), "Please Change Your Picked Date!!!", Toast.LENGTH_LONG).show();
        }
    }

    private void addEndDateText(int year, int month, int day) {
        String date = socialHelper.convertDateToSting(year, month, day);
        if(isEventDateCorrect("end", date)) {
            endText.setText(date);
        }else{
            Toast.makeText(getActivity(), "Please Change Your Picked Date!!!", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isEventDateCorrect(String field, String date) {
        long start, end;
        switch (field){
            case "start":
                if(!socialHelper.empty(endText.getText().toString())){
                        start = socialHelper.convertDateToLong(date);
                        end = socialHelper.convertDateToLong(endText.getText().toString());
                        if (start > end)
                            return false;
                }else{
                    endText.setText(date);
                }
                break;
            case "end":
                if(!socialHelper.empty(startText.getText().toString())){
                    start = socialHelper.convertDateToLong(startText.getText().toString());
                    end = socialHelper.convertDateToLong(date);
                    if (start > end)
                        return false;
                }else{
                    startText.setText(date);
                }
                break;
        }

        return true;
    }


}