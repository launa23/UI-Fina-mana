package com.project.financemanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.financemanager.adapters.TitleAdapter;
import com.project.financemanager.models.TitleTime;
import com.project.financemanager.models.Transaction;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        List<Transaction> transactionList = new ArrayList<>();
        List<Transaction> transactionList1 = new ArrayList<>();
        List<Transaction> transactionList2 = new ArrayList<>();

        transactionList.add(new Transaction("Ăn uống", "120.000", R.mipmap.ae62936a076ab59b78155a12ff9d9064));
        transactionList.add(new Transaction("Đi chợ", "340.000", R.mipmap.ae62936a076ab59b78155a12ff9d9064));
        transactionList.add(new Transaction("Bạn bè", "20.000", R.mipmap.ae62936a076ab59b78155a12ff9d9064));
        transactionList.add(new Transaction("Nhà cửa", "420.000", R.mipmap.ae62936a076ab59b78155a12ff9d9064));

        transactionList1.add(new Transaction("Ăn uống", "120.000", R.mipmap.food));
        transactionList1.add(new Transaction("Đi chợ", "340.000", R.mipmap.food));
        transactionList1.add(new Transaction("Bạn bè", "20.000", R.mipmap.food));
        transactionList1.add(new Transaction("Nhà cửa", "420.000", R.mipmap.food));

        transactionList2.add(new Transaction("Ăn uống", "120.000", R.mipmap.dontknow));
        transactionList2.add(new Transaction("Đi chợ", "340.000", R.mipmap.dontknow));
        transactionList2.add(new Transaction("Bạn bè", "20.000", R.mipmap.food));
        transactionList2.add(new Transaction("Nhà cửa", "420.000", R.mipmap.dontknow));

        List<TitleTime> titleTimeList = new ArrayList<>();

        titleTimeList.add(new TitleTime("09", "Thứ tư", "Tháng 7/2023", transactionList));
        titleTimeList.add(new TitleTime("19", "Thứ năm", "Tháng 2/2023", transactionList1));
        titleTimeList.add(new TitleTime("28", "Chủ nhật", "Tháng 10/2023", transactionList2));

        recyclerView = rootView.findViewById(R.id.rcvTransactions);

        TitleAdapter titleAdapter = new TitleAdapter(titleTimeList, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(titleAdapter);
        return rootView;
    }
}