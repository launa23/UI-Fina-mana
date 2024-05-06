package com.project.financemanager.adapters;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.financemanager.R;
import com.project.financemanager.dtos.StatisticByCategoryDTO;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DetailStatisticAdapter extends RecyclerView.Adapter<DetailStatisticAdapter.DetailViewHolder>{
    private List<StatisticByCategoryDTO> statistics;
    private Activity activity;
    private int[] colors;

    public DetailStatisticAdapter(List<StatisticByCategoryDTO> statistics, Activity activity, int[] colors) {
        this.statistics = statistics;
        this.activity = activity;
        this.colors = colors;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_transaction_statistc, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        StatisticByCategoryDTO statistic = statistics.get(position);
        int resourceId = activity.getResources().getIdentifier(statistic.getIcon(), "drawable", activity.getPackageName());
        holder.imgCategoryInDetail.setImageResource(resourceId);
        holder.txtCategoryInDetail.setText(statistic.getName());
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        holder.txtAmountInDetail.setText(String.format("%s đ", numberFormat.format(Long.parseLong(statistic.getTotal()))));
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        double percent = (Double.parseDouble(statistic.getTotal()) / calculateTotal())*100;
        holder.txtPercentInDetail.setText("(" + decimalFormat.format(percent) + "%)");
        holder.progressInDetail.setProgress((int) Math.round(percent), true);
        holder.progressInDetail.setProgressTintList(ColorStateList.valueOf(colors[position]));
    }

    @Override
    public int getItemCount() {
        return statistics.size();
    }
    private long calculateTotal(){
        long total = 0;
        for (StatisticByCategoryDTO s : statistics) {
            total += Long.parseLong(s.getTotal());
        }
        return total;
    }
    class DetailViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgCategoryInDetail;
        private TextView txtCategoryInDetail;
        private TextView txtAmountInDetail;
        private TextView txtPercentInDetail;
        private ProgressBar progressInDetail;
        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCategoryInDetail = itemView.findViewById(R.id.imgCategoryInDetail);
            txtCategoryInDetail = itemView.findViewById(R.id.txtCategoryInDetail);
            txtAmountInDetail = itemView.findViewById(R.id.txtAmountInDetail);
            txtPercentInDetail = itemView.findViewById(R.id.txtPercentInDetail);
            progressInDetail = itemView.findViewById(R.id.progressInDetail);

        }
    }
}
