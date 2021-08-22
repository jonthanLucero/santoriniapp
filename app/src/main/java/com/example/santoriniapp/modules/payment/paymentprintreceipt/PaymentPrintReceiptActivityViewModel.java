package com.example.santoriniapp.modules.payment.paymentprintreceipt;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

public class PaymentPrintReceiptActivityViewModel extends ViewModel
{
    private MutableLiveData<PaymentPrintReceiptViewModelReponse> mPanelResponse;

    public LiveData<PaymentPrintReceiptViewModelReponse> getCurrentPaymentPrintInformation(String userId, Date paymentDate) {

        // If the response is NULL, then load the INITIAL data...
        if (mPanelResponse == null)
        {
            mPanelResponse = new MutableLiveData<>();
            loadPaymentPrintInformation(userId,paymentDate);
        }

        return mPanelResponse;
    }

    private void loadPaymentPrintInformation(final String userId,final Date mPaymentDate){

        // -------------------------------------
        // Set the LOADING STATUS...
        // -------------------------------------
        Observable.defer(new Func0<Observable<PaymentPrintReceiptViewModelReponse>>() {
            @Override
            public Observable<PaymentPrintReceiptViewModelReponse> call()  {
                return Observable.just(PaymentPrintReceiptActivityViewModelHelper.getInitialLoadingStatus(userId,mPaymentDate));
            }
        }).subscribeOn(Schedulers.io()) // Code BEFORE is called on background thread...
                .observeOn(AndroidSchedulers.mainThread()) // Code AFTER is called on main thread...
                .subscribe(new Subscriber<PaymentPrintReceiptViewModelReponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        PaymentPrintReceiptViewModelReponse response = new PaymentPrintReceiptViewModelReponse();
                        mPanelResponse.postValue(response); // This will trigger in the activity.
                    }

                    @Override
                    public void onNext(PaymentPrintReceiptViewModelReponse response) {
                        // Show the Loading Status in UI.
                        mPanelResponse.postValue(response);
                    }
                });

    }
}
