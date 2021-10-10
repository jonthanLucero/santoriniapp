package com.example.santoriniapp.retrofit.dtos.paymentListDtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentListRequest
{
    @SerializedName("UserLogin")
    @Expose
    private String UserLogin;

    @SerializedName("UserPassword")
    @Expose
    private String UserPassword;

    @SerializedName("PaymentMonth")
    @Expose
    private String PaymentMonth;

    // ----------------------------------
    // Getters
    // ----------------------------------

    public String getUserLogin() {
        return UserLogin;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public String getPaymentMonth() {
        return PaymentMonth;
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

    public void setPaymentMonth(String paymentMonth) {
        PaymentMonth = paymentMonth;
    }
}
