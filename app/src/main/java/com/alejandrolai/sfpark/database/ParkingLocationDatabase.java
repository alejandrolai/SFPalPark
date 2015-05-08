package com.alejandrolai.sfpark.database;

import com.alejandrolai.sfpark.data.ParkingLocation;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ihsan Taha and Ami Parikh on 5/6/15.
 */
public class ParkingLocationDatabase extends SQLiteOpenHelper {

    // Data fields

    private static final int DATABASE_VERSION = 1;

    private static final String
        DATABASE_NAME = "parkingHistory.db",
        TABLE_PARKING_HISTORY = "parkingHistory",
        KEY_ID = "_id",
        KEY_LATITUDE = "lat",
        KEY_LONGITUDE = "lon",
        KEY_TIME = "time";

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }



    // Data methods
    /**
     *
     *
     * @param context
     */
    public ParkingLocationDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    /**
     *
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PARKING_HISTORY + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_LATITUDE + " TEXT," + KEY_LONGITUDE + " TEXT," + KEY_TIME + " TEXT" + ")");
    }



    /**
     *
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARKING_HISTORY);

        onCreate(db);
    }



    /**
     * Creates a new parking location
     *
     * @param parkingLocation
     */
    public void createParkingLocation(ParkingLocation parkingLocation) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_LATITUDE, parkingLocation.getLatitude());
        values.put(KEY_LONGITUDE, parkingLocation.getLongitude());
        values.put(KEY_TIME, parkingLocation.getTime());

        db.insert(TABLE_PARKING_HISTORY, null, values);
        db.close();
    }



    /**
     * Gets a new parking location
     *
     * @param id
     * @return
     */
    public ParkingLocation getParkingLocation(int id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_PARKING_HISTORY, new String[] {KEY_ID, KEY_LATITUDE, KEY_LONGITUDE, KEY_TIME}, KEY_ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        ParkingLocation parkingLocation = new ParkingLocation(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        db.close();
        cursor.close();
        return parkingLocation;
    }




    /**
     * Deletes a parking location
     */
    public boolean deleteParkingHistory() {
        boolean result = true;
        String query = "Delete from " + TABLE_PARKING_HISTORY + "";
        SQLiteDatabase db = this.getWritableDatabase();
        ParkingLocation parkingLocation = new ParkingLocation();
        db.execSQL(query);
        db.close();
        return result;
    }



    /**
     * Updates the parking location
     *
     * @param parkingLocation
     * @return
     */
    public int updateParkingLocation(ParkingLocation parkingLocation) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_LATITUDE, parkingLocation.getLatitude());
        values.put(KEY_LONGITUDE, parkingLocation.getLongitude());
        values.put(KEY_TIME, parkingLocation.getTime());

        return db.update(TABLE_PARKING_HISTORY, values, KEY_ID + "=?", new String[] {String.valueOf(parkingLocation.getId())} );
    }



    /**
     *
     *
     * @return
     */
    public int getParkingsCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PARKING_HISTORY, null);
        int count = cursor.getCount();
        db.close();
        cursor.close();

        return count;
    }



    /**
     *
     *
     * @return
     */
    public List<ParkingLocation> getAllParkingLocations() {
        List<ParkingLocation> parkingLocations = new ArrayList<ParkingLocation>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PARKING_HISTORY, null);

        if (cursor.moveToFirst()) {
            do {
                ParkingLocation parkingLocation = new ParkingLocation(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                parkingLocations.add(parkingLocation);
            } while (cursor.moveToNext());
        }
        return parkingLocations;
    }
}
