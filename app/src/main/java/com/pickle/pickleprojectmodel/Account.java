package com.pickle.pickleprojectmodel;

import java.io.Serializable;

/**
 * Created by Yanuar Wicaksana on 11/14/15.
 */
public class Account implements Serializable {
    private String id;
    private String username;
    private String email;
    private String password;
    private String phone_number;
    private String profile_picture_url;
=======
 * Created by danieldeem on 11/16/2015.
 */
public class Account implements Serializable {
    public String id;
    public String username;
    public String email;
    public String password;
    public String phoneNumber;
    public String photoUrl;
>>>>>>> f7721f36dc64109b7625a0f03ddfa32c491c4a8b

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

<<<<<<< HEAD
    public String getPhone_number() {
        return phone_number;
    }

    public String getProfile_picture_url() {
        return profile_picture_url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
=======
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
>>>>>>> f7721f36dc64109b7625a0f03ddfa32c491c4a8b
    }

    public void setEmail(String email) {
        this.email = email;
    }

<<<<<<< HEAD
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setProfile_picture_url(String profile_picture_url) {
        this.profile_picture_url = profile_picture_url;
    }

    public String toString() {
        final StringBuffer sb = new StringBuffer("Account{");
        sb.append("id=").append(getId());
        sb.append(", username='").append(getUsername()).append('\'');
        sb.append(", email='").append(getEmail()).append('\'');
        sb.append(", password='").append(getPassword()).append('\'');
        sb.append(", phone number='").append(getPhone_number()).append('\'');
        sb.append(", profile picture=").append(getProfile_picture_url());
        return sb.toString();
    }
}
=======
    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}

>>>>>>> f7721f36dc64109b7625a0f03ddfa32c491c4a8b
