package com.example.santoriniapp.modules.payment.paymentheader;

import android.content.Context;

import androidx.core.app.NotificationCompatSideChannelService;

import com.example.santoriniapp.entity.Payment;
import com.example.santoriniapp.entity.PaymentPhoto;
import com.example.santoriniapp.entity.PaymentType;
import com.example.santoriniapp.modules.payment.paymentlist.PaymentDateRowSpinnerItem;
import com.example.santoriniapp.repository.LoginRepository;
import com.example.santoriniapp.repository.PaymentPhotoRepository;
import com.example.santoriniapp.repository.PaymentRepository;
import com.example.santoriniapp.repository.PaymentTypeRepository;
import com.example.santoriniapp.utils.DateFunctions;
import com.example.santoriniapp.utils.ImageFunctions;
import com.example.santoriniapp.utils.NumericFunctions;
import com.example.santoriniapp.utils.PaymentUtils;
import com.example.santoriniapp.utils.UrbanizationConstants;
import com.example.santoriniapp.utils.UrbanizationGlobalUtils;
import com.example.santoriniapp.utils.UrbanizationUtils;
import com.example.santoriniapp.utils.inalambrikAddPhotoGallery.InalambrikAddPhotoGalleryItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class PaymentActivityViewModelHelper
{
    public static final String LOG_TAG = PaymentActivityViewModelHelper.class.getSimpleName();

    public static PaymentActivityViewModelResponse getInitialLoadingStatus(String userId,String mode, Date mPaymentDate,int selectedMonthPositionSpiner, int selectedTypeCodePositionSpinner) {

        PaymentActivityViewModelResponse response = new PaymentActivityViewModelResponse();

        String userName;
        //Get UserName
        LoginRepository loginRepository = new LoginRepository();
        if(loginRepository.getLoginName(userId) == null)
            userName = "N/A";
        else
            userName =  loginRepository.getLoginName(userId).trim().isEmpty() ? "N/A" : loginRepository.getLoginName(userId).trim();
        response.userName = userName;

        ArrayList<PaymentDateRowSpinnerItem> paymentDateRowSpinnerItemList = PaymentUtils.getPaymentDateRowSpinnerList("PAYMENT");
        response.paymentListSpinnerList = paymentDateRowSpinnerItemList;

        ArrayList<PaymentTypeItem> paymentTypeList = PaymentUtils.getPaymentTypeSpinnerList();
        response.paymentTypeListSpinnerList = paymentTypeList;

        Payment payment;
        PaymentRepository paymentRepository = new PaymentRepository();
        payment = paymentRepository.getPayment(mPaymentDate);
        String errorMessage;

        if(payment == null)
        {
            response = new PaymentActivityViewModelResponse();
            errorMessage = "Ha ocurrido un error. Por favor intentar nuevamente.";
            response.errorMessage = errorMessage;
            return response;
        }

        response.paymentDateText = DateFunctions.getDDMMMYYYYHHMMSSDateString(mPaymentDate);
        response.paymentDateMonthCode = payment.getPaymentmonth();
        response.paymentTypeCode = payment.getPaymenttypecode();
        response.paymentAmount   = payment.getPaymentamount();
        response.showPrintReceipt = !payment.getPaymentstatus().equalsIgnoreCase(UrbanizationConstants.PAYMENT_APPROVED);
        response.enablePhotAddButton = payment.getPaymentstatus().equalsIgnoreCase(UrbanizationConstants.PAYMENT_PENDING);
        response.paymentStatus = payment.getPaymentstatus();
        response.paymentNumber = payment.getPaymentnumber();
        response.paymentReceiptNumber = payment.getPaymentreceiptnumber();
        response.userId = payment.getUserid();
        response.paymentCommentary = payment.getPaymentmemo().trim();
        response.isDisplayMode = payment.getPaymentstatus().equalsIgnoreCase(UrbanizationConstants.PAYMENT_PENDING);
        response.errorMessage = "";

        //Load Payment Month and Payment Type Spinner Values
        if(mode.equalsIgnoreCase("INSERT"))
        {
            response.paymentTimeSpinnerPosition = selectedMonthPositionSpiner == -1 ? getPaymentDateRecordPosition(paymentDateRowSpinnerItemList, UrbanizationUtils.getTodayMonthRequestCode()) : 0;
            response.paymentTypeSpinnerPosition = selectedTypeCodePositionSpinner == -1 ? getPaymentTypeRecordPosition(paymentTypeList, UrbanizationConstants.PAYMENTTYPECODE_CASH) : 0;
        }
        else
        {
            response.paymentTimeSpinnerPosition = getPaymentDateRecordPosition(paymentDateRowSpinnerItemList,payment.getPaymentmonth());
            response.paymentTypeSpinnerPosition = getPaymentTypeRecordPosition(paymentTypeList, payment.getPaymenttypecode());
        }

        PaymentPhotoRepository paymentPhotoRepository = new PaymentPhotoRepository();
        List<PaymentPhoto> paymentPhotoList = paymentPhotoRepository.getAllPaymentPhotosOfPayment(mPaymentDate);
        final boolean isDisplayMode = paymentPanelIsDisplayMode(payment);

        if(paymentPhotoList != null && paymentPhotoList.size() > 0) {
            // Load Saved Photos.
            response.paymentPhotoList = Observable.from(paymentPhotoList)
                    .map(new Func1<PaymentPhoto, InalambrikAddPhotoGalleryItem>() {
                        @Override
                        public InalambrikAddPhotoGalleryItem call(PaymentPhoto item) {
                            return new InalambrikAddPhotoGalleryItem(item.getPaymentphototitle(), item.getPaymentphotodescription(), item.getPaymentphotopath(),isDisplayMode);
                        }
                    }).toList().toBlocking().first();

        }
        // To indicate that the data loaded was from DB and make that those values are set initially in the form.
        response.loadDataFromDB = true;
        response.isDisplayMode = isDisplayMode;
        return  response;
    }

    public static PaymentActivityViewModelResponse sendPaymentToServer(Date payymentDate,PaymentActivityViewModelResponse currentResponse)
    {
        PaymentActivityViewModelResponse response = new PaymentActivityViewModelResponse();

        // -----------------------------------------------------------
        // Convert what it is in the view to a prospect_class.
        // -----------------------------------------------------------
        Payment oldPayment;
        List<PaymentPhoto> oldPaymentPhotos;
        Payment paymentToBeSaved = getPaymentClassFromView(payymentDate,currentResponse);

        // -----------------------------------------------------------
        // Getting context.
        // -----------------------------------------------------------
        Context context = UrbanizationGlobalUtils.getInstance();

        // --------------------------------------------------------------------
        // Very unlikely to happen - But it cant happen in any circumstances.
        // It cant exist an empty date.
        // --------------------------------------------------------------------
        if(paymentToBeSaved == null) {
            currentResponse.errorMessage = "Ha ocurrido un error inesperado. Por favor cerrar el formulario y llenarlo nuevamente.";
            return currentResponse;
        }

        // ---------------------------------------------------------------
        // Just in case - Checking that the prospect is not already SENT
        // ---------------------------------------------------------------
        PaymentRepository paymentRepository = new PaymentRepository();
        PaymentPhotoRepository paymentPhotoRepository = new PaymentPhotoRepository();
        oldPayment = paymentRepository.getPayment(payymentDate);

        if(oldPayment == null) {
            currentResponse.errorMessage = "Ha ocurrido un error inesperado. Por favor cerrar el formulario y llenarlo nuevamente.";
            return currentResponse;
        }

        if(oldPayment.getPaymentstatus().equalsIgnoreCase(UrbanizationConstants.PAYMENT_SENT)) {
            currentResponse.errorMessage = "Pago ya enviado al servidor.";
            return currentResponse;
        }

        // --------------------------------------------------------------------------------------------------------------
        // Saving in DB PAYMENT
        // --------------------------------------------------------------------------------------------------------------
        paymentToBeSaved.setPaymentstatus(UrbanizationConstants.PAYMENT_SENT);
        paymentRepository.updatePaymentToDB(paymentToBeSaved);

        // --------------------------------------------------------------------------------------------------------------
        // Saving in DB PAYMENT PHOTOS
        // --------------------------------------------------------------------------------------------------------------
        if(currentResponse.paymentPhotoList.size() > 0)
        {
            paymentPhotoRepository.deletePaymentPhotosOfPayment(payymentDate);

            for(InalambrikAddPhotoGalleryItem item : currentResponse.paymentPhotoList)
            {
                String photoCompressedAsBase64 = ImageFunctions.getCompressed64Imagev2(item.photoPath());
                if(photoCompressedAsBase64.trim().isEmpty()){
                   response.errorMessage = "Una de las fotos no pudo ser comprimida. Por favor intente nuevamente.\n\nNOTA: Prospecto ha sido guardado como pendiente de envío." ;
                    return response;
                }

                // Adding the Prospect Photo (compressed) to the Request.
                PaymentPhoto onePhoto = new PaymentPhoto();
                onePhoto.setPaymentdate(NumericFunctions.toLong(payymentDate));
                onePhoto.setPaymentphototitle(item.photoTitle().trim());
                onePhoto.setPaymentphotodescription(item.photoDescription().trim());
                onePhoto.setPaymentphotopath(item.photoPath().trim());
                onePhoto.setPaymentphotobase64(photoCompressedAsBase64);
                paymentPhotoRepository.insertPaymentPhotoToDB(onePhoto);
            }
        }


        // Update Current Prospect Status in the ViewModel.
        currentResponse.paymentStatus = paymentToBeSaved.getPaymentstatus();

        /*
        // --------------------------------------------------------------------------------------------------------------
        // Sending to Server
        // --------------------------------------------------------------------------------------------------------------
        ProspectDeviceToServerV3SDResponse wsResponse = sendProspectToServerThroughWS(context, currentResponse.prospectDate);

        // If there was an error...
        if(!wsResponse.getErrorMessage().trim().isEmpty()) {
            currentResponse.errorMessage = wsResponse.getErrorMessage();
            return currentResponse;
        }

        // ------------------------------------
        // At this Step everything is FINE.
        // Prospect is set as Sent
        // ------------------------------------
        prospect_class prospectRecord   = new prospect_class() ;
        prospectRecord.prospectdate     = currentResponse.prospectDate;
        prospectRecord.loadprospect() ;
        prospectRecord.prospectstatus = ProspectUtils.PROSPECT_SENT_STATUS;
        prospectRecord.updateprospect() ;
        PedidosMovilesProvider.commit();

        // Flag that indicated that the Prospect was created succesfully.
        currentResponse.prospectSentSuccessfully = true;

        // Mark as Display Mode to avoid edit.
        currentResponse.isDisplayMode = true;

        // Update Current Prospect Status in the ViewModel.
        currentResponse.prospectStatus = prospectRecord.prospectstatus;
        */
        currentResponse.isPaymentSent = true;
        return currentResponse;

    }

    private static Payment getPaymentClassFromView(Date paymentDate, PaymentActivityViewModelResponse currentResponse){

        Payment payment;
        PaymentRepository paymentRepository = new PaymentRepository();
        payment = paymentRepository.getPayment(paymentDate);

        if(payment == null)
            return null;

        // Getting Basic Info.
        payment.setPaymentmonth(currentResponse.paymentDateMonthCode);
        payment.setPaymenttypecode(currentResponse.paymentTypeCode);
        payment.setPaymentAmount(currentResponse.paymentAmount);
        payment.setPaymentmemo(currentResponse.paymentCommentary);
        return payment;
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

    public static int getPaymentTypeRecordPosition(ArrayList<PaymentTypeItem> ItemList, String paymentTypeCode)
    {
        // Current PaymentTypeList
        for (int i = 0; i < ItemList.size(); i++) {
            PaymentTypeItem item = ItemList.get(i);
            if (item.paymentTypeCode.equalsIgnoreCase(paymentTypeCode))
                return i;
        }
        return -1;
    }

    public static boolean paymentPanelIsDisplayMode(Payment payment){
        return payment.getPaymentstatus().equalsIgnoreCase(UrbanizationConstants.PAYMENT_SENT);
    }
}
