package com.alejandrolai.sfpark;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.alejandrolai.sfpark.model.ParkingSpotList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends ActionBarActivity {

    private Toolbar mToolbar;

    ListView mParkingListView;
    ParkingSpotList mParkingSpotList;
    Adapter mAdapter;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        // Set up adapter to put data into the listview (we don't need this)
        mAdapter = new Adapter(this);
        mParkingListView = (ListView) findViewById(R.id.parking_location_list);
        mParkingListView.setAdapter(mAdapter);
        getData();
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
        switch (id) {
            case R.id.action_settings:
                Toast.makeText(this,getString(R.string.settings),Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_refresh:
                //getData();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    private void retrieveData(final ParkingSpotList parkingSpotList) throws InterruptedException, ExecutionException, TimeoutException {
        if (parkingSpotList != null) {
            AsyncTask<Void,Void,ParkingSpotList> task = new AsyncTask<Void, Void, ParkingSpotList>() {
                @Override
                protected ParkingSpotList doInBackground(Void... params) {
                    return parkingSpotList;
                }

                @Override
                protected void onPostExecute(ParkingSpotList parkingSpotList) {
                    super.onPostExecute(parkingSpotList);
                    mParkingSpotList = parkingSpotList;
                    mAdapter.setParkingSpots(mParkingSpotList.getList());
                }
            };
            task.execute();
            task.get(4000, TimeUnit.MILLISECONDS);
        }
        else {
            Toast.makeText(this, getString(R.string.error_connecting), Toast.LENGTH_SHORT).show();
        }
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

    public void shareToMap(View view) {

        String location = (String) view.getContentDescription();

        Uri uri = Uri.parse("geo:0,0?q=" + location);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

}