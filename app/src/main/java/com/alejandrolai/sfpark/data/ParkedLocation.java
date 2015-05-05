package com.alejandrolai.sfpark.data;


import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


/**
 * Created by ihsan_taha on 4/30/15.
 */
public class ParkedLocation extends ActionBarActivity implements LocationListener {

    private int _id;
    private float _locationid;
    private float _xlocation;
    private float _ylocation;
    Location location;



    /**
     * Default Constructor.
     */
    public ParkedLocation() {

    }



    /**
     * Constructor.
     *
     * @param
     * @param xloc
     * @param yloc
     */

    public ParkedLocation( float xloc , float yloc) {
        //this._locationid = locID;
        this._xlocation = xloc;
        this._ylocation = yloc;
    }


    public ParkedLocation(float locID, float xloc , float yloc) {
        this._locationid = locID;
        this._xlocation = xloc;
        this._ylocation = yloc;
    }



    /**
     * Constructor.
     *
     * @param id
     * @param locID
     * @param xloc
     * @param yloc
     */
    public ParkedLocation(int id, float locID, float xloc , float yloc) {
        this._id  = id;
        this._locationid = locID;
        this._xlocation = xloc;
        this._ylocation = yloc;
    }



    // Setters
    public void setID(int id) { this._id = id; }

    public void setLocationID (float locID) { this._locationid = locID; }

    public void setXLocation (float xloc) { this._xlocation = xloc; }

    public void setYLocation (float yloc) { this._ylocation = yloc; }



    // Getters
    public int getID() {return this._id;}

    public float getLocationID() { return this._locationid; }

    public float getXLocation() { return this._xlocation; }

    public float getYLocation() { return this._ylocation; }


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
}