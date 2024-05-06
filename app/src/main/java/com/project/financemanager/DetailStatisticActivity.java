package com.project.financemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.project.financemanager.adapters.DetailStatisticAdapter;
import com.project.financemanager.dtos.StatisticByCategoryDTO;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DetailStatisticActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView titleInDetail;
    private PieChart pieChartInDetail;
    private TextView labelTotal;
    private TextView valueTotal;
    private ImageView btnBackInStatistic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_statistic);
        recyclerView = findViewById(R.id.rcvDetail);
        titleInDetail = findViewById(R.id.titleInDetail);
        pieChartInDetail = findViewById(R.id.pieChartInDetail);
        labelTotal = findViewById(R.id.labelTotal);
        valueTotal = findViewById(R.id.valueTotal);
        btnBackInStatistic = findViewById(R.id.btnBackInStatistic);

        List<StatisticByCategoryDTO> myList = (List<StatisticByCategoryDTO>) getIntent().getSerializableExtra("detailList");
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        String type = getIntent().getStringExtra("type");
        titleInDetail.setText(type);

        int[] colorsI = new int[]{
                Color.parseColor("#9E05FC"),
                Color.parseColor("#FCBA04"),
                Color.parseColor("#FF6666"),
                Color.parseColor("#FC8905"),
                Color.parseColor("#C80000"),
                Color.parseColor("#771F30"),
                Color.parseColor("#8954A5"),
                Color.parseColor("#FF3636"),
                Color.parseColor("#9030EA"),
                Color.parseColor("#02b835"),
                Color.parseColor("#0A6847"),
                Color.parseColor("#6962AD"),
                Color.parseColor("#378CE7"),
                Color.parseColor("#8DECB4"),
                Color.parseColor("#87A922"),
                Color.parseColor("#8954A5"),
                Color.parseColor("#FF3636"),
                Color.parseColor("#9030EA")
        };
        int[] colorsO = new int[]{
                Color.parseColor("#02b835"),
                Color.parseColor("#0A6847"),
                Color.parseColor("#6962AD"),
                Color.parseColor("#378CE7"),
                Color.parseColor("#8DECB4"),
                Color.parseColor("#87A922"),
                Color.parseColor("#8954A5"),
                Color.parseColor("#FF3636"),
                Color.parseColor("#9030EA"),
                Color.parseColor("#9E05FC"),
                Color.parseColor("#FCBA04"),
                Color.parseColor("#FF6666"),
                Color.parseColor("#FC8905"),
                Color.parseColor("#C80000"),
                Color.parseColor("#771F30"),
                Color.parseColor("#8954A5"),
                Color.parseColor("#FF3636"),
                Color.parseColor("#9030EA")
        };


        // Biểu đồ

        int totalOutcome = 0;
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (StatisticByCategoryDTO statistic : myList) {
            entries.add(new PieEntry(Float.parseFloat(statistic.getTotal()), statistic.getName()));
            totalOutcome += Integer.parseInt(statistic.getTotal());
        }
        PieDataSet dataSet = new PieDataSet(entries, null);
        if(type.equals("Chi tiêu")){
            dataSet.setColors(colorsI);
            valueTotal.setTextColor(Color.parseColor("#d43939"));
            labelTotal.setText("TỔNG CHI");
        }
        else {
            dataSet.setColors(colorsO);
            valueTotal.setTextColor(Color.parseColor("#02b835"));
            labelTotal.setText("TỔNG THU");
        }
        dataSet.setValueTextSize(0f);
        dataSet.setSliceSpace(1f);
        Legend legend = pieChartInDetail.getLegend();
        legend.setEnabled(false);
        pieChartInDetail.getDescription().setEnabled(false);
        pieChartInDetail.setTransparentCircleAlpha(0);
        pieChartInDetail.setHoleColor(Color.TRANSPARENT);

        valueTotal.setText(numberFormat.format((totalOutcome)) + " đ");
        pieChartInDetail.setHoleRadius(40f);
        PieData data = new PieData(dataSet);
        pieChartInDetail.setTransparentCircleAlpha(0);
        pieChartInDetail.getDescription().setEnabled(false);
        pieChartInDetail.setData(data);
        pieChartInDetail.setEntryLabelTextSize(8f);
        pieChartInDetail.invalidate();

        // Kết thúc biểu đồ


        DetailStatisticAdapter adapter = null;
        if (type.equals("Chi tiêu")){
            adapter = new DetailStatisticAdapter(myList, this, colorsI);
        }
       else {
            adapter = new DetailStatisticAdapter(myList, this, colorsO);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

        btnBackInStatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}