package my.edu.devhacktesting3.API;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Weather {

    public interface WeatherCallback {
        void onWeatherDataReceived(String weatherMain, String weatherDesc);
        void onWeatherDataFailed();
    }

    public WeatherCallback callback;

    public Weather(WeatherCallback callback) {
        this.callback = callback;
    }

    public void getWeatherData(String apiUrl) {
        new WeatherApiTask().execute(apiUrl);
    }

    private class WeatherApiTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }
                reader.close();

                return response.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                // Parse the JSON response and extract weather info
                String weatherMain = "";
                String weatherDesc = "";

                try {
                    JSONObject responseObj = new JSONObject(result);
                    JSONArray weatherArray = responseObj.getJSONArray("weather");

                    if (weatherArray.length() > 0) {
                        JSONObject weatherObj = weatherArray.getJSONObject(0);
                        weatherMain = weatherObj.getString("main");
                        weatherDesc = weatherObj.getString("description");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Pass weather data back to the callback
                callback.onWeatherDataReceived(weatherMain, weatherDesc);
            } else {
                // Handle the case where the API request failed
                callback.onWeatherDataFailed();
            }
        }
    }
}
