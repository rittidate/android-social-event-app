package com.arraieot.android.socialevent.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SimpleEvent implements Event, Parcelable {
    private long id;
    private String title;
    private String location;
    private long startDateTime;
    private long startDate;
    private String startTime;
    private long endDateTime;
    private long endDate;
    private String endTime;
    private String description;
    private String attendee;
    private String note;
    private String address;
    private double latitude;
    private double longitude;


    public SimpleEvent() {
    }

    protected SimpleEvent(Parcel in) {
        id = in.readLong();
        title = in.readString();
        location = in.readString();
        address = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        startDateTime = in.readLong();
        startDate = in.readLong();
        startTime = in.readString();
        endDateTime = in.readLong();
        endDate = in.readLong();
        endTime = in.readString();
        description = in.readString();
        attendee = in.readString();
        note = in.readString();
    }

    public static final Creator<SimpleEvent> CREATOR = new Creator<SimpleEvent>() {
        @Override
        public SimpleEvent createFromParcel(Parcel in) {
            return new SimpleEvent(in);
        }

        @Override
        public SimpleEvent[] newArray(int size) {
            return new SimpleEvent[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeString(location);
        parcel.writeString(address);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeLong(startDateTime);
        parcel.writeLong(startDate);
        parcel.writeString(startTime);
        parcel.writeLong(endDateTime);
        parcel.writeLong(endDate);
        parcel.writeString(endTime);
        parcel.writeString(description);
        parcel.writeString(attendee);
        parcel.writeString(note);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public long getStartDateTime() {
        return startDateTime;
    }

    @Override
    public void setStartDateTime(long startDateTime) {
        this.startDateTime = startDateTime;
    }

    @Override
    public long getStartDate() {
        return startDate;
    }

    @Override
    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    @Override
    public String getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Override
    public long getEndDateTime() {
        return endDateTime;
    }

    @Override
    public void setEndDateTime(long endDateTime) {
        this.endDateTime = endDateTime;
    }

    @Override
    public long getEndDate() {
        return endDate;
    }

    @Override
    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    @Override
    public String getEndTime() {
        return endTime;
    }

    @Override
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getAttendee() {
        return attendee;
    }

    @Override
    public void setAttendee(String attendee) {
        this.attendee = attendee;
    }

    @Override
    public String getNote() {
        return note;
    }

    @Override
    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }



}
