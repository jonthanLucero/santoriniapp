package com.example.santoriniapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import com.example.santoriniapp.entity.Payment;
import com.example.santoriniapp.entity.PaymentPhoto;
import com.example.santoriniapp.utils.DateConverter;

import java.util.Date;
import java.util.List;

@Dao
public interface PaymentPhotoDAO
{

    @Insert
    Long insertPaymentPhoto(PaymentPhoto paymentPhoto);


    @Query("SELECT * FROM payment_photo ORDER BY paymentdate desc")
    LiveData<List<PaymentPhoto>> getAllPaymentPhotos();

    @Query("SELECT * FROM payment_photo ORDER BY paymentdate desc")
    List<PaymentPhoto> getAllPaymentPhotoList();


    @TypeConverters(DateConverter.class)
    @Query("SELECT * FROM payment_photo WHERE paymentdate =:PaymentDate")
    List<PaymentPhoto> getPaymentPhotoList(Date PaymentDate);

    @TypeConverters(DateConverter.class)
    @Query("SELECT * FROM payment_photo WHERE paymentdate =:PaymentDate")
    PaymentPhoto getPaymentPhoto(Date PaymentDate);


    @Update
    void updatePaymentPhoto(PaymentPhoto paymentPhoto);

    @TypeConverters(DateConverter.class)
    @Query("DELETE FROM payment_photo WHERE paymentdate =:paymentDate")
    void deletePaymentPhotosOfPayment(Date paymentDate);


    @Delete
    void deletePaymentPhoto(PaymentPhoto paymentPhoto);

    @Query("DELETE FROM payment_photo")
    void deleteAllPaymentPhotos();
}
