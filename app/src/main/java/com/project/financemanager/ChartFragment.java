package com.project.financemanager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.project.financemanager.adapters.ViewPagerAdapter;
import com.project.financemanager.api.ApiService;
import com.project.financemanager.common.CustomMarkerView;
import com.project.financemanager.dtos.StatisticByCategoryDTO;
import com.tapadoo.alerter.Alerter;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChartFragment extends Fragment {
    private PieChart pieChartIncome;
    private PieChart pieChartOutcome;
    private RelativeLayout txtTitleIncome;
    private RelativeLayout txtTitleOutcome;
    private List<StatisticByCategoryDTO> statisticIncome;
    private List<StatisticByCategoryDTO> statisticOutcome;
    private ProgressBar progressBarInPieOutcome;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_chart, container, false);
        pieChartIncome = rootView.findViewById(R.id.pieChartIncome);
        pieChartOutcome = rootView.findViewById(R.id.pieChartOutcome);
        txtTitleOutcome = rootView.findViewById(R.id.txtTitleIncome);
        txtTitleIncome = rootView.findViewById(R.id.txtTitleOutcome);
        progressBarInPieOutcome = rootView.findViewById(R.id.progressBarInPieOutcome);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new StatisticByDayFragment(), "Theo ngày");
        adapter.addFragment(new StatisticByMonthFragment(), "Theo tháng");
        adapter.addFragment(new StatisticByYearFragment(), "Theo năm");
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) rootView.findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);

        // Biểu đồ
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String start = sdf.format(calendar.getTime());

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String end = sdf.format(calendar.getTime());

        int[] colorsI = new int[]{
                Color.parseColor("#9E05FC"),
                Color.parseColor("#FCBA04"),
                Color.parseColor("#FF6666"),
                Color.parseColor("#FC8905"),
                Color.parseColor("#C80000"),
                Color.parseColor("#771F30"),
                Color.parseColor("#8954A5"),
                Color.parseColor("#FF3636"),
                Color.parseColor("#9030EA"),
                Color.parseColor("#02b835"),
                Color.parseColor("#0A6847"),
                Color.parseColor("#6962AD"),
                Color.parseColor("#378CE7"),
                Color.parseColor("#8DECB4"),
                Color.parseColor("#87A922"),
                Color.parseColor("#8954A5"),
                Color.parseColor("#FF3636"),
                Color.parseColor("#9030EA")
        };
        int[] colorsO = new int[]{
                Color.parseColor("#02b835"),
                Color.parseColor("#0A6847"),
                Color.parseColor("#6962AD"),
                Color.parseColor("#378CE7"),
                Color.parseColor("#8DECB4"),
                Color.parseColor("#87A922"),
                Color.parseColor("#8954A5"),
                Color.parseColor("#FF3636"),
                Color.parseColor("#9030EA"),
                Color.parseColor("#9E05FC"),
                Color.parseColor("#FCBA04"),
                Color.parseColor("#FF6666"),
                Color.parseColor("#FC8905"),
                Color.parseColor("#C80000"),
                Color.parseColor("#771F30"),
                Color.parseColor("#8954A5"),
                Color.parseColor("#FF3636"),
                Color.parseColor("#9030EA")
        };
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        Call<List<StatisticByCategoryDTO>> call = ApiService.getInstance(getContext()).getiApiService().getStatisticByCategory(start, end, "incom");
        call.enqueue(new Callback<List<StatisticByCategoryDTO>>() {
            @Override
            public void onResponse(Call<List<StatisticByCategoryDTO>> call, Response<List<StatisticByCategoryDTO>> response) {
                List<StatisticByCategoryDTO> statistics = response.body();
                statisticOutcome = response.body();
                int totalOutcome = 0;
                ArrayList<PieEntry> entries = new ArrayList<>();
                for (StatisticByCategoryDTO statistic : statistics) {
                    entries.add(new PieEntry(Float.parseFloat(statistic.getTotal()), statistic.getName()));
                    totalOutcome += Integer.parseInt(statistic.getTotal());
                }
                PieDataSet dataSet = new PieDataSet(entries, null);
                dataSet.setColors(colorsI);
                dataSet.setValueTextSize(0f);
                dataSet.setSliceSpace(1f);
//                Legend legend = pieChartIncome.getLegend();
//                legend.setDrawInside(false);
//                legend.setWordWrapEnabled(true);
//                legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                pieChartOutcome.getDescription().setEnabled(false);
                pieChartOutcome.setTransparentCircleAlpha(0);
                pieChartOutcome.setHoleColor(Color.TRANSPARENT);

                pieChartOutcome.setCenterText(numberFormat.format((totalOutcome)) + " đ");
                pieChartOutcome.setCenterTextSize(16f);
                pieChartOutcome.setCenterTextTypeface(Typeface.DEFAULT_BOLD);
                pieChartOutcome.setCenterTextColor(Color.parseColor("#d43939"));

                PieData data = new PieData(dataSet);
                pieChartOutcome.setTransparentCircleAlpha(0);
                pieChartOutcome.getDescription().setEnabled(false);
                pieChartOutcome.setData(data);
                pieChartOutcome.setEntryLabelTextSize(8f);
                pieChartOutcome.invalidate();
                pieChartOutcome.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<List<StatisticByCategoryDTO>> call, Throwable throwable) {

            }
        });

        Call<List<StatisticByCategoryDTO>> call1 = ApiService.getInstance(getContext()).getiApiService().getStatisticByCategory(start, end, "income");
        call1.enqueue(new Callback<List<StatisticByCategoryDTO>>() {
            @Override
            public void onResponse(Call<List<StatisticByCategoryDTO>> call, Response<List<StatisticByCategoryDTO>> response) {
                List<StatisticByCategoryDTO> statistics = response.body();
                statisticIncome = response.body();
                int totalOutcome = 0;
                ArrayList<PieEntry> entries = new ArrayList<>();
                for (StatisticByCategoryDTO statistic : statistics) {
                    entries.add(new PieEntry(Float.parseFloat(statistic.getTotal()), statistic.getName()));
                    totalOutcome += Integer.parseInt(statistic.getTotal());
                }
                PieDataSet dataSet = new PieDataSet(entries, null);
                dataSet.setColors(colorsO);
                dataSet.setValueTextSize(0f);
                dataSet.setSliceSpace(1f);
                Legend legend = pieChartIncome.getLegend();
                legend.setDrawInside(false);
                legend.setWordWrapEnabled(true);
                legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                pieChartIncome.getDescription().setEnabled(false);
                pieChartIncome.setTransparentCircleAlpha(0);
                pieChartIncome.setHoleColor(Color.TRANSPARENT);

                pieChartIncome.setCenterText(numberFormat.format((totalOutcome)) + " đ");
                pieChartIncome.setCenterTextSize(16f);
                pieChartIncome.setCenterTextTypeface(Typeface.DEFAULT_BOLD);
                pieChartIncome.setCenterTextColor(Color.parseColor("#02b835"));

                PieData data = new PieData(dataSet);
                pieChartIncome.setTransparentCircleAlpha(0);
                pieChartIncome.getDescription().setEnabled(false);
                pieChartIncome.setData(data);
                pieChartIncome.setEntryLabelTextSize(8f);
                pieChartIncome.invalidate();
                pieChartIncome.setVisibility(View.VISIBLE);
                progressBarInPieOutcome.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<StatisticByCategoryDTO>> call, Throwable throwable) {

            }
        });


        txtTitleOutcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(statisticOutcome.isEmpty()){
                    Alerter.create(getActivity())
                            .setTitle("Vui lòng thử lại sau!")
                            .enableSwipeToDismiss()
                            .setIcon(R.drawable.ic_baseline_info_24)
                            .setBackgroundColorRes(R.color.orange)
                            .setIconColorFilter(0)
                            .setIconSize(R.dimen.icon_alert)
                            .show();
                }
                else {
                    Intent intent = new Intent(getActivity(), DetailStatisticActivity.class);
                    intent.putExtra("detailList", (Serializable) statisticOutcome);
                    intent.putExtra("type", "Chi tiêu");
                    startActivity(intent);
                }
            }
        });
        txtTitleIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(statisticIncome.isEmpty()){
                    Alerter.create(getActivity())
                            .setTitle("Vui lòng thử lại sau!")
                            .enableSwipeToDismiss()
                            .setIcon(R.drawable.ic_baseline_info_24)
                            .setBackgroundColorRes(R.color.orange)
                            .setIconColorFilter(0)
                            .setIconSize(R.dimen.icon_alert)
                            .show();
                }
                else {
                    Intent intent = new Intent(getActivity(), DetailStatisticActivity.class);
                    intent.putExtra("detailList", (Serializable) statisticIncome);
                    intent.putExtra("type", "Thu nhập");
                    startActivity(intent);
                }
            }
        });
        return rootView;
    }

}