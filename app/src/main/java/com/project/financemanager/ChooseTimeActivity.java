package com.project.financemanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.financemanager.api.ApiService;
import com.project.financemanager.api.IApiService;
import com.project.financemanager.dtos.TitleTime;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseTimeActivity extends AppCompatActivity {
    AlertDialog alertDialog;
    private ConstraintLayout layoutDialogLoading;
    private ImageView back;
    private TextView thisMonth;
    private TextView lastMonth;
    private TextView thisYear;
    private TextView lastYear;
    private SharedPreferences sharedPreferences;
    private TextView allTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_time);

        back = findViewById(R.id.btnBackInChooseTime);
        thisMonth = findViewById(R.id.thisMonth);
        lastMonth = findViewById(R.id.lastMonth);
        thisYear = findViewById(R.id.thisYear);
        lastYear = findViewById(R.id.lastYear);
        allTime = findViewById(R.id.allTime);
        layoutDialogLoading = findViewById(R.id.layoutDialogLoading);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        thisMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                callApiMonthAndYear("Tháng này", calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR));
            }
        });
        lastMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                callApiMonthAndYear("Tháng trước", calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
            }
        });
        thisYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                callApiMonthAndYear("Năm nay", 0, calendar.get(Calendar.YEAR));
            }
        });
        lastYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                callApiMonthAndYear("Năm trước", 0, calendar.get(Calendar.YEAR)-1);
            }
        });

        allTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                callApiMonthAndYear("Toàn bộ thời gian", 0, 0);
            }
        });
    }

    private void callApiMonthAndYear(String title, int month, int year){
        // Lấy id của ví dưới sharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        long idWallet = sharedPreferences.getLong("idWallet", 0);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("month", month);
        editor.putInt("year", year);
        editor.putString("titleMonthAndYear", title);
        editor.apply();

        Call<List<TitleTime>> call = ApiService.getInstance(getApplicationContext()).getiApiService().getTransByMonthAndYear(month, year, idWallet);
        call.enqueue(new Callback<List<TitleTime>>() {
            @Override
            public void onResponse(Call<List<TitleTime>> call, Response<List<TitleTime>> response) {
                List<TitleTime> titleTimeList = response.body();
                Bundle bundle = new Bundle();
                bundle.putSerializable("objectList", (Serializable) titleTimeList);

                Intent resultIntent = new Intent();
                resultIntent.putExtras(bundle);
                resultIntent.putExtra("month", title);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }

            @Override
            public void onFailure(Call<List<TitleTime>> call, Throwable throwable) {

            }
        });
    }

    private AlertDialog showLoadingDialog(AlertDialog alertDialog){
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.loading_progress_bar, layoutDialogLoading);

        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setView(view);
        builder.setCancelable(false);
        alertDialog = builder.create();

        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
        return alertDialog;
    }

    private void dismissLoadingDialog(AlertDialog alertDialog) {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        }, 1000);
    }
}