package com.example.santoriniapp.dao.daoutils;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.santoriniapp.entity.Payment;
import com.example.santoriniapp.repository.LoginRepository;
import com.example.santoriniapp.repository.PaymentRepository;
import com.example.santoriniapp.utils.DateConverter;
import com.example.santoriniapp.utils.DateFunctions;
import com.example.santoriniapp.utils.NumericFunctions;
import com.example.santoriniapp.utils.StringFunctions;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class DAOPaymentViewModel extends AndroidViewModel {

    private PaymentRepository mRepository;
    private LiveData<List<Payment>> mAllPayments;

    public DAOPaymentViewModel (Application application) {
        super(application);
        mRepository = new PaymentRepository(application);
        mAllPayments = mRepository.getAllPayments();
    }

    public LiveData<List<Payment>> getAllPayments() { return mAllPayments; }

    public LiveData<Payment> getPayment(Date paymentDate)
    {
        return mRepository.getPayment(paymentDate);
    }

    public List<Payment> getPaymentsByMonth(String month)
    {
        return mRepository.getAllPaymentByMonth(month);
    }

    public void insertPayment(Payment payment) { mRepository.insertPaymentToDB(payment); }

    public void insertDummyPayments()
    {
        //Delete all login records
        mRepository.deletePaymentsFromDB();

        //Insert record
        Date date;
        Long dateTime;

        Long dateNow = DateFunctions.today().getTime();

        //Enero
        date = DateConverter.StringToDateTime("01-01-2021 10:20:20");
        dateTime = date.getTime();
        mRepository.insertPaymentToDB(new Payment("1",dateTime,1,1,100,"A","Comentario 1",dateNow));

        date = DateConverter.StringToDateTime("15-01-2021 10:20:20");
        dateTime = date.getTime();
        mRepository.insertPaymentToDB(new Payment("1",dateTime,1,1,100,"A","Comentario 1",dateNow));

        //Febrero
        date = DateConverter.StringToDateTime("02-02-2021 10:20:20");
        dateTime = date.getTime();
        mRepository.insertPaymentToDB(new Payment("1",dateTime,2,2,200,"P","Comentario 2",dateNow));

        date = DateConverter.StringToDateTime("16-02-2021 10:20:20");
        dateTime = date.getTime();
        mRepository.insertPaymentToDB(new Payment("1",dateTime,3,3,300,"P","Comentario 3",dateNow));

        //Marzo
        date = DateConverter.StringToDateTime("03-03-2021 10:20:20");
        dateTime = date.getTime();
        mRepository.insertPaymentToDB(new Payment("1",dateTime,3,3,300,"X","Comentario 3",dateNow));

        date = DateConverter.StringToDateTime("16-03-2021 10:20:20");
        dateTime = date.getTime();
        mRepository.insertPaymentToDB(new Payment("1",dateTime,3,3,300,"X","Comentario 3",dateNow));

        //Agosto
        date = DateConverter.StringToDateTime("03-08-2021 10:20:20");
        dateTime = date.getTime();
        mRepository.insertPaymentToDB(new Payment("1",dateTime,3,3,300,"X","Comentario 3",dateNow));

        date = DateConverter.StringToDateTime("16-08-2021 10:20:20");
        dateTime = date.getTime();
        mRepository.insertPaymentToDB(new Payment("1",dateTime,3,3,300,"X","Comentario 3",dateNow));

    }
}

