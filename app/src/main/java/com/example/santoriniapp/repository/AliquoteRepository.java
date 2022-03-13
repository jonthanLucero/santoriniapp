package com.example.santoriniapp.repository;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import com.example.santoriniapp.dao.AliquoteDAO;
import com.example.santoriniapp.database.UrbanizationDatabase;
import com.example.santoriniapp.entity.Aliquote;
import com.example.santoriniapp.utils.UrbanizationGlobalUtils;
import com.example.santoriniapp.utils.UrbanizationSessionUtils;

import java.util.List;

public class AliquoteRepository
{
    private AliquoteDAO mAliquoteDAO;
    private LiveData<List<Aliquote>> mAllAliquotes;
    private LiveData<Aliquote> mPayment;

    public AliquoteRepository(Application application) {
        UrbanizationDatabase db = UrbanizationDatabase.getDatabase(application);
        mAliquoteDAO = db.aliquoteDAO();
        String userId = UrbanizationSessionUtils.getLoggedUser(application);
        mAllAliquotes = mAliquoteDAO.getAllAliquotes(userId);
    }

    public AliquoteRepository()
    {
        Context context = UrbanizationGlobalUtils.getInstance();
        UrbanizationDatabase db = UrbanizationDatabase.getDatabase(context);
        mAliquoteDAO = db.aliquoteDAO();
    }

    public LiveData<List<Aliquote>> getAllAliquotes() {
        return mAllAliquotes;
    }

    public List<Aliquote> getAllAliquoteList(String userId) {
        return mAliquoteDAO.getAllAliquoteList(userId);
    }

    public void insertAliquoteToDB (Aliquote aliquote) {
        new insertAliquoteAsyncTask(mAliquoteDAO).execute(aliquote);
    }

    public void updateAliquoteToDB(Aliquote aliquote)
    {
        new updateAliquoteAsyncTask(mAliquoteDAO).execute(aliquote);
    }

    public void deleteAliquotesFromDB()
    {
        new deleteAllAliquotesAsyncTask(mAliquoteDAO).execute();
    }

    public Aliquote getAliquote(String userId,int aliquoteyear,String aliquotemonthcode)
    {
        return mAliquoteDAO.getAliquote(userId,aliquoteyear,aliquotemonthcode);
    }

    public void markAllAliquotesAsDeleted(String userId)
    {
        mAliquoteDAO.markAllAliquoteAsDeleted(userId);
    }

    private static class insertAliquoteAsyncTask extends AsyncTask<Aliquote, Void, Void> {

        private AliquoteDAO mAliquoteAsyncTaskDao;

        insertAliquoteAsyncTask(AliquoteDAO dao) {
            mAliquoteAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Aliquote... params) {
            mAliquoteAsyncTaskDao.insertAliquote(params[0]);
            return null;
        }
    }

    private static class updateAliquoteAsyncTask extends AsyncTask<Aliquote,Void,Void>
    {

        private AliquoteDAO mAliquoteAsyncTaskDao;

        updateAliquoteAsyncTask(AliquoteDAO dao)
        {
            mAliquoteAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Aliquote... payments)
        {
            mAliquoteAsyncTaskDao.updateAliquote(payments[0]);
            return null;
        }
    }

    private static class deleteAllAliquotesAsyncTask extends AsyncTask<Void,Void,Void>
    {
        private AliquoteDAO mAliquoteAsyncTaskDao;

        deleteAllAliquotesAsyncTask(AliquoteDAO dao) {
            mAliquoteAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAliquoteAsyncTaskDao.deleteAllAliquotes();
            return null;
        }
    }

}
