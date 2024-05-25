package com.project.financemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.project.financemanager.adapters.SearchAdapter;
import com.project.financemanager.api.ApiService;
import com.project.financemanager.common.IClickListener;
import com.project.financemanager.models.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private SearchAdapter adapter;
    private RecyclerView recyclerView;
    private EditText inputSearch;
    private List<Transaction> transactionList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recyclerView = findViewById(R.id.rcvResultSearch);
        inputSearch = findViewById(R.id.inputSearch);


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
    }
}