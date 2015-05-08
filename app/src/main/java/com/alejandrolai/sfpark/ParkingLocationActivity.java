package com.alejandrolai.sfpark;

import com.alejandrolai.sfpark.data.ParkingLocation;
import com.alejandrolai.sfpark.database.ParkingLocationDatabase;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ihsan Taha and Ami Parikh on 5/6/15.
 */
public class ParkingLocationActivity extends ActionBarActivity {

    // Data fields

    double latitude, longitude;
    EditText latitudeTxt, longitudeTxt, timeTxt;
    ParkingLocationDatabase dbParkingLocation;
    ListView parkingListView;
    List<ParkingLocation> ParkingLocations = new ArrayList<ParkingLocation>();

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }



    // Data methods

    /**
     * Adds visual elements of the Activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Creates the layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Creates a toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Passes the latitude and longitude values from the Main Activity
        Intent intent = getIntent();
        if (intent != null) {
            longitude = intent.getDoubleExtra("longitude", 0);
            latitude = intent.getDoubleExtra("latitude", 0);
            latitudeTxt = (EditText) findViewById(R.id.history_park_latitude);
            longitudeTxt = (EditText) findViewById(R.id.history_park_longitude);
            timeTxt = (EditText) findViewById(R.id.history_park_time);
            parkingListView = (ListView) findViewById(R.id.listView);
            dbParkingLocation = new ParkingLocationDatabase(getApplicationContext());

            // Creates the tabs
            TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
            tabHost.setup();
            TabHost.TabSpec tabSpec;

            tabSpec = tabHost.newTabSpec("Park");
            tabSpec.setContent((R.id.park));
            tabSpec.setIndicator("Park");
            tabHost.addTab(tabSpec);

            tabSpec = tabHost.newTabSpec("History");
            tabSpec.setContent((R.id.history));
            tabSpec.setIndicator("History");
            tabHost.addTab(tabSpec);

            // Adds a button that saves the current time and location
            final Button addBtn = (Button) findViewById(R.id.history_save);
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ParkingLocation parkingLocation = new ParkingLocation(dbParkingLocation.getParkingsCount(), String.valueOf(latitudeTxt.getText()), String.valueOf(longitudeTxt.getText()), String.valueOf(timeTxt.getText()));
                    dbParkingLocation.createParkingLocation(parkingLocation);
                    ParkingLocations.add(parkingLocation);
                    populateList();
                    Toast.makeText(getApplicationContext(), "Location saved to Parking History.", Toast.LENGTH_SHORT).show();
                }
            });

            // Deletes the parking history data
            final Button deleteBtn = (Button) findViewById(R.id.history_delete);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dbParkingLocation.deleteParkingHistory();
                    Toast.makeText(getApplicationContext(), "Your Parking History has been deleted.", Toast.LENGTH_SHORT).show();
                }
            });

            latitudeTxt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    addBtn.setEnabled(!latitudeTxt.getText().toString().trim().isEmpty());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            // Adds and displays the saved locations in a list
            List<ParkingLocation> addableLocations = dbParkingLocation.getAllParkingLocations();
            int locationCount = dbParkingLocation.getParkingsCount();

            for (int i = 0; i < locationCount; i++) {
                ParkingLocations.add(addableLocations.get(i));
            }

            if (!addableLocations.isEmpty()) {
                populateList();
            }
        }

        latitudeTxt.setText("Latitude:      " + String.valueOf(latitude));
        longitudeTxt.setText("Longitude:    " + String.valueOf(longitude));
        timeTxt.setText("Time:      " + getDateTime());
    }



    /**
     *
     */
    private void populateList() {
        ArrayAdapter<ParkingLocation> adapter = new ParkingListAdapter();
        parkingListView.setAdapter(adapter);
    }



    /**
     *
     */
    private class ParkingListAdapter extends ArrayAdapter<ParkingLocation> {

        public ParkingListAdapter() {
            super (ParkingLocationActivity.this, R.layout.listview_history, ParkingLocations);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.listview_history, parent, false);
            }

            ParkingLocation currentParkingLocation = ParkingLocations.get(position);

            TextView lat = (TextView) view.findViewById(R.id.history_latitude);
            lat.setText(currentParkingLocation.getLatitude());

            TextView lon = (TextView) view.findViewById(R.id.history_longitude);
            lon.setText(currentParkingLocation.getLongitude());

            TextView time = (TextView) view.findViewById(R.id.history_time);
            time.setText(currentParkingLocation.getTime());

            return view;
        }

    }



    /**
     * Displays the menu
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_about, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_history, menu);
        return true;
    }



    /**
     * Responds to the actions in the menu
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_back:
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
