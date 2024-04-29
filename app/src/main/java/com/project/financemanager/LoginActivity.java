package com.project.financemanager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.financemanager.api.ApiService;
import com.project.financemanager.api.IApiService;
import com.project.financemanager.dtos.LoginResponse;
import com.project.financemanager.dtos.UserLogin;
import com.tapadoo.alerter.Alerter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    AlertDialog alertDialog;
    private ConstraintLayout layoutDialogLoading;
    private SharedPreferences sharedPreferences;
    private ImageView imgHideOrView;
    private EditText inputPasswordInLogin;
    private Button btnLogin;
    private TextView inputUserNameInLogin;
    private TextView btnRegisterInLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        imgHideOrView = findViewById(R.id.imgHideOrView);
        inputPasswordInLogin = findViewById(R.id.inputPasswordInLogin);
        btnLogin = findViewById(R.id.btnLogin);
        inputUserNameInLogin = findViewById(R.id.inputUserNameInLogin);
        layoutDialogLoading = findViewById(R.id.layoutDialogLoading);
        btnRegisterInLogin = findViewById(R.id.btnRegisterInLogin);
        imgHideOrView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputPasswordInLogin.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    inputPasswordInLogin.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgHideOrView.setImageResource(R.mipmap.hide);
                }
                else{
                    inputPasswordInLogin.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgHideOrView.setImageResource(R.mipmap.view);
                }
            }
        });
        ActivityResultLauncher<Intent> launcherRegister = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == LoginActivity.RESULT_OK) {
                        Intent data = result.getData();

                    }
                }
        );
        btnRegisterInLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                launcherRegister.launch(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = showAlertDialog(alertDialog);
                String username = inputUserNameInLogin.getText().toString();
                String password = inputPasswordInLogin.getText().toString();
                UserLogin userLogin = new UserLogin(username, password);
                Call<LoginResponse> call = ApiService.getInstance().getiApiService().login(userLogin);
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()){
                            sharedPreferences = getApplicationContext().getSharedPreferences("CHECK_TOKEN", getApplicationContext().MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("token", "Bearer " + response.body().getToken());
                            editor.apply();

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                            dismissAlertDialog(alertDialog);

                        }
                        else{
                            Alerter.create(LoginActivity.this)
                                    .setTitle("Tài khoản mật khẩu không phù hợp!")
                                    .enableSwipeToDismiss()
                                    .setIcon(R.drawable.ic_baseline_info_24)
                                    .setBackgroundColorRes(R.color.red)
                                    .setIconColorFilter(0)
                                    .setIconSize(R.dimen.icon_alert)
                                    .show();
                            dismissAlertDialog(alertDialog);
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable throwable) {

                    }
                });
            }
        });
    }
    private AlertDialog showAlertDialog(AlertDialog alertDialog){
        View view = LayoutInflater.from(this).inflate(R.layout.loading_progress_bar, layoutDialogLoading);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setCancelable(false);
        alertDialog = builder.create();

        if(alertDialog.getWindow() != null){
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
        }, 1000);
    }
}