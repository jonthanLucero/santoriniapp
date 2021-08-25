package com.example.santoriniapp.modules.payment.paymentsummary;

import java.util.List;

public class PaymentSummaryActivityViewModelResponse
{
    public String errorMessage;
    public String userName;
    public boolean isLoading;
    public List<PaymentSummaryItem> paymentList;

    public PaymentSummaryActivityViewModelResponse()
    {
        this.userName = "";
        this.errorMessage = "";
        this.isLoading = false;
    }
}
