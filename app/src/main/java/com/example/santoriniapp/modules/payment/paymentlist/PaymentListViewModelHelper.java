package com.example.santoriniapp.modules.payment.paymentlist;

import android.content.Context;
import android.util.Log;
import com.example.santoriniapp.entity.Payment;
import com.example.santoriniapp.repository.LoginRepository;
import com.example.santoriniapp.repository.PaymentRepository;
import com.example.santoriniapp.utils.DateFunctions;
import com.example.santoriniapp.utils.PaymentUtils;
import com.example.santoriniapp.utils.StringFunctions;
import com.example.santoriniapp.utils.UrbanizationGlobalUtils;
import com.example.santoriniapp.utils.UrbanizationUtils;

import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.functions.Func1;

public class PaymentListViewModelHelper
{
    private static final String TAG =PaymentListViewModelHelper.class.getName() ;

    public static  PaymentListViewModelResponse getPaymentListHeaderAndDetail(String userId,String requestCode, int selectedPositionSpinner){

        // Getting Context.
        Context context = UrbanizationGlobalUtils.getInstance();
        PaymentListViewModelResponse response= new PaymentListViewModelResponse();

        ArrayList<PaymentDateRowSpinnerItem> paymentDateRowSpinnerItemList = PaymentUtils.getPaymentDateRowSpinnerList("");

        // Load Spinner
        response.paymentListSpinnerList= paymentDateRowSpinnerItemList;


        String errorMessage;
        if (context == null) {
            response = new PaymentListViewModelResponse();
            errorMessage = "Ha ocurrido un error. Por favor intentar nuevamente.";
            response.errorMessage =errorMessage;
            return response;
        }

        Log.i(TAG,"loadInBackground: Calling WS...");

        String userName;
        LoginRepository loginRepository = new LoginRepository();
        if(loginRepository.getLoginName(userId) == null)
            userName = "N/A";
        else
            userName = loginRepository.getLoginName(userId).trim().isEmpty() ? "N/A" : loginRepository.getLoginName(userId).trim();
        response.userName = userName;

        List<Payment> payments;
        PaymentRepository paymentRepository = new PaymentRepository();

        if(requestCode.equalsIgnoreCase("00"))
            payments = paymentRepository.getAllPaymentList();
        else
            payments = paymentRepository.getAllPaymentByMonthCode(requestCode);

        for(Payment p : payments)
            Log.d("LOG_TAG","PAYMENT ONE MONTH=>"+p.getPaymentdate());

        response.paymentList = Observable.from(payments)
                .map(new Func1<Payment, PaymentItem>() {
                    @Override
                    public PaymentItem call(Payment payment) {
                        // Add to viewModel List.
                        PaymentItem newItem = new PaymentItem();
                        newItem.paymentDate          = DateFunctions.toDate(payment.getPaymentdate());
                        newItem.paymentDateText      = DateFunctions.getDDMMMYYYYHHMMSSDateString(DateFunctions.toDate(payment.getPaymentdate()));
                        newItem.paymentStatusText    = payment.getPaymentstatus();
                        newItem.paymentTotalText     = StringFunctions.toMoneyString(payment.getPaymentamount());
                        newItem.paymentMonthToPay    = UrbanizationUtils.getMonthName(payment.getPaymentmonth());
                        newItem.paymentCommentary    = payment.getPaymentmemo().trim();
                        return newItem;
                    }
                }).toList()
                .toBlocking()
                .first();

            response.currentTimeSpinnerPosition = selectedPositionSpinner == -1 ? getPaymentDateRecordPosition(paymentDateRowSpinnerItemList,requestCode) : 0;
            response.paymentDateRequestCode = requestCode;

        return  response;

    }

    public static int getPaymentDateRecordPosition(ArrayList<PaymentDateRowSpinnerItem> paymentDateRowSpinnerItemList, String dateCode)
    {
        // Current PaymentList
        for (int i = 0; i < paymentDateRowSpinnerItemList.size(); i++) {
            PaymentDateRowSpinnerItem item = paymentDateRowSpinnerItemList.get(i);
            if (item.timeCode.equalsIgnoreCase(dateCode))
                return i;
        }
        return -1;
    }
}
