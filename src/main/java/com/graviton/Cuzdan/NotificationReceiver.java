package com.graviton.Cuzdan;

import Helpers.User;
import android.app.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Umut Seven on 8.3.2015, for Graviton.
 */
public class NotificationReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        BigDecimal todayLimit = BigDecimal.ZERO;
        BigDecimal todayBalance = BigDecimal.ZERO;

        User user = null;

        try {

            String fileName = "userConfigTest54";

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
            if (user.GetBanker().GetSavings().size() > 0 && todayLimit.compareTo(BigDecimal.ZERO) > 0) {
                String message = "";

                if (todayBalance.compareTo(todayLimit) == 0) {
                    message = "Birikim hedefinize ulaştınız";
                } else if (todayBalance.compareTo(todayLimit) == 1) {
                    BigDecimal offset = todayBalance.subtract(todayLimit).setScale(2, BigDecimal.ROUND_DOWN);
                    message = "Bugün içinde " + offset.toString() + " " + context.getString(R.string.currency) + " daha harcayabilirsiniz.";
                } else if (todayBalance.compareTo(todayLimit) == -1) {
                    BigDecimal offset = todayLimit.subtract(todayBalance).setScale(2, BigDecimal.ROUND_DOWN);
                    message = "Birikim hedefinizi " + offset.toString() + " " + context.getString(R.string.currency) + " aştınız.";
                }


                PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
                Uri notificationSoundPath = Uri.parse("android.resource://com.graviton.Cuzdan/" + R.raw.cuzdan_notification);

                Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);

                Notification noti = new Notification.Builder(context).setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                        .setContentTitle("Cüzdan birikim")
                        .setContentText(message).setSmallIcon(R.drawable.ic_launcher).setLargeIcon(bm)
                        .setContentIntent(pIntent).build();
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                noti.flags |= Notification.FLAG_AUTO_CANCEL;

                notificationManager.notify(0, noti);
            }
        }
            catch(Exception e){
                e.printStackTrace();
            }

    }

}
