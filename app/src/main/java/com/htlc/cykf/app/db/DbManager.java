package com.htlc.cykf.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.htlc.cykf.app.App;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by sks on 2016/1/4.
 */
public class DbManager {
    public static final String DATABASE_LAST_MODIFY = "database_last_modify";

    static final String DATABASE_NAME = "city_list.db";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_CREATE =
            "CREATE TABLE area ( _id integer primary key autoincrement, AREA_CODE integer, AREA_NAME varchar(20), TYPE integer, PARENT_ID integer);";



    private static DatabaseHelper databaseHelper;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.wtf("DatabaseHelper", "Upgrading database from version " + oldVersion + "to " +
                    newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }

    //版本3-----------------------
    private static DbManager instance;
    private AtomicInteger mOpenCounter = new AtomicInteger();
    private SQLiteDatabase mDatabase;
    private DbManager(){
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(App.app);
        }
    }
    public static synchronized DbManager getInstance() {
        if(instance == null){
            instance = new DbManager();
        }
        return instance;
    }
    public synchronized SQLiteDatabase openDatabase() {
        if(mOpenCounter.incrementAndGet() == 1) {
            mDatabase = databaseHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        if(mOpenCounter.decrementAndGet() == 0) {
            mDatabase.close();
        }
    }
}

