package com.example.santoriniapp.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "paymenttype_table")
public class PaymentType
{
    @PrimaryKey(autoGenerate = true)
    private int _id;

    @NonNull
    @ColumnInfo(name = "paymenttypecode")
    private String paymenttypecode;

    @ColumnInfo(name = "paymenttypedescription")
    private String paymenttypedescription;

    @ColumnInfo(name = "paymenttypestatus")
    private String paymenttypestatus;

    @ColumnInfo(name = "paymenttyperequiresphoto")
    private boolean paymenttyperequiresphoto;

    @ColumnInfo(name = "paymenttypemodifiedon")
    private Long paymenttypemodifiedon;

    public PaymentType(@NonNull String paymenttypecode, String paymenttypedescription, String paymenttypestatus,
                       boolean paymenttyperequiresphoto, long paymenttypemodifiedon) {
        this.paymenttypecode = paymenttypecode;
        this.paymenttypedescription = paymenttypedescription;
        this.paymenttypestatus = paymenttypestatus;
        this.paymenttyperequiresphoto = paymenttyperequiresphoto;
        this.paymenttypemodifiedon = paymenttypemodifiedon;
    }

    public int get_id()
    {
        return _id;
    }

    public void set_id(int id)
    {
        this._id = id;
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

    public boolean getPaymenttyperequiresphoto() {
        return paymenttyperequiresphoto;
    }

    public Long getPaymenttypemodifiedon() {
        return paymenttypemodifiedon;
    }
}
