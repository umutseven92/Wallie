package Helpers;

import android.content.Context;
import com.graviton.Cuzdan.R;

import java.math.BigDecimal;
import java.util.Locale;

/**
 * Created by Umut Seven on 14.2.2015, for Graviton.
 */
public class SavingsHelper {

    private static final int DAY = 1;
    private static final int WEEK = 7;
    private static final int MONTH = 30;
    private static final int THREE_MONTHS = 90;
    private static final int SIX_MONTHS = 180;
    private static final int YEAR = 365;

    /**
     * Birikimin user-friendly aciklamasini yaratiyoruz.
     * Ornek: "Araba icin, 1 yil 4 ay 3 hafta 2 gun sonunda 14000 TRY birikim"
     *
     * @param name      Birikim sebebi
     * @param totalDays Yil / ay / haftaya cevrilecek toplam gun sayisi
     * @param amount    Birikim miktari
     * @return Birikim aciklamasi
     */
    public static String CreateDescription(String name, int totalDays, BigDecimal amount, String currency) {

        String namePart, periodPart, amountPart;

        if (Locale.getDefault().getLanguage().equals("tr")) {
            namePart = String.format("%s için", name);
            periodPart = "";

            int years = totalDays / YEAR;
            if (years > 0) {
                periodPart += String.format("%s yıl ", years);
                totalDays -= years * YEAR;
            }
            int months = totalDays / MONTH;
            if (months > 0) {
                periodPart += String.format("%s ay ", months);
                totalDays -= months * MONTH;
            }
            int weeks = totalDays / WEEK;
            if (weeks > 0) {
                periodPart += String.format("%s hafta ", weeks);
                totalDays -= weeks * WEEK;
            }
            int days = totalDays / DAY;
            if (days > 0) {
                periodPart += String.format("%s gün ", days);
                totalDays -= days * DAY;
            }

            amountPart = String.format("sonunda %s " + currency + " birikim.", amount.setScale(2, BigDecimal.ROUND_DOWN).toString());
        } else {
            periodPart = "";

            int years = totalDays / YEAR;
            if (years > 0) {
                periodPart += String.format("%s year(s) ", years);
                totalDays -= years * YEAR;
            }
            int months = totalDays / MONTH;
            if (months > 0) {
                periodPart += String.format("%s month(s) ", months);
                totalDays -= months * MONTH;
            }
            int weeks = totalDays / WEEK;
            if (weeks > 0) {
                periodPart += String.format("%s week(s) ", weeks);
                totalDays -= weeks * WEEK;
            }
            int days = totalDays / DAY;
            if (days > 0) {
                periodPart += String.format("%s day(s) ", days);
                totalDays -= days * DAY;
            }

            namePart = String.format("worth of savings, named %s, ", name);
            amountPart = String.format("valued at %s." + currency, amount.setScale(2, BigDecimal.ROUND_DOWN).toString());
        }

        return periodPart + namePart + amountPart;

    }

    public static String GetPeriodString(Saving.Period period, Context context) {
        String periodRep = "";

        switch (period) {
            case Day:
                periodRep = context.getString(R.string.day);
                break;
            case Week:
                periodRep = context.getString(R.string.week);
                break;
            case Month:
                periodRep = context.getString(R.string.month);
                break;
            case ThreeMonths:
                periodRep = context.getString(R.string.three_months);
                break;
            case SixMonths:
                periodRep = context.getString(R.string.six_months);
                break;
            case Year:
                periodRep = context.getString(R.string.year);
                break;
            case Custom:
                periodRep = context.getString(R.string.custom);
                break;
        }
        return periodRep;
    }

    public static BigDecimal CalculateDailyLimit(BigDecimal revenue, Saving sav) {
        BigDecimal totalLimit = revenue.subtract(sav.GetAmount());

        BigDecimal dailyLimit = totalLimit.divide(new BigDecimal(sav.GetRemainingDays()), BigDecimal.ROUND_DOWN);
        return dailyLimit.setScale(2, BigDecimal.ROUND_DOWN);

    }

}
