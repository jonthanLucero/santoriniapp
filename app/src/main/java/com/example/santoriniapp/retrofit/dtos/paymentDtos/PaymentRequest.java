package com.example.santoriniapp.retrofit.dtos.paymentDtos;

import com.example.santoriniapp.dao.PaymentDAO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentRequest
{
    @SerializedName("UserLogin")
    @Expose
    private String UserLogin;

    @SerializedName("UserPassword")
    @Expose
    private String UserPassword;

    @SerializedName("PaymentDate")
    @Expose
    private String PaymentDate;

    @SerializedName("PaymentYear")
    @Expose
    private Integer PaymentYear;

    @SerializedName("PaymentMonth")
    @Expose
    private String PaymentMonth;

    @SerializedName("PaymentTypeCode")
    @Expose
    private String PaymentTypeCode;

    @SerializedName("PaymentAmount")
    @Expose
    private Double PaymentAmount;

    @SerializedName("PaymentMemo")
    @Expose
    private String PaymentMemo;

    @SerializedName("PaymentPhotoList")
    @Expose
    private String PaymentPhotoList;


    // ----------------------------------
    // Getters
    // ----------------------------------

    public String getUserLogin() {
        return UserLogin;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public String getPaymentDate() {
        return PaymentDate;
    }

    public Integer getPaymentYear() {
        return PaymentYear;
    }

    public String getPaymentMonth() {
        return PaymentMonth;
    }

    public String getPaymentTypeCode() {
        return PaymentTypeCode;
    }

    public Double getPaymentAmount() {
        return PaymentAmount;
    }

    public String getPaymentMemo() {
        return PaymentMemo;
    }

    public String getPaymentPhotoList() {
        return PaymentPhotoList;
    }


    // ----------------------------------
    // Setters
    // ----------------------------------

    public void setUserLogin(String userLogin) {
        UserLogin = userLogin;
    }

    public void setUserPassword(String userPassword) {
        UserPassword = userPassword;
    }

    public void setPaymentDate(String paymentDate) {
        PaymentDate = paymentDate;
    }

    public void setPaymentYear(Integer paymentYear) {
        PaymentYear = paymentYear;
    }

    public void setPaymentMonth(String paymentMonth) {
        PaymentMonth = paymentMonth;
    }

    public void setPaymentTypeCode(String paymentTypeCode) {
        PaymentTypeCode = paymentTypeCode;
    }

    public void setPaymentAmount(Double paymentAmount) {
        PaymentAmount = paymentAmount;
    }

    public void setPaymentMemo(String paymentMemo) {
        PaymentMemo = paymentMemo;
    }

    public void setPaymentPhotoList(String paymentPhotoList){PaymentPhotoList = paymentPhotoList;}

}
