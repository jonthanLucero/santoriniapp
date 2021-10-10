package com.example.santoriniapp.retrofit;

import com.example.santoriniapp.retrofit.dtos.loginDtos.LoginRequest;
import com.example.santoriniapp.retrofit.dtos.loginDtos.LoginResponse;
import com.example.santoriniapp.retrofit.dtos.paymentDtos.PaymentRequest;
import com.example.santoriniapp.retrofit.dtos.paymentDtos.PaymentResponse;
import com.example.santoriniapp.retrofit.dtos.paymentListDtos.PaymentListRequest;
import com.example.santoriniapp.retrofit.dtos.paymentListDtos.PaymentListResponse;
import com.example.santoriniapp.retrofit.dtos.syncDtos.SyncInformationRequest;
import com.example.santoriniapp.retrofit.dtos.syncDtos.SyncInformationResponse;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface
{
    // --------------------------------------------------------------------------------
    //  WS that are CONVERTED from SOAP to Retrofit.
    // --------------------------------------------------------------------------------

    @POST(WSNames.UserLoginWS)
    Call<LoginResponse> userLoginToWS(@Body LoginRequest request);

    @POST(WSNames.SyncInformationWS)
    Call<SyncInformationResponse> syncInformationWS(@Body SyncInformationRequest request);

    @POST(WSNames.PaymentSendWS)
    Call<PaymentResponse> paymentSendWS(@Body PaymentRequest request);

    @POST(WSNames.SyncPaymentInformationWS)
    Call<PaymentListResponse> syncPaymentInformationWS(@Body PaymentListRequest request);
}
