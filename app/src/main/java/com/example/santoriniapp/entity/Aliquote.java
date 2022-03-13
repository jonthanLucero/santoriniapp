package com.example.santoriniapp.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "aliquote_table")
public class Aliquote implements Serializable
{
    @PrimaryKey(autoGenerate = true)
    private int _id;
    @NonNull
    private String userid;
    @NonNull

    @ColumnInfo(name = "aliquoteyear")
    private int aliquoteyear;

    @ColumnInfo(name = "aliquotemonthcode")
    private String aliquotemonthcode;

    @ColumnInfo(name = "aliquotevalue")
    private double aliquotevalue;

    @ColumnInfo(name = "aliquotereceivedvalue")
    private double aliquotereceivedvalue;

    @ColumnInfo(name = "aliquotestatus")
    private String aliquotestatus;

    @ColumnInfo(name = "aliquotemodifiedon")
    private Long aliquotemodifiedon;

    public Aliquote(@NonNull String userid,int aliquoteyear, String aliquotemonthcode,double aliquotevalue,double aliquotereceivedvalue,
                   String aliquotestatus, Long aliquotemodifiedon) {

        this.userid                  = userid;
        this.aliquoteyear            = aliquoteyear;
        this.aliquotemonthcode       = aliquotemonthcode;
        this.aliquotevalue           = aliquotevalue;
        this.aliquotereceivedvalue   = aliquotereceivedvalue;
        this.aliquotestatus          = aliquotestatus;
    }

    public Aliquote() {
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

    public int getAliquoteyear() {
        return aliquoteyear;
    }

    public void setAliquoteyear(int aliquoteyear) {
        this.aliquoteyear = aliquoteyear;
    }

    public String getAliquotemonthcode() {
        return aliquotemonthcode;
    }

    public void setAliquotemonthcode(String aliquotemonthcode) {
        this.aliquotemonthcode = aliquotemonthcode;
    }

    public double getAliquotevalue(){return aliquotevalue;}

    public void setAliquotevalue(Double aliquotevalue)
    {
        this.aliquotevalue = aliquotevalue;
    }

    public double getAliquotereceivedvalue(){return aliquotereceivedvalue;}

    public void setAliquotereceivedvalue(Double aliquotereceivedvalue)
    {
        this.aliquotereceivedvalue = aliquotereceivedvalue;
    }

    public String getAliquotestatus() {
        return aliquotestatus;
    }

    public void setAliquotestatus(String aliquotestatus)
    {
        this.aliquotestatus = aliquotestatus;
    }

    public Long getAliquotemodifiedon(){return aliquotemodifiedon;}

    public void setAliquotemodifiedon(Long aliquotemodifiedon) {
        this.aliquotemodifiedon = aliquotemodifiedon;
    }
}
