package com.example.santoriniapp.modules.login;

import android.content.Context;

import com.example.santoriniapp.entity.Login;
import com.example.santoriniapp.repository.LoginRepository;
import com.example.santoriniapp.retrofit.ApiInterface;
import com.example.santoriniapp.retrofit.ServiceGenerator;
import com.example.santoriniapp.retrofit.dtos.loginDtos.LoginRequest;
import com.example.santoriniapp.retrofit.dtos.loginDtos.LoginResponse;
import com.example.santoriniapp.retrofit.dtos.loginDtos.PaymentDetailListSDT;
import com.example.santoriniapp.retrofit.dtos.loginDtos.PaymentListSDT;
import com.example.santoriniapp.utils.UrbanizationConstants;
import com.example.santoriniapp.utils.UrbanizationGlobalUtils;
import com.example.santoriniapp.utils.UrbanizationSessionUtils;
import com.example.santoriniapp.utils.UrbanizationUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import retrofit2.Call;

public class LoginViewModelHelper
{
    private final static String LOG_TAG = "LoginViewModelHelper";

    public static LoginViewModelResponse loginToWS(String userLogin,String passwordLogin) {

        // Getting Context.
        final Context context = UrbanizationGlobalUtils.getInstance();

        LoginRepository loginRepository = new LoginRepository();

        LoginViewModelResponse mResponse = new LoginViewModelResponse();

        // -----------------------------------
        // Set WS Request.
        // -----------------------------------
        LoginRequest request = new LoginRequest();
        request.setUserLogin(userLogin);
        request.setUserPassword(passwordLogin);


        try {
            //  -----------------------------------------------------------------------------------
            // Calling WS.
            //  -----------------------------------------------------------------------------------
            ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
            Call<LoginResponse> call = apiInterface.userLoginToWS(request);

            //  -----------------------------------------------------------------------------------
            // Getting response.
            //  -----------------------------------------------------------------------------------
            LoginResponse wsResponse = call.execute().body();

            // If there is no response...
            if(wsResponse==null) {
                mResponse.errorMessage = "\"No se pudo contactar Servidor. Por favor intentar nuevamente.";
                mResponse.isLoading = false;
                return mResponse;
            }

            // If server returned an error...

            if(!wsResponse.getErrorMessage().trim().isEmpty()) {
                mResponse.errorMessage = wsResponse.getErrorMessage().trim();
                return mResponse;
            }

            //Set PaymentHeader List
            /*
            ArrayList<PaymentListSDT> paymentListSDT = (ArrayList<PaymentListSDT>)
                    UrbanizationUtils.fromJson(wsResponse.getPayments(),new TypeToken<ArrayList<PaymentListSDT>>(){}.getType());
            */

            String userId         = wsResponse.getUserId().trim();
            String userName       = wsResponse.getUserName().trim();
            String userTaxPayerId = wsResponse.getUserTaxPayerId().trim();
            String userPhotoURL   = wsResponse.getUserPhotoURL().trim();


            //TODO Setting in DB


            //  --------------------------
            //  Setting login was correct
            //  --------------------------
            mResponse.errorMessage = "";
            UrbanizationSessionUtils.setLoggedIn(context,true);
            UrbanizationSessionUtils.setLoggedUser(context,userId);

            //Delete login table then save the data
            loginRepository.deleteLoginsFromDB();

            //Save User in DB
            loginRepository.insertLoginToDB(LoginUtils.loginToSave(userId,userLogin,passwordLogin,userName,userTaxPayerId,userPhotoURL));
            return  mResponse;


        }catch (Exception e) {
            e.printStackTrace();
            mResponse.errorMessage = "No se pudo contactar Servidor. Por favor intentar nuevamente.";
            return  mResponse;
        }
        finally {
            if(userLogin.equalsIgnoreCase(UrbanizationConstants.DEBUG_USER) &&
                passwordLogin.equalsIgnoreCase(UrbanizationConstants.DEBUG_PASSWORD))
            {
                mResponse.errorMessage = "";
                //Delete login table then save the data
                loginRepository.deleteLoginsFromDB();

                String userId         = UrbanizationConstants.DEBUG_USER_ID;
                String userName       = UrbanizationConstants.DEBUG_USER_NAME;
                String userTaxPayerId = UrbanizationConstants.DEBUG_USER_TAXPAYERID;
                String userPhotoURL   = UrbanizationConstants.DEBUG_USERPHOTOURL;

                //Save User in DB
                loginRepository.insertLoginToDB(LoginUtils.loginToSave(userId,userLogin,passwordLogin,userName,userTaxPayerId,userPhotoURL));
                mResponse.errorMessage = "";

                UrbanizationSessionUtils.setLoggedIn(context,true);
                UrbanizationSessionUtils.setLoggedUser(context,userId);

                return  mResponse;
            }
        }
    }


}