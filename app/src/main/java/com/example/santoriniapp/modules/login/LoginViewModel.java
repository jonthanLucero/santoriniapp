package com.example.santoriniapp.modules.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.util.Log;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

public class LoginViewModel extends ViewModel
{
    public static String TAG = "DAOLoginViewModel";

    private MutableLiveData<LoginViewModelResponse> mResponse;
    private Subscription panelLoginSubscription;

    // ----------------------------------------------------------------------------------
    // OBSERVER: Method called from Activity that get the current SalespersonIndicators status.
    // ----------------------------------------------------------------------------------
    public LiveData<LoginViewModelResponse> getCurrentPanelData() {

        // If the response is NULL, then load the INITIAL data...
        if (mResponse == null)
        {
            mResponse = new MutableLiveData<>();
            loadPanelData();
        }

        return mResponse;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public void loadPanelData(){

        // Init Response (Initial Status show the Name and Imei always).
        LoginViewModelResponse response = new LoginViewModelResponse();
        //for the moment this will be empty
        loadLogin();
    }

    // -----------------------
    // Method to Login to WS
    // -----------------------
    public void loginToWS(final String userLogin, final String userPassword) {

        // Set Initial Status...
        LoginViewModelResponse initialStatus = new LoginViewModelResponse();
        initialStatus.isLoading             = true; // Set the Loading Status
        mResponse.postValue(initialStatus); // This will trigger the activity.

        // Aborting any previous search.
        if (this.panelLoginSubscription != null && !this.panelLoginSubscription.isUnsubscribed()) {
            Log.d(TAG, "panelLoginSubscription has been forced stopped, to be restarted...");
            this.panelLoginSubscription.unsubscribe();
        }

        // Began to load the data.
        this.panelLoginSubscription = Observable.defer(
                new Func0<Observable<LoginViewModelResponse>>() {
                    @Override
                    public Observable<LoginViewModelResponse> call() {
                        return Observable.just(LoginViewModelHelper.loginToWS(userLogin,userPassword));
                    }
                })
                .subscribeOn(Schedulers.io()) // Code BEFORE is called on background thread...
                .observeOn(AndroidSchedulers.mainThread()) // Code AFTER is called on main thread...
                .subscribe(new Subscriber<LoginViewModelResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        LoginViewModelResponse response = new LoginViewModelResponse();
                        response.isLoading              = false;
                        response.errorMessage           = "Ha ocurrido un error. Por favor intentar nuevamente.";
                        mResponse.postValue(response); // This will trigger the activity.
                    }

                    @Override
                    public void onNext(LoginViewModelResponse response) {
                        response.isLoading              = false;
                        mResponse.postValue(response); // This will trigger the activity.
                    }
                });
    }

    // -----------------------
    // Method to Login to WS
    // -----------------------
    public void loadLogin() {

        // Set Initial Status...
        LoginViewModelResponse initialStatus = new LoginViewModelResponse();
        initialStatus.isLoading             = false; // Set the Loading Status
        mResponse.postValue(initialStatus); // This will trigger the activity.
    }


    // ----------------------------------------------------------------
    //  Reset
    // ----------------------------------------------------------------
    public void resetErrorMessage(){
        if(mResponse != null && mResponse.getValue() != null)
            mResponse.getValue().errorMessage = "";
    }
}
