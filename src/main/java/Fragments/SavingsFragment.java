package Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.graviton.Cuzdan.R;

public class SavingsFragment extends Fragment {

    View v;
    int savingsCount = 0;

    public static final SavingsFragment newInstance() {
        SavingsFragment f = new SavingsFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.savings_fragment, container, false);

        RelativeLayout lytNoSavings = (RelativeLayout)v.findViewById(R.id.lytEmptySavings);
        RelativeLayout lytSavings = (RelativeLayout)v.findViewById(R.id.lytSavingsList);
        Button btnAddSaving = (Button)v.findViewById(R.id.btnAddSaving);

        if(savingsCount <= 0)
        {
            lytNoSavings.setVisibility(View.VISIBLE);
            lytSavings.setVisibility(View.INVISIBLE);
            btnAddSaving.setText("Birikime Başla");
        }
        else if(savingsCount > 0)
        {
            lytNoSavings.setVisibility(View.INVISIBLE);
            lytSavings.setVisibility(View.VISIBLE);
            btnAddSaving.setText("Birikim Ekle");
        }
        btnAddSaving.setTextColor(getResources().getColor(R.color.foreground));

        return v;
    }
}
