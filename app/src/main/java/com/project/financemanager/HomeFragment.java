package com.project.financemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.financemanager.adapters.RvItemClickListener;
import com.project.financemanager.adapters.TitleAdapter;
import com.project.financemanager.api.ApiService;
import com.project.financemanager.dtos.TitleTime;
import com.project.financemanager.dtos.Total;
import com.project.financemanager.models.Transaction;
import com.project.financemanager.models.Wallet;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    private TextView txtWalletName;
    private TextView txtWalletMoney;
    private TextView txtWalletId;
    private TextView amountTotalIncome;
    private TextView amountTotalOutcome;
    private TextView amountTotal;
    private TextView txtChoosenMonth;
    private RelativeLayout chooseTime;
    private TextView idWallet;
    private RelativeLayout chooseWallet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        chooseTime = rootView.findViewById(R.id.choosenMonth);
        txtChoosenMonth = rootView.findViewById(R.id.txtChoosenMonth);
        chooseWallet = rootView.findViewById(R.id.chooseWallet);

        loadDataWallet(rootView);

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK) {
                        Intent data = result.getData();
                        Bundle bundle = data.getExtras();

                        List<TitleTime> objectList = (List<TitleTime>) bundle.getSerializable("objectList");
                        loadRecyclerView(rootView, objectList);
                        String selectedMonthYear = data.getStringExtra("month");
                        txtChoosenMonth.setText(selectedMonthYear);

                        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                        long idWallet = sharedPreferences.getLong("idWallet", 0);
                        int month = sharedPreferences.getInt("month", 0);
                        int year = sharedPreferences.getInt("year", 0);

                        loadDateTotal(rootView, idWallet, month, year);
                    }
                }
        );

        ActivityResultLauncher<Intent> launcherWallet = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK) {
                        Intent data = result.getData();
                        String nameWallet = data.getStringExtra("nameWallet");
                        long idWallet1 = data.getIntExtra("idWallet", 0);
                        txtWalletName.setText(nameWallet);
                        txtWalletId.setText(String.valueOf(idWallet1));
                        loadDataWallet(rootView);

                    }
                }
        );

        // Bắt sự kiện click chọn thời gian
        chooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                long data = Long.parseLong(txtWalletId.getText().toString());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("idWallet", data);
                editor.apply();

                Intent intent = new Intent(v.getContext(), ChooseTimeActivity.class);
                launcher.launch(intent);

            }
        });

        // Bắt sự kiện click chọn ví
        chooseWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), WalletActivity.class);
                launcherWallet.launch(intent);
            }
        });

        return rootView;
    }

    private void loadDataWallet(View rootView){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        long idWallet = sharedPreferences.getLong("idWallet", 0);
        int month = sharedPreferences.getInt("month", 0);
        int year = sharedPreferences.getInt("year", 0);
        String titleMonthAndYear = sharedPreferences.getString("titleMonthAndYear", "Toàn bộ thời gian");
        txtChoosenMonth.setText(titleMonthAndYear);
        if (idWallet != 0){
            ApiService.apiService.getWalletById(idWallet).enqueue(new Callback<Wallet>() {
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
                    loadDataTransaction(rootView, walletId, month, year);
                    loadDateTotal(rootView, walletId, month, year);

                }
                @Override
                public void onFailure(Call<Wallet> call, Throwable throwable) {

                }
            });
        }
        else {
            ApiService.apiService.getFirstWallet().enqueue(new Callback<Wallet>() {
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
                    loadDataTransaction(rootView, walletId, month, year);
                    loadDateTotal(rootView, walletId, month, year);

                }
                @Override
                public void onFailure(Call<Wallet> call, Throwable throwable) {

                }
            });
        }
    }

    private void loadDataTransaction(View rootView, long walletId, int month, int year){
        ApiService.apiService.getTransByMonthAndYear(month, year, walletId).enqueue(new Callback<List<TitleTime>>() {
            @Override
            public void onResponse(Call<List<TitleTime>> call, Response<List<TitleTime>> response) {
                List<TitleTime> titleTimeList = response.body();
                loadRecyclerView(rootView, titleTimeList);

            }

            @Override
            public void onFailure(Call<List<TitleTime>> call, Throwable throwable) {
                Toast.makeText(getActivity(), "Thất bại", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void loadDateTotal(View rootView, long walletId, int month, int year){
        ApiService.apiService.getTotalIncomeAndOutcome(month, year, walletId).enqueue(new Callback<Total>() {
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

    private void loadRecyclerView(View rootView, List<TitleTime> titleTimeList){
        recyclerView = rootView.findViewById(R.id.rcvTransactions);
        TitleAdapter titleAdapter = new TitleAdapter(titleTimeList, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(titleAdapter);
        titleAdapter.setRvItemClickListener(new RvItemClickListener<Transaction>() {
            @Override
            public void onChildItemClick(int parentPosition, int childPosition, Transaction item) {
                Toast.makeText(getActivity().getApplicationContext(), item.getCategoryName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(rootView.getContext(), InsertAndUpdateTransaction.class);
                intent.putExtra("transaction", item);
                startActivity(intent);
            }
        });
    }
}