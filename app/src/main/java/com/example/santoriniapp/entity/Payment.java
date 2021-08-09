package com.example.santoriniapp.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.santoriniapp.utils.DateConverter;
import com.example.santoriniapp.utils.DateFunctions;
import com.example.santoriniapp.utils.NumericFunctions;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "payment_table")
public class Payment implements Serializable
{

    @PrimaryKey(autoGenerate = true)
    private int _id;

    @NonNull
    private String userid;
    @NonNull

    @ColumnInfo(name = "paymentdate")
    private Long paymentdate;

    @ColumnInfo(name = "paymentnumber")
    private int paymentnumber;
    @ColumnInfo(name = "paymentreceiptnumber")
    private int paymentreceiptnumber;
    @ColumnInfo(name = "paymentamount")
    private double paymentamount;
    @ColumnInfo(name = "paymentstatus")
    private String paymentstatus;
    @ColumnInfo(name = "paymentmemo")
    private String paymentmemo;

    @ColumnInfo(name = "paymentmodifiedon")
    private Long paymentmodifiedon;

    public Payment(@NonNull String userid, @NonNull Long paymentdate, int paymentnumber, int paymentreceiptnumber,
                   double paymentamount, String paymentstatus, String paymentmemo,Long paymentmodifiedon) {
        this.userid = userid;
        this.paymentdate = paymentdate;
        this.paymentnumber = paymentnumber;
        this.paymentreceiptnumber = paymentreceiptnumber;
        this.paymentamount = paymentamount;
        this.paymentstatus = paymentstatus;
        this.paymentmemo = paymentmemo;
        this.paymentmodifiedon = paymentmodifiedon;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    @NonNull
    public String getUserid() {
        return userid;
    }

    @NonNull
    public Long getPaymentdate() {
        return paymentdate;
    }

    public int getPaymentnumber() {
        return paymentnumber;
    }

    public int getPaymentreceiptnumber() {
        return paymentreceiptnumber;
    }

    public double getPaymentamount() {
        return paymentamount;
    }

    public String getPaymentstatus() {
        return paymentstatus;
    }

    public String getPaymentmemo() {
        return paymentmemo;
    }

    public Long getPaymentmodifiedon() {
        return paymentmodifiedon;
    }
}
