package Fragments;

import Helpers.*;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;
import com.google.gson.Gson;
import com.graviton.Cuzdan.Global;
import com.graviton.Cuzdan.R;
import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Umut Seven on 27.1.2015, for Graviton.
 */
public class ExpenseGraphFragment extends Fragment implements OnChartValueSelectedListener, ExpenseLoadListener {

    TextView txtExpenseGraphDate;
    ImageButton imgLeft, imgRight, btnCalendar;
    User user;
    Date dateBeingViewed;
    LineChart expenseLineChart;
    ArrayList<String> xVals;
    List<Expense> expenses;
    DatePickerFragment datePickerFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.expense_graph_fragment, container, false);

        user = ((Global) getActivity().getApplication()).GetUser();
        expenseLineChart = (LineChart) v.findViewById(R.id.expenseGraph);
        txtExpenseGraphDate = (TextView) v.findViewById(R.id.txtExpenseGraphDate);
        imgLeft = (ImageButton) v.findViewById(R.id.imgExpenseGraphLeft);
        imgRight = (ImageButton) v.findViewById(R.id.imgExpenseGraphRight);
        btnCalendar = (ImageButton) v.findViewById(R.id.btnExpenseGraphCalendar);

        imgLeft.setOnClickListener(onLeftArrowClick);
        imgRight.setOnClickListener(onRightArrowClick);
        btnCalendar.setOnClickListener(onCalendarClick);

        datePickerFragment = new DatePickerFragment();
        datePickerFragment.SetExpenseListener(this);

        expenseLineChart.setOnChartValueSelectedListener(this);

        dateBeingViewed = new Date();

        ChartHelper.InitializeLineChart(expenseLineChart, user.GetCurrency(), getActivity().getApplicationContext());
        try {
            LoadLineChart();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Legend l = expenseLineChart.getLegend();

        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);

        XLabels xl = expenseLineChart.getXLabels();
        xl.setTextColor(Color.BLACK);
        xl.setTextSize(12f);

        YLabels yl = expenseLineChart.getYLabels();
        yl.setTextColor(Color.BLACK);
        yl.setTextSize(12f);

        return v;

    }

    public void LoadLineChart() throws ParseException, IOException, JSONException {
        txtExpenseGraphDate.setText(DateFormatHelper.GetMonthText(dateBeingViewed, getResources()));

        expenseLineChart.clear();

        expenses = user.GetBanker().GetExpensesFromMonth(dateBeingViewed);

        xVals = new ArrayList<String>();
        for (Expense expense : expenses) {
            xVals.add((DateFormatHelper.GetDayText(expense.GetDate())));
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();
        for (int i = 0; i < expenses.size(); i++) {
            yVals.add(new Entry(expenses.get(i).GetAmount().floatValue(), i));
        }

        LineDataSet set = new LineDataSet(yVals, getString(R.string.budget_expenses));
        set.setColor(getResources().getColor(R.color.cuzdan_red));
        set.setCircleColor(ColorTemplate.getHoloBlue());
        set.setLineWidth(4f);
        set.setCircleSize(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(ColorTemplate.getHoloBlue());

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();

        dataSets.add(set);

        LineData data = new LineData(xVals, dataSets);
        expenseLineChart.setData(data);
    }

    View.OnClickListener onLeftArrowClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                getLastDateIncomes();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
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
                getNextDateIncomes();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    View.OnClickListener onCalendarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            datePickerFragment.SetMode("expense");
            datePickerFragment.show(getActivity().getFragmentManager(), "datepicker");
        }
    };

    public void getNextDateIncomes() throws ParseException, JSONException, IOException {
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

        if (calDayOfMonth == calTodayDayOfMonth && calMonth == calTodayMonth && calYear == calTodayYear) {
            return;
        }

        cal.add(Calendar.MONTH, 1);
        dateBeingViewed = cal.getTime();
        LoadLineChart();
    }

    public void getLastDateIncomes() throws ParseException, JSONException, IOException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateBeingViewed);

        cal.add(Calendar.MONTH, -1);
        dateBeingViewed = cal.getTime();
        LoadLineChart();
    }

    @Override
    public void onValueSelected(Entry entry, int i) {
        Expense expense = expenses.get(entry.getXIndex());
        Bundle bundle = new Bundle();
        bundle.putBoolean("canDelete", false);

        bundle.putString("expense", new Gson().toJson(expense));
        ExpenseDialogFragment dialog = new ExpenseDialogFragment();

        dialog.setArguments(bundle);
        dialog.show(getActivity().getFragmentManager(), "dialog");

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onDismissed() {

    }

    @Override
    public void onDateSelected(Date date) {
        dateBeingViewed = date;

        try {
            LoadLineChart();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
