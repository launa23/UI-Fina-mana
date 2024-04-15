package com.project.financemanager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.financemanager.adapters.TitleAdapter;
import com.project.financemanager.api.ApiService;
import com.project.financemanager.models.TitleTime;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseTimeActivity extends AppCompatActivity {
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
        String idWallet = sharedPreferences.getString("idWallet", "");
        ApiService.apiService.getTransByMonthAndYear(month, year, Integer.parseInt(idWallet)).enqueue(new Callback<List<TitleTime>>() {
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
}