package com.tadnyasoftech.geoattendance.models;

import java.io.Serializable;

/**
 * Created by dell on 19/12/17.
 */

public class Attendance {

    private String id;

    private long checkInTime;

    private long checkOutTime;

    private String imageUrl;

    public String getId() { return id; }

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

    public void setCheckOutTime(long checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
