package com.example.santoriniapp.modules.payment.paymentprintreceipt;

public class PaymentPrintReceiptViewModelReponse
{
    public String paymentNumberString;
    public String paymentDateString;
    public String userName;
    public String paymentAmountString;
    public String paymentCommentary;
    public String errorMessage;

    public PaymentPrintReceiptViewModelReponse()
    {
        this.paymentNumberString = "";
        this.paymentDateString   = "";
        this.userName = "";
        this.paymentAmountString = "";
        this.paymentCommentary   = "";
        this.errorMessage = "";
    }
}
