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
import com.alejandrolai.sfpark.R;

import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

public class ReminderActivity extends ActionBarActivity{

    Button startButton, stopButton,setTimer, resetTimer;
    TextView textViewTime;
    Vibrator v;
    PendingIntent pendingIntent;
    private CounterClass timer;
    long userInputTime, millisecs;
    long timeInTimerWhenPause, systemTimeWhenPause;
    //AlarmManager reminderAlarm;

    /**
     * When Reminder Page is opened, onCreate creates objects needed for activity to function,
     * such as buttons, textview, etc
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //Add a vibrator
        v =(Vibrator) getSystemService(VIBRATOR_SERVICE);

        // Adds buttons
        startButton = (Button) findViewById(R.id.buttonStart);
        stopButton = (Button) findViewById(R.id.buttonStop);
        setTimer = (Button) findViewById(R.id.setTimeButton);
        resetTimer = (Button) findViewById(R.id.buttonReset);

        // Views the time
        textViewTime = (TextView) findViewById(R.id.viewTime);

    }


    /**
     * This method sets the text to the time user wants to use for timer.
     * This is also where the timer is created.
     * @param view
     */
    public void setTimerTime(View view){

        //Get Hour and Minute from XML file
        TextView hour = (EditText) findViewById(R.id.hour);
        TextView minute = (EditText) findViewById(R.id.minute);

        //MAKE SURE TIMER IS STILL THERE WHEN RETURNING TO MAP
        //NEED TO PUT A CONTINUE BUTTON WHEN TIME IS STOPPED.
        //NEED TO PUT A WAY THAT IF THERE IS A TIMER ON, and when a new set timer is clicked, previous timer stops.

        //Change Hour and Minute from TextView Object to Int
        int hourInt = Integer.parseInt(hour.getText().toString());
        int minuteInt = Integer.parseInt(minute.getText().toString());


        // Sets the text to the tested time. Need to Set text time to user input
        textViewTime.setText(hourInt+":"+minuteInt+":00");

        //Set Start button text to start
        startButton.setText("Start");

        // Set this timer to the time the user specifies into milliseconds
        long hourToMillisecs = hourInt*3600000;
        long minuteToMillisecs = minuteInt*60000;

        //Puts the users total desired time into milliseconds
        userInputTime = hourToMillisecs + minuteToMillisecs;




        //Resets Timer
        resetTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton.setText("Start");
                textViewTime.setText("00:00:00");
                timer.cancel();
            }
        });

        //Starts timer countdown
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer = new CounterClass(userInputTime, 1000);
                if(startButton.getText().equals("Start")){
                    //Create timer with user time

                    startButton.setText("Restart");
                    stopButton.setText("Stop");
                    timer.start();

                }
                else if(startButton.getText().equals("Restart")){
                    stopButton.setText("Stop");
                    timer.start();
                }

            }
        });

        //Stops resume timer when clicked
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

    }

    /**
     *CountDown Timer Class. This is how the timer is counted down.
     */
    public class CounterClass extends CountDownTimer{

        /**
         * Counts down the timer
         * @param millisInFuture the exact time when the timer is done
         * @param countDownInterval is time inbetween count downs
         */
        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        /**
         * Implementation of ticking down. String is dynamically changed in runtime, where the time
         * is shown by hour, minutes, and seconds.
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
         *When the time reaches zero, this method is called. Should be vibrate, alarm, etc.
         */
        @Override
        public void onFinish() {
            v.vibrate(4500);

            //Get Notes from user
            TextView notes = (EditText) findViewById(R.id.Notes);
            String userNotes = notes.getText().toString();

            //Intent to open App
            Intent notificationIntent = new Intent(ReminderActivity.this.getApplicationContext(), MainActivity.class);
            PendingIntent openAppIntent = PendingIntent.getActivity(ReminderActivity.this,
                    0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            //Builds a Notification object with settings
            NotificationCompat.Builder builder = new NotificationCompat.Builder(ReminderActivity.this);
            builder.setAutoCancel(true);
            builder.setContentTitle("Reminder");
            builder.setContentText(userNotes);
            builder.setContentIntent(openAppIntent);

            //Want to added Logo Here
            builder.setSmallIcon(R.drawable.applogo);


            //Builds notification to Notify
            Notification notify = builder.build();

            //Creates Notification Manager to Notification services
            NotificationManager notifyManager = (NotificationManager) ReminderActivity.this.getSystemService(NOTIFICATION_SERVICE);

            notifyManager.notify(8, notify);


            //Get Notes from user
            /*TextView notes = (EditText) findViewById(R.id.Notes);
            String userNotes = notes.getText().toString();

            //Create Alert Dialog
            AlertDialog reminderDialog = new AlertDialog.Builder(ReminderActivity.this).create();

            //Set Title
            reminderDialog.setTitle("Reminder");

            //Dialog to User Notes
            reminderDialog.setMessage(userNotes);


            reminderDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                        // here you can add functions
                }
            });

           if(!isFinishing()){

               reminderDialog.show();
           }*/
        }
    }

}
