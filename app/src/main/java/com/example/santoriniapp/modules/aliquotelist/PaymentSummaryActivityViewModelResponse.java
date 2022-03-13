package com.example.santoriniapp.modules.aliquotelist;

import java.util.ArrayList;
import java.util.List;

public class PaymentSummaryActivityViewModelResponse
{
    public String errorMessage;
    public String userName;
    public boolean isLoading;
    public List<PaymentSummaryItem> paymentList;
    public boolean isDownloadingFromWS;

    public PaymentSummaryActivityViewModelResponse()
    {
        this.userName = "";
        this.errorMessage = "";
        this.isLoading = false;
        this.isDownloadingFromWS = false;
        this.paymentList = new ArrayList<>();
    }

    public boolean showNoContentFound(){
        return  !showContentFound()&&!isLoading;
    }

    public boolean showContentFound(){
        return  (paymentList.size() >0);
    }
}
