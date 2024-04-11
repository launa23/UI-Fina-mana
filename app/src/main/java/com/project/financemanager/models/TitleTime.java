package com.project.financemanager.models;

import java.util.List;

public class TitleTime {
    private String date;
    private String day;
    private String monthAndYear;
    private List<Transaction> transactionList;

    public TitleTime(String date, String day, String monthAndYear, List<Transaction> transactionList) {
        this.date = date;
        this.day = day;
        this.monthAndYear = monthAndYear;
        this.transactionList = transactionList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonthAndYear() {
        return monthAndYear;
    }

    public void setMonthAndYear(String monthAndYear) {
        this.monthAndYear = monthAndYear;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }
}
