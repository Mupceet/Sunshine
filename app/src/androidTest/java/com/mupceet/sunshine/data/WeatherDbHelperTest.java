package com.mupceet.sunshine.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

@RunWith(AndroidJUnit4.class)
public class WeatherDbHelperTest {

    private final Context mContext = InstrumentationRegistry.getTargetContext();

    private final Class mDbHelperClass = WeatherDbHelper.class;

    @Before
    public void setUp() {
        deleteTheDatabase();
    }

    @Test
    public void create_database_test() throws Exception {
        SQLiteOpenHelper dbHelper =
                (SQLiteOpenHelper) mDbHelperClass.getConstructor(Context.class).newInstance(mContext);

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        String databaseIsNotOpen = "The database should be open and isn't";
        Assert.assertEquals(databaseIsNotOpen, true, database.isOpen());

        Cursor tableNameCursor = database.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table'" +
                        " AND name='" + WeatherContract.WeatherEntry.TABLE_NAME + "'",
                null
        );

        String errorInCreatingDatabase =
                "Error: This means that the database has not been created correctly";
        Assert.assertTrue(errorInCreatingDatabase, tableNameCursor.moveToFirst());

        Assert.assertEquals("Error: Your database was created without the expected tables",
                WeatherContract.WeatherEntry.TABLE_NAME, tableNameCursor.getString(0));

        tableNameCursor.close();
    }

    void deleteTheDatabase() {
        try {
            Field f = mDbHelperClass.getDeclaredField("DATABASE_NAME");
            f.setAccessible(true);
            mContext.deleteDatabase((String) f.get(null));
        } catch (NoSuchFieldException e) {
            Assert.fail("Make sure you have a member called DATABASE_NAME in the WeatherDbHelper");
        } catch (IllegalAccessException e) {
            Assert.fail(e.getMessage());
        }
    }
}
