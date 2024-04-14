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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TitleAdapter extends RecyclerView.Adapter<TitleAdapter.TitleViewHolder>{

    final private List<TitleTime> titleTimeList;
    final private Activity activity;
    private int day;
    private int month;
    private int year;
    private String dayOfWeekString;

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

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date = inputFormat.parse(titleTime.getTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH) + 1;
            year = calendar.get(Calendar.YEAR);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            dayOfWeekString = "";
            switch (dayOfWeek){
                case 1:
                    dayOfWeekString = "Chủ nhật";
                    break;
                case 2:
                    dayOfWeekString = "Thứ Hai";
                    break;
                case 3:
                    dayOfWeekString = "Thứ Ba";
                    break;
                case 4:
                    dayOfWeekString = "Thứ Tư";
                    break;
                case 5:
                    dayOfWeekString = "Thứ Năm";
                    break;
                case 6:
                    dayOfWeekString = "Thứ Sáu";
                    break;
                case 7:
                    dayOfWeekString = "Thứ Bảy";
                    break;
//                default:
//                    throw new IllegalStateException("Unexpected value: " + dayOfWeek);
            }
            holder.txtDateTitle.setText(String.valueOf(day));
            holder.txtDayTitle.setText(String.valueOf(dayOfWeekString));
            holder.txtMonthAndYear.setText(String.format("Tháng %d/%d", month, year));
//            if(titleTime.getTransactionList().isEmpty()){
//                holder.rcvTransactions.setVisibility(View.GONE);
//            }
            TransactionAdapter transactionAdapter = new TransactionAdapter(titleTime.getTransactionList(), activity);
            holder.rcvTransactions.setLayoutManager(new LinearLayoutManager(activity));
            holder.rcvTransactions.setAdapter(transactionAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }


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
