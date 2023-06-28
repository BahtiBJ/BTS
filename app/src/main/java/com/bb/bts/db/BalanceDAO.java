package com.bb.bts.db;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.bb.bts.models.DBBalance;

@Dao
public interface BalanceDAO {

    @Query("SELECT * FROM DBBalance")
    DBBalance getBalance();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void update(DBBalance DBBalance);
}
