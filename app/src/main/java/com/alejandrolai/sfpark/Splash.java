package com.alejandrolai.sfpark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;


/**
 * Created by den on 4/21/2015.
 */
public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashh);

        final ImageView iv = (ImageView) findViewById(R.id.imageView);
        Thread startTimer = new Thread(){
            public void run(){
                try {
                    sleep(2500);
                    Intent i = new Intent(Splash.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        startTimer.start();
    }
}
