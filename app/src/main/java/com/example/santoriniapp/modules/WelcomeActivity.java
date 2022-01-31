package com.example.santoriniapp.modules;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.santoriniapp.R;
import com.example.santoriniapp.dao.daoutils.DAOLoginViewModel;
import com.example.santoriniapp.dao.daoutils.DAOPaymentTypeViewModel;
import com.example.santoriniapp.dao.daoutils.DAOPaymentViewModel;
import com.example.santoriniapp.databinding.ActivityWelcomeBinding;
import com.example.santoriniapp.entity.Login;
import com.example.santoriniapp.entity.PaymentType;
import com.example.santoriniapp.modules.dashboard.DashboardMenuActivity;
import com.example.santoriniapp.modules.login.LoginActivity;
import com.example.santoriniapp.modules.payment.paymentlist.PaymentListActivity;
import com.example.santoriniapp.modules.payment.paymentsummary.PaymentSummaryActivity;

import java.util.List;

public class WelcomeActivity extends AppCompatActivity
{

    ActivityWelcomeBinding mBinding;

    private DAOLoginViewModel mLoginViewModel;
    private DAOPaymentViewModel mPaymentViewModel;
    private DAOPaymentTypeViewModel mPaymentTypeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);

        // Get a new or existing ViewModel from the ViewModelProvider.
        mLoginViewModel = ViewModelProviders.of(this).get(DAOLoginViewModel.class);

        mPaymentViewModel = ViewModelProviders.of(this).get(DAOPaymentViewModel.class);

        mPaymentTypeViewModel = ViewModelProviders.of(this).get(DAOPaymentTypeViewModel.class);

        mLoginViewModel.insertDummyLogins();

        mPaymentTypeViewModel.insertDummyPaymentTypes();

        mLoginViewModel.getAllLogins().observe(this, new Observer<List<Login>>() {
            @Override
            public void onChanged(@Nullable final List<Login> logins) {
                for(Login l : logins)
                    Log.d("LOG_TAG","LOGINS User=>"+l.getUserlogin()+", pwd=>"+l.getUserpassword());
            }
        });

        mPaymentTypeViewModel.getAllPaymentTypes().observe(this, new Observer<List<PaymentType>>() {
            @Override
            public void onChanged(@Nullable final List<PaymentType> paymentTypes) {
                for(PaymentType p : paymentTypes)
                    Log.d("LOG_TAG","PAYMENT TYPES code=>"+p.getPaymenttypecode()+", descripcion=>"+p.getPaymenttypedescription());
            }
        });

        mBinding.actionMenuDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(DashboardMenuActivity.launchIntent(getApplicationContext(),"1"));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        });

        mBinding.actionLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(LoginActivity.launchIntent(getApplicationContext()));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        });

        mBinding.actionPaymentlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(PaymentListActivity.launchIntent(getApplicationContext(),"1"));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        });

        mBinding.actionPaymentlistPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(PaymentSummaryActivity.launchIntent(getApplicationContext(),"1"));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
            }
        });

    }
}
