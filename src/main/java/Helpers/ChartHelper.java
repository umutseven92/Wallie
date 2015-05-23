package Helpers;

import android.graphics.Color;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;

/**
 * Created by Umut Seven on 27.1.2015, for Graviton.
 * <p/>
 * Istatistik ekranlarinda kullanilan grafikleri initialize ettigimiz class.
 * Her grafik bu class'i kullanmali.
 */
public class ChartHelper {

    public static void InitializePieChart(PieChart chart) {
        chart.setDescription("");

        chart.setUsePercentValues(true);
        chart.setValueTextColor(Color.BLACK);
        chart.setValueTextSize(15f);

        chart.setDrawLegend(false);

        chart.setDrawYValues(true);
        chart.setDrawXValues(true);

        chart.setDrawHoleEnabled(true);
        chart.setHoleRadius(40f);
        chart.setTransparentCircleRadius(0f);
        chart.setHoleColor(Color.parseColor("#ffe0a8"));

        chart.setCenterText("");
        chart.setCenterTextSize(20f);

        chart.setRotationAngle(0);
        chart.setRotationEnabled(true);

        chart.spin(800, 0, 360);

    }

    public static void InitializeLineChart(LineChart chart, String currency) {
        chart.setValueTextColor(Color.BLACK);
        chart.setUnit(" " + currency);
        chart.setDrawUnitsInChart(true);

        chart.setDrawYValues(false);
        chart.setStartAtZero(false);
        chart.setDrawBorder(true);

        chart.setBorderPositions(new BarLineChartBase.BorderPosition[]
                {
                        BarLineChartBase.BorderPosition.BOTTOM
                });

        chart.setDescription("");
        chart.setNoDataText("Gelir yok.");
        chart.setNoDataTextDescription("Gelir yok.");

        chart.setHighlightEnabled(true);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setDrawVerticalGrid(true);
        chart.setDrawHorizontalGrid(true);

        chart.setPinchZoom(true);

        chart.setBackgroundColor(Color.TRANSPARENT);

        chart.animateX(2000);
    }
}
