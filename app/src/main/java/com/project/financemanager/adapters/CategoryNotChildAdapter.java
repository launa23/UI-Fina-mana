package com.project.financemanager.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.financemanager.R;
import com.project.financemanager.models.Category;

import java.util.List;

public class CategoryNotChildAdapter extends RecyclerView.Adapter<CategoryNotChildAdapter.CategoryViewHolder> {
    private List<Category> categories;
    private Activity activity;

    public CategoryNotChildAdapter(List<Category> categories, Activity activity) {
        this.categories = categories;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_outcome_category_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.txtNameChildCategoryInOutcome.setText(category.getName());
        int resourceId = activity.getResources().getIdentifier(category.getIcon(), "drawable", activity.getPackageName());
        holder.imgChildCategoryInOutcome.setImageResource(resourceId);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgChildCategoryInOutcome;
        private TextView txtNameChildCategoryInOutcome;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgChildCategoryInOutcome = itemView.findViewById(R.id.imgChildCategoryInOutcome);
            txtNameChildCategoryInOutcome = itemView.findViewById(R.id.txtNameChildCategoryInOutcome);
        }
    }
}
