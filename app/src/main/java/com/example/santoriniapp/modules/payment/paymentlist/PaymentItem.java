package com.example.santoriniapp.modules.payment.paymentlist;

import com.example.santoriniapp.utils.UrbanizationConstants;
import com.example.santoriniapp.utils.UrbanizationUtils;

import java.util.Date;

public class PaymentItem
{

    // Main Variables.
    public String paymentDateText;
    public String paymentStatusText;

    public String paymentTotalText;
    public String paymentMonthToPay;
    public String quoteUpdateConvertToOrderStatus;
    public String orderItemQuoteCloseMotiveStatusText;
    public String paymentCommentary;
    public Date paymentDate;


    // Error Messages.
    public String errorMessage;

    public PaymentItem() {
        paymentDate = new Date();
        paymentDateText = "";
        paymentStatusText = "";
        paymentTotalText = "";
        paymentMonthToPay = "";
        quoteUpdateConvertToOrderStatus     = "";
        this.errorMessage                   = "";
        orderItemQuoteCloseMotiveStatusText = "";
        this.paymentCommentary              = "";

    }

    public String paymentDateString()
    {
        return paymentDateText;
    }

    //GRAY  --> #808080
    //RED   --> #BE2A2A
    //BLUE  --> #2196f3
    //GREEN --> #3D9C60
    public int  paymentStatusBackgroundColor()
    {
        if(paymentStatusText.trim().isEmpty())
            return  UrbanizationUtils.getColor("#808080");                                    //GRAY

        if(paymentStatusText.equalsIgnoreCase(UrbanizationConstants.PAYMENT_VOID))                  //RED
            return UrbanizationUtils.getColor("#BE2A2A");

        if(paymentStatusText.equalsIgnoreCase(UrbanizationConstants.PAYMENT_APPROVED))              //GREEN
            return UrbanizationUtils.getColor("#3D9C60");

        if(paymentStatusText.equalsIgnoreCase(UrbanizationConstants.PAYMENT_SENT))                  //ORANGE
            return UrbanizationUtils.getColor("#FF6F00");

        return UrbanizationUtils.getColor("#2196f3");                                         //BLUE
    }

    public boolean showPaymentCommentary() {
        return !paymentCommentary.trim().isEmpty();
    }

    public String paymentStatusDescription()
    {
        if(paymentStatusText.trim().isEmpty())
            return  "Pendiente";

        if(paymentStatusText.equalsIgnoreCase(UrbanizationConstants.PAYMENT_VOID))
            return "Anulado";

        if(paymentStatusText.equalsIgnoreCase(UrbanizationConstants.PAYMENT_APPROVED))
            return "Aprobado";

        if(paymentStatusText.equalsIgnoreCase(UrbanizationConstants.PAYMENT_SENT))
            return "Enviado";

        return "Pendiente";
    }
}
