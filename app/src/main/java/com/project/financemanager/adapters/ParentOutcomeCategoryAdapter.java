package com.project.financemanager.adapters;

import android.app.Activity;
import android.content.Context;
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
import com.project.financemanager.common.RvItemClickListener;
import com.project.financemanager.models.Category;

import java.util.List;

public class ParentOutcomeCategoryAdapter extends RecyclerView.Adapter<ParentOutcomeCategoryAdapter.ParentOutcomeCategoryViewHolder>{
    private List<Category> parentCategoryList;
    private Activity activity;
    private HandleClickParentCategory handleClickParentCategory;
    private RvItemClickListener rvItemClickListener;
    public ParentOutcomeCategoryAdapter(List<Category> parentCategoryList, Activity activity) {
        this.parentCategoryList = parentCategoryList;
        this.activity = activity;
    }
    public void setRvItemClickListener(RvItemClickListener rvItemClickListener){
        this.rvItemClickListener = rvItemClickListener;
    }

    public ParentOutcomeCategoryAdapter(List<Category> parentCategoryList, Activity activity, HandleClickParentCategory handleClickParentCategory) {
        this.parentCategoryList = parentCategoryList;
        this.activity = activity;
        this.handleClickParentCategory = handleClickParentCategory;
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
        ChildOutcomeCategoryAdapter childOutcomeCategoryAdapter = new ChildOutcomeCategoryAdapter(category.getCategoryChilds(), activity, new ChildOutcomeCategoryAdapter.HandleClickChildCategory() {
            @Override
            public void onItemClick(int childPosition) {
                Category childCategory = category.getCategoryChilds().get(childPosition);
                rvItemClickListener.onChildItemClick(position, childPosition, childCategory);
            }
        });
        if (category.getCategoryOf().equals("User")){
            holder.iconLock.setVisibility(View.GONE);
        }
        holder.rcvChildCategoryList.setHasFixedSize(true);
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
        private RelativeLayout relativeInOutcome;
        private ImageView iconLock;
        public ParentOutcomeCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgParentCategoryInOutcome = itemView.findViewById(R.id.imgParentCategoryInOutcome);
            txtNameParentCategoryInOutcome = itemView.findViewById(R.id.txtNameParentCategoryInOutcome);
            rcvChildCategoryList = itemView.findViewById(R.id.rcvChildCategoryList);
            relativeInOutcome = itemView.findViewById(R.id.relativeInOutcome);
            iconLock = itemView.findViewById(R.id.iconLock4);
            relativeInOutcome.setOnClickListener(v -> {
                handleClickParentCategory.onItemClick(getAdapterPosition());
            });
        }
        public ImageView getIconLock(){
            return iconLock;
        }
        public void setIconLock(){
            iconLock.setVisibility(View.VISIBLE);
        }
    }


    public interface HandleClickParentCategory{
        void onItemClick(int position);
    }
}
