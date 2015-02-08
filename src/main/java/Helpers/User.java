package Helpers;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;


/**
 * Created by Umut Seven on 16.11.2014, for Graviton.
 *
 * Kullanici class'i. Tum bilgiler kullanici ustunden kaydediliyor.
 */
public class User implements Serializable {

    public User(JSONObject data, String filePath) throws JSONException, ParseException {

        assert data != null;
        JSONObject jsonUser = data.getJSONObject("user");
        _userName = jsonUser.getString("userName");
        _name = jsonUser.getString("name");
        _lastName = jsonUser.getString("lastName");
        _profilePicture = "@drawable/profile.png";
        this._filePath = filePath;
        _banker = new Banker(jsonUser.getJSONArray("incomes"), jsonUser.getJSONArray("expenses"), this._filePath);

    }

    private String _filePath;

    private String _userName;

    public String GetUserName() {
        return _userName;
    }

    public void SetString(String userName) {
        _userName = userName;
    }

    private String _name;

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

    private String _profilePicture;

    public String GetProfilePicture() {
        return _profilePicture;
    }

    public void SetProfilePicture(String profilePicture) {
        _profilePicture = profilePicture;
    }

    private Banker _banker;

    public Banker GetBanker() {
        return _banker;
    }

}
