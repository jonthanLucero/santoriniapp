package com.example.santoriniapp.modules.payment.paymentlist;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.santoriniapp.dao.daoutils.DAOPaymentViewModel;
import com.example.santoriniapp.entity.Payment;
import com.example.santoriniapp.repository.PaymentRepository;
import com.example.santoriniapp.utils.DateFunctions;
import com.example.santoriniapp.utils.NumericFunctions;
import com.example.santoriniapp.utils.StringFunctions;
import com.example.santoriniapp.utils.UrbanizationGlobalUtils;
import com.example.santoriniapp.utils.UrbanizationUtils;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class PaymentListViewModelHelper
{
    private static final String TAG =PaymentListViewModelHelper.class.getName() ;

    public static  PaymentListViewModelResponse getPaymentListHeaderAndDetail(String requestCode, int selectedPositionSpinner){

        // Getting Context.
        Context context = UrbanizationGlobalUtils.getInstance();
        PaymentListViewModelResponse response= new PaymentListViewModelResponse();

        // Load Spinner
        ArrayList<PaymentDateRowSpinnerItem> paymentDateRowSpinnerItemList = new ArrayList<>();
        paymentDateRowSpinnerItemList.add(new PaymentDateRowSpinnerItem("01", "Enero")) ;
        paymentDateRowSpinnerItemList.add(new PaymentDateRowSpinnerItem("02", "Febrero"));
        paymentDateRowSpinnerItemList.add(new PaymentDateRowSpinnerItem("03", "Marzo"));
        paymentDateRowSpinnerItemList.add(new PaymentDateRowSpinnerItem("04", "Abril"));
        paymentDateRowSpinnerItemList.add(new PaymentDateRowSpinnerItem("05", "mayo"));
        paymentDateRowSpinnerItemList.add(new PaymentDateRowSpinnerItem("06", "Junio"));
        paymentDateRowSpinnerItemList.add(new PaymentDateRowSpinnerItem("07", "Julio"));
        paymentDateRowSpinnerItemList.add(new PaymentDateRowSpinnerItem("08", "Agosto"));
        paymentDateRowSpinnerItemList.add(new PaymentDateRowSpinnerItem("09", "Septiembre"));
        paymentDateRowSpinnerItemList.add(new PaymentDateRowSpinnerItem("10", "Octubre"));
        paymentDateRowSpinnerItemList.add(new PaymentDateRowSpinnerItem("11", "Noviembre"));
        paymentDateRowSpinnerItemList.add(new PaymentDateRowSpinnerItem("12", "Diciembre"));
        response.paymentListSpinnerList=paymentDateRowSpinnerItemList;


        String errorMessage="";
        if (context == null) {
            response = new PaymentListViewModelResponse();
            errorMessage = "Ha ocurrido un error. Por favor intentar nuevamente.";
            response.errorMessage =errorMessage;
            return response;
        }

        Log.i(TAG,"loadInBackground: Calling WS...");

        //TODO loading from DB
        // FIRST Load the PENDING TO SEND Payment (Ordered by Date - Most Recent to oldest.)
        List<Payment> payments;
        PaymentRepository paymentRepository = new PaymentRepository();

        payments = paymentRepository.getAllPaymentByMonth(requestCode);
        for(Payment p : payments)
            Log.d("LOG_TAG","PAYMENT ONE MONTH=>"+p.getPaymentdate());

        response.paymentList = Observable.from(payments)
                .map(new Func1<Payment, PaymentItem>() {
                    @Override
                    public PaymentItem call(Payment payment) {
                        // Add to viewModel List.
                        PaymentItem newItem = new PaymentItem();
                        newItem.paymentDateText      = DateFunctions.getDDMMMYYYYHHMMSSDateString(DateFunctions.toDate(payment.getPaymentdate()));
                        newItem.paymentStatusText    = payment.getPaymentstatus();
                        newItem.paymentTotalText     = StringFunctions.toMoneyString(payment.getPaymentamount());
                        newItem.itemCountText        = "0 Items";
                        newItem.paymentCommentary    = payment.getPaymentmemo();
                        return newItem;
                    }
                }).toList()
                .toBlocking()
                .first();

        /*
        List<Payment> payments = paymentRepository.getAllPaymentByMonth(String.valueOf(requestCode));
        for(Payment p : payments)
            Log.d("LOG_TAG","LOGIN=>"+p.getPaymentdate());
         */

        //List<Cursor> cursorList = paymentRepository.getAllPaymentByMonth(String.valueOf(requestCode));


        /*
        mViewModel.getPaymentsByMonth(String.valueOf(requestCode)).observe(activity, new Observer<List<Payment>>() {
            @Override
            public void onChanged(List<Payment> payments) {
                for(Payment p : payments)
                    Log.d("LOG_TAG","LOGINS payment=>"+p.getPaymentdate());
            }
        });

         */

        /*
        return mRepository.getLogin(userId);



        PaymentSelection pendingPaymentsSelection = new PaymentSelection().customercodeEquals(customerCode).and().paymentstatusEquals(PaymentUtils.PAYMENT_PENDING_STATUS);
        if(!textToSearch.isEmpty())
            pendingPaymentsSelection = getPaymentSelectionWithTextFilterIncluded(pendingPaymentsSelection,textToSearch);
        pendingPaymentsSelection = pendingPaymentsSelection.orderBy(PaymentContract.Indexes.INDEX_STATUSDATEINDEXDESC);

        // Adding all resultant list to the main list.
        response.itemList = Observable.from(pendingPaymentsSelection.get(context).all())
                .map(new Func1<payment_class, WWPaymentNativePanelItem>() {
                    @Override
                    public WWPaymentNativePanelItem call(payment_class paymentRecord) {
                        // Add to viewModel List.
                        WWPaymentNativePanelItem newItem = new WWPaymentNativePanelItem();
                        newItem.paymentDate                                     = paymentRecord.paymentdate;
                        newItem.paymentDetailAppliedAndUnappliedAmountCharacter = "Aplicado: "+ StringFunctions.toMoneyString(paymentRecord.paymentappliedamount) + " / No Aplicado: " + StringFunctions.toMoneyString(paymentRecord.paymentunappliedamount);
                        newItem.paymentAmount                                   = paymentRecord.paymentamount;
                        newItem.paymentNotes                                    = paymentRecord.paymentmemo;
                        newItem.paymentNumber                                   = paymentRecord.paymentnumber;
                        newItem.paymentStatus                                   = paymentRecord.paymentstatus;
                        newItem.valuesReceivedCount                             = PaymentDetailUtils.getPaymentDetailsList(context,paymentRecord.paymentdate).size();

                        return newItem;
                    }
                }).toList()
                .toBlocking()
                .first();

         */


        /*
        try {
            // -----------------------------------
            // Set WS Request.
            // -----------------------------------
            SalesPersonActivitiesAndSummaryWSRequest request= new SalesPersonActivitiesAndSummaryWSRequest();
            request.setUserId(sfaplusGlobals.getInstance().getuserid());
            request.setUserLogin(sfaplusGlobals.getInstance().getuserlogin());
            request.setUserPassword(sfaplusGlobals.getInstance().getuserpassword());
            request.setCustomerCode(customerCode);
            request.setRequestCode(requestCode);
            request.setRequiresQuotes("1");

            //  -----------------------------------------------------------------------------------
            // Calling WS.
            //  -----------------------------------------------------------------------------------
            ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
            Call<SalesPersonActivitiesAndSummaryWSResponse> call ;
            call = apiInterface.salesPersonActivitiesAndSummaryWS(request);

            //  -----------------------------------------------------------------------------------
            // Getting response.
            //  -----------------------------------------------------------------------------------
            SalesPersonActivitiesAndSummaryWSResponse serverResponse = call.execute().body();
            response.activitiesList = (ArrayList<SalesPersonActivitiesAndSummaryItemActivitySDT>) fromJson(serverResponse.getActivities(),new TypeToken<ArrayList<SalesPersonActivitiesAndSummaryItemActivitySDT>>(){}.getType());
            SalesPersonActivitiesAndSummaryItemSummarySDT itemSummary    = (SalesPersonActivitiesAndSummaryItemSummarySDT) fromJson(serverResponse.getSummary(),new TypeToken<SalesPersonActivitiesAndSummaryItemSummarySDT>(){}.getType());
            SalesPersonSummaryActivityItem newItemSummary = new SalesPersonSummaryActivityItem();
            newItemSummary.ordersTotal                = itemSummary.getOrdersTotal();
            newItemSummary.ordersCount                = itemSummary.getOrdersCount();
            newItemSummary.paymentsTotal              = itemSummary.getPaymentsTotal();
            newItemSummary.paymentsCount              = itemSummary.getPaymentsCount();
            newItemSummary.visitsCount                = itemSummary.getVisitsCount();
            newItemSummary.returnRequestTotal         = itemSummary.getReturnRequestTotal();
            newItemSummary.returnRequestCount         = itemSummary.getReturnRequestCount();
            newItemSummary.quotesTotal                = itemSummary.getQuotesTotal();
            newItemSummary.quotesCount                = itemSummary.getQuotesCount();
            newItemSummary.ordersCustomerCount        = itemSummary.getOrdersCustomerCount();
            newItemSummary.paymentsCustomerCount      = itemSummary.getPaymentsCustomerCount();
            newItemSummary.returnRequestCustomerCount = itemSummary.getReturnRequestCustomerCount();
            newItemSummary.quotesCustomerCount        = itemSummary.getQuotesCustomerCount();
            newItemSummary.visitsCustomerCount        = itemSummary.getVisitsCustomerCount();
            newItemSummary.totalCustomerCount         = itemSummary.getTotalCustomerCount();
            */
            //response.itemSummary = newItemSummary;
            //System.out.println("getActivities " +serverResponse.getActivities());
            //response.reloadFragmentSummaryList = true;
            response.reloadFragmentActivitiesList = true;
            response.currentTimeSpinnerPosition = selectedPositionSpinner == -1 ? getPaymentDateRecordPosition(paymentDateRowSpinnerItemList,requestCode) : 0;
            response.paymentDateRequestCode = requestCode;





            // If there is no response...

            /*
            if(serverResponse==null){
                response.errorMessage = "No se pudo contactar Servidor. Por favor intentar nuevamente.";
                return response;
            }
            */

        /*
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = "Existió un error al descargar las Actividades Realizadas. Por favor intente nuevamente.";
            response.errorMessage = errorMessage;
            return response;

        }

         */
        return  response;

    }
    public static Object fromJson(String jsonString, Type type) {
        return new Gson().fromJson(jsonString, type);
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
