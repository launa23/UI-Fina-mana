package com.project.financemanager.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.project.financemanager.R;
import com.project.financemanager.common.IClickListener;
import com.project.financemanager.models.Transaction;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private List<Transaction> transactions;
    private Context activity;
    private IClickListener clickListener;
    public SearchAdapter(List<Transaction> transactions, Context activity) {
        this.transactions = transactions;
        this.activity = activity;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        NumberFormat numberFormatComma = NumberFormat.getNumberInstance(Locale.getDefault());
        String formattedNumberComma = numberFormatComma.format(Integer.parseInt(transaction.getAmount()));
        if (transaction.getType().equals("Income")){
            holder.amountTransaction.setTextColor(Color.parseColor("#1BB639"));
            holder.moneyUnit.setTextColor(Color.parseColor("#1BB639"));
        }
        else{
            holder.amountTransaction.setTextColor(Color.RED);
            holder.moneyUnit.setTextColor(Color.RED);
        }
        if (!transaction.getDescription().isEmpty()){
            holder.descTransaction.setText(transaction.getDescription());
            holder.descTransaction.setVisibility(View.VISIBLE);
        }
        else {
            holder.descTransaction.setVisibility(View.GONE);

        }
        int resourceId = activity.getResources().getIdentifier(transaction.getImage(), "drawable", activity.getPackageName());
        holder.iconTransaction.setImageResource(resourceId);
        holder.categoryName.setText(transaction.getCategoryName());
        holder.amountTransaction.setText(formattedNumberComma);
        String inputFormat = "yyyy-MM-dd'T'HH:mm:ss";
        String outputFormat = "dd/MM/yyyy";

        SimpleDateFormat inputDateFormat = new SimpleDateFormat(inputFormat, Locale.getDefault());
        SimpleDateFormat outputDateFormat = new SimpleDateFormat(outputFormat, Locale.getDefault());
        Date date = null;
        try {
            date = inputDateFormat.parse(transaction.getTime());
            String outputDate = outputDateFormat.format(date);
            holder.timeTransaction.setText(outputDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    public void setClickListener(IClickListener clickListener) {
        this.clickListener = clickListener;
        notifyDataSetChanged();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder{
        private ImageView iconTransaction;
        private TextView categoryName;
        private TextView descTransaction;
        private TextView amountTransaction;
        private TextView timeTransaction;
        private TextView moneyUnit;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            iconTransaction = itemView.findViewById(R.id.cateChildImageInSearch);
            categoryName = itemView.findViewById(R.id.cateChildNameInSearch);
            descTransaction = itemView.findViewById(R.id.cateChildDescInSearch);
            amountTransaction = itemView.findViewById(R.id.cateChildAmountInSearch);
            timeTransaction = itemView.findViewById(R.id.dateInSearchView);
            moneyUnit = itemView.findViewById(R.id.moneyUnitInSearch);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClickListener(getAdapterPosition());
                }
            });
        }
    }
}
