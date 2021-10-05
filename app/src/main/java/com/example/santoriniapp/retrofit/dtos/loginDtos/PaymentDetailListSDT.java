package com.example.santoriniapp.retrofit.dtos.loginDtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentDetailListSDT
{
    @SerializedName("PaymentDate")
    @Expose
    private String PaymentDate;

    @SerializedName("PaymentDetailLine")
    @Expose
    private Integer PaymentDetailLine;

    @SerializedName("PaymenDetailPaymentTypeCode")
    @Expose
    private String PaymenDetailPaymentTypeCode;

    @SerializedName("PaymentDetailAmount")
    @Expose
    private Double PaymentDetailAmount;

    @SerializedName("PaymentDetailDate")
    @Expose
    private String PaymentDetailDate;

    @SerializedName("PaymentDetailPhotoBase64")
    @Expose
    private String PaymentDetailPhotoBase64;

    // ---------------------------------------------------------------------
    //  Getters
    // ---------------------------------------------------------------------
    public String getPaymentDate() {
        return PaymentDate;
    }
    public int getPaymentDetailLine(){return PaymentDetailLine;}
    public String getPaymenDetailPaymentTypeCode() {
        return PaymenDetailPaymentTypeCode;
    }
    public Double getPaymentDetailAmount() {
        return PaymentDetailAmount;
    }
    public String getPaymentDetailDate(){return PaymentDetailDate;}
    public String getPaymentDetailPhotoBase64() {
        return PaymentDetailPhotoBase64;
    }

    // ---------------------------------------------------------------------
    //  Setters
    // ---------------------------------------------------------------------
    public void setPaymentDate(String paymentDate) {
        PaymentDate = paymentDate;
    }
    public void setPaymentDetailLine(int paymentDetailLine){PaymentDetailLine = paymentDetailLine;}
    public void setPaymenDetailPaymentTypeCode(String paymenDetailPaymentTypeCode) {
        PaymenDetailPaymentTypeCode = paymenDetailPaymentTypeCode;
    }
    public void setPaymentDetailAmount(Double paymentDetailAmount) {
        PaymentDetailAmount = paymentDetailAmount;
    }
    public void setPaymentDetailDate(String paymentDetailDate){PaymentDetailDate = paymentDetailDate;}
    public void setPaymentDetailPhotoBase64(String paymentDetailPhotoBase64){PaymentDetailPhotoBase64 = paymentDetailPhotoBase64;}

}
