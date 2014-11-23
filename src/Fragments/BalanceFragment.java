package Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.Cuzdan.R;

/**
 * Created by Umut on 22.11.2014.
 */
public class BalanceFragment extends Fragment {

    public static final BalanceFragment newInstance()
    {
        BalanceFragment f = new BalanceFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.balancefragment, container, false);
        return v;
    }

}
