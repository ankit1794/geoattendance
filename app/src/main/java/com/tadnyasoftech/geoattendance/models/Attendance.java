package com.tadnyasoftech.geoattendance.models;

import android.location.Location;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by dell on 19/12/17.
 */

public class Attendance {

    private String id;

    private long checkInTime;

    private long checkOutTime;

    private String address;

    private String imageUrl;

    private double latitude;

    private double longitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(long checkInTime) {
        this.checkInTime = checkInTime;
    }

    public long getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime() {
        this.checkOutTime = checkOutTime;
    }

    public  String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
