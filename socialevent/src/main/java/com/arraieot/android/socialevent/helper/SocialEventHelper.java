package com.arraieot.android.socialevent.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SocialEventHelper {
    public static final int MAIN_ACTIVITY = 1001;
    public static final int CALENDAR_ACTIVITY = 1002;
    public static final int EDIT_ACTIVITY = 1003;

    public final static int DONE_RESULTCODE = 2001;
    public final static int BACK_RESULTCODE = 2002;
    public final static int DONE_SELECETED_DATE_RESULTCODE = 2003;

    public SocialEventHelper(){

    }

    public static boolean empty( final String s ) {
        return s == null || s.trim().isEmpty();
    }

    public static String convertDateToSting(int year, int month, int day){
        SimpleDateFormat dt= new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        return dt.format(cal.getTime());
    }

    public long convertDateToLong(String dateSting){
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date d = f.parse(dateSting);
            long milliseconds = d.getTime();
            return milliseconds;
        }catch (ParseException e){
            return 0;
        }
    }

    public static String convertTimeToSting(int hourOfDay, int minute){
        SimpleDateFormat dt= new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);

        return dt.format(cal.getTime());
    }

    public long convertDateTimeToLong(String datetimeString){
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            Date d = f.parse(datetimeString);
            long milliseconds = d.getTime();
            return milliseconds;
        }catch (ParseException e){
            return 0;
        }
    }

    public long convertTimeToLong(String timeString){
        SimpleDateFormat f = new SimpleDateFormat("HH:mm");
        try {
            Date d = f.parse(timeString);
            long milliseconds = d.getTime();
            return milliseconds;
        }catch (ParseException e){
            return 0;
        }
    }

    public long convertSQLiteDateToLong(String datetimeString){
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = f.parse(datetimeString);
            long milliseconds = d.getTime();
            return milliseconds;
        }catch (ParseException e){
            return 0;
        }
    }

    public long convertSQLiteDateTimeToLong(String datetimeString){
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d = f.parse(datetimeString);
            long milliseconds = d.getTime();
            return milliseconds;
        }catch (ParseException e){
            return 0;
        }
    }

    public String convertLongToSQLiteDate(long timestamp){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return dateFormat.format(cal.getTime());
    }

    public String convertLongToSQLiteDateTime(long timestamp){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return dateFormat.format(cal.getTime());
    }

    public String convertLongToDateTime(long timestamp){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return dateFormat.format(cal.getTime());
    }

    public String convertLongToDate(long timestamp){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return dateFormat.format(cal.getTime());
    }

    public String convertLongToDateWithFullYear(long timestamp){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return dateFormat.format(cal.getTime());
    }

    public String convertLongToTime(long timestamp){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        return dateFormat.format(cal.getTime());
    }

    public String currentDateTime(){
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(dt);
    }
}
