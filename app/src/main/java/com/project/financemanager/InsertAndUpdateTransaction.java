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

import com.project.financemanager.common.NumberFormattingTextWatcher;
import com.project.financemanager.models.Transaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_and_update_transaction);
        final int green = ContextCompat.getColor(getApplicationContext(), R.color.green);
        btnBackInUpdate = findViewById(R.id.btnBackInUpdateTrans);
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
        int monthCurrent = calendar.get(Calendar.MONTH)+1;
        int dateCurrent = calendar.get(Calendar.DAY_OF_MONTH);
        int yearCurrent = calendar.get(Calendar.YEAR);
        int hourCurrent = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String strDate = "Hôm nay, " + dateCurrent + "/" + monthCurrent  + "/" + yearCurrent;
        String strTime = hourCurrent + ":" + minute;
        txtHourInUpdate.setText(strTime);
        txtDateInUpdate.setText(strDate);
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
                if(Integer.parseInt(typeCategory) != position){
                    txtCategoryIdInUpdate.setText("");
                    txtCategoryName.setText("Chọn danh mục");
                    txtCategoryName.setTextColor(Color.GRAY);
                    imgCategory.setImageResource(R.drawable.question_mark);
                }
                if (position == 1){
                    inputAmonut.setHintTextColor(green);
                    inputAmonut.setTextColor(green);
                }
                else {
                    inputAmonut.setTextColor(Color.RED);
                }
            }
        });

        btnBackInUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fillDataToTransaction(yearCurrent, monthCurrent, dateCurrent);

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
                        }
                        else {
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
    private void fillDataToTransaction(int yearCurrent, int monthCurrent, int dateCurrent){
        Intent intent = getIntent();
        Transaction myObject = (Transaction) intent.getSerializableExtra("transaction");
//        iconName = myObject.getImage();
        int green = ContextCompat.getColor(getApplicationContext(), R.color.green);
        if(myObject.getType().equals("Income")){
            inputAmonut.setTextColor(green);
            autoCompleteTextView.setText(items[1]);
            txtCategoryTypeInUpdate.setText("1");
        }
        else {
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

    private void onDialogDate(){
        Calendar calendar = Calendar.getInstance();
        int monthCurrent = calendar.get(Calendar.MONTH)+1;
        int dateCurrent = calendar.get(Calendar.DAY_OF_MONTH);
        int yearCurrent = calendar.get(Calendar.YEAR);
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String strDate;
                if(dateCurrent == dayOfMonth && monthCurrent == month+1 && yearCurrent == year){
                    strDate = "Hôm nay, " + dayOfMonth + "/" + (month+1) + "/" + year;
                }
                else if (dateCurrent - 1 == dayOfMonth && monthCurrent == month+1 &&yearCurrent == year){
                    strDate = "Hôm qua, " + dayOfMonth + "/" + (month+1) + "/" + year;
                }
                else if (dateCurrent + 1 == dayOfMonth && monthCurrent == month+1 &&yearCurrent == year){
                    strDate = "Ngày mai, " + dayOfMonth + "/" + (month+1) + "/" + year;
                }
                else {
                    strDate = dayOfMonth + "/" + (month+1) + "/" + year;
                }
                txtDateInUpdate.setText(strDate);
            }
        }, yearCurrent, monthCurrent-1, dateCurrent);
        dialog.show();
    }

    private void onDialogTime(){
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
                                              int dateCurrent){
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
            if(dateCurrent == day && monthCurrent == month+1 && yearCurrent == year){
                strDate = "Hôm nay, " + day + "/" + (month+1) + "/" + year;
            }
            else if (dateCurrent - 1 == day && monthCurrent == month+1 &&yearCurrent == year){
                strDate = "Hôm qua, " + day + "/" + (month+1) + "/" + year;
            }
            else if (dateCurrent + 1 == day && monthCurrent == month+1 &&yearCurrent == year){
                strDate = "Ngày mai, " + day + "/" + (month+1) + "/" + year;
            }
            else {
                strDate = day + "/" + (month+1) + "/" + year;
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
}