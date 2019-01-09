package com.prueba.conex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class AppDatabase extends SQLiteOpenHelper {

    public AppDatabase(Context context) {
        super(context, "appDB", null, 1);

        Log.v("AppDatabaseHelper", "DB working....");
    }

    private StringBuilder trackingPositionsScript = new StringBuilder("CREATE TABLE trackingPositions( ")
            .append("id INTEGER  PRIMARY KEY AUTOINCREMENT, ")
            .append("date TEXT, ")
            .append("holderId INTEGER, ")
            .append("activityId INTEGER, ")
            .append("ownerId INTEGER, ")
            .append("holderStatus INTEGER, ")
            .append("activityStatus INTEGER, ")
            .append("lat TEXT, ")
            .append("lng TEXT, ")
            .append("accuracy DECIMAL, ")
            .append("speed DECIMAL )");

    @Override public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(trackingPositionsScript.toString());
    }

    @Override public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long insertMessage(TrackingPositionModel trackingPositionModel){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues insert = new ContentValues();
        insert.put("date", trackingPositionModel.getDate());
        insert.put("holderId", trackingPositionModel.getHolderId());
        insert.put("activityId", trackingPositionModel.getActivityId());
        insert.put("ownerId", trackingPositionModel.getOwnerId());
        insert.put("holderStatus", trackingPositionModel.getHolderStatus());
        insert.put("activityStatus", trackingPositionModel.getActivityStatus());
        insert.put("lat", trackingPositionModel.getLat());
        insert.put("lng", trackingPositionModel.getLng());
        insert.put("accuracy", trackingPositionModel.getAccuracy());
        insert.put("speed", trackingPositionModel.getSpeed());

        long newID = db.insert("trackingPositions", null, insert);

        db.close();

        return newID;
    }

    public List<String> getTrackingPositions() {

        List<String> items = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT id, date, lat, lng FROM trackingPositions order by id desc", null);

        if (cursor.moveToFirst()) {
            do {

                Integer id = cursor.getInt(cursor.getColumnIndex("id"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String lat = cursor.getString(cursor.getColumnIndex("lat"));
                String lng = cursor.getString(cursor.getColumnIndex("lng"));

                TrackingPositionModel model = new TrackingPositionModel();
                model.setId(id);
                model.setDate(date);
                model.setLat(lat);
                model.setLng(lng);

                String text = date+", lat: "+lat+", lng: "+lng;

                items.add(text);

            } while (cursor.moveToNext());
        }

        db.close();

        return items;
    }

    public void deleteAll() {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM trackingPositions");

        db.close();
    }
}
