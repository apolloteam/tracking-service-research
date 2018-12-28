package com.prueba.conex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import com.prueba.conex.Message;

public class AppDatabase extends SQLiteOpenHelper {

    public AppDatabase(Context context) {
        super(context, "appDB", null, 1);

        Log.v("AppDatabaseHelper", "DB working....");
    }

    @Override public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE message ( id INTEGER  PRIMARY KEY AUTOINCREMENT , text TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long insertMessage(Message message){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues insert = new ContentValues();
        insert.put("text", message.getText());

        long newID = db.insert("message", null, insert);

        Log.v("AppDatabaseHelper", "new message ID: "+newID);

        db.close();

        return newID;
    }

    public List<String> getMessages() {

        List<String> items = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT text FROM message order by id desc", null);

        if (cursor.moveToFirst()) {
            do {

                String item = cursor.getString(cursor.getColumnIndex("text"));
                items.add(item);
            } while (cursor.moveToNext());
        }

        db.close();

        Log.v("AppDatabaseHelper", "Messages count: "+items.size());

        return items;
    }

    public void deleteAll() {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM message");

        db.close();

        Log.v("AppDatabaseHelper", "Messages were deleted");
    }
}
