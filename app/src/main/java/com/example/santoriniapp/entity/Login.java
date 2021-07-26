package com.example.santoriniapp.entity;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.example.santoriniapp.utils.DateConverter;

@Entity(tableName = "login_table",primaryKeys = {"userid"})
public class Login
{
    @NonNull
    private String userid;

    @ColumnInfo(name = "userlogin")
    private String userlogin;
    @ColumnInfo(name = "userpassword")
    private String userpassword;
    @ColumnInfo(name = "userstatus")
    private String userstatus;
    @ColumnInfo(name = "username")
    private String username;
    @ColumnInfo(name = "usertaxpayerid")
    private String usertaxpayerid;

    @TypeConverters(DateConverter.class)
    @ColumnInfo(name = "usermodifiedon")
    private String usermodifiedon;

    public Login(@NonNull String userid, String userlogin, String userpassword,
                 String userstatus, String username, String usertaxpayerid, String usermodifiedon) {
        this.userid = userid;
        this.userlogin = userlogin;
        this.userpassword = userpassword;
        this.userstatus = userstatus;
        this.username = username;
        this.usertaxpayerid = usertaxpayerid;
        this.usermodifiedon = usermodifiedon;
    }

    @NonNull
    public String getUserid() {
        return userid;
    }

    public String getUserlogin() {
        return userlogin;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public String getUserstatus() {
        return userstatus;
    }

    public String getUsername() {
        return username;
    }

    public String getUsertaxpayerid() {
        return usertaxpayerid;
    }

    public String getUsermodifiedon() {
        return usermodifiedon;
    }
}
