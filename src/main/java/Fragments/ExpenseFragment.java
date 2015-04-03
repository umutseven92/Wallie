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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ExpenseFragment extends Fragment implements AdapterView.OnItemSelectedListener, ExpenseLoadListener {

    static User _user;
    String mode = "day";
    View infView;
    Date dateBeingViewed;
    ImageButton leftArrow, rightArrow, btnAddExpense, btnExpenseStats, btnExpenseCalendar;
    TextView txtExpenseDate;
    ListView lv;
    ExpenseDialogFragment dialog;
    DatePickerFragment datePickerFragment;

    public static final ExpenseFragment newInstance() {
        ExpenseFragment f = new ExpenseFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        infView = inflater.inflate(R.layout.expense_fragment, container, false);

        Spinner spnDate = (Spinner) infView.findViewById(R.id.spnDateExpense);
        leftArrow = (ImageButton) infView.findViewById(R.id.imgLeftExpense);
        rightArrow = (ImageButton) infView.findViewById(R.id.imgRightExpense);

        txtExpenseDate = (TextView) infView.findViewById(R.id.txtExpenseDate);
        btnAddExpense = (ImageButton) infView.findViewById(R.id.btnAddExpense);
        btnExpenseStats = (ImageButton) infView.findViewById(R.id.btnExpenseStats);
        btnExpenseCalendar = (ImageButton) infView.findViewById(R.id.btnExpenseCalendar);
        lv = (ListView) infView.findViewById(R.id.lstExpenses);

        datePickerFragment = new DatePickerFragment();
        datePickerFragment.SetExpenseListener(this);

        dialog = new ExpenseDialogFragment();
        dialog.SetListener(this);
        ((Global) getActivity().getApplication()).expenseDialog = dialog;

        leftArrow.setOnClickListener(onLeftArrowClick);
        rightArrow.setOnClickListener(onRightArrowClick);
        btnAddExpense.setOnClickListener(onExpenseClick);
        btnExpenseStats.setOnClickListener(onExpenseStatsClick);
        btnExpenseCalendar.setOnClickListener(onCalendarClick);
        lv.setOnItemClickListener(onItemClickListener);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(infView.getContext(), R.array.dateArray, R.layout.cuzdan_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDate.setAdapter(adapter);
        spnDate.setOnItemSelectedListener(this);

        dateBeingViewed = new Date();
        _user = ((Global) getActivity().getApplication()).GetUser();

        return infView;
    }

    @Override
    public void onResume() {

        if (mode.equals("day")) {
            try {
                LoadListView(dateBeingViewed, true);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (mode.equals("month")) {
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

    View.OnClickListener onCalendarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            datePickerFragment.SetMode("expense");
            datePickerFragment.show(getActivity().getFragmentManager(), "datepicker");
        }
    };

    View.OnClickListener onExpenseStatsClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
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
        Calendar calToday = Calendar.getInstance();
        calToday.setTime(today);

        Calendar cal = Calendar.getInstance();
        cal.setTime(dateBeingViewed);

        int calDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int calTodayDayOfMonth = calToday.get(Calendar.DAY_OF_MONTH);

        int calMonth = cal.get(Calendar.MONTH);
        int calTodayMonth = calToday.get(Calendar.MONTH);

        int calYear = cal.get(Calendar.YEAR);
        int calTodayYear = calToday.get(Calendar.YEAR);

        if (mode.equals("month")) {
            if (calMonth == calTodayMonth && calYear == calTodayYear) {
                return;
            }
        } else if (mode.equals("day")) {
            if (calDayOfMonth == calTodayDayOfMonth && calMonth == calTodayMonth && calYear == calTodayYear) {
                return;
            }

        }

        if (mode.equals("day")) {
            cal.add(Calendar.DATE, 1);
            dateBeingViewed = cal.getTime();
            LoadListView(dateBeingViewed, true);
        } else if (mode.equals("month")) {
            cal.add(Calendar.MONTH, 1);
            dateBeingViewed = cal.getTime();
            LoadListView(dateBeingViewed, false);

        }
    }

    public void getLastDateExpenses() throws JSONException, ParseException, IOException {

        Calendar cal = Calendar.getInstance();
        cal.setTime(dateBeingViewed);

        if (mode.equals("day")) {
            cal.add(Calendar.DATE, -1);
            dateBeingViewed = cal.getTime();
            LoadListView(dateBeingViewed, true);
        } else if (mode.equals("month")) {
            cal.add(Calendar.MONTH, -1);
            dateBeingViewed = cal.getTime();
            LoadListView(dateBeingViewed, false);

        }
    }


    public void LoadListView(Date date, boolean day) throws JSONException, ParseException, IOException {

        ArrayList<Expense> expenses;

        if (day) {
            expenses = _user.GetBanker().GetExpensesFromDay(date);
            txtExpenseDate.setText(DateFormatHelper.GetDayText(date));
        } else {
            expenses = _user.GetBanker().GetExpensesFromMonth(date);
            txtExpenseDate.setText(DateFormatHelper.GetMonthText(date, getResources()));
        }

        BigDecimal total = BigDecimal.ZERO;

        for (Expense expense : expenses) {
            BigDecimal val = expense.GetAmount();

            total = total.add(val);
        }

        lv.setAdapter(new ExpenseListAdapter(this.getActivity(), expenses, _user.GetCurrency()));

        TextView txtTotalExpense = (TextView) infView.findViewById(R.id.txtTotalExpense);
        txtTotalExpense.setText(total.toString() + " " + _user.GetCurrency());
        txtTotalExpense.setTextColor(Color.RED);

        NotificationHelper.SetPermaNotification(getActivity(), _user.GetBanker().GetBalance(new Date(), true), _user.GetCurrency());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Date today = new Date();

        if (position == 0) {
            mode = "day";
            try {
                if (dateBeingViewed.getMonth() == today.getMonth()) {
                    dateBeingViewed.setDate(today.getDate());
                }
                LoadListView(dateBeingViewed, true);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (position == 1) {
            mode = "month";
            try {
                dateBeingViewed.setDate(1);
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
            ExpenseListAdapter adapter = (ExpenseListAdapter) lv.getAdapter();
            Expense exp = (Expense) adapter.getItem(position);

            Bundle bundle = new Bundle();
            bundle.putString("expense", new Gson().toJson(exp));
            bundle.putBoolean("canDelete", true);
            dialog.setArguments(bundle);
            dialog.show(getActivity().getFragmentManager(), "dialog");

        }
    };

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDismissed() {

        if (mode.equals("day")) {
            try {
                LoadListView(dateBeingViewed, true);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (mode.equals("month")) {
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

    @Override
    public void onDateSelected(Date date) {
        dateBeingViewed = date;

        if (mode.equals("day")) {
            try {
                LoadListView(dateBeingViewed, true);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (mode.equals("month")) {
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
