package com.alejandrolai.sfpark.database;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alejandrolai.sfpark.ParkedLocation;
import com.alejandrolai.sfpark.R;


/**
 * Created by ihsan_taha on 4/30/15.
 */
public class LocationDatabaseActivity extends ActionBarActivity {

    // Data members

    TextView idView;
    EditText xBox;
    EditText yBox;



    // Data methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_parked_location);

        idView = (TextView) findViewById(R.id.productID);
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

        float xlocation = Float.parseFloat(xBox.getText().toString());

        float ylocation = Float.parseFloat(yBox.getText().toString());

        ParkedLocation parkedlocation = new ParkedLocation(xlocation, ylocation);

        dbHandler.addParkedLocation(parkedlocation);

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

        ParkedLocation parkedlocation = dbHandler.findLocation(Float.parseFloat(xBox.getText().toString()));

        if (parkedlocation != null) {

            idView.setText(String.valueOf(parkedlocation.getID()));

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

        boolean result = dbHandler.deleteParkedLocation(Float.parseFloat(xBox.getText().toString()));

        if (result) {

            idView.setText("Record Deleted");
            xBox.setText("");
            yBox.setText("");

        } else {

            idView.setText("No Match Found");
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
