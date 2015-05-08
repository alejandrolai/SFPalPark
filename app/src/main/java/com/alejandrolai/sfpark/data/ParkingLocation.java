package com.alejandrolai.sfpark.data;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by Ihsan Taha and Ami Parikh on 5/6/15.
 */
public class ParkingLocation /*extends ActionBarActivity implements LocationListener*/ {

    // Data fields

    private String _latitude, _longitude, _time;
    private int _id;
    private Location location;



    // Data methods

    /**
     * Default Constructor
     */
    public ParkingLocation() {

    }



    /**
     * Constructor
     *
     * @param lat
     * @param lon
     * @param time
     */
    public ParkingLocation( String lat, String lon, String time) {
        _latitude = lat;
        _longitude = lon;
        _time = time;
    }



    /**
     * Initializes the data fields
     *
     * @param id
     * @param lat
     * @param lon
     * @param time
     */
    public ParkingLocation(int id, String lat, String lon, String time) {
        _id = id;
        _latitude = lat;
        _longitude = lon;
        _time = time;
    }



    // Accessors

    public int getId() {
        return this._id;
    }

    public String getLatitude() {
        return this._latitude;
    }

    public String getLongitude() {
        return this._longitude;
    }

    public String getTime() {
        return this._time;
    }

}









/*
    public double getLatitude() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        return this.location.getLatitude();
    }

    public double getLongitude() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        return this.location.getLongitude();
    }




    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
*/


