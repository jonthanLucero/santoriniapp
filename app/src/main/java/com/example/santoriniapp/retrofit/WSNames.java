package com.example.santoriniapp.retrofit;

public class WSNames
{
    public static final String baseUrl = "/api/";

    // ------------------------------------------------
    // Retrofit WS URLs.
    // NOTE: Add here all new ws urls for Retrofit.
    // ------------------------------------------------

    // Login WS
    public static final String UserLoginWS                  = baseUrl + "UserLoginWS.php";

    // Sync WS
    public static final String SyncInformationWS            = baseUrl + "SyncInformationWS.php";

    // Payment Send with photos WS
    public static final String PaymentSendWS                = baseUrl + "PaymentSendWS.php";

    // Payment Sync Information
    public static final String SyncPaymentInformationWS     = baseUrl + "PaymentSyncWS.php";
}
