package com.project.financemanager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.project.financemanager.api.ApiService;
import com.project.financemanager.common.NumberFormattingTextWatcher;
import com.project.financemanager.dtos.WalletDTO;
import com.project.financemanager.models.Transaction;
import com.project.financemanager.models.Wallet;
import com.tapadoo.alerter.Alerter;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InsertAndUpdateWallet extends AppCompatActivity {
    private RelativeLayout buttonSaveWallet, buttonRemoveWallet, relativeAmount;
    private ConstraintLayout layoutConfirmDeleteWalletDialog;
    private EditText inputAmountWallet, inputNameWallet;
    private TextView titleInUpdateWallet;
    private ImageView btnBackInUpdateWallet;
    private Wallet w, walletUpdate, walletCreate;
    private String flag, money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_and_update_wallet);
        buttonSaveWallet = findViewById(R.id.buttonSaveWallet);
        buttonRemoveWallet = findViewById(R.id.buttonRemoveWallet);
        btnBackInUpdateWallet = findViewById(R.id.btnBackInUpdateWallet);
        relativeAmount = findViewById(R.id.relativeAmount);
        titleInUpdateWallet = findViewById(R.id.titleInUpdateWallet);
        inputAmountWallet = findViewById(R.id.inputAmountWallet);
        inputAmountWallet.addTextChangedListener(new NumberFormattingTextWatcher(inputAmountWallet));
        inputNameWallet = findViewById(R.id.inputNameWallet);
        layoutConfirmDeleteWalletDialog = findViewById(R.id.layoutConfirmDeleteWalletDialog);
        Animation blinkAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink_animation);

        int green = ContextCompat.getColor(getApplicationContext(), R.color.green);
        inputAmountWallet.setTextColor(green);

        Intent i = getIntent();
        flag = i.getStringExtra("flag");

        if (flag.equals("1")) {
            //neu la nut sua thi day gia tri can sua len giao dien
            w = (Wallet) i.getSerializableExtra("wallet");
            inputAmountWallet.setText(w.getMoney());
            inputNameWallet.setText(w.getName());
            titleInUpdateWallet.setText(w.getName());
            relativeAmount.setVisibility(View.GONE);
            if (w.getBelongUser().equals("false")) {
                buttonRemoveWallet.setVisibility(View.GONE);
            }
        }

        buttonSaveWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSaveWallet.startAnimation(blinkAnimation);
                // xử lí amount
                money = (inputAmountWallet.getText().toString()).replaceAll("[,.]", "");
                money = (money.equals("")) ? "0" : money;
                // xử lí wallet name
                String nameWallet = inputNameWallet.getText().toString();
                //validate
                boolean validateNameWallet = validateEmpty(nameWallet, "Bạn quên đặt tên cho ví rồi!");

                if (validateNameWallet) {
                    WalletDTO dataWallet = new WalletDTO(nameWallet, "money", money);
                    if (flag.equals("1")) {
                        //Lưu vào sqlite
                        try {
                            int idWallet = w.getId();
                            Call<Wallet> call = ApiService.getInstance(getApplicationContext()).getiApiService().updateWallet(idWallet, dataWallet);
                            call.enqueue(new Callback<Wallet>() {
                                @Override
                                public void onResponse(Call<Wallet> call, Response<Wallet> response) {
                                    if (response.code() != 200) {
                                        if (response.code() == 400) {
                                            validateEmpty("", "Tên ví đã tồn tại!");
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Error: Sửa ví không thành công!", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        walletUpdate = response.body();
                                        Intent t = new Intent();
                                        t.putExtra("walletUpdate", walletUpdate);
                                        t.putExtra("flagUD", 1);
                                        setResult(RESULT_OK, t);
                                        finish();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Wallet> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (Exception ex) {
                            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        try {
                            Call<Wallet> call = ApiService.getInstance(getApplicationContext()).getiApiService().createWallet(dataWallet);
                            call.enqueue(new Callback<Wallet>() {
                                @Override
                                public void onResponse(Call<Wallet> call, Response<Wallet> response) {
                                    if (response.code() != 200) {
                                        if (response.code() == 400) {
                                            validateEmpty("", "Tên ví đã tồn tại!");
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Error: Sửa ví không thành công!", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        walletCreate = response.body();
                                        Intent t = new Intent();
                                        t.putExtra("walletCreate", walletCreate);
                                        setResult(RESULT_OK, t);
                                        finish();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Wallet> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (Exception ex) {
                            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

            }
        });

        buttonRemoveWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonRemoveWallet.startAnimation(blinkAnimation);
                if (flag.equals("1")) {
                    try {
                        showAlertConfirmDelDialog();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    try {
                        clearInputWallet();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

        });

        btnBackInUpdateWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean validateEmpty(String input, String message) {
        boolean isEmptyError = true;
        if (input.trim().equals("")) {
            isEmptyError = false;
            Alerter.create(InsertAndUpdateWallet.this)
                    .setTitle(message)
                    .enableSwipeToDismiss()
                    .setIcon(R.drawable.ic_baseline_info_24)
                    .setBackgroundColorRes(R.color.orange)
                    .setIconColorFilter(0)
                    .setIconSize(R.dimen.icon_alert)
                    .show();
        }
        return isEmptyError;
    }

    private void clearInputWallet() {
        inputAmountWallet.setText("0");
        inputNameWallet.setText("");
    }

    private void showAlertConfirmDelDialog() {
        View view = LayoutInflater.from(InsertAndUpdateWallet.this).inflate(R.layout.alert_confirm_delete_wallet_dialog, layoutConfirmDeleteWalletDialog);
        TextView btnOk = view.findViewById(R.id.alertOkDeleteWallet);
        TextView btnCancel = view.findViewById(R.id.alertCancelDeleteWallet);
        AlertDialog.Builder builder = new AlertDialog.Builder(InsertAndUpdateWallet.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                int idWallet = w.getId();
                Call<Void> call = ApiService.getInstance(getApplicationContext()).getiApiService().deleteWallet(idWallet);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() != 200) {
                            Toast.makeText(getApplicationContext(), "Error: Xóa ví không thành công!", Toast.LENGTH_LONG).show();
                        } else {
                            Intent t = new Intent();
                            t.putExtra("walletDelete", w);
                            t.putExtra("flagUD", 2);
                            setResult(RESULT_OK, t);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
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
