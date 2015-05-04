package com.alejandrolai.sfpark;

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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends ActionBarActivity
        implements LocationListener {

    private static String theme = "beach";

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private static final String TAG = MainActivity.class.getSimpleName();

    Location location;
    double currentLatitude = 0;
    double currentLongitude = 0;

    Button parkMebutton;

    AlertDialogs dialog = AlertDialogs.getInstance();

    ParkingSpotList mParkingSpotList;

    static ParkingSpot currentSpot;

    // Added By Ihsan Taha on 5/3/15
    static double latitudeForParking;
    static double longitudeForParking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new TermsOfService(this).show();

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        parkMebutton = (Button) findViewById(R.id.parkMebutton);

        setUpMapIfNeeded();

        if (isOnline()) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAltitudeRequired(true);
            String bestProvider = locationManager.getBestProvider(criteria, true);
            location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                onLocationChanged(location);
            } else {
                Toast.makeText(this, "Location could not be determined. Turn on location services", Toast.LENGTH_SHORT).show();
                dialog.showLocationSettingsAlert(this);

            }
            locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
        } else {
            dialog.showInternetAlert(this);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng;
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        if (currentLatitude != 0 && currentLongitude != 0) {
            latLng = new LatLng(currentLatitude, currentLongitude);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)
                    .zoom(15)
                    .build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }


    public double getLatitude() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        return this.location.getLatitude();

    }



    /**
     * Added By Ihsan Taha on 5/3/15
     *
     * Adds the current latitude to help save the current parking location
     * in LocationDatabaseActivity's "Use Current Location" button;
     */
    public void setLatitudeForParking() {
        latitudeForParking = getLatitude();
    }



    /**
     * Added By Ihsan Taha on 5/3/15
     *
     * Adds the current longitude to help save the current parking location
     * in LocationDatabaseActivity's "Use Current Location" button;
     */
    public void setLongitudeForParking() {
        longitudeForParking = getLongitude();
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

        // Added By Ihsan Taha on 5/3/15
        setLatitudeForParking();
        setLongitudeForParking();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
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
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.setMyLocationEnabled(true);
    }

    /**
     * Check if there internet connection
     *
     * @return true if there is a connection to the internet
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     *
     * @param startLatitude starting latitude of block
     * @param startLongitude starting longitude of block
     */
    private void addMarker(String streetName,/* double rate, String rateQual,
                           String endTime,*/ double startLatitude, double startLongitude) {
        /*
        if (rateQual.equals("Per hour")) {
            mMap.addMarker(new MarkerOptions()

                    .position(new LatLng(startLatitude, startLongitude))
                    .draggable(true)
                    .title(streetName)
                    .snippet("$" + rate + " " + rateQual + " until " + endTime));
        } else {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(startLatitude, startLongitude))
                    .draggable(true)
                    .title(streetName)
                    .snippet(rateQual + " until " + endTime));
        }
        */

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(startLatitude, startLongitude))
                .draggable(true)
                .title(streetName));

    }

    /**
     * Adds a line to the map
     *
     * @param startLatLng start location of block
     * @param endLatLng   end location of block
     */
    private void addLine(LatLng startLatLng, LatLng endLatLng/*, double rate*/) {
        int color = getResources().getColor(R.color.black);
        /*
        if (rate <= 1) {
            color = getResources().getColor(R.color.green_500);
        } else if (rate > 1 && rate <= 2) {
            color = getResources().getColor(R.color.yellow_500);
        } else if (rate > 2) {
            color = getResources().getColor(R.color.red_500);
        }
        */
        mMap.addPolyline(new PolylineOptions().geodesic(true)
                .color(color)
                .add(startLatLng)
                .add(endLatLng));
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

        switch (item.getItemId()) {

            case R.id.action_settings:
                if (theme.equalsIgnoreCase("beach")) {
                    Intent intent = new Intent(this, SettingsActivity.class);
                    startActivity(intent);
                    return true;
                } else if (theme.equalsIgnoreCase("garden")) {
                    Intent intent = new Intent(this, SettingsActivity2.class);
                    startActivity(intent);
                    return true;
                } else if (theme.equalsIgnoreCase("lady")) {
                    Intent intent = new Intent(this, SettingsActivity3.class);
                    startActivity(intent);
                    return true;
                }

            case R.id.action_search:
                getData();
                return true;
            case R.id.action_history:
                startActivity(new Intent(this, LocationDatabaseActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void sendToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goToStoreLocation(View view) {
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
        //StoreLocation SL = new StoreLocation(context);
    }

    /**
     * This method is used for the Park Me button, when called, opens up the
     * reminder Activity
     *
     * @param view button view
     */
    public void goToReminder(View view) {

        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, ReminderActivity.class);
        startActivity(intent);
    }


    /**
     * Added by Dolly 4/27/15
     *
     * @param numberReturn - number of  nearest spots wanted (in our case pass 5)
     * @param listOfSpots  - list of Parking spots (in our case the whole sfpark list)
     */
    public void getNearestParkingSpots(int numberReturn, ParkingSpotList listOfSpots) {

        // the list that  will contain numberReturn nearest spots (in our case 5 spots)
        ParkingSpotList nearestParkingSpots = new ParkingSpotList();
        LatLng current_loc = new LatLng(this.getLatitude(), this.getLongitude());
        //temporary list
        ParkingSpot[] copy_parkings = new ParkingSpot[listOfSpots.getListSize()];

        // check if there is enough spots in the list compared to the number of nearest spots to be returned
        if (numberReturn <= listOfSpots.getListSize()) {

            //copy into temporary list for sorting
            for (int i = 0; i < listOfSpots.getListSize(); i++) {
                copy_parkings[i] = listOfSpots.getSpot(i);
            }

            // bubble sorting the list in ascending order
            boolean swapped = true;
            int j = 0;
            ParkingSpot tmp;

            while (swapped) {
                swapped = false;
                j++;
                for (int i = 0; i < copy_parkings.length - j; i++) {

                    if (copy_parkings[i].computeDistanceFrom(current_loc) >
                            copy_parkings[i + 1].computeDistanceFrom(current_loc)) {
                        tmp = copy_parkings[i];
                        copy_parkings[i] = copy_parkings[i + 1];
                        copy_parkings[i + 1] = tmp;
                        swapped = true;
                    }
                }
            }
            //copy the first numberReturn ( first 5) spots

            for (int i = 0; i < numberReturn; i++) {
                nearestParkingSpots.addParkingSpot(copy_parkings[i]);
            }

        } else {

            //add error message that  the list doesn't contain enough spots to return
        }
        markNearSpots(nearestParkingSpots);
    }

    /**
     * A method that iterates add marker method and pass the coordinates from the nearestparkingspot list
     *
     * @param nearSpots
     */
    public void markNearSpots(ParkingSpotList nearSpots) {

        ArrayList<ParkingSpot> list = nearSpots.getList();

        for (ParkingSpot parkingSpot : list) {
            String streetName = parkingSpot.getStreetName();
            double startLatitude = parkingSpot.getStartLatitude();
            double startLongitude = parkingSpot.getStartLongitude();
            double endLatitude = parkingSpot.getEndLatitude();
            double endLongitude = parkingSpot.getEndLongitude();
            LatLng startLatLng = new LatLng(startLatitude, startLongitude);
            LatLng endLatLng = new LatLng(endLatitude,endLongitude);
            /*
            double rate = parkingSpot.getRate();
            String endTime = parkingSpot.getEndTime();
            String rateQual = parkingSpot.getRateQualifier();

            Log.i("Locations: ", startLatLng.toString() + " - " + endLatLng.toString());
            */
            addLine(startLatLng,endLatLng/*, rate*/);
            addMarker(streetName,/*rate, rateQual, endTime,*/ startLatitude, startLongitude);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(startLatLng)
                    .zoom(15)
                    .build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

    }

    public void getData() {

        if (isOnline()) {
            Toast.makeText(this, getString(R.string.retrieving_data), Toast.LENGTH_SHORT).show();
            // Call getParkingSpots() and test connection to Sfpark,
            // on succcess retrieve
            Service.getService().getParkingSpots(new Callback<ParkingSpotList>() {
                @Override
                public void success(ParkingSpotList parkingSpotList, Response response) {
                    try {
                        retrieveData(parkingSpotList);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, error.getLocalizedMessage());
                    try {
                        retrieveData(null);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void retrieveData(final ParkingSpotList parkingSpotList)
            throws InterruptedException, ExecutionException, TimeoutException {

        final int numOfParkingSpots = 10;
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
                    getNearestParkingSpots(numOfParkingSpots,mParkingSpotList);

                }
            };
            task.execute();
            task.get(4000, TimeUnit.MILLISECONDS);
        } else {
            Toast.makeText(this, "error connecting", Toast.LENGTH_SHORT).show();
        }
    }

    public static void setTheme(String theTheme) {
        theme = theTheme;
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            dialog.showLocationSettingsAlert(this);
        } else if (!isOnline()){
            dialog.showInternetAlert(this);
        }

    }

    public static String getCurrentTheme() {
        return theme;
    }

}