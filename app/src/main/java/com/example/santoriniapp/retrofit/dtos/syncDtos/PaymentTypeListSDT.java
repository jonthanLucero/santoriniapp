package com.example.santoriniapp.retrofit.dtos.syncDtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentTypeListSDT
{

    @SerializedName("PaymentTypeCode")
    @Expose
    private String PaymentTypeCode;

    @SerializedName("PaymentTypeName")
    @Expose
    private String PaymentTypeName;

    @SerializedName("PaymentTypeRequiresPhoto")
    @Expose
    private Integer PaymentTypeRequiresPhoto;

    // ---------------------------------------------------------------------
    //  Getters
    // ---------------------------------------------------------------------
    public String getPaymentTypeCode() {
        return PaymentTypeCode;
    }
    public String getPaymentTypeName() {
        return PaymentTypeName;
    }
    public Integer getPaymentTypeRequiresPhoto(){return PaymentTypeRequiresPhoto;}

    // ---------------------------------------------------------------------
    //  Setters
    // ---------------------------------------------------------------------
    public void setPaymentTypeCode(String paymentTypeCode) {
        PaymentTypeCode = paymentTypeCode;
    }
    public void setPaymentTypeName(String paymentTypeName) {
        PaymentTypeName = paymentTypeName;
    }
    public void setPaymentTypeRequiresPhoto(int paymentTypeRequiresPhoto){PaymentTypeRequiresPhoto = paymentTypeRequiresPhoto;}

}
