package com.example.Cuzdan;

import Helpers.User;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import java.io.File;
import java.io.FileOutputStream;

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

        setContentView(R.layout.splash);


        File file = new File(this.getFilesDir(),"userInfo");
        if(!file.exists())
        {
            String filename = "userInfo";
            String userName = "Umut Seven";
            FileOutputStream outputStream;

            try {
                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(userName.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        final User user = new User("Umut Seven");
        ((Global) this.getApplication()).SetUser(user);

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
