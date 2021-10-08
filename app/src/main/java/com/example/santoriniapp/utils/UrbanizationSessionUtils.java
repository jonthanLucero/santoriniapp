package com.example.santoriniapp.utils;

import android.content.SharedPreferences;
import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

public class UrbanizationSessionUtils
{
    private final static String LOGGED_IN = "LOGGED_IN";

    private static final String PASSWORD                     = "50c9b8b2tls="; // Encripted name for the field Password.
    private static final String SANTORINI_LOGIN_ENCRIPT_KEY = "SANTORINI_LOGIN_ENCRIPT_KEY";
    private final static String LOGGED_OFFLINE               = "LOGGED_OFFLINE";
    private final static String LOGGED_USER               = "LOGGED_USER";
    private final static String LOGGED_LOGIN               = "LOGGED_LOGIN";
    private final static String LOGGED_PASSWORD               = "LOGGED_PASSWORD";

    private static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setLoggedIn(Context context, boolean isLoggedIn){
        if(context == null) return;

        Log.d("UrbanizationSessionUtls","Session is being set to: " + isLoggedIn);

        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(LOGGED_IN, isLoggedIn);
        editor.commit();
    }

    public static boolean isLoggedIn(Context context){
        boolean isLoggedIn = getPreferences(context).getBoolean(LOGGED_IN, false);
        Log.d("UrbanizationSessionUtls","Session logged: " + isLoggedIn);
        return isLoggedIn;
    }

    public static void setLoggedUser(Context context, String user){
        if(context == null) return;

        Log.d("UrbanizationSessionUtls","Session is being set to: " + user);

        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(LOGGED_USER, user);
        editor.commit();
    }

    public static String getLoggedUser(Context context){
        String user = getPreferences(context).getString(LOGGED_USER, "");
        Log.d("UrbanizationSessionUtls","Session logged: " + user);
        return user;
    }

    public static void setLoggedLogin(Context context, String login){
        if(context == null) return;

        Log.d("UrbanizationSessionUtls","Session is being set to: " + login);

        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(LOGGED_LOGIN, login);
        editor.commit();
    }

    public static String getLoggedLogin(Context context){
        String login = getPreferences(context).getString(LOGGED_LOGIN, "");
        Log.d("UrbanizationSessionUtls","Session logged: " + login);
        return login;
    }

    public static void setLoggedPassword(Context context, String password){
        if(context == null) return;

        Log.d("UrbanizationSessionUtls","Session is being set to: " + password);

        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(LOGGED_PASSWORD, password);
        editor.commit();
    }

    public static String getLoggedPassword(Context context){
        String password = getPreferences(context).getString(LOGGED_PASSWORD, "");
        Log.d("UrbanizationSessionUtls","Session logged: " + password);
        return password;
    }

    // -----------------------------------------------------------------------------
    // For Password encription (for offline login)
    // -----------------------------------------------------------------------------
    public static void storePassword(Context context, String wsApiPass, String userPassword) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        //TODO
        //editor.putString(PASSWORD, InalambrikEncriptionUtils.code(wsApiPass,userPassword));
        editor.commit();
    }

    //Get decrypted sharepreference password
    public static String getPassword(Context context, String api_pass) {
        String userPass = getPreferences(context).getString(PASSWORD,"");
        userPass = (userPass!= null) ? userPass : "";
        if (userPass.isEmpty())
            return userPass;
        else
            return null;
            //TODO
            //return InalambrikEncriptionUtils.decode(api_pass,userPass);
    }

    //Save ApiPass in sharepreference
    public static void storeLoginEncryptKey(Context context, String loginEncryptKey) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(SANTORINI_LOGIN_ENCRIPT_KEY, loginEncryptKey);
        editor.commit();
    }

    //Get ApiPass from sharepreference
    public static String getLoginEncriptKey(Context context) {
        return getPreferences(context).getString(SANTORINI_LOGIN_ENCRIPT_KEY,"");
    }


    public static void setLoggedOffline(Context context, boolean isLoginOffline){
        if(context == null) return;
        Log.d("UrbanizationSessionUtls","Session offline: " + isLoginOffline);
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(LOGGED_OFFLINE, isLoginOffline);
        editor.commit();
    }

    public static boolean isLoggedOffline(Context context){
        boolean isLoginOffline = getPreferences(context).getBoolean(LOGGED_OFFLINE, false);
        Log.d("UrbanizationSessionUtls","Session offline: " + isLoginOffline);
        return isLoginOffline;
    }
}
