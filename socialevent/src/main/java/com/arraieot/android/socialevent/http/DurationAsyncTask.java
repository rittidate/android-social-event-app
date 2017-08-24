package com.arraieot.android.socialevent.http;


import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.View;

import com.arraieot.android.socialevent.view.EditActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class DurationAsyncTask extends AsyncTask<RequestPackage, String, String> {

    EditActivity activity = null;

    public DurationAsyncTask(EditActivity activity)
    {
        attach(activity);
    }

    @Override
    protected void onPreExecute() {
        if (activity.tasks.size() == 0) {
            activity.progressBar.setVisibility(View.VISIBLE);
        }
        activity.tasks.add(this);
    }

    @Override
    protected String doInBackground(RequestPackage... requestPackages) {
        String content = HttpManager.getData(requestPackages[0]);
        SystemClock.sleep(500);
        return content;
    }

    @Override
    protected void onPostExecute(String result) {

        activity.tasks.remove(this);
        if (activity.tasks.size() == 0) {
            activity.progressBar.setVisibility(View.INVISIBLE);
        }
        updateDuration(result);
    }

    private void updateDuration(String result){
        try {
            JSONObject obj;
            String time = "";

            obj = new JSONObject(result);
            JSONObject duration = obj.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("duration");

            Double min = Double.parseDouble(duration.getString("value")) / 60;

            if((min / 60) > 0)
                time += (int)(min / 60) + " hr ";

            time += (int)(min % 60) + " mins";

            activity.location_duration_time.setText(time + "\n");
        }catch (JSONException e){
            e.getMessage();
        }
    }

    public void attach(EditActivity activity)
    {
        this.activity = activity;
    }
}
