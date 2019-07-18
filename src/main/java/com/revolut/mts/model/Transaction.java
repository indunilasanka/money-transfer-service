package com.revolut.mts.model;

import static com.revolut.mts.common.Constants.StatusMessages.SUCCESS_STATUS;

/**
 * Holds the transactions related data
 *
 * @author iasa0862 18/07/19
 */
public class Transaction {

    private int transactionId;
    private String status = SUCCESS_STATUS;
    private int sender;
    private int receiver;
    private double amount;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
