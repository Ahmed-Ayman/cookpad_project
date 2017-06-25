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

/**
 * Created by ahmed-ayman on 6/25/17.
 */

public class WeeklyFragment extends Fragment {
    ListView listView1 ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weekly_fragment, container, false);

        listView1 = (ListView) view.findViewById(R.id.list_view1);
        listView1.setAdapter( new WeatherItemsAdapter(getActivity(), MainActivity.weatherItemsArray));
        return view;
    }
}
