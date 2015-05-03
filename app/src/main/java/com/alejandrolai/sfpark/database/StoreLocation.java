package com.alejandrolai.sfpark.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alejandrolai.sfpark.MainActivity;
import com.google.android.gms.analytics.ecommerce.Product;

/**
 * Created by amiparikh on 4/24/15.
 */
public class StoreLocation extends SQLiteOpenHelper {

    public ContentValues mValues;
    public MainActivity MP;

    private static final int DATABASE_VERSION = 2;
    private static final String LOCATION_HISTORY = "user_information";
    private static final String KEY_ID = "";
    private static final String DATABASE_NAME = "";
    private static final String KEY_DEFINITION = "";

    // public static final String COLUMN_ID = "_id";
    public static final String COLUMN_Latitude = "latitude";
    public static final String COLUMN_Longitude = "longitude";
    public static final String COLUMN_currenttime = "currentime";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + LOCATION_HISTORY + " (" +
                    KEY_ID + " TEXT, " +
                    KEY_DEFINITION + " TEXT, " +
                    // COLUMN_ID + " TEXT, " +
                    COLUMN_Latitude + " TEXT, " +
                    COLUMN_Longitude + " TEXT, " +
                    COLUMN_currenttime + " TEXT);";




    /**
     * ?
     *
     * @param context
     */
    public StoreLocation(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mValues = new ContentValues();
    }



    /**
     * ?
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL(TABLE_CREATE);
        //insertInDB();
        addProduct(MP);
    }



    /**
     * ?
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("Drop table if exists " + LOCATION_HISTORY);
        onCreate(db);
    }



    /*
    public long insertInDB(SQLiteDatabase db) {


       mValues.put(COLUMN_Latitude, "hi");
       return db.insert(TABLE_CREATE, null, mValues);
    }
    */



    /**
     * Puts the latitude, longitude, and time into the database.
     *
     * @param MP
     */
    public void addProduct(MainActivity MP) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_Latitude, MP.getLatitude());
        values.put(COLUMN_Longitude, MP.getLongitude());
        values.put(COLUMN_currenttime, System.currentTimeMillis());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(LOCATION_HISTORY, null, values);
        db.close();
    }



    /*
    public Product findLATLONG(Integer ID) {
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + KEY_ID + " =  \"" + ID + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);



        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            int latitude= Integer.parseInt(cursor.getString(0));
            int longitude = Integer.parseInt(cursor.getString(1));
            product.setQuantity(Integer.parseInt(cursor.getString(2)));
            cursor.close();
        } else {

        }
        db.close();
        return product;
    }
    */

}


