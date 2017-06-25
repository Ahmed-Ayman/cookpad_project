package com.example.ahmed_ayman.weatherforecast.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ahmed_ayman.weatherforecast.MainActivity;
import com.example.ahmed_ayman.weatherforecast.R;
import com.example.ahmed_ayman.weatherforecast.controllers.WeatherItemsAdapter;
import com.example.ahmed_ayman.weatherforecast.model.WeatherForecastItem;

import java.util.ArrayList;

/**
 * Created by ahmed-ayman on 6/25/17.
 */

public class TodayForecastFragment extends Fragment {
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.today_and_tomorrow_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.list_view);

        ArrayList<WeatherForecastItem> items = MainActivity.weatherItemsArray;
        if(items.size()>2)
        listView.setAdapter(new WeatherItemsAdapter(getActivity(), items.subList(0, 2)));
        else
            listView.setAdapter(new WeatherItemsAdapter(getActivity(),items));
        return view;
    }


}
