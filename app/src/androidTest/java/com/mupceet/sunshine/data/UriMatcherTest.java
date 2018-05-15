package com.mupceet.sunshine.data;


import android.content.UriMatcher;
import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RunWith(AndroidJUnit4.class)
public class UriMatcherTest {

    private static final Uri TEST_WEATHER_DIR = WeatherContract.WeatherEntry.CONTENT_URI;
    private static final Uri TEST_WEATHER_WITH_DATE_DIR = WeatherContract.WeatherEntry
            .buildWeatherUriWithDate(TestUtilities.DATE_NORMALIZED);

    private static final String weatherCodeVariableName = "CODE_WEATHER";
    private static final String weatherCodeWithDateVariableName = "CODE_WEATHER_WITH_DATE";
    private static int REFLECTED_WEATHER_CODE;
    private static int REFLECTED_WEATHER_WITH_DATE_CODE;

    private UriMatcher testUriMatcher;

    @Before
    public void init() {
        try {
            Method buildUriMatcher = WeatherProvider.class.getDeclaredMethod("buildUriMatcher");
            buildUriMatcher.setAccessible(true);
            testUriMatcher = (UriMatcher) buildUriMatcher.invoke(WeatherProvider.class);

            REFLECTED_WEATHER_CODE = TestUtilities.getStaticIntegerField(
                    WeatherProvider.class,
                    weatherCodeVariableName
            );

            REFLECTED_WEATHER_WITH_DATE_CODE = TestUtilities.getStaticIntegerField(
                    WeatherProvider.class,
                    weatherCodeWithDateVariableName
            );
        } catch (NoSuchMethodException e) {
            Assert.fail("It doesn't appear that you have created a method called " +
                    "buildUriMather " +
                    "in the WeatherProvider class.");
        } catch (IllegalAccessException | InvocationTargetException e) {
            Assert.fail(e.getMessage());
        } catch (NoSuchFieldException e) {
            Assert.fail(TestUtilities.studentReadableNoSuchField(e));
        }
    }


    @Test
    public void testUriMatcher() {
        String weatherUriDoesNotMatch = "Error: The CODE_WEATHER URI was matched incorrectly.";
        int actualWeatherCode = testUriMatcher.match(TEST_WEATHER_DIR);
        int expectWeatherCode = REFLECTED_WEATHER_CODE;

        Assert.assertEquals(weatherUriDoesNotMatch, expectWeatherCode, actualWeatherCode);

        String weatherWithDateUriDoesNotMatch = "Error: The CODE_WEATHER_WITH_DATE " +
                " URI was matched incorrectly.";
        int actualWeatherWithDateCode = testUriMatcher.match(TEST_WEATHER_WITH_DATE_DIR);
        int expectWeatherWithDateCode = REFLECTED_WEATHER_WITH_DATE_CODE;

        Assert.assertEquals(weatherWithDateUriDoesNotMatch,
                expectWeatherWithDateCode, actualWeatherWithDateCode);
    }

}
