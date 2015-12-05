package com.pickle.pickleprojectmodel;

import java.io.Serializable;

/**
 * Created by Yanuar Wicaksana on 12/4/15.
 */
public class Notification implements Serializable {
    public String username;
    public String token;

    public Notification (String username, String token){
        this.username = username;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Trash{");
        sb.append("username='").append(username).append('\'');
        sb.append(", token='").append(token);
        return sb.toString();
    }
}
