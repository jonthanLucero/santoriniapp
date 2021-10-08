package com.example.santoriniapp.retrofit.dtos.syncDtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SyncInformationResponse
{
    @SerializedName("ErrorMessage")
    @Expose
    private String ErrorMessage;

    @SerializedName("PaymentTypeList")
    @Expose
    private String PaymentTypeList;

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public String getPaymentTypeList() {
        return PaymentTypeList;
    }

    public void setErrorMessage(String errorMessage) { ErrorMessage = errorMessage; }

    public void setPaymentTypeList(String paymentTypeList) { PaymentTypeList = paymentTypeList; }
}
