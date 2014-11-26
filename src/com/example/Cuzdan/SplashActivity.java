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

        String fileName = "userConfigTest11";
        File file = new File(this.getFilesDir(),fileName);
        JSONObject userInfo = null;


        // The user doesn't exist; so create one and save the file to internal storage.
        if(!file.exists())
        {
            String userName = "umutseven92";
            String firstName = "Umut";
            String lastName = "Seven";
            FileOutputStream outputStream;

            try {
                String userSettings =String.format("{\n" +
                        "\t\"user\": {\n" +
                        "\t\t\"userName\": \"%s\",\n" +
                        "\t\t\"birthDate\": \"1992-08-05\",\n" +
                        "\t\t\"name\": \"%s\",\n" +
                        "\t\t\"lastName\": \"%s\",\n" +
                        "\t\t\"city\": \"Istanbul\",\n" +
                        "\t\t\"email\": \"umutseven92@gmail.com\",\n" +
                        "\n" +
                        "\t\t\"incomes\": [{\n" +
                        "\n" +
                        "\t\t\t\"income\": {\n" +
                        "\t\t\t\t\t\"category\": \"borc\",\n" +
                        "\t\t\t\t\t\"subCategory\": \"borc odemesi\",\n" +
                        "\t\t\t\t\t\"amount\": \"43\",\n" +
                        "\t\t\t\t\t\"tag\": \"personal\",\n" +
                        "\t\t\t\t\t\"desc\": \"Alican borcunu odedi\",\n" +
                        "\t\t\t\t\t\"date\": \"2014-03-13\"\n" +
                        "\t\t\t\t},\n" +
                        "\n" +
                        "\t\t\t\"income\": {\n" +
                        "\t\t\t\t\t\"category\": \"maas\",\n" +
                        "\t\t\t\t\t\"subCategory\": \"maas odemesi\",\n" +
                        "\t\t\t\t\t\"amount\": \"1400\",\n" +
                        "\t\t\t\t\t\"tag\": \"personal\",\n" +
                        "\t\t\t\t\t\"desc\": \"Maas\",\n" +
                        "\t\t\t\t\t\"date\": \"2014-03-13\"\n" +
                        "\n" +
                        "\t\t\t\t}\n" +
                        "\t\t}],\n" +
                        "\n" +
                        "\t\t\"expenses\": [{\n" +
                        "\n" +
                        "\t\t\t\"expense\": {\n" +
                        "\t\t\t\t\t\"category\": \"yemek\",\n" +
                        "\t\t\t\t\t\"subCategory\": \"fast food\",\n" +
                        "\t\t\t\t\t\"amount\": \"43\",\n" +
                        "\t\t\t\t\t\"tag\": \"personal\",\n" +
                        "\t\t\t\t\t\"desc\": \"Burger King\",\n" +
                        "\t\t\t\t\t\"date\": \"2014-03-13\"\n" +
                        "\t\t\t\t},\n" +
                        "\n" +
                        "\t\t\t\"expense\": {\n" +
                        "\t\t\t\t\t\"category\": \"icecek\",\n" +
                        "\t\t\t\t\t\"subCategory\": \"alkollu icecek\",\n" +
                        "\t\t\t\t\t\"amount\": \"13\",\n" +
                        "\t\t\t\t\t\"tag\": \"personal\",\n" +
                        "\t\t\t\t\t\"desc\": \"Bira\",\n" +
                        "\t\t\t\t\t\"date\": \"2014-03-13\"\n" +
                        "\n" +
                        "\t\t\t\t}\n" +
                        "\n" +
                        "\t\t}]\n" +
                        "\n" +
                        "\t}\n" +
                        "\n" +
                        "}\n",userName,firstName,lastName );
                userInfo = new JSONObject(userSettings);

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
