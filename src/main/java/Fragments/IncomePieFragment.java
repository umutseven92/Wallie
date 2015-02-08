package Fragments;

import Helpers.*;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
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
 * Created by Umut Seven on 15.1.2015, for Graviton.
 */
public class IncomePieFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    PieChart incomePieChart;
    Spinner spnIncomePieDate;
    String mode = "month";
    ImageButton imgLeft, imgRight;
    Date dateBeingViewed;
    User user;
    TextView txtIncomeDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.income_pie_fragment, container, false);

        spnIncomePieDate = (Spinner) v.findViewById(R.id.spnIncomePieDate);
        incomePieChart = (PieChart) v.findViewById(R.id.incomePieChart);
        imgLeft = (ImageButton) v.findViewById(R.id.imgIncomePieLeft);
        imgRight = (ImageButton) v.findViewById(R.id.imgIncomePieRight);
        imgLeft.setOnClickListener(onLeftArrowClick);
        imgRight.setOnClickListener(onRightArrowClick);

        user = ((Global) getActivity().getApplication()).GetUser();
        dateBeingViewed = new Date();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(), R.array.balanceDateArray, R.layout.cuzdan_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnIncomePieDate.setAdapter(adapter);
        spnIncomePieDate.setOnItemSelectedListener(this);

        ChartHelper.InitializePieChart(incomePieChart);

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

        Legend l = incomePieChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setTextSize(12f);

        return v;
    }


    private void LoadPieChart(boolean day) throws ParseException, IOException, JSONException {

        ArrayList<String> incomeNames = new ArrayList<String>();
        ArrayList<Entry> entries = new ArrayList<Entry>();
        Banker banker = user.GetBanker();

        if (day) {
            ArrayList<Income> incomes = banker.GetIncomesFromDay(dateBeingViewed);

            for (int i = 0; i < incomes.size(); i++) {
                Income income = incomes.get(i);
                incomeNames.add(income.GetCategory());
                entries.add(new Entry(income.GetAmount().floatValue(), i));
            }

            incomePieChart.setCenterText(DateFormatHelper.GetDayText(dateBeingViewed));
        } else {
            ArrayList<Income> incomes = banker.GetIncomesFromMonth(dateBeingViewed);

            for (int i = 0; i < incomes.size(); i++) {
                Income income = incomes.get(i);
                incomeNames.add(income.GetCategory());
                entries.add(new Entry(income.GetAmount().floatValue(), i));
            }

            incomePieChart.setCenterText(DateFormatHelper.GetMonthText(dateBeingViewed, getResources()));

        }

        PieDataSet set = new PieDataSet(entries, "");
        set.setSliceSpace(3f);
        set.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData data = new PieData(incomeNames.toArray(new String[incomeNames.size()]), set);

        incomePieChart.setData(data);
        incomePieChart.highlightValues(null);
        incomePieChart.invalidate();
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

    public void getNextDateIncomes() throws ParseException, JSONException, IOException {
        Date today = new Date();

        if (mode.equals("month")) {
            if (dateBeingViewed.getMonth() == today.getMonth() && dateBeingViewed.getYear() == today.getYear()) {
                return;
            }
        } else if (mode.equals("day")) {
            if (dateBeingViewed.getDay() == today.getDay() && dateBeingViewed.getMonth() == today.getMonth() && dateBeingViewed.getYear() == today.getYear()) {
                return;
            }
        }

        Calendar cal = Calendar.getInstance();

        cal.setTime(dateBeingViewed);

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

    public void getLastDateIncomes() throws ParseException, JSONException, IOException {
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
}
