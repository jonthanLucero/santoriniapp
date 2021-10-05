package com.example.santoriniapp.retrofit.dtos.loginDtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentListSDT
{
    @SerializedName("PaymentDate")
    @Expose
    private String PaymentDate;

    @SerializedName("PaymentNumber")
    @Expose
    private Integer PaymentNumber;

    @SerializedName("PaymentReceiptNumber")
    @Expose
    private long PaymentReceiptNumber;

    @SerializedName("PaymentStatus")
    @Expose
    private String PaymentStatus;

    @SerializedName("PaymentAmount")
    @Expose
    private Double PaymentAmount;

    @SerializedName("PaymentMemo")
    @Expose
    private String PaymentMemo;

    // ---------------------------------------------------------------------
    //  Getters
    // ---------------------------------------------------------------------
    public String getPaymentDate() {
        return PaymentDate;
    }
    public Integer getPaymentNumber(){ return PaymentNumber;}
    public String getPaymentStatus() {
        return PaymentStatus;
    }
    public Double getPaymentAmount() {
        return PaymentAmount;
    }
    public long getPaymentReceiptNumber() {
        return PaymentReceiptNumber;
    }
    public String getPaymentMemo() {
        return PaymentMemo;
    }

    // ---------------------------------------------------------------------
    //  Setters
    // ---------------------------------------------------------------------
    public void setPaymentDate(String paymentDate) {
        PaymentDate = paymentDate;
    }
    public void setPaymentStatus(String paymentStatus) {
        PaymentStatus = paymentStatus;
    }

    public void setPaymentAmount(Double paymentAmount) {
        PaymentAmount = paymentAmount;
    }

    public void setPaymentReceiptNumber(long paymentReceiptNumber) {
        PaymentReceiptNumber = paymentReceiptNumber;
    }
    public void setPaymentMemo(String paymentMemo) {
        PaymentMemo = paymentMemo;
    }

    public void setPaymentNumber(int paymentNumber)
    {
        PaymentNumber = paymentNumber;
    }
}
