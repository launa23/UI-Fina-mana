package com.project.financemanager;

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
import java.util.ArrayList;
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
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedMonthYear", "Tháng này");
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
        lastMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedMonthYear", "Tháng trước");
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
        thisYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedMonthYear", "Năm nay");
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
        lastYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedMonthYear", "Năm trước");
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        allTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedMonthYear", "Toàn bộ thời gian");
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}