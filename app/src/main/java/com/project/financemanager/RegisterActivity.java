package com.project.financemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.financemanager.api.ApiService;
import com.project.financemanager.dtos.CategoryDTO;
import com.project.financemanager.dtos.UserDTO;
import com.tapadoo.alerter.Alerter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private TextView btnLoginNow;
    private TextView txtErrorFullName;
    private TextView txtErrorUsername;
    private TextView txtErrorEmail;
    private TextView txtErrorPassword;
    private TextView txtErrorRetypePassword;
    private ImageView imgHideOrView1;
    private ImageView imgHideOrView2;
    private Button btnRegister;
    private EditText inputFullName;
    private EditText inputUsername;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputRetypePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnLoginNow = findViewById(R.id.btnLoginNow);
        imgHideOrView1 = findViewById(R.id.imgHideOrView1);
        imgHideOrView2 = findViewById(R.id.imgHideOrView2);
        btnRegister = findViewById(R.id.btnRegister);
        inputFullName = findViewById(R.id.inputFullName);
        inputUsername = findViewById(R.id.inputUsername);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputRetypePassword = findViewById(R.id.inputRetypePassword);
        txtErrorFullName = findViewById(R.id.txtErrorFullName);
        txtErrorUsername = findViewById(R.id.txtErrorUsername);
        txtErrorEmail = findViewById(R.id.txtErrorEmail);
        txtErrorPassword = findViewById(R.id.txtErrorPassword);

        txtErrorRetypePassword = findViewById(R.id.txtErrorRetypePassword);

        btnLoginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgHideOrView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgHideOrView1.setImageResource(R.mipmap.hide);
                } else {
                    inputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgHideOrView1.setImageResource(R.mipmap.view);
                }
            }
        });
        imgHideOrView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputRetypePassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    inputRetypePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgHideOrView2.setImageResource(R.mipmap.hide);
                } else {
                    inputRetypePassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgHideOrView2.setImageResource(R.mipmap.view);
                }
            }
        });


        //register
        hideNotifyError(inputFullName, txtErrorFullName);
        hideNotifyError(inputUsername, txtErrorUsername);
        hideNotifyError(inputEmail, txtErrorEmail);
        hideNotifyError(inputPassword, txtErrorPassword);
        hideNotifyError(inputRetypePassword, txtErrorRetypePassword);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String fullName = inputFullName.getText().toString();
                    String username = inputUsername.getText().toString();
                    String email = inputEmail.getText().toString();
                    String password = inputPassword.getText().toString();
                    String retypePassword = inputRetypePassword.getText().toString();

                    boolean validateFullName = validateEmpty(inputFullName, "Không được để trống họ và tên!", txtErrorFullName);
                    boolean validateUsername = validateEmpty(inputUsername, "Không được để trống tên đăng nhập!", txtErrorUsername);
                    boolean validateEmail = validateEmpty(inputEmail, "Không được để trống email!", txtErrorEmail);
                    boolean validatePassword = validateEmpty(inputPassword, "Nhập mật khẩu!", txtErrorPassword);
                    boolean validateRetypePassword = validateEmpty(inputRetypePassword, "Nhập lại mật khẩu!", txtErrorRetypePassword);

                    if (validateEmail) {
                        validateEmail = validateFormatEmail(inputEmail, "Nhập đúng định dạng gmail (e.g: xxx@yyy.zzz.www)!", txtErrorEmail);
                    }

                    if (validateRetypePassword) {
                        validateRetypePassword = validatePasswordMatched(inputPassword, inputRetypePassword, "Mật khẩu không khớp!", txtErrorRetypePassword);
                    }

                    if (validateFullName && validateUsername && validateEmail && validatePassword && validateRetypePassword) {
                        UserDTO dataUser = new UserDTO(fullName,username,email,password,retypePassword,"2003-08-08T08:00:00");
                        Call<Void> call = ApiService.getInstance(getApplicationContext()).getiApiService().register(dataUser);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.code() != 200) {
                                    if (response.code() == 400) {
                                        validateEmpty(new EditText(getApplicationContext()), "Username đã tồn tại!",txtErrorUsername);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Error: Đăng ký không thành công!", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Intent intent = new Intent(RegisterActivity.this,PrepareActivity.class);
                                    intent.putExtra("username", username);
                                    intent.putExtra("password",password);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } catch (Exception ex) {
                    Toast.makeText(RegisterActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    private boolean validatePasswordMatched(EditText edtPassword, EditText edtRepPassword, String message, TextView txtError) {
        edtRepPassword.clearFocus();
        boolean isMatched;
        String input = edtPassword.getText().toString();
        String input2 = edtRepPassword.getText().toString();

        if (!input2.equals(input)) {
            isMatched = false;
            txtError.setText(message);
            txtError.setVisibility(View.VISIBLE);
            edtRepPassword.setBackgroundResource(R.drawable.xml_input_error);
        }else{
            isMatched = true;
            edtRepPassword.setBackgroundResource(R.drawable.xml_custom_input);
            txtError.setVisibility(View.GONE);
            txtError.setText("");
        }
        return isMatched;
    }

    private boolean validateFormatEmail(EditText edtInput, String message, TextView txtError) {
        edtInput.clearFocus();
        boolean isEmail;
        String input = edtInput.getText().toString();
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            isEmail = false;
            txtError.setText(message);
            txtError.setVisibility(View.VISIBLE);
            edtInput.setBackgroundResource(R.drawable.xml_input_error);
        }else{
            isEmail = true;
            txtError.setVisibility(View.GONE);
            txtError.setText("");
        }
        return isEmail;
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
}