package com.example.santoriniapp.modules.aliquotelist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

public class PaymentSummaryActivityViewModel extends ViewModel
{
    private MutableLiveData<PaymentSummaryActivityViewModelResponse> mPanelResponse;

    public LiveData<PaymentSummaryActivityViewModelResponse> getCurrentPaymentSummaryListInformation(String userId) {

        // If the response is NULL, then load the INITIAL data...
        if (mPanelResponse == null)
        {
            mPanelResponse = new MutableLiveData<>();
            loadPaymentSummaryListInformation(userId,false);
        }

        return mPanelResponse;
    }

    private void loadPaymentSummaryListInformation(final String userId,final boolean downloadWS){
        final PaymentSummaryActivityViewModelResponse currentStatus = getCurrentPanelResponse();

        PaymentSummaryActivityViewModelResponse initialStatus = new PaymentSummaryActivityViewModelResponse();
        initialStatus.isLoading = true; // Set the Loading Status
        mPanelResponse.postValue(initialStatus); // This will trigger the activity.

        // -------------------------------------
        // Set the LOADING STATUS...
        // -------------------------------------
        Observable.defer(new Func0<Observable<PaymentSummaryActivityViewModelResponse>>() {
            @Override
            public Observable<PaymentSummaryActivityViewModelResponse> call()  {
                return Observable.just(PaymentSummaryActivityViewModelHelper.getPaymentSummaryList(userId,downloadWS));
            }
        }).subscribeOn(Schedulers.io()) // Code BEFORE is called on background thread...
                .observeOn(AndroidSchedulers.mainThread()) // Code AFTER is called on main thread...
                .subscribe(new Subscriber<PaymentSummaryActivityViewModelResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        PaymentSummaryActivityViewModelResponse response = new PaymentSummaryActivityViewModelResponse();
                        response.errorMessage               = "Ha ocurrido un error. Por favor intentar nuevamente.";
                        response.isLoading = false; // Set the Loading Status
                        mPanelResponse.postValue(response); // This will trigger in the activity.
                    }

                    @Override
                    public void onNext(PaymentSummaryActivityViewModelResponse response) {
                        // Show the Loading Status in UI.
                        response.isLoading = false; // Set the Loading Status
                        mPanelResponse.postValue(response);
                    }
                });

    }

    public PaymentSummaryActivityViewModelResponse getCurrentPanelResponse() {
        if(mPanelResponse != null && mPanelResponse.getValue() != null)
            return mPanelResponse.getValue();
        return new PaymentSummaryActivityViewModelResponse();
    }

    // Method to force the reload of the panel (and fragments).
    public void reloadPanels(String userId,boolean downloadFromWS){
        loadPaymentSummaryListInformation(userId,downloadFromWS);
    }

    public boolean isDownloadingFromWS(){
        if(mPanelResponse != null && mPanelResponse.getValue() != null)
            return mPanelResponse.getValue().isDownloadingFromWS;
        return false;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
