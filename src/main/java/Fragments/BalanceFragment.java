package Fragments;

import Helpers.ExpenseLoadListener;
import Helpers.User;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.graviton.Cuzdan.Global;
import com.graviton.Cuzdan.R;
import sun.font.TrueTypeFont;

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
    ImageButton leftArrow, rightArrow, btnCalendar;
    TextView txtBalanceDate;
    DatePickerFragment datePickerFragment;

    public static final BalanceFragment newInstance() {
        BalanceFragment f = new BalanceFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        infView = inflater.inflate(R.layout.balance_fragment, container, false);
        _user = ((Global) getActivity().getApplication()).GetUser();
        datePickerFragment = new DatePickerFragment();
        datePickerFragment.SetExpenseListener(this);

        Spinner spnDate = (Spinner) infView.findViewById(R.id.spnDateBalance);
        leftArrow = (ImageButton) infView.findViewById(R.id.imgLeftBalance);
        rightArrow = (ImageButton) infView.findViewById(R.id.imgRightBalance);
        btnCalendar = (ImageButton) infView.findViewById(R.id.btnBalanceCalendar);

        txtBalanceDate = (TextView) infView.findViewById(R.id.txtBalanceDate);
        ExpenseDialogFragment edf = ((Global) getActivity().getApplication()).expenseDialog;
        edf.SetSecondListener(this);

        btnCalendar.setOnClickListener(onCalendarClick);
        leftArrow.setOnClickListener(onLeftArrowClick);
        rightArrow.setOnClickListener(onRightArrowClick);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(infView.getContext(), R.array.balanceDateArray, R.layout.cuzdan_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDate.setAdapter(adapter);
        spnDate.setOnItemSelectedListener(this);
        dateBeingViewed = new Date();

        return infView;
    }

    View.OnClickListener onCalendarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            datePickerFragment.SetMode("expense");
            datePickerFragment.show(getActivity().getFragmentManager(), "datepicker");
        }
    };

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

    public void UpdateDayText() {
        Format formatter = new SimpleDateFormat("dd/MM/yyyy");
        txtBalanceDate.setText(formatter.format(dateBeingViewed));
    }

    public void UpdateMonthText() {
        Format formatter = new SimpleDateFormat("MM");
        String[] months = getResources().getStringArray(R.array.turkishMonths);
        String month = "m";

        switch (Integer.parseInt(formatter.format(dateBeingViewed))) {
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

    public void getNextDateBalances() {

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

    public void getLastDateBalance() {
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

    public void LoadListView(Date date, boolean day) {
        BigDecimal incomeTotal;
        BigDecimal expenseTotal;
        BigDecimal total;

        if (day) {
            incomeTotal = _user.GetBanker().GetTotalDayIncome(date);
            expenseTotal = _user.GetBanker().GetTotalDayExpense(date);
            total = _user.GetBanker().GetBalance(date, true);
            UpdateDayText();
        } else {
            incomeTotal = _user.GetBanker().GetTotalMonthIncome(date);
            expenseTotal = _user.GetBanker().GetTotalMonthExpense(date);
            total = _user.GetBanker().GetBalance(date, false);
            UpdateMonthText();
        }

        TextView txtIncome = (TextView) infView.findViewById(R.id.txtBalanceIncome);
        TextView txtExpense = (TextView) infView.findViewById(R.id.txtBalanceExpense);
        TextView txtTotal = (TextView) infView.findViewById(R.id.txtBalance);

        txtIncome.setText(incomeTotal.toString() + " " + getString(R.string.currency));
        txtExpense.setText(expenseTotal.toString() + " " + getString(R.string.currency));
        txtTotal.setText(total.toString() + " " + getString(R.string.currency));

        if (total.compareTo(BigDecimal.ZERO) == 0) {
            txtTotal.setTextColor(getResources().getColor(R.color.cuzdan_green));
        } else if (total.compareTo(BigDecimal.ZERO) == 1) {
            txtTotal.setTextColor(getResources().getColor(R.color.cuzdan_green));
        } else if (total.compareTo(BigDecimal.ZERO) == -1) {
            txtTotal.setTextColor(getResources().getColor(R.color.cuzdan_red));
        }
    }

    @Override
    public void onResume() {
        if (mode.equals("day")) {
            LoadListView(dateBeingViewed, true);
        } else if (mode.equals("month")) {
            LoadListView(dateBeingViewed, false);
        }
        super.onResume();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Date today = new Date();

        if (position == 0) {
            mode = "month";
            dateBeingViewed.setDate(1);
            LoadListView(dateBeingViewed, false);
        } else if (position == 1) {
            mode = "day";
            if (dateBeingViewed.getMonth() == today.getMonth()) {
                dateBeingViewed.setDate(today.getDate());
            }
            LoadListView(dateBeingViewed, true);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDismissed() {
        if (mode.equals("day")) {
            LoadListView(dateBeingViewed, true);
        } else if (mode.equals("month")) {
            LoadListView(dateBeingViewed, false);
        }

    }

    @Override
    public void onDateSelected(Date date) {

        dateBeingViewed = date;
        if (mode.equals("day")) {
            LoadListView(dateBeingViewed, true);
        } else if (mode.equals("month")) {
            LoadListView(dateBeingViewed, false);
        }
    }
}
