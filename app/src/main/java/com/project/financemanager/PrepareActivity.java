package com.project.financemanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.project.financemanager.api.ApiService;
import com.project.financemanager.common.NumberFormattingTextWatcher;
import com.project.financemanager.dtos.LoginResponse;
import com.project.financemanager.dtos.UserDTO;
import com.project.financemanager.dtos.UserLogin;
import com.project.financemanager.dtos.WalletDTO;
import com.tapadoo.alerter.Alerter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrepareActivity extends AppCompatActivity {
    AlertDialog alertDialog;
    private ConstraintLayout layoutDialogLoading;

    private EditText inputAmountStart;
    private Button btnStartUsing;
    private SharedPreferences sharedPreferences;

    private String username;
    private String password;
    private String money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare);
        layoutDialogLoading = findViewById(R.id.layoutDialogLoading);
        inputAmountStart = findViewById(R.id.inputAmountStart);
        btnStartUsing = findViewById(R.id.btnStartUsing);
        inputAmountStart.addTextChangedListener(new NumberFormattingTextWatcher(inputAmountStart));

        try {
            Intent intent = getIntent();
            username = intent.getStringExtra("username");
            password = intent.getStringExtra("password");
        } catch (Exception ex) {
            Toast.makeText(PrepareActivity.this, "Co loi xay ra, vui long thu lai sau...", Toast.LENGTH_LONG).show();
        }

        btnStartUsing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    alertDialog = showAlertDialog(alertDialog);
                    money = inputAmountStart.getText().toString().replaceAll("[,.]", "");
                    money = (money.equals("")) ? "0" : money;

                    UserLogin userLogin = new UserLogin(username, password);
                    Call<LoginResponse> call = ApiService.getInstance().getiApiService().login(userLogin);
                    call.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if (response.isSuccessful()) {
                                sharedPreferences = getApplicationContext().getSharedPreferences("CHECK_TOKEN", getApplicationContext().MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("token", "Bearer " + response.body().getToken());
                                editor.apply();

                                //create first wallet
                                WalletDTO dataWallet = new WalletDTO("Ví tiền mặt", "money", money);
                                Call<WalletDTO> callWallet = ApiService.getInstance(getApplicationContext()).getiApiService().createFirstWallet(dataWallet);
                                callWallet.enqueue(new Callback<WalletDTO>() {
                                    @Override
                                    public void onResponse(Call<WalletDTO> callWallet, Response<WalletDTO> response) {
                                        if (response.code() != 200) {
                                            Toast.makeText(getApplicationContext(), "Error: Tạo ví không thành công!", Toast.LENGTH_LONG).show();
                                        } else {
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            dismissAlertDialog(alertDialog);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<WalletDTO> callWallet, Throwable t) {
                                        Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
                                    }
                                });


                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                            Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception ex) {
                    Toast.makeText(PrepareActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private AlertDialog showAlertDialog(AlertDialog alertDialog) {
        View view = LayoutInflater.from(this).inflate(R.layout.loading_progress_bar, layoutDialogLoading);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setCancelable(false);
        alertDialog = builder.create();

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
        return alertDialog;
    }

    private void dismissAlertDialog(AlertDialog alertDialog) {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        }, 100);
    }
}