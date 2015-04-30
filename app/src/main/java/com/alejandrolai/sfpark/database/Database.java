package com.alejandrolai.sfpark.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.alejandrolai.sfpark.data.ParkingSpot;

import java.util.ArrayList;
import java.util.Date;


public class Database {
    public static final String TAG = Database.class.getSimpleName();

    private Database mHelper;
    private SQLiteDatabase mDatabase;

    public void insertParkingSpots(ArrayList<ParkingSpot> parkingSpotList){
        String sql = "INSERT INTO " + DatabaseHelper.TABLE_NAME + " VALUES(?,?,?,?);";
        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (int i=0; i<parkingSpotList.size();i++) {
            ParkingSpot parkingSpot = parkingSpotList.get(i);
            statement.clearBindings();
            statement.bindString(2, parkingSpot.getStreetName());
            statement.bindString(3, parkingSpot.getParkingType());
            statement.bindString(4, parkingSpot.getLocation());

            statement.execute();
        }
        //set the transaction as successful and end the transaction
        Log.d(TAG,"inserting entries " + parkingSpotList.size() + new Date(System.currentTimeMillis()));
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public Database(Context context) {
        mHelper = new Database(context);
    }

    public void deleteAll() {
        mDatabase.delete(DatabaseHelper.TABLE_NAME, null, null);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        private static final int DATABASE_VERSION = 1;
        private Context mContext;
        private static final String DATABASE_NAME = "parkingSpots.db";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mContext = context;
        }

        public static final String TABLE_NAME = "parkingSpots";
        public static final String COLUMN_ID = "_id";
        public static final String STREET_NAME = "street_name";
        public static final String PARKING_TYPE = "parking_type";
        public static final String LOCATION = "location";
        //public static final String LATITUDE = "latitude";
        //public static final String LONGITUDE = "longitude";

        public static final String DICTIONARY_TABLE_CREATE = "CREATE TABLE "
                + TABLE_NAME + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + STREET_NAME + " TEXT NOT NULL COLLATE NOCASE, "
                + PARKING_TYPE + " TEXT NOT NULL COLLATE NOCASE, "
                + LOCATION + " TEXT NOT NULL);";

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DICTIONARY_TABLE_CREATE);
            } catch (SQLiteException exception){
                Log.e("Database", "Could not create database");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(Database.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
