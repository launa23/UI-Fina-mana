package com.project.financemanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.project.financemanager.adapters.ViewPageAdapter;


public class CategoryFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_category, container, false);
        tabLayout = rootView.findViewById(R.id.tabLayoutInCategory);
        viewPager = rootView.findViewById(R.id.viewPager);
        tabLayout.setupWithViewPager(viewPager);

        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getActivity().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPageAdapter.addFragment(new OutcomeCategory(), "Chi tiêu");
        viewPageAdapter.addFragment(new IncomeCategory(), "Thu nhập");
        viewPager.setAdapter(viewPageAdapter);
        return rootView;
    }
}