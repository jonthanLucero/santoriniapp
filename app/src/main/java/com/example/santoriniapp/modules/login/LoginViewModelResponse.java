package com.example.santoriniapp.modules.login;

public class LoginViewModelResponse
{
    public String errorMessage;
    public boolean isLoading;

    public LoginViewModelResponse(){

        this.errorMessage                           = "";
        this.isLoading                              = false;
    }

    public boolean isLoading(){
        return isLoading;
    }
}
