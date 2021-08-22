package com.example.santoriniapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.santoriniapp.entity.PaymentType;

import java.util.List;

@Dao
public interface PaymentTypeDAO
{
    @Insert
    Long insertPaymentType(PaymentType paymentType);


    @Query("SELECT * FROM paymenttype_table")
    LiveData<List<PaymentType>> getAllPaymentTypes();

    @Query("SELECT * FROM paymenttype_table")
    List<PaymentType> getAllPaymentTypeList();

    @Query("SELECT paymenttypedescription FROM paymenttype_table WHERE paymenttypecode = :paymentTypeCode")
    String getPaymentTypeDescription(String paymentTypeCode);

    @Update
    void updatePaymentType(PaymentType paymentType);


    @Delete
    void deletePaymentType(PaymentType paymentType);

    @Query("DELETE FROM paymenttype_table")
    void deleteAllPaymentTypes();
}
