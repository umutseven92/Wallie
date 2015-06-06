package Helpers;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.SplittableRandom;


/**
 * Created by Umut Seven on 16.11.2014, for Graviton.
 * <p/>
 * Kullanici class'i. Tum bilgiler kullanici ustunden kaydediliyor.
 */
public class User implements Serializable {

    public User(JSONObject data, String filePath, Application app) throws Exception {

        assert data != null;
        JSONObject jsonUser = data.getJSONObject("user");
        _name = jsonUser.getString("name");
        _lastName = jsonUser.getString("lastName");
        this._filePath = filePath;
        _app = app;
        _currency = jsonUser.getString("currency");

        if (jsonUser.getString("pro").equals("true")) {
            _version = Version.Pro;
        } else {
            _version = Version.Free;
        }

        _notification = jsonUser.getString("notifications");
        _remNotification = jsonUser.getString("remNotifications");
        _savingNotHour = jsonUser.getInt("savNotHour");
        _remNotHour = jsonUser.getInt("remNotHour");
        _statusNotification = jsonUser.getString("statusNot");

        _banker = new Banker(jsonUser.getJSONArray("incomes"), jsonUser.getJSONArray("expenses"), jsonUser.getJSONArray("savings"), jsonUser.getJSONArray("incomeCustoms"), jsonUser.getJSONArray("expenseCustoms"), this._filePath, app, _currency);

        // Yeni featurelar buradan sonra
        // Auto Backup
        try
        {
            _autoBackup = jsonUser.getString("autoBackup");
        }
        catch (JSONException ex)
        {
            _autoBackup = "false";
            JSONObject json = new JSONObject(_banker.ReadUserInfo());
            json.getJSONObject("user").put("autoBackup", _autoBackup);
            _banker.WriteUserInfo(json.toString());
        }

    }

    public enum Version {
        Pro,
        Free
    }

    private Version _version;

    public Version GetVersion() {
        return _version;
    }

    public void SetVersion(Version version) {
        _version = version;
    }

    private String _autoBackup;

    public void SetAutoBackup(String auto)
    {
        _autoBackup = auto;
    }

    public String GetAutoBackup()
    {
        return _autoBackup;
    }

    private String _remNotification;

    private PendingIntent _reminderNotification;
    private PendingIntent _savingNotification;

    private String _statusNotification;

    public String GetStatusNotification() {
        return _statusNotification;
    }

    private int _savingNotHour;

    public void SetSavingNotHour(int hour) {
        _savingNotHour = hour;
    }

    public int GetSavingNotHour() {
        return _savingNotHour;
    }

    public int GetRemNotHour() {
        return _remNotHour;
    }

    private int _remNotHour;

    public void SetRemNotHour(int hour) {
        _remNotHour = hour;
    }

    public void SetUserNotifications(PendingIntent savNot, PendingIntent remNot) {
        _reminderNotification = remNot;
        _savingNotification = savNot;
    }

    private String _filePath;

    private Application _app;

    private String _name;

    private String _notification;

    public void ToggleStatusNotification(Context context) {
        if (_statusNotification.equals("true")) {
            _statusNotification = "false";
            NotificationHelper.RemoveNotification(context);
        } else {
            _statusNotification = "true";
            NotificationHelper.SetPermaNotification(context, this.GetBanker().GetBalance(new Date(), false), this.GetCurrency());
        }
    }

    public void ToggleAutoBackup()
    {
        if(_autoBackup.equals("true"))
        {
            _autoBackup = "false";
        }
        else
        {
            _autoBackup = "true";
        }
    }

    public void ToggleSavNotifications() {
        AlarmManager alarmManager = (AlarmManager) _app.getSystemService(Context.ALARM_SERVICE);

        if (_notification.equals("true")) {
            _notification = "false";
            alarmManager.cancel(_savingNotification);
        } else if (_notification.equals("false")) {
            _notification = "true";

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, _savingNotHour);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, _savingNotification);

        }

    }

    public void ToggleRemNotifications() {
        AlarmManager alarmManager = (AlarmManager) _app.getSystemService(Context.ALARM_SERVICE);

        if (_remNotification.equals("true")) {
            _remNotification = "false";
            alarmManager.cancel(_reminderNotification);
        } else if (_remNotification.equals("false")) {
            _remNotification = "true";

            Calendar calendarReminder = Calendar.getInstance();
            calendarReminder.set(Calendar.HOUR_OF_DAY, _remNotHour);
            calendarReminder.set(Calendar.MINUTE, 0);
            calendarReminder.set(Calendar.SECOND, 0);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendarReminder.getTimeInMillis(), AlarmManager.INTERVAL_DAY, _reminderNotification);
        }
    }

    public void UpdateRemNotification() {
        AlarmManager alarmManager = (AlarmManager) _app.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(_reminderNotification);

        Calendar calendarReminder = Calendar.getInstance();
        calendarReminder.set(Calendar.HOUR_OF_DAY, _remNotHour);
        calendarReminder.set(Calendar.MINUTE, 0);
        calendarReminder.set(Calendar.SECOND, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendarReminder.getTimeInMillis(), AlarmManager.INTERVAL_DAY, _reminderNotification);
    }

    public void UpdateSavNotification() {
        AlarmManager alarmManager = (AlarmManager) _app.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(_savingNotification);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, _savingNotHour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, _savingNotification);
    }

    public String GetNotifications() {
        return _notification;
    }

    public String GetRemNotifications() {
        return _remNotification;
    }

    private String _currency;

    public void SetCurrency(String currency) {
        _currency = currency;
    }

    public String GetCurrency() {
        return _currency;
    }

    public String GetName() {
        return _name;
    }

    public void SetName(String name) {
        _name = name;
    }

    private String _lastName;

    public String GetLastName() {
        return _lastName;
    }

    public void SetLastName(String lastName) {
        _lastName = lastName;
    }

    private Banker _banker;

    public Banker GetBanker() {
        return _banker;
    }

}
