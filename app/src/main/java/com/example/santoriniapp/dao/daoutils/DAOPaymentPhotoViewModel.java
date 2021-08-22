package com.example.santoriniapp.dao.daoutils;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.santoriniapp.entity.Payment;
import com.example.santoriniapp.entity.PaymentPhoto;
import com.example.santoriniapp.repository.PaymentPhotoRepository;
import com.example.santoriniapp.repository.PaymentRepository;

import java.util.List;

public class DAOPaymentPhotoViewModel extends AndroidViewModel
{
    private PaymentPhotoRepository mRepository;
    private LiveData<List<PaymentPhoto>> mAllPaymentPhotos;

    public DAOPaymentPhotoViewModel (Application application) {
        super(application);
        mRepository = new PaymentPhotoRepository(application);
        mAllPaymentPhotos = mRepository.getAllPaymentPhotos();
    }

    public LiveData<List<PaymentPhoto>> getAllPaymentPhotos() { return mAllPaymentPhotos; }

    public void insertPaymentPhoto(PaymentPhoto paymentPhoto) { mRepository.insertPaymentPhotoToDB(paymentPhoto); }
}



