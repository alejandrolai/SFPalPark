package com.alejandrolai.sfpark.data;

import com.google.android.gms.maps.model.LatLng;

import com.google.maps.android.SphericalUtil;

/**
 * Created by Alejandro on 4/8/15.
 */

/**
 * ParkingSpot class which represents a parking spot
 */
public class ParkingSpot {

    private String parkingType;
    private String streetName;

    private double startLongitude;
    private double startLatitude;
    private double endLongitude;
    private double endLatitude;

    private String endTime;
    private double rate;
    // Per hour, street sweep, no charge
    private String rateQualifier;

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getParkingType() { return parkingType; }

    public void setParkingType(String parkingType) {
        this.parkingType = parkingType;
    }

    public double getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(double startLongitude) {
        this.startLongitude = startLongitude;
    }

    public double getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(double startLatitude) {
        this.startLatitude = startLatitude;
    }

    public double getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(double endLongitude) {
        this.endLongitude = endLongitude;
    }

    public double getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(double endLatitude) {
        this.endLatitude = endLatitude;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getRateQualifier() {
        return rateQualifier;
    }

    public void setRateQualifier(String rateQualifier) {
        this.rateQualifier = rateQualifier;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

}