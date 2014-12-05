package Fragments;

import Helpers.User;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.Cuzdan.Global;
import com.example.Cuzdan.R;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Umut on 22.11.2014.
 */
public class BalanceFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    static User _user;
    View infView;
    String mode = "day";

    public static final BalanceFragment newInstance()
    {
        BalanceFragment f = new BalanceFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        infView = inflater.inflate(R.layout.balancefragment, container, false);
        _user = ((Global) getActivity().getApplication()).GetUser();

        Spinner spnDate = (Spinner)infView.findViewById(R.id.spnDateBalance);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(infView.getContext(), R.array.dateArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDate.setAdapter(adapter);
        spnDate.setOnItemSelectedListener(this);


        return infView;
    }

    public void LoadListView(Date date, boolean day)
    {
        BigDecimal incomeTotal;
        BigDecimal expenseTotal;
        BigDecimal total;
        if(day)
        {
            incomeTotal = _user.GetBanker().GetTotalDayIncome(new Date());
            expenseTotal = _user.GetBanker().GetTotalDayExpense(new Date());
            total = _user.GetBanker().GetBalance(new Date(), true);

        }
        else
        {
            incomeTotal = _user.GetBanker().GetTotalMonthIncome(new Date());
            expenseTotal = _user.GetBanker().GetTotalMonthExpense(new Date());
            total = _user.GetBanker().GetBalance(new Date(), false);
        }

        TextView txtIncome = (TextView)infView.findViewById(R.id.txtBalanceIncome);
        TextView txtExpense = (TextView)infView.findViewById(R.id.txtBalanceExpense);
        TextView txtTotal = (TextView)infView.findViewById(R.id.txtBalance);

        txtIncome.setText(incomeTotal.toString());
        txtExpense.setText(expenseTotal.toString());
        txtTotal.setText(total.toString());
        if(total.compareTo(BigDecimal.ZERO) == 0)
        {
            txtTotal.setTextColor(Color.GREEN);
        }
        else if (total.compareTo(BigDecimal.ZERO) == 1)
        {
            txtTotal.setTextColor(Color.GREEN);
        }
        else if (total.compareTo(BigDecimal.ZERO) == -1)
        {
            txtTotal.setTextColor(Color.RED);
        }
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
}
