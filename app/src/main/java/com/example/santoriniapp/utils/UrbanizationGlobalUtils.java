package com.example.santoriniapp.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.multidex.MultiDex;

public class UrbanizationGlobalUtils extends Application
{
    public final static String tag = "UrbanizationGlobalUtils";
    private static UrbanizationGlobalUtils _instance = null;

    private int accountid;
    private String appversion;
    private int customercount;
    private String organizationname;
    private String salespersoncode;
    private String salespersonname;
    private String userid;
    private String userlogin;
    private String userpassword;

    public void onCreate()
    {
        super.onCreate();
        _instance = this;

        setupMint();
        init();
        loadState();
    }

    public static synchronized UrbanizationGlobalUtils getInstance()
    {
        return _instance;
    }

    public void setupMint()
    {
        /*
        try
        {
            String deviceId = SystemFunctions.getDeviceId(this);
            int deviceBatteryLevel = SystemFunctions.getDeviceBatteryLevel(this);

            Mint.initAndStartSession(this, "dbc0b4a3");
            Mint.addExtraData("device_id", deviceId);
            Mint.addExtraData("battery_level", StringFunctions.toString(deviceBatteryLevel));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

         */

    }

    public void init()
    {
        accountid = 0;
        appversion = "";
        customercount = 0;
        organizationname = "";
        salespersoncode = "";
        salespersonname = "";
        userid = "";
        userlogin = "";
        userpassword = "";
    }

    //Getter Methods
    public synchronized int getaccountid()
    {
        SharedPreferences sp = getSharedPreferences("sfaplusGlobals", MODE_PRIVATE);
        accountid = sp.getInt("accountid", accountid);
        return accountid;
    }

    public synchronized String getappversion()
    {
        SharedPreferences sp = getSharedPreferences("sfaplusGlobals", MODE_PRIVATE);
        appversion = sp.getString("appversion", appversion);
        return appversion;
    }

    public synchronized int getcustomercount()
    {
        SharedPreferences sp = getSharedPreferences("sfaplusGlobals", MODE_PRIVATE);
        customercount = sp.getInt("customercount", customercount);
        return customercount;
    }

    public synchronized String getorganizationname()
    {
        SharedPreferences sp = getSharedPreferences("sfaplusGlobals", MODE_PRIVATE);
        organizationname = sp.getString("organizationname", organizationname);
        return organizationname;
    }

    public synchronized String getsalespersoncode()
    {
        SharedPreferences sp = getSharedPreferences("sfaplusGlobals", MODE_PRIVATE);
        salespersoncode = sp.getString("salespersoncode", salespersoncode);
        return salespersoncode;
    }

    public synchronized String getsalespersonname()
    {
        SharedPreferences sp = getSharedPreferences("sfaplusGlobals", MODE_PRIVATE);
        salespersonname = sp.getString("salespersonname", salespersonname);
        return salespersonname;
    }

    public synchronized String getuserid()
    {
        SharedPreferences sp = getSharedPreferences("sfaplusGlobals", MODE_PRIVATE);
        userid = sp.getString("userid", userid);
        return userid;
    }

    public synchronized String getuserlogin()
    {
        SharedPreferences sp = getSharedPreferences("sfaplusGlobals", MODE_PRIVATE);
        userlogin = sp.getString("userlogin", userlogin);
        return userlogin;
    }

    public synchronized String getuserpassword()
    {
        SharedPreferences sp = getSharedPreferences("sfaplusGlobals", MODE_PRIVATE);
        userpassword = sp.getString("userpassword", userpassword);
        return userpassword;
    }

    //Setter Methods
    public synchronized void setaccountid(int value)
    {
        accountid = value;
        SharedPreferences sp = getSharedPreferences("sfaplusGlobals", MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.apply();
    }

    public synchronized void setappversion(String value)
    {
        appversion = value;
        SharedPreferences sp = getSharedPreferences("sfaplusGlobals", MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("appversion", appversion);
        e.apply();
    }

    public synchronized void setcustomercount(int value)
    {
        customercount = value;
        SharedPreferences sp = getSharedPreferences("sfaplusGlobals", MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.apply();
    }

    public synchronized void setorganizationname(String value)
    {
        organizationname = value;
        SharedPreferences sp = getSharedPreferences("sfaplusGlobals", MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("organizationname", organizationname);
        e.apply();
    }


    public synchronized void setsalespersoncode(String value)
    {
        salespersoncode = value;
        SharedPreferences sp = getSharedPreferences("sfaplusGlobals", MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("salespersoncode", salespersoncode);
        e.apply();
    }

    public synchronized void setsalespersonname(String value)
    {
        salespersonname = value;
        SharedPreferences sp = getSharedPreferences("sfaplusGlobals", MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("salespersonname", salespersonname);
        e.apply();
    }

    public synchronized void setuserid(String value)
    {
        userid = value;
        SharedPreferences sp = getSharedPreferences("sfaplusGlobals", MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("userid", userid);
        e.apply();
    }

    public synchronized void setuserlogin(String value)
    {
        userlogin = value;
        SharedPreferences sp = getSharedPreferences("sfaplusGlobals", MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("userlogin", userlogin);
        e.apply();
    }

    public synchronized void setuserpassword(String value)
    {
        userpassword = value;
        SharedPreferences sp = getSharedPreferences("sfaplusGlobals", MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString("userpassword", userpassword);
        e.apply();
    }

    public void loadState()
    {
        SharedPreferences sp = getSharedPreferences("sfaplusGlobals", MODE_PRIVATE);
        accountid = sp.getInt("accountid", accountid);
        appversion = sp.getString("appversion", appversion);
        customercount = sp.getInt("customercount", customercount);
        organizationname = sp.getString("organizationname", organizationname);
        salespersoncode = sp.getString("salespersoncode", salespersoncode);
        salespersonname = sp.getString("salespersonname", salespersonname);
        userid = sp.getString("userid", userid);
        userlogin = sp.getString("userlogin", userlogin);
        userpassword = sp.getString("userpassword", userpassword);
    }

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
