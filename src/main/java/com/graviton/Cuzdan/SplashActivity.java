package com.graviton.Cuzdan;

import Helpers.JSONHelper;
import Helpers.User;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;


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

        String fileName = "userConfigTest54";
        ((Global) this.getApplication()).SetFilePath(fileName);

        File file = new File(this.getFilesDir(), fileName);
        JSONObject userInfo = null;

        // Dummy data daha bitmedi
        // Simdilik kullanma
        if (getResources().getString(R.string.dummyData).equals("true")) {
            try {
                StringBuilder buf = new StringBuilder();
                InputStream dummy = getAssets().open("userConfig.txt");
                BufferedReader in = new BufferedReader(new InputStreamReader(dummy, "UTF-8"));
                String str;

                while ((str = in.readLine()) != null) {
                    buf.append(str);
                }

                in.close();
                user = new User(new JSONObject(buf.toString()), "", getApplication());
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            // Tarih formatı ISO 8601 (YEAR-MONTH-DATE)
            if (!file.exists()) {

                // Kullanıcı yok, JSON üstünden yeni yaratıyoruz
                String userName = "umutseven92";
                String firstName = "Umut";
                String lastName = "Seven";

                try {
                    userInfo = JSONHelper.CreateStartingJSON(userName, firstName, lastName);
                    user = new User(userInfo, file.getAbsolutePath(), getApplication());

                    user.GetBanker().WriteUserInfo(userInfo.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {

                // Kullanıcı var, JSON üstünden yüklüyoruz
                try {
                    StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                        sb.append("\n");
                    }

                    user = new User(new JSONObject(sb.toString()), file.getAbsolutePath(), getApplication());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        ((Global) this.getApplication()).SetUser(user);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        SetSavingsNotification(calendar);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_LENGTH);


    }

    public void SetSavingsNotification(Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        Intent intent = new Intent(SplashActivity.this, NotificationReceiver.class);
        PendingIntent event = PendingIntent.getBroadcast(SplashActivity.this, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), event);
    }
}
