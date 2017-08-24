package com.arraieot.android.socialevent.view.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.arraieot.android.socialevent.R;
import com.arraieot.android.socialevent.helper.SocialEventHelper;
import com.arraieot.android.socialevent.model.Event;

import java.util.List;


public class EventListAdapter extends ArrayAdapter<Event> {

    private Context context;
    private List<Event> events;
    SocialEventHelper socialHelper;

    public EventListAdapter(Context context, int resource, List<Event> events) {
        super(context, resource, events);
        this.events = events;
        this.context = context;
        socialHelper = new SocialEventHelper();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.event_list_item, parent,false);
        }

        Event event = events.get(position);
        TextView titleText = (TextView) convertView.findViewById(R.id.title_listView);
        titleText.setText(event.getTitle());

        TextView startDateTime = (TextView) convertView.findViewById(R.id.start_data_listView);
        startDateTime.setText(socialHelper.convertLongToDateTime(event.getStartDateTime()));

        TextView endDateTime = (TextView) convertView.findViewById(R.id.end_data_listView);
        endDateTime.setText(socialHelper.convertLongToDateTime(event.getEndDateTime()));

        return convertView;
    }
}
