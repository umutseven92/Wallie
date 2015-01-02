package com.graviton.Cuzdan;

import Helpers.User;
import android.app.Application;

/**
 * Created by Umut on 18.11.2014.
 */
public class Global extends Application {

    private User _user;

    public void SetUser(User user)
    {
        _user = user;
    }

    public User GetUser()
    {
        return _user;
    }

    private String _filePath;

    public void SetFilePath(String path)
    {
        _filePath = path;
    }

    public String GetFilePath()
    {
        return _filePath;
    }
}
