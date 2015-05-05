package com.alejandrolai.sfpark.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.widget.Toast;

import com.alejandrolai.sfpark.data.ParkedLocation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ihsan_taha on 4/30/15.
 */
public class MyLocationDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "parkedLocationDB.db";
    private static final String TABLE_PARKED_LOCATION = "parked_location";

    private static final String COLUMN_ID = "_id";
  //  private static final String COLUMN_LOCATIONID = "locationid";
    private static final String COLUMN_XLOCATION = "xlocation";
    private static final String COLUMN_YLOCATION = "ylocation";
    public static final String COLUMN_currenttime = "currentime";

    public MyLocationDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        //context.deleteDatabase(DATABASE_NAME);
    }



    /**
     * Creates a parked location database table with three columns:
     * the id, location id, x coordinate, and y coordinate.
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {



        String CREATE_PARKEDLOCATION_TABLE = "CREATE TABLE " +
                TABLE_PARKED_LOCATION + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_currenttime + " INTEGER," + COLUMN_XLOCATION + " FLOAT," + COLUMN_YLOCATION
                + " FLOAT" + ")";
        db.execSQL(CREATE_PARKEDLOCATION_TABLE);

    }



    /**
     * Upgrades the database.
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARKED_LOCATION);
        onCreate(db);

    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }



    /**
     * Puts the x and y coordinate values into the database.
     *
     * @param parkedlocation
     */
    public void addParkedLocation(ParkedLocation parkedlocation) {



        ContentValues values = new ContentValues();




        values.put(COLUMN_currenttime, getDateTime());
       // values.put(COLUMN_LOCATIONID, parkedlocation.getLocationID());
        values.put(COLUMN_XLOCATION, parkedlocation.getXLocation());
        values.put(COLUMN_YLOCATION, parkedlocation.getYLocation());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_PARKED_LOCATION, null, values);
        db.close();

    }



    /**
     * Searches for the parked location based on a given location id input value.
     *
     * @param input_location_id
     * @return
     */
    public ParkedLocation findLocation(float input_location_id) {

        //String query = "SELECT * FROM " + TABLE_PARKED_LOCATION + " WHERE " + COLUMN_LOCATIONID + " = \"" + input_location_id + "\"";

        String query = "SELECT * FROM " + TABLE_PARKED_LOCATION + " order by  " + COLUMN_currenttime + "";


        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        ParkedLocation parkedlocation = new ParkedLocation();

        if (cursor.moveToFirst()) {

            cursor.moveToFirst();
            parkedlocation.setID(Integer.parseInt(cursor.getString(0)));
            parkedlocation.setLocationID(Float.parseFloat(cursor.getString(1)));
            parkedlocation.setXLocation(Float.parseFloat(cursor.getString(2)));
            parkedlocation.setYLocation(Float.parseFloat(cursor.getString(3)));
            cursor.close();

        } else {
            parkedlocation = null;
        }

        db.close();
        return parkedlocation;

    }



    /**
     * Deletes a parked location based on the location id, x, and y coordinate input values.
     *
     * @param input_location_id
     * @return
     */
    public boolean deleteParkedLocation(float input_location_id) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_PARKED_LOCATION + " WHERE " + COLUMN_currenttime + " =  \"" + input_location_id + "\"";

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
