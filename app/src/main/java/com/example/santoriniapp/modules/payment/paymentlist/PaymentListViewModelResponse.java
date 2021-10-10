package com.example.santoriniapp.modules.payment.paymentlist;

import java.util.ArrayList;
import java.util.List;

public class PaymentListViewModelResponse
{
    // Error Messages.
    public String errorMessage;
    public Boolean isLoading  ;
    public List<PaymentItem> paymentList;
    public int currentTimeSpinnerPosition;
    public ArrayList<PaymentDateRowSpinnerItem> paymentListSpinnerList;
    public boolean isReload;
    public boolean isDownloadingFromWS;
    public String paymentDateRequestCode;
    public String userName;

    public PaymentListViewModelResponse() {
        this.errorMessage   = "";
        this.isLoading      = false;
        this.paymentList = new ArrayList<>();
        this.currentTimeSpinnerPosition = 0;
        this.paymentListSpinnerList=new ArrayList<>();
        this.isReload = false;
        this.paymentDateRequestCode = "";
        this.userName = "";
        this.isDownloadingFromWS = false;
    }
    //public String totalCustomer(){ return itemSummary.totalCustomerCount+""; }

    //public String totalPerformedActivities(){return performedActivities()+"";}
    //public int performedActivities(){ return itemSummary.ordersCount+itemSummary.paymentsCount+itemSummary.quotesCount+itemSummary.returnRequestCount+itemSummary.visitsCount; }
    public boolean showNoContentFound(){
        return  !showContentFound()&&!isLoading;
    }
    public boolean showContentFound(){
        return  (paymentList.size() >0)?true:false;
    }

    public String userNameDescription()
    {
        return userName;
    }

    /*
    public String showMessage(){
        String message ="";
        if (paymentList.size()>0){
            PaymentDateRowSpinnerItem itemCurrentSpinner = paymentListSpinnerList.get(this.currentTimeSpinnerPosition );
            message = "No se han encontrado gestiones para el rango de fecha escogido ("+itemCurrentSpinner.dateTitle()+": "+itemCurrentSpinner.dateName()+")";

        }
        return (!this.errorMessage.trim().isEmpty())?this.errorMessage:message;
    }

     */

    public boolean showAddPaymentButton()
    {
        return !isLoading;
    }
}
