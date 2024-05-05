package com.project.financemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_parent_category);
        recyclerView = findViewById(R.id.rcvCategoryNotChild);
        txtNotChoose = findViewById(R.id.txtNotChoose);
        progressBar = findViewById(R.id.progressBarInChooseParent);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        if(type.equals("1")){
            callApiGetData("income");
        }
        else{
            callApiGetData("outcome");
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
}