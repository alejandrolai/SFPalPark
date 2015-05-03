package com.alejandrolai.sfpark.Timer;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alejandrolai.sfpark.MainActivity;
import com.alejandrolai.sfpark.R;

import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

public class ReminderActivity extends ActionBarActivity{

    Button startButton, stopButton;
    TextView textViewTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        // Adds buttons
        startButton = (Button) findViewById(R.id.buttonStart);
        stopButton = (Button) findViewById(R.id.buttonStop);

        // Views the time
        textViewTime = (TextView) findViewById(R.id.viewTime);

        // Sets the text to the tested time. Need to Set text time to user input
        textViewTime.setText("00:00:30");

        // Needs to set this timer to the time the user specifies
        final CounterClass timer = new CounterClass(30000, 1000);

        // Starts the timer with the Start Button
        // Needs to fix bug that when timer is restarted, it continues from the previous time
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.start();
            }
        });

        //Stops the timer with the stop button
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            textViewTime.setText("Time is up!");
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
