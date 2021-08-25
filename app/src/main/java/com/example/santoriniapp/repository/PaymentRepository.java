package com.example.santoriniapp.repository;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import com.example.santoriniapp.dao.PaymentDAO;
import com.example.santoriniapp.database.UrbanizationDatabase;
import com.example.santoriniapp.entity.Payment;
import com.example.santoriniapp.utils.UrbanizationGlobalUtils;
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
        mAllPayments = mPaymentDAO.getAllPayments();
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

    public List<Payment> getAllPaymentList() {
        return mPaymentDAO.getAllPaymentList();
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

    public Payment getPayment(Date paymentDate)
    {
        return mPaymentDAO.getPayment(paymentDate);
    }

    public List<Payment> getAllPaymentByMonth(String month)
    {
        return mPaymentDAO.getPaymentsFromMonth(month);
    }

    public List<Payment> getAllPaymentByMonthCode(String monthCode)
    {
        return mPaymentDAO.getPaymentsFromMonthCode(monthCode);
    }

    public Double getAllPaymentTotalFromMonthCode(String monthCode)
    {
        return mPaymentDAO.getPaymentTotalFromMonthCode(monthCode);
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
