package com.project.financemanager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.project.financemanager.models.TitleTime;
import com.project.financemanager.models.Transaction;

import java.util.Calendar;
import java.util.List;

public class InsertAndUpdateTransaction extends AppCompatActivity {
    String[] items = {"Chi tiêu", "Thu nhập"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> arrayAdapter;
    private ImageView btnBackInUpdate;
    private TextView txtCategoryName;
    private ImageView imgCategory;
    private EditText inputAmonut;
    private EditText inputDescription;
    private TextView txtNameWalletInUpdate;
    private TextView txtIdWalletInUpdate;
    private RelativeLayout relative5;
    private TextView txtDateInUpdate;
    private TextView txtHourInUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_and_update_transaction);
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

        autoCompleteTextView.setText(items[0]);
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, items);
        autoCompleteTextView.setAdapter(arrayAdapter);

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
                Toast.makeText(getApplicationContext(), "Item: " + item, Toast.LENGTH_SHORT).show();
            }
        });

        btnBackInUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fillDataToTransaction();

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
    }

    // Đổ dữ liệu của transaction vào các trường tương ứng
    private void fillDataToTransaction(){
        Intent intent = getIntent();
        Transaction myObject = (Transaction) intent.getSerializableExtra("transaction");
        if(myObject.getType().equals("Income")){
            inputAmonut.setTextColor(Color.GREEN);
            autoCompleteTextView.setText(items[1]);
        }
        else {
            inputAmonut.setTextColor(Color.RED);
            autoCompleteTextView.setText(items[0]);
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
        // Còn nữa, làm sau......
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
}