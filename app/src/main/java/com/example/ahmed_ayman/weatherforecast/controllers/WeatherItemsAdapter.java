package com.example.ahmed_ayman.weatherforecast.controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmed_ayman.weatherforecast.R;
import com.example.ahmed_ayman.weatherforecast.model.WeatherForecastItem;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;

/**
 * Created by ahmed-ayman on 6/25/17.
 * <p>
 * Adapter to be used with the List Views.
 */

public class WeatherItemsAdapter extends ArrayAdapter<WeatherForecastItem> {

    public WeatherItemsAdapter(Context context, List<WeatherForecastItem> items) {
        super(context, 0, items);
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        WeatherForecastItem weatherForecastItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView detailsField = (TextView) convertView.findViewById(R.id.details_field);
        TextView maxTemp = (TextView) convertView.findViewById(R.id.max_temp);
        TextView minTemp = (TextView) convertView.findViewById(R.id.min_tem);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView humadity = (TextView) convertView.findViewById(R.id.humidity);
        TextView pressure = (TextView) convertView.findViewById(R.id.pressure);
        try {
            date.setText(weatherForecastItem.getDate());
            humadity.setText("Humadity : "+weatherForecastItem.getHumadity());
            pressure.setText("Pressure : "+ weatherForecastItem.getPresuure());
            detailsField.setText(weatherForecastItem.getIconDesc().toUpperCase(Locale.US));

            String IMG_URL = "http://openweathermap.org/img/w/" + weatherForecastItem.getIconCode() + ".png";

            maxTemp.setText( String.format("%.2f", weatherForecastItem.getDayTemp()) + " ℃" );
            minTemp.setText("Night : " + String.format("%.2f", weatherForecastItem.getNighTemp()) + " ℃");
            new DownloadImageTask((ImageView) convertView.findViewById(R.id.weather_icon))
                    .execute(IMG_URL);

        } catch (Exception e) {
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
        return convertView;
    }

    /**
     * this AsyncTask gets the Current weather state icon.
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
