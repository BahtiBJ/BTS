package com.bb.bts.db;


import android.content.Context;

import androidx.room.Room;

import com.bb.bts.models.DBBalance;
import com.bb.bts.models.UIBalance;


public class RepositoryBalance {

    private static RepositoryBalance INSTANCE;
    private static String contextName;

    private BalanceDAO balanceDAO;

    private RepositoryBalance(Context context) {
        balanceDAO = Room.databaseBuilder(context,
                BalanceDatabase.class, "database")
                .build()
                .getDAO();
    }

    public static RepositoryBalance getInstance(Context context){
        if (INSTANCE == null && !context.getClass().getName().equals(contextName)){
            INSTANCE = new RepositoryBalance(context);
        }
        return INSTANCE;
    }

    public UIBalance getBalance(){
        DBBalance dbBalance = balanceDAO.getBalance();
        if (dbBalance == null)
            return new UIBalance(0,0.00000000f);
        return new UIBalance(dbBalance.balance,dbBalance.btcBalance);
    }

    public void update(UIBalance uiBalance){
        balanceDAO.update(new DBBalance(uiBalance.balance, uiBalance.btcBalance));
    }
}
