package com.example.santoriniapp.entity;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.santoriniapp.utils.DateConverter;
import com.example.santoriniapp.utils.DateFunctions;
import com.example.santoriniapp.utils.StringFunctions;

import java.io.Serializable;

@Entity(tableName = "login_table")
public class Login implements Serializable
{

    @PrimaryKey(autoGenerate = true)
    private int _id;

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

    @ColumnInfo(name = "userphotourl")
    private String userphotourl;

    @TypeConverters(DateConverter.class)
    @ColumnInfo(name = "usermodifiedon")
    private String usermodifiedon;

    public Login()
    {

    }

    public Login(@NonNull String userid, String userlogin, String userpassword,
                 String userstatus, String username, String usertaxpayerid,String userphotourl) {
        this.userid = userid;
        this.userlogin = userlogin;
        this.userpassword = userpassword;
        this.userstatus = userstatus;
        this.username = username;
        this.usertaxpayerid = usertaxpayerid;
        this.usermodifiedon = StringFunctions.toString(DateFunctions.today().getTime());
        this.userphotourl = userphotourl;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
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

    public String getUserphotourl(){return userphotourl;}

    public void setUserid(@NonNull String userid) {
        this.userid = userid;
    }

    public void setUserlogin(String userlogin) {
        this.userlogin = userlogin;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public void setUserstatus(String userstatus) {
        this.userstatus = userstatus;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUsertaxpayerid(String usertaxpayerid) {
        this.usertaxpayerid = usertaxpayerid;
    }

    public void setUsermodifiedon(String usermodifiedon) {
        this.usermodifiedon = usermodifiedon;
    }

    public void setUserphotourl(String userphotourl){this.userphotourl = userphotourl;}
}
