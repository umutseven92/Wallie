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
import org.json.JSONException;

import java.math.BigDecimal;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ExpenseFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    static User _user;
    String mode = "day";
    View infView;
    Date dateBeingViewed;
    ImageButton leftArrow;
    ImageButton rightArrow;
    TextView txtExpenseDate;

    public static final ExpenseFragment newInstance()
    {
        ExpenseFragment f = new ExpenseFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        infView = inflater.inflate(R.layout.expensefragment, container, false);

        Spinner spnDate = (Spinner)infView.findViewById(R.id.spnDateExpense);
        leftArrow = (ImageButton)infView.findViewById(R.id.imgLeftExpense);
        rightArrow = (ImageButton)infView.findViewById(R.id.imgRightExpense);
        txtExpenseDate = (TextView)infView.findViewById(R.id.txtExpenseDate);

        leftArrow.setOnClickListener(onLeftArrowClick);
        rightArrow.setOnClickListener(onRightArrowClick);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(infView.getContext(), R.array.dateArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDate.setAdapter(adapter);
        spnDate.setOnItemSelectedListener(this);

        dateBeingViewed = new Date();
        _user = ((Global) getActivity().getApplication()).GetUser();

        return infView;
    }

    View.OnClickListener onLeftArrowClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                getLastDateExpenses();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    View.OnClickListener onRightArrowClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                getNextDateExpenses();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    public void getNextDateExpenses() throws JSONException, ParseException {
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

    public void getLastDateExpenses() throws JSONException, ParseException {
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

    public void UpdateDayText()
    {
        Format formatter = new SimpleDateFormat("dd/MM/yyyy");
        txtExpenseDate.setText(formatter.format(dateBeingViewed));
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
        txtExpenseDate.setText(month);
    }


    public void LoadListView(Date date, boolean day) throws JSONException, ParseException {
        ArrayList<Expense> expenses;

        if(day)
        {
            expenses =_user.GetBanker().GetExpensesFromDay(date);
            UpdateDayText();
        }
        else
        {
            expenses =_user.GetBanker().GetExpensesFromMonth(date);
            UpdateMonthText();

        }

        ListView lv = (ListView)infView.findViewById(R.id.lstExpenses);

        BigDecimal total = BigDecimal.ZERO;

        for (int i = 0; i<expenses.size(); i++)
        {
            BigDecimal val = expenses.get(i).GetAmount();

            total = total.add(val);
        }

        lv.setAdapter(new ExpenseListAdapter(this.getActivity(),expenses));

        TextView txtTotalIncome = (TextView)infView.findViewById(R.id.txtTotalExpense);
        txtTotalIncome.setText(total.toString() + " TL");
        txtTotalIncome.setTextColor(Color.RED);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        if(position == 0)
        {
            mode = "day";
            try {
                LoadListView(new Date(), true);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        else if(position == 1)
        {
            mode = "month";
            try {
                LoadListView(new Date(), false);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
