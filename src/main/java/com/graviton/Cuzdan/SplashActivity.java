package com.graviton.Cuzdan;

import Helpers.Billing.IabHelper;
import Helpers.Billing.IabResult;
import Helpers.Billing.Inventory;
import Helpers.JSONHelper;
import Helpers.NotificationHelper;
import Helpers.User;
import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import org.json.JSONObject;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Umut Seven on 11.11.2014, for Graviton.
 */
public class SplashActivity extends Activity {

    private final int SPLASH_LENGTH = 1000;
    boolean first = true;
    File file;
    private String userName;
    private String userLastName;
    private String userCurrency;

    // Billing
    IabHelper iabHelper;
    boolean pro;
    boolean completed = false;
    IabHelper.QueryInventoryFinishedListener gotInventoryListener;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.splash_fragment);


        // Not = Bu key simdilik bu halde
        // Bunu sonra daha secure yap
        String shineALight = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwPEw2hcLUtHvpxhc/vxeTH4MN9kv7TFcaNhkeeenj5xNqLDrXVmgUaL3KRsght+my50Yt3xjmDVS5NkMV7OZXE7VNKouvUCp12s5iJmoRCDfUpOxxrv3EtmJfYw+H9kwRpbQtDPm6giUEGjXGLO3mEbfbQ3qNOyeSU8hCgYRPsIQrcH6p57y/kwR2+sI5AYTe++AqcjkgpNrpmP4cKLdGe3646G5FOLLv53deQgu26cBkCWKdRuZ3pEl/6CmlPO5bGyckplJlJIfI14Zw9seWrVBt6yrGe0zmy7eMFXA0hZRmp47hzNpzeR4E0mgsPVdSvvSP5xPq6AhjEDipyhPYQIDAQAB";

        iabHelper = new IabHelper(this, shineALight);

        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (result.isFailure()) {
                    Log.d("BILLING", "Problem setting in-app billing: " + result);
                    return;
                }
                ((Global) getApplication()).iabHelper = iabHelper;
                iabHelper.queryInventoryAsync(gotInventoryListener);
            }
        });


        gotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
            public void onQueryInventoryFinished(IabResult result, Inventory inv) {

                if (result.isFailure()) {
                    Log.d("BILLING", "Could not get inventory: " + result);
                    return;
                } else {
                    pro = (inv.hasPurchase("cuzdan_pro"));
                    completed = true;
                }
            }
        };


        String fileName = getString(R.string.cuzdanUserConfig);
        ((Global) this.getApplication()).SetFilePath(fileName);

        file = new File(this.getFilesDir(), fileName);

        // Tarih formatı ISO 8601 (YEAR-MONTH-DATE)
        if (file.exists()) {

            // Kullanıcı var, JSON üstünden yüklüyoruz
            first = false;
            ((Global) this.getApplication()).SetFirst(false);

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

                String sav = userJSON.getJSONObject("user").getString("notifications");
                String rem = userJSON.getJSONObject("user").getString("remNotifications");

                SetNotifications(Integer.parseInt(userJSON.getJSONObject("user").getString("savNotHour")), Integer.parseInt(userJSON.getJSONObject("user").getString("remNotHour")), sav, rem);

                if (user.GetStatusNotification().equals("true")) {
                    NotificationHelper.SetPermaNotification(this, user.GetBanker().GetBalance(new Date(), true), user.GetCurrency());
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

            ((Global) this.getApplication()).SetFirst(true);
            CreateUserDialog();

        }
    }


    private void SetFirstUser() throws Exception {
        String proString = "";

        if (pro) {
            proString = "true";
        } else {
            proString = "false";
        }

        JSONObject userInf = JSONHelper.CreateStartingJSON(userName, userLastName, userCurrency, proString, "true", "true", "8", "14", "true");
        User user = new User(userInf, file.getAbsolutePath(), getApplication());
        user.GetBanker().WriteUserInfo(userInf.toString());

        ((Global) this.getApplication()).SetUser(user);
        SetNotifications(8, 14, "true", "true");
        NotificationHelper.SetPermaNotification(this, user.GetBanker().GetBalance(new Date(), true), user.GetCurrency());
    }

    private void SetNotifications(int savNotHour, int remNotHour, String sav, String rem) {
        if (sav.equals("true")) {

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, savNotHour);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            SetNotification(calendar, SavingsNotificationReceiver.class);
        }
        if (rem.equals("true")) {
            Calendar calendarReminder = Calendar.getInstance();
            calendarReminder.set(Calendar.HOUR_OF_DAY, remNotHour);
            calendarReminder.set(Calendar.MINUTE, 0);
            calendarReminder.set(Calendar.SECOND, 0);
            SetNotification(calendarReminder, ReminderNotificationReceiver.class);
        }

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
