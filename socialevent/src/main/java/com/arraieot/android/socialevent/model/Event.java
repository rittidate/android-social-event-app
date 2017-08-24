package com.arraieot.android.socialevent.model;

public interface Event {
    long getId();

    void setId(long id);

    String getTitle();

    void setTitle(String title);

    String getLocation();

    void setLocation(String location);

    long getStartDateTime();

    void setStartDateTime(long startDateTime);

    long getStartDate();

    void setStartDate(long startDate);

    String getStartTime();

    void setStartTime(String startTime);

    long getEndDateTime();

    void setEndDateTime(long endDateTime);

    long getEndDate();

    void setEndDate(long endDate);

    String getEndTime();

    void setEndTime(String endTime);

    String getDescription();

    void setDescription(String description);

    String getAttendee();

    void setAttendee(String attendee);

    String getNote();

    void setNote(String note);

    String getAddress();

    void setAddress(String address);

    double getLatitude();

    void setLatitude(double latitude);

    double getLongitude();

    void setLongitude(double longitude);
}
