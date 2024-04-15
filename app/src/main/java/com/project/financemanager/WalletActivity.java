package com.project.financemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.project.financemanager.adapters.WalletAdapter;
import com.project.financemanager.api.ApiService;
import com.project.financemanager.models.Wallet;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    private ImageView btnBackInWallet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        recyclerView = findViewById(R.id.rcvWallets);
        btnBackInWallet = findViewById(R.id.btnBackInWallet);
        ApiService.apiService.getAllMyWallet().enqueue(new Callback<List<Wallet>>() {
            @Override
            public void onResponse(Call<List<Wallet>> call, Response<List<Wallet>> response) {
                List<Wallet> walletList = response.body();
                WalletAdapter walletAdapter = new WalletAdapter(walletList, new WalletAdapter.HandleClick() {
                    @Override
                    public void onItemClick(int position) {
                        Wallet wallet = walletList.get(position);
                        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(WalletActivity.this);
                        long data = wallet.getId();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putLong("idWallet", data);
                        editor.apply();

                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("idWallet", wallet.getId());
                        resultIntent.putExtra("nameWallet", wallet.getName());
                        setResult(Activity.RESULT_OK, resultIntent);

                        Toast.makeText(getApplicationContext(), wallet.getName(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(walletAdapter);
            }

            @Override
            public void onFailure(Call<List<Wallet>> call, Throwable throwable) {

            }
        });
        btnBackInWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}