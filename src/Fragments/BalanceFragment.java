package Fragments;

import Helpers.User;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.Cuzdan.Global;
import com.example.Cuzdan.R;

import java.math.BigDecimal;

/**
 * Created by Umut on 22.11.2014.
 */
public class BalanceFragment extends Fragment {

    static User _user;

    public static final BalanceFragment newInstance()
    {
        BalanceFragment f = new BalanceFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.balancefragment, container, false);
        _user = ((Global) getActivity().getApplication()).GetUser();

        Spinner spnDate = (Spinner)v.findViewById(R.id.spnDateBalance);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(), R.array.dateArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDate.setAdapter(adapter);

        BigDecimal incomeTotal = _user.GetBanker().GetTotalIncome();
        BigDecimal expenseTotal = _user.GetBanker().GetTotalExpense();
        BigDecimal total = _user.GetBanker().GetBalance();

        TextView txtIncome = (TextView)v.findViewById(R.id.txtBalanceIncome);
        TextView txtExpense = (TextView)v.findViewById(R.id.txtBalanceExpense);
        TextView txtTotal = (TextView)v.findViewById(R.id.txtBalance);

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

        return v;
    }

}
