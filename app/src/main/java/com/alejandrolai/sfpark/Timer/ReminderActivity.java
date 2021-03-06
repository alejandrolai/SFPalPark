package com.alejandrolai.sfpark.Timer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.app.AlarmManager;
import android.content.DialogInterface;
import android.provider.AlarmClock;
import android.content.BroadcastReceiver;
import android.widget.Toast;
import android.app.PendingIntent;
import android.app.NotificationManager;
import android.app.Notification;


import com.alejandrolai.sfpark.MainActivity;
import com.alejandrolai.sfpark.ParkingLocationActivity;
import com.alejandrolai.sfpark.R;
import com.alejandrolai.sfpark.SettingsActivity;
import com.alejandrolai.sfpark.database.ParkingLocationDatabase;

import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

public class ReminderActivity extends ActionBarActivity{



    Button startButton, stopButton,setTimer, resetTimer;
    TextView textViewTime;
    EditText notes;
    Vibrator v;
    PendingIntent pendingIntent;
    private CounterClass timer = new CounterClass(0,0);
    long userInputTime, millisecs, newTime;
    static long timeInTimerWhenPause, systemTimeWhenPause;
    static boolean isPaused = false;
    static String textInNotes;

    Toolbar mToolbar;

    // AlarmManager reminderAlarm;

    /**
     * When Reminder Page is opened, onCreate creates objects needed for activity to function,
     * such as buttons, textview, etc
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Adds a vibrator
        v =(Vibrator) getSystemService(VIBRATOR_SERVICE);

        // Adds buttons
        startButton = (Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);
        setTimer = (Button) findViewById(R.id.setTimer);
        resetTimer = (Button) findViewById(R.id.resetTimer);

        // Views the time
        textViewTime = (TextView) findViewById(R.id.viewTime);
        //notes
        notes = (EditText) findViewById(R.id.Notes);

        checkColorTheme();

    }



    /**
     * This method sets the text to the time user wants to use for timer.
     * This is also where the timer is created.
     * @param view
     */
    public void setTimerTime(View view){

        // Gets Hour and Minute from XML file
        timer.cancel();
        TextView hour = (EditText) findViewById(R.id.hour);
        TextView minute = (EditText) findViewById(R.id.minute);

        // ChangeS Hour and Minute from TextView Object to Int
        int hourInt = Integer.parseInt(hour.getText().toString());
        int minuteInt = Integer.parseInt(minute.getText().toString());


        // Sets the text to the tested time. Need to Set text time to user input
        textViewTime.setText(hourInt+":"+minuteInt+":00");

        // Sets Start button text to start
        startButton.setText("Start");

        // Sets this timer to the time the user specifies into milliseconds
        long hourToMillisecs = hourInt*3600000;
        long minuteToMillisecs = minuteInt*60000;

        // Puts the users total desired time into milliseconds
        userInputTime = hourToMillisecs + minuteToMillisecs;
        timer = new CounterClass(userInputTime, 1000);


    }

    /**
     * A method that is called when the user clicks the start/ restart button
     * @param v
     */

     public void startButtonListener(View v){


         if(startButton.getText().equals("Start")){
             startButton.setText("Restart");
             stopButton.setText("Stop");
             timer.start();

         }
         else if(startButton.getText().equals("Restart")){
             stopButton.setText("Stop");
             timer.start();
         }
     }

    /**
     * A method that is invoked when the stop / resume button is clicked
     * @param v
     */
     public void stopButtonListener(View v){

         if(stopButton.getText().equals("Stop")){
             stopButton.setText("Resume");
             startButton.setText("Restart");
             timer.cancel();
         }
         else if(stopButton.getText().equals("Resume")){
             stopButton.setText("Stop");
             startButton.setText("Restart");
             timer.cancel();
             timer = new CounterClass(millisecs, 1000);
             timer.start();
         }
     }

    /**
     * A method invoked when the reset button is called
     * @param v
     */
     public void resetButtonListener(View v){
         millisecs = 0;
         startButton.setText("Start");
         textViewTime.setText("00:00:00");
         timer.cancel();
     }


    /**
     * Over ride the onPause method and gets called when the user exits the activity to go back to the main activity
      */
    @Override
    protected void onPause(){
        super.onPause();

        systemTimeWhenPause = System.currentTimeMillis();
        timeInTimerWhenPause = millisecs;
        textInNotes = notes.getText().toString();

        isPaused = true;


    }

    /**
     * Over ride the onResume() method and gets called when the user resumes to the reminder page
     */
    @Override
    protected void onResume() {
        super.onResume();
        timer.cancel();
        if (isPaused) {
            isPaused = false;
            timer.cancel();

            newTime = timeInTimerWhenPause - (System.currentTimeMillis() - systemTimeWhenPause);
            if(newTime > 0) {
                timer = new CounterClass(newTime, 1000);
                timer.start();
            }
            notes.setText(textInNotes);
        }

    }
        /**
         * CountDown Timer Class. This is how the timer is counted down.
         */
        public class CounterClass extends CountDownTimer {

            /**
             * Counts down the timer
             *
             * @param millisInFuture    the exact time when the timer is done
             * @param countDownInterval is time inbetween count downs
             */

            public CounterClass(long millisInFuture, long countDownInterval) {
                super(millisInFuture, countDownInterval);
            }


            /**
             * Implementation of ticking down. String is dynamically changed in runtime, where the time
             * is shown by hour, minutes, and seconds.
             *
             * @param millisUntilFinished the exact time when the timer is done
             */
            @Override
            public void onTick(long millisUntilFinished) {
                millisecs = millisUntilFinished;
                String tick = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisecs),
                        TimeUnit.MILLISECONDS.toMinutes(millisecs) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisecs)),
                        TimeUnit.MILLISECONDS.toSeconds(millisecs) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisecs)));

                System.out.println(tick);
                textViewTime.setText(tick);
            }


            /**
             * When the time reaches zero, this method is called. Should be vibrate, alarm, etc.
             */
            @Override
            public void onFinish() {
                v.vibrate(4500);

                // Gets Notes from user
                TextView notes = (EditText) findViewById(R.id.Notes);
                String userNotes = notes.getText().toString();

                //sIntent to open App
                Intent notificationIntent = new Intent(ReminderActivity.this.getApplicationContext(), ReminderActivity.class);
                PendingIntent openAppIntent = PendingIntent.getActivity(ReminderActivity.this,
                        0, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                //sBuilds a Notification object with settings
                NotificationCompat.Builder builder = new NotificationCompat.Builder(ReminderActivity.this);
                builder.setAutoCancel(true);
                builder.setContentTitle("Reminder");
                builder.setContentText(userNotes);
                builder.setContentIntent(openAppIntent);

                //Want to added Logo Here
                builder.setSmallIcon(R.drawable.applogo);


                // Builds notification to Notify
                Notification notify = builder.build();

                // Creates Notification Manager to Notification services
                NotificationManager notifyManager = (NotificationManager) ReminderActivity.this.getSystemService(NOTIFICATION_SERVICE);

                notifyManager.notify(8, notify);

            }
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
        inflater.inflate(R.menu.menu_reminder, menu);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    /**
     * Changes the color theme of the toolbar and buttons on the main page
     */
    public void checkColorTheme() {
        startButton.setTextColor(getResources().getColor(R.color.bright_snow));
        stopButton.setTextColor(getResources().getColor(R.color.bright_snow));
        setTimer.setTextColor(getResources().getColor(R.color.bright_snow));
        resetTimer.setTextColor(getResources().getColor(R.color.bright_snow));

        if (MainActivity.theme.equalsIgnoreCase("default")) {
            startButton.setBackgroundColor(getResources().getColor(R.color.bright_default));
            stopButton.setBackgroundColor(getResources().getColor(R.color.bright_default));
            setTimer.setBackgroundColor(getResources().getColor(R.color.bright_default));
            resetTimer.setBackgroundColor(getResources().getColor(R.color.bright_default));
            mToolbar.setBackgroundColor(getResources().getColor(R.color.default_grey));
        } else if (MainActivity.theme.equalsIgnoreCase("beach")) {
            startButton.setBackgroundColor(getResources().getColor(R.color.beach_orange));
            stopButton.setBackgroundColor(getResources().getColor(R.color.beach_orange));
            setTimer.setBackgroundColor(getResources().getColor(R.color.beach_orange));
            resetTimer.setBackgroundColor(getResources().getColor(R.color.beach_orange));
            mToolbar.setBackgroundColor(getResources().getColor(R.color.beach_blue));
        } else if (MainActivity.theme.equalsIgnoreCase("garden")) {
            startButton.setBackgroundColor(getResources().getColor(R.color.garden_plant));
            stopButton.setBackgroundColor(getResources().getColor(R.color.garden_plant));
            setTimer.setBackgroundColor(getResources().getColor(R.color.garden_plant));
            resetTimer.setBackgroundColor(getResources().getColor(R.color.garden_plant));
            mToolbar.setBackgroundColor(getResources().getColor(R.color.garden_foilage));
        } else if (MainActivity.theme.equalsIgnoreCase("rose")) {
            startButton.setBackgroundColor(getResources().getColor(R.color.bright_rose));
            stopButton.setBackgroundColor(getResources().getColor(R.color.bright_rose));
            setTimer.setBackgroundColor(getResources().getColor(R.color.bright_rose));
            resetTimer.setBackgroundColor(getResources().getColor(R.color.bright_rose));
            stopButton.setBackgroundColor(getResources().getColor(R.color.bright_rose));
            mToolbar.setBackgroundColor(getResources().getColor(R.color.red_rose));
        } else if (MainActivity.theme.equalsIgnoreCase("ice")) {
            startButton.setBackgroundColor(getResources().getColor(R.color.bright_ice));
            stopButton.setBackgroundColor(getResources().getColor(R.color.bright_ice));
            setTimer.setBackgroundColor(getResources().getColor(R.color.bright_ice));
            resetTimer.setBackgroundColor(getResources().getColor(R.color.bright_ice));
            mToolbar.setBackgroundColor(getResources().getColor(R.color.ice_blue));
        } else if (MainActivity.theme.equalsIgnoreCase("desert")) {
            startButton.setBackgroundColor(getResources().getColor(R.color.bright_desert));
            stopButton.setBackgroundColor(getResources().getColor(R.color.bright_desert));
            setTimer.setBackgroundColor(getResources().getColor(R.color.bright_desert));
            resetTimer.setBackgroundColor(getResources().getColor(R.color.bright_desert));
            mToolbar.setBackgroundColor(getResources().getColor(R.color.desert_yellow));
        } else if (MainActivity.theme.equalsIgnoreCase("royal")) {
            startButton.setBackgroundColor(getResources().getColor(R.color.bright_purple));
            stopButton.setBackgroundColor(getResources().getColor(R.color.bright_purple));
            setTimer.setBackgroundColor(getResources().getColor(R.color.bright_purple));
            resetTimer.setBackgroundColor(getResources().getColor(R.color.bright_purple));
            mToolbar.setBackgroundColor(getResources().getColor(R.color.royal_purple));
        } else if (MainActivity.theme.equalsIgnoreCase("snow")) {
            startButton.setBackgroundColor(getResources().getColor(R.color.bright_snow));
            stopButton.setBackgroundColor(getResources().getColor(R.color.bright_snow));
            setTimer.setBackgroundColor(getResources().getColor(R.color.bright_snow));
            resetTimer.setBackgroundColor(getResources().getColor(R.color.bright_snow));
            mToolbar.setBackgroundColor(getResources().getColor(R.color.black));
            startButton.setTextColor(getResources().getColor(R.color.default_grey));
            stopButton.setTextColor(getResources().getColor(R.color.default_grey));
            setTimer.setTextColor(getResources().getColor(R.color.default_grey));
            resetTimer.setTextColor(getResources().getColor(R.color.default_grey));
        }
    }

}
