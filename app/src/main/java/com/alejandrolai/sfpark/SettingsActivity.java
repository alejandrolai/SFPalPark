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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;

import com.alejandrolai.sfpark.Timer.ReminderActivity;
import com.alejandrolai.sfpark.database.ParkingLocationDatabase;
import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * Created by Ihsan Taha on 4/30/15.
 */
public class SettingsActivity extends ActionBarActivity {

    //private String theme = MainActivity.getCurrentTheme();

    Toolbar mToolbar;
    ParkingLocationDatabase dbParkingLocation;

    int color = 0xffffff;

    SharedPreferencesHelper sharedPreferencesHelper = SharedPreferencesHelper.getInstance();

    Button changeColorButton1;
    Button changeColorButton2;
    Button changeColorButton3;
    int goodColor;
    int okColor;
    int badColor;
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
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        checkColorTheme();

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec  tabSpec= tabHost.newTabSpec("Map");
        tabSpec.setContent(R.id.map);
        tabSpec.setIndicator("Map");
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

        goodColor = sharedPreferencesHelper.readIntFromPreferences(this,SharedPreferencesHelper.GOOD_COLOR,getResources().getColor(R.color.green_600));
        okColor =  sharedPreferencesHelper.readIntFromPreferences(this,SharedPreferencesHelper.OK_COLOR,getResources().getColor(R.color.orange_600));
        badColor =   sharedPreferencesHelper.readIntFromPreferences(this,SharedPreferencesHelper.BAD_COLOR,getResources().getColor(R.color.black));

        changeColorButton1 = (Button) findViewById(R.id.changeColorButton1);
        changeColorButton1.setBackgroundColor(goodColor);
        changeColorButton2 = (Button) findViewById(R.id.changeColorButton2);
        changeColorButton2.setBackgroundColor(okColor);
        changeColorButton3 = (Button) findViewById(R.id.changeColorButton3);
        changeColorButton3.setBackgroundColor(badColor);

        changeColorButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog1(false);
            }
        });

        changeColorButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog2(false);
            }
        });

        changeColorButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog3(false);
            }
        });

    }

    void openDialog1(boolean supportsAlpha) {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(SettingsActivity.this, color, supportsAlpha, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
                sharedPreferencesHelper.saveIntToPreferences(SettingsActivity.this,SharedPreferencesHelper.GOOD_COLOR,color);
                changeColorButton1.setBackgroundColor(color);
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }
    void openDialog2(boolean supportsAlpha) {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(SettingsActivity.this, color, supportsAlpha, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
                sharedPreferencesHelper.saveIntToPreferences(SettingsActivity.this,SharedPreferencesHelper.OK_COLOR,color);
                changeColorButton2.setBackgroundColor(color);
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }
    void openDialog3(boolean supportsAlpha) {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(SettingsActivity.this, color, supportsAlpha, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
                sharedPreferencesHelper.saveIntToPreferences(SettingsActivity.this,SharedPreferencesHelper.BAD_COLOR,color);
                changeColorButton3.setBackgroundColor(color);
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }



    /**
     * Displays the menu
     *
     * @param //menu
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
     * @param //item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_back:
                finish();
                return true;
            case R.id.action_history:
                finish();
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
        dbParkingLocation = new ParkingLocationDatabase(getApplicationContext());
        dbParkingLocation.changeDatabaseTheme("default");
        checkColorTheme();

    }

    public void changeToBeach(View view) {
        Toast.makeText(getApplicationContext(), "Theme changed to beach.", Toast.LENGTH_SHORT).show();
        MainActivity.setTheme("beach");
        dbParkingLocation = new ParkingLocationDatabase(getApplicationContext());
        dbParkingLocation.changeDatabaseTheme("beach");
        checkColorTheme();
    }

    public void changeToGarden(View view) {
        Toast.makeText(getApplicationContext(), "Theme changed to garden.", Toast.LENGTH_SHORT).show();
        MainActivity.setTheme("garden");
        dbParkingLocation = new ParkingLocationDatabase(getApplicationContext());
        dbParkingLocation.changeDatabaseTheme("garden");
        checkColorTheme();
    }

    public void changeToRose(View view) {
        Toast.makeText(getApplicationContext(), "Theme changed to rose.", Toast.LENGTH_SHORT).show();
        MainActivity.setTheme("rose");
        dbParkingLocation = new ParkingLocationDatabase(getApplicationContext());
        dbParkingLocation.changeDatabaseTheme("rose");
        checkColorTheme();
    }

    public void changeToIce(View view) {
        Toast.makeText(getApplicationContext(), "Theme changed to ice.", Toast.LENGTH_SHORT).show();
        MainActivity.setTheme("ice");
        dbParkingLocation = new ParkingLocationDatabase(getApplicationContext());
        dbParkingLocation.changeDatabaseTheme("ice");
        checkColorTheme();
    }

    public void changeToDesert(View view) {
        Toast.makeText(getApplicationContext(), "Theme changed to desert.", Toast.LENGTH_SHORT).show();
        MainActivity.setTheme("desert");
        dbParkingLocation = new ParkingLocationDatabase(getApplicationContext());
        dbParkingLocation.changeDatabaseTheme("desert");
        checkColorTheme();
    }

    public void changeToRoyal(View view) {
        Toast.makeText(getApplicationContext(), "Theme changed to royal.", Toast.LENGTH_SHORT).show();
        MainActivity.setTheme("royal");
        dbParkingLocation = new ParkingLocationDatabase(getApplicationContext());
        dbParkingLocation.changeDatabaseTheme("royal");
        checkColorTheme();
    }

    public void changeToSnow(View view) {
        Toast.makeText(getApplicationContext(), "Theme changed to snow.", Toast.LENGTH_SHORT).show();
        MainActivity.setTheme("snow");
        dbParkingLocation = new ParkingLocationDatabase(getApplicationContext());
        dbParkingLocation.changeDatabaseTheme("snow");
        checkColorTheme();
    }



    /**
     * Changes the color theme of the toolbar and buttons on the Parking History page.
     */
    public void checkColorTheme() {
        if (MainActivity.theme.equalsIgnoreCase("default")) {
            mToolbar.setBackgroundColor(getResources().getColor(R.color.default_grey));
        } else if (MainActivity.theme.equalsIgnoreCase("beach")) {
            mToolbar.setBackgroundColor(getResources().getColor(R.color.beach_blue));
        } else if (MainActivity.theme.equalsIgnoreCase("garden")) {
            mToolbar.setBackgroundColor(getResources().getColor(R.color.garden_foilage));
        } else if (MainActivity.theme.equalsIgnoreCase("rose")) {
            mToolbar.setBackgroundColor(getResources().getColor(R.color.red_rose));
        } else if (MainActivity.theme.equalsIgnoreCase("ice")) {
            mToolbar.setBackgroundColor(getResources().getColor(R.color.ice_blue));
        } else if (MainActivity.theme.equalsIgnoreCase("desert")) {
            mToolbar.setBackgroundColor(getResources().getColor(R.color.desert_yellow));
        }  else if (MainActivity.theme.equalsIgnoreCase("royal")) {
            mToolbar.setBackgroundColor(getResources().getColor(R.color.royal_purple));
        } else if (MainActivity.theme.equalsIgnoreCase("snow")) {
            mToolbar.setBackgroundColor(getResources().getColor(R.color.black));
        }
    }

}