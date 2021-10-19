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

    @ColumnInfo(name = "paymentmonth")
    private String paymentmonth;

    @ColumnInfo(name = "paymenttypecode")
    private String paymenttypecode;

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

    @ColumnInfo(name = "paymentvoidmemo")
    private String paymentvoidmemo;


    @ColumnInfo(name = "paymentmodifiedon")
    private Long paymentmodifiedon;

    public Payment(@NonNull String userid, @NonNull Long paymentdate,String paymentmonth,String paymenttypecode, int paymentnumber, int paymentreceiptnumber,
                   double paymentamount, String paymentstatus, String paymentmemo,String paymentVoidMemo, Long paymentmodifiedon) {
        this.userid = userid;
        this.paymentdate = paymentdate;
        this.paymentmonth = paymentmonth;
        this.paymenttypecode = paymenttypecode;
        this.paymentnumber = paymentnumber;
        this.paymentreceiptnumber = paymentreceiptnumber;
        this.paymentamount = paymentamount;
        this.paymentstatus = paymentstatus;
        this.paymentmemo = paymentmemo;
        this.paymentvoidmemo = paymentVoidMemo;
        this.paymentmodifiedon = paymentmodifiedon;
    }

    public Payment() {
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

    public void setUserid(@NonNull String userid) {
        this.userid = userid;
    }

    @NonNull
    public Long getPaymentdate() {
        return paymentdate;
    }

    public String getPaymentmonth() {
        return paymentmonth;
    }

    public void setPaymentmonth(String paymentmonth) {
        this.paymentmonth = paymentmonth;
    }

    public String getPaymenttypecode() {
        return paymenttypecode;
    }

    public void setPaymenttypecode(String paymenttypecode) {
        this.paymenttypecode = paymenttypecode;
    }

    public void setPaymentAmount(Double paymentAmount)
    {
        this.paymentamount = paymentAmount;
    }

    public void setPaymentmemo(String paymentmemo)
    {
        this.paymentmemo = paymentmemo;
    }

    public void setPaymentstatus(String paymentstatus)
    {
        this.paymentstatus = paymentstatus;
    }

    public void setPaymentmodifiedon(Long paymentmodifiedon) {
        this.paymentmodifiedon = paymentmodifiedon;
    }

    public void setPaymentdate(@NonNull Long paymentdate) {
        this.paymentdate = paymentdate;
    }

    public void setPaymentnumber(int paymentnumber) {
        this.paymentnumber = paymentnumber;
    }

    public void setPaymentreceiptnumber(int paymentreceiptnumber) {
        this.paymentreceiptnumber = paymentreceiptnumber;
    }

    public void setPaymentamount(double paymentamount) {
        this.paymentamount = paymentamount;
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

    public String getPaymentvoidmemo()
    {
        return paymentvoidmemo;
    }

    public void setPaymentvoidmemo(String Paymentvoidmemo)
    {
        this.paymentvoidmemo = Paymentvoidmemo;
    }
}
