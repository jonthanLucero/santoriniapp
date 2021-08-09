package com.example.santoriniapp.utils;

import android.annotation.SuppressLint;

import androidx.room.TypeConverter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @SuppressLint("SimpleDateFormat")
    public static Date StringToDateTime(String dateTimeString)
    {
        try
        {
            return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(dateTimeString);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
