package com.example.santoriniapp.modules.login;

import android.content.Context;
import android.util.Log;
import com.example.santoriniapp.entity.PaymentType;
import com.example.santoriniapp.repository.LoginRepository;
import com.example.santoriniapp.repository.PaymentTypeRepository;
import com.example.santoriniapp.retrofit.ApiInterface;
import com.example.santoriniapp.retrofit.ServiceGenerator;
import com.example.santoriniapp.retrofit.dtos.loginDtos.LoginRequest;
import com.example.santoriniapp.retrofit.dtos.loginDtos.LoginResponse;
import com.example.santoriniapp.retrofit.dtos.syncDtos.PaymentTypeListSDT;
import com.example.santoriniapp.utils.DateFunctions;
import com.example.santoriniapp.utils.UrbanizationConstants;
import com.example.santoriniapp.utils.UrbanizationGlobalUtils;
import com.example.santoriniapp.utils.UrbanizationSessionUtils;
import com.example.santoriniapp.utils.UrbanizationUtils;
import com.google.gson.Gson;
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
        Log.d("LOG_TAG","REQUEST=>"+new Gson().toJson(request));

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

            String userId         = wsResponse.getUserId().trim();
            String userName       = wsResponse.getUserName().trim();
            String userTaxPayerId = wsResponse.getUserTaxPayerId().trim();
            String userPhotoURL   = wsResponse.getUserPhotoURL().trim();

            //  --------------------------
            //  Setting login was correct
            //  --------------------------

            mResponse.errorMessage = "";
            UrbanizationSessionUtils.setLoggedIn(context,true);
            UrbanizationSessionUtils.setLoggedUser(context,userId);
            UrbanizationSessionUtils.setLoggedLogin(context,userLogin);
            UrbanizationSessionUtils.setLoggedPassword(context,passwordLogin);


            //Delete login table then save the data
            loginRepository.deleteLoginsFromDB();

            //Save User in DB
            loginRepository.insertLoginToDB(LoginUtils.loginToSave(userId,userLogin,passwordLogin,userName,userTaxPayerId,userPhotoURL));


            //Set PaymentType List information
            ArrayList<PaymentTypeListSDT> paymentTypeListCollectionSDT = (ArrayList<PaymentTypeListSDT>)
                    UrbanizationUtils.fromJson(wsResponse.getPaymentTypeList(),new TypeToken<ArrayList<PaymentTypeListSDT>>(){}.getType());

            //Delete paymenttype information
            PaymentTypeRepository paymentTypeRepository = new PaymentTypeRepository();
            paymentTypeRepository.deletePaymentTypessFromDB();

            Long dateNow = DateFunctions.today().getTime();

            //Save all paymentTypes
            for(PaymentTypeListSDT paymentType : paymentTypeListCollectionSDT)
            {
                //Save paymenttype in db
                paymentTypeRepository.insertPaymentTypeToDB(
                        new PaymentType(paymentType.getPaymentTypeCode(),
                                paymentType.getPaymentTypeName(),
                                UrbanizationConstants.PAYMENTTYPESTATUS_ACTIVE,
                                paymentType.getPaymentTypeRequiresPhoto() == 1,dateNow));
            }

            return  mResponse;


        }catch (Exception e) {
            e.printStackTrace();
            mResponse.errorMessage = "No se pudo contactar Servidor. Por favor intentar nuevamente.";
            return  mResponse;
        }
    }


}