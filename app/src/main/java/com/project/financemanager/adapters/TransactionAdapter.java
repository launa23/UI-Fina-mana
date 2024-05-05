package com.project.financemanager.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.financemanager.R;
import com.project.financemanager.models.Transaction;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>{

    final private List<Transaction> transactionList;
    private Activity activity;
    private HandleClick handleClick;
    public TransactionAdapter(List<Transaction> transactionList, Activity activity, HandleClick handleClick) {
        this.transactionList = transactionList;
        this.activity = activity;
        this.handleClick = handleClick;
    }

    public void setHandleClick(HandleClick handleClick){
        this.handleClick = handleClick;
    }
    public TransactionAdapter(List<Transaction> transactionList, Activity activity) {
        this.transactionList = transactionList;
        this.activity = activity;
    }
    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_home, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        NumberFormat numberFormatComma = NumberFormat.getNumberInstance(Locale.getDefault());
        String formattedNumberComma = numberFormatComma.format(Integer.parseInt(transaction.getAmount()));
        if (transaction.getType().equals("Income")){
            holder.txtAmount.setTextColor(Color.parseColor("#1BB639"));
            holder.moneyUnit.setTextColor(Color.parseColor("#1BB639"));
        }
        else{
            holder.txtAmount.setTextColor(Color.RED);
            holder.moneyUnit.setTextColor(Color.RED);

        }
        if (!transaction.getDescription().isEmpty()){
            holder.cateChildDesc.setText(transaction.getDescription());
            holder.cateChildDesc.setVisibility(View.VISIBLE);
        }
        int resourceId = activity.getResources().getIdentifier(transaction.getImage(), "drawable", activity.getPackageName());
        holder.imageView.setImageResource(resourceId);
        holder.txtCategoryName.setText(transaction.getCategoryName());
        holder.txtAmount.setText(formattedNumberComma);
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    class TransactionViewHolder extends RecyclerView.ViewHolder{

        final private ImageView imageView;
        final private TextView txtCategoryName;
        final private TextView txtAmount;
        final private TextView moneyUnit;
        private TextView cateChildDesc;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cateChildImage);
            txtCategoryName = itemView.findViewById(R.id.cateChildName);
            txtAmount = itemView.findViewById(R.id.cateChildAmount);
            moneyUnit = itemView.findViewById(R.id.moneyUnit);
            cateChildDesc = itemView.findViewById(R.id.cateChildDesc);
            itemView.setOnClickListener(v -> {
                handleClick.onItemClick(getAdapterPosition());
            });
        }
    }

    public interface HandleClick{
        void onItemClick(int position);
    }
}
