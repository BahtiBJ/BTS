package com.bb.bts.db;



import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.bb.bts.models.DBBalance;

@Database(entities = {DBBalance.class},version = 1)
public abstract class BalanceDatabase extends RoomDatabase {

    public abstract BalanceDAO getDAO();

}
