package com.example.santoriniapp.utils;

import android.util.Log;

import java.math.BigDecimal;
import java.util.Date;

public class NumericFunctions {

    public static final String TAG = NumericFunctions.class.getSimpleName();

    public static boolean toBoolean(String value){
        try{
            return Boolean.parseBoolean(value);
        }catch(Exception e){
            return false;
        }
    }

    //Byte Functions
    //String
    public static byte toByte(String value){
        try{
            return Byte.parseByte(value);
        }catch(Exception e){
            return 0;
        }
    }
    //Integer
    public static byte toByte(int value){
        try{
            return new Double(value).byteValue();
        }catch(Exception e){
            return 0;
        }
    }

    //Short Functions
    //String
    public static short toShort(String value){
        try{
            return Short.parseShort(value);
        }catch(Exception e){
            return 0;
        }
    }
    //Integer
    public static short toShort(int value){
        try{
            return new Double(value).shortValue();
        }catch(Exception e){
            return 0;
        }
    }
    //Float
    public static short toShort(float value){
        try{
            return new Double(value).shortValue();
        }catch(Exception e){
            return 0;
        }
    }

    //Int Functions
    //String
    public static int toInteger(String value){
        try{
            return Integer.parseInt(value);
        }catch(Exception e){
            return 0;
        }
    }
    //Byte
    public static int toInteger(byte value){
        try{
            return new Integer(value).intValue();
        }catch(Exception e){
            return 0;
        }
    }
    //Double
    public static int toInteger(double value){
        try{
            return new Double(value).intValue();
        }catch(Exception e){
            return 0;
        }
    }

    //LONG FUNCTIONS
    //STRING TO LONG
    public static long toLong(String value){
        try{
            return Long.parseLong(value);
        }catch(Exception e){
            return 0;
        }
    }
    //INT TO LONG
    public static long toLong(int value){
        try{
            return new Long(value).longValue();
        }catch(Exception e){
            return 0;
        }
    }
    //DATE TO LONG
    public static long toLong(Date value){
        try{
            return value.getTime();
        }catch(Exception e){
            return 0;
        }
    }

    //TO FLOAT FUNCTIONS
    public static float toFloat(String value){
        try{
            return Float.parseFloat(value);
        }catch(Exception e){
            return 0;
        }
    }
    public static float toFloat(double value){
        try{
            return new Double(value).floatValue();
        }catch(Exception e){
            return 0;
        }
    }

    //TO DOUBLE FUNCTIONS
    public static double toDouble(String value){
        try{
            return Double.parseDouble(value);
        }catch(Exception e){
            return 0;
        }
    }

    //ROUND FUNCTIONS
    public static float round(float value, int decimals){
        BigDecimal aux = new BigDecimal(value);
        BigDecimal rounded = aux.setScale(decimals, BigDecimal.ROUND_HALF_UP);
        return rounded.floatValue();
    }

    public static double round(double value, int decimals){
        BigDecimal aux = new BigDecimal(value);
        BigDecimal rounded = aux.setScale(decimals, BigDecimal.ROUND_HALF_UP);
        return rounded.doubleValue();
    }

    public static double mod(double dividend, double divisor) {
        BigDecimal a = new BigDecimal(dividend + "");
        BigDecimal b = new BigDecimal(divisor + "");
        BigDecimal[] c = a.divideAndRemainder(b);

        Log.d(TAG, "Dividend: " + a);
        Log.d(TAG, "Divisor: " + b);

        Log.d(TAG, "D: " + c[0]);
        Log.d(TAG, "R: " + c[1]);

        return c[1].doubleValue();
    }
}