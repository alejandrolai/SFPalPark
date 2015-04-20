package com.alejandrolai.sfpark;

import android.util.Log;
import android.widget.Toast;

import com.alejandrolai.sfpark.model.ParkingSpot;
import com.alejandrolai.sfpark.model.ParkingSpotList;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class ParkingConverter implements JsonDeserializer<ParkingSpotList> {

    @Override
    public ParkingSpotList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        ParkingSpotList parkingSpotList = new ParkingSpotList();

        if (json.isJsonObject()) {
            JsonObject sfParkData = json.getAsJsonObject();
            if (sfParkData.getAsJsonArray("AVL").isJsonArray()) {
                JsonArray data = sfParkData.getAsJsonArray("AVL");
                // Log.i("ParkingConverter", "Getting Data");
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
                        if (!dataObject.get("NAME").isJsonNull() && !dataObject.get("DESC").isJsonNull()) {
                            parkingSpot.setStreetName(dataObject.get("NAME").getAsString()
                                    + ", " + dataObject.get("DESC").getAsString());
                        }
                    }
                    if (!dataObject.get("LOC").isJsonNull()) {
                        String location = dataObject.get("LOC").getAsString();
                        String[] parts = location.split(",");
                        String longitude = parts[0];
                        String latitude = parts[1];
                        parkingSpot.setLocation(latitude + "," + longitude);
                        /*
                        String location = dataObject.get("LOC").getAsString();
                        String[] parts = location.split(",");
                        parkingSpot.startLatitude = parts[1];
                        parkingSpot.startLongitude = parts[0];
                        parkingSpot.endLatitude = parts[3];
                        parkingSpot.endLongitude = parts[2];
                        */
                    }
                    parkingSpotList.addParkingSpot(parkingSpot);
                }
            }
        } else {
            Log.w("ParkingConverter", "No Data 1");
        }

        return parkingSpotList;
    }
}
