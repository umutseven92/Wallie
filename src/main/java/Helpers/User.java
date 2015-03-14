package Helpers;

import android.app.Application;
import org.json.JSONObject;

import java.io.Serializable;


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
        _banker = new Banker(jsonUser.getJSONArray("incomes"), jsonUser.getJSONArray("expenses"), jsonUser.getJSONArray("savings"), this._filePath, app);
        _currency = jsonUser.getString("currency");
    }

    private String _filePath;

    private String _name;

    private String _currency;

    public void SetCurrency(String currency)
    {
        _currency = currency;
    }

    public String GetCurrency()
    {
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
