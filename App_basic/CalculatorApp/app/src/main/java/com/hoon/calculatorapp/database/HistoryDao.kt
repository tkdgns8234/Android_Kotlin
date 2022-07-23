package com.hoon.calculatorapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDao {
    @Query("SELECT * FROM History")
    fun getAll(): MutableList<History>

    @Insert
    fun insertHistory(history: History)

    @Query("DELETE FROM History")
    fun deleteAll()
}