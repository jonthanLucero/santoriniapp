package com.example.santoriniapp.retrofit;

import android.os.Build;
import android.util.Log;

import com.example.santoriniapp.utils.UrbanizationConstants;
import com.example.santoriniapp.utils.TLSSocketFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator
{
    public static String TAG = "ServiceGenerator";

    private static Retrofit retrofit = null;
    private static Gson gson = new GsonBuilder().create();
    //private static String BASE_API_URL = "http://10.0.2.2";
    //private static String BASE_API_URL = "http://127.0.0.1";
    //private static String BASE_API_URL = "http://192.168.100.124:8080";
    //private static String BASE_API_URL = "http://192.168.100.58:8080";
    //private static String BASE_API_URL = "http://192.168.100.10:8080";
    private static String BASE_API_URL = "https://urbanidata.com";

    public static final boolean IS_IN_PRODUCTION = false;
    public static final boolean IS_USING_PRIMARY_SERVERS_BY_DEFAULT = false; //pedidos.inalambrik.com.ec or 192.168.1.214 depending on the value

    public static <T> T createService(Class<T> serviceClass){
        return setRetrofit(serviceClass);
    }

    private static <T> T setRetrofit(Class<T> serviceClass){

        // Getting previous Domain URL set on the retrofit previous instance.
        String previousRetrofitUrl = retrofit != null ? retrofit.baseUrl().url().toString().trim() : "NONE";
        if(!previousRetrofitUrl.isEmpty() && previousRetrofitUrl.endsWith("/")) previousRetrofitUrl = previousRetrofitUrl.substring(0,previousRetrofitUrl.length() - 1);


        if(retrofit == null
                || !previousRetrofitUrl.trim().equalsIgnoreCase(BASE_API_URL)) {

            Log.i(TAG,"Changing Retrofit instance to new URL: " + BASE_API_URL + "...");

            // By now. Timeout fixed in 3min.
            // TODO: Need to fix the change of timeout. For now is fixed on 180 seconds.
            final OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder()
                    .readTimeout(180, TimeUnit.SECONDS)
                    .connectTimeout(180, TimeUnit.SECONDS);

            // ---------------------------------
            // If we are in LOCALHOST...
            // ---------------------------------
            if (BASE_API_URL.equalsIgnoreCase(UrbanizationConstants.APP_LOCAL_URL)) {

                // Set LoggingInterceptor to allow
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


                okHttpBuilder.addInterceptor(interceptor);
            } else {

                // ---------------------------------
                // If we are in PRODUCTION (pedidos.inalambrik.com.ec) AND the Android Version
                // is less or equals than API 19 (Android 4.4) we force to connect to TLS 1.2
                // ---------------------------------
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                    try {
                        okHttpBuilder.sslSocketFactory(new TLSSocketFactory());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }

            // Finally we build the client with previous setup.
            final OkHttpClient okHttpClient = okHttpBuilder.build();

            // Now we build the Retrofit Object with the client setup above.
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_API_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
        }

        Log.i(TAG,"Checking WS in server: " + BASE_API_URL + "...");

        return retrofit.create(serviceClass);
    }
}
