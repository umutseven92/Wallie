package Fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Legend;
import com.graviton.Cuzdan.R;

import java.util.ArrayList;

/**
 * Created by Umut on 15.1.2015.
 */
public class IncomeGraphFragment extends Fragment {

    PieChart incomePieChart;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.incomegraphfragment, container,false);
        incomePieChart = (PieChart)v.findViewById(R.id.incomePieChart);
        incomePieChart.setDescription("");

        incomePieChart.setValueTextColor(Color.BLACK);
        incomePieChart.setValueTextSize(15f);

        incomePieChart.setHoleColor(Color.rgb(235,235,235));
        incomePieChart.setHoleRadius(0);
        incomePieChart.setDrawYValues(true);
        incomePieChart.setDrawCenterText(true);
        incomePieChart.setDrawHoleEnabled(false);
        incomePieChart.setRotationAngle(0);

        incomePieChart.setDrawXValues(true);
        incomePieChart.setRotationEnabled(true);
        incomePieChart.setUsePercentValues(true);

        setData(2,100);

        incomePieChart.spin(1000,0, 360);

        Legend l = incomePieChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setTextSize(15f);

        return v;
    }

    private void setData(int count, float range) {

        String[] mParties = new String[]{"Demokrat", "Cumhuriyetci", "Anarsist"};
        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < count + 1; i++) {
            yVals1.add(new Entry((float) (Math.random() * mult) + mult / 5, i));
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < count + 1; i++)
            xVals.add(mParties[i % mParties.length]);

        PieDataSet set1 = new PieDataSet(yVals1, "");
        set1.setSliceSpace(0f);

        set1.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData data = new PieData(xVals, set1);
        incomePieChart.setData(data);

        // undo all highlights
        incomePieChart.highlightValues(null);

        incomePieChart.invalidate();
    }

}
