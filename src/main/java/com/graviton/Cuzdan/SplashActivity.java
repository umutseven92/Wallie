package com.graviton.Cuzdan;

import Helpers.User;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


/**
 * Created by Umut Seven on 11.11.2014, for Graviton.
 */
public class SplashActivity extends Activity {

    private final int SPLASH_LENGTH = 1200;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.splash_fragment);

        User user = null;

        String fileName = "userConfigTest38";
        ((Global) this.getApplication()).SetFilePath(fileName);

        File file = new File(this.getFilesDir(), fileName);
        JSONObject userInfo = null;

        // Tarih formatı ISO 8601 (YEAR-MONTH-DATE)
        if (!file.exists()) {

            // Kullanıcı yok, JSON üstünden yeni yaratıyoruz
            String userName = "umutseven92";
            String firstName = "Umut";
            String lastName = "Seven";

            try {
                String userSettings = String.format("{\n" +
                        "\t\"user\": {\n" +
                        "\t\t\"userName\": \"%s\",\n" +
                        "\t\t\"birthDate\": \"1992-08-05\",\n" +
                        "\t\t\"name\": \"%s\",\n" +
                        "\t\t\"lastName\": \"%s\",\n" +
                        "\t\t\"city\": \"Istanbul\",\n" +
                        "\t\t\"email\": \"umutseven92@gmail.com\",\n" +
                        "\n" +
                        "\t\t\"incomes\": [],\n" +
                        "\t\t\"expenses\": []\n" +
                        "\t}\n" +
                        "}\n", userName, firstName, lastName);

                userInfo = new JSONObject(userSettings);
                user = new User(userInfo, file.getAbsolutePath());

                user.GetBanker().WriteUserInfo(getApplication(), userInfo.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        else {

            // Kullanıcı var, JSON üstünden yüklüyoruz
            try {
                StringBuilder sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }

                user = new User(new JSONObject(sb.toString()), file.getAbsolutePath());

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
        }, SPLASH_LENGTH);


    }
}
