package com.project.financemanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.project.financemanager.adapters.TitleAdapter;
import com.project.financemanager.api.ApiService;
import com.project.financemanager.models.TitleTime;
import com.project.financemanager.models.Total;
import com.project.financemanager.models.Transaction;
import com.project.financemanager.models.Wallet;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView txtWalletName;
    private TextView txtWalletMoney;
    private TextView txtWalletId;
    private TextView amountTotalIncome;
    private TextView amountTotalOutcome;
    private TextView amountTotal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        ApiService.apiService.getWalletById().enqueue(new Callback<Wallet>() {
            @Override
            public void onResponse(Call<Wallet> call, Response<Wallet> response) {
                Wallet wallet = response.body();
                NumberFormat numberFormatComma = NumberFormat.getNumberInstance(Locale.getDefault());
                String formattedNumberComma = numberFormatComma.format(Integer.parseInt(wallet.getMoney()));
                txtWalletName = rootView.findViewById(R.id.txtWalletName);
                txtWalletMoney = rootView.findViewById(R.id.moneyInWallet);
                txtWalletId = rootView.findViewById(R.id.txtWalletId);
                txtWalletMoney.setText(formattedNumberComma);
                txtWalletName.setText(wallet.getName());
                txtWalletId.setText(String.valueOf(wallet.getId()));

                // Chưa xử lý bất đồng bộ nên hơi cùi
                long walletId = Long.parseLong(txtWalletId.getText().toString());
                loadDataTransaction(rootView, walletId);
                loadDateTotal(rootView, walletId);

            }
            @Override
            public void onFailure(Call<Wallet> call, Throwable throwable) {

            }
        });
        return rootView;
    }

    private void loadDataTransaction(View rootView, long walletId){
        ApiService.apiService.getTransByMonthAndYear(9, 2014, walletId).enqueue(new Callback<List<TitleTime>>() {
            @Override
            public void onResponse(Call<List<TitleTime>> call, Response<List<TitleTime>> response) {
                List<TitleTime> titleTimeList = response.body();
                recyclerView = rootView.findViewById(R.id.rcvTransactions);

                TitleAdapter titleAdapter = new TitleAdapter(titleTimeList, getActivity());
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setAdapter(titleAdapter);
            }

            @Override
            public void onFailure(Call<List<TitleTime>> call, Throwable throwable) {
                Toast.makeText(getActivity(), "Thất bại", Toast.LENGTH_SHORT);
            }
        });
    }

    private void loadDateTotal(View rootView, long walletId){
        ApiService.apiService.getTotalIncomeAndOutcome(9, 2014, walletId).enqueue(new Callback<Total>() {
            @Override
            public void onResponse(Call<Total> call, Response<Total> response) {
                Total total = response.body();
                NumberFormat numberFormatComma = NumberFormat.getNumberInstance(Locale.getDefault());
                String formattedIncome = numberFormatComma.format(Integer.parseInt(total.getIncome()));
                String formattedOutcome = numberFormatComma.format(Integer.parseInt(total.getOutcome()));
                String formattedTotal = numberFormatComma.format(Integer.parseInt(total.getTotal()));
                amountTotalIncome = rootView.findViewById(R.id.amountTotalIncome);
                amountTotalOutcome = rootView.findViewById(R.id.amountTotalOutcome);
                amountTotal = rootView.findViewById(R.id.amountTotal);
                amountTotalIncome.setText(formattedIncome);
                amountTotalOutcome.setText(formattedOutcome);
                amountTotal.setText(formattedTotal);
            }

            @Override
            public void onFailure(Call<Total> call, Throwable throwable) {

            }
        });
    }
}