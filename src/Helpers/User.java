package Helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Umut on 16.11.2014.
 */
public class User implements Serializable {

    public User(JSONObject data) throws JSONException, ParseException {
        if(data!= null)
        {
            JSONObject jsonUser = data.getJSONObject("user");
            _userName = jsonUser.getString("userName");
            _name = jsonUser.getString("name");
            _lastName =jsonUser.getString("lastName");
            _profilePicture = "@drawable/profile.png";
            JSONArray ar = jsonUser.getJSONArray("expenses");
            _banker = new Banker(jsonUser.getJSONArray("incomes"), jsonUser.getJSONArray("expenses"));

        }
    }

    private String _userName;

    public String GetUserName()
    {
        return _userName;
    }

    public void SetString(String userName)
    {
        _userName = userName;
    }

    private String _name;

    public String GetName()
    {
        return _name;
    }

    public void SetName(String name)
    {
        _name = name;
    }

    private String _lastName;

    public String GetLastName()
    {
        return _lastName;
    }

    public void SetLastName(String lastName)
    {
        _lastName = lastName;
    }

    private String _profilePicture;

    public String GetProfilePicture()
    {
        return _profilePicture;
    }

    public void SetProfilePicture(String profilePicture)
    {
        _profilePicture = profilePicture;
    }

    private Banker _banker;

    public Banker GetBanker()
    {
        return _banker;
    }

    public void SetBanker(Banker banker)
    {
        _banker = banker;
    }
}
