package com.alejandrolai.sfpark;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;

import com.google.android.gms.maps.model.PolylineOptions;
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
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alejandrolai.sfpark.list.ListActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends ActionBarActivity implements LocationListener, GoogleMap.OnMapClickListener {


    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private static final String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    Location location;
    double currentLatitude=0;
    double currentLongitude=0;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            // Set a Toolbar to act as the ActionBar for this Activity window.
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setUpMapIfNeeded();

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String location = extras.getString("location_key");
            Toast.makeText(this,"location: " + location,Toast.LENGTH_SHORT).show();
            String[] parts = location.split(",");
            Double longitude = Double.parseDouble(parts[1]);
            Double latitude = Double.parseDouble(parts[0]);
            LatLng latLng = new LatLng(latitude,longitude);
            addMarker(latLng);
        } else {
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
                    showLocationSettingsAlert();
                }
                locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
            } else {
                showInternetAlert();
            }
        }
    }

    @Override
    public void onLocationChanged (Location location){
        LatLng latLng;
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        if (currentLatitude != 0 && currentLongitude != 0){
            latLng = new LatLng(currentLatitude, currentLongitude);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)
                    .zoom(15)
                    .build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }



    }

    @Override
    public void onProviderDisabled (String provider){
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled (String provider){
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged (String provider,int status, Bundle extras){
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
     * Show a Dialog to prompt the user to change internet settings
     */
    public void showInternetAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Setting Dialog Title
        alertDialog.setTitle("No connection available");

        //Setting Dialog Message
        alertDialog.setMessage("Turn on wifi or data services");

        //On Pressing Setting button
        alertDialog.setPositiveButton("settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
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
     * Puts marker in map and zooms in
     * @param latLng LatLng object latitude and longitude coordinates
     */
    private void addMarker(LatLng latLng) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(latLng.toString()));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(15)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void goToList() {
        if (location != null) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            Intent intent = new Intent(this, ListActivity.class);
            intent.putExtra("latitude_key",location.getLatitude());
            intent.putExtra("longitude_key",location.getLongitude());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            Toast.makeText(this,"no location",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onMapClick(LatLng latLng) {
        addMarker(latLng);
    }

    private void addLine(LatLng startLatLng, LatLng endLatLng){
        mMap.clear();
        mMap.addPolyline(new PolylineOptions().geodesic(true)
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_settings:
                settingsActivity();
                return true;
            case R.id.action_search:
                goToList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void sendToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void settingsActivity() {

        setContentView(R.layout.activity_menu);

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("reminder");
        tabSpec.setContent(R.id.reminder);
        tabSpec.setIndicator("Reminder");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("About");
        tabSpec.setContent(R.id.about);
        tabSpec.setIndicator("About");
        tabHost.addTab(tabSpec);
    }
}
