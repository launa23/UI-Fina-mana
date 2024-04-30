package com.project.financemanager;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;


public class ChartFragment extends Fragment {
    private BarChart barChart;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_chart, container, false);

        barChart = rootView.findViewById(R.id.barChart);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.getXAxis().setDrawAxisLine(false);
        barChart.getXAxis().setDrawLabels(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.getDescription().setEnabled(false);

        BarDataSet barDataSet1 = new BarDataSet(dataValue1(), null);
        barDataSet1.setColor(Color.parseColor("#02b835"));
        BarDataSet barDataSet2 = new BarDataSet(dataValue2(), null);
        barDataSet2.setColor(Color.parseColor("#d43939"));
        BarData barData = new BarData(barDataSet1, barDataSet2);
        barChart.setData(barData);

        String[] title = new String[]{"Chi tiêu", "Thu nhập"};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(title));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);

        float groupSpace = 0.44f;
        float barSpace = 0.68f;
        float barWidth = 3.0f;

        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(0+barChart.getBarData().getGroupWidth(groupSpace, barSpace)*2);
        barData.setBarWidth(barWidth);


        barChart.groupBars(0, groupSpace, barSpace);
        barChart.invalidate();
        return rootView;
    }

    private ArrayList<BarEntry> dataValue1(){
        ArrayList<BarEntry> data = new ArrayList<>();
        data.add(new BarEntry(1, 19000000));
        return data;
    }
    private ArrayList<BarEntry> dataValue2(){
        ArrayList<BarEntry> data = new ArrayList<>();
        data.add(new BarEntry(1, 20000000));
        return data;
    }
}