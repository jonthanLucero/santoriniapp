package com.example.santoriniapp.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import com.example.santoriniapp.entity.Payment;
import com.example.santoriniapp.utils.DateConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Dao
public interface PaymentDAO
{
    @Insert
    Long insertPayment(Payment payment);


    @Query("SELECT * FROM payment_table ORDER BY paymentdate desc")
    LiveData<List<Payment>> getAllPayments();

    @Query("SELECT * FROM payment_table ORDER BY paymentdate desc")
    List<Payment> getAllPaymentList();


    @TypeConverters(DateConverter.class)
    @Query("SELECT * FROM payment_table WHERE paymentdate =:PaymentDate")
    LiveData<Payment> getPayment(Date PaymentDate);

    @Query("SELECT * FROM payment_table WHERE strftime('%m', paymentdate / 1000, 'unixepoch') =:month order by paymentdate desc")
    List<Payment> getPaymentsFromMonth(String month);

    @Update
    void updatePayment(Payment payment);


    @Delete
    void deletePayment(Payment payment);

    @Query("DELETE FROM payment_table")
    void deleteAllPayments();
}
