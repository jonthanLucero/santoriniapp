package com.example.santoriniapp.repository;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import com.example.santoriniapp.dao.PaymentPhotoDAO;
import com.example.santoriniapp.database.UrbanizationDatabase;
import com.example.santoriniapp.entity.PaymentPhoto;
import com.example.santoriniapp.utils.UrbanizationGlobalUtils;
import java.util.Date;
import java.util.List;

public class PaymentPhotoRepository
{
    private PaymentPhotoDAO mPaymentPhotoDAO;
    private LiveData<List<PaymentPhoto>> mAllPaymentPhotos;
    private LiveData<PaymentPhoto> mPaymentPhoto;

    public PaymentPhotoRepository(Application application) {
        UrbanizationDatabase db = UrbanizationDatabase.getDatabase(application);
        mPaymentPhotoDAO = db.paymentPhotoDAO();
        mAllPaymentPhotos = mPaymentPhotoDAO.getAllPaymentPhotos();
    }

    public PaymentPhotoRepository()
    {
        Context context = UrbanizationGlobalUtils.getInstance();
        UrbanizationDatabase db = UrbanizationDatabase.getDatabase(context);
        mPaymentPhotoDAO = db.paymentPhotoDAO();
    }

    public LiveData<List<PaymentPhoto>> getAllPaymentPhotos() {
        return mAllPaymentPhotos;
    }

    public List<PaymentPhoto> getAllPaymentPhotosOfPayment(Date paymentDate) {
        return mPaymentPhotoDAO.getPaymentPhotoList(paymentDate);
    }

    public List<PaymentPhoto> getAllPaymentPhotoList() {
        return mPaymentPhotoDAO.getAllPaymentPhotoList();
    }

    public void insertPaymentPhotoToDB (PaymentPhoto paymentPhoto) {
        new insertPaymentPhotoAsyncTask(mPaymentPhotoDAO).execute(paymentPhoto);
    }

    public void deletePaymentPhotosFromDB()
    {
        new deleteAllPaymentPhotosAsyncTask(mPaymentPhotoDAO).execute();
    }

    public void deletePaymentPhotosOfPayment(Date paymentDate)
    {
        mPaymentPhotoDAO.deletePaymentPhotosOfPayment(paymentDate);
    }

    public PaymentPhoto getPaymentPhoto(Date paymentDate)
    {
        return mPaymentPhotoDAO.getPaymentPhoto(paymentDate);
    }

    private static class insertPaymentPhotoAsyncTask extends AsyncTask<PaymentPhoto, Void, Void> {

        private PaymentPhotoDAO mPaymentPhotoAsyncTaskDao;

        insertPaymentPhotoAsyncTask(PaymentPhotoDAO dao) {
            mPaymentPhotoAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final PaymentPhoto... params) {
            mPaymentPhotoAsyncTaskDao.insertPaymentPhoto(params[0]);
            return null;
        }
    }

    private static class deleteAllPaymentPhotosAsyncTask extends AsyncTask<Void,Void,Void>
    {
        private PaymentPhotoDAO mPaymentPhotoAsyncTaskDao;

        deleteAllPaymentPhotosAsyncTask(PaymentPhotoDAO dao) {
            mPaymentPhotoAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mPaymentPhotoAsyncTaskDao.deleteAllPaymentPhotos();
            return null;
        }
    }

}
