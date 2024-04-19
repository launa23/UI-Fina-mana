package com.project.financemanager.dtos;

import com.google.gson.annotations.SerializedName;

public class Total {
    @SerializedName("Income")
    private String income;
    @SerializedName("Outcome")
    private String outcome;
    @SerializedName("Total")
    private String total;

    public Total(String income, String outcome, String total) {
        this.income = income;
        this.outcome = outcome;
        this.total = total;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
