package com.hoon.calculatorapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [History::class], version = 1)
abstract class HistoryDataBase : RoomDatabase(){
    abstract fun historyDao(): HistoryDao
}