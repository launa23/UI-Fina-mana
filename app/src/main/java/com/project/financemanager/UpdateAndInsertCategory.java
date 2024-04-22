package com.project.financemanager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class UpdateAndInsertCategory extends AppCompatActivity {
    private ImageView imgIconDefault;
    private TextView txtChooseIcon;
    private ImageView btnCloseInUpdateCate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_and_insert_category);
        imgIconDefault = findViewById(R.id.imgIconDefault);
        txtChooseIcon = findViewById(R.id.txtChooseIcon);
        btnCloseInUpdateCate = findViewById(R.id.btnCloseInUpdateCate);
        ActivityResultLauncher<Intent> launcherIcon = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == this.RESULT_OK) {
                        Intent data = result.getData();
                        int resourceId = data.getIntExtra("resourceId", 0);
                        imgIconDefault.setImageResource(resourceId);
                    }
                }
        );
        imgIconDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickChooseIcon(launcherIcon);
            }
        });
        txtChooseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickChooseIcon(launcherIcon);
            }
        });
        btnCloseInUpdateCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void onClickChooseIcon(ActivityResultLauncher<Intent> launcher){
        Intent intent = new Intent(this, IconActivity.class);
        launcher.launch(intent);
    }
}