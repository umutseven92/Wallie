package com.example.Cuzdan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

/**
 * Created by Umut on 11.11.2014.
 */
public class SplashActivity extends Activity {

    // How long in ms the user will wait
    private final int splashLength = 3000;

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, splashLength);


    }
}
