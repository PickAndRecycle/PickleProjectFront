package com.pickle.pickleprojectmodel;

import java.io.Serializable;

/**
 * Created by danieldeem on 11/16/2015.
 */
public class Account implements Serializable {
    public String id;
    public String username;
    public String email;
    public String phoneNumber;
    public String photoUrl;

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}

