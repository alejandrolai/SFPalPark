package com.alejandrolai.sfpark.model;

/**
 * Created by Alejandro on 4/8/15.
 */
public class ParkingSpot {

    private String parkingType;
    private String streetName;

    private String location;
    /*
    public String startLongitude;
    public String startLatitude;
    public String endLongitude;
    public String endLatitude;
    */
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
    }
}