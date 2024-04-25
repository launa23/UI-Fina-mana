package com.project.financemanager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.project.financemanager.api.ApiService;
import com.project.financemanager.common.NumberFormattingTextWatcher;
import com.project.financemanager.dtos.Total;
import com.project.financemanager.models.Transaction;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertAndUpdateTransaction extends AppCompatActivity {
//    private String iconName;
    String[] items = {"Chi tiêu", "Thu nhập"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> arrayAdapter;
    private ImageView btnBackInUpdate;
    private TextView txtCategoryName;
    private TextView txtCategoryIdInUpdate;
    private TextView txtCategoryTypeInUpdate;
    private ImageView imgCategory;
    private EditText inputAmonut;
    private EditText inputDescription;
    private TextView txtNameWalletInUpdate;
    private TextView txtIdWalletInUpdate;
    private RelativeLayout relative5;
    private RelativeLayout relative2;
    private TextView txtDateInUpdate;
    private TextView txtHourInUpdate;

    //tus
    private RelativeLayout btnSave;
    private RelativeLayout btnRemove;

    private String flag;
    private Transaction transCreate;

    //sut
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_and_update_transaction);
        final int green = ContextCompat.getColor(getApplicationContext(), R.color.green);
        btnBackInUpdate = findViewById(R.id.btnBackInUpdateTrans);
        //tus
        btnSave = findViewById(R.id.buttonSave);
        btnRemove = findViewById(R.id.buttonRemove);
        //sut
        inputAmonut = findViewById(R.id.inputAmount);
        txtCategoryName = findViewById(R.id.txtCategoryNameInUpdate);
        imgCategory = findViewById(R.id.imgCategoryInUpdate);
        autoCompleteTextView = findViewById(R.id.autoCompleteTxt);
        inputDescription = findViewById(R.id.inputDescription);
        txtNameWalletInUpdate = findViewById(R.id.txtNameWalletInUpdate);
        relative5 = findViewById(R.id.relative5);
        txtIdWalletInUpdate = findViewById(R.id.txtIdWalletInUpdate);
        txtDateInUpdate = findViewById(R.id.txtDateInUpdate);
        txtHourInUpdate = findViewById(R.id.txtHourInUpdate);
        relative2 = findViewById(R.id.relative2);
        txtCategoryIdInUpdate = findViewById(R.id.txtCategoryIdInUpdate);
        txtCategoryTypeInUpdate = findViewById(R.id.txtCategoryTypeInUpdate);
        autoCompleteTextView.setText(items[0]);
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, items);
        autoCompleteTextView.setAdapter(arrayAdapter);
        inputAmonut.addTextChangedListener(new NumberFormattingTextWatcher(inputAmonut));
        Calendar calendar = Calendar.getInstance();
        int monthCurrent = calendar.get(Calendar.MONTH) + 1;
        int dateCurrent = calendar.get(Calendar.DAY_OF_MONTH);
        int yearCurrent = calendar.get(Calendar.YEAR);
        int hourCurrent = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String strDate = "Hôm nay, " + dateCurrent + "/" + monthCurrent + "/" + yearCurrent;
        String strTime = hourCurrent + ":" + minute;
        txtHourInUpdate.setText(strTime);
        txtDateInUpdate.setText(strDate);

        //tus
        //lay ve gia tri ben activity cha la HomeFragment
        Intent iHomeFrg = getIntent();
        flag = iHomeFrg.getStringExtra("flag");
        Toast.makeText(getApplicationContext(), flag, Toast.LENGTH_SHORT).show();
        //sut
        txtDateInUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDialogDate();
            }
        });

        txtHourInUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDialogTime();
            }
        });
        // Click chọn loại transaction trên toolbar
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                String typeCategory = txtCategoryTypeInUpdate.getText().toString();
                if (Integer.parseInt(typeCategory) != position) {
                    txtCategoryIdInUpdate.setText("");
                    txtCategoryName.setText("Chọn danh mục");
                    txtCategoryName.setTextColor(Color.GRAY);
                    imgCategory.setImageResource(R.drawable.question_mark);
                }
                if (position == 1) {
                    inputAmonut.setHintTextColor(green);
                    inputAmonut.setTextColor(green);
                } else {
                    //tus
                    inputAmonut.setHintTextColor(Color.RED);
                    //sut
                    inputAmonut.setTextColor(Color.RED);
                }
            }
        });

        btnBackInUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tus
                if (flag.equals("1")) {//sua
                    //Lưu vào sqlite
                    //Contact c = new Contact(idEdit, name, phone);
                    //db.updateContact(c);
                    //day tra ve mainactivity
                    Transaction k = new Transaction(8, "3", "3", "3", "3", "3", "3");

                    Intent t = new Intent();
                    t.putExtra("contact1", k);
                    Log.e("Editing", "success");
                    setResult(RESULT_OK, t);
                    finish();
                } else {//tao moi
                    Intent t = new Intent();
                    t.putExtra("transCreate", transCreate);
                    setResult(RESULT_OK, t);
                    finish();
                }
                //sut
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tus
                if (flag.equals("1")) {//sua
                    //Lưu vào sqlite
                    //Contact c = new Contact(idEdit, name, phone);
                    //db.updateContact(c);
                    //day tra ve mainactivity
//                    Transaction k = new Transaction(8,"3","3","3","3","3","3");
//
//                    Intent t = new Intent();
//                    t.putExtra("contact1", k);
//                    Log.e("Editing", "success");
//                    setResult(RESULT_OK, t);
//                    finish();
                } else {//tao moi
                    try {
                        // xử lí amount
                        String amount = (inputAmonut.getText().toString()).replaceAll("[,.]", "");
                        amount = (amount.equals("")) ? "0" : amount;
                        // xử lí descrip
                        String description = inputDescription.getText().toString();
                        // xử lí time transaction
                        String[] timeArrCreate = ((txtDateInUpdate.getText().toString()).replaceAll("\\b(?:Hôm\\s+nay,\\s*|Hôm\\s+qua,\\s*|Ngày\\s+mai,\\s*)\\b", "")).split("/");
                        String[] hourArrCreate = (txtHourInUpdate.getText().toString()).split(":");

                        String monthCreate = (Integer.parseInt(timeArrCreate[1]) >= 10) ? timeArrCreate[1] : "0" + timeArrCreate[1];
                        String dateCreate = (Integer.parseInt(timeArrCreate[0]) >= 10) ? timeArrCreate[0] : "0" + timeArrCreate[0];
                        String hourCreate = (Integer.parseInt(hourArrCreate[0]) >= 10) ? hourArrCreate[0] : "0" + hourArrCreate[0];
                        String minuteCreate = (Integer.parseInt(hourArrCreate[1]) >= 10) ? hourArrCreate[1] : "0" + hourArrCreate[1];

                        String time = timeArrCreate[2] + "-" + monthCreate + "-" + dateCreate + "T" + hourCreate + ":" + minuteCreate + ":00";
                        //xử lí danh mục và loại ví
                        String categoryID = (txtCategoryIdInUpdate.getText().toString());
                        String walletID = txtIdWalletInUpdate.getText().toString();

                        String typeTrans = autoCompleteTextView.getText().toString();
                        boolean validateCate = validateEmpty(categoryID, "Không được để trống danh mục!");
                        boolean validateWallet = validateEmpty(walletID, "Không được để trống ví!");

                        if (validateWallet && validateCate) {
                            Transaction dataTrans = new Transaction(amount, description, time, categoryID, walletID);
                            if (typeTrans.equals("Thu nhập")) {
                                ApiService.apiService.createIncomeTransaction(dataTrans).enqueue(new Callback<Transaction>() {
                                    @Override
                                    public void onResponse(Call<Transaction> call, Response<Transaction> response) {
                                        if (response.code() != 200) {
                                            Toast.makeText(getApplicationContext(), "Error: Thêm khoản thu không thành công!", Toast.LENGTH_LONG).show();
                                        } else {
                                            transCreate = response.body();
                                            Log.d("Response", "onResponse: " + transCreate);
                                            clearInputTrans();
                                            Toast.makeText(getApplicationContext(), "Thêm mới giao dịch thành công!!", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Transaction> call, Throwable t) {
                                        Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
                                        Log.e("error", t.getMessage());
                                    }
                                });

                            } else {
                                ApiService.apiService.createOutcomeTransaction(dataTrans).enqueue(new Callback<Transaction>() {
                                    @Override
                                    public void onResponse(Call<Transaction> call, Response<Transaction> response) {
                                        if (response.code() != 200) {
                                            Toast.makeText(getApplicationContext(), "Error: Thêm khoản chi không thành công!", Toast.LENGTH_LONG).show();
                                        } else {
                                            transCreate = response.body();
                                            Log.d("Response", "onResponse: " + transCreate);
                                            clearInputTrans();
                                            Toast.makeText(getApplicationContext(), "Thêm mới giao dịch thành công!!", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Transaction> call, Throwable t) {
                                        Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
                                        Log.e("error", t.getMessage());
                                    }
                                });
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                //sut
            }

        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tus
                if (flag.equals("1")) {//sua
                    //Lưu vào sqlite
                    //Contact c = new Contact(idEdit, name, phone);
                    //db.updateContact(c);
                    //day tra ve mainactivity
//                    Transaction k = new Transaction(8,"3","3","3","3","3","3");
//
//                    Intent t = new Intent();
//                    t.putExtra("contact1", k);
//                    Log.e("Editing", "success");
//                    setResult(RESULT_OK, t);
//                    finish();
                } else {//tao moi
                    try {
                        clearInputTrans();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                //sut
            }

        });


        //tus
        if (flag.equals("1")) {
            fillDataToTransaction(yearCurrent, monthCurrent, dateCurrent);
        } else {
            txtCategoryTypeInUpdate.setText("0");
            inputAmonut.setTextColor(Color.RED);
            inputAmonut.setHintTextColor(Color.RED);
        }
        //sut
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == this.RESULT_OK) {
                        Intent data = result.getData();
                        txtNameWalletInUpdate.setTextColor(Color.BLACK);
                        String nameWallet = data.getStringExtra("nameWallet");
                        int idWallet = data.getIntExtra("idWallet", 0);
                        txtNameWalletInUpdate.setText(nameWallet);
                        txtIdWalletInUpdate.setText(String.valueOf(idWallet));
                    }
                }
        );
        relative5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), WalletActivity.class);
                launcher.launch(intent);
            }
        });

        ActivityResultLauncher<Intent> launcherChooseCategory = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == this.RESULT_OK) {
                        Intent data = result.getData();
                        txtCategoryIdInUpdate.setText(String.valueOf(data.getIntExtra("idCategory", 0)));
                        txtCategoryName.setText(data.getStringExtra("nameCategory"));
                        String icon = data.getStringExtra("icon");
//                        iconName = icon;
                        int type = data.getIntExtra("type", 0);
                        txtCategoryTypeInUpdate.setText(String.valueOf(type));
                        if (type == 1) {
                            autoCompleteTextView.setText(items[1]);
                            arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, items);
                            autoCompleteTextView.setAdapter(arrayAdapter);
                            inputAmonut.setTextColor(green);
                        } else {
                            autoCompleteTextView.setText(items[0]);
                            arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, items);
                            autoCompleteTextView.setAdapter(arrayAdapter);
                            inputAmonut.setTextColor(Color.RED);
                        }
                        txtCategoryName.setTextColor(Color.BLACK);
                        int resourceId = this.getResources().getIdentifier(icon, "drawable", this.getPackageName());
                        imgCategory.setImageResource(resourceId);
                    }
                }
        );
        relative2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChooseCategoryActivity.class);
                launcherChooseCategory.launch(intent);
            }
        });
    }

    // Đổ dữ liệu của transaction vào các trường tương ứng
    private void fillDataToTransaction(int yearCurrent, int monthCurrent, int dateCurrent) {
        Intent intent = getIntent();
        Transaction myObject = (Transaction) intent.getSerializableExtra("transaction");
//        iconName = myObject.getImage();
        int green = ContextCompat.getColor(getApplicationContext(), R.color.green);
        if (myObject.getType().equals("Income")) {
            inputAmonut.setTextColor(green);
            autoCompleteTextView.setText(items[1]);
            txtCategoryTypeInUpdate.setText("1");
        } else {
            inputAmonut.setTextColor(Color.RED);
            autoCompleteTextView.setText(items[0]);
            txtCategoryTypeInUpdate.setText("0");

        }
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, items);
        autoCompleteTextView.setAdapter(arrayAdapter);
        inputAmonut.setText(myObject.getAmount());
        txtCategoryName.setText(myObject.getCategoryName());
        txtCategoryName.setTextColor(Color.BLACK);
        int resourceId = this.getResources().getIdentifier(myObject.getImage(), "drawable", this.getPackageName());
        imgCategory.setImageResource(resourceId);
        inputDescription.setTextColor(Color.BLACK);
        inputDescription.setText(myObject.getDescription());
        txtNameWalletInUpdate.setTextColor(Color.BLACK);
        txtNameWalletInUpdate.setText(myObject.getWalletName());
        Map<String, String> data = parseDateTime(myObject.getTime(), yearCurrent, monthCurrent, dateCurrent);
        txtDateInUpdate.setText(data.get("date"));
        txtHourInUpdate.setText(data.get("time"));
    }

    private void onDialogDate() {
        Calendar calendar = Calendar.getInstance();
        int monthCurrent = calendar.get(Calendar.MONTH) + 1;
        int dateCurrent = calendar.get(Calendar.DAY_OF_MONTH);
        int yearCurrent = calendar.get(Calendar.YEAR);
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String strDate;
                if (dateCurrent == dayOfMonth && monthCurrent == month + 1 && yearCurrent == year) {
                    strDate = "Hôm nay, " + dayOfMonth + "/" + (month + 1) + "/" + year;
                } else if (dateCurrent - 1 == dayOfMonth && monthCurrent == month + 1 && yearCurrent == year) {
                    strDate = "Hôm qua, " + dayOfMonth + "/" + (month + 1) + "/" + year;
                } else if (dateCurrent + 1 == dayOfMonth && monthCurrent == month + 1 && yearCurrent == year) {
                    strDate = "Ngày mai, " + dayOfMonth + "/" + (month + 1) + "/" + year;
                } else {
                    strDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                }
                txtDateInUpdate.setText(strDate);
            }
        }, yearCurrent, monthCurrent - 1, dateCurrent);
        dialog.show();
    }

    private void onDialogTime() {
        Calendar calendar = Calendar.getInstance();
        int hourCurrent = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog dialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        txtHourInUpdate.setText(hourOfDay + ":" + minute);
                    }
                }, hourCurrent, minute, true);
        dialog.show();
    }

    private Map<String, String> parseDateTime(String dateTime,
                                              int yearCurrent,
                                              int monthCurrent,
                                              int dateCurrent) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Map<String, String> map = new HashMap<>();
        try {
            Date date = inputFormat.parse(dateTime);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            String strDate;
            if (dateCurrent == day && monthCurrent == month + 1 && yearCurrent == year) {
                strDate = "Hôm nay, " + day + "/" + (month + 1) + "/" + year;
            } else if (dateCurrent - 1 == day && monthCurrent == month + 1 && yearCurrent == year) {
                strDate = "Hôm qua, " + day + "/" + (month + 1) + "/" + year;
            } else if (dateCurrent + 1 == day && monthCurrent == month + 1 && yearCurrent == year) {
                strDate = "Ngày mai, " + day + "/" + (month + 1) + "/" + year;
            } else {
                strDate = day + "/" + (month + 1) + "/" + year;
            }
            String strTime = hour + ":" + minute;
            map.put("date", strDate);
            map.put("time", strTime);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    private void clearInputTrans() {
        Calendar calendar = Calendar.getInstance();
        int monthCurrent = calendar.get(Calendar.MONTH) + 1;
        int dateCurrent = calendar.get(Calendar.DAY_OF_MONTH);
        int yearCurrent = calendar.get(Calendar.YEAR);
        int hourCurrent = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String strDate = "Hôm nay, " + dateCurrent + "/" + monthCurrent + "/" + yearCurrent;
        String strTime = hourCurrent + ":" + minute;
        txtHourInUpdate.setText(strTime);
        txtDateInUpdate.setText(strDate);

        inputAmonut.setText("0");
        txtCategoryName.setText("");
        txtCategoryName.setHint("Chọn danh mục");
        inputDescription.setText("");
        inputDescription.setHint("Ghi chú");
        txtNameWalletInUpdate.setText("");
        txtNameWalletInUpdate.setHint("Chọn ví");
        imgCategory.setImageResource(R.drawable.question_mark);
        txtCategoryIdInUpdate.setText("");
        txtIdWalletInUpdate.setText("");
    }

    private boolean validateEmpty(String input, String message) {
        boolean isEmptyError = true;
        if (input.trim().equals("")) {
            isEmptyError = false;
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
        return isEmptyError;
    }
}