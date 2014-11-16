package com.example.Cuzdan;

import Helpers.User;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class IncomeFragment extends Fragment {

    static User _user;

    public static void SetUser(User user)
    {
        _user = user;
    }

    public static final IncomeFragment newInstance()
    {
        IncomeFragment f = new IncomeFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.incomefragment, container, false);
        TextView txtUser = (TextView)v.findViewById(R.id.txtUserName);
        txtUser.setText(_user.GetUserName());
        return v;
    }
}
