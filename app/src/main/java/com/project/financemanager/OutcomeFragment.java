package com.project.financemanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.financemanager.adapters.ParentOutcomeCategoryAdapter;
import com.project.financemanager.api.ApiService;
import com.project.financemanager.models.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutcomeFragment extends Fragment {
    private RecyclerView rcvParentList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_outcome, container, false);

        rcvParentList = rootView.findViewById(R.id.rcvParentList);
        ApiService.apiService.getAllOutcomeCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> categoryList = response.body();
                ParentOutcomeCategoryAdapter parentOutcomeCategoryAdapter = new ParentOutcomeCategoryAdapter(categoryList, getActivity());
                rcvParentList.setLayoutManager(new LinearLayoutManager(getActivity()));
                rcvParentList.setNestedScrollingEnabled(false);
                rcvParentList.setAdapter(parentOutcomeCategoryAdapter);
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable throwable) {

            }
        });

        return rootView;
    }
}