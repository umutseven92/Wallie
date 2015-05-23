package Fragments;

import Helpers.*;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.graviton.Cuzdan.Global;
import com.graviton.Cuzdan.R;
import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;


/**
 * Created by Umut Seven on 23.1.2015, for Graviton.
 */
public class ExpensePieFragment extends Fragment implements AdapterView.OnItemSelectedListener, ExpenseLoadListener {

    PieChart expensePieChart;
    Spinner spnExpenseDate;
    String mode = "month";
    ImageButton imgLeft, imgRight, btnCalendar;
    Date dateBeingViewed;
    User user;
    DatePickerFragment datePickerFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.expense_pie_fragment, container, false);
        spnExpenseDate = (Spinner) v.findViewById(R.id.spnExpensePieDate);
        expensePieChart = (PieChart) v.findViewById(R.id.expensePieChart);
        imgLeft = (ImageButton) v.findViewById(R.id.imgExpensePieLeft);
        imgRight = (ImageButton) v.findViewById(R.id.imgExpensePieRight);
        btnCalendar = (ImageButton) v.findViewById(R.id.btnExpensePieCalendar);


        imgLeft.setOnClickListener(onLeftArrowClick);
        imgRight.setOnClickListener(onRightArrowClick);
        btnCalendar.setOnClickListener(onCalendarClick);

        datePickerFragment = new DatePickerFragment();
        datePickerFragment.SetExpenseListener(this);

        user = ((Global) getActivity().getApplication()).GetUser();
        dateBeingViewed = new Date();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(), R.array.balanceDateArray, R.layout.cuzdan_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnExpenseDate.setAdapter(adapter);
        spnExpenseDate.setOnItemSelectedListener(this);

        ChartHelper.InitializePieChart(expensePieChart);

        if (mode.equals("day")) {
            try {
                LoadPieChart(true);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (mode.equals("month")) {
            try {
                LoadPieChart(false);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return v;
    }

    private void LoadPieChart(boolean day) throws ParseException, IOException, JSONException {

        ArrayList<String> expenseNames = new ArrayList<String>();
        ArrayList<Entry> entries = new ArrayList<Entry>();
        Banker banker = user.GetBanker();

        if (day) {
            ArrayList<Expense> expenses = banker.GetExpensesFromDay(dateBeingViewed);
            Dictionary<String, Integer> dataKey = new Hashtable<String, Integer>();

            for (int i = 0; i < expenses.size(); i++) {
                Expense expense = expenses.get(i);
                boolean dup = false;

                if (!expense.GetCategory().startsWith("Özel Kategori")) {

                    if (!expenseNames.contains(expense.GetCategory())) {
                        expenseNames.add(expense.GetCategory());
                    } else {
                        dup = true;
                    }
                } else {
                    if (!expenseNames.contains(expense.GetSubCategory())) {
                        expenseNames.add(expense.GetSubCategory());
                    } else {
                        dup = true;
                    }
                }

                if (expense.GetCategory().equals("Özel Kategori")) {
                    if (!dup) {
                        entries.add(new Entry(expense.GetAmount().floatValue(), i));
                        dataKey.put(expense.GetSubCategory(), i);
                    } else {
                        for (Entry e : entries) {
                            if (e.getXIndex() == dataKey.get(expense.GetSubCategory())) {
                                e.setVal(e.getVal() + expense.GetAmount().floatValue());
                            }
                        }
                    }
                } else {
                    if (!dup) {
                        entries.add(new Entry(expense.GetAmount().floatValue(), i));
                        dataKey.put(expense.GetCategory(), i);
                    } else {
                        for (Entry e : entries) {
                            if (e.getXIndex() == dataKey.get(expense.GetCategory())) {
                                e.setVal(e.getVal() + expense.GetAmount().floatValue());
                            }
                        }
                    }

                }
            }

            expensePieChart.setCenterText(DateFormatHelper.GetDayText(dateBeingViewed));
        } else {
            ArrayList<Expense> expenses = banker.GetExpensesFromMonth(dateBeingViewed);
            Dictionary<String, Integer> dataKey = new Hashtable<String, Integer>();

            for (int i = 0; i < expenses.size(); i++) {
                Expense expense = expenses.get(i);
                boolean dup = false;

                if (!expense.GetCategory().startsWith("Özel Kategori")) {

                    if (!expenseNames.contains(expense.GetCategory())) {
                        expenseNames.add(expense.GetCategory());
                    } else {
                        dup = true;
                    }
                } else {
                    if (!expenseNames.contains(expense.GetSubCategory())) {
                        expenseNames.add(expense.GetSubCategory());
                    } else {
                        dup = true;
                    }
                }

                if (expense.GetCategory().equals("Özel Kategori")) {
                    if (!dup) {
                        entries.add(new Entry(expense.GetAmount().floatValue(), i));
                        dataKey.put(expense.GetSubCategory(), i);
                    } else {
                        for (Entry e : entries) {
                            if (e.getXIndex() == dataKey.get(expense.GetSubCategory())) {
                                e.setVal(e.getVal() + expense.GetAmount().floatValue());
                            }
                        }
                    }
                } else {
                    if (!dup) {
                        entries.add(new Entry(expense.GetAmount().floatValue(), i));
                        dataKey.put(expense.GetCategory(), i);
                    } else {
                        for (Entry e : entries) {
                            if (e.getXIndex() == dataKey.get(expense.GetCategory())) {
                                e.setVal(e.getVal() + expense.GetAmount().floatValue());
                            }
                        }
                    }

                }
            }

            expensePieChart.setCenterText(DateFormatHelper.GetMonthText(dateBeingViewed, getResources()));

        }

        PieDataSet set = new PieDataSet(entries, "");
        set.setSliceSpace(3f);
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(expenseNames.toArray(new String[expenseNames.size()]), set);

        expensePieChart.setData(data);
        expensePieChart.highlightValues(null);
        expensePieChart.invalidate();
    }

    View.OnClickListener onLeftArrowClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                getLastDateExpenses();
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
                getNextDateExpenses();
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

    public void getNextDateExpenses() throws ParseException, JSONException, IOException {
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
            LoadPieChart(true);
        } else if (mode.equals("month")) {
            cal.add(Calendar.MONTH, 1);
            dateBeingViewed = cal.getTime();
            LoadPieChart(false);
        }
    }

    public void getLastDateExpenses() throws ParseException, JSONException, IOException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateBeingViewed);

        if (mode.equals("day")) {
            cal.add(Calendar.DATE, -1);
            dateBeingViewed = cal.getTime();
            LoadPieChart(true);
        } else if (mode.equals("month")) {
            cal.add(Calendar.MONTH, -1);
            dateBeingViewed = cal.getTime();
            LoadPieChart(false);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Date today = new Date();
        if (position == 0) {
            mode = "month";
            try {
                dateBeingViewed.setDate(1);
                LoadPieChart(false);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (position == 1) {
            mode = "day";
            try {
                if (dateBeingViewed.getMonth() == today.getMonth()) {
                    dateBeingViewed.setDate(today.getDate());
                }
                LoadPieChart(true);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDismissed() {

    }

    @Override
    public void onDateSelected(Date date) {

        dateBeingViewed = date;

        if (mode.equals("day")) {
            try {
                LoadPieChart(true);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (mode.equals("month")) {
            try {
                LoadPieChart(false);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
