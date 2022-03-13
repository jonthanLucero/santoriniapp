package com.example.santoriniapp.retrofit.dtos.aliquoteListDtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AliquoteListResponse
{
    @SerializedName("ErrorMessage")
    @Expose
    private String ErrorMessage;

    @SerializedName("AliquoteList")
    @Expose
    private String AliquoteList;


    // ----------------------------------
    // Getters
    // ----------------------------------
    public String getErrorMessage() {
        return ErrorMessage;
    }

    public String getAliquoteList() {
        return AliquoteList;
    }

    // ----------------------------------
    // Setters
    // ----------------------------------
    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    public void setAliquoteList(String aliquoteList) {
        AliquoteList = aliquoteList;
    }

}
