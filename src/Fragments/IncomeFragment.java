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
import java.util.Date;

public class IncomeFragment extends Fragment {

    static User _user;

    public static final IncomeFragment newInstance()
    {
        IncomeFragment f = new IncomeFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.incomefragment, container, false);

        _user = ((Global) getActivity().getApplication()).GetUser();

        ArrayList<Income> incomes = _user.GetBanker().GetIncomes();

        ListView lv = (ListView)v.findViewById(R.id.lstIncomes);

        BigDecimal total = BigDecimal.ZERO;

        for (int i = 0; i<incomes.size(); i++)
        {
            BigDecimal val = incomes.get(i).GetAmount();

           total = total.add(val);
        }

        lv.setAdapter(new IncomeListAdapter(this.getActivity(),incomes));

        TextView txtTotalIncome = (TextView)v.findViewById(R.id.txtTotalIncome);
        txtTotalIncome.setText(total.toString() + " TL");
        txtTotalIncome.setTextColor(Color.GREEN);

        return v;
    }

}
