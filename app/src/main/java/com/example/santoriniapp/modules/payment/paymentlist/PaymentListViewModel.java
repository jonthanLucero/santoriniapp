package com.example.santoriniapp.modules.payment.paymentlist;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.santoriniapp.modules.payment.PaymentHelper;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

public class PaymentListViewModel extends ViewModel
{

    private MutableLiveData<PaymentListViewModelResponse> mPanelResponse;

    public LiveData<PaymentListViewModelResponse> getCurrentPaymentListInformation(String requestCode) {

        // If the response is NULL, then load the INITIAL data...
        if (mPanelResponse == null)
        {
            mPanelResponse = new MutableLiveData<>();
            loadPaymentListInformation(requestCode,-1);
        }

        return mPanelResponse;
    }

    private void loadPaymentListInformation(final String requestCode, final int selectedPositionSpinner){
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
                return Observable.just(PaymentListViewModelHelper.getPaymentListHeaderAndDetail(requestCode,selectedPositionSpinner));
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
                        //response.salesPersonSummaryTimeCount = currentStatus.salesPersonSummaryTimeCount;
                        mPanelResponse.postValue(response); // This will trigger in the activity.
                    }

                    @Override
                    public void onNext(PaymentListViewModelResponse response) {
                        // Show the Loading Status in UI.
                        response.isLoading = false; // Set the Loading Status
                        if(selectedPositionSpinner != - 1)
                            response.currentTimeSpinnerPosition = currentStatus.currentTimeSpinnerPosition;
                        //response.salesPersonSummaryTimeCount = currentStatus.salesPersonSummaryTimeCount;
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
    public void reloadPanels(String requestCode){
        loadPaymentListInformation(requestCode,getSelectedPositionSpinner());
    }


    /*
    public static String TAG = "PaymentListViewModel";

    private MutableLiveData<PaymentListViewModelResponse> mResponse;

    private String mTextToSearch = "";

    private Subscription searchSubscription;
    private Subscription downloadSubscription;

    // ----------------------------------------------------------------------------------
    // Method called from Activity with OBSERVER (to detect changes).
    // Returns the response to the Activity.
    // ----------------------------------------------------------------------------------
    public LiveData<PaymentListViewModelResponse> getPaymentPanelCurrentStatus(String customerCode) {

        // If the response is NULL, then make the request...
        if (mResponse == null)
        {
            mResponse       = new MutableLiveData<>();
            mTextToSearch   = ""; // Be default empty string.
            loadPaymentList(customerCode,mTextToSearch,false);
        }

        return mResponse;
    }

    // ----------------------------------------------------------------------------------
    // Method called from Activity.
    // Used to force Load of the list. Will be called when "Refresh" in the Activity is pressed.
    // ----------------------------------------------------------------------------------
    public void reloadList(String customerCode,boolean forceSearch, String newTextToSearch){
        if(forceSearch || !mTextToSearch.equalsIgnoreCase(newTextToSearch)) {
            mTextToSearch = newTextToSearch;
            loadPaymentList(customerCode,mTextToSearch,false);
        }
    }

    // ----------------------------------------------------------------------------------
    // Method called from Activity.
    // Loads from WS.
    // ----------------------------------------------------------------------------------
    public void loadFromWS(String customerCode){
        mTextToSearch = "";
        loadPaymentList(customerCode,mTextToSearch,true);
    }

    // ----------------------------------------------------------------------------------
    // Method that load the response (Load List from DB)
    // ----------------------------------------------------------------------------------
    private void loadPaymentList(final String customerCode, final String textToSearch, final boolean loadFromWs) {

        // Set Initial Status...
        PaymentListViewModelResponse initialStatus = new PaymentListViewModelResponse();
        initialStatus.isLoading             = true; // Set the Loading Status
        //initialStatus.isDownloadingFromWS   = loadFromWs;
        mResponse.postValue(initialStatus); // This will trigger the activity.

        // Aborting any previous search.
        if (this.searchSubscription != null && !this.searchSubscription.isUnsubscribed()) {
            Log.d(TAG, "SearchSubscription has been forced stopped, to be restarted...");
            this.searchSubscription.unsubscribe();
        }

        // Began to load the data.
        this.searchSubscription = Observable.defer(
                new Func0<Observable<PaymentListViewModelResponse>>() {
                    @Override
                    public Observable<PaymentListViewModelResponse> call() {
                        return Observable.just(PaymentHelper.getPaymentList(customerCode,textToSearch,loadFromWs));
                    }
                })
                .subscribeOn(Schedulers.io()) // Code BEFORE is called on background thread...
                .observeOn(AndroidSchedulers.mainThread()) // Code AFTER is called on main thread...
                .subscribe(new Subscriber<PaymentListViewModelResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        PaymentListViewModelResponse response = new PaymentListViewModelResponse();
                        response.isLoading              = false;
                        //response.isDownloadingFromWS    = false;
                        response.errorMessage           = "Ha ocurrido un error. Por favor intentar nuevamente.";
                        mResponse.postValue(response); // This will trigger the activity.
                    }

                    @Override
                    public void onNext(PaymentListViewModelResponse response) {
                        Log.i(TAG,"VIEWMODEL: loadOrderFollowUpList() ends FINE...");
                        response.isLoading              = false;
                        //response.isDownloadingFromWS    = false;
                        mResponse.postValue(response); // This will trigger the activity.
                    }
                });
    }
    */


    // ----------------------------------------------------------------
    //  Getters
    // ----------------------------------------------------------------

    /*
    public boolean isDownloadingFromWS(){
        return false;

        if(mResponse != null && mResponse.getValue() != null)
            return mResponse.getValue().isDownloadingFromWS;
        return false;


    }

    public String getTextToSearch(){
        if(mTextToSearch != null)
            return mTextToSearch;
        return "";
    }

    // ----------------------------------------------------------------
    //  Reset
    // ----------------------------------------------------------------
    public void resetErrorMessage(){
        if(mResponse != null && mResponse.getValue() != null)
            mResponse.getValue().errorMessage = "";
    }

    public void resetErrorMessageFromLoadingFromWS(){
        if(mResponse != null && mResponse.getValue() != null)
            mResponse.getValue().errorMessage="";// errorMessageFromLoadingFromWS = "";
    }



    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i(TAG,"VIEWMODEL: OnCleared is called...");
    }
    */
}
