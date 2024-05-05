package com.project.financemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.tabs.TabLayout;
import com.project.financemanager.adapters.ViewPagerAdapter;

public class ChooseCategoryActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView btnBackInChooseCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPage);
        btnBackInChooseCategory = findViewById(R.id.btnBackInChooseCate);
        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPageAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPageAdapter.addFragment(new OutcomeFragment(), "Chi tiêu");
        viewPageAdapter.addFragment(new IncomeFragment(), "Thu nhập");
        viewPager.setAdapter(viewPageAdapter);
        btnBackInChooseCategory.setOnClickListener(v -> {
            finish();
        });
    }
}