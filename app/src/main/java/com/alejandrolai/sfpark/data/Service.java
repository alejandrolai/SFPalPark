package com.alejandrolai.sfpark.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;

/**
 * Created by Alejandro on 4/8/15.
 */

/**
 * Service class that sets the url to to get the data from SFPark, and configures gson to use our custom serializer (ParkingConverter)
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

    }

}
