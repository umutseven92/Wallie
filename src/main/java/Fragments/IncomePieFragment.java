package Fragments;

import Helpers.Banker;
import Helpers.Income;
import Helpers.User;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
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
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;



/**
 * Created by Umut Seven on 15.1.2015, for Graviton.
 */
public class IncomePieFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    PieChart incomePieChart;
    Spinner spnIncomePieDate;
    String mode = "month";
    ImageButton imgLeft, imgRight;
    Date dateBeingViewed;
    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.incomepiefragment, container,false);

        spnIncomePieDate = (Spinner)v.findViewById(R.id.spnIncomePieDate);
        incomePieChart = (PieChart)v.findViewById(R.id.incomePieChart);
        imgLeft = (ImageButton)v.findViewById(R.id.imgIncomePieLeft);
        imgRight= (ImageButton)v.findViewById(R.id.imgIncomePieRight);

        imgLeft.setOnClickListener(onLeftArrowClick);
        imgRight.setOnClickListener(onRightArrowClick);

        user = ((Global)getActivity().getApplication()).GetUser();
        dateBeingViewed = new Date();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(), R.array.balanceDateArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnIncomePieDate.setAdapter(adapter);
        spnIncomePieDate.setOnItemSelectedListener(this);

        InitializePieChart(incomePieChart);

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

        Legend l = incomePieChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setTextSize(12f);

        return v;
    }

    public void InitializePieChart(PieChart chart)
    {
        chart.setDescription("");

        chart.setUsePercentValues(true);
        chart.setValueTextColor(Color.BLACK);
        chart.setValueTextSize(15f);

        chart.setDrawYValues(true);
        chart.setDrawXValues(true);

        chart.setDrawHoleEnabled(true);
        chart.setHoleRadius(40f);
        chart.setTransparentCircleRadius(0f);
        chart.setHoleColor(Color.parseColor("#FFFFF0"));

        chart.setCenterText("");
        chart.setCenterTextSize(20f);

        chart.setRotationAngle(0);
        chart.setRotationEnabled(true);

        chart.spin(800, 0, 360);

    }

    private void LoadPieChart(boolean day) throws ParseException, IOException, JSONException {

        ArrayList<String> incomeNames = new ArrayList<String>();
        ArrayList<Entry> entries = new ArrayList<Entry>();
        Banker banker = user.GetBanker();

        if(day)
        {
            ArrayList<Income> incomes =  banker.GetIncomesFromDay(dateBeingViewed);

            for (int i = 0;i<incomes.size();i++)
            {
                Income income = incomes.get(i);
                incomeNames.add(income.GetCategory());
                entries.add(new Entry(income.GetAmount().floatValue(),i));
            }

            incomePieChart.setCenterText(GetDayText());
        }
        else
        {
            ArrayList<Income> incomes = banker.GetIncomesFromMonth(dateBeingViewed);

            for (int i = 0;i<incomes.size();i++)
            {
                Income income = incomes.get(i);
                incomeNames.add(income.GetCategory());
                entries.add(new Entry(income.GetAmount().floatValue(),i));
            }

            incomePieChart.setCenterText(GetMonthText());

        }

        PieDataSet set = new PieDataSet(entries,"");
        set.setSliceSpace(3f);
        set.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData(incomeNames.toArray(new String[incomeNames.size()]),set);

        incomePieChart.setData(data);
        incomePieChart.highlightValues(null);
        incomePieChart.invalidate();
    }


    View.OnClickListener onLeftArrowClick = new View.OnClickListener() {
        @Override
        public void onClick(View v){
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

    public void getLastDateIncomes() throws ParseException, JSONException, IOException {
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

    public String GetDayText()
    {
        Format formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(dateBeingViewed);
    }

    public String GetMonthText()
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
        return month;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
