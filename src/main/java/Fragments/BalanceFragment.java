package Fragments;

import Helpers.ExpenseLoadListener;
import Helpers.User;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.graviton.Cuzdan.Global;
import com.graviton.Cuzdan.R;

import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Umut Seven on 22.11.2014, for Graviton.
 */
public class BalanceFragment extends Fragment implements AdapterView.OnItemSelectedListener, ExpenseLoadListener {

    static User _user;
    View infView;
    String mode = "month";
    Date dateBeingViewed;
    ImageButton leftArrow, rightArrow;
    TextView txtBalanceDate;

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
        leftArrow = (ImageButton)infView.findViewById(R.id.imgLeftBalance);
        rightArrow = (ImageButton)infView.findViewById(R.id.imgRightBalance);
        txtBalanceDate = (TextView)infView.findViewById(R.id.txtBalanceDate);
        ExpenseDialogFragment edf = ((Global)getActivity().getApplication()).expenseDialog;
        edf.SetSecondListener(this);

        leftArrow.setOnClickListener(onLeftArrowClick);
        rightArrow.setOnClickListener(onRightArrowClick);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(infView.getContext(), R.array.balanceDateArray, R.layout.cuzdanspinneritem);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDate.setAdapter(adapter);
        spnDate.setOnItemSelectedListener(this);
        dateBeingViewed = new Date();

        return infView;
    }

    View.OnClickListener onLeftArrowClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getLastDateBalance();
        }
    };

    View.OnClickListener onRightArrowClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getNextDateBalances();
        }
    };

    public void UpdateDayText()
    {
        Format formatter = new SimpleDateFormat("dd/MM/yyyy");
        txtBalanceDate.setText(formatter.format(dateBeingViewed));
    }

    public void UpdateMonthText()
    {
        Format formatter = new SimpleDateFormat("MM");
        String[] months = getResources().getStringArray(R.array.turkishMonths);
        String month = "m";

        switch (Integer.parseInt(formatter.format(dateBeingViewed)))
        {
            case 1:
                month = months[0];
                break;
            case 2:
                month = months[1];
                break;
            case 3:
                month = months[2];
                break;
            case 4:
                month = months[3];
                break;
            case 5:
                month = months[4];
                break;
            case 6:
                month = months[5];
                break;
            case 7:
                month = months[6];
                break;
            case 8:
                month = months[7];
                break;
            case 9:
                month = months[8];
                break;
            case 10:
                month = months[9];
                break;
            case 11:
                month = months[10];
                break;
            case 12:
                month = months[11];
                break;

        }

        Format formatterYear = new SimpleDateFormat("yyyy");
        month += " " + formatterYear.format(dateBeingViewed);
        txtBalanceDate.setText(month);
    }

    public void getNextDateBalances()
    {
        Date today = new Date();

        if(dateBeingViewed.getDay() == today.getDay() && dateBeingViewed.getMonth() == today.getMonth() && dateBeingViewed.getYear() == today.getYear() )
        {
            return;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateBeingViewed);

        if(mode == "day")
        {
            cal.add(Calendar.DATE,1);
            dateBeingViewed = cal.getTime();
            LoadListView(dateBeingViewed,true);
        }
        else if (mode == "month")
        {
            cal.add(Calendar.MONTH,1);
            dateBeingViewed = cal.getTime();
            LoadListView(dateBeingViewed,false);

        }
    }

    public void getLastDateBalance()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateBeingViewed);

        if(mode == "day")
        {
            cal.add(Calendar.DATE,-1);
            dateBeingViewed = cal.getTime();
            LoadListView(dateBeingViewed,true);
        }
        else if (mode == "month")
        {
            cal.add(Calendar.MONTH,-1);
            dateBeingViewed = cal.getTime();
            LoadListView(dateBeingViewed,false);

        }
    }

    public void LoadListView(Date date, boolean day)
    {
        BigDecimal incomeTotal;
        BigDecimal expenseTotal;
        BigDecimal total;

        if(day)
        {
            incomeTotal = _user.GetBanker().GetTotalDayIncome(date);
            expenseTotal = _user.GetBanker().GetTotalDayExpense(date);
            total = _user.GetBanker().GetBalance(date, true);
            UpdateDayText();
        }
        else
        {
            incomeTotal = _user.GetBanker().GetTotalMonthIncome(date);
            expenseTotal = _user.GetBanker().GetTotalMonthExpense(date);
            total = _user.GetBanker().GetBalance(date, false);
            UpdateMonthText();
        }

        TextView txtIncome = (TextView)infView.findViewById(R.id.txtBalanceIncome);
        TextView txtExpense = (TextView)infView.findViewById(R.id.txtBalanceExpense);
        TextView txtTotal = (TextView)infView.findViewById(R.id.txtBalance);

        txtIncome.setText(incomeTotal.toString());
        txtExpense.setText(expenseTotal.toString());
        txtTotal.setText(total.toString());

        if(total.compareTo(BigDecimal.ZERO) == 0)
        {
            txtTotal.setTextColor(Color.parseColor("#216C2A"));
        }
        else if (total.compareTo(BigDecimal.ZERO) == 1)
        {
            txtTotal.setTextColor(Color.parseColor("#216C2A"));
        }
        else if (total.compareTo(BigDecimal.ZERO) == -1)
        {
            txtTotal.setTextColor(Color.RED);
        }
    }

    @Override
    public void onResume()
    {
        if(mode == "day")
        {
            LoadListView(dateBeingViewed, true);
        }
        else if (mode == "month")
        {
            LoadListView(dateBeingViewed, false);
        }
        super.onResume();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0)
        {
            mode = "month";
            LoadListView(dateBeingViewed, false);
        }
        else if(position == 1)
        {
            mode = "day";
            LoadListView(dateBeingViewed, true);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDismissed() {
        if(mode == "day")
        {
            LoadListView(dateBeingViewed, true);
        }
        else if (mode == "month")
        {
            LoadListView(dateBeingViewed, false);
        }

    }
}
