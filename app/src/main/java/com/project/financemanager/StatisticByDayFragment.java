package com.project.financemanager;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

public class StatisticByDayFragment extends Fragment {
    private BarChart barChart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_statistic_by_day, container, false);
        barChart = rootView.findViewById(R.id.barChartInChart);
        ValueFormatter yAxisFormatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                // Chuyển đổi giá trị lớn thành đơn vị "k"
                if (value >= 1000000) {
                    return Math.round(value / 1000000) + "M";
                } else {
                    return String.valueOf(value);
                }
            }
        };

        barChart.getAxisLeft().setValueFormatter(yAxisFormatter);
        barChart.getXAxis().setDrawGridLines(false);
//        barChart.getAxisRight().setDrawGridLines(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.getXAxis().setDrawAxisLine(false);
//        barChart.getXAxis().setDrawLabels(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(true);
        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.getDescription().setEnabled(false);

        String[] title = new String[]{"24/4", "25/4", "26/4", "27/4", "28/4", "29/4", "30/4", "31/4", "32/4"};

        ArrayList<BarEntry> entries1 = new ArrayList<>();
        ArrayList<BarEntry> entries2 = new ArrayList<>();
        for (int i = 0; i < title.length; i++) {
            float value1 = dataValue1().get(i).getY();
            float value2 = dataValue2().get(i).getY();
            entries1.add(new BarEntry(i, value1));
            entries2.add(new BarEntry(i, value2));
        }

        BarDataSet barDataSet1 = new BarDataSet(entries1, null);
        barDataSet1.setColor(Color.parseColor("#02b835"));

        BarDataSet barDataSet2 = new BarDataSet(entries2, null);
        barDataSet2.setColor(Color.parseColor("#d43939"));

        BarData barData = new BarData(barDataSet1, barDataSet2);
        barData.setDrawValues(false);
        barChart.setData(barData);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(title));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(0);
        xAxis.setGranularityEnabled(true);
        xAxis.setAxisMinimum(0f);

        float groupSpace = 0.36f;
        float barWidth = 0.3f;

        barChart.getBarData().setBarWidth(barWidth);
        barChart.groupBars(0, groupSpace, 0.02f);

        barChart.invalidate();
        xAxis.setSpaceMin(0.08f);
        xAxis.setSpaceMax(0.8f);
        return rootView;
    }

    private ArrayList<BarEntry> dataValue1(){
        ArrayList<BarEntry> data = new ArrayList<>();
        data.add(new BarEntry(0, 12000000));
        data.add(new BarEntry(1, 13500000));
        data.add(new BarEntry(2, 12000000));
        data.add(new BarEntry(3, 13500000));
        data.add(new BarEntry(4, 12000000));
        data.add(new BarEntry(5, 13500000));
        data.add(new BarEntry(6, 13500000));
        data.add(new BarEntry(7, 13500000));
        data.add(new BarEntry(8, 13500000));
        return data;
    }
    private ArrayList<BarEntry> dataValue2(){
        ArrayList<BarEntry> data = new ArrayList<>();
        data.add(new BarEntry(0, 11500000));
        data.add(new BarEntry(1, 16500000));
        data.add(new BarEntry(2, 12000000));
        data.add(new BarEntry(3, 13500000));
        data.add(new BarEntry(4, 12000000));
        data.add(new BarEntry(5, 13500000));
        data.add(new BarEntry(6, 13500000));
        data.add(new BarEntry(7, 13500000));
        data.add(new BarEntry(8, 11500000));
        return data;
    }
}