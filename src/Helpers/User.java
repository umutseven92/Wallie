package Helpers;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Umut on 16.11.2014.
 */
public class User implements Serializable {

    public User(String userName)
    {
        _userName = userName;
        _banker = new Banker();
        _profilePicture = "@drawable/profile.png";
    }

    public User(JSONObject data) throws JSONException {
        if(data!= null)
        {
            _userName = data.getJSONObject("user").getString("userName");
             _banker = new Banker();
            _profilePicture = "@drawable/profile.png";
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
