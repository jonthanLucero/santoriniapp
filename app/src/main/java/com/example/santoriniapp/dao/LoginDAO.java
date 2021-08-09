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


    @Update
    void updateLogin(Login login);


    @Delete
    void deleteLogin(Login login);

    @Query("DELETE FROM login_table")
    void deleteAllLogin();
}
