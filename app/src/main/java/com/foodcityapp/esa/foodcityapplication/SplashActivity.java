package com.foodcityapp.esa.foodcityapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    private ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        logo = (ImageView) findViewById(R.id.logo);
        Animation splash = AnimationUtils.loadAnimation(this, R.anim.splash);
        logo.startAnimation(splash);

        final Intent startactivity = new Intent(this, MainActivity.class);

        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(startactivity);
                    finish();
                }
            }

        };
        timer.start();

    }
}
