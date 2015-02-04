package Helpers;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Created by Umut Seven on 4.2.2015, for Graviton.
 */
public class Saving {

    public Saving() {
        InitializePeriodDayDict();
    }

    private final int DAY = 1;
    private final int WEEK = 7;
    private final int MONTH = 30;
    private final int THREE_MONTHS = 90;
    private final int SIX_MONTHS = 180;
    private final int YEAR = 365;

    public enum Period {
        Day,
        Week,
        Month,
        ThreeMonths,
        SixMonths,
        Year,
        Custom
    }

    private Period _savingPeriod;

    public void SetPeriod(Period period) {
        _savingPeriod = period;
    }

    public Period GetPeriod() {
        return _savingPeriod;
    }

    private HashMap<Period, Integer> _periodDayDict = new HashMap<Period, Integer>();

    private void InitializePeriodDayDict() {
        _periodDayDict.put(Period.Day, DAY);
        _periodDayDict.put(Period.Week, WEEK);
        _periodDayDict.put(Period.Month, MONTH);
        _periodDayDict.put(Period.ThreeMonths, THREE_MONTHS);
        _periodDayDict.put(Period.SixMonths, SIX_MONTHS);
        _periodDayDict.put(Period.Year, YEAR);
    }

    private String _desription;

    public void SetDescription() {
        _desription = CreateDescription(_name, _periodDayDict.get(_savingPeriod), _amount);
    }

    public String CreateDescription(String name, int totalDays, BigDecimal amount) {

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

        String amountPart = String.format("sonunda %s TL birikim.", amount.setScale(2, BigDecimal.ROUND_DOWN).toString());

        return namePart + periodPart + amountPart;

    }

    public String GetDescription() {
        return _desription;
    }

    private String _name;

    public void SetName(String name) {
        _name = name;
    }

    public String GetName() {
        return _name;
    }

    private BigDecimal _amount;

    public void SetAmount(BigDecimal amount) {
        _amount = amount;
    }

    private boolean _repeating;

    public void SetRepeating(boolean rep) {
        _repeating = rep;
    }

    public boolean GetRepeating() {
        return _repeating;
    }
}
