package com.example.myapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBService extends SQLiteOpenHelper {
    public static final String TABLE = "notes";
    public static final String ID = "_id";
    public static final String TITLE ="title";
    public static final String CONTENT = "content";
    public static final String TIME = "time";
    public DBService(Context context) {
        super(context,"notepad.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+TABLE+"( "+ID+
                " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                TITLE +" VARCHAR(30) ,"+
                CONTENT + " TEXT , "+
                TIME + " DATETIME NOT NULL )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor findById(Context context, Long id) {
        SQLiteDatabase db = new com.example.myapp.DBService(context).getReadableDatabase();
        return db.rawQuery("select * from " + com.example.myapp.DBService.TABLE + " where _id =?", new String[]{id + ""});
    }
}
