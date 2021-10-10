package com.example.santoriniapp.modules.payment.paymentheader;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.santoriniapp.utils.inalambrikAddPhotoGallery.InalambrikAddPhotoGalleryItem;
import java.util.Date;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

public class PaymentActivityViewModel extends ViewModel
{
    private MutableLiveData<PaymentActivityViewModelResponse> mPanelResponse;

    public LiveData<PaymentActivityViewModelResponse> getCurrentPaymentInformation(String userId,String mMode,Date paymentDate,String monthCode) {

        // If the response is NULL, then load the INITIAL data...
        if (mPanelResponse == null)
        {
            mPanelResponse = new MutableLiveData<>();
            loadPaymentInformation(userId,mMode,paymentDate,-1,-1,monthCode);
        }

        return mPanelResponse;
    }

    private void loadPaymentInformation(final String userId,final String mMode,final Date mPaymentDate,
                                        final int selectedMonthPositionSpinner,final int selectedPaymentTypePositionSpinner,
                                        final String monthCode){
        final PaymentActivityViewModelResponse currentStatus = getCurrentPanelResponse();

        // -------------------------------------
        // Set the LOADING STATUS...
        // -------------------------------------
        Observable.defer(new Func0<Observable<PaymentActivityViewModelResponse>>() {
            @Override
            public Observable<PaymentActivityViewModelResponse> call()  {
                return Observable.just(PaymentActivityViewModelHelper.getInitialLoadingStatus(userId,mMode,mPaymentDate, selectedMonthPositionSpinner,selectedPaymentTypePositionSpinner,monthCode));
            }
        }).subscribeOn(Schedulers.io()) // Code BEFORE is called on background thread...
                .observeOn(AndroidSchedulers.mainThread()) // Code AFTER is called on main thread...
                .subscribe(new Subscriber<PaymentActivityViewModelResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        PaymentActivityViewModelResponse response = new PaymentActivityViewModelResponse();
                        response.errorMessage               = "Ha ocurrido un error. Por favor intentar nuevamente.";
                        response.paymentTimeSpinnerPosition = currentStatus.paymentTimeSpinnerPosition;
                        response.paymentTypeSpinnerPosition = currentStatus.paymentTypeSpinnerPosition;
                        mPanelResponse.postValue(response); // This will trigger in the activity.

                        mPanelResponse.postValue(response); // This will trigger in the activity.
                    }

                    @Override
                    public void onNext(PaymentActivityViewModelResponse response) {
                        // Show the Loading Status in UI.

                        //if(selectedPaymentTypePositionSpinner != -1)
                        //    response.paymentTypeSpinnerPosition = currentStatus.paymentTypeSpinnerPosition;

                        mPanelResponse.postValue(response);
                    }
                });

    }

    // ----------------------------------------------------------------------------------
    // Method that SENDS the pending agendas to SERVER...
    // ----------------------------------------------------------------------------------
    public void saveAndSendPaymentToServer(final Date paymentDate){

        // ----------------------------------------------------------
        // Check if Current Response is available (Just-In-Case)
        // ----------------------------------------------------------
        if(mPanelResponse == null || mPanelResponse.getValue() == null){
            PaymentActivityViewModelResponse currentResponse = new PaymentActivityViewModelResponse();
            currentResponse.errorMessage = "Ha ocurrido un error. Por favor intente nuevamente.";
            mPanelResponse.postValue(currentResponse);  // Trigger Observer in Activity.
            return;
        }

        // ----------------------------------------------------------
        // Getting Previous Response safely.
        // ----------------------------------------------------------
        final PaymentActivityViewModelResponse currentResponse = mPanelResponse.getValue();

        // ----------------------------------------------------------
        // If we are currently sending to server, then return...
        // ----------------------------------------------------------
        if(currentResponse.isSendingPayment){
            mPanelResponse.postValue(currentResponse);
            return;
        }

        // ----------------------------------------------------------
        // Set Initial Load (and reset error message).
        // ----------------------------------------------------------
        currentResponse.isSendingPayment           = true;
        currentResponse.isSendingPaymentPhotos     = currentResponse.paymentPhotoList.size() > 0;
        currentResponse.errorMessage               = ""; // Empty Error Message.
        mPanelResponse.postValue(currentResponse); // Trigger Observer in Activity.

        // ----------------------------------------------------
        // Began to load the data.
        // ----------------------------------------------------
        Observable.defer(
                new Func0<Observable<PaymentActivityViewModelResponse>>() {
                    @Override
                    public Observable<PaymentActivityViewModelResponse> call() {
                        return Observable.just(PaymentActivityViewModelHelper.sendPaymentToServer(paymentDate,currentResponse));
                    }
                })
                .subscribeOn(Schedulers.io()) // Code BEFORE is called on background thread...
                .observeOn(AndroidSchedulers.mainThread()) // Code AFTER is called on main thread...
                .subscribe(new Subscriber<PaymentActivityViewModelResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        PaymentActivityViewModelResponse response = new PaymentActivityViewModelResponse();
                        response.isSendingPayment      = false;
                        response.errorMessage           = "Ha ocurrido un error. Por favor intentar nuevamente. (*)";
                        mPanelResponse.postValue(response);   // Trigger Observer in Activity.
                    }

                    @Override
                    public void onNext(PaymentActivityViewModelResponse response) {
                        response.isSendingPayment      = false;
                        response.isSendingPaymentPhotos        = false;
                        mPanelResponse.postValue(response);  // Trigger Observer in Activity.
                    }
                });
    }

    public void resetErrorMessage(){
        if(mPanelResponse != null && mPanelResponse.getValue() != null)
            mPanelResponse.getValue().errorMessage = "";
    }


    public PaymentActivityViewModelResponse getCurrentPanelResponse() {
        if(mPanelResponse != null && mPanelResponse.getValue() != null)
            return mPanelResponse.getValue();
        return new PaymentActivityViewModelResponse();
    }

    public void setSelectedPaymentMonthPositionSpinner(int selectedPosition) {
        if(mPanelResponse != null && mPanelResponse.getValue() != null && mPanelResponse.getValue().paymentTimeSpinnerPosition != selectedPosition)
        {
            mPanelResponse.getValue().paymentTimeSpinnerPosition = selectedPosition;
            mPanelResponse.getValue().paymentDateMonthCode = mPanelResponse.getValue().paymentListSpinnerList.get(selectedPosition).timeCode;
        }
    }

    public void setSelectedPaymentTypePositionSpinner(int selectedPosition) {
        if(mPanelResponse != null && mPanelResponse.getValue() != null && mPanelResponse.getValue().paymentTypeSpinnerPosition != selectedPosition)
        {
            mPanelResponse.getValue().paymentTypeSpinnerPosition = selectedPosition;
            mPanelResponse.getValue().paymentTypeCode = mPanelResponse.getValue().paymentTypeListSpinnerList.get(selectedPosition).paymentTypeCode;
        }
    }

    // -----------------------------------------------------------------------------------
    // Update Methods (To get the values set on the EditText and save it on the LiveData)
    // -----------------------------------------------------------------------------------
    public void updatePaymentAmount(double paymentAmount){
        if(mPanelResponse!=null && mPanelResponse.getValue() != null) mPanelResponse.getValue().paymentAmount = paymentAmount;
    }

    public void updatePaymentPhotos(List<InalambrikAddPhotoGalleryItem> photoList){
        if(mPanelResponse!=null && mPanelResponse.getValue() != null) mPanelResponse.getValue().paymentPhotoList = photoList;
    }

    public void updatePaymentCommentary(String paymentCommentary){
        if(mPanelResponse!=null && mPanelResponse.getValue() != null) mPanelResponse.getValue().paymentCommentary = paymentCommentary;
    }

    /*
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
    public void reloadPanels(String requestCode){
        loadPaymentListInformation(requestCode,getSelectedPositionSpinner());
    }

     */
}
