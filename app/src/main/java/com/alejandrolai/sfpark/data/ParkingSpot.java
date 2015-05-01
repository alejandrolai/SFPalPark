package com.alejandrolai.sfpark.data;

import com.google.android.gms.maps.model.LatLng;

import com.google.maps.android.SphericalUtil;

/**
 * Created by Alejandro on 4/8/15.
 */


public class ParkingSpot {

    private String parkingType;
    private String streetName;

    private String location;

    private double latitude;
    private double longitude;

    private double startLongitude;
    private double startLatitude;
    private double endLongitude;
    private double endLatitude;

    private String endTime;

    private double rate;
    // per hour, street sweep, no charge
    private String rateQualifier;

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getParkingType() {

        return parkingType;
    }

    public void setParkingType(String parkingType) {
        this.parkingType = parkingType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;

        //Added by dolly to extract latitude & longitude from string location and parse them to double
        String[] splittedLocation = location.split(",");
        this.latitude = Double.parseDouble(splittedLocation[0]);
        this.longitude = Double.parseDouble(splittedLocation[1]);
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

    /**
     * Added by Dolly 4/27/15
     * A method that computes the distance of a parking spot from the current location
     *
     * @param original_position - coordinates of current location
     * @return distance from current location
     */
    public double computeDistanceFrom(LatLng original_position) {

        LatLng parking_spot_coord = new LatLng(this.latitude, this.longitude);

        return SphericalUtil.computeDistanceBetween(parking_spot_coord, original_position);
    }

    /**
     * Added by Dolly 4/27/2015
     *
     * @return LatLng object for the coordinates of the spot
     */
    public LatLng getCoordinates() {
        LatLng coordinates;
        return coordinates = new LatLng(this.latitude, this.longitude);
    }

}