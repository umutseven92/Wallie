package com.graviton.Cuzdan;

import Helpers.WidgetHelper;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Created by Umut Seven on 8.4.2015, for Graviton.
 */
public class CuzdanWidgetProvider extends AppWidgetProvider {

    @Override
    public void onEnabled(Context context) {

        try {
            WidgetHelper.UpdateInfo(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Intent intent = new Intent(context, SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.cuzdan_widget);
        views.setOnClickPendingIntent(R.id.btnWidgetBalance, pendingIntent);

        ComponentName thisWidget = new ComponentName(context, CuzdanWidgetProvider.class);

        appWidgetManager.updateAppWidget(thisWidget, views);

        try {
            WidgetHelper.UpdateInfo(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        super.onReceive(context, intent);
    }
}
