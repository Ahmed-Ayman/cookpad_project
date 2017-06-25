package com.example.ahmed_ayman.weatherforecast.utility;

import com.example.ahmed_ayman.weatherforecast.model.WeatherForecastItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ahmed-ayman on 6/25/17.
 *
 * this Class has just on method that takes a String Json response and parse it.
 */

public class Parser {
    /**
     *
     * @param jsonResponse: String response.
     * @return Array List of weather objects full of the data from the API.
     * @throws JSONException
     */
    public static  ArrayList<WeatherForecastItem> parseJson(JSONObject jsonResponse) throws JSONException {
        WeatherForecastItem weatherItem ;
          ArrayList<WeatherForecastItem> items = new ArrayList<>();

        // city..
        JSONObject city =jsonResponse.getJSONObject("city");
        String cityName=city.getString("name").toUpperCase(Locale.US)+", " + city.getString("country");

        JSONArray list = jsonResponse.getJSONArray("list");
        for (int i =0;i<list.length();i++){

            weatherItem = new WeatherForecastItem();
            JSONObject currentObject = list.getJSONObject(i);
            weatherItem.setCity(cityName);
            //date
            Long date=currentObject.getLong("dt");
            DateFormat df = DateFormat.getDateInstance();
            if(i==0)
                weatherItem.setDate("TODAY");
            else if (i==1)
                weatherItem.setDate("TOMORROW");
            else
               weatherItem.setDate(df.format(new Date(date*1000)));
            //temp
            JSONObject temp = currentObject.getJSONObject("temp");
            weatherItem.setDayTemp(temp.getDouble("day"));
            weatherItem.setNighTemp(temp.getDouble("night"));

            //characteristics
            weatherItem.setHumadity(currentObject.getString("humidity"));
            weatherItem.setPresuure(currentObject.getString("pressure"));

            //weather
            JSONArray weatherArray = currentObject.getJSONArray("weather");

                JSONObject weather = weatherArray.getJSONObject(0);
                weatherItem.setIconCode(weather.getString("icon"));
                weatherItem.setIconDesc(weather.getString("description"));
                weatherItem.setIconId(weather.getInt("id"));

            items.add(weatherItem);
        }
        return items;
    }
}
