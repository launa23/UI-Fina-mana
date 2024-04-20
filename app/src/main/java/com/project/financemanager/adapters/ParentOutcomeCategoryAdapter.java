package com.project.financemanager.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.financemanager.R;
import com.project.financemanager.models.Category;

import java.util.List;

public class ParentOutcomeCategoryAdapter extends RecyclerView.Adapter<ParentOutcomeCategoryAdapter.ParentOutcomeCategoryViewHolder>{
    private List<Category> parentCategoryList;
    private Activity activity;

    public ParentOutcomeCategoryAdapter(List<Category> parentCategoryList, Activity activity) {
        this.parentCategoryList = parentCategoryList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ParentOutcomeCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.outcome_category_item, parent, false);
        return new ParentOutcomeCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentOutcomeCategoryViewHolder holder, int position) {
        Category category = parentCategoryList.get(position);
        int resourceId = activity.getResources().getIdentifier(category.getIcon(), "drawable", activity.getPackageName());
        holder.imgParentCategoryInOutcome.setImageResource(resourceId);
        holder.txtNameParentCategoryInOutcome.setText(category.getName());
        ChildOutcomeCategoryAdapter childOutcomeCategoryAdapter = new ChildOutcomeCategoryAdapter(category.getCategoryChilds(), activity);
        holder.rcvChildCategoryList.setLayoutManager(new LinearLayoutManager(activity));
        holder.rcvChildCategoryList.setAdapter(childOutcomeCategoryAdapter);
    }

    @Override
    public int getItemCount() {
        return parentCategoryList.size();
    }

    class ParentOutcomeCategoryViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgParentCategoryInOutcome;
        private TextView txtNameParentCategoryInOutcome;
        private RecyclerView rcvChildCategoryList;
        public ParentOutcomeCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgParentCategoryInOutcome = itemView.findViewById(R.id.imgParentCategoryInOutcome);
            txtNameParentCategoryInOutcome = itemView.findViewById(R.id.txtNameParentCategoryInOutcome);
            rcvChildCategoryList = itemView.findViewById(R.id.rcvChildCategoryList);
        }
    }

}
