package com.example.santoriniapp.utils;

import java.util.Calendar;
import java.util.Date;

public class DateFunctions
{
    public final static String TAG = DateFunctions.class.getSimpleName();

    /*
     * Date now() //TESTED
     * Returns present date
     */
    public static Date now(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTime();
    }

    /*
     * Date today() //TESTED
     * Returns present date
     */
    public static Date today(){
        Date d = setDate(getYear(now()), getMonth(now()), getDay(now()));
        return d;
    }

    /*
     * int _getDateParameter(Date, int)
     * Returns int parameters of a given date object
     * int parameters = Calendar CONSTANTS
     */
    private static int _getDateParameter(Date date, int parameter){
        int result = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        result = calendar.get(parameter);
        return result;
    }

    /*
     * int getYear(Date) //TESTED
     * Returns year of a given date object
     */
    public static int getYear(Date d){
        int year = 0;
        year = _getDateParameter(d, Calendar.YEAR);
        return year;
    }

    /*
     * int getMonth(Date) //TESTED
     * Returns month of a given date object
     */
    public static int getMonth(Date d){
        int month = 0;
        month = _getDateParameter(d, Calendar.MONTH) + 1;
        return month;
    }

    /*
     * int getDay(Date) //TESTED
     * Returns date of a given date object
     */
    public static int getDay(Date d){
        int date = 0;
        date = _getDateParameter(d, Calendar.DATE);
        return date;
    }

    /*
     * int getHour(Date) //TESTED
     * Returns hour of a given date object
     */
    public static int getHour(Date d){
        int hour = 0;
        hour = _getDateParameter(d, Calendar.HOUR_OF_DAY);
        return hour;
    }

    /*
     * int getMinute(Date) //TESTED
     * Returns minute of a given date object
     */
    public static int getMinute(Date d){
        int minute = 0;
        minute = _getDateParameter(d, Calendar.MINUTE);
        return minute;
    }

    /*
     * int getSecond(Date) //TESTED
     * Returns second of a given date object
     */
    public static int getSecond(Date d){
        int second = 0;
        second = _getDateParameter(d, Calendar.SECOND);
        return second;
    }

    /*
     * String getYearString(Date)
     * Returns year string of a given date object
     */
    public static String getYearString(Date d){
        int year = getYear(d);
        String yearString = "";
        yearString = year + "";
        return yearString;
    }

    /*
     * String getMonthString(Date)
     * Returns month string of a given date object
     */
    public static String getMonthString(Date d){
        int month = getMonth(d);
        String monthString = "";
        monthString = (month < 10) ? "0" + month : "" + month ;
        return monthString;
    }

    /*
     * String getDateString(Date)
     * Returns date string of a given date object
     */
    public static String getDateString(Date d){
        int date = getDay(d);
        String dateString = "";
        dateString = (date < 10) ? "0" + date : "" + date ;
        return dateString;
    }

    /*
     * String getHourString(Date)
     * Returns hour string of a given date object
     */
    public static String getHourString(Date d){
        int hour = getHour(d);
        String hourString = "";
        hourString = (hour < 10) ? "0" + hour : "" + hour ;
        return hourString;
    }

    /*
     * String getMinuteString(Date)
     * Returns minute string of a given date object
     */
    public static String getMinuteString(Date d){
        int minute = getMinute(d);
        String minuteString = "";
        minuteString = (minute < 10) ? "0" + minute : "" + minute ;
        return minuteString;
    }

    /*
     * String getSecondString(Date)
     * Returns second string of a given date object
     */
    public static String getSecondString(Date d){
        int second = getSecond(d);
        String secondString = "";
        secondString = (second < 10) ? "0" + second : "" + second ;
        return secondString;
    }

    /*
     * String getYYYYMMDDString
     * Returns date string in format YYYY-MM-DD of a given date object
     * TESTED
     */
    public static String getYYYYMMDDString(Date d){
        String result = "";
        result = getYearString(d) + "-" + getMonthString(d) + "-" + getDateString(d);
        return result;
    }

    /*
     * String getYYYYMMDDHHMMSSString
     * Returns date string in format YYYY-MM-DD of a given date object
     * TESTED
     */
    public static String getYYYYMMDDHHMMSSString(Date d){
        String result = "";
        result += getYearString(d) + "-" + getMonthString(d) + "-" + getDateString(d) + " ";
        result += getHourString(d) + ":" + getMinuteString(d) + ":" + getSecondString(d);
        return result;
    }

    //SET DATE METHODS *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
    /*
     * Date setDate(year, month, date, hour, minute, second)
     * Returns date object with values specified
     * TESTED
     */
    public static Date setDateTime(int year, int month, int date, int hour, int minute, int second){
        Date result = new Date(0);
        result = set(result, Calendar.MILLISECOND, 0);
        result = set(result, Calendar.SECOND, second);
        result = set(result, Calendar.MINUTE, minute);
        result = set(result, Calendar.HOUR_OF_DAY, hour);

        result = set(result, Calendar.YEAR, year);
        result = set(result, Calendar.DATE, date);
        result = set(result, Calendar.MONTH, month);

        return result;
    }

    /*
     * Date setDate(year, month, date)
     * Returns date object with values specified
     * TESTED
     */
    public static Date setDate(int year, int month, int date){
        Date result = new Date(0);
        result = set(result, Calendar.MILLISECOND, 0);
        result = set(result, Calendar.SECOND, 0);
        result = set(result, Calendar.MINUTE, 0);
        result = set(result, Calendar.HOUR_OF_DAY, 0);

        result = set(result, Calendar.YEAR, year);
        result = set(result, Calendar.DATE, date);
        result = set(result, Calendar.MONTH, month);

        return result;
    }

    /*
     * Date set(Date, Calendar CONSTANT, int)
     * Sets the corresponding value of Calendar CONSTANT of a given date object
     * Returns Date Object
     */
    private static Date set(Date d, int calendarField, int value){
        Date result;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        if( Calendar.MONTH == calendarField )
            calendar.set(calendarField, value - 1);
        else
            calendar.set(calendarField, value);
        result = calendar.getTime();
        return result;
    }

    /**
     * Date stringToDateTime(String date) format: YYYY-MM-DD HH:MM:SS
     * Returns date object of a given string representing a datetime
     * TESTED
     */
    public static Date stringToDateTime(String d){
        Date result = new Date(0);
        int year = NumericFunctions.toInteger( StringFunctions.substr(d, 0, 4) );
        int month = NumericFunctions.toInteger( StringFunctions.substr(d, 5, 2) );
        int day = NumericFunctions.toInteger( StringFunctions.substr(d, 8, 2) );
        int hour = NumericFunctions.toInteger( StringFunctions.substr(d, 11, 2) );
        int minute = NumericFunctions.toInteger( StringFunctions.substr(d, 14, 2) );
        int second = NumericFunctions.toInteger( StringFunctions.substr(d, 17, 2) );
        result = setDateTime(year, month, day, hour, minute, second);
        return result;
    }

    /**
     * Date stringToDate(String date) format: YYYY-MM-DD
     * Returns date object of a given string representing a date
     * TESTED
     */
    public static Date stringToDate(String d){
        Date result = new Date(0);
        int year = NumericFunctions.toInteger( StringFunctions.substr(d, 0, 4) );
        int month = NumericFunctions.toInteger( StringFunctions.substr(d, 5, 2) );
        int day = NumericFunctions.toInteger( StringFunctions.substr(d, 8, 2) );
        int hour = 0;
        int minute = 0;
        int second = 0;
        result = setDateTime(year, month, day, hour, minute, second);
        return result;
    }

    /**
     * datediff(Date fromDate, Date toDate)
     * Returns difference in seconds between two dates
     * TESTED
     */
    public static long datediff(Date toDate, Date fromDate){
        long seconds_elapsed = 0;
        seconds_elapsed = (toDate.getTime() - fromDate.getTime()) / 1000;

        return seconds_elapsed;
    }

    /**
     * endOfMonth(Date)
     * Returns date object with last date in month
     * TESTED
     */
    public static Date endOfMonth(Date date){
        int year = getYear(date);
        int month = getMonth(date);
        int lastDayOfMonth = 1;

        switch(month - 1){
            case Calendar.JANUARY:
                lastDayOfMonth = 31;
                break;
            case Calendar.FEBRUARY:
                lastDayOfMonth = ((year%4) == 0) ? 29 : 28;
                break;
            case Calendar.MARCH:
                lastDayOfMonth = 31;
                break;
            case Calendar.APRIL:
                lastDayOfMonth = 30;
                break;
            case Calendar.MAY:
                lastDayOfMonth = 31;
                break;
            case Calendar.JUNE:
                lastDayOfMonth = 30;
                break;
            case Calendar.JULY:
                lastDayOfMonth = 31;
                break;
            case Calendar.AUGUST:
                lastDayOfMonth = 31;
                break;
            case Calendar.SEPTEMBER:
                lastDayOfMonth = 30;
                break;
            case Calendar.OCTOBER:
                lastDayOfMonth = 31;
                break;
            case Calendar.NOVEMBER:
                lastDayOfMonth = 30;
                break;
            case Calendar.DECEMBER:
                lastDayOfMonth = 31;
                break;

        }
        return setDate(year, month, lastDayOfMonth);
    }

    /**
     * addSeconds(Date, long)
     * Add seconds to a given date
     * TESTED
     */
    public static Date addSeconds(Date value, long seconds){
        return new Date(value.getTime() + (seconds * 1000));
    }

    /**
     * addDays(Date, long)
     * Add days to a given date
     * TESTED
     */
    public static Date addDays(Date value, long days){
        return new Date(value.getTime() + (days * 24 * 3600 * 1000));
    }

    /**
     * dayOfWeek(Date)
     * Returns int day of week
     * TESTED
     */
    public static int dayOfWeek(Date d){
        return _getDateParameter(d, Calendar.DAY_OF_WEEK);
    }

    /**
     * dayOfWeekName(Date)
     * Returns name of day of week
     * TESTED
     */
    public static String dayOfWeekName(Date d){
        if( _getDateParameter(d, Calendar.DAY_OF_WEEK) == Calendar.SUNDAY )
            return "Domingo";
        if( _getDateParameter(d, Calendar.DAY_OF_WEEK) == Calendar.MONDAY )
            return "Lunes";
        if( _getDateParameter(d, Calendar.DAY_OF_WEEK) == Calendar.TUESDAY )
            return "Martes";
        if( _getDateParameter(d, Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY )
            return "Miércoles";
        if( _getDateParameter(d, Calendar.DAY_OF_WEEK) == Calendar.THURSDAY )
            return "Jueves";
        if( _getDateParameter(d, Calendar.DAY_OF_WEEK) == Calendar.FRIDAY )
            return "Viernes";
        if( _getDateParameter(d, Calendar.DAY_OF_WEEK) == Calendar.SATURDAY )
            return "Sábado";
        return "";
    }

    /**
     * monthName(Date)
     * Returns name of month
     * TESTED
     */
    public static String monthName(Date d){
        if( _getDateParameter(d, Calendar.MONTH) == Calendar.JANUARY )
            return "Enero";
        if( _getDateParameter(d, Calendar.MONTH) == Calendar.FEBRUARY )
            return "Febrero";
        if( _getDateParameter(d, Calendar.MONTH) == Calendar.MARCH )
            return "Marzo";
        if( _getDateParameter(d, Calendar.MONTH) == Calendar.APRIL )
            return "Abril";
        if( _getDateParameter(d, Calendar.MONTH) == Calendar.MAY )
            return "Mayo";
        if( _getDateParameter(d, Calendar.MONTH) == Calendar.JUNE )
            return "Junio";
        if( _getDateParameter(d, Calendar.MONTH) == Calendar.JULY )
            return "Julio";
        if( _getDateParameter(d, Calendar.MONTH) == Calendar.AUGUST )
            return "Agosto";
        if( _getDateParameter(d, Calendar.MONTH) == Calendar.SEPTEMBER )
            return "Septiembre";
        if( _getDateParameter(d, Calendar.MONTH) == Calendar.OCTOBER )
            return "Octubre";
        if( _getDateParameter(d, Calendar.MONTH) == Calendar.NOVEMBER )
            return "Noviembre";
        if( _getDateParameter(d, Calendar.MONTH) == Calendar.DECEMBER )
            return "Diciembre";
        return "";
    }

    /**
     * emptyDate()
     * Return an empty Date
     * TESTED
     */
    public static Date emptyDate(){
        return new Date(0);
    }

    /**
     * ToDate()
     * Return new Date with milliSeconds provided
     * TESTED
     */
    public static Date toDate(long milliSeconds){
        return new Date(milliSeconds);
    }

    public static String getDDMMMYYYYDateString(Date d){
        String monthName = monthName(d);
        if (monthName.length()>=3) monthName = monthName.substring(0,3);

        return getDay(d) + "-" + monthName + "-" + getYearString(d);
    }

    public static String getDDMMMYYYYHHMMSSDateString(Date d){
        String monthName = monthName(d);
        if (monthName.length()>=3) monthName = monthName.substring(0,3);

        return getDay(d) + "-" + monthName + "-" + getYearString(d) + " " + getHourString(d)+":"+getMinuteString(d)+":"+getSecondString(d);
    }


    public static String getDayNameWithDayNumberOfMonthName(Date d){
        String dayName      = dayOfWeekName(d);
        String monthName    = monthName(d);
        return dayName + " " + DateFunctions.getDay(d) + " de " + monthName;
    }

    public static Date addMonths(Date date, int monthsToAdd){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH,monthsToAdd);
        return calendar.getTime();
    }

    public static Date firstOfMonth(Date d){
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date firstDayOfWeek(Date d){
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK,c.getFirstDayOfWeek());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date lastDayOfWeek(Date d){
        Date firstDayOfWeek = firstDayOfWeek(d);
        return addDays(firstDayOfWeek,6);
    }
}
