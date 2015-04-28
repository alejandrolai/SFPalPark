package com.alejandrolai.sfpark.model;

import com.alejandrolai.sfpark.model.ParkingSpot;

import java.util.ArrayList;

/**
 * Created by Alejandro on 4/8/15.
 */
public class ParkingSpotList {

    ArrayList<ParkingSpot> list;

    public ParkingSpotList() {
        list = new ArrayList<>();
    }

    public ArrayList<ParkingSpot> getList(){
        return list;
    }

    public int getListSize(){ return list.size(); }

    public void addParkingSpot(ParkingSpot newParkingSpot) {
        list.add(newParkingSpot);
    }


    public ParkingSpot getSpot(int i){ return list.get(i);}
}
