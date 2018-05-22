package com.mupceet.sunshine.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.mupceet.sunshine.data.WeatherContract;
import com.mupceet.sunshine.utilities.NetworkUtils;
import com.mupceet.sunshine.utilities.OpenWeatherJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class SunshineSyncTask {

    public static void syncWeather(Context context) {
        URL weatherRequestUrl = NetworkUtils.getUrl(context);
        try {
            String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
            ContentValues[] weatherValues = OpenWeatherJsonUtils
                    .getWeatherContentValuesFromJson(context, jsonWeatherResponse);

            if (weatherValues != null && weatherValues.length != 0) {
                ContentResolver contentResolver = context.getContentResolver();
                contentResolver.delete(
                        WeatherContract.WeatherEntry.CONTENT_URI,
                        null,
                        null
                );

                contentResolver.bulkInsert(
                        WeatherContract.WeatherEntry.CONTENT_URI,
                        weatherValues
                );
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
