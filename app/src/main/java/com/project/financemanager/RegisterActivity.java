package com.project.financemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {
    private TextView btnLoginNow;
    private ImageView imgHideOrView1;
    private ImageView imgHideOrView2;
    private EditText editText4;
    private EditText editText5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnLoginNow = findViewById(R.id.btnLoginNow);
        imgHideOrView1 = findViewById(R.id.imgHideOrView1);
        imgHideOrView2 = findViewById(R.id.imgHideOrView2);
        editText4 = findViewById(R.id.editText4);
        editText5 = findViewById(R.id.editText5);

        btnLoginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgHideOrView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText4.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    editText4.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgHideOrView1.setImageResource(R.mipmap.hide);
                }
                else{
                    editText4.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgHideOrView1.setImageResource(R.mipmap.view);
                }
            }
        });
        imgHideOrView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText5.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    editText5.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgHideOrView2.setImageResource(R.mipmap.hide);
                }
                else{
                    editText5.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgHideOrView2.setImageResource(R.mipmap.view);
                }
            }
        });
    }
}