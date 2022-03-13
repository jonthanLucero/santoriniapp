package com.example.santoriniapp.repository;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import com.example.santoriniapp.dao.PaymentDAO;
import com.example.santoriniapp.database.UrbanizationDatabase;
import com.example.santoriniapp.entity.Payment;
import com.example.santoriniapp.utils.UrbanizationGlobalUtils;
import com.example.santoriniapp.utils.UrbanizationSessionUtils;

import java.util.Date;
import java.util.List;

public class PaymentRepository
{
    private PaymentDAO mPaymentDAO;
    private LiveData<List<Payment>> mAllPayments;
    private LiveData<Payment> mPayment;

    public PaymentRepository(Application application) {
        UrbanizationDatabase db = UrbanizationDatabase.getDatabase(application);
        mPaymentDAO = db.paymentDAO();
        String userId = UrbanizationSessionUtils.getLoggedUser(application);
        mAllPayments = mPaymentDAO.getAllPayments(userId);
    }

    public PaymentRepository()
    {
        Context context = UrbanizationGlobalUtils.getInstance();
        UrbanizationDatabase db = UrbanizationDatabase.getDatabase(context);
        mPaymentDAO = db.paymentDAO();
    }

    public LiveData<List<Payment>> getAllPayments() {
        return mAllPayments;
    }

    public List<Payment> getAllPaymentList(String userId) {
        return mPaymentDAO.getAllPaymentList(userId);
    }

    public void insertPaymentToDB (Payment payment) {
        new insertPaymentAsyncTask(mPaymentDAO).execute(payment);
    }

    public void updatePaymentToDB(Payment payment)
    {
        new updatePaymentAsyncTask(mPaymentDAO).execute(payment);
    }

    public void deletePaymentsFromDB()
    {
        new deleteAllPaymentsAsyncTask(mPaymentDAO).execute();
    }

    public Payment getPayment(Date paymentDate,String userId)
    {
        return mPaymentDAO.getPayment(paymentDate,userId);
    }

    public List<Payment> getAllPaymentByMonth(String month,String userId)
    {
        return mPaymentDAO.getPaymentsFromMonth(month,userId);
    }

    public List<Payment> getAllPaymentByMonthCode(String monthCode,String userId)
    {
        return mPaymentDAO.getPaymentsFromMonthCode(monthCode,userId);
    }

    public Double getAllPaymentTotalFromMonthCode(String monthCode,String userId)
    {
        return mPaymentDAO.getPaymentTotalFromMonthCode(monthCode,userId);
    }

    public List<Payment> getAllSentPaymentOnPaymentYearMonthCode(int year, String monthCode, String userId)
    {
        return mPaymentDAO.getSentPaymentsFromYearMonthCode(year,monthCode,userId);
    }

    private static class insertPaymentAsyncTask extends AsyncTask<Payment, Void, Void> {

        private PaymentDAO mPaymentAsyncTaskDao;

        insertPaymentAsyncTask(PaymentDAO dao) {
            mPaymentAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Payment... params) {
            mPaymentAsyncTaskDao.insertPayment(params[0]);
            return null;
        }
    }

    private static class updatePaymentAsyncTask extends AsyncTask<Payment,Void,Void>
    {

        private PaymentDAO mPaymentAsyncTaskDao;

        updatePaymentAsyncTask(PaymentDAO dao)
        {
            mPaymentAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Payment... payments)
        {
            mPaymentAsyncTaskDao.updatePayment(payments[0]);
            return null;
        }
    }

    private static class deleteAllPaymentsAsyncTask extends AsyncTask<Void,Void,Void>
    {
        private PaymentDAO mPaymentAsyncTaskDao;

        deleteAllPaymentsAsyncTask(PaymentDAO dao) {
            mPaymentAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mPaymentAsyncTaskDao.deleteAllPayments();
            return null;
        }
    }

}
