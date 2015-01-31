package Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.graviton.Cuzdan.R;

public class SavingsFragment extends Fragment {

    public static final SavingsFragment newInstance()
    {
        SavingsFragment f = new SavingsFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.savings_fragment, container, false);
        return v;
    }
}
