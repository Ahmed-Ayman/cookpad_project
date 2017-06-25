package com.example.ahmed_ayman.weatherforecast;

/**
 * Created by ahmed-ayman on 6/25/17.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ahmed_ayman.weatherforecast.controllers.PagerAdapter;
import com.example.ahmed_ayman.weatherforecast.model.WeatherForecastItem;
import com.example.ahmed_ayman.weatherforecast.utility.CityPreference;
import com.example.ahmed_ayman.weatherforecast.utility.HttpHandler;
import com.example.ahmed_ayman.weatherforecast.utility.Parser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    public static ArrayList<WeatherForecastItem> weatherItemsArray;
    private ProgressDialog pDialog;
    public static String cityName;
/*
    Kindly add your API KEY right here
adb221b9d8fbb0804f3c60664ced3631
 */
    String id ="adb221b9d8fbb0804f3c60664ced3631" ;
    private String url = "http://api.openweathermap.org/data/2.5/forecast/daily?q=%s&units=metric&&cnt=7&APPID=".concat(id);

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.change_city) {
            showInputDialog();
        }
        if (item.getItemId() == R.id.menu_item_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareSubject = getString(R.string.share_title);
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSubject);
            String shareBody = "the weather today in " + weatherItemsArray.get(0).getCity() + ": " +
                    +weatherItemsArray.get(0).getDayTemp() + " ℃" +
                    "\n see more details at " + "<APP URL>";
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }

        return false;
    }
    /*
        Input Dialog to get the New City
     */
    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Change City");
        final EditText input = new EditText(MainActivity.this);
        input.setHint("Enter a city Name eg, (cairo,eg)");
        input.setHintTextColor(Color.GRAY);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newCity = input.getText().toString();
                changeCity(newCity);
            }
        });
        builder.show();
    }
    private void showLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.dialog_title));
        builder.setMessage(getString(R.string.dialog_message));
        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isOnline()){
            setContentView(R.layout.main_activity);
            cityName = new CityPreference(this).getCity();
        try {
            weatherItemsArray = new FetchTheWeatherData().execute(cityName).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("TODAY"));
        tabLayout.addTab(tabLayout.newTab().setText("THIS WEEK"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //Pager
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    } else{
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Connection Problem");
            builder.setMessage("there is a problem with the connection\n"+
            "Do you want to check the WIFI settings ?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    MainActivity.this.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
            });
            builder.setNegativeButton("Close",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.exit(1);
                }
            });
            builder.show();
        }
    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            return false;
        }
        return true;
    }

    /**
     *    getting the new city and restarting everything again to get the new data.
     * @param city : the new city.
     *
     */
    public void changeCity(String city) {
        updateWeatherData(city);
        this.onRestart();
        new CityPreference(MainActivity.this).setCity(city);
    }

    /**
     *      Just implementing the Async Task
     * @param city : the new city.
     */
    private void updateWeatherData(String city) {
        new FetchTheWeatherData().execute(city);
    }


    class FetchTheWeatherData extends AsyncTask<String, Void, ArrayList<WeatherForecastItem>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected ArrayList<WeatherForecastItem> doInBackground(String... arg0) {
            weatherItemsArray = new ArrayList<>();
            HttpHandler httpHandler = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = httpHandler.makeServiceCall(String.format(url, arg0));  //pass the city name
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    weatherItemsArray.addAll(Parser.parseJson(new JSONObject(jsonStr)));
                    cityName = weatherItemsArray.get(0).getCity();
                    Log.v("size", "" + weatherItemsArray.size());


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,
                                    "something went wrong! (" + e.getMessage() + ")",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        showInputDialog();
                        showLocationDialog();
                        Toast.makeText(MainActivity.this, "kindly Enter a valid city \nlike : (Cairo,eg)", Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return weatherItemsArray;
        }
        @Override
        protected void onPostExecute(ArrayList<WeatherForecastItem> weatherForecastItems) {
            super.onPostExecute(weatherForecastItems);
            if (pDialog.isShowing())
                pDialog.dismiss();
            if(weatherForecastItems.size()>0)
            MainActivity.this.setTitle(weatherForecastItems.get(0).getCity());
            else
                MainActivity.this.setTitle("no specified location");

        }
    }
    //I will restart the activity after every  CityName change.

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent i = new Intent(MainActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}