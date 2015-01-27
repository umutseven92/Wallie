package Fragments;

import Helpers.*;
import android.os.Bundle;
import android.app.Fragment;
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
import com.github.mikephil.charting.utils.Legend;
import com.graviton.Cuzdan.Global;
import com.graviton.Cuzdan.R;
import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Umut Seven on 23.1.2015, for Graviton.
 */
public class ExpensePieFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    PieChart expensePieChart;
    Spinner spnExpenseDate;
    String mode = "month";
    ImageButton imgLeft, imgRight;
    Date dateBeingViewed;
    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.expensepiefragment, container,false);
        spnExpenseDate = (Spinner)v.findViewById(R.id.spnExpensePieDate);
        expensePieChart = (PieChart)v.findViewById(R.id.expensePieChart);
        imgLeft = (ImageButton)v.findViewById(R.id.imgExpensePieLeft);
        imgRight = (ImageButton)v.findViewById(R.id.imgExpensePieRight);

        imgLeft.setOnClickListener(onLeftArrowClick);
        imgRight.setOnClickListener(onRightArrowClick);

        user = ((Global)getActivity().getApplication()).GetUser();
        dateBeingViewed = new Date();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(), R.array.balanceDateArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnExpenseDate.setAdapter(adapter);
        spnExpenseDate.setOnItemSelectedListener(this);

        ChartHelpers.InitializePieChart(expensePieChart);

        if(mode.equals("day"))
        {
            try {
                LoadPieChart(true);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(mode.equals("month"))
        {
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

        Legend l = expensePieChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setTextSize(12f);

        return v;
    }

    private void LoadPieChart(boolean day) throws ParseException, IOException, JSONException {

        ArrayList<String> expenseNames = new ArrayList<String>();
        ArrayList<Entry> entries = new ArrayList<Entry>();
        Banker banker = user.GetBanker();

        if(day)
        {
            ArrayList<Expense> expenses =  banker.GetExpensesFromDay(dateBeingViewed);

            for (int i = 0;i<expenses.size();i++)
            {
                Expense expense = expenses.get(i);
                expenseNames.add(expense.GetCategory());
                entries.add(new Entry(expense.GetAmount().floatValue(),i));
            }

            expensePieChart.setCenterText(ChartHelpers.GetDayText(dateBeingViewed));
        }
        else
        {
            ArrayList<Expense> expenses = banker.GetExpensesFromMonth(dateBeingViewed);

            for (int i = 0;i<expenses.size();i++)
            {
                Expense expense = expenses.get(i);
                expenseNames.add(expense.GetCategory());
                entries.add(new Entry(expense.GetAmount().floatValue(),i));
            }

            expensePieChart.setCenterText(ChartHelpers.GetMonthText(dateBeingViewed,getResources()));

        }

        PieDataSet set = new PieDataSet(entries,"");
        set.setSliceSpace(3f);
        set.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData(expenseNames.toArray(new String[expenseNames.size()]),set);

        expensePieChart.setData(data);
        expensePieChart.highlightValues(null);
        expensePieChart.invalidate();
    }

    View.OnClickListener onLeftArrowClick = new View.OnClickListener() {
        @Override
        public void onClick(View v){
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

    public void getNextDateExpenses() throws ParseException, JSONException, IOException {
        Date today = new Date();

        if(dateBeingViewed.getDay() == today.getDay() && dateBeingViewed.getMonth() == today.getMonth() && dateBeingViewed.getYear() == today.getYear())
        {
            return;
        }

        Calendar cal = Calendar.getInstance();

        cal.setTime(dateBeingViewed);

        if(mode.equals("day"))
        {
            cal.add(Calendar.DATE,1);
            dateBeingViewed = cal.getTime();
            LoadPieChart(true);
        }
        else if (mode.equals("month"))
        {
            cal.add(Calendar.MONTH,1);
            dateBeingViewed = cal.getTime();
            LoadPieChart(false);
        }
    }

    public void getLastDateExpenses() throws ParseException, JSONException, IOException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateBeingViewed);

        if(mode.equals("day"))
        {
            cal.add(Calendar.DATE,-1);
            dateBeingViewed = cal.getTime();
            LoadPieChart(true);
        }
        else if (mode.equals("month"))
        {
            cal.add(Calendar.MONTH,-1);
            dateBeingViewed = cal.getTime();
            LoadPieChart(false);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0)
        {
            mode = "month";
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
        else if(position == 1)
        {
            mode = "day";
            try {
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
}