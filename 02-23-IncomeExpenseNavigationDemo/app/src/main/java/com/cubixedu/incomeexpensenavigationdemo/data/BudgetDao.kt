package com.cubixedu.incomeexpensenavigationdemo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budgetdata")
    fun getAllBudget(): List<BudgetData>

    @Query("SELECT * FROM budgetdata WHERE amount > 0")
    fun getIncomeList(): List<BudgetData>

    @Query("SELECT * FROM budgetdata WHERE amount < 0")
    fun getExpenseList(): List<BudgetData>

    @Query("SELECT SUM(amount) as sumAmount FROM budgetdata WHERE amount > 0")
    fun getIncomeSum(): Int

    @Query("SELECT SUM(amount) as sumAmount FROM budgetdata WHERE amount < 0")
    fun getExpenseSum(): Int

    @Insert
    fun insertBudget(vararg budgetData: BudgetData)

    @Delete
    fun deleteBudget(budgetData: BudgetData)
}