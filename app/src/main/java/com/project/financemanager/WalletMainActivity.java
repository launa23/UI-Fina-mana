package com.project.financemanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.financemanager.adapters.WalletAdapter;
import com.project.financemanager.api.ApiService;
import com.project.financemanager.dtos.WalletDTO;
import com.project.financemanager.models.Wallet;
import com.tapadoo.alerter.Alerter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletMainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageView btnBackInWalletMain, btnAddInWalletMain;
    private WalletAdapter walletAdapter;
    private ActivityResultLauncher<Intent> launcherforAdd;
    private ActivityResultLauncher<Intent> launcherforEdit;
    private int iPosition;
    private List<Wallet> walletList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_main);
        recyclerView = findViewById(R.id.rcvWalletsMain);
        btnBackInWalletMain = findViewById(R.id.btnBackInWalletMain);
        btnAddInWalletMain = findViewById(R.id.btnAddInWalletMain);
        initResultLauncher();
        Call<List<Wallet>> call = ApiService.getInstance(getApplicationContext()).getiApiService().getAllMyWallet();
        call.enqueue(new Callback<List<Wallet>>() {
            @Override
            public void onResponse(Call<List<Wallet>> call, Response<List<Wallet>> response) {
                walletList = response.body();
                walletAdapter = new WalletAdapter(walletList, new WalletAdapter.HandleClick() {
                    @Override
                    public void onItemClick(int position) {
                        Wallet wallet = walletList.get(position);
                        iPosition = position;
                        Intent i = new Intent(WalletMainActivity.this, InsertAndUpdateWallet.class);
                        i.putExtra("wallet", wallet);
                        i.putExtra("flag", "1");
                        launcherforEdit.launch(i);
                    }
                });
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(walletAdapter);
            }

            @Override
            public void onFailure(Call<List<Wallet>> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }
        });

        btnAddInWalletMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WalletMainActivity.this, InsertAndUpdateWallet.class);
                intent.putExtra("flag", "0");//danh dau flag la chuc nang them moi
                launcherforAdd.launch(intent);
            }
        });
        btnBackInWalletMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initResultLauncher() {
        try {
            launcherforAdd = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result != null && result.getResultCode() == RESULT_OK) {
                    //lay ve wallet tai day va cap nhat len giao dien
                    Wallet w = (Wallet) result.getData().getSerializableExtra("walletCreate");
                    //cap nhat tren giao dien
                    walletList.add(0, w);
                    walletAdapter.notifyItemInserted(0);
                    Alerter.create(WalletMainActivity.this)
                            .setTitle("Tạo ví thành công!")
                            .enableSwipeToDismiss()
                            .setIcon(R.drawable.ic_baseline_check_circle_24)
                            .setBackgroundColorRes(R.color.green)
                            .setIconColorFilter(0)
                            .setIconSize(R.dimen.icon_alert)
                            .show();
                }
            });
            launcherforEdit = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                //edit data
                if (result != null && result.getResultCode() == RESULT_OK) {
                    int flagUD = result.getData().getIntExtra("flagUD", 0);
                    if (flagUD == 2) {
                        //lay ve wallet tai day va cap nhat len giao dien
                        Wallet w = (Wallet) result.getData().getSerializableExtra("walletDelete");
                        //cap nhat tren giao dien
                        walletList.remove(iPosition);
                        walletAdapter.notifyItemRemoved(iPosition);
                        Alerter.create(WalletMainActivity.this)
                                .setTitle("Xóa ví thành công!")
                                .enableSwipeToDismiss()
                                .setIcon(R.drawable.ic_baseline_check_circle_24)
                                .setBackgroundColorRes(R.color.green)
                                .setIconColorFilter(0)
                                .setIconSize(R.dimen.icon_alert)
                                .show();
                    } else if (flagUD == 1) {
                        //lay ve wallet tai day va cap nhat len giao dien
                        Wallet w = (Wallet) result.getData().getSerializableExtra("walletUpdate");
                        //cap nhat tren giao dien
                        walletList.set(iPosition, w);
                        walletAdapter.notifyItemChanged(iPosition);
                        Alerter.create(WalletMainActivity.this)
                                .setTitle("Sửa ví thành công!")
                                .enableSwipeToDismiss()
                                .setIcon(R.drawable.ic_baseline_check_circle_24)
                                .setBackgroundColorRes(R.color.green)
                                .setIconColorFilter(0)
                                .setIconSize(R.dimen.icon_alert)
                                .show();
                    }
                }
            });
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}