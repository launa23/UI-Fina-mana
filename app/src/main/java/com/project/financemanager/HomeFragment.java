package com.project.financemanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.project.financemanager.api.ApiService;
import com.project.financemanager.common.RvItemClickListener;
import com.project.financemanager.adapters.TitleAdapter;
import com.project.financemanager.api.IApiService;
import com.project.financemanager.dtos.TitleTime;
import com.project.financemanager.dtos.Total;
import com.project.financemanager.models.Transaction;
import com.project.financemanager.models.Wallet;
import com.tapadoo.alerter.Alerter;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    private int totalIncome;
    private int totalOutcome;
    private int totaler;
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
    private BarChart barChart;
    private RelativeLayout rltEmpty;
    private ConstraintLayout layoutDialog;
    private boolean isConnected;
    private ImageView hideOrView;
    private TextView moneyUnit;
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
        rltEmpty = rootView.findViewById(R.id.rltEmpty);
        layoutDialog = rootView.findViewById(R.id.layoutDialogInNotConnection);
        hideOrView = rootView.findViewById(R.id.imgHideOrViewInHome);
        moneyUnit = rootView.findViewById(R.id.moneyUnitInHome);
        txtWalletName = rootView.findViewById(R.id.txtWalletName);
        txtWalletMoney = rootView.findViewById(R.id.moneyInWallet);
        txtWalletId = rootView.findViewById(R.id.txtWalletId);
        Animation blinkAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.blink_animation);
        //tus
        initResultLauncher(rootView);
        //sut
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = networkInfo != null && networkInfo.isConnected();

        hideOrView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moneyUnit.getVisibility() == View.VISIBLE){
                    hideOrView.setImageResource(R.mipmap.hide);
                    txtWalletMoney.setVisibility(View.GONE);
                    moneyUnit.setVisibility(View.GONE);
                }
                else {
                    hideOrView.setImageResource(R.mipmap.view);
                    txtWalletMoney.setVisibility(View.VISIBLE);
                    moneyUnit.setVisibility(View.VISIBLE);
                }
            }
        });

        if(isConnected){
            loadDataWallet(rootView);
        }
        else {
            showAlertNotConnection();
        }
        barChart = rootView.findViewById(R.id.barChartInHome);

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
                try {
                    if(isConnected){
                        chooseTime.startAnimation(blinkAnimation);
                        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                        long data = Long.parseLong(txtWalletId.getText().toString());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putLong("idWallet", data);
                        editor.apply();

                        Intent intent = new Intent(v.getContext(), ChooseTimeActivity.class);
                        launcher.launch(intent);
                    }
                    else {
                        showAlertNotConnection();
                    }
                }
                catch (Exception e){
                    Toast.makeText(getActivity(), "Vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                }

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
        Calendar calendar = Calendar.getInstance();

        long idWallet = sharedPreferences.getLong("idWallet", 0);
        int month = sharedPreferences.getInt("month", calendar.get(Calendar.MONTH)+1);
        int year = sharedPreferences.getInt("year", calendar.get(Calendar.YEAR));
        String titleMonthAndYear = sharedPreferences.getString("titleMonthAndYear", "Tháng này");
        txtChoosenMonth.setText(titleMonthAndYear);
        try {
            if (idWallet != 0) {
                Call<Wallet> call = ApiService.getInstance(getContext()).getiApiService().getWalletById(idWallet);

                call.enqueue(new Callback<Wallet>() {
                    @Override
                    public void onResponse(Call<Wallet> call, Response<Wallet> response) {
                        Wallet wallet = response.body();
                        NumberFormat numberFormatComma = NumberFormat.getNumberInstance(Locale.getDefault());
                        String formattedNumberComma = numberFormatComma.format(Integer.parseInt(wallet.getMoney()));

                        txtWalletMoney.setText(formattedNumberComma);
                        txtWalletName.setText(wallet.getName());
                        txtWalletId.setText(String.valueOf(wallet.getId()));

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
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadDataTransaction(View rootView, long walletId, int month, int year) {
        try {
            Call<List<TitleTime>> call = ApiService.getInstance(getContext()).getiApiService().getTransByMonthAndYear(month, year, walletId);
            call.enqueue(new Callback<List<TitleTime>>() {
                @Override
                public void onResponse(Call<List<TitleTime>> call, Response<List<TitleTime>> response) {
                    titleTimeList = response.body();
                    if (titleTimeList.isEmpty()){
                        rltEmpty.setVisibility(View.VISIBLE);
                    }
                    else{
                        rltEmpty.setVisibility(View.GONE);
                    }
                    loadRecyclerView(rootView, titleTimeList);
                }

                @Override
                public void onFailure(Call<List<TitleTime>> call, Throwable throwable) {
                    Toast.makeText(getActivity(), "Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();

        }
    }

    private void loadDateTotal(View rootView, long walletId, int month, int year) {
        Call<Total> call = ApiService.getInstance(getContext()).getiApiService().getTotalIncomeAndOutcome(month, year, walletId);

        call.enqueue(new Callback<Total>() {
            @Override
            public void onResponse(Call<Total> call, Response<Total> response) {
                Total total = response.body();
                NumberFormat numberFormatComma = NumberFormat.getNumberInstance(Locale.getDefault());
                totalIncome = Integer.parseInt(total.getIncome());
                totalOutcome = Integer.parseInt(total.getOutcome());
                totaler = Integer.parseInt(total.getTotal());
                String formattedIncome = numberFormatComma.format(Integer.parseInt(total.getIncome()));
                String formattedOutcome = numberFormatComma.format(Integer.parseInt(total.getOutcome()));
                String formattedTotal = numberFormatComma.format(Integer.parseInt(total.getTotal()));
                amountTotalIncome = rootView.findViewById(R.id.amountTotalIncome);
                amountTotalOutcome = rootView.findViewById(R.id.amountTotalOutcome);
                amountTotal = rootView.findViewById(R.id.amountTotal);
                amountTotalIncome.setText(formattedIncome);
                amountTotalOutcome.setText(formattedOutcome);
                amountTotal.setText(formattedTotal);
                // Biểu đồ
                barChart.getXAxis().setDrawGridLines(false);
                barChart.getAxisLeft().setDrawGridLines(false);
                barChart.getAxisRight().setDrawGridLines(false);
                barChart.setDoubleTapToZoomEnabled(false);
                barChart.getXAxis().setDrawAxisLine(false);
                barChart.getXAxis().setDrawLabels(false);
                barChart.getAxisRight().setEnabled(false);
                barChart.getAxisLeft().setEnabled(false);
                barChart.getAxisLeft().setAxisMinimum(0);
                barChart.getDescription().setEnabled(false);

                BarDataSet barDataSet1 = new BarDataSet(dataValue1(), null);
                barDataSet1.setColor(Color.parseColor("#02b835"));
                BarDataSet barDataSet2 = new BarDataSet(dataValue2(), null);
                barDataSet2.setColor(Color.parseColor("#d43939"));
                BarData barData = new BarData(barDataSet1, barDataSet2);
                barChart.setData(barData);

                String[] title = new String[]{"Chi tiêu", "Thu nhập"};
                XAxis xAxis = barChart.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(title));
                xAxis.setCenterAxisLabels(true);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setGranularity(1);
                xAxis.setGranularityEnabled(true);

                float groupSpace = 0.44f;
                float barSpace = 0.68f;
                float barWidth = 3.0f;

                xAxis.setAxisMinimum(0);
                xAxis.setAxisMaximum(0+barChart.getBarData().getGroupWidth(groupSpace, barSpace)*2);
                barData.setBarWidth(barWidth);


                barChart.groupBars(0, groupSpace, barSpace);
                barChart.invalidate();

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
    private void showAlertNotConnection() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.alert_no_connection, layoutDialog);
        Button btnOk = view.findViewById(R.id.alertBtnNotConnection);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
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
                    if (flagUD == 2) {
                        Alerter.create(getActivity())
                                .setTitle("Xóa giao dịch thành công!")
                                .enableSwipeToDismiss()
                                .setIcon(R.drawable.ic_baseline_check_circle_24)
                                .setBackgroundColorRes(R.color.green)
                                .setIconColorFilter(0)
                                .setIconSize(R.dimen.icon_alert)
                                .show();
                        loadDataWallet(rootView);
                    }
                    else if(flagUD == 1){
                        Alerter.create(getActivity())
                                .setTitle("Sửa giao dịch thành công!")
                                .enableSwipeToDismiss()
                                .setIcon(R.drawable.ic_baseline_check_circle_24)
                                .setBackgroundColorRes(R.color.green)
                                .setIconColorFilter(0)
                                .setIconSize(R.dimen.icon_alert)
                                .show();
                        loadDataWallet(rootView);
                    }
                }
                else {
                    Log.e("editTransaction", "failed");
                }
            });

        } catch (Exception ex) {
            Log.e("initResultLauncher", Objects.requireNonNull(ex.getMessage()));
        }
    }
    //sut

    private ArrayList<BarEntry> dataValue1(){
        ArrayList<BarEntry> data = new ArrayList<>();
        data.add(new BarEntry(1, totalIncome));
        return data;
    }
    private ArrayList<BarEntry> dataValue2(){
        ArrayList<BarEntry> data = new ArrayList<>();
        data.add(new BarEntry(1, totalOutcome));
        return data;
    }
}