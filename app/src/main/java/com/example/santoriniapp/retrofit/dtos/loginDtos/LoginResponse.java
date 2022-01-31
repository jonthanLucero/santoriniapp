package com.example.santoriniapp.retrofit.dtos.loginDtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse
{
    @SerializedName("UserName")
    @Expose
    private String UserName;

    @SerializedName("UserId")
    @Expose
    private String UserId;

    @SerializedName("UserTaxPayerId")
    @Expose
    private String UserTaxPayerId;

    @SerializedName("UserPhotoURL")
    @Expose
    private String UserPhotoURL;

    @SerializedName("ErrorMessage")
    @Expose
    private String ErrorMessage;

    @SerializedName("PaymentTypeList")
    @Expose
    private String PaymentTypeList;

    // ----------------------------------
    // Getters
    // ----------------------------------

    public String getErrorMessage() {
        return ErrorMessage;
    }
    /*
    public String getPayments() {
        return Payments;
    }
    */

    public String getUserName() {
        return UserName;
    }

    public String getUserId(){return UserId;}

    public String getUserTaxPayerId() { return UserTaxPayerId; }

    public String getUserPhotoURL() {
        return UserPhotoURL;
    }

    public String getPaymentTypeList(){return PaymentTypeList;}


    // ----------------------------------
    // Setters
    // ----------------------------------
    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    /*
    public void setPayments(String payments) {
        Payments = payments;
    }
    */

    public void setUserName(String userName) { UserName = userName; }

    public void setUserId(String userId) { UserId = userId; }

    public void setUserPhotoURL(String userPhotoURL) { UserPhotoURL = userPhotoURL; }

    public void setUserTaxPayerId(String userTaxPayerId) { UserTaxPayerId = userTaxPayerId; }

    public void  setPaymentTypeList(String paymentTypeList){PaymentTypeList = paymentTypeList;}

}
