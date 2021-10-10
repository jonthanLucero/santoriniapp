package com.example.santoriniapp.retrofit.dtos.paymentListDtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentListResponse
{
    @SerializedName("ErrorMessage")
    @Expose
    private String ErrorMessage;

    @SerializedName("Payments")
    @Expose
    private String Payments;


    // ----------------------------------
    // Getters
    // ----------------------------------
    public String getErrorMessage() {
        return ErrorMessage;
    }

    public String getPayments() {
        return Payments;
    }

    // ----------------------------------
    // Setters
    // ----------------------------------
    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    public void setPayments(String payments) {
        Payments = payments;
    }

}
