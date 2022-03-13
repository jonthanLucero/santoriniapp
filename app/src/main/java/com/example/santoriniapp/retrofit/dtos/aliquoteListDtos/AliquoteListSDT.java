package com.example.santoriniapp.retrofit.dtos.aliquoteListDtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AliquoteListSDT
{
    @SerializedName("AliquoteYear")
    @Expose
    private Integer AliquoteYear;

    @SerializedName("AliquoteMonthCode")
    @Expose
    private String AliquoteMonthCode;

    @SerializedName("AliquoteValue")
    @Expose
    private Double AliquoteValue;

    @SerializedName("AliquoteReceivedValue")
    @Expose
    private Double AliquoteReceivedValue;

    @SerializedName("AliquoteStatus")
    @Expose
    private String AliquoteStatus;

    // ---------------------------------------------------------------------
    //  Getters
    // ---------------------------------------------------------------------
    public Integer getAliquoteYear() {
        return AliquoteYear;
    }
    public String getAliquoteMonthCode(){return AliquoteMonthCode;}
    public Double getAliquoteValue(){return AliquoteValue;}
    public Double getAliquoteReceivedValue(){return AliquoteReceivedValue;}
    public String getAliquoteStatus() {
        return AliquoteStatus;
    }

    // ---------------------------------------------------------------------
    //  Setters
    // ---------------------------------------------------------------------
    public void setAliquoteYear(Integer aliquoteYear) {
        AliquoteYear = aliquoteYear;
    }
    public void setAliquoteMonthCode(String aliquoteMonthCode) { AliquoteMonthCode = aliquoteMonthCode; }
    public void setAliquoteValue(Double aliquoteValue) {
        AliquoteValue = aliquoteValue;
    }
    public void setAliquoteReceivedValue(Double aliquoteReceivedValue) { AliquoteReceivedValue = aliquoteReceivedValue; }
    public void setAliquoteStatus(String aliquoteStatus) {
        AliquoteStatus = aliquoteStatus;
    }
}
