package Fragments;

import Helpers.User;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.graviton.Cuzdan.Global;
import com.graviton.Cuzdan.R;
import com.graviton.Cuzdan.SavingsWizardActivity;

public class SavingsFragment extends Fragment {

    View v;
    int savingsCount;

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

        btnAddSaving.setOnClickListener(onSavingsClick);

        User _user = ((Global)getActivity().getApplication()).GetUser();

        savingsCount = _user.GetBanker().GetSavingsCount();

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


    View.OnClickListener onSavingsClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent savingWizardIntent = new Intent(getActivity(), SavingsWizardActivity.class);
            getActivity().startActivity(savingWizardIntent);

        }
    };
}
