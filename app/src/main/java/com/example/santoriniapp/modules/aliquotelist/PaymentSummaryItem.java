package com.example.santoriniapp.modules.aliquotelist;

import com.example.santoriniapp.utils.StringFunctions;
import com.example.santoriniapp.utils.UrbanizationConstants;
import com.example.santoriniapp.utils.UrbanizationUtils;

public class PaymentSummaryItem
{
    // Main Variables.
    public int paymentYearToPay;
    public String paymentMonthNameToPay;
    public String paymentMonthCodeToPay;
    public String paymentMonthStatus;
    public String paymentObservation;
    public boolean showPaymentAction;
    public double paymentPendingTotal;
    public double paymentSentTotal;

    public PaymentSummaryItem()
    {
        this.paymentYearToPay = 0;
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
        switch (paymentMonthStatus)
        {
            case UrbanizationConstants.PAYMENT_MONTH_PENDING:
                return UrbanizationUtils.getColor("#BE2A2A");

            case UrbanizationConstants.PAYMENT_MONTH_PAID:
                return UrbanizationUtils.getColor("#3D9C60");

            case UrbanizationConstants.PAYMENT_MONTH_SENT:
                return UrbanizationUtils.getColor("#FF6F00");
        }
        return 0;
    }

    public String paymentStatusDescription()
    {
        switch (paymentMonthStatus)
        {
            case UrbanizationConstants.PAYMENT_MONTH_PENDING:
                return "Pendiente";

            case UrbanizationConstants.PAYMENT_MONTH_PAID:
                return "Aprobado";

            case UrbanizationConstants.PAYMENT_MONTH_SENT:
                return "Enviado";
        }
        return "";
    }

    public String paymentPendingTotalText(){return StringFunctions.toMoneyString(paymentPendingTotal);}
    public String paymentSentTotalText(){return StringFunctions.toMoneyString(paymentSentTotal);}

}
