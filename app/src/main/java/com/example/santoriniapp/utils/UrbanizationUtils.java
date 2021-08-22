package com.example.santoriniapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;

import com.google.gson.Gson;
import java.lang.reflect.Type;
import java.util.Date;

public class UrbanizationUtils
{
    public static Object fromJson(String jsonString, Type type) {
        return new Gson().fromJson(jsonString, type);
    }

    public static AlertDialog showMessage(Context context, String message)
    {
        return new AlertDialog.Builder(context)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes,null).show();
    }

    public static final String PAYMENT_SENT_STATUS     = "S";

    public static int getColor(String sColor){
        int color;
        try{
            color = Color.parseColor(sColor);
        }catch(Exception e){
            color = Color.BLACK;
        }
        return color;
    }

    public static String getTodayMonthRequestCode()
    {
        Date date = DateFunctions.today();
        int month = date.getMonth();

        switch (month)
        {
            case 0:
                return "01";

            case 1:
                return "02";

            case 2:
                return "03";

            case 3:
                return "04";

            case 4:
                return "05";

            case 5:
                return "06";

            case 6:
                return "07";

            case 7:
                return "08";

            case 8:
                return "09";

            case 9:
                return "10";

            case 10:
                return "11";

            case 11:
                return "12";
        }
        return "01";
    }

    public static String getMonthName(String monthCode)
    {
        switch (monthCode)
        {
            case "01":
                return "Enero";

            case "02":
                return "Febrero";

            case "03":
                return "Marzo";

            case "04":
                return "Abril";

            case "05":
                return "Mayo";

            case "06":
                return "Junio";

            case "07":
                return "Julio";

            case "08":
                return "Agosto";

            case "09":
                return "Septiembre";

            case "10":
                return "Octubre";

            case "11":
                return "Noviembre";

            case "12":
                return "Diciembre";
        }
        return "Enero";
    }
}
