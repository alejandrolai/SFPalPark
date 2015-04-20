package com.alejandrolai.sfpark;

import com.alejandrolai.sfpark.model.ParkingSpotList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;

/**
 * Created by Alejandro on 4/8/15.
 */
public class Service {

    private static SFParkService sfParkService = null;

    public static SFParkService getService(){

        if (sfParkService == null) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(ParkingSpotList.class, new ParkingConverter())
                    .create();

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://api.sfpark.org")
                    .setConverter(new GsonConverter(gson))
                    .build();

            sfParkService = restAdapter.create(SFParkService.class);

        }
        return sfParkService;
    }

    public interface SFParkService {

        @GET("/sfpark/rest/availabilityservice?response=json&pricing=yes")
        void getParkingSpots(Callback<ParkingSpotList> callback);
    }
}
