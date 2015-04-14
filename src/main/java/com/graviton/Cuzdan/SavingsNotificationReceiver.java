package com.graviton.Cuzdan;

import Helpers.User;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Umut Seven on 8.3.2015, for Graviton.
 */
public class SavingsNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        BigDecimal todayLimit = BigDecimal.ZERO;
        BigDecimal todayBalance = BigDecimal.ZERO;

        User user = null;

        try {

            String fileName = context.getString(R.string.configVersion);

            File file = new File(context.getFilesDir(), fileName);
            if (!file.exists()) {
                return;
            }
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }

            user = new User(new JSONObject(sb.toString()), file.getAbsolutePath(), null);
            todayBalance = user.GetBanker().GetBalance(new Date(), false);
            todayLimit = user.GetBanker().GetTotalSavingLimit();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (user.GetBanker().GetSavings().size() > 0 && todayLimit.compareTo(BigDecimal.ZERO) > 0 && user.GetNotifications().equals("true")) {
                String message = "";

                if (todayBalance.compareTo(todayLimit) == 0) {
                    message = "Birikim hedefinize ulaştınız";
                } else if (todayBalance.compareTo(todayLimit) == 1) {
                    BigDecimal offset = todayBalance.subtract(todayLimit).setScale(2, BigDecimal.ROUND_DOWN);
                    message = "Bugün içinde " + offset.toString() + " " + user.GetCurrency() + " daha harcayabilirsiniz.";
                } else if (todayBalance.compareTo(todayLimit) == -1) {
                    BigDecimal offset = todayLimit.subtract(todayBalance).setScale(2, BigDecimal.ROUND_DOWN);
                    message = "Birikim hedefinizi " + offset.toString() + " " + user.GetCurrency() + " aştınız.";
                }

                Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);

                Intent intentToStart = new Intent(context, SplashActivity.class);
                PendingIntent pendingIntentToStart = PendingIntent.getActivity(context, 0, intentToStart, 0);

                Notification noti = new Notification.Builder(context)
                        .setContentTitle("Cüzdan Birikim")
                        .setContentText(message).setSmallIcon(R.drawable.ic_launcher).setLargeIcon(bm)
                        .setLights(Color.parseColor("#F39C12"), 5000, 5000)
                        .setContentIntent(pendingIntentToStart).build();
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                noti.flags |= Notification.FLAG_AUTO_CANCEL;

                notificationManager.notify(0, noti);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
