package com.project.financemanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.project.financemanager.adapters.ViewPagerAdapter;
import com.project.financemanager.databinding.ActivityMainBinding;


public class CategoryFragment extends Fragment {
    private ViewPagerAdapter viewPageAdapter;
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

        viewPageAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPageAdapter.addFragment(new OutcomeCategory(), "Chi tiêu");
        viewPageAdapter.addFragment(new IncomeCategory(), "Thu nhập");
        viewPager.setAdapter(viewPageAdapter);

        return rootView;
    }
}