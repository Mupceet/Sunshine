package com.mupceet.sunshine;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.ShareCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.mupceet.sunshine.data.WeatherContract;
import com.mupceet.sunshine.databinding.ActivityDetailBinding;
import com.mupceet.sunshine.utilities.SunshineDateUtils;
import com.mupceet.sunshine.utilities.SunshineWeatherUtils;

import java.util.Objects;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int INDEX_COLUMN_DATE = 0;
    public static final int INDEX_COLUMN_MAX_TEMP = 1;
    public static final int INDEX_COLUMN_MIN_TEMP = 2;
    public static final int INDEX_COLUMN_HUMIDITY = 3;
    public static final int INDEX_COLUMN_PRESSURE = 4;
    public static final int INDEX_COLUMN_WIND_SPEED = 5;
    public static final int INDEX_COLUMN_DEGREES = 6;
    public static final int INDEX_COLUMN_WEATHER_ID = 7;
    public static final String[] WEATHER_DETAIL_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID
    };
    private static final String TAG = DetailActivity.class.getSimpleName();
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    private static final int ID_DETAIL_LOADER = 2333;
    private String mForecast;
//    private TextView mTvWeatherDisplay;
    private Uri mUri;

//    private TextView mTvDate;
//    private TextView mTvDescription;
//    private TextView mTvMaxTemp;
//    private TextView mTvMinTemp;
//    private TextView mTvHumidity;
//    private TextView mTvPressure;
//    private TextView mTvWind;
    private ActivityDetailBinding mDetailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_detail);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

//        mTvWeatherDisplay = findViewById(R.id.tv_display_weather);
//        mTvDate = findViewById(R.id.tv_date);
//        mTvDescription = findViewById(R.id.tv_description);
//        mTvMaxTemp = findViewById(R.id.tv_max_temp);
//        mTvMinTemp = findViewById(R.id.tv_min_temp);
//        mTvHumidity = findViewById(R.id.tv_humidity);
//        mTvPressure = findViewById(R.id.tv_pressure);
//        mTvWind = findViewById(R.id.tv_wind);
        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);


//        Intent intentThatStartedThisActivity = getIntent();
//        if (intentThatStartedThisActivity != null) {
//            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
//                mForecast = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
////                mTvWeatherDisplay.setText(mForecast);
//            }
//        }
        mUri = getIntent().getData();
        if (null == mUri) {
            throw new NullPointerException("URI for DetailActivity cannot be null");
        }

        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.detail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setIntent(createShareForecastIntent());
        return super.onCreateOptionsMenu(menu);
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mForecast + FORECAST_SHARE_HASHTAG)
                .createChooserIntent();
        return shareIntent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            case ID_DETAIL_LOADER:
                return new CursorLoader(this,
                        mUri,
                        WEATHER_DETAIL_PROJECTION,
                        null,
                        null,
                        null);
                default:
                    throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            /* We have valid data, continue on to bind the data to the UI */
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            /* No data to display, simply return and do nothing */
            return;
        }

        /****************
         * Weather Date *
         ****************/
        long localDateMidnightGmt = data.getLong(INDEX_COLUMN_DATE);
        String dateText = SunshineDateUtils.getFriendlyDateString(this, localDateMidnightGmt, true);

//        mTvDate.setText(dateText);
        mDetailBinding.primaryInfo.tvDate.setText(dateText);

        /****************
         * Weather Icon *
         ****************/
        int weatherId = data.getInt(INDEX_COLUMN_WEATHER_ID);
        int weatherImageId = SunshineWeatherUtils.getLargeArtResourceIdForWeatherCondition(weatherId);

        mDetailBinding.primaryInfo.weatherIcon.setImageResource(weatherImageId);

//      COMPLETED (27) Display the weather description (using SunshineWeatherUtils)
        /***********************
         * Weather Description *
         ***********************/
        /* Read weather condition ID from the cursor (ID provided by Open Weather Map) */
        /* Use the weatherId to obtain the proper description */
        String description = SunshineWeatherUtils.getStringForWeatherCondition(this, weatherId);

        /* Set the text */
//        mTvDescription.setText(description);
        mDetailBinding.primaryInfo.weatherDescription.setText(description);

//      COMPLETED (28) Display the high temperature
        /**************************
         * High (max) temperature *
         **************************/
        /* Read high temperature from the cursor (in degrees celsius) */
        double highInCelsius = data.getDouble(INDEX_COLUMN_MAX_TEMP);
        /*
         * If the user's preference for weather is fahrenheit, formatTemperature will convert
         * the temperature. This method will also append either °C or °F to the temperature
         * String.
         */
        String highString = SunshineWeatherUtils.formatTemperature(this, highInCelsius);

        /* Set the text */
        mDetailBinding.primaryInfo.highTemperature.setText(highString);

//      COMPLETED (29) Display the low temperature
        /*************************
         * Low (min) temperature *
         *************************/
        /* Read low temperature from the cursor (in degrees celsius) */
        double lowInCelsius = data.getDouble(INDEX_COLUMN_MIN_TEMP);
        /*
         * If the user's preference for weather is fahrenheit, formatTemperature will convert
         * the temperature. This method will also append either °C or °F to the temperature
         * String.
         */
        String lowString = SunshineWeatherUtils.formatTemperature(this, lowInCelsius);

        /* Set the text */
        mDetailBinding.primaryInfo.lowTemperature.setText(lowString);

//      COMPLETED (30) Display the humidity
        /************
         * Humidity *
         ************/
        /* Read humidity from the cursor */
        float humidity = data.getFloat(INDEX_COLUMN_HUMIDITY);
        String humidityString = getString(R.string.format_humidity, humidity);

        /* Set the text */
        mDetailBinding.extraDetails.humidity.setText(humidityString);

//      COMPLETED (31) Display the wind speed and direction
        /****************************
         * Wind speed and direction *
         ****************************/
        /* Read wind speed (in MPH) and direction (in compass degrees) from the cursor  */
        float windSpeed = data.getFloat(INDEX_COLUMN_WIND_SPEED);
        float windDirection = data.getFloat(INDEX_COLUMN_DEGREES);
        String windString = SunshineWeatherUtils.getFormattedWind(this, windSpeed, windDirection);

        /* Set the text */
        mDetailBinding.extraDetails.wind.setText(windString);

//      COMPLETED (32) Display the pressure
        /************
         * Pressure *
         ************/
        /* Read pressure from the cursor */
        float pressure = data.getFloat(INDEX_COLUMN_PRESSURE);

        /*
         * Format the pressure text using string resources. The reason we directly access
         * resources using getString rather than using a method from SunshineWeatherUtils as
         * we have for other data displayed in this Activity is because there is no
         * additional logic that needs to be considered in order to properly display the
         * pressure.
         */
        String pressureString = getString(R.string.format_pressure, pressure);

        /* Set the text */
        mDetailBinding.extraDetails.pressure.setText(pressureString);

//      COMPLETED (33) Store a forecast summary in mForecastSummary
        /* Store the forecast summary String in our forecast summary field to share later */
        mForecast = String.format("%s - %s - %s/%s",
                dateText, description, highString, lowString);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
