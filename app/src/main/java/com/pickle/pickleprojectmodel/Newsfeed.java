package com.pickle.pickleprojectmodel;

import java.io.Serializable;

/**
 * Created by admin on 11/13/2015.
 */
public class Newsfeed implements Serializable {
    public String id,title,photoData,content,timestamp;

    public Newsfeed(String id, String title, String photoData, String content, String timestamp) {
        this.id = id;
        this.title = title;
        this.photoData = photoData;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhotoData() {
        return photoData;
    }

    public void setPhotoData(String photoData) {
        this.photoData = photoData;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
