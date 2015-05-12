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

    public static String theme = "default";

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private static final String TAG = MainActivity.class.getSimpleName();

    Location location;

    double currentLatitude = 0;
    double currentLongitude = 0;
    AlertDialogs dialog = AlertDialogs.getInstance();
    ParkingSpotList mParkingSpotList;
    SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance();

    ParkingLocationDatabase dbParkingLocation;

    // Added By Ihsan Taha on 5/7/15
    Button parkMebutton, RemindMe;
    Toolbar mToolbar;
    // End of Addition

    LinkedHashMap<String, String> map = new LinkedHashMap();

    ArrayList<ParkingSpot> list = new ArrayList();

    public static final String RADIUS = "radiusKey";
    public static final String UNIT = "unitKey";
    public static final String FIRST_BOOT = "firstBootKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String firstboot = sharedPreferencesHelper.readFromPreferences(this, FIRST_BOOT, "true");

        /*final Button parkMebutton = (Button) findViewById(R.id.parkMebutton);*/ // Edited By Ihsan Taha on 5/7/15
        parkMebutton = (Button) findViewById(R.id.parkMebutton);
        RemindMe = (Button) findViewById(R.id.RemindMe);

        parkMebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToParkHistory();
            }
        });

        new TermsOfService(this).show();

        /*Toolbar*/
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        setUpMapIfNeeded();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String[] location = extras.getString("location").split(",");
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1])))
                    .title("You parked here " + location[2]));
        } else if (isOnline()) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double latitude = getLatitude();
                double longitude = getLongitude();

                zoomToMap(latitude, longitude);
                addCircle(latitude, longitude);
                getResponse(latitude, longitude);
                if (firstboot.equals("true")) {
                    dialog.showNoLocationsFoundDialog(this);
                    sharedPreferencesHelper.saveToPreferences(this, FIRST_BOOT, "false");
                }
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

    private void addCircle(double latitude, double longitude) {
        double radius = Double.parseDouble(sharedPreferencesHelper.readFromPreferences(this, RADIUS, "0.25"));
        mMap.addCircle(new CircleOptions()
                .center(new LatLng(latitude, longitude))
                .radius(radius * 1609.34) // miles to meters
                .strokeColor(Color.GREEN)
                .fillColor(Color.argb(50, 100, 100, 100)));
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
    }


    public double getLatitude() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        return this.location.getLatitude();
    }


    public double getLongitude() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        return this.location.getLongitude();
    }


    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
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

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

        // Added by Ihsan on 5/7/15
        checkColorTheme();
    }

    private void zoomToMap(double latitude, double longitude) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(14)
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
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera.
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
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        .position(latLng)
                        .draggable(true))
                        .setTitle("You are here: " + latLng.toString());
                currentLatitude = latLng.latitude;
                currentLongitude = latLng.longitude;
                addCircle(currentLatitude, currentLongitude);
                getResponse(currentLatitude, currentLongitude);
            }
        });
    }

    /**
     * Check if there is internet connection
     *
     * @return true if there is a connection to the internet, false otherwise
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_about, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;

        switch (item.getItemId()) {
            case R.id.action_back:
                return true;
            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_refresh:
                getResponse(getLongitude(), getLongitude());
                return true;
            case R.id.action_history:
                intent = new Intent(this, ParkingLocationActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void goToStoreLocation(View view) {
        //StoreLocation SL = new StoreLocation(context);
    }

    /**
     * This method is used for the Park Me button, when called, opens up the
     * reminder Activity
     *
     * @param view button view
     */
    public void goToReminder(View view) {

        Intent intent = new Intent(this, ReminderActivity.class);
        startActivity(intent);
    }

    public void goToParkHistory() {

        Intent intent = new Intent(this, ParkingLocationActivity.class);
        double latitude = currentLatitude;
        double longitude = currentLongitude;
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        startActivity(intent);
    }


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
     * Adds a marker to the map with information about the prices
     *
     * @param streetName Name of the block
     * @param rate       Price per hour
     * @param rateQual   Per hour, street sweep or no charge
     * @param endTime    End time of the current time bracket
     * @param position   Position of marker
     */
    private void addMarkerWithInfo(String streetName, double rate, String rateQual,
                                   String endTime, LatLng position) {

        if (rateQual.equals("Per hour")) {
            mMap.addMarker(new MarkerOptions()
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
                    .position(position)
                    .draggable(true)
                    .title(streetName)
                    .snippet("$" + rate + " " + rateQual + " until " + endTime));
        } else {
            mMap.addMarker(new MarkerOptions()
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
                    .position(position)
                    .draggable(true)
                    .title(streetName)
                    .snippet(rateQual + " until " + endTime));
        }
    }

    /**
     * Adds colored polyline to the map according to its rate
     *
     * @param startLatLng Starting latitude and longitude of the block
     * @param endLatLng   Ending latitude and longitude of the block
     * @param rate        Rate of the block
     */
    private void addColoredLine(LatLng startLatLng, LatLng endLatLng, double rate) {
        int color = getResources().getColor(R.color.black);

        if (rate <= 1) {
            color = getResources().getColor(R.color.green_500);
        } else if (rate > 1 && rate <= 2) {
            color = getResources().getColor(R.color.yellow_500);
        } else if (rate > 2) {
            color = getResources().getColor(R.color.red_500);
        }
        mMap.addPolyline(new PolylineOptions().geodesic(true)
                .color(color)
                .add(startLatLng)
                .add(endLatLng));
    }

    /**
     * Uses a callback to check the response from SF Park
     */
    public void getResponse(double newLatitude, double newLongitude) {

        String newRadius = sharedPreferencesHelper.readFromPreferences(this, RADIUS, ".25");
        String newUOM = sharedPreferencesHelper.readFromPreferences(this, UNIT, "mile");

        map.put("lat", Double.toString(newLatitude));
        map.put("long", Double.toString(newLongitude));
        map.put("radius", newRadius);
        map.put("uom", newUOM);
        map.put("response", "json");
        map.put("pricing", "yes");

        if (isOnline()) {
            Toast.makeText(this, getString(R.string.retrieving_data), Toast.LENGTH_SHORT).show();
            // Call getParkingSpots() and test connection to Sfpark,
            // on success retrieve
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


    @Override
    protected void onRestart() {
        super.onRestart();

        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            dialog.showLocationSettingsAlert(this);
        } else if (!isOnline()) {
            dialog.showInternetAlert(this);
        }
    }



    public static String getCurrentTheme() {
        return theme;
    }



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