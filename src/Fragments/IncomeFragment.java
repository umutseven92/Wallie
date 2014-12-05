package Fragments;

import Helpers.*;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.Cuzdan.Global;
import com.example.Cuzdan.R;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

public class IncomeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    static User _user;
    String mode = "day";
    View infView;

    public static final IncomeFragment newInstance()
    {
        IncomeFragment f = new IncomeFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        infView = inflater.inflate(R.layout.incomefragment, container, false);

        Spinner spnDate = (Spinner)infView.findViewById(R.id.spnDateIncome);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(infView.getContext(), R.array.dateArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDate.setAdapter(adapter);
        spnDate.setOnItemSelectedListener(this);

        _user = ((Global) getActivity().getApplication()).GetUser();

        return infView;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(position == 0)
        {
            mode = "day";
            LoadListView(new Date(), true);

        }
        else if(position == 1)
        {
            mode = "month";
            LoadListView(new Date(), false);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void LoadListView(Date date, boolean day)
    {
        ArrayList<Income> incomes;
        if(day)
        {
            incomes = _user.GetBanker().GetIncomesFromDay(date);
        }
        else
        {
            incomes = _user.GetBanker().GetIncomesFromMonth(date);
        }
        BigDecimal total = BigDecimal.ZERO;

        for (int i = 0; i<incomes.size(); i++)
        {
            BigDecimal val = incomes.get(i).GetAmount();
            total = total.add(val);
        }
        ListView lv = (ListView)infView.findViewById(R.id.lstIncomes);
        TextView txtTotalIncome = (TextView)infView.findViewById(R.id.txtTotalIncome);
        lv.setAdapter(new IncomeListAdapter(this.getActivity(),incomes));

        txtTotalIncome.setText(total.toString() + " TL");
        txtTotalIncome.setTextColor(Color.GREEN);
    }
}
