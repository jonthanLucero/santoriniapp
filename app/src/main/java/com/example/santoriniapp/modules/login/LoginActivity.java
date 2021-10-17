package com.example.santoriniapp.modules.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.santoriniapp.R;
import com.example.santoriniapp.dao.daoutils.DAOLoginViewModel;
import com.example.santoriniapp.databinding.ActivityLoginBinding;
import com.example.santoriniapp.entity.Login;
import com.example.santoriniapp.modules.dashboard.DashboardMenuActivity;
import com.example.santoriniapp.utils.UrbanizationConstants;
import com.example.santoriniapp.utils.UrbanizationSessionUtils;
import com.example.santoriniapp.utils.UrbanizationUtils;
import com.jakewharton.rxbinding.view.RxView;

import java.util.List;

import rx.functions.Action1;

public class LoginActivity extends AppCompatActivity implements LifecycleOwner
{

    ActivityLoginBinding mBinding;
    public boolean firstLoad;
    LoginViewModel mViewModel;
    DAOLoginViewModel mLoginViewModel;
    LoginActivity mLoginActivity;

    String user;
    String password;
    Context context;

    public static Intent launchIntent(Context context)
    {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        mLoginActivity = this;

        mLoginViewModel = ViewModelProviders.of(mLoginActivity).get(DAOLoginViewModel.class);

        if(savedInstanceState != null && savedInstanceState.containsKey(UrbanizationConstants.FIRST_TIME))
            firstLoad = savedInstanceState.getBoolean(UrbanizationConstants.FIRST_TIME,true);


        //Setting the viewmodel
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        // ViewModel - Get Basic Panel Data (MenuItems, User Photo, Name, etc)
        mViewModel.getCurrentPanelData()
                .observe(this, new Observer<LoginViewModelResponse>() {
                    @Override
                    public void onChanged(@Nullable LoginViewModelResponse response) {

                        // Managing response.
                        if(response==null){
                            Toast.makeText(LoginActivity.this, R.string.an_error_has_ocurred_please_try_again, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Set the Binding.
                        mBinding.setViewModel(response);

                        // Show any error message.
                        if(!response.errorMessage.trim().isEmpty()) {
                            Toast.makeText(LoginActivity.this, response.errorMessage.trim(), Toast.LENGTH_SHORT).show();
                            mViewModel.resetErrorMessage();
                        }

                        // Checking if session is alive.
                        boolean isLoggedIn = UrbanizationSessionUtils.isLoggedIn(getApplicationContext());
                        if(isLoggedIn && !response.isLoading)
                        {
                            String userId = UrbanizationSessionUtils.getLoggedUser(getApplicationContext());
                            DashboardMenuActivity.launch(LoginActivity.this,userId);
                            finish();
                        }
                    }
                });

        loginActions();
    }

    public void loginActions()
    {
        mBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                user = mBinding.txtUser.getText().toString().trim();
                password = mBinding.txtPassword.getText().toString().trim();
                String errorMessage = LoginUtils.checkUserPasswordForLogin(user,password);
                if(!errorMessage.trim().isEmpty())
                    UrbanizationUtils.showMessage(LoginActivity.this,errorMessage);
                else
                {
                    /*
                    mLoginViewModel.getLogin(user).observe(mLoginActivity, new Observer<Login>() {
                        @Override
                        public void onChanged(Login login)
                        {
                            if(login == null)
                                Log.d("LOG_TAG","LOGINS Login es null");
                            else
                                Log.d("LOG_TAG","LOGINS login=>"+login.getUserlogin());
                        }
                    });
                     */
                    mViewModel.loginToWS(user,password);
                }

            }
        });

        RxView.clicks(mBinding.passwordViewtoggle).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid)
            {
                if(mBinding.txtPassword == null) return;
                if (mBinding.txtPassword.getTag().toString().equalsIgnoreCase("0")) {
                    mBinding.txtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                    mBinding.passwordViewtoggle.setImageResource(R.drawable.ic_visibility);
                    mBinding.txtPassword.setTag("1");
                } else{
                    mBinding.txtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                    mBinding.passwordViewtoggle.setImageResource(R.drawable.ic_visibility_off);
                    mBinding.txtPassword.setTag("0");
                }
            }
        });
    }
}
