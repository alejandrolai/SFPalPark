package com.alejandrolai.sfpark.Timer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.alejandrolai.sfpark.MainActivity;
import com.alejandrolai.sfpark.R;

import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

public class ReminderActivity extends ActionBarActivity{

    Button startButton, stopButton,setTimer, resetTimer;
    TextView textViewTime;
    Vibrator v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

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

    public void setTimerTime(View view){
        //Get Hour and Minute from XML file
        TextView hour = (EditText) findViewById(R.id.hour);
        TextView minute = (EditText) findViewById(R.id.minute);

        //MAKE SURE TIMER IS STILL THERE WHEN RETURNING TO MAP
        //NEED TO PUT AN IF STATEMENT IF TEXTVIEW IS NULL, make it zero
        //NEED TO PUT A CONTINUE BUTTON WHEN TIME IS STOPPED.
        //NEED TO PUT A WAY THAT IF THERE IS A TIMER ON, and when a new set timer is clicked, previous timer stops.
        //ALSO NEED TO PUT VIBRATE AND ALARM WITH NOTES.

        //Change Hour and Minute from TextView Object to Int
        int hourInt = Integer.parseInt(hour.getText().toString());
        int minuteInt = Integer.parseInt(minute.getText().toString());



        // Sets the text to the tested time. Need to Set text time to user input
        textViewTime.setText(hourInt+":"+minuteInt+":00");

        //Set Start button text to start
        //startButton.setText("Start");

        // Set this timer to the time the user specifies into milliseconds
        long hourToMillisecs = hourInt*3600000;
        long minuteToMillisecs = minuteInt*60000;

        long userInputTime = hourToMillisecs + minuteToMillisecs;
        final CounterClass timer = new CounterClass(userInputTime, 1000);

        resetTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton.setText("Start");
                textViewTime.setText("00:00:00");
                timer.cancel();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton.setText("Start");
                timer.start();
            }
        });

        //Stops the timer with the stop button
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stopButton.setText("Continue");
                timer.cancel();
            }
        });

    }

    //@TargetApi(Build.VERSION_CODES.GINGERBREAD)

    //CountDown Timer Class
    public class CounterClass extends CountDownTimer{

        public CounterClass(long millisInFuture, long countDownInterval){
            super(millisInFuture, countDownInterval);
        }

        // Counting down method of Timer
        @Override
        public void onTick(long millisUntilFinished) {
            long millisecs = millisUntilFinished;
            String tick = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisecs),
                    TimeUnit.MILLISECONDS.toMinutes(millisecs) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisecs)),
                    TimeUnit.MILLISECONDS.toSeconds(millisecs) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisecs)));

            System.out.println(tick);
            textViewTime.setText(tick);
        }

        // When the time reaches zero, this method is called. Should be vibrate, alarm, etc.
        @Override
        public void onFinish() {
            v.vibrate(2000);

            //I want this to be a dialog.
            textViewTime.setText("Time is up! Return to Parking Spot!");

        }
    }


    public void createTimer() {
        CountDownTimer countDownTimer;
        boolean timerHasStarted = false;

        //int

        //long startTime =
        long interval = 1 * 1000;

    }

}
