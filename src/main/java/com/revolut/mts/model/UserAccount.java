package com.revolut.mts.model;

/**
 * Holds the user accounts related data
 *
 * @author iasa0862 18/07/19
 */
public class UserAccount {

    private int accountNumber;
    private String accountHolder;
    private Double balance;

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
