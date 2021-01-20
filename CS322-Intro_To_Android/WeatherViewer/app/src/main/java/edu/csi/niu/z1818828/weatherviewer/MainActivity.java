package edu.csi.niu.z1818828.weatherviewer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Weather> weatherList = new ArrayList<>();

    private WeatherArrayAdapter weatherArrayAdapter;
    private ListView weatherListView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        weatherListView = findViewById(R.id.weatherListView);
        weatherArrayAdapter = new WeatherArrayAdapter(this, weatherList);
        weatherListView.setAdapter(weatherArrayAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText locationEditText = findViewById(R.id.locationEditText);
                URL url = createURL(locationEditText.getText().toString());

                if(url != null) {
                    dismissKeyboard(locationEditText);
                    GetWeatherTask getLocalWeatherTask = new GetWeatherTask();
                    getLocalWeatherTask.execute(url);
                }
                else {
                    Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.invalid_url, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void dismissKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private URL createURL(String city) {
        String apiKey = getString(R.string.api_key);
        String baseUrl = getString(R.string.web_service_url);

        try {
            String urlString = baseUrl + URLEncoder.encode(city, "UTF-8") + "&units=imperial&APPID=" + apiKey;
            return new URL(urlString);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private class GetWeatherTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... params) {
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) params[0].openConnection();
                int response = connection.getResponseCode();

                if(response == HttpURLConnection.HTTP_OK) {
                    StringBuilder builder = new StringBuilder();

                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        String line;
                        while((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                    }
                    catch (IOException e) {
                        Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.read_error, Snackbar.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                    return new JSONObject(builder.toString());
                }
                else {
                    Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.connect_error, Snackbar.LENGTH_LONG).show();
                }
            }
            catch (Exception e) {
                Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.connect_error, Snackbar.LENGTH_LONG).show();
            }
            finally {
                connection.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject weather) {
            convertJSONtoArrayList(weather);
            weatherArrayAdapter.notifyDataSetChanged();;
            weatherListView.smoothScrollToPosition(0);
        }

        private void convertJSONtoArrayList(JSONObject forecast) {
            weatherList.clear();

            try{
                JSONArray list = forecast.getJSONArray("list");

                for(int i = 0; i < list.length(); i++) {
                    JSONObject day = list.getJSONObject(i);

                    JSONObject temperatures = day.getJSONObject("temp");
                    JSONObject weather = day.getJSONArray("weather").getJSONObject(0);

                    weatherList.add(new Weather(
                            day.getLong("dt"),
                            temperatures.getDouble("min"),
                            temperatures.getDouble("max"),
                            day.getDouble("humidity"),
                            weather.getString("description"),
                            weather.getString("icon")));
                }
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}