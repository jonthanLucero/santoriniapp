package com.example.santoriniapp.retrofit.dtos.paymentDtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentListSDT
{
    @SerializedName("PaymentDate")
    @Expose
    private String PaymentDate;

    @SerializedName("PaymentMonth")
    @Expose
    private String PaymentMonth;

    @SerializedName("PaymentTypeCode")
    @Expose
    private String PaymentTypeCode;

    @SerializedName("PaymentNumber")
    @Expose
    private Integer PaymentNumber;

    @SerializedName("PaymentReceiptNumber")
    @Expose
    private Integer PaymentReceiptNumber;

    @SerializedName("PaymentStatus")
    @Expose
    private String PaymentStatus;

    @SerializedName("PaymentAmount")
    @Expose
    private Double PaymentAmount;

    @SerializedName("PaymentMemo")
    @Expose
    private String PaymentMemo;

    @SerializedName("PaymentVoidMemo")
    @Expose
    private String PaymentVoidMemo;

    // ---------------------------------------------------------------------
    //  Getters
    // ---------------------------------------------------------------------
    public String getPaymentDate() {
        return PaymentDate;
    }
    public String getPaymentMonth(){return PaymentMonth;}
    public String getPaymentTypeCode(){return PaymentTypeCode;}
    public Integer getPaymentNumber(){ return PaymentNumber;}
    public String getPaymentStatus() {
        return PaymentStatus;
    }
    public Double getPaymentAmount() {
        return PaymentAmount;
    }
    public Integer getPaymentReceiptNumber() {
        return PaymentReceiptNumber;
    }
    public String getPaymentMemo() {
        return PaymentMemo;
    }
    public String getPaymentVoidMemo(){return PaymentVoidMemo;}

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

    public void setPaymentReceiptNumber(int paymentReceiptNumber) {
        PaymentReceiptNumber = paymentReceiptNumber;
    }
    public void setPaymentMemo(String paymentMemo) {
        PaymentMemo = paymentMemo;
    }

    public void setPaymentNumber(int paymentNumber)
    {
        PaymentNumber = paymentNumber;
    }

    public void setPaymentVoidMemo(String paymentVoidMemo)
    {
        this.PaymentVoidMemo = paymentVoidMemo;
    }
}
