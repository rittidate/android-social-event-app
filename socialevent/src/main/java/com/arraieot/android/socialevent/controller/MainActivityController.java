package com.arraieot.android.socialevent.controller;


import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import com.arraieot.android.socialevent.R;
import com.arraieot.android.socialevent.view.CalenderActivity;
import com.arraieot.android.socialevent.view.EditActivity;
import com.arraieot.android.socialevent.view.MainActivity;

public class MainActivityController {
    private final static String LOG_TAG = "SOCIAL_TAG";
    public MainActivityController() {
    }

    public void listView(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public Intent weekView(Context context){
        Intent intent = new Intent(context, CalenderActivity.class);
        return intent;
    }

    public Intent addEvent(Context context, MenuItem item, boolean isNewEvent){
        Intent intent;
        switch (item.getItemId()){
            case R.id.action_add:
                intent = new Intent(context, EditActivity.class);
                intent.putExtra("isNewEvent",isNewEvent);
                return intent;
        }
        return null;
    }
}
