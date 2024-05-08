package com.project.financemanager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
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
    private EditText inputUserNameInLogin;
    private TextView btnRegisterInLogin;
    private TextView txtErrorUsernameInLogin;
    private TextView txtErrorPasswordInLogin;
    private ConstraintLayout layoutDialog;
    private boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        imgHideOrView = findViewById(R.id.imgHideOrView);
        inputPasswordInLogin = findViewById(R.id.inputPasswordInLogin);
        btnLogin = findViewById(R.id.btnLogin);
        inputUserNameInLogin = findViewById(R.id.inputUserNameInLogin);
        txtErrorUsernameInLogin = findViewById(R.id.txtErrorUsernameInLogin);
        txtErrorPasswordInLogin = findViewById(R.id.txtErrorPasswordInLogin);
        layoutDialogLoading = findViewById(R.id.layoutDialogLoading);
        btnRegisterInLogin = findViewById(R.id.btnRegisterInLogin);
        layoutDialog = findViewById(R.id.layoutDialogInNotConnection);
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = networkInfo != null && networkInfo.isConnected();
        imgHideOrView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputPasswordInLogin.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    inputPasswordInLogin.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgHideOrView.setImageResource(R.mipmap.hide);
                } else {
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
                if (isConnected) {
                    String username = inputUserNameInLogin.getText().toString();
                    String password = inputPasswordInLogin.getText().toString();
                    boolean validateUsername = validateEmpty(inputUserNameInLogin, "Không được để trống username!", txtErrorUsernameInLogin);
                    boolean validatePassword = validateEmpty(inputPasswordInLogin, "Không được để trống mật khẩu!", txtErrorPasswordInLogin);
                    if (validateUsername && validatePassword) {
                        alertDialog = showAlertDialog(alertDialog);
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

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    dismissAlertDialog(alertDialog);
                                    finish();
                                } else {
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
                                showAlertNotConnection();
                            }
                        });
                    }
                } else {
                    showAlertNotConnection();
                }
            }
        });

        // khi bat dau nhap vao o input username va password thi error text se an di
        hideNotifyError(inputUserNameInLogin, txtErrorUsernameInLogin);
        hideNotifyError(inputPasswordInLogin, txtErrorPasswordInLogin);
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

    private boolean validateEmpty(EditText edtInput, String message, TextView txtError) {
        String input = edtInput.getText().toString();
        edtInput.clearFocus();
        boolean isEmptyError = true;
        if (input.trim().equals("")) {
            isEmptyError = false;
            txtError.setText(message);
            txtError.setVisibility(View.VISIBLE);
            edtInput.setBackgroundResource(R.drawable.xml_input_error);
        }
        return isEmptyError;
    }

    private void hideNotifyError(EditText edtInput, TextView txtError) {
        edtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Thực thi trước khi text trong EditText thay đổi
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Thực thi khi text trong EditText thay đổi
                edtInput.setBackgroundResource(R.drawable.xml_custom_input);
                txtError.setVisibility(View.GONE);
                txtError.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Thực thi sau khi text trong EditText thay đổi
                //String newText = s.toString();
                // Xử lý newText ở đây
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