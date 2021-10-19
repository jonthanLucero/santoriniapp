package com.example.santoriniapp.utils;

import android.util.Log;

import com.example.santoriniapp.entity.Payment;
import com.example.santoriniapp.entity.PaymentPhoto;
import com.example.santoriniapp.entity.PaymentType;
import com.example.santoriniapp.modules.payment.paymentheader.PaymentActivityViewModelResponse;
import com.example.santoriniapp.modules.payment.paymentheader.PaymentTypeItem;
import com.example.santoriniapp.modules.payment.paymentlist.PaymentDateRowSpinnerItem;
import com.example.santoriniapp.repository.PaymentPhotoRepository;
import com.example.santoriniapp.repository.PaymentRepository;
import com.example.santoriniapp.repository.PaymentTypeRepository;
import com.example.santoriniapp.utils.inalambrikAddPhotoGallery.InalambrikAddPhotoGalleryItem;

import java.util.ArrayList;
import java.util.List;

public class PaymentUtils
{
    public static Payment getNewPayment(String userId)
    {
        Long newDate = NumericFunctions.toLong(DateFunctions.now());

        return new Payment(userId,newDate,UrbanizationUtils.getTodayMonthRequestCode(),
                           UrbanizationConstants.PAYMENTTYPECODE_CASH,0,
                0,0,UrbanizationConstants.PAYMENT_PENDING,"","",newDate);
    }

    public static ArrayList<PaymentDateRowSpinnerItem> getPaymentDateRowSpinnerList(String calledFrom)
    {
        // Load Spinner
        ArrayList<PaymentDateRowSpinnerItem> paymentDateRowSpinnerItemList = new ArrayList<>();
        if(calledFrom.equalsIgnoreCase(""))
            paymentDateRowSpinnerItemList.add(new PaymentDateRowSpinnerItem("00", "Todos")) ;

        paymentDateRowSpinnerItemList.add(new PaymentDateRowSpinnerItem("01", "Enero")) ;
        paymentDateRowSpinnerItemList.add(new PaymentDateRowSpinnerItem("02", "Febrero"));
        paymentDateRowSpinnerItemList.add(new PaymentDateRowSpinnerItem("03", "Marzo"));
        paymentDateRowSpinnerItemList.add(new PaymentDateRowSpinnerItem("04", "Abril"));
        paymentDateRowSpinnerItemList.add(new PaymentDateRowSpinnerItem("05", "mayo"));
        paymentDateRowSpinnerItemList.add(new PaymentDateRowSpinnerItem("06", "Junio"));
        paymentDateRowSpinnerItemList.add(new PaymentDateRowSpinnerItem("07", "Julio"));
        paymentDateRowSpinnerItemList.add(new PaymentDateRowSpinnerItem("08", "Agosto"));
        paymentDateRowSpinnerItemList.add(new PaymentDateRowSpinnerItem("09", "Septiembre"));
        paymentDateRowSpinnerItemList.add(new PaymentDateRowSpinnerItem("10", "Octubre"));
        paymentDateRowSpinnerItemList.add(new PaymentDateRowSpinnerItem("11", "Noviembre"));
        paymentDateRowSpinnerItemList.add(new PaymentDateRowSpinnerItem("12", "Diciembre"));
        return paymentDateRowSpinnerItemList;
    }

    public static ArrayList<PaymentTypeItem> getPaymentTypeSpinnerList()
    {
        //Get PaymentTypes
        PaymentTypeRepository paymentTypeRepository = new PaymentTypeRepository();
        List<PaymentType> paymentTypeList = paymentTypeRepository.getAllPaymentTypeList();
        ArrayList <PaymentTypeItem> itemList = new ArrayList<>();
        if(paymentTypeList == null)
            return null;

        PaymentTypeItem item;
        for(PaymentType p : paymentTypeList)
        {
            item = new PaymentTypeItem();
            item.paymentTypeCode = p.getPaymenttypecode().trim();
            item.paymentTypeDescription = p.getPaymenttypedescription().trim();
            itemList.add(item);
        }
        return itemList;
    }

    public static PaymentActivityViewModelResponse savePaymentWithPhotos(Payment paymentToBeSaved, List<InalambrikAddPhotoGalleryItem> itemPhotoList)
    {
        PaymentActivityViewModelResponse currentResponse = new PaymentActivityViewModelResponse();

        PaymentRepository paymentRepository = new PaymentRepository();
        PaymentPhotoRepository paymentPhotoRepository = new PaymentPhotoRepository();

        paymentToBeSaved.setPaymentstatus(UrbanizationConstants.PAYMENT_PENDING);
        paymentRepository.updatePaymentToDB(paymentToBeSaved);

        // -----------------------------------------------------------------------------------------------
        //  Saving the Photos.
        // -----------------------------------------------------------------------------------------------
        if(itemPhotoList.size() > 0)
        {
            List<PaymentPhoto> paymentPhotos;
            paymentPhotoRepository.deletePaymentPhotosOfPayment(DateFunctions.toDate(paymentToBeSaved.getPaymentdate()));

            paymentPhotos = paymentPhotoRepository.getAllPaymentPhotosOfPayment(DateFunctions.toDate(paymentToBeSaved.getPaymentdate()));
            Log.d("LOG_TAG","AFTER DELETE PaymentPhotos=>"+paymentPhotos.size());

            //For each paymentphoto and the insert again in DB
            for(int i = 0; i < itemPhotoList.size();i++)
            {
                Long dateNow = DateFunctions.today().getTime();
                InalambrikAddPhotoGalleryItem item = itemPhotoList.get(i);

                String photoCompressedAsBase64 = ImageFunctions.getCompressed64Imagev2(item.photoPath());
                if(photoCompressedAsBase64.trim().isEmpty()){
                    currentResponse.errorMessage = "Una de las fotos no pudo ser comprimida. Por favor intente nuevamente.\n\nNOTA: Prospecto ha sido guardado como pendiente de envío." ;
                    return currentResponse;
                }

                paymentPhotoRepository.insertPaymentPhotoToDB(new PaymentPhoto(paymentToBeSaved.getPaymentdate(),
                        item.photoTitle().trim(),
                        item.photoDescription().trim(),
                        item.photoPath().trim(),
                        photoCompressedAsBase64,
                        dateNow,
                        dateNow
                        ));

                //Save in memory
                itemPhotoList.get(i).setPhotoBase64(photoCompressedAsBase64);
            }

            paymentPhotos = paymentPhotoRepository.getAllPaymentPhotosOfPayment(DateFunctions.toDate(paymentToBeSaved.getPaymentdate()));
            Log.d("LOG_TAG","AFTER INSERT PaymentPhotos=>"+paymentPhotos.size());

        }
        currentResponse.errorMessage = "";
        currentResponse.paymentPhotoList = itemPhotoList;
        return currentResponse;
    }

}
