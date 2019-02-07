package com.prueba.conex;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {

    private SharedPreferences sp;

    private final String SP_NAME = "TRACKER_SP";

    public static final String TRACKING_API_BASE_URL  = "trackingApiBaseUrl";
    public static final String LOG_API_BASE_URL       = "logApiBaseUrl";
    public static final String GPS_INTERVAL           = "gpsInterval";
    public static final String SERVICE_RUNNING        = "serviceRunning";
    public static final String HOLDER_ID              = "holderId";
    public static final String ACTIVITY_ID            = "activityId";
    public static final String OWNER_ID               = "ownerId";
    public static final String HOLDER_STATUS          = "holderStatus";
    public static final String ACTIVITY_STATUS        = "activityStatus";
    public static final String LAST_POSITION_DATE     = "lastPositionDate";
    public static final String LAST_POSITION          = "lastPosition";
    public static final String USB_MILLISECOND          = "usb_millisenconds";
    public static final String GPS_MILLISECOND          = "gps_milliseconds";
    public static final String USB_LAST_VALUE          = "usb_last_value";
    public static final String GPS_LAST_VALUE          = "gps_last_value";

    private final String EMPTY = "";

    public AppPreferences(Context context){

        this.sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public void setUsbMillisecond(long millis) {
        this.sp.edit().putLong(USB_MILLISECOND, millis).apply();
    }

    public long getUsbMillisecond() {
        return this.sp.getLong(USB_MILLISECOND, 0L);
    }

    public void setGpsMillisecond(long millis) {
        this.sp.edit().putLong(GPS_MILLISECOND, millis).apply();
    }

    public long getGpsMillisecond() {
        return this.sp.getLong(GPS_MILLISECOND, 0L);
    }

    public void setGpsLastValue(Boolean value){
        this.sp.edit().putBoolean(GPS_LAST_VALUE, value).apply();
    }

    public boolean getGpsLastValue(){
        return this.sp.getBoolean(GPS_LAST_VALUE, false);
    }

    public void setUsbLastValue(Boolean value){
        this.sp.edit().putBoolean(USB_LAST_VALUE, value).apply();
    }

    public boolean getUsbLastValue(){
        return this.sp.getBoolean(USB_LAST_VALUE, false);
    }

    public void setTrackingApiBaseUrl(String urlBase) {
        this.sp.edit().putString(TRACKING_API_BASE_URL, urlBase).apply();
    }

    public String getTrackingApiBaseUrl() {
        return this.sp.getString(TRACKING_API_BASE_URL, EMPTY);
    }

    public void setLogApiBaseUrl(String urlBase) {
        this.sp.edit().putString(LOG_API_BASE_URL, urlBase).apply();
    }

    public String getLogApiBaseUrl() {
        return this.sp.getString(LOG_API_BASE_URL, EMPTY);
    }

    public void setGspInterval(Integer gpsInterval) {
        this.sp.edit().putInt(GPS_INTERVAL, gpsInterval).apply();
    }

    public Integer getGspInterval() {
        return this.sp.getInt(GPS_INTERVAL, 0);
    }

    public void setServiceRunning(boolean isRunning) {
        this.sp.edit().putInt(SERVICE_RUNNING, isRunning ? 1 : 0 ).apply();
    }

    public Integer getServiceRunning() {
        return this.sp.getInt(SERVICE_RUNNING, 0);
    }

    public Boolean isServiceRunning() {
        return this.sp.getInt(SERVICE_RUNNING, 0) == 1;
    }

    public void setHolderId(Integer value) {
        this.sp.edit().putInt(HOLDER_ID, value).apply();
    }

    public Integer getHolderId() {
        return this.sp.getInt(HOLDER_ID, 0);
    }

    public void setLastPosition(String value) {
        this.sp.edit().putString(LAST_POSITION, value).apply();
    }

    public String getLastPosition() {
        return this.sp.getString(LAST_POSITION, EMPTY);
    }

    public void setLastPositionDate(String value) {
        this.sp.edit().putString(LAST_POSITION_DATE, value).apply();
    }

    public String getLastPositionDate() {
        return this.sp.getString(LAST_POSITION_DATE, EMPTY);
    }

    public void setActivityStatus(Integer value) {
        this.sp.edit().putInt(ACTIVITY_STATUS, value).apply();
    }

    public Integer getActivityStatus() {
        return this.sp.getInt(ACTIVITY_STATUS, 0);
    }

    public void setHolderStatus(Integer value) {
        this.sp.edit().putInt(HOLDER_STATUS, value).apply();
    }

    public Integer getHolderStatus() {
        return this.sp.getInt(HOLDER_STATUS, 0);
    }

    public void setOwnerId(Integer value) {
        this.sp.edit().putInt(OWNER_ID, value).apply();
    }

    public Integer getOwnerId() {
        return this.sp.getInt(OWNER_ID, 0);
    }

    public void setActivityId(Integer value) {
        this.sp.edit().putInt(ACTIVITY_ID, value).apply();
    }

    public Integer getActivityId() {
        return this.sp.getInt(ACTIVITY_ID, 0);
    }
}
