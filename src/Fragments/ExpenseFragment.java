package Fragments;

import Helpers.Balance;
import Helpers.Expense;
import Helpers.Income;
import Helpers.ListViewAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.example.Cuzdan.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

public class ExpenseFragment extends Fragment {

    public static final ExpenseFragment newInstance()
    {
        ExpenseFragment f = new ExpenseFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.expensefragment, container, false);

        ArrayList<Balance> expenses = new ArrayList<Balance>();

        expenses.add(new Expense("Yemek", "Fast Food", new BigDecimal(32), "McDonalds", new Date(), Balance.Tags.Personal));
        expenses.add(new Expense("İçecek", "Alkollü İçecek", new BigDecimal(13), "Bira", new Date(), Balance.Tags.Personal));

        ListView lv = (ListView)v.findViewById(R.id.lstExpenses);

        BigDecimal total = BigDecimal.ZERO;

        for (int i = 0; i<expenses.size(); i++)
        {
            BigDecimal val = expenses.get(i).GetAmount();

            total = total.add(val);
        }

        lv.setAdapter(new ListViewAdapter(this.getActivity(),expenses));

        TextView txtTotalIncome = (TextView)v.findViewById(R.id.txtTotalExpense);
        txtTotalIncome.setText(total.toString() + " TL");
        txtTotalIncome.setTextColor(Color.RED);

        return v;
    }
}
