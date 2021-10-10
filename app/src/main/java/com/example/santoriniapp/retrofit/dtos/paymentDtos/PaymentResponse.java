package com.example.santoriniapp.retrofit.dtos.paymentDtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentResponse
{
    @SerializedName("ErrorMessage")
    @Expose
    private String ErrorMessage;

    @SerializedName("PaymentNumber")
    @Expose
    private Integer PaymentNumber;

    @SerializedName("PaymentReceiptNumber")
    @Expose
    private Integer PaymentReceiptNumber;


    // ----------------------------------
    // Getters
    // ----------------------------------
    public String getErrorMessage() {
        return ErrorMessage;
    }

    public Integer getPaymentNumber() {
        return PaymentNumber;
    }

    public Integer getPaymentReceiptNumber() {
        return PaymentReceiptNumber;
    }

    // ----------------------------------
    // Setters
    // ----------------------------------
    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    public void setPaymentNumber(Integer paymentNumber) {
        PaymentNumber = paymentNumber;
    }

    public void setPaymentReceiptNumber(Integer paymentReceiptNumber){PaymentReceiptNumber = paymentReceiptNumber;}

}
