package com.example.santoriniapp.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "paymenttype_table",primaryKeys = {"paymenttypecode"})
public class PaymentType
{
    @NonNull
    @ColumnInfo(name = "paymenttypecode")
    private String paymenttypecode;

    @ColumnInfo(name = "paymenttypedescription")
    private String paymenttypedescription;

    @ColumnInfo(name = "paymenttypestatus")
    private String paymenttypestatus;

    @ColumnInfo(name = "paymenttyperequiresphoto")
    private String paymenttyperequiresphoto;

    @ColumnInfo(name = "paymenttypemodifiedon")
    private String paymenttypemodifiedon;

    public PaymentType(@NonNull String paymenttypecode, String paymenttypedescription, String paymenttypestatus,
                       String paymenttyperequiresphoto, String paymenttypemodifiedon) {
        this.paymenttypecode = paymenttypecode;
        this.paymenttypedescription = paymenttypedescription;
        this.paymenttypestatus = paymenttypestatus;
        this.paymenttyperequiresphoto = paymenttyperequiresphoto;
        this.paymenttypemodifiedon = paymenttypemodifiedon;
    }

    @NonNull
    public String getPaymenttypecode() {
        return paymenttypecode;
    }

    public String getPaymenttypedescription() {
        return paymenttypedescription;
    }

    public String getPaymenttypestatus() {
        return paymenttypestatus;
    }

    public String getPaymenttyperequiresphoto() {
        return paymenttyperequiresphoto;
    }

    public String getPaymenttypemodifiedon() {
        return paymenttypemodifiedon;
    }
}
