package com.graviton.Cuzdan;

import Helpers.User;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.math.BigDecimal;
import java.util.Date;


/**
 * Created by Umut Seven on 3.3.2015, for Graviton.
 */
public class NotificationAlarmService extends Service {
     @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate()
    {
        User user = ((Global)getApplication()).GetUser();
        BigDecimal todayBalance = user.GetBanker().GetBalance(new Date(), true);
        BigDecimal todayLimit = user.GetBanker().GetTotalSavingLimit();

        String message = "";
        if(todayBalance.compareTo(todayLimit) == 0)
        {
            message = "Birikim hedefinize ulaştınız";
        }
        else if(todayBalance.compareTo(todayLimit) == 1)
        {
            BigDecimal offset = todayBalance.subtract(todayLimit).setScale(2, BigDecimal.ROUND_DOWN);
            message = "Bugün içinde " + offset.toString() + " " + getString(R.string.currency) + " daha harcayabilirsiniz.";
        }
        else if(todayBalance.compareTo(todayLimit) == -1)
        {
            BigDecimal offset = todayLimit.subtract(todayBalance).setScale(2, BigDecimal.ROUND_DOWN);
            message = "Birikim hedefinizi " + offset.toString() + " " + getString(R.string.currency) + " aştınız.";
        }


        Intent intent = new Intent(this, SplashActivity.class);

        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification noti = new Notification.Builder(this).setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setContentTitle("Cüzdan birikim")
                .setContentText(message).setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pIntent).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);
    }
}
