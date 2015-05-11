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

    String counter = "empty";
    int numOfSavedLocations;
    double latitude, longitude;
    TextView latitudeTxt, longitudeTxt, timeTxt;
    ParkingLocationDatabase dbParkingLocation;
    ListView parkingListView;
    List<ParkingLocation> ParkingLocations = new ArrayList<ParkingLocation>();

    Button history_save, history_delete;
    Toolbar mToolbar;

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
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Creates a toolbar
        /*Toolbar*/ mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        history_save  = (Button) findViewById(R.id.history_save);
        history_delete  = (Button) findViewById(R.id.history_delete);

        checkColorTheme();
        //checkCounter();

        // Passes the latitude and longitude values from the Main Activity
        Intent intent = getIntent();
        if (intent != null) {
            longitude = intent.getDoubleExtra("longitude", 0);
            latitude = intent.getDoubleExtra("latitude", 0);
            latitudeTxt = (TextView) findViewById(R.id.history_park_latitude);
            longitudeTxt = (TextView) findViewById(R.id.history_park_longitude);
            timeTxt = (TextView) findViewById(R.id.history_park_time);
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
                    numOfSavedLocations++;
                    //dbParkingLocation = new ParkingLocationDatabase(getApplicationContext());
                    //dbParkingLocation.changeDatabaseCounter("notEmpty");

                }
            });

            // Deletes the parking history data
            final Button deleteBtn = (Button) findViewById(R.id.history_delete);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //checkCounter();
                    if (/*counter.equalsIgnoreCase("notEmpty")*/numOfSavedLocations>0) {
                        dbParkingLocation.deleteParkingHistory();
                        Toast.makeText(getApplicationContext(), "Your Parking History has been deleted.", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                        numOfSavedLocations = 0;
                        //dbParkingLocation = new ParkingLocationDatabase(getApplicationContext());
                        //dbParkingLocation.changeDatabaseCounter("empty");
                    } else {
                        Toast.makeText(getApplicationContext(), "There is nothing in your Parking History.", Toast.LENGTH_SHORT).show();
                    }
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

        latitudeTxt.setText(String.valueOf(latitude));
        longitudeTxt.setText(String.valueOf(longitude));
        timeTxt.setText(getDateTime());
    }



    /**
     * Accumulates the locations in the list
     */
    private void populateList() {
        ArrayAdapter<ParkingLocation> adapter = new ParkingListAdapter();
        parkingListView.setAdapter(adapter);
    }



    /**
     * Displays the accumulated locations in the list
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
     * @param //menu
     * @return
     */
    //@Override
    /*public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_history, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_history, menu);
        return true;
    }*/



    /**
     * Responds to the actions in the menu
     *
     * @param //item
     * @return
     */
    //@Override
    /*public boolean onOptionsItemSelected(MenuItem item) {

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
    }*/


    /**
     * Checks the current counter value
     */
    public void checkCounter() {
        dbParkingLocation = new ParkingLocationDatabase(getApplicationContext());
        counter = dbParkingLocation.checkDatabaseCounter();
    }


    /**
     * Changes the color theme of the toolbar and buttons on the Parking History page
     */
    public void checkColorTheme() {
        history_save.setTextColor(getResources().getColor(R.color.bright_snow));
        history_delete.setTextColor(getResources().getColor(R.color.bright_snow));

        if (MainActivity.theme.equalsIgnoreCase("default")) {
            mToolbar.setBackgroundColor(getResources().getColor(R.color.default_grey));
        } else if (MainActivity.theme.equalsIgnoreCase("beach")) {
            history_save.setBackgroundColor(getResources().getColor(R.color.beach_orange));
            history_delete.setBackgroundColor(getResources().getColor(R.color.beach_orange));
            mToolbar.setBackgroundColor(getResources().getColor(R.color.beach_blue));
        } else if (MainActivity.theme.equalsIgnoreCase("garden")) {
            history_save.setBackgroundColor(getResources().getColor(R.color.garden_foilage));
            history_delete.setBackgroundColor(getResources().getColor(R.color.garden_foilage));
            mToolbar.setBackgroundColor(getResources().getColor(R.color.garden_foilage));
        } else if (MainActivity.theme.equalsIgnoreCase("rose")) {
            history_save.setBackgroundColor(getResources().getColor(R.color.red_rose));
            history_delete.setBackgroundColor(getResources().getColor(R.color.red_rose));
            mToolbar.setBackgroundColor(getResources().getColor(R.color.red_rose));
        } else if (MainActivity.theme.equalsIgnoreCase("ice")) {
            history_save.setBackgroundColor(getResources().getColor(R.color.ice_blue));
            history_delete.setBackgroundColor(getResources().getColor(R.color.ice_blue));
            mToolbar.setBackgroundColor(getResources().getColor(R.color.ice_blue));
        } else if (MainActivity.theme.equalsIgnoreCase("desert")) {
            history_save.setBackgroundColor(getResources().getColor(R.color.desert_yellow));
            history_delete.setBackgroundColor(getResources().getColor(R.color.desert_yellow));
            mToolbar.setBackgroundColor(getResources().getColor(R.color.desert_yellow));
        }  else if (MainActivity.theme.equalsIgnoreCase("royal")) {
            history_save.setBackgroundColor(getResources().getColor(R.color.royal_purple));
            history_delete.setBackgroundColor(getResources().getColor(R.color.royal_purple));
            mToolbar.setBackgroundColor(getResources().getColor(R.color.royal_purple));
        } else if (MainActivity.theme.equalsIgnoreCase("snow")) {
            history_save.setBackgroundColor(getResources().getColor(R.color.snow_white));
            history_delete.setBackgroundColor(getResources().getColor(R.color.snow_white));
            mToolbar.setBackgroundColor(getResources().getColor(R.color.snow_white));
            history_save.setTextColor(getResources().getColor(R.color.default_grey));
            history_delete.setTextColor(getResources().getColor(R.color.default_grey));
        }
    }
}