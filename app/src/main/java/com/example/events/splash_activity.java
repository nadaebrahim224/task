package com.example.events;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

public class splash_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_activity);

        Handler hand = new Handler();
        Runnable run = new Runnable() {
            @Override
            public void run() {
                Intent go = new Intent(splash_activity.this, MainActivity.class);
                startActivity(go);
            }
        };

        hand.postDelayed(run,3000);

    }
}
