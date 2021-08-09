package com.example.santoriniapp.modules.payment.paymentlist;

import com.example.santoriniapp.utils.UrbanizationConstants;
import com.example.santoriniapp.utils.UrbanizationUtils;
import com.example.santoriniapp.utils.ViewModel;

import java.util.Date;

public class PaymentItem implements ViewModel
{

    // Main Variables.
    public String paymentDateText;
    public String paymentStatusText;

    public String paymentTotalText;
    public String itemCountText;
    public String quoteUpdateConvertToOrderStatus;
    public String orderItemQuoteCloseMotiveStatusText;
    public String paymentCommentary;
    public Date orderDate;

    // ------------------------------------
    // Status
    // ------------------------------------
    public boolean orderQueuetNotSent;
    public boolean orderQueuetSent;
    public boolean orderQueuetSending;
    public boolean orderQueuetPending;
    public boolean orderQueuetExpirationDays;
    public boolean orderQueuetNeutral;

    public boolean showQuoteCloseMotiveContainer;

    public boolean quotePendingToClose;
    public boolean quoteAlreadyClosed;

    public int orderItemQuoteCloseMotiveStatusColor;

    // Error Messages.
    public String errorMessage;

    public PaymentItem() {
        paymentDateText = "";
        paymentStatusText = "";
        paymentTotalText = "";
        itemCountText                       = "";
        quoteUpdateConvertToOrderStatus     = "";
        this.errorMessage                   = "";
        orderItemQuoteCloseMotiveStatusText = "";
        this.paymentCommentary              = "";
        this.orderDate                      = new Date(0);

        orderQueuetNotSent                 = false;
        orderQueuetSent                    = false;
        orderQueuetSending                 = false;
        orderQueuetPending                 = false;
        orderQueuetExpirationDays          = false;
        orderQueuetNeutral                 = false;
        showQuoteCloseMotiveContainer      = false;

        quotePendingToClose                = false;
        quoteAlreadyClosed                 = false;

        orderItemQuoteCloseMotiveStatusColor = 0;


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

        return "Pendiente";
    }
}
