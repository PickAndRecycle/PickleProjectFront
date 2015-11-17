package com.pickle.pickleprojectmodel;

import java.io.Serializable;

/**
 * Created by admin on 11/13/2015.
 */
public class Article implements Serializable {
    public String id;
    public String title;
    public String photo_url;
    public String content;

    public Article(String id, String title, String photo_url, String content){
        this.id = id;
        this.title = title;
        this.photo_url = photo_url;
        this.content = content;
    }

    public Article(){

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
