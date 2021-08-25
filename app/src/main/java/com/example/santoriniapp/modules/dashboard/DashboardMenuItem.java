package com.example.santoriniapp.modules.dashboard;

import android.os.Bundle;

public class DashboardMenuItem
{
    private int icon;
    private Class className;
    private String itemText;
    private Bundle parms;


    //Constructors
    public DashboardMenuItem(int icon, Class className, String itemText){
        this.icon = icon;
        this.className = className;
        this.itemText = itemText;
        this.parms = new Bundle();
    }

    //Getters
    public int get_icon_drawable(){
        return this.icon;
    }

    public Class get_class_name(){
        return this.className;
    }

    public String get_item_text(){
        return this.itemText;
    }

    public Bundle get_parms(){
        return this.parms;
    }
}
