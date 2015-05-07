package com.alejandrolai.sfpark;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alejandrolai.sfpark.data.ParkedLocation;
import com.alejandrolai.sfpark.data.ParkingSpot;
import com.alejandrolai.sfpark.database.MyLocationDBHandler;

/**
 * Created by ihsan_taha on 4/30/15.
 */
public class LocationDatabaseActivity extends ActionBarActivity {

    TextView idView;
   // EditText locBox;
   TextView xBox;
    TextView yBox;

    double longitude;
    double latitude;

    ParkingSpot currentSpot;

    /**
     *
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_parked_location);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Added by Alejandro.
        Intent intent = getIntent();
        if (intent != null) {
            longitude = intent.getDoubleExtra("longitude", 0);
            latitude = intent.getDoubleExtra("latitude",0);

        }

        idView = (TextView) findViewById(R.id.productID);
        //locBox = (EditText) findViewById(R.id.input_location_id);
       xBox = (TextView) findViewById(R.id.input_x_location);
       yBox = (TextView) findViewById(R.id.input_y_location);


    }



    /**
     * Adds a new parked location when the location id, x, and y coordinates are entered,
     * and the add location button is pressed.
     *
     * @param view
     */
    public void newParkedLocation (View view) {

        MyLocationDBHandler dbHandler = new MyLocationDBHandler(this, null, null, 1);

      //  float locationid = Float.parseFloat(locBox.getText().toString());

        float xlocation = Float.parseFloat(xBox.getText().toString());

        float ylocation = Float.parseFloat(yBox.getText().toString());

       // ParkedLocation parkedlocation = new ParkedLocation(locationid, xlocation, ylocation);

        ParkedLocation parkedlocation = new ParkedLocation( xlocation, ylocation);

        dbHandler.addParkedLocation(parkedlocation);

        //locBox.setText("");
        xBox.setText("");
        yBox.setText("");

    }



    /**
     * Searches for a parked location when an x-coordinate is entered
     * and the get location button is pressed.
     *
     * @param view
     */
   public void lookupParkedLocation (View view) {

        MyLocationDBHandler dbHandler = new MyLocationDBHandler(this, null, null, 1);

        ParkedLocation parkedlocation = dbHandler.findLocation();

       Toast.makeText(getApplicationContext(), "Retrieving your data .",
               Toast.LENGTH_SHORT).show();


        if (parkedlocation != null) {

            idView.setText(String.valueOf(parkedlocation.getID()));

          //  locBox.setText(String.valueOf(parkedlocation.getLocationID()));

            xBox.setText(String.valueOf(parkedlocation.getXLocation()));

            yBox.setText(String.valueOf(parkedlocation.getYLocation()));

            Toast.makeText(getApplicationContext(), "Parking spot not null.",
                    Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(getApplicationContext(), "Parking spot is null.",
                    Toast.LENGTH_SHORT).show();

            idView.setText("No Match Found");

        }

    }



    /**
     * Removes a saved parked location when both of the coordinates are entered
     * and the delete location button is pressed.
     *
     * @param view
     */
   public void removeParkedLocation (View view) {

        MyLocationDBHandler dbHandler = new MyLocationDBHandler(this, null, null, 1);

        boolean result = dbHandler.deleteParkedLocation();

        if (result) {

            idView.setText("Record Deleted");
            //locBox.setText("");
            xBox.setText("");
            yBox.setText("");

        } else {

            idView.setText("No Match Found");
        }

    }


    /**
     * Automatically inserts current location into the x and y fields.
     */
    public void useCurrentLocation(View view) {


        Toast.makeText(getApplicationContext(), "Your parking spot has been saved.",
                Toast.LENGTH_SHORT).show();

        Intent intent = getIntent();
        if (intent != null) {
            longitude = intent.getDoubleExtra("longitude", 0);
            latitude = intent.getDoubleExtra("latitude",0);

        }

        double xloc = latitude;
        double yloc = longitude;
        float xlo = (float) xloc;
        float ylo = (float) yloc;

        //float xlo = 35;
        //float ylo = 40;

        MyLocationDBHandler dbHandler = new MyLocationDBHandler(this, null, null, 1);

        ParkedLocation parkedlocation = new ParkedLocation(xlo, ylo);

        dbHandler.addParkedLocation(parkedlocation);


      //  xBox.setText(String.valueOf(xlo));
      //yBox.setText(String.valueOf(ylo));

    }

}
