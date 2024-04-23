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

public class ChildOutcomeCategoryAdapter extends RecyclerView.Adapter<ChildOutcomeCategoryAdapter.ChildCategoryViewHolder>{

    private List<Category> childCategoryList;
    private Activity activity;
    private HandleClickChildCategory handleClickChildCategory;
    public ChildOutcomeCategoryAdapter(List<Category> childCategoryList, Activity activity) {
        this.childCategoryList = childCategoryList;
        this.activity = activity;
    }

    public ChildOutcomeCategoryAdapter(List<Category> childCategoryList, Activity activity, HandleClickChildCategory handleClickChildCategory) {
        this.childCategoryList = childCategoryList;
        this.activity = activity;
        this.handleClickChildCategory = handleClickChildCategory;
    }

    @NonNull
    @Override
    public ChildCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_outcome_category_item, parent, false);
        return new ChildCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildCategoryViewHolder holder, int position) {
        Category category = childCategoryList.get(position);

        holder.txtNameChildCategory.setText(category.getName());
        holder.txtChildCategoryOf.setText(category.getCategoryOf());
        int resourceId = activity.getResources().getIdentifier(category.getIcon(), "drawable", activity.getPackageName());
        holder.imgChildCategory.setImageResource(resourceId);
        if (category.getCategoryOf().equals("User")){
            holder.iconLock.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return childCategoryList.size();
    }

    public class ChildCategoryViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgChildCategory;
        private TextView txtNameChildCategory;
        private TextView txtChildCategoryOf;
        private ImageView iconLock;
        public ChildCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgChildCategory = itemView.findViewById(R.id.imgChildCategoryInOutcome);
            txtNameChildCategory = itemView.findViewById(R.id.txtNameChildCategoryInOutcome);
            txtChildCategoryOf = itemView.findViewById(R.id.txtChildCategoryOf);
            iconLock = itemView.findViewById(R.id.iconLock2);
            itemView.setOnClickListener(v -> {
                handleClickChildCategory.onItemClick(getAdapterPosition());
            });
        }
    }
    public interface HandleClickChildCategory{
        void onItemClick(int position);
    }
}
