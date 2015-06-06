package com.graviton.Cuzdan;

import Helpers.Billing.*;
import Helpers.ErrorDialog;
import Helpers.JSONHelper;
import Helpers.NotificationHelper;
import Helpers.User;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.Base64;

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
    String pros = "";
    Inventory inv;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.splash_fragment);

        String firstLight = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwPEw2hcLUtHvpxhc/vxeTH4MN9kv7TFcaNhkeeenj5xNqLDrXVmgUaL";
        String secondLight = "3KRsght+my50Yt3xjmDVS5NkMV7OZXE7VNKouvUCp12s5iJmoRCDfUpOxxrv3EtmJfYw+H9kwRpbQtDPm6giUEGjXGLO3mEbfbQ3qNOyeSU8hCgYRPsI";
        String thirdLight = "QrcH6p57y/kwR2+sI5AYTe++AqcjkgpNrpmP4cKLdGe3646G5FOLLv53deQgu26cBkCWKdRuZ3pEl/6CmlPO5bGyckplJlJIfI14Zw9seWrVBt6yrGe0zmy7eMFXA0hZRmp47hzNpzeR4E0mgsPVdSvvSP5xPq6AhjEDipyhPYQIDAQAB";

        String shineALight = firstLight.concat(secondLight).concat(thirdLight);

        iabHelper = new IabHelper(this, shineALight);

        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (result.isFailure()) {
                    ErrorDialog.ShowErrorDialog(getApplication(), null, "In-app billing hatası.", result.getMessage());
                    Log.d("BILLING", "Problem setting in-app billing: " + result);
                    return;
                }
                ((Global) getApplication()).iabHelper = iabHelper;
                try {
                    inv = iabHelper.queryInventory(true, null, null);
                    if (inv.hasPurchase("cuzdan_pro")) {
                        pros = "true";
                    } else {
                        pros = "false";
                    }
                } catch (IabException e) {
                    e.printStackTrace();
                    ErrorDialog.ShowErrorDialog(getApplication(), e, "In-app billing hatası.", null);
                }
            }
        });


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
                    NotificationHelper.SetPermaNotification(this, user.GetBanker().GetBalance(new Date(), false), user.GetCurrency());
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
                ErrorDialog.ShowErrorDialog(getApplication(), e, "Varolan kullanıcıyı okurken hata oluştu.", null);
                e.printStackTrace();
            }
        } else {

            // Kullanıcı yok, JSON üstünden yeni yaratıyoruz
            first = true;

            ((Global) this.getApplication()).SetFirst(true);
            CreateUserDialog();

        }
    }

    private void CreateBackupUser(File file) throws Exception {

        ((Global) this.getApplication()).SetFirst(false);

        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }

        String fileName = getString(R.string.cuzdanUserConfig);
        File main = new File(this.getFilesDir(), fileName);

        String info = new String(android.util.Base64.decode(sb.toString(), android.util.Base64.DEFAULT));

        JSONObject userJSON = new JSONObject(info);
        User user = new User(userJSON, main.getAbsolutePath(), getApplication());
        user.GetBanker().WriteUserInfo(userJSON.toString());

        user.SetUserNotifications(PendingIntent.getBroadcast(SplashActivity.this, 0, new Intent(SplashActivity.this, SavingsNotificationReceiver.class), 0), PendingIntent.getBroadcast(SplashActivity.this, 0, new Intent(SplashActivity.this, ReminderNotificationReceiver.class), 0));

        ((Global) this.getApplication()).SetUser(user);

        String sav = userJSON.getJSONObject("user").getString("notifications");
        String rem = userJSON.getJSONObject("user").getString("remNotifications");

        SetNotifications(Integer.parseInt(userJSON.getJSONObject("user").getString("savNotHour")), Integer.parseInt(userJSON.getJSONObject("user").getString("remNotHour")), sav, rem);

        if (user.GetStatusNotification().equals("true")) {
            NotificationHelper.SetPermaNotification(this, user.GetBanker().GetBalance(new Date(), false), user.GetCurrency());
        }

    }

    private void SetFirstUser() throws Exception {

        JSONObject userInf = JSONHelper.CreateStartingJSON(userName, userLastName, userCurrency, pros, "true", "true", "8", "14", "true");
        User user = new User(userInf, file.getAbsolutePath(), getApplication());
        user.GetBanker().WriteUserInfo(userInf.toString());

        ((Global) this.getApplication()).SetUser(user);
        SetNotifications(8, 14, "true", "true");
        NotificationHelper.SetPermaNotification(this, user.GetBanker().GetBalance(new Date(), false), user.GetCurrency());
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

    private void ShowErrorDialog(String message) {
        new AlertDialog.Builder(SplashActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Hata")
                .setMessage(message)
                .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }

                })
                .show();
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
        }).setNegativeButton("Yedekten Yükle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

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

                // Yeni kullanici
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
                                ErrorDialog.ShowErrorDialog(getApplication(), e, "Kullanici yaratirken hata oluştu.", null);
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

                Button backup = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                // Backup
                backup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String state = android.os.Environment.getExternalStorageState();

                        if (android.os.Environment.MEDIA_MOUNTED.equals(state)) {
                            File file = new File(Environment.getExternalStorageDirectory() + "/Documents");
                            File backupFile = new File(file.getAbsolutePath(), "cuzdanBackup");

                            if (!backupFile.exists()) {
                                ShowErrorDialog("Yedek bulunamadı.");
                            } else if (backupFile.exists()) {
                                try {
                                    CreateBackupUser(backupFile);
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
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                        } else {
                            ShowErrorDialog("Harici hafıza bulunamadı.");
                        }

                    }
                });
            }
        });

        dialog.show();

    }

}
