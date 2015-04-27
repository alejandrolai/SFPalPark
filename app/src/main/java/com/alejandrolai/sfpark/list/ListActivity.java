package com.alejandrolai.sfpark.list;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import com.alejandrolai.sfpark.Adapter;
import com.alejandrolai.sfpark.MainActivity;
import com.alejandrolai.sfpark.R;
import com.alejandrolai.sfpark.Service;
import com.alejandrolai.sfpark.model.ParkingSpotList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ListActivity extends ActionBarActivity {


    private static final String TAG = ListActivity.class.getSimpleName();

    private Toolbar mToolbar;

    ListView mParkingListView;
    ParkingSpotList mParkingSpotList;
    Adapter mAdapter;
    private double currentLatitude;
    private double currentLongitude;

    LinkedHashMap map = new LinkedHashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Set up Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentLatitude = extras.getDouble("latitude_key");
            currentLongitude = extras.getDouble("longitude_key");
            // Set up adapter to put data into the listview (we don't need this)
            mAdapter = new Adapter(this);
            mParkingListView = (ListView) findViewById(R.id.parking_location_list);
            mParkingListView.setAdapter(mAdapter);
            getData();
        } else {
            Toast.makeText(this,"Unable to get location",Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_settings:
                settingsActivity();
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

    public void shareToMap(View view) {

        String location = (String) view.getContentDescription();
        /*
        Uri uri = Uri.parse("geo:0,0?q=" + location);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
        */

        Intent intent = new Intent(this, MainActivity.class);

        intent.putExtra("location_key",location.toString());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Toast.makeText(this,location,Toast.LENGTH_SHORT).show();

        startActivity(intent);

    }

    /**
     * Check if internet connection is on
     * @return true if there is internet connection
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void getData() {

        map.put("lat",Double.toString(currentLatitude));
        map.put("long",Double.toString(currentLongitude));
        map.put("radius","4");
        map.put("uom","mile");
        map.put("response","json");
        if (isOnline()) {
            Toast.makeText(this, getString(R.string.retrieving_data), Toast.LENGTH_SHORT).show();
            // Call getParkingSpots() and test connection to Sfpark,
            // on succcess retrieve
            Service.getService().getParkingSpots(map,new Callback<ParkingSpotList>() {
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

    private void retrieveData(final ParkingSpotList parkingSpotList) throws InterruptedException, ExecutionException, TimeoutException {
        if (parkingSpotList != null) {
            AsyncTask<Void,Void,ParkingSpotList> task = new AsyncTask<Void, Void, ParkingSpotList>() {
                @Override
                protected ParkingSpotList doInBackground(Void... params) {
                    // here the list of parking spots is returned
                    return parkingSpotList;
                }

                @Override
                protected void onPostExecute(ParkingSpotList parkingSpotList) {
                    super.onPostExecute(parkingSpotList);
                    // and here is where you can use it
                    // here first is passed to a local variable (mParkingSpotList)
                    // then it is set passed
                    mParkingSpotList = parkingSpotList;
                    mAdapter.setParkingSpots(mParkingSpotList.getList());
                    getLocation(mParkingSpotList);
                }
            };
            task.execute();
            task.get(4000, TimeUnit.MILLISECONDS);
        }
        else {
            Toast.makeText(this,"error connecting", Toast.LENGTH_SHORT).show();
        }
    }

    private void getLocation(ParkingSpotList mParkingSpotList) {

    }


}