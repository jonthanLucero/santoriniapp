package com.example.santoriniapp.dao.daoutils;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.santoriniapp.entity.Login;
import com.example.santoriniapp.repository.LoginRepository;

import java.util.List;

public class DAOLoginViewModel extends AndroidViewModel {

    private LoginRepository mRepository;
    private LiveData<List<Login>> mAllLogins;

    public DAOLoginViewModel (Application application) {
        super(application);
        mRepository = new LoginRepository(application);
        mAllLogins = mRepository.getAllLogins();
    }

    public LiveData<List<Login>> getAllLogins() { return mAllLogins; }

    public LiveData<Login> getLogin(String userId)
    {
        return mRepository.getLogin(userId);
    }

    public String getLoginName(String userId)
    {
        return mRepository.getLoginName(userId);
    }

    public void insertLogin(Login login) { mRepository.insertLoginToDB(login); }

    public void insertDummyLogins()
    {
        //Delete all login records
        mRepository.deleteLoginsFromDB();

        //Insert record
        mRepository.insertLoginToDB(new Login("1","jlucero","123","A","Jonathan Lucero","111",""));
        mRepository.insertLoginToDB(new Login("2","jlopez","123","A","Jose Lopez","222",""));
        mRepository.insertLoginToDB(new Login("3","jlana","123","A","Juan Lana ","333",""));
    }
}
