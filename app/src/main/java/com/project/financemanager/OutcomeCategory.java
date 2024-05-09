package com.project.financemanager;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.financemanager.adapters.ParentOutcomeCategoryAdapter;
import com.project.financemanager.api.ApiService;
import com.project.financemanager.api.IApiService;
import com.project.financemanager.common.RvItemClickListener;
import com.project.financemanager.models.Category;
import com.tapadoo.alerter.Alerter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutcomeCategory extends Fragment {
    AlertDialog alertDialog;
    private RecyclerView rcvParentList;
    private ConstraintLayout layoutDialogInNotConnection;
    private final String FLAG = "1";
    private ConstraintLayout layoutDialog;
    private ActivityResultLauncher<Intent> launcher;
    private ProgressBar progressBarInChooseCate;
    private boolean isConnected;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_outcome, container, false);

        rcvParentList = rootView.findViewById(R.id.rcvParentList);
        layoutDialog = rootView.findViewById(R.id.layoutDialog);
        layoutDialogInNotConnection = rootView.findViewById(R.id.layoutDialogInNotConnection);
        progressBarInChooseCate = rootView.findViewById(R.id.progressBarInChooseCate);

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = networkInfo != null && networkInfo.isConnected();

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK) {
                        int flagUD = result.getData().getIntExtra("flagUD", 0);
                        if (flagUD == 2) {
                            Alerter.create(getActivity())
                                    .setTitle("Xóa danh mục thành công!")
                                    .enableSwipeToDismiss()
                                    .setIcon(R.drawable.ic_baseline_check_circle_24)
                                    .setBackgroundColorRes(R.color.green)
                                    .setIconColorFilter(0)
                                    .setIconSize(R.dimen.icon_alert)
                                    .show();
                            fillDataToCategoryList();
                        }
                        else if(flagUD == 1){
                            Alerter.create(getActivity())
                                    .setTitle("Sửa danh mục thành công!")
                                    .enableSwipeToDismiss()
                                    .setIcon(R.drawable.ic_baseline_check_circle_24)
                                    .setBackgroundColorRes(R.color.green)
                                    .setIconColorFilter(0)
                                    .setIconSize(R.dimen.icon_alert)
                                    .show();
                            fillDataToCategoryList();
                        }

                    }
                }
        );

        fillDataToCategoryList();
        return rootView;
    }
    public void fillDataToCategoryList(){
        if(isConnected){
            progressBarInChooseCate.setVisibility(View.VISIBLE);
            Call<List<Category>> call = ApiService.getInstance(getContext()).getiApiService().getAllOutcomeCategories();
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
                rcvParentList.setHasFixedSize(true);
                rcvParentList.setAdapter(parentOutcomeCategoryAdapter);
                parentOutcomeCategoryAdapter.setRvItemClickListener(new RvItemClickListener<Category>() {
                    @Override
                    public void onChildItemClick(int parentPosition, int childPosition, Category item) {
                        if(item.getCategoryOf().equals("User")){
                            Intent intent = new Intent(getContext(), UpdateAndInsertCategory.class);
                            intent.putExtra("categoryItem", item);
                            intent.putExtra("isParent", 0);
                            intent.putExtra("categoryParent", categoryList.get(parentPosition));
                            intent.putExtra("fl", FLAG);
                            launcher.launch(intent);
                        }
                        else {
                            showAlertDialog();
                        }
                    }
                });
                progressBarInChooseCate.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable throwable) {
                showAlertNotConnection();
            }
        });
        }
        else {
            showAlertNotConnection();
        }
    }

    private void showAlertDialog(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.alert_dont_update_dialog, layoutDialog);
        Button btnOk = view.findViewById(R.id.alertBtn);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

    private void showAlertNotConnection() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.alert_no_connection, layoutDialogInNotConnection);
        Button btnOk = view.findViewById(R.id.alertBtnNotConnection);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
