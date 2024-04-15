package com.project.financemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.project.financemanager.adapters.WalletAdapter;
import com.project.financemanager.api.ApiService;
import com.project.financemanager.models.Wallet;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        recyclerView = findViewById(R.id.rcvWallets);
        ApiService.apiService.getAllMyWallet().enqueue(new Callback<List<Wallet>>() {
            @Override
            public void onResponse(Call<List<Wallet>> call, Response<List<Wallet>> response) {
                List<Wallet> walletList = response.body();
                WalletAdapter walletAdapter = new WalletAdapter(walletList);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(walletAdapter);
            }

            @Override
            public void onFailure(Call<List<Wallet>> call, Throwable throwable) {

            }
        });

    }
}