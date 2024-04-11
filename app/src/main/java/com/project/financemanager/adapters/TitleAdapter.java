package com.project.financemanager.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.financemanager.R;
import com.project.financemanager.models.TitleTime;

import java.util.List;

public class TitleAdapter extends RecyclerView.Adapter<TitleAdapter.TitleViewHolder>{

    final private List<TitleTime> titleTimeList;
    final private Activity activity;

    public TitleAdapter(List<TitleTime> titleTimeList, Activity activity) {
        this.titleTimeList = titleTimeList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public TitleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_time_title, parent, false);
        return new TitleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TitleViewHolder holder, int position) {
        TitleTime titleTime = titleTimeList.get(position);
        holder.txtDateTitle.setText(titleTime.getDate());
        holder.txtDayTitle.setText(titleTime.getDay());
        holder.txtMonthAndYear.setText(titleTime.getMonthAndYear());

        TransactionAdapter transactionAdapter = new TransactionAdapter(titleTime.getTransactionList());
        holder.rcvTransactions.setLayoutManager(new LinearLayoutManager(activity));
        holder.rcvTransactions.setAdapter(transactionAdapter);

    }

    @Override
    public int getItemCount() {
        return titleTimeList.size();
    }

    class TitleViewHolder extends RecyclerView.ViewHolder{

        final private TextView txtDateTitle;
        final private TextView txtDayTitle;
        final private TextView txtMonthAndYear;
        final private RecyclerView rcvTransactions;
        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDateTitle = itemView.findViewById(R.id.dateOftitle);
            txtDayTitle = itemView.findViewById(R.id.dayOfTitle);
            txtMonthAndYear = itemView.findViewById(R.id.monthAndYear);
            rcvTransactions = itemView.findViewById(R.id.rcv_children);
        }
    }
}
