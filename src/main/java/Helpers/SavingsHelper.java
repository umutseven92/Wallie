package Helpers;

import java.math.BigDecimal;

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

        String namePart = String.format("%s için, ", name);
        String periodPart = "";

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

        String amountPart = String.format("sonunda %s " + currency + " birikim.", amount.setScale(2, BigDecimal.ROUND_DOWN).toString());

        return namePart + periodPart + amountPart;

    }

    public static String GetPeriodString(Saving.Period period) {
        String periodRep = "";

        switch (period) {
            case Day:
                periodRep = "Gün";
                break;
            case Week:
                periodRep = "Hafta";
                break;
            case Month:
                periodRep = "Ay";
                break;
            case ThreeMonths:
                periodRep = "3 Ay";
                break;
            case SixMonths:
                periodRep = "6 Ay";
                break;
            case Year:
                periodRep = "Yıl";
                break;
            case Custom:
                periodRep = "Özel";
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
