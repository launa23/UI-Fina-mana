package com.project.financemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.project.financemanager.api.ApiService;
import com.project.financemanager.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {
    Handler handler;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkLogin();
            }
        }, 700);
    }

    private void checkLogin(){
        sharedPreferences = getApplicationContext().getSharedPreferences("CHECK_TOKEN", getApplicationContext().MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        if (token.isEmpty()){
            Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Call<User> call = ApiService.getInstance(getApplicationContext()).getiApiService().getCurrentUser();
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()){
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("fullname", response.body().getFullName());
                        editor.putString("email", response.body().getEmail());

                        editor.apply();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable throwable) {

                }
            });
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}