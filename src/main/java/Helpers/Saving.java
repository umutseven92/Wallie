package Helpers;

import org.json.JSONException;
import org.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Umut Seven on 4.2.2015, for Graviton.
 */
public class Saving {

    public Saving(JSONObject jsonSaving) throws JSONException, ParseException {
        InitializePeriodDayDict();
        this.SetID(jsonSaving.getString("id"));
        this.SetName(jsonSaving.getString("name"));
        this.SetAmount(new BigDecimal(jsonSaving.getDouble("amount")));
        Date d = new SimpleDateFormat("yyyy-MM-d").parse(jsonSaving.getString("date"));
        this.SetDate(d);
        this.SetPeriod(GetPeriodFromString(jsonSaving.getString("period")));
        this.SetDescription(jsonSaving.getString("name"),_periodDayDict.get(GetPeriodFromString(jsonSaving.getString("period"))),new BigDecimal(jsonSaving.getDouble("amount")));
        if(jsonSaving.getString("repeating").equals("true"))
        {
            this.SetRepeating(true);
        }
        else
        {
            this.SetRepeating(false);
        }
    }

    public Saving(String name, BigDecimal amount, Date date, Period period, boolean repeating) {
        InitializePeriodDayDict();
        this.GenerateID();
        this.SetName(name);
        this.SetAmount(amount);
        this.SetDate(date);
        this.SetPeriod(period);
        this.SetDescription(name, _periodDayDict.get(period), amount);
        this.SetRepeating(repeating);
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

    private Period GetPeriodFromString(String period) {
        Period per = null;

        if (period.equals("day")) {
            per = Period.Day;
        } else if (period.equals("week")) {
            per = Period.Week;
        } else if (period.equals("month")) {
            per = Period.Month;
        } else if (period.equals("three_months")) {
            per = Period.ThreeMonths;
        } else if (period.equals("six_months")) {
            per = Period.SixMonths;
        } else if (period.equals("year")) {
            per = Period.Year;
        } else if (period.equals("custom")) {
            per = Period.Custom;
        }

        assert per != null;

        return per;
    }

    private HashMap<Period, Integer> _periodDayDict = new HashMap<Period, Integer>();

    /**
     * Birikim periyotlarini gun sayilarina bagliyoruz.
     */
    private void InitializePeriodDayDict() {
        _periodDayDict.put(Period.Day, DAY);
        _periodDayDict.put(Period.Week, WEEK);
        _periodDayDict.put(Period.Month, MONTH);
        _periodDayDict.put(Period.ThreeMonths, THREE_MONTHS);
        _periodDayDict.put(Period.SixMonths, SIX_MONTHS);
        _periodDayDict.put(Period.Year, YEAR);
    }

    private String _desription;

    private void SetDescription(String name, int totalDays, BigDecimal amount) {
        _desription = CreateDescription(name, totalDays, amount);
    }

    public String GetDescription()
    {
        return _desription;
    }

    public int GetDays(Period period) {
        return _periodDayDict.get(period);
    }

    /**
     * Birikimin user-friendly aciklamasini yaratiyoruz.
     * Ornek: "Araba icin, 1 yil 4 ay 3 hafta 2 gun sonunda 14000 TL birikim"
     *
     * @param name      Birikim sebebi
     * @param totalDays Yil / ay / haftaya cevrilecek toplam gun sayisi
     * @param amount    Birikim miktari
     * @return Birikim aciklamasi
     */
    private String CreateDescription(String name, int totalDays, BigDecimal amount) {

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

    private String _name;

    public void SetName(String name) {
        _name = name;
    }

    public String GetName() {
        return _name;
    }

    private BigDecimal _amount;

    public BigDecimal GetAmount() {
        return _amount;
    }

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

    private String _id;

    public void GenerateID() {
        _id = UUID.randomUUID().toString();
    }

    public String GetID() {
        return _id;
    }

    private void SetID(String id)
    {
        _id = id;
    }

    private Date _date;

    public void SetDate(Date date) {
        _date = date;
    }

    public Date GetDate() {
        return _date;
    }

}
