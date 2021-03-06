package com.tadnyasoftech.geoattendance.models;

/**
 * Created by dell on 19/12/17.
 */

public class User {

    private String id;

    private String fullName;

    private String gender;

    private String companyName;

    private String email;

    private String imageUrl;


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() { return id; }

    public void setId(String id) {
        this.id = id;
    }


}
