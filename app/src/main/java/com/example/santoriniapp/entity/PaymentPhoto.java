package com.example.santoriniapp.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "payment_photo")
public class PaymentPhoto implements Serializable
{
    @PrimaryKey(autoGenerate = true)
    private int _id;

    @NonNull
    @ColumnInfo(name = "paymentdate")
    private Long paymentdate;

    @ColumnInfo(name = "paymentphototitle")
    private String paymentphototitle;

    @ColumnInfo(name = "paymentphotodescription")
    private String paymentphotodescription;

    @ColumnInfo(name = "paymentphotopath")
    private String paymentphotopath;

    @ColumnInfo(name = "paymentphotobase64")
    private String paymentphotobase64;

    @ColumnInfo(name = "paymentphotomodifiedon")
    private Long paymentphotomodifiedon;

    public PaymentPhoto(Long paymentdate, String paymentphototitle, String paymentphotodescription, String paymentphotopath, String paymentphotobase64, Long paymentphotomodifiedon) {
        this.paymentdate = paymentdate;
        this.paymentphototitle = paymentphototitle;
        this.paymentphotodescription = paymentphotodescription;
        this.paymentphotopath = paymentphotopath;
        this.paymentphotobase64 = paymentphotobase64;
        this.paymentphotomodifiedon = paymentphotomodifiedon;
    }

    public PaymentPhoto()
    {

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public Long getPaymentdate() {
        return paymentdate;
    }

    public void setPaymentdate(Long paymentdate) {
        this.paymentdate = paymentdate;
    }

    public String getPaymentphototitle() {
        return paymentphototitle;
    }

    public void setPaymentphototitle(String paymentphototitle) {
        this.paymentphototitle = paymentphototitle;
    }

    public String getPaymentphotodescription() {
        return paymentphotodescription;
    }

    public void setPaymentphotodescription(String paymentphotodescription) {
        this.paymentphotodescription = paymentphotodescription;
    }

    public String getPaymentphotopath() {
        return paymentphotopath;
    }

    public void setPaymentphotopath(String paymentphotopath) {
        this.paymentphotopath = paymentphotopath;
    }

    public String getPaymentphotobase64() {
        return paymentphotobase64;
    }

    public void setPaymentphotobase64(String paymentphotobase64) {
        this.paymentphotobase64 = paymentphotobase64;
    }

    public Long getPaymentphotomodifiedon() {
        return paymentphotomodifiedon;
    }

    public void setPaymentphotomodifiedon(Long paymentphotomodifiedon) {
        this.paymentphotomodifiedon = paymentphotomodifiedon;
    }
}
