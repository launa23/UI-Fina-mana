package com.project.financemanager;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleObserver;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.financemanager.api.ApiService;
import com.project.financemanager.dtos.CategoryDTO;
import com.project.financemanager.models.Category;
import com.project.financemanager.models.Transaction;
import com.tapadoo.alerter.Alerter;

import java.util.Calendar;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateAndInsertCategory extends AppCompatActivity {
    private String iconName;
    private ImageView imgIconDefault;
    private TextView txtChooseIcon;
    private EditText inputNameCategory;
    private ImageView btnCloseInUpdateCate;
    private RelativeLayout btnSaveCate;
    private RelativeLayout btnRemoveCate;
    private RelativeLayout relative11;
    private ConstraintLayout layoutConfirmDeleteCateDialog;

    private ImageView imgParentIconDefault;
    private TextView txtNameParentCategory;
    private TextView txtIdParentCategory;
    private TextView txtNameIconCategory;
    private TextView titleInUpdateCate;
    private TextView typeCategory;
    private OnBackPressedCallback backEvent;
    private String flag;
    private Category category, categoryParent;
    private CategoryDTO cateCreate, cateUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_and_insert_category);
        imgIconDefault = findViewById(R.id.imgIconDefault);
        txtChooseIcon = findViewById(R.id.txtChooseIcon);
        btnCloseInUpdateCate = findViewById(R.id.btnCloseInUpdateCate);
        btnSaveCate = findViewById(R.id.buttonSaveCate);
        btnRemoveCate = findViewById(R.id.buttonRemoveCate);
        layoutConfirmDeleteCateDialog = findViewById(R.id.layoutConfirmDeleteCateDialog);
        relative11 = findViewById(R.id.relative11);
        inputNameCategory = findViewById(R.id.inputNameCategory);
        imgParentIconDefault = findViewById(R.id.imgParentIconDefault);
        txtNameParentCategory = findViewById(R.id.txtNameParentCategory);
        txtIdParentCategory = findViewById(R.id.txtIdParentCategory);
        txtNameIconCategory = findViewById(R.id.txtNameIconCategory);
        titleInUpdateCate = findViewById(R.id.titleInUpdateCate);
        typeCategory = findViewById(R.id.typeInUpdateCate);

        Intent iCategoryFrg = getIntent();
        flag = iCategoryFrg.getStringExtra("fl");

        if (Objects.equals(flag, "1")) {
            fillDataToIntent();
        } else {
            Intent intent = getIntent();
            typeCategory.setText(intent.getStringExtra("typeCategory"));
            titleInUpdateCate.setText("Thêm danh mục");
        }
        ActivityResultLauncher<Intent> launcherIcon = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == this.RESULT_OK) {
                        Intent data = result.getData();
                        int resourceId = data.getIntExtra("resourceId", 0);
                        iconName = getResources().getResourceEntryName(resourceId);
                        imgIconDefault.setImageResource(resourceId);
                        String name = getResources().getResourceEntryName(resourceId);
                        txtNameIconCategory.setText(name);
                    }
                }
        );

        ActivityResultLauncher<Intent> launcherParent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == this.RESULT_OK) {
                        Intent data = result.getData();
                        int noChoose = data.getIntExtra("noChoose", 1);
                        if (noChoose == 0) {
                            imgParentIconDefault.setImageResource(R.mipmap.empty);
                            txtNameParentCategory.setText("Không");
                            txtIdParentCategory.setText("0");
                        } else {
                            Category parentSelected = (Category) data.getSerializableExtra("parentSelected");
                            int resourceIdParent = this.getResources().getIdentifier(parentSelected.getIcon(), "drawable", this.getPackageName());
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
                backEventCustom();
            }
        });

        btnSaveCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //xu li ten icon
                    String iconNameCate = txtNameIconCategory.getText().toString();
                    //xu li ten danh muc
                    String nameCate = inputNameCategory.getText().toString();
                    // danh muc cha
                    String parentCateId = txtIdParentCategory.getText().toString();
                    parentCateId = parentCateId.equals("0") ? "" : parentCateId;
                    // loai danh muc
                    String typeCate = typeCategory.getText().toString();
                    typeCate = typeCate.equals("1") ? "income" : "outcome";
                    boolean validateNameCate = validateEmpty(nameCate, "Bạn chưa đặt tên cho danh mục!");
                    boolean validateIconNameCate = validateEmpty(iconNameCate, "Hãy chọn icon cho danh mục nhé!");
                    if (validateNameCate && validateIconNameCate) {
                        CategoryDTO dataCate = new CategoryDTO(parentCateId, nameCate, iconNameCate);
                        if (flag.equals("0")) {
                            Call<CategoryDTO> call = ApiService.getInstance(getApplicationContext()).getiApiService().createCategory(typeCate, dataCate);
                            call.enqueue(new Callback<CategoryDTO>() {
                                @Override
                                public void onResponse(Call<CategoryDTO> call, Response<CategoryDTO> response) {
                                    if (response.code() != 200) {
                                        if (response.code() == 400) {
                                            validateEmpty("", "Tên danh mục đã tồn tại!");
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Error: Thêm danh mục không thành công!", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Alerter.create(UpdateAndInsertCategory.this)
                                                .setTitle("Thêm danh mục mới thành công!")
                                                .enableSwipeToDismiss()
                                                .setIcon(R.drawable.ic_baseline_check_circle_24)
                                                .setBackgroundColorRes(R.color.green)
                                                .setIconColorFilter(0)
                                                .setIconSize(R.dimen.icon_alert)
                                                .show();
                                        cateCreate = response.body();
                                        clearInputCate();
                                    }
                                }

                                @Override
                                public void onFailure(Call<CategoryDTO> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
                                    Log.e("error", t.getMessage());
                                }
                            });
                        } else {
                            int idCate = category.getId();
                            if (parentCateId.equals(String.valueOf(idCate))) {
                                validateEmpty("", "Bạn không thể chọn danh mục cha là chính nó!");
                            } else {
                                Call<CategoryDTO> call = ApiService.getInstance(getApplicationContext()).getiApiService().updateCategory(typeCate, idCate, dataCate);
                                call.enqueue(new Callback<CategoryDTO>() {
                                    @Override
                                    public void onResponse(Call<CategoryDTO> call, Response<CategoryDTO> response) {
                                        if (response.code() != 200) {
                                            if (response.code() == 400) {
                                                validateEmpty("", "Tên danh mục đã tồn tại!");
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Error: Sửa danh mục không thành công!", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            cateUpdate = response.body();
                                            Intent t = new Intent();
                                            t.putExtra("cateUpdate", cateUpdate);
                                            t.putExtra("flagUD", 1);
                                            setResult(RESULT_OK, t);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<CategoryDTO> call, Throwable t) {
                                        Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
                                        Log.e("error", t.getMessage());
                                    }
                                });
                            }
                        }

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnRemoveCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag.equals("1")) {
                    try {
                        showAlertConfirmDelDialog();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    try {
                        clearInputCate();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        relative11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickChooseParent(launcherParent);
            }
        });

        // xử lí sự kiện back mặc định của hệ thống
        backEvent = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                backEventCustom();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, backEvent);
    }

    private void onClickChooseIcon(ActivityResultLauncher<Intent> launcher) {
        Intent intent = new Intent(this, IconActivity.class);
        launcher.launch(intent);
    }

    private void onClickChooseParent(ActivityResultLauncher<Intent> launcher) {
        Intent intent = new Intent(this, ChooseParentCategory.class);
        String type = String.valueOf(typeCategory.getText());
        intent.putExtra("type", type);
        launcher.launch(intent);
    }

    private void fillDataToIntent() {
        Intent intent = getIntent();
        category = (Category) intent.getSerializableExtra("categoryItem");
        int isParent = intent.getIntExtra("isParent", 1);
        inputNameCategory.setText(category.getName());
        iconName = category.getIcon();
        int resourceId = this.getResources().getIdentifier(category.getIcon(), "drawable", this.getPackageName());
        imgIconDefault.setImageResource(resourceId);
        typeCategory.setText(String.valueOf(category.getType()));
        txtNameIconCategory.setText(category.getIcon());
        if (isParent == 1) {
            relative11.setVisibility(View.GONE);
        } else {
            categoryParent = (Category) intent.getSerializableExtra("categoryParent");
            if (categoryParent != null) {
                int resourceIdParent = this.getResources().getIdentifier(categoryParent.getIcon(), "drawable", this.getPackageName());
                imgParentIconDefault.setImageResource(resourceIdParent);
                txtNameParentCategory.setText(categoryParent.getName());
                txtIdParentCategory.setText(String.valueOf(categoryParent.getId()));
            }

        }
    }

    private void clearInputCate() {
        inputNameCategory.setText("");
        txtNameIconCategory.setText("");
        txtIdParentCategory.setText("");
        imgIconDefault.setImageResource(R.mipmap.empty);
        imgParentIconDefault.setImageResource(R.mipmap.empty);
        txtNameParentCategory.setText("Danh mục cha");
    }

    private boolean validateEmpty(String input, String message) {
        boolean isEmptyError = true;
        if (input.trim().equals("")) {
            isEmptyError = false;
            Alerter.create(UpdateAndInsertCategory.this)
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

    private void showAlertConfirmDelDialog() {
        View view = LayoutInflater.from(UpdateAndInsertCategory.this).inflate(R.layout.alert_confirm_delete_cate_dialog, layoutConfirmDeleteCateDialog);
        TextView btnOk = view.findViewById(R.id.alertOkDeleteCate);
        TextView btnCancel = view.findViewById(R.id.alertCancelDeleteCate);
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateAndInsertCategory.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                String typeCate = typeCategory.getText().toString().toLowerCase();
                int idCate = category.getId();
                Call<Void> call = ApiService.getInstance(getApplicationContext()).getiApiService().deleteCategory(typeCate, idCate);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() != 200) {
                            Toast.makeText(getApplicationContext(), "Error: Xóa khoản giao dịch không thành công!", Toast.LENGTH_LONG).show();
                        } else {
                            Intent t = new Intent();
                            t.putExtra("cateDelete", category);
                            t.putExtra("flagUD", 2);
                            setResult(RESULT_OK, t);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
                        Log.e("error", t.getMessage());
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

    private void backEventCustom() {
        if (flag.equals("1")) {
            finish();
        } else {
            Intent intent = new Intent();
            intent.putExtra("cateCreate", cateCreate);
            setResult(MainActivity.RESULT_OK, intent);
            finish();
        }
    }

}