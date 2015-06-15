package Helpers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import com.graviton.Cuzdan.R;
import com.graviton.Cuzdan.SplashActivity;

import java.math.BigDecimal;

/**
 * Created by Umut Seven on 3.4.2015, for Graviton.
 */
public class NotificationHelper {

    public static void SetPermaNotification(Context context, BigDecimal amount, String cur) {

        String message = "";

        if (amount.compareTo(BigDecimal.ZERO) == -1) {
            message = context.getString(R.string.not_stat_lost) + " " + amount.negate() + " " + cur + " " + context.getString(R.string.not_stat_loss2);
        } else if (amount.compareTo(BigDecimal.ZERO) == 0) {
            message = context.getString(R.string.not_stat_equal);
        } else if (amount.compareTo(BigDecimal.ZERO) == 1) {
            message = context.getString(R.string.not_stat_prof1) + " " + amount + " " + cur + " " + context.getString(R.string.not_stat_prof2);
        }

        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.notification_logo)
                        .setLargeIcon(bmp)
                        .setContentTitle(context.getString(R.string.not_fast))
                        .setPriority(NotificationCompat.PRIORITY_MIN)
                        .setContentText(message);

        Intent resultIntent = new Intent(context, SplashActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(SplashActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification not = mBuilder.build();
        not.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;

        mNotificationManager.notify(31, not);
    }

    public static void RemoveNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(31);
    }
}
