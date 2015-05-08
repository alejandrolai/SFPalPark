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
 * Created by ihsan_taha on 4/30/15.
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
     *
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_about, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return true;
    }



    /**
     *
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
                Intent intent2 = new Intent(this, ParkingLocationActivity.class);
                startActivity(intent2);
                return true;
            //case R.id.action_preferences:
            //   startActivity(new Intent(this,PreferencesActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    /**
     * Changes the color theme to "beach".
     *
     * @param view
     */
    /*public void sendToBeach(View view) {

        Context context = getApplicationContext();
        CharSequence text = "Theme changed to beach! Exit settings and return to see the new theme!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        MainActivity.setTheme("beach");
    }*/



    /**
     * Changes the color theme to "garden".
     *
     * @param view
     */
    /*public void sendToGarden(View view) {

        Context context = getApplicationContext();
        CharSequence text = "Theme changed to garden! Exit settings and return to see the new theme!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        MainActivity.setTheme("garden");
    }*/



    /**
     * Changes the color theme to "lady".
     *
     * @param view
     */
    /*public void sendToLady(View view) {

        Context context = getApplicationContext();
        CharSequence text = "Theme changed to lady! Exit settings and return to see the new theme!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        MainActivity.setTheme("lady");

    }*/

}
