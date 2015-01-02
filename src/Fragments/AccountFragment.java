package Fragments;

import Helpers.User;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.graviton.Cuzdan.R;

/**
 * Created by Umut on 18.11.2014.
 */
public class AccountFragment extends Fragment {

    static User _user;

    public static final AccountFragment newInstance()
    {
        AccountFragment f = new AccountFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.accountfragment, container, false);
        TextView txtUserName = (TextView)v.findViewById(R.id.txtUserName);
        TextView txtName = (TextView)v.findViewById(R.id.txtName);

        _user = ((com.graviton.Cuzdan.Global) getActivity().getApplication()).GetUser();
        txtUserName.setText(_user.GetUserName());
        txtName.setText(_user.GetName() + " " + _user.GetLastName());
        return v;
    }
}
