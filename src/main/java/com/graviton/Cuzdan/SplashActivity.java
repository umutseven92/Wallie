package com.graviton.Cuzdan;

import Helpers.JSONHelper;
import Helpers.User;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import org.json.JSONObject;

import java.io.*;
import java.util.Calendar;

/**
 * Created by Umut Seven on 11.11.2014, for Graviton.
 */
public class SplashActivity extends Activity {

    private final int SPLASH_LENGTH = 1200;
    boolean first = true;
    File file;
    private String userName;
    private String userLastName;
    private String userCurrency;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.splash_fragment);

        String fileName = "userConfigTest102";
        ((Global) this.getApplication()).SetFilePath(fileName);

        file = new File(this.getFilesDir(), fileName);

        // Tarih formatı ISO 8601 (YEAR-MONTH-DATE)
        if (file.exists()) {

            // Kullanıcı var, JSON üstünden yüklüyoruz
            first = false;

            try {
                StringBuilder sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }

                JSONObject userJSON = new JSONObject(sb.toString());
                User user = new User(userJSON, file.getAbsolutePath(), getApplication());
                user.SetUserNotifications(PendingIntent.getBroadcast(SplashActivity.this, 0, new Intent(SplashActivity.this, SavingsNotificationReceiver.class), 0), PendingIntent.getBroadcast(SplashActivity.this, 0, new Intent(SplashActivity.this, ReminderNotificationReceiver.class), 0));

                ((Global) this.getApplication()).SetUser(user);


                if(userJSON.getJSONObject("user").getString("notifications").equals("true"))
                {
                    SetNotifications(Integer.parseInt(userJSON.getJSONObject("user").getString("savNotHour")), Integer.parseInt(userJSON.getJSONObject("user").getString("remNotHour")));
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                        mainIntent.putExtra("first", first);
                        SplashActivity.this.startActivity(mainIntent);
                        SplashActivity.this.finish();
                    }
                }, SPLASH_LENGTH);


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            // Kullanıcı yok, JSON üstünden yeni yaratıyoruz
            first = true;
            CreateUserDialog();

        }
    }

    private void SetFirstUser() throws Exception {
        JSONObject userInf = JSONHelper.CreateStartingJSON(userName, userLastName, userCurrency, "true", "true","8","14");
        User user = new User(userInf, file.getAbsolutePath(), getApplication());
        user.GetBanker().WriteUserInfo(userInf.toString());

        ((Global) this.getApplication()).SetUser(user);
        SetNotifications(8, 14);
    }

    private void SetNotifications(int savNotHour, int remNotHour) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, savNotHour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        SetNotification(calendar, SavingsNotificationReceiver.class);

        Calendar calendarReminder = Calendar.getInstance();
        calendarReminder.set(Calendar.HOUR_OF_DAY, remNotHour);
        calendarReminder.set(Calendar.MINUTE, 0);
        calendarReminder.set(Calendar.SECOND, 0);
        SetNotification(calendarReminder, ReminderNotificationReceiver.class);
    }

    public void SetNotification(Calendar calendar, Class receiver) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(SplashActivity.this, receiver);


        boolean alarmUp = (PendingIntent.getBroadcast(SplashActivity.this, 0, intent, PendingIntent.FLAG_NO_CREATE) != null);

        if (!alarmUp) {
            PendingIntent event = PendingIntent.getBroadcast(SplashActivity.this, 0, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, event);
        }
    }

    private void CreateUserDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View accountView = layoutInflater.inflate(R.layout.account_create, null);
        alert.setView(accountView);

        final EditText txtName = (EditText) accountView.findViewById(R.id.etName);
        final EditText txtLastName = (EditText) accountView.findViewById(R.id.etLastName);

        alert.setTitle("Merhaba!").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setCancelable(false);

        final Spinner spnCurrency = (Spinner) accountView.findViewById(R.id.spnCurrencies);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.currencies, R.layout.cuzdan_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCurrency.setAdapter(adapter);

        final AlertDialog dialog = alert.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {

                Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        String name = txtName.getText().toString().trim();
                        String lastName = txtLastName.getText().toString().trim();
                        String selectedCurrency = spnCurrency.getSelectedItem().toString();

                        String currencyCode = selectedCurrency.substring(selectedCurrency.indexOf("(") + 1, selectedCurrency.indexOf(")"));


                        if (!name.equals("") && !lastName.equals("")) {
                            userName = name;
                            userLastName = lastName;
                            userCurrency = currencyCode;

                            try {
                                SetFirstUser();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                                    mainIntent.putExtra("first", first);
                                    SplashActivity.this.startActivity(mainIntent);
                                    SplashActivity.this.finish();
                                }
                            }, SPLASH_LENGTH);
                        }
                    }
                });
            }
        });

        dialog.show();

    }
}
