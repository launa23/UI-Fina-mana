package com.project.financemanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.project.financemanager.adapters.WalletAdapter;
import com.project.financemanager.api.ApiService;
import com.project.financemanager.api.IApiService;
import com.project.financemanager.models.Wallet;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    private ImageView btnBackInWallet;
    private ConstraintLayout layoutDialog;
    private boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        recyclerView = findViewById(R.id.rcvWallets);
        btnBackInWallet = findViewById(R.id.btnBackInWallet);
        layoutDialog = findViewById(R.id.layoutDialogInNotConnection);
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = networkInfo != null && networkInfo.isConnected();

        if (isConnected) {
            Call<List<Wallet>> call = ApiService.getInstance(getApplicationContext()).getiApiService().getAllMyWallet();
            call.enqueue(new Callback<List<Wallet>>() {
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
                    showAlertNotConnection();
                }
            });
        } else {
            showAlertNotConnection();
        }
        btnBackInWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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