package com.example.santoriniapp.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.santoriniapp.dao.LoginDAO;
import com.example.santoriniapp.database.UrbanizationDatabase;
import com.example.santoriniapp.entity.Login;

import java.util.List;

public class LoginRepository
{
    private LoginDAO mLoginDAO;
    private LiveData<List<Login>> mAllLogins;
    private LiveData<Login> mLogin;

    public LoginRepository(Application application) {
        UrbanizationDatabase db = UrbanizationDatabase.getDatabase(application);
        mLoginDAO = db.loginDAO();
        mAllLogins = mLoginDAO.getAllLogins();
    }

    public LiveData<List<Login>> getAllLogins() {
        return mAllLogins;
    }

    public void insertLoginToDB (Login login) {
        new insertLoginAsyncTask(mLoginDAO).execute(login);
    }

    public void deleteLoginsFromDB()
    {
        new deleteAllLoginAsyncTask(mLoginDAO).execute();
    }

    public LiveData<Login> getLogin(String userId)
    {
        return mLoginDAO.getLogin(userId);
    }

    private static class insertLoginAsyncTask extends AsyncTask<Login, Void, Void> {

        private LoginDAO mLoginAsyncTaskDao;

        insertLoginAsyncTask(LoginDAO dao) {
            mLoginAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Login... params) {
            mLoginAsyncTaskDao.insertLogin(params[0]);
            return null;
        }
    }

    private static class deleteAllLoginAsyncTask extends AsyncTask<Void,Void,Void>
    {
        private LoginDAO mLoginAsyncTaskDao;

        deleteAllLoginAsyncTask(LoginDAO dao) {
            mLoginAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mLoginAsyncTaskDao.deleteAllLogin();
            return null;
        }
    }

}
