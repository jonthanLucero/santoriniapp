package com.example.santoriniapp.modules.dashboard;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.santoriniapp.repository.LoginRepository;
import com.example.santoriniapp.utils.UrbanizationGlobalUtils;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

public class DashboardMenuActivityViewModel extends ViewModel
{
    public static String TAG = "WWOrderPanelActivityViewModel";
    private MutableLiveData<DashboardMenuActivityViewModelResponse> mPanelResponse;

    private Subscription panelDataSubscription;

    // ----------------------------------------------------------------------------------
    // OBSERVER: Method called from Activity that get the current SalespersonIndicators status.
    // ----------------------------------------------------------------------------------
    public LiveData<DashboardMenuActivityViewModelResponse> getCurrentPanelData(String userId) {

        // If the response is NULL, then load the INITIAL data...
        if (mPanelResponse == null)
        {
            mPanelResponse = new MutableLiveData<>();
            getDashboardInformation(userId,false);
        }

        return mPanelResponse;
    }

    public void forceRefreshPanelData(String userId){
        getDashboardInformation(userId,false);
    }

    public void getDashboardInformation(final String userId,final boolean downloadWS){

        // Init Response (Initial Status show the Name and Imei always).
        DashboardMenuActivityViewModelResponse response = new DashboardMenuActivityViewModelResponse();

        response.isLoading = true;
        if(downloadWS)
        {
            response.isDownloading = true;
            response.isLoading  = false;
        }

        mPanelResponse.postValue(response);

        // Aborting any previous search.
        if (this.panelDataSubscription != null && !this.panelDataSubscription.isUnsubscribed()) {
            Log.d(TAG, "SearchSubscription has been forced stopped, to be restarted...");
            this.panelDataSubscription.unsubscribe();
        }

        // -------------------------------------
        // Set the LOADING STATUS...
        // -------------------------------------
        this.panelDataSubscription = Observable.defer(new Func0<Observable<DashboardMenuActivityViewModelResponse>>() {
            @Override
            public Observable<DashboardMenuActivityViewModelResponse> call()  {
                return Observable.just(DashboardMenuActivityViewModelHelper.getDashboardInformation(userId,downloadWS));
            }
        }).subscribeOn(Schedulers.io()) // Code BEFORE is called on background thread...
                .observeOn(AndroidSchedulers.mainThread()) // Code AFTER is called on main thread...
                .subscribe(new Subscriber<DashboardMenuActivityViewModelResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        DashboardMenuActivityViewModelResponse response = new DashboardMenuActivityViewModelResponse();
                        response.errorMessage               = "Ha ocurrido un error. Por favor intentar nuevamente.";
                        response.isLoading = false;
                        response.isDownloading = false;
                        mPanelResponse.postValue(response); // This will trigger in the activity.
                    }

                    @Override
                    public void onNext(DashboardMenuActivityViewModelResponse response) {

                        response.isLoading = false;
                        response.isDownloading = false;
                        mPanelResponse.postValue(response);
                    }
                });
    }

    public void syncInformation(String userId, boolean downloadWS)
    {
        getDashboardInformation(userId,downloadWS);
    }

    // -------------------------------------
    // Turn Off Flags methods
    // --------------------------------------
    public void turnOffPanelDataErrorMessage(){
        if(mPanelResponse !=null && mPanelResponse.getValue() != null)
            mPanelResponse.getValue().errorMessage = "";
    }
}
