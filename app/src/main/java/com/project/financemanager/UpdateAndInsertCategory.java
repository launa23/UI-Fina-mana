package com.project.financemanager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.financemanager.models.Category;
import com.project.financemanager.models.Transaction;

import java.util.Objects;

public class UpdateAndInsertCategory extends AppCompatActivity {
    private String iconName;
    private ImageView imgIconDefault;
    private TextView txtChooseIcon;
    private EditText inputNameCategory;
    private ImageView btnCloseInUpdateCate;
    private RelativeLayout relative11;
    private ImageView imgParentIconDefault;
    private TextView txtNameParentCategory;
    private TextView txtIdParentCategory;
    private TextView typeCategory;
    private String flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_and_insert_category);
        imgIconDefault = findViewById(R.id.imgIconDefault);
        txtChooseIcon = findViewById(R.id.txtChooseIcon);
        btnCloseInUpdateCate = findViewById(R.id.btnCloseInUpdateCate);
        relative11 = findViewById(R.id.relative11);
        inputNameCategory = findViewById(R.id.inputNameCategory);
        imgParentIconDefault = findViewById(R.id.imgParentIconDefault);
        txtNameParentCategory = findViewById(R.id.txtNameParentCategory);
        txtIdParentCategory = findViewById(R.id.txtIdParentCategory);
        typeCategory = findViewById(R.id.typeInUpdateCate);

        Intent iCategoryFrg = getIntent();
        flag = iCategoryFrg.getStringExtra("fl");

        if(Objects.equals(flag, "1")){
            fillDataToIntent();
        }
        else{
            Intent intent = getIntent();
            typeCategory.setText(intent.getStringExtra("typeCategory"));
        }
        ActivityResultLauncher<Intent> launcherIcon = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == this.RESULT_OK) {
                        Intent data = result.getData();
                        int resourceId = data.getIntExtra("resourceId", 0);
                        iconName = getResources().getResourceEntryName(resourceId);
                        imgIconDefault.setImageResource(resourceId);
                    }
                }
        );

        ActivityResultLauncher<Intent> launcherParent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == this.RESULT_OK) {
                        Intent data = result.getData();
                        int noChoose = data.getIntExtra("noChoose", 1);
                        if(noChoose == 0){
                            imgParentIconDefault.setImageResource(R.mipmap.empty);
                            txtNameParentCategory.setText("Không");
                            txtIdParentCategory.setText("0");
                        }
                        else {
                            Category parentSelected = (Category) data.getSerializableExtra("parentSelected");
                            int resourceIdParent = this.getResources().getIdentifier(parentSelected.getIcon(), "drawable", this.getPackageName());
//                            String name = getResources().getResourceEntryName(resourceIdParent);
                            imgParentIconDefault.setImageResource(resourceIdParent);
                            txtNameParentCategory.setText(parentSelected.getName());
                            txtIdParentCategory.setText(String.valueOf(parentSelected.getId()));
                        }

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
                Intent intent = new Intent();
                setResult(MainActivity.RESULT_OK, intent);

                finish();
            }
        });
        relative11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickChooseParent(launcherParent);
            }
        });
    }

    private void onClickChooseIcon(ActivityResultLauncher<Intent> launcher){
        Intent intent = new Intent(this, IconActivity.class);
        launcher.launch(intent);
    }

    private void onClickChooseParent(ActivityResultLauncher<Intent> launcher){
        Intent intent = new Intent(this, ChooseParentCategory.class);
        String type = String.valueOf(typeCategory.getText());
        intent.putExtra("type", type);
        launcher.launch(intent);
    }

    private void fillDataToIntent(){
        Intent intent = getIntent();
        Category category = (Category) intent.getSerializableExtra("categoryItem");
        int isParent = intent.getIntExtra("isParent", 1);
        inputNameCategory.setText(category.getName());
        iconName = category.getIcon();
        int resourceId = this.getResources().getIdentifier(category.getIcon(), "drawable", this.getPackageName());
        imgIconDefault.setImageResource(resourceId);
        typeCategory.setText(String.valueOf(category.getType()));
        if (isParent == 1){
            relative11.setVisibility(View.GONE);
        }
        else{
            Category categoryParent = (Category) intent.getSerializableExtra("categoryParent");
            if(categoryParent != null){
                int resourceIdParent = this.getResources().getIdentifier(categoryParent.getIcon(), "drawable", this.getPackageName());
                imgParentIconDefault.setImageResource(resourceIdParent);
                txtNameParentCategory.setText(categoryParent.getName());
            }

        }
    }
}