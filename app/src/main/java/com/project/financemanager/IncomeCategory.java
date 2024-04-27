package com.project.financemanager;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.financemanager.adapters.ParentOutcomeCategoryAdapter;
import com.project.financemanager.api.ApiService;
import com.project.financemanager.api.IApiService;
import com.project.financemanager.common.RvItemClickListener;
import com.project.financemanager.models.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IncomeCategory extends Fragment {
    private final String FLAG = "1";
    private RecyclerView rcvParentList;
    private ConstraintLayout layoutDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_outcome, container, false);
        layoutDialog = rootView.findViewById(R.id.layoutDialog);
        rcvParentList = rootView.findViewById(R.id.rcvParentList);
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK) {
                        Intent data = result.getData();

                    }
                }
        );
        fillDataToCategoryList(rootView, launcher);
        return rootView;
    }
    private void fillDataToCategoryList(View rootView, ActivityResultLauncher<Intent> launcher){
        Call<List<Category>> call = ApiService.getInstance(getContext()).getiApiService().getAllIncomeCategories();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> categoryList = response.body();
                ParentOutcomeCategoryAdapter parentOutcomeCategoryAdapter = new ParentOutcomeCategoryAdapter(categoryList, getActivity(), new ParentOutcomeCategoryAdapter.HandleClickParentCategory() {
                    @Override
                    public void onItemClick(int position) {
                        if (categoryList.get(position).getCategoryOf().equals("User")){
                            Intent intent = new Intent(getContext(), UpdateAndInsertCategory.class);
                            intent.putExtra("categoryItem", categoryList.get(position));
                            if(categoryList.get(position).getCategoryChilds().isEmpty()){
                                intent.putExtra("isParent", 0);
                            }
                            else {
                                intent.putExtra("isParent", 1);
                            }
                            intent.putExtra("fl", FLAG);
                            launcher.launch(intent);
                        }
                        else {
                            showAlertDialog();
                        }
                    }
                });
                rcvParentList.setLayoutManager(new LinearLayoutManager(getActivity()));
                rcvParentList.setNestedScrollingEnabled(false);
//                rcvParentList.setHasFixedSize(true);
                rcvParentList.setAdapter(parentOutcomeCategoryAdapter);
                parentOutcomeCategoryAdapter.setRvItemClickListener(new RvItemClickListener<Category>() {
                    @Override
                    public void onChildItemClick(int parentPosition, int childPosition, Category item) {
                        if(item.getCategoryOf().equals("User")){
                            Intent intent = new Intent(getContext(), UpdateAndInsertCategory.class);
                            intent.putExtra("categoryItem", item);
                            intent.putExtra("isParent", 0);
                            intent.putExtra("fl", FLAG);
                            intent.putExtra("categoryParent", categoryList.get(parentPosition));
                            launcher.launch(intent);
                        }
                        else {
                            showAlertDialog();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable throwable) {

            }
        });
    }
    private void showAlertDialog(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.alert_dont_update_dialog, layoutDialog);
        Button btnOk = view.findViewById(R.id.alertBtn);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
//        builder.setCancelable(false);
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
