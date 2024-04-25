package com.project.financemanager.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.project.financemanager.dtos.LoginResponse;
import com.project.financemanager.dtos.TitleTime;
import com.project.financemanager.dtos.Total;
import com.project.financemanager.dtos.UserLogin;
import com.project.financemanager.models.Category;
import com.project.financemanager.models.Transaction;
import com.project.financemanager.models.User;
import com.project.financemanager.models.Wallet;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.project.financemanager.LoginActivity;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class ApiService{
    private static ApiService instance;
    private IApiService iApiService;
    SharedPreferences sharedPreferences;
    private ApiService(Context context) {
        sharedPreferences = context.getSharedPreferences("CHECK_TOKEN", context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        Interceptor interceptor = chain -> {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            builder.addHeader("Authorization", token);
            return chain.proceed(builder.build());
        };

        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder().addInterceptor(interceptor);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(iApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okBuilder.build())
                .build();
        iApiService = retrofit.create(IApiService.class);
    }

    private ApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(iApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        iApiService = retrofit.create(IApiService.class);
    }

    public static synchronized ApiService getInstance(Context context) {
        if (instance != null) {
            return instance;
        }
        return new ApiService(context);
    }

    public static synchronized ApiService getInstance() {
        if (instance != null) {
            return instance;
        }
        return new ApiService();
    }


    public IApiService getiApiService() {
        return iApiService;
    }
}
