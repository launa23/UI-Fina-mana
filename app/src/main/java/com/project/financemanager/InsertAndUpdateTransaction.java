package com.project.financemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.financemanager.models.Transaction;

public class InsertAndUpdateTransaction extends AppCompatActivity {
    String[] items = {"Chi tiêu", "Thu nhập"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> arrayAdapter;
    private ImageView btnBackInUpdate;
    private TextView txtCategoryName;
    private ImageView imgCategory;
    private EditText inputAmonut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_and_update_transaction);
        btnBackInUpdate = findViewById(R.id.btnBackInUpdateTrans);
        inputAmonut = findViewById(R.id.inputAmount);
        txtCategoryName = findViewById(R.id.txtCategoryNameInUpdate);
        imgCategory = findViewById(R.id.imgCategoryInUpdate);


        autoCompleteTextView = findViewById(R.id.autoCompleteTxt);
        autoCompleteTextView.setText(items[0]);
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, items);

        autoCompleteTextView.setAdapter(arrayAdapter);

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
    }

    private void fillDataToTransaction(){
        Intent intent = getIntent();
        Transaction myObject = (Transaction) intent.getSerializableExtra("transaction");
        if(myObject.getType().equals("Income")){
            inputAmonut.setTextColor(Color.GREEN);
        }
        else {
            inputAmonut.setTextColor(Color.RED);
        }
        inputAmonut.setText(myObject.getAmount());
        txtCategoryName.setText(myObject.getCategoryName());
        txtCategoryName.setTextColor(Color.BLACK);
        int resourceId = this.getResources().getIdentifier(myObject.getImage(), "drawable", this.getPackageName());
        imgCategory.setImageResource(resourceId);
        // Còn nữa, làm sau......

    }
}