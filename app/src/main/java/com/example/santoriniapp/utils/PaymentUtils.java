package com.example.santoriniapp.utils;

import com.example.santoriniapp.entity.Payment;
import com.example.santoriniapp.entity.PaymentType;
import com.example.santoriniapp.modules.payment.paymentheader.PaymentTypeItem;
import com.example.santoriniapp.modules.payment.paymentlist.PaymentDateRowSpinnerItem;
import com.example.santoriniapp.repository.PaymentTypeRepository;

import java.util.ArrayList;
import java.util.List;

public class PaymentUtils
{
    public static Payment getNewPayment(String userId)
    {
        Long newDate = NumericFunctions.toLong(DateFunctions.now());

        return new Payment(userId,newDate,UrbanizationUtils.getTodayMonthRequestCode(),
                           UrbanizationConstants.PAYMENTTYPECODE_CASH,0,
                0,0,UrbanizationConstants.PAYMENT_PENDING,"",newDate);
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

}
