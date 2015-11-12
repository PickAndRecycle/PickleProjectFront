package com.pickle.pickleprojectmodel;

/**
 * Created by Yanuar Wicaksana on 10/15/15.
 */


import java.io.File;
import java.io.Serializable;
import java.sql.Time;
import java.util.*;


public class Trash implements Serializable {
    public String id;
    public int status;
    public String description, photo_url;
    public String title;
    public String photo_data;
    public int latitude,longitude;
    public int timestamp;
    public boolean report;
    public int distance;
    public int size;
    public TrashCategories categories;
    public UnusedCondition trash_condition;
    public String username;

    public Trash(String id, String description, String title, int status, String photo_data, int latitude, int longitude,
                 int timestamp, boolean report, int distance, int size, TrashCategories categories, UnusedCondition trash_condition,
                 String username){
        this.id = id;
        this.description = description;
        this.title = title;
        this.status = status;
        this.photo_data = photo_data;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.report = report;
        this.distance = distance;
        this.size = size;
        this.categories = categories;
        this.trash_condition = trash_condition;
        this.photo_url = "";
        this.username = username;
    }
    public Trash(){

    }

    public void test(){
        System.out.println("tests");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDesc() {
        return description;
    }

    public void setDesc(String desc) {
        this.description = desc;
    }

    public void setTitle(String newtitle) {
        this.title = newtitle;
    }

    public String getTitle(){
        return title;
    }

    public void setPhoto(String newphoto) {
        this.photo_data = newphoto;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isReport() {
        return report;
    }

    public void setReport(boolean report) {
        this.report = report;
    }

    public void setDistance(int newdistance){
        this.distance = newdistance;
    }

    public int getDistance(){
        return distance;
    }

    public void setsize(int newsize){
        this.size = newsize;
    }

    public int getsize(){
        return size;
    }
    public void setCategories(TrashCategories newcategories){
        this.categories = newcategories;
    }

    public TrashCategories getCategories(){
        return categories;
    }

    public void setCondition(UnusedCondition trash_condition){
        this.trash_condition = trash_condition;
    }

    public UnusedCondition getCondition(){
        return trash_condition;
    }

    public void setPhoto_url(String url){
        this.photo_url = url;
    }

    public String getPhoto_url(){
        return photo_url;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }



    public int CalculateDist(Double lat, Double lon){
        double theta = this.longitude - lon;
        double dist = Math.sin(deg2rad(this.latitude)) * Math.sin(deg2rad(lat)) +
                Math.cos(deg2rad(this.latitude)) * Math.cos(deg2rad(lat)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        return (int) dist;
    }
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
