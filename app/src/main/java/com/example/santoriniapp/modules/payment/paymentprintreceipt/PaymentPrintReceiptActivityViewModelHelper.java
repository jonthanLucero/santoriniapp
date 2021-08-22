package com.example.santoriniapp.modules.payment.paymentprintreceipt;

import com.example.santoriniapp.entity.Payment;
import com.example.santoriniapp.repository.LoginRepository;
import com.example.santoriniapp.repository.PaymentRepository;
import com.example.santoriniapp.utils.DateFunctions;
import com.example.santoriniapp.utils.StringFunctions;

import java.util.Date;

public class PaymentPrintReceiptActivityViewModelHelper
{
    public static final String LOG_TAG = PaymentPrintReceiptActivityViewModelHelper.class.getSimpleName();

    public static PaymentPrintReceiptViewModelReponse getInitialLoadingStatus(String userId, Date mPaymentDate) {

        PaymentPrintReceiptViewModelReponse response = new PaymentPrintReceiptViewModelReponse();

        String userName;
        //Get UserName
        LoginRepository loginRepository = new LoginRepository();
        if(loginRepository.getLoginName(userId) == null)
            userName = "N/A";
        else
            userName =  loginRepository.getLoginName(userId).trim().isEmpty() ? "N/A" : loginRepository.getLoginName(userId).trim();
        response.userName = userName;

        Payment payment;
        PaymentRepository paymentRepository = new PaymentRepository();
        payment = paymentRepository.getPayment(mPaymentDate);
        String errorMessage;

        if(payment == null)
        {
            response = new PaymentPrintReceiptViewModelReponse();
            errorMessage = "Ha ocurrido un error. Por favor intentar nuevamente.";
            response.errorMessage = errorMessage;
            return response;
        }

        response.paymentDateString = DateFunctions.getDDMMMYYYYHHMMSSDateString(mPaymentDate);
        response.paymentNumberString = "# "+payment.getPaymentnumber();
        response.paymentAmountString = StringFunctions.toMoneyString(payment.getPaymentamount());
        response.paymentCommentary   = payment.getPaymentmemo().trim();
        response.errorMessage = "";
        return  response;
    }
}
