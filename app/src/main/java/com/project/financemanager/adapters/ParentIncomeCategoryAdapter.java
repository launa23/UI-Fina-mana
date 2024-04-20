package com.project.financemanager.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.financemanager.R;
import com.project.financemanager.models.Category;

import java.util.List;

public class ParentIncomeCategoryAdapter extends RecyclerView.Adapter<ParentIncomeCategoryAdapter.ParentIncomeCategoryViewHolder>{
    private List<Category> parentCategoryList;
    private Activity activity;
    private HandleClickParentCategory handleClickParentCategory;
    private RvItemClickListener rvItemClickListener;
    public ParentIncomeCategoryAdapter(List<Category> parentCategoryList, Activity activity) {
        this.parentCategoryList = parentCategoryList;
        this.activity = activity;
    }
    public void setRvItemClickListener(RvItemClickListener rvItemClickListener){
        this.rvItemClickListener = rvItemClickListener;
    }

    public ParentIncomeCategoryAdapter(List<Category> parentCategoryList, Activity activity, HandleClickParentCategory handleClickParentCategory) {
        this.parentCategoryList = parentCategoryList;
        this.activity = activity;
        this.handleClickParentCategory = handleClickParentCategory;
    }

    @NonNull
    @Override
    public ParentIncomeCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.income_category_item, parent, false);
        return new ParentIncomeCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentIncomeCategoryViewHolder holder, int position) {
        Category category = parentCategoryList.get(position);
        int resourceId = activity.getResources().getIdentifier(category.getIcon(), "drawable", activity.getPackageName());
        holder.imgParentCategoryInIncome.setImageResource(resourceId);
        holder.txtNameParentCategoryInIncome.setText(category.getName());
        ChildIncomeCategoryAdapter childIncomeCategoryAdapter = new ChildIncomeCategoryAdapter(category.getCategoryChilds(), activity, new ChildIncomeCategoryAdapter.HandleClickChildCategory() {
            @Override
            public void onItemClick(int childPosition) {
                Category childCategory = category.getCategoryChilds().get(childPosition);
                rvItemClickListener.onChildItemClick(childPosition, childPosition, childCategory);
            }
        });
        holder.rcvChildCategoryList.setLayoutManager(new LinearLayoutManager(activity));
        holder.rcvChildCategoryList.setAdapter(childIncomeCategoryAdapter);
    }

    @Override
    public int getItemCount() {
        return parentCategoryList.size();
    }

    class ParentIncomeCategoryViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgParentCategoryInIncome;
        private TextView txtNameParentCategoryInIncome;
        private RecyclerView rcvChildCategoryList;
        private RelativeLayout relativeInIncome;
        public ParentIncomeCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgParentCategoryInIncome = itemView.findViewById(R.id.imgParentCategoryInIncome);
            txtNameParentCategoryInIncome = itemView.findViewById(R.id.txtNameParentCategoryInIncome);
            rcvChildCategoryList = itemView.findViewById(R.id.rcvChildCategoryList);
            relativeInIncome = itemView.findViewById(R.id.relativeInIncome);
            relativeInIncome.setOnClickListener(v -> {
                handleClickParentCategory.onItemClick(getAdapterPosition());
            });

        }
    }
    public interface HandleClickParentCategory{
        void onItemClick(int position);
    }
}
