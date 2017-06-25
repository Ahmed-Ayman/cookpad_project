package com.example.ahmed_ayman.weatherforecast.controllers;

/**
 * Created by ahmed-ayman on 6/25/17.
 */


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import android.support.v4.app.Fragment;

import com.example.ahmed_ayman.weatherforecast.fragments.TodayForecastFragment;
import com.example.ahmed_ayman.weatherforecast.fragments.WeeklyFragment;


public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                 TodayForecastFragment tab1 = new TodayForecastFragment();
                return tab1;
            case 1:
                WeeklyFragment tab2 = new WeeklyFragment();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
