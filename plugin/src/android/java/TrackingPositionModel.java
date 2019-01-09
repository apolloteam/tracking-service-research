package com.prueba.conex;

public class TrackingPositionModel {

    private int id;
    private String date;
    private Integer holderId;
    private Integer activityId;
    private Integer ownerId;
    private Integer holderStatus;
    private Integer activityStatus;
    private String lat;
    private String lng;
    private Float accuracy;
    private Float speed;

    public TrackingPositionModel(){}

    public TrackingPositionModel(int id, String date, Integer holderId, Integer activityId,
                                 Integer ownerId, Integer holderStatus, Integer activityStatus,
                                 String lat, String lng, Float accuracy, Float speed) {
        this.id = id;
        this.date = date;
        this.holderId = holderId;
        this.activityId = activityId;
        this.ownerId = ownerId;
        this.holderStatus = holderStatus;
        this.activityStatus = activityStatus;
        this.lat = lat;
        this.lng = lng;
        this.accuracy = accuracy;
        this.speed = speed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getHolderId() {
        return holderId;
    }

    public void setHolderId(Integer holderId) {
        this.holderId = holderId;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getHolderStatus() {
        return holderStatus;
    }

    public void setHolderStatus(Integer holderStatus) {
        this.holderStatus = holderStatus;
    }

    public Integer getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(Integer activityStatus) {
        this.activityStatus = activityStatus;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public Float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Float accuracy) {
        this.accuracy = accuracy;
    }

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }
}