package com.felix.bankingsystem.model;

public class Transaction {
    private String transactionID;
    private String date;
    private String type;
    private Double amount;
    private Double balanceAfter;

    public Transaction(String s, String deposit, double amount, double v) {}

    public Transaction(String transactionId, String date, String type, double amount, double balanceAfter) {
        this.transactionID = transactionId;
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(Double balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %.2f | Balance After: %.2f | Date: %s",
                transactionID, type, amount, balanceAfter, date.toString());
    }
}
