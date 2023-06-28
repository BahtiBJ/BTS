package com.bb.bts.models;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DBBalance {

    @PrimaryKey
    public int id = 5;

    public int balance;
    public float btcBalance;

    public DBBalance(int balance, float btcBalance){
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
