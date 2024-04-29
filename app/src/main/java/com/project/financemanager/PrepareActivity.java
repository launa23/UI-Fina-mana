package com.project.financemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.project.financemanager.common.NumberFormattingTextWatcher;

public class PrepareActivity extends AppCompatActivity {
    private EditText inputAmountStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare);
        inputAmountStart = findViewById(R.id.inputAmountStart);
        inputAmountStart.addTextChangedListener(new NumberFormattingTextWatcher(inputAmountStart));

    }
}