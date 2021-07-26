package com.example.santoriniapp.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.example.santoriniapp.utils.DateConverter;

import java.util.Date;

@Entity(tableName = "paymentdetail_table",primaryKeys = {"paymentdate","paymentdetailline"})
public class PaymentDetail
{
    @NonNull
    @ColumnInfo(name = "paymentdate")
    @TypeConverters(DateConverter.class)
    private Date paymentdate;

    @NonNull
    @ColumnInfo(name = "paymentdetailline")
    private int paymentdetailline;

    @ColumnInfo(name = "paymentdetailpaymenttypecode")
    private String paymentdetailpaymenttypecode;

    @ColumnInfo(name = "paymentdetailamount")
    private double paymentdetailamount;

    @ColumnInfo(name = "paymentdetaildate")
    @TypeConverters(DateConverter.class)
    private Date paymentdetaildate;

    @ColumnInfo(name = "paymentdetailmodifiedon")
    @TypeConverters(DateConverter.class)
    private Date paymentdetailmodifiedon;

    @ColumnInfo(name = "paymentdetailimagepath")
    @TypeConverters(DateConverter.class)
    private Date paymentdetailimagepath;

    @ColumnInfo(name = "paymentdetailimagebase64")
    @TypeConverters(DateConverter.class)
    private Date paymentdetailimagebase64;

    public PaymentDetail(@NonNull Date paymentdate, int paymentdetailline, String paymentdetailpaymenttypecode,
                         double paymentdetailamount, Date paymentdetaildate, Date paymentdetailmodifiedon,
                         Date paymentdetailimagepath, Date paymentdetailimagebase64) {
        this.paymentdate = paymentdate;
        this.paymentdetailline = paymentdetailline;
        this.paymentdetailpaymenttypecode = paymentdetailpaymenttypecode;
        this.paymentdetailamount = paymentdetailamount;
        this.paymentdetaildate = paymentdetaildate;
        this.paymentdetailmodifiedon = paymentdetailmodifiedon;
        this.paymentdetailimagepath = paymentdetailimagepath;
        this.paymentdetailimagebase64 = paymentdetailimagebase64;
    }

    @NonNull
    public Date getPaymentdate() {
        return paymentdate;
    }

    public int getPaymentdetailline() {
        return paymentdetailline;
    }

    public String getPaymentdetailpaymenttypecode() {
        return paymentdetailpaymenttypecode;
    }

    public double getPaymentdetailamount() {
        return paymentdetailamount;
    }

    public Date getPaymentdetaildate() {
        return paymentdetaildate;
    }

    public Date getPaymentdetailmodifiedon() {
        return paymentdetailmodifiedon;
    }

    public Date getPaymentdetailimagepath() {
        return paymentdetailimagepath;
    }

    public Date getPaymentdetailimagebase64() {
        return paymentdetailimagebase64;
    }
}
