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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

/**
 * Created by Umut Seven on 11.3.2015, for Graviton.
 */
public class ReminderNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        User user = null;

        try {

            String fileName = context.getString(R.string.cuzdanUserConfig);

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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (user.GetRemNotifications().equals("true")) {

            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);

            Intent intentToStart = new Intent(context, SplashActivity.class);
            PendingIntent pendingIntentToStart = PendingIntent.getActivity(context, 0, intentToStart, 0);

            Notification noti = new Notification.Builder(context).setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                    .setContentTitle("Cüzdan")
                    .setContentText("Günlük harcamalarınızı kaydettiniz mi?").setSmallIcon(R.drawable.ic_launcher).setLargeIcon(bm)
                    .setLights(Color.parseColor("#F39C12"), 5000, 5000)
                    .setContentIntent(pendingIntentToStart).build();
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            noti.flags |= Notification.FLAG_AUTO_CANCEL;

            notificationManager.notify(0, noti);
        }

    }
}
