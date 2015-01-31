package Fragments;

import Helpers.*;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
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
import java.util.*;


/**
 * Created by Umut Seven on 27.1.2015, for Graviton.
 */
public class IncomeGraphFragment  extends Fragment implements OnChartValueSelectedListener{

    TextView txtIncomeGraphDate;
    ImageButton imgLeft, imgRight;
    User user;
    Date dateBeingViewed;
    LineChart incomeLineChart;
    ArrayList<String> xVals;
    List<Income> incomes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.income_graph_fragment, container, false);

        user =  ((Global)getActivity().getApplication()).GetUser();
        incomeLineChart = (LineChart)v.findViewById(R.id.incomeGraph);
        txtIncomeGraphDate = (TextView)v.findViewById(R.id.txtIncomeGraphDate);
        imgLeft = (ImageButton)v.findViewById(R.id.imgIncomeGraphLeft);
        imgRight = (ImageButton)v.findViewById(R.id.imgIncomeGraphRight);

        imgLeft.setOnClickListener(onLeftArrowClick);
        imgRight.setOnClickListener(onRightArrowClick);
        incomeLineChart.setOnChartValueSelectedListener(this);

        dateBeingViewed = new Date();

        ChartHelper.InitializeLineChart(incomeLineChart);
        try {
            LoadLineChart();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Legend l = incomeLineChart.getLegend();

        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);

        XLabels xl = incomeLineChart.getXLabels();
        xl.setTextColor(Color.BLACK);
        xl.setTextSize(12f);

        YLabels yl = incomeLineChart.getYLabels();
        yl.setTextColor(Color.BLACK);
        yl.setTextSize(12f);

        return v;
    }

    public void LoadLineChart() throws ParseException, IOException, JSONException {
        txtIncomeGraphDate.setText(DateFormatHelper.GetMonthText(dateBeingViewed, getResources()));

        incomeLineChart.clear();

        incomes = user.GetBanker().GetIncomesFromMonth(dateBeingViewed);

        xVals = new ArrayList<String>();
        for (Income income : incomes) {
            xVals.add((DateFormatHelper.GetDayText(income.GetDate())));
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();
        for (int i = 0;i <incomes.size(); i++)
        {
            yVals.add(new Entry(incomes.get(i).GetAmount().floatValue(),i));
        }

        LineDataSet set = new LineDataSet(yVals, "Gelirler");
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
        incomeLineChart.setData(data);
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

        cal.add(Calendar.MONTH,1);
        dateBeingViewed = cal.getTime();
        LoadLineChart();
    }

    public void getLastDateIncomes() throws ParseException, JSONException, IOException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateBeingViewed);

        cal.add(Calendar.MONTH,-1);
        dateBeingViewed = cal.getTime();
        LoadLineChart();
    }

    @Override
    public void onValueSelected(Entry entry, int i) {
        Income income = incomes.get(entry.getXIndex());
        Bundle bundle = new Bundle();
        bundle.putBoolean("canDelete", false);

        bundle.putString("income", new Gson().toJson(income));
        IncomeDialogFragment dialog = new IncomeDialogFragment();

        dialog.setArguments(bundle);
        dialog.show(getActivity().getFragmentManager(), "dialog");

    }

    @Override
    public void onNothingSelected() {

    }
}
