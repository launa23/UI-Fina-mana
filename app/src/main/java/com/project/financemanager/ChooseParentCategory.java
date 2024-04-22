package com.project.financemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.project.financemanager.adapters.CategoryNotChildAdapter;
import com.project.financemanager.api.ApiService;
import com.project.financemanager.models.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseParentCategory extends AppCompatActivity {
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_parent_category);
        recyclerView = findViewById(R.id.rcvCategoryNotChild);
        ApiService.apiService.getCategoryParents("outcome").enqueue(new Callback<List<Category>>() {
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

    private void fillDataToList(List<Category> categories){
        CategoryNotChildAdapter categoryNotChildAdapter = new CategoryNotChildAdapter(categories, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(categoryNotChildAdapter);
    }
}