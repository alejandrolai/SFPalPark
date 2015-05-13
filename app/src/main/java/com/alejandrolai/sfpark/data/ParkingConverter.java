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


/**
 * Custom serializer to create custom java objects from the SFPark data
 */
public class ParkingConverter implements JsonDeserializer<ParkingSpotList> {

    @Override
    public ParkingSpotList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        final String TAG = ParkingConverter.class.getSimpleName();

        // gets the current time in am/pm form (6:49 PM)
        Calendar now = Calendar.getInstance();
        int currentHour;
        if (now.get(Calendar.AM_PM) == 1) { // 1 == pm, 0 == am
            currentHour = now.get(Calendar.HOUR) + 12;
        } else {
            currentHour = now.get(Calendar.HOUR);
        }

        ParkingSpotList parkingSpotList = new ParkingSpotList();

        if (json.isJsonObject()) {
            JsonObject sfParkData = json.getAsJsonObject();
            int numRecords = sfParkData.get("NUM_RECORDS").getAsInt();
            if (numRecords > 0) {
                if (!sfParkData.getAsJsonArray("AVL").isJsonNull() && sfParkData.getAsJsonArray("AVL").isJsonArray()) {
                    JsonArray data = sfParkData.getAsJsonArray("AVL");
                    for (int i = 0; i < data.size(); i++) {
                        JsonObject dataObject = data.get(i).getAsJsonObject();
                        ParkingSpot parkingSpot = new ParkingSpot();
                        parkingSpot.setParkingType("");
                        parkingSpot.setRateQualifier("");
                        if (!dataObject.get("TYPE").isJsonNull() &&
                                dataObject.get("TYPE").getAsString().equals("ON")) {
                            parkingSpot.setParkingType("Street Parking");
                            if (!dataObject.get("NAME").isJsonNull()) {
                                parkingSpot.setStreetName(dataObject.get("NAME").getAsString());
                            }

                            if (dataObject.getAsJsonObject("RATES").getAsJsonArray("RS").isJsonArray()) {
                                JsonArray prices = dataObject.getAsJsonObject("RATES").getAsJsonArray("RS");
                                for (int j = 0; j < prices.size(); j++) {
                                    JsonObject pricesObject = prices.get(j).getAsJsonObject();
                                    if (!pricesObject.get("RATE").isJsonNull() &&
                                            !pricesObject.get("RQ").isJsonNull() &&
                                            !pricesObject.get("END").isJsonNull() &&
                                            !pricesObject.get("BEG").isJsonNull()) {
                                        String[] begTime = pricesObject.get("BEG").getAsString().split(" ");
                                        String[] endTime = pricesObject.get("END").getAsString().split(" ");
                                        String[] begHours = begTime[0].split(":");
                                        double begHour;
                                        String[] endHours = endTime[0].split(":");
                                        double endHour;

                                        if (Double.parseDouble(begHours[0]) != 12 && begTime[1].equals("PM")) {
                                            begHour = Double.parseDouble(begHours[0]) + 12;
                                        } else if (Double.parseDouble(begHours[0]) == 12 && begTime[1].equals("AM")) {
                                            begHour = 0;
                                        } else if (Double.parseDouble(begHours[0]) == 12 && begTime[1].equals("PM")) {
                                            begHour = 12;
                                        } else {
                                            begHour = Double.parseDouble(begHours[0]);
                                        }

                                        if (Double.parseDouble(endHours[0]) != 12 && endTime[1].equals("PM")) {
                                            endHour = Double.parseDouble(endHours[0]) + 12;
                                        } else if (Double.parseDouble(endHours[0]) == 12 && endTime[1].equals("AM")) {
                                            endHour = 24;
                                        } else if (Double.parseDouble(endHours[0]) == 12 && endTime[1].equals("PM")) {
                                            endHour = 12;
                                        } else {
                                            endHour = Double.parseDouble(endHours[0]);
                                        }

                                        if (begHour <= currentHour && currentHour <= endHour) {
                                            parkingSpot.setRate(Double.parseDouble(pricesObject.get("RATE").getAsString()));
                                            parkingSpot.setRateQualifier(pricesObject.get("RQ").getAsString());
                                            parkingSpot.setEndTime(pricesObject.get("END").getAsString());
                                        }
                                    }
                                }
                            } else {
                                Log.e("ParkingConverter", "No array");
                            }

                            if (!dataObject.get("LOC").isJsonNull()) {
                                String location = dataObject.get("LOC").getAsString();
                                String[] parts = location.split(",");
                                if (parts.length == 4) {
                                    parkingSpot.setStartLongitude(Double.parseDouble(parts[0]));
                                    parkingSpot.setStartLatitude(Double.parseDouble(parts[1]));
                                    parkingSpot.setEndLongitude(Double.parseDouble(parts[2]));
                                    parkingSpot.setEndLatitude(Double.parseDouble(parts[3]));
                                }
                            } else {
                                Log.e("ParkingConverter", "No parking spots");
                            }
                        }

                        if (parkingSpot.getParkingType().equals("Street Parking")) {
                            parkingSpotList.addParkingSpot(parkingSpot);
                        }
                    }
                }
            } else {
                return parkingSpotList;
            }

        } else {
            Log.e(TAG, "No Data");
        }

        return parkingSpotList;
    }
}