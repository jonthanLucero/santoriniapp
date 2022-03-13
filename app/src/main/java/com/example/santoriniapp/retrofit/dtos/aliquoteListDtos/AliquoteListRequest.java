package com.example.santoriniapp.retrofit.dtos.aliquoteListDtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AliquoteListRequest
{
    @SerializedName("UserLogin")
    @Expose
    private String UserLogin;

    @SerializedName("UserPassword")
    @Expose
    private String UserPassword;

    // ----------------------------------
    // Getters
    // ----------------------------------

    public String getUserLogin() {
        return UserLogin;
    }

    public String getUserPassword() {
        return UserPassword;
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
}