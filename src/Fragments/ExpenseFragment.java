package Fragments;

import Helpers.*;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.example.Cuzdan.Global;
import com.example.Cuzdan.R;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ExpenseFragment extends Fragment {

    static User _user;

    public static final ExpenseFragment newInstance()
    {
        ExpenseFragment f = new ExpenseFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.expensefragment, container, false);
        _user = ((Global) getActivity().getApplication()).GetUser();

        ArrayList<Expense> expenses =_user.GetBanker().GetExpenses();

        ListView lv = (ListView)v.findViewById(R.id.lstExpenses);

        BigDecimal total = BigDecimal.ZERO;

        for (int i = 0; i<expenses.size(); i++)
        {
            BigDecimal val = expenses.get(i).GetAmount();

            total = total.add(val);
        }

        lv.setAdapter(new ExpenseListAdapter(this.getActivity(),expenses));

        TextView txtTotalIncome = (TextView)v.findViewById(R.id.txtTotalExpense);
        txtTotalIncome.setText(total.toString() + " TL");
        txtTotalIncome.setTextColor(Color.RED);

        return v;
    }
}
