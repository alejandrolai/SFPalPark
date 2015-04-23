package com.alejandrolai.sfpark;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alejandrolai.sfpark.list.ListActivity;
import com.alejandrolai.sfpark.model.ParkingSpotList;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends ActionBarActivity implements LocationListener, GoogleMap.OnMapClickListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private static final String TAG = MainActivity.class.getSimpleName();

    private Button button;

    //private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();


        /*
        ActionBarActivity/Toolbar
        if (mToolbar != null) {
            // Set a Toolbar to act as the ActionBar for this Activity window.
            setSupportActionBar(mToolbar);
            //getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        */

        button = (Button) findViewById(R.id.button);
        button.setEnabled(true);

        if (isOnline()) {
            setUpMapIfNeeded();
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAltitudeRequired(true);
            String bestProvider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                onLocationChanged(location);
            } else {
                Toast.makeText(this, "Location could not be determined. Turn on location services", Toast.LENGTH_SHORT).show();
                showLocationSettingsAlert();
            }

            locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
        } else {
            showInternetAlert();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        TextView locationTv = (TextView) findViewById(R.id.LatLongLocation);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        LatLng latLng = new LatLng(latitude, longitude);
        /*
        mMap.addMarker(new MarkerOptions().position(latLng));

        CameraUpdate
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(7));

        */
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

        // You can customize the marker image using images bundled with
        // your app, or dynamically generated bitmaps.

        addMarker(latLng);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(15)
                .bearing(30)
                .tilt(50)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        locationTv.setText("Latitude:" + latitude + ", Longitude:" + longitude);

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
     * @return true if there is a connection to the internet
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Connect to SFpark
     */
    public void connect() {
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
    }

    /**
     * Retrieve the data from SFPark
     * @param parkingSpotList list of parking spots
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    private void retrieveData(final ParkingSpotList parkingSpotList) throws InterruptedException, ExecutionException, TimeoutException {
        if (parkingSpotList != null) {
            //new Requestor(parkingSpotList).execute();
            Toast.makeText(this,"Requesting data...",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.error_connecting), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Prompt user to change location settings
     */
    public void showLocationSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Setting Dialog Title
        alertDialog.setTitle("Location services not activated");

        //Setting Dialog Message
        alertDialog.setMessage("Activate location services");

        //On Pressing Setting button
        alertDialog.setPositiveButton("settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        //On pressing cancel button
        alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    /**
     *  Show a Dialog to prompt the user to change internet settings
     */
    public void showInternetAlert()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Setting Dialog Title
        alertDialog.setTitle("No connection available");

        //Setting Dialog Message
        alertDialog.setMessage("Turn on wifi or data services");

        //On Pressing Setting button
        alertDialog.setPositiveButton("settings", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
            }
        });

        //On pressing cancel button
        alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void addMarker(LatLng latLng){
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(latLng)
                .title(latLng.toString()));
    }

    public void goToList(View view){
        startActivity(new Intent(this, ListActivity.class));
    }

    @Override
    public void onMapClick(LatLng latLng) {
        addMarker(latLng);
    }


}