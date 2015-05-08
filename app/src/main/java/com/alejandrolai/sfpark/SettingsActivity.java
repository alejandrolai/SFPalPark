package com.alejandrolai.sfpark;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

/**
 * Created by Ihsan Taha on 4/30/15.
 */
public class SettingsActivity extends ActionBarActivity {

    private String theme = MainActivity.getCurrentTheme();


    /**
     * Creates a tab menu with the Settings' features.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Creates a toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("Radius");
        tabSpec.setContent(R.id.radius);
        tabSpec.setIndicator("Radius");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("Themes");
        tabSpec.setContent(R.id.themes);
        tabSpec.setIndicator("Themes");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("Help");
        tabSpec.setContent(R.id.help);
        tabSpec.setIndicator("Help");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("About");
        tabSpec.setContent(R.id.about);
        tabSpec.setIndicator("About");
        tabHost.addTab(tabSpec);

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
        //getMenuInflater().inflate(R.menu.menu_settings, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
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
            case R.id.action_history:
                Intent intent = new Intent(this, ParkingLocationActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    // Changes the color themes based on the selected theme

    public void changeToDefault(View view) {
        Toast.makeText(getApplicationContext(), "Theme changed to default.", Toast.LENGTH_SHORT).show();
        MainActivity.setTheme("default");
    }

    public void changeToBeach(View view) {
        Toast.makeText(getApplicationContext(), "Theme changed to beach.", Toast.LENGTH_SHORT).show();
        MainActivity.setTheme("beach");
    }

    public void changeToGarden(View view) {
        Toast.makeText(getApplicationContext(), "Theme changed to garden.", Toast.LENGTH_SHORT).show();
        MainActivity.setTheme("garden");
    }

    public void changeToRose(View view) {
        Toast.makeText(getApplicationContext(), "Theme changed to rose.", Toast.LENGTH_SHORT).show();
        MainActivity.setTheme("rose");
    }

    public void changeToIce(View view) {
        Toast.makeText(getApplicationContext(), "Theme changed to ice.", Toast.LENGTH_SHORT).show();
        MainActivity.setTheme("ice");
    }

    public void changeToDesert(View view) {
        Toast.makeText(getApplicationContext(), "Theme changed to desert.", Toast.LENGTH_SHORT).show();
        MainActivity.setTheme("desert");
    }

    public void changeToRoyal(View view) {
        Toast.makeText(getApplicationContext(), "Theme changed to royal.", Toast.LENGTH_SHORT).show();
        MainActivity.setTheme("royal");
    }

    public void changeToSnow(View view) {
        Toast.makeText(getApplicationContext(), "Theme changed to snow.", Toast.LENGTH_SHORT).show();
        MainActivity.setTheme("snow");
    }

}
