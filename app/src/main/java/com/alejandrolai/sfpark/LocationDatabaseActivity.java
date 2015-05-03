package com.alejandrolai.sfpark;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alejandrolai.sfpark.data.ParkedLocation;
import com.alejandrolai.sfpark.database.MyLocationDBHandler;

/**
 * Created by ihsan_taha on 4/30/15.
 */
public class LocationDatabaseActivity extends ActionBarActivity {

    TextView idView;
    EditText locBox;
    EditText xBox;
    EditText yBox;



    /**
     *
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_parked_location);

        idView = (TextView) findViewById(R.id.productID);
        locBox = (EditText) findViewById(R.id.input_location_id);
        xBox = (EditText) findViewById(R.id.input_x_location);
        yBox = (EditText) findViewById(R.id.input_y_location);


    }



    /**
     * Adds a new parked location when the x and y coordinates are entered,
     * and the add location button is pressed.
     *
     * @param view
     */
    public void newParkedLocation (View view) {

        MyLocationDBHandler dbHandler = new MyLocationDBHandler(this, null, null, 1);

        float locationid = Float.parseFloat(locBox.getText().toString());

        float xlocation = Float.parseFloat(xBox.getText().toString());

        float ylocation = Float.parseFloat(yBox.getText().toString());

        ParkedLocation parkedlocation = new ParkedLocation(locationid, xlocation, ylocation);

        dbHandler.addParkedLocation(parkedlocation);

        locBox.setText("");
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

        ParkedLocation parkedlocation = dbHandler.findLocation(Integer.parseInt(locBox.getText().toString()));

        if (parkedlocation != null) {

            idView.setText(String.valueOf(parkedlocation.getID()));

            locBox.setText(String.valueOf(parkedlocation.getLocationID()));

            xBox.setText(String.valueOf(parkedlocation.getXLocation()));

            yBox.setText(String.valueOf(parkedlocation.getYLocation()));

        } else {

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

        boolean result = dbHandler.deleteParkedLocation(Float.parseFloat(locBox.getText().toString()));

        if (result) {

            idView.setText("Record Deleted");
            locBox.setText("");
            xBox.setText("");
            yBox.setText("");

        } else {

            idView.setText("No Match Found");
        }

    }

}
