package com.alejandrolai.sfpark;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;


public class SettingsActivity3 extends ActionBarActivity {

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendToBeach(View view) {

        Context context = getApplicationContext();
        CharSequence text = "Theme changed to beach! Exit settings and return to see the new theme!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        MainActivity.setTheme("beach");
    }

    public void sendToGarden(View view) {

        Context context = getApplicationContext();
        CharSequence text = "Theme changed to garden! Exit settings and return to see the new theme!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        MainActivity.setTheme("garden");
    }

    public void sendToLady(View view) {

        Context context = getApplicationContext();
        CharSequence text = "Theme changed to lady! Exit settings and return to see the new theme!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        MainActivity.setTheme("lady");
    }
}
