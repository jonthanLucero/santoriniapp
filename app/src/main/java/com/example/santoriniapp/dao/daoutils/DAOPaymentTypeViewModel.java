package com.example.santoriniapp.dao.daoutils;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.santoriniapp.entity.PaymentType;
import com.example.santoriniapp.repository.PaymentTypeRepository;
import com.example.santoriniapp.utils.DateFunctions;
import com.example.santoriniapp.utils.UrbanizationConstants;

import java.util.Date;
import java.util.List;

public class DAOPaymentTypeViewModel extends AndroidViewModel
{
    private PaymentTypeRepository mRepository;
    private LiveData<List<PaymentType>> mAllPaymentTypes;

    public DAOPaymentTypeViewModel (Application application) {
        super(application);
        mRepository = new PaymentTypeRepository(application);
        mAllPaymentTypes = mRepository.getAllPaymentTypes();
    }

    public LiveData<List<PaymentType>> getAllPaymentTypes() { return mAllPaymentTypes; }

    public void insertPaymentType(PaymentType payment) { mRepository.insertPaymentTypeToDB(payment); }

    public void insertDummyPaymentTypes()
    {
        //Delete all login records
        mRepository.deletePaymentTypessFromDB();

        //Insert record
        Long dateNow = DateFunctions.today().getTime();
        mRepository.insertPaymentTypeToDB(new PaymentType(UrbanizationConstants.PAYMENTTYPECODE_CASH,"Efectivo","A",true,dateNow));

        mRepository.insertPaymentTypeToDB(new PaymentType(UrbanizationConstants.PAYMENTTYPECODE_TRANSFERENCE,"Transferencia","A",true,dateNow));

        mRepository.insertPaymentTypeToDB(new PaymentType(UrbanizationConstants.PAYMENTTYPECODE_CHECK,"Cheque","A",true,dateNow));
    }
}
