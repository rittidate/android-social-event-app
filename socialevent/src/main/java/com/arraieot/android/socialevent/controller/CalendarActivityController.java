package com.arraieot.android.socialevent.controller;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import com.arraieot.android.socialevent.R;
import com.arraieot.android.socialevent.view.EditActivity;

public class CalendarActivityController {

    public Intent addEvent(Context context, MenuItem item, boolean isNewEvent){
        switch (item.getItemId()){
            case R.id.action_add:
                Intent intent = new Intent(context, EditActivity.class);
                intent.putExtra("isNewEvent",isNewEvent);
                return intent;
        }
        return null;
    }

    public Intent addEventWithDate(Context context, boolean isNewEvent, String date){
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra("isNewEvent",isNewEvent);
        intent.putExtra("selectedDate", date);
        return intent;
    }
}
