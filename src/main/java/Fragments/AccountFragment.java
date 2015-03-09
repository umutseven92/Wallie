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
 * Created by Umut Seven on 18.11.2014, for Graviton.
 */
public class AccountFragment extends Fragment {

    static User _user;

    public static final AccountFragment newInstance() {
        AccountFragment f = new AccountFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.account_fragment, container, false);
        TextView txtName = (TextView) v.findViewById(R.id.txtName);
        TextView txtVersion = (TextView) v.findViewById(R.id.txtVersion);

        _user = ((com.graviton.Cuzdan.Global) getActivity().getApplication()).GetUser();
        txtName.setText(_user.GetName() + " " + _user.GetLastName());
        txtVersion.setText(R.string.version);
        return v;
    }
}
