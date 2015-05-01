package com.alejandrolai.sfpark.database;

/**
 * Created by ihsan_taha on 4/30/15.
 */

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;

import com.alejandrolai.sfpark.ParkedLocation;

public class MyLocationDBHandler extends SQLiteOpenHelper {



    // Data Members

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "parkedLocationDB.db";
    private static final String TABLE_PARKED_LOCATION = "parked_location";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_XLOCATION = "xlocation";
    private static final String COLUMN_YLOCATION = "ylocation";



    // Data Methods
    // Why won't you commit?!

    public MyLocationDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, DATABASE_NAME, factory, DATABASE_VERSION);

    }


    /**
     * Creates a parked location database table with three columns:
     * the id, x coordinate, and y coordinate.
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_PARKEDLOCATION_TABLE = "CREATE TABLE " +
                TABLE_PARKED_LOCATION + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_XLOCATION + " FLOAT," + COLUMN_YLOCATION
                + " FLOAT" + ")";
        db.execSQL(CREATE_PARKEDLOCATION_TABLE);

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARKED_LOCATION);
        onCreate(db);

    }


    /**
     * Puts the x and y coordinate values into the database.
     *
     * @param parkedlocation
     */
    public void addParkedLocation(ParkedLocation parkedlocation) {

        ContentValues values = new ContentValues();

        values.put(COLUMN_XLOCATION, parkedlocation.getXLocation());
        values.put(COLUMN_YLOCATION, parkedlocation.getYLocation());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_PARKED_LOCATION, null, values);
        db.close();

    }


    /**
     * Searches for the parked location based on a given x coordinate input value.
     *
     * @param input_x_location
     * @return
     */
    public ParkedLocation findLocation(float input_x_location) {

        String query = "SELECT * FROM " + TABLE_PARKED_LOCATION + " WHERE " + COLUMN_XLOCATION + " = \"" + input_x_location + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        ParkedLocation parkedlocation = new ParkedLocation();

        if (cursor.moveToFirst()) {

            cursor.moveToFirst();
            parkedlocation.setID(Integer.parseInt(cursor.getString(0)));
            parkedlocation.setXLocation(Float.parseFloat(cursor.getString(1)));
            parkedlocation.setYLocation(Float.parseFloat(cursor.getString(2)));
            cursor.close();

        } else {
            parkedlocation = null;
        }

        db.close();
        return parkedlocation;

    }


    /**
     * Deletes a parked location based on the xa nd y coordinate input values.
     *
     * @param input_x_location
     * @return
     */
    public boolean deleteParkedLocation(Float input_x_location) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_PARKED_LOCATION + " WHERE " + COLUMN_XLOCATION + " =  \"" + input_x_location + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        ParkedLocation parkedlocation = new ParkedLocation();

        if (cursor.moveToFirst()) {
            parkedlocation.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_PARKED_LOCATION, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(parkedlocation.getID()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

}
