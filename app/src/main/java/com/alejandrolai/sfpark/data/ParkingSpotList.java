package com.alejandrolai.sfpark.data;

import java.util.ArrayList;

/**
 * Created by Alejandro on 4/8/15.
 */

/**
 * ParkingSpotList class which contains an ArrayList of ParkingSpot objects
 */
public class ParkingSpotList {

    ArrayList<ParkingSpot> list;

    public ParkingSpotList() {
        list = new ArrayList<>();
    }

    public ArrayList<ParkingSpot> getList(){
        return list;
    }

    public void addParkingSpot(ParkingSpot newParkingSpot) {
        list.add(newParkingSpot);
    }
}
