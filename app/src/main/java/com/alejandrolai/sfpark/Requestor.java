package com.alejandrolai.sfpark;

import android.os.AsyncTask;

import com.alejandrolai.sfpark.data.ParkingSpotList;

/**
 * Created by Alejandro on 4/21/15.
 */
public class Requestor extends AsyncTask<Void,Void,ParkingSpotList>{

    ParkingSpotList parkingSpotList;

    public Requestor(ParkingSpotList newParkingSpotList){
        parkingSpotList = newParkingSpotList;
    }
    @Override
    protected ParkingSpotList doInBackground(Void... params) {
        return parkingSpotList;
    }

    @Override
    protected void onPostExecute(ParkingSpotList parkingSpotList) {
        super.onPostExecute(parkingSpotList);
        /* Put on database */
    }
}
