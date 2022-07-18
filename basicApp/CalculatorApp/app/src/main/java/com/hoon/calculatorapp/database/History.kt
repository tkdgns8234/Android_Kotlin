package com.hoon.calculatorapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class History(
    @PrimaryKey(autoGenerate = true) val uid: Int?,
    @ColumnInfo val expression: String?,
    @ColumnInfo val result: String?
)