package com.example.santoriniapp.modules.aliquotelist;

import android.content.Context;
import android.util.Log;

import com.example.santoriniapp.entity.Aliquote;
import com.example.santoriniapp.entity.Payment;
import com.example.santoriniapp.repository.AliquoteRepository;
import com.example.santoriniapp.repository.LoginRepository;
import com.example.santoriniapp.repository.PaymentRepository;
import com.example.santoriniapp.retrofit.ApiInterface;
import com.example.santoriniapp.retrofit.ServiceGenerator;
import com.example.santoriniapp.retrofit.dtos.aliquoteListDtos.AliquoteListRequest;
import com.example.santoriniapp.retrofit.dtos.aliquoteListDtos.AliquoteListResponse;
import com.example.santoriniapp.retrofit.dtos.aliquoteListDtos.AliquoteListSDT;
import com.example.santoriniapp.utils.DateFunctions;
import com.example.santoriniapp.utils.UrbanizationConstants;
import com.example.santoriniapp.utils.UrbanizationGlobalUtils;
import com.example.santoriniapp.utils.UrbanizationSessionUtils;
import com.example.santoriniapp.utils.UrbanizationUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;

public class PaymentSummaryActivityViewModelHelper
{
    private static final String TAG =PaymentSummaryActivityViewModelHelper.class.getName() ;

    public static  PaymentSummaryActivityViewModelResponse getPaymentSummaryList(String userId, boolean downloadWS){

        // Getting Context.
        Context context = UrbanizationGlobalUtils.getInstance();
        PaymentSummaryActivityViewModelResponse response= new PaymentSummaryActivityViewModelResponse();

        String errorMessage;
        if (context == null) {
            response = new PaymentSummaryActivityViewModelResponse();
            errorMessage = "Ha ocurrido un error. Por favor intentar nuevamente.";
            response.errorMessage =errorMessage;
            return response;
        }

        String userName;
        LoginRepository loginRepository = new LoginRepository();
        if(loginRepository.getLoginName(userId) == null)
            userName = "N/A";
        else
            userName = loginRepository.getLoginName(userId).trim().isEmpty() ? "N/A" : loginRepository.getLoginName(userId).trim();
        response.userName = userName;

        if(downloadWS)
            response.errorMessage = downloadAliquotesFromWS();

        AliquoteRepository aliquoteRepository = new AliquoteRepository();

        //Get All aliquotes of the user
        ArrayList<PaymentSummaryItem> aliquoteSummaryList = new ArrayList<>();
        PaymentSummaryItem item;

        List<Aliquote> aliquoteList = aliquoteRepository.getAllAliquoteList(userId);

        for(Aliquote aliquote : aliquoteList)
        {
            item = new PaymentSummaryItem();
            item.paymentYearToPay      = aliquote.getAliquoteyear();
            item.paymentMonthCodeToPay = aliquote.getAliquotemonthcode();
            item.paymentMonthNameToPay = UrbanizationUtils.getMonthName(aliquote.getAliquotemonthcode()) +" - " +aliquote.getAliquoteyear();
            item.paymentMonthStatus    = checkSentAliquoteOnPayment(aliquote.getAliquoteyear(),aliquote.getAliquotemonthcode(),userId) ? UrbanizationConstants.PAYMENT_MONTH_SENT : aliquote.getAliquotestatus();
            item.paymentObservation    = "";
            item.paymentPendingTotal   = aliquote.getAliquotevalue();
            item.paymentSentTotal      = aliquote.getAliquotereceivedvalue();
            aliquoteSummaryList.add(item);
        }

        response.paymentList = aliquoteSummaryList;
        return  response;
    }

    public static String downloadAliquotesFromWS()
    {
        String errorMessage;

        // Getting Context.
        final Context context = UrbanizationGlobalUtils.getInstance();

        // -----------------------------------
        // Set WS Request.
        // -----------------------------------
        AliquoteListRequest request = new AliquoteListRequest();
        request.setUserLogin(UrbanizationSessionUtils.getLoggedLogin(context));
        request.setUserPassword(UrbanizationSessionUtils.getLoggedPassword(context));

        try {
            //  -----------------------------------------------------------------------------------
            // Calling WS.
            //  -----------------------------------------------------------------------------------
            ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
            Call<AliquoteListResponse> call = apiInterface.syncAliquoteInformationWS(request);

            //  -----------------------------------------------------------------------------------
            // Getting response.
            //  -----------------------------------------------------------------------------------
            AliquoteListResponse wsResponse = call.execute().body();

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
            ArrayList<AliquoteListSDT> aliquoteListCollectionSDT = (ArrayList<AliquoteListSDT>)
                    UrbanizationUtils.fromJson(wsResponse.getAliquoteList(),new TypeToken<ArrayList<AliquoteListSDT>>(){}.getType());

            errorMessage = "";


            //Check if the aliquote is existent in the DB
            //if it does then update the aliquote, else create the aliquote
            AliquoteRepository aliquoteRepository = new AliquoteRepository();

            Long dateNow = DateFunctions.today().getTime();
            String userId = UrbanizationSessionUtils.getLoggedUser(context);

            Aliquote aliquoteDB;
            int aliquoteYear;
            String aliquoteMonth;
            double aliquoteValue;
            double aliquoteReceivedValue;
            String aliquoteStatus;
            boolean isNewAliquote;

            //For each Payments
            for(AliquoteListSDT aliquote : aliquoteListCollectionSDT)
            {
                isNewAliquote = false;
                Log.d("LOG","Alicuota=>"+new Gson().toJson(aliquote));
                aliquoteYear             = aliquote.getAliquoteYear();
                aliquoteMonth            = aliquote.getAliquoteMonthCode();
                aliquoteValue            = aliquote.getAliquoteValue();
                aliquoteReceivedValue    = aliquote.getAliquoteReceivedValue();
                aliquoteStatus           = aliquote.getAliquoteStatus();

                //Check if the aliquote exists
                aliquoteDB = aliquoteRepository.getAliquote(userId,aliquoteYear,aliquoteMonth);

                if(aliquoteDB == null)
                {
                    aliquoteDB = new Aliquote();
                    isNewAliquote = true;
                }

                aliquoteDB.setUserid(userId);
                aliquoteDB.setAliquoteyear(aliquoteYear);
                aliquoteDB.setAliquotemonthcode(aliquoteMonth);
                aliquoteDB.setAliquotevalue(aliquoteValue);
                aliquoteDB.setAliquotereceivedvalue(aliquoteReceivedValue);
                aliquoteDB.setAliquotestatus(aliquoteStatus);
                aliquoteDB.setAliquotemodifiedon(dateNow);

                //Update aliquote else create it
                if(isNewAliquote)
                    aliquoteRepository.insertAliquoteToDB(aliquoteDB);
                else
                    aliquoteRepository.updateAliquoteToDB(aliquoteDB);
            }
            return  errorMessage;

        }catch (Exception e) {
            e.printStackTrace();
            errorMessage = "No se pudo contactar Servidor. Por favor intentar nuevamente.";
            return  errorMessage;
        }
    }

    public static boolean checkSentAliquoteOnPayment(int year, String month,String userId)
    {
        PaymentRepository paymentRepository = new PaymentRepository();
        AliquoteRepository aliquoteRepository = new AliquoteRepository();
        List<Payment> paymentList = paymentRepository.getAllSentPaymentOnPaymentYearMonthCode(year,month,userId);
        Aliquote aliquote = aliquoteRepository.getAliquote(userId,year,month);
        double paymentAmountTotal = 0;
        double aliquoteValue;
        if(paymentList.size() > 0)
        {
            for(Payment p: paymentList)
                paymentAmountTotal += p.getPaymentamount();

            if(aliquote != null)
                aliquoteValue = aliquote.getAliquotevalue();
            else
                return false;

            return paymentAmountTotal >= aliquoteValue;
        }
        return false;
    }
}
