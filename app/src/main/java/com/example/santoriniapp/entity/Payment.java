package com.example.santoriniapp.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.example.santoriniapp.utils.DateConverter;

import java.util.Date;

@Entity(tableName = "payment_table",primaryKeys = {"paymentdate"})
public class Payment
{
    @NonNull
    private String userid;
    @NonNull

    @TypeConverters(DateConverter.class)
    private Date paymentdate;

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

    @TypeConverters(DateConverter.class)
    @ColumnInfo(name = "paymentmodifiedon")
    private Date paymentmodifiedon;

    public Payment(@NonNull String userid, @NonNull Date paymentdate, int paymentnumber, int paymentreceiptnumber,
                   double paymentamount, String paymentstatus, String paymentmemo, Date paymentmodifiedon) {
        this.userid = userid;
        this.paymentdate = paymentdate;
        this.paymentnumber = paymentnumber;
        this.paymentreceiptnumber = paymentreceiptnumber;
        this.paymentamount = paymentamount;
        this.paymentstatus = paymentstatus;
        this.paymentmemo = paymentmemo;
        this.paymentmodifiedon = paymentmodifiedon;
    }

    @NonNull
    public String getUserid() {
        return userid;
    }

    @NonNull
    public Date getPaymentdate() {
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

    public Date getPaymentmodifiedon() {
        return paymentmodifiedon;
    }
}
