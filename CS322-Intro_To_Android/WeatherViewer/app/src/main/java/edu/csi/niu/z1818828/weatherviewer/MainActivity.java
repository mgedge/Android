// MainActivity.java
// Z1818828 - Matthew Gedge
// Maintains the interaction between the UI and API calls

package edu.csi.niu.z1818828.weatherviewer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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
    private Coordinate coordinatesObj = new Coordinate();   //Create a coordinate object to hold coordinates
    private WeatherArrayAdapter weatherArrayAdapter;
    private ListView weatherListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        weatherListView = (ListView) findViewById(R.id.weatherListView);
        weatherArrayAdapter = new WeatherArrayAdapter(this, weatherList);
        weatherListView.setAdapter(weatherArrayAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText locationEditText = (EditText) findViewById(R.id.locationEditText);

                //Begin by preparing an API call given the location
                URL coordinates = coordinatesURL(locationEditText.getText().toString());
                if (coordinates != null) {
                    dismissKeyboard(locationEditText);
                    GetLocationTask getLocationTask = new GetLocationTask();
                    getLocationTask.execute(coordinates);
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

    private URL createURL(String lat, String lng) {
        String apiKey = getString(R.string.api_key);
        String baseUrl = getString(R.string.web_service_url);

        try {
            String urlString = baseUrl + "lat=" + lat + "&lon=" + lng + "&exclude=current,minutely,hourly,alerts&units=imperial&APPID=" + apiKey;
            return new URL(urlString);
        } catch (Exception e) {
            System.out.println("Invalid Weather url build");
            e.printStackTrace();
            return null;
        }
    }

    private URL coordinatesURL(String city) {
        // Create the variables
        String apiKey = getString(R.string.geocodio_api);
        String url = getString(R.string.geocodio_url);

        try {
            // Then create the url given the location
            String urlString = url + URLEncoder.encode(city, "UTF-8") + "&api_key=" + apiKey;
            return new URL(urlString);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private class GetWeatherTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... params) {
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) params[0].openConnection();
                int response = connection.getResponseCode();

                if (response == HttpURLConnection.HTTP_OK) {
                    StringBuilder builder = new StringBuilder();

                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                    } catch (IOException e) {
                        Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.read_error, Snackbar.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                    return new JSONObject(builder.toString());
                } else {
                    Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.connect_error, Snackbar.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.connect_error, Snackbar.LENGTH_LONG).show();
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject weather) {
            convertJSONtoArrayList(weather);
            weatherArrayAdapter.notifyDataSetChanged();
            weatherListView.smoothScrollToPosition(0);
        }

        private void convertJSONtoArrayList(JSONObject forecast) {
            weatherList.clear();

            try {
                JSONArray list = forecast.getJSONArray("daily");

                for (int i = 0; i < list.length(); i++) {
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class GetLocationTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... params) {
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) params[0].openConnection();
                int response = connection.getResponseCode();

                if (response == HttpURLConnection.HTTP_OK) {
                    StringBuilder builder = new StringBuilder();

                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                    } catch (IOException e) {
                        Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.read_error, Snackbar.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                    return new JSONObject(builder.toString());
                } else {
                    Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.connect_error, Snackbar.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.connect_error, Snackbar.LENGTH_LONG).show();
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject location) {
            //Get the coordinates after reading the API results, then call WeatherMap API
            convertJSONtoCoordinates(location);

            String lat, lng;
            lat = coordinatesObj.getLat();
            lng = coordinatesObj.getLng();

            URL url = createURL(lat, lng);
            if (url != null) {
                GetWeatherTask getLocalWeatherTask = new GetWeatherTask();
                getLocalWeatherTask.execute(url);
            } else {
                Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.invalid_url, Snackbar.LENGTH_LONG).show();
            }
        }

        private void convertJSONtoCoordinates(JSONObject location) {
            //Retrieve the coordinates from the JSON object
            try {
                JSONArray list = location.getJSONArray("results");
                JSONObject day = list.getJSONObject(0);
                JSONObject coordinates = day.getJSONObject("location");

                coordinatesObj.setLat(coordinates.getDouble("lat"));
                coordinatesObj.setLng(coordinates.getDouble("lng"));
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("Could not convert coordinates!");
                return;
            }
        }
    }
}