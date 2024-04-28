package com.project.financemanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.financemanager.api.ApiService;
import com.project.financemanager.common.RvItemClickListener;
import com.project.financemanager.adapters.TitleAdapter;
import com.project.financemanager.api.IApiService;
import com.project.financemanager.dtos.TitleTime;
import com.project.financemanager.dtos.Total;
import com.project.financemanager.models.Transaction;
import com.project.financemanager.models.Wallet;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    AlertDialog alertDialog;
    private ConstraintLayout layoutDialogLoading;
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
    //tus
    private ActivityResultLauncher<Intent> launcherforEdit;
    private List<TitleTime> titleTimeList;
    private TitleAdapter titleAdapter;

    //sut
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        chooseTime = rootView.findViewById(R.id.choosenMonth);
        txtChoosenMonth = rootView.findViewById(R.id.txtChoosenMonth);
        chooseWallet = rootView.findViewById(R.id.chooseWallet);
        layoutDialogLoading = rootView.findViewById(R.id.layoutDialogLoading);
        //tus
        initResultLauncher(rootView);
        //sut
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

    private void loadDataWallet(View rootView) {
        alertDialog = showAlertDialog(alertDialog);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        long idWallet = sharedPreferences.getLong("idWallet", 0);
        int month = sharedPreferences.getInt("month", 0);
        int year = sharedPreferences.getInt("year", 0);
        String titleMonthAndYear = sharedPreferences.getString("titleMonthAndYear", "Toàn bộ thời gian");
        txtChoosenMonth.setText(titleMonthAndYear);
        if (idWallet != 0) {
            Call<Wallet> call = ApiService.getInstance(getContext()).getiApiService().getWalletById(idWallet);

            call.enqueue(new Callback<Wallet>() {
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
                    dismissAlertDialog(alertDialog);

                }

                @Override
                public void onFailure(Call<Wallet> call, Throwable throwable) {

                }
            });
        } else {
            Call<Wallet> call = ApiService.getInstance(getActivity()).getiApiService().getFirstWallet();
            call.enqueue(new Callback<Wallet>() {
                @Override
                public void onResponse(Call<Wallet> call, Response<Wallet> response) {
                    if (response.isSuccessful()) {
                        Wallet wallet = response.body();
                        NumberFormat numberFormatComma = NumberFormat.getNumberInstance(Locale.getDefault());
                        String formattedNumberComma = numberFormatComma.format(Integer.parseInt(wallet.getMoney()));
                        txtWalletName = rootView.findViewById(R.id.txtWalletName);
                        txtWalletMoney = rootView.findViewById(R.id.moneyInWallet);
                        txtWalletId = rootView.findViewById(R.id.txtWalletId);
                        txtWalletMoney.setText(formattedNumberComma);
                        txtWalletName.setText(wallet.getName());
                        txtWalletId.setText(String.valueOf(wallet.getId()));

                        long walletId = Long.parseLong(txtWalletId.getText().toString());
                        loadDataTransaction(rootView, walletId, month, year);
                        loadDateTotal(rootView, walletId, month, year);
                        dismissAlertDialog(alertDialog);
                    } else {
                        Toast.makeText(getActivity(), "Lỗi", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<Wallet> call, Throwable throwable) {

                }
            });
        }
    }

    private void loadDataTransaction(View rootView, long walletId, int month, int year) {
        Call<List<TitleTime>> call = ApiService.getInstance(getContext()).getiApiService().getTransByMonthAndYear(month, year, walletId);
        call.enqueue(new Callback<List<TitleTime>>() {
            @Override
            public void onResponse(Call<List<TitleTime>> call, Response<List<TitleTime>> response) {
                titleTimeList = response.body();
                loadRecyclerView(rootView, titleTimeList);

            }

            @Override
            public void onFailure(Call<List<TitleTime>> call, Throwable throwable) {
                Toast.makeText(getActivity(), "Thất bại", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void loadDateTotal(View rootView, long walletId, int month, int year) {
        Call<Total> call = ApiService.getInstance(getContext()).getiApiService().getTotalIncomeAndOutcome(month, year, walletId);

        call.enqueue(new Callback<Total>() {
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

    private void loadRecyclerView(View rootView, List<TitleTime> titleTimeList) {
        recyclerView = rootView.findViewById(R.id.rcvTransactions);
        titleAdapter = new TitleAdapter(titleTimeList, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(titleAdapter);
        titleAdapter.setRvItemClickListener(new RvItemClickListener<Transaction>() {
            @Override
            public void onChildItemClick(int parentPosition, int childPosition, Transaction item) {
                Toast.makeText(getActivity().getApplicationContext(), item.getCategoryName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), InsertAndUpdateTransaction.class);
                intent.putExtra("transaction", item);
                //tus
                intent.putExtra("flag", "1");
                launcherforEdit.launch(intent);
                //sut
            }
        });

    }

    private AlertDialog showAlertDialog(AlertDialog alertDialog) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.loading_progress_bar, layoutDialogLoading);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        builder.setCancelable(false);
        alertDialog = builder.create();

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
        return alertDialog;
    }

    private void dismissAlertDialog(AlertDialog alertDialog) {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        }, 1000);
    }

    //tus
    private void initResultLauncher(View rootView) {
        try {
            launcherforEdit = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                //edit data
                if (result != null && result.getResultCode() == Activity.RESULT_OK) {
                    int flagUD = result.getData().getIntExtra("flagUD", 0);
                    if (flagUD == 2 || flagUD == 1) {
                        loadDataWallet(rootView);
                    }
                } else {
                    Log.e("editTransaction", "failed");
                }
            });

        } catch (Exception ex) {
            Log.e("initResultLauncher", Objects.requireNonNull(ex.getMessage()));
        }
    }
    //sut
}