package Fragments;

import Helpers.Saving;
import Helpers.SavingListAdapter;
import Helpers.User;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.graviton.Cuzdan.Global;
import com.graviton.Cuzdan.R;
import com.graviton.Cuzdan.SavingsWizardActivity;
import org.json.JSONException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public class SavingsFragment extends Fragment {

    View v;
    User _user;
    RelativeLayout lytNoSavings, lytSavings;
    Button btnAddSaving;
    ListView lv;

    public static final SavingsFragment newInstance() {
        SavingsFragment f = new SavingsFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.savings_fragment, container, false);

        lytNoSavings = (RelativeLayout) v.findViewById(R.id.lytEmptySavings);
        lytSavings = (RelativeLayout) v.findViewById(R.id.lytSavingsList);
        btnAddSaving = (Button) v.findViewById(R.id.btnAddSaving);
        lv = (ListView) v.findViewById(R.id.lstSavings);

        btnAddSaving.setOnClickListener(onSavingsClick);
        btnAddSaving.setTextColor(getResources().getColor(R.color.foreground));

        _user = ((Global) getActivity().getApplication()).GetUser();

        return v;
    }

    @Override
    public void onResume() {

        try {
            InitializeSavings();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    private void InitializeSavings() throws JSONException, ParseException, IOException {
        int savingsCount = 0;
        ArrayList<Saving> savings = _user.GetBanker().GetSavings();
        try {
            savingsCount = _user.GetBanker().GetSavingsCount();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (savingsCount <= 0) {
            lytNoSavings.setVisibility(View.VISIBLE);
            lytSavings.setVisibility(View.INVISIBLE);
            btnAddSaving.setText("Birikime Başla");
        } else if (savingsCount > 0) {
            lytNoSavings.setVisibility(View.INVISIBLE);
            lytSavings.setVisibility(View.VISIBLE);
            btnAddSaving.setText("Birikim Ekle");

            lv.setAdapter(new SavingListAdapter(this.getActivity(), savings));
        }


    }

    View.OnClickListener onSavingsClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent savingWizardIntent = new Intent(getActivity(), SavingsWizardActivity.class);
            getActivity().startActivity(savingWizardIntent);

        }
    };
}
