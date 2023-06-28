package com.bb.bts.models;

public class UIBalance {

    public int balance;
    public float btcBalance;

    public UIBalance(int balance, float btcBalance){
        this.balance = balance;
        this.btcBalance = btcBalance;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public float getBtcBalance() {
        return btcBalance;
    }

    public void setBtcBalance(float btcBalance) {
        this.btcBalance = btcBalance;
    }
}
