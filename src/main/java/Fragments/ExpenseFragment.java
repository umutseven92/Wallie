package Fragments;

import Helpers.*;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.gson.Gson;
import com.graviton.Cuzdan.ExpenseStatsActivity;
import com.graviton.Cuzdan.ExpenseWizardActivity;
import com.graviton.Cuzdan.Global;
import com.graviton.Cuzdan.R;
import org.json.JSONException;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ExpenseFragment extends Fragment implements AdapterView.OnItemSelectedListener, ExpenseLoadListener {

    static User _user;
    String mode = "day";
    View infView;
    Date dateBeingViewed;
    ImageButton leftArrow, rightArrow, btnAddExpense, btnExpenseStats;
    TextView txtExpenseDate;
    ListView lv;
    ExpenseDialogFragment dialog;

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
        btnAddExpense = (ImageButton)infView.findViewById(R.id.btnAddExpense);
        btnExpenseStats = (ImageButton)infView.findViewById(R.id.btnExpenseStats);
        lv = (ListView)infView.findViewById(R.id.lstExpenses);
        dialog = new ExpenseDialogFragment();
        dialog.SetListener(this);
        ((Global)getActivity().getApplication()).expenseDialog = dialog;

        leftArrow.setOnClickListener(onLeftArrowClick);
        rightArrow.setOnClickListener(onRightArrowClick);
        btnAddExpense.setOnClickListener(onExpenseClick);
        btnExpenseStats.setOnClickListener(onExpenseStatsClick);
        lv.setOnItemClickListener(onItemClickListener);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(infView.getContext(), R.array.dateArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDate.setAdapter(adapter);
        spnDate.setOnItemSelectedListener(this);

        dateBeingViewed = new Date();
        _user = ((Global) getActivity().getApplication()).GetUser();

        return infView;
    }

    @Override
    public void onResume()
    {
        if (mode == "day")
        {
            try {
                LoadListView(dateBeingViewed, true);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if (mode == "month")
        {
            try {
                LoadListView(dateBeingViewed, false);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        super.onResume();
    }

    View.OnClickListener onExpenseStatsClick = new View.OnClickListener(){
        @Override
        public void onClick(View v)
        {
            Intent expenseStatsIntent = new Intent(getActivity(), ExpenseStatsActivity.class);
            getActivity().startActivity(expenseStatsIntent);
        }
    };

    View.OnClickListener onLeftArrowClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                getLastDateExpenses();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    View.OnClickListener onExpenseClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent expenseWizardIntent = new Intent(getActivity(), ExpenseWizardActivity.class);
            getActivity().startActivity(expenseWizardIntent);
        }
    };

    public void getNextDateExpenses() throws JSONException, ParseException, IOException {
        Date today = new Date();

        if(dateBeingViewed.getDay() == today.getDay())
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

    public void getLastDateExpenses() throws JSONException, ParseException, IOException {
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


    public void LoadListView(Date date, boolean day) throws JSONException, ParseException, IOException {
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

        BigDecimal total = BigDecimal.ZERO;

        for (int i = 0; i<expenses.size(); i++)
        {
            BigDecimal val = expenses.get(i).GetAmount();

            total = total.add(val);
        }

        lv.setAdapter(new ExpenseListAdapter(this.getActivity(),expenses));

        TextView txtTotalExpense = (TextView)infView.findViewById(R.id.txtTotalExpense);
        txtTotalExpense.setText(total.toString() + " TL");
        txtTotalExpense.setTextColor(Color.RED);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        if(position == 0)
        {
            mode = "day";
            try {
                LoadListView(dateBeingViewed, true);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if(position == 1)
        {
            mode = "month";
            try {
                LoadListView(dateBeingViewed, false);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ExpenseListAdapter adapter = (ExpenseListAdapter)lv.getAdapter();
            Expense exp = (Expense)adapter.getItem(position);

            Bundle bundle = new Bundle();
            bundle.putString("expense",new Gson().toJson(exp));
            dialog.setArguments(bundle);
            dialog.show(getActivity().getFragmentManager(),"dialog");

        }
    };

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDismissed() {

        if (mode == "day")
        {
            try {
                LoadListView(dateBeingViewed, true);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (mode == "month")
        {
            try {
                LoadListView(dateBeingViewed, false);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}