package com.example.santoriniapp.modules.payment.paymentlist;

import android.content.Context;
import android.util.Log;
import com.example.santoriniapp.entity.Payment;
import com.example.santoriniapp.repository.LoginRepository;
import com.example.santoriniapp.repository.PaymentRepository;
import com.example.santoriniapp.retrofit.ApiInterface;
import com.example.santoriniapp.retrofit.ServiceGenerator;
import com.example.santoriniapp.retrofit.dtos.paymentDtos.PaymentListSDT;
import com.example.santoriniapp.retrofit.dtos.paymentDtos.PaymentRequest;
import com.example.santoriniapp.retrofit.dtos.paymentListDtos.PaymentListRequest;
import com.example.santoriniapp.retrofit.dtos.paymentListDtos.PaymentListResponse;
import com.example.santoriniapp.utils.DateFunctions;
import com.example.santoriniapp.utils.NumericFunctions;
import com.example.santoriniapp.utils.PaymentUtils;
import com.example.santoriniapp.utils.StringFunctions;
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
import rx.Observable;
import rx.functions.Func1;

public class PaymentListViewModelHelper
{
    private static final String TAG =PaymentListViewModelHelper.class.getName() ;

    public static  PaymentListViewModelResponse getPaymentListHeaderAndDetail(String userId,String requestCode, int selectedPositionSpinner,boolean downloadFromWS){

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

        if(downloadFromWS)
            response.errorMessage = downloadPaymentsFromWS(requestCode);

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

    public static String downloadPaymentsFromWS(String requestCode) {

        String errorMessage;

        // Getting Context.
        final Context context = UrbanizationGlobalUtils.getInstance();

        // -----------------------------------
        // Set WS Request.
        // -----------------------------------
        PaymentListRequest request = new PaymentListRequest();
        request.setUserLogin(UrbanizationSessionUtils.getLoggedLogin(context));
        request.setUserPassword(UrbanizationSessionUtils.getLoggedPassword(context));
        request.setPaymentMonth(requestCode);

        try {
            //  -----------------------------------------------------------------------------------
            // Calling WS.
            //  -----------------------------------------------------------------------------------
            ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
            Call<PaymentListResponse> call = apiInterface.syncPaymentInformationWS(request);

            //  -----------------------------------------------------------------------------------
            // Getting response.
            //  -----------------------------------------------------------------------------------
            PaymentListResponse wsResponse = call.execute().body();

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
            ArrayList<PaymentListSDT> paymentListCollectionSDT = (ArrayList<PaymentListSDT>)
                    UrbanizationUtils.fromJson(wsResponse.getPayments(),new TypeToken<ArrayList<PaymentListSDT>>(){}.getType());

            errorMessage = "";


            //Check if the payment is existent in the DB
            //if it does then update the payment, then create the payment
            PaymentRepository paymentRepository = new PaymentRepository();

            Long dateNow = DateFunctions.today().getTime();
            int userId = Integer.parseInt(UrbanizationSessionUtils.getLoggedUser(context));
            Payment paymentDB;

            //For each Payments
            for(PaymentListSDT payment : paymentListCollectionSDT)
            {
                Log.d("LOG","Pago=>"+new Gson().toJson(payment));
                //Check if the paymentDate exists
                Date paymentDate = DateFunctions.stringToDateTime(payment.getPaymentDate());
                long paymentDateLong = NumericFunctions.toLong(paymentDate);

                String paymentMonth = payment.getPaymentMonth();
                String paymentTypeCode = payment.getPaymentTypeCode();
                int paymentNumber = payment.getPaymentNumber();
                int paymentReceiptNumber = payment.getPaymentReceiptNumber();
                double paymentAmount = payment.getPaymentAmount();
                String paymentStatus = payment.getPaymentStatus();
                String paymentMemo = payment.getPaymentMemo();

                paymentDB = paymentRepository.getPayment(paymentDate);

                boolean pagoExiste = paymentDB != null && paymentDB.getPaymentdate() > 0;

                //Se actualiza el pago
                if(pagoExiste)
                {
                    //Si el estado es diferente de enviado entonces actualizar el pago
                    //con su respectivo comentario
                    if(!paymentStatus.equalsIgnoreCase(UrbanizationConstants.PAYMENT_PENDING))
                    {
                        paymentDB.setPaymentstatus(paymentStatus);
                        if(!paymentMemo.trim().isEmpty())
                            paymentDB.setPaymentmemo(paymentMemo);
                        paymentRepository.updatePaymentToDB(paymentDB);
                    }
                }
                else
                {
                    paymentRepository.insertPaymentToDB(new Payment(String.valueOf(userId),
                                                                    paymentDateLong,
                                                                    paymentMonth,
                                                                    paymentTypeCode,
                                                                    paymentNumber,
                                                                    paymentReceiptNumber,
                                                                    paymentAmount,
                                                                    paymentStatus.equalsIgnoreCase(UrbanizationConstants.PAYMENT_PENDING)
                                                                            ? UrbanizationConstants.PAYMENT_SENT : paymentStatus,
                                                                    paymentMemo,
                                                                    dateNow));
                }
            }
            return  errorMessage;

        }catch (Exception e) {
            e.printStackTrace();
            errorMessage = "No se pudo contactar Servidor. Por favor intentar nuevamente.";
            return  errorMessage;
        }
    }
}
