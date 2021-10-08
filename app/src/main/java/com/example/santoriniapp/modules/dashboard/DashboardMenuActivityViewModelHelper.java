package com.example.santoriniapp.modules.dashboard;

import android.content.Context;
import android.os.Bundle;
import com.example.santoriniapp.R;
import com.example.santoriniapp.entity.PaymentType;
import com.example.santoriniapp.modules.news.NewsListActivity;
import com.example.santoriniapp.modules.payment.paymentlist.PaymentListActivity;
import com.example.santoriniapp.modules.payment.paymentsummary.PaymentSummaryActivity;
import com.example.santoriniapp.modules.socialclub.SocialClubActivity;
import com.example.santoriniapp.repository.LoginRepository;
import com.example.santoriniapp.repository.PaymentTypeRepository;
import com.example.santoriniapp.retrofit.ApiInterface;
import com.example.santoriniapp.retrofit.ServiceGenerator;
import com.example.santoriniapp.retrofit.dtos.syncDtos.PaymentTypeListSDT;
import com.example.santoriniapp.retrofit.dtos.syncDtos.SyncInformationRequest;
import com.example.santoriniapp.retrofit.dtos.syncDtos.SyncInformationResponse;
import com.example.santoriniapp.utils.DateFunctions;
import com.example.santoriniapp.utils.UrbanizationConstants;
import com.example.santoriniapp.utils.UrbanizationGlobalUtils;
import com.example.santoriniapp.utils.UrbanizationSessionUtils;
import com.example.santoriniapp.utils.UrbanizationUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import retrofit2.Call;

public class DashboardMenuActivityViewModelHelper
{
    private static final String LOG_TAG = "SalesPersonPanel2Helper";

    //Set User general data
    public static DashboardMenuActivityViewModelResponse getDashboardInformation(String userId, boolean syncInformation){

        // Init Response.
        DashboardMenuActivityViewModelResponse response = new DashboardMenuActivityViewModelResponse();

        String userName,taxpayerId;

        //Get UserName
        LoginRepository loginRepository = new LoginRepository();
        if(loginRepository.getLoginName(userId)== null)
            userName = "N/A";
        else
            userName =  loginRepository.getLoginName(userId).trim().isEmpty() ? "N/A" : loginRepository.getLoginName(userId).trim();

        if(loginRepository.getLoginTaxPayerId(userId) == null)
            taxpayerId = "N/A";
        else
            taxpayerId =  loginRepository.getLoginTaxPayerId(userId).trim().isEmpty() ? "N/A" : loginRepository.getLoginTaxPayerId(userId).trim();

        if(syncInformation)
            response.errorMessage = syncData();

        response.userName = userName;
        response.taxpayerId   = taxpayerId;

        // Load the "options" list.
        response.itemList = getMenuItemList();


        // Get User Picture URL.
        //TODO VERIIFY USER FILE PHOTO
        response.userPictureUrl = "";
        if(!response.userPictureUrl.trim().isEmpty())
            response.userPictureUrl = response.userPictureUrl.contains("pedidos.inalambrik.com.ec") ? "https://".concat(response.userPictureUrl.trim()) : "http://".concat(response.userPictureUrl.trim());

        return response;
    }

    public static ArrayList<DashboardMenuItem> getMenuItemList(){

        // ------------------------------------------------------------------------------
        // Defining the Options in the Menu.
        // ------------------------------------------------------------------------------
        ArrayList<DashboardMenuItem> items = new ArrayList<>();
        DashboardMenuItem item;
        int iconDrawable;
        Class className;
        String itemText;
        Bundle parms;

        //OPTION: SINGLE PAYMENT
        iconDrawable = R.drawable.payment_individual;
        className    = PaymentListActivity.class;
        itemText     = "Pagos";
        item         = new DashboardMenuItem(iconDrawable,className,itemText);
        items.add(item);

        // OPTION: ACCOUNT STATUS
        iconDrawable        = R.drawable.account_status;
        className           = PaymentSummaryActivity.class;
        itemText            = "Estado de Cuenta";
        item                = new DashboardMenuItem(iconDrawable, className, itemText);
        items.add(item);

        // OPTION: NEWS
        iconDrawable        = R.drawable.newspaper;
        className           = NewsListActivity.class;
        itemText            = "Noticias";
        item                = new DashboardMenuItem(iconDrawable, className, itemText);
        items.add(item);

        // OPTION: SOCIAL CLUB
        iconDrawable        = R.drawable.social_club;
        className           = SocialClubActivity.class;
        itemText            = "Club Social";
        item                = new DashboardMenuItem(iconDrawable, className, itemText);
        items.add(item);

        return items;
    }

    public static String syncData() {

        String errorMessage;

        // Getting Context.
        final Context context = UrbanizationGlobalUtils.getInstance();

        // -----------------------------------
        // Set WS Request.
        // -----------------------------------
        SyncInformationRequest request = new SyncInformationRequest();
        request.setUserLogin(UrbanizationSessionUtils.getLoggedLogin(context));
        request.setUserPassword(UrbanizationSessionUtils.getLoggedPassword(context));


        try {
            //  -----------------------------------------------------------------------------------
            // Calling WS.
            //  -----------------------------------------------------------------------------------
            ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
            Call<SyncInformationResponse> call = apiInterface.syncInformationWS(request);

            //  -----------------------------------------------------------------------------------
            // Getting response.
            //  -----------------------------------------------------------------------------------
            SyncInformationResponse wsResponse = call.execute().body();

            // If there is no response...
            if(wsResponse==null) {
                errorMessage = "\"No se pudo contactar Servidor. Por favor intentar nuevamente.";
                return errorMessage;
            }

            // If server returned an error...

            if(!wsResponse.getErrorMessage().trim().isEmpty()) {
                errorMessage = wsResponse.getErrorMessage().trim();
                return errorMessage;
            }

            //Set PaymentType List information
            ArrayList<PaymentTypeListSDT> paymentTypeListCollectionSDT = (ArrayList<PaymentTypeListSDT>)
                    UrbanizationUtils.fromJson(wsResponse.getPaymentTypeList(),new TypeToken<ArrayList<PaymentTypeListSDT>>(){}.getType());

            errorMessage = "";


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
            return  errorMessage;

        }catch (Exception e) {
            e.printStackTrace();
            errorMessage = "No se pudo contactar Servidor. Por favor intentar nuevamente.";
            return  errorMessage;
        }
    }


}
