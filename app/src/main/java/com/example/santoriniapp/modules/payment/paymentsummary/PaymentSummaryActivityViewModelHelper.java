package com.example.santoriniapp.modules.payment.paymentsummary;

import android.content.Context;
import android.util.Log;

import com.example.santoriniapp.entity.Payment;
import com.example.santoriniapp.modules.payment.paymentlist.PaymentItem;
import com.example.santoriniapp.repository.LoginRepository;
import com.example.santoriniapp.repository.PaymentRepository;
import com.example.santoriniapp.utils.StringFunctions;
import com.example.santoriniapp.utils.UrbanizationConstants;
import com.example.santoriniapp.utils.UrbanizationGlobalUtils;
import com.example.santoriniapp.utils.UrbanizationUtils;

import java.util.ArrayList;
import java.util.List;

public class PaymentSummaryActivityViewModelHelper
{
    private static final String TAG =PaymentSummaryActivityViewModelHelper.class.getName() ;

    public static  PaymentSummaryActivityViewModelResponse getPaymentSummaryList(String userId){

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

        PaymentRepository paymentRepository = new PaymentRepository();

        //Check if there was paid the month according to payments done in the app
        ArrayList<PaymentSummaryItem> paymentList = generateAllYearPayments();
        PaymentSummaryItem item;
        String monthCode;
        double summaryAmountTotal,paymentAmountTotal;
        for(int i = 0;i < paymentList.size();i++)
        {
            item = paymentList.get(i);
            monthCode= item.paymentMonthCodeToPay;
            summaryAmountTotal = item.paymentPendingTotal;
            paymentAmountTotal = paymentRepository.getAllPaymentTotalFromMonthCode(monthCode);
            paymentList.get(i).paymentSentTotal = paymentAmountTotal;
            /*
            if(paymentAmountTotal >= summaryAmountTotal)
                paymentList.get(i).paymentMonthStatus = "S";
                ++
             */
        }
        response.paymentList = paymentList;
        return  response;

    }

    public static ArrayList<PaymentSummaryItem> generateAllYearPayments()
    {
        ArrayList<PaymentSummaryItem> paymentSummaryList = new ArrayList<>();
        PaymentSummaryItem item;
        String monthCode;
        for(int m = 1; m <= 12 ; m++)
        {
            monthCode = m < 10 ? "0"+m : ""+m;
            item = new PaymentSummaryItem();
            item.paymentMonthCodeToPay  = monthCode;
            item.paymentMonthNameToPay  = UrbanizationUtils.getMonthName(monthCode);
            item.paymentMonthStatus     = "P";
            item.paymentPendingTotal    = UrbanizationConstants.paymentMonthQuote;
            item.paymentSentTotal       = 0;
            item.showPaymentAction      = false;
            paymentSummaryList.add(item);
        }
        return paymentSummaryList;
    }
}
