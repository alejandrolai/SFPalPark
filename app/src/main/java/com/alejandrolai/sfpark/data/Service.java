package com.alejandrolai.sfpark.data;

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

/**
 * Service class that sets up everything for retrofit. Building an adapter, setting up the custom converter and serializer
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
        @GET("/sfpark/rest/availabilityservice")
        void getParkingSpots(@QueryMap Map<String, String> options, Callback<ParkingSpotList> callback);
    }

}
