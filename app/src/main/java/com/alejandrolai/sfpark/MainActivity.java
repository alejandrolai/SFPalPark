package com.alejandrolai.sfpark;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import com.alejandrolai.sfpark.Timer.ReminderActivity;
import com.alejandrolai.sfpark.data.ParkingSpot;
import com.alejandrolai.sfpark.data.ParkingSpotList;
import com.alejandrolai.sfpark.data.Service;
import com.alejandrolai.sfpark.database.ParkingLocationDatabase;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends ActionBarActivity
        implements LocationListener {

    // Data fields

    Toolbar mToolbar;
    Button parkMebutton, RemindMe;
    ParkingLocationDatabase dbParkingLocation;
    Location location;

    double currentLatitude = 0;
    double currentLongitude = 0;

    public static String theme = "default";
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private static final String TAG = MainActivity.class.getSimpleName();

    ParkingSpotList mParkingSpotList;
    AlertDialogs dialog = AlertDialogs.getInstance();
    SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance();
    LinkedHashMap<String, String> map = new LinkedHashMap();
    ArrayList<ParkingSpot> list = new ArrayList();


    /**
     * Adds functional visual elements to the main Activity upon its creation
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Displays the main Activity layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String firstboot = sharedPreferencesHelper.readStringsFromPreferences(this, SharedPreferencesHelper.FIRST_BOOT, "true");
        if (firstboot.equals("true")) {
            dialog.showNoLocationsFoundDialog(this);
            sharedPreferencesHelper.saveStringToPreferences(this, SharedPreferencesHelper.FIRST_BOOT, "false");
        }

        // Displays the Terms of Service (if this is the first time the app has been opened since its installation)
        new TermsOfService(this).show();

        // Displays the toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Displays the "Park Me!" and "Reminder!" buttons
        parkMebutton = (Button) findViewById(R.id.parkMebutton);
        RemindMe = (Button) findViewById(R.id.RemindMe);

        // Calls the goToParkHistory function when the "Park Me!" button isclicked
        parkMebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToParkHistory();
            }
        });

        // Calls the goToReminder function when the "Reminder" button is clicked
        RemindMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToReminder();
            }
        });

        // Calls the setUpMapIfNeeded function
        setUpMapIfNeeded();

        // Gets the required materials to give functionality to the map (latitude, longitude, zoom level, radius, and location updates)
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String[] location = extras.getString("location").split(",");
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1])))
                    .title("You parked here " + location[2]));
            zoomToMap(Double.parseDouble(location[0]), Double.parseDouble(location[1]),16);
        } else if (isOnline()) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double latitude = getLatitude();
                double longitude = getLongitude();

                zoomToMap(latitude, longitude, 14);
                addCircle(latitude, longitude);
                getResponse(latitude, longitude);

            } else {
                dialog.showLocationSettingsAlert(this);
            }

            Criteria criteria = new Criteria();
            criteria.setAltitudeRequired(true);
            String bestProvider = locationManager.getBestProvider(criteria, true);
            locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
        } else {
            dialog.showInternetAlert(this);
        }

    }

    /**
     * Displays the radius surrounding the marker's current location
     *
     * @param latitude
     * @param longitude
     */
    private void addCircle(double latitude, double longitude) {
        double radius = Double.parseDouble(sharedPreferencesHelper.readStringsFromPreferences(this, SharedPreferencesHelper.RADIUS, "0.25"));
        mMap.addCircle(new CircleOptions()
                .center(new LatLng(latitude, longitude))
                .radius(radius * 1609.34) // miles to meters
                .strokeColor(Color.GREEN)
                .fillColor(Color.argb(50, 100, 100, 100)));
    }

    /**
     * Updates the location
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
    }


    /**
     *  Gets the current location's latitude
     * @return latitude
     */
    public double getLatitude() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        return this.location.getLatitude();
    }


    /**
     * Gets the current location's longitude
     * @return longitude
     */
    public double getLongitude() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        return this.location.getLongitude();
    }



    /**
     * Handles service provider issues
     *
     * @param provider
     */
    @Override
    public void onProviderDisabled(String provider) {
        dialog.showLocationSettingsAlert(this);
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }


    /**
     * Sets up the map if required, and updates the color theme based on the theme's current value
     */
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        checkColorTheme();
    }


    /**
     * Zooms the map on to the current location's latitude, longitude
     *
     * @param latitude
     * @param longitude
     * @param zoomLevel
     */
    private void zoomToMap(double latitude, double longitude, int zoomLevel) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(zoomLevel)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }



    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Does a null check to confirm that the user has not already instantiated the map.
        if (mMap == null) {
            // Tries to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Checks if the user was successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }



    /**
     * Allows the user to add markers or lines, add listeners or move the camera.
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .draggable(true))
                        .setTitle("You are here: " + latLng.toString());
                currentLatitude = latLng.latitude;
                currentLongitude = latLng.longitude;
                zoomToMap(currentLatitude,currentLongitude,16);
                addCircle(currentLatitude, currentLongitude);
                getResponse(currentLatitude, currentLongitude);
            }
        });
    }


    /**
     * Checks if there is internet connection
     * @return true if there is a connection to the internet, false otherwise
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    /**
     * Inflates the menu in the main Activity
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }



    /**
     * Handles user interaction with the actions in the menu
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;

        switch (item.getItemId()) {
            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_history:
                intent = new Intent(this, ParkingLocationActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    /**
     * Sends the user to the Reminder Activity
     */
    public void goToReminder() {
        Intent intent = new Intent(this, ReminderActivity.class);
        startActivity(intent);
    }



    /**
     * Sends the user to the Parking History Activity, and passes the current location's latitude and longitude values
     */
    public void goToParkHistory() {
        Intent intent = new Intent(this, ParkingLocationActivity.class);
        double latitude = currentLatitude;
        double longitude = currentLongitude;
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        startActivity(intent);
    }



    /**
     * Adds information to the list of nearest parking spots
     *
     * @param nearSpots
     */
    public void markNearSpots(ParkingSpotList nearSpots) {


        list = nearSpots.getList();

        if (list.size() > 0) {
            for (ParkingSpot parkingSpot : list) {
                String streetName = parkingSpot.getStreetName();
                double startLatitude = parkingSpot.getStartLatitude();
                double startLongitude = parkingSpot.getStartLongitude();
                double endLatitude = parkingSpot.getEndLatitude();
                double endLongitude = parkingSpot.getEndLongitude();
                LatLng startLatLng = new LatLng(startLatitude, startLongitude);
                LatLng endLatLng = new LatLng(endLatitude, endLongitude);
                if (!parkingSpot.getRateQualifier().equals("")) {
                    double rate = parkingSpot.getRate();
                    String endTime = parkingSpot.getEndTime();
                    String rateQual = parkingSpot.getRateQualifier();
                    addColoredLine(startLatLng, endLatLng, rate);
                    addMarkerWithInfo(streetName, rate, rateQual, endTime, startLatLng);
                }
            }
        } else {
            Toast.makeText(this, "No parking spots within your selected radius. Long tap in a different location", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Adds a marker to the map with information about the street name, price, parking type, parking time availability, and location
     * @param streetName Name of the block
     * @param rate Price per hour
     * @param rateQual Per hour, street sweep or no charge
     * @param endTime End time of the current time bracket
     * @param position Position of marker
     */
    private void addMarkerWithInfo(String streetName, double rate, String rateQual,
                                   String endTime, LatLng position) {

        if (rateQual.equals("Per hour")) {
            mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
                    .position(position)
                    .draggable(true)
                    .title(streetName)
                    .snippet("$" + rate + " " + rateQual + " until " + endTime));
        } else {
            mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
                    .position(position)
                    .draggable(true)
                    .title(streetName)
                    .snippet(rateQual + " until " + endTime));
        }
    }


    /**
     * Adds colored polyline to the map according to its rate
     * @param startLatLng Starting latitude and longitude of the block
     * @param endLatLng Ending latitude and longitude of the block
     * @param rate Rate of the block
     */
    private void addColoredLine(LatLng startLatLng, LatLng endLatLng, double rate) {
        int color;
        if (rate <= 1) {
            color = sharedPreferencesHelper.readIntFromPreferences(this,SharedPreferencesHelper.GOOD_COLOR,getResources().getColor(R.color.green_700));
        } else if (rate > 1 && rate <= 2) {
            color = sharedPreferencesHelper.readIntFromPreferences(this,SharedPreferencesHelper.OK_COLOR,getResources().getColor(R.color.yellow_800));
        } else {
            color = sharedPreferencesHelper.readIntFromPreferences(this, SharedPreferencesHelper.BAD_COLOR, getResources().getColor(R.color.black));
        }
        mMap.addPolyline(new PolylineOptions().geodesic(true)
                .color(color)
                .add(startLatLng)
                .add(endLatLng));
    }


    /**
     * Method that uses a callback to check the sfpark connection
     * @param newLatitude latitude of requested spot
     * @param newLongitude longitude of requested spot
     */
    public void getResponse(double newLatitude, double newLongitude) {

        String newRadius = sharedPreferencesHelper.readStringsFromPreferences(this, SharedPreferencesHelper.RADIUS, "0.25");
        String newUOM = sharedPreferencesHelper.readStringsFromPreferences(this, SharedPreferencesHelper.UNIT, "mile");

        map.put("lat", Double.toString(newLatitude));
        map.put("long", Double.toString(newLongitude));
        map.put("radius", newRadius);
        map.put("uom", newUOM);
        map.put("response", "json");
        map.put("pricing", "yes");

        if (isOnline()) {
            Toast.makeText(this, getString(R.string.retrieving_data), Toast.LENGTH_SHORT).show();
            // Calls getParkingSpots() and tests connection to Sfpark on success retrieve
            Service.getService().getParkingSpots(map, new Callback<ParkingSpotList>() {
                @Override
                public void success(ParkingSpotList parkingSpotList, Response response) {
                    try {
                        retrieveData(parkingSpotList);
                    } catch (InterruptedException | ExecutionException | TimeoutException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, error.getLocalizedMessage());
                    try {
                        retrieveData(null);
                    } catch (InterruptedException | ExecutionException | TimeoutException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Toast.makeText(this, getString(R.string.no_parking_spots_within_radius), Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Contains an asynctask to get all the data from SFPark
     *
     * @param parkingSpotList The list of all parking spots
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    private void retrieveData(final ParkingSpotList parkingSpotList)
            throws InterruptedException, ExecutionException, TimeoutException {

        if (parkingSpotList != null) {
            AsyncTask<Void, Void, ParkingSpotList> task = new AsyncTask<Void, Void, ParkingSpotList>() {
                @Override
                protected ParkingSpotList doInBackground(Void... params) {
                    return parkingSpotList;
                }

                @Override
                protected void onPostExecute(ParkingSpotList parkingSpotList) {
                    super.onPostExecute(parkingSpotList);
                    mParkingSpotList = parkingSpotList;
                    markNearSpots(mParkingSpotList);
                }
            };
            task.execute();
        } else {
            Toast.makeText(this, getString(R.string.problem_communicating_with_sfpark), Toast.LENGTH_SHORT).show();
        }
    }



    /**
     * Gets the current color theme value
     *
     * @return
     */
    public static String getCurrentTheme() {
        return theme;
    }



    /**
     * Sets the current color theme value
     *
     * @param theTheme
     */
    public static void setTheme(String theTheme) {
        theme = theTheme;
    }



    /**
     * Changes the color theme of the toolbar and buttons on the main page
     */
    public void checkColorTheme() {
        dbParkingLocation = new ParkingLocationDatabase(getApplicationContext());
        theme = dbParkingLocation.checkDatabaseTheme();

        parkMebutton.setTextColor(getResources().getColor(R.color.bright_snow));
        RemindMe.setTextColor(getResources().getColor(R.color.bright_snow));

        if (theme.equalsIgnoreCase("default")) {
            parkMebutton.setBackgroundColor(getResources().getColor(R.color.bright_default));
            RemindMe.setBackgroundColor(getResources().getColor(R.color.bright_default));
            mToolbar.setBackgroundColor(getResources().getColor(R.color.default_grey));
        } else if (theme.equalsIgnoreCase("beach")) {
            parkMebutton.setBackgroundColor(getResources().getColor(R.color.beach_orange));
            RemindMe.setBackgroundColor(getResources().getColor(R.color.beach_orange));
            mToolbar.setBackgroundColor(getResources().getColor(R.color.beach_blue));
        } else if (theme.equalsIgnoreCase("garden")) {
            parkMebutton.setBackgroundColor(getResources().getColor(R.color.garden_plant));
            RemindMe.setBackgroundColor(getResources().getColor(R.color.garden_plant));
            mToolbar.setBackgroundColor(getResources().getColor(R.color.garden_foilage));
        } else if (theme.equalsIgnoreCase("rose")) {
            parkMebutton.setBackgroundColor(getResources().getColor(R.color.bright_rose));
            RemindMe.setBackgroundColor(getResources().getColor(R.color.bright_rose));
            mToolbar.setBackgroundColor(getResources().getColor(R.color.red_rose));
        } else if (theme.equalsIgnoreCase("ice")) {
            parkMebutton.setBackgroundColor(getResources().getColor(R.color.bright_ice));
            RemindMe.setBackgroundColor(getResources().getColor(R.color.bright_ice));
            mToolbar.setBackgroundColor(getResources().getColor(R.color.ice_blue));
        } else if (theme.equalsIgnoreCase("desert")) {
            parkMebutton.setBackgroundColor(getResources().getColor(R.color.bright_desert));
            RemindMe.setBackgroundColor(getResources().getColor(R.color.bright_desert));
            mToolbar.setBackgroundColor(getResources().getColor(R.color.desert_yellow));
        } else if (theme.equalsIgnoreCase("royal")) {
            parkMebutton.setBackgroundColor(getResources().getColor(R.color.bright_purple));
            RemindMe.setBackgroundColor(getResources().getColor(R.color.bright_purple));
            mToolbar.setBackgroundColor(getResources().getColor(R.color.royal_purple));
        } else if (theme.equalsIgnoreCase("snow")) {
            parkMebutton.setBackgroundColor(getResources().getColor(R.color.bright_snow));
            RemindMe.setBackgroundColor(getResources().getColor(R.color.bright_snow));
            mToolbar.setBackgroundColor(getResources().getColor(R.color.black));
            parkMebutton.setTextColor(getResources().getColor(R.color.default_grey));
            RemindMe.setTextColor(getResources().getColor(R.color.default_grey));
        }
    }

}