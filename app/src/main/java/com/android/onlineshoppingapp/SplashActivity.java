package com.android.onlineshoppingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // direct to login activity
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                // close splash activity
                finish();
            }
        }, 500);
    }
}