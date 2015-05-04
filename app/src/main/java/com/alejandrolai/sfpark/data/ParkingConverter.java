package com.alejandrolai.sfpark.data;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Calendar;

public class ParkingConverter implements JsonDeserializer<ParkingSpotList> {


    /**
     * ?
     *
     * @param json
     * @param typeOfT
     * @param context
     * @return
     * @throws JsonParseException
     */
    @Override
    public ParkingSpotList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        final String TAG = ParkingConverter.class.getSimpleName();

        // gets the current time in am/pm form (6:49 PM)
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR);
        int minutes = now.get(Calendar.MINUTE);
        int meridian = now.get(Calendar.AM_PM); // 0 if am, 1 if pm
        ParkingSpotList parkingSpotList = new ParkingSpotList();

        if (json.isJsonObject()) {
            JsonObject sfParkData = json.getAsJsonObject();
            if (sfParkData.getAsJsonArray("AVL").isJsonArray()) {
                JsonArray data = sfParkData.getAsJsonArray("AVL");
                for (int i = 0; i < data.size(); i++) {
                    JsonObject dataObject = data.get(i).getAsJsonObject();
                    ParkingSpot parkingSpot = new ParkingSpot();
                    if (!dataObject.get("TYPE").isJsonNull() && dataObject.get("TYPE").getAsString().equals("ON")) {
                        parkingSpot.setParkingType("Street Parking");
                        if (!dataObject.get("NAME").isJsonNull()) {
                            parkingSpot.setStreetName(dataObject.get("NAME").getAsString());
                        }
                    } else if (!dataObject.get("TYPE").isJsonNull() && dataObject.get("TYPE").getAsString().equals("OFF")) {
                        parkingSpot.setParkingType("Garage");
                        if (!dataObject.get("NAME").isJsonNull() &&
                                !dataObject.get("DESC").isJsonNull() && !dataObject.get("INTER").isJsonNull()) {
                            parkingSpot.setStreetName(dataObject.get("NAME").getAsString()
                                    + ", " + dataObject.get("DESC").getAsString()
                                    + ", " + dataObject.get("INTER").getAsString());
                        }
                    } else {
                        Log.e("ParkingConverter", "No parking spots");
                    }
                    if (!dataObject.get("LOC").isJsonNull()) {
                        String location = dataObject.get("LOC").getAsString();
                        String[] parts = location.split(",");
                        if (parts.length == 4) {
                            parkingSpot.setStartLongitude(Double.parseDouble(parts[0]));
                            parkingSpot.setStartLatitude(Double.parseDouble(parts[1]));
                            parkingSpot.setEndLongitude(Double.parseDouble(parts[2]));
                            parkingSpot.setEndLatitude(Double.parseDouble(parts[3]));
                        } else {
                            parkingSpot.setStartLatitude(Double.parseDouble(parts[1]));
                            parkingSpot.setStartLongitude(Double.parseDouble(parts[0]));
                            parkingSpot.setEndLatitude(Double.parseDouble(parts[1]));
                            parkingSpot.setEndLongitude(Double.parseDouble(parts[0]));
                        }

                    } else {
                        Log.e("ParkingConverter", "No parking spots");
                    }
                    /*
                    if (dataObject.getAsJsonObject("RATES").isJsonObject()){
                        /*
                        JsonObject rates = dataObject.getAsJsonObject("RATES");
                        if (rates.getAsJsonObject("RS").isJsonObject() || rates.getAsJsonArray("RS").isJsonArray()){
                            JsonObject rt = rates.getAsJsonObject("RS");
                            Log.i(TAG,"object or array");

                            JsonObject object = rt.get(1).getAsJsonObject();
                            parkingSpot.setRate(Double.parseDouble(object.get("RATE").getAsString()));
                            parkingSpot.setRateQualifier("Per hour");
                            parkingSpot.setEndTime("10:00 AM");

                        }else {
                            Log.e(TAG,"object");
                            Log.e(TAG,"array");
                        }

                    } else {
                        Log.e(TAG,"no array here");
                    }
                    */

                    parkingSpotList.addParkingSpot(parkingSpot);
                }
            } else {
                Log.e("ParkingConverter", "No parking spots");
            }
        } else {
            Log.w("ParkingConverter", "No Data 1");
        }

        return parkingSpotList;
    }
}