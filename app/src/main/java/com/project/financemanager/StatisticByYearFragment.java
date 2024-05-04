package com.project.financemanager;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
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
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StatisticByYearFragment extends Fragment {

    private BarChart barChart;
    private ArrayList<String> recentDates;
    private ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_statistic_by_year, container, false);
        barChart = rootView.findViewById(R.id.barChartInChartByYear);
        progressBar = rootView.findViewById(R.id.progressInYear);

        progressBar.setVisibility(View.VISIBLE);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.getDefault());
        recentDates = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            recentDates.add(sdf.format(calendar.getTime()));
            calendar.add(Calendar.YEAR, -1); // Giảm 1 ngày
        }
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
        Call<List<StatisticByDayDTO>> call = ApiService.getInstance(getActivity()).getiApiService().getStatisticByDay("thisYear");
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
                        if (Integer.parseInt(data.get(j).getDate()) == Integer.parseInt(recentDates.get(i))){
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
                for (int i = 0; i < recentDates.size(); i++) {
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
                xAxis.setValueFormatter(new IndexAxisValueFormatter(recentDates));
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

        return rootView;
    }
}