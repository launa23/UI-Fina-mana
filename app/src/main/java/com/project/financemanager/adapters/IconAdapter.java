package com.project.financemanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.financemanager.R;

import java.util.List;

public class IconAdapter extends RecyclerView.Adapter<IconAdapter.IconViewHolder>{
    private List<Integer> imageResourceIds;
    private HandleClickIcon handleClickIcon;
    public IconAdapter(List<Integer> imageResourceIds) {
        this.imageResourceIds = imageResourceIds;
    }

    public IconAdapter(List<Integer> imageResourceIds, HandleClickIcon handleClickIcon) {
        this.imageResourceIds = imageResourceIds;
        this.handleClickIcon = handleClickIcon;
    }

    @NonNull
    @Override
    public IconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_item, parent, false);
        return new IconViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IconViewHolder holder, int position) {
        int resourceId = imageResourceIds.get(position);
        holder.iconImage.setImageResource(resourceId);
    }

    @Override
    public int getItemCount() {
        return imageResourceIds.size();
    }

    class IconViewHolder extends RecyclerView.ViewHolder{
        private ImageView iconImage;
        public IconViewHolder(@NonNull View itemView) {
            super(itemView);
            iconImage = itemView.findViewById(R.id.imageViewIcon);
            itemView.setOnClickListener(v -> {
                handleClickIcon.onItemClick(getAdapterPosition());
            });
        }
    }
    public interface HandleClickIcon{
        void onItemClick(int position);
    }
}
