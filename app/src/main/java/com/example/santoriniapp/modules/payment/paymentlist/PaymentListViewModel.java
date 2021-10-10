package com.example.santoriniapp.modules.payment.paymentlist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

public class PaymentListViewModel extends ViewModel
{

    private MutableLiveData<PaymentListViewModelResponse> mPanelResponse;

    public LiveData<PaymentListViewModelResponse> getCurrentPaymentListInformation(String userId,String requestCode) {

        // If the response is NULL, then load the INITIAL data...
        if (mPanelResponse == null)
        {
            mPanelResponse = new MutableLiveData<>();
            loadPaymentListInformation(userId,requestCode,-1,false);
        }

        return mPanelResponse;
    }

    private void loadPaymentListInformation(final String userId,final String requestCode, final int selectedPositionSpinner, final boolean downloadFromWS){
        final PaymentListViewModelResponse currentStatus = getCurrentPanelResponse();

        PaymentListViewModelResponse initialStatus = new PaymentListViewModelResponse();
        initialStatus.isLoading = true; // Set the Loading Status
        initialStatus.currentTimeSpinnerPosition = currentStatus.currentTimeSpinnerPosition; //selectedPositionSpinner;
        initialStatus.paymentListSpinnerList = currentStatus.paymentListSpinnerList;
        mPanelResponse.postValue(initialStatus); // This will trigger the activity.

        // -------------------------------------
        // Set the LOADING STATUS...
        // -------------------------------------
        Observable.defer(new Func0<Observable<PaymentListViewModelResponse>>() {
            @Override
            public Observable<PaymentListViewModelResponse> call()  {
                return Observable.just(PaymentListViewModelHelper.getPaymentListHeaderAndDetail(userId,requestCode,selectedPositionSpinner,downloadFromWS));
            }
        }).subscribeOn(Schedulers.io()) // Code BEFORE is called on background thread...
                .observeOn(AndroidSchedulers.mainThread()) // Code AFTER is called on main thread...
                .subscribe(new Subscriber<PaymentListViewModelResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        PaymentListViewModelResponse response = new PaymentListViewModelResponse();
                        response.errorMessage               = "Ha ocurrido un error. Por favor intentar nuevamente.";
                        response.isLoading = false; // Set the Loading Status
                        response.currentTimeSpinnerPosition = currentStatus.currentTimeSpinnerPosition;
                        mPanelResponse.postValue(response); // This will trigger in the activity.
                    }

                    @Override
                    public void onNext(PaymentListViewModelResponse response) {
                        // Show the Loading Status in UI.
                        response.isLoading = false; // Set the Loading Status
                        if(selectedPositionSpinner != - 1)
                            response.currentTimeSpinnerPosition = currentStatus.currentTimeSpinnerPosition;
                        mPanelResponse.postValue(response);


                    }
                });

    }

    public PaymentListViewModelResponse getCurrentPanelResponse() {
        if(mPanelResponse != null && mPanelResponse.getValue() != null)
            return mPanelResponse.getValue();
        return new PaymentListViewModelResponse();
    }

    public void setSelectedPositionSpinner(int selectedPosition) {
        if(mPanelResponse != null && mPanelResponse.getValue() != null && mPanelResponse.getValue().currentTimeSpinnerPosition != selectedPosition)
        {
            mPanelResponse.getValue().currentTimeSpinnerPosition = selectedPosition;
            mPanelResponse.getValue().isReload = true;
            mPanelResponse.getValue().paymentDateRequestCode = mPanelResponse.getValue().paymentListSpinnerList.get(selectedPosition).timeCode;
        }
    }

    public int getSelectedPositionSpinner() {
        if(mPanelResponse != null && mPanelResponse.getValue() != null)
            return mPanelResponse.getValue().currentTimeSpinnerPosition;
        return 0;
    }

    public String getSelectedPositionItemRequestCode() {
        if(mPanelResponse != null && mPanelResponse.getValue() != null)
            return  mPanelResponse.getValue().paymentDateRequestCode;
        return "01";
    }

    public boolean isReload() {
        if(mPanelResponse != null && mPanelResponse.getValue() != null)
            return mPanelResponse.getValue().isReload;

        return false;
    }

    public void resetErrorMessage(){
        if(mPanelResponse != null && mPanelResponse.getValue() != null)
            mPanelResponse.getValue().errorMessage = "";
    }

    // Method to force the reload of the panel (and fragments).
    public void reloadPanels(String userId,String requestCode,boolean downloadFromWS){
        loadPaymentListInformation(userId,requestCode,getSelectedPositionSpinner(),downloadFromWS);
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
