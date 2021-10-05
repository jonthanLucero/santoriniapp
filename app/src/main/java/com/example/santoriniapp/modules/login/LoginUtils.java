package com.example.santoriniapp.modules.login;

import com.example.santoriniapp.entity.Login;

public class LoginUtils
{
    public static String checkUserPasswordForLogin(String user, String password)
    {
        if(user.isEmpty() || password.isEmpty())
            return "Favor ingrese usuario y contraseña";
        return "";
    }

    public static Login loginToSave(String userId, String userLogin, String passwordLogin, String userName, String userTaxPayerId, String userPhotoURL)
    {
        return new Login(userId,userLogin,passwordLogin,"A",userName,userTaxPayerId,userPhotoURL);
    }
}
