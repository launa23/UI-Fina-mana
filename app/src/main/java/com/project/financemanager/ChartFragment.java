package com.project.financemanager;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.project.financemanager.adapters.ViewPagerAdapter;

import java.util.ArrayList;


public class ChartFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_chart, container, false);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new StatisticByDayFragment(), "Theo ngày");
        adapter.addFragment(new StatisticByMonthFragment(), "Theo tháng");
        adapter.addFragment(new StatisticByYearFragment(), "Theo năm");
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) rootView.findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);

        return rootView;
    }

}