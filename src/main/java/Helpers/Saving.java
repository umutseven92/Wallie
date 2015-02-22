package Helpers;

import org.json.JSONException;
import org.json.JSONObject;

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
        this.SetDescription(jsonSaving.getString("name"), _periodDayDict.get(GetPeriodFromString(jsonSaving.getString("period"))), new BigDecimal(jsonSaving.getDouble("amount")));
        if (jsonSaving.getString("repeating").equals("true")) {
            this.SetRepeating(true);
        } else {
            this.SetRepeating(false);
        }

        int days = _periodDayDict.get(this.GetPeriod());
        BigDecimal daily = this.GetAmount().divide(new BigDecimal(days), BigDecimal.ROUND_DOWN);
        this.SetDailyGoal(daily);
        this.SetPriority(jsonSaving.getInt("priority"));
    }

    public Saving(String name, BigDecimal amount, Date date, Period period, boolean repeating, int priority) {
        InitializePeriodDayDict();
        this.GenerateID();
        this.SetName(name);
        this.SetAmount(amount);
        this.SetDate(date);
        this.SetPeriod(period);
        this.SetDescription(name, _periodDayDict.get(period), amount);
        this.SetRepeating(repeating);

        this.SetProgress(BigDecimal.ZERO);

        int days = _periodDayDict.get(period);
        this.SetRemainingDays(days);
        BigDecimal daily = this.GetAmount().divide(new BigDecimal(days), BigDecimal.ROUND_DOWN);
        this.SetDailyGoal(daily);

        this.SetPriority(priority);
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

    public int GetTotalDays(Period period)
    {
        return _periodDayDict.get(period);
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
        _desription = SavingsHelper.CreateDescription(name, totalDays, amount);
    }

    public String GetDescription() {
        return _desription;
    }

    public int GetDays(Period period) {
        return _periodDayDict.get(period);
    }

    private String _name;

    public void SetName(String name) {
        _name = name;
    }

    public String GetName() {
        return _name;
    }

    private BigDecimal _progress;

    public BigDecimal GetProgress()
    {
        return _progress;
    }

    public void SetProgress(BigDecimal progress)
    {
        _progress = progress;
    }

    private BigDecimal _dailyLimit;

    public BigDecimal GetDailyLimit()
    {
        return _dailyLimit;
    }

    public void SetDailyLimit(BigDecimal rev)
    {
        _dailyLimit = SavingsHelper.CalculateDailyLimit(rev,this);
    }

    private int _remainingDays;

    public int GetRemainingDays()
    {
        return _remainingDays;
    }

    public void SetRemainingDays(int remainingDays)
    {
        _remainingDays = remainingDays;
    }

    private BigDecimal _amount;

    public BigDecimal GetAmount() {
        return _amount.setScale(2, BigDecimal.ROUND_DOWN);
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

    private void SetID(String id) {
        _id = id;
    }

    private Date _date;

    public void SetDate(Date date) {
        _date = date;
    }

    public Date GetDate() {
        return _date;
    }

    private BigDecimal _dailyGoal;

    public void SetDailyGoal(BigDecimal dailyGoal) {
        _dailyGoal = dailyGoal;
    }

    public BigDecimal GetDailyGoal() {
        return _dailyGoal;
    }

    private int _priority;

    public void SetPriority(int priority)
    {
        _priority = priority;
    }

    public int GetPriority()
    {
        return _priority;
    }
}
