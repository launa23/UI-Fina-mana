package com.project.financemanager.api;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.project.financemanager.models.TitleTime;
import com.project.financemanager.models.Total;
import com.project.financemanager.models.Wallet;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    Interceptor interceptor = chain -> {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        builder.addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImxhdW4iLCJzdWIiOiJsYXVuIiwiZXhwIjoxNzE1NTI0MDU1fQ.TkBvCVQuStxL4Vc166C1Z7RsOHBg6wvFPKUix2u3ZLA");
        return chain.proceed(builder.build());
    };

    OkHttpClient.Builder okBuilder = new OkHttpClient.Builder().addInterceptor(interceptor);

    ApiService apiService = new Retrofit.Builder()
            .baseUrl("http://192.168.1.7:8081/api/v1/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okBuilder.build())
            .build()
            .create(ApiService.class);

    @GET("transaction/monthandyear")
    Call<List<TitleTime>> getTransByMonthAndYear(@Query("month") int month, @Query("year") int year, @Query("walletId") long walletId);

    @GET("wallet/{id}")
    Call<Wallet> getWalletById(@Path("id") long walletId);

    @GET("wallet/mine")
    Call<List<Wallet>> getAllMyWallet();

    @GET("transaction/total")
    Call<Total> getTotalIncomeAndOutcome(@Query("month") int month, @Query("year") int year, @Query("walletId") long walletId);
}
