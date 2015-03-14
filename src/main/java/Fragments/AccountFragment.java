package Fragments;

import Helpers.User;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import com.graviton.Cuzdan.R;

/**
 * Created by Umut Seven on 18.11.2014, for Graviton.
 */
public class AccountFragment extends Fragment {

    static User _user;
    TextView txtName, txtCurrency;
    Switch swcSaving;

    public static final AccountFragment newInstance() {
        AccountFragment f = new AccountFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.account_fragment, container, false);
        txtName = (TextView) v.findViewById(R.id.txtName);
        txtCurrency = (TextView) v.findViewById(R.id.txtAccountCurrency);
        swcSaving = (Switch)v.findViewById(R.id.swcAccountSaving);

        TextView txtVersion = (TextView) v.findViewById(R.id.txtVersion);

        _user = ((com.graviton.Cuzdan.Global) getActivity().getApplication()).GetUser();
        txtVersion.setText(R.string.version);
        return v;
    }

    @Override
    public void onResume() {
        txtName.setText(_user.GetName() + " " + _user.GetLastName());
        txtCurrency.setText(_user.GetCurrency());
        super.onResume();
    }
}
