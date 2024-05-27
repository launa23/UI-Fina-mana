package com.project.financemanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.financemanager.api.ApiService;
import com.project.financemanager.api.IApiService;
import com.project.financemanager.dtos.TitleTime;
import com.project.financemanager.models.Transaction;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseTimeActivity extends AppCompatActivity {
    private AlertDialog alertDialog;
    private ConstraintLayout layoutDialogSelectTime;

    private ConstraintLayout layoutDialogLoading;
    private ImageView back;
    private TextView thisMonth;
    private TextView lastMonth;
    private TextView thisYear;
    private TextView lastYear;
    private int choose;
    private TextView option;
    private SharedPreferences sharedPreferences;
    private TextView allTime;
    private ConstraintLayout layoutDialog;
    private boolean isConnected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_time);
        choose = 0;
        back = findViewById(R.id.btnBackInChooseTime);
        thisMonth = findViewById(R.id.thisMonth);
        lastMonth = findViewById(R.id.lastMonth);
        thisYear = findViewById(R.id.thisYear);
        lastYear = findViewById(R.id.lastYear);
        allTime = findViewById(R.id.allTime);
        option = findViewById(R.id.option);
        layoutDialogSelectTime = findViewById(R.id.selectTimeAlert);
        layoutDialogLoading = findViewById(R.id.layoutDialogLoading);
        layoutDialog = findViewById(R.id.layoutDialogInNotConnection);

        Animation blinkAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink_animation);
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = networkInfo != null && networkInfo.isConnected();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        thisMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisMonth.startAnimation(blinkAnimation);
                Calendar calendar = Calendar.getInstance();
                callApiMonthAndYear("Tháng này", calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR));
            }
        });
        lastMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastMonth.startAnimation(blinkAnimation);
                Calendar calendar = Calendar.getInstance();
                callApiMonthAndYear("Tháng trước", calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
            }
        });
        thisYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisYear.startAnimation(blinkAnimation);
                Calendar calendar = Calendar.getInstance();
                callApiMonthAndYear("Năm nay", 0, calendar.get(Calendar.YEAR));
            }
        });
        lastYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastYear.startAnimation(blinkAnimation);
                Calendar calendar = Calendar.getInstance();
                callApiMonthAndYear("Năm trước", 0, calendar.get(Calendar.YEAR)-1);
            }
        });

        allTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allTime.startAnimation(blinkAnimation);
                Calendar calendar = Calendar.getInstance();
                callApiMonthAndYear("Toàn bộ thời gian", 0, 0);
            }
        });
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                option.startAnimation(blinkAnimation);
                showAlertDialog();
            }
        });
    }

    private void callApiMonthAndYear(String title, int month, int year){
        if(isConnected){
    //        alertDialog = showLoadingDialog(alertDialog);
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
        else {
            showAlertNotConnection();
        }
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
        }, 100);
    }
    private void showAlertNotConnection() {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.alert_no_connection, layoutDialog);
        Button btnOk = view.findViewById(R.id.alertBtnNotConnection);
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    private void showAlertDialog(){
        choose = 0;
        View view = LayoutInflater.from(this).inflate(R.layout.alert_select_time, layoutDialogSelectTime);
        TextView txt1 = view.findViewById(R.id.txt1);
        TextView txt2 = view.findViewById(R.id.txt2);
        RelativeLayout title1 = view.findViewById(R.id.title1);
        RelativeLayout title2 = view.findViewById(R.id.title2);
        DatePicker datePicker = view.findViewById(R.id.datepicker);
        TextView date1 = view.findViewById(R.id.date1);
        TextView date2 = view.findViewById(R.id.date2);
        TextView btnOk = view.findViewById(R.id.btnOK);
        TextView btnCancel = view.findViewById(R.id.btnCancel);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        title1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(choose == 1){
                    choose = 0;
                }
                title2.setBackgroundColor(Color.WHITE);
                txt2.setTextColor(Color.BLACK);
                date2.setTextColor(Color.BLACK);

                title1.setBackgroundColor(Color.parseColor("#02dac5"));
                txt1.setTextColor(Color.WHITE);
                date1.setTextColor(Color.WHITE);
            }
        });
        title2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(choose == 0){
                    choose = 1;
                }
                title1.setBackgroundColor(Color.WHITE);
                txt1.setTextColor(Color.BLACK);
                date1.setTextColor(Color.BLACK);

                title2.setBackgroundColor(Color.parseColor("#02dac5"));
                txt2.setTextColor(Color.WHITE);
                date2.setTextColor(Color.WHITE);
            }
        });
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if(choose == 0){
                    date1.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                }
                else {
                    date2.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                }
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pattern = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd");
                String start, end;
                Date dateTime1, dateTime2;
                try {
                    dateTime1 = sdf.parse(date1.getText().toString());
                    dateTime2 = sdf.parse(date2.getText().toString());
                    start = outputFormat.format(dateTime1);
                    end = outputFormat.format(dateTime2);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return;
                }
                if (dateTime1.before(dateTime2)) {
                    option.setText("Tùy chọn: " + date1.getText().toString() + " - " + date2.getText().toString());
                    Call<List<TitleTime>> call = ApiService.getInstance(getApplicationContext()).getiApiService().getTransByDateStartAndEnd(start, end);
                    call.enqueue(new Callback<List<TitleTime>>() {
                        @Override
                        public void onResponse(Call<List<TitleTime>> call, Response<List<TitleTime>> response) {
                            List<TitleTime> titleTimeList = response.body();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("objectList", (Serializable) titleTimeList);

                            Intent resultIntent = new Intent();
                            resultIntent.putExtras(bundle);
                            resultIntent.putExtra("month", "Từ: " + date1.getText().toString() + " - " + date2.getText().toString());
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<List<TitleTime>> call, Throwable throwable) {

                        }
                    });
                } else {
                    Toasty.warning(getApplicationContext(), "Ngày bắt đầu phải trước ngày kết thúc!", Toast.LENGTH_SHORT, true).show();
                }

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}