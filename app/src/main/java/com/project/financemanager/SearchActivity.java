package com.project.financemanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.financemanager.adapters.SearchAdapter;
import com.project.financemanager.api.ApiService;
import com.project.financemanager.common.IClickListener;
import com.project.financemanager.models.Transaction;
import com.tapadoo.alerter.Alerter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private SearchAdapter adapter;
    private RecyclerView recyclerView;
    private EditText inputSearch;
    private List<Transaction> transactionList;
    private int choose;
    private ConstraintLayout layoutDialog;
    private RelativeLayout selectTimeBtn;
    private TextView timeFromTo;
    private List<Transaction> transactionsRoot;
    private TextView txtEmpty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recyclerView = findViewById(R.id.rcvResultSearch);
        inputSearch = findViewById(R.id.inputSearch);
        layoutDialog = findViewById(R.id.selectTimeAlert);
        selectTimeBtn = findViewById(R.id.selectTimeBtn);
        timeFromTo = findViewById(R.id.timeFromTo);
        txtEmpty = findViewById(R.id.txtEmpty);
        Call<List<Transaction>> call = ApiService.getInstance(getApplicationContext()).getiApiService().getAllTransaction();
        call.enqueue(new Callback<List<Transaction>>() {
            @Override
            public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                List<Transaction> transactions = response.body();
                transactionList = response.body();
                transactionsRoot = response.body();
                adapter = new SearchAdapter(transactions, getApplicationContext());
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter.setClickListener(new IClickListener() {
                    @Override
                    public void onClickListener(int position) {
                        Toast.makeText(getApplicationContext(), transactions.get(position).getCategoryName(), Toast.LENGTH_SHORT).show();
                    }
                });
//                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Transaction>> call, Throwable throwable) {

            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString().trim();
                List<Transaction> transactions = new ArrayList<>();
                if(transactionList != null){
                    if (keyword.isEmpty()) {
                        transactions = transactionList;
                    } else {
                        keyword = keyword.toLowerCase(Locale.getDefault());
                        for (Transaction transaction : transactionList) {
                            if (transaction.getDescription().toLowerCase(Locale.getDefault()).contains(keyword)
                                    || transaction.getCategoryName().toLowerCase(Locale.getDefault()).contains(keyword)) {
                                transactions.add(transaction);
                            }
                        }
                    }

                }
                if(transactions.isEmpty()){
                    txtEmpty.setVisibility(View.VISIBLE);
                }
                else {
                    txtEmpty.setVisibility(View.GONE);
                }
                List<Transaction> finalTransactions = transactions;
                adapter.setClickListener(new IClickListener() {
                    @Override
                    public void onClickListener(int position) {
                        Toast.makeText(getApplicationContext(), finalTransactions.get(position).getCategoryName(), Toast.LENGTH_SHORT).show();
                    }
                });
                adapter.setTransactions(transactions);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        selectTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
//                Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showAlertDialog(){
        choose = 0;
        View view = LayoutInflater.from(this).inflate(R.layout.alert_select_time, layoutDialog);
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
                String pattern2 = "yyyy-MM-dd'T'HH:mm:ss";
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                SimpleDateFormat sdf2 = new SimpleDateFormat(pattern2, Locale.getDefault());
                Date dateTime1, dateTime2, dateTime3;
                try {
                    dateTime1 = sdf.parse(date1.getText().toString());
                    dateTime2 = sdf.parse(date2.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                    return;
                }
                if (dateTime1.before(dateTime2)) {
                    timeFromTo.setText(date1.getText().toString() + " - " + date2.getText().toString());
                    List<Transaction> list = new ArrayList<>();
                    try {
                        for (Transaction tr: transactionsRoot) {
                            dateTime3 = sdf2.parse(tr.getTime());
                            if (dateTime1.before(dateTime3) && dateTime2.after(dateTime3)){
                                list.add(tr);
                            }
                        }

                        transactionList = list;
                        if(transactionList.isEmpty()){
                            txtEmpty.setVisibility(View.VISIBLE);
                        }
                        else {
                            txtEmpty.setVisibility(View.GONE);
                        }
                        adapter.setTransactions(transactionList);
                        alertDialog.dismiss();

                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
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