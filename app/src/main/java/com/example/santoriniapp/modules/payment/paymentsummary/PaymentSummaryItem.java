package com.example.santoriniapp.modules.payment.paymentsummary;

import com.example.santoriniapp.utils.StringFunctions;
import com.example.santoriniapp.utils.UrbanizationConstants;
import com.example.santoriniapp.utils.UrbanizationUtils;

public class PaymentSummaryItem
{
    // Main Variables.
    public String paymentMonthNameToPay;
    public String paymentMonthCodeToPay;
    public String paymentMonthStatus;
    public String paymentObservation;
    public boolean showPaymentAction;
    public double paymentPendingTotal;
    public double paymentSentTotal;

    public PaymentSummaryItem()
    {
        this.paymentMonthNameToPay = "";
        this.paymentMonthCodeToPay = "";
        this.paymentMonthStatus = "";
        this.paymentObservation = "";
        this.showPaymentAction = false;
    }

    //GRAY  --> #808080
    //RED   --> #BE2A2A
    //BLUE  --> #2196f3
    //GREEN --> #3D9C60
    public int  paymentStatusBackgroundColor()
    {
        if(paymentMonthStatus.equalsIgnoreCase(UrbanizationConstants.PAYMENT_PENDING))         //RED
            return UrbanizationUtils.getColor("#BE2A2A");
        else
            return UrbanizationUtils.getColor("#3D9C60");                               //GREEN
    }

    public String paymentStatusDescription()
    {
        if(paymentMonthStatus.equalsIgnoreCase(UrbanizationConstants.PAYMENT_MONTH_PENDING))
            return "Pendiente";
        else
            return "Aprobado";
    }

    public String paymentPendingTotalText(){return StringFunctions.toMoneyString(paymentPendingTotal);}
    public String paymentSentTotalText(){return StringFunctions.toMoneyString(paymentSentTotal);}

}
