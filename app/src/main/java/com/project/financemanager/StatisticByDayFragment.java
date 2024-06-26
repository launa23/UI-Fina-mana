package com.project.financemanager;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.project.financemanager.api.ApiService;
import com.project.financemanager.dtos.StatisticByDayDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticByDayFragment extends Fragment {
    private BarChart barChart;
    private ArrayList<String> recentDates;
    private ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_statistic_by_day, container, false);
        barChart = rootView.findViewById(R.id.barChartInChart);
        progressBar = rootView.findViewById(R.id.progressInDay);

        progressBar.setVisibility(View.VISIBLE);
        ArrayList<String> title = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM", Locale.getDefault());
        recentDates = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            recentDates.add(sdf.format(calendar.getTime()));
            title.add(sdf1.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_YEAR, -1); // Giảm 1 ngày
        }
        Collections.reverse(title);
        Collections.reverse(recentDates);
        ValueFormatter yAxisFormatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value >= 1000000) {
                    return Math.round(value / 1000000) + "M";
                } else if (value >= 1000){
                    return Math.round(value / 1000) + "K";
                }
                else {
                    return String.valueOf(value);
                }
            }
        };
        Call<List<StatisticByDayDTO>> call = ApiService.getInstance(getActivity()).getiApiService().getStatisticByDay("today");
        call.enqueue(new Callback<List<StatisticByDayDTO>>() {
            @Override
            public void onResponse(Call<List<StatisticByDayDTO>> call, Response<List<StatisticByDayDTO>> response) {
                List<StatisticByDayDTO> data = response.body();
                ArrayList<BarEntry> incomes = new ArrayList<>();
                ArrayList<BarEntry> outcomes = new ArrayList<>();
                int fl;
                for (int i = 0; i < recentDates.size(); i++){
                    fl = 0;
                    for (int j = 0; j < data.size(); j++){
                        if (Objects.equals(data.get(j).getDate(), recentDates.get(i))){
                            fl = 1;
                            incomes.add(new BarEntry(i, Float.valueOf(data.get(j).getTotalIncome())));
                            outcomes.add(new BarEntry(i, Float.valueOf(data.get(j).getTotalOutcome())));
                            break;
                        }
                    }
                    if(fl == 0){
                        incomes.add(new BarEntry(i, 0));
                        outcomes.add(new BarEntry(i, 0));
                    }
                }
                barChart.getAxisLeft().setValueFormatter(yAxisFormatter);
                barChart.getXAxis().setDrawGridLines(false);
                barChart.setDoubleTapToZoomEnabled(false);
                barChart.getXAxis().setDrawAxisLine(false);
                barChart.getAxisRight().setEnabled(false);
                barChart.getAxisLeft().setEnabled(true);
                barChart.getAxisLeft().setAxisMinimum(0);
                barChart.getDescription().setEnabled(false);

                ArrayList<BarEntry> entries1 = new ArrayList<>();
                ArrayList<BarEntry> entries2 = new ArrayList<>();
                for (int i = 0; i < title.size(); i++) {
                    float value1 = incomes.get(i).getY();
                    float value2 = outcomes.get(i).getY();
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
                xAxis.setGranularity(1f);
                xAxis.setLabelCount(9);
                xAxis.setGranularityEnabled(true);
                xAxis.setAxisMinimum(0f);

                float groupSpace = 0.36f;
                float barWidth = 0.3f;

                barChart.getBarData().setBarWidth(barWidth);
                barChart.groupBars(0, groupSpace, 0.02f);
                xAxis.setSpaceMin(0f);
                xAxis.setSpaceMax(0f);
                barChart.invalidate();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<StatisticByDayDTO>> call, Throwable throwable) {

            }
        });
//        barChart.getAxisLeft().setValueFormatter(yAxisFormatter);
//        barChart.getXAxis().setDrawGridLines(false);
//        barChart.setDoubleTapToZoomEnabled(false);
//        barChart.getXAxis().setDrawAxisLine(false);
//        barChart.getAxisRight().setEnabled(false);
//        barChart.getAxisLeft().setEnabled(true);
//        barChart.getAxisLeft().setAxisMinimum(0);
//        barChart.getDescription().setEnabled(false);
//
//        ArrayList<BarEntry> entries1 = new ArrayList<>();
//        ArrayList<BarEntry> entries2 = new ArrayList<>();
//        for (int i = 0; i < title.size(); i++) {
//            float value1 = dataValue1().get(i).getY();
//            float value2 = dataValue2().get(i).getY();
//            entries1.add(new BarEntry(i, value1));
//            entries2.add(new BarEntry(i, value2));
//        }
//
//        BarDataSet barDataSet1 = new BarDataSet(entries1, null);
//        barDataSet1.setColor(Color.parseColor("#02b835"));
//
//        BarDataSet barDataSet2 = new BarDataSet(entries2, null);
//        barDataSet2.setColor(Color.parseColor("#d43939"));
//
//        BarData barData = new BarData(barDataSet1, barDataSet2);
//        barData.setDrawValues(false);
//        barChart.setData(barData);
//
//        XAxis xAxis = barChart.getXAxis();
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(title));
//        xAxis.setCenterAxisLabels(true);
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setGranularity(0);
//        xAxis.setGranularityEnabled(true);
//        xAxis.setAxisMinimum(0f);
//
//        float groupSpace = 0.36f;
//        float barWidth = 0.3f;
//
//        barChart.getBarData().setBarWidth(barWidth);
//        barChart.groupBars(0, groupSpace, 0.02f);
//        xAxis.setSpaceMin(0.08f);
//        xAxis.setSpaceMax(0.8f);
        barChart.invalidate();

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