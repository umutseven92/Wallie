package Helpers;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;
import com.graviton.Cuzdan.CuzdanWidgetProvider;
import com.graviton.Cuzdan.R;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Umut Seven on 13.4.2015, for Graviton.
 */
public class WidgetHelper {

    private static User user;

    public static void UpdateInfo(Context context) throws Exception {

        if (user == null) {
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
        }

        BigDecimal todayIncome = user.GetBanker().GetTotalMonthIncome(new Date());
        BigDecimal todayExpense = user.GetBanker().GetTotalMonthExpense(new Date());
        BigDecimal todayBalance = user.GetBanker().GetBalance(new Date(), false);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.cuzdan_widget);

        views.setTextViewText(R.id.txtWidgetIncome, todayIncome.toString());
        views.setTextViewText(R.id.txtWidgetExpense, todayExpense.toString());
        views.setTextViewText(R.id.txtWidgetBalance, todayBalance.toString() + " " + user.GetCurrency());

        if (todayBalance.compareTo(BigDecimal.ZERO) < 1) {
            views.setTextColor(R.id.txtWidgetBalance, context.getResources().getColor(R.color.cuzdan_red));
        } else {
            views.setTextColor(R.id.txtWidgetBalance, context.getResources().getColor(R.color.cuzdan_green));
        }

        ComponentName thisWidget = new ComponentName(context, CuzdanWidgetProvider.class);

        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thisWidget, views);
    }


    public static void UpdateInfoWithUser(Context context, User user) throws Exception {

        BigDecimal todayIncome = user.GetBanker().GetTotalMonthIncome(new Date());
        BigDecimal todayExpense = user.GetBanker().GetTotalMonthExpense(new Date());
        BigDecimal todayBalance = user.GetBanker().GetBalance(new Date(), false);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.cuzdan_widget);

        views.setTextViewText(R.id.txtWidgetIncome, todayIncome.toString());
        views.setTextViewText(R.id.txtWidgetExpense, todayExpense.toString());
        views.setTextViewText(R.id.txtWidgetBalance, todayBalance.toString() + " " + user.GetCurrency());

        if (todayBalance.compareTo(BigDecimal.ZERO) < 1) {
            views.setTextColor(R.id.txtWidgetBalance, context.getResources().getColor(R.color.cuzdan_red));
        } else {
            views.setTextColor(R.id.txtWidgetBalance, context.getResources().getColor(R.color.cuzdan_green));
        }

        ComponentName thisWidget = new ComponentName(context, CuzdanWidgetProvider.class);

        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thisWidget, views);
    }
}
