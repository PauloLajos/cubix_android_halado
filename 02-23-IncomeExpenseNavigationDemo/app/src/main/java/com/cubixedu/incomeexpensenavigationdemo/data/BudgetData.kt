package com.cubixedu.incomeexpensenavigationdemo.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "budgetdata")
data class BudgetData(
    @PrimaryKey(autoGenerate = true) var budgetId: Int?,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "amount") var amount: Int
)