package com.alejandrolai.sfpark.data;

import com.alejandrolai.sfpark.data.ParkingConverter;
import com.alejandrolai.sfpark.data.ParkingSpotList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.QueryMap;

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
                    .setLogLevel(RestAdapter.LogLevel.FULL)
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

        @GET("/sfpark/rest/availabilityservice")
        void getParkingSpots(@QueryMap Map<String, String> options, Callback<ParkingSpotList> callback);
    }
}
