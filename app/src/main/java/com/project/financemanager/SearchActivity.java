package com.project.financemanager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.project.financemanager.adapters.SearchAdapter;
import com.project.financemanager.api.ApiService;
import com.project.financemanager.common.IClickListener;
import com.project.financemanager.models.Transaction;
import com.project.financemanager.models.Wallet;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private SearchAdapter adapter;
    private RecyclerView recyclerView;
    private ImageView btnCloseInSearch;
    private EditText inputSearch;
    private List<Transaction> transactionList, finalTransactions;
    private ActivityResultLauncher<Intent> launcherforEdit;
    private int iPosition, fPosition, count, flag;
    private boolean isConnected;
    private ConstraintLayout layoutDialog;

    private HashMap<Integer, Integer> mapPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initResultLauncher();
        recyclerView = findViewById(R.id.rcvResultSearch);
        layoutDialog = findViewById(R.id.layoutDialogInNotConnection);
        inputSearch = findViewById(R.id.inputSearch);
        btnCloseInSearch = findViewById(R.id.btnCloseInSearch);
        mapPosition = new HashMap<>();
        flag = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = networkInfo != null && networkInfo.isConnected();
        if (isConnected) {
            Call<List<Transaction>> call = ApiService.getInstance(getApplicationContext()).getiApiService().getAllTransaction();
            call.enqueue(new Callback<List<Transaction>>() {
                @Override
                public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                    List<Transaction> transactions = response.body();
                    transactionList = response.body();
                    adapter = new SearchAdapter(transactions, getApplicationContext());
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter.setClickListener(new IClickListener() {
                        @Override
                        public void onClickListener(int position) {
                            iPosition = position;
                            fPosition = position;
                            Intent i = new Intent(SearchActivity.this, InsertAndUpdateTransaction.class);
                            i.putExtra("transaction", transactions.get(position));
                            i.putExtra("flag", "1");
                            launcherforEdit.launch(i);
                        }
                    });
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<List<Transaction>> call, Throwable throwable) {
                    Log.e("error:", throwable.getMessage());
                }
            });
        } else {
            showAlertNotConnection();
        }

        btnCloseInSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent();
                t.putExtra("flag", flag);
                setResult(Activity.RESULT_OK, t);
                finish();
            }
        });
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    mapPosition.clear();
                    String keyword = s.toString().trim();
                    List<Transaction> transactions = new ArrayList<>();
                    if (transactionList != null) {
                        if (keyword.isEmpty()) {
                            transactions = transactionList;
                        } else {
                            count = 0;
                            keyword = keyword.toLowerCase(Locale.getDefault());
                            for (Transaction transaction : transactionList) {
                                if (transaction.getDescription().toLowerCase(Locale.getDefault()).contains(keyword)
                                        || transaction.getCategoryName().toLowerCase(Locale.getDefault()).contains(keyword)) {
                                    transactions.add(transaction);
                                    mapPosition.put(count, transactionList.indexOf(transaction));
                                    count++;
                                }
                            }
                        }

                    }
                    finalTransactions = transactions;
                    adapter.setClickListener(new IClickListener() {
                        @Override
                        public void onClickListener(int position) {
                            if (mapPosition.get(position) != null) {
                                iPosition = mapPosition.get(position);
                                fPosition = position;
                            } else {
                                iPosition = position;
                                fPosition = position;
                            }
                            Intent i = new Intent(SearchActivity.this, InsertAndUpdateTransaction.class);
                            i.putExtra("transaction", finalTransactions.get(position));
                            i.putExtra("flag", "1");
                            launcherforEdit.launch(i);
                        }
                    });
                    adapter.setTransactions(finalTransactions);
                } catch (Exception ex) {
                    Log.e("error:", ex.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    // khi nhan nut back mac dinh thi dong search view
    @Override
    public void onBackPressed() {
        if (inputSearch.hasFocus()) {
            inputSearch.clearFocus();
        } else {
            Intent t = new Intent();
            t.putExtra("flag", flag);
            setResult(Activity.RESULT_OK, t);
            finish();
        }
    }

    private void initResultLauncher() {
        try {
            launcherforEdit = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                //edit data
                if (result != null && result.getResultCode() == Activity.RESULT_OK) {
                    flag = 1;
                    int flagUD = result.getData().getIntExtra("flagUD", 0);
                    if (flagUD == 2) {
                        //lay ve trans tai day va cap nhat len giao dien
                        Transaction t = (Transaction) result.getData().getSerializableExtra("transDelete");
                        //cap nhat tren giao dien
                        if (transactionList.equals(finalTransactions)) { //nhap khoang trang
                            transactionList.remove(fPosition);
                        } else {
                            if (finalTransactions != null) { // co nhap text
                                transactionList.remove(iPosition);
                                finalTransactions.remove(fPosition);
                            } else {// khong nhap gi
                                transactionList.remove(fPosition);
                            }
                        }
                        adapter.notifyItemRemoved(fPosition);
                        Alerter.create(this)
                                .setTitle("Xóa giao dịch thành công!")
                                .enableSwipeToDismiss()
                                .setIcon(R.drawable.ic_baseline_check_circle_24)
                                .setBackgroundColorRes(R.color.green)
                                .setIconColorFilter(0)
                                .setIconSize(R.dimen.icon_alert)
                                .show();
                    } else if (flagUD == 1) {
                        //lay ve trans tai day va cap nhat len giao dien
                        Transaction t = (Transaction) result.getData().getSerializableExtra("transUpdate");
                        //cap nhat tren giao dien
                        if (transactionList.equals(finalTransactions)) { //nhap khoang trang
                            transactionList.set(fPosition, t);
                        } else {
                            if (finalTransactions != null) { // co nhap text
                                transactionList.set(iPosition, t);
                                finalTransactions.set(fPosition, t);
                            } else {// khong nhap gi
                                transactionList.set(fPosition, t);
                            }
                        }
                        adapter.notifyItemChanged(fPosition);
                        Alerter.create(this)
                                .setTitle("Sửa giao dịch thành công!")
                                .enableSwipeToDismiss()
                                .setIcon(R.drawable.ic_baseline_check_circle_24)
                                .setBackgroundColorRes(R.color.green)
                                .setIconColorFilter(0)
                                .setIconSize(R.dimen.icon_alert)
                                .show();
                    }
                } else {
                    Log.e("editTransaction", "failed");
                }
            });

        } catch (Exception ex) {
            Log.e("initResultLauncher", Objects.requireNonNull(ex.getMessage()));
        }
    }

    private void showAlertNotConnection() {
        View view = LayoutInflater.from(this).inflate(R.layout.alert_no_connection, layoutDialog);
        Button btnOk = view.findViewById(R.id.alertBtnNotConnection);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}