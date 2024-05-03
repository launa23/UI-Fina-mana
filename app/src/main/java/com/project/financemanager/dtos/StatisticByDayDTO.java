package com.project.financemanager.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StatisticByDayDTO implements Serializable {
    @SerializedName("date")
    private String date;
    @SerializedName("totalOutcome")
    private String totalOutcome;
    @SerializedName("totalIncome")
    private String totalIncome;

    public StatisticByDayDTO(String date, String totalOutcome, String totalIncome) {
        this.date = date;
        this.totalOutcome = totalOutcome;
        this.totalIncome = totalIncome;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotalOutcome() {
        return totalOutcome;
    }

    public void setTotalOutcome(String totalOutcome) {
        this.totalOutcome = totalOutcome;
    }

    public String getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(String totalIncome) {
        this.totalIncome = totalIncome;
    }
}
