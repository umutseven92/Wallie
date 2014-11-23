package com.example.Cuzdan;

import Helpers.User;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import org.json.*;

import java.io.*;

/**
 * Created by Umut on 11.11.2014.
 */
public class SplashActivity extends Activity {

    // How long in ms the user will wait, change this to be an accurate load time
    private final int splashLength = 3000;

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        setContentView(R.layout.splash);

        User user = null;

        String fileName = "userConfigTest2";
        File file = new File(this.getFilesDir(),fileName);
        JSONObject userInfo = null;


        // The user doesn't exist; so create one and save the file to internal storage.
        if(!file.exists())
        {
            String userName = "Max Planck";
            FileOutputStream outputStream;

            try {
                userInfo = new JSONObject("{ 'user': {'userName':'" + userName + "'} }");

                outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);

                if (userInfo != null) {
                    outputStream.write(userInfo.toString().getBytes());
                }
                outputStream.close();
                user = new User(userInfo);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        // The user exists; so attempt to load it from internal storage.
        else
        {
            try {

                StringBuilder sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null)
                {
                    sb.append(line);
                    sb.append("\n");
                }

                user = new User(new JSONObject(sb.toString()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


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
