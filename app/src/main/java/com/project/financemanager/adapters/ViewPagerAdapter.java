package com.project.financemanager.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private final ArrayList<Fragment> fragments = new ArrayList<>();
    private final ArrayList<String> fragmentTitles = new ArrayList<>();
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(Fragment fragment, String fragmentTitle){
        fragments.add(fragment);
        fragmentTitles.add(fragmentTitle);
    }

    public void removeAll(){
        fragments.removeAll(fragments);
        fragmentTitles.removeAll(fragmentTitles);
        notifyDataSetChanged();
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitles.get(position);
    }
    public void removeAllFragments(ViewPager viewPager) {
        for (int i = 0; i < getCount(); i++) {
            Fragment fragment = getItem(i);
            destroyItem(viewPager, i, fragment);
        }
    }
}
