package com.example.santoriniapp.retrofit.dtos.paymentDtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentPhotoSDT
{
    @SerializedName("PaymentDate")
    @Expose
    private String PaymentDate;

    @SerializedName("PaymentPhotoTitle")
    @Expose
    private String PaymentPhotoTitle;

    @SerializedName("PaymentPhotoDescription")
    @Expose
    private String PaymentPhotoDescription;

    @SerializedName("PaymentPhotoBase64")
    @Expose
    private String PaymentPhotoBase64;

    // ---------------------------------------------------------------------
    //  Getters
    // ---------------------------------------------------------------------
    public String getPaymentDate() {
        return PaymentDate;
    }
    public String getPaymentPhotoTitle(){return PaymentPhotoTitle;}
    public String getPaymentPhotoDescription(){return PaymentPhotoDescription;}
    public String getPaymentPhotoBase64() {
        return PaymentPhotoBase64;
    }

    // ---------------------------------------------------------------------
    //  Setters
    // ---------------------------------------------------------------------
    public void setPaymentDate(String paymentDate) {
        PaymentDate = paymentDate;
    }
    public void setPaymentPhotoTitle(String paymentPhotoTitle){PaymentPhotoTitle = paymentPhotoTitle;}
    public void setPaymentPhotoDescription(String paymentPhotoDescription){PaymentPhotoDescription = paymentPhotoDescription;}
    public void setPaymentPhotoBase64(String paymentPhotoBase64){PaymentPhotoBase64 = paymentPhotoBase64;}
}
