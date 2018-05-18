package com.mupceet.sunshine;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mupceet.sunshine.data.SunshinePreferences;
import com.mupceet.sunshine.data.WeatherContract;
import com.mupceet.sunshine.utilities.FakeDataUtils;

public class MainActivity extends AppCompatActivity
        implements ForecastAdapter.ForecastAdapterOnclickHandler,
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final String[] MAIN_FORECAST_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID
    };

    // 这里的顺序与上面查询结果顺序是一样的……
    public static final int INDEX_COLUMN_DATE = 0;
    public static final int INDEX_COLUMN_MAX_TEMP = 1;
    public static final int INDEX_COLUMN_MIN_TEMP = 2;
    public static final int INDEX_COLUMN_WEATHER_ID = 3;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int FORECAST_LOADER_ID = 22;
    private static boolean PREFERENCES_HAVE_BEEN_UPDATE = false;
    //    private TextView mTvWeatherData;
    private RecyclerView mRecyclerView;
    private ForecastAdapter mForecastAdapter;
    private TextView mTvErrorMessage;
    private int mPosition = RecyclerView.NO_POSITION;
    private ProgressBar mProgressBar;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        FakeDataUtils.insertFakeData(this);

//        mTvWeatherData = findViewById(R.id.tv_weather_data);
        mRecyclerView = findViewById(R.id.recyclerview_forecast);

        mTvErrorMessage = findViewById(R.id.tv_error_message);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mForecastAdapter = new ForecastAdapter(this, this);
        mRecyclerView.setAdapter(mForecastAdapter);

        mProgressBar = findViewById(R.id.progress_bar);
//        String[] dummyWeatherData = {
//                "Today, May 17 - Clear - 17°C / 15°C",
//                "Tomorrow - Cloudy - 19°C / 15°C",
//                "Thursday - Rainy- 30°C / 11°C",
//                "Friday - Thunderstorms - 21°C / 9°C",
//                "Saturday - Thunderstorms - 16°C / 7°C",
//                "Sunday - Rainy - 16°C / 8°C",
//                "Monday - Partly Cloudy - 15°C / 10°C",
//                "Tue, May 24 - Meatballs - 16°C / 18°C",
//                "Wed, May 25 - Cloudy - 19°C / 15°C",
//                "Thu, May 26 - Stormy - 30°C / 11°C",
//                "Fri, May 27 - Hurricane - 21°C / 9°C",
//                "Sat, May 28 - Meteors - 16°C / 7°C",
//                "Sun, May 29 - Apocalypse - 16°C / 8°C",
//                "Mon, May 30 - Post Apocalypse - 15°C / 10°C"
//        };
//
//        for (String dummyWeatherDay :
//                dummyWeatherData) {
//            mTvWeatherData.append(dummyWeatherDay + "\n\n\n");
//        }

//        loadWeatherData();
        showLoading();
        getSupportLoaderManager().initLoader(FORECAST_LOADER_ID, null, this);

//        PreferenceManager.getDefaultSharedPreferences(this)
//                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (PREFERENCES_HAVE_BEEN_UPDATE) {
//            mForecastAdapter.setWeatherData(null);
            getSupportLoaderManager().restartLoader(FORECAST_LOADER_ID, null, this);
            PREFERENCES_HAVE_BEEN_UPDATE = false;
        }
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        PreferenceManager.getDefaultSharedPreferences(this)
//                .unregisterOnSharedPreferenceChangeListener(this);
//    }

    //    private void loadWeatherData() {
//        showWeatherDataView();
//        String location = SunshinePreferences.getPreferredWeatherLocation(MainActivity.this);
//        new FetchWeatherTask().execute(location);
//    }

    private void showWeatherDataView() {
        mTvErrorMessage.setVisibility(View.INVISIBLE);
//        mTvWeatherData.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }


//    private void showErrorMessage() {
//        mTvErrorMessage.setVisibility(View.VISIBLE);
////        mTvWeatherData.setVisibility(View.INVISIBLE);
//        mRecyclerView.setVisibility(View.INVISIBLE);
//    }

    @Override
    public void onForecastAdapterItemClick(long date) {
//        if (mToast != null) {
//            mToast.cancel();
//        }
//        mToast = Toast.makeText(this, weatherForDay, Toast.LENGTH_SHORT);
//        mToast.show();
        Intent intent = new Intent(this, DetailActivity.class);
//        intent.putExtra(Intent.EXTRA_TEXT, weatherForDay);
        Uri uriForDate = WeatherContract.WeatherEntry.buildWeatherUriWithDate(date);
        intent.setData(uriForDate);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.forecast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
//            mTvWeatherData.setText("");
//            mForecastAdapter.setWeatherData(null);
//            loadWeatherData();
            getSupportLoaderManager().restartLoader(FORECAST_LOADER_ID, null, this);
            return true;
        } else if (id == R.id.action_map) {
            openLocationInMap();
            return true;
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openLocationInMap() {
        String addressString = SunshinePreferences.getPreferredWeatherLocation(this);
        Uri geoLocation = Uri.parse("geo:0,0?q=" + addressString);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d(TAG, "Couldn't call " + geoLocation.toString()
                    + ", no receiving apps installed!");
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        switch (id) {
            case FORECAST_LOADER_ID:
                Uri forecastQueryUri = WeatherContract.WeatherEntry.CONTENT_URI;
                String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
                String selection = WeatherContract.WeatherEntry.getSqlSelectForTodayOnwards();
                return new CursorLoader(this,
                        forecastQueryUri,
                        MAIN_FORECAST_PROJECTION,
                        selection,
                        null,
                        sortOrder);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }

//        return new AsyncTaskLoader<String[]>(this) {
//
//            String[] mWeatherData = null;
//
//            @Override
//            protected void onStartLoading() {
//                if (mWeatherData != null) {
//                    deliverResult(mWeatherData);
//                } else {
//                    mProgressBar.setVisibility(View.VISIBLE);
//                    forceLoad();
//                }
//            }
//
//            @Nullable
//            @Override
//            public String[] loadInBackground() {
//                String location = SunshinePreferences.getPreferredWeatherLocation(MainActivity.this);
//                URL weatherRequestUrl = NetworkUtils.getUrl(location);
//
//                try {
//                    String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
//                    String[] simpleJsonWeatherData = OpenWeatherJsonUtils
//                            .getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);
//                    return simpleJsonWeatherData;
//                } catch (IOException | JSONException e) {
//                    e.printStackTrace();
//                    return null;
//                }
//            }
//
//            @Override
//            public void deliverResult(@Nullable String[] data) {
//                mWeatherData = data;
//                super.deliverResult(data);
//            }
//        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
//        mProgressBar.setVisibility(View.INVISIBLE);
//        mForecastAdapter.setWeatherData(data);
//        if (data != null) {
//            showWeatherDataView();
////                for (String weatherString :
////                        weatherData) {
////                    mTvWeatherData.append(weatherString + "\n\n\n");
////                }
//        } else {
//            showErrorMessage();
//        }
        mForecastAdapter.swapCursor(data);

        if (mPosition == RecyclerView.NO_POSITION) {
            mPosition = 0;
        }
        mRecyclerView.smoothScrollToPosition(mPosition);

        if (data.getCount() != 0) {
            showWeatherDataView();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mForecastAdapter.swapCursor(null);
    }

//    @Override
//    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        PREFERENCES_HAVE_BEEN_UPDATE = true;
//    }

    private void showLoading() {
        mTvErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

//    private class FetchWeatherTask extends AsyncTask<String, Void, String[]> {
//
//        @Override
//        protected void onPreExecute() {
//            mProgressBar.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected String[] doInBackground(String... params) {
//
//            if (params.length == 0) {
//                return null;
//            }
//
//            String location = params[0];
//            URL weatherRequestUrl = NetworkUtils.buildUrl(location);
//
//            try {
//                String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
//                String[] simpleJsonWeatherData = OpenWeatherJsonUtils
//                        .getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);
//                return simpleJsonWeatherData;
//            } catch (IOException e) {
//                e.printStackTrace();
//                return null;
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return new String[0];
//        }
//
//        @Override
//        protected void onPostExecute(String[] weatherData) {
//            mProgressBar.setVisibility(View.INVISIBLE);
//            if (weatherData != null) {
//                showWeatherDataView();
////                for (String weatherString :
////                        weatherData) {
////                    mTvWeatherData.append(weatherString + "\n\n\n");
////                }
//                mForecastAdapter.setWeatherData(weatherData);
//            } else {
//                showErrorMessage();
//            }
//        }
//    }
}
