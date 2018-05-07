package com.mupceet.sunshine.data;

import android.provider.BaseColumns;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class WeatherContractTest {

    @Test
    public void inner_class_exists() {
        Class[] innerClasses = WeatherContract.class.getDeclaredClasses();
        Assert.assertEquals("There should be 1 Inner class inside the contract class", 1, innerClasses.length);
    }

    @Test
    public void inner_class_type_correct() {
        Class[] innerClasses = WeatherContract.class.getDeclaredClasses();
        Assert.assertEquals("Cannot find inner class to complete unit test", 1, innerClasses.length);
        Class entryClass = innerClasses[0];
        Assert.assertTrue("Inner class should implement the BaseColumns interface", BaseColumns.class.isAssignableFrom(entryClass));
        Assert.assertTrue("Inner class should be final", Modifier.isFinal(entryClass.getModifiers()));
        Assert.assertTrue("Inner class should be static", Modifier.isStatic(entryClass.getModifiers()));
    }

    @Test
    public void inner_class_members_correct() {
        Class[] innerClasses = WeatherContract.class.getDeclaredClasses();
        Assert.assertEquals("Cannot find inner class to complete unit test", 1, innerClasses.length);
        Class entryClass = innerClasses[0];
        Field[] fields = entryClass.getDeclaredFields();
        Assert.assertEquals("There should be exactly 9 String members in the inner class", 9, fields.length);
        for (Field field :
                fields) {
            Assert.assertTrue("All members in the contract class should String", field.getType() == String.class);
            Assert.assertTrue("All members in the contract class should final", Modifier.isFinal(field.getModifiers()));
            Assert.assertTrue("All members in the contract class should static", Modifier.isStatic(field.getModifiers()));
        }
    }

}
