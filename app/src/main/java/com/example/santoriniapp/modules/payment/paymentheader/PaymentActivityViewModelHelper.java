package com.example.santoriniapp.modules.payment.paymentheader;

import android.content.Context;
import com.example.santoriniapp.entity.Payment;
import com.example.santoriniapp.entity.PaymentPhoto;
import com.example.santoriniapp.modules.payment.paymentlist.PaymentDateRowSpinnerItem;
import com.example.santoriniapp.repository.LoginRepository;
import com.example.santoriniapp.repository.PaymentPhotoRepository;
import com.example.santoriniapp.repository.PaymentRepository;
import com.example.santoriniapp.repository.PaymentTypeRepository;
import com.example.santoriniapp.retrofit.ApiInterface;
import com.example.santoriniapp.retrofit.ServiceGenerator;
import com.example.santoriniapp.retrofit.dtos.paymentDtos.PaymentRequest;
import com.example.santoriniapp.retrofit.dtos.paymentDtos.PaymentResponse;
import com.example.santoriniapp.utils.DateFunctions;
import com.example.santoriniapp.utils.ImageFunctions;
import com.example.santoriniapp.utils.NumericFunctions;
import com.example.santoriniapp.utils.PaymentUtils;
import com.example.santoriniapp.utils.UrbanizationConstants;
import com.example.santoriniapp.utils.UrbanizationGlobalUtils;
import com.example.santoriniapp.utils.UrbanizationSessionUtils;
import com.example.santoriniapp.utils.UrbanizationUtils;
import com.example.santoriniapp.utils.inalambrikAddPhotoGallery.InalambrikAddPhotoGalleryItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import rx.Observable;
import rx.functions.Func1;

public class PaymentActivityViewModelHelper
{
    public static final String LOG_TAG = PaymentActivityViewModelHelper.class.getSimpleName();

    public static PaymentActivityViewModelResponse getInitialLoadingStatus(String userId,String mode, Date mPaymentDate,
                                                                           int selectedMonthPositionSpiner, int selectedTypeCodePositionSpinner,String monthCode) {

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
        response.paymentDateMonthCode = monthCode.trim().isEmpty() ? payment.getPaymentmonth() : monthCode;
        response.paymentTypeCode = payment.getPaymenttypecode();
        response.paymentAmount   = payment.getPaymentamount();
        response.enablePhotAddButton = payment.getPaymentstatus().equalsIgnoreCase(UrbanizationConstants.PAYMENT_PENDING);
        response.paymentStatus = payment.getPaymentstatus();
        response.paymentNumber = payment.getPaymentnumber();
        response.paymentReceiptNumber = payment.getPaymentreceiptnumber();
        response.userId = payment.getUserid();
        response.paymentCommentary = payment.getPaymentmemo().trim();
        response.isDisplayMode = payment.getPaymentstatus().equalsIgnoreCase(UrbanizationConstants.PAYMENT_PENDING);
        response.errorMessage = "";

        //Load Payment Month and Payment Type Spinner Values
        if(mode.equalsIgnoreCase(UrbanizationConstants.PAYMENT_MODE_INSERT))
        {
            response.paymentTimeSpinnerPosition = selectedMonthPositionSpiner == -1 ? getPaymentDateRecordPosition(paymentDateRowSpinnerItemList, UrbanizationUtils.getTodayMonthRequestCode()) : 0;
            response.paymentTypeSpinnerPosition = selectedTypeCodePositionSpinner == -1 ? getPaymentTypeRecordPosition(paymentTypeList, UrbanizationConstants.PAYMENTTYPECODE_CASH) : 0;
        }
        else
        {
            if(monthCode.trim().isEmpty())
                response.paymentTimeSpinnerPosition = getPaymentDateRecordPosition(paymentDateRowSpinnerItemList, payment.getPaymentmonth());
            else
                response.paymentTimeSpinnerPosition = PaymentActivityViewModelHelper.getPaymentDateRecordPosition(paymentDateRowSpinnerItemList,monthCode);

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
        response.monthCodeToBlock = monthCode;
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

        //Check if paymentType requires to send photo
        PaymentTypeRepository paymentTypeRepository = new PaymentTypeRepository();
        boolean paymentTypeIsRequiredPhoto = paymentTypeRepository.getPaymentTypeRequiresPhoto(paymentToBeSaved.getPaymenttypecode());

        if(paymentTypeIsRequiredPhoto && currentResponse.paymentPhotoList.size() <= 0)
        {
            currentResponse.errorMessage = "La forma de pago requiere capturar/adjuntar foto";
            return currentResponse;
        }

        // --------------------------------------------------------------------------------------------------------------
        // Saving in DB PAYMENT
        // --------------------------------------------------------------------------------------------------------------
        paymentToBeSaved.setPaymentstatus(UrbanizationConstants.PAYMENT_PENDING);
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
                   currentResponse.errorMessage = "Una de las fotos no pudo ser comprimida. Por favor intente nuevamente.\n\nNOTA: Prospecto ha sido guardado como pendiente de envío." ;
                    return currentResponse;
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

        // --------------------------------------------------------------------------------------------------------------
        // Sending to Server
        // --------------------------------------------------------------------------------------------------------------


        // -----------------------------------
        // Set WS Request.
        // -----------------------------------
        PaymentRequest request = new PaymentRequest();
        request.setUserLogin(UrbanizationSessionUtils.getLoggedLogin(context));
        request.setUserPassword(UrbanizationSessionUtils.getLoggedPassword(context));
        request.setPaymentDate(DateFunctions.getYYYYMMDDHHMMSSString(DateFunctions.toDate(paymentToBeSaved.getPaymentdate())));
        request.setPaymentMonth(paymentToBeSaved.getPaymentmonth());
        request.setPaymentTypeCode(paymentToBeSaved.getPaymenttypecode());
        request.setPaymentAmount(paymentToBeSaved.getPaymentamount());
        request.setPaymentMemo(paymentToBeSaved.getPaymentmemo());


        try {
            //  -----------------------------------------------------------------------------------
            // Calling WS.
            //  -----------------------------------------------------------------------------------
            ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
            Call<PaymentResponse> call = apiInterface.paymentSendWS(request);

            //  -----------------------------------------------------------------------------------
            // Getting response.
            //  -----------------------------------------------------------------------------------
            PaymentResponse wsResponse = call.execute().body();

            // If there is no response...
            if(wsResponse==null) {
                currentResponse.errorMessage = "\"No se pudo contactar Servidor. Por favor intentar nuevamente.";
                currentResponse.isSendingPayment = false;
                return currentResponse;
            }

            // If server returned an error...

            if(!wsResponse.getErrorMessage().trim().isEmpty()) {
                currentResponse.errorMessage = wsResponse.getErrorMessage().trim();
                return currentResponse;
            }

            response.isPaymentSent = true;
            response.monthCodeToBlock = paymentToBeSaved.getPaymentmonth();

            int paymentNumber = wsResponse.getPaymentNumber();
            int paymentReceiptNumber = wsResponse.getPaymentReceiptNumber();

            //Update PaymentNumber and paymentStatus
            paymentToBeSaved.setPaymentnumber(paymentNumber);
            paymentToBeSaved.setPaymentreceiptnumber(paymentReceiptNumber);
            paymentToBeSaved.setPaymentstatus(UrbanizationConstants.PAYMENT_SENT);
            paymentRepository.updatePaymentToDB(paymentToBeSaved);

            response.paymentNumber = paymentNumber;
            response.serverMessage = "Pago enviado correctamente.";

            return response;

        }catch (Exception e) {
            e.printStackTrace();
            currentResponse.errorMessage = "No se pudo contactar Servidor. Por favor intentar nuevamente.";
            return  currentResponse;
        }
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
