package com.example.santoriniapp.repository;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.santoriniapp.dao.PaymentTypeDAO;
import com.example.santoriniapp.database.UrbanizationDatabase;
import com.example.santoriniapp.entity.PaymentType;
import com.example.santoriniapp.utils.UrbanizationGlobalUtils;

import java.util.List;

public class PaymentTypeRepository
{
    private PaymentTypeDAO mPaymentTypeDAO;
    private LiveData<List<PaymentType>> mAllPaymentTypes;
    private LiveData<PaymentType> mPaymentType;

    public PaymentTypeRepository(Application application) {
        UrbanizationDatabase db = UrbanizationDatabase.getDatabase(application);
        mPaymentTypeDAO = db.paymentTypeDAO();
        mAllPaymentTypes = mPaymentTypeDAO.getAllPaymentTypes();
    }

    public PaymentTypeRepository()
    {
        Context context = UrbanizationGlobalUtils.getInstance();
        UrbanizationDatabase db = UrbanizationDatabase.getDatabase(context);
        mPaymentTypeDAO = db.paymentTypeDAO();
    }

    public LiveData<List<PaymentType>> getAllPaymentTypes() {
        return mAllPaymentTypes;
    }

    public List<PaymentType> getAllPaymentTypeList() {
        return mPaymentTypeDAO.getAllPaymentTypeList();
    }

    public void insertPaymentTypeToDB (PaymentType payment) {
        new insertPaymentTypeAsyncTask(mPaymentTypeDAO).execute(payment);
    }

    public void deletePaymentTypessFromDB()
    {
        new deleteAllPaymentsAsyncTask(mPaymentTypeDAO).execute();
    }

    private static class insertPaymentTypeAsyncTask extends AsyncTask<PaymentType, Void, Void> {

        private PaymentTypeDAO mPaymentTypeAsyncTaskDao;

        insertPaymentTypeAsyncTask(PaymentTypeDAO dao) {
            mPaymentTypeAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final PaymentType... params) {
            mPaymentTypeAsyncTaskDao.insertPaymentType(params[0]);
            return null;
        }
    }

    private static class deleteAllPaymentsAsyncTask extends AsyncTask<Void,Void,Void>
    {
        private PaymentTypeDAO mPaymentAsyncTaskDao;

        deleteAllPaymentsAsyncTask(PaymentTypeDAO dao) {
            mPaymentAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mPaymentAsyncTaskDao.deleteAllPaymentTypes();
            return null;
        }
    }

}
