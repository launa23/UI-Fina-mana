package com.project.financemanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.project.financemanager.adapters.CategoryNotChildAdapter;
import com.project.financemanager.api.ApiService;
import com.project.financemanager.api.IApiService;
import com.project.financemanager.models.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseParentCategory extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView txtNotChoose;
    private ProgressBar progressBar;
    private boolean isConnected;
    private ConstraintLayout layoutDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_parent_category);
        recyclerView = findViewById(R.id.rcvCategoryNotChild);
        txtNotChoose = findViewById(R.id.txtNotChoose);
        progressBar = findViewById(R.id.progressBarInChooseParent);
        layoutDialog = findViewById(R.id.layoutDialogInNotConnection);

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = networkInfo != null && networkInfo.isConnected();

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        if(isConnected){
            if(type.equals("1")){
                callApiGetData("income");
            }
            else{
                callApiGetData("outcome");
            }
        }
        else {
            showAlertNotConnection();
        }
        txtNotChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("noChoose", 0);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private void fillDataToList(List<Category> categories){
        CategoryNotChildAdapter categoryNotChildAdapter = new CategoryNotChildAdapter(categories, this, new CategoryNotChildAdapter.HandleClickNotParentCategory() {
            @Override
            public void onItemClick(int position) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("parentSelected", categories.get(position));
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(categoryNotChildAdapter);
        progressBar.setVisibility(View.GONE);
    }

    private void callApiGetData(String type){
        progressBar.setVisibility(View.VISIBLE);

        Call<List<Category>> call = ApiService.getInstance(getApplicationContext()).getiApiService().getCategoryParents(type);
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> categories = response.body();
                fillDataToList(categories);
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable throwable) {

            }
        });
    }
    private void showAlertNotConnection() {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.alert_no_connection, layoutDialog);
        Button btnOk = view.findViewById(R.id.alertBtnNotConnection);
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}