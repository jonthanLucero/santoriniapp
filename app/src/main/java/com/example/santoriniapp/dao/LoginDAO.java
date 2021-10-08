package com.example.santoriniapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.santoriniapp.entity.Login;

import java.util.List;

@Dao
public interface LoginDAO
{
    @Insert
    Long insertLogin(Login login);


    @Query("SELECT * FROM login_table ORDER BY usermodifiedon desc")
    LiveData<List<Login>> getAllLogins();


    @Query("SELECT * FROM login_table WHERE userlogin =:UserLogin")
    LiveData<Login> getLogin(String UserLogin);

    @Query("SELECT username FROM login_table WHERE userid = :userId")
    String getUserName(String userId);

    @Query("SELECT usertaxpayerid FROM login_table WHERE userid = :userId")
    String getUserTaxPayerId(String userId);

    @Query("SELECT username FROM login_table limit 1")
    String getLoginUserName();

    @Query("SELECT usertaxpayerid FROM login_table limit 1")
    String getLoginUserTaxPayerId();


    @Update
    void updateLogin(Login login);


    @Delete
    void deleteLogin(Login login);

    @Query("DELETE FROM login_table")
    void deleteAllLogin();
}
