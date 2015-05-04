package com.alejandrolai.sfpark;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

/**
 * Created by ihsan_taha on 4/30/15.
 */
public class SettingsActivity3 extends ActionBarActivity {



    /**
     * Creates a tab menu with the Settings' features.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_activity3);

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

        tabSpec = tabHost.newTabSpec("Themes");
        tabSpec.setContent(R.id.themes);
        tabSpec.setIndicator("Themes");
        tabHost.addTab(tabSpec);
    }



    /**
     * Changes the color theme to "beach".
     *
     * @param view
     */
    public void sendToBeach(View view) {

        Context context = getApplicationContext();
        CharSequence text = "Theme changed to beach! Exit settings and return to see the new theme!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        MainActivity.setTheme("beach");
    }



    /**
     * Changes the color theme to "garden".
     *
     * @param view
     */
    public void sendToGarden(View view) {

        Context context = getApplicationContext();
        CharSequence text = "Theme changed to garden! Exit settings and return to see the new theme!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        MainActivity.setTheme("garden");
    }



    /**
     * Changes the color theme to "lady".
     *
     * @param view
     */
    public void sendToLady(View view) {

        Context context = getApplicationContext();
        CharSequence text = "Theme changed to lady! Exit settings and return to see the new theme!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        MainActivity.setTheme("lady");
    }

}
