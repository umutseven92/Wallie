package Helpers;

import android.content.res.Resources;
import com.graviton.Cuzdan.R;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Umut Seven on 28.1.2015, for Graviton.
 */
public class DateFormatHelper {
    public static String GetDayText(Date date)
    {
        Format formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }

    public static String GetMonthText(Date date, Resources res)
    {
        Format formatter = new SimpleDateFormat("MM");
        String[] months = res.getStringArray(R.array.turkishMonths);
        String month = "m";

        switch (Integer.parseInt(formatter.format(date)))
        {
            case 1:
                month = months[0];
                break;
            case 2:
                month = months[1];
                break;
            case 3:
                month = months[2];
                break;
            case 4:
                month = months[3];
                break;
            case 5:
                month = months[4];
                break;
            case 6:
                month = months[5];
                break;
            case 7:
                month = months[6];
                break;
            case 8:
                month = months[7];
                break;
            case 9:
                month = months[8];
                break;
            case 10:
                month = months[9];
                break;
            case 11:
                month = months[10];
                break;
            case 12:
                month = months[11];
                break;

        }

        Format formatterYear = new SimpleDateFormat("yyyy");
        month += " " + formatterYear.format(date);
        return month;
    }
}
