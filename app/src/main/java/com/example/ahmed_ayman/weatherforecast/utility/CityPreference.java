package com.example.ahmed_ayman.weatherforecast.utility;

/**
 * Created by ahmed-ayman on 6/25/17.
 */
import android.app.Activity;
import android.content.SharedPreferences;

public class CityPreference {

    SharedPreferences prefs;

    public CityPreference(Activity activity){
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    /* If the user has not chosen a city yet, return
        Cairo as the default city
    */
    public String getCity(){
        return prefs.getString("city", "cairo");
    }

    public void setCity(String city){
        prefs.edit().putString("city", city).commit();
    }

}