package Fragments;

import android.graphics.Color;
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
import com.graviton.Cuzdan.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Umut on 15.1.2015.
 */
public class IncomePieFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    PieChart incomePieChart;
    Spinner spnIncomePieDate;
    String mode = "month";
    ImageButton imgLeft, imgRight;
    Date dateBeingViewed;

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
        dateBeingViewed = new Date();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(), R.array.balanceDateArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnIncomePieDate.setAdapter(adapter);
        spnIncomePieDate.setOnItemSelectedListener(this);

        InitializePieChart(incomePieChart);

        if(mode == "day")
        {
            LoadPieChart(2, 100, true);
        }
        else if(mode == "month")
        {
            LoadPieChart(2,100, false);
        }

        Legend l = incomePieChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setTextSize(15f);

        return v;
    }

    public void InitializePieChart(PieChart chart)
    {
        chart.setDescription("");
        chart.setValueTextColor(Color.BLACK);
        chart.setValueTextSize(15f);
        chart.setHoleColor(Color.parseColor("#fffff0"));
        chart.setHoleRadius(60f);
        chart.setDrawYValues(true);
        chart.setDrawCenterText(true);
        chart.setCenterTextSize(20f);
        chart.setDrawHoleEnabled(false);
        chart.setRotationAngle(0);
        chart.setDrawXValues(true);
        chart.setRotationEnabled(true);
        chart.setUsePercentValues(true);
        chart.spin(1000, 0, 360);

    }

    private void LoadPieChart(int count, float range, boolean day) {

        String[] mParties = new String[]{"Demokrat", "Cumhuriyetci", "Anarsist"};
        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i = 0; i < count + 1; i++) {
            yVals1.add(new Entry((float) (Math.random() * mult) + mult / 5, i));
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < count + 1; i++)
            xVals.add(mParties[i % mParties.length]);

        PieDataSet set1 = new PieDataSet(yVals1, "");
        set1.setSliceSpace(3f);

        set1.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData(xVals, set1);
        incomePieChart.setData(data);

        // undo all highlights
        incomePieChart.highlightValues(null);

        incomePieChart.invalidate();
    }


    View.OnClickListener onLeftArrowClick = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            getLastDateIncomes();
        }
    };

    View.OnClickListener onRightArrowClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getNextDateIncomes();
        }
    };

    public void getNextDateIncomes()
    {
        Date today = new Date();

        if(dateBeingViewed.getDay() == today.getDay())
        {
            return;
        }

        Calendar cal = Calendar.getInstance();

        cal.setTime(dateBeingViewed);

        if(mode.equals("day"))
        {
            cal.add(Calendar.DATE,1);
            dateBeingViewed = cal.getTime();
            LoadPieChart(2,100,true);
        }
        else if (mode.equals("month"))
        {
            cal.add(Calendar.MONTH,1);
            dateBeingViewed = cal.getTime();
            LoadPieChart(2,100,false);
        }
    }

    public void getLastDateIncomes() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateBeingViewed);

        if(mode.equals("day"))
        {
            cal.add(Calendar.DATE,-1);
            dateBeingViewed = cal.getTime();
            LoadPieChart(2,100,true);
        }
        else if (mode.equals("month"))
        {
            cal.add(Calendar.MONTH,-1);
            dateBeingViewed = cal.getTime();
            LoadPieChart(2,100,false);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0)
        {
            mode = "month";
            LoadPieChart(2,100, false);
        }
        else if(position == 1)
        {
            mode = "day";
            LoadPieChart(2,100, true);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
