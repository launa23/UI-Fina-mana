package com.project.financemanager.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.project.financemanager.LoginActivity;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService{
    private static ApiService instance;
    private IApiService iApiService;
    SharedPreferences sharedPreferences;
    private ApiService(Context context) {
        sharedPreferences = context.getSharedPreferences("CHECK_TOKEN", context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("token", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImxhdW4iLCJzdWIiOiJsYXVuIiwiZXhwIjoxNzE2NjA1MDczfQ.Wp2i7M8MhC6xeoF9cpFsP-6FhzjShFgKIgH4vXYgRZ4");
//        editor.apply();
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
        if (instance == null) {
            instance = new ApiService(context);
        }
        return new ApiService(context);
    }

    public static synchronized ApiService getInstance() {
        if (instance == null) {
            instance = new ApiService();
        }
        return new ApiService();
    }

    public IApiService getiApiService() {
        return iApiService;
    }
}
