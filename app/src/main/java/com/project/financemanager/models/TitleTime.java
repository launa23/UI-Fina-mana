package com.project.financemanager.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TitleTime implements Serializable{
    @SerializedName("time")
    private String time;
    @SerializedName("transactionResponseList")
    private List<Transaction> transactionList;

    public TitleTime(String time, List<Transaction> transactionList) {
        this.time = time;
        this.transactionList = transactionList;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }
}
