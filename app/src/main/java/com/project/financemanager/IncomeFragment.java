package com.project.financemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.project.financemanager.adapters.ParentIncomeCategoryAdapter;
import com.project.financemanager.adapters.RvItemClickListener;
import com.project.financemanager.api.ApiService;
import com.project.financemanager.models.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IncomeFragment extends Fragment {
    private RecyclerView rcvParentList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_income, container, false);

        rcvParentList = rootView.findViewById(R.id.rcvIncomeParentCategory);
        ApiService.apiService.getAllIncomeCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> categoryList = response.body();
                ParentIncomeCategoryAdapter parentIncomeCategoryAdapter = new ParentIncomeCategoryAdapter(categoryList, getActivity(), new ParentIncomeCategoryAdapter.HandleClickParentCategory() {
                    @Override
                    public void onItemClick(int position) {
                        Toast.makeText(getActivity().getApplicationContext(), categoryList.get(position).getName(), Toast.LENGTH_SHORT).show();
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("idCategory", categoryList.get(position).getId());
                        resultIntent.putExtra("nameCategory", categoryList.get(position).getName());
                        resultIntent.putExtra("icon", categoryList.get(position).getIcon());
                        resultIntent.putExtra("type", categoryList.get(position).getType());
                        requireActivity().setResult(getActivity().RESULT_OK, resultIntent);
                        requireActivity().finish();
                    }
                });
                rcvParentList.setLayoutManager(new LinearLayoutManager(getActivity()));
                rcvParentList.setNestedScrollingEnabled(false);
                rcvParentList.setAdapter(parentIncomeCategoryAdapter);
                parentIncomeCategoryAdapter.setRvItemClickListener(new RvItemClickListener<Category>() {
                    @Override
                    public void onChildItemClick(int parentPosition, int childPosition, Category item) {
                        Toast.makeText(getActivity().getApplicationContext(), item.getName(), Toast.LENGTH_SHORT).show();
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("idCategory", item.getId());
                        resultIntent.putExtra("nameCategory", item.getName());
                        resultIntent.putExtra("icon", item.getIcon());
                        resultIntent.putExtra("type", item.getType());

                        requireActivity().setResult(getActivity().RESULT_OK, resultIntent);
                        requireActivity().finish();
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable throwable) {

            }
        });

        return rootView;
    }
}