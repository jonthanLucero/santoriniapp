package com.example.santoriniapp.modules.payment.paymentheader;

import com.example.santoriniapp.modules.payment.paymentlist.PaymentDateRowSpinnerItem;
import com.example.santoriniapp.utils.DateFunctions;
import com.example.santoriniapp.utils.StringFunctions;
import com.example.santoriniapp.utils.UrbanizationConstants;
import com.example.santoriniapp.utils.UrbanizationUtils;
import com.example.santoriniapp.utils.inalambrikAddPhotoGallery.InalambrikAddPhotoGalleryItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PaymentActivityViewModelResponse
{
    public String paymentDateText;
    public int paymentYearSpinnerPosition;
    public int paymentTimeSpinnerPosition;
    public int paymentDateYearCode;
    public String paymentDateMonthCode;
    public String paymentTypeCode;
    public double paymentAmount;
    public boolean enablePhotAddButton;
    public String paymentStatus;
    public int paymentNumber;
    public int paymentReceiptNumber;
    public String userId;
    public boolean isDisplayMode;
    public String errorMessage;
    public String userName;
    public ArrayList<Integer> paymentYearSpinnerList;
    public ArrayList<PaymentDateRowSpinnerItem> paymentListSpinnerList;
    public ArrayList<PaymentTypeItem> paymentTypeListSpinnerList;
    public List<InalambrikAddPhotoGalleryItem> paymentPhotoList;
    public int paymentTypeSpinnerPosition;
    public boolean isSendingPayment;
    public boolean isSavingPayment;
    public boolean isSendingPaymentPhotos;
    public boolean loadDataFromDB;
    public String paymentCommentary;
    public String paymentVoidCommentary;
    public boolean isPaymentSent;
    public boolean isPaymentDraft;
    public boolean isPaymentDeleted;
    public String paymentSentNumber;
    public String monthCodeToBlock;
    public String serverMessage;

    //Status Color
    private int pendingColor   = UrbanizationUtils.getColor("#2196f3");
    private int sentColor      = UrbanizationUtils.getColor("#FF6F00");
    private int voidColor      = UrbanizationUtils.getColor("#BE2A2A");
    private int approvedColor  = UrbanizationUtils.getColor("#3D9C60");

    public PaymentActivityViewModelResponse()
    {
        this.paymentDateText        = "";
        this.paymentDateYearCode    = 0;
        this.paymentDateMonthCode   = "";
        this.paymentTypeCode        = "";
        this.paymentAmount          = 0.0;
        this.enablePhotAddButton    = false;
        this.paymentStatus          = "";
        this.paymentNumber          = 0;
        this.paymentReceiptNumber   = 0;
        this.userId                 = "";
        this.userName               = "";
        this.isDisplayMode          = false;
        this.errorMessage           = "";
        this.paymentYearSpinnerList = new ArrayList<>();
        this.paymentListSpinnerList = new ArrayList<>();
        this.paymentTimeSpinnerPosition = 0;
        this.paymentPhotoList       = new ArrayList<>();
        this.isSendingPayment       = false;
        this.isSendingPaymentPhotos = false;
        this.loadDataFromDB         = false;
        this.paymentCommentary      = "";
        this.isPaymentSent          = false;
        this.isSavingPayment        = false;
        this.serverMessage          = "";
        this.paymentVoidCommentary  = "";
        this.isPaymentDraft         = false;
        this.isPaymentDeleted       = false;
    }

    //Methods
    public String userNameDescription()
    {
        return userName;
    }



    public String getPaymentReceivedValue()
    {
        return  StringFunctions.toMoneyString(this.paymentAmount);
    }

    public boolean paymentIsPending()
    {
        return this.paymentStatus.equalsIgnoreCase(UrbanizationConstants.PAYMENT_PENDING);
    }

    public boolean paymentIsSent()
    {
        return this.paymentStatus.equalsIgnoreCase(UrbanizationConstants.PAYMENT_SENT);
    }

    public boolean paymentIsVoid()
    {
        return this.paymentStatus.equalsIgnoreCase(UrbanizationConstants.PAYMENT_VOID);
    }

    public boolean paymentIsApproved()
    {
        return this.paymentStatus.equalsIgnoreCase(UrbanizationConstants.PAYMENT_APPROVED);
    }

    public boolean paymentIsDraft()
    {
        return this.paymentStatus.equalsIgnoreCase(UrbanizationConstants.PAYMENT_DRAFT);
    }

    public boolean showSavePaymentAction(){
        return paymentIsPending() || paymentIsDraft();
    }
    public boolean showDeletePaymentAction(){
        return false;
    }

    public boolean showSaveDraftPaymentAction(){
        return paymentIsDraft() || paymentIsPending();
    }

    //METHODS
    public String statusLabelText()
    {
        if(paymentIsPending())
            return "Pago pendiente";

        if(paymentIsDraft())
            return "Pago en Borrador";

        if(paymentIsVoid())
            return "Pago Anulado "+paymentVoidCommentary.trim();
        if(paymentIsSent())
            return "Pago Enviado";
        if(paymentIsApproved())
           return "Pago Aprobado #"+paymentNumber;

        return "Pago en espera";
    }

    public int statusLabelBackgroundColor()
    {
        if(paymentIsPending() || paymentIsDraft())return pendingColor;
        if(paymentIsVoid())return voidColor;
        if(paymentIsSent())return sentColor;
        if(paymentIsApproved())return approvedColor;
        return pendingColor;
    }

    public String getPaymentDateString()
    {
        return paymentDateText;
    }

    public boolean showStatusLabel()
    {
        return (paymentIsPending() || paymentIsVoid() || paymentIsSent() || paymentIsApproved() || paymentIsDraft()) && !isSendingPayment && !isPaymentSent;
    }

    public boolean showOnErrorMessage()
    {
        return !errorMessage.trim().isEmpty();
    }

    public boolean showPaymentRegisterContainer()
    {
        return isPaymentSent || !isSendingPayment;
    }

    public String paymentNumberString()
    {
        return "Pago :"+paymentNumber;
    }

    public boolean showPrintReceipt()
    {
        return paymentIsSent() || paymentIsApproved();
    }

    public boolean enableMonthCodeSpinner()
    {
        return !isDisplayMode && monthCodeToBlock.trim().isEmpty();
    }
}
