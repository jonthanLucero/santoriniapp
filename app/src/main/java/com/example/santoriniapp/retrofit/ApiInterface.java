package com.example.santoriniapp.retrofit;

import com.example.santoriniapp.retrofit.dtos.loginDtos.LoginRequest;
import com.example.santoriniapp.retrofit.dtos.loginDtos.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface
{
    // --------------------------------------------------------------------------------
    //  WS that are CONVERTED from SOAP to Retrofit.
    // --------------------------------------------------------------------------------
    @POST(WSNames.UserLoginWS)
    Call<LoginResponse> userLoginToWS(@Body LoginRequest request);
}
