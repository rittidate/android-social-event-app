package com.arraieot.android.socialevent.controller;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;

import com.arraieot.android.socialevent.view.EditActivity;
import com.arraieot.android.socialevent.view.fragment.DatePickerFragment;
import com.arraieot.android.socialevent.view.fragment.TimePickerFragment;

public class EditActivityController {

    private EditActivity activity;

    public EditActivityController(EditActivity activity)
    {
        attach(activity);
    }

    public void attach(EditActivity activity)
    {
        this.activity = activity;
    }

    public void setDatePickerListener(EditText editText){
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    showDatePickerDialog(view);
                }
            }
        });
    }

    public void setTimePickerListener(EditText ed){
        ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view);
            }
        });

        ed.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    showTimePickerDialog(view);
                }
            }
        });
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("itemID", v.getId());
        newFragment.setArguments(bundle);
        newFragment.show(activity.getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("itemID", v.getId());
        newFragment.setArguments(bundle);
        newFragment.show(activity.getSupportFragmentManager(), "timePicker");
    }

}
