package com.arraieot.android.socialevent.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.arraieot.android.socialevent.R;
import com.arraieot.android.socialevent.helper.SocialEventHelper;
import com.arraieot.android.socialevent.model.SimpleEvent;
import com.arraieot.android.socialevent.view.MainActivity;


public class AlertReceiver extends BroadcastReceiver {
    private SimpleEvent event;
    private SocialEventHelper socialHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        socialHelper = new SocialEventHelper();
        Bundle b = intent.getExtras();
        event = b.getParcelable(".model.Event");
        createNotification(context, "You have 1 event!!!", event.getTitle() + " Start at: " + socialHelper.convertLongToDateTime(event.getStartDateTime()), "Alert");
    }

    public void createNotification(Context context, String msg, String msgText, String msgAlert){

        PendingIntent notificIntent = PendingIntent.getActivity(context, 0,
                        new Intent(context, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setContentTitle(msg)
                .setContentText(msgText)
                .setTicker(msgAlert)
                .setSmallIcon(R.drawable.stat_happy);

        mBuilder.setContentIntent(notificIntent);

        mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);

        mBuilder.setAutoCancel(true);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1, mBuilder.build());
    }
}
