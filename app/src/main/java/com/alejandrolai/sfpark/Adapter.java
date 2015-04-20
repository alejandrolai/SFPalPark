package com.alejandrolai.sfpark;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.alejandrolai.sfpark.model.ParkingSpot;

import java.util.ArrayList;

/**
 * Created by Alejandro on 4/8/15.
 */
public class Adapter extends BaseAdapter {

    Context context;
    private ArrayList<ParkingSpot> parkingSpots;

    public Adapter(Context context) {
        this.context = context;
        parkingSpots = new ArrayList<>();
    }
    @Override
    public int getCount() {
        return parkingSpots.size();
    }

    @Override
    public Object getItem(int position) {
        return parkingSpots.get(position);
    }

    @Override
    public long getItemId(int position) {
        return parkingSpots.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_item, parent, false);
        }

        ParkingSpot parkingSpot = (ParkingSpot) getItem(position);

        TextView parkingType = (TextView) convertView.findViewById(R.id.parking_type);
        parkingType.setText(parkingSpot.getParkingType());
        TextView streetName = (TextView) convertView.findViewById(R.id.street_name);
        streetName.setText(parkingSpot.getStreetName());
        Button button1 = (Button) convertView.findViewById(R.id.button);
        button1.setContentDescription(parkingSpot.getLocation());
        return convertView;
    }

    public void setParkingSpots(ArrayList<ParkingSpot> list) {
        this.parkingSpots = list;
        notifyDataSetChanged();
    }
}
